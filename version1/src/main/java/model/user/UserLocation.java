package model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class UserLocation {
    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;
}
