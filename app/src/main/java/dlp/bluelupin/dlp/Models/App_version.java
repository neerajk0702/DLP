package dlp.bluelupin.dlp.Models;

/**
 * Created by harsh on 13-04-2018.
 */

public class App_version {
    private String force_update;

    private String version;

    public String getForce_update ()
    {
        return force_update;
    }

    public void setForce_update (String force_update)
    {
        this.force_update = force_update;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [force_update = "+force_update+", version = "+version+"]";
    }
}

