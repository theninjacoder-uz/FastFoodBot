package service.base;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BaseService<T, R> {
    ObjectMapper obj = new ObjectMapper();
    R save(T item);
    R edit(T item);
    T getByUUId(UUID id);
    T getByChatId(String id);
    List<T> getList() throws IOException;
    List<T> getListByUUID(UUID id);
    List<T> getListByChatId(String id);

    default void write(File file, List<T> list) {
        try {
            obj.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
