import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer0 implements Runnable{
    public static int SERVER_PORT = 10010;

    private final Socket socket;

    public BrokerServer0(Socket socket){
        this.socket = socket;
    }


    public void run() {
        try (BufferedReader in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream())){
            while (true){
                String str = in.readLine();
                if(str == null){
                    continue;
                }
                System.out.println("(Runnable)½ÓÊÕµ½Ô­Ê¼Êý¾Ý£º" + str);
                if (str.equals("consume")) { 
                    String message = Broker.consume();
                    out.println (message);
                    out.flush();
                } else { 
                    Broker.produce(str);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server  = new ServerSocket(SERVER_PORT);
        while (true){
            BrokerServer0 brokerServer0 = new BrokerServer0(server.accept());
            new Thread(brokerServer0).start();
        }
    }
}
