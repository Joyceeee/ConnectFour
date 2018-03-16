package nl.joyce.connectfour.tudelft.ide.software.connectfour.exception;

/**
 * Created by joyce on 24-2-2018.
 */

public class ColumnRangeException extends Exception {
    @Override
    public String getMessage() {
        return "Not in the column's range.";
    }

}
