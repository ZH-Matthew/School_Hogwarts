package ru.hogwarts.school.service;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {

    private long count = 0;
    private  final Map<Long, Student> studentMap = new HashMap<>();

    public Student createStudent(Student student){
        student.setId(++count);
        studentMap.put(count,student);
        return student;
    }

    public Student findStudent(long id){
        return studentMap.get(id);
    }

    public Student editStudent(Student student){
        if(studentMap.containsKey(student.getId())) {
            studentMap.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id){
        return studentMap.remove(id);
    }

    public Collection<Student> getAllStudentByAge(long id) {
        List<Student> listStudentByAge = new ArrayList<>();
        for (Map.Entry<Long, Student> entry: studentMap.entrySet()) {
            if(entry.getValue().getAge()==id){
                listStudentByAge.add(entry.getValue());
            }
        }
        return listStudentByAge;
    }
}
