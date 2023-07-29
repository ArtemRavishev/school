package artefact.school.maper;



import artefact.school.dto.StudentDtoIn;
import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDtoOut toDto(Student student) {
        StudentDtoOut studentDtoOut= new StudentDtoOut();
        studentDtoOut.setId(student.getId());
        studentDtoOut.setName(student.getName());
        studentDtoOut.setAge(student.getAge());
        return studentDtoOut;
    }


    public Student toEntity(StudentDtoIn studentDtoIn) {
        Student student = new Student();
        student.setAge(studentDtoIn.getAge());
        student.setName(studentDtoIn.getName());
        return student;
    }
}
