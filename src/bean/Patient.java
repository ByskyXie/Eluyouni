package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import classes.DBHelper;

public class Patient {
	
	private DBHelper dbHelper = null;
	private long pid;
	private int psex;
	private String pname;
	private String ppwd;
	private String picon;
	private long ecoin;
	private int pscore;
	
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPicon() {
		return picon;
	}

	public void setPicon(String picon) {
		this.picon = picon;
	}

	public long getEcoin() {
		return ecoin;
	}

	public void setEcoin(long ecoin) {
		this.ecoin = ecoin;
	}

	public int getPscore() {
		return pscore;
	}

	public void setPscore(int pscore) {
		this.pscore = pscore;
	}

	public String getPpwd() {
		return ppwd;
	}

	public void setPpwd(String ppwd) {
		this.ppwd = ppwd;
	}

	public int getPsex() {
		return psex;
	}

	public void setPsex(int psex) {
		this.psex = psex;
	}

	public Patient(){}
	
	public Patient(long pid, int psex, String pname, String ppwd, String picon, long ecoin, int pscore) {
		super();
		this.pid = pid;
		this.psex = psex;
		this.pname = pname;
		this.ppwd = ppwd;
		this.picon = picon;
		this.ecoin = ecoin;
		this.pscore = pscore;
	}

	public ResultSet getPatientInfo(long pid){
		ResultSet result=null;
		String sql = "SELECT * "
				+ "FROM PATIENT "
				+ "WHERE PID="+pid
				+ ";";
		if(dbHelper == null)
			dbHelper = new DBHelper(sql);
		try{
			result = dbHelper.execute();
		}catch(SQLException e){
			System.err.println(e);
			e.printStackTrace();
		}
		return result;
	}
	
	public void close(){
		if(dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}
	}
	
}
