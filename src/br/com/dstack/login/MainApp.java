package br.com.dstack.login;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	/**
	 * Abre a tela de Login
	 */
	@Override
	public void start(Stage stage) throws Exception {
		new LoginController(stage);
	}

	public static void main(String[] args) {
		launch();
	}
}
