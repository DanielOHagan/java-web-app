package webapp.sockets;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/userchatsocket")
public class UserChatSocket {

    @OnOpen
    public void onOpen() {

    }

    @OnMessage
    public String onMessage(String message) {
        String result = "";

        return result;
    }

    @OnError
    public void onError(Throwable throwable) {

    }

    @OnClose
    public void onClose() {

    }
}