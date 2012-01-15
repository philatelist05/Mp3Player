package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

public interface SettingsService {
    public static final String[] SongFileTypesAll = new String[] { "wav", "mp3" };

    public static final String[] SongTableColumnsAll = new String[] {
	    "Track Nr.", "Title", "Artist", "Album", "Year", "Genre",
	    "Duration", "Rating", "Playcount" };

    public static final int XXXPlayedCountDefault = 40;

    public static final int XXXRatedCountDefault = 40;

    public String[] getUserFileTypes();

    public void setUserFileTypes(String[] types);

    public String[] getUserColumns();

    public void setUserColumns(String[] cols);

    public int getTopXXRatedCount();

    public void setTopXXRatedCount(int anzahl);

    public int getTopXXPlayedCount();

    public void setTopXXPlayedCount(int anzahl);
}
