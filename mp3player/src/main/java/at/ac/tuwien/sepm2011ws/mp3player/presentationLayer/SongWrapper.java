package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public class SongWrapper {
	 private Song song;
	 private ComponentType type;
	 private String text;

	 public SongWrapper(Song song, ComponentType type, String text) {
		 this.song = song;
	     this.type = type;
	     this.text = text;
	 }

    public Song getSong() {
		return song;
	}

	@Override
    public String toString() {
    	String temp;
    	switch (this.type) {
    	case ComboBox:
    		temp = text;
    		break;
    	case List:
    		temp = text;
    		break;
    	default:
    		temp = text;
    		break;
    	}
    	
    	return temp;
    }
}