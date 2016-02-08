package adapterPrivate;

public class ContactPrivate {
	String name = null;
	String phone = null;
	boolean selected = false;
	
	public ContactPrivate(){
		name = "";
		phone="";
		selected = false;
	}
	
	public ContactPrivate(String name, String phone, boolean selected){
		this.name = name;
		this.phone = phone;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "ContactPrivate [name=" + name + ", phone=" + phone + ", selected=" + selected + "]";
	}
	
	
}
