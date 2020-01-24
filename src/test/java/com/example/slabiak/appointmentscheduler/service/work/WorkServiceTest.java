package com.example.slabiak.appointmentscheduler.service.work;

import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.service.impl.WorkServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkServiceImpl workService;

    private Work work;
    private Optional<Work> workOptional;
    private List<Work> works;

    @Before
    public void initObjects() {
        work = new Work();
        workOptional = Optional.of(work);
    }

    @Test
    public void shouldSaveWork() {
        workService.createNewWork(work);
        verify(workRepository, times(1)).save(work);
    }

    @Test
    public void shouldFindWorkById() {
        when(workRepository.findById(1)).thenReturn(workOptional);
        assertEquals(workOptional.get(), workService.getWorkById(1));
        verify(workRepository, times(1)).findById(1);
    }

    @Test
    public void shouldFindAllWorks() {
        when(workRepository.findAll()).thenReturn(works);
        assertEquals(works, workService.getAllWorks());
        verify(workRepository, times(1)).findAll();
    }

    @Test
    public void shouldDeleteWorkById() {
        workService.deleteWorkById(1);
        verify(workRepository).deleteById(1);
    }
}
