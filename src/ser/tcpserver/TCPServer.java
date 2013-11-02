package ser.tcpserver;

import java.io.DataInputStream;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

import java.net.*;
import java.io.*;

import  ser.serialdata.SerData;

public class TCPServer {

    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream streamIn =  null;
    private DataOutputStream streamOut =  null;


    public void startServer()
    {
        int port = 2222;

        try
        {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);

            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted: " + socket);

            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            boolean done = false;

        while (!done)
        {
            try
            {
                //читаем входящее соообщение
                String line = streamIn.readUTF();

                //парсим
                String stat = parceData(line);

                //отправляем сообщение
                streamOut.writeUTF(stat);
                streamOut.flush();

                done = line.equals(".bye");
            }
            catch(IOException ioe)
            {
               done = true;
               System.out.println(ioe);
            }
        }

        close();

    }
    catch(IOException ioe)
    {
        System.out.println(ioe);
    }

   }

        public void close() throws IOException
        {
            if (socket != null)    socket.close();
            if (streamIn != null)  streamIn.close();
        }


    public String parceData(String messageData)  {

        String status="err";

        if (messageData!=null) {

        System.out.println(messageData);

        String[] tokens = messageData.split("[,]");
        Integer[] serMesInt= new Integer[3];

        if (tokens.length==4 && tokens[0].equals("MOTOR"))
        {
            serMesInt[0]= Integer.parseInt(tokens[1]);
            serMesInt[1]= Integer.parseInt(tokens[2]);
            serMesInt[2]= Integer.parseInt(tokens[3]);

            if (0<=serMesInt[0]&& serMesInt[0]<4   &&
                    0<=serMesInt[1]&& serMesInt[1]<256 &&
                    0<=serMesInt[2]&& serMesInt[2]<256    )
            {
                        //формируем строку
                        SerData.sendstring =  serMesInt[0].toString()+","+
                        serMesInt[1].toString()+","+
                        serMesInt[2].toString()+"*";
                        System.out.println(SerData.sendstring);
                        status ="Set motor: "+ SerData.sendstring;
            }
        }

        if (tokens.length==1 && tokens[0].equals("STATUS"))
        {
          status ="SEN:"+ SerData.data0;
            System.out.println(SerData.data0);
        }

        }
       return status;

     }
}
