/* Dmitri Roujan <dmitri.roujan@maine.edu> */
package edu.maine.usm.cos246.nPuzzledmitriroujan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;



public class GamePlay extends ActionBarActivity {

    int empty;
    int boardSize = 4;
    Context context = this;
    GameAdapter adapter;
    int numberOfMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Draw layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_main);

        //Fetch intents
        Intent intent = getIntent();
        final int imageId = intent.getIntExtra("IMAGE_TRANSFER", 0);
        //Setup board
        boardSize = 4+intent.getIntExtra("GAME_DIFFICULTY", 0);
        final FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
        final GridView mahView = (GridView)findViewById(R.id.gridView);
        ViewTreeObserver vto = mahView.getViewTreeObserver();

        //When everything is loaded
        if (vto != null) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //Setup board according to the size of the device
                    mahView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width  = mahView.getWidth();
                    int height = mahView.getHeight();
                    mahView.setNumColumns(boardSize);
                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                            imageId);
                    final Bitmap resized = ResizeAndRotate(icon, height, width);
                    final ImageView solution = new ImageView(context);
                    solution.setImageBitmap(resized);
                    //Show preview
                    frame.addView(solution);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 3s = 3000ms

                            //Remove preview
                            frame.removeView(solution);
                            Bitmap bitArray[] = SliceAndDice(resized,boardSize,boardSize);
                            adapter = new GameAdapter(context, bitArray);
                            mahView.setAdapter(adapter);
                            empty = (boardSize*boardSize)-1;


                            //Setup puzzle for game
                            adapter.setWinner();
                            adapter.reverseValues();
                            adapter.removeItem(empty);
                            adapter.notifyDataSetChanged();

                            //On click logic
                            mahView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    if(validSquare(position))
                                    {//If valid square adjust visuals and data
                                        numberOfMoves++;
                                        ImageView temp = (ImageView)v;
                                        adapter.removeItem(position);
                                        adapter.setItem(temp, empty);
                                        empty = position;
                                        adapter.notifyDataSetChanged();
                                        //Find out why not triggering
                                        Bitmap[] win = adapter.getWinner();
                                        Bitmap[] val = adapter.getValues();


                                        /*

                                        At this point I would save the level data and all that jazz
                                        but because it doesn't take arrays or bitmaps for that matter
                                        this would require me to save it as a string and then parse OR
                                        to extend the class to do to accept it. Both of which are time
                                        consuming and I no longer have energy for.

                                         */

                                        //If the player has won run the on win function
                                        if(adapter.checkWinner())onWin();
                                    }


                                }
                            });



                        }
                    }, 3000);
            }
            });
        }
    }
    //Checks if the square the player click on is adjacent to the empty one
    public boolean validSquare(int intended)
    {
        if(intended+1 == empty || intended-1==empty ||intended+boardSize==empty || intended-boardSize == empty)
            return true;
        else return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //Kills this activity and preforms all relevant actions
            case R.id.resetGame:
                finish();
                startActivity(getIntent());
                return true;
            case R.id.selectDifficulty:
                AlertDialog.Builder builder = new AlertDialog.Builder(GamePlay.this);
                builder.setTitle("Choose Difficulty:");
                builder.setItems(new String[]{"Easy","Medium","Hard"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent temp = getIntent();
                        temp.putExtra("GAME_DIFFICULTY", which);
                        finish();
                        startActivity(temp);
                    }
                });
                builder.show();
                return true;
            case R.id.selectImage:
                Intent intent = new Intent(context, ImageSelection.class);
                finish();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //If player has won, it moves on to the next activity
    public void onWin()
    {
        Intent tempInt = new Intent(context, YouWin.class);
        tempInt.putExtra("CURRENT_SCORE", numberOfMoves);
        finish();
        startActivity(tempInt);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.gameplay_fragment_main, container, false);
            return rootView;
        }
    }

    public Bitmap ResizeAndRotate(Bitmap _iv, int _h, int _w)
    {
        Bitmap bit = _iv;

        int h = _h;
        int w = _w;
        double aspect = ((double)bit.getWidth())/((double)bit.getHeight());
        double deviceAspect = (double)_w/(double)_h;
        boolean wide = bit.getWidth() < bit.getHeight();
        //Adjust for different aspect ratios
            if(aspect > deviceAspect)
                {
                    w = _w;
                    h = (int) Math.round(((double)w)/aspect);
                }
                else
                {
                    h = _h;
                    w = (int) Math.round(((double)h)*aspect);
                }
        //Get aspect ratio
        bit = bit.createScaledBitmap(bit, w ,h, false);
        return bit;
    }

    //Cuts up a bitmap into the specified array
    public Bitmap[] SliceAndDice(Bitmap _bit, int xSize, int ySize)
    {
        Bitmap[] returnArray = new Bitmap[xSize*ySize];
        int length = Math.round(_bit.getWidth()/xSize);
        int height = Math.round(_bit.getHeight()/ySize);
        for (int i = 0; i <xSize;i++)
        {
            for(int j = 0; j <ySize; j++)
            {
                returnArray[(j*ySize)+i] = _bit.createBitmap(_bit,i*length,j*height,length,height);
            }
        }
        return returnArray;

    }



}
