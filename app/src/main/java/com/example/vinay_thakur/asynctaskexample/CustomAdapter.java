package com.example.vinay_thakur.asynctaskexample;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by vinay_thakur on 7/4/2018.
 */
public class CustomAdapter extends ArrayAdapter<ArrayList<HashMap<String,String>>> {

    ImageView imageView;
    Context activity;
    LayoutInflater inflater;
    ArrayList<String> arrayList;
    ArrayList<String> imagelist;
    private final LruCache<String, Bitmap> cache;
    public CustomAdapter(Context activity, ArrayList<String> arrayList,ArrayList imagelist)
    {
        super(activity,R.layout.layout_item);
        //super(activity, R.layout.layout_item);
        Log.d("checkhere31","hey"+arrayList.size());
        this.arrayList=arrayList;
        this.imagelist=imagelist;
        this.activity=  activity;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() / 1024;
            }
        };
    }
    @Override
    public int getCount() {
        Log.d("checkher",""+arrayList.size());
        return arrayList.size();
    }
    @Override
    public ArrayList<HashMap<String, String>> getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", "" + src);
            URL url = new URL(src);
            if (url != null) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Log.e("Bitmap", "returned");
                return myBitmap;
            }
            } catch(IOException e){
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
                return null;
            }
            return null;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        inflater= (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v=view;
        Log.d("checkhere321",""+v);
        if(view==null) {
            Log.d("checkhere321",""+v);
            v=inflater.inflate(R.layout.layout_item,null,true);
        }
        TextView textView=(TextView)v.findViewById(R.id.title);
        imageView=(ImageView)v.findViewById(R.id.image);

        Log.d("checkher1",""+arrayList.get(i));
        textView.setText(arrayList.get(i));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String src=imagelist.get(i);
                int id=124+i;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String imageKey = String.valueOf(id);
                final Bitmap bitmap = getBitmapFromMemCache(imageKey);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    //imageView.setImageResource(R.drawable.image_placeholder);
                    BitmapWorkerTask task = new BitmapWorkerTask(imageView,src,String.valueOf(id));
                    task.execute(id);

                }
            }
        },0);

        Log.d("checkhere321",""+v);
        return v;
    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            cache.put(key, bitmap);
        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        String src1;
        ImageView imageView;
        String id;
        public BitmapWorkerTask(ImageView imageView,String src,String id) {
            src1 = src;
            this.imageView=imageView;
            this.id=id;
        }
        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            URL url = null;
            try {
                url = new URL(src1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Bitmap bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
               // imageView.setImageBitmap(bitmap);
                addBitmapToMemoryCache(id,bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}


