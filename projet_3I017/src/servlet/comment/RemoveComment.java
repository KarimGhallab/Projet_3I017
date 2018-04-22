package servlet.comment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * 
 * Servlet de suppression de commentaire.
 */
public class RemoveComment extends HttpServlet 
{
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String key = request.getParameter("key");
		String idCommentaire = request.getParameter("idCommentaire");
		String idMessage = request.getParameter("idMessage");
		
		PrintWriter out = response.getWriter();
		
		JSONObject res = service.CommentServices.removeComment(key, idCommentaire , idMessage);
		out.println(res);
	}
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		doGet(request, response);
	}
}

