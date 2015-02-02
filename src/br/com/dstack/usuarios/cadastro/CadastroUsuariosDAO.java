package br.com.dstack.usuarios.cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.usuarios.Usuarios;

public class CadastroUsuariosDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(CadastroUsuariosDAO.class);

	public CadastroUsuariosDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Invoca o método autoDecrement e insere os dados no banco de dados
	 * 
	 * @param cum
	 * @return Boolean
	 */
	public boolean insere(Usuarios cum) {

		String sql = "INSERT INTO USUARIOS (USUARIO, NOME_COMPLETO, SENHA, EMAIL, SITUACAO, PERFIL) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			autoDecrement();
			stmt.setString(1, cum.usuarioProperty().getValue());
			stmt.setString(2, cum.nomeCompletoProperty().getValue());
			stmt.setString(3, cum.senhaProperty().getValue());
			stmt.setString(4, cum.emailProperty().getValue());
			stmt.setString(5, cum.situacaoProperty().getValue());
			stmt.setString(6, cum.perfilProperty().getValue());
			if (stmt.executeUpdate() > 0) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return false;
	}

	/**
	 * Realiza o update das informações do usuario no banco de dados
	 * 
	 * @param codigo
	 * @param usuario
	 * @param nomeCompleto
	 * @param senha
	 * @param email
	 * @param situacao
	 * @param perfil
	 * @return Boolean
	 */
	public boolean update(int codigo, String usuario, String nomeCompleto, String senha, String email, String situacao, String perfil) {

		String sql = "UPDATE USUARIOS SET NOME_COMPLETO = ?, USUARIO = ?, SENHA = ?, EMAIL = ?, SITUACAO = ?, PERFIL = ? WHERE COD_USUARIO = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nomeCompleto);
			stmt.setString(2, usuario);
			stmt.setString(3, senha);
			stmt.setString(4, email);
			stmt.setString(5, situacao);
			stmt.setString(6, perfil);
			stmt.setInt(7, codigo);
			if (stmt.executeUpdate() > 0) {
				return true;
			}
		}
		catch (SQLException e2) {
			logger.log(Level.TRACE, "SQLException", e2);
		}
		return false;
	}

	/**
	 * Método que retorna a quantidade de linhas da tabela usuarios
	 * 
	 * @return int com numero de rows
	 */
	public int pegaQuantidadeLinhas() {

		String sql = "SELECT * FROM USUARIOS";
		int currentRows = 0;

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					currentRows = rs.getRow();
				}
			}
		}
		catch (SQLException e3) {
			logger.log(Level.TRACE, "SQLException", e3);
		}
		return currentRows;
	}

	/**
	 * Altera o auto_increment da tabela usuarios com a quantidade de linhas existentes na tabela
	 */
	public void autoDecrement() {

		String sql = "ALTER TABLE USUARIOS AUTO_INCREMENT = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pegaQuantidadeLinhas());
			stmt.executeUpdate();
		}
		catch (SQLException e4) {
			logger.log(Level.TRACE, "SQLException", e4);
		}
	}
}
