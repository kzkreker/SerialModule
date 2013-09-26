
package ser.serialport;

//
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

//
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

//Информационный класс
import  ser.serialdata.SerData;

//библиотеки для работы с таймером
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class SerialTest implements SerialPortEventListener {
    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {"/dev/ttyACM0", "/dev/ttyACM1"};

    private BufferedReader input;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    private OutputStream output = null;


    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }

        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            // output = serialPort.getOutputStream();
            output = serialPort.getOutputStream();
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine=input.readLine();
                System.out.println(inputLine);
                SerData.data0=inputLine;

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }



    public void writeData()
    {
       ScheduledExecutorService service =
                Executors.newSingleThreadScheduledExecutor();

        service.scheduleWithFixedDelay(new Runnable()
        {
            public void run()
            {

                try
                {
                    if (!SerData.buff.equals(SerData.sendstring))
                    {
                        output.write(SerData.sendstring.getBytes());
                        output.flush();
                        System.out.println("Sended");
                        SerData.buff = SerData.sendstring;

                    }
                    else
                    {
                       // System.out.println("Nofing to send");
                    }
                }
                catch (Exception e)
                {
                    System.out.println("This is an error");
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
}