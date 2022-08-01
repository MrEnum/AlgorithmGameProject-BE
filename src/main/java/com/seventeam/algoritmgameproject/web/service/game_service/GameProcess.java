package com.seventeam.algoritmgameproject.web.service.game_service;

import com.seventeam.algoritmgameproject.domain.model.game.GameProcessMessage;
import com.seventeam.algoritmgameproject.domain.model.login.User;

public interface GameProcess {
    void GameProcessManager(GameProcessMessage.Request request);

    void exit(GameProcessMessage.Request request);
    void exitAndComPile3FailWinMessage(GameProcessMessage.Request request);

    void timeoutMessage(GameProcessMessage.Request request);

    void compileFail(String username, String roomId);

    void loseMessage(String username, String roomId);
    void sendProcessMessage(GameProcessMessage.Request request);

    void winAndOtherLoseProcess(String roomId, User user);

    void winProcess(User user);
    void lose(User user);

    void loseProcess(String roomId, User user);
    User getUnWrapUser(String username);
}
