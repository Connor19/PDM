package com.example.manwest.proyectov10;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by User on 26/04/2016.
 */
public class SigninActivity extends AsyncTask<String, Void, String> {
    private Context context;
    private boolean ingreso;
    private ProgressDialog pDialog;
    public SigninActivity(Context context, boolean ingreso){
        this.context=context;
        this.ingreso=ingreso;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Login ...");
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
            String link = "http://frzasd.esy.es/RecyclingBins/login.php?nombre="+username+"&clave="+password;
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
                    Log.d("result ****", String.valueOf((result)));
                    json.put("response_", new JSONObject(result));
                    instream.close();
                }
            }else {
                Log.d("result **** error", String.valueOf((0)));
            }
            return json.toString();
        } catch (Exception e){
            return new String("Exception: "+e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result){
        pDialog.dismiss();
        String json_str = String.valueOf(result);
        try {
            JSONObject my_obj = new JSONObject(json_str);
            String response = my_obj.getString("response_");
            JSONObject response_ = new JSONObject(response);
            String data = response_.getString("data");
            Log.d("result", String.valueOf(data));
            ingreso = false;
            if(data.equalsIgnoreCase("OK")){
                Toast.makeText(context,"Successful Login!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context,"Need a registration!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context,"Fail Connection!", Toast.LENGTH_SHORT).show();
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

