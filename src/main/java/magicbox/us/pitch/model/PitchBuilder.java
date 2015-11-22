package magicbox.us.pitch.model;

import java.sql.Timestamp;
import java.util.Calendar;

public class PitchBuilder {
    private String title,
            description,
            videourl;
    private Timestamp date;
    private int uid;

    public PitchBuilder() {
        title = "";
        description = "";
        videourl = "";
        try {
            date = new Timestamp(Calendar.getInstance().getTime().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        uid = 0;
    }

    public PitchEntity buildPitch() {
        PitchEntity p = new PitchEntity(title, description, videourl, date, uid);
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

    public PitchBuilder date(Timestamp _date) {
        this.date = _date;
        return this;
    }

    public PitchBuilder uid(int _uid) {
        this.uid = _uid;
        return this;
    }
}
