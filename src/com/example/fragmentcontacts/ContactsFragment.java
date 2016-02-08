package com.example.fragmentcontacts;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import adapterPro.Contact;
import adapterPro.ContactAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactsFragment extends Fragment {

	private ListView listView;
	private ArrayList<Contact> contacts;
	private String leJson;
	private JSONArray arrayJson;

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ContactsFragment newInstance(int sectionNumber) {
		ContactsFragment fragment = new ContactsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ContactsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
		listView = (ListView)rootView.findViewById(R.id.listview);
		contacts = new ArrayList<Contact>();
		Bundle json = getArguments();
		leJson = json.getString("leJson");
		Log.i("dispJson", ""+leJson);
		
		try{
			JSONObject jsonContacts = new JSONObject(leJson);
			arrayJson = jsonContacts.getJSONArray("contacts");
			Log.i("arrayJson", ""+arrayJson);
			
			Contact currentContact = null;
			
			for (int i = 0; i < arrayJson.length(); i++) {
				currentContact = new Contact();

				JSONObject jsonObject = arrayJson.getJSONObject(i);
				
					
				String name = jsonObject.getString("name");
				String phone = jsonObject.getString("phone");

				currentContact.setName(name);
				currentContact.setPhone(phone);
				contacts.add(currentContact);
			}
			ArrayAdapter<Contact> adapter = new ContactAdapter(getActivity(), R.layout.row, contacts);
			listView.setAdapter(adapter);
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
}
