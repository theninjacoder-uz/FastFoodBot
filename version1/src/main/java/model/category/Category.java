package model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;
import org.glassfish.grizzly.compression.lzma.impl.Base;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseModel {
    private UUID parentId;
    private boolean isLast;

    public Category(String name) {
        super(name);
    }
}
