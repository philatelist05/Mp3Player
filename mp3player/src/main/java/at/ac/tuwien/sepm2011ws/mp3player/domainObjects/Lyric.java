/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

/**
 * @author oli
 * 
 */
public class Lyric {

	private String text;

	public Lyric() {
		this("Unkown");
	}

	/**
	 * @param text
	 */
	public Lyric(String text) {
		this.setText(text);
	}

	/**
	 * 
	 * Returns the text
	 * 
	 * @return text
	 * 
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * 
	 * Sets the text
	 * 
	 * @param text
	 * 
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass() == obj.getClass()) {
			Lyric other = (Lyric) obj;

			return ((this.text == null && other.text == null) || (this.text != null && this.text
					.equals(other.text)));
		}
		return false;
	}
}
