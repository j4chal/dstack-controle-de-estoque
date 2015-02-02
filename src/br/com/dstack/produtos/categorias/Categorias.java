package br.com.dstack.produtos.categorias;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.utils.DbConn;

public class Categorias {

	private SimpleStringProperty descricao;
	private static Logger logger = LogManager.getLogger(Categorias.class);

	public Categorias() {
	}

	public Categorias(String descricao) {
		this.descricao = new SimpleStringProperty(descricao);
	}

	public StringProperty descricaoProperty() {
		return this.descricao;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 * 
	 * @return Boolean
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			CategoriasDAO dao = new CategoriasDAO(conn);

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
