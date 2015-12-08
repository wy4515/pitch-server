package magicbox.us.pitch.model;

import javax.servlet.http.Part;
import java.sql.Timestamp;
import java.util.Calendar;

public class PitchBuilder {
    private String title,
            description,
            videourl;
    private Part video;
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

    public PitchEntity buildPitch() {
        PitchEntity p = new PitchEntity(title, description, video, date, email, tag);
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

    public PitchBuilder videourl(String _videourl) {
        this.videourl = _videourl;
        return this;
    }

    public PitchBuilder video(Part _video) {
        this.video = _video;
        return this;
    }

    public PitchBuilder date(Timestamp _date) {
        this.date = _date;
        return this;
    }

    public PitchBuilder email(String _email) {
        this.email = _email;
        return this;
    }

    public PitchBuilder tag(String _tag) {
        this.tag = _tag;
        return this;
    }
}
