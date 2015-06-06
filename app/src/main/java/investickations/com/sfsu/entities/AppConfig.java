package investickations.com.sfsu.entities;

/**
 * <p>
 * <tt>AppConfig</tt> class is the Application Level Configuration Class which contains all the
 * default and static final keywords as well as methods required through out the application.
 * </p>
 * Created by Pavitra on 6/5/2015.
 */
public class AppConfig {

    // Log message for Application Context
    public static final String LOGSTRING = "#LOG_INVEST=====> ";

    // Base url to the endpoint
    public static final String BASE_URL = "http://investickation.com";

    // Resource Identifiers
    public static final String USER_RESOURCE = "user";
    public static final String ACTIVITY_RESOURCE = "activities";
    public static final String OBSERVATION_RESOURCE = "observations";
    public static final String TICK_RESOURCE = "ticks";

    // HTTP verbs
    public static final String GET_VERB = "GET";
    public static final String POST_VERB = "POST";
    public static final String PUT_VERB = "PUT";
    public static final String DELETE_VERB = "DELETE";


}
