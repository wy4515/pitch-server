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

public class Comment extends AbstractServlet implements DbConfig {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
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

        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        try {
            jsonObject = new JSONObject(sb.toString());

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "INSERT INTO pitch_comment (pid, email, comment) values (?, ?, ?)";

            int pid = jsonObject.getInt("pid");
            String email = jsonObject.getString("email");
            String comment = jsonObject.getString("comment");

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, comment);

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
