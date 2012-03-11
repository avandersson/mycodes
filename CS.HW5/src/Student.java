import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Student implements Runnable {
	String name;
	boolean done = false;

	public Student(String name){
		this.name = name;
	}

	public void run(){

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String partner = null;

		try {
			/* Tries to connect to the server */
			socket = new Socket("localhost", 8080);
			System.out.println(socket.getInetAddress());
			/* Creates a writer */
			out = new PrintWriter(socket.getOutputStream(), true);
			/* Sends a message to the teacher containing the students name */
			out.println(name);
			/* Creates a reader */
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			/* Reads a message coming from the server containing the students partner */
			partner = in.readLine();

			/* If the student receives a "done" message, print the students partner */
			while(done != true){
				if(in.readLine().equals("done")) 
					done = true;
				System.out.println("Student: " + name + ", Partner: " + partner);
				/* Close the connection */
				socket.close();
				out.close();
				in.close();
			}

		} 
		catch (IOException e) {e.printStackTrace();}
	}
}
