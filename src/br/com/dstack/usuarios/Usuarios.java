package br.com.dstack.usuarios;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.usuarios.cadastro.CadastroUsuariosDAO;
import br.com.dstack.usuarios.consulta.UsuariosDAO;
import br.com.dstack.utils.BCrypt;
import br.com.dstack.utils.DbConn;
import br.com.dstack.utils.SaltParaSenha;

public class Usuarios {

	private SimpleIntegerProperty codigo;
	private SimpleStringProperty usuario;
	private SimpleStringProperty nomeCompleto;
	private SimpleStringProperty senha;
	private SimpleStringProperty email;
	private SimpleStringProperty situacao;
	private SimpleStringProperty perfil;

	private static Logger logger = LogManager.getLogger(Usuarios.class);

	public Usuarios() {

	}

	public Usuarios(int codigo) {
		this.codigo = new SimpleIntegerProperty(codigo);
	}

	/**
	 * Construtor que recebe os dados para cadastro e realiza o hash da senha
	 */
	public Usuarios(String usuario, String nomeCompleto, String senha, String email, String situacao, String perfil) {
		this.usuario = new SimpleStringProperty(usuario);
		this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
		this.senha = new SimpleStringProperty(BCrypt.hashpw(senha, SaltParaSenha.getSalt()));
		this.email = new SimpleStringProperty(email);
		this.situacao = new SimpleStringProperty(situacao);
		this.perfil = new SimpleStringProperty(perfil);
	}

	public Usuarios(int codigo, String usuario, String nomeCompleto, String senha, String email, String situacao, String perfil) {
		this.codigo = new SimpleIntegerProperty(codigo);
		this.usuario = new SimpleStringProperty(usuario);
		this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
		this.senha = new SimpleStringProperty(senha);
		this.email = new SimpleStringProperty(email);
		this.situacao = new SimpleStringProperty(situacao);
		this.perfil = new SimpleStringProperty(perfil);
	}

	public SimpleIntegerProperty codigoProperty() {
		return codigo;
	}

	public SimpleStringProperty usuarioProperty() {
		return usuario;
	}

	public SimpleStringProperty nomeCompletoProperty() {
		return nomeCompleto;
	}

	public SimpleStringProperty senhaProperty() {
		return senha;
	}

	public SimpleStringProperty emailProperty() {
		return email;
	}

	public SimpleStringProperty situacaoProperty() {
		return situacao;
	}

	public SimpleStringProperty perfilProperty() {
		return perfil;
	}

	/**
	 * Faz a conexão com banco de dados e pede para inserir
	 * 
	 * @return Boolean
	 */
	public boolean insere() {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroUsuariosDAO dao = new CadastroUsuariosDAO(conn);

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
	 * @return ObservableList de Usuarios
	 */
	public ObservableList<Usuarios> consulta() {
		ObservableList<Usuarios> listaConsulta = FXCollections.observableArrayList();

		try (Connection conn = new DbConn().getConnection()) {
			listaConsulta = new UsuariosDAO(conn).consulta();
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
			UsuariosDAO dao = new UsuariosDAO(conn);

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
	 * @param usuario
	 * @param nomeCompleto
	 * @param senha
	 * @param email
	 * @param situacao
	 * @param perfil
	 * @return Boolean
	 */
	public boolean altera(int codigo, String usuario, String nomeCompleto, String senha, String email, String situacao, String perfil) {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroUsuariosDAO dao = new CadastroUsuariosDAO(conn);

			if (dao.update(codigo, usuario, nomeCompleto, BCrypt.hashpw(senha, SaltParaSenha.getSalt()), email, situacao, perfil)) {
				return true;
			}
		}
		catch (SQLException e4) {
			logger.log(Level.TRACE, "SQLException", e4);
		}
		return false;
	}
}
