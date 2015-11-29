package magicbox.us.pitch.rest;

import magicbox.us.pitch.model.PitchBuilder;
import magicbox.us.pitch.model.PitchEntity;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * To schedule a pitch
 */
public class Pitch extends HttpServlet
        implements DbConfig {

    private final static Logger LOGGER = Logger.getLogger(Pitch.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // uid=-1 should consider as error
        int uid = -1;
        try {
            uid = (Integer)session.getAttribute("uid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM pitch where uid=?";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, uid);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            JSONObject jsonObject = new JSONObject();

            while (resultSet.next()) {
                JSONObject pitch = new JSONObject();

                pitch.put("pid", resultSet.getInt(1));
                pitch.put("tittle", resultSet.getString(2));
                pitch.put("description", resultSet.getString(3));
                pitch.put("video_url", resultSet.getString(4));
                pitch.put("date", resultSet.getDate(5));
                pitch.put("uid", resultSet.getInt(6));

                jsonObject.put("pitch", pitch);
            }

            response.setStatus(response.SC_OK);
            response.setContentType("application/json");

            PrintWriter out = response.getWriter();
            out.print(jsonObject);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

//        StringBuffer sb = new StringBuffer();
//        String line = null;
//        try {
//            BufferedReader reader = request.getReader();
//            while ((line = reader.readLine()) != null)
//                sb.append(line);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = null;

        PreparedStatement preparedStatement = null;
        Connection conn = null;

        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        try {
//            int uid = (Integer) session.getAttribute("uid");

//            jsonObject = new JSONObject(sb.toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//            Date parsedDate = dateFormat.parse(jsonObject.getString("date"));
            System.out.println(request.getParameter("date"));
            Date parsedDate = dateFormat.parse(request.getParameter("date"));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());



//            PitchEntity pitch = new PitchBuilder()
//                    .title(jsonObject.getString("title"))
//                    .description(jsonObject.getString("description"))
//                    .videourl(jsonObject.getString("video"))
//                    .date(timestamp)
//                    .uid(uid)
//                    .buildPitch();
            PitchEntity pitch = new PitchBuilder()
                    .title(request.getParameter("title"))
                    .description(request.getParameter("description"))
                    .video(request.getPart("video"))
                    .date(timestamp)
                    .email(request.getParameter("email"))
                    .buildPitch();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql1 = "INSERT INTO pitch values (?, ?, ?, ?, ?, ?) RETURNING id";

            preparedStatement = conn.prepareStatement(sql1);
            preparedStatement.setString(1, pitch.getTitle());
            preparedStatement.setString(2, pitch.getDescription());
            preparedStatement.setString(3, pitch.getVideourl());
            preparedStatement.setTimestamp(4, pitch.getDate());
            preparedStatement.setString(5, pitch.getEmail());

            // init like-counts for a pitch
            int pid = preparedStatement.executeUpdate();

            String sql2 = "INSERT INTO pitch_like values (?, ?)";

            preparedStatement = conn.prepareStatement(sql2);
            preparedStatement.setInt(1, pid);
            preparedStatement.setInt(2, 0);

            respnoseJsonObject.put("Success", "True");
        } catch (JSONException e) {
            respnoseJsonObject.put("Success", "False");

            LOGGER.info("JSON parse error");
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        } catch (Exception e) {
            respnoseJsonObject.put("Success", "False");

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
