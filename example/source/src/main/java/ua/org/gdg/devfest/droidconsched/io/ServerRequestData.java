package ua.org.gdg.devfest.droidconsched.io;

import org.json.JSONException;

/**
 * Base Class for classes hold data which is sent to the server
 */
public abstract class ServerRequestData {
    /**
     * Convert the class data into a JSON string.
     */

    public abstract String toJSON() throws JSONException;
}
