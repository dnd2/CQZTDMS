package com.infodms.dms.actions.sales.customerInfoManage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.customerInfoManage.VehicleTestDriveDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVehicleTestDriveInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleTestDrive extends BaseDao{
	public Logger logger = Logger.getLogger(VehicleTestDrive.class);
	private ActionContext act = ActionContext.getContext();
	private VehicleTestDriveDAO dao = VehicleTestDriveDAO.getInstance() ;
	
	private final String vehicleTestDriveQueryInitUrl = "/jsp/sales/customerInfoManage/vehicleTestDrive.jsp";
	private final String addVehicleTestDriveInitUrl = "/jsp/sales/customerInfoManage/vehicleTestDriveAdd.jsp";
	private final String updateVehicleTestDriveInitUrl = "/jsp/sales/customerInfoManage/vehicleTestDriveUpdate.jsp";
	private final String queryVehicleTestDrive = "/jsp/sales/customerInfoManage/vehicleTestDriveQuery.jsp";
	
	//试乘试驾页面初始化 2013-2-4
	public void vehicleTestDriveQueryInit(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
			String dealerIds = logonUser.getDealerId();
			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
			act.setOutData("dealerList", dealerList);
			act.setForword(vehicleTestDriveQueryInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾页面 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	//试乘试驾添加初始化 2013-2-4
	public void addVehicleTestDriveInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(addVehicleTestDriveInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾添加页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	//试乘试驾修改初始化 2013-2-4
	public void updateVehicleTestDriveInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String driveInfoId = request.getParamValue("DRIVE_INFO_ID");
		
			TtVehicleTestDriveInfoPO dp = new TtVehicleTestDriveInfoPO();
			dp.setDriveInfoId(Long.parseLong(driveInfoId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(dp);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setForword(updateVehicleTestDriveInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾修改页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//删除试乘试驾客户信息(经销商) 2013-2-8
	public void deleteVehicleTestDrive(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String driveInfoId = CommonUtils.checkNull(request.getParamValue("DRIVE_INFO_ID"));
			int i = dao.deleteVehicleTestDriveInfo(driveInfoId);
			if(i==1){
				act.setOutData("flag", true);
			}else {
				act.setOutData("flag", false);	
			}		
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"试乘试驾客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	//试乘试驾初始化页面客户简单信息查询 2013-2-22
	public void vehicleTestDriveQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			StringBuffer con1 = new StringBuffer();
			Long orgId= logonUser.getOrgId(); //组织ID
			String dutyType = logonUser.getDutyType(); //组织类型
			String dealerCodes=""; //单个经销商code
			String dealerCode=""; //多个经销商code
			String customerName = request.getParamValue("customer_Name");
			String vehicleDriveNo = request.getParamValue("driveNo");
			String materialId = request.getParamValue("materialId"); 
			String codes = request.getParamValue("dealerCodes");
			//处理经销商code
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
				dealerCodes=logonUser.getDealerCode();
			}else{
				dealerCode=codes;
				if("".equals(dealerCode)||dealerCode==null){
					dealerCodes=dealerCode;
				}else{
					dealerCodes=dealerCode.replace(",", "','");
				}
			}
			TtVehicleTestDriveInfoPO dp = new TtVehicleTestDriveInfoPO();

			//客户姓名  
			if (customerName != null && !"".equals(customerName)) {
				con.append(" and dp.CUSTOMER_NAME like '%" + customerName +"%'");  
			}
			//机动车驾照号
			if (vehicleDriveNo != null && !"".equals(vehicleDriveNo)) {
				con.append(" and dp.VEHICLE_DRIVE_NO like '%" + vehicleDriveNo + "%'");  
			}
			//物料id
			if (materialId != null && !"".equals(materialId)) {
				con1.append(" and dp.MATERIAL_ID ='" + materialId + "' ");  
			}
			PageResult<Map<String, Object>> ps = dao.QueryTestDriveCustomerInfo(con.toString(), con1.toString(), dutyType, dealerCodes, orgId, curPage, 10);
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾客户信息(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//试乘试驾新增信息(经销商) 2013-2-5
	@SuppressWarnings("unchecked")
	public void addVehicleTestDrive() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String customerName = CommonUtils.checkNull(request.getParamValue("ctm_name"));  //客户姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex")); //性别
			String cardType = CommonUtils.checkNull(request.getParamValue("card_type")); //证件类别
			String cardNum = CommonUtils.checkNull(request.getParamValue("card_num"));//证件号码
			String mainPhone = CommonUtils.checkNull(request.getParamValue("main_phone"));//主要联系电话
			String otherPhone = CommonUtils.checkNull(request.getParamValue("other_phone"));//其他联系电话
			String email = CommonUtils.checkNull(request.getParamValue("email"));//电子邮件
			String postCode = CommonUtils.checkNull(request.getParamValue("post_code"));//邮编
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));//出生年月
			String vehicleDriveNo = CommonUtils.checkNull(request.getParamValue("vichleNo"));//机动车驾车号
			String income = CommonUtils.checkNull(request.getParamValue("income"));//家庭月收入
			String education = CommonUtils.checkNull(request.getParamValue("education"));//教育程度
			String industry = CommonUtils.checkNull(request.getParamValue("industry"));//所在行业
			String isMarried = CommonUtils.checkNull(request.getParamValue("is_married"));//婚姻状况
			String profession = CommonUtils.checkNull(request.getParamValue("profession"));//职业
			String job = CommonUtils.checkNull(request.getParamValue("job"));//职务
			String salesAddress = CommonUtils.checkNull(request.getParamValue("salesaddress"));//购买用途
			String salesReson = CommonUtils.checkNull(request.getParamValue("salesreson"));//购买原因
			String province = CommonUtils.checkNull(request.getParamValue("province1"));//省份
			String city = CommonUtils.checkNull(request.getParamValue("city1"));//地级市
			String town = CommonUtils.checkNull(request.getParamValue("district1"));//区、县
			String knowAddress = CommonUtils.checkNull(request.getParamValue("ctm_form"));//了解途径
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId"));//欲购车型(物料id)
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));//欲购车型(物料code)
			String testDriveDate = CommonUtils.checkNull(request.getParamValue("testDriveDate")); //试车日期
			String address  = CommonUtils.checkNull(request.getParamValue("address"));//详细地址
			String feeling=CommonUtils.checkNull(request.getParamValue("feeling"));//试驾体验感受
		
			//处理出生日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday1=df.parse(birthday);
			//处理试车日期
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			Date testDriveDate1=df1.parse(testDriveDate);
			
			Integer showFlag = 0; //设置标志位，用于页面数据的显示
			Long driveInfoId=0L;
			TtVehicleTestDriveInfoPO dip = new TtVehicleTestDriveInfoPO();
			driveInfoId = Utility.getLong(SequenceManager.getSequence(""));
			String customerNo = "PU"+driveInfoId;
			dip.setDriveInfoId(driveInfoId);
			dip.setCustomerNo(customerNo);
			dip.setEntityCode(dealerCode);
			dip.setCustomerName(customerName);
			dip.setSex(Integer.parseInt(sex));
			dip.setCardType(Integer.parseInt(cardType));
			dip.setCardNum(cardNum);
			dip.setMainPhone(mainPhone);
			dip.setOtherPhone(otherPhone);
			dip.setEmail(email);
			dip.setPostCode(postCode);
			dip.setBirthday(birthday1);
			dip.setVehicleDriveNo(vehicleDriveNo);
			dip.setIncome(Integer.parseInt(income));
			dip.setEducation(Integer.parseInt(education));
			dip.setIndustry(Integer.parseInt(industry));
			dip.setIsMarried(Integer.parseInt(isMarried));
			dip.setProfession(Integer.parseInt(profession));
			dip.setJob(job);
			dip.setSalesAddress(Integer.parseInt(salesAddress));
			dip.setSalesReson(Integer.parseInt(salesReson));
			dip.setProvince(Long.parseLong(province));
			dip.setCity(Long.parseLong(city));
			dip.setTown(Long.parseLong(town));
			dip.setKnowAddress(Long.parseLong(knowAddress));
			dip.setMaterialId(Long.parseLong(materialId));
			dip.setMaterialCode(materialCode);
			dip.setTestDriveDate(testDriveDate1);
			dip.setAddress(address);
			dip.setFlag(showFlag);
			dip.setDriveFeeling(feeling);
			dao.addVehicleTestDriveInfo(dip); 
			act.setOutData("flag", true);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"试乘试驾客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//试乘试驾信息查看(车厂端) 2013-2-22
	public void queryVehicleTestDrive(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String dealerIds=logonUser.getDealerId();
			RequestWrapper request = act.getRequest();
			String driveInfoId=request.getParamValue("DRIVE_INFO_ID");
			TtVehicleTestDriveInfoPO dp = new TtVehicleTestDriveInfoPO();
			dp.setDriveInfoId(Long.parseLong(driveInfoId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(dp);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setForword(queryVehicleTestDrive);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"试乘试驾客户信息(车厂端)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//试乘试驾修改信息(经销商) 2013-2-6
	@SuppressWarnings("unchecked")
	public void updateVehicleTestDrive(){
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String driveInfoId = CommonUtils.checkNull(request.getParamValue("driveInfoId"));
			String customerName = CommonUtils.checkNull(request.getParamValue("ctm_name"));  //客户姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex")); //性别
			String cardType = CommonUtils.checkNull(request.getParamValue("card_type")); //证件类别
			String cardNum = CommonUtils.checkNull(request.getParamValue("card_num"));//证件号码
			String mainPhone = CommonUtils.checkNull(request.getParamValue("main_phone"));//主要联系电话
			String otherPhone = CommonUtils.checkNull(request.getParamValue("other_phone"));//其他联系电话
			String email = CommonUtils.checkNull(request.getParamValue("email"));//电子邮件
			String postCode = CommonUtils.checkNull(request.getParamValue("post_code"));//邮编
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));//出生年月
			String vehicleDriveNo = CommonUtils.checkNull(request.getParamValue("vichleNo"));//机动车驾车号
			String income = CommonUtils.checkNull(request.getParamValue("income"));//家庭月收入
			String education = CommonUtils.checkNull(request.getParamValue("education"));//教育程度
			String industry = CommonUtils.checkNull(request.getParamValue("industry"));//所在行业
			String isMarried = CommonUtils.checkNull(request.getParamValue("is_married"));//婚姻状况
			String profession = CommonUtils.checkNull(request.getParamValue("profession"));//职业
			String job = CommonUtils.checkNull(request.getParamValue("job"));//职务
			String salesAddress = CommonUtils.checkNull(request.getParamValue("salesaddress"));//购买用途
			String salesReson = CommonUtils.checkNull(request.getParamValue("salesreson"));//购买原因
			String province = CommonUtils.checkNull(request.getParamValue("province1"));//省份
			String city = CommonUtils.checkNull(request.getParamValue("city1"));//地级市
			String town = CommonUtils.checkNull(request.getParamValue("district1"));//区、县
			String knowAddress = CommonUtils.checkNull(request.getParamValue("ctm_form"));//了解途径
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId"));//欲购车型(物料id)
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));//欲购车型(物料code)
			String testDriveDate = CommonUtils.checkNull(request.getParamValue("testDriveDate")); //试车日期
			String address  = CommonUtils.checkNull(request.getParamValue("address"));//详细地址
			String feeling=CommonUtils.checkNull(request.getParamValue("feeling"));//试驾体验感受
			
			//处理出生日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday1 = new Date();
			birthday1 = df.parse(birthday);
			//处理试车日期
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date testDriveDate1 = new Date();
			testDriveDate1=df1.parse(testDriveDate);
			
			TtVehicleTestDriveInfoPO dip = new TtVehicleTestDriveInfoPO();
			dip.setDriveInfoId(Long.parseLong(driveInfoId));
			dip.setEntityCode(dealerCode);
			dip.setCustomerName(customerName);
			dip.setSex(Integer.parseInt(sex));
			dip.setCardType(Integer.parseInt(cardType));
			dip.setCardNum(cardNum);
			dip.setMainPhone(mainPhone);
			dip.setOtherPhone(otherPhone);
			dip.setEmail(email);
			dip.setPostCode(postCode);
			dip.setBirthday(birthday1);
			dip.setVehicleDriveNo(vehicleDriveNo);
			dip.setIncome(Integer.parseInt(income));
			dip.setEducation(Integer.parseInt(education));
			dip.setIndustry(Integer.parseInt(industry));
			dip.setIsMarried(Integer.parseInt(isMarried));
			dip.setProfession(Integer.parseInt(profession));
			dip.setJob(job);
			dip.setSalesAddress(Integer.parseInt(salesAddress));
			dip.setSalesReson(Integer.parseInt(salesReson));
			dip.setProvince(Long.parseLong(province));
			dip.setCity(Long.parseLong(city));
			dip.setTown(Long.parseLong(town));
			dip.setKnowAddress(Long.parseLong(knowAddress));
			dip.setMaterialId(Long.parseLong(materialId));
			dip.setMaterialCode(materialCode);
			dip.setTestDriveDate(testDriveDate1);
			dip.setAddress(address);
			dip.setDriveFeeling(feeling);
			
			int i = dao.updateVehicleTestDriveInfo(dip);
			if(i==1){
				act.setOutData("flag",true);
			}else{
				act.setOutData("flag", false);
			}
			act.setForword(vehicleTestDriveQueryInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"试乘试驾客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
 
	
}
