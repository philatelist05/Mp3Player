INSERT INTO Playlist VALUES
  (DEFAULT,  'Metääälll');

INSERT INTO Album VALUES
  (DEFAULT,  'Black Album', 1995, E'C:/music/Metallica');

INSERT INTO Song VALUES
  (DEFAULT,  'Kalimba', 348, 9, 34, 1995, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Kalimba.wav', 'Metallica', 'Metal');
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));  
  
INSERT INTO Song VALUES
  (DEFAULT,  'Maid', 169, 8, 77, 1995, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Maid.wav', 'Metallica', 'Metal');
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

INSERT INTO Playlist VALUES
  (DEFAULT,  'Party');
  
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

INSERT INTO Album VALUES
  (DEFAULT,  'Panic Prevention', 2001, E'C:/music/JamieT');  
INSERT INTO Song VALUES
  (DEFAULT,  'Sticks n Stones', 275, 5, 12, 2001, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/Sleep.wav', 'JamieT', 'Folk, Ska, Electronic');
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

  
INSERT INTO Song VALUES
  (DEFAULT,  'Nosebleed Secton', 348, 9, 34, 2003, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/blabla.wav', 'Hilltop Hoods', 'Rap');
 
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO Song VALUES  
  (DEFAULT,  'I Kissed a Girl', 275, 5, 12, 2007, E'C:/eclipse/git_clones/SEPMmp3player/TestFiles/tery.wav', 'Katy Perry', 'Pop');
  
INSERT INTO contains VALUES
  (currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
