package magicbox.us.pitch.model;

import java.sql.Timestamp;

public class PitchEntity {
    private String title,
        description,
        videourl;
    private Timestamp date;
    private int uid;

    PitchEntity(String title, String description, String videourl, Timestamp date, int uid) {
        this.title = title;
        this.description = description;
        this.videourl = videourl;
        this.date = date;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideourl() {
        return videourl;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getUid() {
        return uid;
    }
}
