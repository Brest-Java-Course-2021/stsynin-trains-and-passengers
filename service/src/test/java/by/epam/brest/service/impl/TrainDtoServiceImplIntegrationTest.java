package by.epam.brest.service.impl;

import by.epam.brest.dao.jdbc.TrainDtoDaoJdbc;
import by.epam.brest.model.dto.TrainDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainDtoServiceImplIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDtoServiceImplIntegrationTest.class);

    @InjectMocks
    TrainDtoServiceImpl trainDtoService;

    @Mock
    TrainDtoDaoJdbc trainDtoDao;


    @Test
    void shouldReturnTrainListWithPassengersCount() {
        LOGGER.info("shouldReturnTrainListWithPassengersCount()");

        // given
        LocalDate start = LocalDate.of(1900, 1, 1);
        LocalDate end = LocalDate.of(2000, 1, 1);
        List<TrainDto> testTrains = Arrays.asList(
                new TrainDto(1, "first", "up", LocalDate.now(), 5),
                new TrainDto(2, "second", "down", LocalDate.now(), 6));
        when(trainDtoDao.getFilteredByDateTrainListWithPassengersCount(start, end)).thenReturn(testTrains);

        // when
        List<TrainDto> trainDtos = trainDtoService.getFilteredByDateTrainListWithPassengersCount(start, end);

        // then
        assertEquals(testTrains, trainDtos);
        verify(trainDtoDao).getFilteredByDateTrainListWithPassengersCount(start, end);
    }
}