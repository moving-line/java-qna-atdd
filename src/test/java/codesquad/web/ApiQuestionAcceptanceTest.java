package codesquad.web;


import codesquad.domain.Question;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.question;
import static codesquad.domain.QuestionTest.updatedQuestion;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);
    private static final String URL = "/api/questions";

    private String location;

    @Before
    public void setUp() throws Exception {
        location = createResource(URL, question);
    }

    @Test
    public void create() {
        Question dbQuestion = getResource(location, Question.class, defaultUser());

        softly.assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void create_no_login() {
        ResponseEntity<String> response = template().postForEntity(URL, question, String.class);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", response.getBody());
    }

    @Test
    public void show() {
        softly.assertThat(template().getForEntity(location, String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update_no_login() {
        ResponseEntity<String> responseEntity = updateResponse(template());

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", responseEntity.getBody());
    }

    @Test
    public void update_not_owner() {
        ResponseEntity<String> responseEntity = updateResponse(basicAuthTemplate(otherUser()));

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        log.debug("error message : {}", responseEntity.getBody());
    }

    @Test
    public void update() {
        ResponseEntity<String> responseEntity = updateResponse(basicAuthTemplate());

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(responseEntity.getBody().contains(updatedQuestion.getTitle())).isTrue();
        softly.assertThat(responseEntity.getBody().contains(updatedQuestion.getContents())).isTrue();
    }

    private ResponseEntity<String> updateResponse(TestRestTemplate testRestTemplate) {
        return testRestTemplate.exchange(location, HttpMethod.PUT, createHttpEntity(updatedQuestion), String.class);
    }
}
