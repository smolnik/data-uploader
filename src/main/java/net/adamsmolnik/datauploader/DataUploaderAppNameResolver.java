package net.adamsmolnik.datauploader;

import javax.inject.Singleton;
import net.adamsmolnik.setup.ServiceNameResolver;

/**
 * @author ASmolnik
 *
 */
@Singleton
public class DataUploaderAppNameResolver implements ServiceNameResolver {

    @Override
    public String getServiceName() {
        return "data-uploader-app";
    }

}
