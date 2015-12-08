package magicbox.us.pitch.rest;

import com.google.gson.Gson;
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
import java.util.ArrayList;

public class List extends HttpServlet
        implements DbConfig {

    class Meta {
        int pid;
        String title;
        String description;
        String date;
        String email;
        String tags;
        int like;

        public void setPid(int pid) {
            this.pid = pid;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public void setLike(int count) {
            this.like = count;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;
        PrintWriter out = response.getWriter();

        JSONObject jsonObject = new JSONObject();
        try {
            String email = request.getParameter("email");
            String sql1 = "SELECT pitch.pid, title, description, date, email, tags, count FROM pitch, pitch_like WHERE pitch.pid=pitch_like.pid AND pitch.email=?";

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement(sql1);
            preparedStatement.setString(1, email);
            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            java.util.List<Meta> metaList = new ArrayList<Meta>();
            Gson gson = new Gson();
            while (resultSet.next()) {
                Meta m = new Meta();
                m.setPid(resultSet.getInt(1));
                m.setTitle(resultSet.getString(2));
                m.setDescription(resultSet.getString(3));
                m.setDate(resultSet.getString(4));
                m.setEmail(resultSet.getString(5));
                m.setTags(resultSet.getString(6));
                m.setLike(resultSet.getInt(7));

                metaList.add(m);
            }
            jsonObject.put("meta", gson.toJson(metaList.toArray()));
            jsonObject.put("Success", true);
            out.print(jsonObject);
        } catch (Exception e) {
            jsonObject.put("Success", false);
            e.printStackTrace();
        }

        response.setContentType("application/json");
        out.flush();
    }
}
