

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.UserDao;
import dto.Member_info;

@WebServlet("/SignIn")
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignIn() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String token = request.getParameter("token");
		
		response.setContentType("apllication/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject resultJson = new JSONObject();
		
		Member_info member = UserDao.getInstance().select_User(new Member_info(id, password, token));
		
		if (member != null) {
			try {
				UserDao.getInstance().insertTokenById(token, id);
				resultJson.put("code", 200);
				resultJson.put("name", member.getName());
			} catch (SQLException e) {
				e.getMessage();
				e.printStackTrace();
				resultJson.put("code", 400);
			}
		} else {
			resultJson.put("code", 400);
		}
		
		out.println(resultJson.toString());
	}

}
