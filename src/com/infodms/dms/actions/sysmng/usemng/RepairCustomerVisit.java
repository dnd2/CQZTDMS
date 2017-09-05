
/**********************************************************************
* <pre>
* FILE : RepairCustomerVisit.java
* CLASS : RepairCustomerVisit
* 
* AUTHOR : YH
*
* FUNCTION : 维修客户回访管理Action.
*-----------------------------------------------------------
*|2011-04-22| 
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.usemng;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.dao.potentialCustomer.RepairCustomerVisitDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtRepairCusVisitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class RepairCustomerVisit {
    
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	public Logger logger = Logger.getLogger(RepairCustomerVisit.class);
	private final String queryInitUrl = "/jsp/systemMng/userMng/RepairCustomerVisitSearch.jsp";
	private final String queryOemInitUrl = "/jsp/systemMng/userMng/RepairCustomerVisitOemSearch.jsp";
	private final String addInitUrl = "/jsp/systemMng/userMng/RepairCustomerVisitAdd.jsp";
	private final String modfiInitUrl = "/jsp/systemMng/userMng/RepairCustomerVisitUpdate.jsp";
    private RepairCustomerVisitDao dao = RepairCustomerVisitDao.getInstance();
    private SeriesShowDao sdao = SeriesShowDao.getInstance();
	/**
	 * 维修客户查询初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"维修客户回访查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 维修客户回访查询初始化(Oem端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryOemInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List regions = dao.getRegions();
			act.setOutData("regions", regions);
			act.setForword(queryOemInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"维修客户回访查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 维修客户回访添加初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TmVhclMaterialGroupPO> list = sdao.getMaterialGroup();
			act.setOutData("list", list);
			act.setForword(addInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"维修客户回访添加初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修客户回访修改初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String cus_id = request.getParamValue("CUS_ID");
			
			TmPotentialCustomerPO pc = new TmPotentialCustomerPO();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			StringBuffer sql= new StringBuffer();
			sql.append("select rcv.CUS_ID,\n" );
			sql.append("       rcv.Customer_Name,\n" );
			sql.append("       rcv.Group_Code,\n" );
			sql.append("       rcv.Three_Guarantees,\n" );
			sql.append("       rcv.Repair_Item,\n" );
			sql.append("       rcv.No_Visit_Reason,\n" );
			sql.append("       rcv.PHONE,\n" );
			sql.append("       rcv.REMARK,\n" );
			sql.append("       rcv.create_by,\n" );
			sql.append("       rcv.create_date,\n" );
			sql.append("       rcv.update_by,\n" );
			sql.append("       rcv.update_date,\n" );
			sql.append("       to_char(rcv.visit_date,'yyyy-MM-dd') visit_date,\n" );
			sql.append("       rcv.satisfied,\n" );
			sql.append("       rcv.no_satisfied,\n" );
			sql.append("       rcv.no_satisfied_reason,\n" );
			sql.append("       rcv.is_recommend,\n" );
			sql.append("       rcv.dealer_code,rcv.license_no\n" );
			sql.append("  from tt_repair_cus_visit rcv WHERE 1=1  AND CUS_ID='"+cus_id+"'");
			list = dao.pageQuery(sql.toString(), null,null);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			List<TmVhclMaterialGroupPO> list1 = sdao.getMaterialGroup();
			act.setOutData("list", list1);
			act.setForword(modfiInitUrl);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"跳转修改维修客户回访页面失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修客户回访修改提交
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void modfi() {
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String CUS_ID =	CommonUtils.checkNull(request.getParamValue("CUS_ID"));//客户姓名
			String CUSTOMER_NAME =	CommonUtils.checkNull(request.getParamValue("CUSTOMER_NAME"));//客户姓名
			String VISIT_DATE  =  CommonUtils.checkNull(request.getParamValue("CON_END_DAY"));//回访日期
			String PHONE	= CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String	THREE_GUARANTEES = CommonUtils.checkNull(request.getParamValue("THREE_GUARANTEES"));//三包期内外
			String	GROUP_CODE = CommonUtils.checkNull(request.getParamValue("GROUP_CODE"));//车型
			String	LICENSE_NO = CommonUtils.checkNull(request.getParamValue("LICENSE_NO"));//车牌
			String	REPAIR_ITEM= CommonUtils.checkNull(request.getParamValue("REPAIR_ITEM"));//维修项目
			String	NO_VISIT_REASON= CommonUtils.checkNull(request.getParamValue("NO_VISIT_REASON"));//回访不成功理由
			String	SATISFIED = CommonUtils.checkNull(request.getParamValue("SATISFIED"));//是否满意
			String	IS_RECOMMEND = CommonUtils.checkNull(request.getParamValue("IS_RECOMMEND"));//是否愿意推荐
			String	NO_SATISFIED = CommonUtils.checkNull(request.getParamValue("NO_SATISFIED"));//不满意项目
			String	NO_SATISFIED_REASON = CommonUtils.checkNull(request.getParamValue("NO_SATISFIED_REASON"));//不满意具体内容
			String	REMARK = CommonUtils.checkNull(request.getParamValue("REMARK"));//备注
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			//更新的时候
			TtRepairCusVisitPO rcv = new TtRepairCusVisitPO();
			   rcv.setCustomerName(CUSTOMER_NAME);
			   rcv.setVisitDate(dateFormat.parse(VISIT_DATE));
			   rcv.setPhone(PHONE);
			   rcv.setThreeGuarantees(THREE_GUARANTEES);
			   rcv.setGroupCode(GROUP_CODE);
			   rcv.setLicenseNo(LICENSE_NO);
			   rcv.setRepairItem(REPAIR_ITEM);
			   rcv.setNoSatisfiedReason(NO_VISIT_REASON);
			   rcv.setNoVisitReason(NO_VISIT_REASON);
			   rcv.setSatisfied(SATISFIED);
			   rcv.setIsRecommend(IS_RECOMMEND);
			   rcv.setNoSatisfied(NO_SATISFIED);
			   rcv.setNoSatisfiedReason(NO_SATISFIED_REASON);
	           rcv.setRemark(REMARK);
	           rcv.setDealerCode(dealerCode);
	           rcv.setUpdateBy(logonUser.getUserId());
			   rcv.setUpdateDate(new Date());
			   TtRepairCusVisitPO oldrcv = new TtRepairCusVisitPO();
	           oldrcv.setCusId(Long.parseLong(CUS_ID));      
            int i = dao.update(oldrcv,rcv);
            if(i==1){
    			act.setOutData("flag", true);
            }else {
            	act.setOutData("flag", false);
            }
			act.setForword(queryInitUrl);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改维修客户回访失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修客户回访添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void add() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		String dealerCode = logonUser.getDealerCode();
		String orgCode = "";
		String regionCode = "";
		
		List rods =  dao.qeruyDealerOrgReg(logonUser.getDealerCode());
		if(rods.size()>0){
			Map map = (Map)rods.get(0);
			orgCode = map.get("ROOT_ORG_CODE").toString();
			regionCode = map.get("REGION_CODE").toString();
		}
		try{
			String CUSTOMER_NAME =	CommonUtils.checkNull(request.getParamValue("CUSTOMER_NAME"));//客户姓名
			String VISIT_DATE  =  CommonUtils.checkNull(request.getParamValue("CON_END_DAY"));//回访日期
			String PHONE	= CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String	THREE_GUARANTEES = CommonUtils.checkNull(request.getParamValue("THREE_GUARANTEES"));//三包期内外
			String	GROUP_CODE = CommonUtils.checkNull(request.getParamValue("GROUP_CODE"));//车型
			String	LICENSE_NO = CommonUtils.checkNull(request.getParamValue("LICENSE_NO"));//车牌
			String	REPAIR_ITEM= CommonUtils.checkNull(request.getParamValue("REPAIR_ITEM"));//维修项目
			String	NO_VISIT_REASON= CommonUtils.checkNull(request.getParamValue("NO_VISIT_REASON"));//回访不成功理由
			String	SATISFIED = CommonUtils.checkNull(request.getParamValue("SATISFIED"));//是否满意
			String	IS_RECOMMEND = CommonUtils.checkNull(request.getParamValue("IS_RECOMMEND"));//是否愿意推荐
			String	NO_SATISFIED = CommonUtils.checkNull(request.getParamValue("NO_SATISFIED"));//不满意项目
			String	NO_SATISFIED_REASON = CommonUtils.checkNull(request.getParamValue("NO_SATISFIED_REASON"));//不满意具体内容
			String	REMARK = CommonUtils.checkNull(request.getParamValue("REMARK"));//备注
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			//新增的时候	
		   Long id=0L;  
		   TtRepairCusVisitPO rcv = new TtRepairCusVisitPO();
           id = Utility.getLong(SequenceManager.getSequence(""));
           rcv.setCusId(id);
		   rcv.setCustomerName(CUSTOMER_NAME);
		   rcv.setVisitDate(dateFormat.parse(VISIT_DATE));
		   rcv.setPhone(PHONE);
		   rcv.setThreeGuarantees(THREE_GUARANTEES);
		   rcv.setGroupCode(GROUP_CODE);
		   rcv.setLicenseNo(LICENSE_NO);
		   rcv.setRepairItem(REPAIR_ITEM);
		   rcv.setNoSatisfiedReason(NO_VISIT_REASON);
		   rcv.setNoVisitReason(NO_VISIT_REASON);
		   rcv.setSatisfied(SATISFIED);
		   rcv.setIsRecommend(IS_RECOMMEND);
		   rcv.setNoSatisfied(NO_SATISFIED);
		   rcv.setNoSatisfiedReason(NO_SATISFIED_REASON);
           rcv.setRemark(REMARK);
           rcv.setDealerCode(dealerCode);
           rcv.setCreateBy(logonUser.getUserId());
           rcv.setCreateDate(new Date());
           rcv.setOrgCode(orgCode);
           rcv.setRegionCode(regionCode);
		   dao.insert(rcv);
		   act.setOutData("flag", true);
			
		} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维修客户回访添加保存失败");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	
	}
	
	/**
	 * 维修客户回访查询(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void Query() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
			    StringBuffer con = new StringBuffer();	
			    String customer_Name = request.getParamValue("customer_Name"); 
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and rcv.CUSTOMER_NAME like '%" + customer_Name + "%'");
				}					
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and rcv.CREATE_DATE >= to_date('" + strDate +" 00:00:00"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and rcv.CREATE_DATE <= to_date('" + endDate + " 23:59:59"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}	
				// 特定经销商
				if (dealerCode != null && !"".equals(dealerCode)) {
					con.append(" and rcv.dealer_code='" + dealerCode + "' ");
				}
				PageResult<Map<String, Object>> ps = dao.applyQuery(con.toString(), curPage, 10) ; // 按条件查询维修客户回访
				act.setOutData("ps", ps);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维修客户回访查询(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
    public void delete(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String CUS_ID = CommonUtils.checkNull(request.getParamValue("CUS_ID"));//客户编号
			int i = dao.del(CUS_ID);
			if(i==1){
			act.setOutData("flag", true);
			}else {
			act.setOutData("flag", false);	
			}				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维修客户回访删除(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    	
    }
    /*********
	 * 汇总功能
	 */
	@SuppressWarnings("unchecked")
	public void AnalyseExclel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				
				 java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance(); 
				 nf.setMaximumIntegerDigits(5);//小数点前保留几位 
				 nf.setMinimumFractionDigits(2);// 小数点后保留几位 
				 
				String orgCode = request.getParamValue("orgCode");
				String regionCode = request.getParamValue("regionCode");
			    StringBuffer con = new StringBuffer();	
			    String dealerCode = request.getParamValue("dealerCode");
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				String dealerCodes = CommonUtils.strToSql(dealerCode, ",");
				
				 //省份
				if (regionCode != null && !"".equals(regionCode)) {
					con.append(" and rcv.REGION_CODE in (" + regionCode + ") ");  
				}			    
				//经销商
				if (dealerCodes != null && !"".equals(dealerCodes)) {
					con.append(" and rcv.DEALER_CODE in " + dealerCodes );   
				}	
					
				List params = new ArrayList();
				params.add(strDate);
				params.add(endDate);
				
				String strDates = dao.callFunction("f_get_by_date", java.sql.Types.VARCHAR,params ).toString();
				String Dates[] = strDates.split(",");
               
				String strWeeks = dao.callFunction("f_get_week_by_date", java.sql.Types.VARCHAR,params ).toString();
				String weeks[] = strWeeks.split(",");
							   	

				List three_guarantees_params_1 = new ArrayList();
				three_guarantees_params_1.add(strDate);
				three_guarantees_params_1.add(weeks.length);
				three_guarantees_params_1.add("THREE_GUARANTEES");
				three_guarantees_params_1.add("10041001");//三包内			
				three_guarantees_params_1.add(con.toString());
				String three_guarantees_1  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,three_guarantees_params_1).toString();				
				String[] three_guarantees_1s = three_guarantees_1.split(","); //三包内客户数量数组
				float total_three_guarantees_1s = 0;
				for(int i=0;i<three_guarantees_1s.length;i++){
					total_three_guarantees_1s = total_three_guarantees_1s + Float.parseFloat(three_guarantees_1s[i]);
				}
				
				List three_guarantees_params_2 = new ArrayList();
				three_guarantees_params_2.add(strDate);
				three_guarantees_params_2.add(weeks.length);
				three_guarantees_params_2.add("THREE_GUARANTEES");
				three_guarantees_params_2.add("10041002");//三包外
				three_guarantees_params_2.add(con.toString());
				String three_guarantees_2  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,three_guarantees_params_2).toString();				
				String[] three_guarantees_2s = three_guarantees_2.split(","); //三包外客户数量数组
				float total_three_guarantees_2s = 0;
				for(int i=0;i<three_guarantees_2s.length;i++){
					total_three_guarantees_2s = total_three_guarantees_2s + Float.parseFloat(three_guarantees_2s[i]);
				}
				
				float three_guarantees_rate =  total_three_guarantees_1s /(total_three_guarantees_1s + total_three_guarantees_2s) ;
				
 				String str_three_guarantees_rate = nf.format(three_guarantees_rate);// 三包内客户占比
							
				List no_visit_reason_params_1 = new ArrayList();
				no_visit_reason_params_1.add(strDate);
				no_visit_reason_params_1.add(weeks.length);
				no_visit_reason_params_1.add("NO_VISIT_REASON");
				no_visit_reason_params_1.add("8050002");//回访成功
				no_visit_reason_params_1.add(con.toString());
				String no_visit_reason_1  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_visit_reason_params_1).toString();
				String[] no_visit_reason_1s = no_visit_reason_1.split(",");
				float total_no_visit_reason_1s = 0;
				for(int i=0;i<no_visit_reason_1s.length;i++){
					total_no_visit_reason_1s = total_no_visit_reason_1s + Float.parseFloat(no_visit_reason_1s[i]);
				}
							
				List no_visit_reason_params_2 = new ArrayList();
				no_visit_reason_params_2.add(strDate);
				no_visit_reason_params_2.add(weeks.length);
				no_visit_reason_params_2.add("NO_VISIT_REASON");
				no_visit_reason_params_2.add("8050001");//客户信息错误
				no_visit_reason_params_2.add(con.toString());
				String no_visit_reason_2  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_visit_reason_params_2).toString();
				String[] no_visit_reason_2s = no_visit_reason_2.split(",");
				float total_no_visit_reason_2s = 0;
				for(int i=0;i<no_visit_reason_2s.length;i++){
					total_no_visit_reason_2s = total_no_visit_reason_2s+ Float.parseFloat(no_visit_reason_2s[i]);					
				}				
				
				List no_visit_reason_params_3 = new ArrayList();
				no_visit_reason_params_3.add(strDate);
				no_visit_reason_params_3.add(weeks.length);
				no_visit_reason_params_3.add("NO_VISIT_REASON");
				no_visit_reason_params_3.add("8050003");//无法接通
				no_visit_reason_params_3.add(con.toString());
				String no_visit_reason_3  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_visit_reason_params_3).toString();
				String[] no_visit_reason_3s = no_visit_reason_3.split(",");
				float totalno_visit_reason_3s = 0;
				for(int i=0;i<no_visit_reason_3s.length;i++){
					totalno_visit_reason_3s = totalno_visit_reason_3s + Float.parseFloat(no_visit_reason_3s[i]);		
				}
				
				
				List no_visit_reason_params_4 = new ArrayList();
				no_visit_reason_params_4.add(strDate);
				no_visit_reason_params_4.add(weeks.length);
				no_visit_reason_params_4.add("NO_VISIT_REASON");
				no_visit_reason_params_4.add("8050004");//拒绝回访
				no_visit_reason_params_4.add(con.toString());
				String no_visit_reason_4  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_visit_reason_params_4).toString();
				String[] no_visit_reason_4s = no_visit_reason_4.split(",");
				float totalno_no_visit_reason_4 = 0;
				for(int i=0;i<no_visit_reason_4s.length;i++){
					totalno_no_visit_reason_4 = totalno_no_visit_reason_4 + Float.parseFloat(no_visit_reason_4s[i]);		
				}
	
				float  no_visit_reason_rate = total_no_visit_reason_1s/(total_no_visit_reason_1s+total_no_visit_reason_2s+totalno_visit_reason_3s+totalno_no_visit_reason_4);
				String str_no_visit_reason_rate = nf.format(no_visit_reason_rate); //信息准确率
				
				
				List no_satisfied_params_1  = new ArrayList();
				no_satisfied_params_1.add(strDate);
				no_satisfied_params_1.add(weeks.length);
				no_satisfied_params_1.add("SATISFIED");
				no_satisfied_params_1.add("10041001");//满意数
				no_satisfied_params_1.add(con.toString());
				String no_satisfied_1  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_1).toString();
				String[] no_satisfied_1s = no_satisfied_1.split(",");
				float total_no_satisfied_1s = 0 ;
				for(int i=0;i<no_satisfied_1s.length;i++){
					total_no_satisfied_1s = total_no_satisfied_1s + Float.parseFloat(no_satisfied_1s[i]);
				}
					
				List no_satisfied_params_2  = new ArrayList();
				no_satisfied_params_2.add(strDate);
				no_satisfied_params_2.add(weeks.length);
				no_satisfied_params_2.add("NO_SATISFIED");
				no_satisfied_params_2.add("8040001");//服务接待
				no_satisfied_params_2.add(con.toString());
				String no_satisfied_2  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_2).toString();
				String[] no_satisfied_2s = no_satisfied_2.split(",");
				float total_no_satisfied_2s = 0 ;
				for(int i=0;i<no_satisfied_2s.length;i++){
					total_no_satisfied_2s = total_no_satisfied_2s + Float.parseFloat(no_satisfied_2s[i]);
				}
				
				List no_satisfied_params_3 = new ArrayList();
				no_satisfied_params_3.add(strDate);
				no_satisfied_params_3.add(weeks.length);
				no_satisfied_params_3.add("NO_SATISFIED");
				no_satisfied_params_3.add("8040002");//服务环境
				no_satisfied_params_3.add(con.toString());
				String no_satisfied_3  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_3).toString();
				String[] no_satisfied_3s = no_satisfied_3.split(",");
				float total_no_satisfied_3s = 0 ;
				for(int i=0;i<no_satisfied_3s.length;i++){
					total_no_satisfied_3s = total_no_satisfied_3s + Float.parseFloat(no_satisfied_3s[i]);
				}
				
				
				List no_satisfied_params_4 = new ArrayList();
				no_satisfied_params_4.add(strDate);
				no_satisfied_params_4.add(weeks.length);
				no_satisfied_params_4.add("NO_SATISFIED");
				no_satisfied_params_4.add("8040003");//维修质量
				no_satisfied_params_4.add(con.toString());
				String no_satisfied_4  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_4).toString();
				String[] no_satisfied_4s = no_satisfied_4.split(",");
				float total_no_satisfied_4s = 0 ;
				for(int i=0;i<no_satisfied_4s.length;i++){
					total_no_satisfied_4s = total_no_satisfied_4s + Float.parseFloat(no_satisfied_4s[i]);
				}
						
				List no_satisfied_params_5 = new ArrayList();
				no_satisfied_params_5.add(strDate);
				no_satisfied_params_5.add(weeks.length);
				no_satisfied_params_5.add("NO_SATISFIED");
				no_satisfied_params_5.add("8040004");//等候时间
				no_satisfied_params_5.add(con.toString());
				String no_satisfied_5  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_5).toString();
				String[] no_satisfied_5s = no_satisfied_5.split(",");
				float total_no_satisfied_5s = 0 ;
				for(int i=0;i<no_satisfied_5s.length;i++){
					total_no_satisfied_5s = total_no_satisfied_5s + Float.parseFloat(no_satisfied_5s[i]);
				}
							
				List no_satisfied_params_6 = new ArrayList();
				no_satisfied_params_6.add(strDate);
				no_satisfied_params_6.add(weeks.length);
				no_satisfied_params_6.add("NO_SATISFIED");
				no_satisfied_params_6.add("8040005");//维修时间
				no_satisfied_params_6.add(con.toString());
				String no_satisfied_6  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_6).toString();
				String[] no_satisfied_6s = no_satisfied_6.split(",");
				float total_no_satisfied_6s = 0 ;
				for(int i=0;i<no_satisfied_6s.length;i++){
					total_no_satisfied_6s = total_no_satisfied_6s + Float.parseFloat(no_satisfied_6s[i]);
				}
	
				List no_satisfied_params_7 = new ArrayList();
				no_satisfied_params_7.add(strDate);
				no_satisfied_params_7.add(weeks.length);
				no_satisfied_params_7.add("NO_SATISFIED");
				no_satisfied_params_7.add("8040006");//备件保供
				no_satisfied_params_7.add(con.toString());
				String no_satisfied_7  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_7).toString();
				String[] no_satisfied_7s = no_satisfied_7.split(",");
				float total_no_satisfied_7s = 0 ;
				for(int i=0;i<no_satisfied_7s.length;i++){
					total_no_satisfied_7s = total_no_satisfied_7s + Float.parseFloat(no_satisfied_7s[i]);
				}
						
				List no_satisfied_params_8 = new ArrayList();
				no_satisfied_params_8.add(strDate);
				no_satisfied_params_8.add(weeks.length);
				no_satisfied_params_8.add("NO_SATISFIED");
				no_satisfied_params_8.add("8040007");//维修收费
				no_satisfied_params_8.add(con.toString());
				String no_satisfied_8  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_8).toString();
				String[] no_satisfied_8s = no_satisfied_8.split(",");
				float total_no_satisfied_8s = 0 ;
				for(int i=0;i<no_satisfied_8s.length;i++){
					total_no_satisfied_8s = total_no_satisfied_8s + Float.parseFloat(no_satisfied_8s[i]);
				}
				
				
				List no_satisfied_params_9 = new ArrayList();
				no_satisfied_params_9.add(strDate);
				no_satisfied_params_9.add(weeks.length);
				no_satisfied_params_9.add("NO_SATISFIED");
				no_satisfied_params_9.add("8040008");//产品质量
				no_satisfied_params_9.add(con.toString());
				String no_satisfied_9  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,no_satisfied_params_9).toString();
				String[] no_satisfied_9s = no_satisfied_9.split(",");
				float total_no_satisfied_9s = 0 ;
				for(int i=0;i<no_satisfied_9s.length;i++){
					total_no_satisfied_9s = total_no_satisfied_9s + Float.parseFloat(no_satisfied_9s[i]);
				}
			
			   float no_satisfied_rate = total_no_satisfied_1s / (total_no_satisfied_1s+total_no_satisfied_2s+total_no_satisfied_3s+total_no_satisfied_4s+total_no_satisfied_5s+total_no_satisfied_6s+total_no_satisfied_7s+total_no_satisfied_8s+total_no_satisfied_9s);
			   String str_no_satisfied_rate = 	nf.format(no_satisfied_rate); //满意率
			   
				List is_recommend_params_1 = new ArrayList();
				is_recommend_params_1.add(strDate);
				is_recommend_params_1.add(weeks.length);
				is_recommend_params_1.add("IS_RECOMMEND");
				is_recommend_params_1.add("10041001");//愿意推荐
				is_recommend_params_1.add(con.toString());
				String is_recommend_1  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,is_recommend_params_1).toString();
				String[] is_recommend_1s = is_recommend_1.split(",");
				float total_is_recommend_1s = 0 ;
				for(int i=0;i<is_recommend_1s.length;i++){
					total_is_recommend_1s = total_is_recommend_1s + Float.parseFloat(is_recommend_1s[i]);
				}
				
				
				List is_recommend_params_2 = new ArrayList();
				is_recommend_params_2.add(strDate);
				is_recommend_params_2.add(weeks.length);
				is_recommend_params_2.add("IS_RECOMMEND");
				is_recommend_params_2.add("10041002");//不愿意推荐
				is_recommend_params_2.add(con.toString());
				String is_recommend_2  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,is_recommend_params_2).toString();
				String[] is_recommend_2s = is_recommend_2.split(",");
				float total_is_recommend_2s = 0 ;
				for(int i=0;i<is_recommend_2s.length;i++){
					total_is_recommend_2s = total_is_recommend_2s + Float.parseFloat(is_recommend_2s[i]);
				}
				float is_recommend_rate = total_is_recommend_1s / (total_is_recommend_1s+total_is_recommend_2s);
				String str_is_recommend_rate = nf.format(is_recommend_rate); //推荐率
				
				List repair_item_params_1 = new ArrayList();
				repair_item_params_1.add(strDate);
				repair_item_params_1.add(weeks.length);
				repair_item_params_1.add("REPAIR_ITEM");
				repair_item_params_1.add("8060001");//保养
				repair_item_params_1.add(con.toString());
				String repair_item_1  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,repair_item_params_1).toString();
				String[] repair_item_1s = repair_item_1.split(",");
				
				List repair_item_params_2 = new ArrayList();
				repair_item_params_2.add(strDate);
				repair_item_params_2.add(weeks.length);
				repair_item_params_2.add("REPAIR_ITEM");
				repair_item_params_2.add("8060002");//维修
				repair_item_params_2.add(con.toString());
				String repair_item_2  =  dao.callFunction("f_RepairCustomerVisit_1",java.sql.Types.VARCHAR,repair_item_params_2).toString();
				String[] repair_item_2s = repair_item_2.split(",");
				
		String head[] = new String[28];	
		head[0]= "维修客户统计分析表"; //下标为0的位置是表头
		head[1]="年度："+strDate+" 至 "+endDate+" "+"   服务站负责人:"+"";
		head[2]="时间";
		head[3]="日期";
		head[4]="星期";
		head[5]="当日进场客户数";
		head[6]= "当日客户回访";
		head[7]= "统计分析项目";
			
		List list = new ArrayList();
		list.add(repair_item_1s);
		list.add(repair_item_2s);
		list.add(three_guarantees_1s);
		list.add(three_guarantees_2s);
		list.add(no_visit_reason_1s);
		list.add(no_visit_reason_2s);
		list.add(no_visit_reason_3s);
		list.add(no_visit_reason_4s);
		list.add(no_satisfied_1s);
		list.add(no_satisfied_2s);
		list.add(no_satisfied_3s);
		list.add(no_satisfied_4s);
		list.add(no_satisfied_5s);
		list.add(no_satisfied_6s);
		list.add(no_satisfied_7s);
		list.add(no_satisfied_8s);
		list.add(no_satisfied_9s);
		list.add(is_recommend_1s);
		list.add(is_recommend_2s);
		
		List list_rate = new ArrayList();
		list_rate.add(str_three_guarantees_rate);
		list_rate.add(str_no_visit_reason_rate);
		list_rate.add(str_no_satisfied_rate);
		list_rate.add(str_is_recommend_rate);
		
		    try {
				this.toRepairCustomerVisitExcel(ActionContext.getContext().getResponse(), request,head,Dates,weeks,list,list_rate,"维修客户统计分析表(OEM)汇总表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}	      
	   }
	
	public  Object toRepairCustomerVisitExcel(ResponseWrapper response,
			RequestWrapper request, String[] head,String[] Dates,String[] weeks,List<String[]> list,List<String[]> list_rate,String name)
			throws Exception {
        int all_col = 36 ; //总列数
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheet", 0);
            
			 /**
			 * 定义单元格样式
			 */
		   WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
		     WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
		     jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		   
		   WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10,
				     WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				     jxl.format.Colour.BLACK); 
		   	   
		   WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
		   //wcf.setBackground(jxl.format.Colour.BLUE_GREY); // 设置单元格的背景颜色
		   wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		   
		   WritableCellFormat wcf2 = new WritableCellFormat(wf2); 
		   wcf2.setAlignment(jxl.format.Alignment.CENTRE);
		   
		   WritableCellFormat wcf3 = new WritableCellFormat(wf2); 
		   wcf3.setAlignment(jxl.format.Alignment.LEFT);
		   
		   ws.addCell(new Label(0,0,head[0], wcf));
		   ws.mergeCells(0, 0, all_col, 0); // 标题 
		   
		   ws.addCell(new Label(1,1,head[1], wcf));
		   ws.mergeCells(1, 1, all_col, 1); // 标题2
		     
		   ws.addCell(new Label(0,2,head[2], wcf2));
		   ws.mergeCells(0, 2, 2, 3); // 时间
		   
		   ws.addCell(new Label(3,2,head[3], wcf2)); //日期
                                            
		   ws.addCell(new Label(3,3,head[4], wcf2)); //星期
		   
		  int col_day = weeks.length + 4 ; 
		  int k = 0;
		  for(int i=4;i<=col_day;i++){	 	  
           if(k+1 <= weeks.length ){
        	   ws.addCell(new Label(i,2,""+Dates[k],wcf2)); //日期天数 
        	   ws.addCell(new Label(i,3,""+weeks[k],wcf2)); //日期星期       	   
           }else {
        	   ws.addCell(new Label(i,3,"合计",wcf2)); //合计    	   
           } 
		   ++k;
		  }
		  
		  ws.addCell(new Label(0,4,head[5], wcf2)); //当日进场客户数
		  ws.mergeCells(0, 4, 0, 7);
		  
		  ws.addCell(new Label(1,4,"保养客户数", wcf2)); //保养客户数
		  ws.mergeCells(1, 4, 3, 4);
		  
		  ws.addCell(new Label(1,5,"维修客户数", wcf2)); //维修客户数
		  ws.mergeCells(1, 5, 3, 5);
		
		  ws.addCell(new Label(1,6,"三包内客户数量", wcf2));//三包内客户数量
		  ws.mergeCells(1, 6, 3, 6);
		  
		  ws.addCell(new Label(1,7,"三包外客户数量", wcf2));//三包外客户数量
		  ws.mergeCells(1, 7, 3, 7);
		  
		  ws.addCell(new Label(0,8,head[6], wcf2));//当日客户回访
		  ws.mergeCells(0,8, 0, 22);
		  
		  ws.addCell(new Label(1,8,"回访客户数", wcf2));  //回访客户数 
		  ws.mergeCells(1,8, 3, 8);
		  
		  ws.addCell(new Label(1,9,"回访不成功原因", wcf2));//回访不成功原因
		  ws.mergeCells(1,9, 1, 11);
		  
		  ws.addCell(new Label(2,9,"客户信息错误", wcf2));//客户信息错误
		  ws.mergeCells(2,9, 3, 9);
		  
		  ws.addCell(new Label(2,10,"无法接通", wcf2));//无法接通
		  ws.mergeCells(2,10, 3, 10);
		  
		  ws.addCell(new Label(2,11,"拒绝回访", wcf2));//拒绝回访
		  ws.mergeCells(2,11, 3, 11);
		  
		  ws.addCell(new Label(1,12,"满意客户数", wcf2));//满意客户数
		  ws.mergeCells(1,12, 3, 12);
		  
		  ws.addCell(new Label(1,13,"不满意项", wcf2));//不满意项
		  ws.mergeCells(1,13, 1, 20);
		  
		  ws.addCell(new Label(2,13,"服务接待", wcf2));//服务接待
		  ws.mergeCells(2,13, 3, 13);
		  
		  ws.addCell(new Label(2,14,"服务环境", wcf2));//服务环境
		  ws.mergeCells(2,14, 3, 14);
		  
		  ws.addCell(new Label(2,15,"维修质量", wcf2));//维修质量
		  ws.mergeCells(2,15, 3, 15);
		  
		  ws.addCell(new Label(2,16,"等候时间", wcf2));//等候时间
		  ws.mergeCells(2,16, 3, 16);
		  
		  ws.addCell(new Label(2,17,"维修时间", wcf2));//维修时间
		  ws.mergeCells(2,17, 3, 17);
		  
		  ws.addCell(new Label(2,18,"备件保供", wcf2));//备件保供
		  ws.mergeCells(2,18, 3, 18);
		  
		  ws.addCell(new Label(2,19,"维修收费", wcf2));//维修收费
		  ws.mergeCells(2,19, 3, 19);
		  
		  ws.addCell(new Label(2,20,"产品质量", wcf2)); //产品质量
		  ws.mergeCells(2,20,3,20);
		  
		  ws.addCell(new Label(1,21,"推荐客户数", wcf2));//不推荐客户数
		  ws.mergeCells(1,21,3,21);
		  
		  ws.addCell(new Label(1,22,"不接受推荐客户数", wcf2));//不推荐客户数
		  ws.mergeCells(1,22,3,22);
		  
		  ws.addCell(new Label(0,23,"统计分析项目", wcf2));//统计分析项目
		  ws.mergeCells(0,23,0,27);
		  
		  ws.addCell(new Label(1,23,"1.当月三包内客户占比=当月三包内客户数量/（三包内客户数量+三包外客户数量）×100%", wcf3));
		  ws.mergeCells(1,23,10,23);
		  
		  ws.addCell(new Label(1,24,"2.当月回访客户信息准确率=当月回访客户信息正确数/当月回访客户数总数×100%", wcf3));
		  ws.mergeCells(1,24,10,24);
		  
		  ws.addCell(new Label(1,25,"3.当月维修客户满意率=当月满意客户数/当月回访客户总数×100%", wcf3));
		  ws.mergeCells(1,25,10,25);
		  
		  ws.addCell(new Label(1,26,"4.当月回访客户推荐率=当月推荐客户数/当月回访客户总数×100%", wcf3));
		  ws.mergeCells(1,26,10,26);
 
		  ws.addCell(new Label(1,27,"5.维修能力指数=当月日平均保养维修总数/工位数×5", wcf3));
		  ws.mergeCells(1,27,10,27);
		  
		  ws.addCell(new Label(12,23,"当月三包内客户占比：", wcf3));
		  ws.mergeCells(12,23,14,23);
		 
		  ws.addCell(new Label(12,24,"当月回访客户信息准确率：", wcf3));
		  ws.mergeCells(12,24,14,24);
		   
		  ws.addCell(new Label(12,25,"当月维修客户满意率：", wcf3));
		  ws.mergeCells(12,25,14,25);
		  
		  ws.addCell(new Label(12,26,"当月回访客户推荐率：", wcf3));
		  ws.mergeCells(12,26,14,26);
		  
		  ws.addCell(new Label(12,27,"维修能力指数：", wcf3));
		  ws.mergeCells(12,27,14,27);
		  	 
		     
			 int y = 0; //集合下标
			 for(int l=4;l<23;l++){
				 int x = 0; //数组下标
 				 String [] s =  (String[]) list.get(y); 
				 int total = 0; // 合计
			  for(int c=4;c<col_day+1;c++){		  
				  if(x <s.length){
					  total = total + Integer.parseInt(s[x]);
					if("0".equals(s[x])){
							  s[x] = "";
						  }	
				     ws.addCell(new Label(c,l,s[x], wcf3));			 
				  }else {
				     ws.addCell(new Label(c,l,""+total, wcf3));	  
				  }
				  ++x;
			   }
			      ++y;
			 } 
			
			int e = 23 ; 
			for(int i= 0 ; i<list_rate.size();i++){
				 ws.addCell(new Label(15,e,""+list_rate.get(i), wcf3));
				 ++e;
			} 	 
	     wwb.write();
		 out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}