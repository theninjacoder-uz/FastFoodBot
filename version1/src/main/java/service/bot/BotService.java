package service.bot;

import model.basket.Basket;
import model.category.Category;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import service.basket.BasketService;
import service.category.CategoryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static utils.BotResponse.*;
import static utils.Const.*;

public class BotService {
    static CategoryService  categoryService = new CategoryService();
    static BasketService basketService = new BasketService();

    public static SendMessage botMenu(Message message){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(MAKE_ORDER);
        keyboardRows.add(keyboardRow);

        KeyboardRow keyboardRow1 = new KeyboardRow();

        keyboardRow1.add(ORDER_HISTORY);
        keyboardRow1.add(CASHBACK);
        keyboardRows.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(SETTINGS);
        keyboardRow2.add(GET_CONTACT);
        keyboardRows.add(keyboardRow2);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), HOME_MENU);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public static SendMessage getRegister(Message message){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(SEND_PHONE_NUMBER);
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        keyboardRows.add(keyboardRow);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(),  PHONE_NUMBER_REQUEST);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage makeOrderMenu(Message message){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(MAKE_DELIVERY);
        keyboardButton.setRequestLocation(true);
        keyboardRow.add(keyboardButton);

        keyboardButton = new KeyboardButton(TAKE_YOURSELF);
        keyboardRow.add(keyboardButton);

        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardButton = new KeyboardButton(BACK);
        keyboardRow.add(keyboardButton);

        keyboardRows.add(keyboardRow);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(),  ORDER_TYPE);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage categoryMenu(Message message, String categoryName){
        UUID id = categoryService.getUUIDFromName(categoryName);

        List<Category> categoryList = categoryService.getListByUUID(id);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), HOME_MENU);

        ReplyKeyboardMarkup repylMurkup = getReplyMarkup(categoryList);
        sendMessage.setReplyMarkup(repylMurkup);

        return sendMessage;
    }

    public static ReplyKeyboardMarkup getReplyMarkup(List<Category> categoryList){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        for(int i = 0; i < categoryList.size(); ++i){
            keyboardRow1.add(categoryList.get(i).getName());
            if((i & 1) == 1){
                keyboardRows.add(keyboardRow1);
                keyboardRow1 = new KeyboardRow();
            }
        }
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(BASKET);
        keyboardRow.add(BACK);
        keyboardRows.add(keyboardRow);

        return replyKeyboardMarkup;
    }

    public static SendMessage showUserBasket(Update update){

        SendMessage sendMessage = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button;

        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage() ;
        String chatId = message.getChatId().toString();
        List<Basket> cartList =  basketService.getListByChatId(chatId);
        StringBuilder text = new StringBuilder();
        BigDecimal sum ;
        BigDecimal total = new BigDecimal(0);
        int ind = 1;


        for (Basket basket: cartList) {
            sum = basket.getPrice().multiply(new BigDecimal(basket.getQuantity()));
            total = total.add(sum);
            text.append(ind).append(". ").append(basket.getProductName()).append(". ").append(basket.getQuantity()).append(" ✖ ").append(basket.getPrice()).append(" = ").append(sum).append("\n\n");
            button = new InlineKeyboardButton();
            buttons = new ArrayList<>();
            button.setText(CANCEL + (ind++) + ". " + basket.getProductName());
            button.setCallbackData(CANCEL + PRODUCT_IN_BASKET + basket.getProductName().toString());
            buttons.add(button);
            list.add(buttons);
            button = new InlineKeyboardButton();
            buttons = new ArrayList<>();
            button.setText(DECREMENT);
            button.setCallbackData(DECREMENT + PRODUCT_IN_BASKET + basket.getProductName().toString());
            buttons.add(button);

            button = new InlineKeyboardButton();
            button.setText(String.valueOf(basket.getQuantity()));
            button.setCallbackData("num");
            buttons.add(button);

            button = new InlineKeyboardButton();
            button.setText(INCREMENT);
            button.setCallbackData(INCREMENT + PRODUCT_IN_BASKET + basket.getProductName() + basket.getQuantity());
            buttons.add(button);
            list.add(buttons);
            buttons = new ArrayList<>();
        }

        button = new InlineKeyboardButton();
        button.setText("buy");
        button.setCallbackData(BUY_FROM_CART+"#"+total);
        button.setPay(true);
        buttons.add(button);
        list.add(buttons);
        if (0 == total.compareTo(BigDecimal.valueOf(0))) {
            text.append("Your basket is empty\uD83D\uDE43");
        } else {
            text.append("Total price: ").append(total);
        }
        inlineKeyboardMarkup.setKeyboard(list);

        sendMessage.setChatId(chatId);
        sendMessage.setText(text.toString());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }





}
