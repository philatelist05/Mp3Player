-- Song (ID, title, duration, rating, playcount, year, path, artist, genre)
CREATE SEQUENCE song_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Song (
  ID		INTEGER		PRIMARY KEY DEFAULT nextval('song_sequence'),
  title		VARCHAR(255)	NOT NULL,
  duration	INTEGER		NOT NULL,
  rating 	NUMERIC(2)	NOT NULL,
  playcount	INTEGER		NOT NULL,
  year		INTEGER		NOT NULL,
  path  	VARCHAR(512)	NOT NULL,
  artist  	VARCHAR(255)	NOT NULL,
  genre		VARCHAR(255),
  lyric		TEXT,
  CHECK (rating BETWEEN -1 AND 10)
);
ALTER SEQUENCE song_sequence OWNED BY Song.ID;

-- Album (ID, title, year, albumart_path)
CREATE SEQUENCE album_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Album (
  ID		INTEGER	PRIMARY KEY DEFAULT nextval('album_sequence'),
  title		VARCHAR(255)	NOT NULL,
  year		INTEGER		NOT NULL,
  albumart_path	VARCHAR(512)
);
ALTER SEQUENCE album_sequence OWNED BY Album.ID;

-- Playlist (ID, name)
CREATE SEQUENCE playlist_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Playlist (
  ID		INTEGER	PRIMARY KEY DEFAULT nextval('playlist_sequence'),
  name		VARCHAR(255)	NOT NULL
);
ALTER SEQUENCE playlist_sequence OWNED BY Playlist.ID;

-- is_on (album:Album.ID, song:Song.ID)
CREATE TABLE is_on (
  album		INTEGER	REFERENCES Album(ID) ON DELETE CASCADE,
  song		INTEGER	REFERENCES Song(ID) ON DELETE CASCADE
);

-- contains (playlist:Playlist.ID, song:Song.ID)
CREATE TABLE contains (
  position	INTEGER CHECK (position >= 0),
  playlist	INTEGER	REFERENCES Playlist(ID) ON DELETE CASCADE,
  song		INTEGER	REFERENCES Song(ID) ON DELETE CASCADE,
  PRIMARY KEY (position, playlist, song)
);


-- Trigger

CREATE LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION songExistingCheck () RETURNS TRIGGER AS $$
BEGIN
	-- If the path of the new line is a new song file, add it, otherwise omit it.
	IF NEW.path NOT IN (SELECT path from song)
	THEN
		RETURN NEW;
	ELSE
		RETURN NULL;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER songExistingCheck BEFORE INSERT ON song
  FOR EACH ROW EXECUTE PROCEDURE songExistingCheck();
