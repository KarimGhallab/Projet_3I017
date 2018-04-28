package servlet.user;

import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import org.json.JSONObject;

import service.UserServices;
import utils.Data;
import utils.ErrorJSON;

public class UploadImage extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException
   {    
      response.setContentType("text/plain");
      java.io.PrintWriter out = response.getWriter( );
      
      DiskFileItemFactory factory = new DiskFileItemFactory();
      
      ServletFileUpload upload = new ServletFileUpload(factory);
      try
      {
		List<FileItem> fileItems = upload.parseRequest(request);
		out.println(service.UserServices.uploadImage(fileItems).toString());
	
      }
      catch (Exception e)
      {
    	  out.println(ErrorJSON.defaultJsonError(e.getMessage(), Data.CODE_ERROR_DB));
      }
   }
      
      public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException
      {
        doGet(request, response);
      }
}