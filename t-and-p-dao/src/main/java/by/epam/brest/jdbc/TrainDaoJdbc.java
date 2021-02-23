package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.*;

public class TrainDaoJdbc implements TrainDao {

    private static final String SQL_GET_ALL_TRAINS =
            "SELECT * FROM TRAIN AS D ORDER BY D.TRAIN_NAME";
    private static final String SQL_GET_TRAIN_BY_ID =
            "SELECT * FROM TRAIN AS D WHERE TRAIN_ID = :TRAIN_ID ORDER BY TRAIN_NAME";
    private static final String SQL_UPDATE_TRAIN =
            "UPDATE TRAIN SET TRAIN_NAME = :TRAIN_NAME WHERE TRAIN_ID = :TRAIN_ID";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    MapSqlParameterSource parameterSource = new MapSqlParameterSource();

    @Override
    public List<Train> findAll() {
        return namedParameterJdbcTemplate.query(
                SQL_GET_ALL_TRAINS,
                new TrainRowMapper());
    }

    @Override
    public Optional<Train> findById(Integer trainId) {
        parameterSource.addValue(TRAIN_ID, trainId); // TODO constant file need
        List<Train> trains = namedParameterJdbcTemplate.query(
                SQL_GET_TRAIN_BY_ID,
                parameterSource,
                new TrainRowMapper());
        return Optional.ofNullable(DataAccessUtils.uniqueResult(trains));
    }

    @Override
    public Integer updateTrain(Train train) {
        parameterSource.addValue(TRAIN_ID, train.getTrainId()).
                addValue(TRAIN_NAME, train.getTrainName()).
                addValue(TRAIN_DESTINATION, train.getTrainDestination()).
                addValue(TRAIN_DEPARTURE_DATE, train.getTrainDepartureDate());
        return namedParameterJdbcTemplate.update(
                SQL_UPDATE_TRAIN,
                parameterSource);
    }

    @Override
    public Integer createTrain(Train train) {
        //TODO check uniqueness
//        private Integer trainId;
//        private String trainName;
//        private String trainDestination;
//        private LocalDate trainDepartureDate;

        return null;
//        INSERT INTO TRAIN (TRAIN_NAME) VALUES('second');
    }

    private static class TrainRowMapper implements RowMapper<Train> {

        @Override
        public Train mapRow(ResultSet resultSet, int i) throws SQLException {
            Train train = new Train();
            train.setTrainId(resultSet.getInt("TRAIN_ID"));
            train.setTrainName(resultSet.getString("TRAIN_NAME"));
            //TODO add other fields
            return train;
        }
    }


}
