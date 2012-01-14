package CoreInteraction;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService;

public aspect VlcAspect {

    private static final Logger logger = Logger
	    .getLogger(VlcjCoreInteractionService.class);

    pointcut play() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.playFromBeginning(..));

    pointcut pause() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.pause(..));

    pointcut stop() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.stop(..));

    pointcut next() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.getNextSong(..));

    pointcut prev() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.getPreviousSong(..));

    pointcut modifyPlayMode() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.setPlayMode(..));

    pointcut seek() : execution(public * at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj.VlcjCoreInteractionService.seek..*(..));

    
    after() : play() {
	Song current = (Song)thisJoinPoint.getArgs()[0];
	logger.info("Now playing " + current.getId() + " " + current.getTitle());
    }
    
    after() : pause() {
	logger.info("The player is now paused");
    }
    
    after() : stop() {
	logger.info("The player is now stopped");
    }
    
    after() returning(Song nextSong) : next() {
	logger.info("Getting next Song from Playlist " + nextSong.getId() + " " + nextSong.getTitle());
    }
    
    after() returning(Song previousSong) : prev() {
	logger.info("Getting next Song from Playlist " + previousSong.getId() + " " + previousSong.getTitle());
    }
    
    before() : modifyPlayMode() {
	PlayMode current = (PlayMode)thisJoinPoint.getArgs()[0];
	logger.info("Current PlayMode set to: " + current);
    }
    
    after() : seek() {
	logger.info("Currently seeking");
    }
}
