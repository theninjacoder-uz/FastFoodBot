package service.bot;

import model.user.UserRole;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.user.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static service.bot.BotService.*;
import static utils.BotResponse.PHONE_NUMBER_REQUEST;
import static utils.BotResponse.SUCCESSFULLY_REGISTERED;
import static utils.Const.*;
import static utils.TelegramUtils.*;

public class FastBot extends TelegramLongPollingBot {
    Map<String, Stack<SendMessage>> userState = new HashMap<>();
    UserService userService = new UserService();
    @Override
    public void onUpdateReceived(Update update) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
            Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
            String chatId = message.getChatId().toString();

            // Defining user States
            if(message.hasContact() || userState.get(chatId) != null && userState.get(chatId).peek().getText().equals(PHONE_NUMBER_REQUEST)){
                String phoneNumber = message.hasContact() ? message.getContact().getPhoneNumber() : message.getText();
                String s = userService.savePhone(phoneNumber, chatId);
                if(s.equals(SUCCESSFULLY_REGISTERED))
                    userState.get(chatId).push(new SendMessage(chatId, s));
            }

            else if(message.getText().equals(START)){
                UserRole userRole = userService.checkAndSave(chatId, UserRole.CONSUMER);

                userState.get(chatId).clear();
                Stack<SendMessage> stateStack = userState.get(chatId);
                stateStack.push(botMenu(message));
                userState.put(chatId, stateStack);

                if(userRole == null) {
                    stateStack.push(getRegister(message));
                    userState.put(chatId, stateStack);
                }
            }
            else if(message.getText().equals(BOT_TOKEN)){
                userService.checkAndSave(chatId, UserRole.ADMIN);
            }


            //Executing methods

            customExecute(userState.get(chatId).peek(), null, null, null);

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
