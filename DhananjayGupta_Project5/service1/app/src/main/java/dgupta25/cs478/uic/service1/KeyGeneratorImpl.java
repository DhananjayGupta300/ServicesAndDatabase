package dgupta25.cs478.uic.service1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import dgupta25.cs478.uic.servicecommon.KeyGenerator;

import static android.content.ContentValues.TAG;

public class KeyGeneratorImpl extends Service
{


	
	private final static Set<UUID	> mIDs = new HashSet<UUID>();
	DataBaseOpenHelper myDatabaseHelper;

	final Context c = this;

	SQLiteDatabase db;

	private final KeyGenerator.Stub myBinder = new KeyGenerator.Stub()
	{

		public DailyCash[] dailyCash(String year, String month, String date, String noofdays1){
			int noofdays = Integer.valueOf(noofdays1);
			String starty = year;
			String startm = Integer.valueOf(month) > 9 ? month : '0'+month;
			String startd = Integer.valueOf(date) > 9 ? date : '0'+date;
			String startdateid = starty + startm + startd;

			String sql1 = "select _id from " + DataBaseOpenHelper.TABLE_NAME + " where date_id >= "+ Integer.valueOf(startdateid);
			Log.i(TAG, getClass().getSimpleName() + "check query" + sql1);
			Cursor c1 = db.rawQuery(sql1, null);
			Log.i(TAG, getClass().getSimpleName() + "retuyt" + c1.moveToFirst());
			int startid = Integer.valueOf(c1.getString(c1.getColumnIndex("_id")));
			int endid = startid + noofdays-1;



			Cursor c = db.rawQuery("select _id, year, month, date, day, cash_dayopen, cash_dayclose from " + DataBaseOpenHelper.TABLE_NAME +
					" where _id between " + startid + " and " + endid , null );
			int a = c.getCount();
			DailyCash[] d = new DailyCash[a];
			c.moveToPosition(-1);
			int i = 0;
			while(c.moveToNext())
			{
				int date1 = Integer.valueOf(c.getString(c.getColumnIndex("date")));
				int month1 = Integer.valueOf(c.getString(c.getColumnIndex("month")));
				int year1 = Integer.valueOf(c.getString(c.getColumnIndex("year")));
				int cash1 = Integer.valueOf(c.getString(c.getColumnIndex("cash_dayclose")));
				String day1 = c.getString(c.getColumnIndex("day"));
				d[i] = new DailyCash(date1, month1, year1, cash1, day1);
				i+=1;
			}
			for(DailyCash q1:d )
			{
					Log.i(TAG, getClass().getSimpleName() + "check data" + q1.mDay + q1.mMonth + q1.mYear + q1.mDayOfWeek + q1.mCash);
			}
			return d;

		}
        public boolean createDataBase()
		{

			myDatabaseHelper = new DataBaseOpenHelper(c);
			db = myDatabaseHelper.getWritableDatabase();
			clearAll();
			// Insert records
			insertArtists();
			Log.i(TAG, getClass().getSimpleName() + "database is created");
			//DailyCash[] q=dailyCash(String.valueOf(2017), String.valueOf(2), String.valueOf(1), String.valueOf(20));
			//for(DailyCash q1:q ) {
			//	Log.i(TAG, getClass().getSimpleName() + "check data" + q1.mDay + q1.mMonth + q1.mYear + q1.mDayOfWeek + q1.mCash);
			//}
			return true;
        }
		// Delete entire table
		private void clearAll()
		{
			// Call SQLiteDatabase.delete() -- null arg deletes all rows in arg table.
			myDatabaseHelper.getWritableDatabase().delete(DataBaseOpenHelper.TABLE_NAME, null, null);
		}
		// Insert several artist records
		private void insertArtists()
		{

//        ContentValues values = new ContentValues();

			List<String> mLines = new ArrayList<>();
			try
			{
				//FileReader fileReader =
				//		new FileReader("treasury-io-final.txt");
				//InputStream is = File.;
				String file = "res/raw/treasury_io_final.txt"; // res/raw/test.txt also work.
				InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
				//);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;

				while ((line = reader.readLine()) != null) {
					Log.i(TAG, getClass().getSimpleName() + "Counter -- 3" + line);
					mLines.add(line);
					List<String> valueList = Arrays.asList(line.split(","));
					String year = valueList.get(0);
					String month = Integer.valueOf(valueList.get(1)) > 9 ? valueList.get(1) : '0' + valueList.get(1);
					String day = Integer.valueOf(valueList.get(2)) > 9 ? valueList.get(2) : '0' + valueList.get(2);
					String date_id = year + month + day;
					Log.i(TAG, getClass().getSimpleName() + "Counter -- 4" + date_id);
					String sql2  = "insert into " + DataBaseOpenHelper.TABLE_NAME + "( date_id, year, month, date, day, cash_dayopen, cash_dayclose) " +
							" values("+ date_id + ", " + valueList.get(0) +", "+ valueList.get(1) +","+ valueList.get(2) +",'"
							+ valueList.get(3) +"',"+ valueList.get(4) +","+ valueList.get(5) +");";
					Log.i(TAG, getClass().getSimpleName() + "Counter -- 5" + sql2);
					db.execSQL("insert into " + DataBaseOpenHelper.TABLE_NAME + "( date_id, year, month, date, day, cash_dayopen, cash_dayclose) " +
							" values("+ date_id + ", " + valueList.get(0) +", "+ valueList.get(1) +","+ valueList.get(2) +",'"
							+ valueList.get(3) +"',"+ valueList.get(4) +","+ valueList.get(5) +");" );
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Implement the remote method
		public String getKey()
		{
		
			UUID id;
			
			// Acquire lock to ensure exclusive access to mIDs 
			// Then examine and modify mIDs
			
			synchronized (mIDs)
			{
				
				do
					{
				
					id = UUID.randomUUID();
				
				}
				while (mIDs.contains(id));

				mIDs.add(id);
			}
			return id.toString();
		}
	};

	// Return the Stub defined above
	@Override
	public IBinder onBind(Intent intent)
	{
		return myBinder;
	}
}
