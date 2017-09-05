package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfServiceInfoDetailPO;

/**
 * 
 * <p>Title:ServiceInfoDao.java</p>
 *
 * <p>Description: 服务资料明细表持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-21</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceInfoDetailDao extends BaseDao<TtIfServiceInfoDetailPO>{
	
	private static final ServiceInfoDetailDao dao = new ServiceInfoDetailDao();
	
	public static final ServiceInfoDetailDao getInstance() {
		return dao;
	}
	
	/**
	 * 取得服务资料明细表主键ID
	 */ 
	public Long getDetailId(TtIfServiceInfoDetailPO po){
		
		return dao.getLongPK(po);
	}
    
	
	/**
	 * 新增服务资料明细表记录
	 */
	public void saveDetail(TtIfServiceInfoDetailPO po) {
		dao.insert(po);
	}
	
	/**
	 * 通过orderID查询详细表的数据，用于修改之前的查询
	 * @param orderId
	 * @param infoId
	 * @return
	 */
	public List<Map<String, Object>> getDetailByOrderId(String orderId){
		
		String sql="SELECT * FROM TT_IF_SERVICE_INFO_DETAIL D WHERE D.ORDER_ID ='"+orderId;
		List<Map<String, Object>> ps = pageQuery(sql,null,"com.infodms.dms.dao.feedbackMng.ServiceInfoDetailDao.getDetailIdByInfoId");
		
		return ps;
	}
	
	protected TtIfServiceInfoDetailPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
