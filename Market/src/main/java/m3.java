import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;


public class m3
{
    final static int ServerPort = 5001;
    ArrayList<String> Items = new ArrayList<String>();

    static Instrument x = new Instrument("Dimpho",10, 20, 5);


    public static void main(String args[]) throws UnknownHostException, IOException
    {
        final Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        final DataInputStream dis = new DataInputStream(s.getInputStream());
        final DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        dos.writeUTF(msg );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();

                        //split message and check it
                        if (msg.length() > 6)
                        {
                            String array1[]= msg.split(Pattern.quote("|"));

                            System.out.println(array1[0]);
                            System.out.println(array1[1]);
                            System.out.println(array1[2]);
//                            int a = x.sellqty(array1[2]);
//                            int y = x.buyqty(array1[2]);
//                            System.out.println(a);

                        }else{
                            System.out.println(msg);
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });
        sendMessage.start();
        readMessage.start();
    }

}
