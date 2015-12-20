/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import magicbox.us.pitch.database.DbConfig;
import magicbox.us.pitch.util.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * List api to provide meta information about a user
 */
public class List extends AbstractServlet
        implements DbConfig {

    JsonHelper<Meta> jsonHelper = new JsonHelper();

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet resultSet = null;
        PrintWriter out = response.getWriter();

        JsonObject jsonObject = new JsonObject();
        try {
            String email = null;
            String sql1 = null;

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            if (request.getParameterMap().containsKey("email")) {
                email = request.getParameter("email");
                sql1 = "SELECT pitch.pid, title, description, date, email, tags, count FROM pitch, pitch_like WHERE pitch.pid=pitch_like.pid AND pitch.email=?";
                preparedStatement = conn.prepareStatement(sql1);
                preparedStatement.setString(1, email);
            } else {
                sql1 = "SELECT pitch.pid, title, description, date, email, tags, count FROM pitch, pitch_like WHERE pitch.pid=pitch_like.pid limit 10";
                preparedStatement = conn.prepareStatement(sql1);
            }
            System.out.println(preparedStatement);

            resultSet = preparedStatement.executeQuery();

            java.util.ArrayList<Meta> metaList = new ArrayList<Meta>();
            Gson gson = new Gson();

            JsonArray jsonArr = new JsonArray();
            while (resultSet.next()) {
                JsonObject tmp = new JsonObject();
//                Meta m = new Meta();
//                m.setPid(resultSet.getInt(1));
//                m.setTitle(resultSet.getString(2));
//                m.setDescription(resultSet.getString(3));
//                m.setDate(resultSet.getString(4));
//                m.setEmail(resultSet.getString(5));
//                m.setTags(resultSet.getString(6));
//                m.setLike(resultSet.getInt(7));
//
//                metaList.add(m);
                tmp.addProperty("pid", resultSet.getInt(1));
                tmp.addProperty("title", resultSet.getString(2));
                tmp.addProperty("description", resultSet.getString(3));
                tmp.addProperty("date", resultSet.getString(4));
                tmp.addProperty("email", resultSet.getString(5));
                tmp.addProperty("tag", resultSet.getString(6));
                tmp.addProperty("like", resultSet.getInt(7));

                jsonArr.add(tmp);
            }
            jsonHelper.toJsonArray(metaList);
            jsonObject.add("meta", jsonArr);
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
    @Deprecated
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
