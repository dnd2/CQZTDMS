package com.infodms.dms.dao.sales.customerInfoManage;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVehicleTestDriveInfoPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleTestDriveDAO extends BaseDao{
	public static final Logger logger = Logger.getLogger(VehicleTestDriveDAO.class);
	private static final VehicleTestDriveDAO dao = new VehicleTestDriveDAO();
	private static  POFactory factory = POFactoryBuilder.getInstance();
	
	public static VehicleTestDriveDAO getInstance() {
		return dao;
	}
	
	//试乘试驾客户信息添加 2013-2-6
	public void addVehicleTestDriveInfo(PO obj){
		insert(obj);
	}
	
	//试乘试驾初始化页面客户简单信息查询 2013-2-22
	public  PageResult<Map<String, Object>> QueryTestDriveCustomerInfo(String con ,String con1,String status,String dealerCodes,Long orgId,int curpage,int pagesize){
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct\n");
		sql.append(" dp.Drive_INFO_ID,\n");
		sql.append(" dp.ENTITY_CODE,\n");
		sql.append(" td.DEALER_SHORTNAME,\n");
		sql.append(" dp.CUSTOMER_NAME,\n");
		sql.append(" dp.CUSTOMER_NO,\n");
		sql.append(" dp.VEHICLE_DRIVE_NO,\n");
		sql.append(" tvm.COLOR_NAME,\n");   
		sql.append(" dp.MAIN_PHONE,\n");
		sql.append(" tvm.MATERIAL_CODE,\n"); 
		sql.append(" TO_CHAR(dp.TEST_DRIVE_DATE,'yyyy-MM-dd') TEST_DRIVE_DATE,\n");
		sql.append(" tc.CODE_DESC\n");
		sql.append(" from TT_Vehicle_Test_Drive_INFO dp,TM_VHCL_MATERIAL tvm,TC_CODE tc,VW_ORG_DEALER vod,TM_DEALER td\n");
		sql.append(" where dp.MATERIAL_ID = tvm.MATERIAL_ID and dp.KNOW_ADDRESS = tc.CODE_ID\n");
		sql.append(" and vod.ROOT_DEALER_NAME = td.DEALER_NAME\n");
		sql.append(" and dp.ENTITY_CODE = td.DEALER_CODE\n");
		
		if(!"".equals(dealerCodes)&&dealerCodes!=null){ //经销商code
			sql.append("  AND td.DEALER_CODE IN('"+dealerCodes+"')");
		}
		if(status.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){ //大区
			sql.append("  AND vod.ROOT_ORG_ID="+orgId);
		}
		if(status.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){ //小区
			sql.append("  AND vod.PQ_ORG_ID="+orgId);
		}
		if (con!=null&&!("").equals(con)){
			sql.append(con);
		}
		if (con1!=null&&!("").equals(con1)){
			sql.append(con1);
		}
		int flag = 0;
		sql.append("  AND dp.FLAG="+flag);
		sql.append(" order by TEST_DRIVE_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	
	
	//试乘试驾客户信息修改 2013-2-6 
	public int updateVehicleTestDriveInfo(TtVehicleTestDriveInfoPO dip) throws ParseException{
		
		//获取出生日期Date-->String
		Date Birthday =  dip.getBirthday() ;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String birthday = formatter.format(Birthday);

		//获取试车日期Date-->String
		Date test_Drive_Date =  dip.getTestDriveDate();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		String testTime = formatter1.format(test_Drive_Date);
		
		int flag = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_Vehicle_Test_Drive_INFO\n");
		sql.append("   SET CUSTOMER_NAME		='"+dip.getCustomerName()+"',\n");
		sql.append("   		SEX					='"+dip.getSex()+"',\n");
		sql.append("   		CARD_TYPE			='"+dip.getCardType()+"',\n");
		sql.append("   		CARD_NUM			='"+dip.getCardNum()+"',\n");
		sql.append("   		MAIN_PHONE			='"+dip.getMainPhone()+"',\n");
		sql.append("   		OTHER_PHONE			='"+dip.getOtherPhone()+"',\n");
		sql.append("   		EMAIL				='"+dip.getEmail()+"',\n");
		sql.append("   		POST_CODE			='"+dip.getPostCode()+"',\n");
		sql.append("   		BIRTHDAY	=TO_DATE('"+birthday+"','yyyy-MM-dd'),\n");
		sql.append("   		VEHICLE_DRIVE_NO	='"+dip.getVehicleDriveNo()+"',\n");
		sql.append("   		INCOME				='"+dip.getIncome()+"',\n");
		sql.append("   		EDUCATION			='"+dip.getEducation()+"',\n");
		sql.append("   		INDUSTRY			='"+dip.getIndustry()+"',\n");
		sql.append("   		IS_MARRIED			='"+dip.getIsMarried()+"',\n");
		sql.append("   		PROFESSION			='"+dip.getProfession()+"',\n");
		sql.append("   		JOB					='"+dip.getJob()+"',\n");
		sql.append("   		SALES_ADDRESS		='"+dip.getSalesAddress()+"',\n");
		sql.append("   		SALES_RESON			='"+dip.getSalesReson()+"',\n");
		sql.append("   		PROVINCE			='"+dip.getProvince()+"',\n");
		sql.append("   		CITY				='"+dip.getCity()+"',\n");
		sql.append("   		TOWN				='"+dip.getTown()+"',\n");
		sql.append("   		KNOW_ADDRESS		='"+dip.getKnowAddress()+"',\n");
		sql.append("   		MATERIAL_ID			='"+dip.getMaterialId()+"',\n");
		sql.append("   		MATERIAL_CODE		='"+dip.getMaterialCode()+"',\n");
		sql.append("   		ADDRESS				='"+dip.getAddress()+"',\n");
		sql.append("   		ENTITY_CODE			='"+dip.getEntityCode()+"',\n");
		sql.append("   		FLAG  				='"+flag+"',\n");
		sql.append("   		TEST_DRIVE_DATE	=TO_DATE('"+testTime+"','yyyy-MM-dd')\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("   AND DRIVE_INFO_ID = '"+dip.getDriveInfoId()+"'");
		int i = factory.update(sql.toString(), null);
		return i;
	}
	
	//删除试乘试驾客户信息 2013-2-8
	public int deleteVehicleTestDriveInfo(String driveInfoId) throws Exception{
		int flag = 1;
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_Vehicle_Test_Drive_INFO\n");
		sql.append("   SET FLAG		="+flag+"\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("   AND DRIVE_INFO_ID = '"+driveInfoId+"'");
		int i = factory.update(sql.toString(), null);
		return i;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 
}
