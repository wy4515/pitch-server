package magicbox.us.pitch.exception;

public class ExceptionLog {
    private static final String TAG = "PITCH: ";

    public ExceptionLog() {

    }

    public void log(String msg) {
        // log into tomcat server log
        System.out.println(TAG+msg);

    }
}
