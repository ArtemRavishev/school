package artefact.school.controller;


import artefact.school.dto.StudentDtoIn;
import artefact.school.dto.StudentDtoOut;
import artefact.school.repository.StudentRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final Faker faker = new Faker();

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
    }


    @Test
    public StudentDtoOut createStudent() {
        StudentDtoIn studentDtoIn = generate();

        ResponseEntity<StudentDtoOut> responseEntity =
                testRestTemplate.postForEntity(
                "http://localhost:"+port+"/students",
                studentDtoIn,
                StudentDtoOut.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut).isNotNull();
        assertThat(studentDtoOut.getId()).isNotEqualTo(0L);
        assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());
        assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
        return studentDtoOut;
    }

    @Test
    public void updateStudent() {
        StudentDtoOut created = createStudent();
        StudentDtoIn studentDtoIn=new StudentDtoIn();
        studentDtoIn.setName(faker.name().fullName());
        studentDtoIn.setAge(created.getAge());



        ResponseEntity<StudentDtoOut> responseEntity =
                testRestTemplate.exchange(
                        "http://localhost:"+port+"/students/" +created.getId(),
                        HttpMethod.PUT,
                        new HttpEntity<>(studentDtoIn),
                        StudentDtoOut.class
                );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut).isNotNull();
        assertThat(studentDtoOut.getId()).isEqualTo(created.getId());
        assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());
        assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());


        long incorrectId = created.getId() +1;

         ResponseEntity<String>stringResponseEntity = testRestTemplate.exchange(
                        "http://localhost:"+port+"/students/" +incorrectId ,
                        HttpMethod.PUT,
                        new HttpEntity<>(studentDtoIn),
                        String.class
                );
        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(stringResponseEntity.getBody()).isEqualTo("Студент с id=" + incorrectId + " не найден");
    }



    public StudentDtoIn generate(){
        StudentDtoIn studentDtoIn=new StudentDtoIn();
        studentDtoIn.setAge(faker.random().nextInt(7,18));
        studentDtoIn.setName(faker.name().fullName());
        return studentDtoIn;
    }

}
