package service.user;

import com.twilio.rest.api.v2010.account.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import model.user.User;
import model.user.UserLocation;
import model.user.UserRole;
import service.base.BaseService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static utils.BotResponse.*;
import static utils.DatabasePath.USER_PATH;
import static utils.TelegramUtils.*;

public class UserService implements BaseService<User, String> {

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
            return obj.readValue(file, new TypeReference<List<User>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    public UserRole checkAndSave(String chatId, UserRole role) {
        User user = getByChatId(chatId);
        if (user == null) {
            save(new User(chatId, "", new BigDecimal(0), role, null));
            return null;
        }
//        save(user);
        if (user.getUserRole().equals(UserRole.ADMIN)) return UserRole.ADMIN;

        else if (!user.getPhoneNumber().isEmpty() && user.getPhoneNumber() != null)
            return user.getUserRole();

        return null;
    }

    public UserRole get(String chatId) {
        User user = getByChatId(chatId);
        assert user != null;
        return user.getUserRole();
    }

    public String savePhone(String phoneNumber, String chatId) {
        User user = getByChatId(chatId);
        user.setPhoneNumber(phoneNumber);
        save(user);
        return PHONE_SUCCESSFULLY_SAVED;

//        return INVALID_PHONE_NUMBER;
    }

    public String sendSmsCode(String phoneNumber) {
        Random r = new Random();
        String code = String.format("%04d", r.nextInt(9999));

        Twilio.init(ACCOUNT_SID, ACCOUNT_TOKEN);
        Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(ACCOUNT_PHONE), "Your verification code is " + code + "\n do not show anybody !!").create();

        return code;
    }

    public UserLocation checkAndGetLocation(String chatId, double latitude, double longitude) {
        User user = getByChatId(chatId);
        user.setUserLocation(new UserLocation(latitude, longitude));
        save(user);
        return user.getUserLocation();

    }

    @Override
    public List<User> getListByUUID(UUID id) {
        return null;
    }

    @Override
    public List<User> getListByChatId(String id) {
        return null;
    }
}
