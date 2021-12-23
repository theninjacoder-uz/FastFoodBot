package model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import model.BaseModel;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"id", "chatId", "name", "isActive", "phoneNumber", "cashback", "userRole", "location", "createdAt"})
public class User extends BaseModel {
    @JsonProperty("chatId")
    private String chatId;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("cashback")
    private BigDecimal cashback;

    @JsonProperty("userRole")
    private UserRole userRole;

    @JsonProperty("location")
    private UserLocation userLocation = new UserLocation();

    public User(String name, String chatId, String phoneNumber) {
        super(name);
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
    }
}
