package magicbox.us.pitch.database;

/**
 * This interface define some basic variables for Postgresql
 */
public interface DbConfig {
    final String DB_URL = "jdbc:postgresql://pitch.cof6tchaa9lf.us-east-1.rds.amazonaws.com:5432/pitch";

    // TODO: remove this from git
    static final String USER = "root";
    static final String PASS = "12345678";
}