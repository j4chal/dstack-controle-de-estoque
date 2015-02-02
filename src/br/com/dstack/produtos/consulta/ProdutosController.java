package br.com.dstack.produtos.consulta;

import impl.org.controlsfx.i18n.Localization;

import java.io.IOException;
import java.io.InputStream;
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

import br.com.dstack.login.Login;
import br.com.dstack.menu.MenuController;
import br.com.dstack.produtos.Produtos;
import br.com.dstack.produtos.cadastro.CadastroProdutosController;
import br.com.dstack.produtos.categorias.CategoriasController;
import br.com.dstack.produtos.marcas.MarcasController;

public class ProdutosController {

	@FXML
	private TableView<Produtos> tbvProdutos;
	@FXML
	private TableColumn<Produtos, String> tbcDescricao;
	@FXML
	private TableColumn<Produtos, String> tbcFornecedor;
	@FXML
	private TableColumn<Produtos, String> tbcUnidadeMedida;
	@FXML
	private TableColumn<Produtos, Number> tbcEstoqueAtual;
	@FXML
	private TableColumn<Produtos, Number> tbcPreco;
	@FXML
	private TableColumn<Produtos, Number> tbcEstoqueMinimo;
	@FXML
	private TableColumn<Produtos, Number> tbcEstoqueMaximo;
	@FXML
	private TableColumn<Produtos, String> tbcObservacoes;
	@FXML
	private TableColumn<Produtos, String> tbcCategoria;
	@FXML
	private TableColumn<Produtos, String> tbcMarca;
	@FXML
	private TableColumn<Produtos, String> tbcData;

	@FXML
	private TextField txtProcurar;
	@FXML
	private Button btnNovo;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnApagar;
	@FXML
	private Button btnAtualizar;
	@FXML
	private Button btnCadastrarCategoria;
	@FXML
	private Button btnCadastrarMarca;

	private Produtos produto = new Produtos();
	private Parent parent;
	private static Logger logger = LogManager.getLogger(ProdutosController.class);

	public ProdutosController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProdutosView.fxml"));
			fxmlLoader.setController(this);
			parent = fxmlLoader.load();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	public Parent getParentClientes() {
		return this.parent;
	}

	/**
	 * Método que inicializa com a classe
	 */
	@FXML
	private void initialize() {
		setAtributosEmColunas();
		listaProdutosCadastrados();
		perfilUsuarioVendedor();
		cssNosBotoes();
	}

	/**
	 * Botão que chama o método de abrir a tela de cadastro
	 */
	@FXML
	private void btnNovoAction() {
		new MenuController().abreCadastroProdutos();
	}

	/**
	 * Botão que chama o método editar que pega os items e setam na tela de cadastro
	 */
	@FXML
	private void btnEditarAction() {
		editar();
	}

	/**
	 * Botão que chama método para excluir item da TableView e do banco de dados
	 */
	@FXML
	private void btnApagarAction() {
		confirmaExclusao();
	}

	/**
	 * Botão que chama o método para atualizar a tableview
	 */
	@FXML
	private void btnAtualizarAction() {
		listaProdutosCadastrados();
	}

	/**
	 * Botão que cria objeto das categorias e abre a tela de cadastro
	 */
	@FXML
	private void btnCadastrarCategoriaAction() {
		new CategoriasController();
	}

	/**
	 * Botão que cria objeto de Marcas e abre a tela de cadastro
	 */
	@FXML
	private void btnCadastrarMarcaAction() {
		new MarcasController();
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
	 * Adiciona css aos botões da tela
	 */
	public void cssNosBotoes() {
		btnNovo.setStyle("-fx-background-image: url('add_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnEditar.setStyle("-fx-background-image: url('edit_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 32 32;");
		btnApagar.setStyle("-fx-background-image: url('apagar_fornecedores.png'); -fx-background-repeat: no-repeat; -fx-background-position: left; -fx-background-size: 28 28;");
		btnAtualizar.setStyle("-fx-background-image: url('refresh.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 32 32;");
	}

	/**
	 * Verifica se o item não é null, caso não seja, o usuario do sistema recebe uma mensagem para confirmar a exclusão do item, senão é recebido uma mensagem para selecionar o
	 * item
	 */
	public void confirmaExclusao() {
		if (tbvProdutos.getSelectionModel().getSelectedItem() != null) {
			Localization.setLocale(new Locale("pt", "BR"));
			Action response = Dialogs.create().title("Confirmação").message("Tem certeza que deseja apagar esse produto?").actions(Dialog.Actions.YES, Dialog.Actions.NO).showConfirm();
			if (response == Dialog.Actions.YES) {
				remove();
			}
		}
		else {
			Dialogs.create().title("Aviso").message("Você não selecionou nenhum item da lista!").showWarning();
		}
	}

	/**
	 * Listar e filtrar os items da tableview pelo nome do cliente
	 */
	public void listaProdutosCadastrados() {
		FilteredList<Produtos> filteredData = new FilteredList<>(produto.consulta(), user -> true);

		filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> um -> um.descricaoProperty().getValue().contains(txtProcurar.getText()), txtProcurar.textProperty()));

		SortedList<Produtos> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tbvProdutos.comparatorProperty());
		tbvProdutos.setItems(sortedData);
	}

	/**
	 * Seta os atributos em suas respectivas TableColumns
	 */
	public void setAtributosEmColunas() {
		tbcDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());
		tbcFornecedor.setCellValueFactory(cellData -> cellData.getValue().nomeFornecedorProperty());
		tbcUnidadeMedida.setCellValueFactory(cellData -> cellData.getValue().unidadeMedidaProperty());
		tbcEstoqueAtual.setCellValueFactory(cellData -> cellData.getValue().estoqueAtualProperty());
		tbcPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty());
		tbcEstoqueMinimo.setCellValueFactory(cellData -> cellData.getValue().estoqueMinimoProperty());
		tbcEstoqueMaximo.setCellValueFactory(cellData -> cellData.getValue().estoqueMaximoProperty());
		tbcObservacoes.setCellValueFactory(cellData -> cellData.getValue().observacoesProperty());
		tbcCategoria.setCellValueFactory(cellData -> cellData.getValue().descricaoCategoriaProperty());
		tbcMarca.setCellValueFactory(cellData -> cellData.getValue().descricaoMarcaProperty());
		tbcData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
	}

	/**
	 * Passa os dados do item selecionado para o método setValores
	 */
	public void editar() {
		int selecionado = tbvProdutos.getSelectionModel().getSelectedIndex();

		if (tbvProdutos.getSelectionModel().getSelectedItem() != null) {
			if (selecionado >= 0) {
				int codigo = tbvProdutos.getSelectionModel().getSelectedItem().codigoProperty().get();
				String descricao = tbvProdutos.getSelectionModel().getSelectedItem().descricaoProperty().get();
				InputStream img = tbvProdutos.getSelectionModel().getSelectedItem().getInputImage();
				String fornecedor = tbvProdutos.getSelectionModel().getSelectedItem().nomeFornecedorProperty().get();
				String unidadeMedida = tbvProdutos.getSelectionModel().getSelectedItem().unidadeMedidaProperty().get();
				int estoqueAtual = tbvProdutos.getSelectionModel().getSelectedItem().estoqueAtualProperty().get();
				double preco = tbvProdutos.getSelectionModel().getSelectedItem().precoProperty().get();
				int estoqueMinimo = tbvProdutos.getSelectionModel().getSelectedItem().estoqueMinimoProperty().get();
				int estoqueMaximo = tbvProdutos.getSelectionModel().getSelectedItem().estoqueMaximoProperty().get();
				String observacoes = tbvProdutos.getSelectionModel().getSelectedItem().observacoesProperty().get();
				String categoria = tbvProdutos.getSelectionModel().getSelectedItem().descricaoCategoriaProperty().get();
				String marca = tbvProdutos.getSelectionModel().getSelectedItem().descricaoMarcaProperty().get();
				int codigoFornecedor = tbvProdutos.getSelectionModel().getSelectedItem().codigoFornecedorProperty().get();
				int codigoCategoria = tbvProdutos.getSelectionModel().getSelectedItem().codigoCategoriaProperty().get();
				int codigoMarca = tbvProdutos.getSelectionModel().getSelectedItem().codigoMarcaProperty().get();

				CadastroProdutosController cpc = new CadastroProdutosController();
				cpc.setValores(codigo, descricao, img, fornecedor, unidadeMedida, estoqueAtual, preco, estoqueMinimo, estoqueMaximo, observacoes, categoria, marca, codigoFornecedor, codigoCategoria,
						codigoMarca);
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
		int selecionado = tbvProdutos.getSelectionModel().getSelectedIndex();
		IntegerProperty itemSelecionado = tbvProdutos.getSelectionModel().getSelectedItem().codigoProperty();

		if (selecionado >= 0) {
			produto.delete(itemSelecionado.get());
			listaProdutosCadastrados();
		}
		else {
			Dialogs.create().title("Aviso").message("Selecione um produto da tabela!").showWarning();
		}
	}
}
