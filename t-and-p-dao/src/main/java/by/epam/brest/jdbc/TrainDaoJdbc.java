package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${TRN.sqlGetAllTrains}")
    private String sqlGetAllTrains;

    @Value("${TRN.sqlGetTrainById}")
    private String sqlGetTrainById;

    @Value("${TRN.sqlGetTrainByName}")
    private String sqlGetTrainByName;

    @Value("${TRN.sqlUpdateTrain}")
    private String sqlUpdateTrain;

    @Value("${TRN.sqlCreateTrain}")
    private String sqlCreateTrain;

    @Value("${TRN.sqlDeleteTrainById}")
    private String sqlDeleteTrainById;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    RowMapper<Train> rowMapper = BeanPropertyRowMapper.newInstance(Train.class);

    @Override
    public List<Train> findAll() {
        return namedParameterJdbcTemplate.query(
                sqlGetAllTrains,
                rowMapper);
    }

    @Override
    public Optional<Train> findById(Integer trainId) {
        List<Train> trains = namedParameterJdbcTemplate.query(
                sqlGetTrainById,
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
                sqlUpdateTrain,
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
                sqlCreateTrain,
                parameterSource,
                keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Integer deleteTrain(Integer trainId) {
        return namedParameterJdbcTemplate.update(
                sqlDeleteTrainById,
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
                sqlGetTrainByName,
                new MapSqlParameterSource(TRAIN_NAME, train.getTrainName()),
                rowMapper);
        return trains.size() > 0;
    }
}
