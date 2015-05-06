package main;

import communication.Connection;

public class Main {

	public static void main(String[] args) {
		Connection connection = Connection.getInstance();
		connection.connect("10.192.91.41");
	}

}
