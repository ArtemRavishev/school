package artefact.school.repository;

import artefact.school.dto.FacultyDtoOut;
import artefact.school.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {

    List<Faculty> findAllByColor(String color);

    List<Faculty> findAllByNameAndColorIgnoreCase(String name, String color);
}
