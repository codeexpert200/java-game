import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class GUIClient extends JFrame
{
   private JTextField jftName;
   private JTextField jftIP;
   private JTextField jtfPortNum;
   private JTextField jtfMessage;
   private JTextArea jtaChat;
   private JButton jbConnect;
   private JButton jbSend;
   public GUIClient()
   {
      JPanel jpNorth = new JPanel();
      JPanel jpCenter = new JPanel();
      JPanel jpSouth = new JPanel();
      JMenuBar jmb = new JMenuBar();       
      setJMenuBar(jmb);
      JMenu file = new JMenu("File");
      JMenuItem exit = new JMenuItem("Exit");
      file.add(exit);
      jmb.add(file);
      JMenu help = new JMenu("Help");
      JMenuItem about = new JMenuItem("About");
      help.add(about);
      jmb.add(help);
      exit.addActionListener( 
         new ActionListener()
         {
            public void actionPerformed(ActionEvent ae) 
            {
               System.exit(0);   
            }
         } 
                            );
      about.addActionListener(
         new ActionListener()
         {
            public void actionPerformed(ActionEvent ae)
            {
               JOptionPane.showMessageDialog(null,"\t\tThis is a chat program which connects multiple clients and allows them to chat with each other \nWorking: \nA client will have to enter their username, IP Address and Port number in order to connect to the server.\nUser Name: name user wants to display\nIP Address: IP adrress of the client(user), IP address can be found by typing \"ipconfig\" in CMD\nPort No.: Port that user wnats to connect to. ");
            }
         }
                              );                      
      JLabel jlName = new JLabel("User Name");
      jftName = new JTextField(15);
      JLabel jlIP = new JLabel("IP Adress");
      jftIP = new JTextField(20);
      JLabel jlPort = new JLabel("Port Number");
      jtfPortNum = new JTextField(10);
      jbConnect = new JButton("Connect");
      jpNorth.add(jlName);
      jpNorth.add(jftName);
      jpNorth.add(jlIP);
      jpNorth.add(jftIP);
      jpNorth.add(jlPort);
      jpNorth.add(jtfPortNum);
      jpNorth.add(jbConnect);
      jtaChat = new JTextArea(20,50);
      JScrollPane jsp = new JScrollPane(jtaChat,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      jpCenter.add(jsp);
      JLabel jlMessage = new JLabel("Enter Message here ");
      jtfMessage = new JTextField(30);
      jbSend = new JButton("Send Message");
      jpSouth.add(jlMessage);
      jpSouth.add(jtfMessage);
      jpSouth.add(jbSend);
      add(jpNorth,BorderLayout.NORTH);
      add(jpCenter, BorderLayout.CENTER);
      add(jpSouth, BorderLayout.SOUTH);
      Client listener = new Client(jtaChat, jtfMessage, jftName, jftIP, jtfPortNum, jbSend, jbConnect);
      jbSend.addActionListener(listener);
      jbConnect.addActionListener(listener);
      setLocation(250,250);
      setVisible(true);
      setTitle("Client");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
   }
   public static void main(String[] args)
   {   
      new GUIClient();
   }
}