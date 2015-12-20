/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.model;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Pitch model builder
 */
public class PitchBuilder {
    private String title,
            description,
            videourl;
//    private File video;
    private Timestamp date;
    private String email;
    private String tag;

    public PitchBuilder() {
        title = "";
        description = "";
        videourl = "";
        try {
            date = new Timestamp(Calendar.getInstance().getTime().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        email = "";
        tag = "";
    }

    public PitchEntity buildPitch() throws Exception {
        if (email=="" || tag=="" || title=="")
            throw new Exception();
        PitchEntity p = new PitchEntity(title, description, date, email, tag);
        return p;
    }

    public PitchBuilder title(String _title) {
        this.title = _title;
        return this;
    }

    public PitchBuilder description(String _description) {
        this.description = _description;
        return this;
    }

    public PitchBuilder videourl(String _videourl) throws Exception {
        this.videourl = _videourl;
        if (!videourl.contains("/"))
            throw new Exception();
        return this;
    }

//    public PitchBuilder video(File _video) {
//        this.video = _video;
//        return this;
//    }

    public PitchBuilder date(Timestamp _date) {
        this.date = _date;
        return this;
    }

    public PitchBuilder email(String _email) throws Exception {
        this.email = _email;
        if (!email.contains("@"))
            throw new Exception();
        return this;
    }

    public PitchBuilder tag(String _tag) {
        this.tag = _tag;
        return this;
    }
}
