package dto;

public class Alarm_info {
	private String title, explanation, creator, create_date, time;
	private int alarm_id, end_time, modify_number;

	public Alarm_info() {
		
	}
	
	public Alarm_info(int alarm_id) {
		this.alarm_id = alarm_id;
	}

	public Alarm_info(int alarm_id, String title, String explanation, String creator, String alarm_time,
			String create_date, int end_time, int modify_number) {
		this.alarm_id = alarm_id;
		this.title = title;
		this.explanation = explanation;
		this.creator = creator;
		this.time = alarm_time;
		this.create_date = create_date;
		this.end_time = end_time;
		this.modify_number = modify_number;
	}

	public int getAlarm_Id() {
		return alarm_id;
	}

	public String getTitle() {
		return title;
	}

	public String getExplanation() {
		return explanation;
	}

	public String getCreator() {
		return creator;
	}

	public String getTime() {
		return time;
	}

	public int getEnd_Time() {
		return end_time;
	}

	public String getCreate_Date() {
		return create_date;
	}

	public int getModify_Number() {
		return modify_number;
	}
	
	public void setAlarm_id(int alarm_id) {
		this.alarm_id = alarm_id;
	}
	
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}
	
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	public void setModify_number(int modify_number) {
		this.modify_number = modify_number;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
