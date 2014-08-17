package galaxyWar;

public class Main {
	public static void main(String args[]) throws InterruptedException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		Class.forName("com.mysql.jdbc.Driver").newInstance();

		RegisterThread[] registerThreads = new RegisterThread[100];
		AttackThread[] attackThreads = new AttackThread[4];

		System.out.println("Wait for registerThread");
		for (int i = 0; i < registerThreads.length; ++i) {
			registerThreads[i] = new RegisterThread();
			registerThreads[i].start();
		}
		
		boolean running = true;
		while (running) {
			Thread.sleep(10);
			for (int i = 0; i < registerThreads.length; ++i) {
				if (registerThreads[i].isRunning() == false)
					running = false;
			}
		}
		
		for (int i = 0; i < registerThreads.length; ++i) {
			registerThreads[i].finish();
			registerThreads[i].interrupt();
		}
		
		System.out.println("Fin wating");

		for (int i = 0; i < attackThreads.length; ++i) {
			attackThreads[i] = new AttackThread();
			attackThreads[i].start();
		}

		running = true;
		while (running) {
			Thread.sleep(10);
			for (int i = 0; i < attackThreads.length; ++i) {
				if (attackThreads[i].isRunning() == false)
					running = false;
			}
		}

		for (int i = 0; i < attackThreads.length; ++i) {
			attackThreads[i].finish();
			attackThreads[i].interrupt();
		}

		System.out.println("Game Finished : Galaxy #" + AttackThread.destroyed
				+ " destroyed.");
	}
}
