/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.model;

import java.sql.Timestamp;

/**
 * Map with Pitch schema in databsae
 */
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
