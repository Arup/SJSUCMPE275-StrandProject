package OneVillageGroup.webservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class GroupApplication extends Application {
    
    //private Set<Object> service_singletons = new HashSet<Object>();

    /**
     * Per-request service classes are instantiated for each request, and
     * disposed of after the request is processed.
     */
    /*
    private Set<Class<?>> service_classes = new HashSet<Class<?>>();

    public GroupApplication() {
        service_singletons.add(GroupServicesImpl.getInstance());
    }

    @Override
    public Set<Object> getSingletons() {
        return service_singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return service_classes;
    }
    */
    
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		serviceClasses.add(GroupServicesImpl.class);
		return serviceClasses;
	}
	
	 @Override
	  public Set<Object> getSingletons() {
	    Set<Object> s = new HashSet<Object>();
	    
	    // Register the Jackson provider for JSON
	    
	    // Make (de)serializer use a subset of JAXB and (afterwards) Jackson annotations
	    // See http://wiki.fasterxml.com/JacksonJAXBAnnotations for more information
	    ObjectMapper mapper = new ObjectMapper();
	    AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
	    AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
	    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
	    mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
	    mapper.getSerializationConfig().setAnnotationIntrospector(pair);
	    
	    // Set up the provider
	    JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
	    jaxbProvider.setMapper(mapper);
	    
	    s.add(jaxbProvider);
	    return s;
	  }	
	
	
}