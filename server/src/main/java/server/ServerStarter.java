
package server;
import java.io.IOException;

/*
THIS WILL BE OUR MAIN CLASS WHERE EVERYTHING STARTS
THIS STARTS THE SERVER VROOM VROOM

*/
public class ServerStarter
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        System.out.println("server is listening");

        //shutdown hook to close session when server is shut down
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
                server.stopServer();
                System.out.println("Server stopped and session closed");
        }));

        server.listen();
    }
}
