package artefact.school.controller;


import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.entity.Faculty;
import artefact.school.maper.FacultyMapper;
import artefact.school.maper.StudentMapper;
import artefact.school.repository.FacultyRepository;
import artefact.school.repository.StudentRepository;
import artefact.school.service.FacultyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

 @Autowired
 private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private FacultyMapper facultyMapper;

    @SpyBean
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createTest() throws Exception {
        FacultyDtoIn facultyDtoIn = generate();
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName(facultyDtoIn.getName());
        faculty.setColor(facultyDtoIn.getColor());
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/faculty")
                    .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultyDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(result -> {result.getResponse().getContentAsString();
            FacultyDtoOut facultyDtoOut=objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    FacultyDtoOut.class
            );
            assertThat(facultyDtoOut).isNotNull();
            assertThat(facultyDtoOut.getId()).isEqualTo(1L);
            assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
            assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
        });
        verify(facultyRepository, new Times(1)).save(any());
    }


    private FacultyDtoIn generate() {
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.harryPotter().house());
        facultyDtoIn.setColor(faker.color().name());

        return facultyDtoIn;
    }

}
