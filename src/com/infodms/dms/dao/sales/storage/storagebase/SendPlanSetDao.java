package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesCheckParPO;
import com.infoservice.po3.bean.PO;

/**
 * 
 * @ClassName     : SendPlanSetDao 
 * @Description   : 发运计划参数设置DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-9
 */
public class SendPlanSetDao extends BaseDao<PO>{
	private static final SendPlanSetDao dao = new SendPlanSetDao ();
	public static final SendPlanSetDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 发运计划参数添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  void sendPlanSetAdd(TtSalesCheckParPO ttSalesCheckParPO) {
		dao.insert(ttSalesCheckParPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 发运计划参数修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  void sendPlanSetUpdate(TtSalesCheckParPO seachPO,TtSalesCheckParPO ttSalesCheckParPO) {
		dao.update(seachPO, ttSalesCheckParPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据产地查询参数信息
	 * @param      : @param yieldly
	 * @param      : @return     参数信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public Map<String, Object> getSendPlanSetQuery(String yieldly){
		
		Map<String, Object> map=new HashMap<String, Object>();
		if(yieldly!=null && !"".equals(yieldly)){
		StringBuffer sql= new StringBuffer();
			sql.append("SELECT TSCP.PAR_ID,TSCP.PAR_VALUE,TSCP.YIELDLY FROM TT_SALES_CHECK_PAR TSCP WHERE TSCP.YIELDLY=\n");
			sql.append(Long.parseLong(yieldly));
			map = pageQueryMap(sql.toString(),null,getFunName());
		}
		return map;
	}
}
