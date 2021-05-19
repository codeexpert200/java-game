import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;




public class Server
{
   public static void main(String [] args) 
   {
      new Server();
   }
   private ArrayList<PrintWriter> pwList = new ArrayList<PrintWriter>();
   private ArrayList<String> nameList = new ArrayList<String>();
   //Constructor
   public Server()
   {
      ServerSocket ss = null;
      try 
      {
         System.out.println("getLocalHost: "+InetAddress.getLocalHost() );
         System.out.println("getByName:    "+InetAddress.getByName("localhost") );
         ss = new ServerSocket(16789);
         Socket cs = null;
         while(true)
         { 		
         // run forever once up
         //try{
            cs = ss.accept(); 				// wait for connection
            ThreadServer ths = new ThreadServer( cs );
            ths.start();
         } // end while
      }
      catch( BindException be ) 
      {
         System.out.println("Server already running on this computer, stopping.");
      }
      catch( IOException ioe ) 
      {
         System.out.println("IO Error");
         ioe.printStackTrace();
      }
   } // end main
	
   class ThreadServer extends Thread 
   {
      Socket cs;
      public ThreadServer( Socket cs ) 
      {
         this.cs = cs;
      }
      public void run() 
      {
         BufferedReader br;
         PrintWriter opw = null;
         String clientMsg;
         String clientName ="";
         try 
         {
            br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            opw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));    
            clientName = br.readLine();
            if(!(nameList.contains(clientName)))
            {
               opw.println("Client " + clientName + "accepted");
               opw.flush();      
               pwList.add(opw);
               nameList.add(clientName);
               for(PrintWriter onePw:pwList)
               {
                  System.out.println("PrintWriter object " + onePw);
                  onePw.println(clientName + " joined");
                  onePw.flush();     
               }
            }
            else
            {
               opw.println("Deny");
               opw.flush();
               interrupt();
               return;
            }
            while(true)
            {
               clientMsg = br.readLine();					// from client        
               String str2="";
               int n=clientMsg.length();
               for(int i=0;i<n;i++)
               {
                  char c=clientMsg.charAt(i);
                  System.out.println(clientMsg);
                  int a=(int)c;
                  if(Character.isLetter(c))
                  {  
                     a-=3;
                     str2=str2+((char)a);
                  }
                  else
                  {
                     str2=str2+(c);
                  }
               }
               System.out.println("Server read: "+ clientMsg);
               if(clientMsg == null)
               {
                  System.out.println("Null as " + opw);
                  System.out.println("Pwlist size before " + pwList.size());
                  int count = 0;
                  for(Iterator<PrintWriter> it = pwList.iterator(); it.hasNext();)
                  {
                     if(it.next().equals(opw))
                     {
                        System.out.println("Removing object" + opw);
                        it.remove();
                        nameList.remove(count);
                     }
                     count++;
                  }
                  for(PrintWriter onePw : pwList)
                  {
                     System.out.println("PW Object" + onePw);
                     onePw.println(clientName + " left");
                     onePw.flush();
                  }
                  System.out.println("PwList size after " + pwList.size());
                  break;
               }// end of if
               else
               {
                  for(PrintWriter onePw : pwList)
                  {
                     System.out.println("Pw Object " + onePw);
                     onePw.println(str2);
                     onePw.flush();
                  }  
               }             
            }
         }
         catch(SocketException se)
         {
            int count = 0;
            for(Iterator<PrintWriter> it = pwList.iterator(); it.hasNext();){
               if(it.next().equals(opw)){
                  System.out.println("Removing object " + opw);
                  it.remove();
                  nameList.remove(count);
               }
               count++;
            //--END for
               for(PrintWriter onePw: pwList){
                  System.out.println("PW Object" + onePw);
                  onePw.println(clientName + " has left the chat");
                  onePw.flush();
               }
            }
         } catch( IOException e )
         { 
            System.out.println("Inside catch"); 
            e.printStackTrace();
         }// end while
      } // end class ThreadServer 
   }
}