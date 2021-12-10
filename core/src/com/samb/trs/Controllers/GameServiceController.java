package com.samb.trs.Controllers;

import com.samb.trs.Model.Score;
import com.samb.trs.Resources.EventId;
import com.samb.trs.Resources.LeaderboardId;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.IGameServiceListener;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

/**
 * GameServiceController manages all important features of google play services or other game services depending on the operating system.
 */
public class GameServiceController extends BaseController implements IGameServiceListener{

    private IGameServiceClient gameServiceClient;

    /**
     *
     * @param mainController The boss controller
     * @param gameServiceClient The operating system dependent service client
     */
    public GameServiceController(MainController mainController, IGameServiceClient gameServiceClient){
        super(mainController);
        this.gameServiceClient = gameServiceClient;

        // Set NoGameServiceClient if no gameServiceClient is specified
        if(this.gameServiceClient == null) this.gameServiceClient = new NoGameServiceClient();

        mainController.getMainAdapter().setGameServiceClient(gameServiceClient);

        // Set gameServiceListener
        this.gameServiceClient.setListener(this);

        // Log player in to game service
        this.gameServiceClient.resumeSession();
    }

    public void submitScore(Score score){
        gameServiceClient.submitToLeaderboard(LeaderboardId.LEADERBOARD.getId(), score.getScore(), null);
        gameServiceClient.submitEvent(EventId.COIN_COLLECTED.getId(), score.getCollected_coins());
        gameServiceClient.submitEvent(EventId.GAME_PLAYED.getId(), 1);
    }

    /**
     * Needs to be called when the application is resumed.
     */
    public void resumeSession(){
        gameServiceClient.resumeSession();
    }

    /**
     * Needs to be called when the application is paused.
     */
    public void pauseSession(){
        gameServiceClient.pauseSession();
    }

    public void logIn(){
        gameServiceClient.logIn();
    }

    public void logOff(){
        gameServiceClient.logOff();
    }

    /**
     * Standard Constructor if no gameService is available
     * @param mainController The boss controller
     */
    public GameServiceController(MainController mainController){
        this(mainController, mainController.getMainAdapter().getGameServiceClient());
    }

    public IGameServiceClient getGameServiceClient() {
        return gameServiceClient;
    }

    @Override
    public void gsOnSessionActive() {

    }

    @Override
    public void gsOnSessionInactive() {

    }

    @Override
    public void gsShowErrorToUser(GsErrorType et, String msg, Throwable t) {

    }

    @Override
    public void dispose() {

    }
}
