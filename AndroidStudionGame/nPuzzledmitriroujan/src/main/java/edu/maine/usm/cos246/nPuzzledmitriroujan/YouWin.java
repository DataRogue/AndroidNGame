/* Dmitri Roujan <dmitri.roujan@maine.edu> */
package edu.maine.usm.cos246.nPuzzledmitriroujan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class YouWin extends ActionBarActivity {

    int highScore;
    int currentScore;
    Context context = this;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youwin_main);

        //Load intent
        Intent selfIntent = getIntent();
        currentScore = selfIntent.getIntExtra("CURRENT_SCORE",0);
        //Load data
        sharedPref = context.getSharedPreferences("HollaHollaGetPreferenceDolla", Context.MODE_PRIVATE);

        //Setup highscore
        highScore = sharedPref.getInt("HIGH_SCORE", 999999);

        //If new highscore is lower than the previous one, it becomes new high score
        if(currentScore<=highScore)
        {
            editor = sharedPref.edit();
            highScore=currentScore;
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();
        }
        //Prints highscore
        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText("High Score: " + highScore + " Your score: " + currentScore);
        //Sends back tos tart
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newInt = new Intent(context, ImageSelection.class);
                finish();
                startActivity(newInt);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.you_win, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
    }
    //Pressing back would cause a weird error that I was in no mood to debug, so I just disabled it
    @Override
    public void onBackPressed() {
    }

}
