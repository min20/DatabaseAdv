package createTestDataSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CreateCache {
	final static int NUM_USERS = 10000;
	static BufferedWriter bw;
	static Random random = new Random();
	
	public static void main(String args[]) throws IOException {
		String fileName = "./user_data";
		
		bw = new BufferedWriter(new FileWriter(fileName));
		for (int i = 0; i < 10000; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append((i + 1) + ",").append(getRandomString(random, 10000).append("\n").toString());
			bw.write(sb.toString());
		}
		bw.close();
	}
	
	public static StringBuffer getRandomString(Random random, int length) {
		StringBuffer name = new StringBuffer();
		char charctor;
		
		for(int idx = 0 ; idx < length ; idx++) {
			charctor = (char) getASCII(random);
			name.append(charctor);
		}
		
		return name;
	}
	
	public static int getASCII(Random random) {
		// 숫자 48 ~ 57 (10개)
		// 대문자 65 ~ 90 (26개)
		// 소문자 97 ~ 122 (26개)
		// 총 62개
		int out;
		out = random.nextInt(61) + 1;
		
		if(1 <= out && out < 10) {
			return out + 47;
		}
		else if(10 <= out && out < 36) {
			return out + 55;
		}
		else {
			return out + 61;
		}
	}


}
