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
import static utils.BotResponse.*;
import static utils.Const.*;
import static utils.TelegramUtils.*;

public class FastBot extends TelegramLongPollingBot {
    Map<String, Stack<SendMessage>> userState = new HashMap<>();
    Map<String, String> userPhoneNum = new HashMap<>();
    Map<String, String> userSmsCode = new HashMap<>();
    UserService userService = new UserService();

    @Override
    public void onUpdateReceived(Update update) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
            String chatId = message.getChatId().toString();

            // Defining user States
            if (message.hasContact() || userState.get(chatId) != null && userState.get(chatId).peek().getText().equals(PHONE_NUMBER_REQUEST)) {
                String phoneNumber = message.hasContact() ? message.getContact().getPhoneNumber() : message.getText();
                String s = userService.savePhone(phoneNumber, chatId);
                if (s.equals(PHONE_SUCCESSFULLY_SAVED)) {
                    userState.get(chatId).push(new SendMessage(chatId, s));
                    userPhoneNum.put(chatId, phoneNumber);
                }
            }
            else if(message.hasLocation() || message.getText().equals(TAKE_YOURSELF)){
                userService.checkAndGetLocation(chatId, message.getLocation().getLatitude(), message.getLocation().getLongitude());
                userState.get(chatId).push(categoryMenu(message, null));
            }
            else if (update.hasMessage()) {
                String text = message.getText();
                if (text.equals(START)) {
                    UserRole userRole = userService.checkAndSave(chatId, UserRole.CONSUMER);

                    Stack<SendMessage> stateStack = new Stack<>();
                    stateStack.push(botMenu(message));
                    userState.put(chatId, stateStack);

                    if (userRole == null) {
                        stateStack.push(getRegister(message));
                        userState.put(chatId, stateStack);
                    }
                }
                else if(text.equals(MAKE_ORDER)){
                    userState.get(chatId).push(makeOrderMenu(message));
                }
                else if(userState.get(chatId).peek().getText().equals(ORDER_TYPE)){
                    userState.get(chatId).push(categoryMenu(message, text));
                }
                else if (message.getText().equals(BOT_TOKEN)) {
                    userService.checkAndSave(chatId, UserRole.ADMIN);
                }
            }


            //Executing methods

            if (userState.get(chatId).peek().getText().equals(PHONE_SUCCESSFULLY_SAVED)) {
                String code = userService.sendSmsCode("+" + userPhoneNum.get(chatId));
                userSmsCode.put(chatId, code);
                userState.get(chatId).push(new SendMessage(chatId, SMS_CODE_VERIFICATION));
                customExecute(new SendMessage(chatId, SMS_CODE_VERIFICATION), null, null, null);
                return;
            } else if (userState.get(chatId).peek().getText().equals(SMS_CODE_VERIFICATION)) {
                if (message.getText().equals(userSmsCode.get(chatId))) {
                    Stack<SendMessage> stateStack = new Stack<>();
                    stateStack.push(botMenu(message));
                    userState.put(chatId, stateStack);
                    customExecute(new SendMessage(chatId, SUCCESSFULLY_REGISTERED), null, null, null);
                }
            }

            customExecute(userState.get(chatId).peek(), null, null, null);
        });
    }

    public void customExecute(SendMessage sendMessage, AnswerCallbackQuery answerCallbackQuery, EditMessageText editMessageText, DeleteMessage deleteMessage) {
        try {
            if (sendMessage != null)
                execute(sendMessage);
            else if (answerCallbackQuery != null)
                execute(answerCallbackQuery);
            else if (editMessageText != null)
                execute(editMessageText);
            else if (deleteMessage != null)
                execute(deleteMessage);

        } catch (TelegramApiException exception) {
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
