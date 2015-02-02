package br.com.dstack.produtos.marcas;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.utils.DbConn;

public class Marcas {

	private SimpleStringProperty descricao;
	private static Logger logger = LogManager.getLogger(Marcas.class);

	public Marcas() {

	}

	public Marcas(String descricao) {
		this.descricao = new SimpleStringProperty(descricao);
	}

	public StringProperty descricaoProperty() {
		return this.descricao;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			MarcasDAO dao = new MarcasDAO(conn);

			if (dao.insere(this)) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return false;
	}
}
