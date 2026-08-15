package org.just_mc_community.controller;

import org.just_mc_community.model.PlayerApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("player")
public class PlayerController {
    @Value("${player.api.url}")
    private String EXTERNAL_API_URL;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("list")
    public ResponseEntity<?> list(){
        PlayerApiResponse response  = restTemplate.getForObject(EXTERNAL_API_URL, PlayerApiResponse.class);
        if (response != null && response.getCode() == 200) {
            return ResponseEntity.ok(response.getPlayers());
        }
        return ResponseEntity.ok(null);
    }
}
