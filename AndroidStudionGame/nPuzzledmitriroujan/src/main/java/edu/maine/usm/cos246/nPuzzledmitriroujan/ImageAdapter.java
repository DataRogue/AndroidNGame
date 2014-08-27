/* Dmitri Roujan <dmitri.roujan@maine.edu> */
package edu.maine.usm.cos246.nPuzzledmitriroujan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by SweetBro on 11/20/13.
 */
public class ImageAdapter extends BaseAdapter {



    //Variable setup
    private Context context;
    private int[] values;
    private int h;
    private int w;

    //Construct the image parameter, get context, the array of images, and the height/width of them
    public ImageAdapter(Context context, int[] _values, int _h, int _w) {
        this.context = context;
        this.values = _values;
        h = _h;
        w = _w;
    }

    //Convert Dp to pixels for modularity
    private int dpToPx(int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return values[i];
    }

    //Draws the image all pretty and stuff
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(values[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int pad = dpToPx(10);
        imageView.setPadding(pad,pad,pad,pad);
        imageView.setLayoutParams(new GridView.LayoutParams(dpToPx(h), dpToPx(w)));
        return imageView;
    }


}
