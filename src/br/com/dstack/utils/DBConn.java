package br.com.dstack.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {

	private String url = "jdbc:mariadb://localhost:3308/dstack_estoque";
	private String urlSemDB = "jdbc:mariadb://localhost:3308";
	private String user = "root";
	private String password = "dRZsu9Er82GmmSqE";

	/**
	 * JDBC - Conexão com MariaDB
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public Connection getConnectionSemDB() throws SQLException {
		return DriverManager.getConnection(urlSemDB, user, password);
	}
}
