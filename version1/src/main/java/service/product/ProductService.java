package service.product;

import com.fasterxml.jackson.core.type.TypeReference;
import model.product.Product;
import service.base.BaseService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static utils.BotResponse.SUCCESS;
import static utils.DatabasePath.PRODUCT_PATH;


public class ProductService implements BaseService<Product, String> {
    File file = new File(PRODUCT_PATH);

    @Override
    public String save(Product item) {
        List<Product> products = getList();
        if (products == null) {
            products = new ArrayList<>();
            products.add(item);
        } else {
            int ind = 0;
            boolean notFound = true;
            for (Product product : products) {
                if (product.getId().equals(item.getId())) {
                    products.set(ind, item);
                    notFound = false;
                    break;
                }
                ind++;
            }
            if (notFound)
                products.add(item);
        }
        write(file, products);

        return SUCCESS;
    }

    @Override
    public String edit(Product item) {
        return null;
    }

    @Override
    public Product getByUUId(UUID id) {
        List<Product> products = getList();
        if (products == null)
            return null;
        for (Product product : products) {
            if (product.getId().equals(id))
                return product;
        }
        return null;
    }

    @Override
    public Product getByChatId(String id) {
        return null;
    }

    @Override
    public List<Product> getList() {
        try {
            return obj.readValue(file, new TypeReference<List<Product>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> getListByUUID(UUID id) {
        List<Product> productList = getList();
        List<Product> ansList = new ArrayList<>();

        for(Product product: productList){
            if(product.getCategoryId().equals(id))
                ansList.add(product);
        }
        return ansList;
    }

    @Override
    public List<Product> getListByChatId(String id) {
        return null;
    }
}
