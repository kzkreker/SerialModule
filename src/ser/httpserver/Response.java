package ser.httpserver;

import  ser.serialdata.SerData;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;

/*
  HTTP Response = Status-Line
    *(( general-header | response-header | entity-header ) CRLF)
    CRLF
    [ message-body ]
    Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
*/

public class Response {

    Request request;
    OutputStream output;
    String uri;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request, String uri) {
        this.request = request;
        this.uri=uri;
    }

    public void sendStaticResource() throws IOException {

        try {

            String httpMessage;


            if (uri.equals("/sensors"))
            {
                System.out.println("Whe are here!");

                 httpMessage =
                 "HTTP/1.0 200 Ok\r\n"+
                 "Content-Type: text/html; charset=\"utf-8\"\r\n"+
                 "\r\n" +
                 "Current sensors: "+SerData.data0+"\n";

                 output.write(httpMessage.getBytes());
            }
            else if (!uri.equalsIgnoreCase("/sensors"))
            {

               SerData.sendstring = parceUrlData(uri,SerData.sendstring);
                httpMessage =
                "HTTP/1.0 200 Ok\r\n"+
                "Content-Type: text/html; charset=\"utf-8\"\r\n"+
                "\r\n" +
                "Send new speed: "+uri+"\n"+
                "Current sensors: "+SerData.data0+"\n";

                 output.write(httpMessage.getBytes());
            }

        }
        catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString() );
        }
    }

    public String parceUrlData(String urlData, String dataSerOld)  {

        String serMess=dataSerOld;

        urlData=urlData.substring(1);

        System.out.println(urlData+" //*parseurl");


        String[] tokens = urlData.split("[,]");
        Integer[] serMesInt= new Integer[3];

        if (tokens.length==3)
        {
            serMesInt[0]= Integer.parseInt(tokens[0]);
            serMesInt[1]= Integer.parseInt(tokens[1]);
            serMesInt[2]= Integer.parseInt(tokens[2]);

            if (0<=serMesInt[0]&& serMesInt[0]<4   &&
                0<=serMesInt[1]&& serMesInt[1]<256 &&
                0<=serMesInt[2]&& serMesInt[2]<256    )
            {
                serMess =  serMesInt[0].toString()+","+
                           serMesInt[1].toString()+","+
                           serMesInt[2].toString()+"*";
            }

           }
        System.out.println(serMess);
        return serMess;

    }
}