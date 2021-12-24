package service.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import model.basket.Basket;
import model.category.Category;
import service.base.BaseService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static utils.BotResponse.SUCCESS;
import static utils.DatabasePath.BASKET_PATH;

public class BasketService implements BaseService<Basket, String> {
    File file = new File(BASKET_PATH);
    @Override
    public String save(Basket item) {
        List<Basket> basketList = getList();
        if (basketList == null) {
            basketList = new ArrayList<>();
            basketList.add(item);
        } else {
            int ind = 0;
            boolean notFound = true;
            for (Basket basket : basketList) {
                if (basket.getProductName().equals(item.getProductName())) {
                    basketList.set(ind, item);
                    notFound = false;
                    break;
                }
                ind++;
            }
            if (notFound)
                basketList.add(item);
        }
        write(file, basketList);

        return SUCCESS;
    }

    @Override
    public String edit(Basket item) {
        return null;
    }

    @Override
    public Basket getByUUId(UUID id) {
        return null;
    }

    @Override
    public Basket getByChatId(String id) {
        return null;
    }

    @Override
    public List<Basket> getList() {
        try {
            return obj.readValue(file, new TypeReference<List<Basket>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Basket> getListByUUID(UUID id) {
        return null;
    }

    @Override
    public List<Basket> getListByChatId(String id) {

        try {
            List<Basket> basketList = new ArrayList<>();

            List<Basket> list = getList();
            if(list != null)
                for (Basket basket: list) {
                if(basket != null && basket.getUserId().equals(id))
                    basketList.add(basket);

                }

            return basketList;
        }
        catch (IndexOutOfBoundsException | NullPointerException exception){
            exception.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Basket getByUserAndProductId(String productName, String chatId) {
        List<Basket> basketList = getListByChatId(chatId);
        try{
            for (Basket basket: basketList) {
                if(basket.getProductName().equals(productName))
                    return basket;
            };
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }
}
