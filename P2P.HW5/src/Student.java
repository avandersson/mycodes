import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

public class Student implements Runnable{
	private int id;
	private int nPeers;
	private String partner;

	public Student(int id, int nPeers){
		this.id = id;
		this.nPeers = nPeers;
	}

	@Override
	public void run() {

		int studentsLeft[] = new int[nPeers], randomValue = 0, possiblePartners = 0;
		String temp, print;
		ServerSocket server = null;
		PrintWriter outForward = null, outPre = null;
		BufferedReader inPre = null, inForward = null;
		Socket peer = null, socket = null;

		try {
			System.out.println("Student: " + id + ", With port: " + 8080 + id);
			/* Creates a server socket that will be listening for requests */
			server = new ServerSocket(8080 + id);

			/* If someone requests a connection, accept the connection */
			peer = server.accept();
			/* Creates a reader and a writer to be able to receive and 
			 * send messages to the connected peer */
			inPre = new BufferedReader(new InputStreamReader(peer.getInputStream()));
			outPre = new PrintWriter(peer.getOutputStream(), true);
			Thread.sleep(500);

			/* Reads the first line that is the initialization message.			
			 * Reads all lines sent until end message "done" is sent. 
			 * Removes it self from the list of available students. */
			String initMessage = inPre.readLine();
			while(!(temp = inPre.readLine()).equals("done")){				
				if(Integer.parseInt(temp) != id){
					studentsLeft[possiblePartners] = Integer.parseInt(temp);
					possiblePartners++;
				}			
			}
			/* If there are partners to choose from: enter */
			if(possiblePartners != 0){
				Random rand = new Random();
				if(possiblePartners == 1) randomValue = 0;
				else randomValue=rand.nextInt(possiblePartners);

				/* Connects to a random student and creates a reader and a writer to 
				 * the this student */
				socket = new Socket("localhost", 8080 + studentsLeft[randomValue]);
				outForward = new PrintWriter(socket.getOutputStream(), true);
				inForward = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				/* Uses the initialization message to see if the student should pick a partner or
				 * someone els have picked him. If someone have picked him set the one sending the message
				 * to be the partner.	
				 * 			
				 * If the student should pick a partner, pick a partner and send id to it */
				if(initMessage.equals("pick any student")){
					outForward.println(id);
					partner = ""+studentsLeft[randomValue];

				}
				/* Send a init message to the student and sets its own partner to be the senders id */
				else{					
					outForward.println("pick any student");
					partner = initMessage;
				}
				print = "My id: " + id + "\t My Partner: " + partner;
				/* Sends the updated list of students that are without a partner and prints "done */
				for(int j = 0; j < possiblePartners; j++){
					outForward.println(studentsLeft[j]);
				}
				outForward.println("done");
				System.out.println("Student " + id + " done selecting!");
				boolean done = false;

				/* Waits for a "print" message and prints if received */
				while(done != true){
					if(inForward.readLine().equals("print")){ 
						done = true;
						System.out.println(print);
					}
				}				

				/* Sends a message to the student this student received a message from, 
				 * letting him know he should print his partner */
				outPre.println("print");

				/* Closes the connections */
				inForward.close();
				outForward.close();
				socket.close();
			}
			/* If this student is the last one picking a partner */
			else{
				System.out.println("Student " + id + " done selecting!\n");
				System.out.println("Number of students in the class: " + nPeers);

				/* If this student receives a pick message it means that
				 * this student should pick itself as a partner 
				 * If not set its partner to be the sender */
				if(initMessage.equals("pick any student")){
					System.out.println("My id: " + id + "\t My Partner: " + id + "");
				}else{
					System.out.println("My id: " + id + "\t My Partner: " + initMessage);
				}
				/* Starts the printing of partners with sending a print message in the opposite direction */
				outPre.println("print");
			}

			/* Closes the connections */
			inPre.close();
			outPre.close();
			server.close();
		} 
		catch (IOException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}		
	}
}
