package artefact.school.repository;

import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Faculty;
import artefact.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {

    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int ageMin, int ageMax);

    List<Student> findAllByFaculty_Id(long facultyId);


}
