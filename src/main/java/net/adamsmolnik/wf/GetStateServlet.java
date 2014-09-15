package net.adamsmolnik.wf;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import net.adamsmolnik.datauploader.TextOutputServlet;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternal;
import net.adamsmolnik.workflow.DataProcessingWorkflowClientExternalFactoryImpl;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;

/**
 * @author ASmolnik
 *
 */
@WebServlet(name = "GetStateServlet", urlPatterns = {"/getstate"})
public class GetStateServlet extends TextOutputServlet {

    private static final long serialVersionUID = -2488497460307138L;

    @Override
    protected String getTextToBeSent(HttpServletRequest request) {
        DataProcessingWorkflowClientExternal client = new DataProcessingWorkflowClientExternalFactoryImpl(wf, conf.getServiceValue(
                conf.getServiceName(), "swf.domain")).getClient(new WorkflowExecution().withWorkflowId(request.getParameter("workflowId")).withRunId(
                request.getParameter("runId")));
        String state = client.getState();
        return state == null ? "---" : state;
    }

}