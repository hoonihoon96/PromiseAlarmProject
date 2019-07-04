

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.AlarmDao;
import dto.Alarm_info;


@WebServlet("/AcceptInvite")
public class AcceptInvite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AcceptInvite() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String id = request.getParameter("id");
		
		System.out.println("id = " + id);
		
		response.setContentType("apllication/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject resultJson = new JSONObject();
		
		try {
			Alarm_info alarm = AlarmDao.getInstance().select_Alarm(Integer.parseInt(id));
			
			resultJson.put("code", 200);
			resultJson.put("id", alarm.getAlarm_Id());
			resultJson.put("title", alarm.getTitle());
			resultJson.put("description", alarm.getExplanation());
			resultJson.put("creator", alarm.getCreator());
			resultJson.put("alarmDateTime", alarm.getTime());
			resultJson.put("endMinute", alarm.getEnd_Time());
			resultJson.put("createdDateTime", alarm.getCreate_Date());
			resultJson.put("modifiedCount", alarm.getModify_Number());
			
			out.println(resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 400);
			out.println(resultJson.toString());
		}
	}

}
