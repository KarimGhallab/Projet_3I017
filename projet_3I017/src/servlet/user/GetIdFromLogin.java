package servlet.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class GetIdFromLogin extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String login = request.getParameter("loginUser");
		
		JSONObject json = service.UserServices.getIdFromLogin(login);
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/plain");
		out.println(json.toString());
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException
	{
		doGet(req, resp);
	}
}
