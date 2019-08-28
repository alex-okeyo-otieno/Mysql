package com.example.okeyobrains.loginapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.AsyncTask;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Okeyo Brains on 8/25/2019.
 */

public class BackgroundTask extends AsyncTask<String,Void,String>  {
    String register_url="http://10.0.2.2/loginapp/register.php";
    String login_url="http://10.0.2.2/loginapp/login.php";
    Context ctx;
    Activity activity;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    public BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
        activity=(Activity) ctx;
    }
    @Override
    protected void onPreExecute() {
        builder=new AlertDialog.Builder(activity);
        progressDialog=new ProgressDialog(ctx);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting to the server...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            progressDialog.dismiss();
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("server_response");
            JSONObject jo=jsonArray.getJSONObject(0);
            String code=jo.getString("code");
            String message=jo.getString("message");
            if (code.equals("Registration_true"))
            {
                ShowDialog("Registration Success",code,message);
            }
            else if (code.equals("Registration_false"))
            {
                ShowDialog("Registration Failed",code,message);
            }
            else if (code.equals("login_true"))
            {
                Intent intent =new Intent(activity,HomeActivity.class);
                intent.putExtra("message",message);
                activity.startActivity(intent);
            }
            else if (code.equals("login_false"))
            {
                ShowDialog("Login Error",code,message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void ShowDialog(String Title,String code,String message)
    {
            builder.setTitle(Title);

        if (code.equals("Registration_true")||code.equals("Registration_false"))
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    activity.finish();
                }
            });


        }
     else if (code.equals("login_true"))
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    EditText Pass,email;
                    email=activity.findViewById(R.id.email);
                    Pass=activity.findViewById(R.id.password);
                    email.setText("");
                    Pass.setText("");

                    dialogInterface.dismiss();
                    activity.finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    protected String doInBackground(String... params) {

       String method=params[0];
       if (method.equals("register"))
       {
           try {
               URL url=new URL(register_url);
               HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoInput(true);
               httpURLConnection.setDoOutput(true);
               OutputStream outputStream=httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
               String name=params[1];
               String email=params[2];
               String password=params[3];
               String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

               bufferedWriter.write(data);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();

               InputStream inputStream=httpURLConnection.getInputStream();
               BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
               StringBuilder stringBuilder=new StringBuilder();
               String line="";
               while((line=bufferedReader.readLine())!=null)
               {
                   stringBuilder.append(line+"\n");
               }
               httpURLConnection.disconnect();
               Thread.sleep(5000);
        return stringBuilder.toString().trim();
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       else if (method.equals("login"))
       {
           try {
               URL url=new URL(login_url);

               HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoInput(true);
               httpURLConnection.setDoOutput(true);
               OutputStream outputStream=httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

               String email=params[1];
               String password=params[2];
               String data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                       URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

               bufferedWriter.write(data);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();

               InputStream inputStream=httpURLConnection.getInputStream();
               BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
               StringBuilder stringBuilder=new StringBuilder();
               String line="";
               while((line=bufferedReader.readLine())!=null)
               {
                   stringBuilder.append(line+"\n");
               }
               httpURLConnection.disconnect();
               Thread.sleep(5000);
               return stringBuilder.toString().trim();


           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           } catch (ProtocolException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

       }


        return null;
    }
}
