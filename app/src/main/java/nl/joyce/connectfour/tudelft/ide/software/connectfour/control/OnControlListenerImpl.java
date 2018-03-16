package nl.joyce.connectfour.tudelft.ide.software.connectfour.control;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import nl.joyce.connectfour.tudelft.ide.software.connectfour.MainActivity;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.R;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.model.Game;

/**
 * Created by joyce on 24-2-2018.
 */

public class OnControlListenerImpl implements SensorControlListener.OnControlListener {
    private static final int ROLLING_SPEED = 10;
    private static final long TIME_TRESHOLD=700;
    private MainActivity mActivity;
    private float sensorX=0;
    private long time=System.currentTimeMillis();
    public OnControlListenerImpl(MainActivity mActivity){
        this.mActivity=mActivity;
    }

    @Override
    public void onShake() {
        if (mActivity.getACCON()) {
            Log.i("OnControlListenerImpl","Shake");
            mActivity.newGameButtonClicked(null);
        }
    }

    @Override
    public void onRoll(float x) {
        if (mActivity.getGame().isGameOver()) return; // not to roll the disc when the game is over
        if (mActivity.getACCON()) {
            Log.i("OnControlListenerImpl","Roll "+x);
            TableLayout tableLayout1= (TableLayout)
                    mActivity.findViewById(R.id.board_tablelayout);
            sensorX = sensorX - x * ROLLING_SPEED;
            if (sensorX < tableLayout1.getLeft())
                sensorX = tableLayout1.getLeft();
            if (sensorX > tableLayout1.getRight())
                sensorX = tableLayout1.getRight();

            Bitmap b=mActivity.drawDisc(mActivity.getColor());
            ImageView imageView = (ImageView) mActivity.findViewById(R.id.hiddenView); // hiddenView holds the played disc image.
            imageView.setVisibility(View.VISIBLE); // the dropping disc is invisible until needed...
            imageView.setImageBitmap(b);
            imageView.setX(sensorX);
        }
    }

    @Override
    public void onPitch(float z) {
        if (mActivity.getGame().isGameOver()) return; // ignore when game over
        long t=System.currentTimeMillis();
        if ((mActivity.getACCON()) && (t-time>TIME_TRESHOLD)) {
            Log.i("OnControlListenerImpl","Pitch "+z);
            int column = findColumnByPosition(sensorX);
            mActivity.playDisc(null, column);
            time=System.currentTimeMillis();
        }
    }

    private int findColumnByPosition(float x) {
        TableLayout tableLayout1= (TableLayout) mActivity.findViewById(R.id.board_tablelayout);
        int columnWidth =(tableLayout1.getRight()-tableLayout1.getLeft())/ Game.NB_COLUMNS;
        Log.i("tableWidth:", String.valueOf(columnWidth));
        int i=1;
        while (i<=Game.NB_COLUMNS){
            if (x<tableLayout1.getLeft()+i*columnWidth)
                return i;
            i++;
        }
        return i;
    }

}
