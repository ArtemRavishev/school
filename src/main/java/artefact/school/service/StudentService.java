package artefact.school.service;

import artefact.school.dto.FacultyDtoOut;
import artefact.school.dto.StudentDtoIn;
import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Avatar;
import artefact.school.entity.Student;
import artefact.school.exception.FacultyNotFoundException;
import artefact.school.exception.StudentNotFondException;
import artefact.school.maper.FacultyMapper;
import artefact.school.maper.StudentMapper;
import artefact.school.repository.FacultyRepository;
import artefact.school.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;



    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, FacultyRepository facultyRepository, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }

    public  StudentDtoOut uploadAvatar(long id,MultipartFile multipartFile) {
        Student student  = studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFondException(id));
        Avatar avatar =avatarService.create(student,multipartFile);
        StudentDtoOut studentDtoOut = studentMapper.toDto(student);
        studentDtoOut.setAvatarUrl("http://localhost:8080/avatars/"+avatar.getId()+"/from-db");
        return studentDtoOut;
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
                    Optional.ofNullable(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(facultyRepository.findById(facultyId)
                                    .orElseThrow(()-> new FacultyNotFoundException(facultyId))
                                    )
                            );
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


    public List<StudentDtoOut> findByAgeBetween(int ageMin, int ageMax) {
        return studentRepository.findAllByAgeBetween(ageMin, ageMax).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(()->new StudentNotFondException(id));
    }
}
