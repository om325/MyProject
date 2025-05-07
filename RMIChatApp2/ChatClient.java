import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatService service = (ChatService) registry.lookup("ChatService");

            for (int i = 1; i <= 5; i++) {
                final int clientId = i;
                new Thread(() -> {
                    try {
                        String response = service.sendMessage("Client-" + clientId, "Hello from client " + clientId);
                        System.out.println("Response: " + response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
