package hive.main;

import hive.engine.Controller;
import hive.engine.Game;
import hive.engine.IController;
import hive.engine.Move;
import hive.view.Table;

public class MainApp {
	
	Game current_game;
	Table current_view = new Table();
	IController current_controller;
	
	public Game createNewGame() {
		current_game = new Game();
		current_view.setGame(current_game);
		
		current_controller = new Controller(current_game, current_view) {
			@Override
			public void newGame() {
				createNewGame();
			}
		};
		
		current_game.start();
		
		return current_game;
	}
	
	public Table view() {
		return current_view;
	}
	
	public IController controller() {
		return current_controller;
	}


}
