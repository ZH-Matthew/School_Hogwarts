package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.repository.sqlInteface.AllColumnsStudents;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public Student findStudentByName(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }


    public List<Student> findByAgeBetween(long min, long max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> findStudentsByFaculty(long id) {
        return studentRepository.findStudentsByFaculty_Id(id);
    }

    public Long getNumberOfStudents(){
        return studentRepository.getNumberOfStudents();
    }

    public Long getAverageAge(){
        return studentRepository.getAverageAge();
    }

    public List<AllColumnsStudents> getLastFiveStudents(){
        return studentRepository.getLastFiveStudents();
    }

}
