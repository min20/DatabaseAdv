package galaxyWar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class AttackThread extends Thread {
	private final static String SHARD_SLAVE1 = "10.211.55.5";
	private final static String SHARD_SLAVE2 = "10.211.55.6";
	private final static String SHARD_MASTER = "10.211.55.7";

	private boolean running = true;

	public static int destroyed = -1;

	public void finish() {
		running = false;
	}

	public boolean isRunning() {
		return running;
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
						.prepareCall("{ CALL getRandomUserLocation( ?, ? ) }");
				stmtMaster.registerOutParameter(1, Types.INTEGER);
				stmtMaster.registerOutParameter(2, Types.INTEGER);
				stmtMaster.executeQuery();
				int outUid = (int) stmtMaster.getObject(1);
				int outDbid = (int) stmtMaster.getObject(2);
				stmtMaster.close();
				
				System.out.println("User #" + outUid + " attacks Galaxy #"
						+ outDbid);

				CallableStatement stmtSlave = connSlave[outDbid]
						.prepareCall("{ CALL attack( ?, ?, ? ) }");
				stmtSlave.setInt(1, outUid);
				stmtSlave.registerOutParameter(2, Types.INTEGER);
				stmtSlave.registerOutParameter(3, Types.TINYINT);
				stmtSlave.executeQuery();

				int power = (int) stmtSlave.getObject(2);
				int gid = (int) stmtSlave.getObject(3);
				stmtSlave.close();

				Statement stmt = connSlave[gid % 2].createStatement();
				int row = stmt.executeUpdate("UPDATE galaxy SET hp = hp - "
						+ power + " WHERE gid = " + gid);
				stmt.close();

				if (row == 0)
					System.out.println("Update Failed");

				stmt = connSlave[gid % 2].createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT gid FROM galaxy WHERE hp <= 0 ");

				if (rs.next()) {
					destroyed = rs.getInt(1);
					running = false;
				}
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			running = false;
		}
	}
}
