/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Abstract servlet super class. Define every sub-class should implement get/post methods
 * and return success information through a JSON object.
 */
public abstract class AbstractServlet extends HttpServlet {

    // return result
    private JSONObject jsonObject = new JSONObject();

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
