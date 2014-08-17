package galaxyWar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class RegisterThread extends Thread {
	private final static String SHARD_SLAVE1 = "10.211.55.5";
	private final static String SHARD_SLAVE2 = "10.211.55.6";
	private final static String SHARD_MASTER = "10.211.55.7";
	
	private boolean running = true;
	
	public static int isFull = -1;

	public boolean isRunning() {
		return this.running;
	}
	
	public void finish() {
		running = false;
	}

	@Override
	public void run() {
		Connection connMaster = null;
		Connection connSlave[] = new Connection[2];
		try {
			connMaster = DriverManager.getConnection("jdbc:mysql://"
					+ SHARD_MASTER + "/shard_master", "shard_master",
					"db1004");
			connSlave[0] = DriverManager.getConnection("jdbc:mysql://"
					+ SHARD_SLAVE1 + "/shard_slave", "shard_slave",
					"db1004");
			connSlave[1] = DriverManager.getConnection("jdbc:mysql://"
					+ SHARD_SLAVE2 + "/shard_slave", "shard_slave",
					"db1004");

			while (running) {
				CallableStatement stmtMaster = connMaster
						.prepareCall("{ CALL createAllUserLocation( ?, ?, ? ) }");
				stmtMaster.registerOutParameter(1, Types.INTEGER);
				stmtMaster.registerOutParameter(2, Types.INTEGER);
				stmtMaster.registerOutParameter(3, Types.TINYINT);
				stmtMaster.executeQuery();

				int outUid = (int) stmtMaster.getObject(1);
				int outDbid = (int) stmtMaster.getObject(2);
				int outGid = (int) stmtMaster.getObject(3);
				stmtMaster.close();

				CallableStatement stmtSlave = connSlave[outDbid]
						.prepareCall("{ CALL createUser( ?, ? ) }");
				stmtSlave.setInt(1, outUid);
				stmtSlave.setInt(2, outGid);
				stmtSlave.executeQuery();
				
				Statement stmt = connSlave[outGid % 2].createStatement();
				stmt = connSlave[outGid % 2].createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT uid FROM user WHERE uid >= 100000 ");

				if (rs.next()) {
					isFull = rs.getInt(1);
					running = false;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			running = false;
		}
	}
}
