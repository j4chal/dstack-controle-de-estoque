package br.com.dstack.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(LoginDAO.class);

	public LoginDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Dado usuario, é retornado o hash da senha desse usuario
	 * 
	 * @param usuario
	 * @return String com Hash da Senha
	 */
	public String autentica(String usuario) {
		String hashSenha = null;

		String sql = "SELECT SENHA FROM USUARIOS WHERE USUARIO = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, usuario);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					hashSenha = rs.getString("SENHA").trim();
				}
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return hashSenha;
	}

	/**
	 * Dado usuario, é selecionado o perfil e situação desse usuario, e é colocado dentro de um HashMap e retornado
	 * 
	 * @param usuario
	 * @return Situação e perfil desse usuario
	 */
	public Map<String, String> validaConta(String usuario) {
		Map<String, String> situacaoComPerfil = new HashMap<>();

		String sql = "SELECT PERFIL, SITUACAO FROM USUARIOS WHERE USUARIO = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, usuario);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String perfil = rs.getString("PERFIL");
					String situacao = rs.getString("SITUACAO");
					situacaoComPerfil.put("Perfil", perfil);
					situacaoComPerfil.put("Situacao", situacao);
				}
			}
		}
		catch (SQLException e2) {
			logger.log(Level.TRACE, "SQLException", e2);
		}
		return situacaoComPerfil;
	}
}
