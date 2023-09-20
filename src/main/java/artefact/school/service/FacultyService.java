package artefact.school.service;


import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.dto.StudentDtoOut;
import artefact.school.exception.FacultyNotFoundException;
import artefact.school.entity.Faculty;
import artefact.school.maper.FacultyMapper;
import artefact.school.maper.StudentMapper;
import artefact.school.repository.FacultyRepository;
import artefact.school.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository,
                          FacultyMapper facultyMapper, StudentRepository studentRepository, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
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
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }


    public FacultyDtoOut delete(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public FacultyDtoOut get(long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FacultyDtoOut> findByNameAndColorIgnoreCase(String name, String color) {
        return facultyRepository.findAllByNameAndColorIgnoreCase(name, color).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findStudents(long id) {
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .get();
    }

    public Integer sum() {
        long start=System.currentTimeMillis();
        int res = Stream.iterate(1,a->a+1)
                .limit(1_000_000)
                .reduce(0,(a,b) -> a+b);
        long finish=System.currentTimeMillis();
        long dif = finish-start;
        System.out.println("simple:"+dif);
        return res;
    }
    public Integer sumImpr() {
        long start=System.currentTimeMillis();
        int res = Stream.iterate(1,a->a+1)
                .parallel()
                .limit(1_000_000)
                .reduce(0,(a,b) -> a+b);
        long finish=System.currentTimeMillis();
        long dif = finish-start;
        System.out.println("parallel:"+dif);
        return res;
    }
}