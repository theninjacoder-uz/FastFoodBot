package service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import model.user.User;
import model.user.UserRole;
import service.base.BaseService;
import service.bot.BotService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static utils.BotResponse.*;
import static utils.DatabasePath.USER_PATH;

public class UserService implements BaseService<User, String, List<User>> {

    File file = new File(USER_PATH);

    @Override
    public String save(User item) {
        List<User> users = getList();
        if (users == null) {
            users = new ArrayList<>();
            users.add(item);
        } else {
            int ind = 0;
            boolean notFound = true;
            for (User user : users) {
                if (user.getChatId().equals(item.getChatId())) {
                    users.set(ind, item);
                    notFound = false;
                    break;
                }
                ind++;
            }
            if (notFound)
                users.add(item);
        }
        write(file, users);

        return SUCCESS;
    }

    @Override
    public String edit(User item) {
        return null;
    }

    @Override
    public User getByUUId(UUID id) {
        return null;
    }

    @Override
    public User getByChatId(String id) {
        List<User> users = getList();
        if (users == null)
            return null;
        for (User user : users) {
            if (user.getChatId().equals(id))
                return user;
        }
        return null;
    }

    @Override
    public List<User> getList() {
        try {
            obj.readValue(file, new TypeReference<List<User>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void checkAndSave(String chatId, UserRole userRole){
        User user = getByChatId(chatId);
        if(user == null){
            save(new User(chatId, "", new BigDecimal(0), userRole, null));
        }
        assert user != null;
        user.setUserRole(userRole);
        save(user);
    }

    public UserRole get(String chatId, UserRole userRole){
        User user = getByChatId(chatId);
        assert user != null;
        return user.getUserRole();
    }

    @Override
    public List<User> getListByUUID(UUID id) {
        return null;
    }

    @Override
    public List<User> getListByChatId(String id) {
        return null;
    }

    @Override
    public void write(File file, List<User> list) {
        BaseService.super.write(file, list);
    }
}
