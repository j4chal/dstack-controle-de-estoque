package br.com.dstack.fornecedores.consulta;

import impl.org.controlsfx.i18n.Localization;

import java.io.IOException;
import java.util.Locale;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
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

import br.com.dstack.fornecedores.Fornecedores;
import br.com.dstack.fornecedores.cadastro.CadastroFornecedoresController;
import br.com.dstack.login.Login;
import br.com.dstack.menu.MenuController;

public class FornecedoresController {

	/**
	 * Componentes injetados - @FXML
	 */
	@FXML
	private TableView<Fornecedores> tbvFornecedores;
	@FXML
	private TableColumn<Fornecedores, String> tbcNome;
	@FXML
	private TableColumn<Fornecedores, String> tbcEndereco;
	@FXML
	private TableColumn<Fornecedores, Number> tbcNumero;
	@FXML
	private TableColumn<Fornecedores, String> tbcBairro;
	@FXML
	private TableColumn<Fornecedores, String> tbcCidade;
	@FXML
	private TableColumn<Fornecedores, String> tbcUF;
	@FXML
	private TableColumn<Fornecedores, Number> tbcCEP;
	@FXML
	private TableColumn<Fornecedores, Number> tbcCNPJ;
	@FXML
	private TableColumn<Fornecedores, Number> tbcIE;
	@FXML
	private TableColumn<Fornecedores, String> tbcEmail;
	@FXML
	private TableColumn<Fornecedores, Number> tbcTelefone;
	@FXML
	private TableColumn<Fornecedores, Number> tbcCelular;
	@FXML
	private TableColumn<Fornecedores, Number> tbcFAX;
	@FXML
	private TableColumn<Fornecedores, String> tbcDataDoCadastro;
	@FXML
	private TableColumn<Fornecedores, String> tbcObservacoes;

	@FXML
	private Button btnAdicionar;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnApagar;
	@FXML
	private Button btnAtualizar;

	@FXML
	private TextField txtProcurar;

	private Fornecedores fm = new Fornecedores();
	private Parent parent;
	private static Logger logger = LogManager.getLogger(FornecedoresController.class);

	public FornecedoresController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FornecedoresView.fxml"));
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
	public Parent getParentFornecedores() {
		return this.parent;
	}

	/**
	 * Método initialize que inicia toda vez que essa classe é acessada
	 */
	@FXML
	private void initialize() {
		setAtributosEmColunas();
		listaFornecedoresCadastrados();
		perfilUsuarioVendedor();
		cssNosBotoes();
	}

	/**
	 * Seta CSS nos botões
	 */
	public void cssNosBotoes() {
		btnAdicionar.setStyle("-fx-background-image: url('add_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnEditar.setStyle("-fx-background-image: url('edit_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnApagar.setStyle("-fx-background-image: url('apagar_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 28 28;");
		btnAtualizar.setStyle("-fx-background-image: url('refresh.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 32 32;");
	}

	/**
	 * Verifica o usuario logado e seta as decisões informadas
	 */
	public void perfilUsuarioVendedor() {
		Login login = new Login();
		String perfil = login.getSituacaoEPerfil(Login.usuarioProperty().get()).get("Perfil");
		if (perfil.equals("Vendedor")) {
			btnEditar.setDisable(true);
			btnApagar.setDisable(true);
		}
	}

	/**
	 * Botão que chama o método para abrir a janela de cadastro de fornecedores
	 */
	@FXML
	private void btnAdicionarAction() {
		new MenuController().abreCadastroFornecedores();
	}

	/**
	 * Botão que chama o método para abrir a janela de cadastro de fornecedores com as informações do item selecionado
	 */
	@FXML
	private void btnEditarAction() {
		editarFornecedor();
	}

	/**
	 * Botão que chama o método de confirmação de exclusão
	 */
	@FXML
	private void btnApagarAction() {
		confirmaExclusaoDeFornecedor();
	}

	/**
	 * Botão que chama o método para listar os usuarios do banco de dados
	 */
	@FXML
	private void btnAtualizarAction() {
		listaFornecedoresCadastrados();
	}

	/**
	 * Verifica se o item não é null, caso não seja, o usuario do sistema recebe uma mensagem para confirmar a exclusão do item, senão é recebido uma mensagem para selecionar o
	 * item
	 */
	public void confirmaExclusaoDeFornecedor() {
		if (tbvFornecedores.getSelectionModel().getSelectedItem() != null) {
			Localization.setLocale(new Locale("pt", "BR"));
			Action response = Dialogs.create().title("Confirmação").message("Tem certeza que deseja apagar esse fornecedor?").actions(Dialog.Actions.YES, Dialog.Actions.NO).showConfirm();
			if (response == Dialog.Actions.YES) {
				remove();
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Listar e filtrar os items da tableview pelo nome do usuario
	 */
	public void listaFornecedoresCadastrados() {
		FilteredList<Fornecedores> filteredData = new FilteredList<>(fm.consulta(), user -> true);

		filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> um -> um.nomeProperty().getValue().contains(txtProcurar.getText()), txtProcurar.textProperty()));

		SortedList<Fornecedores> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tbvFornecedores.comparatorProperty());
		tbvFornecedores.setItems(sortedData);
	}

	/**
	 * Seta os atributos em suas respectivas TableColumns
	 */
	public void setAtributosEmColunas() {
		tbcNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		tbcEndereco.setCellValueFactory(cellData -> cellData.getValue().enderecoProperty());
		tbcNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
		tbcBairro.setCellValueFactory(cellData -> cellData.getValue().bairroProperty());
		tbcCidade.setCellValueFactory(cellData -> cellData.getValue().cidadeProperty());
		tbcUF.setCellValueFactory(cellData -> cellData.getValue().ufProperty());
		tbcCEP.setCellValueFactory(cellData -> cellData.getValue().cepProperty());
		tbcCNPJ.setCellValueFactory(cellData -> cellData.getValue().cnpjProperty());
		tbcIE.setCellValueFactory(cellData -> cellData.getValue().ieProperty());
		tbcEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		tbcTelefone.setCellValueFactory(cellData -> cellData.getValue().telefoneProperty());
		tbcCelular.setCellValueFactory(cellData -> cellData.getValue().celularProperty());
		tbcFAX.setCellValueFactory(cellData -> cellData.getValue().faxProperty());
		tbcDataDoCadastro.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
		tbcObservacoes.setCellValueFactory(cellData -> cellData.getValue().observacoesProperty());
	}

	/**
	 * Passa os dados do item selecionado para o método setValores
	 */
	public void editarFornecedor() {
		int selecionado = tbvFornecedores.getSelectionModel().getSelectedIndex();

		if (tbvFornecedores.getSelectionModel().getSelectedItem() != null) {
			if (selecionado >= 0) {
				String codigo = tbvFornecedores.getSelectionModel().getSelectedItem().codigoProperty().get();
				String nome = tbvFornecedores.getSelectionModel().getSelectedItem().nomeProperty().get();
				String endereco = tbvFornecedores.getSelectionModel().getSelectedItem().enderecoProperty().get();
				int numero = tbvFornecedores.getSelectionModel().getSelectedItem().numeroProperty().get();
				String bairro = tbvFornecedores.getSelectionModel().getSelectedItem().bairroProperty().get();
				String cidade = tbvFornecedores.getSelectionModel().getSelectedItem().cidadeProperty().get();
				String uf = tbvFornecedores.getSelectionModel().getSelectedItem().ufProperty().get();
				int cep = tbvFornecedores.getSelectionModel().getSelectedItem().cepProperty().get();
				long cnpj = tbvFornecedores.getSelectionModel().getSelectedItem().cnpjProperty().get();
				long ie = tbvFornecedores.getSelectionModel().getSelectedItem().ieProperty().get();
				String email = tbvFornecedores.getSelectionModel().getSelectedItem().emailProperty().get();
				long telefone = tbvFornecedores.getSelectionModel().getSelectedItem().telefoneProperty().get();
				long celular = tbvFornecedores.getSelectionModel().getSelectedItem().celularProperty().get();
				long fax = tbvFornecedores.getSelectionModel().getSelectedItem().faxProperty().get();
				String contato = tbvFornecedores.getSelectionModel().getSelectedItem().contatoProperty().get();
				String observacoes = tbvFornecedores.getSelectionModel().getSelectedItem().observacoesProperty().get();

				CadastroFornecedoresController cfc = new CadastroFornecedoresController();
				cfc.setValores(codigo, nome, endereco, numero, bairro, cidade, uf, cep, cnpj, ie, email, telefone, celular, fax, contato, observacoes);
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Remove o fornecedor selecionado na tableview e atualiza a lista
	 */
	public void remove() {
		int selecionado = tbvFornecedores.getSelectionModel().getSelectedIndex();
		StringProperty itemSelecionado = tbvFornecedores.getSelectionModel().getSelectedItem().codigoProperty();

		if (selecionado >= 0) {
			fm.delete(itemSelecionado.get());
			listaFornecedoresCadastrados();
		}
		else {
			Dialogs.create().title("Selecione").message("Selecione um fornecedor da tabela!").showWarning();
		}
	}
}
