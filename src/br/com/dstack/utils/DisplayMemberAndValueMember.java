package br.com.dstack.utils;

public class DisplayMemberAndValueMember {

	public String displayMember;
	public int valueMember;

	/**
	 * Classe que guarda o id e uma string para representação, de um combobox.
	 * O id é usado para inserir na tabela no banco de dados
	 */

	public DisplayMemberAndValueMember(String displayMember) {
		this.displayMember = displayMember;
	}

	public DisplayMemberAndValueMember(String displayMember, int valueMember) {
		this.displayMember = displayMember;
		this.valueMember = valueMember;
	}

	public String toString() {
		return this.displayMember;
	}

	public int getKey() {
		return this.valueMember;
	}
}
