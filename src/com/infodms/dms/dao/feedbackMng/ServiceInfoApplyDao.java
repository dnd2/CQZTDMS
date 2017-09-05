package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.ServiceInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcAaPO;
import com.infodms.dms.po.TtIfServiceInfoPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

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
public class ServiceInfoApplyDao extends BaseDao<TtIfServiceInfoPO>{
	
	private static final ServiceInfoApplyDao dao = new ServiceInfoApplyDao();
	
	public static final ServiceInfoApplyDao getInstance() {
		return dao;
	}
	
	/**
	 * 分页查询服务资料审批表
	 * @param orderId
	 * @param startDate
	 * @param endDate
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryServiceInfo(String dealerId,String orderId,String startDate,String endDate,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT INFO.ORDER_ID,INFO.MAIL_TYPE,TO_CHAR(INFO.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,INFO.STATUS\n" );
		sql.append("FROM TT_IF_SERVICE_INFO INFO\n");
		sql.append("WHERE INFO.IS_DEL = 0\n");
		sql.append("AND   INFO.STATUS IN(");
		sql.append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);
		sql.append(",");
		sql.append(Constant.SERVICEINFO_VIP_AREA_STATUS_REJECT).append(",");
		sql.append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT).append(",");
		sql.append(Constant.SERVICEINFO_VIP_AUDIT_STATUS_REJECT);
		sql.append(")");
		
		if(null!=dealerId&&!("").equals(dealerId)){
			sql.append("AND   INFO.DEALER_ID = ");
			sql.append(dealerId);
			sql.append("\n");
		}
		
		if(null!=orderId&&!("").equals(orderId)){
			sql.append(" and   info.order_id like '%");
			sql.append(orderId);
			sql.append("%' \n");
		}
		if(null!=startDate&&!("").equals(startDate)){
			sql.append(" and   info.create_date >= TO_DATE('" );
			sql.append(startDate);
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(null!=endDate&&!("").equals(endDate)){
			sql.append(" and   info.create_date <= TO_DATE('");
			sql.append(endDate);
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		sql.append("  ORDER BY INFO.CREATE_DATE DESC");
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, "com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao.queryServiceInfo",pageSize, curPage);
		return ps;
		
	}
	
	/**
	 * 根据ID查询服务资料审批表的基本信息
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> searchServiceInfoBaseById(String orderId){
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT INFO.ORDER_ID,\n" );
		sql.append("       INFO.MAIL_TYPE,\n" );
		sql.append("       INFO.MAIL_ADDRESS,\n" );
		sql.append("       INFO.FAX,\n" );
		sql.append("       INFO.TEL,\n" );
		sql.append("       INFO.Zip_Code,\n" );
		sql.append("       INFO.LINK_MAN,\n" );
		sql.append("       INFO.SE_CONTENT,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_NAME\n" );
		sql.append("  FROM TT_IF_SERVICE_INFO INFO, TM_DEALER TD\n" );
		sql.append(" WHERE INFO.DEALER_ID = TD.DEALER_ID\n" );
		sql.append("   AND INFO.ORDER_ID = '");
	    sql.append(orderId);
	    sql.append("'");
		
		Map<String, Object> ps = pageQueryMap(sql.toString(),null,getFunName());
		return ps;
		
	}
	
	/**
	 * 根据orderID查询服务资料明细表信息TT_IF_SERVICE_INFO_DETAIL
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getInfoDetail(String orderId){
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT SD.DATA_ID,D.ID,\n" );
		sql.append("       D.AMOUNT,\n" );
		sql.append("       D.REMARK,\n" );
		sql.append("       SD.DATA_NAME,\n" );
		sql.append("       D.PRICE,\n" );
		sql.append("       (D.PRICE * D.AMOUNT) AS SUMPRICE,\n" );
		sql.append("       NVL((D.PRICE * D.AMOUNT) ,0)AS AUDIT_SUM\n" );
		sql.append("FROM TT_IF_SERVICE_INFO_DETAIL D, TM_SERVICE_DATA SD\n" );
		sql.append("WHERE D.INFO_ID = SD.DATA_ID\n" );
		sql.append("AND D.ORDER_ID = '");
		sql.append(orderId);
		sql.append("'");

		List<Map<String, Object>> detailList = 
		pageQuery(sql.toString(), null, getFunName());
		return detailList;
	}
	
	
	/**
	 * 查询服务资料表取得资料信息供页面选择
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> getDealerInfo(String dealerId){
		String sql = "SELECT TD.LINK_MAN,TD.PHONE,TD.FAX_NO,TD.ADDRESS FROM TM_DEALER TD WHERE  TD.DEALER_ID = "+dealerId;
	    Map<String, Object> ps = pageQueryMap(sql,null,"com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao.getDealerInfo");
		return ps;
		
	}
	
	/**
	 * 根据orderId查询出审批表的基本信息和资料明细
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getServiceInfoById(String orderId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ORDER_ID,\n" );
		sql.append("       A.LINK_MAN,\n" );
		sql.append("       A.TEL,\n" );
		sql.append("       A.FAX,\n" );
		sql.append("       A.MAIL_TYPE,\n" );
		sql.append("       A.MAIL_ADDRESS,\n" );
		sql.append("       A.SE_CONTENT,\n" );
		sql.append("       B.AMOUNT,\n" );
		sql.append("       B.REMARK,\n" );
		sql.append("       C.DATA_NAME,\n" );
		sql.append("       C.PRICE\n" );
		sql.append("  FROM TT_IF_SERVICE_INFO A,\n" );
		sql.append("       TT_IF_SERVICE_INFO_DETAIL B,\n" );
		sql.append("       TM_SERVICE_DATA C\n" );
		sql.append("  WHERE A.ORDER_ID = B.ORDER_ID\n" );
		sql.append("  AND   B.INFO_ID = C.DATA_ID\n" );
		sql.append("  AND   A.ORDER_ID = '");
		sql.append(orderId);
		sql.append("'");

		Map<String, Object> ps = pageQueryMap(sql.toString(),null,"com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao.getServiceInfoById");
		return ps;
		
	}
    
	/**
	 * 新增服务资料审批表数据
	 * @param po
	 */
	public void saveServiceInfo(TtIfServiceInfoPO po){
		dao.insert(po);
	}
	
	protected TtIfServiceInfoPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
