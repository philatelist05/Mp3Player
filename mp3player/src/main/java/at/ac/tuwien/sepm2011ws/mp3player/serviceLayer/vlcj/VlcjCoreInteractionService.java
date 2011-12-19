package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;

class VlcjCoreInteractionService implements CoreInteractionService {

	public VlcjCoreInteractionService() {
		// TODO Auto-generated constructor stub
	}

	public void playFromBeginning(Song song) {
		// TODO Auto-generated method stub

	}

	public void playPause() {
		// TODO Auto-generated method stub

	}

	public void playPause(Song song) {
		// TODO Auto-generated method stub

	}

	public void pause() {
		// TODO Auto-generated method stub

	}

	public void playNext() {
		// TODO Auto-generated method stub

	}

	public void playPrevious() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public void toggleMute() {
		// TODO Auto-generated method stub

	}

	public void setVolume(int level) {
		// TODO Auto-generated method stub

	}

	public int getVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isMute() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPaused() {
		// TODO Auto-generated method stub
		return false;
	}

	public void seek(double percent) {
		// TODO Auto-generated method stub

	}

	public void seekToSecond(int seconds) {
		// TODO Auto-generated method stub

	}

	public double getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDurationAt(double percent) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getPlayTimeInSeconds() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Song getCurrentSong() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPlayerListener(PlayerListener pl) {
		// TODO Auto-generated method stub

	}

	public void removePlayerListener() {
		// TODO Auto-generated method stub

	}

}
