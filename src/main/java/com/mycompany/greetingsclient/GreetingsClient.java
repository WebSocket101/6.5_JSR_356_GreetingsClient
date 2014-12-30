package com.mycompany.greetingsclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class GreetingsClient {
    private Session userSession = null;
    public static String message;
    public GreetingsClient(URI endpointURI) throws DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        userSession = container.connectToServer(this, endpointURI);
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("SessionId:" + session.getId());
    }

    public Session getSession() {
        return this.userSession;
    }

    @OnClose
    public void onClose(Session session,CloseReason closeReason) {
        this.userSession = null;
        System.out.println(closeReason);
    }

    public void sendMessage(String message) throws IOException {
        this.userSession.getBasicRemote().sendText(message);
    }

    public static void main(String[] args) throws URISyntaxException, IOException, DeploymentException {
        
        //String webSocketServerURI = "ws://echo.websocket.org";
        String webSocketServerURI = "ws://localhost:8080/GreetingsServerAnnotations/polite";
        //String webSocketServerURI = "ws://localhost:8080/GreetingsServerAPI/polite";
        
        GreetingsClient client = new GreetingsClient(new URI(webSocketServerURI));
        client.sendMessage("Mr. President");   
        client.getSession().addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String message) {
                System.out.println(message);   
            }
        });
        while(true);
    }
}
