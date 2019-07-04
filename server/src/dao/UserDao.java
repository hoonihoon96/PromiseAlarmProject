package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Member_info;

public class UserDao {
	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/alarm";
	static final String id = "root";
	static final String password = "password";
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	private static UserDao instance = new UserDao();

	public static UserDao getInstance() {
		return instance;
	}

	private UserDao() {

	}

	public void insert_User(Member_info info) throws SQLException {
		String sql = "insert into user(user_id, user_password, user_name, user_token, registered_date) values(?, ?, ?, ?, ?) ";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, password);
			System.out.println("데이터 베이스 접속!");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getID());
			pstmt.setString(2, info.getPassword());
			pstmt.setString(3, info.getName());
			pstmt.setString(4, info.getToken());
			pstmt.setString(5, info.getDate());
			pstmt.executeUpdate();
		} catch (ClassNotFoundException ce) {
		} catch (SQLException se) {
			se.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new SQLException();
		} finally {
			try {
				pstmt.close();
			} catch (Exception pe) {
			}
			try {
				conn.close();
			} catch (Exception ce) {
			}
		}
	}

	public Member_info select_User(Member_info info) {
		String sql = "select * from user where user_id = ? and user_password = ?";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, password);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getID());
			pstmt.setString(2, info.getPassword());
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String id = rs.getString("user_id");
				String name = rs.getString("user_name");
				//String password = rs.getString("user_password");
				
				System.out.println("name = " + name);

				return new Member_info(id, name);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception re) {
			}
			try {
				pstmt.close();
			} catch (Exception pe) {
			}
			try {
				conn.close();
			} catch (Exception ce) {
			}
		}

		return null;
	}
	
	public Member_info selectUserById(String id) {
		String sql = "select * from user where user_id = ?";
		System.out.println(sql);
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, this.id, password);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String userId = rs.getString("user_id");
				
				System.out.println(userId);

				return new Member_info(userId);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception re) {
			}
			try {
				pstmt.close();
			} catch (Exception pe) {
			}
			try {
				conn.close();
			} catch (Exception ce) {
			}
		}
		
		return null;
	}
	
	public String selectTokenById(String id) {
		String sql = "select user_token from user where user_id = ?";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, this.id, password);
			System.out.println("데이터 베이스 접속!");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String token = rs.getString("user_token");

				return token;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception re) {
			}
			try {
				pstmt.close();
			} catch (Exception pe) {
			}
			try {
				conn.close();
			} catch (Exception ce) {
			}
		}
		
		return null;
	}
	
	public void insertTokenById(String token, String id) throws SQLException {
		String sql = "update user set user_token = ? where user_id = ?";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, this.id, password);
			System.out.println("데이터 베이스 접속!");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException ce) {
		} catch (SQLException se) {
			System.out.println("토큰 에러");
			se.printStackTrace();
			System.out.println(se.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new SQLException();
		} finally {
			try {
				pstmt.close();
			} catch (Exception pe) {
			}
			try {
				conn.close();
			} catch (Exception ce) {
			}
		}
	}
}
