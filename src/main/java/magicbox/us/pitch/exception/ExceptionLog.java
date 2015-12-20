/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.exception;

/**
 * The exception handler class: log down error messages
 */
public class ExceptionLog {
    private static final String TAG = "PITCH: ";

    public ExceptionLog() {

    }

    public void log(String msg) {
        // log into tomcat server log
        System.out.println(TAG+msg);
    }
}
