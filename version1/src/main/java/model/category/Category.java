package model.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.BaseModel;
import org.glassfish.grizzly.compression.lzma.impl.Base;

import java.util.UUID;
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class Category extends BaseModel {
    @JsonProperty("parentId")
    private UUID parentId;

    @JsonProperty("isLast")
    private boolean isLast;

    public Category(String name) {
        super(name);
    }
}

