package br.com.dstack.fornecedores.cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.fornecedores.Fornecedores;
import br.com.dstack.utils.Data;

public class CadastroFornecedoresDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(CadastroFornecedoresDAO.class);

	public CadastroFornecedoresDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Insere dados do Fornecedor no banco de dados
	 * 
	 * @param fornecedores
	 * @return Boolean
	 */
	public boolean insere(Fornecedores fornecedores) {

		String sql = "INSERT INTO FORNECEDORES (NOME, CNPJ, INSCRICAO_ESTADUAL, TELEFONE, CELULAR, FAX, CEP, ENDERECO, NUMERO, BAIRRO, CIDADE, UF, EMAIL, CONTATO, OBSERVACOES, DATA) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, fornecedores.nomeProperty().get());
			stmt.setLong(2, fornecedores.cnpjProperty().get());
			stmt.setLong(3, fornecedores.ieProperty().get());
			stmt.setLong(4, fornecedores.telefoneProperty().get());
			stmt.setLong(5, fornecedores.celularProperty().get());
			stmt.setLong(6, fornecedores.faxProperty().get());
			stmt.setInt(7, fornecedores.cepProperty().get());
			stmt.setString(8, fornecedores.enderecoProperty().get());
			stmt.setInt(9, fornecedores.numeroProperty().get());
			stmt.setString(10, fornecedores.bairroProperty().get());
			stmt.setString(11, fornecedores.cidadeProperty().get());
			stmt.setString(12, fornecedores.ufProperty().get());
			stmt.setString(13, fornecedores.emailProperty().get());
			stmt.setString(14, fornecedores.contatoProperty().get());
			stmt.setString(15, fornecedores.observacoesProperty().get());
			stmt.setString(16, Data.getDataAtual());

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
	 * Realiza o update dos dados modificados do Fornecedor
	 * 
	 * @param codigo
	 * @param nome
	 * @param cnpj
	 * @param ie
	 * @param telefone
	 * @param celular
	 * @param fax
	 * @param cep
	 * @param endereco
	 * @param numero
	 * @param bairro
	 * @param cidade
	 * @param uf
	 * @param email
	 * @param contato
	 * @param observacoes
	 * @return Boolean
	 */
	public boolean update(int codigo, String nome, long cnpj, long ie, long telefone, long celular, long fax, int cep, String endereco, int numero, String bairro, String cidade, String uf,
			String email, String contato, String observacoes) {

		String sql = "UPDATE FORNECEDORES SET NOME = ?, CNPJ = ?, INSCRICAO_ESTADUAL = ?, TELEFONE = ?, CELULAR = ?, FAX = ?, CEP = ?, "
				+ "ENDERECO = ?, NUMERO = ?, BAIRRO = ?, CIDADE = ?, UF = ?, EMAIL = ?, CONTATO = ?, OBSERVACOES = ? WHERE COD_FORNECEDOR = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nome);
			stmt.setLong(2, cnpj);
			stmt.setLong(3, ie);
			stmt.setLong(4, telefone);
			stmt.setLong(5, celular);
			stmt.setLong(6, fax);
			stmt.setInt(7, cep);
			stmt.setString(8, endereco);
			stmt.setInt(9, numero);
			stmt.setString(10, bairro);
			stmt.setString(11, cidade);
			stmt.setString(12, uf);
			stmt.setString(13, email);
			stmt.setString(14, contato);
			stmt.setString(15, observacoes);
			stmt.setInt(16, codigo);

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
	 * Realiza uma consulta de UF's da tabela Estados
	 * 
	 * @return ObservableList de UF's
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
