package nus.iss.edu.sg.workshop39.server.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.edu.sg.workshop39.server.models.MarvelCharacter;
import nus.iss.edu.sg.workshop39.server.services.CharacterService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/characters", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarvelRestController {
    @Autowired
    private CharacterService characterSvc;

    @GetMapping
    public ResponseEntity<String> getCharactersList(
        @RequestParam(required = true) String charName,
        @RequestParam(required = true) Integer limit,
        @RequestParam(required = true) Integer offset)
    {
        JsonArray result = null;
        Optional<List<MarvelCharacter>>  or = this.characterSvc.listCharacters(charName, limit, offset);
        List<MarvelCharacter> results = or.get();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(MarvelCharacter mc : results)
        {
            arrayBuilder.add(mc.toJson());
        }
        result = arrayBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

    @GetMapping(path="/{charId}")
    public ResponseEntity<String> getDetails (@PathVariable(required=true) Integer charId) throws IOException
    {
        MarvelCharacter character = this.characterSvc.getDetails(charId);
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("details", character.toJson());
        JsonObject result = objBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());

    }
    
}
