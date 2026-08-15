package org.just_mc_community.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerApiResponse {
    private int code;
    private List<Player> players;
    private String error;

}