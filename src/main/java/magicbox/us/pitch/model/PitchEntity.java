package magicbox.us.pitch.model;

import java.io.File;
import java.sql.Timestamp;

import javax.servlet.http.Part;

public class PitchEntity {
    private String title,
        description,
        videourl;
//    private File videoBytes;
    private Timestamp date;
    private String email;
    private String tag;

    PitchEntity(String title, String description, Timestamp date, String email, String tag) {
        this.title = title;
        this.description = description;
//        this.videoBytes = video;
        this.date = date;
        this.email = email;
        this.tag = tag;
    }
//    public File getVideo() {
//        return this.videoBytes;
//    }

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

    public String getEmail() {
        return email;
    }

    public String getTag() { return tag; }
}
