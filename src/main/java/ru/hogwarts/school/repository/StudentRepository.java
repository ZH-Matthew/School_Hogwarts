package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.sqlInteface.AllColumnsStudents;

import java.util.Collection;
import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(long age);

    List<Student> findByAgeBetween(long min, long max);

    List<Student> findStudentsByFaculty_Id(long id);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Long getNumberOfStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Long getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<AllColumnsStudents> getLastFiveStudents();
}
