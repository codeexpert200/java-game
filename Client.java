import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Client implements ActionListener
{
   private JTextArea jtaChat;;
   private JTextField jtfMessage;
   private JTextField jtfName;
   private JTextField jtfIP;
   private JTextField jtfPortNum;
   private JButton jbSend;
   private JButton jbConnect;
   private String clientMsg;
   private String clientName;
   private PrintWriter pout;
   private Socket s;
   private ClientClass client;  
   public Client(JTextArea jtaChat, JTextField jtfMessage, JTextField jtfName, JTextField jtfIP, JTextField jtfPortNum, JButton jbSend, JButton jbConnect)
   {
      this.jtaChat = jtaChat;
      this.jtfMessage = jtfMessage;
      this.jtfName = jtfName;
      this.jtfIP = jtfIP;
      this.jtfPortNum = jtfPortNum;
      this.jbSend = jbSend;
      this.jbConnect = jbConnect;
   }
   public void actionPerformed(ActionEvent e)
   {  
      if(e.getActionCommand().equals("Connect"))
      {    
         if(jtfName.getText().equals("") || jtfIP.getText().equals("") || jtfPortNum.getText().equals(""))
         {
            JOptionPane.showMessageDialog(null, "Please enter your UserName, IP and/ or Port Number before trying to connect");
         }
         else
         {
            try
            {
               String IP = jtfIP.getText();     
               int port = Integer.parseInt(jtfPortNum.getText());
               s = new Socket(IP, port);
            //open input from server
               BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream()));
               OutputStream out = s.getOutputStream();
               pout = new PrintWriter(out);           
               clientName = jtfName.getText();
               System.out.println("Client Name" + clientName);
               if(sendName(clientName, bin) == true)
               {
                  client = new ClientClass(bin);
                  client.start();
                  jtfName.setEditable(false);
                  jtfIP.setEditable(false);
                  jtfPortNum.setEditable(false);
                  jbSend.setEnabled(true);
                  jbConnect.setText("Disconnect");
               }
            }
            catch(Exception ez)
            {
               JOptionPane.showMessageDialog(null, "ERROR could not connect");
            }
         }
      }
      else if(e.getActionCommand().equals("Disconnect"))
      {
         try
         {
            String str2="";
            String msg =clientName + " disconnected";
            int n=msg.length();
            for(int i=0;i<n;i++)
            {
               char c=msg.charAt(i);
               System.out.println(msg);   
               int a=(int)c;
               if(Character.isLetter(c))
               {  
                  a+=3;
                  str2=str2+((char)a);
               }
               else
               {
                  str2=str2+(c);
               }
            }
            pout.flush(); 
            jtfName.setEditable(false);
            jbConnect.setEnabled(false);  
            jtfIP.setEditable(false);
            jtfPortNum.setEditable(false);
            jbSend.setEnabled(false);
            sendMsg(str2);
            System.exit(0);
         }catch(Exception ed)
         {
            System.out.println("Exception in Disconnect");
         }
      }
      else if(e.getSource() == jbSend)
      {
         String str2="";
         String msg = clientName + ":-" + jtfMessage.getText();
         int n=msg.length();
         for(int i=0;i<n;i++)
         {
            char c=msg.charAt(i);
            System.out.println(msg);   
            int a=(int)c;
            if(Character.isLetter(c))
            {  
               a+=3;
               str2=str2+((char)a);
            }
            else
            {
               str2=str2+(c);
            }
         }
         sendMsg(str2);
         jtfMessage.setText("");
      }
   }// end action performed
   public void sendMsg(String _clientMessage)
   {
      try
      {
         int counter = 0;
         clientMsg = _clientMessage;
         while(counter < 2)
         {
            pout.println(clientMsg);
            pout.flush();
            counter++;
         }
      }catch(Exception eh)
      {
         eh.printStackTrace();
      }
   }
   public boolean sendName(String _name, BufferedReader br)
   {
      System.out.println("Entered sendName, _name = " + _name);
      try
      {
         pout.println(_name);
         pout.flush();
         if(br.readLine().equals("DENY"))
         {
            JOptionPane.showMessageDialog(null, "Sorry that name alread exists. Please try another one");   
            return false;
         }
      }
      catch(Exception ef)
      {
         ef.printStackTrace();
      }
      return true;
   }// end send message
   class ClientClass extends Thread
   {
      private BufferedReader br;
      public ClientClass(BufferedReader _br)
      {
         br = _br;
      }
      public void run()
      {
         String message;
         try
         {
            while(true)
            {
               if((message = br.readLine()) != null)
                  jtaChat.append(br.readLine() + "\n");
            }
         }
         catch(SocketException z)
         {
            JOptionPane.showMessageDialog(null,"Error: server has disconnected");
            pout.println(clientName + "Disconnected");
            pout.flush();
            client.interrupt();
            jtfName.setEditable(true);
            jtfIP.setEditable(true);
            jtfPortNum.setEditable(true);
            jbSend.setEnabled(false);
            jtaChat.setText("");
            jbConnect.setEnabled(true);
            jbConnect.setText("Connect");
            try
            {
               s.close();
               br.close();
            }
            catch(IOException ioe)
            {
            }   
         }
         catch(IOException ioe)
         {
            JOptionPane.showMessageDialog(null,"IOException occured in Client"); 
         }
      }
   }
   public static void main(String[] args)
   {
      new GUIClient();
   }
}