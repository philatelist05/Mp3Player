-- Song (ID, title, duration, rating, playcount, year, path, artist, genre)
CREATE SEQUENCE song_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Song (
  ID		INTEGER		PRIMARY KEY DEFAULT nextval('song_sequence'),
  title		VARCHAR(255)	NOT NULL,
  duration	INTEGER		NOT NULL,
  rating 	NUMERIC(2,1)	NOT NULL,
  playcount	INTEGER		NOT NULL,
  year		INTEGER		NOT NULL,
  path  	VARCHAR(512)	NOT NULL,
  artist  	VARCHAR(255)	NOT NULL,
  genre		VARCHAR(255),
  lyric		TEXT,
  pathOk	BOOLEAN DEFAULT true,
  CHECK (rating BETWEEN 0 AND 5)
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
  album		INTEGER	REFERENCES Album(ID) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
  song		INTEGER	REFERENCES Song(ID) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED
);

-- contains (playlist:Playlist.ID, song:Song.ID)
CREATE TABLE contains (
  position	INTEGER CHECK (position >= 0),
  playlist	INTEGER	REFERENCES Playlist(ID) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
  song		INTEGER	REFERENCES Song(ID) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
  PRIMARY KEY (position, playlist, song)
);
