package service.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static utils.Const.*;

public class BotService {
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
}
