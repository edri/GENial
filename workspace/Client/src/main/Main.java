package main;

import application.App;

public class Main {

	public static void main(String[] args) {
		Thread mainThread = new Thread(new App());
		mainThread.start();
	}

}
