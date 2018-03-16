package nl.joyce.connectfour.tudelft.ide.software.connectfour;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View.OnDragListener;


import nl.joyce.connectfour.tudelft.ide.software.connectfour.control.OnControlListenerImpl;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.control.SensorControlListener;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.userinterface.MyShadowBuilder;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.model.*;

public class MainActivity extends AppCompatActivity{

    private TextView infoTextView;
    private void info(String text) { infoTextView.setText(text); }
    private TableLayout boardTableLayout;

    private Player player1=new Player("Xavier"),player2=new Player("Oliver");
    private Game game=new Game(player1,player2);

    private SensorManager mSensorManager;
    private SensorControlListener mSensorListener;
    private boolean isACCON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTextView=(TextView)findViewById(R.id.info_textview);
        boardTableLayout=(TableLayout)findViewById(R.id.board_tablelayout);

        // Added by Adrie
        colorButtons(Color.RED);
        findViewById(R.id.info_textview).setOnLongClickListener(longListen);

        // Attach listeners to top of columns
        findViewById(R.id.imageView1).setOnDragListener(dragListener);
        findViewById(R.id.imageView2).setOnDragListener(dragListener);
        findViewById(R.id.imageView3).setOnDragListener(dragListener);
        findViewById(R.id.imageView4).setOnDragListener(dragListener);
        findViewById(R.id.imageView5).setOnDragListener(dragListener);
        findViewById(R.id.imageView6).setOnDragListener(dragListener);
        findViewById(R.id.imageView7).setOnDragListener(dragListener);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new SensorControlListener();
        mSensorListener.setOnShakeListener(new OnControlListenerImpl(this));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void onPause() {
        Log.i("onPause", "is active");
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }
    protected void onResume() {
        Log.i("onResume", "app is running again");
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void newGameButtonClicked(View view) {
        recreate();
    }
    public boolean getACCON(){ return isACCON; }
    public Game getGame(){ return game; }

    private void hideAllButtons(){
        TableRow buttonTableRow=(TableRow)boardTableLayout.getChildAt(6); // get the button row
        int buttonColumn=7;
        while (--buttonColumn>=0) buttonTableRow.getChildAt(buttonColumn).setVisibility(View.INVISIBLE);
    }
    private void showDisc(int row, int column, int playerId) {
        TableRow tableRow=(TableRow)boardTableLayout.getChildAt(6-row); // MDH I hate this!
        ImageView imageView=(ImageView)tableRow.getChildAt(column-1);
        imageView.setBackgroundColor(playerId==1?Color.RED:Color.BLUE);
    }

    public void columnButtonClicked(View view) {
        Button columnButton = (Button) view;
        int column = Integer.parseInt(columnButton.getText().toString());
        playDisc(view, column);
    }

    public void playDisc(View view, int column) {
        Player currentPlayer=game.getCurrentPlayer();
        int row=game.move(column);
        if(row<=Game.NB_ROWS) { // a valid row (and column therefore)
            dropDisc(row,column,currentPlayer.getGameId());
        } else {
            info(currentPlayer.getName()+", you tried to drop a disc in a full column, please select another column.");
            view.setVisibility(View.INVISIBLE); // hide the button!!!
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ACC_ON:
                item.setChecked(!item.isChecked());
                if(isACCON)
                    isACCON=false;
                else
                    isACCON=true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public int getColor(){
        return game.getCurrentPlayer().getGameId()==1?Color.RED:Color.BLUE;
    }

    // Coloured buttons - an adaption of hideAllbuttons
    private void colorButtons(int c){
        TableRow buttonTableRow=(TableRow)boardTableLayout.getChildAt(6); // get the button row
        int buttonColumn=7;
        while (--buttonColumn>=0) buttonTableRow.getChildAt(buttonColumn).setBackgroundColor(c);
    }

    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            MyShadowBuilder dragShadow = new MyShadowBuilder(view, game.getCurrentPlayer().getGameId()==1?Color.RED:Color.BLUE);
            //ClipData clip = ClipData.newPlainText("", ""); //1st parameter of startdrag
            view.startDrag(null, dragShadow, view, 0);
            return false;
        }
    };

    OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            switch(dragEvent) {
                case DragEvent.ACTION_DROP:
                    Log.i("Drag Event","Dropped");
                    int column = Integer.parseInt(v.getTag().toString());
                    // obtain the column button in the given column to pass to playDisc()
                    playDisc(((TableRow)boardTableLayout.getChildAt(6)).getChildAt(column-1),column);
                    return true;
            }
            return true;
        }
    };

    // Animation
    final static int bounceTime=1000;
    private Animation moveAnimation(float fromX, float toX, float fromY, float toY) {
        Animation animation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,fromX,
                TranslateAnimation.RELATIVE_TO_SELF,toX,
                TranslateAnimation.RELATIVE_TO_SELF,fromY,
                TranslateAnimation.RELATIVE_TO_SELF,toY);
        animation.setDuration(bounceTime); // duration - a full second
        animation.setInterpolator(new BounceInterpolator());
        animation.setRepeatCount(0);
        return animation;

    }

    public Bitmap drawDisc(final int color) {
        Bitmap bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawCircle(24, 24, 24, paint);
        return bitmap;
    }

    private void dropDisc(final int row, final int column, final int playerId) {

        Log.i("MainActivity","Drop disc in column: "+column);

        float X=(column-1);
        float Y=(7-row);

        final ImageView imageView = (ImageView) findViewById(R.id.hiddenView); // hiddenView holds the played disc image.
        imageView.setX(32); // MDH this worked no idea why
        imageView.setVisibility(View.VISIBLE); // the dropping disc is invisible until needed...
        Bitmap bitmap = drawDisc(playerId==1?Color.RED:Color.BLUE);
        imageView.setImageBitmap(bitmap);
        imageView.startAnimation(moveAnimation(X, X, 1, Y));

        //delay the execution of showDisc until the dropping disc has fallen
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                imageView.setVisibility(View.INVISIBLE);
                showDisc(row,column, playerId);
                // the game might be over!!
                if (game.isGameOver()){
                    if (game.getWinnerId()==0)
                        info("Nobody won!");
                    else
                        info(game.getCurrentPlayer().getName()+" is the winner!");
                    hideAllButtons();
                } else // game not over yet
                    colorButtons(game.getCurrentPlayer().getGameId()==1?Color.RED:Color.BLUE);
            }
        }, bounceTime);
    }

}

