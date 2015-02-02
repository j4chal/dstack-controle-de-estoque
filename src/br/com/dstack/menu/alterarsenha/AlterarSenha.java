package br.com.dstack.menu.alterarsenha;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.utils.BCrypt;
import br.com.dstack.utils.DbConn;
import br.com.dstack.utils.SaltParaSenha;

public class AlterarSenha {

	private String novaSenha;
	private static Logger logger = LogManager.getLogger(AlterarSenha.class);

	public AlterarSenha() {

	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	/**
	 * Faz a conexão com banco dados e passa hash da nova senha
	 * 
	 * @return Boolean
	 */
	public boolean enviarNovaSenha() {
		try (Connection conn = new DbConn().getConnection()) {
			AlterarSenhaDAO dao = new AlterarSenhaDAO(conn);

			if (dao.update(BCrypt.hashpw(novaSenha, SaltParaSenha.getSalt()))) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return false;
	}
}
