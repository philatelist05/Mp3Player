package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class VvvSettingsService implements SettingsService {

    private XMLConfiguration userConfig;
    private static final Logger logger = Logger
	    .getLogger(VvvSettingsService.class);

    public VvvSettingsService(String fileName) {
	try {
	    this.userConfig = new XMLConfiguration(new File(fileName));
	} catch (ConfigurationException e) {
	    logger.error(e.getMessage());
	    this.userConfig = new XMLConfiguration();
	}
    }

    @Override
    public String[] getUserFileTypes() {
	return this.userConfig.getStringArray("FileTypes");
    }

    @Override
    public void setUserFileTypes(String[] types) {
	for (String type : types) {
	    this.userConfig.setProperty("FileTypes", type);
	}
    }

    @Override
    public String[] getUserColumns() {
	return this.userConfig.getStringArray("Columns");
    }

    @Override
    public void setUserColumns(String[] cols) {
	for (String col : cols) {
	    this.userConfig.setProperty("Columns", col);
	}
    }

    @Override
    public int getTopXXRatedCount() {
	return this.userConfig.getInt("TopXXRated");
    }

    @Override
    public void setTopXXRatedCount(int anzahl) {
	this.userConfig.setProperty("TopXXRated", new Integer(anzahl));
    }

    @Override
    public int getTopXXPlayedCount() {
	return this.userConfig.getInt("TopXXPlayed");
    }

    @Override
    public void setTopXXPlayedCount(int anzahl) {
	this.userConfig.setProperty("TopXXPlayed", new Integer(anzahl));
    }

}
