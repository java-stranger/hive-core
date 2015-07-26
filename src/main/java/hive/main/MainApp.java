package hive.main;

import hive.engine.Controller;
import hive.engine.Game;
import hive.engine.IController;
import hive.view.Table;

public class MainApp {
	
	Game current_game;
	Table current_view = new Table();
	final Controller controller = new Controller() {
		@Override
		public void newGame() {
			createNewGame();
		}
	};
	
	public Game createNewGame() {
		current_game = new Game();
		current_view.setGame(current_game);
		controller.setGameAndView(current_game, current_view);
		
		current_game.start();
		
		return current_game;
	}
	
	public Table view() {
		return current_view;
	}
	
	public IController controller() {
		return controller;
	}


}
