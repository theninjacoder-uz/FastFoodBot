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
                String s = userService.savePhone(message.getContact().getPhoneNumber(), chatId);
                customExecute(new SendMessage(chatId, s), null, null, null);
            }

            if(message.getText().equals(START)){
                UserRole userRole = userService.checkAndSave(chatId, UserRole.CONSUMER);
                if(userRole == null) {
                    Stack<SendMessage> stateStack = new Stack<>();
                    stateStack.push(getRegister(message));
                    userState.put(chatId, stateStack);

                }
                else{
                    Stack<SendMessage> stateStack = userState.get(chatId);
                    stateStack.push(botMenu(message));
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
