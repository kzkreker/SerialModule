package ser.httpserver;

import  ser.serialdata.SerData;

import java.io.OutputStream;
import java.io.IOException;


public class Response {

    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;

    }

    public void sendStaticResource() throws IOException {

        try
        {
            String httpMessage;

            httpMessage =
            "HTTP/1.0 200 Ok\r\n"+
            "Content-Type: text/html; charset=\"utf-8\"\r\n"+
            "\r\n" +
            "Current sensors: "+SerData.data0+"\n";

            output.write(httpMessage.getBytes());
        }
        catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString() );
        }
    }
}