package br.com.dstack.produtos.cadastro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.produtos.Produtos;
import br.com.dstack.utils.Data;
import br.com.dstack.utils.DisplayMemberAndValueMember;

public class CadastroProdutosDAO {

	private final Connection conn;
	private static Logger logger = LogManager.getLogger(CadastroProdutosDAO.class);

	public CadastroProdutosDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Insere os dados do produto no banco de dados
	 * 
	 * @param produtos
	 * @return Boolean
	 */
	public boolean insere(Produtos produtos) {

		String sql = "INSERT INTO PRODUTOS (COD_FORNECEDOR, DESCRICAO, IMAGEM, UNIDADE_MEDIDA, ESTOQUE_ATUAL, PRECO, ESTOQUE_MINIMO, ESTOQUE_MAXIMO, OBSERVACOES, COD_CATEGORIA, COD_MARCA, DATA) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, produtos.codigoFornecedorProperty().get());
			stmt.setString(2, produtos.descricaoProperty().get());
			stmt.setBinaryStream(3, new FileInputStream(produtos.getFile()), (int) produtos.getFile().length());
			stmt.setString(4, produtos.unidadeMedidaProperty().get());
			stmt.setInt(5, produtos.estoqueAtualProperty().get());
			stmt.setDouble(6, produtos.precoProperty().get());
			stmt.setInt(7, produtos.estoqueMinimoProperty().get());
			stmt.setInt(8, produtos.estoqueMaximoProperty().get());
			stmt.setString(9, produtos.observacoesProperty().get());
			stmt.setInt(10, produtos.codigoCategoriaProperty().get());
			stmt.setInt(11, produtos.codigoMarcaProperty().get());
			stmt.setString(12, Data.getDataAtualDb());

			if (stmt.executeUpdate() > 0) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		catch (FileNotFoundException e2) {
			logger.log(Level.TRACE, "FileNotFoundException", e2);
		}
		return false;
	}

	/**
	 * Faz o update de dados do produto no banco de dados
	 * 
	 * @param codigo
	 * @param descricao
	 * @param file
	 * @param codigoFornecedor
	 * @param unidadeMedida
	 * @param estoqueAtual
	 * @param preco
	 * @param estoqueMinimo
	 * @param estoqueMaximo
	 * @param observacoes
	 * @param codigoCategoria
	 * @param codigoMarca
	 * @return Boolean
	 */
	public boolean update(int codigo, String descricao, InputStream file, int codigoFornecedor, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo,
			String observacoes, int codigoCategoria, int codigoMarca) {

		String sql = "UPDATE PRODUTOS SET COD_FORNECEDOR = ?, DESCRICAO = ?, IMAGEM = ?, UNIDADE_MEDIDA = ?, ESTOQUE_ATUAL = ?, PRECO = ?, ESTOQUE_MINIMO = ?, ESTOQUE_MAXIMO = ?, OBSERVACOES = ?, "
				+ "COD_CATEGORIA = ?, COD_MARCA = ? WHERE COD_PRODUTO = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, codigoFornecedor);
			stmt.setString(2, descricao);
			stmt.setBinaryStream(3, file, file.available());
			stmt.setString(4, unidadeMedida);
			stmt.setInt(5, estoqueAtual);
			stmt.setDouble(6, preco);
			stmt.setInt(7, estoqueMinimo);
			stmt.setInt(8, estoqueMaximo);
			stmt.setString(9, observacoes);
			stmt.setInt(10, codigoCategoria);
			stmt.setInt(11, codigoMarca);
			stmt.setInt(12, codigo);

			if (stmt.executeUpdate() > 0) {
				return true;
			}
		}
		catch (SQLException e3) {
			logger.log(Level.TRACE, "SQLException", e3);
		}
		catch (IOException e4) {
			logger.log(Level.TRACE, "IOException", e4);
		}
		return false;
	}

	/**
	 * Faz a consulta de fornecedores e retorna um ObservableMap com o nome e o codigo
	 * 
	 * @param combobox
	 * @return ObservableMap
	 * @throws SQLException
	 */
	public ObservableMap<String, String> consultaDeFornecedores(ComboBox<DisplayMemberAndValueMember> combobox) {
		ObservableMap<String, String> consulta = FXCollections.observableHashMap();

		String sql = "SELECT COD_FORNECEDOR, NOME AS NOME_FORNECEDOR FROM FORNECEDORES";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_FORNECEDOR");
					String nome = rs.getString("NOME_FORNECEDOR");

					combobox.getItems().add(new DisplayMemberAndValueMember(nome, codigo));
				}
			}
		}
		catch (SQLException e5) {
			logger.log(Level.TRACE, "SQLException", e5);
		}
		return consulta;
	}

	/**
	 * Consulta as categorias e retorna um ObservableMap com o codigo e a descrição
	 * 
	 * @param combobox
	 * @return ObservableMap de Categorias
	 * @throws SQLException
	 */
	public ObservableMap<String, String> consultaDeCategorias(ComboBox<DisplayMemberAndValueMember> combobox) {
		ObservableMap<String, String> consulta = FXCollections.observableHashMap();

		String sql = "SELECT COD_CATEGORIA, DESCRICAO FROM CATEGORIAS";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_CATEGORIA");
					String descricao = rs.getString("DESCRICAO");

					combobox.getItems().add(new DisplayMemberAndValueMember(descricao, codigo));
				}
			}
		}
		catch (SQLException e6) {
			logger.log(Level.TRACE, "SQLException", e6);
		}
		return consulta;
	}

	/**
	 * Consulta marcas no banco de dados e retorna um ObservableMap com o codigo e descrição
	 * 
	 * @param combobox
	 * @return ObservableMap de Marcas
	 * @throws SQLException
	 */
	public ObservableMap<String, String> consultaDeMarcas(ComboBox<DisplayMemberAndValueMember> combobox) {
		ObservableMap<String, String> consulta = FXCollections.observableHashMap();

		String sql = "SELECT COD_MARCA, DESCRICAO FROM MARCAS";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while (rs.next()) {
					int codigo = rs.getInt("COD_MARCA");
					String descricao = rs.getString("DESCRICAO");

					combobox.getItems().add(new DisplayMemberAndValueMember(descricao, codigo));
				}
			}
		}
		catch (SQLException e7) {
			logger.log(Level.TRACE, "SQLException", e7);
		}
		return consulta;
	}
}
