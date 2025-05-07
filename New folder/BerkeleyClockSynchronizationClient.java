import java.io.*;
import java.net.*;

public class BerkeleyClockSynchronizationClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345); // connect to coordinator
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        // Read time sent by client handler (simulated)
        long clientTime = input.readLong(); 
        System.out.println("Client time sent: " + clientTime);

        // Send coordinator's time
        long coordinatorTime = System.currentTimeMillis();
        output.writeLong(coordinatorTime);

        // Read offset sent back
        int offset = input.readInt();
        System.out.println("Offset received: " + offset);

        // Receive corrected time
        long correctedTime = input.readLong();
        long finalTime = clientTime + correctedTime;
        System.out.println("Final synchronized client time: " + finalTime);

        input.close();
        output.close();
        socket.close();
    }
}
