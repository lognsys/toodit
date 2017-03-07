package com.lognsys.toodit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lognsys.toodit.R;
import com.lognsys.toodit.fragment.CartFragment;
import com.lognsys.toodit.fragment.ListMallOutlets;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 03-03-2017.
 */

public class ImageAdapterOutlates extends BaseAdapter {
    private Context mContext;
    private ArrayList<ListMallOutlets> imageUrl=new ArrayList<>();

    public ImageAdapterOutlates(Context c , ArrayList<ListMallOutlets> imageUrl) {
        mContext = c;
        this.imageUrl=imageUrl;
    }

    public int getCount() {

        return imageUrl.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        de.hdodenhof.circleimageview.CircleImageView imageView;
        if (convertView == null) {

            // if it's not recycled, initialize some attributes

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            imageView = new de.hdodenhof.circleimageview.CircleImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 4, screenWidth / 4));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (de.hdodenhof.circleimageview.CircleImageView) convertView;
        }


      new ImageLoadTask(imageUrl.get(position).getOutlet_image().toString(),imageView).execute();

        return imageView;
    }



    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bitmap circularBitmap = new ImageConverter().getRoundedCornerBitmap(myBitmap, 80);

                return circularBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
    public class ImageConverter {

        public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }
}
