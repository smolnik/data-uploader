package net.adamsmolnik.wf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import net.adamsmolnik.base.AbstractServiceServlet;
import net.adamsmolnik.workflow.ActionType;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternal;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternalFactoryImpl;

/**
 * @author ASmolnik
 *
 */
@WebServlet(name = "ProcessingLauncherServlet", urlPatterns = {"/processinglauncher"})
public class ProcessingLauncherServlet extends AbstractServiceServlet {

    private static final long serialVersionUID = 1637233346212064507L;

    private static class LaunchParams {

        private String workflowId;

        private String runId;

        private LaunchParams(String workflowId, String runId) {
            this.workflowId = workflowId;
            this.runId = runId;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String objectKey = request.getParameter("objectKey");
        String[] actionTypesParamValues = request.getParameterValues("actionTypes");
        Set<ActionType> actionTypes = new HashSet<>();
        for (String actionTypeValue : actionTypesParamValues) {
            actionTypes.add(ActionType.valueOf(actionTypeValue));
        }
        LaunchParams lp = launchWF(objectKey, actionTypes);
        response.sendRedirect("results.jsp?objectKey=" + encode(objectKey) + "&workflowId=" + encode(lp.workflowId) + "&runId=" + encode(lp.runId));
    }

    private String encode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
    }

    private LaunchParams launchWF(String objectKey, Set<ActionType> actionTypes) {
        Map<String, String> confMap = conf.getServiceConfMap(conf.getServiceName());
        DataProcessingWorkflowClientExternal client = new DataProcessingWorkflowClientExternalFactoryImpl(wf, confMap.get("swf.domain")).getClient();
        client.launch(conf.getGlobalValue("bucketName"), objectKey, actionTypes);
        WorkflowExecution we = client.getWorkflowExecution();
        return new LaunchParams(we.getWorkflowId(), we.getRunId());
    }

}