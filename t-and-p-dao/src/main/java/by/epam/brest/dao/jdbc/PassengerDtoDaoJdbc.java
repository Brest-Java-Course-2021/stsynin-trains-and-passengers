package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.PassengerDtoDao;
import by.epam.brest.model.dto.PassengerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class PassengerDtoDaoJdbc implements PassengerDtoDao {

    @Value("${PSG.sqlFindAllPassengersWithTrainName}")
    private String sqlFindAllPassengersWithTrainName;

    Logger logger = LoggerFactory.getLogger(TrainDtoDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PassengerDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<PassengerDto> findAllPassengersWithTrainName() {
        logger.debug("findAllPassengersWithTrainName()");
        return namedParameterJdbcTemplate.query(
                sqlFindAllPassengersWithTrainName,
                BeanPropertyRowMapper.newInstance(PassengerDto.class));
    }
}
