package br.com.dstack.utils;

public class SaltParaSenha {

	private static String SALT = "$2a$11$llP9G6IyibUob8h5XRt9xuRczaGdCm/AiV6SSjf5v78XS824EGbh.";

	/**
	 * Retorna o salt para hashar a senha
	 * 
	 * @return String SALT
	 */
	public static String getSalt() {
		return SALT;
	}
}
