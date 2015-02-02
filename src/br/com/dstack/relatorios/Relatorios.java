package br.com.dstack.relatorios;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.utils.DbConn;

public class Relatorios {

	private static Logger logger = LogManager.getLogger(Relatorios.class);

	/**
	 * Dado data de Inicio e Fim, é realizado a conexão, e passado como parametro. E então é retornado um JasperPrint
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return JasperPrint
	 */
	public JasperPrint gerarReportProdutos(LocalDate dataInicio, LocalDate dataFim) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		JasperPrint print = null;

		if (dataInicio != null && dataFim != null) {
			try (Connection conn = new DbConn().getConnection()) {

				parametros.put("DATA_INICIO", new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio.toString()));
				parametros.put("DATA_FIM", new SimpleDateFormat("yyyy-MM-dd").parse(dataFim.toString()));

				JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("report_produtos.jrxml"));
				print = JasperFillManager.fillReport(report, parametros, conn);
			}
			catch (SQLException e1) {
				logger.log(Level.TRACE, "SQLException", e1);
			}
			catch (ParseException e2) {
				logger.log(Level.TRACE, "ParseException", e2);
			}
			catch (JRException e3) {
				logger.log(Level.TRACE, "JRException", e3);
			}
		}
		return print;
	}

	/**
	 * Gera JasperPrint do jrxml de Fornecedores
	 * 
	 * @return JasperPrint
	 */
	public JasperPrint gerarReportFornecedores() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		JasperPrint print = null;

		try (Connection conn = new DbConn().getConnection()) {
			JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("report_fornecedores.jrxml"));
			print = JasperFillManager.fillReport(report, parametros, conn);
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
		}
		catch (JRException e2) {
			logger.log(Level.TRACE, "JRException", e2);
		}
		return print;
	}
}
