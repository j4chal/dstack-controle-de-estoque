package br.com.dstack.clientes.consulta;

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

import br.com.dstack.clientes.Clientes;
import br.com.dstack.clientes.cadastro.CadastroClientesController;
import br.com.dstack.login.Login;
import br.com.dstack.menu.MenuController;

public class ClientesController {

	@FXML
	private TableView<Clientes> tbvClientes;
	@FXML
	private TableColumn<Clientes, String> tbcNome;
	@FXML
	private TableColumn<Clientes, String> tbcCpf;
	@FXML
	private TableColumn<Clientes, String> tbcRg;
	@FXML
	private TableColumn<Clientes, String> tbcFantasia;
	@FXML
	private TableColumn<Clientes, String> tbcPessoa;
	@FXML
	private TableColumn<Clientes, String> tbcSexo;
	@FXML
	private TableColumn<Clientes, String> tbcDataCadastro;
	@FXML
	private TableColumn<Clientes, String> tbcEmail;
	@FXML
	private TableColumn<Clientes, String> tbcDataNascimento;
	@FXML
	private TableColumn<Clientes, String> tbcCep;
	@FXML
	private TableColumn<Clientes, String> tbcEndereco;
	@FXML
	private TableColumn<Clientes, Number> tbcNumero;
	@FXML
	private TableColumn<Clientes, String> tbcBairro;
	@FXML
	private TableColumn<Clientes, String> tbcCidade;
	@FXML
	private TableColumn<Clientes, String> tbcUf;
	@FXML
	private TableColumn<Clientes, String> tbcTelefone;
	@FXML
	private TableColumn<Clientes, String> tbcFax;
	@FXML
	private TableColumn<Clientes, String> tbcBanco;
	@FXML
	private TableColumn<Clientes, Number> tbcAgencia;

	@FXML
	private Button btnNovo;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnApagar;
	@FXML
	private Button btnAtualizar;

	@FXML
	private TextField txtProcurar;

	private Clientes clientes = new Clientes();
	private Parent parent;
	private static Logger logger = LogManager.getLogger(ClientesController.class);

	public ClientesController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientesView.fxml"));
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
	public Parent getParentClientes() {
		return this.parent;
	}

	/**
	 * Método initialize que inicia toda vez que a classe é acessada
	 */
	@FXML
	private void initialize() {
		setAtributosEmColunas();
		listaClientesCadastrados();
		perfilUsuarioVendedor();
		cssNosBotoes();
	}

	/**
	 * Adiciona css aos botões da tela
	 */
	public void cssNosBotoes() {
		btnNovo.setStyle("-fx-background-image: url('add_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnEditar.setStyle("-fx-background-image: url('edit_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnApagar.setStyle("-fx-background-image: url('apagar_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 28 28;");
		btnAtualizar.setStyle("-fx-background-image: url('refresh.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 32 32;");
	}

	/**
	 * Verifica o perfil do usuario e toma as decisões setadas
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
	 * Botão que chama o método para abrir a janela de cadastro de clientes
	 */
	@FXML
	private void btnNovoAction() {
		new MenuController().abreCadastroClientes();
	}

	/**
	 * Botão que chama o método para abrir a janela de cadastro de clientes com as informações do item selecionado
	 */
	@FXML
	private void btnEditarAction() {
		editar();
	}

	/**
	 * Botão que chama o método de confirmação de exclusão
	 */
	@FXML
	private void btnApagarAction() {
		confirmaExclusao();
	}

	/**
	 * Verifica se o item não é null, caso não seja, o usuario do sistema recebe uma mensagem para confirmar a exclusão do item, senão é recebido uma mensagem para selecionar o
	 * item
	 */
	public void confirmaExclusao() {
		if (tbvClientes.getSelectionModel().getSelectedItem() != null) {
			Localization.setLocale(new Locale("pt", "BR"));
			Action response = Dialogs.create().title("Confirmação").message("Tem certeza que deseja excluir esse cliente?").actions(Dialog.Actions.YES, Dialog.Actions.NO).showConfirm();
			if (response == Dialog.Actions.YES) {
				remove();
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Botão que chama o método para atualizar a lista de clientes do banco de dados
	 */
	@FXML
	private void btnAtualizarAction() {
		listaClientesCadastrados();
	}

	/**
	 * Listar e filtrar os items da tableview pelo nome do cliente
	 */
	public void listaClientesCadastrados() {
		FilteredList<Clientes> filteredData = new FilteredList<>(clientes.consulta(), user -> true);

		filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> um -> um.nomeProperty().getValue().contains(txtProcurar.getText()), txtProcurar.textProperty()));

		SortedList<Clientes> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tbvClientes.comparatorProperty());
		tbvClientes.setItems(sortedData);
	}

	/**
	 * Seta os atributos em suas respectivas TableColumns
	 */
	public void setAtributosEmColunas() {
		tbcNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		tbcCpf.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());
		tbcRg.setCellValueFactory(cellData -> cellData.getValue().rgProperty());
		tbcFantasia.setCellValueFactory(cellData -> cellData.getValue().fantasiaProperty());
		tbcPessoa.setCellValueFactory(cellData -> cellData.getValue().pessoaProperty());
		tbcSexo.setCellValueFactory(cellData -> cellData.getValue().sexoProperty());
		tbcDataCadastro.setCellValueFactory(cellData -> cellData.getValue().dataCadastroProperty());
		tbcEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		tbcDataNascimento.setCellValueFactory(cellData -> cellData.getValue().dataNascimentoProperty());
		tbcCep.setCellValueFactory(cellData -> cellData.getValue().cepProperty());
		tbcEndereco.setCellValueFactory(cellData -> cellData.getValue().enderecoProperty());
		tbcNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
		tbcBairro.setCellValueFactory(cellData -> cellData.getValue().bairroProperty());
		tbcCidade.setCellValueFactory(cellData -> cellData.getValue().cidadeProperty());
		tbcUf.setCellValueFactory(cellData -> cellData.getValue().ufProperty());
		tbcTelefone.setCellValueFactory(cellData -> cellData.getValue().telefoneProperty());
		tbcFax.setCellValueFactory(cellData -> cellData.getValue().faxProperty());
		tbcBanco.setCellValueFactory(cellData -> cellData.getValue().bancoProperty());
		tbcAgencia.setCellValueFactory(cellData -> cellData.getValue().agenciaProperty());
	}

	/**
	 * Passa os dados do item selecionado para o método setValores
	 */
	public void editar() {
		int selecionado = tbvClientes.getSelectionModel().getSelectedIndex();

		if (tbvClientes.getSelectionModel().getSelectedItem() != null) {
			if (selecionado >= 0) {
				int codigo = tbvClientes.getSelectionModel().getSelectedItem().codigoProperty().get();
				String nome = tbvClientes.getSelectionModel().getSelectedItem().nomeProperty().get();
				String cpf = tbvClientes.getSelectionModel().getSelectedItem().cpfProperty().get();
				String rg = tbvClientes.getSelectionModel().getSelectedItem().rgProperty().get();
				String fantasia = tbvClientes.getSelectionModel().getSelectedItem().fantasiaProperty().get();
				String pessoa = tbvClientes.getSelectionModel().getSelectedItem().pessoaProperty().get();
				String sexo = tbvClientes.getSelectionModel().getSelectedItem().sexoProperty().get();
				String dataCadastro = tbvClientes.getSelectionModel().getSelectedItem().dataCadastroProperty().get();
				String email = tbvClientes.getSelectionModel().getSelectedItem().emailProperty().get();
				String dataNascimento = tbvClientes.getSelectionModel().getSelectedItem().dataNascimentoProperty().get();
				String cep = tbvClientes.getSelectionModel().getSelectedItem().cepProperty().get();
				String endereco = tbvClientes.getSelectionModel().getSelectedItem().enderecoProperty().get();
				int numero = tbvClientes.getSelectionModel().getSelectedItem().numeroProperty().get();
				String bairro = tbvClientes.getSelectionModel().getSelectedItem().bairroProperty().get();
				String cidade = tbvClientes.getSelectionModel().getSelectedItem().cidadeProperty().get();
				String uf = tbvClientes.getSelectionModel().getSelectedItem().ufProperty().get();
				String telefone = tbvClientes.getSelectionModel().getSelectedItem().telefoneProperty().get();
				String fax = tbvClientes.getSelectionModel().getSelectedItem().faxProperty().get();
				String banco = tbvClientes.getSelectionModel().getSelectedItem().bancoProperty().get();
				int agencia = tbvClientes.getSelectionModel().getSelectedItem().agenciaProperty().get();

				CadastroClientesController ccc = new CadastroClientesController();
				ccc.setValores(codigo, nome, cpf, rg, fantasia, pessoa, sexo, dataCadastro, email, dataNascimento, cep, endereco, numero, bairro, cidade, uf, telefone, fax, banco, agencia);
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Remove cliente selecionado na tableview e atualiza a lista
	 */
	public void remove() {
		int selecionado = tbvClientes.getSelectionModel().getSelectedIndex();
		IntegerProperty itemSelecionado = tbvClientes.getSelectionModel().getSelectedItem().codigoProperty();

		if (selecionado >= 0) {
			clientes.delete(itemSelecionado.get());
			listaClientesCadastrados();
		}
		else {
			Dialogs.create().title("Aviso").message("Selecione um cliente da tabela!").showWarning();
		}
	}
}
