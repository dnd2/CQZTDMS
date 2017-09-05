package com.infodms.dms.actions.sales.storage.balancemanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.balancemanage.SalesBalanceApplyDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtSalesBalApplyPO;
import com.infodms.dms.po.TtSalesBalFilePO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.service.balancemanage.SalesBalanceService;
import com.infodms.dms.service.balancemanage.SalesBalanceServiceImpl;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 运费发票补录
 * @author shuyh
 *
 */
public class SalesBalanceSuppply{
	public Logger logger = Logger.getLogger(SalesBalanceSuppply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SalesBalanceApplyDao reDao = SalesBalanceApplyDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final String supplyInitUrl = "/jsp/sales/storage/balance/billBalSupply.jsp";
	private final String modifyBalUrl = "/jsp/sales/storage/balance/balInvoiceSupply.jsp";
	private final String payInitUrl = "/jsp/sales/storage/balance/billBalPayment.jsp";
	private final String QueryInitUrl = "/jsp/sales/storage/balance/billBalQuery.jsp";
	/**
	 * 补录初始化
	 */
	public void supplyInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(supplyInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费发票补录初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发票补录列表查询
	 */
	public void billSupplyList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String applyNo = CommonUtils.checkNull(request.getParamValue("apply_no")); //申请单号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			/******************************页面查询字段end***************************/
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("logiId", logiId);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiIdU", (BigDecimal)pmap.get("LOGI_ID"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getBalanceSupplyQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发票补录列表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 补录页面跳转
	 */
	public void toEditBalSupply(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算单ID
			
			act.setOutData("applyId", applyId);
			act.setForword(modifyBalUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补录页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 补录信息添加
	 */
	public void addBalSupply(){
		//ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算单ID
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("invoice_no")); //发票号
			FileObject[] uploadFile = request.getParamObjects("uploadFile");//获取导入文件
			if(uploadFile!=null&&uploadFile.length>0){
				for(int i=0;i<uploadFile.length;i++){
					FileObject file=uploadFile[i];
					if(file.getLength() > 1024*1000*5)
					{
						throw new Exception("上传文件限制大小为5M，服务器资源有限，请节约使用！");
					}
					String fileName = file.getFileName();//获取文件名
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
					FileStore store = FileStore.getInstance();
					// 上传到文件服务器并获取文件ID
					String fileid = store.write(file.getFileName(), file.getContent());
					// 通过文件ID获取文件URL
					String fileUrl = store.getDomainURL(fileid);
					
					//向补录文件表插入数据
					//TtSalesBalFilePO po=new TtSalesBalFilePO();
					FsFileuploadPO po=new FsFileuploadPO();
					po.setFjid(Long.parseLong(SequenceManager.getSequence("")));
					po.setYwzj(Long.parseLong(applyId));
					po.setFilename(fileName);
					po.setFileurl(fileUrl);
					po.setFileid(fileid);
					po.setStatus(Constant.STATUS_ENABLE);
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());
					reDao.insert(po);
				}
				//更新发票号至结算表中
				TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
				tsb.setApplyId(Long.parseLong(applyId));
				TtSalesBalApplyPO tsb2=new TtSalesBalApplyPO();
				tsb2.setInvoiceNo(invoiceNo);
				tsb2.setUpdateBy(logonUser.getUserId());
				tsb2.setUpdateDate(new Date());
				tsb2.setStatus(Constant.BAL_ORDER_STATUS_04);//已补录
				reDao.update(tsb, tsb2);
				//act.setOutData("returnValue", "1");
				supplyInit();
			}else{
				act.setOutData("eMsg", "上传文件为空");
				return;
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补录信息添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 付款初始化
	 */
	public void paymentInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(payInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算付款初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 付款列表查询
	 */
	public void billPayList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String applyNo = CommonUtils.checkNull(request.getParamValue("apply_no")); //申请单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("invice_no")); //发票号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			/******************************页面查询字段end***************************/
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiId", logiId);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getBalancePayQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发票补录列表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 确认付款
	 */
	public void payConfirmBill(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算单ID
			TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
			tsb.setApplyId(Long.parseLong(applyId));
			TtSalesBalApplyPO tsb2=new TtSalesBalApplyPO();
			tsb2.setUpdateBy(logonUser.getUserId());
			tsb2.setUpdateDate(new Date());
			tsb2.setStatus(Constant.BAL_ORDER_STATUS_05);//已付款
			reDao.update(tsb, tsb2);
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"确认付款");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算查询初始化
	 */
	public void balanceQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(QueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算查询列表
	 */
	public void billQueryList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String applyNo = CommonUtils.checkNull(request.getParamValue("apply_no")); //申请单号
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("invice_no")); //发票号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			String status = CommonUtils.checkNull(request.getParamValue("status")); //状态
			String balNo = CommonUtils.checkNull(request.getParamValue("bal_no")); //挂账单号
			String isChange = CommonUtils.checkNull(request.getParamValue("isChange")); //是否调整
			
			String common = CommonUtils.checkNull(request.getParamValue("common")); //操作标记，1表示导出
			/******************************页面查询字段end***************************/
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("invoiceNo", invoiceNo);
			map.put("logiId", logiId);
			map.put("status", status);
			map.put("balNo", balNo);
			map.put("isChange", isChange);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiIdU", (BigDecimal)pmap.get("LOGI_ID"));
			if("1".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getBalanceListQueryExp(map);
				String [] head={"结算单号","运费发票号","承运商","运输总量", "挂账合计", "补款金额","扣款金额",  "其他金额", "结算金额", "状态"};
				String [] cols={"APPLY_NO","INVOICE_NO","LOGI_NAME","BAL_COUNT","BAL_AMOUNT","SUPPLY_MONEY","DEDUCT_MONEY","OTHER_MONEY","SUM_AMOUNT","STATUS_TXT"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "结算单查询列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getBalanceListQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算列表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
