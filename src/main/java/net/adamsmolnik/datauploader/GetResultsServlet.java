package net.adamsmolnik.datauploader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import net.adamsmolnik.entity.EntityReference;
import net.adamsmolnik.provider.EntityProvider;
import net.adamsmolnik.util.StreamReader;

/**
 * @author ASmolnik
 *
 */
@WebServlet(name = "GetResultsServlet", urlPatterns = {"/results"})
public class GetResultsServlet extends TextOutputServlet {

    private static final long serialVersionUID = -2488497460307138L;

    @Inject
    private EntityProvider entityProvider;

    @Override
    protected String getTextToBeSent(HttpServletRequest request) {
        EntityReference objEntityReference = new EntityReference(request.getParameter("objectKey"));
        Map<String, String> md = entityProvider.getMetadata(objEntityReference);
        String destObjectKey = md.get("destObjectKey".toLowerCase());
        Map<String, String> mdMap = entityProvider.getMetadata(new EntityReference(destObjectKey));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> mapEntry : mdMap.entrySet()) {
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            if ("extractioninfoobjectkey".equals(key)) {
                InputStream is = entityProvider.getEntity(new EntityReference(value)).getInputStream();
                byte[] bytes = StreamReader.getBytes(is);
                String contents = " [" + new String(bytes, StandardCharsets.UTF_8) + "]";
                sb.append(contents);
            }
            sb.append("<br>\n");
        }
        entityProvider.delete(objEntityReference);
        return sb.toString();
    }

}