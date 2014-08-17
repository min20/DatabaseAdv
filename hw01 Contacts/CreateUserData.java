import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CreateUserData {
	final static int NUM_USERS = 1000000;
	final static int NUM_FILES = 1;
	
	public static void main(String args[]) throws IOException {
		String fileName = "./user_data";
		String extension = ".csv";
		
		for (int fileNumber = 0 ; fileNumber < NUM_FILES ; fileNumber++) {
			createData(fileName + fileNumber + extension, fileNumber);
		}
	}
	
	public static void createData(String fileName, int fileNumber) throws IOException {
		Random random = new Random();
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		
		for (int idx = 1 ; idx <= NUM_USERS / NUM_FILES ; idx++) {
			String output = "";
			
			int birth1 = random.nextInt(100);
			int birth2 = random.nextInt(12) + 1;
			int birth3 = random.nextInt(30) + 1;
			
			int id  = fileNumber * NUM_USERS + idx;
			
			int phone2 = random.nextInt(9000) + 1000;
			int phone3 = random.nextInt(10000);
			
			// id
			output += String.format("%02d", birth1) + String.format("%02d", birth2) + String.format("%02d", birth3)
					+ "-" + String.format("%07d", id) + ",";
			
			// name
			output += getRandomString(random, 5) +  ",";
			
			// phone number
			output += String.format("%03d", 10) + "-" + String.format("%04d", phone2) + "-"
					+ String.format("%04d", phone3) + ",";
			
			// addr
			output += getRandomString(random, 50) +  "\n";
			
			bw.write(output);
		}
		
		bw.close();
	}
	
	public static String getRandomString(Random random, int length) {
		String name = "";
		char charctor;
		
		for(int idx = 0 ; idx < length ; idx++) {
			charctor = (char) getASCII(random);
			name += charctor;
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
