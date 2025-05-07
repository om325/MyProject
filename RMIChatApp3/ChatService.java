import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    String sendMessage(String clientName, String message) throws RemoteException;
}
