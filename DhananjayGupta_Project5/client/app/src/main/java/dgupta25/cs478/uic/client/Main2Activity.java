package dgupta25.cs478.uic.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends Activity
{
    public static ListView listView;
    ArrayList<Integer> dcash = new ArrayList<>();
    ArrayList<Integer> dmonth = new ArrayList<>();
    ArrayList<String> ddayofweek = new ArrayList<>();
    ArrayList<Integer> dyear = new ArrayList<>();
    ArrayList<Integer> dday = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        Parcelable[] d =  intent.getParcelableArrayExtra("Data");
        for( Parcelable d2:d ) {
            DailyCash d1 = (DailyCash) d2;
            dyear.add(d1.mYear);
            dmonth.add(d1.mMonth);
            dday.add(d1.mDay);
            dcash.add(d1.mCash);
            ddayofweek.add(d1.mDayOfWeek);
        }

        listView = findViewById(R.id.listValues);
        myAdapter custom = new myAdapter();
        listView.setAdapter(custom);
    }
    class myAdapter extends BaseAdapter
    {

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public int getCount()
        {
            return dyear.size();
        }



        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View adapV = getLayoutInflater().inflate(R.layout.list_layout, null);
            TextView month = adapV.findViewById(R.id.month);
            TextView year = adapV.findViewById(R.id.year);
            TextView dayofweek = adapV.findViewById(R.id.dayofweek);
            TextView date = adapV.findViewById(R.id.date);

            TextView cash = adapV.findViewById(R.id.cash);

            date.setText(String.valueOf(dday.get(position)));
            month.setText(String.valueOf(dmonth.get(position)));
            year.setText(String.valueOf(dyear.get(position)));
            dayofweek.setText(ddayofweek.get(position));
            cash.setText(String.valueOf(dcash.get(position)));
            return adapV;
        }
    }
}
