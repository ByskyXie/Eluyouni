package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.DBHelper;

@WebServlet(urlPatterns="/doctorlist")
public class DoctorListServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public DoctorListServlet() {
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
		out.append("Error method");
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
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		BufferedReader br = new BufferedReader( new InputStreamReader(  request.getInputStream() ));
		//获得输入流
		PrintWriter out = response.getWriter();
		String param = br.readLine();		param = URLDecoder.decode(param,"UTF-8");	//转换为中文
		String ill,pid;
		///读取完成
		ill = param.substring(param.indexOf('=')+1, param.indexOf('&'));
		pid = param.substring(param.lastIndexOf('=')+1);
		br.close();
		try{
			//疾病
			ResultSet result = getDoctorList(ill);
			outputDoctorList(response,result);
			if(result != null)
				result.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		out.flush();	out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

	public ResultSet getDoctorList(String ill){
		String sql = ""
				+ "SELECT * "
				+ "FROM DOCTOR "
				+ "WHERE DILLNESS='"+ill+"' "
				+ "ORDER BY DGRADE DESC;";
		if(ill.equals("名医达人")){
			sql = ""
				+ "SELECT * "
				+ "FROM DOCTOR "
				+ "WHERE DGRADE >= 3 "	//说明要的是名医列表
				+ "ORDER BY DGRADE DESC, DHOT_LEVEL DESC;";
		}
		DBHelper helper = new DBHelper(sql);
		try{
			ResultSet set = helper.execute();
			return set;
		}catch(SQLException se){
			se.printStackTrace();
			return null;
		}
	}
	
	private void outputDoctorList(HttpServletResponse response, ResultSet result){
		if(result == null)
			return;
		try{
			result.last();
			int num = result.getRow();
			response.getWriter().append( "num="+num+'\n' );
			int did=result.findColumn("DID"),dsex=result.findColumn("DSEX"),dname=result.findColumn("DNAME")
					,dicon=result.findColumn("DICON"),dillness=result.findColumn("DILLNESS"),dgrade=result.findColumn("DGRADE")
					,dprofess=result.findColumn("DPROFESS"),dcareer=result.findColumn("DCAREER"),dhospital=result.findColumn("DHOSPITAL")
					,dmarking=result.findColumn("DMARKING"),d24h=result.findColumn("D24HREPLY"),dpatient_num=result.findColumn("DPATIENT_NUM")
					,dhot_level=result.findColumn("DHOT_LEVEL");
			for(int i=1; i<=num; i++){
				result.absolute(i);
				response.getWriter().append( "did="+result.getString(did)+'\n' );
				response.getWriter().append( "dsex="+result.getString(dsex)+'\n' );
				response.getWriter().append( "dname="+result.getString(dname)+'\n' );
				response.getWriter().append( "dicon="+result.getString(dicon)+'\n' );
				response.getWriter().append( "dillness="+result.getString(dillness)+'\n' );
				response.getWriter().append( "dgrade="+result.getString(dgrade)+'\n' );
				response.getWriter().append( "dprofess="+result.getString(dprofess)+'\n' );
				response.getWriter().append( "dcareer="+result.getString(dcareer)+'\n' );
				response.getWriter().append( "dhospital="+result.getString(dhospital)+'\n' );
				response.getWriter().append( "dmarking="+result.getString(dmarking)+'\n' );
				response.getWriter().append( "d24hreply="+result.getString(d24h)+'\n' );
				response.getWriter().append( "dpatient_num="+result.getString(dpatient_num)+'\n' );
				response.getWriter().append( "dhot_level="+result.getString(dhot_level)+'\n' );
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

}
