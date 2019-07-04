package dto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Alarm_info;

public class AlarmDao {
	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/alarm";
	static final String id = "root";
	static final String password = "apmsetup";
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	private static AlarmDao instance = new AlarmDao();

	public static AlarmDao getInstance() {
		return instance;
	}

	private AlarmDao() {

	}

	public int selectNextAutoIncrement() {
		String sql = "select auto_increment from information_schema.TABLES where TABLE_SCHEMA = 'alarm' and TABLE_NAME = 'alarm";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, password);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.isFirst()) {
				int id = rs.getInt("auto_increment");

				return id;
			}
		} catch (ClassNotFoundException | SQLException e) {
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
		
		return -1;
	}

	public void insert_Alarm(Alarm_info info) throws SQLException {

		String sql = "insert into alarm(alarm_title, alarm_explanation, alarm_creator, alarm_time, alarm_end_time, alarm_create_date, alarm_modify_number) values(?, ?, ?, ?, ?, ?, ?) ";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, password);
			System.out.println("������ ���̽� ����!");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getTitle());
			pstmt.setString(2, info.getExplanation());
			pstmt.setString(3, info.getCreator());
			pstmt.setString(4, info.getTime());
			pstmt.setInt(5, info.getEnd_Time());
			pstmt.setString(6, info.getCreate_Date());
			pstmt.setInt(7, info.getModify_Number());
			pstmt.executeUpdate();
		} catch (ClassNotFoundException ce) {
		} catch (SQLException se) {
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

	public Alarm_info select_Alarm(Alarm_info info) {

		String sql = "select alarm_id from alarm where alarm_id = ?";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, password);
			System.out.println("������ ���̽� ����!");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, info.getAlarm_Id());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("alarm_id");

				return new Alarm_info(id);
			}
		} catch (Exception e) {

		}

		return null;
	}
}
