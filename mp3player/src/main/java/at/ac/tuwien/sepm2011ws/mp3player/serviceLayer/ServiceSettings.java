package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

public interface ServiceSettings {
    public String[] getUserFileTypes();

    public void setUserFileTypes(String[] types);

    public String[] getAllFileTypes();

    public String[] getUserColumns();

    public void setUserColumns();

    public String[] getallColumns();

    public void getTopXXRatedCount();

    public void setTopXXRatedCount(int anzahl);

    public void getTopXXPlayedCount();

    public void setTopXXPlayedCount(int anzahl);
}
