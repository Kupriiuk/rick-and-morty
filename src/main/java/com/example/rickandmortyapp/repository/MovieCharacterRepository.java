package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.MovieCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCharacterRepository extends JpaRepository<MovieCharacter,Long> {
    List<MovieCharacter> findAllByExternalIdIn(Set<Long> externalId);

    List<MovieCharacter> findAllByNameContains(String namePart);
}
