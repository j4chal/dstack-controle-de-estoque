package br.com.dstack.clientes;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.clientes.cadastro.CadastroClientesDAO;
import br.com.dstack.clientes.consulta.ClientesDAO;
import br.com.dstack.utils.DbConn;

public class Clientes {

	private SimpleIntegerProperty codigo;
	private SimpleStringProperty nome;
	private SimpleStringProperty cpf;
	private SimpleStringProperty rg;
	private SimpleStringProperty fantasia;
	private SimpleStringProperty pessoa;
	private SimpleStringProperty sexo;
	private SimpleStringProperty dataCadastro;
	private SimpleStringProperty email;
	private SimpleStringProperty dataNascimento;
	private SimpleStringProperty cep;
	private SimpleStringProperty endereco;
	private SimpleIntegerProperty numero;
	private SimpleStringProperty bairro;
	private SimpleStringProperty cidade;
	private SimpleStringProperty uf;
	private SimpleStringProperty telefone;
	private SimpleStringProperty fax;
	private SimpleStringProperty banco;
	private SimpleIntegerProperty agencia;

	private static Logger logger = LogManager.getLogger(Clientes.class);

	public Clientes() {

	}

	public Clientes(int codigo) {
		this.codigo = new SimpleIntegerProperty(codigo);
	}

	public Clientes(String nome, String cpf, String rg, String fantasia, String pessoa, String sexo, String email, String dataNascimento, String cep, String endereco, int numero,
			String bairro, String cidade, String uf, String telefone, String fax, String banco, int agencia) {
		this.nome = new SimpleStringProperty(nome);
		this.cpf = new SimpleStringProperty(cpf);
		this.rg = new SimpleStringProperty(rg);
		this.fantasia = new SimpleStringProperty(fantasia);
		this.pessoa = new SimpleStringProperty(pessoa);
		this.sexo = new SimpleStringProperty(sexo);
		this.email = new SimpleStringProperty(email);
		this.dataNascimento = new SimpleStringProperty(dataNascimento);
		this.cep = new SimpleStringProperty(cep);
		this.endereco = new SimpleStringProperty(endereco);
		this.numero = new SimpleIntegerProperty(numero);
		this.bairro = new SimpleStringProperty(bairro);
		this.cidade = new SimpleStringProperty(cidade);
		this.uf = new SimpleStringProperty(uf);
		this.telefone = new SimpleStringProperty(telefone);
		this.fax = new SimpleStringProperty(fax);
		this.banco = new SimpleStringProperty(banco);
		this.agencia = new SimpleIntegerProperty(agencia);
	}

	public Clientes(int codigo, String nome, String cpf, String rg, String fantasia, String pessoa, String sexo, String dataCadastro, String email, String dataNascimento, String cep, String endereco,
			int numero, String bairro, String cidade, String uf, String telefone, String fax, String banco, int agencia) {
		this.codigo = new SimpleIntegerProperty(codigo);
		this.nome = new SimpleStringProperty(nome);
		this.cpf = new SimpleStringProperty(cpf);
		this.rg = new SimpleStringProperty(rg);
		this.fantasia = new SimpleStringProperty(fantasia);
		this.pessoa = new SimpleStringProperty(pessoa);
		this.sexo = new SimpleStringProperty(sexo);
		this.dataCadastro = new SimpleStringProperty(dataCadastro);
		this.email = new SimpleStringProperty(email);
		this.dataNascimento = new SimpleStringProperty(dataNascimento);
		this.cep = new SimpleStringProperty(cep);
		this.endereco = new SimpleStringProperty(endereco);
		this.numero = new SimpleIntegerProperty(numero);
		this.bairro = new SimpleStringProperty(bairro);
		this.cidade = new SimpleStringProperty(cidade);
		this.uf = new SimpleStringProperty(uf);
		this.telefone = new SimpleStringProperty(telefone);
		this.fax = new SimpleStringProperty(fax);
		this.banco = new SimpleStringProperty(banco);
		this.agencia = new SimpleIntegerProperty(agencia);
	}

	public void setDataCadastroProperty(String dataCadastro) {
		this.dataCadastro.set(dataCadastro);
	}

	public IntegerProperty codigoProperty() {
		return codigo;
	}

	public StringProperty nomeProperty() {
		return nome;
	}

	public StringProperty cpfProperty() {
		return cpf;
	}

	public StringProperty rgProperty() {
		return rg;
	}

	public StringProperty fantasiaProperty() {
		return fantasia;
	}

	public StringProperty pessoaProperty() {
		return pessoa;
	}

	public StringProperty sexoProperty() {
		return sexo;
	}

	public StringProperty dataCadastroProperty() {
		return dataCadastro;
	}

	public StringProperty emailProperty() {
		return email;
	}

	public StringProperty dataNascimentoProperty() {
		return dataNascimento;
	}

	public StringProperty cepProperty() {
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

	public StringProperty telefoneProperty() {
		return telefone;
	}

	public StringProperty faxProperty() {
		return fax;
	}

	public StringProperty bancoProperty() {
		return banco;
	}

	public IntegerProperty agenciaProperty() {
		return agencia;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 * 
	 * @return Boolean
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroClientesDAO dao = new CadastroClientesDAO(conn);

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
	 * @return ObservableList de Clientes
	 */
	public ObservableList<Clientes> consulta() {
		ObservableList<Clientes> listaConsulta = FXCollections.observableArrayList();

		try (Connection conn = new DbConn().getConnection()) {
			listaConsulta = new ClientesDAO(conn).consulta();
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
			ClientesDAO dao = new ClientesDAO(conn);

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
	public boolean altera(int codigo, String nome, String cpf, String rg, String fantasia, String pessoa, String sexo, String email, String dataNascimento, String cep, String endereco, int numero,
			String bairro, String cidade, String uf, String telefone, String fax, String banco, int agencia) {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroClientesDAO dao = new CadastroClientesDAO(conn);

			if (dao.update(codigo, nome, cpf, rg, fantasia, pessoa, sexo, email, dataNascimento, cep, endereco, numero, bairro, cidade, uf, telefone, fax, banco, agencia)) {
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
			listaConsulta = new CadastroClientesDAO(conn).consultaDeUf();
		}
		catch (SQLException e5) {
			logger.log(Level.TRACE, "SQLException", e5);
		}
		return listaConsulta;
	}
}
