package magicbox.us.pitch.rest;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Video extends HttpServlet
        implements DbConfig {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        JSONObject respnoseJsonObject = new JSONObject();
        PrintWriter out = response.getWriter();

        try {
            int pid = Integer.parseInt(request.getParameter("pid"));

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement ps = conn.prepareStatement("SELECT video FROM pitch WHERE pid = ?");
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                respnoseJsonObject.put("Data", new String(imgBytes, "UTF-8"));
            }
            respnoseJsonObject.put("Success", true);
        } catch (Exception e) {
            respnoseJsonObject.put("Success", false);
            e.printStackTrace();
        }
        out.print(respnoseJsonObject);
        out.flush();
    }
}
