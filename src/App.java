import java.io.IOException;

import customCursor.CustomCursor;

public class App {

	public static void main(String[] args) throws IOException {
		//CursorMod mod = new CursorMod();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				//mod.unHideCursor();
				System.out.println("In shutdown hook");
			}
		}, "Shutdown-thread"));

		//mod.hideCursor();
	
		CustomCursor cursor = new CustomCursor();
		cursor.show();

		System.out.println("Running");
	}
}
