package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		return checkSetting(this.userConfig.getStringArray("FileTypes"),
				SettingsService.SongFileTypesAll);
	}

	@Override
	public void setUserFileTypes(String[] types) {
		try {
			this.userConfig.setProperty("FileTypes",
					checkSetting(types, SettingsService.SongFileTypesAll));
			this.userConfig.save();
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}

	@Override
	public String[] getUserColumns() {
		return checkSetting(this.userConfig.getStringArray("Columns"),
				SettingsService.SongTableColumnsAll);
	}

	@Override
	public void setUserColumns(String[] cols) {
		try {
			this.userConfig.setProperty("Columns",
					checkSetting(cols, SettingsService.SongTableColumnsAll));
			this.userConfig.save();
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}

	@Override
	public int getTopXXRatedCount() {
		return checkSetting(this.userConfig.getInt("TopXXRated"),
				SettingsService.XXXRatedCountDefault);
	}

	@Override
	public void setTopXXRatedCount(int count) {
		try {
			this.userConfig.setProperty(
					"TopXXRated",
					new Integer(checkSetting(count,
							SettingsService.XXXRatedCountDefault)));
			this.userConfig.save();
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}

	@Override
	public int getTopXXPlayedCount() {
		return checkSetting(this.userConfig.getInt("TopXXPlayed"),
				SettingsService.XXXPlayedCountDefault);
	}

	@Override
	public void setTopXXPlayedCount(int count) {
		try {
			this.userConfig
					.setProperty(
							"TopXXPlayed",
							new Integer(checkSetting(count,
									SettingsService.XXXPlayedCountDefault)));
			this.userConfig.save();
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}

	/**
	 * Removes invalid values from a settings list.
	 * 
	 * @param testees
	 *            The settings list
	 * @param accepted
	 *            The list of the accepted values
	 * @return the filtered list or if it is empty, the accepted list
	 */
	private <T> T[] checkSetting(T[] testees, T[] accepted) {
		List<T> retList = new ArrayList<T>();
		List<T> acceptedList = Arrays.asList(accepted);

		for (T testee : testees) {
			if (acceptedList.contains(testee))
				retList.add(testee);
		}

		if (retList.size() != 0) {
			return retList.toArray(testees);
		}
		return accepted;
	}

	/**
	 * Checks the settings' value.
	 * 
	 * @param setting
	 *            The setting to check
	 * @param defaultValue
	 *            The default value for the setting
	 * @return the value of the setting in case it is valid, otherwise the
	 *         default value
	 */
	private int checkSetting(int setting, int defaultValue) {
		if (setting < 1) {
			return defaultValue;
		}
		return setting;
	}

}
