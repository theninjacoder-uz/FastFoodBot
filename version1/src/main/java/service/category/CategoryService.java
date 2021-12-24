package service.category;

import com.fasterxml.jackson.core.type.TypeReference;
import model.category.Category;
import model.product.Product;
import service.base.BaseService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static utils.BotResponse.SUCCESS;
import static utils.DatabasePath.CATEGORY_PATH;

public class CategoryService implements BaseService<Category, String> {

    File file = new File(CATEGORY_PATH);

    @Override
    public String save(Category item) {
        List<Category> categoryList = getList();
        if (categoryList == null) {
            categoryList = new ArrayList<>();
            categoryList.add(item);
        } else {
            int ind = 0;
            boolean notFound = true;
            for (Category category : categoryList) {
                if (category.getId().equals(item.getId())) {
                    categoryList.set(ind, item);
                    notFound = false;
                    break;
                }
                ind++;
            }
            if (notFound)
                categoryList.add(item);
        }
        write(file, categoryList);

        return SUCCESS;
    }

    @Override
    public String edit(Category item) {
        return null;
    }

    @Override
    public Category getByUUId(UUID id) {
        List<Category> categoryList = getList();
        if (categoryList == null)
            return null;
        for (Category category : categoryList) {
            if (category.getId().equals(id))
                return category;
        }
        return null;
    }

    @Override
    public Category getByChatId(String id) {
        return null;
    }

    @Override
    public List<Category> getList() {
        try {
            return obj.readValue(file, new TypeReference<List<Category>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Category> getListByUUID(UUID id) {
        List<Category> categoryList = getList();
        List<Category> ansList = new ArrayList<>();

        for(Category product: categoryList){
            if(product.getParentId().equals(id))
                ansList.add(product);
        }
        return ansList;
    }

    @Override
    public List<Category> getListByChatId(String id) {
        return null;
    }

    public UUID getUUIDFromName(String categoryName){
        List<Category> categoryList = getList();
        for (Category category: categoryList) {
            if(category.getName().equals(categoryName))
                return category.getId();
        }
        return null;
    }
}
