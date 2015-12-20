/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import magicbox.us.pitch.database.DbAdapter;
import magicbox.us.pitch.database.DbConfig;
import magicbox.us.pitch.exception.ExceptionLog;
import magicbox.us.pitch.model.User;
import magicbox.us.pitch.model.UserBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import java.util.logging.Logger;

/**
 * handle both validation check and new user register
 */
public class Register extends AbstractServlet
    implements DbConfig {

    private final static Logger LOGGER = Logger.getLogger(Register.class.getName());
    private ExceptionLog exceptionLog = new ExceptionLog();
    private DbAdapter dbAdapter = null;

    /**
     * get setting configure from web.xml
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // read the uploadDir from the servlet parameters
        try {
            dbAdapter = new DbAdapter();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionLog.log(e.getMessage());
        }
    }

    /**
     * user email-password validation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM pitch_user where email=? and password=?";

        try {
            Class.forName("org.postgresql.Driver");
            // TODO: check if this replacement work
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn = dbAdapter.getDbConnection();
            preparedStatement = conn.prepareStatement(sql);

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            JSONObject jsonObject = new JSONObject();
            response.setContentType("application/json");

            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");

                jsonObject.put("success", true);
            }
            else {
                jsonObject.put("success", false);
            }
            PrintWriter out = response.getWriter();
            out.print(jsonObject);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * new user register
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
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
        PreparedStatement preparedStatement = null;
        Connection conn = null;

        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        try {
            jsonObject = new JSONObject(sb.toString());

            User user = new UserBuilder()
                    .email(jsonObject.getString("email"))
                    .password(jsonObject.getString("password"))
                    .skills(jsonObject.getString("skills"))
                    .buildUser();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "INSERT INTO pitch_user (name, password, email, headline, pictureurl, pitchable, skills) values (?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getHeadline());
            preparedStatement.setString(5, user.getPictureUrl());
            preparedStatement.setBoolean(6, user.isPitchable());
            preparedStatement.setString(7, user.getSkills());

            preparedStatement.executeUpdate();

            respnoseJsonObject.put("success", true);
        } catch (JSONException e) {
            LOGGER.info("JSON parse error");
            respnoseJsonObject.put("success", false);
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        } catch (Exception e) {
            respnoseJsonObject.put("success", false);

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.info(errors.toString());
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.print(respnoseJsonObject);
        out.flush();
    }
}
