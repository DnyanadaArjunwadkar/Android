package com.example.dny.weather2;


//Watch the video here @https://www.youtube.com/watch?v=FGyaJ5MH3Hg.THANK YOU :)


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Weather extends AppCompatActivity {
    public String readJSONFeed(String URL1) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp = URL1;
        URL yahoo = null;
        try {
            yahoo = new URL(temp);
            URLConnection yc = yahoo.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                stringBuilder.append(inputLine);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuilder.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText et = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // accept City name from User
                String city = String.valueOf(et.getText());
                // call Weather API
                String URL1 = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=4f6f268a496ed32149b37570e57bef91";

                new ReadPlacesFeedTask().execute(URL1);

            }
        });

    }

    private class ReadPlacesFeedTask extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls) {

            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result)
        {
            try {
                Log.e("Ketan:",result);
                JSONObject jobj =new JSONObject(result);
                JSONArray arr2 = jobj.getJSONArray("weather");
                String icon_url="";
                for (int i = 0; i < arr2.length(); i++)
                {
                    icon_url = arr2.getJSONObject(i).getString("icon");
                    Log.e("icon_url:",icon_url);
                }

                String ImgUrl="http://openweathermap.org/img/w/"+icon_url+".png";
                new DownloadImageTask().execute(ImgUrl);



                String temp=jobj.getJSONObject("main").getString("temp");

                float temp1=Float.valueOf(temp);
                float temp11= (float) (temp1-272.15);

                Log.e("Temp:", String.valueOf(temp11));


                String temp_min=jobj.getJSONObject("main").getString("temp_min");

                float temp_min_int=Float.valueOf(temp_min);
                float min_temp_final= (float) (temp_min_int-272.15);
                Log.e("Temp_Minimum:", String.valueOf(min_temp_final));
                TextView textView3=(TextView)findViewById(R.id.textView3);
                final String DEGREE  = "\u00b0";

                textView3.setText("Min:"+String.valueOf(min_temp_final)+DEGREE+"C");



                String temp_max=jobj.getJSONObject("main").getString("temp_max");

                float temp_max_int=Float.valueOf(temp_max);
                float max_temp_final= (float) (temp_max_int-272.15);

                Log.e("Temp_Maximum:", String.valueOf(max_temp_final));
            //    EditText editText3=(EditText)findViewById(R.id.editText3);
             //   editText3.setText(String.valueOf(max_temp_final));
                TextView textView4=(TextView)findViewById(R.id.textView4);

                textView4.setText("Max:"+String.valueOf(max_temp_final)+DEGREE+"C");

                JSONArray arr = jobj.getJSONArray("weather");
                for (int i = 0; i < arr.length(); i++)
                {
                    String desc = arr.getJSONObject(i).getString("description");
                    Log.e("descROCK",desc);
                  String s1=desc;
                    char[] chars = s1.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    s1= new String(chars);

                    TextView textView2=(TextView)findViewById(R.id.textView2);
                    textView2.setText(s1);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_weather, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            try {
                return loadImageFromNetwork(urls[0]);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {

            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageBitmap(result);

        }

        private Bitmap loadImageFromNetwork(String url)
                throws MalformedURLException, IOException {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                    url).getContent());
            return bitmap;
        }

    }
    }

