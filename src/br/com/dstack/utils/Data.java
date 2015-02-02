package br.com.dstack.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Data {

	/**
	 * Retorna a data atual junto com dia da semana, regex: EEEE, dd/MM/yyyy
	 * 
	 * @return String Data Atual com Dia da Semana
	 */
	public static String getDataAtualComDiaSemana() {
		LocalDate dataAtual = LocalDate.now();
		DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy");
		return dataAtual.format(dataFormat);
	}

	/**
	 * Retorna a DataAtual, regex: dd/MM/yyyy
	 * 
	 * @return String Data Atual
	 */
	public static String getDataAtual() {
		LocalDate dataAtual = LocalDate.now();
		DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dataAtual.format(dataFormat);
	}

	/**
	 * Retorna a data atual com regex para inserção no banco de dados
	 * 
	 * @return String Data Atual com formato para BD
	 */
	public static String getDataAtualDb() {
		LocalDate dataAtual = LocalDate.now();
		DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return dataAtual.format(dataFormat);
	}
}
