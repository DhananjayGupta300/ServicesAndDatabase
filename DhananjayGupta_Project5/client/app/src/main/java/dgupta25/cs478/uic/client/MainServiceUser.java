// declare the package name

package dgupta25.cs478.uic.client;

//import the required packages
import android.app.Activity ;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import dgupta25.cs478.uic.servicecommon.KeyGenerator;

import static android.text.TextUtils.isDigitsOnly;

//code for class
public class MainServiceUser extends Activity
{

	protected static final String TAG = "MainServiceUser";

	int createdDatabase = 0;

	private boolean myIsBound = false;

	private KeyGenerator myKeyGeneratingService;

	private GoogleApiClient client;

	EditText year;
	EditText month;
	EditText date;
	EditText numberOfDays;

	@Override
	public void onCreate(Bundle icicle)
	{
		//call the parent class constructor
		super.onCreate(icicle);
		// SET THE LAYOUT FILE TO LOOK IN
		setContentView(R.layout.activity_main);

		month = findViewById(R.id.textViewOne);
		year = findViewById(R.id.textView);
		numberOfDays = findViewById(R.id.textViewThree);
		date = findViewById(R.id.textViewFour);

		Button dataFetchingButton = (Button) findViewById(R.id.button2);

		//Setting up the listerner for the button
		dataFetchingButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final DailyCash[] q;
				if (createdDatabase == 1)
				{
					try
					{
						if(!isDigitsOnly(year.getText().toString()) || !isDigitsOnly(month.getText().toString()) || !isDigitsOnly(date.getText().toString()) || !isDigitsOnly(numberOfDays.getText().toString()) || numberOfDays.getText().toString().isEmpty() || year.getText().toString().isEmpty() || month.getText().toString().isEmpty() || date.getText().toString().isEmpty() )
						{
							Toast toast1 = Toast.makeText(getApplicationContext(),"Please input info in every field",Toast.LENGTH_LONG);
							toast1.show();
						}
						else if(Integer.valueOf(month.getText().toString()) > 12 || Integer.valueOf(month.getText().toString()) < 1)
						{
							Toast toast1 = Toast.makeText(getApplicationContext(),"Please input the month from 1-12",Toast.LENGTH_LONG);
							toast1.show();

						}
						else if(Integer.valueOf(year.getText().toString()) != 2017 && Integer.valueOf(year.getText().toString()) != 2018)
						{
						Toast toast1 = Toast.makeText(getApplicationContext(),"Please input year in either 2017 or 2018",Toast.LENGTH_LONG);
						toast1.show();
						}
						else if(Integer.valueOf(numberOfDays.getText().toString()) > 31 || Integer.valueOf(numberOfDays.getText().toString()) < 1)
						{
							Toast toast1 = Toast.makeText(getApplicationContext(),"Please Enter number of days 1-31",Toast.LENGTH_LONG);
							toast1.show();
						}
						else if(Integer.valueOf(date.getText().toString()) > 31 || Integer.valueOf(date.getText().toString()) < 1)
						{
						Toast toast1 = Toast.makeText(getApplicationContext(),"Please input the date 1-31",Toast.LENGTH_LONG);
						toast1.show();

						}
						// Call KeyGenerator for getting a new ID
						else if (myIsBound)
						{
							q = myKeyGeneratingService.dailyCash(year.getText().toString(), month.getText().toString(), date.getText().toString(), numberOfDays.getText().toString());
							Log.i(TAG, getClass().getSimpleName() + "atleast this much" + q.length);
							for (DailyCash q1 : q) {
								Log.i(TAG, getClass().getSimpleName() + "check data" + q1.mDay + q1.mMonth + q1.mYear + q1.mDayOfWeek + q1.mCash);
							}
							//setting up the intent
							Intent intent = new Intent(MainServiceUser.this, Main2Activity.class);

							Bundle args = new Bundle();

							intent.putExtra("Data", q);
							startActivityForResult(intent, 1);
						} else
							//print the log message
							{
							Log.i(TAG, "Ugo says that the service was not bound!");
						}

					}
					catch (RemoteException e)
					{
						Log.e(TAG, e.toString());

					}
				}
				else
					{
					Toast toast1 = Toast.makeText(getApplicationContext(),"Create DataBase First",Toast.LENGTH_SHORT);
					toast1.show();
				}
			}
		});
		final Context c = this;

		final Button goButton = (Button) findViewById(R.id.button);
		goButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{

				try
				{

					// Call KeyGenerator and get a new ID
					if (myIsBound) {
						myKeyGeneratingService.createDataBase();
						createdDatabase = 1;
						Toast toast1 = Toast.makeText(getApplicationContext(),"DataBase Created",Toast.LENGTH_SHORT);
						toast1.show();
					} else {
						Log.i(TAG, "Ugo says that the service was not bound!");
					}

				}
				catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});

		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	// Bind to KeyGenerator Service
	@Override
	protected void onResume()
	{
		super.onResume();

		if (!myIsBound)
		{

			boolean b = false;
			Intent i = new Intent(KeyGenerator.class.getName());
			Log.i(TAG, KeyGenerator.class.getName());



			ResolveInfo info = getPackageManager().resolveService(i, 0);
			i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

			b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
			if (b)
			{
				Log.i(TAG, "Ugo says bindService() succeeded!");
			} else
				{
				Log.i(TAG, "Ugo says bindService() failed!");
			}

		}
	}


	@Override
	protected void onPause()
	{


		super.onPause();
	}

	private final ServiceConnection mConnection = new ServiceConnection()
	{

		public void onServiceConnected(ComponentName className, IBinder iservice)
		{

			myKeyGeneratingService = KeyGenerator.Stub.asInterface(iservice);

			myIsBound = true;

		}

		public void onServiceDisconnected(ComponentName className)
		{

			myKeyGeneratingService = null;

			myIsBound = false;

		}
	};

	@Override
	public void onStart()
	{
		super.onStart();


		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"MainServiceUser Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://dgupta25.cs478.uic.client/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"MainServiceUser Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://dgupta25.cs478.uic.client/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}
}
