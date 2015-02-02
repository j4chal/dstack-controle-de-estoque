package br.com.dstack.usuarios.consulta;

import impl.org.controlsfx.i18n.Localization;

import java.io.IOException;
import java.util.Locale;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import br.com.dstack.menu.MenuController;
import br.com.dstack.usuarios.Usuarios;
import br.com.dstack.usuarios.cadastro.CadastroUsuariosController;

public class UsuariosController {

	/*
	 * Componentes injetados
	 */
	@FXML
	private TableView<Usuarios> tbvConsulta;
	@FXML
	private TableColumn<Usuarios, Number> tbcCodigo;
	@FXML
	private TableColumn<Usuarios, String> tbcNomeCompleto;
	@FXML
	private TableColumn<Usuarios, String> tbcEmail;
	@FXML
	private TableColumn<Usuarios, String> tbcUsuario;
	@FXML
	private TableColumn<Usuarios, String> tbcSenha;
	@FXML
	private TableColumn<Usuarios, String> tbcPerfil;
	@FXML
	private TableColumn<Usuarios, String> tbcSituacao;

	@FXML
	private Button btnAdiciona;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnExcluir;
	@FXML
	private Button btnAtualizarLista;

	@FXML
	private TextField txtProcurar;

	private Usuarios um = new Usuarios();
	private Parent parent;
	private static Logger logger = LogManager.getLogger(UsuariosController.class);

	public UsuariosController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UsuariosView.fxml"));
			fxmlLoader.setController(this);
			parent = fxmlLoader.load();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	/**
	 * Retorna o parent do FXML
	 * 
	 * @return Parent
	 */
	public Parent getParentUsuario() {
		return this.parent;
	}

	/**
	 * Método initialize que inicia toda vez que essa classe é acessada
	 */
	@FXML
	private void initialize() {
		setaAtributosEmColunas();
		listaDeUsuariosComFiltro();
		cssNosBotoes();
	}

	/**
	 * Aplica css nos botões
	 */
	public void cssNosBotoes() {
		btnAdiciona.setStyle("-fx-background-image: url('new.png'); -fx-background-repeat: no-repeat; -fx-background-position: top;");
		btnEditar.setStyle("-fx-background-image: url('edit.png'); -fx-background-repeat: no-repeat; -fx-background-position: top;");
		btnExcluir.setStyle("-fx-background-image: url('delete.png'); -fx-background-repeat: no-repeat; -fx-background-position: top;");
		btnAtualizarLista.setStyle("-fx-background-image: url('refresh.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 20 20;");
	}

	/**
	 * Botão que chama o método para listar os usuarios do banco de dados
	 */
	@FXML
	public void btnAtualizarListaAction() {
		listaDeUsuariosComFiltro();
	}

	/**
	 * Listar e filtrar os items da tableview pelo nome do usuario
	 */
	public void listaDeUsuariosComFiltro() {
		FilteredList<Usuarios> filteredData = new FilteredList<>(um.consulta(), user -> true);

		filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> um -> um.nomeCompletoProperty().getValue().contains(txtProcurar.getText()), txtProcurar.textProperty()));

		SortedList<Usuarios> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tbvConsulta.comparatorProperty());
		tbvConsulta.setItems(sortedData);
	}

	/**
	 * Seta os atributos em suas respectivas TableColumns
	 */
	public void setaAtributosEmColunas() {
		tbcNomeCompleto.setCellValueFactory(cellData -> cellData.getValue().nomeCompletoProperty());
		tbcEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		tbcUsuario.setCellValueFactory(cellData -> cellData.getValue().usuarioProperty());
		tbcSenha.setCellValueFactory(cellData -> cellData.getValue().senhaProperty());
		tbcPerfil.setCellValueFactory(cellData -> cellData.getValue().perfilProperty());
		tbcSituacao.setCellValueFactory(cellData -> cellData.getValue().situacaoProperty());
	}

	/**
	 * Botão que abre a janela de cadastro de usuarios
	 */
	@FXML
	private void btnAdicionaAction() {
		new MenuController().abreCadastroUsuarios();
	}

	/**
	 * Botão que abre a jenal de cadastro de usuarios com os atributos do item selecionado
	 */
	@FXML
	private void btnEditarAction() {
		editarUsuario();
	}

	/**
	 * Botão para excluir item selecionado
	 */
	@FXML
	private void btnExcluirAction() {
		confirmaExclusaoDeUsuario();
	}

	/**
	 * Verifica se o item não é null, caso não seja, o usuario do sistema recebe uma mensagem para confirmar a exclusão do item, senão é recebido uma mensagem para selecionar o
	 * item
	 */
	public void confirmaExclusaoDeUsuario() {
		if (tbvConsulta.getSelectionModel().getSelectedItem() != null) {
			Localization.setLocale(new Locale("pt", "BR"));
			Action response = Dialogs.create().title("Confirmação").message("Tem certeza que deseja remover esse usuario?").actions(Dialog.Actions.YES, Dialog.Actions.NO).showConfirm();
			if (response == Dialog.Actions.YES) {
				remove();
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Passa os dados do item selecionado para o método setValores
	 */
	public void editarUsuario() {
		int selecionado = tbvConsulta.getSelectionModel().getSelectedIndex();

		if (tbvConsulta.getSelectionModel().getSelectedItem() != null) {
			if (selecionado >= 0) {
				int codigo = tbvConsulta.getSelectionModel().getSelectedItem().codigoProperty().get();
				String nomeCompleto = tbvConsulta.getSelectionModel().getSelectedItem().nomeCompletoProperty().get();
				String email = tbvConsulta.getSelectionModel().getSelectedItem().emailProperty().get();
				String usuario = tbvConsulta.getSelectionModel().getSelectedItem().usuarioProperty().get();
				String senha = tbvConsulta.getSelectionModel().getSelectedItem().senhaProperty().get();
				String perfil = tbvConsulta.getSelectionModel().getSelectedItem().perfilProperty().get();
				String situacao = tbvConsulta.getSelectionModel().getSelectedItem().situacaoProperty().get();

				CadastroUsuariosController cc2 = new CadastroUsuariosController();
				try {
					cc2.setValores(codigo, nomeCompleto, email, usuario, senha, perfil, situacao);
				}
				catch (IOException e2) {
					logger.log(Level.TRACE, "IOException", e2);
				}
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Remove usuario selecionado na tableview e atualiza a lista
	 */
	public void remove() {
		int selecionado = tbvConsulta.getSelectionModel().getSelectedIndex();
		IntegerProperty itemSelecionado = tbvConsulta.getSelectionModel().getSelectedItem().codigoProperty();

		if (selecionado >= 0) {
			um.delete(itemSelecionado.get());
			listaDeUsuariosComFiltro();
		}
		else {
			Dialogs.create().title("Selecione").message("Selecione um usuario da tabela!").showWarning();
		}
	}
}
