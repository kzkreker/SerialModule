package ser.httpserver;

import ser.serialdata.SerData;

import java.io.InputStream;
import java.io.IOException;

public class Request {

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public void   parse() {
        // Read a set of characters from the socket
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j=0; j<i; j++) {
            request.append((char) buffer[j]);
        }

        uri = parseUri(request.toString());
        SerData.sendstring = parceUrlData(uri,SerData.sendstring);

    }


    public String parceUrlData(String urlData, String dataSerOld)  {

        String serMess=dataSerOld;

        urlData=urlData.substring(1);

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
        return serMess;
    }


    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}