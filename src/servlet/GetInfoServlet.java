package servlet;

import java.io.BufferedReader;
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

import bean.Doctor;
import bean.Patient;

@WebServlet(urlPatterns="/baseinfo")
public class GetInfoServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GetInfoServlet() {
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
		//获取用户名、密码
		String reqid = request.getParameter("reqid");
		String erid = request.getParameter("erid");
		String ertype = request.getParameter("ertype");
		if(ertype.equals("1")){
			//patient info
			try {
				Patient patient = new Patient();
				ResultSet info = patient.getPatientInfo(Long.parseLong(erid));
				if(!info.absolute(1)){
					//无此用户
					response.getWriter().append("failed\nno such patient");
					return;
				}
				//输出信息
				response.getWriter().append("accepted\n");
				outputPatientBaseInfo(response, info);
				info.close();
				patient.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(NumberFormatException nfe){
				nfe.printStackTrace();
				response.getWriter().append("failed\nerror erid format");
			}

		}else if(ertype.equals("2")){
			// doctor
			try {
				Doctor doctor = new Doctor();
				ResultSet info = doctor.getPatientInfo(Long.parseLong(erid));
				if(!info.absolute(1)){
					//无此用户
					response.getWriter().append("failed\nno such doctor");
					return;
				}
				//输出信息
				response.getWriter().append("accepted\n");
				outputDoctorBaseInfo(response, info);
				info.close();
				doctor.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(NumberFormatException nfe){
				nfe.printStackTrace();
				response.getWriter().append("failed\nerror erid format");
			}
			
		}else{
			response.getWriter().append("failed\nerror request");
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
	
	private void outputPatientBaseInfo(HttpServletResponse response, ResultSet resultSet){
		try{
			response.getWriter().append("PID=" + resultSet.getString("PID")+"\n");
			response.getWriter().append("PSEX=" + resultSet.getString("PSEX")+"\n");
			response.getWriter().append("PNAME=" + resultSet.getString("PNAME")+"\n");
			response.getWriter().append("PICON=" + resultSet.getString("PICON")+"\n");
			response.getWriter().append("PSCORE=" + resultSet.getString("PSCORE")+"\n");
		}catch(SQLException e){
			System.err.println(e);
		}catch(IOException ioe){
			System.err.println(ioe);
		}
	}
	
	private void outputDoctorBaseInfo(HttpServletResponse response, ResultSet result){
		try{
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
		}catch(SQLException e){
			System.err.println(e);
		}catch(IOException ioe){
			System.err.println(ioe);
		}
	}

}
