package magicbox.us.pitch.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangwu on 11/24/15.
 */
public class test {
    public static void main(String args[]) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date parsedDate = dateFormat.parse("2015-11-10 11:00");
            System.out.println(parsedDate.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
