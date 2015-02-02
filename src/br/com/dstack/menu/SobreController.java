package br.com.dstack.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import br.com.dstack.utils.Versao;

public class SobreController {

	/**
	 * Componentes injetados
	 */
	@FXML
	private Button btnOK;
	@FXML
	private ScrollPane scpDetalhes;
	@FXML
	private Label lblVersaoAtual;
	@FXML
	private ImageView imgLogo;

	/**
	 * Método initialize que inicia ao acessar essa "classe"
	 */
	@FXML
	public void initialize() {
		imgLogo.setImage(new Image(ClassLoader.getSystemResourceAsStream("logo.png")));
		lblVersaoAtual.setText(Versao.getVersaoAtual());
		scpContent();
	}

	/**
	 * Botão que fecha a janela ao clicar
	 */
	@FXML
	private void btnOKAction() {
		Stage stage = (Stage) btnOK.getScene().getWindow();
		stage.close();
	}

	/**
	 * Texto (Direitos do Software) como conteudo para o ScrollPane
	 */
	public void scpContent() {
		Text content = new Text("Copyright (c) 2014, DStack" + "\nTodos direitos reservados." + "\n" + "\nRedistribution and use in source and binary forms, with or without"
				+ "\nmodification, are permitted provided that the following conditions are met:" + "\n  * Redistributions of source code must retain the above copyright"
				+ "\nnotice, this list of conditions and the following disclaimer." + "\n  * Redistributions in binary form must reproduce the above copyright"
				+ "\nnotice, this list of conditions and the following disclaimer in the" + "\ndocumentation and/or other materials provided with the distribution."
				+ "\n  * Neither the name of ControlsFX, any associated website, nor the" + "\nnames of its contributors may be used to endorse or promote products"
				+ "\nderived from this software without specific prior written permission." + "\n" + "\nTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS AS IS AND"
				+ "\nANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED" + "\nWARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE"
				+ "\nDISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY" + "\nDIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES"
				+ "\n(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;" + "\nLOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND"
				+ "\nON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT" + "\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS"
				+ "\nSOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
		scpDetalhes.setContent(content);
	}
}
