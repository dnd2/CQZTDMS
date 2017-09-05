package com.infodms.dms.actions.claim.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimListBean;
import com.infodms.dms.bean.TcCodeBean;
import com.infodms.dms.bean.TtAsWrApplicationBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.bean.TtAsWrWinterMaintenBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TmClaimContactPhonePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmParameterPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrApplicationPartPO;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infodms.dms.po.TtAsWrNegativeInventoryPO;
import com.infodms.dms.po.TtAsWrNetitemExtPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrWinterMaintenDealerPO;
import com.infodms.dms.po.TtAsWrWinterMaintenPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.service.ClaimBillStatusTrackService;
import com.infodms.dms.service.impl.ClaimBillStatusTrackServiceImpl;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.action.ClaimAction;
import com.infodms.yxdms.constant.MyConstant;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 * 
 * @ClassName: ClaimBillStatusTrack
 * @Description: TODO(索赔单状态跟踪)
 * @author wangchao
 * @date Jun 23, 2010 3:13:56 PM
 * 
 */
public class ClaimBillStatusTrack extends BaseAction {
	
	private Logger logger = Logger.getLogger(ClaimBillStatusTrack.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	private ClaimBillStatusTrackService service=new ClaimBillStatusTrackServiceImpl();
	private final String CLAIM_BILL_URL = "/jsp/claim/application/claimBillStatusTrack.jsp";// 主页面（查询）
	private final String CLAIM_BILL_URL_DATE = "/jsp/claim/application/claimBillStatusTrackDate.jsp";// 先关时间主页面（查询）
	private final String CLAIM_BILL_DETAIL_URL = "/jsp/claim/application/claimBillStatusDetail.jsp";// 明细页面
	private final String CLAIM_BILL_DETAIL_URL2 = "/jsp/claim/application/claimBillStatusDetail2.jsp";// 明细页面
	private final String CLAIM_BILL_DETAIL_URLNew = "/jsp_new/claim/normalViewDetail.jsp";//一般索赔 新分單明细页面2015-6-3
	private final String keep_fit_manage_view ="/jsp_new/claim/keep_fit_manage_add.jsp";//保养 新分單明细页面2015-6-4
	private final String pdi_manage_view ="/jsp_new/claim/pdi_manage_add.jsp";// pdi新分單明细页面2015-6-4
	private final String CLAIM_REIN_BILL_DETAIL_URL = "/jsp/claim/application/claimReinBillStatusDetail.jsp";// 明细页面
	private final String CLAIM_BILL_DETAIL_WC_URL = "/jsp/claim/application/claimBillStatusDetailWC.jsp";// 明细页面（微车）
	private final String CLAIM_BILL_DETAIL_URL_TOW = "/jsp/claim/application/claimBillStatusDetail_TOW.jsp";// 下端查询索赔明细页面
	private final String CLAIM_BILL_DETAIL_URL_TOW_ERROR = "/jsp/claim/application/claimBillStatusDetail_TOW_error.jsp";// 下端查询索赔错误页面
	private final String WINTER_MAINTEN_URL = "/jsp/claim/application/winterMainten.jsp";
	private final String WINTER_MAINTEN_ADD_URL = "/jsp/claim/application/winterMaintenAdd.jsp";
	private final String WINTER_MAINTEN_DETAIL_URL = "/jsp/claim/application/winterMaintenDetail.jsp";
	private final String NEGATIVE_STOCK_URL = "/jsp/claim/application/negativeStock.jsp";
	private final String TIME_PARAMETER = "/jsp/claim/application/timeParameter.jsp";//时销参数维护
	private final String ADD_TIME_PARAMETER = "/jsp/claim/application/timeParameterAdd.jsp";//时销参数维护
	private static final String UPDATE_TIME_PARAMETER =  "/jsp/claim/application/timeParameterUpdate.jsp";//时销参数维护
	private DeCommonDao commonDao = DeCommonDao.getInstance();

	
	/**
	 * 分页方法 begin
	 * @return
	 */
	protected Integer getCurrPage() {
		return request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
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
	 * @Title: claimBillForward
	 * @Description: TODO(索赔状态查询跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBillForward() {
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());
			List<TmBusinessAreaPO> list = dao.select(new TmBusinessAreaPO());
			act.setOutData("loginUser", loginUser.getName());
			act.setOutData("yieldly", yieldly);
			act.setOutData("yieldlys", list);
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenForward() {
		try {
			act.setForword(WINTER_MAINTEN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季保养维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenDetailForward() {
		try {
			String id = request.getParamValue("id");
			List<Map<String, Object>> winterDetail = dao.queryWinterDetail(id);
			act.setOutData("winterDetail", winterDetail);
			act.setForword(WINTER_MAINTEN_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季保养维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void negativeStockForward() {
		try {
			act.setForword(NEGATIVE_STOCK_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "负库存维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void negativeStockQuery() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
					
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			
			map.put("dealerCode", dealerCode);
			map.put("dealerName", dealerName);
			
			PageResult<Map<String, Object>> ps = dao.query2(map,Constant.PAGE_SIZE_MIDDLE, curPage);
			
			act.setOutData("ps", ps);
			act.getSession().set("num1", getCurrPage());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "负库存维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void insertOrUpdateStock() {
		try {
			
			String id = request.getParamValue("id");
//			String[] idList = id.split(",");
			String dealerId = request.getParamValue("dealerId");
			String partNum = request.getParamValue("partNum");
//			String mutilPartNum = request.getParamValue("mutilPartNum");
			TtAsWrNegativeInventoryPO po = new TtAsWrNegativeInventoryPO();
			TtAsWrNegativeInventoryPO po2 = new TtAsWrNegativeInventoryPO();
			
//			if (idList.length >= 0) {
//				for (int i=0;i<idList.length;i++) {
//					if (idList[i].split("@").length > 1) {
//						id = idList[i].split("@")[0];
//						dealerId = idList[i].split("@")[1];
//					} else {
//						id = idList[i];
//					}
//					if (partNum == null) {
//						partNum = mutilPartNum;
//					}
					if (id == null || ("null").equals(id)) {
						po.setId(Long.parseLong(SequenceManager.getSequence("")));
						po.setDealerId(Long.parseLong(dealerId));
						po.setPartNum(Long.parseLong(partNum));
						po.setCreateDate(new Date());
						dao.insert(po);
					} else {
						po.setId(Long.parseLong(id));
						po2.setPartNum(Long.parseLong(partNum));
						dao.update(po, po2);
					}
//				}
//			} else {
//				act.setOutData("result", "fail");
//			}
			
			act.setOutData("num1", act.getSession().get("num1"));
			act.setOutData("result", "success");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "负库存维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void multInsertOrUpdateStock() {
		try {
			
			String id = request.getParamValue("id");
			String[] idList = id.split(",");
//			String dealerId = request.getParamValue("dealerId");
//			String partNum = request.getParamValue("partNum");
			String mutilPartNum = request.getParamValue("mutilPartNum");
			String dealerId = "";
			if (idList.length >= 0) {
				for (int i=0;i<idList.length;i++) {
					TtAsWrNegativeInventoryPO po = new TtAsWrNegativeInventoryPO();
					TtAsWrNegativeInventoryPO po2 = new TtAsWrNegativeInventoryPO();
						id = idList[i].split("@")[0];
						dealerId = idList[i].split("@")[1];
					if (id == null || ("null").equals(id)) {
						po.setId(Long.parseLong(SequenceManager.getSequence("")));
						po.setDealerId(Long.parseLong(dealerId));
						po.setPartNum(Long.parseLong(mutilPartNum));
						po.setCreateDate(new Date());
						dao.insert(po);
					} else {
						po.setId(Long.parseLong(id));
						po2.setPartNum(Long.parseLong(mutilPartNum));
						dao.update(po, po2);
					}
				}
			} else {
				act.setOutData("result", "fail");
			}
			
			act.setOutData("num1", act.getSession().get("num1"));
			act.setOutData("result", "success");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "负库存维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenQuery() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
					
			String amount = request.getParamValue("amount");
			String startDate = request.getParamValue("start_date");
			String endDate = request.getParamValue("end_date");
			
			map.put("amount", amount);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			
			PageResult<TtAsWrWinterMaintenBean> ps = dao.queryWinterMainten(
					loginUser, map, null, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季保养维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenAdd() {
		try {
			List<Map<String, Object>> winterDetail = new ArrayList();
			act.setOutData("winterDetail", winterDetail);
			act.setOutData("mod", "false");
			act.setForword(WINTER_MAINTEN_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季保养维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenDealerQuery() {
		try {
			String dealerCode = request.getParamValue("DEALER_CODE");
			String[] code = dealerCode.split(",");
			PageResult<Map<String, Object>> ps = dao.query1(code,5000, getCurrPage());
			String[] dealerId = new String[ps.getRecords().size()];
			for (int i=0;i<ps.getRecords().size();i++) {
				dealerId[i] = ps.getRecords().get(i).get("DEALER_ID").toString();
			}
			act.setOutData("dealerId", dealerId);
			act.setOutData("dealerCodeBack", dealerCode);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季保养维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void winterMaintenDealerAdd() {
		try {
			String amount = request.getParamValue("amount");
			String startDate = request.getParamValue("start_date");
			String endDate = request.getParamValue("end_date");
			String dealerId = request.getParamValue("dealerId");
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenDealerPO po2 = new TtAsWrWinterMaintenDealerPO();
			Long pkId = DaoFactory.getPkId();
			po.setId(pkId);
			po.setAmount(Double.valueOf(amount));
			po.setStartDate(Utility.getDate(startDate, 1));
			po.setEndDate(Utility.getDate(endDate, 1));
			po.setCreateDate(new Date());
			po.setStatus(Constant.SERVICEACTIVITY_STATUS_01);
			po.setCreateBy(loginUser.getName());
			dao.insert(po);
			po2.setId(pkId);
			String[] code = dealerId.split(",");
			for (int i=0;i<code.length;i++) {
				po2.setDealerId(Long.parseLong(code[i]));
				dao.insert(po2);
			}
			act.setOutData("result", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季维护新增");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void winterMaintenModForward() {
		try {
			String id = request.getParamValue("id");
			String status = request.getParamValue("status");
			List<Map<String, Object>> winterDetail = dao.queryWinterDetail(id);
			act.setOutData("winterDetail", winterDetail);
			act.setOutData("mod", "true");
			act.setOutData("status", status);
			act.setForword(WINTER_MAINTEN_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季维护修改");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void publishWinter() {
		try {
			String id = request.getParamValue("id");
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenPO po2 = new TtAsWrWinterMaintenPO();
			po.setId(Long.parseLong(id));
			po2.setStatus(Constant.SERVICEACTIVITY_STATUS_02);
			dao.update(po, po2);
			act.setOutData("result", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季维护发布");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void winterMaintenMod() {
		try {
			String id = request.getParamValue("id");
			String amount = request.getParamValue("amount");
			String startDate = request.getParamValue("start_date");
			String endDate = request.getParamValue("end_date");
			String dealerId = request.getParamValue("dealerId");
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenPO po2 = new TtAsWrWinterMaintenPO();
			TtAsWrWinterMaintenDealerPO pod = new TtAsWrWinterMaintenDealerPO();
			po.setId(Long.parseLong(id));
			po2.setAmount(Double.valueOf(amount));
			po2.setStartDate(Utility.getDate(startDate, 1));
			po2.setEndDate(Utility.getDate(endDate, 1));
			dao.update(po, po2);
			pod.setId(Long.parseLong(id));
			dao.delete("delete from TT_AS_WR_WINTER_MAINTEN_DEALER where id=" + id, null);
			
			String[] code = dealerId.split(",");
			for (int i=0;i<code.length;i++) {
				pod.setDealerId(Long.parseLong(code[i]));
				dao.insert(pod);
			}
			act.setOutData("result", "success");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季维护修改");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void delWinter() {
		try {
			String id = request.getParamValue("id");
			TtAsWrWinterMaintenPO po = new TtAsWrWinterMaintenPO();
			po.setId(Long.parseLong(id));
			dao.delete(po);
			dao.delete("delete from TT_AS_WR_WINTER_MAINTEN_DEALER where id=" + id, null);
			act.setOutData("result", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "冬季维护删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	

	public void claimBillForwardDate() {
		try {
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			List<TmBusinessAreaPO> areaPO = commonUtilDao.getYieldly();
			act.setOutData("areaPO", areaPO);
			act.setForword(CLAIM_BILL_URL_DATE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "相关时间");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void applicationQueryupdate() {
		try {

			String POST_DATE = request.getParamValue("POST_DATE");
			String TICK_DATE = request.getParamValue("TICK_DATE");
			String PROOF_DATE = request.getParamValue("PROOF_DATE");
			String[] checkIds = request.getParamValues("checkId");
			for (String checkId : checkIds) {
				TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
				applicationPO.setId(Long.parseLong(checkId));
				TtAsWrApplicationPO wrApplicationPO = new TtAsWrApplicationPO();
				if (Utility.testString(POST_DATE))
					wrApplicationPO.setPostDate(Utility.getDate(POST_DATE, 1));
				if (Utility.testString(TICK_DATE))
					wrApplicationPO.setTickDate(Utility.getDate(TICK_DATE, 1));
				if (Utility.testString(PROOF_DATE))
					wrApplicationPO
							.setProofDate(Utility.getDate(PROOF_DATE, 1));
				dao.update(applicationPO, wrApplicationPO);

			}
			act.setOutData("yes", "添加成功");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "加入失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void applicationQuerydate() {
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String claimNo = request.getParamValue("claimNo");
			String modelCode = request.getParamValue("modelCode");
			String vin = request.getParamValue("vin");
			String dealer_id = request.getParamValue("dealer_id");
			String CLAIM_TYPE = request.getParamValue("CLAIM_TYPE");
			String dealerName = request.getParamValue("dealerName");
			String yieldly = request.getParamValue("yieldly");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String claimStatus = request.getParamValue("claimStatus");
			String startDate2 = request.getParamValue("startDate2");
			String endDate2 = request.getParamValue("endDate2");
			TtAsWrApplicationBean applicationPO = new TtAsWrApplicationBean();
			applicationPO.setClaimNo(claimNo);
			applicationPO.setModelCode(modelCode);
			applicationPO.setVin(vin);
			if (dealer_id != null && dealer_id.length() > 0) {
				applicationPO.setDealerId(dealer_id);
			}
			applicationPO.setDealerName(dealerName);
			if (CLAIM_TYPE != null && CLAIM_TYPE.length() > 0) {
				applicationPO.setClaimType(CLAIM_TYPE);
			}
			applicationPO.setYieldly(yieldly);
			if (claimStatus != null && claimStatus.length() > 0) {
				applicationPO.setLastStatus(claimStatus);
			}
			applicationPO.setStartDate(startDate);
			applicationPO.setEndDate(endDate);
			applicationPO.setStartDate2(startDate2);
			applicationPO.setEndDate2(endDate2);
			PageResult<Map<String, Object>> ps = dao.querydate(loginUser,
					applicationPO, null, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "相关时间");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: applicationQuery
	 * @Description: TODO(索赔申请单查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationQuery() {

		// String dealerId = loginUser.getDealerId();
		StringBuffer con = new StringBuffer();
		Date date = new Date();
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String dealerCode = request.getParamValue("DEALER_CODE");
			String dealerName = request.getParamValue("DEALER_NAME");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimNo = request.getParamValue("CLAIM_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String approveDate = request.getParamValue("approve_date");// 审核时间
			String approveDate2 = request.getParamValue("approve_date2");
			String status = request.getParamValue("STATUS");
			String person = request.getParamValue("PERSON");
			String yieldly = request.getParamValue("YIELDLY");// 查询条件--产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser
					.getPoseId());// 该用户拥有的产地权限
			String isImport = request.getParamValue("is_import");

			List params = new LinkedList();
			// map.put("dealerId", dealerId);
			// map.put("claimNo", claimNo);
			map.put("dealerCode", dealerCode);
			map.put("dealerName", dealerName);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());
			map.put("track", "track"); // 状态跟踪查询
			map.put("yieldly", yieldly);
			map.put("yieldlys", yieldlys);
			map.put("person", person);
			map.put("approveDate", approveDate);
			map.put("approveDate2", approveDate2);
			map.put("claimNo", claimNo);
			map.put("isImport", isImport);
			PageResult<TtAsWrApplicationExtBean> ps = dao.queryApplication1(
					loginUser, map, params, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 车厂索赔单查询 zyw 2014-8-8 重构 删减代码80%
	 */
	public void applicationQuery2() {
		try{
		PageResult<TtAsWrApplicationExtBean> ps = dao.queryApplication2(request,loginUser, ActionUtil.getPageSize(request), getCurrPage());
		act.setOutData("ps", ps);
		ActionUtil.setCustomPageSizeFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: claimBillDetailForward
	 * @Description: TODO(索赔状态跟踪明细页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimBillDetailForward() {
		try {
			String id = request.getParamValue("ID");
			TtAsWrApplicationPO a = new TtAsWrApplicationPO();
			a.setId(Long.valueOf(id));
			a= (TtAsWrApplicationPO) dao.select(a).get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Long time = a.getCreateDate().getTime();
			Long time1 = sdf.parse(MyConstant.onlineDate).getTime();// 新分单上线时间节点
			if(time1>time && (a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_02.toString())||a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_11.toString()))){
				 getOlddate(a);
				act.setForword(CLAIM_BILL_DETAIL_URL);
			}else if (time1<time) {//新分单
				if ( (a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_02.toString()))) {
					act.setOutData("type", "view");
					keepFitView(request);//保养
				}else if (a.getClaimType().toString().equalsIgnoreCase(Constant.CLA_TYPE_11.toString())) {//pdi
					act.setOutData("type", "view");
					pdiView();
				}else {
					act.setOutData("type", "view");
					normalView();//一般索赔
				}
			}else{
				 getOlddate(a);
				act.setForword(CLAIM_BILL_DETAIL_URL2);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	private void getOlddate(TtAsWrApplicationPO a){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long provinceId = null;
		String phone = "";
		RequestWrapper request = act.getRequest();
		TtAsActivityPO activity = new TtAsActivityPO();
		String type = request.getParamValue("type");
		String id = request.getParamValue("ID");
		List<Map<String, Object>> mainCodeList = dao.queryMainList(id);//得到主因件集合
		TtAsWrApplicationExtPO tawep = dao.queryApplicationById(id);
		List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
		List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
		List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
		List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
		List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);//审核授权信息
		
		List<Map<String, Object>> accessoryDtlList = dao.getclaimAccessoryDtl(a.getClaimNo());
		act.setOutData("accessoryDtlList", accessoryDtlList);
		
		//====================zyw 2014-8-4 补偿费查询明细
		List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoneyAPP(a.getClaimNo());
		if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
			act.setOutData("compensationMoneyList", compensationMoneyList);
		} 
		//==============================
		TmDealerPO d =  new TmDealerPO();
		d.setDealerCode(tawep.getDealerCode());
		
		List<TmDealerPO> resList = dao.select(d);
		if (resList != null && resList.size() > 0) {
			TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
			phone = dealerPO.getPhone();
			provinceId = dealerPO.getProvinceId();
		}
		act.setOutData("application", tawep);
		act.setOutData("ACTIVITY", activity);
		act.setOutData("ID", id);
		act.setOutData("itemLs", itemls);
		act.setOutData("partLs", partls);
		act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
		act.setOutData("mainCodeList",mainCodeList);
		act.setOutData("size",mainCodeList.size());
		act.setOutData("otherLs", otherls);
		act.setOutData("attachLs", attachLs);
		act.setOutData("type", type);
		act.setOutData("phone", phone);
	}
	
	private void pdiView() {
		Map<String,Object> data=service.pdiView(loginUser,request);
		act.setOutData("t", data);
		this.getFile("ID");
		request.setAttribute("type", "view");
		act.setForword(pdi_manage_view);//索赔单PDI
	}
	private void keepFitView(RequestWrapper request) {
		this.findData("view");
		act.setForword(keep_fit_manage_view);//索赔单保养明细
	}
	private void findData(String type) {
		
		List<Map<String,Object>> labours2= service.findLaboursById(request);
		List<Map<String,Object>> parts2= service.findPartsById(request);
		Map<String,Object> data =service.findClaimPoById(request);
		act.setOutData("t", data);
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
		this.getFile("ID");
		request.setAttribute("type", type);
	}
	private void normalView() {
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("select\n" );
		sql.append("to_char( a.create_date,'yyyy-MM-dd hh24:mi' ) sb_date ,\n" );
		sql.append("b.name sb_name\n" );
		sql.append(", to_char(a.report_date,'yyyy-MM-dd hh24:mi') sh_date,\n" );
		sql.append("d.name sh_name,\n" );
		sql.append("to_char(pf.create_date,'yyyy-MM-dd hh24:mi') hy_date,\n" );
		sql.append("pa.name  hy_name,\n" );
		sql.append("to_char(pf.SIGN_DATE,'yyyy-MM-dd hh24:mi') qs_date,\n" );
		sql.append("pb.name  qs_name,\n" );
		sql.append("to_char(pf.IN_WARHOUSE_DATE,'yyyy-MM-dd hh24:mi') rk_date,\n" );
		sql.append("pf.IN_WARHOUSE_NAME rk_name,\n" );
		sql.append("to_char(pc.create_date,'yyyy-MM-dd hh24:mi') kp_date,\n" );
		sql.append("pc.apply_person_name kp_name,\n" );
		sql.append("to_char(pd.COLLECT_TICKETS_DATE,'yyyy-MM-dd hh24:mi') sp_date,\n" );
		sql.append("pe.name sp_name,\n" );
		sql.append("to_char(pd.CHECK_TICKETS_DATE,'yyyy-MM-dd hh24:mi') yp_date,\n" );
		sql.append("pg.name zz_name,\n" );
		sql.append("to_char(pd.TRANSFER_TICKETS_DATE,'yyyy-MM-dd hh24:mi') zz_date,\n" );
		sql.append("ph.name zz_name,\n" );
		sql.append("to_char(pi.create_date,'yyyy-MM-dd hh24:mi') ck_date,\n" );
		sql.append("pj.name  ck_name,\n" );
		sql.append("to_char(phh.audit_date, 'yyyy-mm-dd hh24:mi') tp_date ,\n" );
		sql.append("ppp.name tp_name\n" );
		sql.append("\n" );
		sql.append("from  tc_user b , tt_as_wr_application a left join\n" );
		sql.append("(select\n" );
		sql.append("k.claim_id , max(k.audit_by) audit_by from\n" );
		sql.append(" tt_as_wr_app_audit_detail k group by  k.claim_id  ) c\n" );
		sql.append(" on a.id = c.claim_id\n" );
		sql.append("left join   tc_user d on  d.user_id = c.audit_by\n" );
		sql.append("\n" );
		sql.append("left join  (select aa.claim_id,max(aa.return_id) return_id from   tt_as_wr_old_returned_detail aa group by aa.claim_id ) p   on p.claim_id = a.id\n" );
		sql.append("left join tt_as_wr_old_returned pf on  pf.id = p.return_id\n" );
		sql.append("left join tc_user pa on pa.user_id = pf.create_by\n" );
		sql.append("left join tc_user pb on pb.user_id = pf.sign_person\n" );
		sql.append("left join tt_as_wr_claim_balance pc on pc.start_date < a.report_date and pc.end_date +1 > a.report_date and pc.dealer_id = a.dealer_id\n" );
		sql.append("left join ( select max(COLLECT_TICKETS) COLLECT_TICKETS\n" );
		sql.append(" , max(COLLECT_TICKETS_DATE )COLLECT_TICKETS_DATE ,\n" );
		sql.append(" max(CHECK_TICKETS) CHECK_TICKETS,\n" );
		sql.append(" max(COLLECT_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
		sql.append(" max(TRANSFER_TICKETS ) TRANSFER_TICKETS,\n" );
		sql.append(" max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE , balance_oder  from\n" );
		sql.append(" tt_as_payment  group by balance_oder     ) pd on pd.balance_oder = pc.remark\n" );
		sql.append(" left join tc_user pe on  pe.user_id = pd.COLLECT_TICKETS\n" );
		sql.append(" left join tc_user pg on  pg.user_id = pd.CHECK_TICKETS\n" );
		sql.append(" left join tc_user ph on  ph.user_id = pd.TRANSFER_TICKETS\n" );
		sql.append(" left join  (select claim_no, max(CREATE_DATE) CREATE_DATE , max(CREATE_by ) CREATE_by,max(out_no) out_no from    tt_as_wr_old_out_part group by  claim_no )  pi on pi.claim_no = a.claim_no\n" );
		sql.append(" left join  tc_user pj  on  pj.user_id = pi.CREATE_by\n" );
		sql.append(" left join Tt_As_Wr_Range_Single phh on  phh.out_no = pi.out_no\n" );
		sql.append(" left join tc_user ppp on ppp.user_id = phh.audit_by\n" );
		sql.append("where a.create_by = b.user_id  and  a.id ="+ request.getParamValue("ID"));
		List<Map<String, Object>> pg = dao.pageQuery01(sql.toString(), null, dao.getFunName());
		act.setOutData("pg", pg.get(0));
		request.setAttribute("type", "view");
		request.setAttribute("goBackType", 2);
		this.getAllData();
		act.setForword(CLAIM_BILL_DETAIL_URLNew);//"一般索赔明细页面"
//		sendMsgByUrl("claim", "normalView", "一般索赔明细页面");
	 }
	private void getAllData() {
		Map<String,Object> data =service.findClaimPoById(request);
		Map<String,Object> outrepair=service.findoutrepair(request);
		Map<String,Object> outrepairmoney=service.findoutrepairmoney(request);
		List<Map<String,Object>> labours= service.findLaboursById(request);
		List<Map<String,Object>> parts= service.findPartsById(request);
		Map<String,Object> dataCom =service.findComPoById(request);
		List<Map<String,Object>> dataAcc =service.findAccPoById(request);
		if(data!=null && data.size()>0 ){
			TcCodePO codePO = new TcCodePO();
			codePO.setCodeId(data.get("CLAIM_TYPE").toString());
			List<TcCodePO> CLAIM_TYPE =dao.select(codePO);
			act.setOutData("claim_type",  CLAIM_TYPE.get(0).getCodeId());
		}
		if(checkListNull(labours)){
			act.setOutData("labours", labours);
		}
		if(checkListNull(parts)){
			act.setOutData("parts", parts);
		}
		if(checkListNull(dataAcc)){
			act.setOutData("acc", dataAcc);
		}
		this.getFile("ID");
		act.setOutData("t", data);
		act.setOutData("com", dataCom);
		act.setOutData("o", outrepairmoney);
		act.setOutData("r", outrepair);	
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = service.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	/**
	 * 
	 * @Title: claimReinBillDetailForward
	 * @Description: TODO(索赔状态跟踪明细页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimReinBillDetailForward() {

		try {

			// TtAsActivityPO activity = new TtAsActivityPO();

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			String id = request.getParamValue("ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);
			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);

			/****** add by liuxh 20101125 roduct date *****/
			TmVehiclePO veh = new TmVehiclePO();
			veh.setVin(tawep.getVin());

			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/***** add by liuxh 20131108判断车架号不能为空 *****/

			TmVehiclePO vehSel = new TmVehiclePO();
			List<TmVehiclePO> list = auditingDao.select(veh);
			if (list != null && list.size() > 0) {
				vehSel = list.get(0);
			}

			/****** add by liuxh 20101125 product date *****/

			// zhumingwei 2011-03-04
			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			// 取的当前需要审核的级别
			String authCode = tawep.getAuthCode();
			String authLevel = "--";
			if (authCode != null) {
				TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
				conditionPO.setApprovalLevelCode(authCode);
				List<TtAsWrAuthinfoPO> authLevelList = auditingDao
						.select(conditionPO);
				if (authLevelList != null && authLevelList.size() > 0) {
					TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
					authLevel = CommonUtils.checkNull(authInfoPO
							.getApprovalLevelName());
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			if (yieldly != null) {
				TmBusinessAreaPO p = CommonUtils.getName(yieldly);
				if (p != null && p.getAreaName() != null)
					yieldlyName = p.getAreaName();
			}
			tawep.setYieldlyName(yieldlyName);
			
			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			// List feeType =
			// dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型列表
			// act.setOutData("OTHERFEE", getOtherfeeStr());

			String roNo = tawep.getRoNo();
			// 2010-08-18 XZM 屏蔽页面不用显示质损相关信息，先不用显示，同时屏蔽页面隐藏域信息
			// act.setOutData("BUSINESS_CHNG_CODE_01",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_01));
			// // 质损程度
			// act.setOutData("BUSINESS_CHNG_CODE_02",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_02));//
			// 质损区域
			// act.setOutData("BUSINESS_CHNG_CODE_03",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_03));//
			// 质损类型
			// act.setOutData("BUSINESS_CHNG_CODE_04",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04));//
			// 故障代码
			act.setOutData("application", tawep);

			// 如果是外出维修，查询外出维修相关信息
			if (Constant.CLA_TYPE_09.toString().equals(
					tawep.getClaimType().toString())) {
				List outList = dao.getOutInfo(id);
				if (outList.size() > 0) {
					act.setOutData("outInfo",
							(TtAsWrOutrepairPO) outList.get(0));
				}
			}
			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl01(tawep.getClaimNo());
			act.setOutData("accessoryDtlList", accessoryDtlList);
			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}
			// ====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao
					.getCompensationMoneyAPPbyRONO(roNo);
			if (compensationMoneyList != null
					&& compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			}
			// ==============================
			act.setOutData("attachLs", attachLs);
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("authLevel", authLevel);// 需要审核的级别

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/******** add by liuxh 20101125 *******/
			act.setOutData("vehSel", vehSel);// add vehcel
			act.setForword(CLAIM_REIN_BILL_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: claimBillDetailForward_tow
	 * @Description: TODO(索赔状态跟踪明细页面跳转供下端查询使用) YH 2011.9.20
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillDetailForward_tow() {

		try {

			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			String RO_NO = request.getParamValue("RO_NO");
			String EntityCode = request.getParamValue("rpcDealerCode");
			Map<String, Object> map = commonDao.getDcsDealerCode(EntityCode);
			String dealerCode = map.get("DEALER_CODE").toString();

			String NEW_RO_NO = dealerCode + RO_NO.replace("RO", "DR");

			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			String id = "";
			TtAsWrApplicationPO tp = new TtAsWrApplicationPO();
			tp.setRoNo(NEW_RO_NO);
			List<TtAsWrApplicationPO> tps = dao.select(tp);
			if (tps.size() > 0) {
				tp = (TtAsWrApplicationPO) tps.get(0);
				id = tp.getId().toString();
				TtAsWrApplicationExtPO tawep = auditingDao
						.queryClaimOrderDetailById(id);
				List<Map<String, Object>> customerList = dao
						.getVinUserName2(id);
				Map<String, Object> customerMap = new HashMap<String, Object>();
				if (customerList != null && customerList.size() > 0)
					customerMap = customerList.get(0);

				/****** add by liuxh 20101125 roduct date *****/
				TmVehiclePO veh = new TmVehiclePO();
				veh.setVin(tawep.getVin());

				/***** add by liuxh 20131108判断车架号不能为空 *****/
				CommonUtils.jugeVinNull(tawep.getVin());
				/***** add by liuxh 20131108判断车架号不能为空 *****/

				TmVehiclePO vehSel = (TmVehiclePO) auditingDao.select(veh).get(
						0);
				/****** add by liuxh 20101125 product date *****/

				// zhumingwei 2011-03-04
				String count = auditingDao.countRepairTimes(id);
				// zhumingwei 2011-03-04

				// 取的当前需要审核的级别
				String authCode = tawep.getAuthCode();
				String authLevel = "--";
				if (authCode != null) {
					TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
					conditionPO.setApprovalLevelCode(authCode);
					List<TtAsWrAuthinfoPO> authLevelList = auditingDao
							.select(conditionPO);
					if (authLevelList != null && authLevelList.size() > 0) {
						TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
						authLevel = CommonUtils.checkNull(authInfoPO
								.getApprovalLevelName());
					}
				}
				// 取得产地名称
				String yieldly = tawep.getYieldly();
				String yieldlyName = "";
				if (yieldly != null) {
					TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
					areaPO.setAreaId(Long.parseLong(yieldly));
					List<TmBusinessAreaPO> arealist = dao.select(areaPO);
					yieldlyName = arealist.get(0).getAreaName();
				}
				tawep.setYieldlyName(yieldlyName);

				List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
				List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
				List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
				List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
				List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
				TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
				String roNo = tawep.getRoNo();
				act.setOutData("application", tawep);

				// 如果是外出维修，查询外出维修相关信息
				if (Constant.CLA_TYPE_09.toString().equals(
						tawep.getClaimType().toString())) {
					List outList = dao.getOutInfo(id);
					if (outList.size() > 0) {
						act.setOutData("outInfo",
								(TtAsWrOutrepairPO) outList.get(0));
					}
				}
				act.setOutData("attachLs", attachLs);
				act.setOutData("ID", id);// 索赔申请单ID
				act.setOutData("itemLs", itemls);// 索赔单之工时信息
				act.setOutData("partLs", partls);// 索赔单之配件信息
				act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
				act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
				act.setOutData("authReason", authReason);// 索赔单之审核原因
				act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
				act.setOutData("customerMap", customerMap);// 客户信息
				act.setOutData("authLevel", authLevel);// 需要审核的级别

				act.setOutData("count", count);// 维修次数
				/******** add by liuxh 20101125 *******/
				act.setOutData("vehSel", vehSel);// add vehcel
				/******** add by liuxh 20101125 *******/
				// 2011-03-11 此方法用于区分轿车和微车
				TcCodePO codePo = new TcCodePO();
				codePo.setType(Constant.chana + "");
				TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
				String codeId = poValue.getCodeId();
				act.setOutData("codeId", Integer.parseInt(codeId));
				// 2011-03-11
				act.setForword(CLAIM_BILL_DETAIL_URL_TOW);
			} else {
				String sql = "select * from tt_as_repair_order_backup ob where ob.ro_no = '"
						+ NEW_RO_NO + "'";
				Map m = dao.pageQueryMap(sql, null, dao.getFunName());
				if (null != m) {
					act.setOutData("remarks", "已转为废弃工单！");
				} else {
					act.setOutData("remarks", "还未生成索赔单！");
				}
				act.setOutData("RO_NO", RO_NO);
				act.setOutData("NEW_RO_NO", NEW_RO_NO);
				act.setForword(CLAIM_BILL_DETAIL_URL_TOW_ERROR);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO(取得其他费用下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOtherfeeStr() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		List<TtAsWrOtherfeePO> seriesList = dao.queryOtherFee(companyId);
		String retStr = "";
		retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeId() + "\'" + "title=\'"
					+ bean.getFeeName() + "\'>" + bean.getFeeCode()
					+ "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getChngCodeStr
	 * @Description: TODO(取得故障代码，质损区域，质损类型，质损程度下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getChngCodeStr(int type) {
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		List<TmBusinessChngCodePO> seriesList = dao.queryChngCodeByType(type,
				companyId, null, null);
		String retStr = "[{";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TmBusinessChngCodePO bean = new TmBusinessChngCodePO();
			bean = (TmBusinessChngCodePO) seriesList.get(i);
			retStr += ",'" + bean.getCode() + "':\'" + bean.getCodeName()
					+ "\'";
		}
		retStr += "}]";
		retStr = retStr.replaceFirst(",", "");
		return retStr;
	}

	/**
	 * add by kevinyin 2011-5-16 分开微车轿车索赔单明细展示页面
	 */
	/**
	 * 
	 * @Title: claimBillDetailForward
	 * @Description: TODO(索赔状态跟踪明细页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillDetailForwardWC() {
		try {
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			String id = request.getParamValue("ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);
			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);

			/****** add by liuxh 20101125 roduct date *****/
			TmVehiclePO veh = new TmVehiclePO();
			veh.setVin(tawep.getVin());

			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/***** add by liuxh 20131108判断车架号不能为空 *****/

			TmVehiclePO vehSel = (TmVehiclePO) auditingDao.select(veh).get(0);
			/****** add by liuxh 20101125 product date *****/

			// zhumingwei 2011-03-04
			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			// 取的当前需要审核的级别
			String authCode = tawep.getAuthCode();
			String authLevel = "--";
			if (authCode != null) {
				TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
				conditionPO.setApprovalLevelCode(authCode);
				List<TtAsWrAuthinfoPO> authLevelList = auditingDao
						.select(conditionPO);
				if (authLevelList != null && authLevelList.size() > 0) {
					TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
					authLevel = CommonUtils.checkNull(authInfoPO
							.getApprovalLevelName());
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			if (yieldly != null) {
				TmBusinessAreaPO p = CommonUtils.getName(yieldly);
				if (p != null && p.getAreaName() != null)
					yieldlyName = p.getAreaName();
			}
			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			// List feeType =
			// dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型列表
			// act.setOutData("OTHERFEE", getOtherfeeStr());

			String roNo = tawep.getRoNo();
			// 2010-08-18 XZM 屏蔽页面不用显示质损相关信息，先不用显示，同时屏蔽页面隐藏域信息
			// act.setOutData("BUSINESS_CHNG_CODE_01",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_01));
			// // 质损程度
			// act.setOutData("BUSINESS_CHNG_CODE_02",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_02));//
			// 质损区域
			// act.setOutData("BUSINESS_CHNG_CODE_03",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_03));//
			// 质损类型
			// act.setOutData("BUSINESS_CHNG_CODE_04",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04));//
			// 故障代码
			act.setOutData("application", tawep);

			// 如果是外出维修，查询外出维修相关信息
			if (Constant.CLA_TYPE_09.toString().equals(
					tawep.getClaimType().toString())) {
				List outList = dao.getOutInfo(id);
				if (outList.size() > 0) {
					act.setOutData("outInfo",
							(TtAsWrOutrepairPO) outList.get(0));
				}
			}
			/*
			 * //2010-08-17 XZM 对应信息不在索赔单明细中呈现 if (tawep!=null) { //如果类型为免费保养 if
			 * (Constant.CLA_TYPE_02.equals(tawep.getClaimType())) { String
			 * flagStr = " in ";//默认设置保养费用
			 * 
			 * HashMap hm = null; StringBuffer sb = new StringBuffer(); List
			 * params = new LinkedList(); Long groupId = tawep.getModelId(); if
			 * (groupId!=null) {
			 * sb.append(" and tvmg.group_id = ? ");//拼sql的查询条件：车型id
			 * params.add(groupId); } PageResult<Map<String, Object>> rs =
			 * dao.feeQuery(Constant.PAGE_SIZE,
			 * 1,sb.toString(),params,Constant.FEE_STATUS.toString(),flagStr);
			 * List ls = rs.getRecords(); if (ls!=null &&
			 * ls.size()>0){//车型保养费用存在 hm = (HashMap)ls.get(0); } else{//不存在，构造
			 * hm = new HashMap(); for(int i=0;i<feeType.size();i++){ HashMap
			 * map = (HashMap)feeType.get(i); hm.put(map.get("CODE_ID"), ""); }
			 * } act.setOutData("FEE", hm);//保养参数对应的值
			 * 
			 * } if (Constant.CLA_TYPE_06.equals(tawep.getClaimType())) {
			 * List<TtAsActivityPO> ls = dao.queryActivity(tawep.getId()); if
			 * (ls!=null) { if (ls.size()>0) {
			 * 
			 * activity = ls.get(0);
			 * 
			 * } }
			 * 
			 * } }
			 */
			// act.setOutData("ACTIVITY",activity );
			act.setOutData("attachLs", attachLs);
			// act.setOutData("FEETYPE", feeType);//查询动态列的集合
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("authLevel", authLevel);// 需要审核的级别

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/******** add by liuxh 20101125 *******/
			act.setOutData("vehSel", vehSel);// add vehcel
			/******** add by liuxh 20101125 *******/
			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			act.setForword(CLAIM_BILL_DETAIL_WC_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * add by kevinyin 2011-5-16 分开微车轿车索赔单明细展示页面(轿车)
	 */
	/**
	 * 
	 * @Title: claimBillDetailForward
	 * @Description: TODO(索赔状态跟踪明细页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillDetailForwardJC() {
		try {
			ClaimManualAuditingDao auditingDao = new ClaimManualAuditingDao();
			String id = request.getParamValue("ID");
			String isAudit = request.getParamValue("isAudit");// 是否是索赔审核（true:是，false:索赔明细查询）
			TtAsWrApplicationExtPO tawep = auditingDao
					.queryClaimOrderDetailById(id);
			List<Map<String, Object>> customerList = dao.getVinUserName2(id);
			Map<String, Object> customerMap = new HashMap<String, Object>();
			if (customerList != null && customerList.size() > 0)
				customerMap = customerList.get(0);

			/****** add by liuxh 20101125 roduct date *****/
			TmVehiclePO veh = new TmVehiclePO();
			veh.setVin(tawep.getVin());

			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/***** add by liuxh 20131108判断车架号不能为空 *****/

			TmVehiclePO vehSel = null;
			if (auditingDao.select(veh) != null
					&& auditingDao.select(veh).size() > 0) {
				vehSel = (TmVehiclePO) auditingDao.select(veh).get(0);
			}

			/****** add by liuxh 20101125 product date *****/

			// zhumingwei 2011-03-04
			String count = auditingDao.countRepairTimes(id);
			// zhumingwei 2011-03-04

			// 取的当前需要审核的级别
			String authCode = tawep.getAuthCode();
			String authLevel = "--";
			if (authCode != null) {
				TtAsWrAuthinfoPO conditionPO = new TtAsWrAuthinfoPO();
				conditionPO.setApprovalLevelCode(authCode);
				List<TtAsWrAuthinfoPO> authLevelList = auditingDao
						.select(conditionPO);
				if (authLevelList != null && authLevelList.size() > 0) {
					TtAsWrAuthinfoPO authInfoPO = authLevelList.get(0);
					authLevel = CommonUtils.checkNull(authInfoPO
							.getApprovalLevelName());
				}
			}
			// 取得产地名称
			String yieldly = tawep.getYieldly();
			String yieldlyName = "";
			if (yieldly != null) {
				TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
				areaPO.setAreaId(Long.parseLong(yieldly));
				List<TmBusinessAreaPO> areaList = dao.select(areaPO);
				yieldlyName = areaList.get(0).getAreaName();
			}
			tawep.setYieldlyName(yieldlyName);

			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<TtAsWrAppauthitemPO> appAuthls = dao.queryAppAuthInfo(id);// 审核授权信息
			TtAsWrWrauthorizationPO authReason = dao.queryAuthReason(id);// 需要授权原因
			// List feeType =
			// dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型列表
			// act.setOutData("OTHERFEE", getOtherfeeStr());

			String roNo = tawep.getRoNo();
			// 2010-08-18 XZM 屏蔽页面不用显示质损相关信息，先不用显示，同时屏蔽页面隐藏域信息
			// act.setOutData("BUSINESS_CHNG_CODE_01",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_01));
			// // 质损程度
			// act.setOutData("BUSINESS_CHNG_CODE_02",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_02));//
			// 质损区域
			// act.setOutData("BUSINESS_CHNG_CODE_03",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_03));//
			// 质损类型
			// act.setOutData("BUSINESS_CHNG_CODE_04",getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04));//
			// 故障代码
			act.setOutData("application", tawep);

			// 如果是外出维修，查询外出维修相关信息
			if (Constant.CLA_TYPE_09.toString().equals(
					tawep.getClaimType().toString())) {
				List outList = dao.getOutInfo(id);
				if (outList.size() > 0) {
					act.setOutData("outInfo",
							(TtAsWrOutrepairPO) outList.get(0));
				}
			}
			TtAsWrApplicationPartPO partPO = new TtAsWrApplicationPartPO();
			partPO.setApplicationPart(Long.parseLong(id));
			act.setOutData("applicationPartList", dao.select(partPO));

			/*
			 * //2010-08-17 XZM 对应信息不在索赔单明细中呈现 if (tawep!=null) { //如果类型为免费保养 if
			 * (Constant.CLA_TYPE_02.equals(tawep.getClaimType())) { String
			 * flagStr = " in ";//默认设置保养费用
			 * 
			 * HashMap hm = null; StringBuffer sb = new StringBuffer(); List
			 * params = new LinkedList(); Long groupId = tawep.getModelId(); if
			 * (groupId!=null) {
			 * sb.append(" and tvmg.group_id = ? ");//拼sql的查询条件：车型id
			 * params.add(groupId); } PageResult<Map<String, Object>> rs =
			 * dao.feeQuery(Constant.PAGE_SIZE,
			 * 1,sb.toString(),params,Constant.FEE_STATUS.toString(),flagStr);
			 * List ls = rs.getRecords(); if (ls!=null &&
			 * ls.size()>0){//车型保养费用存在 hm = (HashMap)ls.get(0); } else{//不存在，构造
			 * hm = new HashMap(); for(int i=0;i<feeType.size();i++){ HashMap
			 * map = (HashMap)feeType.get(i); hm.put(map.get("CODE_ID"), ""); }
			 * } act.setOutData("FEE", hm);//保养参数对应的值
			 * 
			 * } if (Constant.CLA_TYPE_06.equals(tawep.getClaimType())) {
			 * List<TtAsActivityPO> ls = dao.queryActivity(tawep.getId()); if
			 * (ls!=null) { if (ls.size()>0) {
			 * 
			 * activity = ls.get(0);
			 * 
			 * } }
			 * 
			 * } }
			 */
			// act.setOutData("ACTIVITY",activity );
			act.setOutData("attachLs", attachLs);
			// act.setOutData("FEETYPE", feeType);//查询动态列的集合
			act.setOutData("ID", id);// 索赔申请单ID
			act.setOutData("itemLs", itemls);// 索赔单之工时信息
			act.setOutData("partLs", partls);// 索赔单之配件信息
			act.setOutData("otherLs", otherls);// 索赔单之其他项目信息
			act.setOutData("appAuthls", appAuthls);// 索赔单之审核信息
			act.setOutData("authReason", authReason);// 索赔单之审核原因
			act.setOutData("isAudit", isAudit);// true:审核页面 false:明细查询页面
			act.setOutData("customerMap", customerMap);// 客户信息
			act.setOutData("authLevel", authLevel);// 需要审核的级别

			// zhumingwei 2011-03-04
			act.setOutData("count", count);// 维修次数
			// zhumingwei 2011-03-04

			/******** add by liuxh 20101125 *******/
			if (vehSel != null) {
				act.setOutData("vehSel", vehSel);// add vehcel
			}

			/******** add by liuxh 20101125 *******/
			// zhumingwei 2011-03-11 此方法用于区分轿车和微车
			TcCodePO codePo = new TcCodePO();
			codePo.setType(Constant.chana + "");
			TcCodePO poValue = (TcCodePO) dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			// zhumingwei 2011-03-11

			// 站长及三包员联系电话
			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setDealerId(tawep.getDealerId());
			List<TmClaimContactPhonePO> listContact = dao.select(ccp);
			if (listContact.size() > 0) {
				act.setOutData("contact", listContact.get(0));
			}

			act.setForword(CLAIM_BILL_DETAIL_WC_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void timeParameter(){
		
		act.setForword(TIME_PARAMETER);
	}
	
	public void addTimeParameter(){
		String ID = request.getParamValue("ID");
		
		if(ID!=null&&!ID.equals("")){
			TmParameterPO pa=new TmParameterPO();
			pa.setId(Long.parseLong(ID));
			List<TmParameterPO> pas= dao.select(pa);
			TcCodePO code=new TcCodePO();
			
			if(pas.size()>0){
				code.setCodeId(pas.get(0).getParameterType().toString());
				List<TcCodePO> codes=dao.select(code);
				if(codes.size()>0){
					act.setOutData("code", codes.get(0));
				}
				
				act.setOutData("po", pas.get(0));
			}
			act.setForword(UPDATE_TIME_PARAMETER);
		}else{
			act.setForword(ADD_TIME_PARAMETER);
		}
		
	}
	
	public void addOrUpdateTimeParameter(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String ID = request.getParamValue("ID");
		String PARAMETER_NAME = request.getParamValue("PARAMETER_NAME");
		String TIME_OUT = request.getParamValue("TIME_OUT");
		String PARAMETER_CODE = request.getParamValue("PARAMETER_CODE");
		String AMOUNT = request.getParamValue("AMOUNT");
		String PARAMETER_TYPE = request.getParamValue("PARAMETER_TYPE");
		String PARAMETER_STATUS = request.getParamValue("PARAMETER_STATUS");
		
		TmParameterPO pa=new TmParameterPO();
		pa.setAmount(Double.parseDouble(AMOUNT));
		pa.setParameterName(PARAMETER_NAME);
		pa.setParameterCode(PARAMETER_CODE);
		pa.setParameterType(Integer.parseInt(PARAMETER_TYPE));
		pa.setParameterStatus(Integer.parseInt(PARAMETER_STATUS));
		pa.setTimeout(Integer.parseInt(TIME_OUT));
		pa.setCreateDate(new Date());
		pa.setCreateBy(logonUser.getUserId());
		if(ID==null||ID.equals("")){
			ID=SequenceManager.getSequence("");
			pa.setId(Long.parseLong(ID));
			dao.insert(pa);
			act.setForword(TIME_PARAMETER);
		}else{
			TmParameterPO pa1=new TmParameterPO();
			pa1.setId(Long.parseLong(ID));
			pa.setId(Long.parseLong(ID));
			List<TmParameterPO> pas= dao.select(pa1);
			if(pas.size()>0){
				dao.update(pa1, pa);
			}
			act.setForword(TIME_PARAMETER);
		}
	}
	
	public void queryTimeParameter(){
		try{
			PageResult<Map<String, Object>> list=null;
			String PARAMETER_STATUS=request.getParamValue("PARAMETER_STATUS");
			String PARAMETER_TYPE=request.getParamValue("PARAMETER_TYPE");
			String parameterName=request.getParamValue("parameterName");
			String parameterCode=request.getParamValue("parameterCode");
			
			Map<String,String> params=new HashMap();
			params.put("PARAMETER_STATUS", PARAMETER_STATUS);
			params.put("PARAMETER_TYPE", PARAMETER_TYPE);
			params.put("parameterName", parameterName);
			params.put("parameterCode", parameterCode);
			
			list =dao.timeParameter(request,getPage(1),getCurrPage(),params);
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	public void checkParametertype(){
		String parameterType=request.getParamValue("parameterType");
		TmParameterPO pa=new TmParameterPO();
		pa.setParameterType(Integer.parseInt(parameterType));
		List list=dao.select(pa);
		if(list.size()>0){
			act.setOutData("succ", 1);
		}else{
			act.setOutData("succ", 0);
		}
		
		
		
		
	}
	
}
