package com.example.fragmentcontacts;

import android.app.Activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 3000;
	ProgressDialog mProgressDialog;
	public static final String SERVER_URL = "http://www.chamillard.fr/appsandroid/contacts.json";
	String leJson;
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	static class RunnableHandler extends Handler {
		private Runnable monRunnable;
		public RunnableHandler (Runnable runnable){
			monRunnable = runnable;
		}
		
		public void handleMessage(Message msg){
			monRunnable.run();
		}	
	}
	
	private RunnableHandler monHandler = new RunnableHandler (new Runnable() {
		@Override
		public void run() {
			mProgressDialog.dismiss();
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		
		
		mProgressDialog = ProgressDialog.show(this, " Loading", "Chargement en cours" , true);	
		recuperationJson();
		Message msg = new Message();
		msg.what = STOPSPLASH;
		monHandler.sendMessageDelayed(msg, SPLASHTIME);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		switch(position) {
		case 1:
			Bundle json = new Bundle();
			json.putString("leJson", leJson);
			ContactsFragment contactsFragment = ContactsFragment.newInstance(position + 1);
			contactsFragment.setArguments(json);
			fragmentManager.beginTransaction().replace(R.id.container, contactsFragment)
			.commit();
			break;
			
		default:
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
			.commit();
			break;
		}
	}
	
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.addContact) {
			FragmentManager fragmentManager = getFragmentManager();
			NewContactsFragment newContactsFragment = NewContactsFragment.newInstance(3);
			fragmentManager.beginTransaction().replace(R.id.container, newContactsFragment)
			.commit();
		}
		if (id == R.id.listContacts) {
			FragmentManager fragmentManager = getFragmentManager();
			PrivateContactsFragment privateContactsFragment = PrivateContactsFragment.newInstance(3);
			fragmentManager.beginTransaction().replace(R.id.container, privateContactsFragment)
			.commit();
		}
		return super.onOptionsItemSelected(item);
	}
		
	public void recuperationJson() {
		RequestQueue queue = Volley.newRequestQueue(this);
		// Demande une reponse de type String a partir de l'URL et en mode GET
		StringRequest stringRequest = 
				new StringRequest(Request.Method.GET, SERVER_URL,
						new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						leJson = response;
						Log.i("json", ""+leJson);
					}
				},
						new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(),"That didn't work! Vérifiez votre connexion Internet",
								Toast.LENGTH_SHORT).show();
					}
				});
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			
			return rootView;
		}
	}
}
