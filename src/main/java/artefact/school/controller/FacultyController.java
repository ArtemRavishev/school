package artefact.school.controller;

import artefact.school.dto.FacultyDtoIn;
import artefact.school.dto.FacultyDtoOut;
import artefact.school.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyServicee;

    public FacultyController(FacultyService facultyService) {
        this.facultyServicee = facultyService;
    }
   @PostMapping
    public FacultyDtoOut create(@RequestBody FacultyDtoIn facultyDtoIn ){
        return facultyServicee.create(facultyDtoIn);
   }
   @PutMapping("/{id}")
    public FacultyDtoOut update(@PathVariable("id") long id, @RequestBody FacultyDtoIn facultyDtoIn){
        return facultyServicee.update(id,facultyDtoIn);
    }
    @GetMapping("/{id}")
    public FacultyDtoOut get(@PathVariable("id") long id){
        return facultyServicee.get(id);
    }
    @DeleteMapping("/{id}")
    public FacultyDtoOut delete(@PathVariable("id") long id){
        return facultyServicee.delete(id);
    }
    @GetMapping
    public List<FacultyDtoOut> findAll(@RequestParam( required = false)String color){
        return facultyServicee.findAll(color);
    }

}
