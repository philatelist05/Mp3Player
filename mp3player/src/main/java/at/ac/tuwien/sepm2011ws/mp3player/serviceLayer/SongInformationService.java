/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.io.File;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 *
 */
public interface SongInformationService {
    public Song getMetaData(File file);
    public Song getMetaData(File[] file);
    
    
}
