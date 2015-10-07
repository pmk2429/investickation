package investickations.com.sfsu.entities;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * * TODO: Remove this class once the RetroFit is setup completely.
 * <p>
 * <tt>ResourceEndpoint</tt> defines the methods for Resource Class.
 * {@link investickations.com.sfsu.entities.RequestParams.Resource} provides a concrete implementation
 * of the <tt>ResourceEndpoint</tt> interface. All the methods defined reflects the action that
 * is performed on each resource using the endpoint URL.
 * </p>
 */
interface ResourceEndpoint {
    public void index();

    public void create();

    public void show();

    public void edit();

    public void destroy();
}

/**
 * * TODO: Remove this class once the RetroFit is setup completely.
 *
 * <p>The <tt>RequestParams </tt>class provides a generic controller for setting up a remote {@link HttpURLConnection } to
 * by building the URL using params in order to communicate via REST API. The parameterized constructor for the {@link RequestParams } expects a BASE_URL to where connection needs to be setup and the HTTP method Verb for the connection eg. GET, POST, DELETE etc.
 * The RequestParams follows the generic REST standards for setting up the connection and so it follows the requirement
 * for providing an ID as third parameter in order to build the URL.
 * <p/>
 * <p>
 * <tt>RequestParams</tt> uses HashMap to store the params key and value while retrieving the params from remote url or by posting the data to remote url
 * <p/>
 * <p>
 * <tt>RequestParams</tt> class is the interface to the REST endpoint URL.
 * Depending on each of the methods, a dynamic URL is constructed in {@link investickations.com.sfsu.entities.RequestParams} to which the connection is to be made and operations are to be performed.
 * <tt>RequestParams</tt> helps to perform operations on the resources of the applications by providing concrete
 * implementation of each methods and generating dynamic URL for each resource.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class RequestParams implements ResourceEndpoint {

    HashMap<String, String> params = new HashMap<String, String>();
    StringBuilder resourceUrl;
    Entity entityType;
    private String resourceIdentifier, resource_id, httpMethodVerb;

    // the constructor provides mechanism to pass resource name and id(if present).
    public RequestParams(String httpMethodVerb, String resourceIdentifier, String id, Entity entityType) {
        this.httpMethodVerb = httpMethodVerb;
        this.resourceIdentifier = resourceIdentifier;
        this.resource_id = id;
        this.entityType = entityType;
    }

    public Entity getEntityType() {
        return entityType.getEntity();
    }

    /**
     * returns the name of the Resource passed from the activity. Eg. User, Tick etc.
     *
     * @return
     */
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    /**
     * addParams() method is used to add the key-value pairs to the HashMap for building the url using params.
     *
     * @param key
     * @param value
     */
    public void addParams(String key, String value) {
        params.put(key, value);
    }

    /**
     * setConnection() method is used to set the connection between the Client(Android)
     * and the remote URL (Server) where the data is posted or retrieved.
     *
     * @return
     * @throws IOException
     */
    public HttpURLConnection setConnection() throws IOException {
        // convert the Url String to URL in order to establish the connection.
        URL url = new URL(getConnectionURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (httpMethodVerb.equals("GET")) {
            con.setRequestMethod("GET");
        } else if (httpMethodVerb.equals("DELETE")) {
            con.setRequestMethod("DELETE");
            con.setDoOutput(true);
        } else if (httpMethodVerb.equals("POST")) {
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(getEncodedParams());
            writer.flush();
        }
        return con;
    }

    /**
     * getEncodedParams() methods encodes the params passed using UTF-8 encoding and appends
     * it to the final url.
     *
     * @return
     */
    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            try {
                // encode the url
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                // append the value to key
                sb.append(key + "=" + value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * <tt>getConnectionURL()</tt> finally builds the connection url to which HTTP connection is to be made.
     *
     * @return
     */
    private String getConnectionURL() {
        return resourceUrl.toString() + getEncodedParams();
    }

    /**
     * index action returns the list of all the entries in resource.
     *
     * @return
     */
    @Override
    public void index() {
        resourceUrl = new StringBuilder();
        resourceUrl.append(AppConfig.BASE_URL + "/" + resourceIdentifier);
    }

    /**
     * create() method is used to create the specified resource and store in database.
     *
     * @return
     */
    @Override
    public void create() {
        resourceUrl = new StringBuilder();
        resourceUrl.append(AppConfig.BASE_URL + "/" + resourceIdentifier);
    }

    /**
     * show() method displays a specific resource by using ID of that resource.
     *
     * @return
     */
    @Override
    public void show() {
        resourceUrl = new StringBuilder();
        resourceUrl.append(AppConfig.BASE_URL + "/" + resourceIdentifier + "/" + resource_id);
    }

    /**
     * edit() method is called when we need to edit a resource.
     *
     * @return
     */
    @Override
    public void edit() {
        resourceUrl = new StringBuilder();
        resourceUrl.append(AppConfig.BASE_URL + "/" + resourceIdentifier + "/" + resource_id + "/edit");
    }

    /**
     * destroy() when called, destroys the record from database.
     *
     * @return
     */
    @Override
    public void destroy() {
        resourceUrl = new StringBuilder();
        resourceUrl.append(AppConfig.BASE_URL + "/" + resourceIdentifier + "/" + resource_id);
    }

}


