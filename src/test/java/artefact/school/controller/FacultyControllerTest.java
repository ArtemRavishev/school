package artefact.school.controller;


import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.entity.Faculty;
import artefact.school.maper.AvatarMapper;
import artefact.school.maper.FacultyMapper;
import artefact.school.maper.StudentMapper;
import artefact.school.repository.FacultyRepository;
import artefact.school.repository.StudentRepository;
import artefact.school.service.FacultyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @MockBean
    private AvatarMapper avatarMapper;

    private final Faker faker = new Faker();

    @Test
    public void createTest() throws Exception {
        FacultyDtoIn facultyDtoIn = generateDto();

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
        .andExpect(result -> {
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

    @Test
    public void updateTest() throws Exception {
        FacultyDtoIn facultyDtoIn = generateDto();

        Faculty oldFaculty = generate(1);


        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(oldFaculty));

        oldFaculty.setColor(facultyDtoIn.getColor());
        oldFaculty.setName(facultyDtoIn.getName());
        when(facultyRepository.save(any())).thenReturn(oldFaculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(facultyDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut=objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                    assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
                });
        verify(facultyRepository, Mockito.times(1)).save(any());

        Mockito.reset(facultyRepository);



        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(facultyDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                  String respomseString = result.getResponse().getContentAsString();
                    assertThat(respomseString).isNotNull();
                    assertThat(respomseString).isEqualTo("Факультет с id=2 не найден");
                });
        verify(facultyRepository, never()).save(any());

    }

    @Test
    public void getTest() throws Exception {

        Faculty faculty = generate(1);


        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/1")

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut=objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                });


        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/2")

                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String respomseString = result.getResponse().getContentAsString();
                    assertThat(respomseString).isNotNull();
                    assertThat(respomseString).isEqualTo("Факультет с id=2 не найден");
                });
    }

    @Test
    public void deleteTest() throws Exception {

        Faculty faculty = generate(1);



        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/1")

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut=objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                });
        verify(facultyRepository,times(1)).delete(any());
        Mockito.reset(facultyRepository);


        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/2")

                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String respomseString = result.getResponse().getContentAsString();
                    assertThat(respomseString).isNotNull();
                    assertThat(respomseString).isEqualTo("Факультет с id=2 не найден");
                });
        verify(facultyRepository,never()).delete(any());

    }

    @Test
    public void findAllTest() throws Exception {
        List<Faculty> faculties= Stream.iterate(1,id->id+1)
                .map(this::generate)
                .limit(20)
                .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult = faculties.stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty")

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts=objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0,index->index+1)
                            .limit(facultyDtoOuts.size())
                                    .forEach(index->{
                                        FacultyDtoOut facultyDtoOut=facultyDtoOuts.get(index);
                                        FacultyDtoOut expected = expectedResult.get(index);
                                        assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                        assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                                        assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                                    });
                });

        String color =faculties.get(0).getColor();
        faculties=faculties.stream()
                        .filter(faculty -> faculty.getColor().equals(color))
                                .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult2 = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
        when(facultyRepository.findAllByColor(eq(color))).thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty?color={color}",color)

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts=objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0,index->index+1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index->{
                                FacultyDtoOut facultyDtoOut=facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult2.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                            });
                });




    }




    private FacultyDtoIn generateDto() {
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.harryPotter().house());
        facultyDtoIn.setColor(faker.color().name());

        return facultyDtoIn;
    }

    private Faculty generate(long id) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());

        return faculty;
    }

}
