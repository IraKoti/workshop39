package nus.iss.edu.sg.workshop39.server.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import nus.iss.edu.sg.workshop39.server.models.MarvelCharacter;


@Service
public class MarvelApiService 
{   
    // # sample: http://gateway.marvel.com/v1/public/comics?ts=1&apikey=9ae0cfb5d6b1800cb8747e5383026971&hash=ffd275c5130566a2916217b101f26150
    // # sample: https://gateway.marvel.com:443/v1/public/characters?nameStartsWith=doctor&limit=5&offset=0&apikey=9ae0cfb5d6b1800cb8747e5383026971
    // # ts = timestamp
    // # apikey = public key
    // # namesStartsWith = look for character
    // # limit = 20 default
    // # offset = 0 default
    // # hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
    // # hash = timestamp + efb868fd88fb828f7149f1427e2e0ba4f275a42b + 9ae0cfb5d6b1800cb8747e5383026971

    @Value("${marvel.api.url}")
    private String marvelApiUrl;

    @Value("${marvel.api.public.key}")
    private String marvelApiPublicKey;

    @Value("${marvel.api.private.key}")
    private String marvelApiPrivateKey;
    
    //private function because it being call on the same class/java file
    private String[] getMarvelApiHash()
    {
        String[] result = new String[2];
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        long tsVal = timeStamp.getTime();
        String hashVal = tsVal + marvelApiPrivateKey + marvelApiPublicKey;
        result[0] = tsVal + "";
        result[1] = DigestUtils.md5Hex(hashVal);
        return result;
    }
    
    public Optional<List<MarvelCharacter>> getCharacters(String name, Integer limit, Integer offset) 
    {
        ResponseEntity<String> response = null;
        List<MarvelCharacter> lmc = null;
        String[] hkey = getMarvelApiHash();
        System.out.println(hkey[0]);
        System.out.println(hkey[1]);
        String marvelCharApiUrl = UriComponentsBuilder
            .fromUriString(marvelApiUrl+"characters")
            .queryParam("ts", hkey[0])
            .queryParam("apikey", marvelApiPublicKey)
            .queryParam("hash", hkey[1])
            .queryParam("nameStartsWith", name)
            .queryParam("limit", limit)
            .queryParam("offset", offset)
            .toUriString();
        System.out.println(marvelCharApiUrl);
        RestTemplate rTemplate = new RestTemplate();
        response = rTemplate.getForEntity(marvelCharApiUrl, String.class);
        try {
            lmc = MarvelCharacter.create(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(lmc);
        if(lmc != null)
            return Optional.of(lmc);
        return Optional.empty();
    }

    public MarvelCharacter getDetails(Integer id) throws IOException
    {
        ResponseEntity<String> response = null;
        MarvelCharacter mc = null;
        String[] hkey = getMarvelApiHash();
        System.out.println(hkey[0]);
        System.out.println(hkey[1]);
        String marvelCharApiUrl = UriComponentsBuilder
        .fromUriString(marvelApiUrl+"characters/"+id)
        .queryParam("ts", hkey[0])
        .queryParam("apikey", marvelApiPublicKey)
        .queryParam("hash", hkey[1])
        .toUriString();
        RestTemplate rTemplate = new RestTemplate();
        response = rTemplate.getForEntity(marvelCharApiUrl, String.class);
        List<MarvelCharacter>  mcArray = MarvelCharacter.create(response.getBody());
        mc = mcArray.get(0);
        System.out.print(mc.toString());
        return mc;

    }


}
