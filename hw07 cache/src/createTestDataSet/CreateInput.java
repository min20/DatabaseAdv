package createTestDataSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CreateInput {
	final static int NUM_USERS = 10000;
	static BufferedWriter bw;
	static Random random = new Random();
	
	public static void main(String args[]) throws IOException {
		String fileName = "./input.txt";
		ArrayList<Integer> inputList = new ArrayList<Integer>();
		

		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 9; j++) {
				inputList.add(i + 1);
			}
		}
		
		for (int i = 0; i < 1000; i++) {
			inputList.add(random.nextInt(9000) + 1001);
		}
		
		Collections.shuffle(inputList);
		
		bw = new BufferedWriter(new FileWriter(fileName));
		for (int i : inputList) {
			StringBuffer sb = new StringBuffer();
			bw.write(sb.append(i + "\n").toString());
		}
		bw.close();
	}
	
}