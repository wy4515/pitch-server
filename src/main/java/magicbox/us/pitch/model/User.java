package magicbox.us.pitch.model;

public class User {
    private String name;
    private String password;
    private String email;
    private String headline;
    private String pictureUrl;
    private boolean pitchable;

    User(String name, String pwd, String email, String headline, String url, boolean pitchale) {
        this.name = name;
        this.password = pwd;
        this.email = email;
        this.headline = headline;
        this.pictureUrl = url;
        this.pitchable = pitchale;
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
