
public class Member_info {
	private String id, password, name, date;

	public Member_info(String id, String password) {
		this.id = id;
		this.password = password;
	}

	public Member_info(String id, String password, String name, String date) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.date = date;
	}

	public String getID() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}
}
