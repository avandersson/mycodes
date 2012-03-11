
public class Main {

	public static void main(String[] args) throws InterruptedException {
		int nStudents = 7;

		/* Sets the number of students in the class to the first input parameter if any */
		if(args.length > 0){
			nStudents = Integer.parseInt(args[0]);
		}
		System.out.println("There are (" + nStudents + ") students in the class");

		/* Creates and starts the teacher thread */
		new Thread(new Teacher(nStudents)).start();

		Thread.sleep(1000);

		/* Creates and starts the student threads */
		for(int i = 0; i < nStudents; i++){
			new Thread(new Student("(" + i + ")")).start();
		}

	}

}
