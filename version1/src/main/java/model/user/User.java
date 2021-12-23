package model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class User extends BaseModel {
    @JsonProperty("chatId")
    private String chatId;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("cashback")
    private BigDecimal cashback;

    @JsonProperty("location")
    private UserLocation userLocation;

    public User(String name, String chatId, String phoneNumber) {
        super(name);
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
    }
}
