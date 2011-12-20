package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.jmf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

public class ServiceFactory {
	private static ServiceFactory instance;
	private final ApplicationContext context;

	private ServiceFactory() {
		context = new ClassPathXmlApplicationContext("ServiceSettings.xml");
	}

	public static ServiceFactory getInstance() {
		if (instance == null)
			instance = new ServiceFactory();
		return instance;
	}

	public PlaylistService getPlaylistService() {
		return (PlaylistService) context.getBean("PlaylistService");
	}
	
	public CoreInteractionService getCoreInteractionService() {
		return (CoreInteractionService) context.getBean("CoreInteractionServiceBeta");
	}

}
