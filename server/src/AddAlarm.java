
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.AlarmDao;
import dto.Alarm_info;

@WebServlet("/AddAlarm")
public class AddAlarm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddAlarm() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		Calendar now = Calendar.getInstance();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		String title = request.getParameter("title");
		String desciption = request.getParameter("description");
		String creator = request.getParameter("creator");
		String alarmTime = request.getParameter("alarmTime");
		String endTime = request.getParameter("endTime");
		String createdDate = format.format(now.getTime());

		response.setContentType("apllication/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject resultJson = new JSONObject();
		
		Alarm_info alarm = new Alarm_info();
		alarm.setTitle(title);
		alarm.setExplanation(desciption);
		alarm.setCreator(creator);
		alarm.setTime(alarmTime);
		alarm.setEnd_time(Integer.parseInt(endTime));
		alarm.setModify_number(0);
		alarm.setCreate_date(createdDate);
		
		int id;
		
		try {
			id = AlarmDao.getInstance().selectNextAutoIncrement();
			AlarmDao.getInstance().insert_Alarm(alarm);
			resultJson.put("code", 200);
			resultJson.put("id", id);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			resultJson.put("code", 400);
			resultJson.put("sql", e.getSQLState());
			resultJson.put("message", e.getStackTrace());
		}
		
		out.println(resultJson.toString());
		
	}

}
