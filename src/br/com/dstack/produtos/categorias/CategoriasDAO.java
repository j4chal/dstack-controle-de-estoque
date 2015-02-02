package br.com.dstack.produtos.categorias;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoriasDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(CategoriasDAO.class);

	public CategoriasDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Insere a descrição de uma categoria no banco de dados
	 * 
	 * @param categorias
	 * @return Boolean
	 */
	public boolean insere(Categorias categorias) {

		String sql = "INSERT INTO CATEGORIAS (DESCRICAO) VALUES (?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, categorias.descricaoProperty().get());

			if (stmt.executeUpdate() > 0) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return false;
	}
}
