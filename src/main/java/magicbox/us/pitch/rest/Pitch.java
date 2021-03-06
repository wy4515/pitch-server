/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import magicbox.us.pitch.database.DbConfig;
import magicbox.us.pitch.database.S3Helper;
import magicbox.us.pitch.model.PitchBuilder;
import magicbox.us.pitch.model.PitchEntity;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

/**
 * To schedule a pitch
 */
public class Pitch extends AbstractServlet
        implements DbConfig {

    private final static Logger LOGGER = Logger.getLogger(Pitch.class.getName());
    private S3Helper s3Helper = new S3Helper();
    private String dirName;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");
        if (dirName == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }
        System.out.println("dir name: "+dirName);
    }
    @Override
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

                List<CommentEntity> commentList = new ArrayList<CommentEntity>();
                Gson gson = new Gson();
                while (commentSet.next()) {
                    CommentEntity c = new CommentEntity();
                    c.setUser(commentSet.getString(2));
                    c.setComment(commentSet.getString(3));

                    commentList.add(c);
                }
                pitch.put("comments", gson.toJson(commentList));

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

        PreparedStatement preparedStatement = null;
        Connection conn = null;

        JSONObject jsonObject = new JSONObject();
        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        try {
            jsonObject = new JSONObject(sb.toString());

            System.out.println(System.getProperty("user.dir"));
            MultipartRequest multi =
                    new MultipartRequest(request, System.getProperty("user.dir")+dirName, 10*1024*1024); // 10MB

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("datesssss "+jsonObject.getString("date"));
            Date parsedDate = dateFormat.parse(jsonObject.getString("date"));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

            PitchEntity pitch = new PitchBuilder()
                    .title(jsonObject.getString("title"))
                    .description(jsonObject.getString("description"))
                    .date(timestamp)
                    .email(jsonObject.getString("email"))
                    .tag(jsonObject.getString("tag"))
                    .buildPitch();

            System.out.println(timestamp);

            Enumeration files = multi.getFileNames();
            File f = null;
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                f = multi.getFile(name);
                s3Helper.upload2S3(f);
            }

//            PitchEntity pitch = new PitchBuilder()
//                    .title(multi.getParameter("title"))
//                    .description(multi.getParameter("description"))
////                    .video(request.getPart("video"))
//                    .date(timestamp)
//                    .email(multi.getParameter("email"))
//                    .tag(multi.getParameter("tag"))
//                    .buildPitch();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql1 = "INSERT INTO pitch (title, description, date, email, tags) values (?, ?, ?, ?, ?) RETURNING pid";

            preparedStatement = conn.prepareStatement(sql1);
            preparedStatement.setString(1, pitch.getTitle());
            preparedStatement.setString(2, pitch.getDescription());
//            preparedStatement.setString(3, pitch.getVideourl());
            preparedStatement.setTimestamp(3, pitch.getDate());
            preparedStatement.setString(4, pitch.getEmail());
            preparedStatement.setString(5, pitch.getTag());

            preparedStatement.execute();
            preparedStatement.getResultSet().next();
            int pid = preparedStatement.getResultSet().getInt(1);
            System.out.println(pid);

            // insert video if exist
//            if (f!=null) {
//                System.out.println("File length: "+ f.length());
//                FileInputStream fis = new FileInputStream(f);
//                String sql = "UPDATE pitch SET video = (?) WHERE pid = ?";
//                preparedStatement = conn.prepareStatement(sql);
//                preparedStatement.setBinaryStream(1, fis, (int)f.length());
//                preparedStatement.setInt(2, pid);
//                preparedStatement.executeUpdate();
//            }

            // init like-counts for a pitch
            String sql2 = "INSERT INTO pitch_like values (?, ?)";

            preparedStatement = conn.prepareStatement(sql2);
            preparedStatement.setInt(1, pid);
            preparedStatement.setInt(2, 0);

            preparedStatement.executeUpdate();

            respnoseJsonObject.put("success", "True");
        } catch (JSONException e) {
            respnoseJsonObject.put("success", "False");

            LOGGER.info("JSON parse error");
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        } catch (Exception e) {
            respnoseJsonObject.put("success", "False");

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

class CommentEntity {
    String user;
    String comment;

    public void setUser(String s) {this.user = s;}
    public void setComment(String s){this.comment = s;}
}