
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class b1 {

        final static int ServerPort = 5000;
        public static String MarketList[];
        public static String m;

        public static String  gatherOrderInfo()
        {
            final Scanner scn = new Scanner(System.in);
            String MarketName;
            String InstrumentName;
            String Trade;
            int Qty;
            while (true){
                System.out.println("Welcome broker\n ");
                PrintMarkets();
                PrintInstruments();

                String msg = scn.nextLine();

                String array1[] = msg.split(Pattern.quote("|"));

                if (CheckBuyInput(msg) == true)
                {
                    //get market name/id to attach as recipient id
                    String selectedMarket = null;// = array1[0];
                    if (array1[0].equals("MARKET1"))
                    {
                        selectedMarket = MarketList[1];
                    } else if (array1[1].equals("MARKET2"))
                    {
                        selectedMarket = MarketList[2];
                    }
                    m = msg + "#" + selectedMarket;
                    return m;
                }
            }

          //  System.out.println(m);
          //  return  m;
            //InstrumentName +"|"+Trade+"|" + Qty; //scn.nextLine();
        }

        public static void PrintMarkets()
        {
            System.out.println("Markets available: ");
            System.out.println("MARKET1");
            System.out.println("MARKET2");
        }
        public static void PrintInstruments()
        {
            System.out.println("Instruments available: ");
            System.out.println("DIMPHO");
            System.out.println("Instrument2");
            System.out.println("Here's an example of how to place an order:\n" +
                    "BUYING:\nMARKET1|INSTRUMENT2|BUY|4\nSELLING:\nMARKET1|INSTRUMENT2|SELL|4");
        }

    public static boolean StringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

        //Markets buy and sell
        public static Boolean CheckBuyInput(String w) {

            boolean res = false;
            if (w.length() == 0)
                return false;

            String UserInput[] = w.split(Pattern.quote("|"));

            if (UserInput[0].contains("MARKET1")) {
              //  System.out.println("bitch");
                res =true;
            }
            else{
             //   System.out.println("Masepa 1");
                res = false;
            }
            if (UserInput[1].contains("DIMPHO") || UserInput[1].contains("POTATO"))
            {
             //   System.out.println("let me see you");
                res =true;
            }
            else{
               // System.out.println("Masepa 2");
                res = false;
            }
            if (UserInput[2].contains("BUY") || UserInput[2].contains("SELL"))
            {
            //    System.out.println("shake that ass");
                res =true;
            }
            else{
             //   System.out.println("Masepa 3");
                res = false;
            }
            if (StringInt(UserInput[3]) == true)
            {
              //  System.out.println("!!!!");
                res = true;
            }
            else{
               // System.out.println("Masepa 4");
                res = false;
            }

            return res;
        }

        public static void main(String args[]) throws UnknownHostException, IOException
        {
            final String[] myID = new String[1];
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
                        String msg =  gatherOrderInfo();
                      //  System.out.println(msg);//gather order details //scn.nextLine();

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


                            if (msg.length() == 6)
                            {
                                myID[0] = msg;
                                System.out.println(msg);
                            }
                            else if (msg.contains("availablemarkets"))
                            {
                                MarketList = msg.split(Pattern.quote("|"));
//                                System.out.println(MarketList[1]);
//                                System.out.println(MarketList[2]);

                            //    System.out.println(msg);
                            }
                            else if (msg.length() > 6)
                            {
                                System.out.println(msg);
                            }

                           // System.out.println(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            readMessage.start();
            sendMessage.start();
        }
}
