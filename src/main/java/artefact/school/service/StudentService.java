package artefact.school.service;

import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.dto.StudentDtoIn;
import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Faculty;
import artefact.school.entity.Student;
import artefact.school.exception.FacultyNotFoundException;
import artefact.school.exception.StudentNotFondException;
import artefact.school.maper.StudentMapper;
import artefact.school.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;



    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }


    public StudentDtoOut create(StudentDtoIn studentDtoIn) {

        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)));
    }
    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setAge(studentDtoIn.getAge());
                    oldStudent.setName(studentDtoIn.getName());
                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(()->new StudentNotFondException(id));
    }


    public StudentDtoOut delete(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFondException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }
    public StudentDtoOut get(long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(()->new StudentNotFondException(id));
    }

    public List<StudentDtoOut> findAll(Integer age) {
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}
