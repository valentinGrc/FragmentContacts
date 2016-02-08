package adapterPro;

import java.util.List;
import com.example.fragmentcontacts.R;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact>{

	private Activity activity;
	private List<Contact> list;
	private Contact renseignement;
	private int row;

	public ContactAdapter(Activity act, int resource, List<Contact> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.list = arrayList;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((list == null) || ((position + 1) > list.size()))
			return view;

		renseignement = list.get(position);
	
		holder.name = (TextView) view.findViewById(R.id.name);
		holder.phone = (TextView) view.findViewById(R.id.phone);


		if(holder.name != null && null != renseignement.getName()
				&& renseignement.getName().trim().length() > 0) {
			holder.name.setText(Html.fromHtml(renseignement.getName()));
		}
		
		if (holder.phone != null && null != renseignement.getPhone()
				&& renseignement.getPhone().trim().length() > 0) {
			holder.phone.setText(Html.fromHtml(renseignement.getPhone()));
		}
		
		return view;
	}

	public class ViewHolder {
		public TextView name, phone;
	}
}
