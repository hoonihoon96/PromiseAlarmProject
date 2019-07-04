
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.UserDao;
import dto.Member_info;

@WebServlet("/SendInvite")
public class SendInvite extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendInvite() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String sender = request.getParameter("sender");
		String receiver = request.getParameter("receiver");
		String id = request.getParameter("id");
		
		System.out.println("sender : " + sender);
		System.out.println("receiver : " + receiver);
		System.out.println("id : "+ id);

		String token;

		try {
			response.setContentType("application/json; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			
			JSONObject resultJson = new JSONObject();
			
			System.out.println("¸â¹ö Ã£±â Àü");
			
			Member_info member = UserDao.getInstance().selectUserById(receiver);
			
			if (member == null) {
				System.out.println("¸â¹ö ¿Ö ³Î?");
				resultJson.put("code", 400);
				resultJson.put("sub_code", 0);
				
				printWriter.println(resultJson.toString());
				return;
			}
			
			System.out.println("¸â¹ö Ã£±â ¼º°ø");
			
			token = UserDao.getInstance().selectTokenById(receiver);

			String key = "AIzaSyDkS0aFos_R_GqMxl3uXlZeBgLuHd1f3Oo";

			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + key);
			connection.setDoOutput(true);

			JSONObject dataJson = new JSONObject();
			dataJson.put("type", "invite");
			dataJson.put("sender", sender);
			dataJson.put("id", Integer.parseInt(id));

			JSONObject messageJson = new JSONObject();
			messageJson.put("to", token);
			messageJson.put("data", dataJson);

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(messageJson.toString().getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();
			
			int code = connection.getResponseCode();
			String message = connection.getResponseMessage();
			
			System.out.println("FCM Code : " + code);
			System.out.println("FCM Message : " + message);
			
			resultJson.put("code", code);
			
			printWriter.println(resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
