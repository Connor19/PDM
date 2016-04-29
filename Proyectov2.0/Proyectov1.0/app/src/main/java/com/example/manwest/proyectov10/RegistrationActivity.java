package com.example.manwest.proyectov10;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by User on 26/04/2016.
 */
public class RegistrationActivity extends AsyncTask<String, Void, String> {
    private Context context;
    private ProgressDialog pDialog;
    public RegistrationActivity(Context context){
        this.context=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registration ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    @Override
    protected String doInBackground(String... arg0){
        String stream = null;
        JSONObject json = new JSONObject();
        try{
            String username = (String) arg0[0];
            String password = (String) arg0[1];
            String link = "http://frzasd.esy.es/RecyclingBins/create.php?nombre="+username+"&clave="+password;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    json.put("response_", new JSONObject(result));
                    instream.close();
                }
            }else {

            }
            return String.valueOf(json);
        } catch (Exception e){
            return new String("Exception: "+e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result){
        pDialog.dismiss();
        String json_str = String.valueOf(result);
        try{
            JSONObject my_obj = new JSONObject(json_str);
            String response = my_obj.getString("response_");
            JSONObject response_ = new JSONObject(response);
            String data = response_.getString("data");
            Toast.makeText(context,"Client Create!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    private static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while((line = reader.readLine()) !=null){
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

