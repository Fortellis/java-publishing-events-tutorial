import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.*;

public class TokenPost extends HttpServlet{

    public HttpEntity entity;

    public JSONObject object;

    public TokenPost() throws UnsupportedEncodingException{

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://identity.fortellis.io/oauth2/aus1ni5i9n9WkzcYa2p7/v1/token");
        httpPost.setHeader("Accept", "application/json");

        httpPost.setHeader("Authorization", "Basic yourBase64Encoded{ClientID:ClientSecret}");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("grant_type","client_credentials"));
        nameValuePairs.add(new BasicNameValuePair("scope","anonymous"));

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        try{
            HttpResponse response = httpclient.execute(httpPost);
            entity = response.getEntity();
            if (entity != null) {
                object = new JSONObject(EntityUtils.toString(entity));
                System.out.println("This is your token: " + object.getString("access_token"));

            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}