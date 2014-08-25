package cacheTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CacheTestMain {
	static final String DB_ADDRESS = "jdbc:mysql://10.73.45.60/cache";
	static final String DB_USER = "popi";
	static final String DB_PASSWORD = "db1004";
	
	static int countMiss = 0;
	static int countHit = 0;

	public static void main(String[] args) throws Exception {
		Connection connection = null;
		PreparedStatement pstmtSelectById = null;
		BufferedReader inputTxt = null;
		
		// MySQL 드라이버 로드
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("MySQL Driver ERROR: " + classNotFoundException.getMessage());
			
			return;
		}
		System.out.println("MySQL Driver is loaded");

		
		// input.txt 불러오기
		try {
			inputTxt = new BufferedReader(new FileReader("input.txt"));
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("input.txt Not Found: " + fileNotFoundException.getMessage());
			
			return;
		}

		
		// DB 커넥션 생성
		try {
			connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
			pstmtSelectById = connection.prepareStatement("SELECT * FROM data_set WHERE id = ?");
			
			System.out.println("Connection Success");
		} catch (SQLException e) {
			System.err.println("Fail to Connect MySQL Server: " + e.getMessage());
			
			return;
		}

		
		long startTime = System.currentTimeMillis();
		String strTargetId = null;
		int targetId = 0; 
		Cache cache = new Cache();
		cache.setMaxSize(700);
		
		// 메인 테스트로직 수행
		while ((strTargetId = inputTxt.readLine()) != null) {
			targetId = Integer.parseInt(strTargetId);
			
			// Without Cache
//			selectById(pstmtSelectById, targetId);
			
			// Use Full Cache
//			findCache(pstmtSelectById, cache, targetId);
			
			// Use 700 Cache
			findCache(pstmtSelectById, cache, targetId);

		}
		
		long endTime = System.currentTimeMillis();

		
		System.out.println("TIME: " + (endTime - startTime));
		System.out.println("HIT: " + countHit);
		System.out.println("MISS: " + countMiss);

		
		// 커넥션 종료
		if (connection != null) {
			connection.close();
		}
		if (pstmtSelectById != null) {
			pstmtSelectById.close();
		}
	}
	
	private static Record selectById(PreparedStatement pstmt, int id) throws SQLException {
		String name;
		ResultSet resultSet = null;
		
		pstmt.setInt(1, id);
		
		resultSet = pstmt.executeQuery();
		resultSet.next();
		name = resultSet.getString("name");
		
		countMiss++;
		
		return new Record(id, name);
	}
	
	private static Record findCache(PreparedStatement pstmt, Cache cache, int id) throws SQLException {
		Record record = cache.lookUp(id);

		// 캐시에 없으면 새로 추가
		if(record == null) {
			record = selectById(pstmt, id);
			cache.store(record);
		} else {
			countHit++;
		}

		return record;
	}

}
