package com.infodms.dms.actions.claim.oldPart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimDeduceDetailListBean;
import com.infodms.dms.bean.ClaimDeduceOldPartDetailBean;
import com.infodms.dms.bean.ClaimDeductOtherItemListBean;
import com.infodms.dms.bean.ClaimLabourItemListBean;
import com.infodms.dms.bean.ClaimOldPartDeduceListBean;
import com.infodms.dms.bean.ClaimOldPartDeductListBean;
import com.infodms.dms.bean.DeductClaimInfoBean;
import com.infodms.dms.bean.DeductVinInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeduceManagerDao;
import com.infodms.dms.exception.BizException;
//import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--索赔抵扣功能
 * 作者：  赵伦达
 */
public class ClaimOldPartDeduceManager {
	public Logger logger = Logger.getLogger(ClaimOldPartStorageManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private ClaimOldPartDeduceManagerDao dao = null;
	private ResponseWrapper response =null;
	
	//url导向
	private final String queryOldPartDeduceListUrl = "/jsp/claim/oldPart/queryOldPartDeduceList.jsp";
	private final String queryOldPartDeduceDetailUrl = "/jsp/claim/oldPart/queryOldPartDeduceDetail.jsp";
	private final String queryOldPartDeduceUrl = "/jsp/claim/oldPart/deduceOldPart.jsp";
	/**
	 * Function：索赔旧件抵扣--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-21
	 */
	public void queryListPage(){
		try {
			act=ActionContext.getContext();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(queryOldPartDeduceListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--条件查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-21
	 */
	@SuppressWarnings("unchecked")
	public void queryOldPartDeduceList(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			//取得该用户拥有的产地权限
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());
			String yieldly = request.getParamValue("yieldly");//查询条件-产地
			
			Map params=new HashMap<String,String>();
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			params.put("company_id",companyId);
			params.put("dealerCode", request.getParamValue("dealerCode"));
			params.put("dealerName", request.getParamValue("dealerName"));
			params.put("back_order_no", request.getParamValue("back_order_no"));
			params.put("create_start_date", request.getParamValue("create_start_date"));
			params.put("create_end_date", request.getParamValue("create_end_date"));
			params.put("report_start_date", request.getParamValue("report_start_date"));
			params.put("report_end_date", request.getParamValue("report_end_date"));
			params.put("store_start_date", request.getParamValue("store_start_date"));
			params.put("store_end_date", request.getParamValue("store_end_date"));
			params.put("transport_type", request.getParamValue("transport_type"));
			params.put("yieldlys", yieldlys);
			params.put("yieldly", yieldly);
			
			PageResult<ClaimOldPartDeduceListBean> ps=dao.getDeduceByConditionList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			//act.setForword(queryOldPartDeduceListUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--条件查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--明细查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-21
	 */
	@SuppressWarnings("unchecked")
	public void searchDetailInfo(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("return_id", request.getParamValue("RETURN_ID"));
			// 处理当前页
			@SuppressWarnings("unused")
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//获得抵扣的详细信息
			ClaimDeduceOldPartDetailBean detailBean=dao.getClaimApplyDetailInfo(params);
			request.setAttribute("detailBean", detailBean);
			//获得抵扣的列表信息
			//PageResult<ClaimDeduceDetailListBean> ps=dao.getDeduceClaimInfoList(params, curPage, Constant.PAGE_SIZE);
			//act.setOutData("ps", ps);
			act.setForword(queryOldPartDeduceDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--明细查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchDetailList(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("return_id", request.getParamValue("RETURN_ID"));
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			//获得抵扣的列表信息
			PageResult<ClaimDeduceDetailListBean> ps=dao.getDeduceClaimInfoList(params, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(queryOldPartDeduceDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--明细查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--按回运单展现
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-22
	 */
	@SuppressWarnings("unchecked")
	public void deducePreViewInfo(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("claim_back_id", request.getParamValue("claim_back_id"));//回运清单主键
			params.put("claim_no", request.getParamValue("claim_no"));//索赔单号
			params.put("claim_id", request.getParamValue("claim_id"));//索赔单主键
			params.put("vin", request.getParamValue("vin"));//vin
			params.put("company_id",companyId);
			//获得索赔单明细
			DeductClaimInfoBean claimInfoBean=dao.getDeductClaimInfo(params);
			request.setAttribute("claimInfoBean", claimInfoBean);
			//通过vin获得车辆信息
			DeductVinInfoBean vinBean=dao.getVinInfo(params);
			request.setAttribute("vinBean", vinBean);
			//获得索赔单申请内容
			//TtAsWrApplicationExtPO tawep=dao.queryApplicationDetailById(params);
			//request.setAttribute("application", tawep);
			//获得索赔配件抵扣列表
			List<ClaimOldPartDeductListBean> deductPartList=dao.getClaimOldPartDeductInfoList(params);
			act.setOutData("deductPartList", deductPartList);
			//获得索赔工时列表
			List<ClaimLabourItemListBean> deductHourList=dao.getClaimLabourItemList(params);
			act.setOutData("deductHourList", deductHourList);
			//获得索赔其他项目列表
			List<ClaimDeductOtherItemListBean> deductOtherList=dao.getClaimDeductOtherItemList(params);
			act.setOutData("deductOtherList", deductOtherList);
			request.setAttribute("claim_back_id",request.getParamValue("claim_back_id"));
			act.setForword(queryOldPartDeduceUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--按回运单展现");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--查询索赔单是否已结算
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-25
	 */
	@SuppressWarnings("unchecked")
	public void isBalanceOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap();
			params.put("claim_no", request.getParamValue("claim_no"));
			params.put("claim_id", request.getParamValue("claim_id"));
			String retCode=dao.getIsBalanceFlag(params);
			act.setOutData("isBalance", retCode);//retCode为1代表已结算，0代表为未结算
			act.setOutData("claim_back_id", request.getParamValue("claim_back_id"));
			act.setOutData("claim_no", request.getParamValue("claim_no"));
			act.setOutData("vin", request.getParamValue("vin"));
			act.setOutData("claim_id", request.getParamValue("claim_id"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--查询索赔单是否已结算");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--抵扣操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-24
	 */
	public void deductOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			String retCode=dao.deDuctDataOper(request,logonUserBean.getUserId(),companyId,act);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
 			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--抵扣操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣--导出功能
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-29
	 */
	@SuppressWarnings("unchecked")
	public void toExcel(){
		OutputStream os = null;
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			dao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("return_id", request.getParamValue("RETURN_ID"));
			response=act.getResponse();
			//获得抵扣的详细信息
			ClaimDeduceOldPartDetailBean detailBean=dao.getClaimApplyDetailInfo(params);
			
			
			//导出的文件名
			String fileName = "索赔旧件抵扣明细"+request.getParamValue("RETURN_ID")+".csv";
			
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			StringBuffer beanInfo=new StringBuffer();
			beanInfo.append("经销商代码："+detailBean.getDealer_code()+",");
			beanInfo.append("经销商简称："+detailBean.getDealer_name()+",");
			beanInfo.append("所属区域："+detailBean.getAttach_area()+",");
			beanInfo.append("货运方式："+detailBean.getTransport_desc()+",");
			beanInfo.append("回运清单号："+detailBean.getReturn_no()+",");
			beanInfo.append("建单日期："+detailBean.getCreate_date()+",");
			beanInfo.append("提报日期："+detailBean.getReport_date()+",");
			beanInfo.append("入库日期："+detailBean.getStore_date()+",");
			beanInfo.append("索赔单提交日期(年/月)："+detailBean.getWr_start_date()+",");
			beanInfo.append("装箱数量："+detailBean.getParkage_amount()+",");
			beanInfo.append("差异数："+detailBean.getDiff_amount()+",");
			beanInfo.append("审批人："+detailBean.getApprove_name());
			List<List<Object>> list = new LinkedList<List<Object>>();
			//表头
			List<Object> titleList = new LinkedList<Object>();
			titleList.add("序号");
			titleList.add("索赔单申请号");
			titleList.add("VIN");
			titleList.add("需回运数");
			titleList.add("入库数");
			titleList.add("差异数");
			list.add(titleList);
			List<Map<String, Object>> results = dao.getDeductExportList(params);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				List<Object> dataList = new LinkedList<Object>();
				dataList.add(i+1);
				dataList.add(CommonUtils.checkNull(record.get("CLAIM_NO")));
				dataList.add(CommonUtils.checkNull(record.get("VIN")));
				dataList.add(CommonUtils.checkNull(record.get("N_RETURN_AMOUNT")));
				dataList.add(CommonUtils.checkNull(record.get("SIGN_AMOUNT")));
				dataList.add(CommonUtils.checkNull(record.get("DIFF_AMOUNT")));
				list.add(dataList);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsvExt(4, beanInfo.toString(), ",", list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣--导出功能");
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
}
