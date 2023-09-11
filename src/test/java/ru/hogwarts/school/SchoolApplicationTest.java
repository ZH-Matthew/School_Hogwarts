package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetStudent() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
                .isNotNull();
    }

    @Test
    public void testGetIdFacultyByStudentName() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/getFaculty/1", String.class))
                .isNotNull();
    }

    @Test
    public void testGetStudentsByFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/allByFaculty/9", String.class))
                .isNotNull();
    }


    @Test
    public void testGetStudentByAgeBetween() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();
    }

    @Test
    public void testPostStudent() throws Exception {
        Student student = new Student();
        student.setId(20L);
        student.setName("Bob");
        student.setAge(20);

        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();
    }

    @Test
    public void testPutStudent() throws Exception {
        Student student = new Student();
        student.setId(20L);
        student.setName("Bob");
        student.setAge(30);
        HttpEntity<Student> entity = new HttpEntity<Student>(student);

        Assertions
                .assertThat(this.restTemplate.exchange("http://localhost:" + port + "/student", HttpMethod.PUT, entity, Student.class))
                .isNotNull();
    }

    @Test
    public void testDeleteStudent() throws Exception {
        long id = 12;
        Assertions
                .assertThat(this.restTemplate.exchange("http://localhost:" + port + "/student/{id}", HttpMethod.DELETE, null, Student.class, id))
                .isNotNull();
    }
}