package OneVillage.wink.webservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class BlogApplication extends Application {

    private Set<Object> service_singletons = new HashSet<Object>();

    /**
     * Per-request service classes are instantiated for each request, and
     * disposed of after the request is processed.
     */
    private Set<Class<?>> service_classes = new HashSet<Class<?>>();

    public BlogApplication() {
        service_singletons.add(BlogServicesImplementation.getInstance());
    }

    @Override
    public Set<Object> getSingletons() {
        return service_singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return service_classes;
    }
}