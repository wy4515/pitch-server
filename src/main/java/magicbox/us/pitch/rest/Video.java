package magicbox.us.pitch.rest;

import magicbox.us.pitch.database.DbConfig;
import org.json.JSONObject;

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
 * Fetch video by pitch_id
 */
public class Video extends AbstractServlet
        implements DbConfig {

    /**
     * Get request for video with pitch id
     * @param  pid query keyword
     * @param  respnoseJsonObject response in JSON format
     *                            success=true/false
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        JSONObject respnoseJsonObject = new JSONObject();
        PrintWriter out = response.getWriter();
        int pid = -1;
        try {
            pid = Integer.parseInt(request.getParameter("pid"));

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement ps = conn.prepareStatement("SELECT video FROM pitch WHERE pid = ?");
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                respnoseJsonObject.put("Data", new String(imgBytes, "UTF-8"));
            }
            respnoseJsonObject.put("success", true);
        } catch (Exception e) {
            respnoseJsonObject.put("success", false);
            e.printStackTrace();
        }
        out.print(respnoseJsonObject);
        out.flush();
    }

    @Override
    @Deprecated
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
