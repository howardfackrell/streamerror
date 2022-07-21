package hlf.streamerror;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoredWorkService {

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public List<Integer> readAll() {
    var sql = "select work_value from work order by work_value asc";
    var list =
        jdbcTemplate.query(
            sql,
            new RowMapper<Integer>() {
              @Override
              public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
              }
            });
    return list;
  }

  @Transactional
  public void deleteAll() {
    jdbcTemplate.update("delete from work");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void writeWork(Integer value) {
    Object[] params = {value};
    jdbcTemplate.update("insert into work (work_value) values (?)", params);
  }
}
