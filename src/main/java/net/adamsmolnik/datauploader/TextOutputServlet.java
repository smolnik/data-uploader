package net.adamsmolnik.datauploader;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.adamsmolnik.base.AbstractServiceServlet;

/**
 * @author ASmolnik
 *
 */
public abstract class TextOutputServlet extends AbstractServiceServlet {

    private static final long serialVersionUID = -5289457694390452171L;

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(getTextToBeSent(request));
        response.flushBuffer();
    }

    protected abstract String getTextToBeSent(HttpServletRequest request);
}
