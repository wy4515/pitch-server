/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import magicbox.us.pitch.database.DbConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Get a list of experts {email, skills} and send to system for recommendation and matching
 */
public class Expert extends AbstractServlet
        implements DbConfig {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;
        PrintWriter out = response.getWriter();

        JsonObject jsonObject = new JsonObject();

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT email, skills FROM pitch_user";
            preparedStatement = conn.prepareStatement(sql);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            Gson gson = new Gson();

            JsonArray jsonArr = new JsonArray();
            while (resultSet.next()) {
                JsonObject tmp = new JsonObject();
                tmp.addProperty("email", resultSet.getString(1));
                tmp.addProperty("skill", resultSet.getString(2));

                jsonArr.add(tmp);
            }
            jsonObject.add("experts", jsonArr);
            jsonObject.addProperty("success", true);
            out.print(jsonObject);
        } catch (Exception e) {
            jsonObject.addProperty("success", false);
            e.printStackTrace();
        }
        response.setContentType("application/json");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
/**
 * Parser helper class
 */
class EmailSkill {
    String email;
    String skill;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTags(String skill) {
        this.skill = skill;
    }
}