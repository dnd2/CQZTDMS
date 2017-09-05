package com.infodms.dms.dao.sales.financemanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class DerlerCreditDao extends BaseDao{

	public static Logger logger = Logger.getLogger(DerlerCreditDao.class);
	private static final DerlerCreditDao dao = new DerlerCreditDao();
	public static final DerlerCreditDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 从返利临时表中读取数据
	 * */
	public  List<Map<String, Object>> getDetailsTempDate(){
		StringBuffer sql = new StringBuffer("");
		sql.append("select t.contact_dept_id,t.contact_dept_code,t.pay_sum,t.remark,t.CONTACT_DEPT_SHORTNAME from TT_Dlr_Pay_Details_TEMP t  order by t.rom_num asc\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 通过经销商代码得到ID,简称
	 * */
	public List<Map<String, Object>> getDealerId(String dealerCode){
		StringBuffer sql = new StringBuffer("");
		sql.append("select t.dealer_id,t.dealer_shortname from tm_dealer t where  t.dealer_code ='"+ dealerCode+"'");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	
	/**
	 * 查询临时表中的重复项
	 * falg=true，通过经销商名字查找
	 * */
	public List<Map<String, Object>> getTempDealerRepeatData(boolean flag){
		StringBuffer sql = new StringBuffer("");
		if(flag){
			sql.append(" select t.rom_num, t.contact_dept_shortname from TT_Dlr_Pay_Details_TEMP t  where t.contact_dept_shortname in (select t.contact_dept_shortname from TT_Dlr_Pay_Details_TEMP t group by t.contact_dept_shortname having count(1) >= 2)");
		}
		else{
			sql.append(" select t.rom_num, t.contact_dept_code from TT_Dlr_Pay_Details_TEMP t  where t.contact_dept_code in ( select t.contact_dept_code from TT_Dlr_Pay_Details_TEMP t group by t.contact_dept_code having count(1) >= 2)");
		}
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	
	/***
	 * 查找经销商的返利帐号ID
	 */
	public List<Map<String, Object>> getDealerRebateAccount(){
		StringBuffer sql = new StringBuffer("");
		sql.append("select d.type_id from Tt_Vs_Account_Type d where d.type_code=2001");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
}
