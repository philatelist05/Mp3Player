package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class VvvSettingsService implements SettingsService {

    private XMLConfiguration userConfig;
    private static final Logger logger = Logger
	    .getLogger(VvvSettingsService.class);

    public VvvSettingsService() {
	try {
	    File file = new File("SettingsUser.xml");
	    this.userConfig = new XMLConfiguration(file);
	    if (!file.isFile()) {
		file.createNewFile();
		initConfig();
	    }
	} catch (ConfigurationException e) {
	    logger.error(e.getMessage());
	    this.userConfig = new XMLConfiguration();
	} catch (IOException e) {
	    logger.error(e.getMessage());
	    this.userConfig = new XMLConfiguration();
	}
    }

    private void initConfig() throws ConfigurationException {
	this.userConfig.setProperty("FileTypes",
		SettingsService.SongFileTypesAll);
	this.userConfig.setProperty("Columns",
		SettingsService.SongTableColumnsAll);
	this.userConfig.setProperty("TopXXPlayed", new Integer(
		SettingsService.XXXPlayedCountDefault));
	this.userConfig.setProperty("TopXXRated", new Integer(
		SettingsService.XXXRatedCountDefault));
	this.userConfig.save();
    }

    @Override
    public String[] getUserFileTypes() {
	return this.userConfig.getStringArray("FileTypes");
    }

    @Override
    public void setUserFileTypes(String[] types) {
	try {
	    this.userConfig.setProperty("FileTypes", types);
	    this.userConfig.save();
	} catch (ConfigurationException e) {
	    logger.error(e);
	}
    }

    @Override
    public String[] getUserColumns() {
	return this.userConfig.getStringArray("Columns");
    }

    @Override
    public void setUserColumns(String[] cols) {
	try {
	    this.userConfig.setProperty("Columns", cols);
	    this.userConfig.save();
	} catch (ConfigurationException e) {
	    logger.error(e);
	}
    }

    @Override
    public int getTopXXRatedCount() {
	return this.userConfig.getInt("TopXXRated");
    }

    @Override
    public void setTopXXRatedCount(int anzahl) {
	try {
	    this.userConfig.setProperty("TopXXRated", new Integer(anzahl));
	    this.userConfig.save();
	} catch (ConfigurationException e) {
	    logger.error(e);
	}
    }

    @Override
    public int getTopXXPlayedCount() {
	return this.userConfig.getInt("TopXXPlayed");
    }

    @Override
    public void setTopXXPlayedCount(int anzahl) {
	try {
	    this.userConfig.setProperty("TopXXPlayed", new Integer(anzahl));
	    this.userConfig.save();
	} catch (ConfigurationException e) {
	    logger.error(e);
	}
    }

}
