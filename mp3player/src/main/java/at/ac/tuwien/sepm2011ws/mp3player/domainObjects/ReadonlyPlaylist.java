/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import java.util.ArrayList;

/**
 * @author klaus
 * 
 */
public class ReadonlyPlaylist extends ArrayList<Song> {

	protected int id;
	protected String title;

	/**
	 * @param title
	 */
	public ReadonlyPlaylist(String title) {
		this(0, title);
	}

	/**
	 * @param id
	 * @param title
	 */
	public ReadonlyPlaylist(int id, String title) {
		this.id = id;
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass() == obj.getClass()) {
			ReadonlyPlaylist other = (ReadonlyPlaylist) obj;

			if (this.id != other.id || !this.title.equals(other.title))
				return false;

			for (int i = 0; i < this.size(); i++) {
				if (i >= other.size() || !this.get(i).equals(other.get(i)))
					return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.title;
	}

}
