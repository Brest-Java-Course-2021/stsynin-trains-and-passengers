package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.model.dto.TrainDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class TrainDtoDaoJdbc implements TrainDtoDao {

    @Value("${TRN.sqlFindAllWithPassengersCount}")
    private String sqlFindAllWithPassengersCount;

    @Value("${TRN.sqlGetFilteredByDateTrainListWithPassengersCount}")
    private String sqlGetFilteredByDateTrainListWithPassengersCount;

    Logger logger = LoggerFactory.getLogger(TrainDtoDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<TrainDto> findAllWithPassengersCount() {
        logger.debug("findAllWithPassengersCount()");
        return namedParameterJdbcTemplate.query(
                sqlFindAllWithPassengersCount,
                BeanPropertyRowMapper.newInstance(TrainDto.class));
    }

    @Override
    public List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd) {
        logger.debug("getFilteredByDateTrainListWithPassengersCount()");
        logger.debug("Start of period: " + dateStart);
        logger.debug("End of period: " + dateEnd);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("START_DATE", dateStart);
        parameterSource.addValue("END_DATE", dateEnd);
        System.out.println(parameterSource);
        return namedParameterJdbcTemplate.query(
                sqlGetFilteredByDateTrainListWithPassengersCount,
                parameterSource,
                BeanPropertyRowMapper.newInstance(TrainDto.class));
    }
}
