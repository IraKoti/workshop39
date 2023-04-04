package nus.iss.edu.sg.workshop39.server.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MarvelCharacter {
    private int id;
    private String name;
    private String description;
    private String photo;
    private List<Comment> comments;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getphoto() {
        return photo;
    }
    public void setphoto(String photo) {
        this.photo = photo;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static List<MarvelCharacter> create(String json) throws IOException
    {
        List<MarvelCharacter> characters = new LinkedList<>();
        try(InputStream is = new ByteArrayInputStream(json.getBytes()))
        {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonObject oo = o.getJsonObject("data");
            //data is json object for specific result under data
            if(oo.getJsonArray("results") != null)            
                characters = oo.getJsonArray("results").stream()
                    .map( v -> (JsonObject)v)
                    .map( v -> MarvelCharacter.createJson(v))
                    .toList();
            
        } 
        return characters;
    }

    public static MarvelCharacter createDetail(String json) throws IOException
    {
        MarvelCharacter character = new MarvelCharacter();
        try(InputStream is = new ByteArrayInputStream(json.getBytes()))
        {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonObject oo = o.getJsonObject("data");
            //data is json object for specific result under data
            character.setId(oo.getJsonNumber("id").intValue());
            character.setName(oo.getString("name"));
            character.setDescription(oo.getString("description"));
            character.setphoto(oo.getString("photo"));

            
        } 
        return character;
    }

    public static MarvelCharacter createJson(JsonObject o)
    {
        MarvelCharacter c = new MarvelCharacter();
        JsonObject a = o.getJsonObject("thumbnail");
        String path = a.getString("path");
        String ext = a.getString("extension");

        c.id = o.getJsonNumber("id").intValue();
        c.name = o.getString("name");
        c.description = o.getString("description");
        c.photo = path + "." + ext;
        return c;
    }

    public JsonObject toJson()
    {   
        return Json.createObjectBuilder()
            .add("id",getId())
            .add("name", getName())
            .add("description",getDescription())
            .add("photo",getphoto())
            .build();
    }

}
