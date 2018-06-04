package servlet;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Patient;

/**
 * Servlet implementation class PatientLoginServlet
 */
@WebServlet(urlPatterns="/patient/login")
public class PatientLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Patient patient = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PatientLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append("");
		//获取用户名、密码
		String pid = request.getParameter("pid");
		String pwd = request.getParameter("pwd");
		ResultSet info = validateLogin(pid,pwd);
		if(info!=null){
			try {
				if(!info.absolute(1)){
					//无此用户
					response.getWriter().append("failed");
					return;
				}
				//有此用户，比对密码
				String pass = info.getString(info.findColumn("PPWD"));
				if(!pass.equals(pwd)){
					//密码错误
					response.getWriter().append("failed");
					return ;
				}
				//输出信息
				response.getWriter().append("accepted\n");
				outputPatientInfo(response, info);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			response.getWriter().append("failed");
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private ResultSet validateLogin(String userId, String pass){
		ResultSet result = null;
		long pid;
		if(userId.length() != 11 || pass.length() > 18)
			return result;
		try{
			pid = Long.parseLong(userId);
			//查看信息
			if(patient==null)
				patient = new Patient();
			result = patient.getPatientInfo(pid);
		}catch(NumberFormatException nfe){
			return null;
		}
		
		return result;
	}
	
	/**
	 * function：输出用户信息
	 * */
	private void outputPatientInfo(HttpServletResponse response, ResultSet resultSet){
		try{
			response.getWriter().append("PID=" + resultSet.getString("PID")+'\n');
			response.getWriter().append("PSEX=" + resultSet.getString("PSEX")+'\n');
			response.getWriter().append("PNAME=" + resultSet.getString("PNAME")+'\n');
			response.getWriter().append("PPWD=" + resultSet.getString("PPWD")+'\n');
			response.getWriter().append("PICON=" + resultSet.getString("PICON")+'\n');
			response.getWriter().append("ECOIN=" + resultSet.getString("ECOIN")+'\n');
			response.getWriter().append("PSCORE=" + resultSet.getString("PSCORE")+'\n');
			resultSet.close();
		}catch(SQLException e){
			System.err.println(e);
		}catch(IOException ioe){
			System.err.println(ioe);
		}
	}

}
