package br.com.dstack.produtos.marcas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarcasDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(MarcasDAO.class);

	public MarcasDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Insere a descrição de uma marca no banco de dados
	 * 
	 * @param marcas
	 * @return Boolean
	 */
	public boolean insere(Marcas marcas) {

		String sql = "INSERT INTO MARCAS (DESCRICAO) VALUES (?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, marcas.descricaoProperty().get());

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
