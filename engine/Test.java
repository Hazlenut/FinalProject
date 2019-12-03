package engine;
import java.io.IOException;
import java.util.ArrayList;

public class Test extends Events{

	public static void main(String[] args) {
		
		ArrayList<Movie> test = new ArrayList<Movie>();
		try {
			//add method to get zipcode from user input
			//add method to get results displayed 
			test = getMovies(20166, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i).getShowName());
		}
	}

}
