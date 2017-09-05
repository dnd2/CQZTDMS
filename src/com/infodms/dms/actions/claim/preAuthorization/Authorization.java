package com.infodms.dms.actions.claim.preAuthorization;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.preAuthorization.AuthorizationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.dao.ClaimDAO;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class Authorization extends BaseAction {
	private Logger logger = Logger.getLogger(Authorization.class);
	private final AuthorizationDao dao = AuthorizationDao.getInstance();
	private final ClaimDAO claimDAO = new ClaimDAO();
	private OrderService orderservice=new OrderServiceImpl();
	
	private final String AUTHORIZATION_QUERY = "/jsp/claim/preAuthorization/authorizationQuery.jsp";// 索赔单主机厂预授权查询
	private final String AUTHORIZATIONCXINIT = "/jsp/claim/preAuthorization/authorizationCxInit.jsp";// 索赔单主机厂预授权查询
	
	
	private final String AUTHORIZATION_DLR_QUERY = "/jsp/claim/preAuthorization/authorizationDlrQuery.jsp";// 索赔单服务站预授权查询
	private final String AUTHORIZATION_APPLY_QUERY = "/jsp/claim/preAuthorization/authorizationApplyQuery.jsp";// 索赔单预授权申请
	private final String AUTHORIZATION_AUDIT_QUERY = "/jsp/claim/preAuthorization/authorizationAuditQuery.jsp";// 索赔单预授权审核
	private final String AUTHORIZATION_APPLY_ADD = "/jsp/claim/preAuthorization/authorizationApplyAdd.jsp";// 索赔单预授权申请新增
	private final String AUTHORIZATION_CHOOSE_ORD = "/jsp/claim/preAuthorization/authoriChooseOrd.jsp";// 维修工单查询
	private final String AUTHORIZATION_APPLY_MODIFY = "/jsp/claim/preAuthorization/authorizationApplyModify.jsp";// 索赔单预授权修改界面
	private final String AUTHORIZATION_DETAIL = "/jsp/claim/preAuthorization/authorizationDetail.jsp";// 索赔单预授权明细界面
	private final String TC_CODE_CHOOSE = "/jsp/tcCodeChoose.jsp";// 数据字典选择
	

	/**
	 * 
	* @Title: authorizationApplyInit 
	* @author: xyfue
	* @Description: 索赔单预授权申请查询
	* @param     设定文件 
	* @date 2014年11月27日 上午11:35:42 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationApplyInit() {
		try {
			act.setForword(AUTHORIZATION_APPLY_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权申请界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationApplyQuery 
	* @author: xyfue
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2014年11月27日 上午11:41:44 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationApplyQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.authorizationApplyQuery(request,logonUser,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权申请查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationQueryInit 
	* @author: xyfue
	* @Description: 索赔单主机厂预授权查询初始化
	* @param     设定文件 
	* @date 2014年12月1日 下午11:41:46 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationQueryInit() {
		try {
			act.setForword(AUTHORIZATION_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询界面初始（主机厂）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void authorizationCxInit() {
		try {
			act.setForword(AUTHORIZATIONCXINIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询界面初始（主机厂）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationQuery 
	* @author: xyfue
	* @Description: 索赔单主机厂预授权查询
	* @param     设定文件 
	* @date 2014年12月1日 下午11:42:05 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.authorizationQuery(request,logonUser,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询（主机厂）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void authorizationQueryCx() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.authorizationQueryCx(request,logonUser,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询（主机厂）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void authorizationJudeCx() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			 String ID= request.getParamValue("ID");
			 String RO_NO= request.getParamValue("RO_NO");
			 TtAsWrApplicationPO applicationPO = new TtAsWrApplicationPO();
			 applicationPO.setRoNo(RO_NO);
			 List<TtAsWrApplicationPO>  list= dao.select(applicationPO);
			 String jued = "";
			 if(list.size() > 0 )
				 jued = "索赔单已经存在不能撤销！";
			 act.setOutData("jued",jued );
			 act.setOutData("ID", ID);
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询（主机厂）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationDlrQueryInit 
	* @author: xyfue
	* @Description: 索赔单服务站预授权查询
	* @param     设定文件 
	* @date 2014年12月2日 下午11:57:25 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationDlrQueryInit() {
		try {
			act.setForword(AUTHORIZATION_DLR_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询界面初始（服务站）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationDlrQuery 
	* @author: xyfue
	* @Description: 索赔单服务站预授权查询 
	* @param     设定文件 
	* @date 2014年12月2日 下午11:57:49 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationDlrQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.authorizationDlrQuery(request,logonUser,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询（服务站）");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationAuditInit 
	* @author: xyfue
	* @Description: 索赔单预授权申请查询
	* @param     设定文件 
	* @date 2014年11月27日 上午11:35:42 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationAuditInit() {
		try {
			act.setForword(AUTHORIZATION_AUDIT_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权审核界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationAuditQuery 
	* @author: xyfue
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2014年12月1日 下午5:33:49 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationAuditQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.authorizationAuditQuery(request,logonUser,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权审核查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationApplyAddInit 
	* @author: xyfue
	* @Description: 索赔单预授权申请新增界面初始 
	* @param     设定文件 
	* @date 2014年11月27日 下午4:41:38 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationApplyAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String vin = DaoFactory.getParam(request, "vin");
			String mainPartId = DaoFactory.getParam(request, "real_part_id");
			Map<String, Object> vinInfo = null;
			if (!"".equals(vin)) {
				vinInfo =  orderservice.showInfoByVin(request);
			}
			
			Map<String, Object> userInfo = orderservice.findLoginUserInfo(logonUser.getUserId());
			
			Map<String, Object> partInfo = null;
			if(!"".equals(mainPartId)){	
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("MAIN_PART_ID", mainPartId);
				partInfo = dao.getPartInfo(request,logonUser,params);
			}
			//获取三包等级
			String vrLevel = "";
			List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
			if (null != vrLevelList && vrLevelList.size() > 0) {
				vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
			}
			
			act.setOutData("userInfo", userInfo);
			act.setOutData("vinInfo", vinInfo);
			act.setOutData("partInfo", partInfo);
			act.setOutData("vrLevel", vrLevel);
			act.setOutData("VIN", DaoFactory.getParam(request, "vin"));
			act.setOutData("MAKER_CODE", DaoFactory.getParam(request, "producer_code"));
			act.setOutData("IS_RETURN", DaoFactory.getParam(request, "is_return"));
			act.setOutData("APPLY_AMOUNT", DaoFactory.getParam(request, "apply_amount"));
			act.setOutData("AUDIT_AMOUNT", DaoFactory.getParam(request, "pass_amount"));
			act.setOutData("APPLY_REMARK", DaoFactory.getParam(request, "remark"));
			act.setOutData("OUT_PERSON", DaoFactory.getParam(request, "out_person"));
			act.setOutData("APPROVAL_TYPE", claimDAO.changeClaimCodeToRoCode(DaoFactory.getParam(request, "claim_type")));
			act.setOutData("RO_NO", DaoFactory.getParam(request, "ro_no"));
			act.setOutData("ERROR_DESC", DaoFactory.getParam(request, "trouble_reason"));
			act.setOutData("ERROR_REASON", DaoFactory.getParam(request, "trouble_desc"));
			act.setOutData("ERROR_RESULT", DaoFactory.getParam(request, "repair_method"));
			
			act.setForword(AUTHORIZATION_APPLY_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权申请新增界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title: getDetailByVin
	 * @Description: TODO(根据VIN和车主姓名查询车辆信息表)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getDetailByVin() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		try {
			RequestWrapper request = act.getRequest();
			//获取VIN信息
			Map<String, Object> records =  orderservice.showInfoByVin(request);
			//获取三包等级
			String vrLevel = "";
			List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
			if (null != vrLevelList && vrLevelList.size() > 0) {
				vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
			}
			act.setOutData("records", records);
			act.setOutData("vrLevel", vrLevel);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权查询VIN");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: isPdi 
	* @author: xyfue
	* @Description: 判断vin是否做过PDI
	* @param     设定文件 
	* @date 2014年11月28日 下午7:03:19 
	* @return void    返回类型 
	* @throws
	 */
	public void isPdi() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try
		{
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			if("".equals(vin)){
				act.setOutData("succ","0" );
				act.setOutData("msg", "vin不能为空，请输入vin！");
			}else {
				dao.isPdi(request);
				act.setOutData("succ","1" );
				act.setOutData("msg", "做过PDI");
			}
		}
		catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, e.getMessage()+"<br/>");
			logger.error(logonUser, e);
//			act.setException(e1);
			act.setOutData("succ","0" );
			act.setOutData("msg", "通过VIN查询PDI失败");
		}
	}
	
	public void authoriChooseOrdInit() {
		try {
			act.setOutData("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			act.setOutData("repairType", CommonUtils.checkNull(request.getParamValue("repairType")));
			act.setForword(AUTHORIZATION_CHOOSE_ORD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修工单界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authoriChooseOrdQuery 
	* @author: xyfue
	* @Description: 查询维修工单
	* @param     设定文件 
	* @date 2014年11月28日 下午9:44:57 
	* @return void    返回类型 
	* @throws
	 */
	public void  authorizationCx(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String ID = request.getParamValue("ID");
			TtAsWrForeapprovalPO asWrForeapprovalPO = new TtAsWrForeapprovalPO();
			asWrForeapprovalPO.setId(Long.parseLong(ID));
			TtAsWrForeapprovalPO asWrForeapprovalPO1 = new TtAsWrForeapprovalPO();
			asWrForeapprovalPO1.setReportStatus(11561003);
			asWrForeapprovalPO1.setUpdateBy(logonUser.getUserId());
			asWrForeapprovalPO1.setUpdateDate(new Date());
			dao.update(asWrForeapprovalPO, asWrForeapprovalPO1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	
	public void authoriChooseOrdQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.authoriChooseOrdQuery(request,logonUser,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: savePreAuthoriza 
	* @author: xyfue
	* @Description: 保存上报信息
	* @param     设定文件 
	* @date 2014年12月1日 上午11:28:49 
	* @return void    返回类型 
	* @throws
	 */
	public void savePreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String operType  = CommonUtils.checkNull( request.getParamValue("SAVEORCOMIT"));
		String msg = "";
		if ("1".equals(operType)) {
			msg = "保存";
		}else {
			msg = "上报";
		}
		try {
			dao.savePreAuthoriza(request, logonUser);
			act.setOutData("succ", 1);
			act.setOutData("msg", "索赔单预授权"+msg+"成功");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权"+msg);
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权"+msg+"失败");
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: authorizationModifyInit 
	* @author: xyfue
	* @Description: 进入索赔单预授权修改界面 
	* @param     设定文件 
	* @date 2014年12月1日 上午11:29:36 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationModifyInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map< String, Object> foInfoMap = dao.authorizationModifyQuery(request,logonUser);
			Map<String, Object> vinInfo =  orderservice.showInfoByVin(request);
			Map<String, Object> userInfo = orderservice.findLoginUserInfo(Long.valueOf(foInfoMap.get("CREATE_BY").toString()));
			
			Map<String, Object> partInfo = null;
			if(null != foInfoMap.get("MAIN_PART_ID")){		
				partInfo = dao.getPartInfo(request,logonUser,foInfoMap);
			}
			//获取三包等级
			String vrLevel = "";
			List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
			if (null != vrLevelList && vrLevelList.size() > 0) {
				vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
			}
			this.getFile("foId");
			
			act.setOutData("userInfo", userInfo);
			act.setOutData("foInfoMap", foInfoMap);
			act.setOutData("vinInfo", vinInfo);
			act.setOutData("partInfo", partInfo);
			act.setOutData("vrLevel", vrLevel);
			act.setForword(AUTHORIZATION_APPLY_MODIFY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权修改界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: updatePreAuthoriza 
	* @author: xyfue
	* @Description: 修改索赔单预授权信息
	* @param     设定文件 
	* @date 2014年12月1日 下午5:45:04 
	* @return void    返回类型 
	* @throws
	 */
	public void updatePreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String operType  = CommonUtils.checkNull( request.getParamValue("SAVEORCOMIT"));
		String msg = "";
		if ("1".equals(operType)) {
			msg = "保存";
		}else {
			msg = "上报";
		}
		try {
			String foId = CommonUtils.checkNull(request.getParamValue("foId"));
			if ("".equals(foId)) {
				act.setOutData("succ", 0);
				act.setOutData("msg", "FO_ID为空,索赔单预授权"+msg+"失败");
			}else {
				dao.updatePreAuthoriza(request, logonUser);
				act.setOutData("succ", 1);
				act.setOutData("msg", "索赔单预授权"+msg+"成功");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权"+msg);
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权"+msg+"失败");
			act.setException(e1);
		}
	}
	
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = dao.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	
	private void getFileByFoId(String foId) {
		List<FsFileuploadPO> fileList = dao.queryAttById(foId);// 取得附件
		act.setOutData("fileList", fileList);
	}
	
	/**
	 * 
	* @Title: authorizationDetailInit 
	* @author: xyfue
	* @Description: 进入索赔单预授权明细页面
	* @param     设定文件 
	* @date 2014年12月1日 下午5:47:19 
	* @return void    返回类型 
	* @throws
	 */
	public void authorizationDetailInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//操作类型
		String operType = CommonUtils.checkNull(request.getParamValue("operType"));
		try {
			Map< String, Object> foInfoMap = dao.authorizationModifyQuery(request,logonUser);
			Map<String, Object> vinInfo =  orderservice.showInfoByVin(request);
			Map<String, Object> userInfo = orderservice.findLoginUserInfo(Long.valueOf(foInfoMap.get("CREATE_BY").toString()));
			
			Map<String, Object> partInfo = null;
			if(null != foInfoMap.get("MAIN_PART_ID")){		
				partInfo = dao.getPartInfo(request,logonUser,foInfoMap);
			}
			//获取三包等级
			String vrLevel = "";
			List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
			if (null != vrLevelList && vrLevelList.size() > 0) {
				vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
			}
			this.getFileByFoId(String.valueOf(foInfoMap.get("ID")));
			
			act.setOutData("userInfo", userInfo);
			act.setOutData("foInfoMap", foInfoMap);
			act.setOutData("vinInfo", vinInfo);
			act.setOutData("partInfo", partInfo);
			act.setOutData("vrLevel", vrLevel);
			act.setOutData("operType", operType);
			act.setForword(AUTHORIZATION_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权明细界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: auditPreAuthoriza 
	* @author: xyfue
	* @Description: 审核索赔单预授权
	* @param     设定文件 
	* @date 2014年12月1日 下午6:26:47 
	* @return void    返回类型 
	* @throws
	 */
	public void auditPreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String foId = CommonUtils.checkNull(request.getParamValue("foId"));
			String OUT_AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_AUDIT_AMOUNT"));
			String OUT_APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_APPLY_AMOUNT"));
			String AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("AUDIT_AMOUNT"));
			String APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("APPLY_AMOUNT"));
			if ("".equals(foId)) {
				act.setOutData("succ", 0);
				act.setOutData("msg", "FO_ID为空,索赔单预授权审核失败");
			}else {
				if((Double.parseDouble("".equals(OUT_AUDIT_AMOUNT)?"0":OUT_AUDIT_AMOUNT) > Double.parseDouble("".equals(OUT_APPLY_AMOUNT)?"0":OUT_APPLY_AMOUNT))
						|| (Double.parseDouble("".equals(AUDIT_AMOUNT)?"0":AUDIT_AMOUNT) > Double.parseDouble("".equals(APPLY_AMOUNT)?"0":APPLY_AMOUNT))
						){
					act.setOutData("succ", 0);
					act.setOutData("msg", "审核金额必须小于等于申请金额");
				}else{
					dao.auditPreAuthoriza(request, logonUser);
					act.setOutData("succ", 1);
					act.setOutData("msg", "索赔单预授权审核成功");
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权审核");
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权审核失败");
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: rejectPreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权退回 
	* @param     设定文件 
	* @date 2014年12月1日 下午6:27:07 
	* @return void    返回类型 
	* @throws
	 */
	public void backPreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String OUT_AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_AUDIT_AMOUNT"));
			String OUT_APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_APPLY_AMOUNT"));
			String AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("AUDIT_AMOUNT"));
			String APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("APPLY_AMOUNT"));
			String foId = CommonUtils.checkNull(request.getParamValue("foId"));
			if ("".equals(foId)) {
				act.setOutData("succ", 0);
				act.setOutData("msg", "FO_ID为空,索赔单预授权退回失败");
			}else {
				if((Double.parseDouble("".equals(OUT_AUDIT_AMOUNT)?"0":OUT_AUDIT_AMOUNT) > Double.parseDouble("".equals(OUT_APPLY_AMOUNT)?"0":OUT_APPLY_AMOUNT))
						|| (Double.parseDouble("".equals(AUDIT_AMOUNT)?"0":AUDIT_AMOUNT) > Double.parseDouble("".equals(APPLY_AMOUNT)?"0":APPLY_AMOUNT))
						){
					act.setOutData("succ", 0);
					act.setOutData("msg", "审核金额必须小于等于申请金额");
				}else{
					dao.backPreAuthoriza(request, logonUser);
					act.setOutData("succ", 1);
					act.setOutData("msg", "索赔单预授权退回成功");
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权退回");
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权退回失败");
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: rejectPreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权拒绝
	* @param     设定文件 
	* @date 2014年12月1日 下午6:27:07 
	* @return void    返回类型 
	* @throws
	 */
	public void rejectPreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String OUT_AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_AUDIT_AMOUNT"));
			String OUT_APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("OUT_APPLY_AMOUNT"));
			String AUDIT_AMOUNT = CommonUtils.checkNull(request.getParamValue("AUDIT_AMOUNT"));
			String APPLY_AMOUNT = CommonUtils.checkNull(request.getParamValue("APPLY_AMOUNT"));
			String foId = CommonUtils.checkNull(request.getParamValue("foId"));
			if ("".equals(foId)) {
				act.setOutData("succ", 0);
				act.setOutData("msg", "FO_ID为空,索赔单预授权拒绝失败");
			}else {
				if((Double.parseDouble("".equals(OUT_AUDIT_AMOUNT)?"0":OUT_AUDIT_AMOUNT) > Double.parseDouble("".equals(OUT_APPLY_AMOUNT)?"0":OUT_APPLY_AMOUNT))
						|| (Double.parseDouble("".equals(AUDIT_AMOUNT)?"0":AUDIT_AMOUNT) > Double.parseDouble("".equals(APPLY_AMOUNT)?"0":APPLY_AMOUNT))
						){
					act.setOutData("succ", 0);
					act.setOutData("msg", "审核金额必须小于等于申请金额");
				}else{
					dao.rejectPreAuthoriza(request, logonUser);
					act.setOutData("succ", 1);
					act.setOutData("msg", "索赔单预授权拒绝成功");
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预拒绝退回");
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权拒绝失败");
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: wastePreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权废弃
	* @param     设定文件 
	* @date 2014年12月1日 下午6:27:07 
	* @return void    返回类型 
	* @throws
	 */
	public void wastePreAuthoriza() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String foId = CommonUtils.checkNull(request.getParamValue("foId"));
			if ("".equals(foId)) {
				act.setOutData("succ", 0);
				act.setOutData("msg", "FO_ID为空,索赔单预授废弃失败");
			}else {
				dao.wastePreAuthoriza(request, logonUser);
				act.setOutData("succ", 1);
				act.setOutData("msg", "索赔单预授权废弃成功");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单预授权废弃");
			logger.error(loginUser, e1);
			act.setOutData("succ", 0);
			act.setOutData("msg", "索赔单预授权废弃失败");
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: getTcCodeListInit 
	* @author: xyfue
	* @Description: 数据字典初始化
	* @param     设定文件 
	* @date 2014年12月4日 下午5:26:46 
	* @return void    返回类型 
	* @throws
	 */
	public void getTcCodeListInit() {
		try {
			act.setOutData("NOT_CODE_ID", getParam("NOT_CODE_ID"));
			act.setOutData("CODE_TYPE", getParam("CODE_TYPE"));
			act.setOutData("idInput", getParam("idInput"));
			act.setOutData("nameInput", getParam("nameInput"));
			act.setForword(TC_CODE_CHOOSE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "数据字典界面初始");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: getTcCodeListInitQuery 
	* @author: xyfue
	* @Description: 数据字典查询
	* @param     设定文件 
	* @date 2014年12月4日 下午5:27:35 
	* @return void    返回类型 
	* @throws
	 */
	public void getTcCodeListInitQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型(2,导出)
			if(common.equals("2")){//导出

			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
								.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getTcCodeList(request,logonUser,curPage,Constant.PAGE_SIZE_MAX);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "数据字典申请查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
}
