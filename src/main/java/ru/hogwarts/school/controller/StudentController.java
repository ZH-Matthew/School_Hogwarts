package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.sqlInteface.AllColumnsStudents;
import ru.hogwarts.school.service.StudentService;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("student")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class); //only for error
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Optional<Student> student = studentService.findStudent(id);
        if (student.isEmpty()) {
            logger.error("There is not student with id = " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.get());
    }

    @PostMapping
    public Student postStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> putStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            logger.error("There is not student in data base = " + student);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity deleteAllStudent() {
        studentService.deleteAllStudent();
        return ResponseEntity.ok().build();
    }


    //методы не относящиеся к CRUD операциям
    @GetMapping("/getNumberOfStudents")
    public Long getNumberOfStudents(){
        return studentService.getNumberOfStudents();
    }

    @GetMapping("/getAverageAge")
    public Long getAverageAge(){
        return studentService.getAverageAge();
    }

    @GetMapping("/getLastFiveStudents")
    public List<AllColumnsStudents> getLastFiveStudents(){
        return studentService.getLastFiveStudents();
    }

    @GetMapping("getFacultyByStudentId/{id}")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable long id) {
        Optional<Student> student = studentService.findStudent(id);
        if (student.isEmpty()) {
            logger.error("There is not student with id = " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.get().getFaculty());
    }

    @GetMapping("allByFaculty/{id}")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable long id) {
        Collection<Student> allStudentByFaculty = studentService.findStudentsByFaculty(id);
        if (allStudentByFaculty.isEmpty()) {
            logger.error("There is not faculty with id = " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStudentByFaculty);
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> getStudentByAgeBetween(@RequestParam long min,
                                                                      @RequestParam long max) {
        Collection<Student> allStudentByAgeBetween = studentService.findByAgeBetween(min, max);
        if (allStudentByAgeBetween.isEmpty()) {
            logger.error("No students found by age between min =" + min + " max = "+ max);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStudentByAgeBetween);
    }
    @GetMapping("getStudentNameStWithA")
    public ResponseEntity<List<String>> getStudentNamesStartingWithLetterA(){
        return ResponseEntity.ok(studentService.getStudentNamesStartingWithLetterA());
    }

    @GetMapping("getAverageAgeOfAllStudents")
    public ResponseEntity<Double> getAverageAgeOfAllStudents(){
        return ResponseEntity.ok(studentService.getAverageAgeOfAllStudents());
    }



}
