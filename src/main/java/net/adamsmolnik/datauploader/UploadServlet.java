package net.adamsmolnik.datauploader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import net.adamsmolnik.util.Configuration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    
    private static final long serialVersionUID = -4316988494460302168L;
    
    @Inject
    private Configuration conf;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        final String folder = request.getParameter("folder");
        final Part part = request.getPart("file");
        final String fileName = getName(part);
        try (InputStream input = part.getInputStream()) {
            AmazonS3Client s3Client = new AmazonS3Client();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(part.getSize());
            String objectKey = folder + "/" + fileName;
            s3Client.putObject("net.adamsmolnik.warsjawa", objectKey, input, objectMetadata);
            PrintWriter writer = response.getWriter();
            writer.append("Result OK, Object key " + fileName);
        }
    }

    private String getName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "default";
    }

}