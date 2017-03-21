         package com.lognsys.toodit.adapter;

         import android.app.Activity;
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
         import android.support.annotation.IdRes;
         import android.support.annotation.LayoutRes;
         import android.support.annotation.NonNull;
         import android.support.v7.app.AppCompatActivity;
         import android.support.v7.widget.RecyclerView;
         import android.text.Html;
         import android.util.DisplayMetrics;
         import android.util.Log;
         import android.view.LayoutInflater;
         import android.view.View;
         import android.view.ViewGroup;
         import android.widget.ArrayAdapter;
         import android.widget.BaseAdapter;
         import android.widget.GridView;
         import android.widget.ImageView;
         import android.widget.TextView;

         import com.lognsys.toodit.R;
         import com.lognsys.toodit.fragment.CartFragment;
         import com.lognsys.toodit.fragment.HomeFragment;
         import com.lognsys.toodit.fragment.ListMallOutlets;
         import com.lognsys.toodit.util.CallAPI;
         import com.squareup.picasso.Picasso;

         import java.io.IOException;
         import java.io.InputStream;
         import java.net.HttpURLConnection;
         import java.net.URL;
         import java.util.ArrayList;

/**
 * Created by admin on 03-03-2017.
 */

public class ImageAdapterOutlates extends ArrayAdapter<ListMallOutlets> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ListMallOutlets> items = new ArrayList<ListMallOutlets>();
    public ImageAdapterOutlates(Context mContext, int layoutResourceId, ArrayList<ListMallOutlets> items) {
        super(mContext, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.items = items;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<ListMallOutlets> mGridData) {
        this.items = items;
        notifyDataSetChanged();
    }
    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageview = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        ListMallOutlets item = items.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getOutlet_name()));
        Picasso.with(mContext).load(item.getOutlet_image()).into(holder.imageview);
        return row;
    }

class ViewHolder{
    ImageView imageview;
    String imageviewstring;
    Bitmap bitmap;
    TextView titleTextView;
    int position;
}

    public class ImageLoadTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
       protected ViewHolder doInBackground(ViewHolder... params) {

            //load image directly

            ViewHolder viewHolder = params[0];

            try {
                Log.d("error", "adapter doInBackground Image viewHolder.imageviewstring "+viewHolder.imageviewstring);

                URL imageURL = new URL(viewHolder.imageviewstring);

                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());

            } catch (IOException e) {

                Log.e("error", "adapter Downloading Image Failed");

                viewHolder.bitmap = null;

            }

            return viewHolder;

        }
        @Override

        protected void onPostExecute(ViewHolder result) {
            Log.d("error", "adapter onPostExecute Image result "+result);

            if (result.bitmap == null) {

                result.imageview.setImageResource(R.drawable.ic_launcher_toodit);

            } else {

                result.imageview.setImageBitmap(result.bitmap);

            }

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
