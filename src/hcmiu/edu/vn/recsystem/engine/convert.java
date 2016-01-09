 package hcmiu.edu.vn.recsystem.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class convert {

	public static void main(String[] args) throws IOException {
		BufferedReader rd = new BufferedReader(new FileReader("data/ratings.dat"));
		BufferedWriter wt = new BufferedWriter(new FileWriter("data/rate.csv"));
		
		String line;
		while((line = rd.readLine()) != null){
			String values[] = line.split("::", -1);
			
			wt.write(values[0].trim()+","+values[1].trim()+","+values[2].trim()+"\n");
		}
		System.out.print("ok now");
		rd.close();
		wt.close();
	}

}
