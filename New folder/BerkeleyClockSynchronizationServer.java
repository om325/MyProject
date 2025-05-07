import java.util.*; 
import java.net.*; 
import java.io.*; 
class BerkeleyClockSynchronizationServer { 
static final int PORT = 12345; 
static final int N = 5;  // Number of clients 
static long coordinatorTime = 0;  // The time of the coordinator 
static List<ClientHandler> clients = new ArrayList<>(); 
static int totalDelay = 0; 
public static void main(String[] args) throws IOException { 
ServerSocket serverSocket = new ServerSocket(PORT); 
System.out.println("Coordinator started, waiting for clients..."); 
// Accepting N clients 
for (int i = 0; i < N; i++) { 
Socket clientSocket = serverSocket.accept(); 
ClientHandler client = new ClientHandler(clientSocket, i); 
clients.add(client); 
new Thread(client).start(); 
} 
// Coordinator time is set (simulate the coordinator's clock) 
coordinatorTime = System.currentTimeMillis(); 
// Wait for all clients to report back 
try {
Thread.sleep(2000); 
} catch (InterruptedException e) { 
e.printStackTrace(); 
} 
// Compute the time adjustments for each client 
int offsetSum = 0; 
for (ClientHandler client : clients) { 
offsetSum += client.getOffset(); 
} 
long avgOffset = offsetSum / N; 
// Send back the corrected time to clients 
for (ClientHandler client : clients) { 
client.sendCorrectedTime(avgOffset); 
} 
System.out.println("Synchronization completed."); 
serverSocket.close(); 
} 
// ClientHandler: handles each client's request 
static class ClientHandler implements Runnable { 
private Socket clientSocket; 
private int clientId; 
private long clientTime; 
private int offset;
public ClientHandler(Socket socket, int id) { 
this.clientSocket = socket; 
this.clientId = id; 
} 
@Override 
public void run() { 
try { 
DataInputStream input = new DataInputStream(clientSocket.getInputStream()); 
DataOutputStream output = new 
DataOutputStream(clientSocket.getOutputStream()); 
// Client sends its current time to the coordinator 
clientTime = System.currentTimeMillis(); 
output.writeLong(clientTime); 
System.out.println("Client " + clientId + " sent time: " + clientTime); 
// Coordinator sends its time 
long coordinatorReceivedTime = input.readLong(); 
System.out.println("Coordinator time received: " + coordinatorReceivedTime); 
// Calculate the offset between client and coordinator 
offset = (int) (clientTime - coordinatorReceivedTime); 
// Send the offset back to the coordinator 
output.writeInt(offset); 
// Wait for corrected time from the coordinator 
long correctedTime = input.readLong();
long finalClientTime = clientTime + correctedTime; 
System.out.println("Client " + clientId + " adjusted time to: " + finalClientTime); 
input.close(); 
output.close(); 
clientSocket.close(); 
} catch (IOException e) { 
e.printStackTrace(); 
} 
} 
public int getOffset() { 
return offset; 
} 
public void sendCorrectedTime(long avgOffset) throws IOException { 
DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream()); 
output.writeLong(avgOffset); 
output.writeLong(coordinatorTime); 
System.out.println("Sent corrected time " + coordinatorTime + " to client " + 
clientId); 
} 
} 
}