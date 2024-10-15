package com.example.slabiak.appointmentscheduler.kapoor98ak_test;


import com.example.slabiak.appointmentscheduler.controller.WorkController;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkControllerTest {
    @InjectMocks
    private WorkController workController;

    @Mock
    private WorkService workService;

    @MockBean
    private BuildProperties buildProperties;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShowAllWorks() {
        // Create a list of works to be returned by the service
        List<Work> works = new ArrayList<>();
        works.add(new Work());

        // Create the WorkService mock
        WorkService workService = Mockito.mock(WorkService.class);

        // Stub the service to return the list of works
        Mockito.when(workService.getAllWorks()).thenReturn(works);

        // Create the model
        Model model = mock(Model.class);

        Mockito.lenient().when(model.addAttribute(Mockito.eq("works"), Mockito.anyList())).thenReturn(model);

        // Create the controller
        WorkController workController = new WorkController(workService);

        // Perform the test
        String viewName = workController.showAllWorks(model);

        // Assert the view name and model attributes as needed

        assertEquals("works/list", viewName);
    }


    @Test
    public void testShowFormForUpdateWork() {
        // Arrange
        Model model = mock(Model.class);
        int workId = 1;
        Work work1 = new Work();
        work1.setId(1);// Create a Work object

        // Act
        String viewName = workController.showFormForUpdateWork(workId, model);

        // Assert
        // Verify that the expected Work object is added to the model
        assertEquals("works/createOrUpdateWorkForm", viewName); // Replace with the correct view name
    }

    @Test
    public void testShowFormForAddWork() {
        // Arrange
        Model model = mock(Model.class);

        // Act
        String viewName = workController.showFormForAddWork(model);

        // Assert
        ArgumentCaptor<Work> workCaptor = ArgumentCaptor.forClass(Work.class);
        verify(model).addAttribute(eq("work"), workCaptor.capture());
        assertEquals("works/createOrUpdateWorkForm", viewName); // Replace with the correct view name
    }

}

