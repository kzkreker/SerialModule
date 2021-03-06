//импорт классов сервера и обработчика последовательного порта
import ser.httpserver.HttpServer;
import ser.serialport.SerialTest;
import ser.tcpserver.TCPServer;
//основные хедеры


public class Main {

    public static void main(String[] args) throws Throwable {

        // создаем экземпляр класса для работы с последовательным портом
        final SerialTest portData = new SerialTest();

        portData.initialize();
        portData.writeData();

        //запускаем сервер и ждем подключения новых пользователей
        HttpServer server =new HttpServer();
        server.await();

    }
}
