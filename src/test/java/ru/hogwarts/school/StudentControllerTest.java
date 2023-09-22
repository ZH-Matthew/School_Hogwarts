package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static ru.hogwarts.school.Constants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    void create() {
        ResponseEntity<Student> responsePost = restTemplate.postForEntity("/student", STUDENT, Student.class); //вызываем нужный нам метод, результат запроса сохраняем в переменную ответа (responsePost)

        assertThat(responsePost).isNotNull(); // далее прогоняем ответ по нужным нам утверждениям
        assertThat(responsePost.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student respBodyPost = responsePost.getBody(); //Так как ResponseEntity содержит много инфы которая нам может быть не особо нужна, мы возьмем только тело ответа и сохраним его в отдельную переменную для работы с ним, то есть достанем из Респонса сам факультет
        assertThat(respBodyPost).isNotNull();
        assertThat(respBodyPost.getId()).isNotNull(); //так как внутри уже сам факультет можем смело вызывать методы самого факультета getId/getName/getColor и проверять их
        assertThat(respBodyPost.getName()).isEqualTo("Anna");
        assertThat(respBodyPost.getAge()).isEqualTo(20L);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void read() {
        ResponseEntity<Student> responsePost = restTemplate.postForEntity("/student", STUDENT, Student.class);

//        ResponseEntity<Student> exchange = restTemplate
//                .exchange("/student/" + respBody.getId(), HttpMethod.GET, HttpEntity.EMPTY, Student.class);
//        один из вариантов написания
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + responsePost.getBody().getId(), Student.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("Anna");
        assertThat(respBody.getAge()).isEqualTo(20L);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void update() {
        ResponseEntity<Student> responsePost = restTemplate.postForEntity("/student", STUDENT, Student.class);

        Student student = new Student();
        student.setId(responsePost.getBody().getId());
        student.setAge(30L);
        student.setName(NAME_S);

        restTemplate.put("/student", student); //изменение не имеет ответа, поэтому его не сохраняем, просто отправляем, а потом получаем через Get объект и проверяем изменения

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + student.getId(), Student.class); //выдернули нужный объект для проверки изменений
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("Anna");
        assertThat(respBody.getAge()).isEqualTo(30L);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void delete() {
        ResponseEntity<Student> response1 = restTemplate.postForEntity("/student", STUDENT, Student.class);
        restTemplate.delete("/student/" + response1.getBody().getId()); //метод DELETE как и PUT - VOID, так что ничего не возвращают, создаем запрос, в потом проверку на то что действительно объект удален
        ResponseEntity<Student> response2 = restTemplate.getForEntity("/student/" + response1.getBody().getId(), Student.class);
        assertThat(response2).isNotNull();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); //проверили что метод вернул BAD_REQUEST , это означает что запрашиваемого метода нет
    }

    //далее методы не относящиеся к CRUD
    @Test
    void getNumberOfStudents() {
        Student student = new Student();
        student.setAge(30L);
        student.setName("Sam");

        Student student2 = new Student();
        student2.setAge(20L);
        student2.setName("Max");

        Student student3 = new Student();
        student3.setAge(25L);
        student3.setName("Mat");

        restTemplate.postForEntity("/student", student, Student.class);
        restTemplate.postForEntity("/student", student2, Student.class);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", student3, Student.class);
        assertThat(response).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Mat");


        ResponseEntity<Long> responseGet = restTemplate.getForEntity("/student/getNumberOfStudents", Long.class);
        assertThat(responseGet).isNotNull();
        assertThat(responseGet.getBody()).isEqualTo(3L);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void getAverageAge() {
        Student student = new Student();
        student.setAge(30L);
        student.setName("Sam");

        Student student2 = new Student();
        student2.setAge(20L);
        student2.setName("Max");

        Student student3 = new Student();
        student3.setAge(40L);
        student3.setName("Mat");
        restTemplate.postForEntity("/student", student, Student.class);
        restTemplate.postForEntity("/student", student2, Student.class);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", student3, Student.class);
        assertThat(response).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Mat");

        ResponseEntity<Long> responseGet = restTemplate.getForEntity("/student/getAverageAge", Long.class);
        assertThat(responseGet).isNotNull();
        assertThat(responseGet.getBody()).isEqualTo(30L);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void getLastFiveStudents() {
        Student student = new Student();
        student.setAge(30L);
        student.setName("Sam");

        Student student2 = new Student();
        student2.setAge(20L);
        student2.setName("Max");

        Student student3 = new Student();
        student3.setAge(40L);
        student3.setName("Mat");

        Student student4 = new Student();
        student4.setAge(33L);
        student4.setName("Sara");

        Student student5 = new Student();
        student5.setAge(21L);
        student5.setName("Anna");

        Student student6 = new Student();
        student6.setAge(25L);
        student6.setName("Mary");

        restTemplate.postForEntity("/student", student, Student.class);

        ResponseEntity<Student> responseLast = restTemplate.postForEntity("/student", student2, Student.class);
        Long respIdLast = responseLast.getBody().getId();

        restTemplate.postForEntity("/student", student3, Student.class);
        restTemplate.postForEntity("/student", student4, Student.class);
        restTemplate.postForEntity("/student", student5, Student.class);

        ResponseEntity<Student> responseFirst = restTemplate.postForEntity("/student", student6, Student.class);
        Long respIdFirst = responseFirst.getBody().getId();

        ResponseEntity<List<Student>> responseGet = restTemplate.exchange("/student/getLastFiveStudents", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        List<Student> listStudents = responseGet.getBody();
        assertThat(listStudents).isNotNull();
        assertThat(listStudents.size()).isEqualTo(5);
        assertThat(listStudents.get(0).getId()).isEqualTo(respIdFirst);
        assertThat(listStudents.get(4).getId()).isEqualTo(respIdLast);

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void getFacultyByStudentId() {
        Faculty faculty = new Faculty();
        faculty.setColor("Чёрный");
        faculty.setName("BackInBlack");
        ResponseEntity<Faculty> responseFaculty = restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        Student student = new Student();
        student.setAge(30L);
        student.setName("Sam");
        student.setFaculty(responseFaculty.getBody()); //кладем не просто факультет , а уже созданный факультет, у него уже появятся такие поля как ID

        ResponseEntity<Student> responseStudent = restTemplate.postForEntity("/student", student, Student.class);
        assertThat(responseStudent).isNotNull();
        assertThat(responseStudent.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> responseGet = restTemplate.getForEntity("/student/getFacultyByStudentId/" + responseStudent.getBody().getId(), Faculty.class);
        assertThat(responseGet).isNotNull();
        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseGet.getBody().getName()).isEqualTo("BackInBlack");
        assertThat(responseGet.getBody().getColor()).isEqualTo("Чёрный");

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void getStudentsByFaculty() {

        Faculty faculty = new Faculty();
        faculty.setColor("Чёрный");
        faculty.setName("BackInBlack");
        ResponseEntity<Faculty> responseFaculty = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = responseFaculty.getBody().getId();

        Student student = new Student();
        student.setAge(20L);
        student.setName("Sam");
        student.setFaculty(responseFaculty.getBody());


        Student student2 = new Student();
        student2.setAge(30L);
        student2.setName("Mat");
        student2.setFaculty(responseFaculty.getBody());

        Student student3 = new Student();
        student3.setAge(40L);
        student3.setName("Max");
        student3.setFaculty(responseFaculty.getBody());

        ResponseEntity<Student> responseStudent = restTemplate.postForEntity("/student", student, Student.class);
        restTemplate.postForEntity("/student", student2, Student.class);
        ResponseEntity<Student> responseStudent3 = restTemplate.postForEntity("/student", student3, Student.class);
        ResponseEntity<List<Student>> responseGet = restTemplate.exchange("/student/allByFaculty/" + facultyId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        List<Student> students = responseGet.getBody();
        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(3);
        assertThat(students.get(0)).isEqualTo(responseStudent.getBody());
        assertThat(students.get(2)).isEqualTo(responseStudent3.getBody());

        restTemplate.delete("/student/deleteAll");
    }

    @Test
    void getStudentByAgeBetween() {
        Student student0 = new Student();
        student0.setAge(19L);
        student0.setName("Anna");

        Student student1 = new Student();
        student1.setAge(21L);
        student1.setName("Sam");

        Student student2 = new Student();
        student2.setAge(29L);
        student2.setName("Mat");

        Student student3 = new Student();
        student3.setAge(40L);
        student3.setName("Max");

        restTemplate.postForEntity("/student", student0, Student.class);
        ResponseEntity<Student> responseStudent1 = restTemplate.postForEntity("/student", student1, Student.class);
        ResponseEntity<Student> responseStudent2 = restTemplate.postForEntity("/student", student2, Student.class);
        restTemplate.postForEntity("/student", student3, Student.class);

        ResponseEntity<List<Student>> responseGet = restTemplate.exchange("/student?min=20&max=30", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Student> students = responseGet.getBody();
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(2);
        assertThat(students.get(0)).isEqualTo(responseStudent1.getBody());
        assertThat(students.get(1)).isEqualTo(responseStudent2.getBody());

        restTemplate.delete("/student/deleteAll");
    }
}