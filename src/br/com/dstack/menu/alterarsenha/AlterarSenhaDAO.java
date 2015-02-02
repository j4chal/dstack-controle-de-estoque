package br.com.dstack.menu.alterarsenha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.login.Login;

public class AlterarSenhaDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(AlterarSenhaDAO.class);

	public AlterarSenhaDAO(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * Método que recebe nova senha por parametro e faz a alteração no banco de dados dado usuario logado.
	 * 
	 * @param senha
	 * @return Boolean
	 */
	public boolean update(String senha) {

		String sql = "UPDATE USUARIOS SET SENHA = ? WHERE USUARIO = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, senha);
			stmt.setString(2, Login.usuarioProperty().get());
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
