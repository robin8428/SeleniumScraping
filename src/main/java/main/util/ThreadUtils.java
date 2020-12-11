package main.util;

public class ThreadUtils {

	public static void doSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
