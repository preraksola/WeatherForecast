package com.prerak.weather.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.view.ForecastItemView;
import com.prerak.weather.android.R;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prerak on 22/04/2015.
 */
public class ForecastItemCustomAdapter extends ArrayAdapter<ForecastItemView> {

    Context mContext;
    int layoutResourceId;
    ForecastItemView data[] = null;
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    ImageView iv;

    public ForecastItemCustomAdapter(Context mContext, int layoutResourceId, ForecastItemView[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        SharedPreferences preferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView day = (TextView) listItem.findViewById(R.id.day);
        TextView avgTemp = (TextView) listItem.findViewById(R.id.avgTemp);
        TextView desc = (TextView) listItem.findViewById(R.id.weatherDesc);
        iv = (ImageView) listItem.findViewById(R.id.forecastImage);

        ForecastItemView folder = data[position];

        String img_url = IMG_URL+folder.getImageId()+".png";

        day.setText(folder.getDay());
        String unit = preferences.getString(MainActivity.TEMP_UNIT, null);
        if(unit.equals("c"))
            avgTemp.setText(folder.getTemp()+""+(char) 0x00B0+unit);

        else
            avgTemp.setText(folder.getTemp()+""+(char) 0x00B0+unit);

        desc.setText(folder.getDesc());

        imageFetch imf = new imageFetch(iv,this);
        imf.execute(img_url);

        return listItem;
    }

    public class imageFetch extends AsyncTask<String, Void, Bitmap>
    {

        private final WeakReference<ImageView> imageViewReference;
        ForecastItemCustomAdapter fica;


        public imageFetch(ImageView imageView, ForecastItemCustomAdapter fica) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.fica = fica;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloading", "Error downloading image from API");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null)
            {
                ImageView imageView = imageViewReference.get();

                if (imageView != null)
                {
                    if (bitmap != null) {
                        LayerDrawable layerDrawable;
                        Drawable icon = new BitmapDrawable(fica.getContext().getResources(),bitmap);
                        Drawable[] layers = new Drawable[2];
                        layers[0] = fica.getContext().getResources().getDrawable(R.mipmap.ic_forecast_background);
                        layers[1] = icon;
                        layerDrawable = new LayerDrawable(layers);

                        imageView.setImageDrawable(layerDrawable);
                        //imageView.setImageBitmap(b);
                    }

                    else
                    {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.mipmap.ic_forecast_background);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }
}
