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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);
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
        LOG.info("Was invoked method uploadAvatar");
        Student student  = studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFondException(id));
        avatarService.create(student,multipartFile);
        return studentMapper.toDto(student);
    }


    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method create with parameter");

        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)));
    }
    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method update with id={}",id);
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
        LOG.info("Was invoked method delete with id={}",id);
        Student student = studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFondException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }
    public StudentDtoOut get(long id) {
        LOG.info("Was invoked method get with id={}",id);
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(()->new StudentNotFondException(id));
    }

    public List<StudentDtoOut> findAll(Integer age) {
        LOG.info("Was invoked method findAll");
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<StudentDtoOut> findByAgeBetween(int ageMin, int ageMax) {
        LOG.info("Was invoked method findByAgeBetween");
        return studentRepository.findAllByAgeBetween(ageMin, ageMax).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        LOG.info("Was invoked method findFaculty");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(()->new StudentNotFondException(id));
    }



    public int getCountOfStudents() {
        LOG.info("Was invoked method getCountOfStudents");
        return studentRepository.getCountOfStudents();
    }

    public double getAverageAge() {
        LOG.info("Was invoked method getAverageAge");
        return studentRepository.getAverageAge();
    }


    @Transactional(readOnly=true)
    public List<StudentDtoOut> lastStudents(int count) {
        LOG.info("Was invoked method lastStudents");
        return studentRepository.lastStudents(Pageable.ofSize(count)).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<String> getNamesStartWithA() {
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAvgAge() {
       return studentRepository.findAll().stream()
               .mapToDouble(Student::getAge)
               .average()
               .getAsDouble();
    }

    public void taskThread() {
        List<Student> students = studentRepository.findAll();

        printStudent(students.get(0));
        printStudent(students.get(1));

        new Thread(()->{
        printStudent(students.get(2));
        printStudent(students.get(4));
        }).start();
    }
    private void printStudent(Student student) {
        try {
            Thread.sleep(1000);
            LOG.info(student.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
        public void taskThreadSync() {
            List<Student> students = studentRepository.findAll();
            LOG.info(students.toString());

            printStudentSync(students.get(0));
            printStudentSync(students.get(1));

            new Thread(()->{
                printStudentSync(students.get(2));
                printStudentSync(students.get(3));
            }).start();

            new Thread(()->{
                printStudentSync(students.get(4));
                printStudentSync(students.get(5));
            }).start();
        }

        private synchronized void printStudentSync (Student student) {
       printStudent(student);

    //   @Transactional(readOnly=true)
  //  public List<StudentDtoOut> lastStudents(int count) {
  //      return studentRepository.lastStudents(count).stream()
  //              .map(studentMapper::toDto)
  //              .collect(Collectors.toList());
 //   }
}
}

