package project.dang.daniel.acpm_coverflow;

public class Programme {
    /////////////////////////////////////////////////////////////////////////
    //Programme properties
    private String programme_title;
    private int programme_image;

    /////////////////////////////////////////////////////////////////////////
    //Constructor
    public Programme(int imageSource, String name) {
        this.programme_title = name;
        this.programme_image = imageSource;
    }

    /////////////////////////////////////////////////////////////////////////
    //Methods: gets and sets
    public String getProgramme_title() {
        return programme_title;
    }

    public int getProgramme_image() {
        return programme_image;
    }

    public void setProgramme_title(String programme_title) {
        this.programme_title = programme_title;
    }

    public void setProgramme_image(int programme_image) {
        this.programme_image = programme_image;
    }
}
