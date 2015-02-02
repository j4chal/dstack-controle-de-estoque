package br.com.dstack.utils;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class Validacao {

	/**
	 * Resultado Letras e Non-Word
	 */
	public static void restringirNumeros(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, arg0 -> {

			if (arg0.getCharacter().matches("[0-9]")) {
				arg0.consume();
				label.setText("Numeros estão restritos");
			}
			else if (textfield.getText().length() >= limite) {
				arg0.consume();
				label.setText("Limite de caraceteres alcançado");
			}
			else {
				label.setText("");
			}
		});
	}

	/**
	 * Resultado Numeros e Non-Word
	 */
	public static void restringirLetras(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent arg0) {

				if (arg0.getCharacter().matches("[a-zA-Z]")) {
					arg0.consume();
					label.setText("Letras estão restritas");
				}
				else if (textfield.getText().length() >= limite) {
					arg0.consume();
					label.setText("Limite de caraceteres alcançado");
				}
				else {
					label.setText("");
				}
			}
		});
	}

	/**
	 * Resultado Letras e Numeros
	 */
	public static void restringirNaoAlfaNumericos(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent arg0) {

				if (arg0.getCharacter().matches("[^a-zA-Z0-9]")) {
					arg0.consume();
					label.setText("Não alfa numericos estão restritos");
				}
				else if (textfield.getText().length() >= limite) {
					arg0.consume();
					label.setText("Limite de caraceteres alcançado");
				}
				else {
					label.setText("");
				}
			}
		});
	}

	/**
	 * Resultado Letras c/ Espaços
	 */
	public static void restringirNumerosECharactereEspecial(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent arg0) {

				if (arg0.getCharacter().matches("[^a-zA-Z\\s[0-9]]")) {
					arg0.consume();
					label.setText("Numeros e caraceteres especiais estão restritos");
				}
				else if (textfield.getText().length() >= limite) {
					arg0.consume();
					label.setText("Limite de caraceteres alcançado");
				}
				else {
					label.setText("");
				}
			}
		});
	}

	/**
	 * Resultado Numeros
	 */
	public static void restringirLetrasECaractereEspecial(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent arg0) {

				if (arg0.getCharacter().matches("[^0-9[a-zA-Z]]")) {
					arg0.consume();
					label.setText("Letras e caracteres especiais estão restritos");
				}
				else if (textfield.getText().length() >= limite) {
					arg0.consume();
					label.setText("Limite de caraceteres alcançado");
				}
				else {
					label.setText("");
				}
			}
		});
	}

	/**
	 * Limitar Characteres
	 */
	public static void limitarCaracteres(TextField textfield, int limite, Label label) {
		textfield.addEventFilter(KeyEvent.KEY_TYPED, arg0 -> {

			if (textfield.getText().length() >= limite) {
				arg0.consume();
				label.setText("Limite de caracteres alcançado");
			}
			else {
				label.setText("");
			}
		});
	}
}
