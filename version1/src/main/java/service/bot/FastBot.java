package service.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static service.bot.BotService.*;
import static utils.Const.*;
import static utils.TelegramUtils.*;

public class FastBot extends TelegramLongPollingBot {
    Map<String, Stack<SendMessage>> userState;
    @Override
    public void onUpdateReceived(Update update) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
            Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
            String chatId = message.getChatId().toString();
            if(message.getText().equals(START)){
                customExecute(botMenu(message), null, null, null);
            }
        });
    }

    public void customExecute(SendMessage sendMessage, AnswerCallbackQuery answerCallbackQuery, EditMessageText editMessageText, DeleteMessage deleteMessage){
        try {
            if(sendMessage != null)
                execute(sendMessage);
            else if(answerCallbackQuery != null)
                execute(answerCallbackQuery);
            else if(editMessageText != null)
                execute(editMessageText);
            else if(deleteMessage != null)
                execute(deleteMessage);

        } catch (TelegramApiException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
