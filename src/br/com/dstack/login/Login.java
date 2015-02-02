package br.com.dstack.login;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.utils.BCrypt;
import br.com.dstack.utils.DbConn;

public class Login {

	private static StringProperty usuario;
	private StringProperty senha;
	private static Logger logger = LogManager.getLogger(Login.class);

	public Login() {

	}

	public Login(String senha) {
		this.senha = new SimpleStringProperty(senha);
	}

	public static void setUsuarioProperty(String usuario) {
		Login.usuario = new SimpleStringProperty(usuario);
	}

	public static StringProperty usuarioProperty() {
		return usuario;
	}

	/**
	 * Faz a conexão, passa o usuario digitado e retorna o hash da senha desse usuario
	 * 
	 * @return String com hash da senha
	 * @throws SQLException
	 */
	public String enviaDados() throws SQLException {
		String hashSenha = null;

		try (Connection conn = new DbConn().getConnection()) {
			LoginDAO dao = new LoginDAO(conn);
			hashSenha = dao.autentica(Login.usuario.get());
		}

		return hashSenha;
	}

	/**
	 * Compara a senha digitada com o hash da senha registrada no banco de dados
	 * 
	 * @return Boolean
	 * @throws SQLException
	 */
	public boolean verificaHash() throws SQLException {
		if (enviaDados() == null || !enviaDados().startsWith("$2a$")) {

		}
		else {
			if (BCrypt.checkpw(this.senha.get(), enviaDados())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Faz a conexão com o banco e retorna um HashMap da situação e perfil do usuario logado, e joga no método validaConta()
	 * 
	 * @param usuario
	 * @return Map com Situação e Perfil
	 */
	public Map<String, String> getSituacaoEPerfil(String usuario) {
		Map<String, String> situacaoEPerfil = new HashMap<>();

		try (Connection conn = new DbConn().getConnection()) {
			LoginDAO dao = new LoginDAO(conn);
			situacaoEPerfil.putAll(dao.validaConta(usuario));
		}
		catch (SQLException e2) {
			logger.log(Level.TRACE, "SQLException", e2);
		}
		return situacaoEPerfil;
	}
}
