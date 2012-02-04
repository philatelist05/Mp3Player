package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.AlbumDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DbSongDao implements SongDao {
    private AlbumDao ad;
    private SimpleJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert createStmt;
    private SimpleJdbcInsert createIsOnStmt;

    DbSongDao(DataSource dataSource, AlbumDao ad) {
        this.ad = ad;
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.createStmt = new SimpleJdbcInsert(dataSource).withTableName("song").usingColumns("title", "artist", "path", "year", "duration", "playcount", "rating", "genre", "pathOk", "lyric").usingGeneratedKeyColumns("id");
        this.createIsOnStmt = new SimpleJdbcInsert(dataSource).withTableName("is_on").usingColumns("song", "album");
    }

    public void create(Song s) {
        if (s == null)
            throw new IllegalArgumentException("Song must not be null");

        Map<String, Object> parameters = new HashMap<String, Object>(10);
        parameters.put("title", s.getTitle());
        parameters.put("artist", s.getArtist());
        parameters.put("path", s.getPath());
        parameters.put("year", s.getYear());
        parameters.put("duration", s.getDuration());
        parameters.put("playcount", s.getPlaycount());
        parameters.put("rating", s.getRating());
        parameters.put("genre", s.getGenre());
        parameters.put("pathOk", s.isPathOk());

        if (s.getLyric() != null)
            parameters.put("lyric", s.getLyric().getText());
        else
            parameters.put("lyric", null);

        Number newId = this.createStmt.executeAndReturnKey(parameters);
        s.setId(newId.intValue());


        if (s.getAlbum() != null) {
            createAlbumAssociation(s);
        }
    }


    public void update(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song must not be null");
        }
        String lyric = (song.getLyric() != null) ? song.getLyric().getText() : null;
        this.jdbcTemplate.update("UPDATE song SET title=?, artist=?, path=?, year=?, duration=?, playcount=?, rating=?, genre=?, pathOk=?, lyric=? WHERE id = ?", song.getTitle(), song.getArtist(), song.getPath(), song.getYear(), song.getDuration(), song.getPlaycount(), song.getRating(), song.getGenre(), song.isPathOk(), lyric, song.getId());

        if (song.getAlbum() != null) {
            int anzahlAlbum = this.jdbcTemplate.queryForInt("SELECT COUNT(*) AS anzahl FROM is_on WHERE song=?", song.getId());
            if (anzahlAlbum > 0)
                updateAlbumInSong(song.getId(), song.getAlbum());
            else
                createAlbumAssociation(song);
        }
    }

    private void createAlbumAssociation(Song s) {
        // Create album if it doesn't exist
        Album album = s.getAlbum();
        ad.create(album);

        // Create album song association
        Map<String, Object> parameters;
        parameters = new HashMap<String, Object>(2);
        parameters.put("song", s.getId());
        parameters.put("album", album.getId());
        this.createIsOnStmt.execute(parameters);
    }

    private void updateAlbumInSong(int songId, Album album) {
        this.jdbcTemplate.update("UPDATE album SET title=?, year=?, albumart_path=? WHERE id IN (SELECT album FROM is_on WHERE song=?)", album.getTitle(), album.getYear(), album.getAlbumartPath(), songId);

    }

    public void delete(int id) {

        if (id < 0) {
            throw new IllegalArgumentException("ID must be greater or equal 0");
        }
        this.jdbcTemplate.update("DELETE FROM song WHERE id = ?", id);
    }

    @Override
    public Song read(int id) {
        ResultSet result = null;

        if (id < 0) {
            throw new IllegalArgumentException("ID must be greater or equal 0");
        }

        String sql = "SELECT id, title, artist, path, year, duration, playcount, rating, genre, pathOk, lyric, album FROM song LEFT JOIN is_on ON id=song WHERE id=?";
        RowMapper<Song> songMapper = new RowMapper<Song>() {
            @Override
            public Song mapRow(ResultSet resultSet, int i) throws SQLException {
                return mapResultSetToSong(resultSet);
            }
        };
        List<Song> songs = jdbcTemplate.query(sql, songMapper, id);
        if (songs.size() != 1)
            return null;
        return songs.get(0);
    }

    @Override
    public List<Song> readAll() {
        String sql = "SELECT id, title, artist, path, year, duration, playcount, rating, genre, pathOk, lyric, album FROM song LEFT JOIN is_on ON id=song";
        RowMapper<Song> songMapper = new RowMapper<Song>() {
            @Override
            public Song mapRow(ResultSet resultSet, int i) throws SQLException {
                return mapResultSetToSong(resultSet);
            }
        };
        return jdbcTemplate.query(sql, songMapper);
    }

    @Override
    public List<Song> getTopRatedSongs(int number) {
        if (number < 1)
            throw new IllegalArgumentException(
                    "The number XX stands for in TopXX... playlist must be greater than zero");

        String sql = "SELECT id, title, artist, path, year, duration, playcount, rating, genre, pathOk, lyric, album FROM song LEFT JOIN is_on ON id = song ORDER BY rating DESC LIMIT ?";
        RowMapper<Song> mapper = new RowMapper<Song>() {
            @Override
            public Song mapRow(ResultSet resultSet, int i) throws SQLException {
                return mapResultSetToSong(resultSet);
            }
        };
        return jdbcTemplate.query(sql, mapper, number);
    }

    @Override
    public List<Song> getTopPlayedSongs(int number) {
        if (number < 1)
            throw new IllegalArgumentException(
                    "The number XX stands for in TopXX... playlist must be greater than zero");

        String sql = "SELECT id, title, artist, path, year, duration, playcount, rating, genre, pathOk, lyric, album FROM song LEFT JOIN is_on ON id = song ORDER BY playcount DESC LIMIT ?";
        RowMapper<Song> mapper = new RowMapper<Song>() {
            @Override
            public Song mapRow(ResultSet resultSet, int i) throws SQLException {
                return mapResultSetToSong(resultSet);
            }
        };
        return jdbcTemplate.query(sql, mapper, number);
    }

    private Song mapResultSetToSong(ResultSet resultSet) throws SQLException {
        Song s = new Song(resultSet.getString("artist"), resultSet.getString("title"),
                resultSet.getInt("duration"), resultSet.getString("path"));
        s.setPlaycount(resultSet.getInt("playcount"));
        s.setRating(resultSet.getInt("rating"));
        s.setGenre(resultSet.getString("genre"));
        s.setArtist(resultSet.getString("artist"));
        s.setPathOk(resultSet.getBoolean("pathOk"));
        s.setId(resultSet.getInt("id"));

        String lyric = resultSet.getString("lyric");
        if (lyric != null)
            s.setLyric(new Lyric(lyric));
        else
            s.setLyric(null);

        s.setAlbum(ad.read(resultSet.getInt("album")));
        return s;
    }
}