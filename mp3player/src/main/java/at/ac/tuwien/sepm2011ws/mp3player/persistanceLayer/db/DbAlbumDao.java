package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.AlbumDao;
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

class DbAlbumDao implements AlbumDao {

	private SimpleJdbcInsert createStmt;
    private SimpleJdbcTemplate jdbcTemplate;

	@Autowired
	DbAlbumDao(DataSource dataSource)  {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.createStmt = new SimpleJdbcInsert(dataSource).withTableName("album").usingColumns("title", "year", "albumart_path").usingGeneratedKeyColumns("id");
	}

	public void create(Album a)  {
		if (a == null)
			throw new IllegalArgumentException("Album must not be null");

        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("title", a.getTitle());
        parameters.put("year", a.getYear());
        parameters.put("albumart_path", a.getAlbumartPath());
        Number newId = this.createStmt.executeAndReturnKey(parameters);
        a.setId(newId.intValue());
	}

	public void update(Album a) {
		if (a == null) {
			throw new IllegalArgumentException("Album must not be null");
		}
        this.jdbcTemplate.update("UPDATE album SET title=?, year=?, albumart_path=? WHERE id = ?",a.getTitle(),a.getYear(),a.getAlbumartPath(),a.getId());
	}

	public void delete(int id)  {
		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}
        this.jdbcTemplate.update("DELETE FROM album WHERE id = ?", id);

	}

	public Album read(final int id)  {
		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}
        String sql =  "SELECT title, year, albumart_path FROM album WHERE id=?";
        RowMapper<Album> mapper = new RowMapper<Album>() {
            @Override
            public Album mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Album(id, resultSet.getString("title"), resultSet.getInt("year"),
                        resultSet.getString("albumart_path"));

            }
        };
        List<Album> albums = jdbcTemplate.query(sql, mapper, id);

        if(albums.size() == 1)
            return albums.get(0);
        return null;
	}
}
