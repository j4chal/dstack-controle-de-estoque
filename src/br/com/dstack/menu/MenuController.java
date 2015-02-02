package br.com.dstack.menu;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.clientes.cadastro.CadastroClientesController;
import br.com.dstack.clientes.consulta.ClientesController;
import br.com.dstack.fornecedores.cadastro.CadastroFornecedoresController;
import br.com.dstack.fornecedores.consulta.FornecedoresController;
import br.com.dstack.login.Login;
import br.com.dstack.login.LoginController;
import br.com.dstack.menu.alterarsenha.AlterarSenhaController;
import br.com.dstack.produtos.cadastro.CadastroProdutosController;
import br.com.dstack.produtos.consulta.ProdutosController;
import br.com.dstack.relatorios.RelatoriosController;
import br.com.dstack.usuarios.cadastro.CadastroUsuariosController;
import br.com.dstack.usuarios.consulta.UsuariosController;
import br.com.dstack.utils.Data;
import br.com.dstack.utils.Horario;
import br.com.dstack.utils.Versao;

public class MenuController {

	/**
	 * Componentes injetados
	 */
	@FXML
	private MenuItem mnuSair;
	@FXML
	private MenuItem mnuSobre;
	@FXML
	private MenuItem mnuCadastroUsuario;
	@FXML
	private MenuItem mnuCadastroCliente;
	@FXML
	private MenuItem mnuCadastroFornecedor;
	@FXML
	private MenuItem mnuCadastroProduto;
	@FXML
	private MenuItem mnuConsultaUsuarios;
	@FXML
	private MenuItem mnuConsultaClientes;
	@FXML
	private MenuItem mnuConsultaFornecedores;
	@FXML
	private MenuItem mnuConsultaProdutos;
	@FXML
	private MenuItem mnuLogOff;
	@FXML
	private MenuItem mnuAlterarSenha;
	@FXML
	private MenuItem mnuRelatorios;

	@FXML
	private Button btnProdutos;
	@FXML
	private Button btnClientes;
	@FXML
	private Button btnFornecedores;
	@FXML
	private Button btnUsuarios;
	@FXML
	private Button btnRelatorios;

	@FXML
	private StackPane skpTelaPrincipal;

	@FXML
	private Label lblUsuarioLogged;
	@FXML
	private Label lblHorarioAtual;
	@FXML
	private Label lblDataAtual;
	@FXML
	private ImageView imgLogoCentro;

	private Stage stage = new Stage();

	private static Logger logger = LogManager.getLogger(MenuController.class);

	public MenuController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			fxmlLoader.setController(this);
			fxmlLoader.load();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	public MenuController(String coloqueNull) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			fxmlLoader.setController(this);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			this.stage.setScene(scene);
			this.stage.setTitle("Menu | DStack - Controle de Estoque");
			this.stage.getIcons().add(new Image("icon.jpg"));
			this.stage.show();
		}
		catch (IOException e2) {
			logger.log(Level.TRACE, "IOException", e2);
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Método que inicializa junto com a classe
	 */
	@FXML
	public void initialize() {
		imgLogoCentro.setImage(new Image(ClassLoader.getSystemResourceAsStream("logo.png")));
		setUsuarioLoggedComVersao();
		perfilUsuarioVendedor();
		setDataEHorario();
		cssNosBotoes();
	}

	/**
	 * MenuItem que abre a janela de Sobre o software
	 */
	@FXML
	private void mnuSobreAction() {
		try {
			Parent fxmlParent = FXMLLoader.load(getClass().getResource("SobreView.fxml"));
			Scene scene = new Scene(fxmlParent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Sobre | DStack - Controle de Estoque");
			stage.getIcons().add(new Image("icon.jpg"));
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		}
		catch (IOException e3) {
			logger.log(Level.TRACE, "IOException", e3);
		}
	}

	/**
	 * MenuItem que abre a janela para mudar senha do usuario atual
	 */
	@FXML
	private void mnuAlterarSenhaAction() {
		new AlterarSenhaController();
	}

	/**
	 * Botão que chama tela de consulta de usuarios
	 */
	@FXML
	private void btnUsuariosAction() {
		setTelaUsuarioConsulta();
	}

	/**
	 * Botão que chama tela de consulta de clientes
	 */
	@FXML
	private void btnClientesAction() {
		setTelaClientesConsulta();
	}

	/**
	 * Botão que chama tela de consulta de produtos
	 */
	@FXML
	public void btnProdutosAction() {
		setTelaProdutosConsulta();
	}

	/**
	 * Botão que chama tela de consulta de fornecedores
	 */
	@FXML
	private void btnFornecedoresAction() {
		setTelaFornecedoresConsulta();
	}

	/**
	 * Botão que chama o método que seta a tela de relatorios
	 */
	@FXML
	private void btnRelatoriosAction() {
		setTelaRelatorios();
	}

	/**
	 * MenuItem para fazer log off do usuario atual
	 */
	@FXML
	private void mnuLogOffAction() {
		this.stage.close();
		new LoginController(stage);
	}

	/**
	 * MenuItem para fechar o aplicativo
	 */
	@FXML
	private void mnuSairAction() {
		Platform.exit();
	}

	/**
	 * MenuItem para chamar tela de consulta de usuarios
	 */
	@FXML
	private void mnuConsultaUsuariosAction() {
		setTelaUsuarioConsulta();
	}

	/**
	 * MenuItem para setar tela de consulta de usuarios e abrir janela de cadastro de usuarios
	 */
	@FXML
	private void mnuCadastroUsuarioAction() {
		setTelaUsuarioConsulta();
		abreCadastroUsuarios();
	}

	/**
	 * MenuItem que chama o método que seta tela de consulta fornecedores e abre janela de cadastro de fornecedores
	 */
	@FXML
	private void mnuCadastroFornecedorAction() {
		abreCadastroFornecedores();
	}

	/**
	 * MenuItem que chama tela de consulta de fornecedores
	 */
	@FXML
	private void mnuConsultaFornecedoresAction() {
		setTelaFornecedoresConsulta();
	}

	/**
	 * MenuItem que chama o método que seta tela de consulta clientes e abre janela de cadastro de clientes
	 */
	@FXML
	private void mnuCadastroClienteAction() {
		abreCadastroClientes();
	}

	/**
	 * MenuItem que chama tela de consulta de clientes
	 */
	@FXML
	private void mnuConsultaClientesAction() {
		setTelaClientesConsulta();
	}

	/**
	 * MenuItem para abrir a tela de relatórios
	 */
	@FXML
	private void mnuRelatoriosAction() {
		setTelaRelatorios();
	}

	/**
	 * MenuItem para abrir tela de consulta de produtos
	 */
	@FXML
	public void mnuConsultaProdutosAction() {
		setTelaProdutosConsulta();
	}

	/**
	 * MenuItem para abrir tela de cadastro de produtos
	 */
	@FXML
	private void mnuCadastroProdutoAction() {
		abreCadastroProdutos();
	}

	/**
	 * Método que seta CSS nos botões
	 */
	public void cssNosBotoes() {
		btnProdutos.setStyle("-fx-background-image: url('produto.png'); -fx-background-repeat: no-repeat;" + "-fx-background-position: center; -fx-background-size: 46 46; -fx-padding: -1;");
		btnFornecedores.setStyle("-fx-background-image: url('fornecedor.png'); -fx-background-repeat: no-repeat;" + "-fx-background-position: center; -fx-background-size: 46 46; -fx-padding: -1;");
		btnClientes.setStyle("-fx-background-image: url('cliente.png'); -fx-background-repeat: no-repeat;" + "-fx-background-position: center; -fx-background-size: 46 46; -fx-padding: -1;");
		btnRelatorios.setStyle("-fx-background-image: url('relatorio.png'); -fx-background-repeat: no-repeat;" + "-fx-background-position: center; -fx-background-size: 46 46; -fx-padding: -1;");
		btnUsuarios.setStyle("-fx-background-image: url('usuario.png'); -fx-background-repeat: no-repeat;" + "-fx-background-position: center; -fx-background-size: 46 46; -fx-padding: -1;");
	}

	/**
	 * Seta e mostra a data e horario atual
	 */
	public void setDataEHorario() {
		lblDataAtual.setText("  " + Data.getDataAtualComDiaSemana());
		Horario horario = new Horario();
		horario.horario(lblHorarioAtual);
	}

	/**
	 * Verifica o perfil logado e aplica as decisões setadas
	 */
	public void perfilUsuarioVendedor() {
		Login login = new Login();
		String perfil = login.getSituacaoEPerfil(Login.usuarioProperty().get()).get("Perfil");
		if (perfil.equals("Vendedor")) {
			btnUsuarios.setDisable(true);
			mnuCadastroUsuario.setDisable(true);
			mnuConsultaUsuarios.setDisable(true);
			mnuAlterarSenha.setDisable(true);
		}
	}

	/**
	 * Método que chama o objeto da janela de cadastro de usuarios
	 */
	public void abreCadastroUsuarios() {
		new CadastroUsuariosController();
	}

	/**
	 * Método que chama o objeto da janela de cadastro de produtos
	 */
	public void abreCadastroProdutos() {
		new CadastroProdutosController();
	}

	/**
	 * Método que seta a tela de consulta de usuarios como children na tela principal
	 */
	public void setTelaUsuarioConsulta() {
		skpTelaPrincipal.getChildren().setAll(new UsuariosController().getParentUsuario());
	}

	/**
	 * Método que seta o usuario logado e a versão atual do software no MenuBar da janela menu principal
	 */
	public void setUsuarioLoggedComVersao() {
		lblUsuarioLogged.setText("Usuario: " + Login.usuarioProperty().get() + " | " + "Versão: " + Versao.getVersaoAtual());
	}

	/**
	 * Método que chama o objeto da janela de cadastro de fornecedores
	 */
	public void abreCadastroFornecedores() {
		new CadastroFornecedoresController();
	}

	/**
	 * Método que seta a tela de consulta de fornecedores como children na tela principal
	 */
	public void setTelaFornecedoresConsulta() {
		skpTelaPrincipal.getChildren().setAll(new FornecedoresController().getParentFornecedores());
	}

	/**
	 * Método que seta a tela de Relatorios no stackpane da tela principal
	 */
	public void setTelaRelatorios() {
		skpTelaPrincipal.getChildren().setAll(new RelatoriosController().getParentRelatorios());
	}

	/**
	 * Método para setar a tela de consulta de clientes como children do StackPane
	 */
	public void setTelaClientesConsulta() {
		skpTelaPrincipal.getChildren().setAll(new ClientesController().getParentClientes());
	}

	/**
	 * Método que seta a tela de consulta de produtos como children do StackPane
	 */
	public void setTelaProdutosConsulta() {
		skpTelaPrincipal.getChildren().setAll(new ProdutosController().getParentClientes());
	}

	/**
	 * Método que cria um objeto que vai abrir a tela de cadastro de clientes
	 */
	public void abreCadastroClientes() {
		new CadastroClientesController();
	}
}
