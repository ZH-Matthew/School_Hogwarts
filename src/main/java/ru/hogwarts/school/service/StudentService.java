package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.repository.sqlInteface.AllColumnsStudents;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create Student");
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(long id) {
        logger.info("Was invoked method for find Student");
        logger.debug("Requesting Student by id: {}", id);
        return studentRepository.findById(id);
    }


    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit Student");
        logger.debug("Change data in the student : {}", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete Student");
        studentRepository.deleteById(id);
    }

    public void deleteAllStudent() {
        logger.info("Was invoked method for delete all students");
        logger.warn("Attention! All students have been removed from the database!");
        studentRepository.deleteAll();
    }


    public List<Student> findByAgeBetween(long min, long max) {
        logger.info("Was invoked method for find students by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> findStudentsByFaculty(long id) {
        logger.info("Was invoked method for find students by faculty");
        return studentRepository.findStudentsByFaculty_Id(id);
    }

    public Long getNumberOfStudents() {
        logger.info("Was invoked method for requesting number of students");
        return studentRepository.getNumberOfStudents();
    }

    public Long getAverageAge() {
        logger.info("Was invoked method for requesting students average age");
        return studentRepository.getAverageAge();
    }

    public List<AllColumnsStudents> getLastFiveStudents() {
        logger.info("Was invoked method for requesting last five students in data base");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getStudentNamesStartingWithLetterA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .filter(s -> s.startsWith("А"))
                .collect(Collectors.toList());
    }

    public Double getAverageAgeOfAllStudents() {
        return studentRepository.findAll().stream()
                .map(Student::getAge)
                .mapToDouble(x -> x)
                .average()
                .orElse(0);
    }


    public void getNameStudentsInConsole() {
        List<String> names = studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        System.out.println(names.get(0));
        System.out.println(names.get(1));
        System.out.println(Thread.currentThread().getName());

        new Thread(()->{
            System.out.println(names.get(2));
            System.out.println(names.get(3));
            System.out.println(Thread.currentThread().getName());
        }).start();

        new Thread(()->{
            System.out.println(names.get(4));
            System.out.println(names.get(5));
            System.out.println(Thread.currentThread().getName());
        }).start();

        System.out.println(Thread.currentThread());
    }

    public void getNameStudentsInConsoleSynchro() {
        List<String> names = studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        printNameInConsole(names.get(0));
        printNameInConsole(names.get(1));

        new Thread(()->{
            printNameInConsole(names.get(2));
            printNameInConsole(names.get(3));
        }).start();

        new Thread(()->{
            printNameInConsole(names.get(4));
            printNameInConsole(names.get(5));
        }).start();

    }

    private synchronized void printNameInConsole(String name){
        System.out.println(name);
    }


}
