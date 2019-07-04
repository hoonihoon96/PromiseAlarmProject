

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.UserDao;
import dto.Member_info;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignUp() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String token = request.getParameter("token");
		
		response.setContentType("apllication/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject resultJson = new JSONObject();
		
		Member_info member = new Member_info(id, password, name,  token, Calendar.getInstance().getTime().toString());
		
		try {
			UserDao.getInstance().insert_User(member);
			resultJson.put("code", 200);
		} catch (SQLException e) {
			e.printStackTrace();
			resultJson.put("code", 400);
		}
		
		out.println(resultJson.toString());
	}

}
