package artefact.school.maper;

import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.entity.Faculty;
import org.springframework.stereotype.Component;


@Component
public class FacultyMapper {


    public FacultyDtoOut toDto(Faculty faculty) {
        FacultyDtoOut facultyDtoOut= new FacultyDtoOut();
        facultyDtoOut.setId(faculty.getId());
        facultyDtoOut.setName(faculty.getName());
        facultyDtoOut.setColor(facultyDtoOut.getColor());
        return facultyDtoOut;
    }


    public Faculty toEntity(FacultyDtoIn facultyDtoIn) {
        Faculty faculty = new Faculty();
        faculty.setColor(facultyDtoIn.getColor());
        faculty.setName(facultyDtoIn.getName());
        return faculty;
    }
}

