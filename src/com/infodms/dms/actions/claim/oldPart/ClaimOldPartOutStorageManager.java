package com.infodms.dms.actions.claim.oldPart;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOldPartOutPreListBean;
import com.infodms.dms.bean.SpefeeBaseBean;
import com.infodms.dms.bean.TtAsWrMainPartClaimBean;
import com.infodms.dms.bean.TtAsWrOldOutDetailBean;
import com.infodms.dms.bean.TtAsWrOldOutDoorBean;
import com.infodms.dms.bean.TtAsWrOldOutNoticeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.dealerClaimMng.DealerClaimReportDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartOutStorageDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtAsRangeModDetailPO;
import com.infodms.dms.po.TtAsSecondInStoreDetailPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrModelPricePO;
import com.infodms.dms.po.TtAsWrOldOutDetailPO;
import com.infodms.dms.po.TtAsWrOldOutDoorDetailPO;
import com.infodms.dms.po.TtAsWrOldOutDoorPO;
import com.infodms.dms.po.TtAsWrOldOutNoticeDetailPO;
import com.infodms.dms.po.TtAsWrOldOutNoticePO;
import com.infodms.dms.po.TtAsWrOldOutPartPO;
import com.infodms.dms.po.TtAsWrOldPartLabourPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailExtendPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrRangeSinglePO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.po.TtDeliveryOrderPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartMakerProblemPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.service.OutStoreService;
import com.infodms.yxdms.service.impl.OutStoreServiceImpl;
import com.infodms.yxdms.utils.RemindDateUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--索赔旧件出库查询
 * 作者：  赵伦达
 */
public class ClaimOldPartOutStorageManager extends BaseAction{
	private AclUserBean logonUserBean = null;
	private ClaimOldPartOutStorageDao dao=new ClaimOldPartOutStorageDao();
	private int pageSize=Constant.PAGE_SIZE;//页面显示行数
	private final String MAKER_PROBLEM = sendUrl(this.getClass(), "makerProblem");// 
	private final String MAKER_PROBLEM_UPDATE = sendUrl(this.getClass(), "makerProblemUpdate");// 
	private final String MAKER_PROBLEM_DETAIL = sendUrl(this.getClass(), "makerProblemDetail");//
	private final String return_show = sendUrl(this.getClass(), "return_show");//
	private final String  LogInfoByclaimNo = sendUrl(this.getClass(), "LogInfoByclaimNo");//
	private final String Association_Line_add = "/jsp_new/claim/Association_Line_add.jsp";
	private final String OLD_PART_INT = "/jsp/claim/applicationClaim/oldPartList.jsp";// 旧件库存查询跳转
	/**
	 * Function：索赔旧件出库查询--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public void queryListPage(){
		try {
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
//			RemindDateUtils dateUtils = new RemindDateUtils();
//			Date StartTime = dateUtils.getCurrentMonthStartTime();
//			Date EndTime = dateUtils.getCurrentMonthEndTime();
//			act.setOutData("startTime", BaseUtils.printDate("yyyy-MM-dd", StartTime));
//			act.setOutData("endTime", BaseUtils.printDate("yyyy-MM-dd", EndTime));
			this.newdate();
			act.setForword(sendUrl(this.getClass(), "queryOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//线下单号新增
	@SuppressWarnings("unchecked")
	public void AssociationLineAdd() {
		String type = request.getParamValue("type");
		String range_no = request.getParamValue("rang_no");
		if ("insert".equals(type)) {//新增
			String lineNum = request.getParamValue("lineNum");
			dao=ClaimOldPartOutStorageDao.getInstance();
			TtAsWrOldOutPartPO t1 = new TtAsWrOldOutPartPO();
			TtAsWrOldOutPartPO t2 = new TtAsWrOldOutPartPO();
			t2.setLineNum(lineNum);
			t1.setRangeNo(range_no);
			int res = dao.update(t1, t2);
			act.setOutData("succ", res);
		}else {//跳转
			act.setOutData("rang_no", range_no);
			act.setForword(Association_Line_add);
		}
	}
	public void newdate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 19);//把日期设置为当月第一天
		String monthend=sdf.format(c.getTime());
		c.add(Calendar.MONTH, -1);
		c.add(Calendar.DAY_OF_MONTH, 1);
		String monthstart=sdf.format(c.getTime());
		act.setOutData("startTime",monthstart);//上月20号
		act.setOutData("endTime",monthend);//当前月19号
	}
	
	public void ServiceReturn()
	{
		try {
			act.setForword(sendUrl(this.getClass(), "ServiceReturn"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void queryListPage2(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(sendUrl(this.getClass(), "queryOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void rangeFinancialPer(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "queryRangeList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件出库查询--条件查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2013-5-10 15:48
	 * 
	 * 2013-5-10 15:48
	 */
	@SuppressWarnings("unchecked")
	public void queryOutListByCondition(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String,Object>> ps=dao.getOutStorelogList(companyId,request, getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryOutListByConditionSum(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map<String, Object> valueMap  =dao.getOutStorelogListSum(null,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("valueMap", valueMap);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件出库查询--新增出库页面
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public void addPageList(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			act.setOutData("yieldlys", yieldly);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void addNoPageList(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			act.setOutData("yieldlys", yieldly);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList2"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void return_show(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String SUPPLY_CODE = request.getParamValue("SUPPLY_CODE");
			String PART_CODE = request.getParamValue("PART_CODE");
			
			StringBuffer sql= new StringBuffer();
			sql.append(" select a.claim_no,b.down_part_code,b.down_part_name,b.main_part_code from tt_as_wr_application a , tt_as_wr_partsitem b where a.id = b.id\n" );
			sql.append(" and (a.claim_no,b.main_part_code  ) in (\n" );
			sql.append(" SELECT\n" );
			sql.append("              D.CLAIM_NO,\n" );
			sql.append("              d.PART_CODE\n" );
			sql.append("              FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n" );
			sql.append("              TM_DEALER                    DD,\n" );
			sql.append("              TT_AS_WR_OLD_RETURNED        R\n" );
			sql.append("        WHERE 1 = 1\n" );
			sql.append("          AND R.ID = D.RETURN_ID --and r.return_type=10731002\n" );
			sql.append("          AND d.is_main_code = 94001001\n" );
			sql.append("          AND R.DEALER_ID = DD.DEALER_ID\n" );
			sql.append("  AND (D.qhj_flag = 0 or (D.qhj_flag = 1 and d.KCDB_FLAG=2))\n" );
			sql.append("          and r.yieldly = 95411001\n" );
			sql.append("          and (r.status = 10811005 or\n" );
			sql.append("              (r.status = 10811004 and d.is_in_house = 10041001))\n" );
			sql.append("          AND D.SIGN_AMOUNT > 0\n" );
			sql.append("          AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL or D.IS_CLIAM = 2)\n" );
			sql.append("and d.producer_code like '%"+SUPPLY_CODE+"%'\n" );
			sql.append("and d.PART_CODE  = '"+PART_CODE+"'\n" );
			sql.append("          AND D.IS_OUT in (0,2)\n" );
			sql.append("        GROUP BY D.CLAIM_NO,\n" );
			sql.append("                 d.PART_CODE,\n" );
			sql.append("                 d.PART_NAME,\n" );
			sql.append("                 d.part_id,\n" );
			sql.append("                 d.producer_code,\n" );
			sql.append("                 d.producer_name\n" );
			sql.append("       UNION\n" );
			sql.append("            select\n" );
			sql.append("              D.CLAIM_NO,\n" );
			sql.append("              d.PART_CODE\n" );
			sql.append("         FROM TT_AS_WR_OLD_RETURNED_DETAIL D,\n" );
			sql.append("              TM_DEALER                    DD,\n" );
			sql.append("              TT_AS_WR_OLD_RETURNED        R\n" );
			sql.append("        WHERE 1 = 1\n" );
			sql.append("          AND R.ID = D.RETURN_ID --and r.return_type=10731002\n" );
			sql.append("          AND d.is_main_code = 94001001\n" );
			sql.append("          AND R.DEALER_ID = DD.DEALER_ID\n" );
			sql.append("  AND (d.qhj_flag=1 and d.kcdb_flag<>2)\n" );
			sql.append("          and r.yieldly = 95411001\n" );
			sql.append("          and (r.status = 10811005 or\n" );
			sql.append("              (r.status = 10811004 and d.is_in_house = 10041001))\n" );
			sql.append("          AND D.SIGN_AMOUNT > 0\n" );
			sql.append("          AND (D.IS_CLIAM = 0 OR D.IS_CLIAM IS NULL)\n" );
			sql.append("and d.producer_code like '%"+SUPPLY_CODE+"%'\n" );
			sql.append("and d.PART_CODE  = '"+PART_CODE+"'\n" );
			sql.append("          AND D.IS_OUT = 0\n" );
			sql.append("        GROUP BY D.CLAIM_NO,\n" );
			sql.append("                 d.PART_CODE,\n" );
			sql.append("                 d.PART_NAME,\n" );
			sql.append("                 d.part_id,\n" );
			sql.append("                 d.producer_code,\n" );
			sql.append("                 d.producer_name\n" );
			sql.append("       UNION ALL\n" );
			sql.append("       SELECT\n" );
			sql.append("              A.CLAIM_NO,\n" );
			sql.append("              p.part_code\n" );
			sql.append("         FROM TT_AS_WR_PARTSITEM   P,\n" );
			sql.append("              TM_PT_PART_BASE      B,\n" );
			sql.append("              TT_AS_WR_APPLICATION A,\n" );
			sql.append("              tm_dealer            d\n" );
			sql.append("        WHERE P.PART_CODE = B.PART_CODE(+)\n" );
			sql.append("          AND A.ID = P.ID\n" );
			sql.append("          AND a.dealer_id = d.dealer_id\n" );
			sql.append("          AND ((B.IS_RETURN = 95361002 AND B.IS_CLIAM = 95321001 AND\n" );
			sql.append("              P.PART_USE_TYPE = 1) OR\n" );
			sql.append("              (P.PART_USE_TYPE = 0 and B.IS_CLIAM = 95321001 AND\n" );
			sql.append("              p.quantity = 0))\n" );
			sql.append("          AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n" );
			sql.append("          AND P.IS_NOTICE = 10041002\n" );
			sql.append("          AND (A.URGENT = 0 or A.URGENT = 2)\n" );
			sql.append("and P.DOWN_PRODUCT_CODE like '%"+SUPPLY_CODE+"%'\n" );
			sql.append(" and p.DOWN_PRODUCT_CODE  = '"+PART_CODE+"'\n" );
			sql.append("          AND A.STATUS NOT IN (10791001, 10791003, 10791006, 10791005)\n" );
			sql.append("          AND P.RESPONSIBILITY_TYPE = 94001001\n" );
			sql.append("        GROUP BY A.CLAIM_NO,\n" );
			sql.append("                 P.DOWN_PART_CODE,\n" );
			sql.append("                 P.DOWN_PART_NAME,\n" );
			sql.append("                 P.PART_ID,\n" );
			sql.append("                 P.Id,\n" );
			sql.append("                 P.PART_CODE,\n" );
			sql.append("                 P.DOWN_PRODUCT_CODE,\n" );
			sql.append("                 P.DOWN_PRODUCT_NAME\n" );
			sql.append("       having sum(p.quantity) = 0\n" );
			sql.append("       UNION ALL\n" );
			sql.append("       SELECT\n" );
			sql.append("              A.CLAIM_NO,\n" );
			sql.append("              p.part_code\n" );
			sql.append("         FROM TT_AS_WR_PARTSITEM   P,\n" );
			sql.append("              TM_PT_PART_BASE      B,\n" );
			sql.append("              TT_AS_WR_APPLICATION A,\n" );
			sql.append("              tm_dealer            d\n" );
			sql.append("        WHERE P.PART_CODE = B.PART_CODE(+)\n" );
			sql.append("          AND A.ID = P.ID\n" );
			sql.append("          AND a.dealer_id = d.dealer_id\n" );
			sql.append("          AND ((B.IS_RETURN = 95361002 AND B.IS_CLIAM = 95321001 AND\n" );
			sql.append("              P.PART_USE_TYPE = 1) OR\n" );
			sql.append("              (P.PART_USE_TYPE = 0 and B.IS_CLIAM = 95321001 AND\n" );
			sql.append("              p.quantity = 0))\n" );
			sql.append("          AND P.DOWN_PART_CODE NOT IN ('00-000', 'NO_PARTS', '00-0000')\n" );
			sql.append("          AND P.IS_NOTICE = 10041002\n" );
			sql.append("          AND (A.URGENT = 0 or A.URGENT = 2)\n" );
			sql.append("          AND A.STATUS NOT IN (10791001, 10791003, 10791006, 10791005)\n" );
			sql.append("and P.DOWN_PRODUCT_CODE like '%"+SUPPLY_CODE+"%'\n" );
			sql.append(" and p.DOWN_PRODUCT_CODE  = '"+PART_CODE+"'\n" );
			sql.append("          AND P.RESPONSIBILITY_TYPE = 94001001\n" );
			sql.append("        GROUP BY A.CLAIM_NO,\n" );
			sql.append("                 P.DOWN_PART_CODE,\n" );
			sql.append("                 P.DOWN_PART_NAME,\n" );
			sql.append("                 P.PART_ID,\n" );
			sql.append("                  P.Id,\n" );
			sql.append("                 P.PART_CODE,\n" );
			sql.append("                 P.DOWN_PRODUCT_CODE,\n" );
			sql.append("                 P.DOWN_PRODUCT_NAME\n" );
			sql.append("       having sum(p.quantity) > 0)");
			
	       List<Map<String,String>> list = 	dao.pageQuery01(sql.toString(), null, dao.getFunName());
		   act.setOutData("list", list);
		   act.setForword(return_show);
		   
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--查询出库信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function：索赔旧件出库查询--查询出库信息
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-17
	 */
	public void queryPreOutStoreList(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<ClaimOldPartOutPreListBean> ps=dao.getPreOutStoreList(request, getCurrPage());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--查询出库信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//无旧件查询
	@SuppressWarnings("unchecked")
	public void queryNoPartStoreList(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			Map params=new HashMap<String,String>();
			String supply_name=request.getParamValue("supply_name");
			String supply_code=request.getParamValue("supply_code");
			String part_code=request.getParamValue("part_code");
			String part_name=request.getParamValue("part_name");
			String yieldly = request.getParamValue("yieldly");
			params.put("company_id",companyId);
			params.put("supply_name", supply_name);
			params.put("supply_code", supply_code);
			params.put("part_code", part_code);
			params.put("part_name", part_name);
			params.put("yieldly", yieldly);
			
			PageResult<Map<String,Object>> ps=dao.getNoPartStoreList(params, getCurrPage(), Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("ps", ps);
			//act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--查询出库信息");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件出库查询--出库操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2013-5-10 15:48
	 */
	public void outOfStore(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			boolean flag = true;
			if(flag){
				dao.saveOutOfStoreLogOper(request,logonUserBean);	
				act.setOutData("yieldly",request.getParamValue("yieldly"));
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFaile");
				act.setOutData("yieldly",request.getParamValue("yieldly"));
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void outOfStoreCheck(){
		try {
		
		
		logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		dao=ClaimOldPartOutStorageDao.getInstance();
		String idStr=request.getParamValue("idStr");
		String[] arr=idStr.split(",");
		String msg="";
		String venderCode = request.getParamValue("supplyCode"+arr[0]);//供应商代码
		String flag = "";
		String noType = request.getParamValue("noType");
		for(int i=1;i<arr.length-1;i++){
			if(!venderCode.equalsIgnoreCase(request.getParamValue("supplyCode"+arr[i]))){
				msg="一次只能对一家供应商出库!";
			}
		}
		String isHs="";
		HashSet<String> set  = new HashSet<String>();
		for(int i=0;i<arr.length;i++){
			String partCode = request.getParamValue("partCode"+arr[i]);
			Map map = dao.doCheckPartSupplyRelProblems(partCode,venderCode);
			if(null != map && map.size()>0){
				if(!map.get("flag").toString().equals("2"))
					msg+="配件代码【"+partCode+"】与应该供应商【"+venderCode+"】不存在关系!请确认供应商!\n";
				else{
					//非强制关系 可以出库
					//msg+="配件代码【"+partCode+"】与应该供应商【"+venderCode+"】不存在关系!请确认供应商!\n";
				}
			}
			/*String partCode2 = partCode.substring(0, partCode.length()-3)+"000";
			String partCode3 = partCode.substring(0, partCode.length()-3)+"B00";
			String partCode4 = partCode.substring(0, partCode.length()-3)+"B0Y";
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM tt_part_maker_relation r\n");
			sql.append("WHERE r.part_id  in (SELECT d.part_id FROM tt_part_define d WHERE d.part_oldcode in ('"+partCode+"','"+partCode2+"','"+partCode3+"','"+partCode4+"'))\n");
			sql.append("AND r.maker_id = (SELECT md.maker_id  FROM tt_part_maker_define md WHERE md.maker_code='"+venderCode+"')"); 
			List<Map<String,Object>> lit = dao.pageQuery(sql.toString(), null, dao.getFunName());
			if(lit==null ||lit.size()<1){
				msg+="配件代码【"+partCode+"】与应该供应商【"+venderCode+"】不存在关系!请确认供应商!\n";
			}*/
			
			String claimNo = request.getParamValue("claimNo"+arr[i]);
			String sql2 = "SELECT * FROM Tt_As_Wr_Old_Returned_Detail d WHERE d.claim_no='"+claimNo+"' AND d.is_in_house="+Constant.IF_TYPE_NO+"";
			List<Map<String,Object>> list = dao.pageQuery(sql2.toString(), null, dao.getFunName());
			if(list!=null&&list.size()>0){
				msg+="索赔单【"+claimNo+"】存在未入库的旧件,请先进行入库!\n";
			}
			if("1".equalsIgnoreCase(noType)){
				if(claimNo.indexOf("-F")>0){
					set.add("HSF");
					isHs="HSF";
				}else{
					set.add(claimNo.substring(0, 2));
					isHs=claimNo.substring(0, 2);
				}
			}
		}
		if(set.size()>1){
			msg+="分单入库只能选择同一类型的单据!\n";
			act.setOutData("isHs", "");
		}else{
			act.setOutData("isHs",isHs);
		}
			act.setOutData("yieldly",request.getParamValue("yieldly"));
			act.setOutData("msg",msg);
			act.setOutData("idStr",idStr);
			act.setOutData("flag",flag);
	} catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库操作");
		logger.error(logonUserBean, e1);
		act.setException(e1);
	}
	}
	/**
	 * Function：索赔旧件出库查询--全部出库操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-18
	 */
	public void allStore(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String idStr=request.getParamValue("idStr");
			int updateNum=0;
			dao=ClaimOldPartOutStorageDao.getInstance();
			updateNum=dao.saveAllOutOfStoreLogOper(request,logonUserBean);			
			if(updateNum==idStr.split(",").length){
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void queryBatchStock(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "queryBatchStockList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	//查询不索赔出库和二次抵扣出库的出库单
	@SuppressWarnings("unchecked")
	public void queryBatchStockList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String yieldly = request.getParamValue("yieldly");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			params.put("company_id",companyId);
			params.put("Stock_type", request.getParamValue("Stock_type"));
			params.put("out_start_date",  request.getParamValue("out_start_date"));
			params.put("out_end_date",  request.getParamValue("out_end_date"));
			params.put("stock_no",  request.getParamValue("stock_no"));
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			
			PageResult<TtDeliveryOrderPO> ps=dao.queryBatchStockList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	//新增索赔或者二次抵扣扫描出库页面
	public void addStockList(){
		try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockType = request.getParamValue("Stock_type");
			
			//生成出库单
			DealerClaimReportDao dao1=new DealerClaimReportDao();
		String stockNo=	dao1.GenerateStockNo(yieldly);
		
		act.setOutData("stockNo", stockNo);
		
			act.setOutData("yieldly", yieldly);
			
			
			if(stockType.equals(Constant.Stock_type_1.toString())){
				act.setForword(sendUrl(this.getClass(), "queryAddStoreList"));
			}
			else if(stockType.equals(Constant.Stock_type_2.toString())){
				act.setForword(sendUrl(this.getClass(), "queryAddOffsetStore"));
				
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	
	//新增扫描出库查询
	@SuppressWarnings("unchecked")
	public void addStockListQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String yieldly = request.getParamValue("yieldly");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			params.put("company_id",companyId);
			
			params.put("out_start_date",  request.getParamValue("out_start_date"));
			params.put("out_end_date",  request.getParamValue("out_end_date"));
			params.put("stock_no",  request.getParamValue("stock_no"));
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			
			PageResult<TmAsWrBarcodePartStockPO> ps=dao.addStockListQuery(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryAddStoreList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	//不索赔的批量入库
	public void notCliamStockInfo(){
try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockNo = request.getParamValue("stock_no");
			String out_start_date = request.getParamValue("out_start_date");
			String out_end_date = request.getParamValue("out_end_date");
			ClaimBackListDao dao1=new ClaimBackListDao();
			//判断是否有需要出库的数据（如果没有就直接跳转）
			Long count =dao1.isStock(out_start_date, out_end_date);
			
			
			//不索赔的库存出库
			if(count>0){
			dao1.notCliamStockInfo(stockNo,out_start_date,out_end_date,logonUserBean.getUserId());
			}
		
			act.setOutData("yieldly", yieldly);
			act.setForword(sendUrl(this.getClass(), "queryBatchStockList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	////新增二次抵扣出库查询
	@SuppressWarnings("unchecked")
	public void addOffsetStockListQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String yieldly = request.getParamValue("yieldly");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			params.put("company_id",companyId);
			
			params.put("out_start_date",  request.getParamValue("out_start_date"));
			params.put("out_end_date",  request.getParamValue("out_end_date"));
			params.put("stock_no",  request.getParamValue("stock_no"));
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			
			PageResult<TmAsWrBarcodePartStockPO> ps=dao.addOffsetStockListQuery(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryAddOffsetStore"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	//二次抵扣批量入库
	public void offsetStockInfo(){
try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockNo = request.getParamValue("stock_no");
			String out_start_date = request.getParamValue("out_start_date");
			String out_end_date = request.getParamValue("out_end_date");
			ClaimBackListDao dao1=new ClaimBackListDao();
			//判断是否有需要出库的数据（如果没有就直接跳转）
			Long count =dao1.isOffsetStock(out_start_date, out_end_date);
			
			
			//不索赔的库存出库
			if(count>0){
			dao1.offsetStockInfo(stockNo,out_start_date,out_end_date,logonUserBean.getUserId());
			}
		
			act.setOutData("yieldly", yieldly);
			act.setForword(sendUrl(this.getClass(), "queryBatchStockList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//查询不索赔出库单明细
	public void queryClaimDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setForword(sendUrl(this.getClass(), "queryClaimDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//查询不索赔出库单明细
	public void DYClaimDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			String stockId = request.getParamValue("stockId");
			ClaimBackListDao dao1=new ClaimBackListDao();
			yieldly=yieldly.substring(0,8);
			String jw=dao1.queryJW();
			List<Map<String,Object>> ls2=dao1.queryJD(yieldly);
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("ls2", ls2);
			act.setOutData("jw", jw);
			act.setForword(sendUrl(this.getClass(), "DYClaimDetailUrl"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//扫描出库查询
	public void queryScanningStock(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "queryScanningStock"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	

	
	@SuppressWarnings("unchecked")
	public void queryScanningStockList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String yieldly = request.getParamValue("yieldly");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			params.put("company_id",companyId);
			params.put("SUPPLIER_NAME", request.getParamValue("SUPPLIER_NAME"));
			params.put("SUPPLIER_CODE", request.getParamValue("SUPPLIER_CODE"));
			params.put("out_start_date",  request.getParamValue("out_start_date"));
			params.put("out_end_date",  request.getParamValue("out_end_date"));
			params.put("stock_no",  request.getParamValue("stock_no"));
			params.put("is_stock",  request.getParamValue("is_stock"));
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			
			PageResult<TtDeliveryOrderPO> ps=dao.queryScanningStockList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryScanningStock"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}	
	//查询扫描出库明细
	public void queryScanningDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setForword(sendUrl(this.getClass(), "queryScanningDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//补录
	public void queryScanningBulu(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "queryScanningBulu"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//补录
	public void queryScanningBulu1(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			String erpdCode = request.getParamValue("erpdCode");
			String partCode = request.getParamValue("partCode");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail111(stockId,erpdCode,partCode);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "queryScanningBulu"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//补录明细
	public void queryScanningBuluDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			String stockId = request.getParamValue("stockId");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.querybulumDetail1(stockId,partCode,partName);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("stockId", stockId);
			act.setOutData("ls", ls);
		
			act.setForword(sendUrl(this.getClass(), "queryScanningBuluDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//补录明细
	public void queryScanningBuluDetail1(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			String barcodeNo = request.getParamValue("barcodeNo");
			String partCode = request.getParamValue("partCode");
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.querybulumDetail11(stockId,partCode,barcodeNo);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("stockId", stockId);
			act.setOutData("ls", ls);
		
			act.setForword(sendUrl(this.getClass(), "queryScanningBuluDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//补录删除
	public void deleteBulu(){
		try {

			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			ClaimBackListDao dao1=new ClaimBackListDao();
			String id = request.getParamValue("id");
			String[] ids=id.split(",");
			
			for(int i=0;i<ids.length;i++){
			//删除
			dao1.deleteBulu(ids[i]);
			}
			
			String stockId = request.getParamValue("stockId");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
			
			
			List<Map<String,Object>> ls =dao1.querybulumDetail1(stockId,partCode,partName);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "queryScanningBuluDetail"));
			
			
		}
		 catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
				logger.error(logonUserBean, e1);
				act.setException(e1);
			}
		}
	
	
	public void addBulu(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String stockId = request.getParamValue("stockId");
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "addBulu"));
		}
		 catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
				logger.error(logonUserBean, e1);
				act.setException(e1);
			}
	}
	
	/**
	 * 
	 * @Title: selectMainPartCodeForward
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectMainPartCodeForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID");
			act.setOutData("roNo", roNo);
			act.setOutData("vin", vin);
			act.setOutData("GROUP_ID", groupId);
			act.setForword(sendUrl(this.getClass(), "showPartCode"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	public void queryPartCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			Integer curPage = getCurrPage();
			Integer pageSize = 10;
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID"); // 车型
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
			String supplierName = request.getParamValue("SUPPLIER_NAME");
			String ERPD_CODE = request.getParamValue("ERPD_CODE");
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("partCode", partCode);
			
			map.put("partName", partName);
			map.put("groupId", groupId);
			map.put("companyId", companyId.toString());
			map.put("supplierName", supplierName);
			map.put("erpdCode", ERPD_CODE);
			 ClaimBillMaintainDAO dao2 = new ClaimBillMaintainDAO();
			PageResult<TmPtPartBaseExtPO> ps = dao2.queryPartCode2(logonUser,
					map, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void addBuluInfo(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			yieldly=yieldly.substring(0,8);
			System.out.println("yieldly:+"+yieldly);
			String stockId = request.getParamValue("stockId");
			
			String partCode=request.getParamValue("part_code1");
			String partName=request.getParamValue("part_name");
			
			String num=request.getParamValue("num");
			ClaimBackListDao dao1=new ClaimBackListDao();
			dao1.addBuluInfo(stockId, partCode, partName, num, logonUserBean.getUserId(),yieldly);
			
			
			
	
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "queryScanningBulu"));
		}
		 catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
				logger.error(logonUserBean, e1);
				act.setException(e1);
			}
	}
	
	//确定这张出库单
	public void addBuluQueDing(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String stockId = request.getParamValue("stockIds");
		
			String SUPPLIER_ID = request.getParamValue("SUPPLIER_ID");
			String remark = request.getParamValue("remark");
			
			String SUPPLIER_CODE = request.getParamValue("SUPPLIER_CODE");
			ClaimBackListDao dao1=new ClaimBackListDao();
			dao1.addBuluQueDing(stockId, SUPPLIER_ID, SUPPLIER_CODE, logonUserBean.getUserId(),remark);
			
			act.setForword(sendUrl(this.getClass(), "queryScanningStock"));
			
			
			
			
		}
		 catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
				logger.error(logonUserBean, e1);
				act.setException(e1);
			}
		
	}
	
	//明细
	public void ScanningDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail(stockId);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "ScanningDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//明细
	public void notScanningDetail(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.notScanningDetail(stockId);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("stockId", stockId);
			act.setForword(sendUrl(this.getClass(), "notScanningDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void ScanningDetails(){
			try {
		
			
			
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
			
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.querybulumDetail1(stockId,partCode,partName);
			
			
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
		
			act.setForword(sendUrl(this.getClass(), "ScanningDetails"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void ScanningDelete(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String stockId = request.getParamValue("stockId");
			ClaimBackListDao dao1=new ClaimBackListDao();
			dao1.ScanningDelete(stockId);
			act.setForword(sendUrl(this.getClass(), "queryScanningStock"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void ScanningPrint(){
		try {
				
					
					
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			
			String stockId = request.getParamValue("stockId");
			
		
			ClaimBackListDao dao1=new ClaimBackListDao();
			
			List<Map<String,Object>> ls =dao1.queryClaimDetail11(stockId);
			
			List<Map<String,Object>> ls1 =dao1.queryClaimDetail1(stockId);
			
			yieldly=yieldly.substring(0,8);
			
			String jw=dao1.queryJW();
			
			List<Map<String,Object>> ls2=dao1.queryJD(yieldly);
		
			act.setOutData("yieldly", yieldly);
			act.setOutData("ls", ls);
			act.setOutData("ls1", ls1);
			act.setOutData("jw", jw);
			act.setOutData("ls2", ls2);
			act.setForword(sendUrl(this.getClass(), "ScanningPrint"));
				} catch (Exception e) {
					BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--新增出库页面");
					logger.error(logonUserBean, e1);
					act.setException(e1);
				}
			}


	//配件是否索赔维护
	public void queryPartBase(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "queryPartBase"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void queryPartBaseList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String yieldly = request.getParamValue("yieldly");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			params.put("company_id",companyId);
		
			params.put("part_name",  request.getParamValue("part_name"));
			params.put("part_code",  request.getParamValue("part_code"));
			params.put("erpd_code",  request.getParamValue("erpd_code"));
			params.put("isCliam",  request.getParamValue("isCliam"));
			params.put("yieldly", yieldly);
			params.put("yieldlys", yieldlys);
			
			ClaimBackListDao dao1=new ClaimBackListDao();
			PageResult<TmPtPartBasePO> ps=dao1.queryPartBaseList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryPartBase"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}	
	
	public void queryPartBaseUpdate(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partId=request.getParamValue("partId");
			String isCliam=request.getParamValue("isCliam");
			System.out.println("isCliam:"+isCliam);
			ClaimBackListDao dao1=new ClaimBackListDao();
			dao1.queryPartBaseUpdate(partId,isCliam);
			act.setForword(sendUrl(this.getClass(), "queryPartBase"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--全部出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}	
	
	public void showDetailParts(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String part_code=request.getParamValue("partCode");
			String id = request.getParamValue("partId");
			act.setOutData("ID", id);
			act.setOutData("partCode", part_code);
			act.setForword(sendUrl(this.getClass(), "barCodeDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--条码选择");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void showDetailPartsQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			Map params=new HashMap<String,String>();
			String id = request.getParamValue("partId");
			String part_code=request.getParamValue("partCode");
			String supplyCode = request.getParamValue("supplyCode");
			String supply_name=request.getParamValue("supply_name");
			String claimNo = request.getParamValue("CLAIM_NO");
			String model_name=request.getParamValue("model_name");
			System.out.println(model_name);
			params.put("company_id",companyId);
			params.put("supply_name", supply_name);
			params.put("claimNo", claimNo);
			params.put("model_name", model_name);
			
			params.put("part_code", part_code);
			params.put("supplyCode", supplyCode);
			
			System.out.println(part_code+",,,,,,,,"+id+"----"+supplyCode);
			act.setOutData("ID", id);
			List<ClaimOldPartOutPreListBean> detailList=dao.getOldPartBarnoList(params);
			request.setAttribute("detailList", detailList);
		//	act.setOutData("detailList", detailList);
			act.setForword(sendUrl(this.getClass(), "barCodeDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--条码选择");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void showDetailPartsQuery2(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			Map params=new HashMap<String,String>();
			String id = request.getParamValue("partId");
			String part_code=request.getParamValue("partCode");
			String supplyCode = request.getParamValue("supplyCode");
			String model_name=request.getParamValue("model_name");
			String claimNo = request.getParamValue("claim_no");
			String dealer_code = request.getParamValue("dealer_code");
			System.out.println(model_name);
			params.put("model_name",model_name);
			params.put("company_id",companyId);
			params.put("dealer_code",dealer_code);
			params.put("part_code", part_code);
			params.put("supplyCode", supplyCode);
			params.put("claimNo", claimNo);
			System.out.println(part_code+",,,,,,,,"+id+"----"+supplyCode);
			act.setOutData("ID", id);
			List<ClaimOldPartOutPreListBean> detailList=dao.getOldPartBarnoList(params);
			request.setAttribute("detailList", detailList);
		//	act.setOutData("detailList", detailList);
			act.setForword(sendUrl(this.getClass(), "barCodeDetail2"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--条码选择");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 出库记录明细查询
	 * @author KFQ
	 */
	public void outDetail(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			
			
			String outNo = request.getParamValue("out_no");
			String code = request.getParamValue("code");
			List<TtAsWrOldOutDoorBean> dp = dao.getPrintBaseBean(outNo, code);
			List<TtAsWrOldOutDoorDetailPO> list = dao.getPrintList(outNo, code);
			if(dp.size()>0&& dp!=null){
				act.setOutData("baseBean", dp.get(0));
			}else{
				act.setOutData("baseBean", null);
			}
			act.setOutData("listBean", list);
			String yieldly = request.getParamValue("yieldly");
			System.out.println(outNo+",,,,,"+code);
			act.setOutData("listBean", list);
			act.setOutData("yieldly", yieldly);
			act.setForword(sendUrl(this.getClass(), "oldPartOutDetailInfo"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	public void saveOutDoor(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String yieldly = request.getParamValue("yieldly");
//			String outNo = request.getParamValue("outNo");
//			String supplayName = request.getParamValue("supplayName");
//			String code = request.getParamValue("code");
//			String tel = request.getParamValue("tel");
//			String outPartType = request.getParamValue("outPartType");
//			String[] partName = request.getParamValues("partName"); // 
//			String[] partCode = request.getParamValues("partCode"); // 
//			String[] outAmount = request.getParamValues("outAmount");
//			String[] model = request.getParamValues("model");
//			String[] remark = request.getParamValues("remark");
//			TtAsWrOldOutDoorPO dp = new TtAsWrOldOutDoorPO();
//			dp.setDoorId(Utility.getLong(SequenceManager.getSequence("")));
//			dp.setCreateBy(logonUserBean.getUserId());
//			dp.setCreateDate(new Date());
//			dp.setOutByName("江西昌河汽车有限责任公司技术服务处");
//			dp.setOutCompany(supplayName);
//			dp.setOutCompanyCode(code);
//			dp.setOutCompanyTel(tel);
//			dp.setOutNo(outNo);
//			dp.setOutPartType(Integer.parseInt(outPartType));
//			dp.setYieldly(Integer.parseInt(yieldly));
//			dp.setOutTittle("江西昌河汽车质量保证索赔故障件出门证");
//			dao.insert(dp);
//			for(int i=0;i<partName.length;i++){
//				TtAsWrOldOutDoorDetailPO dpp = new TtAsWrOldOutDoorDetailPO();
//				dpp.setId(Utility.getLong(SequenceManager.getSequence("")));
//				dpp.setCreateBy(logonUserBean.getUserId());
//				dpp.setCreateDate(new Date());
//				dpp.setPartCode(partCode[i]);
//				dpp.setDoorId(dp.getDoorId());
//				dpp.setModelName(model[i]);
//				dpp.setOutNum(Integer.parseInt(outAmount[i]));
//				dpp.setOutRemark(remark[i]);
//				dpp.setPartName(partName[i]);
//				dao.insert(dpp);
//			}
			act.setOutData("yieldly", yieldly);
			act.setOutData("updateResult", "updateSuccess");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
		
	}
	/**
	 * 出库记录明细索赔单明细查询
	 * @author KFQ
	 */
	public void claimDetail(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUserBean);
			String partCode = request.getParamValue("partCode");// 页面传来的 配件代码
			String outNo = request.getParamValue("outNo");//出库单编号
			String code = request.getParamValue("code");//供应商代码
			System.out.println(partCode+",,,,,"+code+"..............."+outNo);
			List<TtAsWrOldOutDetailBean> list = dao.getClaimDetail(outNo, code, oemCompanyId, partCode);
			act.setOutData("listBean", list);
			act.setForword(sendUrl(this.getClass(), "oldCliamDetailInfo"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔出库出门证打印
	 * @author KFQ
	 */
	
	@SuppressWarnings("unchecked")
	public void detailPrint(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String code = request.getParamValue("code");
			List<TtAsWrOldOutDoorBean> dp = dao.getPrintBaseBean(outNo, code);
			List<TtAsWrOldOutDoorDetailPO> list = dao.getPrintList(outNo, code);
			if(dp.size()>0&& dp!=null){
				act.setOutData("baseBean", dp.get(0));
			}else{
				act.setOutData("baseBean", null);
			}
			//更新出门证的打印次数和上次打印时间
			String sql = "update tt_as_wr_old_out_door d set d.print_times = d.print_times+1,d.last_print_date=sysdate where d.out_no='"+outNo+"'";
			dao.update(sql, null);
			act.setOutData("listBean", list);
			act.setForword(sendUrl(this.getClass(), "outDetailPrint"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 索赔出库通知单打印
	 * @author KFQ
	 */
	
	@SuppressWarnings("unchecked")
	public void noticePrint(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = request.getParamValue("id");
			
			List<TtAsWrOldOutNoticeBean> dp = dao.getBaseBeanForNoticeOut(id);
			List<TtAsWrOldOutNoticeDetailPO> list = dao.getPrintDetailOut(id,oemCompanyId,yieldlys);
			String sql = "update tt_as_wr_old_out_notice d set d.PRINT_TIMES = d.PRINT_TIMES+1,d.LAST_PRINT_DATE=sysdate where d.notice_id='"+id+"'";
			dao.update(sql, null);
			if(dp.size()>0&& dp!=null){
				act.setOutData("baseBean", dp.get(0));
			}else{
				act.setOutData("baseBean", null);
			}
			act.setOutData("listBean", list);
			act.setForword(sendUrl(this.getClass(), "outNoticePrint"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void outPartNoticeDetail(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = request.getParamValue("id");
			List<TtAsWrOldOutNoticeBean> dp = dao.getBaseBeanForNoticeOut(id);
			List<TtAsWrOldOutNoticeDetailPO> list = dao.getPrintDetailOut(id,oemCompanyId,yieldlys);
			if(dp.size()>0&& dp!=null){
				act.setOutData("baseBean", dp.get(0));
			}else{
				act.setOutData("baseBean", null);
			}
			act.setOutData("listBean", list);
			act.setForword(sendUrl(this.getClass(), "outNoticeDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	

	//通知单生成
	public void outPartNoticeIn(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String code = request.getParamValue("code");
			List<TtAsWrOldOutNoticePO> dp = dao.getBaseBeanForNotice(outNo, code,oemCompanyId);
			List<TtAsWrOldOutDetailBean> list = dao.getPrintDetail(outNo, code,oemCompanyId,"");
			
				act.setOutData("baseBean", dp.get(0));
				act.setOutData("listBean", list);
				act.setOutData("size", list.size());
				act.setOutData("hasDetail", 0);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("outNo", outNo);
			act.setOutData("code", code);
			act.setForword(sendUrl(this.getClass(), "outPartNoticeIn"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//查询通知单明细
	@SuppressWarnings("unchecked")
	public void outPartNoticeInfo(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String code = request.getParamValue("code");//供应商代码
			String outNo = request.getParamValue("out_no");//出库单号
			String yieldly = request.getParamValue("yieldly");//结算基地
			int totalNum = 0;
			int needNum = 0;
			TtAsWrOldOutDoorPO dp = new TtAsWrOldOutDoorPO();
			dp.setOutNo(outNo);
			dp.setOutCompanyCode(code);
			dp = (TtAsWrOldOutDoorPO) dao.select(dp).get(0);
			TtAsWrOldOutDoorDetailPO ddp = new TtAsWrOldOutDoorDetailPO();
			ddp.setDoorId(dp.getDoorId());
			List<TtAsWrOldOutDoorDetailPO> list = dao.select(ddp);
			for(int i=0;i<list.size();i++){
				totalNum+= list.get(i).getOutNum();
				needNum += list.get(i).getRemandNum();
			}
			TtAsWrOldOutNoticePO np = new TtAsWrOldOutNoticePO();
			np.setOutNo(outNo);
			List<TtAsWrOldOutNoticePO> detailList= dao.select(np);
			act.setOutData("detailList", detailList);
			act.setOutData("totalNum", totalNum);
			act.setOutData("needNum", needNum);
			act.setOutData("baseBean", dp);
			act.setOutData("yieldly", yieldly);
			act.setOutData("outNo", outNo);
			act.setOutData("supplayCode", code);
			act.setForword(sendUrl(this.getClass(), "outNoticeInfo"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void checkDoor(){
		
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String code = request.getParamValue("code");
			String outNo = request.getParamValue("out_no");
			String type=request.getParamValue("type");
			String str = "updateSuccess"	;
			TtAsWrOldOutDoorPO dp = new TtAsWrOldOutDoorPO();
			dp.setOutNo(outNo);
			dp.setOutCompanyCode(code);
			List<TtAsWrOldOutDoorPO> list = dao.select(dp);
			if(list!=null && list.size()>0){
				str = "";
			}
			act.setOutData("updateResult", str);
			act.setOutData("outNo", outNo);
			act.setOutData("type", type);
			act.setOutData("supplayCode", code);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 出库记录明细查询
	 * @author KFQ
	 * 2013-7-9 13:51
	 */
	public void outDetail2(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String code = request.getParamValue("code");
			String type = request.getParamValue("type");
			List<Map<String,Object>> list  = dao.getOutDetail2(outNo, code);
			if(list.size()>0){
				act.setOutData("po", list.get(0));
			}
			request.setAttribute("type", type);
			request.setAttribute("outNo", outNo);
			request.setAttribute("code", code);
			act.setForword(sendUrl(this.getClass(), "oldPartOutDetailInfo2"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 旧件出库退赔清单明细查询
	 */
	public void queryOutDetail2(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String,Object>> list  = dao.getOutDetail2(request,getPage(1), getCurrPage());
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 旧件出库退赔清单明细查询
	 */
	public void exportOutDetail2(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String,Object>> list  = dao.getOutDetail2(request,getPage(2), getCurrPage());
			dao.exportOutDetail2(act,list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 生成退赔单跳转
	 */
	public void saveRengePer(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String cType = request.getParamValue("cType");
			System.out.println(outNo+",,,,,");
			List<Map<String,Object>> list = dao.printRenge(outNo);
			if(list!=null && list.size()>0){
				act.setOutData("listBean", list);
			}else{
				act.setOutData("listBean", null);
			}
			act.setOutData("cType", cType);
			request.setAttribute("outNo", outNo);
			act.setForword(sendUrl(this.getClass(), "rangeSingle"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void saveOutNotice(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String type = request.getParamValue("type");
			String yieldly = request.getParamValue("yieldly");
			String outNo = request.getParamValue("outNo");
			String noticeTittle = request.getParamValue("noticeTittle");
			String code = request.getParamValue("code");
			String noticeNo = request.getParamValue("noticeNo");
			String noticeTo = request.getParamValue("noticeTo");
			String noticeTel = request.getParamValue("noticeTel");
			String noticeBy = request.getParamValue("noticeBy");
			String noticeBank = request.getParamValue("noticeBank");
			String noticeAcount = request.getParamValue("noticeAcount");
			String smallTotal2 = request.getParamValue("smallTotal2a");
			String totalPrice2 = request.getParamValue("totalPrice2a");
			String taxPrice2 = request.getParamValue("taxPrice2a");
			String id  = request.getParamValue("id");//特殊费用单Id
			String[] partName=request.getParamValues("partName");
			String[] partCode=request.getParamValues("partCode");
			String[] outAmount=request.getParamValues("outAmount");
			String[] modelName=request.getParamValues("modelName");
			String[] claimPrice=request.getParamValues("claimPrice");
			String[] labourHours=request.getParamValues("labourHours");
			String[] labourPrice=request.getParamValues("labourPrice");
			String[] partPrice=request.getParamValues("partPrice");
			String[] otherPrice=request.getParamValues("otherPrice");
			String[] smallTotal=request.getParamValues("smallTotal");
			String[] taxPrice=request.getParamValues("taxPrice");
			String[] total=request.getParamValues("total");
			String[] outPartType = request.getParamValues("outPartType");
			String[] partCodeIn = request.getParamValues("partCode");
			int noticeType=Constant.OUT_PART_TYPE_01;
			if("1".equalsIgnoreCase(type)){
				noticeNo = dao.GenerateStockNo().split(",")[1];
				yieldly = Constant.PART_IS_CHANGHE_01.toString();
				noticeType=Constant.OUT_PART_TYPE_03;
				TtAsWrSpefeePO fp = new TtAsWrSpefeePO();//更新特殊单数据
				TtAsWrSpefeePO fp2 = new TtAsWrSpefeePO();
				fp.setId(Long.valueOf(id));
				fp2.setIsNotice(Constant.IF_TYPE_YES);
				fp2.setNoticeDate(new Date());
				dao.update(fp, fp2);
			}else if("2".equalsIgnoreCase(type)){
				noticeNo = dao.GenerateStockNo().split(",")[1];
				noticeType=Constant.OUT_PART_TYPE_04;
			}
			boolean flag = true;
			TtAsWrOldOutNoticePO np = new TtAsWrOldOutNoticePO();
			np.setCreateBy(logonUserBean.getUserId());
			np.setCreateDate(new Date());
			np.setNoticeAcount(noticeAcount);
			np.setNoticeBank(noticeBank);
			np.setNoticeCompany(noticeTo);
			np.setNoticeCode(code);
			np.setNoticeCompanyBy(noticeBy);
			np.setNoticeCompanyByTel(noticeTel);
			np.setNoticeId(Utility.getLong(SequenceManager.getSequence("")));
			np.setNoticeNo(noticeNo);
			np.setOutNo(outNo);
			np.setYieldly(Integer.parseInt(yieldly));
			np.setNoticeTittle(noticeTittle);
			np.setSmallTotal(Double.valueOf(smallTotal2));
			np.setTotal(Double.valueOf(totalPrice2));
			np.setTaxTotal(Double.valueOf(taxPrice2));
			np.setSpefeeId(Long.valueOf(id==null?"0":id));
			np.setType(noticeType);
			for(int i=0;i<partName.length;i++){
				if(Integer.parseInt(outAmount[i])==0){
					flag = false;
					continue;
				}else{
				TtAsWrOldOutNoticeDetailPO dp = new TtAsWrOldOutNoticeDetailPO();
				dp.setClaimLabour(Float.valueOf(labourHours[i]));
				dp.setClaimPrice(Float.valueOf(claimPrice[i]));
				dp.setCreateBy(logonUserBean.getUserId());
				dp.setCreateDate(new Date());
				dp.setId(Utility.getLong(SequenceManager.getSequence("")));
				dp.setLabourPrice(Double.valueOf(labourPrice[i]));
				dp.setModelName(modelName[i]);
				dp.setNoticeId(np.getNoticeId());
				dp.setOtherPrice(Double.valueOf(otherPrice[i]));
				dp.setOutNum(Integer.parseInt(outAmount[i]));
				dp.setPartName(partName[i]);
				dp.setPartPrice(Double.valueOf(partPrice[i]));
				dp.setSmallTotal(Double.valueOf(smallTotal[i]));
				dp.setTaxTotal(Double.valueOf(taxPrice[i]));
				dp.setTotal(Double.valueOf(total[i]));
				if(outPartType!=null){
					dp.setOutPartType(Integer.parseInt(outPartType[i]));
				}else {
					dp.setOutPartType(1);
				}
				dp.setPartCode(partCodeIn[i]);
				dao.insert(dp);
				if("1".equalsIgnoreCase(type)){
				}else if("2".equalsIgnoreCase(type)){//开始更新索赔单旧件条码明细表数据
					String sql=" update Tt_As_Wr_Partsitem p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.notice_date=sysdate  where p.down_part_code='"+partCode[i]+"'   and p.down_product_code='"+code+"' ";
					dao.update(sql, null);
				}else {
				String sql ="update TT_AS_WR_OLD_OUT_DOOR_DETAIL d set d.remand_num=d.out_num where d.part_name='"+partName[i].trim()+"'and d.model_name='"+modelName[i].trim()+"'  and d.door_id=(select door_id from TT_AS_WR_OLD_OUT_DOOR where out_no='"+outNo.trim()+"')";
				dao.update(sql, null);
				}
				flag = true;
				}
			}
			if(flag){
				dao.insert(np);
				act.setOutData("updateResult", "updateSuccess");
			}else{
				act.setOutData("updateResult", "noValue");
			}
			act.setOutData("yieldly", yieldly);
			act.setOutData("outNo", outNo);
			act.setOutData("code", code);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
@SuppressWarnings("unchecked")
public void checkNotice(){
		
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String code = request.getParamValue("code");
			String outNo = request.getParamValue("out_no");
			String str = "updateSuccess"	;
			TtAsWrOldOutNoticePO dp = new TtAsWrOldOutNoticePO();
			dp.setOutNo(outNo);
			dp.setNoticeCode(code);
			List<TtAsWrOldOutNoticePO> list = dao.select(dp);
			if(list!=null && list.size()>0){
				str = "";
			}
			act.setOutData("updateResult", str);
			act.setOutData("outNo", outNo);
			act.setOutData("supplayCode", code);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void noPartIsClaim(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(sendUrl(this.getClass(), "noPartClaimPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--无旧件索赔准备");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void noPartIsClaim2(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(sendUrl(this.getClass(), "noPartClaimPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--无旧件索赔准备");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//无旧件索赔查询
	@SuppressWarnings("unchecked")
	public void noPartIsClaimQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
					params.put("supply_name", request.getParamValue("supply_name"));
					params.put("supply_code",  request.getParamValue("SUPPLY_CODE"));
					params.put("NOTICE_NO",  request.getParamValue("NOTICE_NO"));
					params.put("yieldly", request.getParamValue("yieldly"));
					params.put("company_id",companyId);
			PageResult<TtAsWrOldOutNoticeBean> ps=dao.getNoticeInfo2(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("ps", ps);
			act.setForword(sendUrl(this.getClass(), "queryOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	
	public void mainPartClaim(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(sendUrl(this.getClass(), "mainPartClaimPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--无旧件索赔准备");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void mainPartClaim2(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(sendUrl(this.getClass(), "mainPartClaimPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--无旧件索赔准备");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	//特殊费用单二次索赔通知单明细
	@SuppressWarnings("unchecked")
	public void mainPartClaimQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			params.put("supply_name", request.getParamValue("supply_name"));
			params.put("supply_code",  request.getParamValue("SUPPLY_CODE"));
			params.put("NOTICE_NO",  request.getParamValue("NOTICE_NO"));
			params.put("yieldly", request.getParamValue("yieldly"));
			PageResult<TtAsWrOldOutNoticeBean> ps=dao.getNoticeInfo(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void mainPartClaimDetail(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
		
			String supply_code=  request.getParamValue("code");
			String yieldly = request.getParamValue("yieldly");
			act.setOutData("yieldly",yieldly);
			act.setOutData("supply_code",supply_code);
			act.setForword(sendUrl(this.getClass(), "mainPartClaimDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void mainPartClaimDetail2(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String supply_code=  request.getParamValue("supply_code");
			String yieldly = request.getParamValue("yieldly");
			String partName = request.getParamValue("part_name");
			String isPrint = request.getParamValue("IS_PRINT");
			PageResult <ClaimOldPartOutPreListBean> ps=dao.mainPartClaimDetail(supply_code,yieldly,partName,isPrint, curPage, Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("supply_code",supply_code);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void mainPartClaimPrint(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			// 处理当前页
			
			String claimNo=  request.getParamValue("no");
			String code = request.getParamValue("labourCode");
			String yieldly = request.getParamValue("yieldly");
			String str = "昌河";
			List<TtAsWrMainPartClaimBean> bean = dao.getMainPartClaimBean(claimNo,code,yieldly);
			List<TtAsWrMainPartClaimBean> list = dao.getMainPartClaimList(claimNo, code, yieldly);
			String sql = "update tt_as_wr_partsitem p set p.is_old_claim_print="+Constant.IF_TYPE_YES+" where p.wr_labourcode='"+code+"'";
			dao.update(sql, null);
			if(list!=null && list.size()>0){
				act.setOutData("list", list);
			}else{
				act.setOutData("list", null);
			}
			if(Constant.PART_IS_CHANGHE_02.toString().equals(yieldly)){
				str="东安";
			}
			act.setOutData("yieldly", str);
			act.setOutData("bean", bean.get(0));
			act.setForword(sendUrl(this.getClass(), "mainPartClaimPrint"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void querySpefeeList(){
		try {
			
			act.setForword(sendUrl(this.getClass(), "querySpefeeList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void querySpefeeList2(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			String supply_name=  request.getParamValue("supply_name");
			String spefee_no = request.getParamValue("spefee_no");
			String SUPPLY_CODE = request.getParamValue("SUPPLY_CODE");
			String status = request.getParamValue("status");
			PageResult <SpefeeBaseBean> ps=dao.getSpefeeList(supply_name,spefee_no,SUPPLY_CODE,status, curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void addNotice(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id=  request.getParamValue("id");
			SpefeeBaseBean bb = new SpefeeBaseBean();
			List<SpefeeBaseBean> list = dao.getSpefeeBean( id);
			if(list!=null&&list.size()>0){
				bb = list.get(0);
			}
			act.setOutData("bean", bb);
			act.setForword(sendUrl(this.getClass(), "spefeeNoticeAdd"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//通知单删除
	@SuppressWarnings("unchecked")
	public void outPartNoticeDelete(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat sdf = new SimpleDateFormat("MM");
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id=  request.getParamValue("id");
			String type=  request.getParamValue("type");
			String updateResult="updateSuccess";
			String str = "";
			TtAsWrOldOutNoticePO onp = new TtAsWrOldOutNoticePO();
			onp.setNoticeId(Long.valueOf(id));
			onp = (TtAsWrOldOutNoticePO) dao.select(onp).get(0);
			if(Integer.parseInt(sdf.format(onp.getCreateDate()))!=Calendar.getInstance().get(Calendar.MONTH) + 1){
				updateResult = "";
				str = "通知单不能跨月删除!";
			}else{
			TtAsWrOldOutNoticePO np2 = new TtAsWrOldOutNoticePO();
			np2.setNoticeId(Long.valueOf(id));
			TtAsWrOldOutNoticePO np = new TtAsWrOldOutNoticePO();
			np.setNoticeId(Long.valueOf(id));
			np = (TtAsWrOldOutNoticePO) dao.select(np).get(0);
			TtAsWrOldOutNoticeDetailPO  dp = new TtAsWrOldOutNoticeDetailPO();
			dp.setNoticeId(np.getNoticeId());
			List <TtAsWrOldOutNoticeDetailPO> list = dao.select(dp);
			if("0".equalsIgnoreCase(type)){//更新此次开票的旧件数量
				String code = request.getParamValue("code");//供应商代码
				String outNo = request.getParamValue("outNo");//出门证号
				act.setOutData("code", code);
				act.setOutData("outNo", outNo);
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						String sql = "update tt_as_wr_old_out_door_detail d set d.remand_num = 0  where d.part_name='"+list.get(i).getPartName()+"' and d.door_id = (select door_id from TT_AS_WR_OLD_OUT_DOOR where out_no='"+outNo.trim()+"')";
						dao.update(sql, null);
					}
				}
			}else if("1".equalsIgnoreCase(type)){//更新特殊单的开票状态和时间
				String sql = " update tt_as_wr_spefee s set s.is_notice ="+Constant.IF_TYPE_NO+" , s.notice_date =null where s.id ="+np.getSpefeeId();
				dao.update(sql, null);
			}else{
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						String sql=" update Tt_As_Wr_Partsitem p set p.is_notice="+Constant.IF_TYPE_NO+" ,p.notice_date=null  where p.down_part_code='"+list.get(i).getPartCode()+"'   and p.down_product_code='"+onp.getNoticeCode()+"' ";
						dao.update(sql, null);
					}
				}
				
			}
			dao.delete(dp);
			dao.delete(np2);
			}
			act.setOutData("updateResult", updateResult);
			act.setOutData("strInfo", str);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "通知单删除");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 旧件二次入库
	 */
	public void oldPartReInstorePer(){
		try {
			
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setOutData("type", "reIn");
			act.setForword(sendUrl(this.getClass(), "oldPartReInstorePer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 二次入库明细查询初始化
	 */
	public void oldPartReInQuery(){
		try {
			
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setOutData("type", "reIn");
			act.setForword(sendUrl(this.getClass(), "oldPartReInQuery"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "二次入库明细查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void oldPartReInstorePer2(){//东安
		try {
			
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setOutData("type", "reIn");
			act.setForword(sendUrl(this.getClass(), "oldPartReInstorePer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 二次入库明细查询
	 */
	public void oldPartReInQueryDetail(){
		try {
			ClaimBillMaintainDAO dao2 = ClaimBillMaintainDAO.getInstance();
			PageResult <Map<String,Object>> ps=dao2.queryOldPartReinDetail(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询旧件二次入库 zyw 2014-9-5 优化和加查询条件
	 */
	public void oldPartReInstoreQuery(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String yieldly = request.getParamValue("yieldly");
			String type = request.getParamValue("type");
			PageResult <TtAsWrOldReturnedDetailPO> ps=dao.getoldPartList(request,getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("yieldly", yieldly);
			act.setOutData("type", type);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 二次入库 zyw 2014-9-5 
	 */
	public void oldPartReInstoreSave(){
		try {
			int res=1;
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = DaoFactory.getParam(request,"id");
			String isCompensate = DaoFactory.getParam(request, "isCompensate");
			String isMainCode = DaoFactory.getParam(request, "isMainCode");
			
			if("1".equals(isCompensate)){
				if(isMainCode.equals("94001001")){//主因件
					res=dao.updateAmountByMainPartCode(id,1,loginUser);
				}else{//次因件
					res=dao.updateAmountByMainPartCode(id,0,loginUser);
				}
				this.updateOldPo(id);
			}else{
				this.updateOldPo(id,loginUser);
			}
			super.setJsonSuccByres(res);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	private void updateOldPo(String id, AclUserBean loginUser) {
		this.updateOldPo(id);
		dao.insertRecodes(id,loginUser);
	}
	/**
	 * 更新数据 zyw 2014-9-5 
	 * @param id
	 */
	private void updateOldPo(String id) {
		TtAsWrOldReturnedDetailPO dp = new TtAsWrOldReturnedDetailPO();
		TtAsWrOldReturnedDetailPO dp2 = new TtAsWrOldReturnedDetailPO();
		dp.setId(Long.valueOf(id));
		dp2.setSignAmount(1);
		dp2.setDeductRemark(0);
		dp2.setUpdateBy(loginUser.getUserId());
		dp2.setUpdateDate(new Date());
		dao.update(dp, dp2);
	}
	
	/**
	 * 旧件供应商修改
	 */
	public void oldPartReNameQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String is_code = request.getParamValue("is_code");//是否实物,无实物直接由索赔单字表查询数据
			String yieldly = request.getParamValue("yieldly");
			String type = request.getParamValue("type");
			PageResult<ClaimOldPartOutPreListBean> ps  = null;
			if(Constant.IF_TYPE_YES.toString().equalsIgnoreCase(is_code)){
//				ps = dao.getoldPartList2Indate(yieldly,supply_name,supply_code,partCode,partName,claim_no, type,dealerCode,part_type,indate_s,indate_e,curPage, Integer.parseInt(page_amount));
				ps = dao.getReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			}else{
//				ps = dao.getPartList(yieldly,supply_name,supply_code,partCode,partName,claim_no, type,dealerCode,curPage, Integer.parseInt(page_amount));
				ps = dao.getNoReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			}
			ActionUtil.setCustomPageSizeFlag(act, true);
			
			act.setOutData("ps", ps);
			act.setOutData("yieldly", yieldly);
			act.setOutData("type", type);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商修改查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void oldPartSupplyModPer(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
//			String sign_amount = dao.findAllSignNumSum(request);
			PageResult<ClaimOldPartOutPreListBean> ps  = null;
			ps = dao.getReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			String returnAmount = String.valueOf(ps.getTotalRecords());
			ps = dao.getNoReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			String noReturnAmount = String.valueOf(ps.getTotalRecords());
			act.setOutData("returnAmount", returnAmount);
			act.setOutData("noReturnAmount", noReturnAmount);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setOutData("type", "modify");
			act.setForword(sendUrl(this.getClass(), "oldPartSupplyModPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void findAllSignNumSum(){
		dao=ClaimOldPartOutStorageDao.getInstance();
//		String sign_amount = dao.findAllSignNumSum(request);
		String is_code = request.getParamValue("is_code");//是否实物,无实物直接由索赔单字表查询数据
		PageResult<ClaimOldPartOutPreListBean> ps  = null;
		String returnAmount = null;
		String noReturnAmount  = null;
		if(Constant.IF_TYPE_YES.toString().equalsIgnoreCase(is_code)){
			ps = dao.getReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			returnAmount = String.valueOf(ps.getTotalRecords());
			act.setOutData("returnAmount", returnAmount);
		} else {
			ps = dao.getNoReturnPreOutStoreList(request, ActionUtil.getPageSize(request), ActionUtil.getCurPage(request));
			noReturnAmount= String.valueOf(ps.getTotalRecords());
			act.setOutData("noReturnAmount", noReturnAmount);
		}
	}
	
	public void oldPartSupplyModPer2(){//东安
		try {
			
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setOutData("type", "modify");
			act.setForword(sendUrl(this.getClass(), "oldPartSupplyModPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void oldPartSupplyModFoward(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCode = request.getParamValue("partCode");
			String claimId = request.getParamValue("claimId");
			String yieldly = request.getParamValue("yieldly");
			String is_code = request.getParamValue("is_code");
			List<TtAsWrOldReturnedDetailPO> list = null;
			if(Constant.IF_TYPE_YES.toString().equalsIgnoreCase(is_code)){
				list  = dao.getDetail(partCode,claimId);
			}else{
				list  = dao.getDetail2(partCode,claimId);
			}
			
			if(list!=null && list.size()>0){
				act.setOutData("bean", list.get(0));
			}else{
				act.setOutData("bean", null);
			}
			
			act.setOutData("yieldly", yieldly);
			act.setForword(sendUrl(this.getClass(), "oldPartReInstoreInfo"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//选择供应商
	public void selectSupplierForward() {
	
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCodeTemp = request.getParamValue("partCode");
			act.setOutData("partCodeTemp", partCodeTemp);
			String partCode = request.getParamValue("code");
			act.setOutData("partCode", partCode);
			String id = request.getParamValue("id");
			act.setOutData("id", id);
			String yieldly = request.getParamValue("yieldly");
			if(Utility.testString(yieldly)){
				act.setOutData("yieldly", yieldly);
			}else{
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			}
			act.setForword(sendUrl(this.getClass(), "showSupply"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//选择供应商
	public void selectSupplierForward1() {
	
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCodeTemp = request.getParamValue("partCode");
			act.setOutData("partCodeTemp", partCodeTemp);
			String partCode = request.getParamValue("code");
			act.setOutData("partCode", partCode);
			String id = request.getParamValue("id");
			act.setOutData("id", id);
			String yieldly = request.getParamValue("yieldly");
			if(Utility.testString(yieldly)){
				act.setOutData("yieldly", yieldly);
			}else{
				act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			}
			act.setForword(sendUrl(this.getClass(), "showSupply1"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void querySupplier(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCode =getParam("partCode");// request.getParamValue("partCode");
			String partCodeTemp =getParam("partCodeTemp");
			String yieldly = request.getParamValue("yieldly");
			String supplierCode = request.getParamValue("SUPPLIER_CODE"); //供应商代码
			String supplierName = request.getParamValue("SUPPLIER_NAME"); //供应商名称
			String count = request.getParamValue("count"); //判断是否全查
			PageResult<TmPtSupplierPO> ps = dao.querySupplier(partCode,partCodeTemp,yieldly,supplierCode,supplierName,count, getCurrPage(), 10);
			act.setOutData("yieldly", yieldly);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void querySupplier1(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String yieldly = request.getParamValue("yieldly");
			PageResult<TmPtSupplierPO> ps = dao.querySupplier1(request, getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("yieldly", yieldly);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改供应商
	 */
	@SuppressWarnings("unchecked")
	public void supplierSave(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String type=request.getParamValue("type");// type==2 为 批量修改,null或者""为单条修改
			/**
			 * 批量修改数据内容
			 */
			String mod_code = getParam("mod_code"); //供应商代码
			String mod_name = getParam("mod_name"); //供应商名称
			String[] modId = request.getParamValues("recesel");
			if("2".equalsIgnoreCase(type)){
				for(int i=0;i<modId.length;i++){
					String[] str = modId[i].split(";");
					String claimId=str[0];
					String partCode=str[1];
					OutStoreService outstoreservice = new OutStoreServiceImpl();
					int res=outstoreservice.logUpatePartProductCode(logonUserBean,claimId,partCode,mod_code,mod_name);
					if(res==1){
						TtAsWrOldReturnedDetailPO dp = new TtAsWrOldReturnedDetailPO();
						TtAsWrOldReturnedDetailPO dp2 = new TtAsWrOldReturnedDetailPO();
						dp.setClaimId(Long.valueOf(claimId));
						dp.setPartCode(partCode);
						dp2.setProducerCode(mod_code.trim());
						dp2.setProducerName(mod_name);
						dp2.setUpdateBy(logonUserBean.getUserId());
						dp2.setUpdateDate(new Date());
						dao.update(dp, dp2);
						//更新索赔单子表--索赔配件表
						TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
						TtAsWrPartsitemPO p2 = new TtAsWrPartsitemPO();
						p.setDownPartCode(partCode);
						p.setId(Long.valueOf(claimId));
						p2.setDownProductCode(mod_code.trim());
						p2.setDownProductName(mod_name);
						p2.setUpdateBy(logonUserBean.getUserId());
						p2.setUpdateDate(new Date());
						dao.update(p, p2);
					}
				}
			}else{
			/**
			 * 单条修改数据明细
			 */
			String claimId = request.getParamValue("claimId");
			String partCode = request.getParamValue("partCode");
			String yieldly = request.getParamValue("yieldly");
			String supplierCode = request.getParamValue("supply_code"); //供应商代码
			String supplierName = request.getParamValue("supply_name"); //供应商名称
			OutStoreService outstoreservice = new OutStoreServiceImpl();
			int res=outstoreservice.logUpatePartProductCode(logonUserBean,claimId,partCode,mod_code,mod_name);
			if(res==1){
				//修改旧件明细表
				TtAsWrOldReturnedDetailPO dp = new TtAsWrOldReturnedDetailPO();
				TtAsWrOldReturnedDetailPO dp2 = new TtAsWrOldReturnedDetailPO();
				dp.setClaimId(Long.valueOf(claimId));
				dp.setPartCode(partCode);
				dp2.setProducerCode(supplierCode);
				dp2.setProducerName(supplierName);
				dp2.setUpdateBy(logonUserBean.getUserId());
				dp2.setUpdateDate(new Date());
				dao.update(dp, dp2);
				//更新索赔单子表--索赔配件表
				TtAsWrPartsitemPO p = new TtAsWrPartsitemPO();
				TtAsWrPartsitemPO p2 = new TtAsWrPartsitemPO();
				p.setDownPartCode(partCode);
				p.setId(Long.valueOf(claimId));
				p2.setDownProductCode(supplierCode);
				p2.setDownProductName(supplierName);
				dao.update(p, p2);
				act.setOutData("yieldly", yieldly);
				}
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件供应商修改");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//无旧件生成通知单跳转
	@SuppressWarnings("unchecked")
	public void addNoPartNoticePer(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = request.getParamValue("idStr");
			String no = dao.GenerateStockNo();
			String yieldly = request.getParamValue("yieldly");
			String[] str = id.split(",");
			for(int i=0;i<str.length;i++){
				String claimNo = request.getParamValue("claimNo"+str[i]);
    			String partCode = request.getParamValue("partCode"+str[i]);
    			String partName = request.getParamValue("partName"+str[i]);
    			String supplyCode = request.getParamValue("supplyCode"+str[i]);
    			String supplyName = request.getParamValue("supplyName"+str[i]);
    			String allAmount = request.getParamValue("allAmount"+str[i]);
    			String remark = request.getParamValue("remark"+str[i]);
    		
    			/**
    			 * 保存出库明细
    			 */
    			TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
    			pp.setClaimNo(claimNo);
    			pp.setCreateBy(logonUserBean.getUserId());
    			pp.setCreateDate( new Date());
    			pp.setId(Long.parseLong(SequenceManager.getSequence("")));
    			pp.setOutAmout(Integer.parseInt(allAmount));
    			pp.setOutBy(logonUserBean.getUserId());
    			pp.setOutDate( new Date());
    			pp.setOutNo(no);
    			pp.setOutPartCode(partCode);
    			pp.setOutPartName(partName);
    			pp.setRemark(remark);
    			pp.setSupplyCode(supplyCode);
    			pp.setSupplyName(supplyName);
    			pp.setYieldly(Integer.parseInt(yieldly));
    			pp.setOutPartType(Constant.OUT_PART_TYPE_04);
    			dao.insert(pp); 
    			/**
    			 * 修改旧件回运明细的旧件状态
    			 */
    			StringBuffer sql = new StringBuffer();
    			sql.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate where p.part_id="+str[i]);
    			dao.update(sql.toString(), null);
    			List<Map<String,Object>> list = dao.getpartList(partCode, claimNo);
    			if(list!=null && list.size()>0){//如果查询结果集有数据,说明该件下含有次因件，此时就将这些次因件也出库，并且插入到出库明细中
    				for(int k=0;k<list.size();k++){
    					StringBuffer sqlr = new StringBuffer();//更新次因件的出库状态
    					sqlr.append(" update TT_AS_WR_PARTSITEM p set p.is_notice="+Constant.IF_TYPE_YES+" ,p.Notice_Date=sysdate where p.part_id="+list.get(k).get("PART_ID").toString());
    	    			dao.update(sqlr.toString(), null);
    	    			//开始保存次因件的出库记录
    	    			TtAsWrOldOutPartPO pp2 = new TtAsWrOldOutPartPO();
    	    			pp2.setClaimNo(list.get(k).get("CLAIM_NO").toString());
    	    			pp2.setCreateBy(logonUserBean.getUserId());
    	    			pp2.setCreateDate( new Date());
    	    			pp2.setId(Long.parseLong(SequenceManager.getSequence("")));
    	    			pp2.setOutAmout(Integer.parseInt(list.get(k).get("AMOUNT").toString()));
    	    			pp2.setOutBy(logonUserBean.getUserId());
    	    			pp2.setOutDate( new Date());
    	    			pp2.setOutNo(no);
    	    			pp2.setOutPartCode(list.get(k).get("PART_CODE").toString());
    	    			pp2.setOutPartName(list.get(k).get("PART_NAME").toString());
    	    			pp2.setRemark(partCode+"责任连带");
    	    			pp2.setYieldly(Integer.parseInt(yieldly));
    	    			pp2.setOutPartType(Constant.OUT_PART_TYPE_04);
    	    			dao.insert(pp2); 
    				}
    			}
			}
			act.setOutData("updateResult", "updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//无旧件索赔时检测选择的配件
	@SuppressWarnings("unchecked")
	public void checkPart(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = request.getParamValue("idStr");
			String yieldly = request.getParamValue("yieldly");
			String[] str = id.split(",");
			Set set = new HashSet();
			List newList = new ArrayList();
			for(int i=0;i<str.length;i++){
				String part =  request.getParamValue("supplyCode"+str[i]);
				if (set.add(part))
					newList.add(part);
			}
			if(newList.size()>2){
				act.setOutData("note", "Y");
			}else{
				act.setOutData("note", "N");
			}
			act.setOutData("yieldly", yieldly);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次入库");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void detailDown(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String code = request.getParamValue("code");
			List<TtAsWrOldOutDoorBean> dp = dao.getPrintBaseBean(outNo, code);
			List<TtAsWrOldOutDoorDetailPO> list = dao.getPrintList(outNo, code);
			
			toReportExcel(act.getResponse(), request,dp.get(0),list, "二次索赔出门证.xls");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public Object toReportExcel(ResponseWrapper response,RequestWrapper request,TtAsWrOldOutDoorBean dp,List<TtAsWrOldOutDoorDetailPO> list,String excelName) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			//开始构建表格
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,12,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			//居中
			WritableFont font_1 = new WritableFont(WritableFont.ARIAL,17,WritableFont.BOLD);
			font_1.setUnderlineStyle(UnderlineStyle.SINGLE);
			WritableCellFormat wcf_1 = new WritableCellFormat(font_1);
			wcf_1.setAlignment(Alignment.CENTRE);
			WritableCellFormat wcf_2 = new WritableCellFormat(font);
			wcf_2.setAlignment(Alignment.LEFT);
			WritableCellFormat wcf_3 = new WritableCellFormat(font);
			wcf_3.setAlignment(Alignment.RIGHT);
			//第一行 8列
			sheet.mergeCells(0,0,15,0);
			sheet.addCell(new Label(0, 0, dp.getOutTittle(), wcf_1));
			//第二行 2列 分别占4格
			sheet.mergeCells(0,1,7,1);
			sheet.mergeCells(8,1,15,1);
			sheet.addCell(new Label(0,1,"NO:"+dp.getOutNo(),wcf_2));
			sheet.addCell(new Label(8,1,dp.getOutTime(),wcf_3));
			//第三行 
			sheet.mergeCells(0,2,1,2);
			sheet.mergeCells(2,2,10,2);
			sheet.mergeCells(11,2,12,2);
			sheet.mergeCells(13,2,15,2);
			sheet.addCell(new Label(0,2,"出门单位",wcf));
			sheet.addCell(new Label(2,2,dp.getOutCompany(),wcf_2));
			sheet.addCell(new Label(11,2,"联系电话",wcf));
			sheet.addCell(new Label(13,2,dp.getOutCompanyTel(),wcf));
			
			//第四行 
			sheet.mergeCells(0,3,1,3);
			sheet.mergeCells(2,3,15,3);
			sheet.addCell(new Label(0,3,"出库单位",wcf));
			sheet.addCell(new Label(2,3,dp.getOutByName(),wcf_2));
			//配件头开始
			sheet.mergeCells(0,4,1,4);
			sheet.mergeCells(2,4,7,4);
			sheet.mergeCells(8,4,9,4);
			sheet.mergeCells(11,4,15,4);
			sheet.addCell(new Label(0,4,"序号",wcf));
			sheet.addCell(new Label(2,4,"配件名称",wcf));
			sheet.addCell(new Label(8,4,"型号",wcf));
			sheet.addCell(new Label(10,4,"数量",wcf));
			sheet.addCell(new Label(11,4,"备注",wcf));
			int temp = 5;
			for(int i=0;i<list.size();i++){
				sheet.mergeCells(0,temp,1,temp);
				sheet.mergeCells(2,temp,7,temp);
				sheet.mergeCells(8,temp,9,temp);
				sheet.mergeCells(11,temp,15,temp);
				sheet.addCell(new Label(0,temp," "+(i+1),wcf));
				sheet.addCell(new Label(2,temp,list.get(i).getPartName(),wcf));
				sheet.addCell(new Label(8,temp,list.get(i).getModelName(),wcf));
				sheet.addCell(new Label(10,temp,list.get(i).getOutNum().toString(),wcf));
				sheet.addCell(new Label(11,temp,list.get(i).getOutRemark(),wcf));
				temp++;
			}
			sheet.mergeCells(0,temp+3,4,temp+3);
			sheet.mergeCells(5,temp+3,10,temp+3);
			sheet.mergeCells(11,temp+3,15,temp+3);
			sheet.addCell(new Label(0,temp+3,"出门单位：",wcf_2));
			sheet.addCell(new Label(5,temp+3,"经办:",wcf_2));
			sheet.addCell(new Label(11,temp+3,"主管:",wcf_2));
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	public void noticeDown(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			dao=ClaimOldPartOutStorageDao.getInstance();
			String id = request.getParamValue("id");
			List<TtAsWrOldOutNoticeBean> dp = dao.getBaseBeanForNoticeOut(id);
			List<TtAsWrOldOutNoticeDetailPO> list = dao.getPrintDetailOut(id,oemCompanyId,yieldlys);
			toReportExcel2(act.getResponse(), request,dp.get(0),list, "二次索赔通知单.xls");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public Object toReportExcel2(ResponseWrapper response,RequestWrapper request,TtAsWrOldOutNoticeBean dp,List<TtAsWrOldOutNoticeDetailPO> list,String excelName) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			//开始构建表格
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,12,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			//居中
			WritableFont font_1 = new WritableFont(WritableFont.ARIAL,17,WritableFont.BOLD);
			font_1.setUnderlineStyle(UnderlineStyle.SINGLE);
			WritableCellFormat wcf_1 = new WritableCellFormat(font_1);
			wcf_1.setAlignment(Alignment.CENTRE);
			WritableCellFormat wcf_2 = new WritableCellFormat(font);
			wcf_2.setAlignment(Alignment.LEFT);
			WritableCellFormat wcf_3 = new WritableCellFormat(font);
			wcf_3.setAlignment(Alignment.RIGHT);
			//第一行 8列
			sheet.mergeCells(0,0,15,0);
			sheet.addCell(new Label(0, 0, dp.getNoticeTittle(), wcf_1));
			//第二行 2列 分别占4格
			sheet.mergeCells(0,1,7,1);
			sheet.mergeCells(8,1,15,1);
			sheet.addCell(new Label(0,1,"NO:"+dp.getNoticeNo(),wcf_2));
			sheet.addCell(new Label(8,1,dp.getOutTime(),wcf_3));
			//第三行 
			sheet.mergeCells(0,2,1,2);
			sheet.mergeCells(2,2,10,2);
			sheet.mergeCells(11,2,12,2);
			sheet.mergeCells(13,2,15,2);
			sheet.addCell(new Label(0,2,"被索赔单位",wcf));
			sheet.addCell(new Label(2,2,dp.getNoticeCompany(),wcf_2));
			sheet.addCell(new Label(11,2,"联系电话",wcf));
			sheet.addCell(new Label(13,2,dp.getNoticeCompanyByTel(),wcf));
			
			//第四行 
			sheet.mergeCells(0,3,1,3);
			sheet.mergeCells(2,3,15,3);
			sheet.addCell(new Label(0,3,"索赔单位",wcf));
			sheet.addCell(new Label(2,3,dp.getNoticeCompanyBy(),wcf_2));
			//
			sheet.mergeCells(0,4,1,4);
			sheet.mergeCells(2,4,10,4);
			sheet.mergeCells(11,4,12,4);
			sheet.mergeCells(13,4,15,4);
			sheet.addCell(new Label(0,4,"开户行",wcf));
			sheet.addCell(new Label(2,4,dp.getNoticeBank(),wcf_2));
			sheet.addCell(new Label(11,4,"银行账号",wcf));
			sheet.addCell(new Label(13,4,dp.getNoticeAcount(),wcf));
			
			//配件头开始
			sheet.mergeCells(1,5,2,5);
			sheet.mergeCells(10,5,11,5);
			sheet.mergeCells(12,5,13,5);
			sheet.mergeCells(14,5,15,5);
			sheet.addCell(new Label(0,5,"序号",wcf));
			sheet.addCell(new Label(1,5,"配件名称",wcf));
			sheet.addCell(new Label(3,5,"数量",wcf));
			sheet.addCell(new Label(4,5,"型号",wcf));
			sheet.addCell(new Label(5,5,"索赔单价",wcf));
			sheet.addCell(new Label(6,5,"工时",wcf));
			sheet.addCell(new Label(7,5,"材料费",wcf));
			sheet.addCell(new Label(8,5,"工时费",wcf));
			sheet.addCell(new Label(9,5,"其他",wcf));
			sheet.addCell(new Label(10,5,"小计",wcf));
			sheet.addCell(new Label(12,5,"税额",wcf));
			sheet.addCell(new Label(14,5,"合计",wcf));
			int temp = 6;
			for(int i=0;i<list.size();i++){
				sheet.mergeCells(1,temp,2,temp);
				sheet.mergeCells(10,temp,11,temp);
				sheet.mergeCells(12,temp,13,temp);
				sheet.mergeCells(14,temp,15,temp);
				sheet.addCell(new Label(0,temp,""+(i+1),wcf));
				sheet.addCell(new Label(1,temp,list.get(i).getPartName(),wcf));
				sheet.addCell(new Label(3,temp,list.get(i).getOutNum().toString(),wcf));
				sheet.addCell(new Label(4,temp,list.get(i).getModelName(),wcf));
				sheet.addCell(new Label(5,temp,list.get(i).getClaimPrice().toString(),wcf));
				sheet.addCell(new Label(6,temp,list.get(i).getClaimLabour().toString(),wcf));
				sheet.addCell(new Label(7,temp,list.get(i).getPartPrice().toString(),wcf));
				sheet.addCell(new Label(8,temp,list.get(i).getLabourPrice().toString(),wcf));
				sheet.addCell(new Label(9,temp,list.get(i).getOtherPrice().toString(),wcf));
				sheet.addCell(new Label(10,temp,list.get(i).getSmallTotal().toString(),wcf));
				sheet.addCell(new Label(12,temp,list.get(i).getTaxTotal().toString(),wcf));
				sheet.addCell(new Label(14,temp,list.get(i).getTotal().toString(),wcf));
				temp++;
			}
			sheet.mergeCells(0,temp,7,temp);
			sheet.mergeCells(8,temp,9,temp);
			sheet.mergeCells(10,temp,11,temp);
			sheet.mergeCells(12,temp,13,temp);
			sheet.mergeCells(14,temp,15,temp);
			sheet.addCell(new Label(8,temp,"总计",wcf));
			sheet.addCell(new Label(10,temp,dp.getSmallTotal().toString(),wcf));
			sheet.addCell(new Label(12,temp,dp.getTaxTotal().toString(),wcf));
			sheet.addCell(new Label(14,temp,dp.getTotal().toString(),wcf));
			temp++;
			sheet.mergeCells(0,temp,3,temp);
			sheet.mergeCells(4,temp,15,temp);
			sheet.addCell(new Label(0,temp,"总金额大写：",wcf));
			sheet.addCell(new Label(4,temp,Utility.digitUppercase(dp.getTotal()),wcf_2));
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 索赔旧件开票
	 */
	public void queryOut(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_01);
			act.setForword(sendUrl(this.getClass(), "queryOutDoorList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void queryOut2(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("yieldly", Constant.PART_IS_CHANGHE_02);
			act.setForword(sendUrl(this.getClass(), "queryOutDoorList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
//出门证查询
	@SuppressWarnings("unchecked")
	public void queryOutDoorList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
					
			params.put("company_id",companyId);
			params.put("supply_name", request.getParamValue("supply_name"));
			params.put("supply_code", request.getParamValue("SUPPLY_CODE"));
			params.put("yieldly", request.getParamValue("yieldly"));
			PageResult<Map<String, Object>> ps=dao.getOutDoorList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//出门证新增跳转
	public void addOutDoor(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			
			act.setOutData("yieldly", request.getParamValue("yieldly"));
			act.setForword(sendUrl(this.getClass(), "queryPreOutDoorAdd"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出门证新增");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//出门证新增时,查询出库记录
	@SuppressWarnings("unchecked")
	public void outDoorAddQuery(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			Map params=new HashMap<String,String>();
	
			String supply_name=request.getParamValue("supply_name");
			String part_code=request.getParamValue("part_code");
			String part_name=request.getParamValue("part_name");
			String model_code=request.getParamValue("model_code");
			String yieldly = request.getParamValue("yieldly");
			params.put("company_id",companyId);
			params.put("supply_name", supply_name);
			params.put("part_code", part_code);
			params.put("part_name", part_name);
			params.put("model_code", model_code);
			params.put("yieldly", yieldly);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getOutStoreList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("model_code", model_code);
			act.setOutData("ps", ps);
			//act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出门证查询出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
		
	}
	
	//出门证生成跳转
	@SuppressWarnings("unchecked")
	public void outOfDoorAdd(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String idStr=request.getParamValue("idStr");
			String[] arr=idStr.split(",");
			String  yieldly =request.getParamValue("yieldly");
			String venderCode = request.getParamValue("supplyCode"+arr[0]);//供应商代码
			boolean flag = true;
			String msg ="";
			Set set = new HashSet();
			for(int i=0;i<arr.length;i++){
				String modelCode = request.getParamValue("modelCode"+arr[i]);//开票车型
				if(!venderCode.equalsIgnoreCase(request.getParamValue("supplyCode"+arr[i]))){//判断供应商是否是有一家
					flag = false;
					msg="一次只能对一家供应商开票!";
				}
				if(set.add(request.getParamValue("partCode"+arr[i]))){
					set.add(request.getParamValue("partCode"+arr[i]));
				}
				TtAsWrModelPricePO pp =new TtAsWrModelPricePO();
				pp.setModelCode(modelCode.toUpperCase());
				pp.setStatus(Constant.IF_TYPE_YES);
				List<TtAsWrModelPricePO> list = dao.select(pp);
				if(list.size()<1){
					flag = false;
					msg="输入的车型【"+modelCode+"】还未维护工时单价!";
					break;
				}
			}
			if(set.size()>4){
				flag = false;
				msg="一次只能开四种配件!";
			}
			if(flag){
				String no = dao.GenerateStockNo();
				TtPartMakerDefinePO mp = new TtPartMakerDefinePO();
				mp.setMakerCode(request.getParamValue("supplyCode"+arr[0]));
				mp = (TtPartMakerDefinePO) dao.select(mp).get(0);
				TtAsWrOldOutDoorPO dp  = new TtAsWrOldOutDoorPO();
				dp.setDoorId(Long.parseLong(SequenceManager.getSequence("")));
				dp.setCreateBy(logonUserBean.getUserId());
				dp.setCreateDate(new Date());
				dp.setNoticeNo(no.split(",")[1]);
				dp.setOutByName("北汽幻速售后技术服务处");
				dp.setOutCompany(mp.getMakerName()+"*"+mp.getMakerCode());
				dp.setOutCompanyCode(mp.getMakerCode());
				dp.setOutCompanyTel(mp.getTel());
				dp.setOutNo(no.split(",")[0]);
				dp.setOutTittle("北汽幻速汽车质量保证索赔故障件出门证");
				dp.setYieldly(Integer.parseInt(yieldly));
				dp.setPrintTimes(0);
				dao.insert(dp);
				for(int i=0;i<arr.length;i++){//开始处理数据
					String partCode = request.getParamValue("partCode"+arr[i]);//配件代码
					String partName = request.getParamValue("partName"+arr[i]);//配件名称
					String modelCode = request.getParamValue("modelCode"+arr[i]);//开票车型
					String outNum = request.getParamValue("outNum"+arr[i]);//开票数量
					String supplyCode = request.getParamValue("supplyCode"+arr[i]);
					String remark = request.getParamValue("remark"+arr[i]);
					String outPartType = request.getParamValue("OUT_PART_TYPE"+arr[i]);
					TtAsWrOldOutDoorDetailPO p = new TtAsWrOldOutDoorDetailPO();
					p.setCreateBy(logonUserBean.getUserId());
					p.setCreateDate( new Date());
					p.setDoorId(dp.getDoorId());
					p.setId(Long.parseLong(SequenceManager.getSequence("")));
					p.setModelName(modelCode);
					p.setOutNum(Integer.parseInt(outNum));
					p.setOutPartType(Integer.parseInt(outPartType));
					p.setOutRemark(remark);
					p.setPartCode(partCode);
					p.setPartName(partName);
					p.setRemandNum(0);
					dao.insert(p);
					//数据插入完成,开始更新出库单明细数据状态
					TtAsWrOldOutDetailPO ap = new TtAsWrOldOutDetailPO();
					ap.setOutPartCode(partCode);
					ap.setOutPartType(Integer.parseInt(outPartType));
					ap.setSupplayCode(supplyCode);
					ap.setIsDikou(0);
					ap.setOutType(Long.valueOf(Constant.OUT_CLAIM_TYPE_01));
					List<TtAsWrOldOutDetailPO> list = dao.select(ap);
					for(int j=0;j<Integer.parseInt(outNum);j++){
						String sql = "update tt_as_wr_old_out_detail d set d.is_out_door = 1,D.OUT_NO="+dp.getOutNo()+" where d.id = "+list.get(j).getId();
						dao.update(sql, null);
					}
				}
			//	dao.saveOutOfStoreLogOper(request,logonUserBean);	
				act.setOutData("yieldly",yieldly);
				act.setOutData("updateResult","updateSuccess");
				act.setOutData("msg","");
			}else{
				act.setOutData("updateResult","updateFaile");
				act.setOutData("yieldly",yieldly);
				act.setOutData("msg",msg);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//出门证 删除
	@SuppressWarnings("unchecked")
	public void outDel(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			SimpleDateFormat sdf  = new SimpleDateFormat("MM");
			String str = "";
			String updateResult="";
			System.out.println(outNo);
			TtAsWrOldOutNoticePO np = new TtAsWrOldOutNoticePO();
			np.setOutNo(outNo);
			List<TtAsWrOldOutNoticePO> list = dao.select(np);
			TtAsWrOldOutDoorPO dp = new TtAsWrOldOutDoorPO();
			dp.setOutNo(outNo);
			dp = (TtAsWrOldOutDoorPO) dao.select(dp).get(0);
			System.out.println(sdf.format(dp.getCreateDate()));
			System.out.println(Calendar.getInstance().get(Calendar.MONTH) + 1);
			if(list.size()>0){// 判断是否生成了通知单,若有则不能删除
				str = "该出门证已经生成了开票通知单,请先删除通知后再执行删除!";
			}else if(Integer.parseInt(sdf.format(dp.getCreateDate()))!=Calendar.getInstance().get(Calendar.MONTH) + 1){//判断是否是当月生成的出门证,如果不是则不能删除
				str = "该出门证不是本月新增,无法删除(只能删除本月生成的出门证)!";
			}else{
				//先删除明细数据
				TtAsWrOldOutDoorDetailPO ddp = new TtAsWrOldOutDoorDetailPO();
				ddp.setDoorId(dp.getDoorId());
				dao.delete(ddp);
				//删除主表数据
				TtAsWrOldOutDoorPO p = new TtAsWrOldOutDoorPO();
				p.setOutNo(outNo);
				dao.delete(p);
				//然后再将出库明细数据,对应生成了出门证的数据状态改回去
				String sql ="update Tt_As_Wr_Old_Out_Detail d set d.is_out_door=0,d.out_no=null where d.out_no='"+outNo+"'";
				dao.update(sql, null);
				updateResult="ok";
			}
			act.setOutData("strInfo", str);
			act.setOutData("updateResult", updateResult);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出门证删除");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	//配件工时维护
	public void partLabourPer(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "partLabourPer"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void queryPart(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			
			String partCode=request.getParamValue("PART_CODE");
			String partName=request.getParamValue("PART_NAME");
			String labourCode=request.getParamValue("LABOUR_CODE");
			String labourName=request.getParamValue("LABOUR_NAME");
			params.put("partCode", partCode);
			params.put("partName", partName);
			params.put("labourCode", labourCode);
			params.put("labourName", labourName);
			params.put("pos", logonUserBean.getPoseType());
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getPartList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			//act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void modfiyPer(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCode=request.getParamValue("partCode");
			List <TtAsWrOldPartLabourPO> list = dao.getPartDetail(partCode);
			act.setOutData("bean", list.get(0));
			act.setForword(sendUrl(this.getClass(), "partLabourModify"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void showLabour(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(sendUrl(this.getClass(), "showLabour"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void showLabourList(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			
			String labourCode=request.getParamValue("LABOUR_CODE");
			String labourName=request.getParamValue("LABOUR_NAME");
			params.put("labourCode", labourCode);
			params.put("labourName", labourName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getLabourList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			//act.setForword(sendUrl(this.getClass(), "queryPreOutStorageList"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void save(){
		try {
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String labourCode=request.getParamValue("LABOUR_CODE");
			String labourName=request.getParamValue("LABOUR_NAME");
			String partCode=request.getParamValue("PART_CODE");
			String partName=request.getParamValue("PART_NAME");
			String hours=request.getParamValue("LABOUR_HOURS");
			String price=request.getParamValue("LABOUR_PRICE");
			TtAsWrOldPartLabourPO lp = new TtAsWrOldPartLabourPO();
			TtAsWrOldPartLabourPO lp2 = new TtAsWrOldPartLabourPO();
			lp.setPartCode(partCode);
			List<TtAsWrOldPartLabourPO> list = dao.select(lp);
			if(list!=null&&list.size()>0){//如果存在改配件,说明已经维护过了,直接更新
				lp2.setLabourCode(labourCode);
				lp2.setLabourHours(Double.valueOf(hours));
				lp2.setLabourPrice(Double.valueOf(price));
				lp2.setUpdateBy(logonUserBean.getUserId());
				lp2.setUpdateDate(new Date());
				dao.update(lp, lp2);
			}else{//如果不存在,就新增
				lp2.setCreateBy(logonUserBean.getUserId());
				lp2.setCreateDate(new Date());
				lp2.setId(Long.parseLong(SequenceManager.getSequence("")));
				lp2.setLabourCode(labourCode);
				lp2.setLabourHours(Double.valueOf(hours));
				lp2.setLabourName(labourName);
				lp2.setLabourPrice(Double.valueOf(price));
				lp2.setPartCode(partCode);
				lp2.setPartName(partName);
				lp2.setStatus(Constant.IF_TYPE_YES);
				dao.insert(lp2);
			}
			act.setOutData("modResult", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveRenge(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String[] id = request.getParamValues("id");
			String[] partAmount=request.getParamValues("PART_AMOUNT");
			String[] smallAmount=request.getParamValues("SMALL_AMOUNT");
			String[] printNum=request.getParamValues("printNum");
			String[] num = request.getParamValues("NUM");
			String[] partCount = request.getParamValues("PART_AMOUNTS");
			String[] outType = request.getParamValues("OUT_TYPE");
			String[] remark = request.getParamValues("remark");
			/**
			 * 增加工时修改
			 */
			String[] labourCode = request.getParamValues("labourCode");
			String[] labourAmount = request.getParamValues("LABOUR_AMOUNT");
			String[] labourAmountChg = request.getParamValues("LABOUR_AMOUNT_CHG");
			String cType = request.getParamValue("cType");
			if(id!=null){
				for(int i=0;i<id.length;i++){
					TtAsWrRangeSinglePO sp = new TtAsWrRangeSinglePO();
					TtAsWrRangeSinglePO sp2 = new TtAsWrRangeSinglePO();
					TtAsWrRangeSinglePO sp3 = null;
					System.out.println(id[i]);
					sp.setId(Long.valueOf(id[i]));
					List<TtAsWrRangeSinglePO> list=dao.select(sp);
					if(list.size()>0){
						sp3 = (TtAsWrRangeSinglePO)dao.select(sp).get(0);
					}
					
					if("2".equalsIgnoreCase(cType)){
						sp2.setAuditBy(logonUserBean.getUserId());
						sp2.setAuditDate(new Date());
						sp2.setOutType(Integer.parseInt(outType[i])+1);
						sp2.setRemark(remark[i]);
						dao.update(sp, sp2);
					}else{
					sp2.setPrintPart(Double.valueOf(partAmount[i]));
					sp2.setSmallAmount(Double.valueOf(smallAmount[i]));
					sp2.setPrintNum(Integer.parseInt(printNum[i]));
					sp2.setOutType(Integer.parseInt(outType[i])+1);
					sp2.setOldLabourAmount(sp3.getLabourAmount());
					sp2.setLabourAmount(Double.parseDouble(labourAmountChg[i]));
					sp2.setRemark(remark[i]);
					dao.update(sp, sp2);
					if(!printNum[i].equalsIgnoreCase(num[i])&&"0".equalsIgnoreCase(outType[i])){//如果数量发生了变化则记录明细
						TtAsRangeModDetailPO dp = new TtAsRangeModDetailPO();
						dp.setDataId(Long.valueOf(id[i]));
						dp.setId(Long.parseLong(SequenceManager.getSequence("")));
						dp.setModAfter(printNum[i]);
						dp.setModBefor(num[i]);
						dp.setModBy(logonUserBean.getUserId());
						dp.setModDate(new Date());
						dp.setModType(0);
						dp.setLabourBefore(sp3.getLabourAmount());
						dp.setLabourAfter(Double.parseDouble(labourAmountChg[i]));
						dp.setLabourCode(labourCode[i]);
						dao.insert(dp);
					}
					if(!partCount[i].equalsIgnoreCase(partAmount[i])&&"1".equalsIgnoreCase(outType[i])){//如果金额发生了变化则记录明细
						TtAsRangeModDetailPO dp = new TtAsRangeModDetailPO();
						dp.setDataId(Long.valueOf(id[i]));
						dp.setId(Long.parseLong(SequenceManager.getSequence("")));
						dp.setModAfter(partAmount[i]);
						dp.setModBefor(partCount[i]);
						dp.setModBy(logonUserBean.getUserId());
						dp.setModDate(new Date());
						dp.setModType(1);
						dp.setLabourBefore(sp3.getLabourAmount());
						dp.setLabourAfter(Double.parseDouble(labourAmountChg[i]));
						dp.setLabourCode(labourCode[i]);
						dao.insert(dp);
					}
				}
				}
			}
				act.setOutData("cType", cType);
				act.setOutData("updateResult", "updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔旧件出库查询--出库明细");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void printRenge(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("out_no");
			String sql = "update Tt_As_Wr_Range_Single s set s.out_type=1 ,s.print_by="+logonUserBean.getUserId()+",s.print_date=sysdate where s.out_no='"+outNo+"' and s.out_type=0";
			dao.update(sql, null);
			List<Map<String,Object>> list = dao.printRenge(outNo);
			if(DaoFactory.checkListNull(list)){
				act.setOutData("listBean", list);
			}
			List<Map<String,Object>> listOhers = dao.findOthersPartsByOutNo(outNo);
			if(DaoFactory.checkListNull(list)){
				act.setOutData("listOhers", listOhers);
			}
			//List<Map<String,Object>> listRemark = dao.getprintRemark(list);
			/*if(list!=null && list.size()>0){
				act.setOutData("listBean", list);
				act.setOutData("listRemark", listRemark);
			}else{
				act.setOutData("listBean", null);
				act.setOutData("listRemark", null);
			}*/
			act.setForword(sendUrl(this.getClass(), "rangeSinglePrint"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE, "退赔单打印");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 出库时查看关联件
	 */
	public void queryPartClaim(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String partCode = request.getParamValue("partCode");
			String claimNo = request.getParamValue("claimNo");
			String supCode = request.getParamValue("supCode");
			String supName = new String( request.getParamValue("supName").getBytes("ISO-8859-1"),"UTF-8");
			List<Map<String, Object>>  list = dao.queryPartClaim(partCode,claimNo);
			act.setOutData("list", list);
			act.setOutData("supCode", supCode);
			act.setOutData("supName", supName);
			act.setForword(sendUrl(this.getClass(), "mainPartDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 出库时打印关联件
	 */
	public void printPartCode(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String code = request.getParamValue("code");
			String dcode=request.getParamValue("dcode");
			String partCode = request.getParamValue("partCode");
			String claimNo = request.getParamValue("claimNo");
			String name="";
			if(Utility.testString(request.getParamValue("name"))){
				 name= new String(request.getParamValue("name").getBytes("ISO-8859-1"),"UTF-8");
			}
			String dname="";
			if(Utility.testString(request.getParamValue("dname"))){
				dname =new String(request.getParamValue("dname").getBytes("ISO-8859-1"),"UTF-8");
			}
			String partName="";
			 if(Utility.testString(request.getParamValue("partName"))){
				 partName =new String(request.getParamValue("partName").getBytes("ISO-8859-1"),"UTF-8");
			 }
			
			List<Map<String, Object>>  list = dao.printPartCode(code,dcode,partCode,claimNo,name,dname,partName);
			act.setOutData("list", list);
			act.setForword(sendUrl(this.getClass(), "printPartCode"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件工时数维护");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void getNo(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			
			String supply_name=request.getParamValue("supply_name");
			String supply_code=request.getParamValue("supply_code");
			
			List<Map<String, Object>> relationOutNo = dao.getOutNo(supply_code,supply_name);
			act.setOutData("relationOutNo", relationOutNo);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--查询出库信息");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 退赔单财务确认查询
	 */
	public void queryRangeList(){
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String,Object>> ps=dao.queryRangeList(request, getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 入库明细跳转
	 */
	public void inHouseDetail(){
		try {
			
			act.setForword(sendUrl(this.getClass(), "inHouseDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 入库明细查询
	 */
	@SuppressWarnings("unchecked")
	public void inHouseDetailList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			params.put("dealer_name", request.getParamValue("dealer_name"));
			params.put("part_name",  request.getParamValue("part_name"));
			params.put("dealer_code",  request.getParamValue("dealer_code"));
			params.put("part_code", request.getParamValue("part_code"));
			params.put("claim_no",  request.getParamValue("claim_no"));
			params.put("vin",  request.getParamValue("vin"));
			params.put("in_by_name", request.getParamValue("in_by_name"));
			params.put("in_start_date",  request.getParamValue("in_start_date"));
			params.put("in_end_date",  request.getParamValue("in_end_date"));
			params.put("IS_MAIN_CODE",  request.getParamValue("IS_MAIN_CODE"));
			params.put("is_ok",  request.getParamValue("is_ok"));
			params.put("engine_no",  request.getParamValue("engine_no"));
			params.put("producer_name",  request.getParamValue("producer_name"));//==增加供应商模糊查询2015-6-12lj
			PageResult<Map<String,Object>> ps=dao.inHouseDetailList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件出库查询--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1); 
		}
	}
	@SuppressWarnings("unchecked")
	public void toExcel(){
		 
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			response=act.getResponse();
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			params.put("dealer_name", request.getParamValue("dealer_name"));
			params.put("part_name",  request.getParamValue("part_name"));
			params.put("dealer_code",  request.getParamValue("dealer_code"));
			params.put("part_code", request.getParamValue("part_code"));
			params.put("claim_no",  request.getParamValue("claim_no"));
			params.put("vin",  request.getParamValue("vin"));
			params.put("in_by_name", request.getParamValue("in_by_name"));
			params.put("in_start_date",  request.getParamValue("in_start_date"));
			params.put("in_end_date",  request.getParamValue("in_end_date"));
			params.put("IS_MAIN_CODE",  request.getParamValue("IS_MAIN_CODE"));
			params.put("is_ok",  request.getParamValue("is_ok"));
			params.put("engine_no",  request.getParamValue("engine_no"));
			params.put("producer_name",  request.getParamValue("producer_name"));//==增加供应商模糊查询2015-6-12lj
			

		    String[] head={"入库序号","服务站代码","服务站名称","索赔单号","数量","扣件原因","配件代码","配件名称","供应商代码",
		    		"供应商名称","故障描述","车型","VIN","审核人","操作人","入库时间","零件性质","索赔类型","回运单号","旧件编号"};
			PageResult<Map<String,Object>> result=dao.inHouseDetailList2(params);
			List<Map<String, Object>> results = result.getRecords();
			List param=new ArrayList(); 
			for (Map<String, Object> map : results) {
				String[] dataList=new String[20];
				dataList[0]=(CommonUtils.checkNull(map.get("SIGN_NO")));
				dataList[1]=(CommonUtils.checkNull(map.get("DEALER_CODE")));
				dataList[2]=(CommonUtils.checkNull(map.get("DEALER_NAME")));
				dataList[3]=(CommonUtils.checkNull(map.get("CLAIM_NO"))+"");
				dataList[4]=(CommonUtils.checkNull(map.get("SIGN_AMOUNT")));
				dataList[5]=(CommonUtils.checkNull(map.get("DEDUCT_REMARK")));
				dataList[6]=(CommonUtils.checkNull(map.get("PART_CODE")));
				dataList[7]=(CommonUtils.checkNull(map.get("PART_NAME")));
				dataList[8]=(CommonUtils.checkNull(map.get("PRODUCER_CODE")));
				dataList[9]=(CommonUtils.checkNull(map.get("PRODUCER_NAME")));
				dataList[10]=(CommonUtils.checkNull(map.get("REMARK")));
				dataList[11]=(CommonUtils.checkNull(map.get("MODEL_NAME")));
				dataList[12]=(CommonUtils.checkNull(map.get("VIN")));
				dataList[13]=(CommonUtils.checkNull(map.get("IN_WARHOUSE_NAME")));
				dataList[14]=(CommonUtils.checkNull(map.get("NAME")));
				dataList[15]=(CommonUtils.checkNull(map.get("IN_TIME")));
				dataList[16]=(CommonUtils.checkNull(map.get("IS_MAIN_CODE")));
				dataList[17]=(CommonUtils.checkNull(map.get("CLAIM_TYPE")));
				dataList[18]=(CommonUtils.checkNull(map.get("RETURN_NO")));
				dataList[19]=(CommonUtils.checkNull(map.get("BARCODE_NO")));
				param.add(dataList);
			}
			   String systemDateStr = BaseUtils.getSystemDateStr();
				BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, param, "入库明细导出"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件库存查询--导出功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 删除出库记录
	 */
	@SuppressWarnings("unchecked")
	public void delOutOfStore(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			String outNo = request.getParamValue("outNo");
			TtAsWrOldOutPartPO pp = new TtAsWrOldOutPartPO();
			pp.setOutNo(outNo);
			List<TtAsWrOldOutPartPO> list = dao.select(pp);
			//开始回滚数据
			//删除配件供应商对于关系数据
			String sql1 = "DELETE FROM range_temp t WHERE t.out_no='"+outNo+"'";
			dao.delete(sql1, null);
			//如果含有特殊费用单子，更新特殊费用单子状态标志
			String sql2 = "UPDATE Tt_As_Wr_Spefee s SET s.Is_Notice=10041002,s.notice_date = NULL  WHERE s.fee_no IN (SELECT p.claim_no FROM Tt_As_Wr_Old_Out_Part p WHERE p.out_no='"+outNo+"')";
			dao.update(sql2, null);
			//删除通知单数据
			String sql3 = "DELETE FROM Tt_As_Wr_Range_Single rs WHERE rs.out_no='"+outNo+"'";
			dao.delete(sql3, null);
			//查询出库明细循环更新数据
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					//判断是否属于切换件，再看是以无件出库或者实物件出库
					String sql4 = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=0,d.is_cliam=0 WHERE d.claim_no='"+list.get(i).getClaimNo()+"' AND d.part_code='"+list.get(i).getOutPartCode()+"'";
					if(list.get(i).getOutFlag()==2){//第二次以实物出库，此时更新旧件库存表状态为无件出库
						sql4 = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=2,d.is_cliam=2 WHERE d.claim_no='"+list.get(i).getClaimNo()+"' AND d.part_code='"+list.get(i).getOutPartCode()+"'";
					}
					//如果在回运明细则更新回运明细状态
					//String sql4 = "UPDATE Tt_As_Wr_Old_Returned_Detail d SET d.is_out=0,d.is_cliam=0 WHERE d.claim_no='"+list.get(i).getClaimNo()+"' AND d.part_code='"+list.get(i).getOutPartCode()+"'";
					dao.update(sql4, null);
					//如果是无实物,则更新索赔配件字表状态
					String sql5 = "UPDATE Tt_As_Wr_Partsitem p SET p.is_notice=10041002,p.notice_date=NULL WHERE p.ID IN (SELECT ID FROM Tt_As_Wr_Application a WHERE a.claim_no='"+list.get(i).getClaimNo()+"') AND p.part_code='"+list.get(i).getOutPartCode()+"'";
					dao.update(sql5, null);
				}
			}
			//最后删除出库明细记录
			String sql6 = "DELETE FROM tt_as_wr_old_out_part p where p.out_no='"+outNo+"'";
			dao.delete(sql6, null);
			
			act.setOutData("updateResult", "updateSuccess");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "删除出库记录");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 入库明细跳转
	 */
	public void oldPartReturnDetail(){
		try {
			AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			act.setForword(sendUrl(this.getClass(), "oldPartReturnDetail"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件返运明细--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 入库明细查询
	 */
	@SuppressWarnings("unchecked")
	public void oldPartReturnDetailList(){
		try {
			
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("dealer_name", getParam("dealer_name"));
			params.put("part_name",  getParam("part_name"));
			params.put("dealer_code",  getParam("dealer_code"));
			params.put("part_code", getParam("part_code"));
			params.put("vin",  getParam("vin"));
			params.put("return_status", getParam("return_status"));
			params.put("in_start_date",  getParam("in_start_date"));
			params.put("in_end_date",  getParam("in_end_date"));
			params.put("is_ok",  getParam("is_ok"));
			params.put("claim_type",  getParam("claim_type"));
			params.put("return_no",  getParam("return_no"));
			params.put("claim_no",  getParam("claim_no"));
			params.put("beginTime",  getParam("beginTime"));
			params.put("endTime",  getParam("endTime"));
			params.put("root_org_name",  getParam("root_org_name"));
			params.put("urgent",  getParam("urgent"));
			params.put("is_out",  getParam("is_out"));
			params.put("inhouse_start_date",  getParam("inhouse_start_date"));
			params.put("inhouse_end_date",  getParam("inhouse_end_date"));
			params.put("producer_name",  getParam("producer_name"));//lj2015-6-12增加供应商模糊查询
			PageResult<Map<String,Object>> ps=dao.oldPartReturnDetailList(params, getCurrPage(), Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件返运明细--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1); 
		}
	}
	@SuppressWarnings("unchecked")
	public void toExcel2(){
		OutputStream os = null;
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartOutStorageDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("dealer_name", getParam("dealer_name"));
			params.put("part_name",  getParam("part_name"));
			params.put("dealer_code",  getParam("dealer_code"));
			params.put("part_code", getParam("part_code"));
			params.put("vin",  getParam("vin"));
			params.put("return_status", getParam("return_status"));
			params.put("in_start_date",  getParam("in_start_date"));
			params.put("in_end_date",  getParam("in_end_date"));
			params.put("is_ok",  getParam("is_ok"));
			params.put("claim_type",  getParam("claim_type"));
			params.put("return_no",  getParam("return_no"));
			params.put("claim_no",  getParam("claim_no"));
			params.put("beginTime",  getParam("beginTime"));
			params.put("endTime",  getParam("endTime"));
			params.put("root_org_name",  getParam("root_org_name"));
			params.put("urgent",  getParam("urgent"));
			params.put("is_out",  getParam("is_out"));
			params.put("inhouse_start_date",  getParam("inhouse_start_date"));
			params.put("inhouse_end_date",  getParam("inhouse_end_date"));
			params.put("producer_name",  getParam("producer_name"));//lj2015-6-12增加供应商模糊查询
			
			// 导出的文件名
			String fileName = "旧件返运明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list = new LinkedList<List<Object>>();
			//表头
			List<Object> titleList = new LinkedList<Object>();
			titleList.add("序号");
			titleList.add("服务站代码");
			titleList.add("服务站名称");
			titleList.add("大区");
			titleList.add("回运类型");
			titleList.add("索赔单号");
			titleList.add("索赔单申请时间");
			titleList.add("索赔单审核时间");
			titleList.add("入库时间");
			titleList.add("配件代码");
			titleList.add("配件名称");
			titleList.add("回运状态");
			titleList.add("审核状态");
			titleList.add("拒赔原因");
			titleList.add("回运申请时间");
			titleList.add("供应商代码");
			titleList.add("供应商名称");
			titleList.add("车型");
			titleList.add("VIN");
			titleList.add("零件性质");
			titleList.add("索赔类型");
			titleList.add("回运单号");
			titleList.add("旧件编号");
			list.add(titleList);
			PageResult<Map<String,Object>> results=dao.oldPartReturnDetailList(params, getCurrPage(), Constant.PAGE_SIZE_MAX);
			List<Map<String, Object>> records = results.getRecords();
			for (int i = 0; i < records.size(); i++) {
				Map<String, Object> record = records.get(i);
				List<Object> dataList = new LinkedList<Object>();
				dataList.add(i+1);
				dataList.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				dataList.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
				if ("0".equals(record.get("URGENT").toString())) {
					dataList.add("常规回运");//回运类型
				}else if("1".equals(record.get("URGENT").toString())) {
					dataList.add("紧急调件");//回运类型
				}
				dataList.add(CommonUtils.checkNull(record.get("CLAIM_NO"))+"");
				dataList.add(CommonUtils.checkNull(record.get("SUB_TIME")));
				dataList.add(CommonUtils.checkNull(record.get("REPORT_TIME")));
				dataList.add(CommonUtils.checkNull(record.get("IN_WARHOUSE_DATE")));
				dataList.add(CommonUtils.checkNull(record.get("PART_CODE")));
				dataList.add(CommonUtils.checkNull(record.get("PART_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("RETURN_STATUS")));
				dataList.add(CommonUtils.checkNull(record.get("AUDIT_STATUS")));
				dataList.add(CommonUtils.checkNull(record.get("DEDUCT_DESC")));
				dataList.add(CommonUtils.checkNull(record.get("IN_TIME")));
				dataList.add(CommonUtils.checkNull(record.get("DOWN_PRODUCT_CODE")));
				dataList.add(CommonUtils.checkNull(record.get("DOWN_PRODUCT_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("VIN")));
				dataList.add(CommonUtils.checkNull(record.get("RESPONSIBILITY_TYPE_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("CLAIM_TYPE_NAME")));
				dataList.add(CommonUtils.checkNull(record.get("RETURN_NO")));
				dataList.add(CommonUtils.checkNull(record.get("BARCODE_NO")));
				list.add(dataList);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件返运明细导出--导出功能");
			logger.error(logonUserBean, e1);
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
	/**
	 * Function：索赔旧件出库查询--新增出库页面汇合 zyw2014-10-21
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 */
	public void addPageListByMix(){
		//取得该用户拥有的产地权限
		String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
		act.setOutData("yieldlys", yieldly);
		act.setOutData("yieldly", request.getParamValue("yieldly"));
		dao=new ClaimOldPartOutStorageDao();
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		PageResult<Map<String, Object>> ps1 =dao.queryPreOutStoreListByMix(companyId,request,Constant.PAGE_SIZE_MAX,getCurrPage());
		if(ps1!=null && ps1.getRecords()!=null && ps1.getRecords().size()>0){
			List<Map<String, Object>> records = ps1.getRecords();
			Integer retrunamount=0;
			Integer noretrunamount=0;
			for (Map<String, Object> map : records) {
				BigDecimal retrun_amount = (BigDecimal) map.get("RETRUN_AMOUNT");
				BigDecimal no_return_amount = (BigDecimal)map.get("NO_RETURN_AMOUNT");
				Integer retrun_amountTemp = retrun_amount.intValue();
				retrunamount+=retrun_amountTemp;
				Integer no_return_amountTemp = no_return_amount.intValue();
				noretrunamount+=no_return_amountTemp;
			}
			act.setOutData("retrun_amount", retrunamount);
			act.setOutData("no_return_amount", noretrunamount);
		}
		sendMsgByUrl(sendUrl(this.getClass(), "addPageListByMix"), "新增出库页面汇合");
	}
	/**
	 * 汇合出库查询
	 */
	public void queryPreOutStoreListByMix(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			dao=new ClaimOldPartOutStorageDao();
			Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
			String param = getParam("page_amount");
			int parseInt = Integer.parseInt(param);
			PageResult<Map<String, Object>> ps =dao.queryPreOutStoreListByMix(companyId,request,parseInt,getCurrPage());
			act.setOutData("ps", ps);
			ActionUtil.setCheckedValueToOutData(act);
			ActionUtil.setCustomPageSizeFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"汇合出库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 出库
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void doChangeByVal(){
		int res=1;
		try {
			 dao=new ClaimOldPartOutStorageDao();
			 String noType = request.getParamValue("noType");//出库方式 (0混合出库/1分单号出库)
			String isHs = request.getParamValue("isHs");//is_house
			String out_flag = request.getParamValue("out_flag");
			String rangeNo = dao.getrangeNo(loginUser, dao, noType, isHs);
			if("force".equals(out_flag)){
				List<Map> list=dao.checkChangeByVal(request,loginUser,rangeNo);
				//去重
				List<Map> list1=new ArrayList<Map>();
				if(list.size()>0){
					JSONObject json=new JSONObject();
					Map tempMap=new HashMap();
					Map tmpMap2 = new HashMap();
					for (Map map : list) {
						tempMap.put( map.get("partCode"), map.get("supplyCode")); 
						tmpMap2.put(map.get("partCode"), map.get("flag"));
					}
					for (Iterator keys = tempMap.keySet().iterator(); keys.hasNext();) {
						 Map temp=new HashMap<String,String>();
			             String key = (String) keys.next();
			             temp.put("partCode", key);
			             temp.put("supplyCode", tempMap.get(key));
			             temp.put("flag", tmpMap2.get(key));
			             list1.add(temp);
			          }
					
					JSONArray array=new JSONArray();
					for (Map map : list1) {
						array.add(map);
					}
					act.setOutData("result", array.toString());
					System.out.println(array.toString());
					res=-1;
				}
			}else{
				res=dao.doChangeByVal(request,loginUser,rangeNo);
			}
			ActionUtil.removeCheckedValueToOutData(act);
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			setJsonSuccByres(res);
		}
		
	}
	
	
	
	/**
	 * 为满足开票服务站
	 * */
	
	public void belowConditionDealer(){
		act.setForword(sendUrl(this.getClass(), "belowConditionDealer"));
	}
	
	
	public void queryBelowConditionDealer(){
		dao=new ClaimOldPartOutStorageDao();
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String,String> params=new HashMap();
		if(dealerCodess!=null){
			String dealerCodes[] =dealerCodess.split(",");
			String codes="";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes=codes+ "'"+dealerCodes[i]+"'";
				if(i!=dealerCodes.length-1){
					codes+=",";
				}
			}
			params.put("dealerCode", codes);
		}
		PageResult<Map<String, Object>> ps=dao.queryBelowConditionDealer(request,getPage(1),getCurrPage(),params);
		act.setOutData("ps", ps);
		
	}
	
	public void exportBelowConditionDealer(){
		dao=new ClaimOldPartOutStorageDao();
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String,String> params=new HashMap();
		if(dealerCodess!=null){
			String dealerCodes[] =dealerCodess.split(",");
			String codes="";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes=codes+ "'"+dealerCodes[i]+"'";
				if(i!=dealerCodes.length-1){
					codes+=",";
				}
			}
			params.put("dealerCode", codes);
		}
		PageResult<Map<String, Object>> ps=dao.queryBelowConditionDealer(request,getPage(1),getCurrPage(),params);
		dao.exportBelowConditionDealer(act,ps);
	}
	
	private Integer getPage(int type){
		Integer page_size=0;
		if(type==1){
			page_size=Constant.PAGE_SIZE;
		}else if(type==2){
			page_size=Constant.PAGE_SIZE_MAX;
		}
		return page_size;
	}
	
	/**
	 * 
	 * @Title: getGroupNameStr
	 * @Description: TODO(取得车型组下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getGroupNameStr() {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(
				Constant.LOGON_USER);
		List<TtAsWrModelGroupPO> seriesList = dao.queryGroupName();
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrModelGroupPO bean = new TtAsWrModelGroupPO();
			bean = (TtAsWrModelGroupPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getWrgroupId() + "\'>"
					+ bean.getWrgroupName() + "</option>";
		}
		return retStr;
	}
	
	public void selectMainTimeForward() {
		ActionContext act = ActionContext.getContext();
		dao=ClaimOldPartOutStorageDao.getInstance();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String treeCode = request.getParamValue("TREE_CODE");
			String id = request.getParamValue("timeId"); // 主页面中的主工时代码
			String timeCode = request.getParamValue("timeCode");
			String modelId = request.getParamValue("MODEL_ID");
			String aCode = request.getParamValue("aCode");
			String yiedly = request.getParamValue("yiedly");
			String printNum = request.getParamValue("printNum");
			String idex = request.getParamValue("idex");
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			TtPartDefinePO partPO = new TtPartDefinePO();
			partPO.setPartOldcode(partCode);
			List<TtPartDefinePO> l = dao.select(partPO);
			if(null != l && l.size()>0){
				act.setOutData("partName", l.get(0).getPartCname());
			}
			//String partName = ;  
			List<Map<String,Object>> list = dao.getLabourDan(/*logonUser.getDealerId(), modelId, logonUser.getOemCompanyId()*/);
			if(list!=null&&list.size()>0){
				act.setOutData("price", "isPrice");//是否维护了工时单价
			}else{
				act.setOutData("price", "noPrice");
			}
			act.setOutData("yiedly", yiedly);
			act.setOutData("roNo", roNo);
			act.setOutData("aCode", aCode);
			act.setOutData("vin", vin);
			act.setOutData("timeId", id);
			act.setOutData("timeCode", timeCode);
			act.setOutData("TREE_CODE", treeCode);
			act.setOutData("modelId", modelId);
			act.setOutData("GROUP", getGroupNameStr());
			act.setOutData("idex", idex);
			act.setOutData("printNum", printNum);
			act.setForword("/jsp/claim/dealerClaimMng/showMainTime2.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	//查看 页面
	public void makerProblem() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MAKER_PROBLEM);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	public void makerProblemQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map params=new HashMap();
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String, Object>> ps=dao.querymakerProblem(request,getPage(1),getCurrPage(),params);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	//更新页面
	public void makerProblemUpdate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MAKER_PROBLEM_UPDATE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	public void makerProblemUpdateQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map params=new HashMap();
			PageResult<Map<String, Object>> ps=dao.querymakerProblem(request,getPage(1),getCurrPage(),params);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void makerProblemDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			PageResult<Map<String, Object>> ps=dao.querymakerProblemDetail(request,getPage(1),getCurrPage(),null);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void makerProblemDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String problem_id=request.getParamValue("problem_id");
			act.setOutData("problem_id", problem_id);
			act.setForword(MAKER_PROBLEM_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * zyw 重新检测问题代码修改2014-2-2
	 */
	@SuppressWarnings("unchecked")
	public void updateRemark() {
		try {
			dao=ClaimOldPartOutStorageDao.getInstance();
			String problem_id=request.getParamValue("problem_id");
			String remark=new String(DaoFactory.getParam(request, "remark").getBytes("iso8859_1"), "GBK");
			TtPartMakerProblemPO problem=new TtPartMakerProblemPO();
			TtPartMakerProblemPO problem1=new TtPartMakerProblemPO();
			problem.setProblemId(Long.parseLong(problem_id));
			List<TtPartMakerProblemPO> list=dao.select(problem);
			if(list.size()>0){
				problem1=list.get(0);
				problem1.setRemark(remark);
				problem1.setStatus(99971001);
				dao.update(problem, problem1);
			}
			act.setOutData("succ", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	//===============================2015.4.22 lj
	/**
	 * 索赔单号查询日志页面跳转
	 */
	@SuppressWarnings("unchecked")
	public void  LogInfoByclaimNo(){
		String claimNo=getParam("claimNo");
		request.setAttribute("claimNo", claimNo);
		act.setForword(LogInfoByclaimNo);
	}
	@SuppressWarnings("unchecked")
	public void  findLogInfoByclaimNo(){
		dao=ClaimOldPartOutStorageDao.getInstance();
		PageResult<Map<String,Object>>  ps=dao.findLogInfoByclaimNo(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", ps);

	}
	/**
	 * 旧件库存汇总查询跳转
	 */
	public void oldPartInt(){
		try{
		act.setForword(OLD_PART_INT);			
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 旧件库存汇总查询
	 */
	public void hzSelect(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.hzSelect(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}				
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 旧件库存单条查询
	 */
	public void dtSelect(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("dealerId", request.getParamValue("dealerId"));
		paraMap.put("IS_MAIN_PART", request.getParamValue("IS_MAIN_PART"));
		paraMap.put("partCode", request.getParamValue("partCode"));
		paraMap.put("partName", request.getParamValue("partName"));
		paraMap.put("CLAIM_SUPPLIER_CODE", request.getParamValue("CLAIM_SUPPLIER_CODE"));
		paraMap.put("CLAIM_SUPPLIER_NAME", request.getParamValue("CLAIM_SUPPLIER_NAME"));
		paraMap.put("PRODUCER_CODE", request.getParamValue("PRODUCER_CODE"));
		paraMap.put("PRODUCER_NAME", request.getParamValue("PRODUCER_NAME"));
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.dtSelect(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}
		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * @description 责任供应商修改
	 * @Date 2017-09-01
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void producerInfoSave(){
//		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));//索赔单ID
		String partId = CommonUtils.checkNull(request.getParamValue("part_Id"));//配件ID
		String venderCode = CommonUtils.checkNull(request.getParamValue("supplyCode"));//责任供应商编码
		String venderName = CommonUtils.checkNull(request.getParamValue("supplyName"));//责任供应商名称
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页数
		try{
			StringBuffer sql = new StringBuffer("") ;
			List<Object> params = new ArrayList<Object>();
            //修改同一索赔单下所有相同配件责任供应商
            sql.setLength(0);
            sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
			sql.append("   SET A.PRODUCER_CODE = ?,\n") ;
			sql.append("       A.PRODUCER_NAME = ?\n") ;
			sql.append(" WHERE A.PART_ID = ?\n") ;//同一配件
			sql.append("   AND A.CLAIM_ID = ?\n") ;//同一索赔单
			sql.append("   AND A.is_main_code = "+Constant.IF_TYPE_YES+"\n") ;//主因件
			params.clear();
			params.add(venderCode);
			params.add(venderName);
			params.add(partId);
			params.add(claimId);
            dao.update(sql.toString(), params);
            	//修改同一索赔单下主因件对应所有次因件责任供应商
            	sql.setLength(0);
                sql.append("UPDATE TT_AS_WR_RETURNED_ORDER_DETAIL A\n") ;
    			sql.append("   SET A.PRODUCER_CODE = ?,\n") ;
    			sql.append("       A.PRODUCER_NAME = ?\n") ;
    			sql.append(" WHERE A.MAIN_PART_CODE = ?\n") ;//主因件
    			sql.append("   AND A.CLAIM_ID = ?\n") ;//同一索赔单
    			params.clear();
    			params.add(venderCode);
    			params.add(venderName);
    			params.add(partId);
    			params.add(claimId);
                dao.update(sql.toString(), params);
            /*act.setOutData("curPage", curPage);
        	act.setOutData("code", "succ");*/
			act.setOutData("msg", "0");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
            act.setOutData("curPage", curPage);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	
}