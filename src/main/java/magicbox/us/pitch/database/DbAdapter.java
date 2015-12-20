/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The interface that defines some JDBC parameters
 */
public class DbAdapter implements DbConfig {
    Connection conn = null;

    /**
     * Init JDBC driver with password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public DbAdapter() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public Connection getDbConnection() {
        return this.conn;
    }
}
