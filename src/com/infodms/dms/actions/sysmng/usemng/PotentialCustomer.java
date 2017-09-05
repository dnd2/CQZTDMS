
/**********************************************************************
* <pre>
* FILE : PotentialCustomer.java
* CLASS : PotentialCustomer
* 
* AUTHOR : YH
*
* FUNCTION : 潜在客户管理Action.
*-----------------------------------------------------------
*|2011-04-22| 
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.usemng;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.potentialCustomer.PotentialCustomerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PotentialCustomer {
    
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	public Logger logger = Logger.getLogger(PotentialCustomer.class);
	private final String queryPotentialCustomerInitUrl = "/jsp/systemMng/userMng/PotentialCustomerSearch.jsp";
	private final String queryPotentialCustomerOemInitUrl = "/jsp/systemMng/userMng/PotentialCustomerOemSearch.jsp";
	private final String addPotentialCustomerInitUrl = "/jsp/systemMng/userMng/PotentialCustomerAdd.jsp";
	private final String modfiPotentialCustomerInitUrl = "/jsp/systemMng/userMng/PotentialCustomerUpdate.jsp";
	private final String PotentialCustomerDetailUrl = "/jsp/systemMng/userMng/PotentialCustomerDetail.jsp";
	private final String MoreLinkmanUrl = "/jsp/systemMng/userMng/morelinkman.jsp";

    private PotentialCustomerDao dao = PotentialCustomerDao.getInstance();
	/**
	 * 潜在客户查询初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryPotentialCustomerInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryPotentialCustomerInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"潜在客户查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 潜在客户查询初始化(Oem端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryPotentialCustomerOemInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
		    Calendar calendar = Calendar.getInstance(); //昨天的日期
		    calendar.add(Calendar.DATE,-1);
 			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String sd  = dateFormat.format(calendar.getTime());
  			act.setOutData("date", sd);
			act.setForword(queryPotentialCustomerOemInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"潜在客户查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 潜在客户添加初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPotentialCustomerInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();
			/**取得该经销商销售顾问列表**/
			String sql = "SELECT ID,NAME FROM TT_SALES_CONSULTANT WHERE DEALER_ID = "+dealerId+" AND STATUS = "+Constant.SALES_CONSULTANT_STATUS_PASS;
			List<Map<String, Object>> list = dao.pageQuery(sql, null, dao.getFunName());
			act.setOutData("list", list);
			/**取得所有的车系列表**/
			String series_sql = "SELECT GROUP_ID,GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_LEVEL = 2";
			List<Map<String, Object>> series_list = dao.pageQuery(series_sql, null, dao.getFunName());
			act.setOutData("series_list", series_list);
			act.setForword(addPotentialCustomerInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"潜在客户添加初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户修改初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiPotentialCustomerInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String customerNo = request.getParamValue("CUSTOMER_NO");
			String sql_ = "SELECT ID,NAME FROM TT_SALES_CONSULTANT WHERE DEALER_ID = "+logonUser.getDealerId()+" AND STATUS = "+Constant.SALES_CONSULTANT_STATUS_PASS;
			List<Map<String, Object>> list_ = dao.pageQuery(sql_, null, dao.getFunName());
			act.setOutData("list_", list_);
			/**取得所有的车系列表**/
			String series_sql = "SELECT GROUP_ID,GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_LEVEL = 2";
			List<Map<String, Object>> series_list = dao.pageQuery(series_sql, null, dao.getFunName());
			act.setOutData("series_list", series_list);
			TmPotentialCustomerPO pc = new TmPotentialCustomerPO();
			pc.setCustomerNo(customerNo);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		    String sql = "SELECT * FROM Tm_Potential_Customer  WHERE  1=1 AND Customer_No='"+customerNo+"'";
			list = dao.pageQuery(sql, null,null);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
				if(list.get(0).get("GROUP_ID") != null && !"".equals(list.get(0).get("GROUP_ID").toString())){
					String color_sql = "SELECT COLOR_CODE,COLOR_NAME FROM VW_MATERIAL_GROUP_MAT WHERE SERIES_ID = "+list.get(0).get("GROUP_ID").toString()
								+"GROUP BY COLOR_CODE,COLOR_NAME";
					List<Map<String, Object>> color_list = dao.pageQuery(color_sql, null, dao.getFunName());
					act.setOutData("color_list", color_list);
				}
			}
			
			
			act.setForword(modfiPotentialCustomerInitUrl);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"跳转修改潜在客户页面失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户详细查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getPotentialCustomerDetail() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String customerNo = request.getParamValue("CUSTOMER_NO");
			String command = request.getParamValue("command");//标示符，区分查看请求来自上端还是下端
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT  PC.*,TR1.REGION_NAME PROVINCE_NAME,VMG.GROUP_NAME,TSC.NAME SOLD_BY_NAME,\n");
			sbSql.append("        TR2.REGION_NAME CITY_NAME,\n");
			sbSql.append("        TR3.REGION_NAME DISTRICT_NAME\n");
			sbSql.append("FROM    TM_POTENTIAL_CUSTOMER PC,\n");
			sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
			sbSql.append("        TT_SALES_CONSULTANT TSC,\n");
			sbSql.append("        TM_REGION TR1,\n");
			sbSql.append("        TM_REGION TR2,\n");
			sbSql.append("        TM_REGION TR3\n");
			sbSql.append("WHERE   PC.GROUP_ID = VMG.GROUP_ID(+)\n");
			sbSql.append("AND     PC.SOLD_BY = TSC.ID(+)\n");
			sbSql.append("AND     PC.PROVINCE = TR1.REGION_CODE(+)\n");
			sbSql.append("AND     PC.CITY = TR2.REGION_CODE(+)\n");
			sbSql.append("AND     PC.DISTRICT = TR3.REGION_CODE(+) AND PC.CUSTOMER_NO = '"+customerNo+"'\n"); 
			List<Map<String, Object>> list  = dao.pageQuery(sbSql.toString(), null,null);
			if(list != null && list.size()>0){
				act.setOutData("map",list.get(0));
				BigDecimal groupId = (BigDecimal)list.get(0).get("GROUP_ID");
			}
			act.setOutData("command", command);
			act.setForword(PotentialCustomerDetailUrl);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户信息查看");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户修改提交
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void modfiPotentialCustomer() {
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String customerNo = CommonUtils.checkNull(request.getParamValue("CUSTOMER_NO"));//客户编号
			String customerType = 	CommonUtils.checkNull(request.getParamValue("CUSTOMER_TYPE"));//客户类型
			String customerName =	CommonUtils.checkNull(request.getParamValue("customer_Name"));//客户姓名
			String GENDER  =  CommonUtils.checkNull(request.getParamValue("GENDER"));//性别
			String ctCode	= CommonUtils.checkNull(request.getParamValue("DICT_CERTIFICATE"));//证件类型
			String	certificateNo= CommonUtils.checkNull(request.getParamValue("certificateNo"));//证件号码
			String	province= CommonUtils.checkNull(request.getParamValue("province"));//省份
			String	city= CommonUtils.checkNull(request.getParamValue("city"));//城市
			String	district= CommonUtils.checkNull(request.getParamValue("COUNTIES"));//区县
			String	zipCode= CommonUtils.checkNull(request.getParamValue("zipCode"));//邮编
			String	address= CommonUtils.checkNull(request.getParamValue("address"));//地址
			String	contactorPhone= CommonUtils.checkNull(request.getParamValue("contactorPhone"));//联系电话
			String	contactorMobile= CommonUtils.checkNull(request.getParamValue("contactorMobile"));//联系手机
			String	fax= CommonUtils.checkNull(request.getParamValue("fax"));//传真
			String	email= CommonUtils.checkNull(request.getParamValue("email"));//E-MAIL
			String	ownerMarriage= CommonUtils.checkNull(request.getParamValue("MARRIAGE"));//婚姻状况
			String	educationLevel= CommonUtils.checkNull(request.getParamValue("EDUCATION"));//教育水平
			String	industryFirst= CommonUtils.checkNull(request.getParamValue("TRADE_TYPE"));//行业大类
			String	vocationType= CommonUtils.checkNull(request.getParamValue("PROFESSION"));//职业
			String	positionName= CommonUtils.checkNull(request.getParamValue("positionName"));//职务名称
			String	familyIncome= CommonUtils.checkNull(request.getParamValue("familyIncome"));//家庭月收入
			String	cusSource= CommonUtils.checkNull(request.getParamValue("cusSource"));//客户来源
			String	mediaType= CommonUtils.checkNull(request.getParamValue("mediaType"));//媒体类型
			String	buyPurpose= CommonUtils.checkNull(request.getParamValue("buyPurpose"));//购车目的
			String	initLevel= CommonUtils.checkNull(request.getParamValue("initLevel"));//初始级别
			String	intentLevel= CommonUtils.checkNull(request.getParamValue("intentLevel"));//意向级别
			String	soldBy= CommonUtils.checkNull(request.getParamValue("soldBy"));//销售顾问
			String	isFirstBuy= CommonUtils.checkNull(request.getParamValue("isFirstBuy"));//是否首次购车
			String	hasDriverLicense= CommonUtils.checkNull(request.getParamValue("hasDriverLicense"));//是否有驾照
			String	remark= CommonUtils.checkNull(request.getParamValue("remark"));//备注
			String isTryDrive = CommonUtils.checkNull(request.getParamValue("isTryDrive")); //是否试驾
			String isFirstCome = CommonUtils.checkNull(request.getParamValue("isFirstCome"));//是否首次来店
			String customerNum = CommonUtils.checkNull(request.getParamValue("customerNum")); //来店人数
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));//拟购车系
			String colorName = CommonUtils.checkNull(request.getParamValue("colorName")); //选择颜色
			String comeTime = CommonUtils.checkNull(request.getParamValue("comeTime")); //来店时间
			String leaveTime = CommonUtils.checkNull(request.getParamValue("leaveTime")); //离店时间
			String stayMinute = CommonUtils.checkNull(request.getParamValue("stayMinute")); //在店时长
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//更新的时候
			TmPotentialCustomerPO pc = new TmPotentialCustomerPO();
			pc.setCustomerNo(customerNo);
			pc.setEntityCode(dealerCode);
			pc.setCustomerName(customerName);
			pc.setCustomerType(Integer.parseInt(customerType));
			pc.setGender(Integer.parseInt(GENDER));
			pc.setCtCode(Integer.parseInt(ctCode));
			pc.setCertificateNo(certificateNo);
			pc.setProvince(province);
			pc.setCity(city);
			pc.setDistrict(district);
			pc.setZipCode(zipCode);
			pc.setAddress(address);
			pc.setContactorPhone(contactorPhone);
			pc.setContactorMobile(contactorMobile);
			pc.setFax(fax);
			pc.setEMail(email);
			pc.setOwnerMarriage(Integer.parseInt(ownerMarriage));
			pc.setEducationLevel(Integer.parseInt(educationLevel));
			pc.setIndustryFirst(industryFirst);
			pc.setVocationType(Integer.parseInt(vocationType));
			pc.setPositionName(positionName);
			pc.setFamilyIncome(Integer.parseInt(familyIncome));
			pc.setCusSource(Integer.parseInt(cusSource));
			pc.setMediaType(Integer.parseInt(mediaType));
			pc.setBuyPurpose(Integer.parseInt(buyPurpose));
			pc.setInitLevel(Integer.parseInt(initLevel));
			pc.setIntentLevel(Integer.parseInt(intentLevel));
			if(soldBy != null && !"".equals(soldBy)){
				pc.setSoldBy(Long.valueOf(soldBy));
			}
			pc.setIsFirstBuy(Integer.parseInt(isFirstBuy));
			pc.setHasDriverLicense(Integer.parseInt(hasDriverLicense));
			pc.setRemark(remark);
			pc.setIsTryDrive(Long.valueOf(isTryDrive));
			pc.setIsFirstCome(Long.valueOf(isFirstCome));
			pc.setCustomerNum(Integer.valueOf(customerNum));
			pc.setGroupId(Long.valueOf(groupId));
			pc.setColorName(colorName);
			if(!"".equals(comeTime)){
				Date d1 = sdf.parse(comeTime);
				pc.setComeTime(d1);
			}
			if(!"".equals(leaveTime)){
				Date d2 = sdf.parse(leaveTime);
				pc.setLeaveTime(d2);
			}
			if(!"".equals(stayMinute)){
				pc.setStayMinute(Integer.valueOf(stayMinute));
			}
			pc.setUpdateDate(new Date());
		
	
            int i = dao.updatePotentialCustomer(pc);
            if(i==1){
    			act.setOutData("flag", true);
            }else {
            	act.setOutData("flag", false);
            }
			act.setForword(queryPotentialCustomerInitUrl);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改潜在客户失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addPotentialCustomer() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String customerType = 	CommonUtils.checkNull(request.getParamValue("CUSTOMER_TYPE"));//客户类型
			String customerName =	CommonUtils.checkNull(request.getParamValue("customer_Name"));//客户姓名
			String GENDER  =  CommonUtils.checkNull(request.getParamValue("GENDER"));//性别
			String ctCode	= CommonUtils.checkNull(request.getParamValue("DICT_CERTIFICATE"));//证件类型
			String	certificateNo= CommonUtils.checkNull(request.getParamValue("certificateNo"));//证件号码
			String	province= CommonUtils.checkNull(request.getParamValue("province"));//省份
			String	city= CommonUtils.checkNull(request.getParamValue("city"));//城市
			String	district= CommonUtils.checkNull(request.getParamValue("COUNTIES"));//区县
			String	zipCode= CommonUtils.checkNull(request.getParamValue("zipCode"));//邮编
			String	address= CommonUtils.checkNull(request.getParamValue("address"));//地址
			String	contactorPhone= CommonUtils.checkNull(request.getParamValue("contactorPhone"));//联系电话
			String	contactorMobile= CommonUtils.checkNull(request.getParamValue("contactorMobile"));//联系手机
			String	fax= CommonUtils.checkNull(request.getParamValue("fax"));//传真
			String	email= CommonUtils.checkNull(request.getParamValue("email"));//E-MAIL
			String	ownerMarriage= CommonUtils.checkNull(request.getParamValue("MARRIAGE"));//婚姻状况
			String	educationLevel= CommonUtils.checkNull(request.getParamValue("EDUCATION"));//教育水平
			String	industryFirst= CommonUtils.checkNull(request.getParamValue("TRADE_TYPE"));//行业大类
			String	vocationType= CommonUtils.checkNull(request.getParamValue("PROFESSION"));//职业
			String	positionName= CommonUtils.checkNull(request.getParamValue("positionName"));//职务名称
			String	familyIncome= CommonUtils.checkNull(request.getParamValue("familyIncome"));//家庭月收入
			String	cusSource= CommonUtils.checkNull(request.getParamValue("cusSource"));//客户来源
			String	mediaType= CommonUtils.checkNull(request.getParamValue("mediaType"));//媒体类型
			String	buyPurpose= CommonUtils.checkNull(request.getParamValue("buyPurpose"));//购车目的
			String	initLevel= CommonUtils.checkNull(request.getParamValue("initLevel"));//初始级别
			String	intentLevel= CommonUtils.checkNull(request.getParamValue("intentLevel"));//意向级别
			String	soldBy= CommonUtils.checkNull(request.getParamValue("soldBy"));//销售顾问
			String	isFirstBuy= CommonUtils.checkNull(request.getParamValue("isFirstBuy"));//是否首次购车
			String	hasDriverLicense= CommonUtils.checkNull(request.getParamValue("hasDriverLicense"));//是否有驾照
			String	remark= CommonUtils.checkNull(request.getParamValue("remark"));//备注
			String isTryDrive = CommonUtils.checkNull(request.getParamValue("isTryDrive")); //是否试驾
			String isFirstCome = CommonUtils.checkNull(request.getParamValue("isFirstCome"));//是否首次来店
			String customerNum = CommonUtils.checkNull(request.getParamValue("customerNum")); //来店人数
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));//拟购车系
			String colorName = CommonUtils.checkNull(request.getParamValue("colorName")); //选择颜色
			String comeTime = CommonUtils.checkNull(request.getParamValue("comeTime")); //来店时间
			String leaveTime = CommonUtils.checkNull(request.getParamValue("leaveTime")); //离店时间
			String stayMinute = CommonUtils.checkNull(request.getParamValue("stayMinute")); //在店时长
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			//新增的时候
			Long id=0L;
			TmPotentialCustomerPO pc = new TmPotentialCustomerPO();
			id = Utility.getLong(SequenceManager.getSequence(""));
			pc.setId(id);
			pc.setDealerId(Long.valueOf(logonUser.getDealerId()));
			pc.setCustomerNo("PU"+id);
			pc.setEntityCode(dealerCode);
			pc.setCustomerName(customerName);
			pc.setCustomerType(Integer.parseInt(customerType));
			pc.setGender(Integer.parseInt(GENDER));
			pc.setCtCode(Integer.parseInt(ctCode));
			pc.setCertificateNo(certificateNo);
			pc.setProvince(province);
			pc.setCity(city);
			pc.setDistrict(district);
			pc.setZipCode(zipCode);
			pc.setAddress(address);
			pc.setContactorPhone(contactorPhone);
			pc.setContactorMobile(contactorMobile);
			pc.setFax(fax);
			pc.setEMail(email);
			pc.setOwnerMarriage(Integer.parseInt(ownerMarriage));
			pc.setEducationLevel(Integer.parseInt(educationLevel));
			pc.setIndustryFirst(industryFirst);
			pc.setVocationType(Integer.parseInt(vocationType));
			pc.setPositionName(positionName);
			pc.setFamilyIncome(Integer.parseInt(familyIncome));
			pc.setCusSource(Integer.parseInt(cusSource));
			pc.setMediaType(Integer.parseInt(mediaType));
			pc.setBuyPurpose(Integer.parseInt(buyPurpose));
			pc.setInitLevel(Integer.parseInt(initLevel));
			pc.setIntentLevel(Integer.parseInt(intentLevel));
			if(soldBy != null && !"".equals(soldBy)){
				pc.setSoldBy(Long.valueOf(soldBy));
			}	
			pc.setIsFirstBuy(Integer.parseInt(isFirstBuy));
			pc.setHasDriverLicense(Integer.parseInt(hasDriverLicense));
			pc.setRemark(remark);
			pc.setCreateDate(new Date());
			pc.setIsTryDrive(Long.valueOf(isTryDrive));
			pc.setIsFirstCome(Long.valueOf(isFirstCome));
			pc.setCustomerNum(Integer.valueOf(customerNum));
			pc.setGroupId(Long.valueOf(groupId));
			pc.setColorName(colorName);
			if(!"".equals(comeTime)){
				Date d1 = sdf.parse(comeTime);
				pc.setComeTime(d1);
			}
			if(!"".equals(leaveTime)){
				Date d2 = sdf.parse(leaveTime);
				pc.setLeaveTime(d2);
			}
			if(!"".equals(stayMinute)){
				pc.setStayMinute(Integer.valueOf(stayMinute));
			}
			dao.insert(pc);
			act.setOutData("flag", true);
			
		} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户添加保存失败");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	
	}
	
	//更多联系人
	public void getMoreLinkman(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.getSession().get(Constant.LOGON_USER);
			
			String cus_no = CommonUtils.checkNull(request.getParamValue("cus_no"));
			String sql = "select * from Tt_Po_Cus_Linkman p where p.customer_no = '"+cus_no+"'";
			List list = dao.pageQuery(sql, null, dao.getFunName());
			
			act.setOutData("morelinkmans", list);
			act.setForword(MoreLinkmanUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "更多联系人查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户查询(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void PotentialCustomerQuery() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
			    StringBuffer con = new StringBuffer();	
			    String customer_No = request.getParamValue("customer_No");
			    String customer_Name = request.getParamValue("customer_Name");
			    String init_Level = request.getParamValue("init_Level"); 
			    String intent_Level = request.getParamValue("intent_Level");  
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				// 客户编码
				if (customer_No != null && !"".equals(customer_No)) {
					con.append(" and pc.CUSTOMER_NO ='" + customer_No + "' "); //
				}
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and pc.CUSTOMER_NAME like '%" + customer_Name + "%'");
				}	
				// 客户初始级别
				if (init_Level != null && !"".equals(init_Level)) {
					con.append(" and pc.INIT_LEVEL='" + init_Level + "' ");
				}
				// 客户意向级别
				if (intent_Level != null && !"".equals(intent_Level)) {
					con.append(" and pc.INTENT_LEVEL='" + intent_Level + "' ");
				}			
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and pc.CREATE_DATE >= to_date('" + strDate +" 00:00:00"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and pc.CREATE_DATE <= to_date('" + endDate + " 23:59:59"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				con.append(" and pc.dealer_id="+logonUser.getDealerId()+" ");
				//经销商
//				if (dealerCode != null && !"".equals(dealerCode)) {
//					con.append(" and pc.ENTITY_CODE = '"+dealerCode+"'\n");
//				}
				PageResult<Map<String, Object>> ps = dao.applyQuery(con.toString(), curPage, 10) ; // 按条件查询潜在客户
				act.setOutData("ps", ps);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户查询(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户查询(oem端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void PotentialCustomerOemQuery() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
			    StringBuffer con = new StringBuffer();	
			    String orgCode = request.getParamValue("orgCode");
			    String dealerCode = request.getParamValue("dealerCode");
			    String customer_No = request.getParamValue("customer_No");
			    String customer_Name = request.getParamValue("customer_Name");
			    String init_Level = request.getParamValue("init_Level"); 
			    String intent_Level = request.getParamValue("intent_Level");  
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				List par=new ArrayList();
				
				// 大区代码
				if (orgCode != null && !"".equals(orgCode)) {
					//con.append(" and oda.root_org_code ='" + orgCode + "' "); 
					//con.append(MaterialGroupManagerDao.orgQueryConHaveDealer("oda.dealer_id", orgCode));
					con.append(Utility.getConSqlByParamForEqual(orgCode, par,"VOD", "ORG_CODE"));
				}
				//经销商
				if (dealerCode != null && !"".equals(dealerCode)) {
//					String[] array = dealerCode.split(",");
//					con.append(" and pc.entity_code in ("); 
//					for(int i = 0;i<array.length;i++){
//						con.append("'"+array[i]+"'");
//						if(i != array.length - 1){
//							con.append(",");
//						}
//					}
//					con.append(") \n");
					con.append(Utility.getConSqlByParamForEqual(dealerCode, par,"pc", "entity_code"));
				}
				
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and pc.CUSTOMER_NAME like ?\n");
					par.add("%"+customer_Name+"%");
				}	
				// 客户初始级别
				if (init_Level != null && !"".equals(init_Level)) {
					con.append(" and pc.INIT_LEVEL=?\n");
					par.add(init_Level);
				}
				// 客户意向级别
				if (intent_Level != null && !"".equals(intent_Level)) {
					con.append(" and pc.INTENT_LEVEL=?\n");
					par.add(intent_Level);
				}			
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and pc.CREATE_DATE >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(strDate+" 00:00:00");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and pc.CREATE_DATE <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(endDate+" 23:59:59");
				}	
//				// 特定经销商
//				if (dealerCode != null && !"".equals(dealerCode)) {
//					con.append(" and pc.entity_code='" + dealerCode + "' ");
//				}
				con.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("vod.dealer_id", logonUser,par));
				PageResult<Map<String, Object>> ps = dao.applyOemQuery(con.toString(), curPage, 10,par) ; // 按条件查询潜在客户
				act.setOutData("ps", ps);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户查询(OEM端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜在客户汇总查询(oem端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void PotentialCustomerOemTotalQuery() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
			    StringBuffer con = new StringBuffer();	
			    String orgCode = request.getParamValue("orgCode");
			    String dealerCode = request.getParamValue("dealerCode");
			    String customer_No = request.getParamValue("customer_No");
			    String customer_Name = request.getParamValue("customer_Name");
			    String init_Level = request.getParamValue("init_Level"); 
			    String intent_Level = request.getParamValue("intent_Level");  
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				List par=new ArrayList();
				
				// 大区代码
				if (orgCode != null && !"".equals(orgCode)) {
//					con.append(" and oda.root_org_code ='" + orgCode + "' "); 
//					con.append(MaterialGroupManagerDao.orgQueryCon("org.org_id", orgCode));
					con.append(Utility.getConSqlByParamForEqual(orgCode, par,"VOD", "ORG_CODE"));
				}
				//经销商
				if (dealerCode != null && !"".equals(dealerCode)) {
//					String[] array = dealerCode.split(",");
//					con.append(" and pc.entity_code in ("); 
//					for(int i = 0;i<array.length;i++){
//						con.append("'"+array[i]+"'");
//						if(i != array.length - 1){
//							con.append(",");
//						}
//					}
//					con.append(") \n");
					con.append(Utility.getConSqlByParamForEqual(dealerCode, par,"pc", "entity_code"));
				}
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and pc.CUSTOMER_NAME like ?\n");
					par.add("%"+customer_Name+"%");
				}	
				// 客户初始级别
				if (init_Level != null && !"".equals(init_Level)) {
					con.append(" and pc.INIT_LEVEL=? ");
					par.add(init_Level);
				}
				// 客户意向级别
				if (intent_Level != null && !"".equals(intent_Level)) {
					con.append(" and pc.INTENT_LEVEL=? ");
					par.add(intent_Level);
				}			
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and pc.CREATE_DATE >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(strDate+" 00:00:00");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and pc.CREATE_DATE <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(endDate+" 23:59:59");
				}	
//				// 特定经销商
//				if (dealerCode != null && !"".equals(dealerCode)) {
//					con.append(" and pc.entity_code='" + dealerCode + "' ");
//				}
				con.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("oda.dealer_id", logonUser,par));
				PageResult<Map<String, Object>> ps = dao.applyOemQueryCount(con.toString(), curPage, 10,par) ; // 按条件查询潜在客户
				act.setOutData("ps", ps);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户汇总查询(OEM端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
    public void deletePotentialCustomer(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String customerNo = CommonUtils.checkNull(request.getParamValue("CUSTOMER_NO"));//客户编号
			int i = dao.delPotentialCustomer(customerNo);
			if(i==1){
			act.setOutData("flag", true);
			}else {
			act.setOutData("flag", false);	
			}				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜在客户删除(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    	
    }
    /*********
	 * 下载功能
	 */
	public void viewExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
				
			    StringBuffer con = new StringBuffer();	
				String orgCode = request.getParamValue("orgCode");
			    String dealerCode = request.getParamValue("dealerCode");
			    String customer_Name = request.getParamValue("customer_Name");
			    String init_Level = request.getParamValue("init_Level"); 
			    String intent_Level = request.getParamValue("intent_Level");  
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				List par=new ArrayList();
				// 大区代码
				if (orgCode != null && !"".equals(orgCode)) {
//					con.append(" and rog_org_code ='" + orgCode + "' "); //
//					con.append(MaterialGroupManagerDao.orgQueryCon("org.org_id", orgCode));
					con.append(Utility.getConSqlByParamForEqual(orgCode, par,"VOD", "ORG_CODE"));
				}
				//经销商
				if (dealerCode != null && !"".equals(dealerCode)) {
//					String[] array = dealerCode.split(",");
//					con.append(" and pc.entity_code in ("); 
//					for(int i = 0;i<array.length;i++){
//						con.append("'"+array[i]+"'");
//						if(i != array.length - 1){
//							con.append(",");
//						}
//					}
//					con.append(") \n");
					con.append(Utility.getConSqlByParamForEqual(dealerCode, par,"pc", "entity_code"));
				}
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and pc.CUSTOMER_NAME like ?\n");
					par.add("%"+customer_Name+"%");
				}	
				// 客户初始级别
				if (init_Level != null && !"".equals(init_Level)) {
					con.append(" and pc.INIT_LEVEL=?\n");
					par.add(init_Level);
				}
				// 客户意向级别
				if (intent_Level != null && !"".equals(intent_Level)) {
					con.append(" and pc.INTENT_LEVEL=?\n");
					par.add(intent_Level);
				}			
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and pc.CREATE_DATE >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(strDate+" 00:00:00");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and pc.CREATE_DATE <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(endDate+" 23:59:59");
				}	
				con.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("vod.dealer_id", logonUser,par));
//				// 特定经销商
//				if (dealerCode != null && !"".equals(dealerCode)) {
//					con.append(" and pc.entity_code='" + dealerCode + "' ");
//				}
				
				
		String head[] = new String[18];
		head[0]="客户代码";
		head[1]="客户名称";
		head[2]="性别";
		head[3]="电话";
		head[4]="初始级别";
		head[5]="意向级别";
		head[6]="所属大区";
		head[7]="经销商";
		head[8]="创建日期";
		head[9]="客户来源";
		head[10]="媒体类型";
		head[11]="销售顾问";

		List<Map<String,Object>> list = dao.applyOemQueryExcel(con.toString(),Constant.PAGE_SIZE, curPage,par);
		  List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[18];
							detail[0] = String.valueOf(map.get("CUSTOMER_NO")== null?"":map.get("CUSTOMER_NO"));
							detail[1] = String.valueOf(map.get("CUSTOMER_NAME")==null?"":map.get("CUSTOMER_NAME"));
							detail[2] = String.valueOf(map.get("GENDER")==null?"":map.get("GENDER"));
							detail[3] = String.valueOf(map.get("CONTACTOR_MOBILE")==null?"":map.get("CONTACTOR_MOBILE"));
							detail[4] = String.valueOf(map.get("INIT_LEVEL")==null?"":map.get("INIT_LEVEL"));
							detail[5] = String.valueOf(map.get("INTENT_LEVEL")==null?"":map.get("INTENT_LEVEL"));
							detail[6] = String.valueOf(map.get("ROOT_ORG_NAME")==null?"":map.get("ROOT_ORG_NAME"));
							detail[7] = String.valueOf(map.get("DEALER_NAME")==null?"":map.get("DEALER_NAME"));
							detail[8] = String.valueOf(map.get("CREATE_DATE")==null?"":map.get("CREATE_DATE"));
							detail[9] = String.valueOf(map.get("CUS_SOURCE")==null?"":map.get("CUS_SOURCE"));
							detail[10] = String.valueOf(map.get("MEDIA_TYPE")==null?"":map.get("MEDIA_TYPE"));
							detail[11] = String.valueOf(map.get("SOLD_BY")==null?"":map.get("SOLD_BY"));
							list1.add(detail);					
			    	}
				}
		    }
		    try {
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1, "潜在客户(OEM)查询表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}	      
	   }
	
	/*********
	 * 汇总功能
	 */
	public void viewCountExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
				
			    StringBuffer con = new StringBuffer();	
				String orgCode = request.getParamValue("orgCode");
			    String dealerCode = request.getParamValue("dealerCode");
			    String customer_Name = request.getParamValue("customer_Name");
			    String init_Level = request.getParamValue("init_Level"); 
			    String intent_Level = request.getParamValue("intent_Level");  
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				List par=new ArrayList();
				// 大区代码
				if (orgCode != null && !"".equals(orgCode)) {
//					con.append(" and oda.root_org_code ='" + orgCode + "' "); //
//					con.append(MaterialGroupManagerDao.orgQueryCon("org.org_id", orgCode));
					con.append(Utility.getConSqlByParamForEqual(orgCode, par,"VOD", "ORG_CODE"));
				}
//				//经销商
//				if (dealerCode != null && !"".equals(dealerCode)) {
//					con.append(" and pc.entity_code ='" + dealerCode + "' "); //
//				}
				if (dealerCode != null && !"".equals(dealerCode)) {
//					String[] array = dealerCode.split(",");
//					con.append(" and pc.entity_code in ("); 
//					for(int i = 0;i<array.length;i++){
//						con.append("'"+array[i]+"'");
//						if(i != array.length - 1){
//							con.append(",");
//						}
//					}
//					con.append(") \n");
					con.append(Utility.getConSqlByParamForEqual(dealerCode, par,"pc", "entity_code"));
				}
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and pc.CUSTOMER_NAME like ?\n");
					par.add("%"+customer_Name+"%");
				}	
				// 客户初始级别
				if (init_Level != null && !"".equals(init_Level)) {
					con.append(" and pc.INIT_LEVEL=?\n");
					par.add(init_Level);
				}
				// 客户意向级别
				if (intent_Level != null && !"".equals(intent_Level)) {
					con.append(" and pc.INTENT_LEVEL=?\n");
					par.add(intent_Level);
				}			
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and pc.CREATE_DATE >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(strDate+" 00:00:00");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and pc.CREATE_DATE <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') \n");
					par.add(endDate+" 23:59:59");
				}	
				con.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("vod.dealer_id", logonUser,par));
		String head[] = new String[28];
		StringBuffer sbb = new StringBuffer();
 		if (strDate != null && !"".equals(strDate)) {
			sbb.append(strDate);
		}
 		
 		if (endDate != null && !"".equals(endDate)) {
			sbb.append(" 至 "+endDate);
		}	
		
		
		head[0]= "潜在客户汇总表  "+ sbb.toString(); //下标为0的位置是表头
		head[1]="所属大区";
		head[2]="省份";
		head[3]="经销商";
		head[4]="展厅活动";
		head[5]="报纸杂志";
		head[6]="促销活动";
		head[7]="重复购买";
		head[8]="朋友介绍";
		head[9]="老客户介绍";
		head[10]="其他";
		head[11]="陌生拜访";
		head[12]="电台广告";
		head[13]="电视广告";
		head[14]="路牌广告";
		head[15]="网络广告";
		head[16]="路过";
		head[17]="H级";
		head[18]="A级";
		head[19]= "B级";
		head[20]="C级";
		head[21]="D级";
		head[22]="F级";
		head[23]="F0级";
		head[24]="N级";
		head[25]="O级";
		
		List<Map<String,Object>> list = dao.applyOemQueryCountExcel(con.toString(),Constant.PAGE_SIZE, curPage,par);
		List<Map<String,Object>> regions = dao.applyOemQueryCountRegion(con.toString(),Constant.PAGE_SIZE, curPage,par); //省份数据
		List<Map<String,Object>> orgs = dao.applyOemQueryCountOrg(con.toString(),Constant.PAGE_SIZE, curPage,par);//大区数据
		
		  List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[28];
							detail[0] = String.valueOf(map.get("ROOT_ORG_NAME"));
							detail[1] = String.valueOf(map.get("REGION_NAME"));
							detail[2] = String.valueOf(map.get("DEALER_NAME"));
							detail[3] = String.valueOf(map.get("SALE"));
							detail[4] = String.valueOf(map.get("FRIEND"));
							detail[5] = String.valueOf(map.get("AD"));
							detail[6] = String.valueOf(map.get("TV"));
							detail[7] = String.valueOf(map.get("INTERNET"));
							detail[8] = String.valueOf(map.get("NEWSPAPER"));
							detail[9] = String.valueOf(map.get("SHOW"));
							detail[10] = String.valueOf(map.get("RADIO"));
							detail[11] = String.valueOf(map.get("OUTDOORS"));
							detail[12] = String.valueOf(map.get("DM"));
							detail[13] = String.valueOf(map.get("NOTE"));
							detail[14] = String.valueOf(map.get("OTHER"));
							detail[15] = String.valueOf(map.get("AGAIN"));
							detail[16] = String.valueOf(map.get("H"));
							detail[17] = String.valueOf(map.get("A"));
							detail[18] = String.valueOf(map.get("B"));
							detail[19] = String.valueOf(map.get("C"));
							detail[20] = String.valueOf(map.get("D"));
							detail[21] = String.valueOf(map.get("F"));
							detail[22] = String.valueOf(map.get("F0"));
							detail[23] = String.valueOf(map.get("N"));
							detail[24] = String.valueOf(map.get("O"));
							list1.add(detail);					
			    	}
				}	
		    }
		    List list2=new ArrayList();
		    if(regions!=null&&regions.size()!=0){
		    	for(int i=0;i<regions.size();i++){
			    	Map map =(Map)regions.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]region=new String[28];
						region[0] = String.valueOf("");
						region[1] = String.valueOf(map.get("ROOT_ORG_NAME"));
						region[2] = String.valueOf(map.get("REGION_NAME"));
						region[3] = String.valueOf(map.get("SALE"));
						region[4] = String.valueOf(map.get("FRIEND"));
						region[5] = String.valueOf(map.get("AD"));
						region[6] = String.valueOf(map.get("TV"));
						region[7] = String.valueOf(map.get("INTERNET"));
						region[8] = String.valueOf(map.get("NEWSPAPER"));
						region[9] = String.valueOf(map.get("SHOW"));
						region[10] = String.valueOf(map.get("RADIO"));
						region[11] = String.valueOf(map.get("OUTDOORS"));
						region[12] = String.valueOf(map.get("DM"));
						region[13] = String.valueOf(map.get("NOTE"));
						region[14] = String.valueOf(map.get("OTHER"));
						region[15] = String.valueOf(map.get("AGAIN"));
						region[16] = String.valueOf(map.get("H"));
						region[17] = String.valueOf(map.get("A"));
						region[18] = String.valueOf(map.get("B"));
						region[19] = String.valueOf(map.get("C"));
						region[20] = String.valueOf(map.get("D"));
						region[21] = String.valueOf(map.get("F"));
						region[22] = String.valueOf(map.get("F0"));
						region[23] = String.valueOf(map.get("N"));
						region[24] = String.valueOf(map.get("O"));
						list2.add(region);					
			    	}
				}	
		    }
		    
		    List list3=new ArrayList();
		    if(orgs!=null&&orgs.size()!=0){ 
		    	for(int i=0;i<orgs.size();i++){
			    	Map map =(Map)orgs.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]org=new String[28];
						org[0] = String.valueOf(map.get(""));
						org[1] = String.valueOf(map.get(""));
						org[2] = String.valueOf(map.get("ROOT_ORG_NAME"));
						org[3] = String.valueOf(map.get("SALE"));
						org[4] = String.valueOf(map.get("FRIEND"));
						org[5] = String.valueOf(map.get("AD"));
						org[6] = String.valueOf(map.get("TV"));
						org[7] = String.valueOf(map.get("INTERNET"));
						org[8] = String.valueOf(map.get("NEWSPAPER"));
						org[9] = String.valueOf(map.get("SHOW"));
						org[10] = String.valueOf(map.get("RADIO"));
						org[11] = String.valueOf(map.get("OUTDOORS"));
						org[12] = String.valueOf(map.get("DM"));
						org[13] = String.valueOf(map.get("NOTE"));
						org[14] = String.valueOf(map.get("OTHER"));
						org[15] = String.valueOf(map.get("AGAIN"));
						org[16] = String.valueOf(map.get("H"));
						org[17] = String.valueOf(map.get("A"));
						org[18] = String.valueOf(map.get("B"));
						org[19] = String.valueOf(map.get("C"));
						org[20] = String.valueOf(map.get("D"));
						org[21] = String.valueOf(map.get("F"));
						org[22] = String.valueOf(map.get("F0"));
						org[23] = String.valueOf(map.get("N"));
						org[24] = String.valueOf(map.get("O"));
						list3.add(org);					
			    	}
				}   	
		    }
		    
		    try {
				com.infodms.dms.actions.claim.basicData.ToExcel.toPotentialCustomerExcel(ActionContext.getContext().getResponse(), request, head, list1,list2,list3,"潜在客户(OEM)汇总表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}	      
	   }
	
	public void searchColor(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String groupId = request.getParamValue("groupId");
			List<Map<String, Object>> list = null;
			if(groupId != null && !"".equals(groupId)){
				String sql = "SELECT COLOR_CODE,COLOR_NAME FROM VW_MATERIAL_GROUP_MAT WHERE SERIES_ID = "+groupId
							+"GROUP BY COLOR_CODE,COLOR_NAME";
				list = dao.pageQuery(sql, null, dao.getFunName());
			}
			String color = "";
			if(list != null && list.size()>0){
				for(int i = 0 ; i < list.size() ; i++){
					Map<String, Object> temp = list.get(i);
					color += (String)temp.get("COLOR_NAME") + ",";
				}
				act.setOutData("color", color.substring(0,color.length()-1));
			}else{
				act.setOutData("color", "");
			}
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车系颜色");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showStayTime(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String comeTime = CommonUtils.checkNull(request.getParamValue("comeTime"));
			String leaveTime = CommonUtils.checkNull(request.getParamValue("leaveTime"));
			if(!"".equals(comeTime) && !"".equals(leaveTime)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date d1 = sdf.parse(comeTime);
				Date d2 = sdf.parse(leaveTime);
				Long minutes = d2.getTime()/60000 - d1.getTime()/60000;
				act.setOutData("minute", minutes);
			}else{
				act.setOutData("minute", "");
			}
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车系颜色");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}