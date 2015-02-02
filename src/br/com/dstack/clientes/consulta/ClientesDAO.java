package br.com.dstack.clientes.consulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.clientes.Clientes;

public class ClientesDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(ClientesDAO.class);

	public ClientesDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Retorna uma ObservableList com os clientes da tabela Clientes
	 */
	public ObservableList<Clientes> consulta() throws SQLException {
		ObservableList<Clientes> consulta = FXCollections.observableArrayList();

		String sql = "SELECT * FROM CLIENTES";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_CLIENTE");
					String nome = rs.getString("NOME_COMPLETO");
					String cpf = rs.getString("CPF");
					String rg = rs.getString("RG");
					String fantasia = rs.getString("FANTASIA");
					String pessoa = rs.getString("PESSOA");
					String sexo = rs.getString("SEXO");
					String dataCadastro = rs.getString("DATA_CADASTRO");
					String email = rs.getString("EMAIL");
					String dataNascimento = rs.getString("DATA_NASCIMENTO");
					String cep = rs.getString("CEP");
					String endereco = rs.getString("ENDERECO");
					int numero = rs.getInt("NUMERO");
					String bairro = rs.getString("BAIRRO");
					String cidade = rs.getString("CIDADE");
					String uf = rs.getString("UF");
					String telefone = rs.getString("TELEFONE");
					String fax = rs.getString("FAX");
					String banco = rs.getString("BANCO");
					int agencia = rs.getInt("AGENCIA");

					consulta.add(new Clientes(codigo, nome, cpf, rg, fantasia, pessoa, sexo, dataCadastro, email, dataNascimento, cep, endereco, numero, bairro, cidade, uf, telefone, fax, banco,
							agencia));
				}
			}
		}
		return consulta;
	}

	/**
	 * Deleta por id dado o item selecionado
	 */
	public boolean deletePorId(int itemSelecionado) {

		String sql = "DELETE FROM CLIENTES WHERE COD_CLIENTE = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemSelecionado);
			if (stmt.execute()) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.error("SQLException", e1);
		}
		return false;
	}
}
