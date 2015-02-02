package br.com.dstack.usuarios.consulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.usuarios.Usuarios;

public class UsuariosDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(UsuariosDAO.class);

	public UsuariosDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Retorna uma ObservableList com os usuarios da tabela Usuarios
	 * 
	 * @return ObservableList de Usuarios
	 */
	public ObservableList<Usuarios> consulta() {
		ObservableList<Usuarios> consulta = FXCollections.observableArrayList();

		String sql = "SELECT * FROM USUARIOS";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_USUARIO");
					String nomeCompleto = rs.getString("NOME_COMPLETO");
					String usuario = rs.getString("USUARIO");
					String senha = rs.getString("SENHA");
					String email = rs.getString("EMAIL");
					String situacao = rs.getString("SITUACAO");
					String perfil = rs.getString("PERFIL");

					consulta.addAll(new Usuarios(codigo, usuario, nomeCompleto, senha, email, situacao, perfil));
				}
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return consulta;
	}

	/**
	 * Deleta por id o usuario selecionado
	 * 
	 * @param itemSelecionado
	 * @return Boolean
	 */
	public boolean deletePorId(int itemSelecionado) {

		String sql = "DELETE FROM USUARIOS WHERE COD_USUARIO = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemSelecionado);
			if (stmt.execute()) {
				return true;
			}
		}
		catch (SQLException e2) {
			logger.log(Level.TRACE, "SQLException", e2);
		}
		return false;
	}
}
