package magicbox.us.pitch.rest;

import magicbox.us.pitch.model.User;
import magicbox.us.pitch.model.UserBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import java.util.logging.Logger;

public class Register extends HttpServlet
    implements DbConfig {

    private final static Logger LOGGER = Logger.getLogger(Register.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM pitch_user where name=? and password=?";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement(sql);

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                session.setAttribute("uid", uid);

                response.setStatus(response.SC_OK);
                response.getWriter().println("FOUND");
            }
            else {
                response.sendError(response.SC_NOT_FOUND, "user does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        PreparedStatement preparedStatement = null;
        Connection conn = null;

        try {
            jsonObject = new JSONObject(sb.toString());

            User user = new UserBuilder()
                    .name(jsonObject.getString("name"))
                    .password(jsonObject.getString("password"))
                    .buildUser();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

//            stmt = conn.createStatement();

//            String sql = "insert into pitch_user (name, password) values ('"+
//                    user.getName() +"', '" + user.getPassword() +"')";
            String sql = "INSERT INTO pitch_user (name, password, email, headline, pictureurl, pitchable) values (?, ?, ?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getHeadline());
            preparedStatement.setString(5, user.getPictureUrl());
            preparedStatement.setBoolean(6, user.isPitchable());

            preparedStatement.executeUpdate(sql);

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
