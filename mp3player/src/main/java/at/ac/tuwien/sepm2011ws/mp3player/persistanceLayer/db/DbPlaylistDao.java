package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DbPlaylistDao implements PlaylistDao {
    private SongDao sd;
    private SimpleJdbcInsert createStmt;
    private SimpleJdbcInsert createContainsStmt;
    private SimpleJdbcTemplate jdbcTemplate;

	@Autowired
    DbPlaylistDao(DataSource dataSource, SongDao sd) {
        this.createStmt = new SimpleJdbcInsert(dataSource).withTableName("playlist").usingColumns("name").usingGeneratedKeyColumns("id");
        this.createContainsStmt = new SimpleJdbcInsert(dataSource).withTableName("contains").usingColumns("position", "playlist", "song");
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.sd = sd;
    }

    @Override
    public void create(WritablePlaylist playlist) {
        if (playlist.getTitle() == null) throw new IllegalArgumentException("Title of Playlist must not be null");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("name", playlist.getTitle());
        Number newId = this.createStmt.executeAndReturnKey(parameters);
        playlist.setId(newId.intValue());

        createSongAssociation(playlist);
    }

    @Override
    public void update(WritablePlaylist playlist) {
        if (playlist == null) throw new IllegalArgumentException("Playlist must not be null");

        this.jdbcTemplate.update("UPDATE playlist SET name=? WHERE id = ?", playlist.getTitle(), playlist.getId());
        this.jdbcTemplate.update("DELETE FROM contains WHERE playlist = ?;", playlist.getId());
        createSongAssociation(playlist);
    }

    private void createSongAssociation(WritablePlaylist playlist) {
        Map<String, Object> parameters;
        int i = 0;
        for (Song s : playlist) {
            createOrUpdateSong(s);
            parameters = new HashMap<String, Object>(3);
            parameters.put("position", i++);
            parameters.put("playlist", playlist.getId());
            parameters.put("song", s.getId());
            this.createContainsStmt.execute(parameters);
        }
    }

    private void createOrUpdateSong(Song song) {
        Song songInDb = this.sd.read(song.getId());
        if(songInDb == null)
            sd.create(song);
        else
            sd.update(song);
    }

    @Override
    public void delete(int id) {
        if (id < 0) throw new IllegalArgumentException("ID must be greater or equal 0");

        this.jdbcTemplate.update("DELETE FROM playlist WHERE id = ?;", id);
    }

    @Override
    public WritablePlaylist read(final int id) {
        if (id < 0) throw new IllegalArgumentException("ID must be greater or equal 0");

        String sql = "SELECT name FROM playlist WHERE id = ?";
        RowMapper<WritablePlaylist> mapper = new RowMapper<WritablePlaylist>() {
            @Override
            public WritablePlaylist mapRow(ResultSet resultSet, int i) throws SQLException {
                WritablePlaylist playlist = new WritablePlaylist(id, resultSet.getString("name"));
                List<Song> songs = readSongsFromPlaylist(playlist);
                playlist.addAll(songs);
                return playlist;
            }
        };
        List<WritablePlaylist> playlists = this.jdbcTemplate.query(sql, mapper, id);

        if (playlists.size() != 1)
            return null;

        WritablePlaylist playlist = playlists.get(0);

        playlist.addAll(readSongsFromPlaylist(playlist));
        return playlist;
    }

    private List<Song> readSongsFromPlaylist(WritablePlaylist playlist) {
        String sql = "SELECT song FROM contains WHERE playlist = ? ORDER BY position";

        RowMapper<Song> mapper = new RowMapper<Song>() {
            @Override
            public Song mapRow(ResultSet resultSet, int i) throws SQLException {
                Song song = sd.read(resultSet.getInt("song"));
                return song;
            }
        };
        return this.jdbcTemplate.query(sql, mapper, playlist.getId());
    }

    @Override
    public List<WritablePlaylist> readAll() {
        String sql = "SELECT id FROM playlist ORDER BY id";

        RowMapper<WritablePlaylist> mapper = new RowMapper<WritablePlaylist>() {
            @Override
            public WritablePlaylist mapRow(ResultSet resultSet, int i) throws SQLException {
                return read(resultSet.getInt("id"));
            }
        };
        return this.jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void rename(WritablePlaylist p, String name) {
        if (p == null || name == null || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Playlist and name must not be null and name must not be empty");
        }

        this.jdbcTemplate.update("UPDATE playlist SET name=? WHERE id = ?", name, p.getId());
    }
}
