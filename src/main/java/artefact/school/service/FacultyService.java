package artefact.school.service;


import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.exception.FacultyNotFoundException;
import artefact.school.entity.Faculty;
import artefact.school.maper.FacultyMapper;
import artefact.school.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;


    public FacultyService(FacultyRepository facultyRepository,
                          FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {

        return facultyMapper.toDto(
                facultyRepository.save(
                facultyMapper.toEntity(facultyDtoIn)));
    }
    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setColor(facultyDtoIn.getColor());
                    oldFaculty.setName(facultyDtoIn.getName());
                    return facultyMapper.toDto(facultyRepository.save(oldFaculty));
                })
                .orElseThrow(()->new FacultyNotFoundException(id));
    }


    public FacultyDtoOut delete(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(()->new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }
    public FacultyDtoOut get(long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(()->new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }
}