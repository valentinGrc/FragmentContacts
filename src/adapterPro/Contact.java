package adapterPro;

import java.io.Serializable;

public class Contact implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5345553751352859977L;
	String name = null;
	String phone = null;
	
	public Contact(){
		name = "";
		phone="";
	}
	
	public Contact(String name, String phone){
		this.name = name;
		this.phone = phone;
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
		return "Contact [name=" + name + ", phone=" + phone + "]";
	}
	
	
}
