package by.epam.brest.dao.jdbc;

import by.epam.brest.PassengerDao;
import by.epam.brest.model.Passenger;
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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.*;

public class PassengerDaoJdbc implements PassengerDao {

    @SuppressWarnings("unused")
    @Value("${PSG.sqlGetAllPassengers}")
    private String sqlGetAllPassengers;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlGetPassengerById}")
    private String sqlGetPassengerById;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlGetPassengerByName}")
    private String sqlGetPassengerByName;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlUpdatePassenger}")
    private String sqlUpdatePassenger;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlCreatePassenger}")
    private String sqlCreatePassenger;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlDeletePassengerById}")
    private String sqlDeletePassengerById;

    @SuppressWarnings("unused")
    @Value("${PSG.sqlGetPassengersCount}")
    private String sqlGetPassengersCount;

    Logger logger = LoggerFactory.getLogger(PassengerDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PassengerDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    RowMapper<Passenger> rowMapper = BeanPropertyRowMapper.newInstance(Passenger.class);

    @Override
    public List<Passenger> findAll() {
        logger.debug("Get all passengers");
        List<Passenger> allPassengers = namedParameterJdbcTemplate.query(
                sqlGetAllPassengers,
                rowMapper);
        logger.debug("... found {} passenger(s)", allPassengers.size());
        return allPassengers;
    }

    @Override
    public Optional<Passenger> findById(Integer passengerId) {
        logger.debug("Find passenger by id: {}", passengerId);
        List<Passenger> passengers = namedParameterJdbcTemplate.query(
                sqlGetPassengerById,
                new MapSqlParameterSource(PASSENGER_ID, passengerId),
                rowMapper);
        if (passengers.size() == 0) {
            logger.error("Passenger id: {} not found", passengerId);
            throw new IllegalArgumentException("Unknown passenger id: " + passengerId);
        }
        return Optional.ofNullable(DataAccessUtils.uniqueResult(passengers));
    }

    @Override
    public Integer createPassenger(Passenger passenger) {
        if (isPassengerNameExists(passenger)) {
            logger.error("Passenger named {} is already exists", passenger.getPassengerName());
            throw new IllegalArgumentException("Duplicate passenger name: " + passenger.getPassengerName());
        }
        SqlParameterSource parameterSource = newFillParameterSource(passenger);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlCreatePassenger,
                parameterSource,
                keyHolder);
        Integer newPassengerId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        passenger.setPassengerId(newPassengerId);
        logger.debug("Create new {}", passenger);
        return newPassengerId;
    }

    @Override
    public Integer updatePassenger(Passenger passenger) {
        if (isPassengerNameExists(passenger)) {
            logger.error("Passenger named {} is already exists", passenger.getPassengerName());
            throw new IllegalArgumentException("Duplicate passenger name: " + passenger.getPassengerName());
        }
        logger.debug("Update {}", passenger);
        SqlParameterSource parameterSource = newFillParameterSource(passenger);
        return namedParameterJdbcTemplate.update(
                sqlUpdatePassenger,
                parameterSource);
    }

    @Override
    public Integer deletePassenger(Integer passengerId) {
        logger.debug("Delete passenger id: {}", passengerId);
        return namedParameterJdbcTemplate.update(
                sqlDeletePassengerById,
                new MapSqlParameterSource(PASSENGER_ID, passengerId));
    }

    @Override
    public Integer getPassengersCount() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetPassengersCount,
                new HashMap<>(),
                Integer.class);
    }

    private SqlParameterSource newFillParameterSource(Passenger passenger) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.
                addValue(PASSENGER_ID, passenger.getPassengerId()).
                addValue(PASSENGER_NAME, passenger.getPassengerName()).
                addValue(TRAIN_ID, passenger.getTrainId());
        return parameterSource;
    }

    private boolean isPassengerNameExists(Passenger passenger) {
        List<Passenger> passengers = namedParameterJdbcTemplate.query(
                sqlGetPassengerByName,
                new MapSqlParameterSource(PASSENGER_NAME, passenger.getPassengerName()),
                rowMapper);
        return passengers.size() > 0;
    }
}