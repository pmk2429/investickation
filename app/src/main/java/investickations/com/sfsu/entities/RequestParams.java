package investickations.com.sfsu.entities;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Pavitra on 5/19/2015.
 */

/**
 * <p>The <tt>RequestParams </tt>class provides a generic controller for setting up a remote {@link HttpURLConnection }
 * The parameterized constructor for the {@link RequestParams } expects a baseURL to where connection
 * needs to be setup and the HTTP method for the connection eg. GET, POST, DELETE etc.
 * <p/>
 * <p>
 * RequestParams also uses HashMap to store the params key and value while retriving the params
 * from remote url or by posting the data to remote url
 * </p>
 */
public class RequestParams {
    String baseURL, method;
    HashMap<String, String> params = new HashMap<String, String>();

    public RequestParams(String baseURL, String method) {
        this.baseURL = baseURL;
        this.method = method;
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
        if (method.equals("GET")) {
            con.setRequestMethod("GET");
        } else if (method.equals("DELETE")) {
            con.setRequestMethod("DELETE");
            con.setDoOutput(true);
        } else if (method.equals("POST")) {
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

    public String getEncodedUrl() {
        return this.baseURL + "?" + getEncodedParams();
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}


