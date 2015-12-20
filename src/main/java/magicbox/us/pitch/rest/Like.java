/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import magicbox.us.pitch.database.DbConfig;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Like api for servlet
 * increase like count by one if success
 */
public class Like extends AbstractServlet
        implements DbConfig {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM pitch_like where pid=?";

        JSONObject jsonObject = new JSONObject();
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement(sql);

            int pid = Integer.parseInt(request.getParameter("pid"));

            preparedStatement.setInt(1, pid);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            response.setContentType("application/json");

            while (resultSet.next()) {

                jsonObject.put("pid", resultSet.getInt(1));
                jsonObject.put("count", resultSet.getInt(2));

            }
            jsonObject.put("success", true);
        } catch (Exception e) {
            jsonObject.put("success", false);
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }

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

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "UPDATE pitch_like SET count=count+1 WHERE pid=?";

            int pid = jsonObject.getInt("pid");

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, pid);

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();

            respnoseJsonObject.put("success", true);
        } catch (Exception e) {
            respnoseJsonObject.put("success", false);
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.print(respnoseJsonObject);
        out.flush();
    }
}
