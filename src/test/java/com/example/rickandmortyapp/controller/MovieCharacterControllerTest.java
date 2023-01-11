package com.example.rickandmortyapp.controller;


import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import com.example.rickandmortyapp.service.MovieCharacterService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovieCharacterControllerTest {
    @MockBean
    private MovieCharacterService movieCharacterService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldReturnRandomCharacter() {
        MovieCharacter bethSmith = new MovieCharacter();
        bethSmith.setId(4L);
        bethSmith.setExternalId(24L);
        bethSmith.setName("Beth Smith");
        bethSmith.setGender(Gender.valueOf("FEMALE"));
        bethSmith.setStatus(Status.valueOf("ALIVE"));

        Mockito.when(movieCharacterService.getRandomCharacter()).thenReturn(bethSmith);

        RestAssuredMockMvc.when()
                .get("/characters/random")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(4))
                .body("name", Matchers.equalTo("Beth Smith"))
                .body("status", Matchers.equalTo("ALIVE"))
                .body("gender", Matchers.equalTo("FEMALE"));
    }

    @Test
    void shouldReturnCharactersContainsString() {
        MovieCharacter summerSmith = new MovieCharacter();
        summerSmith.setId(3L);
        summerSmith.setExternalId(22L);
        summerSmith.setName("Summer Smith");
        summerSmith.setGender(Gender.valueOf("FEMALE"));
        summerSmith.setStatus(Status.valueOf("ALIVE"));

        MovieCharacter commanderRick = new MovieCharacter();
        commanderRick.setId(2L);
        commanderRick.setExternalId(23L);
        commanderRick.setName("Commander Rick");
        commanderRick.setGender(Gender.valueOf("MALE"));
        commanderRick.setStatus(Status.valueOf("DEAD"));

        Mockito.when(movieCharacterService.findAlByNameContains(any()))
                .thenReturn((List.of(summerSmith, commanderRick)));
        String namePart = "mm";

        RestAssuredMockMvc
                .given()
                .queryParam("name", namePart)
                .when()
                .get("/characters/by-name")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(3))
                .body("[0].name", Matchers.equalTo("Summer Smith"))
                .body("[0].status", Matchers.equalTo("ALIVE"))
                .body("[0].gender", Matchers.equalTo("FEMALE"))
                .body("[1].id", Matchers.equalTo(2))
                .body("[1].name", Matchers.equalTo("Commander Rick"))
                .body("[1].status", Matchers.equalTo("DEAD"))
                .body("[1].gender", Matchers.equalTo("MALE"));
    }
}
