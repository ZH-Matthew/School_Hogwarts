package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.assertj.core.api.Assertions.*; //статик импорт чтобы убрать класс Assertions при вызове Assertions.assertThat
import static ru.hogwarts.school.Constants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;   //Чтобы имитировать REST-запросы, создаем переменную класса TestRestTemplate. Из названия понятно, что это класс шаблонов для тестирования REST-запросов

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull(); //проверка на то что Spring создал контроллер
    }

    @Test
    void create() {

        ResponseEntity<Faculty> responsePost = restTemplate.postForEntity("/faculty", FACULTY, Faculty.class); //вызываем нужный нам метод, результат запроса сохраняем в переменную ответа (responsePost)

        assertThat(responsePost).isNotNull(); // далее прогоняем ответ по нужным нам утверждениям
        assertThat(responsePost.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBodyPost = responsePost.getBody(); //Так как ResponseEntity содержит много инфы которая нам может быть не особо нужна, мы возьмем только тело ответа и сохраним его в отдельную переменную для работы с ним, то есть достанем из Респонса сам факультет
        assertThat(respBodyPost).isNotNull();
        assertThat(respBodyPost.getId()).isNotNull(); //так как внутри уже сам факультет можем смело вызывать методы самого факультета getId/getName/getColor и проверять их
        assertThat(respBodyPost.getName()).isEqualTo("Бригадный подряд");
        assertThat(respBodyPost.getColor()).isEqualTo("Чёрный");
    }

    @Test
    void read() {
        ResponseEntity<Faculty> responsePost = restTemplate.postForEntity("/faculty", FACULTY, Faculty.class);

//        ResponseEntity<Faculty> exchange = restTemplate
//                .exchange("/faculty/" + respBody.getId(), HttpMethod.GET, HttpEntity.EMPTY, Faculty.class);
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + responsePost.getBody().getId(), Faculty.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("Бригадный подряд");
        assertThat(respBody.getColor()).isEqualTo("Чёрный");
    }

    @Test
    void update() {
        ResponseEntity<Faculty> responsePost = restTemplate.postForEntity("/faculty", FACULTY, Faculty.class);

        Faculty faculty = new Faculty();
        faculty.setId(responsePost.getBody().getId());
        faculty.setColor("Красный");
        faculty.setName(NAME);

        restTemplate.put("/faculty", faculty); //изменение не имеет ответа, поэтому его не сохраняем, просто отправляем, а потом получаем через Get объект и проверяем изменения

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + faculty.getId(), Faculty.class); //выдернули нужный объект для проверки изменений
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("Бригадный подряд");
        assertThat(respBody.getColor()).isEqualTo("Красный");
    }

    @Test
    void delete() {
        restTemplate.postForEntity("/faculty", FACULTY, Faculty.class);
        restTemplate.delete("/faculty/" + 1); //метод DELETE как и PUT - VOID, так что ничего не возвращают, создаем запрос, в потом проверку на то что действительно объект удален
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + 1, Faculty.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); //проверили что метод вернул BAD_REQUEST , это означает что запрашиваемого метода нет
    }

    @Test
    void getFacultyByNameOrColor() {
        restTemplate.postForEntity("/faculty", FACULTY, Faculty.class);

        ResponseEntity<List<Faculty>> facultyRs = restTemplate.exchange("/faculty?color=Чёрный", HttpMethod.GET, null, new ParameterizedTypeReference<List<Faculty>>() {
        });
        List<Faculty> faculties = facultyRs.getBody();
        assertThat(facultyRs).isNotNull();
        assertThat(facultyRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties.get(0).getColor()).isEqualTo("Чёрный");

        facultyRs = restTemplate.exchange("/faculty?name=Бригадный подряд", HttpMethod.GET, null, new ParameterizedTypeReference<List<Faculty>>() {
        });
        assertThat(facultyRs).isNotNull();
        assertThat(facultyRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties.get(0).getName()).isEqualTo("Бригадный подряд");

        facultyRs = restTemplate.exchange("/faculty?name=Авадакедавра", HttpMethod.GET, null, new ParameterizedTypeReference<List<Faculty>>() {
        });
        assertThat(facultyRs.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}