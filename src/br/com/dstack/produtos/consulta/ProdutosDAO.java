package br.com.dstack.produtos.consulta;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.produtos.Produtos;

public class ProdutosDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(ProdutosDAO.class);

	public ProdutosDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Retorna uma ObservableList com os produtos da tabela Produtos
	 * 
	 * @return ObservableList de Produtos
	 */
	public ObservableList<Produtos> consulta() {
		ObservableList<Produtos> consulta = FXCollections.observableArrayList();

		String sql = "SELECT PRODUTOS.COD_PRODUTO, PRODUTOS.IMAGEM, FORNECEDORES.NOME AS NOME_FORNECEDOR, PRODUTOS.DESCRICAO, PRODUTOS.UNIDADE_MEDIDA, PRODUTOS.ESTOQUE_ATUAL, PRODUTOS.PRECO, PRODUTOS.ESTOQUE_MINIMO,"
				+ "PRODUTOS.ESTOQUE_MAXIMO, PRODUTOS.OBSERVACOES, CATEGORIAS.DESCRICAO AS DESCRICAO_CATEGORIA, MARCAS.DESCRICAO AS DESCRICAO_MARCA, PRODUTOS.DATA, FORNECEDORES.COD_FORNECEDOR, CATEGORIAS.COD_CATEGORIA, MARCAS.COD_MARCA FROM PRODUTOS LEFT JOIN "
				+ "FORNECEDORES ON FORNECEDORES.COD_FORNECEDOR = PRODUTOS.COD_FORNECEDOR LEFT JOIN CATEGORIAS ON CATEGORIAS.COD_CATEGORIA = PRODUTOS.COD_CATEGORIA LEFT JOIN MARCAS ON "
				+ "MARCAS.COD_MARCA = PRODUTOS.COD_MARCA";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_PRODUTO");
					String nomeFornecedor = rs.getString("NOME_FORNECEDOR");
					String descricao = rs.getString("DESCRICAO");
					InputStream img = rs.getBinaryStream("IMAGEM");
					String unidadeMedida = rs.getString("UNIDADE_MEDIDA");
					int estoqueAtual = rs.getInt("ESTOQUE_ATUAL");
					double preco = rs.getDouble("PRECO");
					int estoqueMinimo = rs.getInt("ESTOQUE_MINIMO");
					int estoqueMaximo = rs.getInt("ESTOQUE_MAXIMO");
					String observacoes = rs.getString("OBSERVACOES");
					String descricaoCategoria = rs.getString("DESCRICAO_CATEGORIA");
					String descricaoMarca = rs.getString("DESCRICAO_MARCA");
					String data = rs.getString("DATA");
					int codigoFornecedor = rs.getInt("COD_FORNECEDOR");
					int codigoCategoria = rs.getInt("COD_CATEGORIA");
					int codigoMarca = rs.getInt("COD_MARCA");

					consulta.add(new Produtos(codigo, nomeFornecedor, descricao, img, unidadeMedida, estoqueAtual, preco, estoqueMinimo, estoqueMaximo, observacoes, descricaoCategoria,
							descricaoMarca, data, codigoFornecedor, codigoCategoria, codigoMarca));
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
	public boolean deletePorId(int itemSelecionado) {

		String sql = "DELETE FROM PRODUTOS WHERE COD_PRODUTO = ?";

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
