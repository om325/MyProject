import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {

    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String sendMessage(String clientName, String message) throws RemoteException {
        String response = "Server received from " + clientName + ": " + message;
        System.out.println(response);
        return "Hello " + clientName + ", your message was: " + message;
    }
}
