package br.com.dstack.relatorios;

import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelatoriosController {

	@FXML
	private DatePicker dateEstoqueDe;
	@FXML
	private DatePicker dateEstoquePara;
	@FXML
	private Button btnGerarReportEstoque;
	@FXML
	private Button btnGerarReportFornecedores;
	@FXML
	private StackPane skpRelatorios;

	private Relatorios relatorios = new Relatorios();

	private Parent parent;
	private Stage stage;
	private static Logger logger = LogManager.getLogger(RelatoriosController.class);

	public RelatoriosController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RelatoriosView.fxml"));
			fxmlLoader.setController(this);
			parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Relatórios | DStack - Controle de Estoque");
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	public Parent getParentRelatorios() {
		return this.parent;
	}

	/**
	 * Botão que após digitado ou escolhido as datas é gerado o report de estoque da tabela produtos
	 */
	@FXML
	private void btnGerarReportEstoqueAction() {
		abrirTelaPdf(relatorios.gerarReportProdutos(getDataInicio(), getDataFim()));
	}

	/**
	 * Botão que gera um report da tabela de fornecedores
	 */
	@FXML
	private void btnGerarReportFornecedoresAction() {
		abrirTelaPdf(relatorios.gerarReportFornecedores());
	}

	/**
	 * Retorna a data inicio
	 * 
	 * @return LocalDate
	 */
	public LocalDate getDataInicio() {
		return dateEstoqueDe.getValue();
	}

	/**
	 * Retorna a data fim
	 * 
	 * @return LocalDate
	 */
	public LocalDate getDataFim() {
		return dateEstoquePara.getValue();
	}

	/**
	 * Dado JasperPrint é setado um SwingNode e então setado como children do StackPane de relatórios
	 * 
	 * @param print
	 */
	public void abrirTelaPdf(JasperPrint print) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingNode swingNode = new SwingNode();
			PdfViewer view = new PdfViewer(print);

			Platform.runLater(() -> {
				swingNode.setContent(view);
				skpRelatorios.getChildren().add(swingNode);
			});
		}
		catch (ClassNotFoundException e2) {
			logger.log(Level.TRACE, "ClassNotFoundException", e2);
		}
		catch (InstantiationException e3) {
			logger.log(Level.TRACE, "InstantiationException", e3);
		}
		catch (IllegalAccessException e4) {
			logger.log(Level.TRACE, "IllegalAccessException", e4);
		}
		catch (UnsupportedLookAndFeelException e5) {
			logger.log(Level.TRACE, "UnsupportedLookAndFeelException", e5);
		}
	}
}
