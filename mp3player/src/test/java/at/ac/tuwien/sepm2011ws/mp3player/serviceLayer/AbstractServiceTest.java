package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:ServiceSettings.xml"})
abstract class AbstractServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	protected PlaylistService playlistService;
	protected CoreInteractionService coreInteractionService;
	protected SettingsService settingsService;
	protected SongInformationService songInformationService;
	protected LastFmService lastFmService;

	@Autowired
	void setPlaylistService(PlaylistService playlistService){
		this.playlistService = playlistService;
	}

	@Autowired
	void setCoreInteractionService(CoreInteractionService coreInteractionService){
		this.coreInteractionService = coreInteractionService;
	}

	@Autowired
	void setSettingsService(SettingsService settingsService){
		this.settingsService = settingsService;
	}

	@Autowired
	void setSongInformationService(SongInformationService songInformationService){
		this.songInformationService = songInformationService;
	}

	@Autowired
	void setLastFmService(LastFmService lastFmService){
		this.lastFmService = lastFmService;
	}
}
