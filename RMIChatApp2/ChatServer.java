import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ChatService service = new ChatServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ChatService", service);
            System.out.println("Chat Server is ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
