package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ServiceInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.po.TtIfServiceInfoAuditPO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ServiceInfoApproveDao.java</p>
 *
 * <p>Description: 服务资料审批明细表持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-23</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceInfoApproveDao extends BaseDao<TtIfServiceInfoAuditPO>{
	
	private static final ServiceInfoApproveDao dao = new ServiceInfoApproveDao();
	
	public static final ServiceInfoApproveDao getInstance() {
		return dao;
	}
	
	/**
	 * 分页查询服务资料审批明细
	 * @param orderId
	 * @param startDate
	 * @param endDate
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryServiceInfoApprove(AclUserBean logonUser,ServiceInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT D.DEALER_CODE,\n" );
		sql.append("       D.DEALER_NAME,\n" );
		sql.append("       A.ORDER_ID,\n" );
		sql.append("       TO_CHAR(A.APP_DATE,'YYYY-MM-DD') APP_DATE,\n" );
		sql.append("       A.MAIL_TYPE,\n" );
		sql.append("       A.STATUS\n" );
		sql.append("  FROM TT_IF_SERVICE_INFO A, TM_DEALER D\n" );
		sql.append(" WHERE A.STATUS = " );
		sql.append(Constant.SERVICEINFO_VIP_STATUS_REPORTED);
		sql.append("\n");
		sql.append("   AND A.DEALER_ID = D.DEALER_ID\n" );
		
		if(Utility.testString(bean.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "D", "dealer_code")); 
		}
		if(!"".equals(bean.getOrderId())){
			sql.append("   AND A.ORDER_ID like '%" );
			sql.append(bean.getOrderId());
			sql.append("%' \n");
		}
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND A.APP_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND A.APP_DATE <= TO_DATE('" );
			sql.append(bean.getEndTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
				}
		
		if(!"".equals(bean.getMailType())){
			sql.append("   AND A.MAIL_TYPE =" );
			sql.append(bean.getMailType());
				}
		
		if(!"".equals(bean.getDealerId())){
			sql.append("   AND A.DEALER_ID =");
			sql.append(bean.getDealerId());
				}
		if(null!=bean.getCompanyId()&&!"".equals(bean.getCompanyId())){
			sql.append("	AND A.company_id ="+bean.getCompanyId());
		}
		sql.append("order by A.APP_DATE ");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), params,getFunName(),pageSize, curPage);
		return ps;
		
	}
	
	
	/**
	 * 服务资料审批表的查询（车厂端）
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> searchAllServiceInfo(AclUserBean logonUser,ServiceInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT D.DEALER_CODE,\n" );
		sql.append("       D.DEALER_NAME,\n" );
		sql.append("       A.ORDER_ID,\n" );
		sql.append("       TO_CHAR(A.APP_DATE,'yyyy-mm-dd') APP_DATE,\n" );
		sql.append("       A.MAIL_TYPE,\n" );
		sql.append("       A.STATUS\n" );
		sql.append("  FROM TT_IF_SERVICE_INFO A, TM_DEALER D\n" );
		sql.append(" WHERE A.STATUS NOT IN ( " );
		sql.append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);
		sql.append(")\n");
		sql.append("   AND A.DEALER_ID = D.DEALER_ID\n" );
		if(!"".equals(bean.getOrderId())){
			sql.append("   AND A.ORDER_ID like '%" );
			sql.append(bean.getOrderId());
			sql.append("%' \n");
		}
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND A.APP_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND A.APP_DATE <= TO_DATE('" );
			sql.append(bean.getEndTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		
		if(!"".equals(bean.getMailType())){
			sql.append("   AND A.MAIL_TYPE =" );
			sql.append(bean.getMailType());
		}if(null!=bean.getCompanyId()&&!"".equals(bean.getCompanyId())){
		 	sql.append("	AND A.company_id = "+bean.getCompanyId());
		}
		//modify by xiayanpeng begin 去除DEALER_ID
//		if(!"".equals(bean.getDealerId())){
//			sql.append("   AND A.DEALER_ID =");
//			sql.append(bean.getDealerId());
//		}
		//modify by xiayanpeng end
		if(!"".equals(bean.getStatus())){
			sql.append("   AND A.STATUS =");
			sql.append(bean.getStatus());
		}
		//modfy by xiayanpeng begin 根据DEALER_CODE，生成SQL
		//拼sql的查询条件
		if (Utility.testString(bean.getDealerCode())) {
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "D", "dealer_code"));
		}
		if (Utility.testString(bean.getDealerName())) {
			sql.append(" and d.dealer_name like '%"+bean.getDealerName()+"%' ");
		}	
		//modify by xiayanpeng end  
		
		//modify by xiayanpeng begin 加入根据用户ORG_ID过滤经销商ID
		String dealerIds = GetOrgIdsOrDealerIdsDAO.getDealerIds(logonUser, Constant.DEALER_TYPE_DWR);
		if(!"".equals(dealerIds)){
			sql.append(" and A.dealer_id in (" +dealerIds+")");
		}
		sql.append(" ORDER BY A.CREATE_DATE DESC ");
		//modify by xiayanpeng end
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
		
	}
	/**
	 * 服务资料审批表的查询（经销商端）
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> searchAllServiceInfoForDealer(ServiceInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT D.DEALER_CODE,\n" );
		sql.append("       D.DEALER_NAME,\n" );
		sql.append("       A.ORDER_ID,\n" );
		sql.append("       TO_CHAR(A.APP_DATE,'YYYY-MM-DD') APP_DATE,\n" );
		sql.append("       A.MAIL_TYPE,\n" );
		sql.append("       A.STATUS\n" );
		sql.append("  FROM TT_IF_SERVICE_INFO A, TM_DEALER D\n" );
		sql.append(" WHERE A.DEALER_ID = D.DEALER_ID\n" );
		sql.append(" AND A.STATUS NOT IN (");
		sql.append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);
		sql.append(")\n");
		if(!"".equals(bean.getOrderId())){
			sql.append("   AND A.ORDER_ID like '%" );
			sql.append(bean.getOrderId());
			sql.append("%' \n");
		}
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND A.APP_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND A.APP_DATE <= TO_DATE('" );
			sql.append(bean.getEndTime());
			sql.append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		
		if(!"".equals(bean.getMailType())){
			sql.append("   AND A.MAIL_TYPE =" );
			sql.append(bean.getMailType());
		}
		
		if(!"".equals(bean.getDealerId())){
			sql.append("   AND A.DEALER_ID =");
			sql.append(bean.getDealerId());
		}
		
		if(!"".equals(bean.getStatus())){
			sql.append("   AND A.STATUS =");
			sql.append(bean.getStatus());
		}
		sql.append(" ORDER BY A.CREATE_DATE DESC ");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
		
	}
	/**
	 * 根据orgId查询服务资料查询明细表
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getAuditInfo(String orderId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TO_CHAR(A.AUDIT_DATE, 'YYYY-MM-DD') AUDIT_DATE, B.NAME, F.CODE_DESC AUDIT_STATUS, A.AUDIT_CONTENT, C.ORG_NAME ");
		sql.append("FROM TT_IF_SERVICE_INFO_AUDIT A, TC_USER B, TM_ORG C,tc_code f ");
		sql.append("WHERE A.AUDIT_BY = B.USER_ID ");
		sql.append("AND A.ORG_ID = C.ORG_ID ");
		sql.append("AND f.CODE_ID = A.AUDIT_STATUS ");
		//modify by xiayanpeng begin 去除状态过滤
		//sql.append("AND A.AUDIT_STATUS IN("); 
		//sql.append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_PASS).append(", ");
		//sql.append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT).append(") ") ;
		//modify by xiayanpeng end 
		sql.append("AND A.ORDER_ID = '").append(orderId).append("' "); 
		sql.append(" order by A.AUDIT_DATE desc");
		List<Map<String, Object>> auditList = 
		pageQuery(sql.toString(), null, getFunName());
		return auditList;
	}
	
	
	
	protected TtIfServiceInfoAuditPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
