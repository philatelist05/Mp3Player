DROP TRIGGER IF EXISTS songExistingCheck ON song;
DROP FUNCTION IF EXISTS songExistingCheck();
DROP TRIGGER IF EXISTS albumExistingCheck ON album;
DROP FUNCTION IF EXISTS albumExistingCheck();

DROP TABLE contains;
DROP TABLE is_on;
DROP TABLE Playlist;
DROP TABLE Album;
DROP TABLE Song;
