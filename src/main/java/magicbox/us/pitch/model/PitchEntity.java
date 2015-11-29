package magicbox.us.pitch.model;

import java.sql.Timestamp;

import javax.servlet.http.Part;

public class PitchEntity {
    private String title,
        description,
        videourl;
    private Part video;
    private Timestamp date;
    private String email;

    PitchEntity(String title, String description, Part video, Timestamp date, String email) {
        this.title = title;
        this.description = description;
        this.video = video;
        this.date = date;
        this.email = email;
    }
    public Object getVideo() {
        return this.video;
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

    public String getEmail() {
        return email;
    }
}
