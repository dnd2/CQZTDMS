package com.infodms.dms.dao.sales.accountsmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DlrPayInquiryDAO extends BaseDao{

	public static final Logger logger = Logger.getLogger(DlrPayInquiryDAO.class);
	private static final DlrPayInquiryDAO dao = new DlrPayInquiryDAO();
	private static  POFactory factory = POFactoryBuilder.getInstance();
	
	public static DlrPayInquiryDAO getInstance() {
		return dao;
	}
	
	//经销商付款查询初始页面查询 2013-3-5  经销商端
	public PageResult<Map<String, Object>> dlrPayInquiryInitQuery(String con,String dealerId,int curpage,int pagesize,String accountsType){
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
		//帐户类型
		if(!"".equals(accountsType)&&accountsType!=null){ 
			sql.append("and tvat.TYPE_ID IN('"+accountsType+"')\n");
		}
		sql.append(" and tdpd.CONTACT_DEPT_ID = td.DEALER_ID\n");
		sql.append(" and tdpd.ACCOUNT_TYPE_ID = tvat.TYPE_ID\n");
		//sql.append("  and tvat.type_code = 2001\n");//返利
		if (con!=null&&!("").equals(con)){
			sql.append(con);
		}
		sql.append(" order by PAY_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	
	//经销商付款查询初始页面查询 2013-3-5  车厂端
	public PageResult<Map<String, Object>> dlrPayInquiryDeptInitQuery(String con,String status,Long orgId,String dealerCodes,int curpage,int pagesize,String accountsType){
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
		//sql.append(" and vod.DEALER_ID = td.DEALER_ID\n");
		sql.append(" and tdpd.CONTACT_DEPT_ID = td.DEALER_ID\n");
		sql.append(" and tdpd.ACCOUNT_TYPE_ID = tvat.TYPE_ID\n");
		//sql.append("  and tvat.type_code != 2001\n");//返利
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
		//帐户类型
		if (accountsType != null && !"".equals(accountsType)) {
			sql.append(" and tvat.TYPE_ID IN ('" + accountsType +"')");  
		}
		
		if (con!=null&&!("").equals(con)){
			sql.append(con);
		}
		sql.append(" order by PAY_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	//根据往来单位ID获取往来单位名称
	public String getContactDeptNameById(Long contactDeptId){
		TmDealerPO td = new TmDealerPO();
		td.setDealerId(contactDeptId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(td);
		TmDealerPO td1 = new TmDealerPO();
		td1 = (TmDealerPO)list.get(0);
		String contactDeptName = td1.getDealerName();
		return contactDeptName;
	}
	
	//根据账户类型ID获得账户类型名称
	public String getAccountNameById(Long accountTypeId){
		TtVsAccountTypePO tvat = new TtVsAccountTypePO();
		tvat.setTypeId(accountTypeId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(tvat);
		TtVsAccountTypePO tvat1 = new TtVsAccountTypePO();
		tvat1 = (TtVsAccountTypePO) list.get(0);
		String accountTypeName = tvat1.getTypeName();
		return accountTypeName;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 得到帐户类型 zxf 2017-7-18
	 * */
	public  List<Map<String, Object>> getAccountType(){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.type_id,t.type_name from Tt_Vs_Account_Type t");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		Map<String, Object> firstNull = new HashMap<String, Object>();//首选为空
		firstNull.put("", "");
		list.add(0, firstNull);
		return list;
	}
	
	
	/*
	 * 得到帐户类型不带返利 zxf 2017-7-18
	 * */
	public  List<Map<String, Object>> getAccountTypeNoReturn(){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.type_id,t.type_name from Tt_Vs_Account_Type t where t.type_id!=2014092297458400");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		Map<String, Object> firstNull = new HashMap<String, Object>();//首选为空
		firstNull.put("", "");
		list.add(0, firstNull);
		return list;
	}
	
	
	/*
	 * 得到详细 zxf 2017-7-18
	 * */
	public  List<Map<String, Object>> getPayDetail(String dealerId,String accountsType){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_Dlr_Pay_Details t where t.CONTACT_DEPT_ID = "+dealerId +" and t.ACCOUNT_TYPE_ID = "+ accountsType);
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	
	/*
	 * 销商账户余额查询  2017-7-18
	 * */
	public  List<Map<String, Object>> getDealerAccount(String dealerId,String accountsType){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.account_balance from Tt_Vs_Dealer_Account t  where t.dealer_id ="+ dealerId+" and t.account_id = "+accountsType);
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/*
	 * 设置销商账户  2017-7-18
	 * */
	public  List<Map<String, Object>> setDealerAccount(String dealerId,String accountsType,Long userId,Date date,Double balance){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE Tt_Vs_Dealer_Account  t SET t.account_balance ="+balance+",\n");
		sql.append("t.update_by ="+userId+",\n");
		sql.append("t.update_date ="+date+"\n");
		sql.append("where t.dealer_id ="+dealerId+"\n");
		sql.append("and t.account_id ="+accountsType+"\n");
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
