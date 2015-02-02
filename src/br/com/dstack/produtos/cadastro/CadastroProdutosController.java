package br.com.dstack.produtos.cadastro;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import br.com.dstack.produtos.Produtos;
import br.com.dstack.produtos.categorias.CategoriasController;
import br.com.dstack.produtos.marcas.MarcasController;
import br.com.dstack.utils.DbConn;
import br.com.dstack.utils.DisplayMemberAndValueMember;
import br.com.dstack.utils.Validacao;

public class CadastroProdutosController {

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnFechar;
	@FXML
	private Button btnLimpar;
	@FXML
	private Button btnEscolherImagem;
	@FXML
	private Button btnNovaCategoria;
	@FXML
	private Button btnNovaMarca;

	@FXML
	private TextField txtCodigo;
	@FXML
	private TextField txtDescricao;
	@FXML
	private TextField txtUnidadeMedida;
	@FXML
	private TextField txtEstoqueAtual;
	@FXML
	private TextField txtEstoqueMinimo;
	@FXML
	private TextField txtEstoqueMaximo;
	@FXML
	private TextField txtObservacoes;
	@FXML
	private TextField txtPreco;

	@FXML
	private ComboBox<DisplayMemberAndValueMember> cmbFornecedor;
	@FXML
	private ComboBox<DisplayMemberAndValueMember> cmbCategorias;
	@FXML
	private ComboBox<DisplayMemberAndValueMember> cmbMarcas;

	@FXML
	private Label lblMensagem;
	@FXML
	private ImageView imgProduto;

	private Produtos produtos = new Produtos();
	private File file;
	private static Logger logger = LogManager.getLogger(CadastroProdutosController.class);

	public CadastroProdutosController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CadastroProdutosView.fxml"));
			fxmlLoader.setController(this);
			Parent parent;
			parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Cadastrar Produtos | DStack - Controle de Estoque");
			stage.getIcons().add(new Image("icon.jpg"));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	/**
	 * Método que inicializa toda vez que essa classe é chamada
	 */
	@FXML
	private void initialize() {
		setComboBoxes();
		validacaoDeCampos();
		escolherImagem();
	}

	/**
	 * Botão que salva ou altera baseado no campo codigo, se estiver vazio, cadastra, senão altera
	 */
	@FXML
	private void btnSalvarAction() {
		if (txtCodigo.getText().trim().length() != 0) {
			alterar();
		}
		if (txtCodigo.getText().trim().length() == 0) {
			verificarInsert();
		}
	}

	/**
	 * Botão que chama o método para fechar a janela
	 */
	@FXML
	private void btnFecharAction() {
		fechaJanela();
	}

	/**
	 * Botão que chama o método para limpar os campos
	 */
	@FXML
	private void btnLimparAction() {
		limparCampos();
	}

	/**
	 * Botão que chama a tela de cadastro de categorias
	 */
	@FXML
	private void btnNovaCategoriaAction() {
		new CategoriasController();
	}

	/**
	 * Botão que chama a tela de cadastro de marcas
	 */
	@FXML
	private void btnNovaMarcaAction() {
		new MarcasController();
	}

	/**
	 * Limpa os campos
	 */
	private void limparCampos() {
		txtCodigo.clear();
		txtDescricao.clear();
		txtUnidadeMedida.clear();
		txtEstoqueAtual.clear();
		txtEstoqueMinimo.clear();
		txtEstoqueMaximo.clear();
		txtPreco.clear();
		txtObservacoes.clear();
		cmbFornecedor.getItems().clear();
		cmbCategorias.getItems().clear();
		cmbMarcas.getItems().clear();
		setComboBoxes();
		cmbFornecedor.setPromptText("Selecione o fornecedor");
		cmbCategorias.setPromptText("Selecione a categoria");
		cmbMarcas.setPromptText("Selecione a marca");
		lblMensagem.setText("");
		imgProduto.setImage(null);
	}

	/**
	 * Método que pega o Stage da janela e chama o método close()
	 */
	public void fechaJanela() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}

	/**
	 * Método para escolhe de imagem do produto e retorna um File dessa imagem
	 * 
	 * @return File
	 */
	public File escolherImagem() {
		EventHandler<ActionEvent> btnLoadEventListener = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();

				FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
				FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
				fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

				file = fileChooser.showOpenDialog(null);
				try {
					BufferedImage bufferedImage = ImageIO.read(file);
					Image image = SwingFXUtils.toFXImage(bufferedImage, null);
					imgProduto.setImage(image);
				}
				catch (IOException e2) {
					logger.log(Level.TRACE, "IOException", e2);
				}
			}
		};
		btnEscolherImagem.setOnAction(btnLoadEventListener);

		return file;
	}

	/**
	 * Converte uma imagem para InputStrem
	 * 
	 * @return InputStream
	 */
	public InputStream converterImageParaInputStream() {
		InputStream is = null;

		try {
			BufferedImage bi = SwingFXUtils.fromFXImage(imgProduto.getImage(), null);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", os);
			is = new ByteArrayInputStream(os.toByteArray());
		}
		catch (IOException e3) {
			logger.log(Level.TRACE, "IOException", e3);
		}
		return is;
	}

	/**
	 * Dado InputStream é convertido para uma Image e retornado
	 * 
	 * @param is
	 * @return Image
	 */
	public Image converterInputStreamParaImage(InputStream is) {
		Image image = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(is);
			if (bufferedImage != null) {
				image = SwingFXUtils.toFXImage(bufferedImage, null);
				is.reset();
			}
		}
		catch (IOException e4) {
			logger.log(Level.TRACE, "IOException", e4);
		}
		return image;
	}

	/**
	 * Seta os valores na tela de edição de produtos
	 * 
	 * @param codigo
	 * @param descricao
	 * @param img
	 * @param fornecedor
	 * @param unidadeMedida
	 * @param estoqueAtual
	 * @param preco
	 * @param estoqueMinimo
	 * @param estoqueMaximo
	 * @param observacoes
	 * @param categoria
	 * @param marca
	 * @param codigoFornecedor
	 * @param codigoCategoria
	 * @param codigoMarca
	 */
	public void setValores(int codigo, String descricao, InputStream img, String fornecedor, String unidadeMedida, int estoqueAtual, double preco, int estoqueMinimo, int estoqueMaximo,
			String observacoes, String categoria, String marca, int codigoFornecedor, int codigoCategoria, int codigoMarca) {
		txtCodigo.setText(Integer.toString(codigo));
		txtDescricao.setText(descricao);
		cmbFornecedor.setValue(new DisplayMemberAndValueMember(fornecedor, codigoFornecedor));
		txtUnidadeMedida.setText(unidadeMedida);
		txtEstoqueAtual.setText(Integer.toString(estoqueAtual));
		txtPreco.setText(Double.toString(preco));
		txtEstoqueMinimo.setText(Integer.toString(estoqueMinimo));
		txtEstoqueMaximo.setText(Integer.toString(estoqueMaximo));
		txtObservacoes.setText(observacoes);
		cmbCategorias.setValue(new DisplayMemberAndValueMember(categoria, codigoCategoria));
		cmbMarcas.setValue(new DisplayMemberAndValueMember(marca, codigoMarca));
		imgProduto.setImage(converterInputStreamParaImage(img));
	}

	/**
	 * Passa os dados e verifica se a inserção ocorreu com sucesso, se retornar true é mostrado a mensagem de sucesso, senão é mostrado uma mensagem com erro
	 */
	public void verificarInsert() {
		try {
			produtos = new Produtos(txtDescricao.getText(), escolherImagem(), cmbFornecedor.getValue().getKey(), txtUnidadeMedida.getText(), Integer.parseInt(txtEstoqueAtual.getText()),
					Double.parseDouble(txtPreco.getText()), Integer.parseInt(txtEstoqueMinimo.getText()), Integer.parseInt(txtEstoqueMaximo.getText()), txtObservacoes.getText(), cmbCategorias
							.getValue().getKey(), cmbMarcas.getValue().getKey());

			if (produtos.insere()) {
				Dialogs.create().title("Sucesso").masthead(null).message("Produto cadastrado com sucesso!").showInformation();
				fechaJanela();
			}
			else {
				Dialogs.create().title("Error").masthead(null).message("Erro ao inserir dados, verifique se todos os campos necessarios estão preenchidos!").showError();
			}
		}
		catch (Exception e5) {
			lblMensagem.setText("Campos não preenchidos");
		}
	}

	/**
	 * Passa os dados para alteração, se retornar true é recebido a mensagem de sucesso e a janela de cadastro é fechada
	 */
	public void alterar() {
		if (produtos.altera(Integer.parseInt(txtCodigo.getText()), txtDescricao.getText(), converterImageParaInputStream(), cmbFornecedor.getValue().getKey(), txtUnidadeMedida.getText(),
				Integer.parseInt(txtEstoqueAtual.getText()), Double.parseDouble(txtPreco.getText()), Integer.parseInt(txtEstoqueMinimo.getText()), Integer.parseInt(txtEstoqueMaximo.getText()),
				txtObservacoes.getText(), cmbCategorias.getValue().getKey(), cmbMarcas.getValue().getKey())) {
			Dialogs.create().title("Alterar").masthead(null).message("Produto foi alterado com sucesso!").showInformation();
			fechaJanela();
		}
		else {
			Dialogs.create().title("Error").message("Verifique se todos os campos estão preenchidos corretamente!").showError();
		}
	}

	/**
	 * Método que seta UF's de uma ObservableList
	 */
	public void setComboBoxes() {
		try (Connection conn = new DbConn().getConnection()) {
			CadastroProdutosDAO dao = new CadastroProdutosDAO(conn);
			dao.consultaDeFornecedores(cmbFornecedor);
			dao.consultaDeCategorias(cmbCategorias);
			dao.consultaDeMarcas(cmbMarcas);
		}
		catch (SQLException e6) {
			logger.log(Level.TRACE, "IOException", e6);
		}
	}

	/**
	 * Faz a validação dos campos, restringe characteres e quantidade de digitos
	 */
	public void validacaoDeCampos() {
		Validacao.limitarCaracteres(txtDescricao, 200, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtUnidadeMedida, 4, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtEstoqueAtual, 5, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtEstoqueMinimo, 5, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtEstoqueMaximo, 5, lblMensagem);
		Validacao.limitarCaracteres(txtObservacoes, 250, lblMensagem);
		Validacao.restringirLetras(txtPreco, 10, lblMensagem);
	}
}
