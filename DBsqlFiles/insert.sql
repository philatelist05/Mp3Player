-- Playlist Metääälll

INSERT INTO Playlist VALUES
  (DEFAULT,  'Metääälll');

-- 1st Song
INSERT INTO Album VALUES
  (DEFAULT,  'Black Album', 1995, E'C:/music/Metallica');

<<<<<<< HEAD
INSERT INTO Song VALUES
  (DEFAULT,  'Kalimba', 348, 9, 34, 1995, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Kalimba.wav', 'Metallica', 'Metal');
=======
INSERT INTO Song (id, title, duration, rating, playcount, year, path, artist, genre) VALUES
  (DEFAULT,  'Enter Sandman', 184, 9, 34, 1995, E'C:\\music\\Metallica\\entersandman', 'Metallica', 'Metal');
>>>>>>> e81dd3ebe65f38fd03f8a1d4724a3047c679e281
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

INSERT INTO contains VALUES
  (0, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));  
  
<<<<<<< HEAD
INSERT INTO Song VALUES
  (DEFAULT,  'Maid', 169, 8, 77, 1995, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Maid.wav', 'Metallica', 'Metal');
=======
-- 2nd Song
INSERT INTO Song (id, title, duration, rating, playcount, year, path, artist, genre) VALUES
  (DEFAULT,  'Wherever I May Roam', 133, 8, 77, 1995, E'C:\\music\\Metallica\\whereverimayroam', 'Metallica', 'Metal');
>>>>>>> e81dd3ebe65f38fd03f8a1d4724a3047c679e281
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO contains VALUES
  (1, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

-- Playlist Party

INSERT INTO Playlist VALUES
  (DEFAULT,  'Party');

-- 1st Song
INSERT INTO contains VALUES
  (0, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

-- 2nd Song
INSERT INTO Album VALUES
<<<<<<< HEAD
  (DEFAULT,  'Panic Prevention', 2001, E'C:/music/JamieT');  
INSERT INTO Song VALUES
  (DEFAULT,  'Sticks n Stones', 275, 5, 12, 2001, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Sleep.wav', 'JamieT', 'Folk, Ska, Electronic');
=======
  (DEFAULT,  'Panic Prevention', 2001, E'C:\\music\\JamieT');  
INSERT INTO Song (id, title, duration, rating, playcount, year, path, artist, genre) VALUES
  (DEFAULT,  'Sticks n Stones', 213, 5, 12, 2001, E'C:\\music\\JamieT\\sticksnstones', 'JamieT', 'Folk, Ska, Electronic');
>>>>>>> e81dd3ebe65f38fd03f8a1d4724a3047c679e281
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO contains VALUES
  (1, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

<<<<<<< HEAD
  
INSERT INTO Song VALUES
  (DEFAULT,  'Nosebleed Secton', 348, 9, 34, 2003, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/blabla.wav', 'Hilltop Hoods', 'Rap');
 
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO Song VALUES  
  (DEFAULT,  'I Kissed a Girl', 275, 5, 12, 2007, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/tery.wav', 'Katy Perry', 'Pop');
=======
-- 3rd Song
INSERT INTO Song (id, title, duration, rating, playcount, year, path, artist, genre) VALUES
  (DEFAULT,  'Nosebleed Secton', 678, 9, 34, 2003, E'C:\\music\\HilltopHoods\\nosebleedsection', 'Hilltop Hoods', 'Rap');
 
INSERT INTO contains VALUES
  (2, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));


-- 4th Song
INSERT INTO Song (id, title, duration, rating, playcount, year, path, artist, genre) VALUES
  (DEFAULT,  'I Kissed a Girl', 302, 5, 12, 2007, E'C:\\music\\KatyPerry\\ikissedagirl', 'Katy Perry', 'Pop');
>>>>>>> e81dd3ebe65f38fd03f8a1d4724a3047c679e281
  
INSERT INTO contains VALUES
  (3, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
