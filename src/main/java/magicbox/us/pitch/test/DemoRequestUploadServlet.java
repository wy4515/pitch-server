package magicbox.us.pitch.test;
/*
 * DemoUploadServlet.java
 *
 * Example servlet to handle file uploads using MultipartRequest for
 * decoding the incoming multipart/form-data stream
 */

import java.sql.*;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import com.oreilly.servlet.MultipartRequest;
import magicbox.us.pitch.rest.DbConfig;
import org.json.JSONObject;

public class DemoRequestUploadServlet extends HttpServlet implements DbConfig {
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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        JSONObject respnoseJsonObject = new JSONObject();
        PrintWriter out = response.getWriter();
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement ps = conn.prepareStatement("SELECT video FROM pitch WHERE pid = ?");
            ps.setInt(1, 2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                respnoseJsonObject.put("Success", true);
                respnoseJsonObject.put("Data", new String(imgBytes, "UTF-8"));
            }

        } catch (Exception e) {
            respnoseJsonObject.put("Success", false);
            e.printStackTrace();
        }
        out.print(respnoseJsonObject);
        out.flush();
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PreparedStatement preparedStatement = null;
        Connection conn = null;

        JSONObject respnoseJsonObject = new JSONObject();
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try {

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

//            Clob myClob = conn.createClob();
//            Writer clobWriter = myClob.setCharacterStream(1);

            MultipartRequest multi =
                    new MultipartRequest(request, System.getProperty("user.dir")+dirName, 10*1024*1024); // 10MB



            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                String filename = multi.getFilesystemName(name);
                String type = multi.getContentType(name);
                File f = multi.getFile(name);
//                out.println("name: " + name);
//                out.println("filename: " + filename);
//                out.println("type: " + type);
                if (f != null) {
//                    String str = this.readFile(f, clobWriter);
//                    System.out.println("Wrote the following: " +
//                            clobWriter.toString());
//                    myClob.setString(1, str);
//                    System.out.println("Length of Clob: " + myClob.length());
//                    String sql = "INSERT INTO pitch (video) WHERE pid=2 values (?)";
                    String sql = "UPDATE pitch SET video = (?) WHERE pid = 2";
                    FileInputStream fis = new FileInputStream(f);
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setBinaryStream(1, fis, (int)f.length());
                }
            }

            preparedStatement.executeUpdate();

            respnoseJsonObject.put("Success", true);
        }
        catch (Exception lEx) {
            respnoseJsonObject.put("Success", false);
            lEx.printStackTrace();
        }
        out.print(respnoseJsonObject);
        out.flush();
    }

    private String readFile(File file, Writer writerArg)
            throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String nextLine = "";
        StringBuffer sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null) {
            System.out.println("Writing: " + nextLine);
            writerArg.write(nextLine);
            sb.append(nextLine);
        }
        // Convert the content into to a string
        String clobData = sb.toString();

        // Return the data.
        return clobData;
    }

}