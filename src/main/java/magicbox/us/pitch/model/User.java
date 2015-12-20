/**
 * 18641 Java Smartphone
 * Pitch App
 */
package magicbox.us.pitch.model;

/**
 * Map with User schema in database
 */
public class User {
    private String name;
    private String password;
    private String email;
    private String headline;
    private String pictureUrl;
    private boolean pitchable;
    private String skills;

    User(String name, String pwd, String email, String headline, String url, boolean pitchale, String skills) {
        this.name = name;
        this.password = pwd;
        this.email = email;
        this.headline = headline;
        this.pictureUrl = url;
        this.pitchable = pitchale;
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getHeadline() {
        return headline;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public boolean isPitchable() {
        return pitchable;
    }
}
