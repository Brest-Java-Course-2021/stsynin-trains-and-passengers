package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.model.dto.TrainDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class TrainDtoDaoJdbc implements TrainDtoDao {

    @Value("${TRN.sqlFindAllWithPassengersCount}")
    private String sqlFindAllWithPassengersCount;

    Logger logger = LoggerFactory.getLogger(TrainDtoDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TrainDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<TrainDto> findAllWithPassengersCount() {
        logger.debug("findAllWithPassengersCount()");
        List<TrainDto> trains = namedParameterJdbcTemplate.query(
                sqlFindAllWithPassengersCount,
                BeanPropertyRowMapper.newInstance(TrainDto.class));
        System.out.println(trains);
        return trains;
    }
}
