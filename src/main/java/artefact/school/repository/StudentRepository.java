package artefact.school.repository;

import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {

    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int ageMin, int ageMax);

    List<Student> findAllByFaculty_Id(long facultyId);


    @Query("SELECT count (s) FROM Student s")
    int getCountOfStudents();


    @Query("SELECT avg (s.age) FROM Student s")
    double getAverageAge();


    //@Query("SELECT new artefact.school.dto.StudentDtoOut(s.id,s.name,s.age,f.id,f.name,f.color,a.id) FROM Student s LEFT join Faculty f ON s.faculty= f left JOIN Avatar a ON a.student= s order by s.id desc ")
    //List<StudentDtoOut> lastStudents(Pageable pageable);
//}
//
   @Query("SELECT s from  Student  s order by s.id desc ")
   List<Student> lastStudents(Pageable pageable);
}

    // @Query(value = "SELECT s.* from  students s order by s.id desc limit :count", nativeQuery = true)
   // List<Student> lastStudents(@Param("count") int count);
//}
