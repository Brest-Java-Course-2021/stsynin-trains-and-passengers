package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.dao.jdbc.exception.ValidationErrorException;
import by.epam.brest.model.dto.TrainDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainDtoDaoJdbc implements TrainDtoDao {

    @Value("${TRN.sqlFindAllWithPassengersCount}")
    private String sqlFindAllWithPassengersCount;

    @Value("${TRN.sqlGetFilteredByDateTrainListWithPassengersCount}")
    private String sqlGetFilteredByDateTrainListWithPassengersCount;

    @Value("${TRN.sqlGetFilteredByStartDateTrainListWithPassengersCount}")
    private String sqlGetFilteredByStartDateTrainListWithPassengersCount;

    @Value("${TRN.sqlGetFilteredByEndDateTrainListWithPassengersCount}")
    private String sqlGetFilteredByEndDateTrainListWithPassengersCount;

    Logger logger = LoggerFactory.getLogger(TrainDtoDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDtoDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd) {
        logger.debug("getFilteredByDateTrainListWithPassengersCount()");
        logger.debug("Start of period: " + dateStart);
        logger.debug("End of period: " + dateEnd);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("START_DATE", dateStart);
        parameterSource.addValue("END_DATE", dateEnd);
        return namedParameterJdbcTemplate.query(
                getSqlRequestForFilteringTrainsByDate(dateStart, dateEnd),
                parameterSource,
                BeanPropertyRowMapper.newInstance(TrainDto.class));
    }

    private String getSqlRequestForFilteringTrainsByDate(LocalDate dateStart, LocalDate dateEnd) {
        if (dateStart == null && dateEnd == null) {
            logger.debug("Don't use a filter");
            return sqlFindAllWithPassengersCount;
        }
        if (dateStart == null) {
            logger.debug("Filtering only by end date");
            return sqlGetFilteredByEndDateTrainListWithPassengersCount;
        }
        if (dateEnd == null) {
            logger.debug("Filtering only by start date");
            return sqlGetFilteredByStartDateTrainListWithPassengersCount;
        }
        logger.debug("Filtering by period");
        if (dateEnd.isBefore(dateStart)) {
            logger.error("Wrong dats order for filtering");
            throw new ValidationErrorException("Wrong date order for filtering");
        }
        return sqlGetFilteredByDateTrainListWithPassengersCount;
    }
}