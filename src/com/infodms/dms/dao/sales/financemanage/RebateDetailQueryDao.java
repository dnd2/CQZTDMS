package com.infodms.dms.dao.sales.financemanage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class RebateDetailQueryDao extends BaseDao{
	
	
	public static Logger logger = Logger.getLogger(RebateDetailQueryDao.class);
	private static final RebateDetailQueryDao dao = new RebateDetailQueryDao();
	public static final RebateDetailQueryDao getInstance() {
		return dao;
	}
	
	
	//经销返利款查询初始页面查询 2013-3-5  经销商端
	 public PageResult<Map<String, Object>> dlrPayInquiryInitQuery(String con,String dealerId,int curpage,int pagesize){
			StringBuffer sql= new StringBuffer();
			sql.append("select distinct\n");
			sql.append(" td.DEALER_CODE,\n");
			sql.append(" td.DEALER_SHORTNAME,\n");
			sql.append(" tdpd.TICKET_NO,\n");
			sql.append(" tdpd.TICKET_ID,\n");
			sql.append(" tdpd.PAY_SUM,\n");
			sql.append(" td.DEALER_NAME,\n");
			sql.append(" TO_CHAR(tdpd.PAY_DATE,'yyyy-MM-dd') PAY_DATE,\n");
			sql.append(" tvat.TYPE_NAME\n");
			sql.append(" from TT_Dlr_Pay_Details tdpd,TM_DEALER td,Tt_Vs_Account_Type tvat\n");
			sql.append("where 1=1\n");
			//经销商id
			if(!"".equals(dealerId)&&dealerId!=null){ 
				sql.append("and tdpd.CONTACT_DEPT_ID IN('"+dealerId+"')\n");
			}
			sql.append(" and tdpd.CONTACT_DEPT_ID = td.DEALER_ID\n");
			sql.append(" and tdpd.ACCOUNT_TYPE_ID = tvat.TYPE_ID\n");
			
			if (con!=null&&!("").equals(con)){
				sql.append(con);
			}
			sql.append(" order by PAY_DATE desc ");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
			return ps;
		}
		
		//经销商返利查询初始页面查询 2013-3-5  车厂端
		public PageResult<Map<String, Object>> dlrPayInquiryDeptInitQuery(String con,String status,Long orgId,String dealerCodes,int curpage,int pagesize){
			StringBuffer sql= new StringBuffer();
			sql.append("select distinct\n");
			sql.append(" td.DEALER_CODE,\n");
			sql.append(" td.DEALER_SHORTNAME,\n");
			sql.append(" tdpd.TICKET_NO,\n");
			sql.append(" tdpd.TICKET_ID,\n");
			sql.append(" tdpd.PAY_SUM,\n");
			sql.append(" td.DEALER_NAME,\n");
			sql.append(" TO_CHAR(tdpd.PAY_DATE,'yyyy-MM-dd') PAY_DATE,\n");
			sql.append(" tvat.TYPE_NAME\n");
			sql.append(" from TT_Dlr_Pay_Details tdpd,TM_DEALER td,Tt_Vs_Account_Type tvat,VW_ORG_DEALER vod\n");
			sql.append(" where vod.ROOT_DEALER_NAME = td.DEALER_NAME\n");
			sql.append(" and vod.DEALER_ID = td.DEALER_ID\n"); 
			sql.append(" and tdpd.CONTACT_DEPT_ID = td.DEALER_ID\n");
			sql.append(" and tdpd.ACCOUNT_TYPE_ID = tvat.TYPE_ID\n");
			sql.append("  and tvat.type_code = 2001\n");//返利
			if(status.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){ //大区
				sql.append("  AND vod.ROOT_ORG_ID="+orgId);
			}
			if(status.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){ //小区
				sql.append("  AND vod.PQ_ORG_ID="+orgId);
			}
			//经销商代码
			if (dealerCodes != null && !"".equals(dealerCodes)) {
				sql.append(" and td.DEALER_CODE IN ('" + dealerCodes +"')");  
			}
			if (con!=null&&!("").equals(con)){
				sql.append(con);
			}
			sql.append(" order by PAY_DATE desc ");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
			return ps;
		}
		
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
