package model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public abstract class BaseModel {
    private final UUID id;
    private final LocalDateTime createdAt;
    private boolean isActive;
    private String name;

    {
        LocalDateTime time = LocalDateTime.now();
        id = UUID.randomUUID();
        createdAt = time;
        isActive = true;
    }

    public BaseModel(String name) {
        this.name = name;
    }

    public BaseModel() {
    }
}
