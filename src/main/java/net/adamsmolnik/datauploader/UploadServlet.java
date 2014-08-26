package net.adamsmolnik.datauploader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import net.adamsmolnik.base.AbstractServiceServlet;
import net.adamsmolnik.entity.EntityReference;
import net.adamsmolnik.provider.EntityProvider;

/**
 * @author ASmolnik
 *
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class UploadServlet extends AbstractServiceServlet {

    private static final long serialVersionUID = -4316988494460302168L;

    @Inject
    private EntityProvider entityProvider;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String folder = request.getParameter("folder");
        Part part = request.getPart("file");
        String fileName = getName(part);
        String objectKey = folder + "/" + fileName;
        try (InputStream is = part.getInputStream()) {
            entityProvider.persist(new EntityReference(objectKey), part.getSize(), is);
        }
        request.setAttribute("objectKey", objectKey);
        response.sendRedirect("processingLauncher.jsp?objectKey=" + URLEncoder.encode(objectKey, StandardCharsets.UTF_8.name()));
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
