import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Teacher implements Runnable {
	private int studentsLeft;
	private int nStudents;
	private boolean done = false;

	public Teacher(int nStudents){
		this.studentsLeft = nStudents;
		this.nStudents = nStudents;
	}

	public void run(){
		/* Creates a serverSocket to listen to incoming calls */
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8080);
		} 
		catch (IOException e) {e.printStackTrace();}
		System.out.println("Server is up!");

		/* Counter to keep track of the pairing and a counter to keep 
		 * track of all clients that have connected*/
		int countPair = 0, count = 0;
		/* A list of two clients is kept so the server can notify both 
			clients when a pairing session is over */
		Socket client[] = new Socket[2];
		String names[] = new String[2];
		/* A list of writers that lets the process write to other processes, 
		 * saves all writers to be able to send a end message that makes the students 
		 * print there partners */
		PrintWriter out[] = new PrintWriter[nStudents];
		/* A reader that lets the teacher read messages from students */
		BufferedReader in = null;

		while(!done){

			/* Loops until two clients have connected */
			while(countPair != 2){

				try {
					/* Accept the request from a client */
					client[countPair] = serverSocket.accept();
					System.out.println("Accepted client!");
					Thread.sleep(500);

					/* Creates a reader to be able to read messages from the student */
					in = new BufferedReader(new InputStreamReader(client[countPair].getInputStream()));
					/* Reads the message */
					names[countPair] = in.readLine();
					System.out.println("Reading message from: " + names[countPair]);

					/* If there are only one student left, assign it self to be its partner */
					if(studentsLeft == 1 && countPair == 0) {
						/* Creates a printer to be able to send messages to the student */
						out[count] = new PrintWriter(client[countPair].getOutputStream(), true);
						/* Sends the message */
						out[count].println(names[countPair]);

						studentsLeft = -1;
						break;
					}
					countPair++;
					count++;
					studentsLeft--;
				} 
				catch (IOException e) {e.printStackTrace();} 
				catch (InterruptedException e) {e.printStackTrace();}	

			}

			/* Resets the countPair counter after two students have connected */
			countPair = 0;
			/* If there are an even amount of students this will always run
			 * Will not run when a student gets assigned as its own partner (uneven) */
			if(studentsLeft != -1){
				try {
					/* Writes the partner name to the clients */
					out[count-2] = new PrintWriter(client[countPair].getOutputStream(), true);
					out[count-2].println(names[countPair + 1]);
					out[count-1] = new PrintWriter(client[countPair + 1].getOutputStream(), true);
					out[count-1].println(names[countPair]);

				} catch (IOException e){ e.printStackTrace();}


			}
			/* If there are no more students to assign a partner to, notify all */
			if(studentsLeft < 1){
				System.out.println("All students have been assigned to a partner");
				/* Sends a message to all clients that the selecting phase is over */
				for(int i = 0; i < nStudents; i++){
					out[i].println("done");
					out[i].close();
				}

				try {
					/* close all connections and shuts down the server */
					serverSocket.close();
					in.close();
					client[0].close();
					client[1].close();
					done = true;
				} 
				catch (IOException e) {e.printStackTrace();}
			}		
		}
	}

}
