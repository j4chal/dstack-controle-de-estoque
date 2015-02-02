package br.com.dstack.fornecedores.consulta;

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

public class FornecedoresDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(FornecedoresDAO.class);

	public FornecedoresDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Retorna uma ObservableList com os fornecedores da tabela fornecedores
	 * 
	 * @return ObservableList de Fornecedores
	 */
	public ObservableList<Fornecedores> consulta() {
		ObservableList<Fornecedores> consulta = FXCollections.observableArrayList();

		String sql = "SELECT * FROM FORNECEDORES";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					String codigo = rs.getString("COD_FORNECEDOR");
					String nome = rs.getString("NOME");
					String cnpj = rs.getString("CNPJ");
					String ie = rs.getString("INSCRICAO_ESTADUAL");
					String telefone = rs.getString("TELEFONE");
					String celular = rs.getString("CELULAR");
					String fax = rs.getString("FAX");
					String cep = rs.getString("CEP");
					String endereco = rs.getString("ENDERECO");
					String numero = rs.getString("NUMERO");
					String bairro = rs.getString("BAIRRO");
					String cidade = rs.getString("CIDADE");
					String uf = rs.getString("UF");
					String email = rs.getString("EMAIL");
					String contato = rs.getString("CONTATO");
					String observacoes = rs.getString("OBSERVACOES");
					String data = rs.getString("DATA");

					consulta.add(new Fornecedores(codigo, nome, cnpj, ie, telefone, celular, fax, cep, endereco, numero, bairro, cidade, uf, email, contato, observacoes, data));
				}
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return consulta;
	}

	/**
	 * Deleta por id dado o item selecionado
	 * 
	 * @param itemSelecionado
	 * @return Boolean
	 */
	public boolean deletePorId(String itemSelecionado) {

		String sql = "DELETE FROM FORNECEDORES WHERE COD_FORNECEDOR = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(itemSelecionado));
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
