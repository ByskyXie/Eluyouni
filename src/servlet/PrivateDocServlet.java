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

import classes.DBHelper;

@WebServlet(urlPatterns="/privatedoc")
public class PrivateDocServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public PrivateDocServlet() {
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
		response.getWriter().append("");
		//记录该用户名请求数据
		String pid = request.getParameter("pid");
		String startPos = request.getParameter("startpos");
		if(pid==null){
			response.getWriter().append("failed\nfalse request");
			response.getWriter().close();
			return;
		}
		ResultSet result = getPrivateDoctorList(Long.parseLong(pid));
		if(result == null){
			response.getWriter().append("failed\nserver error");
			response.getWriter().close();
			return;
		}
		try{
			int pos = Integer.parseInt(startPos);
			
			if(!result.absolute(pos)){
				//错误位置
				response.getWriter().append("failed\nfalse position "+pos);
			}else{
				//输出list
				outputPrivateDoctorList(response, result, Integer.parseInt(startPos));
			}
			result.close();
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
			response.getWriter().append("failed\nfalse position format");
		}catch(SQLException e){
			e.printStackTrace();
		}
		response.getWriter().flush();
		response.getWriter().close();
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
	
	private void outputPrivateDoctorList(HttpServletResponse response, ResultSet result, int startPos) throws IOException, SQLException{
		response.getWriter().append("accepted\n");
		result.last();
		int num = result.getRow();
		int limit;
		result.first();
		//看num有多大
		if(startPos>num)
			return;
		else if(num-startPos>20)
			limit = startPos+20;
		else 
			limit = num;
		response.getWriter().append("num="+(limit-startPos+1)+"\n");		
		//输出
		for(int i=startPos;i<=limit;i++){
			result.absolute(i);
			response.getWriter().append("adid="+result.getString(result.findColumn("DID"))+'\n');
			response.getWriter().append("dsex="+result.getString(result.findColumn("DSEX"))+'\n');
			response.getWriter().append("dname="+result.getString(result.findColumn("DNAME"))+'\n');
			response.getWriter().append("dicon="+result.getString(result.findColumn("DICON"))+'\n');
			response.getWriter().append("dillness="+result.getString(result.findColumn("DILLNESS"))+'\n');
			response.getWriter().append("dgrade="+result.getString(result.findColumn("DGRADE"))+'\n');
			response.getWriter().append("dhospital="+result.getString(result.findColumn("DHOSPITAL"))+'\n');
		}
	}
	
	private ResultSet getPrivateDoctorList(Long pid){
		//自然连接
		String sql = "SELECT * "+
			"FROM PRIVATE_DOCTOR NATURAL JOIN DOCTOR "+
			"WHERE PID="+ pid +";";

		ResultSet result = null;
		try{
			DBHelper helper = new DBHelper(sql);
			result = helper.execute();
		}catch(SQLException e){
			System.err.println(e);
			e.printStackTrace();
		}
		return result;
	}

}
