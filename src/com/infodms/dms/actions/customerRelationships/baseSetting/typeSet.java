package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 




import javax.sound.midi.Sequence;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CodeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.FileConstant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.TypeSetDao;
import com.infodms.dms.dao.sales.dealer.XsDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFinReturnTypePO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TmpXsDealerPO;
import com.infodms.dms.po.TtDealerPreFinPO;
import com.infodms.dms.po.TtDealerPreFinRecordPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.lowagie.tools.concat_pdf;

import flex.messaging.io.ArrayList;

/**
 * 
 * @ClassName     : typeSet
 * @Description   : 类型设置
 * @author        : wangming
 * CreateDate     : 2013-4-6
 */
public class typeSet extends BaseImport{
	private static Logger logger = Logger.getLogger(typeSet.class);
	// 类型初始化页面
	private final String typeSetUrl = "/jsp/customerRelationships/baseSetting/typeSet.jsp";
	private final String delaerAddSetAddOrUpdateInitUrl = "/jsp/systemMng/orgMng/delaerAddSetAddOrUpdate.jsp";
	private final String chengduiTypeSetAddInitUrl = "/jsp/sales/financemanage/DerlerCreditLimit/chengduiTypeSetAdd.jsp";
	//类型新增页面
	private final String typeSetAddOrUpdateUrl = "/jsp/customerRelationships/baseSetting/typeSetAddOrUpdate.jsp";
	private final String FinTypeSetAdd = "/jsp/sales/financemanage/DerlerCreditLimit/FinTypeSetAdd.jsp";
	//返利类型新增页面
	private final String RebateTypeSetAdd = "/jsp/sales/financemanage/DerlerCreditLimit/RebateTypeSetAdd.jsp";
	
	private final String discountInputAdd = "/jsp/sales/financemanage/DerlerCreditLimit/discountInputAdd.jsp";

	private final String discountAudit = "/jsp/sales/financemanage/DerlerCreditLimit/discountAudit.jsp";
	
	private final String preFinDealerImportInit = "/jsp/systemMng/dealer/preFinDealerImportInit.jsp";
	
	private final String discountInput = "/jsp/sales/financemanage/DerlerCreditLimit/discountInput.jsp";
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 类型初始化
	 */
	public void typeSetInit(){		
		try{	
			act.setOutData("typeList", initTypeTopSelect());
			act.setForword(typeSetUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"类型初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//类型顶级
	private List<Map<String,Object>> initTypeTopSelect(){
		CommonUtilDao dao = CommonUtilDao.getInstance();
		List<Map<String,Object>> list = dao.getTcCode(Constant.TYPE_TOP);
		return list;
	}
	
	//初始化类型固定的下拉列表
	private List<Map<String,Object>> initTypeSelect(){
		TypeSetDao dao = TypeSetDao.getInstance();
		List<Map<String,Object>> list = dao.getTypeSelete();
		for(Map<String,Object> map : list){
			int lev = Integer.parseInt(map.get("LEV").toString());
			String descCode = (String)map.get("CODEDESCVIEW");
			descCode = changeDescCode(lev, descCode);
			map.put("CODEDESCVIEW", descCode);
		}
		return list;
	}
	
	public List<Map<String,Object>> getTypeSelect(String type){
		TypeSetDao dao = TypeSetDao.getInstance();
		List<Map<String,Object>> list = dao.getTypeSelete(type);
		int maxLev = dao.getMaxLevelTypeSelete(type);
		for(Map<String,Object> map : list){
			int lev = Integer.parseInt(map.get("LEV").toString());
			String descCode = (String)map.get("CODEDESCVIEW");
			descCode = changeDescCode(lev, descCode);
			map.put("CODEDESCVIEW", descCode);
			map.put("MAXLEV", maxLev);
		}
		return list;
	}
	/**
	 * 根据级别手动处理空格问题
	 * @param lev 级别
	 * @return String
	 */
	private String changeDescCode(int lev,String descCode){
		if(lev == 1) return descCode;
		String str = "";
		for(int i = 0; i<lev-1;i++){
			str +="--";
		}
		return str+descCode;
	}
	public void queryTypeSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			String type = CommonUtils.checkNull(request.getParamValue("type"));  				
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.queryTypeSet(type,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"座席组查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryTypeSetForAddSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.queryTypeSet(Constant.SH_ADDRESS_TYPE.toString(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"座席组查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryTypeSetForTypeSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.queryTypeSet(Constant.VR_TYPE.toString(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"打款用途查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryFinTypeSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.queryTypeSetForCredit(Constant.ACCOUNT_TYPE.toString(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"资金类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRebateTypeSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.queryTypeSetForRebate(Constant.ACCOUNT_TYPE.toString(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"返利类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//与折扣录入查询
	public void discountInputQuery(){
		act.getResponse().setContentType("application/json");
		try{
			
			String dealerId = request.getParamValue("dealerId");
			String month = request.getParamValue("DATE");
			String AUDIT_STATUS_ = request.getParamValue("AUDIT_STATUS");
			String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = (request.getParamValue("curPage") != null&&!request.getParamValue("curPage").equals("undefined")) ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			
			if(cmd.equals("1")){
				List<Map<String,Object>> mapList = dao.discountInputQuery(Constant.ACCOUNT_TYPE.toString(),Constant.PAGE_SIZE_MAX,curPage,dealerId,month,AUDIT_STATUS_).getRecords();
				
				ApplicationDao application = new ApplicationDao();
				 String[] head={"经销商名称","返利类型","月份","申请预返利金额","财务审核比例","实际审核通过预返利金额","审核状态","录入人","录入时间","备注"};
					List<Map<String, Object>> records = mapList;
					List params=new ArrayList();
					if(records!=null &&records.size()>0){
						for (Map<String, Object> map1 : records) {
							String DEALER_NAME = BaseUtils.checkNull(map1.get("DEALER_NAME"));
							String FIN_RETURN_NAME = BaseUtils.checkNull(map1.get("FIN_RETURN_NAME"));
							String MONTH = BaseUtils.checkNull(map1.get("MONTH"));
							String AMOUNT = BaseUtils.checkNull(map1.get("AMOUNT"));
							String DIS_PERCENT = BaseUtils.checkNull(map1.get("DIS_PERCENT_NAME"));
							String ACTUAL_AMOUNT = BaseUtils.checkNull(map1.get("ACTUAL_AMOUNT"));
							String AUDIT_STATUS = BaseUtils.checkNull(map1.get("AUDIT_STATUS_NAME"));
							String CREATE_BY = BaseUtils.checkNull(map1.get("CREATE_BY"));
							String CREATE_DATE = BaseUtils.checkNull(map1.get("CREATE_DATE"));
							String REMARK = BaseUtils.checkNull(map1.get("REMARK"));
							String[] detail={DEALER_NAME,FIN_RETURN_NAME,MONTH,AMOUNT,DIS_PERCENT,ACTUAL_AMOUNT,AUDIT_STATUS,CREATE_BY,CREATE_DATE,REMARK};
							params.add(detail);
						}
					}
						application.toExcel(act, head, params,null,"预折扣录入查询");
			}else{
				PageResult<Map<String,Object>> queryTypeSetData = dao.discountInputQuery(Constant.ACCOUNT_TYPE.toString(),Constant.PAGE_SIZE,curPage,dealerId,month,AUDIT_STATUS_);
				act.setOutData("ps", queryTypeSetData);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"返利类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//与折扣录入查询
	public void discountAuditQuery(){
		act.getResponse().setContentType("application/json");
		try{
			String dealerId = request.getParamValue("dealerId");
			String month = request.getParamValue("DATE");
			String AUDIT_STATUS = request.getParamValue("AUDIT_STATUS");
			
			TypeSetDao dao = TypeSetDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryTypeSetData = dao.discountAuditQuery(Constant.ACCOUNT_TYPE.toString(),Constant.PAGE_SIZE,curPage,dealerId,month,AUDIT_STATUS);
			
			act.setOutData("ps", queryTypeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"返利类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 类型增加或修改
	 * @Description: 类型增加或修改 
	 * LastDate    : 2013-4-6
	 */
	public void addOrUpdateTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TcCodePO tcCodePO = new TcCodePO();
			tcCodePO.setCodeId(id);
			TcCodePO tcCodePO2 = dao.queryTcCodePO(tcCodePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(typeSetAddOrUpdateUrl);
	}
	
	public void UpdateTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TcCodePO tcCodePO = new TcCodePO();
			tcCodePO.setCodeId(id);
			TcCodePO tcCodePO2 = dao.queryTcCodePO(tcCodePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(delaerAddSetAddOrUpdateInitUrl);
	}
	public void UpdateTypeSetForType(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TcCodePO tcCodePO = new TcCodePO();
			tcCodePO.setCodeId(id);
			TcCodePO tcCodePO2 = dao.queryTcCodePO(tcCodePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(chengduiTypeSetAddInitUrl);
	}
	public void UpdateFinTypeSetForType(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		String cmd = request.getParamValue("cmd");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TtVsAccountTypePO accountTypePO = new TtVsAccountTypePO();
			accountTypePO.setTypeId(Long.parseLong(id));
			TtVsAccountTypePO tcCodePO2 = dao.queryTtVsAccountTypePO(accountTypePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("cmd", cmd);
		act.setForword(FinTypeSetAdd);
	}
	
	public void UpdateRebateTypeSetForType(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TmFinReturnTypePO accountTypePO = new TmFinReturnTypePO();
			accountTypePO.setReturnTypeId(Long.parseLong(id));
			TmFinReturnTypePO tcCodePO2 = dao.queryTmFinReturnTypePO(accountTypePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setForword(RebateTypeSetAdd);
	}
	
	public void UpdateDiscountInput(){
		ActionContext act = ActionContext.getContext();
		
		
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			TtDealerPreFinPO accountTypePO = new TtDealerPreFinPO();
			accountTypePO.setPreId(Long.parseLong(id));
			TtDealerPreFinPO  tcCodePO2 = dao.queryTtDealerPreFinPO(accountTypePO);
			act.setOutData("tcCodePO", tcCodePO2);
//			if(dao.isExists(tcCodePO2.getDealerId().toString(), dateFormat.format(tcCodePO2.getMonth()))){
//				act.setOutData("sussess", "1");
//				return;
//			}
			String month = dateFormat.format(tcCodePO2.getMonth());
			act.setOutData("month", month);
			
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(tcCodePO2.getDealerId());
			TmDealerPO dealerPO2 = dao.queryTmDealerPO(dealerPO);
			act.setOutData("dealerInfo", dealerPO2);
			
			act.setOutData("is_Mod", true);
			
		}
		
		TmFinReturnTypePO typePO = new TmFinReturnTypePO();
		TypeSetDao dao = TypeSetDao.getInstance();
		
		act.setOutData("fin_type", dao.select(typePO));
//		act.setOutData("sussess", "true");
		act.setForword(discountInputAdd);
	}
	
	public void tongguo(){
		ActionContext act = ActionContext.getContext();
		Date date = new Date();
		String id = request.getParamValue("id");
		String DIS_PERCENT = request.getParamValue("DIS_PERCENT_");
		String AMOUNT_ = request.getParamValue("AMOUNT_");
		String remark = request.getParamValue("remark_"+id);
		Double AMOUNT = new Double(AMOUNT_);
		
		TtDealerPreFinPO typePO = new TtDealerPreFinPO();
		typePO.setPreId(new Long(id));
		
		TtDealerPreFinPO typePO1 = new TtDealerPreFinPO();
		typePO1.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_03);
		typePO1.setUpdateBy(logonUser.getUserId());
		typePO1.setUpdateDate(date);
		if(StringUtil.isNull(DIS_PERCENT)){
			typePO1.setDisPercent(1D);
		}else{
			typePO1.setDisPercent(Double.parseDouble(DIS_PERCENT));
		}
		
		
		typePO1.setActualAmount(AMOUNT*typePO1.getDisPercent()/100);
		
		typePO1.setAuditBy(logonUser.getUserId());
		typePO1.setAuditDate(date);
		
		TypeSetDao dao = TypeSetDao.getInstance();
		dao.update(typePO, typePO1);
		
		TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
		recordPO.setAuditBy(logonUser.getUserId());
		recordPO.setAuditDate(date);
		recordPO.setAuditRemark(remark);
		recordPO.setPreId(new Long(id));
		recordPO.setAuditStatus(Long.parseLong(typePO1.getAuditStatus().toString()));
		recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
		
		dao.insert(recordPO);
		act.setForword(discountAudit);
	}
	
	public void batchAccept(){
		ActionContext act = ActionContext.getContext();
		Date date = new Date();
		String id = request.getParamValue("id");
		String [] ids = id.split(",");
		
		
		TtDealerPreFinPO typePO = new TtDealerPreFinPO();
		
		for (int i = 0; i < ids.length; i++) {
			String DIS_PERCENT = request.getParamValue("DIS_PERCENT_"+ids[i]);
			String AMOUNT_ = request.getParamValue("AMOUNT_"+ids[i]);
			Double AMOUNT = new Double(AMOUNT_);
			String remark = request.getParamValue("remark_"+ids[i]);
			typePO.setPreId(new Long(ids[i]));
			
			TtDealerPreFinPO typePO1 = new TtDealerPreFinPO();
			typePO1.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_03);
			typePO1.setUpdateBy(logonUser.getUserId());
			typePO1.setUpdateDate(date);
			if(StringUtil.isNull(DIS_PERCENT)){
				typePO1.setDisPercent(1D);
			}else{
				typePO1.setDisPercent(Double.parseDouble(DIS_PERCENT));
			}
			
			typePO1.setActualAmount(AMOUNT*typePO1.getDisPercent()/100);
			
			typePO1.setAuditBy(logonUser.getUserId());
			typePO1.setAuditDate(date);
			
			TypeSetDao dao = TypeSetDao.getInstance();
			dao.update(typePO, typePO1);
			
			TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
			recordPO.setAuditBy(logonUser.getUserId());
			recordPO.setAuditDate(new Date());
			recordPO.setAuditRemark(remark);
			recordPO.setPreId(new Long(ids[i]));
			recordPO.setAuditStatus(Long.parseLong(typePO1.getAuditStatus().toString()));
			recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
			
			dao.insert(recordPO);
		}
		
		act.setForword(discountAudit);
	}
	
	public void bohui(){
		ActionContext act = ActionContext.getContext();
		
		Date date = new Date();
		String id = request.getParamValue("id");
		String remark = request.getParamValue("remark_"+id);
		TtDealerPreFinPO typePO = new TtDealerPreFinPO();
		typePO.setPreId(new Long(id));
		
		TtDealerPreFinPO typePO1 = new TtDealerPreFinPO();
		typePO1.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_04);
		typePO1.setUpdateBy(logonUser.getUserId());
		typePO1.setUpdateDate(date);
		
		typePO1.setAuditBy(logonUser.getUserId());
		typePO1.setAuditDate(date);
		
		TypeSetDao dao = TypeSetDao.getInstance();
		dao.update(typePO, typePO1);
		
		TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
		recordPO.setAuditBy(logonUser.getUserId());
		recordPO.setAuditDate(date);
		recordPO.setAuditRemark(remark);
		recordPO.setPreId(new Long(id));
		recordPO.setAuditStatus(Long.parseLong(typePO1.getAuditStatus().toString()));
		recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
		
		dao.insert(recordPO);
		act.setForword(discountAudit);
	}
	
	public void batchRebut(){
		ActionContext act = ActionContext.getContext();
		Date date = new Date();
		String id = request.getParamValue("id");
		String [] ids = id.split(",");
		
		for (int i = 0; i < ids.length; i++) {
			
			String remark = request.getParamValue("remark_"+ids[i]);
			TtDealerPreFinPO typePO = new TtDealerPreFinPO();
			typePO.setPreId(new Long(ids[i]));
			
			TtDealerPreFinPO typePO1 = new TtDealerPreFinPO();
			typePO1.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_04);
			typePO1.setUpdateBy(logonUser.getUserId());
			typePO1.setUpdateDate(date);
			
			typePO1.setAuditBy(logonUser.getUserId());
			typePO1.setAuditDate(date);
			
			TypeSetDao dao = TypeSetDao.getInstance();
			dao.update(typePO, typePO1);
			
			TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
			recordPO.setAuditBy(logonUser.getUserId());
			recordPO.setAuditDate(date);
			recordPO.setAuditRemark(remark);
			recordPO.setPreId(new Long(ids[i]));
			recordPO.setAuditStatus(Long.parseLong(typePO1.getAuditStatus().toString()));
			recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
			
			dao.insert(recordPO);
		}
		
		act.setForword(discountAudit);
	}
	
	public void dealerAddSetAddOrUpdateTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TcCodePO tcCodePO = new TcCodePO();
			tcCodePO.setCodeId(id);
			TcCodePO tcCodePO2 = dao.queryTcCodePO(tcCodePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(delaerAddSetAddOrUpdateInitUrl);
	}
	
	public void typeSetAddTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TcCodePO tcCodePO = new TcCodePO();
			tcCodePO.setCodeId(id);
			TcCodePO tcCodePO2 = dao.queryTcCodePO(tcCodePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(chengduiTypeSetAddInitUrl);
	}
	public void typeSetAddFinTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		TtVsAccountTypePO accountTypePO = new TtVsAccountTypePO();
		TypeSetDao dao = TypeSetDao.getInstance();
		if(id!=null&&!"".equals(id)){
			accountTypePO.setTypeId(Long.parseLong(id));
			TtVsAccountTypePO tcCodePO2 = dao.queryTtVsAccountTypePO(accountTypePO);
			act.setOutData("tcCodePO", tcCodePO2);
			act.setOutData("is_Mod","yes");
		}else{
			accountTypePO.setStatus(10011001);
			accountTypePO = (TtVsAccountTypePO) dao.select(accountTypePO).get(0);
			act.setOutData("tcCodePO1", accountTypePO.getDiscountRate());
			act.setOutData("is_Add","yes");
		}
		
//		act.setOutData("typeList", initTypeSelect());
		act.setForword(FinTypeSetAdd);
	}
	//新增返利类型
	public void typeSetAddRebateTypeSet(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			TypeSetDao dao = TypeSetDao.getInstance();
			TtVsAccountTypePO accountTypePO = new TtVsAccountTypePO();
			accountTypePO.setTypeId(Long.parseLong(id));
			TtVsAccountTypePO tcCodePO2 = dao.queryTtVsAccountTypePO(accountTypePO);
			act.setOutData("tcCodePO", tcCodePO2);
		}
		act.setOutData("typeList", initTypeSelect());
		act.setForword(RebateTypeSetAdd);
	}
	
	//新增
	public void addDiscountInput(){
		
		ActionContext act = ActionContext.getContext();
		
		TmFinReturnTypePO typePO = new TmFinReturnTypePO();
		TypeSetDao dao = TypeSetDao.getInstance();
		
		act.setOutData("fin_type", dao.select(typePO));
		
		act.setForword(discountInputAdd);
	}
	
public void addTypeSetSubmit(){
		
		String codeDesc = CommonUtils.checkNull(request.getParamValue("codeDesc"));  
		String name = CommonUtils.checkNull(request.getParamValue("name")); //类型内容
		
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			//检查是否重复
			TcCodePO tcCodePO = new TcCodePO();
			TcCodePO tcCodePO2 = new TcCodePO();
			
			tcCodePO.setCodeId((String.valueOf(Integer.valueOf(getCodeIDByType("9402"))+1)));
			tcCodePO.setCreateBy(logonUser.getUserId());
			tcCodePO.setCreateDate(new Date());
			tcCodePO.setType("9402");
//			tcCodePO.setCodeParentId(typeId);
			tcCodePO.setTypeName(name);
			tcCodePO.setCodeDesc(codeDesc);
			tcCodePO.setNum(getSizeByType("9402")+1);
			tcCodePO.setStatus(Constant.STATUS_ENABLE);
			dao.insert(tcCodePO);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}}

public void addFinTypeSetSubmit(){
	
	String typeCode = CommonUtils.checkNull(request.getParamValue("typeCode"));  
	String typeName = CommonUtils.checkNull(request.getParamValue("typeName")); //类型内容
	String tiexiRate = CommonUtils.checkNull(request.getParamValue("tiexiRate")); //类型内容
	try{
		TypeSetDao dao = TypeSetDao.getInstance();
		
		TtVsAccountTypePO accountTypePO = new TtVsAccountTypePO();
		accountTypePO.setTypeId(Long.parseLong(SequenceManager.getSequence("")));
		accountTypePO.setCreateBy(logonUser.getUserId());
		accountTypePO.setCreateDate(new Date());
		accountTypePO.setTypeCode(typeCode);
		accountTypePO.setTypeName(typeName);
		
		accountTypePO.setStatus(Constant.STATUS_ENABLE);
		accountTypePO.setTypeClass(getSize("1025")+1);
		
		dao.insert(accountTypePO);
		
		dao.update("update TT_VS_ACCOUNT_TYPE set DISCOUNT_RATE = "+tiexiRate+"", null);
		act.setOutData("success", "true");
		
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}}
public void addRebateTypeSetSubmit(){
	
		String typeCode = CommonUtils.checkNull(request.getParamValue("typeCode"));  
		String importType = CommonUtils.checkNull(request.getParamValue("IMPORT_TYPE"));  
		String typeName = CommonUtils.checkNull(request.getParamValue("typeName")); //类型内容
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			
			TmFinReturnTypePO finReturnTypePO = new TmFinReturnTypePO();
			finReturnTypePO.setReturnTypeId(Long.parseLong(SequenceManager.getSequence("")));
			finReturnTypePO.setCreateBy(logonUser.getUserId());
			finReturnTypePO.setCreateDate(new Date());
			finReturnTypePO.setFinReturnCode(typeCode);
			finReturnTypePO.setImportType(new Integer(importType));
			finReturnTypePO.setFinReturnName(typeName);
			finReturnTypePO.setStatus(Constant.STATUS_ENABLE);
			finReturnTypePO.setFinReturnType(getSizeByRebate("1902")+1);
			
			dao.insert(finReturnTypePO);
			act.setOutData("success", "true");
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}

public void discountInputAddSubmit(){
	
	String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT"));  
	String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
	String month = CommonUtils.checkNull(request.getParamValue("DATE"));
	String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));
	String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
	String preId = CommonUtils.checkNull(request.getParamValue("preId"));
	String finReturnType = CommonUtils.checkNull(request.getParamValue("fin_type"));
	
	try{
		TypeSetDao dao = TypeSetDao.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		TtDealerPreFinPO finPO = new TtDealerPreFinPO();
		
		if(StringUtil.isNull(preId)){
			if(dao.isExists(finReturnType,dealerId, month)){
				act.setOutData("success", "1");
				return;
			}
			if(cmd.equals("2")){
				finPO = new TtDealerPreFinPO();
				
				finPO.setAmount(Double.parseDouble(amount));
				finPO.setCreateBy(logonUser.getUserId());
				finPO.setCreateDate(new Date());
				finPO.setDealerId(Long.parseLong(dealerId));
				finPO.setMonth(dateFormat.parse(month));
				finPO.setRemark(remark);
				finPO.setPreId(new Long(SequenceManager.getSequence("")));
				finPO.setFinReturnType(new Long(finReturnType));
				finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_02);
			}else{
				
				finPO = new TtDealerPreFinPO();
				
				finPO.setAmount(Double.parseDouble(amount));
				finPO.setCreateBy(logonUser.getUserId());
				finPO.setCreateDate(new Date());
				finPO.setDealerId(Long.parseLong(dealerId));
				finPO.setMonth(dateFormat.parse(month));
				finPO.setRemark(remark);
				finPO.setPreId(new Long(SequenceManager.getSequence("")));
				finPO.setFinReturnType(new Long(finReturnType));
				finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_01);
			}
			
			
			dao.insert(finPO);
			
			TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
			recordPO.setAuditBy(logonUser.getUserId());
			recordPO.setAuditDate(new Date());
			recordPO.setAuditRemark("");
			recordPO.setPreId(finPO.getPreId());
			recordPO.setAuditStatus(Long.parseLong(finPO.getAuditStatus().toString()));
			recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
			
			dao.insert(recordPO);
		}else{
			if(cmd.equals("2")){
				
				TtDealerPreFinPO finPO1 = new TtDealerPreFinPO();
				finPO1.setPreId(Long.parseLong(preId));
				finPO = new TtDealerPreFinPO();
				finPO = new TtDealerPreFinPO();
				
				finPO.setAmount(Double.parseDouble(amount));
				finPO.setUpdateBy(logonUser.getUserId());
				finPO.setUpdateDate(new Date());
				finPO.setMonth(dateFormat.parse(month));
				finPO.setRemark(remark);
				finPO.setFinReturnType(new Long(finReturnType));
				finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_02);
				
				dao.update(finPO1, finPO);
				
				TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
				recordPO.setAuditBy(logonUser.getUserId());
				recordPO.setAuditDate(new Date());
				recordPO.setAuditRemark("");
				recordPO.setPreId(new Long(preId));
				recordPO.setAuditStatus(Long.parseLong(finPO.getAuditStatus().toString()));
				recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
				
				dao.insert(recordPO);
				
			}else{
				TtDealerPreFinPO finPO1 = new TtDealerPreFinPO();
				finPO1.setPreId(Long.parseLong(preId));
				finPO = new TtDealerPreFinPO();
				
				finPO.setAmount(Double.parseDouble(amount));
				finPO.setUpdateBy(logonUser.getUserId());
				finPO.setUpdateDate(new Date());
				finPO.setMonth(dateFormat.parse(month));
				finPO.setRemark(remark);
				finPO.setFinReturnType(new Long(finReturnType));
				finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_01);
				
				dao.update(finPO1, finPO);
				
				TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
				recordPO.setAuditBy(logonUser.getUserId());
				recordPO.setAuditDate(new Date());
				recordPO.setAuditRemark("");
				recordPO.setPreId(new Long(preId));
				recordPO.setAuditStatus(Long.parseLong(finPO.getAuditStatus().toString()));
				recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
				
				dao.insert(recordPO);
			}
		}
		act.setOutData("success", "true");
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}
}

	public void batchComit() {

		String preId = request.getParamValue("id");

		try {
			TypeSetDao dao = TypeSetDao.getInstance();
			String[] preIds = preId.split(",");
			for (int i = 0; i < preId.split(",").length; i++) {
				TtDealerPreFinPO finPO = new TtDealerPreFinPO();
				TtDealerPreFinPO finPO1 = new TtDealerPreFinPO();
				finPO1.setPreId(Long.parseLong(preIds[i]));
				finPO = new TtDealerPreFinPO();
				finPO = new TtDealerPreFinPO();

				finPO.setUpdateBy(logonUser.getUserId());
				finPO.setUpdateDate(new Date());
				finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_02);

				dao.update(finPO1, finPO);

				TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
				recordPO.setAuditBy(logonUser.getUserId());
				recordPO.setAuditDate(new Date());
				recordPO.setAuditRemark("");
				recordPO.setPreId(new Long(preIds[i]));
				recordPO.setAuditStatus(Long.parseLong(finPO.getAuditStatus().toString()));
				recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
				
				dao.insert(recordPO);
			}
			act.setForword(discountInput);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "类型新增修改");
			logger.error(logger, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title      : 新增修改类型提交
	 * @Description: 新增修改类型提交
	 * LastDate    : 2013-4-6
	 */
	public void addOrUpdateTypeSetSubmit(){
		
		String type = CommonUtils.checkNull(request.getParamValue("type"));  				//类型
		String typeId = (type.split(","))[0];
		String typeName = (type.split(","))[1];
		String codeDesc = CommonUtils.checkNull(request.getParamValue("codeDesc"));  		        //类型内容
		
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			//检查是否重复
			int count = dao.getSizeByTypeAndCodeDesc(typeId,codeDesc);
			if(count>0) {
				act.setOutData("repeat", "true");
				return ;
			}
			
			TcCodePO tcCodePO = new TcCodePO();
			TcCodePO tcCodePO2 = new TcCodePO();
			
			String id = request.getParamValue("id");
			if(id!=null&&!"".equals(id)){
				tcCodePO2.setCodeId(id);
				tcCodePO.setUpdateBy(logonUser.getUserId());
				tcCodePO.setUpdateDate(new Date());
			}else{		
				tcCodePO.setCodeId(getCodeIDByType(typeId.substring(0, 4)));
				tcCodePO.setCreateBy(logonUser.getUserId());
				tcCodePO.setCreateDate(new Date());
			}
			tcCodePO.setType(typeId.substring(0, 4));
			tcCodePO.setCodeParentId(typeId);
			tcCodePO.setTypeName(typeName);
			tcCodePO.setCodeDesc(codeDesc);
			tcCodePO.setNum(getSizeByType(typeId.substring(0, 4))+1);
			tcCodePO.setStatus(Constant.STATUS_ENABLE);
			if(id!=null&&!"".equals(id)){
				dao.update(tcCodePO2, tcCodePO);
			}else{
				dao.insert(tcCodePO);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}

	}
public void addOrUpdateTypeSetSubmitForDealer(){
		
		String type = CommonUtils.checkNull(request.getParamValue("type"));  				//类型
		String codeDesc = CommonUtils.checkNull(request.getParamValue("codeDesc"));  
		String name = CommonUtils.checkNull(request.getParamValue("name")); //类型内容
		
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			//检查是否重复
			TcCodePO tcCodePO = new TcCodePO();
			TcCodePO tcCodePO2 = new TcCodePO();
			
			tcCodePO.setCodeId(getCodeIDByType("2048"));
			tcCodePO.setCreateBy(logonUser.getUserId());
			tcCodePO.setCreateDate(new Date());
			tcCodePO.setType("2048");
//			tcCodePO.setCodeParentId(typeId);
			tcCodePO.setTypeName(name);
			tcCodePO.setCodeDesc(codeDesc);
			tcCodePO.setNum(getSizeByType("2048")+1);
			tcCodePO.setStatus(Constant.STATUS_ENABLE);
			dao.insert(tcCodePO);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}}

public void UpdateTypeSetSubmitForDealer(){
	
	String type = CommonUtils.checkNull(request.getParamValue("type"));  				//类型
	String codeDesc = CommonUtils.checkNull(request.getParamValue("codeDesc"));  
	String name = CommonUtils.checkNull(request.getParamValue("name")); //类型内容
	
	try{
		TypeSetDao dao = TypeSetDao.getInstance();
		
		TcCodePO tcCodePO = new TcCodePO();
		TcCodePO tcCodePO2 = new TcCodePO();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			tcCodePO2.setCodeId(id);
			tcCodePO.setUpdateBy(logonUser.getUserId());
			tcCodePO.setUpdateDate(new Date());
		}
		tcCodePO.setType("2048");
//		tcCodePO.setCodeParentId(typeId);
		tcCodePO.setTypeName(name);
		tcCodePO.setCodeDesc(codeDesc);
		tcCodePO.setNum(getSizeByType("2048")+1);
		tcCodePO.setStatus(Constant.STATUS_ENABLE);
		if(id!=null&&!"".equals(id)){
			dao.update(tcCodePO2, tcCodePO);
		}
		act.setOutData("success", "true");
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}}

public void typeSetUpdateTypeSet(){
	
	String type = CommonUtils.checkNull(request.getParamValue("type"));  				//类型
	String codeDesc = CommonUtils.checkNull(request.getParamValue("codeDesc"));  
	String name = CommonUtils.checkNull(request.getParamValue("name")); //类型内容
	
	try{
		TypeSetDao dao = TypeSetDao.getInstance();
		
		TcCodePO tcCodePO = new TcCodePO();
		TcCodePO tcCodePO2 = new TcCodePO();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			tcCodePO2.setCodeId(id);
			tcCodePO.setUpdateBy(logonUser.getUserId());
			tcCodePO.setUpdateDate(new Date());
		}
		tcCodePO.setType("9402");
//		tcCodePO.setCodeParentId(typeId);
		tcCodePO.setTypeName(name);
		tcCodePO.setCodeDesc(codeDesc);
		tcCodePO.setNum(getSizeByType("9402")+1);
		tcCodePO.setStatus(Constant.STATUS_ENABLE);
		if(id!=null&&!"".equals(id)){
			dao.update(tcCodePO2, tcCodePO);
		}
		act.setOutData("success", "true");
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}}
public void typeSetUpdateFinTypeSet(){
	
	String typeCode = CommonUtils.checkNull(request.getParamValue("typeCode"));  
	String typeName = CommonUtils.checkNull(request.getParamValue("typeName")); //类型内容
	String tiexiRate = CommonUtils.checkNull(request.getParamValue("tiexiRate")); //贴现率
	
	try{
		String typeClass = CommonUtils.checkNull(request.getParamValue("typeClass")); 
		String cmd = CommonUtils.checkNull(request.getParamValue("cmd")); 
		
		TypeSetDao dao = TypeSetDao.getInstance();
		
		if(dao.queryFinType(typeClass).size()>0&&(!"2".equals(cmd))){
			act.setOutData("success", "false");
			return;
		}
		
		if("2".equals(cmd)){
			dao.update("update TT_VS_ACCOUNT_TYPE set Discount_Rate = "+tiexiRate+"", null);
		}else{
			TtVsAccountTypePO accountTypePO = new TtVsAccountTypePO();
			TtVsAccountTypePO accountTypePO2 = new TtVsAccountTypePO();
			
			String id = request.getParamValue("id");
			accountTypePO.setTypeId(Long.parseLong(id));
			
			accountTypePO2.setTypeCode(typeCode);
			accountTypePO2.setTypeName(typeName);
			dao.update(accountTypePO, accountTypePO2);
			
			dao.update("update TT_VS_ACCOUNT_TYPE set Discount_Rate = "+tiexiRate+"", null);
		}

		act.setOutData("success", "true");
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}}

public void typeSetUpdateRebateTypeSet(){
	
	String typeCode = CommonUtils.checkNull(request.getParamValue("typeCode"));  
	String importType = CommonUtils.checkNull(request.getParamValue("IMPORT_TYPE"));  
	String typeName = CommonUtils.checkNull(request.getParamValue("typeName")); //类型内容
	
	try{
		String typeClass = CommonUtils.checkNull(request.getParamValue("typeClass")); 
		
		TypeSetDao dao = TypeSetDao.getInstance();
		
		if(dao.queryRebateType(typeClass).size()>0){
			act.setOutData("success", "false");
			return;
		}
		
		TmFinReturnTypePO accountTypePO = new TmFinReturnTypePO();
		TmFinReturnTypePO accountTypePO2 = new TmFinReturnTypePO();
		
		String id = request.getParamValue("id");
		accountTypePO.setReturnTypeId(Long.parseLong(id));
		
		accountTypePO2.setFinReturnCode(typeCode);
		accountTypePO2.setFinReturnName(typeName);
		accountTypePO2.setImportType(new Integer(importType));
		dao.update(accountTypePO, accountTypePO2);
		act.setOutData("success", "true");
	}catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"类型新增修改");
		logger.error(logger,e1);
		act.setException(e1);
	}}
	/**
	 * 
	 * @Title      : getSizeByType
	 * @Description: 获取TC_Code表中TYPE字段开始的数据数量 
	 * @param      : @param typeName type字段数据
	 * @param      : @return int
	 * LastDate    : 2013-4-6
	 * @author Wangming
	 */
	private int getSizeByType(String typeId){
		TypeSetDao dao = TypeSetDao.getInstance();
		return dao.getSizeByType(typeId);
	}
	
	private int getSize(String typeId){
		TypeSetDao dao = TypeSetDao.getInstance();
		return dao.getSize(typeId);
	}
	
	private int getSizeByRebate(String typeId){
		TypeSetDao dao = TypeSetDao.getInstance();
		return dao.getSizeByRebate(typeId);
	}
	
	/**
	 * 
	 * @Title      : getCodeIDByType
	 * @Description: 获取CODE_ID值 如:20381001 自动增长不会超过1000
	 * @param      : @param typeName 类型TYPE值
	 * @param      : @return String 
	 * @throws     :
	 * LastDate    : 2013-4-6
	 * @author Wangming
	 */
	private String getCodeIDByType(String typeId){
		if(typeId==null||"".equals(typeId)) return null;
		int count = getSizeByType(typeId)+1;
		String codeId = null;
		switch(String.valueOf(count).length()){
			case 1:codeId = typeId + "100"+String.valueOf(count);break;
			case 2:codeId = typeId + "10"+String.valueOf(count);break;
			case 3:codeId = typeId + "1"+String.valueOf(count);break;
		}
		return codeId;
	}
	
	public void delTypeSetSubmit(){
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			String type = request.getParamValue("type");
			List<Map<String,Object>> list = dao.queryFinType(type);
			if(list.size()>0){
				act.setOutData("success", "1");
				return;
			}
			String ids = request.getParamValue("ids");
			ids = ids.replaceAll(",", "','");
			dao.deleteType(ids);
			act.setOutData("success", "0");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"类型");
			logger.error(logger,e1);
			act.setException(e1);
		}	
	}

	public void delRebateTypeSetSubmit(){
		try{
			TypeSetDao dao = TypeSetDao.getInstance();
			String type = request.getParamValue("type");
			List<Map<String,Object>> list = dao.queryRebateType(type);
			if(list.size()>0){
				act.setOutData("success", "1");
				return;
			}
			String ids = request.getParamValue("ids");
			ids = ids.replaceAll(",", "','");
			dao.deleteRebateType(ids);
			act.setOutData("success", "0");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"类型");
			logger.error(logger,e1);
			act.setException(e1);
		}	
	}
	
	public void upload() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(preFinDealerImportInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 预折扣模板下载
	 * @author RANJ
	 */
	public void tempDownload(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("经销商代码");
//			listHead.add("经销商名称");
			listHead.add("月份");
			listHead.add("预折扣金额");
			listHead.add("返利类型代码");
//			listHead.add("返利类型名称");
			listHead.add("备注");
			
			list.add(listHead);
			// 导出的文件名
			String fileName = "预折扣导入模版.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void tempUpload(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		XsDealerDao dao=XsDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",5,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
			}else{
				List<Map> list=getMapList();
				insertTmpXsDealer(list, logonUser.getUserId());
				act.setForword(discountInput);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setOutData("message", e.getMessage());
			act.setForword(discountInput);
			act.setException(e);
		}
	}
	
	
	private void insertTmpXsDealer(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCells(key, cells, userId);
			}
		}
	}
	
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws BizException  {
		TypeSetDao dao = TypeSetDao.getInstance();
		TtDealerPreFinPO finPO = new TtDealerPreFinPO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String month = lastNull(1,cells,"").toString().trim();
		try {
			finPO.setMonth(dateFormat.parse(month));
		} catch (ParseException e) {
			throw new BizException("日期格式必须为2000-01-01");
		}
		
		if("00".equals(month.substring(0, 2))){
			throw new BizException("请自定义模板返利月份格式为yyyy-mm-dd");
		}
		if(month.length()!=10){
			throw new BizException("请自定义模板返利月份格式为yyyy-mm-dd");
		}
		TmDealerPO dealerPO = new TmDealerPO();
		dealerPO.setDealerCode(lastNull(0,cells,"").toString().trim());
		List<TmDealerPO> ListDealerPO = dao.select(dealerPO);
		if(ListDealerPO!=null&&ListDealerPO.size()>0){
			dealerPO = ListDealerPO.get(0);
		}else{
			throw new BizException("经销商代码不存在");
		}
		
		TmFinReturnTypePO returnTypePO = new TmFinReturnTypePO();
		returnTypePO.setFinReturnCode(lastNull(3,cells,"").toString().trim());
		List<TmFinReturnTypePO> TmFinReturnTypePO = dao.select(returnTypePO);
		if(TmFinReturnTypePO!=null&&TmFinReturnTypePO.size()>0){
			returnTypePO = TmFinReturnTypePO.get(0);
		}else{
			throw new BizException("返利类型代码不存在");
		}
		
		//一个经销商该月同种类型 已经审核通过提交了就不能在导入
		if(dao.isExists(returnTypePO.getFinReturnType().toString(),dealerPO.getDealerId().toString(), lastNull(1,cells,"").toString().trim())){
			throw new BizException(dealerPO.getDealerName()+":"+lastNull(1,cells,"").toString().trim().substring(0, 7)+":"+lastNull(3,cells,"").toString().trim()+"已录入");
		}else{
			//如果只是保存，驳回和保存中则删除旧数据
			if(dao.isExistsByNoAudit(returnTypePO.getFinReturnType().toString(),dealerPO.getDealerId().toString(), lastNull(1,cells,"").toString().trim())){
				dao.delete("delete Tt_Dealer_Pre_Fin where dealer_id = "+dealerPO.getDealerId()+" and month=to_date('"+lastNull(1,cells,"").toString().trim()+"','yyyy-mm-dd') and FIN_RETURN_TYPE="+returnTypePO.getFinReturnType()+"",null);
			}
		}
		
		java.util.regex.Pattern pattern = Pattern.compile("[1-9]*(\\.?)[0-9]*");
		if(pattern.matcher(lastNull(2,cells,"").toString().trim()).matches()){
			finPO.setAmount(Double.parseDouble(lastNull(2,cells,"").toString().trim()));
		}else{
			throw new BizException("预折扣金额必须是数字至多2位小数");
		}
		
		finPO.setCreateBy(logonUser.getUserId());
		finPO.setCreateDate(new Date());
		finPO.setDealerId(dealerPO.getDealerId());
		finPO.setRemark(lastNull(4,cells,"").toString().trim());
		finPO.setPreId(new Long(SequenceManager.getSequence("")));
		finPO.setFinReturnType(new Long(returnTypePO.getFinReturnType()));
		finPO.setAuditStatus(Constant.FIN_INPUT_AUDIT_STATUS_01);
		
        dao.insert(finPO);	
        
		TtDealerPreFinRecordPO recordPO = new TtDealerPreFinRecordPO();
		recordPO.setAuditBy(logonUser.getUserId());
		recordPO.setAuditDate(new Date());
		recordPO.setPreId(finPO.getPreId());
		recordPO.setAuditStatus(Long.parseLong(finPO.getAuditStatus().toString()));
		recordPO.setRecordId(new Long(SequenceManager.getSequence("")));
		
		dao.insert(recordPO);
}
	private Object lastNull(int i,Cell[] cells,Object obj){
	       return cells.length>i?cells[i].getContents().trim().replace("\n", ""):obj;
	}
}