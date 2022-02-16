package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDao;
import by.epam.brest.model.exception.ValidationErrorException;
import by.epam.brest.model.exception.ArgumentNullException;
import by.epam.brest.model.exception.ArgumentOutOfRangeException;
import by.epam.brest.model.Train;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.*;

@Repository
public class TrainDaoJdbc implements TrainDao {

    @SuppressWarnings("unused")
    @Value("${TRN.sqlGetAllTrains}")
    private String sqlGetAllTrains;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlGetTrainById}")
    private String sqlGetTrainById;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlGetTrainByName}")
    private String sqlGetTrainByName;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlUpdateTrain}")
    private String sqlUpdateTrain;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlCreateTrain}")
    private String sqlCreateTrain;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlDeleteTrainById}")
    private String sqlDeleteTrainById;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlGetTrainsCount}")
    private String sqlGetTrainsCount;

    @SuppressWarnings("unused")
    @Value("${TRN.sqlGetPassengersCountForTrain}")
    private String sqlGetPassengersCountForTrain;

    Logger logger = LoggerFactory.getLogger(TrainDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    RowMapper<Train> rowMapper = BeanPropertyRowMapper.newInstance(Train.class);

    @Override
    public List<Train> findAll() {
        logger.debug("Get all trains");
        List<Train> allTrains = namedParameterJdbcTemplate.query(
                sqlGetAllTrains,
                rowMapper);
        logger.debug("... found {} train(s)", allTrains.size());
        return allTrains;
    }

    @Override
    public Optional<Train> findById(Integer trainId) {
        logger.debug("Find train by id: {}", trainId);
        List<Train> trains = namedParameterJdbcTemplate.query(
                sqlGetTrainById,
                new MapSqlParameterSource(TRAIN_ID, trainId),
                rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(trains));
    }

    @Override
    public Integer updateTrain(Train train) {
        checksTrainNames(train, "Update");
        logger.debug("Update {}", train);
        SqlParameterSource parameterSource = newFillParameterSource(train);
        return namedParameterJdbcTemplate.update(
                sqlUpdateTrain,
                parameterSource);
    }

    @Override
    public Integer createTrain(Train train) {
        checksTrainNames(train, "Create");
        SqlParameterSource parameterSource = newFillParameterSource(train);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlCreateTrain,
                parameterSource,
                keyHolder);
        Integer newTrainId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        train.setTrainId(newTrainId);
        logger.debug("Create new {}", train);
        return newTrainId;
    }

    @Override
    public Integer deleteTrain(Integer trainId) {
        logger.debug("Delete train id: {}", trainId);
        return namedParameterJdbcTemplate.update(
                sqlDeleteTrainById,
                new MapSqlParameterSource(TRAIN_ID, trainId));
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetTrainsCount,
                new HashMap<>(),
                Integer.class);
    }

    private Integer getPassengerCountForTrain(Integer trainId) {
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetPassengersCountForTrain,
                new MapSqlParameterSource(TRAIN_ID, trainId),
                Integer.class);
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

    private boolean isSecondTrainWithSameNameExists(Train train) {
        List<Train> trains = namedParameterJdbcTemplate.query(
                sqlGetTrainByName,
                new MapSqlParameterSource(TRAIN_NAME, train.getTrainName()),
                rowMapper);
        return trains.size() > 0 && !trains.get(0).getTrainId().equals(train.getTrainId());
    }

    private void checksTrainNames(Train train, String stage) {
        String trainName = train.getTrainName();
        if (trainName == null) {
            logger.error(stage + " fail. Train name is null");
            throw new ArgumentNullException(stage + " fail. Train name can't be empty");
        }
        if (trainName.length() > MAX_TRAIN_NAME_LENGTH) {
            logger.error(stage + " fail. Train name {} is too long", trainName);
            throw new ArgumentOutOfRangeException(
                    stage + " fail. This name is too long.");
        }
        if (isSecondTrainWithSameNameExists(train)) {
            logger.error(stage + " fail. Train named {} is already exists", trainName);
            throw new ValidationErrorException(
                    stage + " fail. This name already exists.");
        }
        if (train.getTrainDestination() != null &&
                train.getTrainDestination().length() > MAX_TRAIN_DESTINATION_NAME_LENGTH) {
            logger.error(stage + " fail. Train destination name {} is too long", train.getTrainDestination());
            throw new ArgumentOutOfRangeException(
                    stage + " fail. This name of destination is too long.");
        }
    }
}
