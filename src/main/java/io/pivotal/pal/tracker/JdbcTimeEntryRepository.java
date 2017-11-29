package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private final String CREATE_SQL = "INSERT INTO time_entries (project_id,user_id,date,hours) VALUES (?,?,?,?) ";
    private final String GET_ENTRY_SQL = " SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?";
    private final String LIST_ENTRY_SQL = "SELECT id, project_id, user_id, date, hours FROM time_entries ";
    private final String DELETE_ENTRY_SQL = "DELETE FROM time_entries WHERE id = ?";
    private final String UPDATE_ENTRY_SQL = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?,  hours = ? WHERE id = ?";

    public JdbcTimeEntryRepository(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pstmt = connection.prepareStatement(CREATE_SQL, RETURN_GENERATED_KEYS);
                pstmt.setLong(1, timeEntry.getProjectId());
                pstmt.setLong(2, timeEntry.getUserId());
                pstmt.setDate(3, Date.valueOf(timeEntry.getDate()));
                pstmt.setInt(4, timeEntry.getHours());
                return pstmt;
            }
        }, keyHolder);
        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
         return jdbcTemplate.query(GET_ENTRY_SQL, new Object[] { id }, rsExtractor);
        }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(LIST_ENTRY_SQL,new BeanPropertyRowMapper(TimeEntry.class));
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update(UPDATE_ENTRY_SQL,timeEntry.getProjectId(),timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_ENTRY_SQL,id);
    }

    private final RowMapper<TimeEntry> rowMapper = (resultSet, rowNum) -> new TimeEntry(
            resultSet.getLong("id"),
            resultSet.getLong("project_id"),
            resultSet.getLong("user_id"),
            resultSet.getDate("date").toLocalDate(),
            resultSet.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> rsExtractor =
            (resultSet) -> resultSet.next() ? rowMapper.mapRow(resultSet, 1) : null;
}


