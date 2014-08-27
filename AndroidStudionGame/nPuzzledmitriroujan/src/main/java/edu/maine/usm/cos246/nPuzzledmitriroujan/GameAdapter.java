/* Dmitri Roujan <dmitri.roujan@maine.edu> */
package edu.maine.usm.cos246.nPuzzledmitriroujan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



/**
 * Created by SweetBro on 11/20/13.
 */

public class GameAdapter extends BaseAdapter {



    //Variable setup
    private Context context;
    private Bitmap[] values;
    private Bitmap[] winner;
    private int pad;


    //Construct the image parameter, get context, the array of images, and the height/width of them
    public GameAdapter(Context context, Bitmap[] _values) {
        this.context = context;
        this.values = _values;
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
        return values[i].getGenerationId();
    }

    public void removeItem(int i)
    {
        Bitmap.Config fig =Bitmap.Config.ALPHA_8;
        values[i] = Bitmap.createBitmap(values[0].getWidth()-pad, values[0].getHeight()-pad,fig);
    }
    public void setItem(ImageView view, int i)
    {
        values[i] = ((BitmapDrawable)view.getDrawable()).getBitmap();
    }

    public Bitmap[] getValues()
    {
        return values;
    }

    public Bitmap[] getWinner()
    {
        return winner;
    }

    public void setWinner()
    {
        Log.w("Derp", "Derp2");
        winner = values.clone();
    }

    public boolean checkWinner()
    {
        return compareBitArrays(values,winner);
    }

    public boolean compareBitArrays(Bitmap[] arr1, Bitmap[] arr2)
    {
        int comp = 0;
        if (arr1.length == arr2.length)
        {
            Log.w("Derp", "Same length");
            for (int i=0; i<arr1.length;i++)
            {
                if(arr1[i].sameAs(arr2[i]))
                {
                    comp++;
                }
            }
        }
        Log.w("Derp", ""+comp + " " +(arr1.length-1));
        if (comp == arr1.length-1)return true;
        else return false;
    }

    public void reverseValues()
    {
        Bitmap temp = values[14];
        values[14] = values[9];
        values[9] = temp;
        temp = values[12];
        values[12] = values[13];
        values[13] = temp;
        /*for (int j = 0; j < values.length / 2; j++) {
            Bitmap temp = values[j];
            values[j] = values[values.length - 1 - j];
            values[values.length - 1 - j] = temp;
        }*/
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(values[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pad = dpToPx(1)/2;
        imageView.setPadding(pad,pad,pad,pad);
        imageView.setLayoutParams(new GridView.LayoutParams(values[i].getWidth()-pad, values[i].getHeight()-pad));
        return imageView;
    }


}
