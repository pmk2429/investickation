package investickations.com.sfsu.entities;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>The <tt>RequestParams </tt>class provides a generic controller for setting up a remote {@link HttpURLConnection } to
 * by building the URL using params in order to communicate via REST API. The parameterized constructor for the {@link RequestParams } expects a BASE_URL to where connection needs to be setup and the HTTP method Verb for the connection eg. GET, POST, DELETE etc.
 * The RequestParams follows the generic REST standards for setting up the connection and so it follows the requirement
 * for providing an ID as third parameter in order to build the URL.
 * <p/>
 * <p>
 * RequestParams uses HashMap to store the params key and value while retrieving the params from remote url or by posting the data to remote url
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class RequestParams {
    private static String BASE_URL = "http://pavitrakansara.com/larv/admin/dashboard/";
    String httpMethodVerb, resourceIdentifierName, id;
    boolean editParam;
    HashMap<String, String> params = new HashMap<String, String>();

    public RequestParams(String httpMethodVerb, String resourceIdentifierName, String id) {
        this.httpMethodVerb = httpMethodVerb;
        this.resourceIdentifierName = resourceIdentifierName;
        this.id = id;
    }

    public RequestParams(String httpMethodVerb, String resourceIdentifierName, String id, boolean editParam) {
        this.httpMethodVerb = httpMethodVerb;
        this.resourceIdentifierName = resourceIdentifierName;
        this.id = id;
        this.editParam = editParam;
    }

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
        URL url = new URL(getBaseURL());
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

    private String getBaseURL() {
        return BASE_URL + getEncodedParams();
    }
}


