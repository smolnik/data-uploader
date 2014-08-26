package net.adamsmolnik.base;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import net.adamsmolnik.setup.ServiceNameResolver;
import net.adamsmolnik.util.Configuration;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;

/**
 * @author ASmolnik
 *
 */
public abstract class AbstractServiceServlet extends HttpServlet {

    private static final long serialVersionUID = -3773579110137925638L;

    @Inject
    protected ServiceNameResolver snr;

    @Inject
    protected Configuration conf;

    protected AmazonSimpleWorkflow wf;

    protected AmazonS3Client s3Client;

    @Override
    public void init() throws ServletException {
        ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70 * 1000);
        s3Client = new AmazonS3Client();
        wf = new AmazonSimpleWorkflowClient(config);
        wf.setEndpoint(conf.getServiceValue(snr.getServiceName(), "swf.endpoint"));
    }

}
