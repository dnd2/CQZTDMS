package com.infodms.yxdms.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.oldPart.ClaimApproveOldPartStoredDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TrReturnLogisticsPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.dao.OldReturnDAO;
import com.infodms.yxdms.service.OldReturnService;
import com.infodms.yxdms.service.impl.OldReturnServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;

public class OldReturnAction extends BaseAction {

	private PageResult<Map<String, Object>> list = null;
	private OldReturnService oldreturnservice = new OldReturnServiceImpl();

	public void oldPartApplyAddsub() {
		String type = getParam("type");
		if ("update".equals(type)) {
			oldreturnservice.updateOldPartApplyresonbyID(request);
		}else{
		 Long pkid =   oldreturnservice.insertreturnapply(request);
		    oldreturnservice.updatereturnedstatusbyreturnno(pkid);
		}
		sendMsgByUrl("oldreturn", "old_part_Audit_list", "旧件回运延期列表");
	}

	public void oldPartApplyAuditList() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = oldreturnservice.oldPartApplyList(request,
					Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("oldreturn", "old_part_apply_audit_list", "旧件回运延期审核列表");
	}
	// 延期审核----通过----拒绝
	public void oldPartApplyThrough() {
		String type = getParam("type");
		if ("Through".equals(type)) {
			oldreturnservice.updateOldPartApplybyID(request);
		 int res =	oldreturnservice.updateOldreturnbyID(request);// 修改主表
			setJsonSuccByres(res);
		}
		if ("Refuse".equals(type)) {
			int res =	oldreturnservice.updateOldPartApplybyID1(request);
			setJsonSuccByres(res);
		}
	}

	public void oldPartApplyAddSure() {
		act.setOutData("identify", getParam("identify"));
		int res = oldreturnservice.oldPartApplyAddSure(request, loginUser);
		setJsonSuccByres(res);
	}

	public void oldPartApplyAudit() {
		int res = oldreturnservice.oldPartApplyAudit(request);
		setJsonSuccByres(res);
	}

	public void checkDateOldReturn() {
		boolean flag = oldreturnservice.checkDateOldReturn(request, loginUser);// 延期申请检测
		act.setOutData("flag", flag);
	}

	// 修改操作
	public void oldPartApplyInit() {
	    //撤销操作
		String type =  getParam("type");
		if ("Revoke".equals(type)) {
		  int res =	oldreturnservice.updateOldreturnapplybyID(request);
		  setJsonSuccByres(res);
		}
		Map<String, Object> map = oldreturnservice.finddatabyreturnno(request);
		act.setOutData("map", map);
		act.setOutData("loginUser", loginUser.getName());
		sendMsgByUrl("oldreturn", "modifyClaimBackDetail11lj", "旧件回运修改页面");
	}

	// 审核--明细操作
	public void oldPartApplyInitAutid() {
		String type = getParam("type");
		request.setAttribute("type", type);
		Map<String, Object> map = oldreturnservice.finddatabyreturnno(request);
		act.setOutData("map", map);
		act.setOutData("type", type);
		act.setOutData("loginUser", loginUser.getName());
		if ("apply".equals(type)) {
			sendMsgByUrl("oldreturn", "old_part_applyexamine", "旧件申请明细页面");
		}
		if ("view".equals(type)) {
			sendMsgByUrl("oldreturn", "old_part_examine", "旧件回运明细页面");
		}
		if ("update".equals(type)) {
			sendMsgByUrl("oldreturn", "old_part_apply_Detailed", "旧件回运审核页面");
		}
	}
	//导出功能
	public void	reportoldpartapply(){
	 PageResult<Map<String, Object>> list1 = oldreturnservice.oldPartApplyAudit(request,Constant.PAGE_SIZE_MAX, getCurrPage());
		oldreturnservice.expotDataoldpartapply(list1);
	}
	// 新增
	public void oldPartApplyAdd() {
		request.setAttribute("type", "add");
		request.setAttribute("user_name", loginUser.getName());
		sendMsgByUrl("oldreturn", "old_part_apply_add", "旧件回运延期申请页面");
	}
	// 查询未上报和常规回运的件
	public void queryReturnOrderByConditionlj() {
		list = oldreturnservice.oldPartApplyList1(request, Constant.PAGE_SIZE,
				getCurrPage());
		act.setOutData("list", list);
		sendMsgByUrl("oldreturn", "old_part_apply_add", "旧件回运延期申请页面");
	}
	// 上报操作
	public void oldParReport() {
	    int res = oldreturnservice.oldParReport(request);
		setJsonSuccByres(res);
	}
	//延期申请检测是否已申请
	public void oldPartapplycheck(){
		int res = oldreturnservice.oldPartcheck(request);
		setJsonSuccByres(res);
	}

	// 延期申请查询
	public void oldPartApplyList() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = oldreturnservice.oldPartApplyAudit1(request,
					Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		if ("false".equals(query)) {
			list = oldreturnservice.oldPartApplyAudit(request,
					Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("oldreturn", "old_part_Audit_list", "旧件回运延期列表");
	}

	public void checkBoxTheSame() {
		String res = oldreturnservice.checkBoxTheSame(request);
		act.setOutData("res", res);
	}

	public void returnAmountAudit() {
		int res = oldreturnservice.returnAmountAudit(request, loginUser);
		setJsonSuccByres(res);
	}

	/**
	 * 可以审核运费的列表
	 */
	public void returnAmountAuditList() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = oldreturnservice.returnAmountAuditListData(request,
					Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("oldreturn", "amount_audit_list", "审核运费的列表");
	}

	public void returnAmountAuditInit() {
		Map<String, Object> data = oldreturnservice.findReturnData(request);
		act.setOutData("t", data);
		ClaimApproveOldPartStoredDao dao = ClaimApproveOldPartStoredDao
				.getInstance();
		StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
		List<Map<String, Object>> detailList1 = dao
				.queryClaimBackDetailList3(getParam("id"));
		act.setOutData("detailList1", detailList1);
		// 附件
		List<FsFileuploadPO> fileList = smDao
				.queryAttachFileInfo(getParam("id"));
		act.setOutData("fileList", fileList);
		sendMsgByUrl("oldreturn", "amount_audit_page", "审核运费的列表");
	}
	
	public void  EmergencyTracking(){
	   String type = getParam("type");
	   if ("query".equals(type)) {
		   list = oldreturnservice.QueryEmergencyTracking(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
	      act.setOutData("ps",list);
	   }
		sendMsgByUrl("oldreturn", "Emergency_Tracking_list", "紧急调件状态跟踪");
	}
	public void addRemark(){
	   String type =getParam("type");
	   String id =getParam("id");
	   if ("add".equals(type)) {
		   act.setOutData("id",id);
		  List<TtAsPartBorrowPO> list = oldreturnservice.queryEmergencyremarkByid(request,loginUser);
		  act.setOutData("remark",list.get(0).getRemark());
		   sendMsgByUrl("oldreturn", "Emergency_remark_add", "紧急调件备注增加");
	   }else if ("addsure".equals(type)) {
		 int res =  oldreturnservice.addsure(request,loginUser);
		 setJsonSuccByres(res);
	}
	}
	public void EmergencyTrackExport(){
		oldreturnservice.EmergencyTrackExport(request,loginUser,act,Constant.PAGE_SIZE_MAX,getCurrPage());
	}
	//==========================================质量部  跟运费审核部分
	public void QualityAudit(){
		String type = getParam("type");
		if ("query".equals(type)) {
		  list =	oldreturnservice.QualityAuditlistQuery(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		  act.setOutData("ps",list);
		}
		sendMsgByUrl("oldreturn", "Quality_Audit_list", "质量部入库审核主页面");
	}
	public void  QualityAuditView(){
		Map<String,String> params=new HashMap<String, String>();
		String currp = request.getParamValue("curPage");
		String partCode = request.getParamValue("part_code");
		String partName = request.getParamValue("part_name");
		String yieldly = request.getParamValue("YIELDLY_TYPE");
		if(yieldly==null || "".equalsIgnoreCase(yieldly)){
			yieldly = request.getParamValue("yieldly");
		}
			try {
				if(Utility.testString(partName)){
				partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		String claim_id=request.getParamValue("CLAIM_ID");//此处为物流单ID
		String types = request.getParamValue("types");
		act.setOutData("types",Integer.valueOf(types));
		//回运要入库的物流单ID
		String boxNo=request.getParamValue("boxNo");
		params.put("i_claim_id", claim_id);
		params.put("boxNo", boxNo);
		params.put("partCode", partCode);
		params.put("partName", partName);
			//获取回运清单明细信息List
			ClaimApproveAndStoredReturnInfoBean returnListBean=oldreturnservice.getApproveAndStoredReturnInfo11(params);
			List<Map<String,Object>> detailList =oldreturnservice.queryClaimBackDetailList2(params);
			List<Map<String,Object>> detailList1 =oldreturnservice.queryClaimBackDetailList3(claim_id);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			ClaimBackListDao claimBackDao = ClaimBackListDao.getInstance();
			List<Map<String, Object>> listBoxNo =claimBackDao.getBoxNo(Long.parseLong(claim_id));
			act.setOutData("listBoxNo", listBoxNo);
			act.setOutData("claim_id", claim_id);
			act.setOutData("boxNo", boxNo);
			/*********Iverson add By 2010-12-14 查询装箱单号(并去掉空和重复)*********************/
			act.setOutData("detailList", detailList);
			act.setOutData("detailList1", detailList1);
			act.setOutData("returnListBean", returnListBean);
			act.setOutData("part_code", partCode);
			act.setOutData("part_name", partName);
			act.setOutData("yieldly", yieldly);
			List<Map<String, Object>> list = claimBackDao.getCode();
			act.setOutData("list", list);
			List<Map<String, Object>> deductList = oldreturnservice.getDeductList();
			//附件
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claim_id);
			act.setOutData("fileList", fileList);
			act.setOutData("deductList", deductList);
			act.setOutData("num", act.getSession().get("num"));
			act.setOutData("dealerCodeSS", act.getSession().get("dealerCodeSS"));
		    sendMsgByUrl("oldreturn", "Quality_Audit_Edit", "质量部入库审核界面");
		
	}
	public void oldPartSignAuditIn(){//质量部入库审核
	    int res =oldreturnservice.oldPartSignAuditIn(request,loginUser);
	    setJsonSuccByres(res);
	}
	@SuppressWarnings("unchecked")
	public void historyoldreturnquery(){//质量部入库历史查询
		List detailList =oldreturnservice.historyoldreturnquery(request,loginUser);
		act.setOutData("list",detailList );
		sendMsgByUrl("oldreturn", "Quality_Audit_history_list", "质量部入库审核界面");
	}
	public void returnAmountAuditListNew(){
		String type = getParam("type");
		if ("query".equals(type)) {
		  list = oldreturnservice.returnAmountAuditListNew(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		  act.setOutData("ps", list);
		}
		sendMsgByUrl("oldreturn", "amount_audit_list_new", "旧件运费审核(new)");
	}
	public void returnAmountAuditInitnew() {//旧件运费审核(new)
		Map<String, Object> data = oldreturnservice.findReturnData(request);
		act.setOutData("t", data);
		ClaimApproveOldPartStoredDao dao = ClaimApproveOldPartStoredDao.getInstance();
		StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
		List<Map<String, Object>> detailList1 = dao.queryClaimBackDetailList3(getParam("id"));
		act.setOutData("detailList1", detailList1);
		// 附件
		List<FsFileuploadPO> fileList = smDao.queryAttachFileInfo(getParam("id"));
		act.setOutData("fileList", fileList);
		sendMsgByUrl("oldreturn", "amount_audit_page_new", "审核运费的列表");
	}
	public void returnAmountAuditNew(){
		int res = oldreturnservice.returnAmountAuditNew(request, loginUser);
		setJsonSuccByres(res);
	}
	/**
	 * 旧件回运列表（车厂）
	 */
	public void oldpartReturnlist(){
		String type = getParam("type");
		String returnId = getParam("returnId");
		if (BaseUtils.notNull(returnId)) {
			DBLockUtil.freeLock(returnId.toString(), DBLockUtil.BUSINESS_TYPE_21);
			DBLockUtil.freeLock(returnId.toString(), DBLockUtil.BUSINESS_TYPE_22);
		}
		if ("query".equals(type)) {
			PageResult<TtAsWrBackListQryBean> ps = oldreturnservice.queryClaimBackList(request,act,loginUser, getCurrPage(),Constant.PAGE_SIZE);
		 	act.setOutData("ps", ps);
		}
		sendMsgByUrl("oldreturn", "old_part_return_list", "旧件回运列表");
	}
	//装箱
	public void oldPartModifyDetail(){
			String claimId=getParam("ORDER_ID");//获取物流单ID
			String borrow_no=getParam("borrow_no");//borrow_no
			String partCode = getParam("part_code");
			String partName = getParam("part_name");
			String oper=getParam("oper");//获取操作动作
			try {
				if(Utility.testString(partName)){
					partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			TtAsWrOldPartBackListDetailBean detailInfoBean=oldreturnservice.getClaimBackInfo(request,loginUser);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			List<TtAsWrOldPartDetailListBean> detailList = oldreturnservice.queryClaimBackDetailList(request,loginUser,detailInfoBean);
			if(checkListNull(detailList)){
				if(BaseUtils.testString(borrow_no)){
					TtAsPartBorrowPO borrow=new TtAsPartBorrowPO();
					borrow.setId(Long.parseLong(borrow_no) );
					List<TtAsPartBorrowPO> borrows= oldreturnservice.queryTtAsPartBorrow(borrow);
					if(checkListNull(borrows)){
						borrow=borrows.get(0);
						act.setOutData("borrowPerson", borrow.getBorrowPerson());
						act.setOutData("borrowPhone", borrow.getBorrowPhone());
						act.setOutData("requireDate", borrow.getRequireDate());
					}
				}
		   }
			act.setOutData("detailList", detailList);
			act.setOutData("returnId", claimId);
			act.setOutData("part_code", partCode);
			act.setOutData("part_name", partName);
			boolean isDeal = DBLockUtil.lock(claimId, DBLockUtil.BUSINESS_TYPE_21);//装箱同步
			if (isDeal) {
				setJsonSuccByres(1);	
			   if("mod".equals(oper)){//转到修改页面
				sendMsgByUrl("oldreturn", "old_part_modify_detail", "旧件回装箱页面");
			   }
			}else {
				setJsonSuccByres(-1);	//有人正在操作
				sendMsgByUrl("oldreturn", "old_part_return_list", "旧件回运列表");
			}
	
	}
	//zhumingwei 2011-04-13
	public void updateReturnListInfo11(){
		String returnId=request.getParamValue("returnId");//获取回运清单的修改主键
		String backId=request.getParamValue("i_back_id");//获取回运清单的修改主键
		String boxTotalNum=request.getParamValue("i_boxTotalNum");//装箱总数
		@SuppressWarnings("unused")
		List<TtAsWrOldReturnedDetailPO> returnDetaiList=null;
		int ret=0;
	   String success =	"updateSuccess";
		try{
			//保存索赔回运明细信息表
			returnDetaiList=oldreturnservice.getOldReturnedDetailPOByReturnId(request, 1,10000);
			ret=oldreturnservice.updateClaimBackOrdMainInfo(request,loginUser);
			//判断修改表是否成功
			if(ret==1){
				act.setOutData("sumNum",Integer.parseInt(boxTotalNum));
			}else{
				success="updateFailure";
			}
			act.setOutData("updateResult",success);
			//根据回运主键查询索赔配件清单表信息
			TtAsWrOldPartBackListDetailBean detailInfoBean=oldreturnservice.getClaimBackInfo(request,1,1);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			//根据回运主键查询索赔配件明细表信息
			List<TtAsWrOldPartDetailListBean> detailList = oldreturnservice.queryClaimBackDetailList(request);
			act.setOutData("detailList", detailList);
			//========================================================================2015-7-16增加日志记录操作人
			oldreturnservice.insertTtAsComRecord(request,loginUser);
			//========================================================================2015-7-16增加日志记录操作人
			DBLockUtil.freeLock(returnId.toString(), DBLockUtil.BUSINESS_TYPE_21);//解锁
			act.setOutData("updateResult",success);
			sendMsgByUrl("oldreturn", "modifyClaimBackDetail", "旧件回运明细");
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			act.setOutData("approveResult","approveFailure");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//补录
	public void queryBackClaimDetailInfo22(){
		String claimId=request.getParamValue("ORDER_ID");//获取物流单ID
			try{
				String return_type = DaoFactory.getParam(request, "return_type");
				act.setOutData("claimId", claimId);
				act.setOutData("return_type", return_type);
				TtAsWrOldReturnedPO poValue  = oldreturnservice.queryTtAsWrOldReturnedByid(request);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(poValue.getSendTime()!=null){
					String time = sdf.format(poValue.getSendTime());
					act.setOutData("time", time);
				}
				if (poValue.getArriveDate() != null) {
					String arriveDate = sdf.format(poValue.getArriveDate());
					act.setOutData("arriveDate", arriveDate);
				}
				TrReturnLogisticsPO sp = oldreturnservice.queryTrReturnLogistics(request);
				TtAsWrReturnedOrderPO op = oldreturnservice.queryTtAsWrReturnedOrder(request,sp);
				 String notice="";
				 List<Map<String, Object>> select=null;
				 String id=request.getParamValue("dealer_id");
				 List<TmOldpartTransportPO> sList = oldreturnservice.queryGetTransPList(Long.valueOf(id));
				 if(sList==null || sList.size()<1){
					 notice="noTrans";
					 select=oldreturnservice.getStr(1L);
				 }else{
					 select=oldreturnservice.getStr(sList,return_type);
				 }
				act.setOutData("tranNo", op.getTranNo());
				act.setOutData("poValue", poValue);
				act.setOutData("notice", notice);
				act.setOutData("select", select);
				//附件
				StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
				List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(claimId);
				act.setOutData("fileList", fileList);
//				boolean isDeal = DBLockUtil.lock(claimId.toString(), DBLockUtil.BUSINESS_TYPE_22);	//补录同步
				    sendMsgByUrl("oldreturn", "modifyClaimBackDetail22", "旧件回运补录页面");
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
	
		
	}
	
	//zhumingwei 2011-04-14
	public void updateReturnListInfo22(){
		String claimId=request.getParamValue("claimId");//物流单ID
		List<TtAsWrOldReturnedDetailPO> returnDetaiList=null;
		try{
			String success = "补录成功!";
			returnDetaiList=oldreturnservice.getOldReturnedDetailPOByReturnId1(request, 1,10000);
			int ret = oldreturnservice.updateTtAsWrOldReturned(request,loginUser, returnDetaiList);
			//========================================================================2015-7-16增加日志记录操作人
			oldreturnservice.insertTtAsComRecord1(request,loginUser);
			//========================================================================2015-7-16增加日志记录操作人
			if(ret>0){
				act.setOutData("updateResult","updateSuccess");
			}else{
				act.setOutData("updateResult","updateFailure");
			}
			//附近功能：
			String ywzj=claimId.toString();
			String[] fjids = request.getParamValues("fjid");//获取文件ID
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			if(ywzj!=null&&!ywzj.equals("")){//修改的时候
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			}else{
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);	
			}
			DBLockUtil.freeLock(claimId.toString(), DBLockUtil.BUSINESS_TYPE_22);//解锁
			act.setOutData("msg",success);
			sendMsgByUrl("oldreturn", "old_part_return_list", "旧件主页面");
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SAVE_FAILURE_CODE, "索赔件回运清单维护--修改");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryBackClaimDetailInfo11(){
		try{
			String claimId=getParam("ORDER_ID");//获取物流单ID
			String borrow_no=getParam("borrow_no");//borrow_no
			String partCode = getParam("part_code");
			String partName = getParam("part_name");
			String oper=getParam("oper");//获取操作动作
			if(Utility.testString(partName)){
				partName = new String(request.getParamValue("part_name").getBytes("ISO-8859-1"),"UTF-8");
			}
			TtAsWrOldPartBackListDetailBean detailInfoBean=oldreturnservice.getClaimBackInfo(request,loginUser);
			request.setAttribute("claimPartDetailBean", detailInfoBean);
			List<TtAsWrOldPartDetailListBean> detailList = oldreturnservice.queryClaimBackDetailList1(request,loginUser, detailInfoBean);
			if(checkListNull(detailList)){
				if(BaseUtils.testString(borrow_no)){
					TtAsPartBorrowPO borrow=new TtAsPartBorrowPO();
					borrow.setId(Long.parseLong(borrow_no) );
					List<TtAsPartBorrowPO> borrows= oldreturnservice.queryTtAsPartBorrow(borrow);
					if(checkListNull(borrows)){
						borrow=borrows.get(0);
						act.setOutData("borrowPerson", borrow.getBorrowPerson());
						act.setOutData("borrowPhone", borrow.getBorrowPhone());
						act.setOutData("requireDate", borrow.getRequireDate());
					}
				}
		   }
			act.setOutData("detailList", detailList);
			act.setOutData("returnId", claimId);
			act.setOutData("part_code", partCode);
		 if("mod".equals(oper)){//转到修改页面modifyClaimBackDetail11
				sendMsgByUrl("oldreturn", "modifyClaimBackDetail11", "明细");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔件回运清单维护--查询明细");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	//运费审核
	public void authPriceAudit(){
		String id = CommonUtils.checkNull(request.getParamValue("id"));//旧件明细ID
		String authPrice = CommonUtils.checkNull(request.getParamValue("authPrice"));//审核运费
		String priceRemark = CommonUtils.checkNull(request.getParamValue("priceRemark"));//运费审核备注
		try{
			
            Map<String,Object>params = new HashMap<String, Object>();
			
			params.clear();
			params.put("id", id);
			params.put("authPrice", authPrice);
			params.put("priceRemark", priceRemark);
			params.put("updateBy", loginUser.getUserId());
			OldReturnDAO dao = OldReturnDAO.getInstance();
			dao.authPriceAudit(params);
            
        	act.setOutData("code", "succ");
			act.setOutData("msg", "操作成功");
		} catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存售后服务工单信息保存数据异常.");
            act.setException(e1);
        	act.setOutData("code", "fail");
			act.setOutData("msg", "保存失败!"+e1);
			e.printStackTrace();
        }
	}
	
}
//==================================旧件回运代做车厂
