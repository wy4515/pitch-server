package magicbox.us.pitch.rest;

import magicbox.us.pitch.model.User;
import magicbox.us.pitch.model.UserBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.logging.Logger;

public class Register extends HttpServlet {

    private final String DB_URL = "jdbc:postgresql://pitch.cof6tchaa9lf.us-east-1.rds.amazonaws.com:5432/pitch";
    

    private final static Logger LOGGER = Logger.getLogger(Register.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("<html><body>Register...GET Request?</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            jsonObject = new JSONObject(sb.toString());

            User user = new UserBuilder()
                    .name(jsonObject.getString("name"))
                    .password(jsonObject.getString("password"))
                    .buildUser();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();

            String sql = "insert into pitch_user (name, password) values ('"+
                    user.getName() +"', '" + user.getPassword() +"')";

            stmt.executeUpdate(sql);

        } catch (JSONException e) {
            LOGGER.info("JSON parse error");
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.info(errors.toString());
            e.printStackTrace();
        }
    }
}
