package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public abstract class BaseModel {
    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("createdAt")
    private final String createdAt;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("name")
    private String name;

    {
        id = UUID.randomUUID();
        createdAt = String.valueOf(LocalDateTime.now());
        isActive = true;
    }

    public BaseModel(String name) {
        this.name = name;
    }

    public BaseModel() {
    }
}
