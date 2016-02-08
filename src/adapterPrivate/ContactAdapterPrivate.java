package adapterPrivate;

import java.util.List;

import com.example.fragmentcontacts.R;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapterPrivate extends ArrayAdapter<ContactPrivate>{

	private Activity activity;
	private List<ContactPrivate> list;
	private ContactPrivate renseignement;
	private int row;

	public ContactAdapterPrivate(Activity act, int resource, List<ContactPrivate> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.list = arrayList;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		
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
		holder.selected = (ImageView) view.findViewById(R.id.imageView1);
		//holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
	
		if(holder.name != null && null != renseignement.getName()
				&& renseignement.getName().trim().length() > 0) {
			holder.name.setText(Html.fromHtml(renseignement.getName()));
		}
			
		if (holder.phone != null && null != renseignement.getPhone()
				&& renseignement.getPhone().trim().length() > 0) {
			holder.phone.setText(Html.fromHtml(renseignement.getPhone()));
		}
		
		if (renseignement.isSelected() == true){
			holder.selected.setImageResource(R.drawable.cerclevert);
		}
		else{
			holder.selected.setImageResource(R.drawable.cerclenoir);
		}
			/*
			holder.checkbox.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					ContactPrivate contactPrivate = (ContactPrivate) cb.getTag();
					Log.i("contactPrivate", ""+contactPrivate);
					Toast.makeText(getContext(), "coché", Toast.LENGTH_SHORT).show();
					//contactPrivate.setSelected(cb.isChecked());
				}
			});
			*/
			//holder.checkbox.setChecked(renseignement.isSelected());
		return view;
	}

	public class ViewHolder {
		public TextView name, phone;
		public ImageView selected;
		//public CheckBox checkbox;
	}
}
