package com.example.fragmentcontacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import adapterPrivate.ContactAdapterPrivate;
import adapterPrivate.ContactPrivate;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PrivateContact extends Activity implements OnClickListener{

	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 10000;
	
	ArrayList<ContactPrivate> contacts;
	ListView listView;
	Button add;
	String leJson;
	ProgressDialog mProgressDialog;
	JSONObject json;
	JSONArray jsonArray = new JSONArray(); 
	
	public static final String SERVER_URL = "http://www.chamillard.fr/appsandroid/editContacts.php";
	public static final String SERVER_URL2 = "http://www.chamillard.fr/appsandroid/contacts.json";
	
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
			LancementGO();
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_privatecontacts);
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);	
		listView = (ListView) findViewById(R.id.list2);
		contacts = new ArrayList<ContactPrivate>();
		fetchContacts();	
	}
	
	public void fetchContacts() {
		String phoneNumber = null;
		Boolean state = false;
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, DISPLAY_NAME);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ContactPrivate currentContact = new ContactPrivate();
				String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
				String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
				if (hasPhoneNumber > 0) {
					currentContact.setName(name);
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						currentContact.setPhone(phoneNumber);	
					}
					phoneCursor.close();
					currentContact.setSelected(state);
					contacts.add(currentContact);
					Log.i("current", ""+currentContact);
				}
			}	
		}
		ArrayAdapter<ContactPrivate> adapter = new ContactAdapterPrivate(this, R.layout.privaterow, contacts);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			   public void onItemClick(AdapterView<?> parent, View view,
			     int position, long id) {
				   ContactPrivate contactPrivate = (ContactPrivate) parent.getItemAtPosition(position);
			       if(contactPrivate.isSelected() == false){
			    	   Toast.makeText(getApplicationContext(),
			    			   "Contact à ajouter: " + contactPrivate.getName(), 
			    			   Toast.LENGTH_LONG).show();
			    	   try {
			    		   json = new JSONObject();
			    		   json.put("phone",contactPrivate.getPhone());
			    		   json.put("name",contactPrivate.getName());
				    	   jsonArray.put(json);
			    		   Log.i("jsonArray", ""+jsonArray);
			    	   } catch (JSONException e) {
			    		   e.printStackTrace();
			    	   }
			    	   contactPrivate.setSelected(true);
			       }
			       else{
			    	   Toast.makeText(getApplicationContext(),
			    			   "Ce contact n'est plus à ajouter: " + contactPrivate.getName(), 
			    			   Toast.LENGTH_LONG).show();
			    	   JSONArray tempJa = new JSONArray();
			    		   try {
					    	   for(int i=0; i<jsonArray.length();i++){
					    		   if(!jsonArray.getJSONObject(i).getString("name").equals(contactPrivate.getName())){
					    			   tempJa.put(jsonArray.getJSONObject(i));
					    		   }
					    	   }
			    			   jsonArray = tempJa;
			    			   Log.i("jsonArray", ""+jsonArray);
			    		   } catch (JSONException e) {
			    			   e.printStackTrace();
			    		   }
			    	   contactPrivate.setSelected(false);
			       }
			       listView.getAdapter().getView(position, view, listView);
			    }
			  });
	}
	
	public void envoiJson() {
		RequestQueue queue = Volley.newRequestQueue(this);
		// Demande une reponse de type String a partir de l'URL et en mode GET
		StringRequest stringRequest = 
				new StringRequest(Request.Method.POST, SERVER_URL,
						new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
					}
				},
						new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(),"That didn't work!",
								Toast.LENGTH_SHORT).show();
					}

				}){
				    protected Map<String, String> getParams()
				    {
				        Map<String, String>  params = new HashMap<String, String>();
				        // paramètre du POST
				        params.put("json", jsonArray.toString());
				        return params;
				    }
				};
		queue.add(stringRequest);
		
		RequestQueue queue2 = Volley.newRequestQueue(this);
		StringRequest stringRequest2 =
				new StringRequest(Request.Method.GET, SERVER_URL2,
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
						Toast.makeText(getApplicationContext(),"That didn't work!",
								Toast.LENGTH_SHORT).show();
					}

				});
		queue2.add(stringRequest2);
	}
	
	public void LancementGO(){

		Intent intentGO = new Intent(PrivateContact.this, ContactsFragment.class);
		intentGO.putExtra("leJson", leJson);
		mProgressDialog.dismiss();
		startActivityForResult(intentGO, 10);
	}
	
	@Override
	public void onClick(View v) {
		if(v == add){
			//add contacts with checkbox
			mProgressDialog = ProgressDialog.show(this, "Chargement en cours", "Veuillez patienter pendant l'ajout des contacts ..." , true);			 
			envoiJson();
			Message msg = new Message();
			msg.what = STOPSPLASH;
			monHandler.sendMessageDelayed(msg, SPLASHTIME);
		}
	}	
}