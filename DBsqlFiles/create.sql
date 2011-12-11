-- Song (ID, title, duration, rating, playcount, year, path, artist, genre)
CREATE SEQUENCE song_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Song (
  ID		NUMERIC(8)	PRIMARY KEY DEFAULT nextval('song_sequence'),
  title		VARCHAR(100)	NOT NULL,
  duration      NUMERIC(8)	NOT NULL,
  rating 	NUMERIC(3)	NOT NULL,
  playcount	NUMERIC(8)	NOT NULL,
  year		DATE		NOT NULL,
  path  	VARCHAR(100)	NOT NULL,
  artist  	VARCHAR(100)	NOT NULL,
  genre		VARCHAR(30)	NOT NULL,
  CHECK (rating BETWEEN 0 AND 10)
);
ALTER SEQUENCE song_sequence OWNED BY Song.ID;

-- Album (ID, title, year, albumart_path)
CREATE SEQUENCE album_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Album (
  ID		NUMERIC(8)	PRIMARY KEY DEFAULT nextval('album_sequence'),
  title		VARCHAR(100)	NOT NULL,
  year		DATE		NOT NULL,
  albumart_path	VARCHAR(100)	NOT NULL
);
ALTER SEQUENCE album_sequence OWNED BY Album.ID;

-- Playlist (ID, name)
CREATE SEQUENCE playlist_sequence
  START 1 INCREMENT BY 1;
CREATE TABLE Playlist (
  ID		NUMERIC(8)	PRIMARY KEY DEFAULT nextval('playlist_sequence'),
  name		VARCHAR(100)	NOT NULL
);
ALTER SEQUENCE playlist_sequence OWNED BY Playlist.ID;

-- is_on (album:Album.ID, song:Song.ID)
CREATE TABLE is_on (
  album		NUMERIC(8)	REFERENCES Album(ID),
  song		NUMERIC(8)	REFERENCES Song(ID)
);

-- contains (playlist:Playlist.ID, song:Song.ID)
CREATE TABLE contains (
  playlist	NUMERIC(8)	REFERENCES Playlist(ID),
  song		NUMERIC(8)	REFERENCES Song(ID)
);