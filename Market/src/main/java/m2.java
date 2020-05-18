import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;


public class m2
{
    final static int ServerPort = 5001;
    ArrayList<String> Items = new ArrayList<String>();

    public static String executeOrder(String order)
    {
        String OrderDetails[]= order.split(Pattern.quote(" : "));
        String RecipientsID = OrderDetails[0];
        String message[] = OrderDetails[1].split(Pattern.quote("|"));

        if (message[1].equals("DIMPHO"))
        {
            if (message[2].equals("BUY"))
            {
               return x.buyqty(message[3]) +"#"+RecipientsID;
            } else
            {
                return x.sellqty(message[3]) +"#"+RecipientsID;
            }
        } else if (message[1].equals("OREO"))
        {
            if (message[2].equals("BUY"))
            {
                return a.buyqty(message[3]) +"#"+RecipientsID;
            }
            else
            {
                return a.sellqty(message[3])+"#"+RecipientsID;
            }
        }

        return "Order incorrect.Try again"+"#"+RecipientsID;
    }

    static Instrument x = new Instrument("Dimpho",10, 20, 5);
    static Instrument a = new Instrument("Oreo", 5, 15, 6);


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
                        dos.writeUTF(msg +"#"+msg.length());
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
                        String response = "";
                        //split message and check it
                        if (msg.length() > 6)
                        {
                            response = executeOrder(msg);
                            dos.writeUTF(response +"#"+response.length());
//                            String array1[]= msg.split(Pattern.quote("|"));
//                            System.out.println(array1[0]);
//                            System.out.println(array1[1]);
//                            System.out.println(array1[2]);
//                            System.out.println(array1[3]);
//                            int a = x.sellqty(array1[2]);
//                            int y = x.buyqty(array1[2]);
                            //System.out.println(a);

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
