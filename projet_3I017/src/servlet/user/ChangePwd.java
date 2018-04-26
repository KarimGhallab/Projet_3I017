package servlet.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ChangePwd  extends HttpServlet{


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String ancien = request.getParameter("ancien");
		
		String pwd1 = request.getParameter("pwd1");
		
		String pwd2 = request.getParameter("pwd2");
		
		String key = request.getParameter("key");
		
		JSONObject json = service.UserServices.ChangePwd(ancien, pwd1 , pwd2 , key);
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/plain");
		out.println(json.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
