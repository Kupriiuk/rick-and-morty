package com.example.rickandmortyapp.mapper;

import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.Status;
import lombok.Data;

@Data
public class CharacterResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private Status status;
    private Gender gender;
}
