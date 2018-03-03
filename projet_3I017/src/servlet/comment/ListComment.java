package servlet.comment;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet pour lister les commantaires d'un message.
 *
 */
public class ListComment extends HttpServlet
{
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String key = request.getParameter("key");
		String idMessage = request.getParameter("idMessage");
		
		PrintWriter out = response.getWriter();
		
		JSONObject res = service.CommentServices.listComment(key, idMessage);
		out.println(res);
	}
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		doGet(request, response);
	}
}
