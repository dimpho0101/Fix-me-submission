import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.*;


class BrokerConnection implements Runnable{

    ServerSocket ssMarket = new ServerSocket(5000, 5);
    private Socket m;
    public static int id = 100000;

    BrokerConnection() throws IOException {
    }

    public void run() {
        while (true) {
            try {
//                System.out.println("Server on port 5000 has started");
//                ServerSocket ss = new ServerSocket(5000);
//                System.out.println("Server is waiting for Broker request");
                m = ssMarket.accept();
                System.out.println("Client is connected on Broker");
//                BufferedReader x = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                String str = x.readLine();
//
//                System.out.println(id + " Says: " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataInputStream dis = null;
            try {
                dis = new DataInputStream(m.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(m.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Creating a new handler for this Broker...");

            // Create a new handler object for handling this request.
            try {
                dos.writeUTF(Integer.toString(id));
                //Send list of markets
                String build = "availablemarkets";
                for(connectionHandler mc : Router.ar)
                {
                    if (mc.getName().contains("11000"))
                    {
                        build += "|" + mc.getName();
                    }

                }
                dos.writeUTF(build);
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectionHandler mtch = new connectionHandler(m, Integer.toString(id), dis, dos);
            id++;
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
            System.out.println("Adding this Broker to active Broker list");
            Router.ar.add(mtch);
            Runnable task3 = () -> {
                System.out.println("Executing Task3 inside : " + Thread.currentThread().getName());
                t.start();
            };

            Router.executorService.execute(task3);
          //  executor.execute(task3);

        }
    }
}

class MarketConnection implements Runnable {

    ServerSocket ssMarket = new ServerSocket(5001, 5);
    private Socket m;
    public static int id = 110000;

    MarketConnection() throws IOException {
    }

    public void run() {
        while (true) {
            try {
//                System.out.println("Server on port 5000 has started");
//                ServerSocket ss = new ServerSocket(5000);
//                System.out.println("Server is waiting for Broker request");
                m = ssMarket.accept();
                System.out.println("Client is connected on Market");
//                BufferedReader x = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                String str = x.readLine();
//
//                System.out.println(id + " Says: " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataInputStream dis = null;
            try {
                dis = new DataInputStream(m.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(m.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Creating a new handler for this Market...");

            // Create a new handler object for handling this request.
            try {
                dos.writeUTF(Integer.toString(id));
             //   dos.writeUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectionHandler mtch = new connectionHandler(m, Integer.toString(id), dis, dos);
            id++;
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
            System.out.println("Adding this Market to active Market list");


            Router.ar.add(mtch);


            Runnable task3 = () -> {
                System.out.println("Executing Task3 inside : " + Thread.currentThread().getName());
                t.start();
            };

            Router.executorService.execute(task3);
            //  executor.execute(task3);

        }
    }
}

public class Router {
   static ExecutorService executorService = null;
    static Vector<connectionHandler> ar = new Vector<>();
    static int i = 0;

    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(16);
        BrokerConnection n = new BrokerConnection();
        MarketConnection x = new MarketConnection();

      //  Socket s = null;
       // Socket m = null;

        Thread broker = new Thread(n);
        Thread market = new Thread(x);

       // broker.start();
       // market.start();
        executorService.execute(broker);
        executorService.execute(market);

    }
}

// ClientHandler class
class connectionHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor
    public connectionHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {
                // receive the string
                received = dis.readUTF();

                System.out.println(received);

                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }

                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();
                String checksum = st.nextToken();

                //Basic checksum
                int msgLen = MsgToSend.length() + recipient.length() + 1;
                int checksumVal = Integer.parseInt(checksum);
                if(msgLen != checksumVal)
                {
                    //print invalid message
                    this.dos.writeUTF("Invalid message.Checksum failed");

                }else{
                    // search through clients vector
                    for (connectionHandler mc : Router.ar)
                    {
                        // if the recipient is found, write on its
                        // output stream
                        if (mc.name.equals(recipient) && mc.isloggedin==true)
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            break;
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}