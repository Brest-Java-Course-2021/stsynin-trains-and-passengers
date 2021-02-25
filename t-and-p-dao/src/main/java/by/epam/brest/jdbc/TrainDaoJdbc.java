package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.*;

public class TrainDaoJdbc implements TrainDao {

    private static final String SQL_GET_ALL_TRAINS =
            "SELECT * FROM TRAIN AS D ORDER BY D.TRAIN_NAME";
    private static final String SQL_GET_TRAIN_BY_ID =
            "SELECT * FROM TRAIN AS D WHERE TRAIN_ID = :TRAIN_ID ORDER BY TRAIN_NAME";
    private static final String SQL_UPDATE_TRAIN =
            "UPDATE TRAIN SET TRAIN_NAME = :TRAIN_NAME, TRAIN_DESTINATION = :TRAIN_DESTINATION, TRAIN_DEPARTURE_DATE = :TRAIN_DEPARTURE_DATE WHERE TRAIN_ID = :TRAIN_ID";
    private static final String SQL_CREATE_TRAIN =
            "INSERT INTO TRAIN (TRAIN_NAME, TRAIN_DESTINATION, TRAIN_DEPARTURE_DATE) VALUES(:TRAIN_NAME, :TRAIN_DESTINATION, :TRAIN_DEPARTURE_DATE)";
    private static final String SQL_GET_TRAIN_BY_NAME =
            "SELECT * FROM TRAIN AS D WHERE TRAIN_NAME = :TRAIN_NAME";
    private static final String SQL_DELETE_TRAIN =
            "DELETE FROM TRAIN WHERE TRAIN_ID = :TRAIN_ID";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    RowMapper<Train> rowMapper = BeanPropertyRowMapper.newInstance(Train.class);

    @Override
    public List<Train> findAll() {
        return namedParameterJdbcTemplate.query(
                SQL_GET_ALL_TRAINS,
                rowMapper);
    }

    @Override
    public Optional<Train> findById(Integer trainId) {
        List<Train> trains = namedParameterJdbcTemplate.query(
                SQL_GET_TRAIN_BY_ID,
                new MapSqlParameterSource(TRAIN_ID, trainId),
                rowMapper);
        if (trains.size() == 0) {
            throw new IllegalArgumentException("Unknown train id: " + trainId);
        }
        return Optional.ofNullable(DataAccessUtils.uniqueResult(trains));
    }

    @Override
    public Integer updateTrain(Train train) {
        SqlParameterSource parameterSource = newFillParameterSource(train);
        return namedParameterJdbcTemplate.update(
                SQL_UPDATE_TRAIN,
                parameterSource);
    }

    @Override
    public Integer createTrain(Train train) {
        if (isTrainNameExists(train)) {
            throw new IllegalArgumentException("Duplicate train name: " + train.getTrainName());
        }
        SqlParameterSource parameterSource = newFillParameterSource(train);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                SQL_CREATE_TRAIN,
                parameterSource,
                keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Integer deleteTrain(Integer trainId) {
        return namedParameterJdbcTemplate.update(
                SQL_DELETE_TRAIN,
                new MapSqlParameterSource(TRAIN_ID, trainId));
    }

    private SqlParameterSource newFillParameterSource(Train train) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.
                addValue(TRAIN_ID, train.getTrainId()).
                addValue(TRAIN_NAME, train.getTrainName()).
                addValue(TRAIN_DESTINATION, train.getTrainDestination()).
                addValue(TRAIN_DEPARTURE_DATE, train.getTrainDepartureDate());
        return parameterSource;
    }

    private boolean isTrainNameExists(Train train) {
        List<Train> trains = namedParameterJdbcTemplate.query(
                SQL_GET_TRAIN_BY_NAME,
                new MapSqlParameterSource(TRAIN_NAME, train.getTrainName()),
                rowMapper);
        return trains.size() > 0;
    }
}
