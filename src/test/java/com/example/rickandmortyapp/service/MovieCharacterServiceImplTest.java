package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.dto.external.ApiResponseDto;
import com.example.rickandmortyapp.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import com.example.rickandmortyapp.repository.MovieCharacterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MovieCharacterServiceImplTest {
    @InjectMocks
    private MovieCharacterServiceImpl movieCharacterService;

    @Mock
    private MovieCharacterRepository repository;

    @Mock
    private MovieCharacterMapper movieCharacterMapper;

    @Test
    void shouldSaveDtoToDb_Ok() {
        ApiCharacterDto jerrySmith = new ApiCharacterDto();
        jerrySmith.setId(5L);
        jerrySmith.setName("Jerry Smith");
        jerrySmith.setGender("MALE");
        jerrySmith.setStatus("ALIVE");

        ApiCharacterDto agency = new ApiCharacterDto();
        agency.setId(9L);
        agency.setName("Agency");
        agency.setGender("MALE");
        agency.setStatus("DEAD");

        ApiResponseDto responseDto = new ApiResponseDto();
        responseDto.setInfo(null);
        responseDto.setResults(new ApiCharacterDto[] {jerrySmith, agency});

        Set<Long> externalIds = new HashSet<>();
        externalIds.add(jerrySmith.getId());
        externalIds.add(agency.getId());

        MovieCharacter jerrySmithFromDb = new MovieCharacter();
        jerrySmithFromDb.setExternalId(jerrySmith.getId());
        jerrySmithFromDb.setName(jerrySmith.getName());
        jerrySmithFromDb.setGender(Gender.valueOf(jerrySmith.getGender()));
        jerrySmithFromDb.setStatus(Status.valueOf(jerrySmith.getStatus()));

        MovieCharacter agencyFromDb = new MovieCharacter();
        agencyFromDb.setExternalId(agency.getId());
        agencyFromDb.setName(agency.getName());
        agencyFromDb.setGender(Gender.valueOf(agency.getGender()));
        agencyFromDb.setStatus(Status.valueOf(agency.getStatus()));

        List<MovieCharacter> charactersToSave = List.of(jerrySmithFromDb, agencyFromDb);
        List<MovieCharacter> expected = List.of(jerrySmithFromDb);

        Mockito.when(repository.findAllByExternalIdIn(externalIds)).thenReturn(new ArrayList<>());
        Mockito.when(repository.saveAll(charactersToSave)).thenReturn(expected);
        Mockito.when(movieCharacterMapper.parseMovieCharacterResponseDto(jerrySmith)).thenReturn(jerrySmithFromDb);
        Mockito.when(movieCharacterMapper.parseMovieCharacterResponseDto(agency)).thenReturn(agencyFromDb);

        List<MovieCharacter> actual = movieCharacterService.saveDtosToDb(responseDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }
}
