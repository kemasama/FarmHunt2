package farmhunt.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CMySQL {
	private String host, port, name, user, pass;
	private String url;
	private boolean _isConnected = false;
	private Connection con;

	public CMySQL(String host, String port, String name, String user, String pass){
		this.host = host;
		this.port = port;
		this.name = name;
		this.user = user;
		this.pass = pass;

		Initialize();
	}

	private void Initialize() {
		url = String.format(
				"jdbc:mysql://%s:%s/%s",
				host,
				port,
				name);
	}

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(url, user, pass);
			_isConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return con;
	}

	public void update(String query) {
		update(query, false);
	}
	public void update(String query, boolean reload) {
		try {
			Connection c = getConnection();
			Statement stm = c.createStatement();
			stm.executeUpdate(query);
			stm.close();
		} catch (SQLException e) {
			if (reload) {
				Connect();
				update(query, false);
			}
			e.printStackTrace();
		}
	}

	public ResultSet query(String query) {
		return query(query, false);
	}
	public ResultSet query(String query, boolean reload) {
		try {
			Connection c = getConnection();
			Statement stm = c.createStatement();
			return stm.executeQuery(query);
		} catch (SQLException e) {
			if (reload) {
				Connect();
				return query(query, false);
			}
			e.printStackTrace();
			return null;
		}
	}

	public boolean tableExists(String name){
		try {
			DatabaseMetaData meta = getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, name, new String[] { "TABLE" });

			while (res.next()){
				return true;
			}

			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public void createTable(String name, String column) {

		if (tableExists(name)){
			return;
		}

		String sql = String.format("create table `%s`(%s)", name, column);

		System.out.println(sql);

		Connection c = getConnection();
		try {
			Statement stm = c.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disConnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		_isConnected = false;
	}

	public boolean isConnected() {
		return _isConnected;
	}

}