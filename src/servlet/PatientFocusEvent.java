package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.DBHelper;

@WebServlet(urlPatterns="/patient/focusevent")
public class PatientFocusEvent extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public PatientFocusEvent() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String pid = request.getParameter("pid");
		String startPos = request.getParameter("startpos");
		String ndbody = request.getParameter("ndbody");
		if(pid==null){
			response.getWriter().append("failed\nfalse request");
			response.getWriter().close();
			return;
		}
		ResultSet adset = getFocusADList(Long.parseLong(pid) );
		ResultSet apset = getFocusAPList(Long.parseLong(pid) );
		ResultSet acset = getFocusACList(Long.parseLong(pid) );
		try{
			//输出list
			mixOutputFocusEvent(out, apset, adset, acset, true);
			if(adset != null)	adset.close();
			if(apset != null)	apset.close();
			if(acset != null)	acset.close();
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
			response.getWriter().append("failed\nfalse position format");
		}catch(SQLException e){
			e.printStackTrace();
		}
		response.getWriter().flush();
		response.getWriter().close();
		out.flush(); 	out.close();
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	public ResultSet getFocusADList(long pid){
		String sql = ""
				+ "SELECT * "
				+ "FROM ARTICLE_DOCTOR,CONCERN_PERSON "
				+ "WHERE ARTICLE_DOCTOR.DID = CONCERN_PERSON.ER_ID AND ER_TYPE = 2 "
				+ "AND CONCERN_PERSON.PID="+pid
				+ " ORDER BY TIME DESC;";
		DBHelper db = new DBHelper(sql);
		try {
			return db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getFocusAPList(long pid){
		String sql = ""
				+ "SELECT * "
				+ "FROM ARTICLE_PATIENT,CONCERN_PERSON "
				+ "WHERE ARTICLE_PATIENT.PID = CONCERN_PERSON.ER_ID AND ER_TYPE = 1 "
				+ "AND CONCERN_PERSON.PID="+pid
				+ " ORDER BY TIME DESC;";
		DBHelper db = new DBHelper(sql);
		try {
			return db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getFocusACList(long pid){
		String sql = ""
				+ "SELECT * "
				+ "FROM COMMUNITY,CONCERN_PERSON "
				+ "WHERE COMMUNITY.ER_ID = CONCERN_PERSON.ER_ID "
				+ "AND COMMUNITY.ER_TYPE = CONCERN_PERSON.ER_TYPE "
				+ "AND CONCERN_PERSON.PID="+pid+" "
				+ "ORDER BY TIME DESC;";
		DBHelper db = new DBHelper(sql);
		try {
			return db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void outputADSet(PrintWriter out, ResultSet result, int pos, boolean ndbody) throws IOException, SQLException{
		result.absolute(pos);

		out.append("type=ad\n");
		out.append("adid="+result.getString(1)+'\n');
		out.append("did="+result.getString(2)+'\n');
		out.append("title="+result.getString(3)+'\n');
		out.append("time="+result.getString(4)+'\n');
		if(ndbody){
			out.append("content="+result.getString(5)+'\n');
			out.append("end=end\n");
		}
	}

	public void outputAPSet(PrintWriter out, ResultSet result, int pos, boolean ndbody) throws IOException, SQLException{
		result.absolute(pos);

		out.append("type=ap\n");
		out.append("apid="+result.getString(1)+'\n');
		out.append("pid="+result.getString(2)+'\n');
		out.append("title="+result.getString(3)+'\n');
		out.append("time="+result.getString(4)+'\n');
		if(ndbody){
			out.append("content="+result.getString(5)+'\n');
			out.append("end=end\n");
		}
	}
	
	public void outputACSet(PrintWriter out, ResultSet result, int pos) throws IOException, SQLException{
		result.absolute(pos);
		
		out.append("type=ac\n");
		out.append("cid="+result.getString(1)+'\n');
		out.append("erid="+result.getString(2)+'\n');
		out.append("ertype="+result.getString(3)+'\n');
		out.append("time="+result.getString(4)+'\n');
		out.append("content="+result.getString(5)+'\n');	//区分只能通过"assent="了。
		out.append("assent="+result.getString(6)+'\n');
	}
	
	public void mixOutputFocusEvent(PrintWriter out, ResultSet ap, ResultSet ad, ResultSet ac, boolean ndbody) throws SQLException, IOException{
		ap.last();	ad.last();	ac.last();
		int apNum,adNum,acNum;
		Date apd,add,acd;
		int pi,di,ci;
		apNum = ap.getRow();	adNum = ad.getRow();	acNum = ac.getRow();
		pi=di=ci=1;
		ap.first();	ad.first();	ac.first();
		///
		out.append("num="+(apNum+adNum+acNum)+'\n' );
		while(pi<=apNum || di<=adNum || ci<=acNum){
			if(pi<=apNum)	apd = ap.getDate(4);
			else apd = null;
			
			if(di<=adNum)	add = ad.getDate(4);
			else add = null;
			
			if(ci<=acNum)	acd = ac.getDate(4);
			else acd = null;
			
			int pos = compareTime(apd,add,acd);
			switch(pos){
			case 1:
				outputAPSet(out, ap, pi, ndbody);
				pi++;
				break;
			case 2:
				outputADSet(out, ad, di, ndbody);
				di++;
				break;
			case 3:
				outputACSet(out, ac, ci);
				ci++;
				break;
			}
		}
	}
	
	public int compareTime(Date aptime, Date adtime, Date actime){
		Date max=aptime;
		if(max == null || ( adtime!=null && max.compareTo(adtime)>0 ) )
			max = adtime;
		if(max == null || ( actime!=null && max.compareTo(actime)>0 ) )
			max = actime;
		//
		if(max == null)
			return -1;
		if(max == aptime)
			return 1;
		if(max == adtime)
			return 2;
		return 3;
	}
	
	
}
