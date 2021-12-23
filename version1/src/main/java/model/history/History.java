package model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

@Data
@NoArgsConstructor
public class History extends BaseModel {
    @JsonProperty("text")
    private String text;
    @JsonProperty("userId")
    private String userId;

    public History(String userId, String name, String text) {
        super(name);
        this.userId = userId;
        this.text = text;
    }
}
