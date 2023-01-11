package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.model.MovieCharacter;
import java.util.List;

public interface MovieCharacterService {
    void syncExternalCharacters();

    MovieCharacter getRandomCharacter();

    List<MovieCharacter> findAlByNameContains(String namePart);
}
