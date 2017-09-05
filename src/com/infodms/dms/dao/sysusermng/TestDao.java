package com.infodms.dms.dao.sysusermng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcAaPO;
import com.infoservice.po3.bean.PageResult;

public class TestDao extends BaseDao<TcAaPO> {
	private static final TestDao dao = new TestDao();
	
	public static final TestDao getInstance() {
		return dao;
	}
	
	public PageResult<TcAaPO> pageQuery(int curPage, int pageSize) {
		String sql = "select * from TC_AA";
		List<Object> params = new ArrayList<Object>();
		PageResult<TcAaPO> taps = pageQuery(sql, params, curPage, pageSize);
		return taps;
	}
	
	public int delete() {
		String sql = "delete from TC_AA t where t.id = ? and t.name = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(7);
		params.add("11");
		return delete(sql, params);
	}

	@Override
	protected TcAaPO wrapperPO(ResultSet rs, int idx) {
		TcAaPO po = new TcAaPO();
		try {
			po.setId(rs.getInt("id"));
			po.setName(rs.getString("name"));
			po.setAge(rs.getInt("age"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return po;
	}

}
