package com.example.conferencebookingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * @author Karin
 */
public class ImageDecoder extends AsyncTask<String, Void, Bitmap>{
    private static final String TAG = "ImageDecoder";


    private WeakReference<ImageView> imageView;


    public ImageDecoder(ImageView imageView) {
        super();
        this.imageView = new WeakReference<>(imageView);
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream((InputStream)new URL(strings[0]).getContent());
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(imageView.get() != null) {
            if (bitmap == null) {
                imageView.get().setImageResource(R.drawable.timetomeet_logo);
                imageView.get().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView.get().setImageBitmap(bitmap);
                imageView.get().setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }


    }

}
