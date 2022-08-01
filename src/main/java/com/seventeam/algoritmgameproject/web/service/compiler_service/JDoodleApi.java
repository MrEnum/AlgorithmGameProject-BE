package com.seventeam.algoritmgameproject.web.service.compiler_service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JDoodleApi {

    public JSONObject compile(String codeStr,Language language) {
        String url = "https://api.jdoodle.com/v1/execute";
        String clientId = "586fb20a7ae6a3c3aae8f290321d36f1"; //Replace with your client ID
        String clientSecret = "e8d153f387750de0adf43b141c4fa0353d6c040a47b8d41a3f3fbc19d836949e"; //Replace with your client Secret
        String versionIndex = "0";
        JSONObject res = new JSONObject();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "Application");
            headers.add("Content-Type", "application/json");

            Map<String, String> map = new HashMap<>();
            map.put("clientId", clientId);
            map.put("clientSecret", clientSecret);
            map.put("script", codeStr);
            map.put("language", language.getValue());
            map.put("versionIndex", versionIndex);

            JSONObject jsonObject = new JSONObject(map);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);

            res = responseEntity.getBody();


        } catch (HttpClientErrorException e) {
            log.info(e.getMessage(),"JDoodle Error: ");

        }
        return res;
    }
}
