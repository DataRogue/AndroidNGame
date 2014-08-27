/* Dmitri Roujan <dmitri.roujan@maine.edu> */
package edu.maine.usm.cos246.nPuzzledmitriroujan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class ImageSelection extends ActionBarActivity {

    int size = 10; // Amount of pictures to import


    int[] drawArray = new int[size];
    int difficultyLevel = 0;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Load resource data
       Resources res = this.getResources();
        int i = 0;
        final Context context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_main);

        //Create save data opertaor
        sharedPref = context.getSharedPreferences("HollaHollaGetPreferenceDolla", Context.MODE_PRIVATE);


        //Draw pretty pictures
        do
        {
            int ident = res.getIdentifier("puzzle_"+i, "drawable", getPackageName());
            drawArray[i] = ident;
            i++;

        } while(i != 0 && i < size );
        //Get grid from layout
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        //Load adapter
        ImageAdapter adapter = new ImageAdapter(context, drawArray, 80, 80);
        //Construct grid with new adapter
        gridView.setAdapter(adapter);
        //Add onClick functionality
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
            {

                setDifficultyLevel(context, pos);

            }
        }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //No longer relevant
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDifficultyLevel(final Context context, final int pos)
    {

        //Check if it can load the data
        if(-1 == sharedPref.getInt("DIFFICULTY",-1))
        {
            //If it can't it draws the user dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageSelection.this);
        builder.setTitle("Choose Difficulty:");
        builder.setItems(new String[]{"Easy","Medium","Hard"}, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(context, GamePlay.class);
            difficultyLevel = which;
            intent.putExtra("GAME_DIFFICULTY", difficultyLevel);
            intent.putExtra("IMAGE_TRANSFER", drawArray[pos]);
            setStandardDifficulty(which);
            finish();
            startActivity(intent);
        }


    });
        builder.show();
    }
        //Otherwise it auto remebers the difficulty
        else
        {
            Intent intent = new Intent(context, GamePlay.class);
            difficultyLevel = sharedPref.getInt("DIFFICULTY",-1);
            intent.putExtra("GAME_DIFFICULTY", difficultyLevel);
            intent.putExtra("IMAGE_TRANSFER", drawArray[pos]);
            finish();
            startActivity(intent);
        }
    }
    //Sets user preference
    public void setStandardDifficulty(int difficulty)
    {
        editor = sharedPref.edit();
        editor.putInt("DIFFICULTY", difficulty);
        editor.commit();
    }




    /**
     * A placeholder fragment containing a simple view.
     * Also no longer relvant
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
