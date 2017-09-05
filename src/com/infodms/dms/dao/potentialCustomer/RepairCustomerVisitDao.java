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
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class RepairCustomerVisitDao extends BaseDao  {

	public static final Logger logger = Logger.getLogger(RepairCustomerVisitDao.class);
	private static final  RepairCustomerVisitDao dao = new RepairCustomerVisitDao();
	private static  POFactory factory = POFactoryBuilder.getInstance();

	public static RepairCustomerVisitDao getInstance() {
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
		sql.append("select rcv.cus_id,\n" );
		sql.append("       rcv.customer_name,\n" );
		sql.append("       vmg.group_name group_code,\n" );
		sql.append("       rcv.three_guarantees,\n" );
		sql.append("       rcv.repair_item,\n" );
		sql.append("       rcv.no_visit_reason,\n" );
		sql.append("       rcv.phone,\n" );
		sql.append("       rcv.remark,\n" );
		sql.append("       rcv.visit_date,\n" );
		sql.append("       rcv.satisfied,\n" );
		sql.append("       rcv.no_satisfied,\n" );
		sql.append("       rcv.no_satisfied_reason,\n" );
		sql.append("       rcv.is_recommend,\n" );
		sql.append("       rcv.dealer_code,\n" );
		sql.append("       rcv.license_no,\n" );
		sql.append("       rcv.create_by,\n" );
		sql.append("       rcv.create_date,\n" );
		sql.append("       rcv.update_by,\n" );
		sql.append("       rcv.update_date\n" );
		sql.append("  from tt_repair_cus_visit rcv, tm_vhcl_material_group vmg\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and rcv.group_code = vmg.group_id");
		if (con!=null&&!("").equals(con)){
		  sql.append(con);
		}
		sql.append(" order by rcv.create_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
     
	public List<Map<String, Object>> qeruyDealerOrgReg(String dealerCode) {
		String sql="select distinct * from VW_ORG_DEALER_ALL VOD where VOD.dealer_Code ='"+dealerCode+"'";
		List list=(List) pageQuery(sql, null, getFunName());
		return list;
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
	 * 更新
	 */
	@SuppressWarnings("unchecked")
	public int update(TmPotentialCustomerPO pc) {        
		   StringBuffer sql= new StringBuffer();
			sql.append("UPDATE Tm_Potential_Customer\n" );
			sql.append("   SET Address            = '"+pc.getAddress()+"',\n" );
			sql.append("       Update_Date        = ? ,\n" );
			sql.append("       Zip_Code           = '"+pc.getZipCode()+"',\n" );
			sql.append("       Gender             = '"+pc.getGender()+"',\n" );
			sql.append("       Fax                = '"+pc.getFax()+"',\n" );
			sql.append("       Remark             = '"+pc.getRemark()+"',\n" );
			sql.append("       Customer_No        = '"+pc.getCustomerNo()+"',\n" );
			sql.append("       Entity_Code        = '"+pc.getEntityCode()+"',\n" );
			sql.append("       Customer_Name      = '"+pc.getCustomerName()+"',\n" );
			sql.append("       Customer_Type      = '"+pc.getCustomerType()+"',\n" );
			sql.append("       Ct_Code            = '"+pc.getCtCode()+"',\n" );
			sql.append("       Certificate_No     = '"+pc.getCertificateNo()+"',\n" );
			sql.append("       Province           = '"+pc.getProvince()+"',\n" );
			sql.append("       City               = '"+pc.getCity()+"',\n" );
			sql.append("       District           = '"+pc.getDistrict()+"',\n" );
			sql.append("       Contactor_Phone    = '"+pc.getContactorPhone()+"',\n" );
			sql.append("       Contactor_Mobile   = '"+pc.getContactorMobile()+"',\n" );
			sql.append("       E_Mail             = '"+pc.getEMail()+"',\n" );
			sql.append("       Owner_Marriage     = '"+pc.getOwnerMarriage()+"',\n" );
			sql.append("       Education_Level    = '"+pc.getEducationLevel()+"',\n" );
			sql.append("       Industry_First     = '"+pc.getIndustryFirst()+"',\n" );
			sql.append("       Vocation_Type      = '"+pc.getVocationType()+"',\n" );
			sql.append("       Position_Name      = '"+pc.getPositionName()+"',\n" );
			sql.append("       Family_Income      = '"+pc.getFamilyIncome()+"',\n" );
			sql.append("       Cus_Source         = '"+pc.getCusSource()+"',\n" );
			sql.append("       Media_Type         = '"+pc.getMediaType()+"',\n" );
			sql.append("       Buy_Purpose        = '"+pc.getBuyPurpose()+"',\n" );
			sql.append("       Init_Level         = '"+pc.getInitLevel()+"',\n" );
			sql.append("       Intent_Level       = '"+pc.getIntentLevel()+"',\n" );
			sql.append("       Sold_By            = '"+pc.getSoldBy()+"',\n" );
			sql.append("       Is_First_Buy       = '"+pc.getIsFirstBuy()+"',\n" );
			sql.append("       Has_Driver_License = '"+pc.getHasDriverLicense()+"' \n" );
			sql.append(" WHERE 1 = 1\n" );
			sql.append("   AND Customer_No = '"+pc.getCustomerNo()+"'");
		List list = new ArrayList();
		list.add(new Date());
         int i = factory.update(sql.toString(), list);
         return i;		
	}
	public List<TmRegionPO> getRegions(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select r.region_code,r.region_name from tm_region r where r.region_type = 10541002 \n");
		return this.select(TmRegionPO.class, sql.toString(), null);
	}
	/******
	 * 删除
	 */
	public int del(String CUS_ID){
		String sql = "delete tt_repair_cus_visit rcv where rcv.CUS_ID ='"+CUS_ID+"'";
		int i = factory.delete(sql, null);
		return i;
	}
}
