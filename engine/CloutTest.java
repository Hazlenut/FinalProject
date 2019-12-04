package engine;

import java.util.Scanner;

public class CloutTest {

	public static void cloutTest() {
		
		/* 
		 * Clout = (Likes+Dislikes)/Time
		 * Maybe
		 * Clout = (Likes+Dislikes) * CloutFactor/Time
		 * CloutFactor is credibility of user (default = 5)
		 * Maybe
		 * add a concave down and decreasing curve to base points off of
		 */
		Scanner input = new Scanner(System.in);
		double likes = 2;
		double dislikes = -1;
		double total = 0;
		double time = 12; //hours
		double cloutFactor = 5;
		int n = input.nextInt();
		input.nextLine();
		for(int i = 0; i < n; i++) {
			String a = input.nextLine();
			if(a.equals("a")) {
				total+=likes;
			}else if(a.equals("b")) {
				total+=dislikes;
			}
		}
		for(int i = 1; i < time; i++) {
			double clout = total*cloutFactor/(i);
			System.out.println("time: " + i + " " + clout);
		}

	}

}