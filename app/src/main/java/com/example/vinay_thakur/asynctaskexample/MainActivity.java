package com.example.vinay_thakur.asynctaskexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String url;
    ListView listView;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> imagelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listview);
        HashMap <String ,String> map=new HashMap<>();
        map.put("title","hello");
        url="https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=5f1189716e524dd0aac12d69d7e322d4";
        new FetchData().execute();

    }
    public class FetchData extends AsyncTask<Void,Void ,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler=new HttpHandler();
            HashMap<String,String> map=new HashMap<String, String>();
            String json=httpHandler.makeServiceCall(url);
            try {
                JSONObject jsonObject=new JSONObject(json);
                JSONArray jsonArray=jsonObject.getJSONArray("articles");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    map.put("title",jsonObject1.getString("title"));
                    map.put("urlToImage",jsonObject1.getString("urlToImage"));
                    arrayList.add(jsonObject1.getString("title"));
                    imagelist.add(jsonObject1.getString("urlToImage"));
                    Log.d("cheeckhere",map.get("title")+""+map.get("urlToImage"));
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override

        protected  void onPostExecute(Void result)
        {
            CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),arrayList,imagelist);
            listView.setAdapter(customAdapter);
        }
    }
}
