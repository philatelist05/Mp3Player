-- Playlist Metääälll

INSERT INTO Playlist VALUES
  (DEFAULT,  'Metääälll');

-- 1st Song
INSERT INTO Album VALUES
  (DEFAULT,  'Black Album', 1995, E'C:\\music\\Metallica');

INSERT INTO Song VALUES
  (DEFAULT,  'Enter Sandman', 184, 9, 34, 1995, E'C:\\music\\Metallica\\entersandman', 'Metallica', 'Metal');
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

INSERT INTO contains VALUES
  (0, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));  
  
-- 2nd Song
INSERT INTO Song VALUES
  (DEFAULT,  'Wherever I May Roam', 133, 8, 77, 1995, E'C:\\music\\Metallica\\whereverimayroam', 'Metallica', 'Metal');
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
  (DEFAULT,  'Panic Prevention', 2001, E'C:\\music\\JamieT');  
INSERT INTO Song VALUES
  (DEFAULT,  'Sticks n Stones', 213, 5, 12, 2001, E'C:\\music\\JamieT\\sticksnstones', 'JamieT', 'Folk, Ska, Electronic');
INSERT INTO is_on VALUES
  (currval(pg_get_serial_sequence('Album', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
  
INSERT INTO contains VALUES
  (1, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));

-- 3rd Song
INSERT INTO Song VALUES
  (DEFAULT,  'Nosebleed Secton', 678, 9, 34, 2003, E'C:\\music\\HilltopHoods\\nosebleedsection', 'Hilltop Hoods', 'Rap');
 
INSERT INTO contains VALUES
  (2, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));


-- 4th Song
INSERT INTO Song VALUES  
  (DEFAULT,  'I Kissed a Girl', 302, 5, 12, 2007, E'C:\\music\\KatyPerry\\ikissedagirl', 'Katy Perry', 'Pop');
  
INSERT INTO contains VALUES
  (3, currval(pg_get_serial_sequence('Playlist', 'id')),  currval(pg_get_serial_sequence('Song', 'id')));
