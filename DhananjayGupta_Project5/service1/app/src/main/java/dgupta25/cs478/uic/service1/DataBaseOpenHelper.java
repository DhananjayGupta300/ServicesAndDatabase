package dgupta25.cs478.uic.service1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DataBaseOpenHelper extends SQLiteOpenHelper
{

    final static String date_id = "date_id";
    final static String year = "year";
    final static String month = "month";
    final static String TABLE_NAME = "dailycash";
    final static String cash_dayopen = "cash_dayopen";
    final static String cash_dayclose = "cash_dayclose";
    final static String _id = "_id";

    final static String date = "date";
    final static String day = "day";

    final static String[] columns = { _id, year, month, date, day, cash_dayopen, cash_dayclose};

    final private static String create_tbl = "create table dailycash ( " +
                                            _id + " integer PRIMARY KEY AUTOINCREMENT, " +
                                            date_id + " integer, " +
                                            year + " integer, " +
                                            month + " integer, " +
                                            date + " integer, " +
                                            day + " varchar, " +
                                            cash_dayopen + "  integer, " +
                                            cash_dayclose + " integer )";

    final private static String NAME = "db_dailycash1";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public DataBaseOpenHelper(Context context)
    {
        // Always call superclass's constructor
        super(context, NAME, null, VERSION);

        // Save the context that created DB in order to make calls on that context,
        // e.g., deleteDatabase() below.
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, getClass().getSimpleName() + "Countser -- 2" + create_tbl);

        db.execSQL(create_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // N/A
    }

    // Calls ContextWrapper.deleteDatabase() by way of inheritance
    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }
}
