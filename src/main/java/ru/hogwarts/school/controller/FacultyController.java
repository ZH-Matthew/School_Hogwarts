package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    Logger logger = LoggerFactory.getLogger(FacultyController.class); //only for error
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Optional<Faculty> faculty = facultyService.findFaculty(id);
        if (faculty.isEmpty()) {
            logger.error("There is not faculty with id = " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty.get());
    }

    @PostMapping
    public Faculty postFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> putFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            logger.error("There is not faculty in data base = " + faculty);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Collection<Faculty>> getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                                                       @RequestParam(required = false) String color) {
        Collection<Faculty> facultyByNameOrColor = facultyService.findByNameOrColor(name, color);
        if (facultyByNameOrColor.isEmpty()) {
            logger.error("No faculty found by name =" + name + " or color = "+ color);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyByNameOrColor);
    }
}
