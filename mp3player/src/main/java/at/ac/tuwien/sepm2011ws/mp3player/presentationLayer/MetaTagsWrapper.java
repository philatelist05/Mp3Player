package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;

public class MetaTagsWrapper {
	private MetaTags tags;
	private ComponentType type;
	private String text;
	private Lyric lyric;

	public MetaTagsWrapper(MetaTags tags, Lyric lyric, ComponentType type, String text) {
		this.setTags(tags);
		this.setType(type);
		this.text = text;
		this.lyric = lyric;
	}

	public MetaTags getTags() {
		return tags;
	}

	public void setTags(MetaTags tags) {
		this.tags = tags;
	}
	
	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Lyric getLyric() {
		return lyric;
	}

	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}

	@Override
	public String toString() {
		if (text == null)
			return "Result";		
		else
			return text;
	}
}