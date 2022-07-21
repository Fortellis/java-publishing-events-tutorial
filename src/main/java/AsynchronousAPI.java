import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.taglibs.standard.extra.spath.Token;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AsynchronousAPI extends HttpServlet{

    public static long LAST_TIME = 0L;

    public String data ="";

    public AsynchronousAPI() throws UnsupportedEncodingException {
        
        ClassLoader classLoader = getClass().getClassLoader();
        File  wholeFile =new File (classLoader.getResource("payload.json").getFile());
        
        String fileName = wholeFile.toString();
        

        
            long timestamp = readLastModified(fileName);
            if (timestamp != LAST_TIME) {
                System.out.println("file updated:" + timestamp);

                //Get the token


                try{
                    Scanner readFile = new Scanner(wholeFile);
                    while(readFile.hasNextLine()){
                        data = data + readFile.nextLine();
                    }
                    readFile.close();

                    System.out.println(data);

                    TokenPost TokenPost = new TokenPost();
                    JSONObject tokenObject = TokenPost.object;
                    System.out.println("This is the token: " + tokenObject.getString("access_token"));
                    String token = tokenObject.getString("access_token");


                    HttpClient httpclient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost("http://webhook.site/39c36db6-a973-41eb-adaa-bb1440512281");

                    httpPost.setEntity(new StringEntity(data, "UTF-8"));
                    httpPost.setHeader("Authorization", "Bearer " + token);
                    httpPost.setHeader("partitionKey", "bc215df6-b784-4f68-ac0b-fe749cde4310");
                    httpPost.setHeader("Data-Owner-Id", "b74c3f9a-17ee-4943-81f2-ae22bb4f260d");
                    UUID uuid = UUID.randomUUID();
                    httpPost.setHeader("X-Request-Id", uuid.toString());


                    try{
                        HttpResponse response = httpclient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            InputStream instream = entity.getContent();
                            System.out.println("This is in the stream: " + instream);
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }catch(FileNotFoundException e){
                    System.out.println("An error occurred");
                    e.printStackTrace();
                }      

                LAST_TIME = timestamp;
                data = "";
            }
            
        
        
    }

    

    public static long readLastModified(String fileName) {
        File file = new File(fileName);
        return file.lastModified();
    }
}