package artefact.school.controller;

import artefact.school.dto.FacultyDtoOut;
import artefact.school.dto.StudentDtoIn;
import artefact.school.dto.StudentDtoOut;
import artefact.school.entity.Student;
import artefact.school.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiDesktopPaneUI;
import java.util.List;
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    public StudentDtoOut create(@RequestBody StudentDtoIn StudentDtoIn){
        return studentService.create(StudentDtoIn);
    }
    @PutMapping("/{id}")
    public StudentDtoOut update(@PathVariable("id") long id, @RequestBody StudentDtoIn StudentDtoIn){
        return studentService.update(id,StudentDtoIn);
    }
    @GetMapping("/{id}")
    public StudentDtoOut get(@PathVariable("id") long id){
        return studentService.get(id);
    }
    @DeleteMapping("/{id}")
    public StudentDtoOut delete(@PathVariable("id") long id){
        return studentService.delete(id);
    }

    @GetMapping
    public List<StudentDtoOut> findAll(@RequestParam( required = false)Integer age){
        return studentService.findAll(age);
    }

    @GetMapping("/filter")
    public List<StudentDtoOut> findByAgeBetween(@RequestParam int ageMin, @RequestParam int ageMax){
        return studentService.findByAgeBetween(ageMin,ageMax);
    }
    @GetMapping("/{id}/faculty")
    public FacultyDtoOut findFaculty(@PathVariable("id") long id){
        return studentService.findFaculty(id);
    }

    @PatchMapping(value = "/{id}/avatar",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDtoOut uploadAvatar(@PathVariable long id ,
        @RequestPart("avatar") MultipartFile multipartFile){
        return studentService.uploadAvatar(id,multipartFile);

    }
}
