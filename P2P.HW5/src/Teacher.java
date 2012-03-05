import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Teacher implements Runnable{
	private int nStudents;

	public Teacher(int nStudents){
		this.nStudents = nStudents;
	}

	@Override
	public void run() {
		PrintWriter out = null;
		Random rand = new Random();
		try {
			/* Gets a random value that will be interpreted to a id of a student */
			int randomValue = rand.nextInt(nStudents);
			/* Creates a socket connected to the random student,
			 	a specific student socket is identified by the port number */
			Socket socket = new Socket("localhost", 8080 + randomValue);
			/* Creates a printwriter to send messages to the student */
			out = new PrintWriter(socket.getOutputStream(), true);
			/* Sends an initialize message to the student */
			out.println("pick any student");
			/* Sends the list of students that are available to choose as a partner */
			for(int i = 0; i < nStudents; i++){				
				out.println(i);
			}
			/* Sends a end message letting the student know that the sending is done */
			out.println("done");
			System.out.println("Teacher sent message to: " + randomValue);
			/* Closes the socket and printwriter */
			socket.close();
			out.close();

		} 
		catch (NumberFormatException e) {e.printStackTrace();} 
		catch (UnknownHostException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}

	}

}
