package by.epam.brest.service.impl;

import by.epam.brest.dao.jdbc.TrainDaoJdbc;
import by.epam.brest.model.Train;
import by.epam.brest.service.exception.ResourceLockedException;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(MockitoExtension.class)
class TrainServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainServiceImplTest.class);

    @InjectMocks
    TrainServiceImpl trainService;

    @Mock
    TrainDaoJdbc trainDao;

    @Test
    void shouldFindAll() {
        LOGGER.info("shouldFindAll()");

        // given
        List<Train> testTrains = Arrays.asList(
                new Train(1, "first", "up", LocalDate.now()),
                new Train(2, "second", "down", LocalDate.now()));
        when(trainDao.findAll()).thenReturn(testTrains);

        // when
        List<Train> trains = trainService.findAll();

        // then
        assertEquals(testTrains, trains);
        verify(trainDao).findAll();
    }

    @Test
    void shouldFindById() {
        LOGGER.info("shouldFindById()");

        // given
        Train testTrain = new Train(1, "first", "up", LocalDate.now());
        when(trainDao.findById(1)).thenReturn(Optional.of(testTrain));

        // when
        Train train = trainService.findById(1);

        // then
        assertEquals(testTrain, train);
        verify(trainDao).findById(1);
    }

    @Test
    void shouldReturnExceptionWithUnknownId() {
        LOGGER.info("shouldFindById()");

        // given
        when(trainDao.findById(9)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> trainService.findById(9));

        // then
        assertEquals("No train with id 9 exists!", exception.getMessage());
        verify(trainDao).findById(9);
    }

    @Test
    void shouldCreateTrain() {
        LOGGER.info("shouldCreateTrain()");

        // given
        Train testTrain = new Train(null, "new", "up", LocalDate.now());
        when(trainDao.createTrain(testTrain)).thenReturn(3);

        // when
        Integer id = trainService.createTrain(testTrain);

        // then
        assertEquals(3, id);
        verify(trainDao).createTrain(testTrain);
    }

    @Test
    void shouldUpdateTrain() {
        LOGGER.info("shouldUpdateTrain()");

        // given
        Train testTrain = new Train(1, "new", "up", LocalDate.now());
        when(trainDao.updateTrain(testTrain)).thenReturn(1);

        // when
        Integer count = trainService.updateTrain(testTrain);

        // then
        assertEquals(1, count);
        verify(trainDao).updateTrain(testTrain);
    }

    @Test
    void shouldDeleteById() {
        LOGGER.info("shouldDeleteById()");

        // given
        when(trainDao.deleteTrain(1)).thenReturn(1);

        // when
        Integer count = trainService.deleteById(1);

        // then
        assertEquals(1, count);
        verify(trainDao).deleteTrain(1);
    }

    @Test
    void shouldReturnExceptionIfDeleteTrainWithUnknownId() {
        LOGGER.info("shouldReturnExceptionIfDeleteTrainWithUnknownId()");

        // given
        when(trainDao.deleteTrain(9)).thenReturn(0);

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> trainService.deleteById(9));

        // then
        assertEquals("No train with id 9 exists!", exception.getMessage());
        verify(trainDao).deleteTrain(9);
    }

    @Test
    void shouldReturnExceptionIfDeleteLockedTrain() {
        LOGGER.info("shouldReturnExceptionIfDeleteLockedTrain()");

        // given
        when(trainDao.deleteTrain(2)).thenThrow(DataIntegrityViolationException.class);

        // when
        Exception exception = assertThrows(ResourceLockedException.class,
                () -> trainService.deleteById(2));

        // then
        assertEquals("Delete fail. There are registered passengers. Train id:2", exception.getMessage());
        verify(trainDao).deleteTrain(2);
    }

    @Test
    void shouldGetTrainsCount() {
        LOGGER.info("shouldGetTrainsCount()");

        // given
        when(trainDao.count()).thenReturn(13);

        // when
        Integer count = trainService.getTrainsCount();

        // then
        assertEquals(13, count);
        verify(trainDao).count();
    }
}