
public class Main {


	public static void main(String[] args) throws InterruptedException {

		int nStudents = 7;
		/* Sets the number of students in the class to the first input parameter if any */
		if(args.length > 0){
			nStudents = Integer.parseInt(args[0]);
		}


		/* Creates the students */
		for(int i = 0; i <nStudents; i++){
			if(i < 10){
				new Thread(new Student(i,nStudents)).start();
			}else{
				new Thread(new Student(i,nStudents)).start();
			}
		}

		Thread.sleep(1000);

		/* Creates the teacher */
		new Thread(new Teacher(nStudents)).start();

	}

}
