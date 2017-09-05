package com.infodms.dms.dao.potentialCustomer;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ComplaintCustomerTrackDao extends BaseDao  {

	public static final Logger logger = Logger.getLogger(ComplaintCustomerTrackDao.class);
	private static final  ComplaintCustomerTrackDao dao = new ComplaintCustomerTrackDao();
	private static  POFactory factory = POFactoryBuilder.getInstance();

	public static ComplaintCustomerTrackDao getInstance() {
		return dao;
	}
	
	

	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtAsRepairOrderProblemPO>    返回类型 
	* @throws
	 */
	public  PageResult<Map<String, Object>> applyQuery(String con ,int curpage,int pagesize) {		
			StringBuffer sql= new StringBuffer();
		    sql.append("select cct.cus_id,\n" );
		    sql.append("       cct.customer_name,\n" );
		    sql.append("       vmg.group_name group_code,\n" );
		    sql.append("       cct.phone,\n" );
		    sql.append("       cct.dealer_code,\n" );
			sql.append("       cct.license_no,\n" );
			sql.append("       cct.times,\n" );
			sql.append("       cct.track_date,\n" );
			sql.append("       cct.complaint_record,\n" );
			sql.append("       cct.deal_with,\n" );
			sql.append("       cct.cus_request,\n" );
			sql.append("        cct.create_by,\n" );
			sql.append("       cct.create_date,\n" );
			sql.append("       cct.update_by,\n" );
			sql.append("       cct.update_date\n" );
			sql.append("  from tt_complaint_customer_track cct,tm_vhcl_material_group vmg \n" );
			sql.append(" where 1 = 1\n" );
			sql.append("   and cct.group_code = vmg.group_id");
		if (con!=null&&!("").equals(con)){
		  sql.append(con);
		}
		sql.append(" order by cct.create_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/******
	 * 新增
	 */
	public void insert(TmPotentialCustomerPO pc) {        
		   factory.insert(pc);	
		}
	
	/******
	 * 删除
	 */
	public int del(String CUS_ID){
		String sql = "delete tt_complaint_customer_track cct where cct.CUS_ID ='"+CUS_ID+"'";
		int i = factory.delete(sql, null);
		return i;
	}
}
