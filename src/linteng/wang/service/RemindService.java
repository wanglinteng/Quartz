package linteng.wang.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import linteng.wang.model.Remind;
import linteng.wang.util.DB;

public class RemindService {
	/**
	 * @see 获取一个小时内需要提醒的记录
	 * @return
	 */
	public List<Remind> getReminds() {
		long current = new Date().getTime();
		long time = current / 1000;// 当前时间由于php时间戳10位，所以/1000
		long maxtime = time + 3600;
		Connection conn = DB.createConn();
		String sql = "select distinct time from bc_wx_push_exact where time >= " + time + " and time <= " + maxtime;//查询在此时间段内需要执行的时刻
		PreparedStatement ps = DB.prepare(conn, sql);
		List<Remind> reminds = new ArrayList<Remind>();
		try {
			ResultSet rs = ps.executeQuery();
			Remind r = null;
			while (rs.next()) {
				r = new Remind();
				r.setTime(rs.getLong("time"));
				reminds.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DB.close(ps);
		DB.close(conn);
		return reminds;
	}

	/**
	 * @see 查询某一时刻需要发送的记录
	 * @param time
	 * @return
	 */
	public List<Remind> loadByTime(long time) {
		Connection conn = DB.createConn();
		String sql = "select * from bc_wx_push_exact where time =  ?";
		PreparedStatement ps = DB.prepare(conn, sql);
		List<Remind> reminds = new ArrayList<Remind>();
		try {
			ps.setLong(1, time);
			ResultSet rs = ps.executeQuery();
			Remind r = null;
			while (rs.next()) {
				r = new Remind();
				r.setId(rs.getInt("id"));
				reminds.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DB.close(ps);
		DB.close(conn);
		return reminds;
	}
}
