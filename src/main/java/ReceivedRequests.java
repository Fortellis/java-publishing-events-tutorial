import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReceivedRequests extends HttpServlet{

    public ReceivedRequests(){
        super();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        
        System.out.println("This is you receiving the request.");
        PrintWriter asynchronousAPIResponse = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        asynchronousAPIResponse.print("{\"response\": \"Updated\"}");
        asynchronousAPIResponse.flush();
        //Get the request the request to the Java file.
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String eventRequest = buffer.toString();
        System.out.println("This is your event request: " + eventRequest);
        
        newEvent(eventRequest);

        AsynchronousAPI Event = new AsynchronousAPI();
        String update = Event.data;
        System.out.println("This is the updated data: " + update);


    }
    public void newEvent(String newEventRequest) {
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File  wholeFile =new File (classLoader.getResource("payload.json").getFile());
            Path path = Paths.get(wholeFile.toString());
            String str = newEventRequest.toString();
            byte[] arr = str.getBytes();
            try{
                Files.write( path, arr);
            }catch(IOException ex){
                System.out.print("Invalid Path");
            }
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }


}
