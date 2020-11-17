import com.ogofit.game.client.StompClient;
import com.ogofit.game.models.State;

import java.util.concurrent.ExecutionException;

public class Game {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String clientName = System.getenv("OGA_FIT_GAMECLIENT_NAME");
        String serverUrl = System.getenv("OGA_FIT_GAMESERVER_URL");

        System.out.println("************ Welcome " + clientName + " ************");
        System.out.println("Let's Start the game");
        System.out.println(clientName + ":::" + serverUrl);
        StompClient stompClient = new StompClient(serverUrl, clientName);
        stompClient.start();
    }
}
