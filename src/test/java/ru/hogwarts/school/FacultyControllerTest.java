package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void getFacultyTest() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/9", String.class))
                .isNotNull();
    }

    @Test
    public void postFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(20L);
        faculty.setName("Бригадный подряд");
        faculty.setColor("Чёрный");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    public void putFacultyTest() throws Exception {
        Faculty faculty2 = new Faculty();
        faculty2.setId(20L);
        faculty2.setName("Бригадный подряд");
        faculty2.setColor("Чёрный");
        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(faculty2);
        Assertions
                .assertThat(this.restTemplate.exchange("http://localhost:" + port + "/faculty", HttpMethod.PUT, entity, Student.class))
                .isNotNull();
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        long id = 12;
        Assertions
                .assertThat(this.restTemplate.exchange("http://localhost:" + port + "/faculty/{id}", HttpMethod.DELETE, null, Student.class, id))
                .isNotNull();
    }
}