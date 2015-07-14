package main.java.hive.main;

import main.java.hive.engine.Game;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello hive!");
		
		Game hive_game = new Game();
		hive_game.start();
		
		hive_game.test();

		System.out.println("Game over!");
	}

}
