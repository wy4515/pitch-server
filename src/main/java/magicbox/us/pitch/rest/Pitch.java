package magicbox.us.pitch.rest;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * To schedule a pitch
 */
public class Pitch extends HttpServlet
        implements DbConfig {

    private final static Logger LOGGER = Logger.getLogger(Pitch.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;
        PrintWriter out = response.getWriter();
        try {
            String sql1 = "SELECT title, description, date, count FROM pitch, pitch_like WHERE pitch.pid=pitch_like.pid AND pitch.pid=?";

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement(sql1);
            int pid = Integer.parseInt(request.getParameter("pid"));
            System.out.println(pid);
            preparedStatement.setInt(1, pid);

            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.toString());
            JSONObject jsonObject = new JSONObject();

            while (resultSet.next()) {
                JSONObject pitch = new JSONObject();

//                pitch.put("pid", resultSet.getInt(1));
                pitch.put("tittle", resultSet.getString(1));
                pitch.put("description", resultSet.getString(2));
                pitch.put("date", resultSet.getDate(3));
                pitch.put("likes", resultSet.getInt(4));

                String sql2 = "SELECT * FROM pitch_comment WHERE pid=?";
                Connection conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement preparedStatement2 = conn2.prepareStatement(sql2);
                preparedStatement2.setInt(1, Integer.parseInt(request.getParameter("pid")));

                System.out.println(preparedStatement2);

                ResultSet commentSet = preparedStatement2.executeQuery();

                class CommentEntity {
                    String user;
                    String comment;

                    public void setUser(String s) {this.user = s;}
                    public void setComment(String s){this.comment = s;}
                }

                List<CommentEntity> commentList = new ArrayList<CommentEntity>();
                Gson gson = new Gson();
                while (commentSet.next()) {
                    CommentEntity c = new CommentEntity();
                    c.setUser(commentSet.getString(2));
                    c.setComment(commentSet.getString(3));

                    commentList.add(c);
                }
                pitch.put("comments", gson.toJson(commentList.toArray()));

                jsonObject.put("pitch", pitch);
                out.print(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setStatus(response.SC_OK);
        response.setContentType("application/json");
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;

        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//            System.out.println(request.getParameter("date"));
//            Date parsedDate = dateFormat.parse(request.getParameter("date"));
//            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            Timestamp timestamp = new java.sql.Timestamp(Long.parseLong(request.getParameter("date")));

            PitchEntity pitch = new PitchBuilder()
                    .title(request.getParameter("title"))
                    .description(request.getParameter("description"))
                    .video(request.getPart("video"))
                    .date(timestamp)
                    .email(request.getParameter("email"))
                    .tag(request.getParameter("tag"))
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
