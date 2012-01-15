package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceSettings;

public class VVVServiceSettings implements ServiceSettings {

    @Override
    public String[] getUserFileTypes() {
	return null;
    }

    @Override
    public void setUserFileTypes(String[] types) {
    }

    @Override
    public String[] getUserColumns() {
	return null;
    }

    @Override
    public void setUserColumns() {
    }

    @Override
    public int getTopXXRatedCount() {
	return 0;
    }

    @Override
    public void setTopXXRatedCount(int anzahl) {
    }

    @Override
    public int getTopXXPlayedCount() {
	return 0;
    }

    @Override
    public void setTopXXPlayedCount(int anzahl) {
    }

}
