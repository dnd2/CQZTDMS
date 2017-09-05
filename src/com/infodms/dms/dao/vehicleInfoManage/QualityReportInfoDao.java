package com.infodms.dms.dao.vehicleInfoManage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.QualityReportInfoMaintasinPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtDmsFlowPO;
import com.infodms.dms.po.TtSalesQualityAuditRecordPO;
import com.infodms.dms.po.TtSalesQualityReportInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class QualityReportInfoDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(QualityReportInfoDao.class);
	private static final QualityReportInfoDao dao = new QualityReportInfoDao();

	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * dao单例 提供控制层action调用
	 * 
	 * @return
	 */
	public static final QualityReportInfoDao getInstance() {
		if (dao == null) {
			return new QualityReportInfoDao();
		}
		return dao;
	}
	/**
	 * 拼接的Str静态创建
	 * @return
	 */
	private static StringBuffer JoinInstance(){
		return new StringBuffer();
	}


	/**
	 * 查询列表的数据
	 * @param request
	 * @param currDealerId
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> qualityReportInfo(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb = JoinInstance();
		sb.append("select  t.*,d.dealer_code as dealercode,d.dealer_shortname\n" );
		sb.append("  from Tt_Sales_Quality_Report_Info t join tm_dealer d on d.dealer_id=t.dealer_id \n" );
		sb.append(" where t.VERIFY_STATUS not in (95451004, 95451001, 95451002, 95451003) and  t.dealer_Id="+currDealerId);
		DaoFactory.getsql(sb, "t.vin", DaoFactory.getParam(request, "vin"), 2);
		String status = request.getParamValue("status");
		if(StringUtil.notNull(status)){
			sb.append(" and t.VERIFY_STATUS="+status);
		}
		String jsaudit = request.getParamValue("jsaudit");
		if(StringUtil.notNull(jsaudit)){
			sb.append(" and t.AUDIT_NAME like '%"+jsaudit+"%'");
		}
		String reportname = request.getParamValue("reportname");
		if(StringUtil.notNull(reportname)){
			sb.append(" and t.REPORT_NAME like '%"+reportname+"%'");
		}
		String username = request.getParamValue("username");
		if(StringUtil.notNull(username)){
			sb.append(" and t.USER_NAME like '%"+username+"%'");
		}
		String dealername = request.getParamValue("dealername");
		if(StringUtil.notNull(dealername)){
			sb.append(" and d.dealer_shortname like '%"+dealername+"%'");
		}
		String reportDate1 = request.getParamValue("reportDate1");
		if(Utility.testString(reportDate1)){
			sb.append(" AND  TO_DATE(t.REPORT_DATE,'YYYY-MM-DD' ) >= TO_DATE('"+reportDate1+"', 'YYYY-MM-DD') \n");  
		}
		String reportDate2 = request.getParamValue("reportDate2");
		if(Utility.testString(reportDate2)){
			sb.append("  AND TO_DATE(t.REPORT_DATE,'YYYY-MM-DD' ) <= TO_DATE('"+reportDate2+"', 'YYYY-MM-DD') \n");  
		}
		sb.append(" order by t.REPORT_DATE desc");
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	public Map<String, Object> queryServiceDataById(Long currDealerId) {
		StringBuffer sbSql= JoinInstance();
		sbSql.append("select b.dealer_code DEALER_CODE,\n");
		sbSql.append("       b.dealer_name DEALER_NAME \n");
		sbSql.append("  from tm_dealer b\n");
		sbSql.append(" where b.dealer_id = "+currDealerId); 
		Map<String, Object> ps = pageQueryMap(sbSql.toString(), null, getFunName());
		return ps;
	}

	public List<FsFileuploadPO> queryAttById(String qualityId) {
		StringBuffer sql = JoinInstance();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A WHERE 1=1");
		sql.append(" AND A.YWZJ='" + qualityId + "'");
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}

	public Map<String, Object> queryDataById(String dealerId) {
		StringBuffer sbSql = JoinInstance();
		sbSql.append("select b.dealer_code DEALER_CODE,\n");
		sbSql.append("       b.dealer_name DEALER_NAME \n");
		sbSql.append("  from tm_dealer b\n");
		sbSql.append(" where b.dealer_id = "+dealerId); 
		Map<String, Object> ps = pageQueryMap(sbSql.toString(), null, getFunName());
		return ps;
	}

	public PageResult<Map<String, Object>> queryVINInfoList(RequestWrapper request, Long currDealerId, Integer currPage, Integer pageSize) {
		String claim_no = CommonUtils.checkNull(request.getParamValue("CLAIM_NO"));//索赔单号
        String vin = CommonUtils.checkNull(request.getParamValue("VIN"));// VIN
        PageResult<Map<String, Object>> ps;
		StringBuffer sb=JoinInstance();
		sb.append("SELECT t.VIN ,t.CLAIM_NO,to_char(o.CREATE_DATE, 'yyyy-MM-dd') CREATE_DATE from TT_AS_WR_APPLICATION t left join TT_AS_REPAIR_ORDER o on t.RO_NO=o.RO_NO where 1=1 and t.IS_IMPORT=10041002 and t.claim_type<>10661011 ");
		if(currDealerId!=null){
			if(StringUtil.notNull(currDealerId.toString())){
				sb.append("  and nvl(t.dealer_id,t.second_dealer_id)="+currDealerId);
			}
		}
		if(!"".equals(claim_no)){
			sb.append(" and t.claim_no like '%"+claim_no+"%'");
		}
		if(!"".equals(vin)){
			sb.append(" and t.vin like '%"+vin+"%'");
		}
		ps = pageQuery(sb.toString(), null, getFunName(), pageSize,currPage);
		return ps;
	}

	public Map<String, Object> queryDataByVin(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select b.dealer_code DEALER_CODE,\n" );
		sb.append("       f.group_name cxname,\n" );
		sb.append("       b.dealer_name DEALER_NAME,\n" );
		sb.append("       c.group_name MODEL_NAME,\n" );
		sb.append("       wg.game_name GAME_NAME,\n" );
		sb.append("       a.model_id MODEL_ID,\n" );
		sb.append("       a.engine_no ENGINE_NO,\n" );
		sb.append("       a.SERIES_ID SERIES_ID,\n" );
		sb.append("       a.mileage MILEAGE,\n" );
		sb.append("       a.LICENSE_NO LICENSE_NO,\n" );
		sb.append("       to_char(a.product_date, 'yyyy-MM-dd') PRODUCT_DATE,\n" );
		sb.append("       to_char(a.purchased_date, 'yyyy-MM-dd') BUY_DATE,\n" );
		sb.append("       e.ctm_name CTM_NAME,\n" );
		sb.append("       e.main_phone PHONE,\n" );
		sb.append("       e.ADDRESS ADDRESS\n" );
		sb.append("  from tm_vehicle a\n" );
		sb.append("  left join tm_dealer b\n" );
		sb.append("    on a.dealer_id = b.dealer_id\n" );
		sb.append("  left join tm_vhcl_material_group c\n" );
		sb.append("    on a.model_id = c.group_id\n" );
		sb.append("  left join tm_vhcl_material_group f\n" );
		sb.append("    on a.series_id = f.group_id\n" );
		sb.append("  left join tt_dealer_actual_sales d\n" );
		sb.append("    on a.vehicle_id = d.vehicle_id\n" );
		sb.append("  left join tt_customer e\n" );
		sb.append("    on e.ctm_id = d.ctm_id\n" );
		sb.append("  left join tt_as_wr_game wg\n" );
		sb.append("    on wg.id = a.claim_tactics_id");
		sb.append(" where a.vin = '"+request.getParamValue("vin")+"'"); 
		Map<String, Object> ps = pageQueryMap(sb.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 添加或修改
	 * @param request
	 * @param currDealerId
	 * @param qualityId
	 * @param dealerCode
	 * @param dealerName
	 */
	public String saveOrupdate(RequestWrapper request, Long currDealerId, String qualityId, AclUserBean logonUser,String[]fjids) {
		String ywzj=null;
		try {
			TtSalesQualityReportInfoPO po=new TtSalesQualityReportInfoPO();
			po = setProperties(request,po,currDealerId,logonUser);
			Long id=null;
			//新增
			if(CommonUtils.checkIsNullStr(qualityId)){
				id = ConvertLong(SequenceManager.getSequence(""));
				po.setQualityId(id);
				//this.remberInsertAudit(request, id);
				dao.insert(po);
			}else{
				id=Long.parseLong(qualityId);
				//修改
				TtSalesQualityReportInfoPO po1=new TtSalesQualityReportInfoPO();
				po1.setQualityId(id);
				dao.update(po1, po);
			}
			ywzj = String.valueOf(id);
			delAndReinsetFile(logonUser, fjids, ywzj);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ywzj;
	}
	private void delAndReinsetFile(AclUserBean loginUser, String[] fjids,
			String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}
	/**
	 * 新增是先插入一条记录审批的
	 * @param request
	 * @param id
	 */
	private void remberInsertAudit(RequestWrapper request, Long id) {
		String verifyStatus = request.getParamValue("verifyStatus");
		TtDmsFlowPO tf =new TtDmsFlowPO();
		tf.setMajorKey(id);
		tf.setId(id);
		tf.setStatus(Long.parseLong(String.valueOf(verifyStatus)));
		dao.insert(tf);
	}
	/**
	 * 为审批赋值
	 * @param majorKey
	 * @param logonUser
	 * @param type 1技术部 2质量部
	 * @param verifyStatus 
	 */
	public void updateFlow(String majorKey,AclUserBean logonUser,int type, String verifyStatus){
		Long majorKeyTemp = ConvertLong(majorKey);
		//先判断是否有数据（因为正式库没有维护数据）
		TtDmsFlowPO po = getAudit(majorKey);
		if(po==null){
			TtDmsFlowPO tf =new TtDmsFlowPO();
			tf.setMajorKey(majorKeyTemp);
			tf.setId(majorKeyTemp);
			tf.setStatus(Long.parseLong(String.valueOf(verifyStatus)));
			dao.insert(tf);
		}
		TtDmsFlowPO t1=new TtDmsFlowPO();
		TtDmsFlowPO t2=new TtDmsFlowPO();
		t1.setMajorKey(majorKeyTemp);
		String name = logonUser.getName();
		String systemDateStr = getSystemDateStr();
		if(type==1){
			t2.setAuditName(name);
			t2.setAuditDate(systemDateStr);
			t2.setStatus(ConvertLong(verifyStatus));
		}
		if(type==2){
			t2.setNextAuditName(name);
			t2.setNextAuditDate(systemDateStr);
			t2.setStatus(ConvertLong(verifyStatus));
		}
		dao.update(t1, t2);
	}
	
	/**
	 * 新增审核记录
	 * @param majorKey
	 * @param logonUser
	 * @param type 1技术部 2质量部
	 * @param verifyStatus 
	 */
	public void insertFlow(String majorKey,AclUserBean logonUser,String technicalAuditAdvice, String verifyStatus){
			TtDmsFlowPO tf =new TtDmsFlowPO();
			Long majorKeyTemp = ConvertLong(majorKey);
			tf.setMajorKey(majorKeyTemp);
			tf.setId(ConvertLong(SequenceManager.getSequence("")));
			tf.setAuditName(logonUser.getName());
			tf.setAuditDate(getSystemDateStr());
			tf.setStatus(Long.parseLong(String.valueOf(verifyStatus)));
			tf.setAuditAdvice(technicalAuditAdvice);
			dao.insert(tf);
			
			//修改当前审核人，时间
			TtSalesQualityReportInfoPO po1=new TtSalesQualityReportInfoPO();
			TtSalesQualityReportInfoPO po=new TtSalesQualityReportInfoPO();
			po1.setQualityId(majorKeyTemp);
			po.setAuditName(logonUser.getName());
			po.setAuditDate(getSystemDateStr());
			dao.update(po1, po);
	}
	
	/**
	 * 根据业务主键查询审批记录表
	 * @param majorKey
	 * @return
	 */
	public TtDmsFlowPO getAudit(String majorKey){
		TtDmsFlowPO t=new TtDmsFlowPO();
		t.setMajorKey(ConvertLong(majorKey));
		List<TtDmsFlowPO> list = dao.select(t);
		if(list!=null && list.size()>=1){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 转化系统时间
	 */
	private String getSystemDateStr() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}
	/**
	 * 属性设置值
	 * @param request
	 * @param po
	 * @param currDealerId
	 * @param logonUser 
	 */
	private TtSalesQualityReportInfoPO setProperties(RequestWrapper request,TtSalesQualityReportInfoPO po,Long currDealerId, AclUserBean logonUser) {
		String dealerId = logonUser.getDealerId();
		if(Utility.testString(dealerId)){
			TmDealerPO t=new TmDealerPO();
			t.setDealerId(Long.parseLong(dealerId));
			List list = dao.select(t);
			if(list!=null&& list.size()>0){
				TmDealerPO temp = (TmDealerPO) list.get(0);
				po.setDealerCode(temp.getDealerCode());//经销商code
				po.setDealerName(temp.getDealerShortname());//经销商名称
			}
		}else{
			po.setDealerCode(logonUser.getDealerCode());//主机厂
			po.setDealerName(logonUser.getName());//用户名称
		}
		String claimNo = request.getParamValue("claimNo");
		String isend = request.getParamValue("isend");
		String theme = request.getParamValue("theme");
		String customerComplainNeed = request.getParamValue("customerComplainNeed");
		String problemReason = request.getParamValue("problemReason");
		String checkStepFit = request.getParamValue("checkStepFit");
		String dealAdvice = request.getParamValue("dealAdvice");
		String technicalAuditAdvice = request.getParamValue("technicalAuditAdvice");
		String againAuditAdvice = request.getParamValue("againAuditAdvice");
		String reportNameAdvice = request.getParamValue("reportNameAdvice");
		String applyNews = request.getParamValue("applyNews");
		String reportName = request.getParamValue("reportName");
		String contactType = request.getParamValue("contactType");
		String carClass = request.getParamValue("carClass");
		String carType = request.getParamValue("carType");
		String carNo = request.getParamValue("carNo");
		String vin = request.getParamValue("vin");
		String engineNo = request.getParamValue("engineNo");
		String carSpend = request.getParamValue("carSpend");
		String mileage = request.getParamValue("mileage");
		String carUseType = request.getParamValue("carUseType");
		String userName = request.getParamValue("userName");
		String userAddr = request.getParamValue("userAddr");
		String userPhone = request.getParamValue("userPhone");
		String runSpeed = request.getParamValue("runSpeed");
		String engineSpeed = request.getParamValue("engineSpeed");
		String carStatusRemark = request.getParamValue("carStatusRemark");
		String problemTheSameRemark = request.getParamValue("problemTheSameRemark");
		String problemCodeRemark = request.getParamValue("problemCodeRemark");
		String firstProblemCode = request.getParamValue("firstProblemCode");
		String firstProblemName = request.getParamValue("firstProblemName");
		String firstProblemSupplierCode = request.getParamValue("firstProblemSupplierCode");
		String listGroup = request.getParamValue("listGroup");
		String reportDate = request.getParamValue("reportDate");
		String productDate = request.getParamValue("productDate");
		String buyCarDate = request.getParamValue("buyCarDate");
		String faultDate = request.getParamValue("faultDate");
		String serviceDate = request.getParamValue("serviceDate");
		String verifyStatus = request.getParamValue("verifyStatus");
		
		String partChangeStatus = Convert(request.getParamValues("partChangeStatus"));
		String isFit = Convert(request.getParamValues("isFit"));
		String productQuality = Convert(request.getParamValues("productQuality"));
		String carStatusByProblem = Convert(request.getParamValues("carStatusByProblem"));
		String wayStatus = Convert(request.getParamValues("wayStatus"));
		String weatherStatus = Convert(request.getParamValues("weatherStatus"));
		String oilLeak = Convert(request.getParamValues("oilLeak"));
		String problemProperties = Convert(request.getParamValues("problemProperties"));
		String problemTheSame =  Convert(request.getParamValues("problemTheSame"));
		String problemCode = Convert(request.getParamValues("problemCode"));
		String isAduit = Convert(request.getParamValues("isAduit"));
		String isKeepFit = Convert(request.getParamValues("isKeepFit"));
		String oilLeaRemark = request.getParamValue("oilLeaRemark");
		
		po.setOilLeaRemark(oilLeaRemark);
		po.setProblemReason(problemReason);
		po.setCheckStepFit(checkStepFit);
		po.setDealAdvice(dealAdvice);
		po.setTechnicalAuditAdvice(technicalAuditAdvice);
		po.setAgainAuditAdvice(againAuditAdvice);
		po.setDealerId(currDealerId);
		po.setClaimNo(claimNo);
		po.setReportDate(reportDate.replaceAll(" ", ""));
		po.setReportName(reportName);
		po.setContactType(contactType);
		po.setIsFit(isFit);
		po.setProductQuality(productQuality);
		po.setCarClass(carClass);
		po.setCarType(carType);
		po.setCarNo(carNo);
		po.setVin(vin);
		po.setIsend(isend);
		if("95531002".equals(verifyStatus)){
			po.setReportDateBtn(BaseUtils.getSystemDateStr2());//上报时记录上报按钮时间
		}
		
		//4位序列号
		String seqFour = this.getSeq(logonUser.getDealerCode());
		if(CommonUtils.checkNull(engineNo).length()>0){
			String enginNoTmep = engineNo.replaceAll(" ", "");
			//enginNoTmep+=seqFour;
			po.setEngineNo(enginNoTmep);
		}
		if(CommonUtils.checkNull(productDate).length()>0){
			po.setProductDate(productDate.replaceAll(" ", ""));
		}
		if(CommonUtils.checkNull(buyCarDate).length()>0){
			po.setBuyCarDate(buyCarDate.replaceAll(" ", ""));
		}
		if(CommonUtils.checkNull(faultDate).length()>0){
			po.setFaultDate(faultDate.replaceAll(" ", ""));
		}
		if(CommonUtils.checkNull(serviceDate).length()>0){
			po.setServiceDate(serviceDate.replaceAll(" ", ""));
		}
		po.setCarSpend(carSpend);
		po.setMileage(ConvertLong(mileage));
		po.setCarUseType(carUseType);
		po.setUserName(userName);
		po.setUserAddr(userAddr);
		po.setUserPhone(userPhone);
		po.setIsKeepFit(isKeepFit);
		po.setCarStatusByProblem(carStatusByProblem);
		po.setCarStatusRemark(carStatusRemark);
		po.setRunSpeed(ConvertInt(runSpeed));
		po.setApplyNews(applyNews);
		po.setOilLeak(oilLeak);
		po.setProblemCode(problemCode);
		po.setEngineSpeed(ConvertInt(engineSpeed));
		po.setWayStatus(wayStatus);
		po.setWeatherStatus(weatherStatus);
		po.setProblemProperties(problemProperties);
		po.setProblemTheSame(problemTheSame);
		po.setProblemTheSameRemark(ConvertInt(problemTheSameRemark));
		po.setIsAduit(isAduit);
		po.setProblemCodeRemark(problemCodeRemark);
		po.setFirstProblemCode(firstProblemCode);
		po.setFirstProblemName(firstProblemName);
		po.setFirstProblemSupplierCode(firstProblemSupplierCode);
		po.setListGroup(listGroup);
		po.setTheme(theme);
		po.setCustomerComplainNeed(customerComplainNeed);
		po.setReportNameAdvice(reportNameAdvice);
		po.setPartChangeStatus(partChangeStatus);
		po.setVerifyStatus(ConvertInt(verifyStatus));
		StringBuffer sb= JoinInstance();
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		sb.append(logonUser.getDealerCode());
		sb.append(String.valueOf(year));
		sb.append(String.valueOf(month));
		sb.append(seqFour);
		//设置编号+4位序列号
		String randomId=sb.toString();
		po.setRandomId(randomId);
		
		return po;
	}
	/**
	 * 得到编号
	 * @return
	 */
	private String getSeq(String dealerCode) {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);
		String sql="select * from QUALITY_REPORT_INFO_MAINTASIN t where t.service_code=?  and t.t_year=? and t.t_month=? and t.random_id is not null";
		QualityReportInfoMaintasinPO po=null;
		List<Object> params=new ArrayList<Object>();
		params.add(dealerCode);
		params.add(year);
		params.add(month);
		//查出的是当年当月的数据
		List<Map<String,Object>> list= dao.pageQuery(sql, params, getFunName());
		//先增加
		if(list.size()==0){
			po=new QualityReportInfoMaintasinPO();
			po.setId(ConvertLong(SequenceManager.getSequence("")));
			
			po.setTYear(year);
			po.setTMonth(month);
			po.setTDay(day);
			po.setRandomId(1);
			po.setServiceCode(dealerCode==null?"":dealerCode);
			dao.insert(po);
			return "0001";
		//后修改
		}else if(list.size()>0){
			Map<String, Object> map = list.get(0);
			BigDecimal randomid= (BigDecimal) map.get("RANDOM_ID");
			BigDecimal id= (BigDecimal) map.get("ID");
			po=new QualityReportInfoMaintasinPO();
			po.setId(id.longValue());
			QualityReportInfoMaintasinPO po1=new QualityReportInfoMaintasinPO();
			po1.setId(id.longValue());
			po1.setServiceCode(dealerCode);
			po1.setTYear(year);
			po1.setTMonth(month);
			po1.setTDay(day);
			BigDecimal tempOne = new BigDecimal(1); 
			String RandomStr = returnRandomStr(randomid.add(tempOne).intValue());
			po1.setRandomId(ConvertInt(RandomStr));
			dao.update(po, po1);
			return RandomStr;
		}
		return sql;
	}
	/**
	 * 返回一个带 "0"或不带"0"的四位字符
	 * @param value
	 * @return
	 */
	private String returnRandomStr(int value) {
		if(value>0 && value<10){
			return "000"+value;
		}else if(value>=10 && value<100){
			return "00"+value;
		}else if(value>=100&& value<1000){
			return "0"+value;
		}else if(value>=1000 && value<10000){
			return String.valueOf(value);
		}else{
			return "";
		}
	}

	public Integer ConvertInt(String str){
		if(StringUtil.isNull(str)){
			return null;
		}
		return Integer.parseInt(str);
	}
	public Long ConvertLong(String str){
		if(StringUtil.isNull(str)){
			return null;
		}
		return Long.parseLong(str);
	}
	public Double ConvertDouble(String str){
		if(StringUtil.isNull(str)){
			return null;
		}
		return Double.parseDouble(str);
	}
	/**
	 * 转换 xx,xx,xx
	 * @param paramValues
	 * @return
	 */
	private String Convert(String[] paramValues) {
		StringBuffer sb= JoinInstance();
		if(paramValues==null){
			return "";
		}
		int len=paramValues.length;
		if(len==1){
			sb.append(paramValues[0]);
		}
		if(len>1){
			for (int i = 0; i < len; i++) {
				if(i==len-1){
					sb.append(paramValues[i]);
				}else{
					sb.append(paramValues[i]+",");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 技术部审核
	 * @param request
	 * @param currDealerId
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> technicalSupportDeptInfo(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb = JoinInstance();
		sb.append("select t.*,d.dealer_code as dealercode,d.dealer_shortname,( select ds.root_org_name from vw_org_dealer_service ds where ds.dealer_id=d.dealer_id) as org_name\n" );
		sb.append("  from Tt_Sales_Quality_Report_Info t join tm_dealer d on d.dealer_id=t.dealer_id  where 1=1 and t.VERIFY_STATUS not in(95451004,95451001,95451002,95451003) \n" );
		String status = DaoFactory.getParam(request, "status");
		String jsaudit = DaoFactory.getParam(request, "jsaudit");
		String audit_date_start = DaoFactory.getParam(request, "audit_date_start");//审核时间
		String audit_date_end = DaoFactory.getParam(request, "audit_date_end");//审核时间
		String FIRST_PROBLEM_CODE = DaoFactory.getParam(request, "FIRST_PROBLEM_CODE");//故障件代码
		String FIRST_PROBLEM_NAME = DaoFactory.getParam(request, "FIRST_PROBLEM_NAME");//故障件名称
		String vin = DaoFactory.getParam(request, "vin");
		String reportname = DaoFactory.getParam(request, "reportname");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String dealerName = DaoFactory.getParam(request, "dealerName");
		String reportDate1 = DaoFactory.getParam(request, "reportDate1");
		String reportDate2 = DaoFactory.getParam(request, "reportDate2");
		DaoFactory.getsql(sb, "t.verify_status", status, 1);
		DaoFactory.getsql(sb, "t.FIRST_PROBLEM_CODE", FIRST_PROBLEM_CODE, 2);
		DaoFactory.getsql(sb, "t.FIRST_PROBLEM_NAME", FIRST_PROBLEM_NAME, 2);
		DaoFactory.getsql(sb, "to_date(f.AUDIT_DATE,'yyyy-mm-dd hh24:mi:ss')", audit_date_start, 3);
		DaoFactory.getsql(sb, "to_date(f.AUDIT_DATE,'yyyy-mm-dd hh24:mi:ss')", audit_date_end, 4);
		DaoFactory.getsql(sb, "t.AUDIT_NAME", jsaudit, 2);
		DaoFactory.getsql(sb, "t.VIN", vin, 2);
		DaoFactory.getsql(sb, "d.REPORT_NAME", reportname, 2);
		DaoFactory.getsql(sb, "d.DEALER_CODE", dealerCode, 6);
		DaoFactory.getsql(sb, "d.dealer_shortname", dealerName, 2);
		DaoFactory.getsql(sb, "t.report_date_btn", reportDate1, 31);
		DaoFactory.getsql(sb, "t.report_date_btn", reportDate2, 41);
		sb.append(" order by t.report_date_btn desc");
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	/**
	 * 质量部审核
	 * @param request
	 * @param currDealerId
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> qualityDeptInfo(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb = JoinInstance();
		sb.append("select t.*,f.*,d.dealer_code as dealercode,d.dealer_shortname ,( select ds.root_org_name from vw_org_dealer_service ds where ds.dealer_id=d.dealer_id) as org_name from Tt_Sales_Quality_Report_Info t join tm_dealer d on d.dealer_id=t.dealer_id left join tt_dms_flow f on f.MAJOR_KEY=t.QUALITY_ID where 1=1 and t.VERIFY_STATUS not in(95451004,95451001,95451002,95451003)");
		String status = DaoFactory.getParam(request, "status");
		String zlaudit = DaoFactory.getParam(request, "zlaudit");
		String vin = DaoFactory.getParam(request, "vin");
		String reportname = DaoFactory.getParam(request, "reportname");
		String dealerCode = DaoFactory.getParam(request, "dealerCode");
		String dealerName = DaoFactory.getParam(request, "dealerName");
		String reportDate1 = DaoFactory.getParam(request, "reportDate1");
		String reportDate2 = DaoFactory.getParam(request, "reportDate2");
		DaoFactory.getsql(sb, "t.verify_status", status, 1);
		DaoFactory.getsql(sb, "f.next_audit_name", zlaudit, 2);
		DaoFactory.getsql(sb, "t.VIN", vin, 2);
		DaoFactory.getsql(sb, "t.REPORT_NAME", reportname, 2);
		DaoFactory.getsql(sb, "d.DEALER_CODE", dealerCode, 6);
		DaoFactory.getsql(sb, "d.dealer_shortname", dealerName, 2);
		DaoFactory.getsql(sb, "t.report_date_btn", reportDate1, 31);
		DaoFactory.getsql(sb, "t.report_date_btn", reportDate2, 41);
		sb.append(" order by t.report_date_btn desc");
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	public PageResult<Map<String, Object>> data2(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb = JoinInstance();
		sb.append("select t.*,f.* from Tt_Sales_Quality_Report_Info t left join tt_dms_flow f on f.MAJOR_KEY=t.QUALITY_ID where 1=1 and t.DEALER_CODE is null");
		String status = request.getParamValue("status");
		if(StringUtil.notNull(status)){
			sb.append(" and t.VERIFY_STATUS="+status);
		}
		String reportname = request.getParamValue("reportname");
		if(StringUtil.notNull(reportname)){
			sb.append(" and t.REPORT_NAME like '%"+reportname+"%'");
		}
		String username = request.getParamValue("username");
		if(StringUtil.notNull(username)){
			sb.append(" and t.USER_NAME like '%"+username+"%'");
		}
		String dealername = request.getParamValue("dealername");
		if(StringUtil.notNull(dealername)){
			sb.append(" and t.DEALER_NAME like '%"+dealername+"%'");
		}
		sb.append(" order by t.REPORT_DATE desc");
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	public PageResult<Map<String, Object>> data1(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb = JoinInstance();
		sb.append("select t.*,f.* from Tt_Sales_Quality_Report_Info t left join tt_dms_flow f on f.MAJOR_KEY=t.QUALITY_ID where 1=1 and t.DEALER_CODE is null");
		String status = request.getParamValue("status");
		if(StringUtil.notNull(status)){
			sb.append(" and t.VERIFY_STATUS="+status);
		}
		String reportname = request.getParamValue("reportname");
		if(StringUtil.notNull(reportname)){
			sb.append(" and t.REPORT_NAME like '%"+reportname+"%'");
		}
		String username = request.getParamValue("username");
		if(StringUtil.notNull(username)){
			sb.append(" and t.USER_NAME like '%"+username+"%'");
		}
		String dealername = request.getParamValue("dealername");
		if(StringUtil.notNull(dealername)){
			sb.append(" and t.DEALER_NAME like '%"+dealername+"%'");
		}
		sb.append(" order by t.REPORT_DATE desc");
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	
	/**
	 * 质量信息上报数量 / 这个月服务站上报索赔单的数量  * 35
	 * @return
	 */
	public Integer getAddNumberScore() {
		Integer score=null;
		Integer cliamNo=null;//这个月服务站上报索赔单的数量
		Integer countAllNo=null;//质量信息上报数量
		String sql="SELECT COUNT(*) AS CLIAMNO FROM TT_AS_WR_APPLICATION T WHERE T.REPORT_DATE >= TRUNC(SYSDATE, 'MM') AND T.REPORT_DATE <= LAST_DAY(SYSDATE)";
		//注意这个算法只是计算服务商端上报的单子的数量（T.DEALER_CODE IS NOT NULL）
		String countAllNosql="SELECT COUNT(*) AS COUNTALLNO FROM TT_SALES_QUALITY_REPORT_INFO T WHERE TO_DATE(T.REPORT_DATE，'YYYY-MM-DD') >= TRUNC(SYSDATE, 'MM') AND TO_DATE(T.REPORT_DATE，'YYYY-MM-DD') <= LAST_DAY(SYSDATE) AND T.DEALER_CODE IS NOT NULL";
		cliamNo = this.getNo(sql,"CLIAMNO");
		countAllNo= this.getNo(countAllNosql,"COUNTALLNO");
		if(cliamNo==0){
			score=30;
		}else{
			score=(countAllNo/cliamNo)*30;
			if(score>=30){
				score=30;
			}
		}
		return score;
	}

	private Integer getNo(String sql,String getKey) {
		List<Map<String,Object>> list= dao.pageQuery(sql, null, getFunName());
		Map<String, Object> map = list.get(0);
		BigDecimal numStr= (BigDecimal) map.get(getKey);
		return numStr.intValue();
	}

	public PageResult<Map<String, Object>> selectmalfunction(RequestWrapper request, Long currDealerId, Integer currPage, int pageSize) {
		String malcode = CommonUtils.checkNull(request.getParamValue("malcode"));
        String malname = CommonUtils.checkNull(request.getParamValue("malname"));
        PageResult<Map<String, Object>> ps;
		StringBuffer sb=JoinInstance();
		/*sb.append("SELECT T.* FROM TT_AS_WR_MALFUNCTION T WHERE 1=1 ");
		if(!"".equals(malcode)){
			sb.append(" AND T.MAL_CODE LIKE '%"+malcode+"%'");
		}
		if(!"".equals(malname)){
			sb.append(" AND T.MAL_NAME LIKE '%"+malname+"%'");
		}*/
		sb.append("SELECT T.* FROM TT_PART_DEFINE T WHERE 1=1 ");
		if(!"".equals(malcode)){
			sb.append(" AND T.PART_CODE LIKE '%"+malcode+"%'");
		}
		if(!"".equals(malname)){
			sb.append(" AND T.PART_CNAME LIKE '%"+malname+"%'");
		}
		ps = pageQuery(sb.toString(), null, getFunName(), pageSize,currPage);
		return ps;
	}
	/**
	 * 技术部审核驳回
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public int auditRefuseByTechnicalDept(RequestWrapper request, AclUserBean loginUser) {
		int i=0;
		try {
			String qualityid = CommonUtils.checkNull(request.getParamValue("qualityid"));
			String verifyStatus = CommonUtils.checkNull(request.getParamValue("verifyStatus"));
			String addQualityScore = CommonUtils.checkNull(request.getParamValue("addQualityScore"));
			String technicalAuditAdvice = CommonUtils.checkNull(request.getParamValue("technicalAuditAdvice"));
			String auditStatus = CommonUtils.checkNull(request.getParamValue("auditStatus"));

			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			Integer inTimeScore = this.getinTimeScore(po2);//设置及时性分数30 0
			po2.setInTimeScore(inTimeScore);
			int parseInt = Integer.parseInt(addQualityScore);
			po2.setAddQualityScore(parseInt);//填报质量评分的分数0~35 
			int addNumberScore = this.getAddNumberScore();
			po2.setAddNumberScore(addNumberScore);//填报数量分数需要算法  0-35
			po2.setCountScore(inTimeScore+parseInt+addNumberScore);
			po2.setTechnicalAuditAdvice(technicalAuditAdvice);
			
			dao.update(po, po2);
			this.recordAudit(loginUser, qualityid, auditStatus,technicalAuditAdvice,1);
		} catch (NumberFormatException e) {
			i=-1;
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 质量信息上报数量 / 这个月服务站上报索赔单的数量  * 35
	 * @return
	 */
	/**
	 * 设置及时性的分数 上报日期- 维修日期=48小时 30 
	 * @param po2
	 * @throws ParseException
	 */
	private Integer getinTimeScore(TtSalesQualityReportInfoPO po2)throws ParseException {
		Long servicedate =new java.text.SimpleDateFormat("yyyy-MM-dd").parse(po2.getServiceDate()).getTime();
		String reportDate = po2.getReportDate();
		Long time = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(reportDate).getTime();
		Long restTime=time-servicedate;
		if(restTime<=(getLong48())){
			return 30;
		}
		return 0;
	}
	private long getLong48() {
		return 48L*1000L*60L*60L;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return
	   */
	public static Long getNow() {
	   Date currentTime = new Date();
	   Long time = currentTime.getTime();
	   return time;
	}

	/**
	   * 获取现在时间
	   * 
	   * @return 返回短时间字符串格式yyyy-MM-dd
	   */
	public static String getStringDateShort() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}

	public int auditPassByTechnicalDept(RequestWrapper request,AclUserBean loginUser) {
		int i=0;
		try {
			String qualityid = CommonUtils.checkNull(request.getParamValue("qualityid"));
			String verifyStatus = CommonUtils.checkNull(request.getParamValue("verifyStatus"));
			String addQualityScore = CommonUtils.checkNull(request.getParamValue("addQualityScore"));
			String technicalAuditAdvice = CommonUtils.checkNull(request.getParamValue("technicalAuditAdvice"));//审批意见
			String againAuditAdvice = CommonUtils.checkNull(request.getParamValue("againAuditAdvice"));
			String isend = CommonUtils.checkNull(request.getParamValue("isend"));
			String auditStatus = CommonUtils.checkNull(request.getParamValue("auditStatus"));

			
			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			po2.setAgainAuditAdvice(againAuditAdvice);
			po2.setTechnicalAuditAdvice(technicalAuditAdvice);
			Integer inTimeScore = this.getinTimeScore(po2);//设置及时性分数30 0
			po2.setInTimeScore(inTimeScore);
			int parseInt = Integer.parseInt(addQualityScore);
			po2.setAddQualityScore(parseInt);//填报质量评分的分数0~35 
			int addNumberScore = this.getAddNumberScore();
			po2.setAddNumberScore(addNumberScore);//填报数量分数需要算法  0-30
			po2.setCountScore(inTimeScore+parseInt+addNumberScore);
			po2.setIsend(isend);
			
			dao.update(po, po2);
			
			this.recordAudit(loginUser, qualityid, auditStatus,technicalAuditAdvice,1);
			String[] fjids=request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(qualityid, fjids, loginUser);
		} catch (NumberFormatException e) {
			i=-1;
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 加审批记录和 审核记录
	 * @param loginUser
	 * @param qualityid
	 * @param verifyStatus
	 * @param technicalAuditAdvice
	 */
	private void recordAudit(AclUserBean loginUser, String qualityid,String auditStatus, String technicalAuditAdvice,int type) {
		dao.insertFlow(qualityid, loginUser, technicalAuditAdvice,auditStatus);//审核记录
		TtSalesQualityAuditRecordPO re=new TtSalesQualityAuditRecordPO(DaoFactory.getPkId(),BaseUtils.ConvertLong(qualityid),BaseUtils.getSystemDateStr2(),"技术部",loginUser.getName(),auditStatus,technicalAuditAdvice);
		dao.insert(re);
	}

	public int auditPassByQualityDept(RequestWrapper request,AclUserBean loginUser) {
		int i=0;
		try {
			String qualityid = DaoFactory.getParam(request,"qualityid");
			String verifyStatus = DaoFactory.getParam(request,"verifyStatus");
			String applyNews = DaoFactory.getParam(request,"applyNews");
			
			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			po2.setApplyNews(applyNews);
			
			dao.update(po, po2);
			this.recordAudit(loginUser, qualityid, verifyStatus,applyNews,2);
		} catch (NumberFormatException e) {
			i=-1;
			e.printStackTrace();
		}
		return i;
	}

	public int auditRefuseByQualityDept(RequestWrapper request,AclUserBean loginUser) {
		int i=0;
		try {
			String qualityid = DaoFactory.getParam(request,"qualityid");
			String verifyStatus = DaoFactory.getParam(request,"verifyStatus");
			String applyNews = DaoFactory.getParam(request,"applyNews");
			//po
			TtSalesQualityReportInfoPO po = new TtSalesQualityReportInfoPO();
			po.setQualityId(Long.parseLong(qualityid));
			//po2
			TtSalesQualityReportInfoPO po2 = new TtSalesQualityReportInfoPO();
			po2.setQualityId(Long.parseLong(qualityid));
			po2=(TtSalesQualityReportInfoPO) dao.select(po2).get(0);
			po2.setVerifyStatus(Integer.parseInt(verifyStatus));
			po2.setApplyNews(applyNews);
			
			dao.update(po, po2);
			this.recordAudit(loginUser, qualityid, verifyStatus,applyNews,2);
		} catch (Exception e) {
			i=-1;
			e.printStackTrace();
		}
		return i;
	}

	public List<Map<String, Object>> selectAuditRecord(String qualityId) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" SELECT T.* FROM TT_SALES_QUALITY_AUDIT_RECORD T WHERE T.quality_id='"+qualityId+"'");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}
	
	
	public PageResult<Map<String, Object>> carAlarm(RequestWrapper request,  Integer pageSize,Integer currPage, HashMap params) {
		PageResult<Map<String, Object>> list = null;
		StringBuffer sb= new StringBuffer();
		sb.append("\n" );
		sb.append("select dc.vin\n" );
		sb.append("  from tt_as_wr_vin_repair_days dc\n" );
		sb.append(" where dc.cur_days > (SELECT min(t.vr_warranty) minwarn\n" );
		sb.append("                        from TT_AS_WR_VIN_RULE t\n" );
		sb.append("                       where t.VR_TYPE = 94021001)\n" );
		if(params.get("vin")!=null){
			sb.append(" and vin like '%"+params.get("vin")+"%'");
		}
		sb.append("union\n" );
		sb.append("select distinct (t.vin)\n" );
		sb.append("  from tt_as_wr_vin_part_repair_times t\n" );
		sb.append(" where CUR_TIMES >= (select min(VR_WARRANTY)\n" );
		sb.append("                       from tt_as_wr_vin_rule\n" );
		sb.append("                      where PART_WR_TYPE = 94031001)");
		if(params.get("vin")!=null){
			sb.append(" and vin like '%"+params.get("vin")+"%'");
		}
		list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public void exportToexceltechnical(RequestWrapper request,AclUserBean loginUser, ActionContext act, Integer pageSizeMax,Integer currPage) {
		PageResult<Map<String,Object>> list=this.technicalSupportDeptInfo(request,1l,Constant.PAGE_SIZE_MAX,currPage);
		try {
		    String[] head={"填报人","联系电话","大区","经销商编码","经销商名称","车型","VIN","故障件代码","故障件名称","主题","审核人","审核时间","填报日期","上报日期","状态"};
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[15];
					detail[0]=BaseUtils.checkNull(map.get("REPORT_NAME"));
					detail[1]=BaseUtils.checkNull(map.get("CONTACT_TYPE"));
					detail[2]=BaseUtils.checkNull(map.get("ORG_NAME"));
					detail[3]=BaseUtils.checkNull(map.get("DEALERCODE"));
					
					detail[4]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[5]=BaseUtils.checkNull(map.get("CAR_TYPE"));
					detail[6]=BaseUtils.checkNull(map.get("VIN"));
					detail[7]=BaseUtils.checkNull(map.get("FIRST_PROBLEM_CODE"));
					detail[8]=BaseUtils.checkNull(map.get("FIRST_PROBLEM_NAME"));
					detail[9]=BaseUtils.checkNull(map.get("THEME"));
					detail[10]=BaseUtils.checkNull(map.get("AUDIT_NAME"));
					detail[11]=BaseUtils.checkNull(map.get("AUDIT_DATE"));
					detail[12]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[13]=BaseUtils.checkNull(map.get("REPORT_DATE_BTN"));
					detail[14]=this.getTypeDesc(BaseUtils.checkNull(map.get("VERIFY_STATUS")));
					params.add(detail);
				}
			}
			    String systemDateStr = BaseUtils.getSystemDateStr();
				BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "技术部支持审批数据导出"+systemDateStr+".xls", "导出数据",null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	public String getTypeDesc(String codeId){
		TcCodePO t=new TcCodePO();
		t.setCodeId(codeId);
		List<TcCodePO> list= dao.select(t);
		if(list!=null && list.size()>0){
			t=list.get(0);
			return t.getCodeDesc();
		}else{
			return "无";
		}
	}
	
	/**
	 * 查询审核数据
	 * @param cpid
	 * @return
	 */
	public List<Map<String, Object>> queryDealRecord(long id) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.AUDIT_NAME,\n");
		sql.append("           T.AUDIT_DATE,\n");
		sql.append("           F_GET_TCCODE_DESC(T.STATUS) STATUS,\n");
		sql.append("           T.AUDIT_ADVICE\n");
		sql.append("      FROM TT_DMS_FLOW T\n");
		sql.append("     WHERE T.MAJOR_KEY = "+id+"");
		sql.append("     ORDER BY T.ID DESC ");


		return this.pageQuery(sql.toString(), null, null);
	}
	
	/**
	 * 查询审核数据
	 * @param cpid
	 * @return
	 */
	public Map<String, Object> getDealerInfo(String id) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.dealer_code dealerCode,t.dealer_name dealerName from tm_dealer t where t.dealer_id="+id+"");

	
		return this.pageQueryMap(sql.toString(), null, null);
	}
}
