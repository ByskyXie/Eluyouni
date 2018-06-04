package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/pic")
public class PictureServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public PictureServlet() {
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

		String id = request.getParameter("id");
		String picName = request.getParameter("pic");
		String picType = request.getParameter("pictype");
		String filename = null, path = null;
		ServletContext sc = getServletContext();
		if(picType==null || picType.isEmpty()){
			return;
		}else if(picType.equals("dicon")){
			path = sc.getRealPath("/resource/dicon/"+picName);
		}else if(picType.equals("picon")){
			path = sc.getRealPath("/resource/picon/"+picName);
		}
		if(!new File(path).exists()){
			System.err.println("不存在该文件："+picName);
			return;
		}
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream(path) );
		filename = path.substring(path.lastIndexOf('-')+1);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="+filename);
		//响应长度
		response.addHeader("Content-Length", ""+new File(path).length());
		response.setContentType("image/jpeg;charset-utf-8");
		BufferedOutputStream client = new BufferedOutputStream(response.getOutputStream());
		byte[] bytes = new byte[2000];
		do{
			int i = bis.read(bytes);
			if(i==-1){
				bis.close();
				break;			
			}
			client.write(bytes, 0, i);
		}while(true);
		client.flush();	client.close();
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

}
