package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class LyricTest {

	@Test
	public void testSetText_ShouldBeEqual() {
		Lyric lyric = new Lyric();
		lyric.setText("text");
		assertEquals(lyric.getText(), "text");
	}

	@Test
	public void testEquals_ShouldBeEqual1() {
		Lyric lyric1 = new Lyric();
		Lyric lyric2 = new Lyric();
		assertEquals(lyric1, lyric2);
	}

	@Test
	public void testEquals_ShouldBeEqual2() {
		Lyric lyric1 = new Lyric("text");
		Lyric lyric2 = new Lyric("text");
		assertEquals(lyric1, lyric2);
	}

	@Test
	public void testHashcode_ShouldBeEqual1() {
		Lyric lyric1 = new Lyric();
		Lyric lyric2 = new Lyric();
		assertEquals(lyric1.hashCode(), lyric2.hashCode());
	}

	@Test
	public void testHashcode_ShouldBeEqual2() {
		Lyric lyric1 = new Lyric("text");
		Lyric lyric2 = new Lyric("text");
		assertEquals(lyric1.hashCode(), lyric2.hashCode());
	}
}
