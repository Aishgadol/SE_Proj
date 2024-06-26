
package server;
import java.io.IOException;
import server.SimpleServer;

/*
THIS WILL BE OUR MAIN CLASS WHERE EVERYTHING STARTS
THIS STARTS THE SERVER VROOM VROOM

*/
public class SimpleChatServer
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        System.out.println("server is listening");
        server.listen();
    }
}
