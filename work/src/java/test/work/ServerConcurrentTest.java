package work;

import org.junit.Test;

import java.io.IOException;

public class ServerConcurrentTest {

	public static void main(String[] args) {
		Thread t1 = new Thread(new work.ConcurrentTest(100));
		Thread t2 = new Thread(new work.ConcurrentTest(200));
		Thread t3 = new Thread(new work.ConcurrentTest(300));
		Thread t4 = new Thread(new work.ConcurrentTest(400));
		Thread t5 = new Thread(new work.ConcurrentTest(500));

		Thread t6 = new Thread(new work.ConcurrentTest(600));
		Thread t7 = new Thread(new work.ConcurrentTest(700));
		Thread t8 = new Thread(new work.ConcurrentTest(800));
		Thread t9 = new Thread(new work.ConcurrentTest(900));
		Thread t10 = new Thread(new work.ConcurrentTest(1000));

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();

		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
	}
}
