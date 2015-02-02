package br.com.dstack.produtos;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.produtos.cadastro.CadastroProdutosDAO;
import br.com.dstack.produtos.consulta.ProdutosDAO;
import br.com.dstack.utils.DbConn;

public class Produtos {

	private SimpleIntegerProperty codigo;
	private SimpleIntegerProperty codigoFornecedor;
	private SimpleStringProperty descricao;
	private InputStream inputImage;
	private File file;
	private SimpleStringProperty unidadeMedida;
	private SimpleIntegerProperty estoqueAtual;
	private SimpleDoubleProperty preco;
	private SimpleIntegerProperty estoqueMinimo;
	private SimpleIntegerProperty estoqueMaximo;
	private SimpleStringProperty observacoes;
	private SimpleIntegerProperty codigoCategoria;
	private SimpleIntegerProperty codigoMarca;
	private SimpleStringProperty data;
	private SimpleStringProperty nomeFornecedor;
	private SimpleStringProperty descricaoCategoria;
	private SimpleStringProperty descricaoMarca;

	private static Logger logger = LogManager.getLogger(Produtos.class);

	public Produtos() {

	}

	public Produtos(int codigo) {
		this.codigo = new SimpleIntegerProperty(codigo);
	}

	public Produtos(File file) {
		this.file = file;
	}

	// Inserir
	public Produtos(String descricao, File file, int codigoFornecedor, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo, String observacoes,
			int codigoCategoria, int codigoMarca) {
		this.descricao = new SimpleStringProperty(descricao);
		this.file = file;
		this.codigoFornecedor = new SimpleIntegerProperty(codigoFornecedor);
		this.unidadeMedida = new SimpleStringProperty(unidadeMedida);
		this.estoqueAtual = new SimpleIntegerProperty(estoqueAtual);
		this.preco = new SimpleDoubleProperty(preco);
		this.estoqueMinimo = new SimpleIntegerProperty(estoqueMinimo);
		this.estoqueMaximo = new SimpleIntegerProperty(estoqueMaximo);
		this.observacoes = new SimpleStringProperty(observacoes);
		this.codigoCategoria = new SimpleIntegerProperty(codigoCategoria);
		this.codigoMarca = new SimpleIntegerProperty(codigoMarca);
	}

	// Alterar
	public Produtos(int codigo, String descricao, File file, int codigoFornecedor, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo, String observacoes,
			int codigoCategoria, int codigoMarca) {
		this.codigo = new SimpleIntegerProperty(codigo);
		this.descricao = new SimpleStringProperty(descricao);
		this.file = file;
		this.codigoFornecedor = new SimpleIntegerProperty(codigoFornecedor);
		this.unidadeMedida = new SimpleStringProperty(unidadeMedida);
		this.estoqueAtual = new SimpleIntegerProperty(estoqueAtual);
		this.preco = new SimpleDoubleProperty(preco);
		this.estoqueMinimo = new SimpleIntegerProperty(estoqueMinimo);
		this.estoqueMaximo = new SimpleIntegerProperty(estoqueMaximo);
		this.observacoes = new SimpleStringProperty(observacoes);
		this.codigoCategoria = new SimpleIntegerProperty(codigoCategoria);
		this.codigoMarca = new SimpleIntegerProperty(codigoMarca);
	}

	// Consulta
	public Produtos(int codigo, String nomeFornecedor, String descricao, InputStream inputImage, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo,
			String observacoes, String descricaoCategoria, String descricaoMarca, String data, int codigoFornecedor, int codigoCategoria, int codigoMarca) {
		this.codigo = new SimpleIntegerProperty(codigo);
		this.nomeFornecedor = new SimpleStringProperty(nomeFornecedor);
		this.descricao = new SimpleStringProperty(descricao);
		this.inputImage = inputImage;
		this.unidadeMedida = new SimpleStringProperty(unidadeMedida);
		this.estoqueAtual = new SimpleIntegerProperty(estoqueAtual);
		this.preco = new SimpleDoubleProperty(preco);
		this.estoqueMinimo = new SimpleIntegerProperty(estoqueMinimo);
		this.estoqueMaximo = new SimpleIntegerProperty(estoqueMaximo);
		this.observacoes = new SimpleStringProperty(observacoes);
		this.descricaoCategoria = new SimpleStringProperty(descricaoCategoria);
		this.descricaoMarca = new SimpleStringProperty(descricaoMarca);
		this.data = new SimpleStringProperty(data);
		this.codigoFornecedor = new SimpleIntegerProperty(codigoFornecedor);
		this.codigoCategoria = new SimpleIntegerProperty(codigoCategoria);
		this.codigoMarca = new SimpleIntegerProperty(codigoMarca);
	}

	public IntegerProperty codigoProperty() {
		return codigo;
	}

	public IntegerProperty codigoFornecedorProperty() {
		return codigoFornecedor;
	}

	public StringProperty descricaoProperty() {
		return descricao;
	}

	public StringProperty unidadeMedidaProperty() {
		return unidadeMedida;
	}

	public IntegerProperty estoqueAtualProperty() {
		return estoqueAtual;
	}

	public DoubleProperty precoProperty() {
		return preco;
	}

	public IntegerProperty estoqueMinimoProperty() {
		return estoqueMinimo;
	}

	public IntegerProperty estoqueMaximoProperty() {
		return estoqueMaximo;
	}

	public StringProperty observacoesProperty() {
		return observacoes;
	}

	public IntegerProperty codigoCategoriaProperty() {
		return codigoCategoria;
	}

	public IntegerProperty codigoMarcaProperty() {
		return codigoMarca;
	}

	public StringProperty dataProperty() {
		return data;
	}

	public StringProperty nomeFornecedorProperty() {
		return nomeFornecedor;
	}

	public StringProperty descricaoCategoriaProperty() {
		return descricaoCategoria;
	}

	public StringProperty descricaoMarcaProperty() {
		return descricaoMarca;
	}

	public InputStream getInputImage() {
		return this.inputImage;
	}

	public File getFile() {
		return this.file;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 * 
	 * @return Boolean
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroProdutosDAO dao = new CadastroProdutosDAO(conn);

			if (dao.insere(this)) {
				return true;
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		return false;
	}

	/**
	 * Faz a conexão com banco de dados e retorna uma ObservableList com os dados da tabela
	 * 
	 * @return ObservableList de Produtos
	 */
	public ObservableList<Produtos> consulta() {
		ObservableList<Produtos> listaConsulta = FXCollections.observableArrayList();

		try (Connection conn = new DbConn().getConnection()) {
			listaConsulta = new ProdutosDAO(conn).consulta();
		}
		catch (SQLException e2) {
			logger.log(Level.TRACE, "SQLException", e2);
		}
		return listaConsulta;
	}

	/**
	 * Faz a conexão com banco de dados, dado o id, é solicitado a remoção do registro
	 * 
	 * @param itemSelecionado
	 * @return Boolean
	 */
	public boolean delete(int itemSelecionado) {
		try (Connection conn = new DbConn().getConnection()) {
			ProdutosDAO dao = new ProdutosDAO(conn);

			if (dao.deletePorId(itemSelecionado)) {
				return true;
			}
		}
		catch (SQLException e3) {
			logger.log(Level.TRACE, "SQLException", e3);
		}
		return false;
	}

	/**
	 * Faz a conexão com banco de dados, dado os atributos que foram modificados, alteração é feita na tabela
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
	public boolean altera(int codigo, String descricao, InputStream file, int codigoFornecedor, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo,
			String observacoes, int codigoCategoria, int codigoMarca) {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroProdutosDAO dao = new CadastroProdutosDAO(conn);

			if (dao.update(codigo, descricao, file, codigoFornecedor, unidadeMedida, estoqueAtual, preco, estoqueMinimo, estoqueMaximo, observacoes, codigoCategoria, codigoMarca)) {
				return true;
			}
		}
		catch (SQLException e4) {
			logger.log(Level.TRACE, "SQLException", e4);
		}
		return false;
	}
}
