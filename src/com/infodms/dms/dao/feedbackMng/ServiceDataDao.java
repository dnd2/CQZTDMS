package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmServiceDataPO;
import com.infodms.dms.po.TtIfServiceInfoPO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ServiceInfoDao.java</p>
 *
 * <p>Description: 服务资料审批表持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-17</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceDataDao extends BaseDao<TmServiceDataPO>{
	
	private static final ServiceDataDao dao = new ServiceDataDao();
	
	public static final ServiceDataDao getInstance() {
		return dao;
	}
	
	
	public PageResult<Map<String, Object>> queryServiceData(String dataname,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT D.DATA_ID, D.DATA_NAME, D.PRICE\n" );
		sql.append("  FROM TM_SERVICE_DATA D\n" );
		sql.append(" where D.STATUS = 10011001\n" );
		if(null!=dataname&&!"".equals(dataname)){
			sql.append("   AND D.DATA_NAME IN ('" );
			sql.append(dataname);
			sql.append("')\n");
		}
		
		sql.append(" ORDER BY DATA_ID");

		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(),null,"com.infodms.dms.dao.feedbackMng.ServiceDataDao.queryServiceData",pageSize, curPage);
		return ps;
		
	}

	protected TmServiceDataPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
