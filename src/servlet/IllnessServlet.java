package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.Result;

import classes.DBHelper;

@WebServlet(urlPatterns="/medicine")
public class IllnessServlet extends HttpServlet {
	
	private static final int MAX_FAME_NUM = 9;
	
	private static final String SQL_ILLNESS= ""
			+ "SELECT DILLNESS "
			+ "FROM DOCTOR "
			+ "GROUP BY DILLNESS;";
	private static final String SQL_FAME= ""
			+ "SELECT * "
			+ "FROM DOCTOR "
			+ "WHERE DGRADE>3 "
			+ "ORDER BY DMARKING DESC;";

	/**
		 * Constructor of the object.
		 */
	public IllnessServlet() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String pid = request.getParameter("pid");
		String req = request.getParameter("req");
		try{
			if(req.equals("illness")){
				//¼²²¡
				ResultSet result = getIllness();
				outputIllness(response,result);
				result.close();
			}else if(req.equals("fame")){
				ResultSet result = getFame();
				outputFame(response,result);
				result.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		out.flush();	out.close();
			
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
	
	public ResultSet getIllness() {
		DBHelper helper = new DBHelper(SQL_ILLNESS);
		try{
			return helper.execute();
		}catch(SQLException se){
			se.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getFame() {
		DBHelper helper = new DBHelper(SQL_FAME);
		try{
			return helper.execute();
		}catch(SQLException se){
			se.printStackTrace();
			return null;
		}
	}

	private void outputIllness(HttpServletResponse response, ResultSet result){
		try{
			result.last();
			int num = result.getRow();
			int colum = result.findColumn("DILLNESS");
			response.getWriter().append( "num="+num+'\n' );
			for(int i=1; i<=num; i++){
				result.absolute(i);
				response.getWriter().append( "illness="+result.getString(colum)+'\n' );
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private void outputFame(HttpServletResponse response, ResultSet result){
		try{
			result.last();
			int num = result.getRow();
			if(num>MAX_FAME_NUM)
				num=MAX_FAME_NUM;	//¸ø³ö9¸ö
			response.getWriter().append( "num="+num+'\n' );
			for(int i=1; i<=num; i++){
				result.absolute(i);
				response.getWriter().append( "did="+result.getString(result.findColumn("DID"))+'\n' );
				response.getWriter().append( "dsex="+result.getString(result.findColumn("DSEX"))+'\n' );
				response.getWriter().append( "dname="+result.getString(result.findColumn("DNAME"))+'\n' );
				response.getWriter().append( "dicon="+result.getString(result.findColumn("DICON"))+'\n' );
				response.getWriter().append( "dillness="+result.getString(result.findColumn("DILLNESS"))+'\n' );
				response.getWriter().append( "dgrade="+result.getString(result.findColumn("DGRADE"))+'\n' );
				response.getWriter().append( "dprofess="+result.getString(result.findColumn("DPROFESS"))+'\n' );
				response.getWriter().append( "dcareer="+result.getString(result.findColumn("DCAREER"))+'\n' );
				response.getWriter().append( "dhospital="+result.getString(result.findColumn("DHOSPITAL"))+'\n' );
				response.getWriter().append( "dmarking="+result.getString(result.findColumn("DMARKING"))+'\n' );
				response.getWriter().append( "d24hreply="+result.getString(result.findColumn("D24HREPLY"))+'\n' );
				response.getWriter().append( "dpatient_num="+result.getString(result.findColumn("DPATIENT_NUM"))+'\n' );
				response.getWriter().append( "dhot_level="+result.getString(result.findColumn("DHOT_LEVEL"))+'\n' );
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
}
