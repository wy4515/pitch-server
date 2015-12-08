package magicbox.us.pitch.rest;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangwu on 11/24/15.
 */
public class test {
    public static void main(String args[]) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            java.util.Date today = new java.util.Date();
            System.out.println(new java.sql.Timestamp(today.getTime()).getTime());

            Timestamp t = new Timestamp(Long.parseLong("1449591883643"));
            System.out.println(t.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
