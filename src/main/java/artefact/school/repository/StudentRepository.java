package artefact.school.repository;

import artefact.school.entity.Faculty;
import artefact.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {

    List<Student> findAllByAge(int age);
}
