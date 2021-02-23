package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TrainDaoJdbc implements TrainDao {

    private static final String SQL_GET_ALL_TRAINS =
            "SELECT D.TRAIN_ID, D. FROM TRAIN AS D ORDER BY R.TRAIN_NAME";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Train> findAll() {
        return namedParameterJdbcTemplate.query(SQL_GET_ALL_TRAINS, new DepartmentRowMapper());
    }

    private class DepartmentRowMapper implements RowMapper<Train>{

        @Override
        public Train mapRow(ResultSet resultSet, int i) throws SQLException {
            return null;
        }
    }
}
