package dto;

public class Member_info {
	private String id, password, name, date, token;
	
	public Member_info(String id) {
		this.id = id;
	}
	
	public Member_info(String id, String password, String token) {
		this.id = id;
		this.password = password;
		this.token = token;
	}

	public Member_info(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Member_info(String id, String password, String name, String token, String date) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.token = token;
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
	
	public String getToken() {
		return token;
	}

	public String getDate() {
		return date;
	}
}
