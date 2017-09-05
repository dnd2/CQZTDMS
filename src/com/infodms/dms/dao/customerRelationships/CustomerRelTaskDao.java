package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class CustomerRelTaskDao extends BaseDao {
private static final CustomerRelTaskDao dao = new CustomerRelTaskDao();
	
	public static final CustomerRelTaskDao getInstance()
	{
		return dao;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	/**
	 * 方法描述 ： 获取服务器时间<br/>
	 * 
	 * @return
	 * @throws Exception
	 * @author wangsongwei
	 */
	public String getDbDate() throws Exception
	{
		return dao.pageQueryMap("SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') DB_DATE FROM DUAL", null, getFunName()).get("DB_DATE").toString();
	}

	/**
	 * 查询当前时刻第二天的坐席排班情况
	 * @return 排班坐席的工号,姓名,坐席组代码,坐席组名字
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectSecDaySeats() throws Exception{
		String sql ="SELECT S.SE_ACCOUNT, S.SE_NAME, S.SE_EXT, T.ST_CODE, T.ST_NAME\n" +
					"  FROM TT_CRM_SORT_SHIFT A,\n" + 
					"       TT_CRM_WORKTIME   W,\n" + 
					"       TT_CRM_SEATS_TEAM T,\n" + 
					"       TT_CRM_SEATS      S\n" + 
					" WHERE A.WT_TYPE = W.WT_TYPE\n" + 
					"   AND DECODE(A.WT_TYPE, 95501001, 2, 1) = T.ST_CODE\n" + 
					"   AND A.USER_ID = S.SE_USER_ID\n" + 
					"   AND TRUNC(A.DUTY_DATE) = TRUNC(SYSDATE) + 1\n" +
					"   AND to_char(SYSDATE,'HH24') >= 19";
		
		return dao.pageQuery(sql, null, getFunName());
		
	}
}
