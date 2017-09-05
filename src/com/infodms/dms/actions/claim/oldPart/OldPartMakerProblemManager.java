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
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartMakerProblemDao;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartOutStorageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtAsRangeModDetailPO;
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
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartMakerProblemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.RemindDateUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--索赔旧件出库查询
 * 作者：  赵伦达
 */
public class OldPartMakerProblemManager extends BaseAction{
	private AclUserBean logonUserBean = null;
	private ClaimOldPartMakerProblemDao dao=null;
	//url导向
	private final String queryOutStoreUrl=sendUrl(this.getClass(), "queryOutStorageList");
	private final String queryRangeUrl = sendUrl(this.getClass(), "queryRangeList");
	private final String queryAddOutStoreUrl = sendUrl(this.getClass(), "queryPreOutStorageList");
	private final String queryBatchStockUrl = sendUrl(this.getClass(), "queryBatchStockList");
	//索赔旧件开票
	private final String queryOutDoorUrl = sendUrl(this.getClass(), "queryOutDoorList");
	private final String queryAddOutDoorUrl = sendUrl(this.getClass(), "queryPreOutDoorAdd");
	//不索赔批量出库
	private final String queryAddStoreUrl = sendUrl(this.getClass(), "queryAddStoreList");
	//二次抵扣出库
	private final String queryAddOffsetStoreUrl = sendUrl(this.getClass(), "queryAddOffsetStore");
	//查询明细
	private final String queryClaimDetailUrl =sendUrl(this.getClass(), "queryClaimDetail");
	//查询明细
	private final String DYClaimDetailUrl =sendUrl(this.getClass(), "DYClaimDetailUrl");
	
	private final String queryScanningStockUrl  =sendUrl(this.getClass(), "queryScanningStock");
	
	private final String  queryScanningDetailUrl=sendUrl(this.getClass(), "queryScanningDetail");
	
	private final String  queryScanningBuluUrl=sendUrl(this.getClass(), "queryScanningBulu");
	private final String  queryScanningBuluDetailUrl=sendUrl(this.getClass(), "queryScanningBuluDetail");
	
	private final String  addBuluUrl=sendUrl(this.getClass(), "addBulu");
	private final String MAIN_PARTCODE_URL = sendUrl(this.getClass(), "showPartCode");// 配件选择
	
	
	private final String  ScanningDetailUrl=sendUrl(this.getClass(), "ScanningDetail");
	private final String  ScanningDetailsUrl=sendUrl(this.getClass(), "ScanningDetails");
	private final String  ScanningPrintUrl=sendUrl(this.getClass(), "ScanningPrint");
	
	private final String  notScanningDetailUrl=sendUrl(this.getClass(), "notScanningDetail");
	
	private final String queryPartBaseUrl=sendUrl(this.getClass(), "queryPartBase");
	private final String OLD_PART_BARCODE_DETAIL=sendUrl(this.getClass(), "barCodeDetail");
	private final String OLD_PART_OUT_DETAIL=sendUrl(this.getClass(), "oldPartOutDetailInfo");
	private final String OLD_PART_OUT_DETAIL2=sendUrl(this.getClass(), "oldPartOutDetailInfo2");
	private final String OLD_CLAIM_DETAIL=sendUrl(this.getClass(), "oldCliamDetailInfo");
	private final String OLD_PART_DETAIL=sendUrl(this.getClass(), "outDetailPrint");
	private final String OLD_PART_DETAILS=sendUrl(this.getClass(), "outNoticePrint");
	private final String OLD_PART_DETAILS2=sendUrl(this.getClass(), "outPartNoticeIn");
	private final String NO_PART_CLAIM=sendUrl(this.getClass(), "noPartClaimPer");
	private final String MAIN_PART_CLAIM_PER = sendUrl(this.getClass(), "mainPartClaimPer");
	private final String MAIN_PART_CLAIM_DETAIL = sendUrl(this.getClass(), "mainPartClaimDetail");
	private final String MAIN_PART_CLAIM_PRINT = sendUrl(this.getClass(), "mainPartClaimPrint");
	private final String OLD_PART_BARCODE_DETAIL2=sendUrl(this.getClass(), "barCodeDetail2");
	private final String OUT_NOTICE_INFO=sendUrl(this.getClass(), "outNoticeInfo");
	private final String OLD_PART_NOTICE_DETAIL=sendUrl(this.getClass(), "outNoticeDetail");//出门证明细 spefeeNoticeAdd
	private final String QUERY_SPEFEE_LIST=sendUrl(this.getClass(), "querySpefeeList");
	private final String SPEFEE_NOTICE_ADD=sendUrl(this.getClass(), "spefeeNoticeAdd");
	
	//旧件二次入库jsp
	private final String OLD_PART_RE_INSTORE_PER=sendUrl(this.getClass(), "oldPartReInstorePer");
	private final String OLD_PART_SUPPLY_MODIFY_INFO=sendUrl(this.getClass(), "oldPartReInstoreInfo");//
	private final String OLD_PART_SUPPLY_MOD_PER=sendUrl(this.getClass(), "oldPartSupplyModPer");
	private final String SHOW_SUPPLIER_URL = sendUrl(this.getClass(), "showSupply");
	
	private final String OLD_PART_RE_IN_QUERY=sendUrl(this.getClass(), "oldPartReInQuery");
	//无旧件索赔 
	private final String queryAddOutStoreUrl2 = sendUrl(this.getClass(), "queryPreOutStorageList2");
	//private final String NO_PART_NOTICE="/jsp/claim/oldPart/noPartNotice");
	//配件工时关系维护
	private final String partLabourPer = sendUrl(this.getClass(), "partLabourPer");
	private final String partLabourModify = sendUrl(this.getClass(), "partLabourModify");
	private final String showLabour = sendUrl(this.getClass(), "showLabour");// 工时选择  
	private final String MAKER_PROBLEM = sendUrl(this.getClass(), "makerProblem");// 
	private final String MAKER_PROBLEM_UPDATE = sendUrl(this.getClass(), "makerProblemUpdate");// 
	private final String MAKER_PROBLEM_DETAIL = sendUrl(this.getClass(), "makerProblemDetail");//
	
	//索赔旧件退赔单生成界面
	private final String rangeSingle = sendUrl(this.getClass(), "rangeSingle");
	private final String rangeSinglePrint=sendUrl(this.getClass(), "rangeSinglePrint");
	private final String mainPartDetail = sendUrl(this.getClass(), "mainPartDetail");
	private final String printPartCode = sendUrl(this.getClass(), "printPartCode");
	private final String belowConditionDealer = sendUrl(this.getClass(), "belowConditionDealer");
	
	private final String inHouseDetail=sendUrl(this.getClass(), "inHouseDetail");//旧件入库明细
	private final String oldPartReturnDetail=sendUrl(this.getClass(), "oldPartReturnDetail");//旧件返运明细
	private final String MAIN_TIME_URL = "/jsp/claim/dealerClaimMng/showMainTime2.jsp";// 工时选择

	
	private Integer getPage(int type){
		Integer page_size=0;
		if(type==1){
			page_size=Constant.PAGE_SIZE;
		}else if(type==2){
			page_size=Constant.PAGE_SIZE_MAX;
		}
		return page_size;
	}
	//出门证 删除
	@SuppressWarnings("unchecked")
	public void outDel(){
		try {
			
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			dao=ClaimOldPartMakerProblemDao.getInstance();
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
			dao=ClaimOldPartMakerProblemDao.getInstance();
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
			dao=ClaimOldPartMakerProblemDao.getInstance();
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
	
	public void updateRemark() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			dao=ClaimOldPartMakerProblemDao.getInstance();
			String problem_id=request.getParamValue("problem_id");
			String remark=request.getParamValue("remark");
			TtPartMakerProblemPO problem=new TtPartMakerProblemPO();
			TtPartMakerProblemPO problem1=new TtPartMakerProblemPO();
			problem.setPartId(Long.parseLong(problem_id));
			List<TtPartMakerProblemPO> list=dao.select(problem);
			if(list.size()>0){
				problem1=list.get(0);
				problem1.setRemark(remark);
				problem1.setStatus(99971001);
				dao.update(problem, problem1);
			}
			act.setOutData("succ", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商问题维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}