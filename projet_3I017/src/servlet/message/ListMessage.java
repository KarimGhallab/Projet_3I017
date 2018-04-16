package servlet.message;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet pour lister les messages de l'utilisateur.
 *
 */
public class ListMessage extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String idUser = request.getParameter("idUser");
		String orderAsc = request.getParameter("orderAsc");
		String from = request.getParameter("from");
		String nbMessage = request.getParameter("nbMessage");
		String amis = request.getParameter("amis");
		
		JSONObject json = service.MessageServices.listMessage(idUser, orderAsc, from, nbMessage, amis);
		
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
