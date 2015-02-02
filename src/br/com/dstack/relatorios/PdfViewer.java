package br.com.dstack.relatorios;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;

@SuppressWarnings("serial")
public class PdfViewer extends JRViewer {
	
	/**
	 * Limita os tipos de formato para salvar, pdf apenas
	 */
	public PdfViewer(JasperPrint jasperPrint) {
		super(jasperPrint);
		
		Locale locale = Locale.getDefault();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("net/sf/jasperreports/view/viewer", locale);

		JRPdfSaveContributor pdf = new JRPdfSaveContributor(locale, resourceBundle);
		JRSaveContributor[] jr = new JRSaveContributor[]
			{ pdf };
		tlbToolBar.setSaveContributors(jr);
	}
}
