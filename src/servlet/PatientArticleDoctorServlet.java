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

@WebServlet(urlPatterns="/doctorarticle")
public class PatientArticleDoctorServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String DOCTOR_ARTICLE_SQL = "SELECT * "
			+ "FROM ARTICLE_DOCTOR "
			+ "ORDER BY TIME DESC;";
	/**
		 * Constructor of the object.
		 */
	public PatientArticleDoctorServlet() {
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
		String ndbody = request.getParameter("ndbody");
		if(pid==null){
			response.getWriter().append("failed\nfalse request");
			response.getWriter().close();
			return;
		}
		ResultSet result = getDoctorArticleList();
		if(result == null){
			response.getWriter().append("failed\nserver error");
			response.getWriter().close();
			return;
		}
		try{
			int pos = Integer.parseInt(startPos);
			
			if(!result.absolute(pos)){
				//错误位置
				response.getWriter().append("failed\nfalse position");
			}else{
				//输出list
				if(ndbody.matches("true.*")){					
					outputDoctorArticleList(response, result, Integer.parseInt(startPos), true);
				}else if(ndbody.matches("false.*")){
					outputDoctorArticleList(response, result, Integer.parseInt(startPos), false);
				}
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

	private void outputDoctorArticleList(HttpServletResponse response, ResultSet result, int startPos, boolean ndbody) throws IOException, SQLException{
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
			response.getWriter().append("adid="+result.getString(1)+'\n');
			response.getWriter().append("did="+result.getString(2)+'\n');
			response.getWriter().append("title="+result.getString(3)+'\n');
			response.getWriter().append("time="+result.getString(4)+'\n');
			if(ndbody){
				response.getWriter().append("content="+result.getString(5)+'\n');
				response.getWriter().append("end=end"+'\n');	//结束
			}
		}
	}
	
	private ResultSet getDoctorArticleList(){
		ResultSet result = null;
		try{
			DBHelper helper = new DBHelper(DOCTOR_ARTICLE_SQL);
			result = helper.execute();
		}catch(SQLException e){
			System.err.println(e);
			e.printStackTrace();
		}
		return result;
	}

	
}
