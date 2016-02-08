package com.example.fragmentcontacts;

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
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewContactsFragment extends Fragment {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 10000;
	
	public static final String SERVER_URL = "http://www.chamillard.fr/appsandroid/editContacts.php";
	public static final String SERVER_URL2 = "http://www.chamillard.fr/appsandroid/contacts.json";
	
	private EditText editText1, editText2;
	private Button button;
	private String name, phone;
	private JSONObject json;
	private JSONArray jsonArray = new JSONArray();
	private String leJson;
	
	private ProgressDialog mProgressDialog;
	
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
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static NewContactsFragment newInstance(int sectionNumber) {
		NewContactsFragment fragment = new NewContactsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public NewContactsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_newcontact, container, false);
		editText1 = (EditText)rootView.findViewById(R.id.editText1);
		editText2 = (EditText)rootView.findViewById(R.id.editText2);
		
		button = (Button)rootView.findViewById(R.id.button4);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v == button){
					name = editText1.getText().toString();
					Log.i("name", name);
					phone = editText2.getText().toString();
					Log.i("phone", phone);
					try {
						json = new JSONObject();
						json.put("phone", phone);
						json.put("name", name);
						jsonArray.put(json);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					mProgressDialog = ProgressDialog.show(getActivity(), "Chargement en cours", "Veuillez patienter pendant l'ajout du contact ..." , true);			 
					envoiJson();
					Message msg = new Message();
					msg.what = STOPSPLASH;
					monHandler.sendMessageDelayed(msg, SPLASHTIME);
				}
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
	
	public void envoiJson() {
		
		RequestQueue queue = Volley.newRequestQueue(getActivity());
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
						Toast.makeText(getActivity(),"That didn't work!",
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
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
		
		RequestQueue queue2 = Volley.newRequestQueue(getActivity());
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
						Toast.makeText(getActivity(),"That didn't work!",
								Toast.LENGTH_SHORT).show();
					}

				});
		queue2.add(stringRequest2);
		
	}
	
	public void LancementGO(){
		mProgressDialog.dismiss();
		FragmentManager fragmentManager = getFragmentManager();
		Bundle json = new Bundle();
		json.putString("leJson", leJson);
		ContactsFragment contactsFragment = ContactsFragment.newInstance(2);
		contactsFragment.setArguments(json);
		fragmentManager.beginTransaction().replace(R.id.container, contactsFragment)
		.commit();
	}
}
