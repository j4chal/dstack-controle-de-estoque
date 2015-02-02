package br.com.dstack.fornecedores;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.fornecedores.cadastro.CadastroFornecedoresDAO;
import br.com.dstack.fornecedores.consulta.FornecedoresDAO;
import br.com.dstack.utils.DbConn;

public class Fornecedores {

	private SimpleStringProperty codigo;
	private SimpleStringProperty nome;
	private SimpleLongProperty cnpj;
	private SimpleLongProperty ie;
	private SimpleLongProperty telefone;
	private SimpleLongProperty celular;
	private SimpleLongProperty fax;
	private SimpleIntegerProperty cep;
	private SimpleStringProperty endereco;
	private SimpleIntegerProperty numero;
	private SimpleStringProperty bairro;
	private SimpleStringProperty cidade;
	private SimpleStringProperty uf;
	private SimpleStringProperty email;
	private SimpleStringProperty contato;
	private SimpleStringProperty observacoes;
	private SimpleStringProperty data;

	private static Logger logger = LogManager.getLogger(Fornecedores.class);

	public Fornecedores() {

	}

	public Fornecedores(String codigo) {
		this.codigo = new SimpleStringProperty(codigo);
	}

	public Fornecedores(String codigo, String nome, String cnpj, String ie, String telefone, String celular, String fax, String cep, String endereco, String numero, String bairro, String cidade,
			String uf, String email, String contato, String observacoes, String data) {
		this.codigo = new SimpleStringProperty(codigo);
		this.nome = new SimpleStringProperty(nome);
		this.cnpj = new SimpleLongProperty(Long.parseLong(cnpj));
		this.ie = new SimpleLongProperty(Long.parseLong(ie));
		this.telefone = new SimpleLongProperty(Long.parseLong(telefone));
		this.celular = new SimpleLongProperty(Long.parseLong(celular));
		this.fax = new SimpleLongProperty(Long.parseLong(fax));
		this.cep = new SimpleIntegerProperty(Integer.parseInt(cep));
		this.endereco = new SimpleStringProperty(endereco);
		this.numero = new SimpleIntegerProperty(Integer.parseInt(numero));
		this.bairro = new SimpleStringProperty(bairro);
		this.cidade = new SimpleStringProperty(cidade);
		this.uf = new SimpleStringProperty(uf);
		this.email = new SimpleStringProperty(email);
		this.contato = new SimpleStringProperty(contato);
		this.observacoes = new SimpleStringProperty(observacoes);
		this.data = new SimpleStringProperty(data);
	}

	public StringProperty codigoProperty() {
		return codigo;
	}

	public StringProperty nomeProperty() {
		return nome;
	}

	public LongProperty cnpjProperty() {
		return cnpj;
	}

	public LongProperty ieProperty() {
		return ie;
	}

	public LongProperty telefoneProperty() {
		return telefone;
	}

	public LongProperty celularProperty() {
		return celular;
	}

	public LongProperty faxProperty() {
		return fax;
	}

	public IntegerProperty cepProperty() {
		return cep;
	}

	public StringProperty enderecoProperty() {
		return endereco;
	}

	public IntegerProperty numeroProperty() {
		return numero;
	}

	public StringProperty bairroProperty() {
		return bairro;
	}

	public StringProperty cidadeProperty() {
		return cidade;
	}

	public StringProperty ufProperty() {
		return uf;
	}

	public void setUfProperty(String uf) {
		this.uf.set(uf);
	}

	public StringProperty emailProperty() {
		return email;
	}

	public StringProperty contatoProperty() {
		return contato;
	}

	public StringProperty observacoesProperty() {
		return observacoes;
	}

	public StringProperty dataProperty() {
		return data;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 * 
	 * @return Boolean
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroFornecedoresDAO dao = new CadastroFornecedoresDAO(conn);

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
	 * @return ObservableList de Fornecedores
	 */
	public ObservableList<Fornecedores> consulta() {
		ObservableList<Fornecedores> listaConsulta = FXCollections.observableArrayList();

		try (Connection conn = new DbConn().getConnection()) {
			listaConsulta = new FornecedoresDAO(conn).consulta();
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
	public boolean delete(String itemSelecionado) {
		try (Connection conn = new DbConn().getConnection()) {
			FornecedoresDAO dao = new FornecedoresDAO(conn);

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
	public boolean altera(int codigo, String nome, long cnpj, long ie, long telefone, long celular, long fax, int cep, String endereco, int numero, String bairro, String cidade, String uf,
			String email, String contato, String observacoes) {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroFornecedoresDAO dao = new CadastroFornecedoresDAO(conn);

			if (dao.update(codigo, nome, cnpj, ie, telefone, celular, fax, cep, endereco, numero, bairro, cidade, uf, email, contato, observacoes)) {
				return true;
			}
		}
		catch (SQLException e4) {
			logger.log(Level.TRACE, "SQLException", e4);
		}
		return false;
	}

	/**
	 * Faz a conexão com banco de dados, e recebe uma ObservableList com todos campos UF da tabela Estado
	 * 
	 * @return ObservableList de UF's
	 */
	public ObservableList<String> consultaDeUf() {
		ObservableList<String> listaConsulta = FXCollections.observableArrayList();

		try (Connection conn = new DbConn().getConnection()) {
			listaConsulta = new CadastroFornecedoresDAO(conn).consultaDeUf();
		}
		catch (SQLException e5) {
			logger.log(Level.TRACE, "SQLException", e5);
		}
		return listaConsulta;
	}
}
