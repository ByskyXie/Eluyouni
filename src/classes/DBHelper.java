package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
	//warnning：characterEncodeing
	private static final String url = "jdbc:mysql://localhost/elu?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";//末尾还要标注时区
	private static final String name = "com.mysql.cj.jdbc.Driver";
	private static final String user = "root";
	private static final String pwd = "mysql233";
	
	public Connection conn=null;
	private PreparedStatement pst=null;
	
	public DBHelper(String sql){
		renewConnect(sql);
	}
	
	public void renewConnect(String sql){
		try{
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, pwd);//获取连接
			pst = conn.prepareStatement(sql);
		}catch(ClassNotFoundException cnfe){
			System.err.println(cnfe);
			cnfe.printStackTrace();
		}catch(SQLException e){
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public ResultSet execute() throws SQLException{
		if(pst == null){
			System.out.println("PST is null");
			return null;
		}
		return pst.executeQuery();
	}
	
	public void close(){
		try {
			if(conn!=null)
				conn.close();
			if(pst!=null)
				pst.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
}
