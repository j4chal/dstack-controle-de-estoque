package br.com.dstack.clientes.cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.clientes.Clientes;
import br.com.dstack.utils.Data;

public class CadastroClientesDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(CadastroClientesDAO.class);

	public CadastroClientesDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Insere os dados do cliente no banco de dados
	 * 
	 * @param clientes
	 * @return Boolean
	 */
	public boolean insere(Clientes clientes) {

		String sql = "INSERT INTO CLIENTES (NOME_COMPLETO, CPF, RG, FANTASIA, PESSOA, SEXO, DATA_CADASTRO, EMAIL, DATA_NASCIMENTO, CEP, ENDERECO, NUMERO, BAIRRO, CIDADE, UF, TELEFONE, FAX, BANCO, AGENCIA) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, clientes.nomeProperty().get());
			stmt.setString(2, clientes.cpfProperty().get());
			stmt.setString(3, clientes.rgProperty().get());
			stmt.setString(4, clientes.fantasiaProperty().get());
			stmt.setString(5, clientes.pessoaProperty().get());
			stmt.setString(6, clientes.sexoProperty().get());
			stmt.setString(7, Data.getDataAtual());
			stmt.setString(8, clientes.emailProperty().get());
			stmt.setString(9, clientes.dataNascimentoProperty().get());
			stmt.setString(10, clientes.cepProperty().get());
			stmt.setString(11, clientes.enderecoProperty().get());
			stmt.setInt(12, clientes.numeroProperty().get());
			stmt.setString(13, clientes.bairroProperty().get());
			stmt.setString(14, clientes.cidadeProperty().get());
			stmt.setString(15, clientes.ufProperty().get());
			stmt.setString(16, clientes.telefoneProperty().get());
			stmt.setString(17, clientes.faxProperty().get());
			stmt.setString(18, clientes.bancoProperty().get());
			stmt.setInt(19, clientes.agenciaProperty().get());

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
	 * Faz o update de dados do cliente no banco de dados
	 * 
	 * @param codigo
	 * @param nome
	 * @param cpf
	 * @param rg
	 * @param fantasia
	 * @param pessoa
	 * @param sexo
	 * @param email
	 * @param dataNascimento
	 * @param cep
	 * @param endereco
	 * @param numero
	 * @param bairro
	 * @param cidade
	 * @param uf
	 * @param telefone
	 * @param fax
	 * @param banco
	 * @param agencia
	 * @return Boolean
	 */
	public boolean update(int codigo, String nome, String cpf, String rg, String fantasia, String pessoa, String sexo, String email, String dataNascimento, String cep,
			String endereco, int numero, String bairro, String cidade, String uf, String telefone, String fax, String banco, int agencia) {

		String sql = "UPDATE CLIENTES SET NOME_COMPLETO = ?, CPF = ?, RG = ?, FANTASIA = ?, PESSOA = ?, SEXO = ?, EMAIL = ?, DATA_NASCIMENTO = ?, CEP = ?, ENDERECO = ?,"
				+ " NUMERO = ?, BAIRRO = ?, CIDADE = ?, UF = ?, TELEFONE = ?, FAX = ?, BANCO = ?, AGENCIA = ? WHERE COD_CLIENTE = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nome);
			stmt.setString(2, cpf);
			stmt.setString(3, rg);
			stmt.setString(4, fantasia);
			stmt.setString(5, pessoa);
			stmt.setString(6, sexo);
			stmt.setString(7, email);
			stmt.setString(8, dataNascimento);
			stmt.setString(9, cep);
			stmt.setString(10, endereco);
			stmt.setInt(11, numero);
			stmt.setString(12, bairro);
			stmt.setString(13, cidade);
			stmt.setString(14, uf);
			stmt.setString(15, telefone);
			stmt.setString(16, fax);
			stmt.setString(17, banco);
			stmt.setInt(18, agencia);
			stmt.setInt(19, codigo);

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
	 * Faz a consulta de UF's da tabela Estado
	 * 
	 * @return ObservableList
	 */
	public ObservableList<String> consultaDeUf() {
		ObservableList<String> consulta = FXCollections.observableArrayList();

		String sql = "SELECT UF FROM ESTADO";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					String uf = rs.getString("UF");

					consulta.add(uf);
				}
			}
		}
		catch (SQLException e3) {
			logger.log(Level.TRACE, "SQLException", e3);
		}
		return consulta;
	}
}
