package br.com.dstack.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class Horario {
	
	/**
	 * Dado a label uma task é iniciada e é realizado um bind a partir do horario atual
	 */
	public void horario(Label label) {

		Task<Void> relogio = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss" + "  ");
				while (true) {
					updateMessage(dtf.format(LocalDateTime.now()));
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException ex) {
						break;
					}
				}
				return null;
			}
		};
		label.textProperty().bind(relogio.messageProperty());
		Thread t1 = new Thread(relogio);
		t1.setDaemon(true);
		t1.start();
	}
}
