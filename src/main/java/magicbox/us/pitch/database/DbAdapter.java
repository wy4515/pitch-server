package magicbox.us.pitch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAdapter implements DbConfig {
    Connection conn = null;

    public DbAdapter() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public Connection getDbConnection() {
        return this.conn;
    }
}
