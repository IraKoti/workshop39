package nus.iss.edu.sg.workshop39.server.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.edu.sg.workshop39.server.models.MarvelCharacter;

@Service
public class CharacterService {
    @Autowired 
    private MarvelApiService marvelApiSvc;

    public Optional<List<MarvelCharacter>> listCharacters(String name, Integer limit, Integer offset)
    {
        return this.marvelApiSvc.getCharacters(name, limit, offset);
    }

    public MarvelCharacter getDetails(Integer charId) throws IOException
    {
        return this.marvelApiSvc.getDetails(charId);
    }
    
}
