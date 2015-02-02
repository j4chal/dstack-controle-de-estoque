package br.com.dstack.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.Label;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CriarBDComTabelas {

	private static Logger logger = LogManager.getLogger(CriarBDComTabelas.class);
	
	/**
	 * Método que faz a verificação ao abrir a aplicação, caso não detectar o banco de dados, é rodado o script
	 */
	public void criar(Label label) {
		try (Connection conn = new DbConn().getConnection()) {
			conn.createStatement();
		}
		catch (SQLException ex) {
			if (ex.getErrorCode() == -1) {
				try (Connection conn = new DbConn().getConnectionSemDB()) {

					InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/br/com/dstack/utils/sql/script_dstack.sql"));
					try (BufferedReader bf = new BufferedReader(isr)) {
						new ScriptRunner(conn).runScript(bf);
					}
					catch (IOException e1) {
						logger.log(Level.TRACE, "IOException", e1);
					}
				}
				catch (SQLException e2) {
					label.setText("Não foi possivel realizar a conexão com banco de dados");
					logger.log(Level.TRACE, "SQLException", e2);
				}
			}
		}
	}
}
