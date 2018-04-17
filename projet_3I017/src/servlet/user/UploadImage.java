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

public class UploadImage extends HttpServlet
{
   
   private boolean isMultipart;
   private String filePath;
   private File file ;

   public void init( )
   {
      // Get the file location where it would be stored.
      //filePath = getServletContext().getInitParameter("file-upload");
      filePath = "/users/nfs/Etu8/3772468/twister_images/";
      //filePath = "/users/nfs/Etu8/3674226/twister_images/";
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
   
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/plain");
      java.io.PrintWriter out = response.getWriter( );
      
      out.println("1");
      DiskFileItemFactory factory = new DiskFileItemFactory();
      
      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      out.println("2");
      try { 
         // Parse the request to get file items.
         List<FileItem> fileItems = upload.parseRequest(request);
	
         // Process the uploaded file items
         Iterator i = fileItems.iterator();
         
         out.println("3");
         while ( i.hasNext () ) {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () )
            {
               // Get the uploaded file parameters
               String fileName = fi.getName();
               out.println("filePath : " + filePath);
               out.println("fileName : " + fileName);
            
               
               file = new File( filePath + fileName);
               
               // Write the file
               fi.write( file ) ;
               out.println(filePath + fileName);
            }
         }
         } catch(Exception ex) {
            out.println("error : " + ex.getMessage());
         }
      }
      
      public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        doGet(request, response);
      }
}