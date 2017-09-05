package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.feedbackMng.ServiceCarWCDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsServicecarAuditPO;
import com.infodms.dms.po.TtAsServicecarPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 微车
 * 服务车申请，审核类
 * @author nova_zuo
 *
 */
public class ServiceCarWCAction {
	private ActionContext act ;
	private RequestWrapper req ;
	private AclUserBean logonUser ;
	private Logger logger = Logger.getLogger(ServiceCarWCAction.class);
	private ServiceCarWCDao dao = ServiceCarWCDao.getInstance() ;
	
	private final String APPLY_INIT_URL = "/jsp/feedbackMng/apply/servicecarApplyInitVC.jsp" ;  //服务商申请服务车初始化页面
	private final String ADD_SERVICECAR_URL = "/jsp/feedbackMng/apply/addServicecarVC.jsp" ;    //新增服务车申请页面
	private final String MODIFY_SERVICECAR_URL = "/jsp/feedbackMng/apply/modifyServicecarVC.jsp" ;//修改服务车页面
	private final String AREA_APPROVE_URL = "/jsp/feedbackMng/approve/areaApproveVC.jsp" ;//事业部初审初始化页面
	private final String AREA_APPROVE_DO_URL = "/jsp/feedbackMng/approve/areaApproveDoVC.jsp" ;//事业部真正审核页面初始化
	private final String FANCE_APPROVE_URL = "/jsp/feedbackMng/approve/fanceApproveVC.jsp" ;//财务初审初始化页面
	private final String FANCE_APPROVE_DO_URL = "/jsp/feedbackMng/approve/fanceApproveDoVC.jsp" ;//财务初审真正页面
	private final String DETAIL_URL = "/jsp/feedbackMng/approve/serviceCarDetailVC.jsp" ;//服务车申请明细页面
	private final String OEM_APPROVE_URL = "/jsp/feedbackMng/approve/oemApproveVC.jsp" ;//服务车总部审核初始化页面
	private final String OEM_APPROVE_DO_URL = "/jsp/feedbackMng/approve/oemApproveDoVC.jsp" ;//服务车总部审核真正页面
	
	private final String FILES_UPLOAD_URL = "/jsp/feedbackMng/apply/filesUploadVC.jsp" ;//服务车资料上传初始化页面
	private final String FILES_UPLOAD_DO_URL = "/jsp/feedbackMng/apply/filesUploadDoVC.jsp" ;//服务车资料上传实现页面
	private final String OEM_FILES_QUERY_URL = "/jsp/feedbackMng/approve/oemFilesQueryVC.jsp" ;//服务车资料总部查询主页面
	private final String OEM_FILES_APPROVE_DO_URL = "/jsp/feedbackMng/approve/oemFilesApproveDoVC.jsp" ;//服务车资料总部审核页面
	private final String SALES_FILES_QUERY_URL = "/jsp/feedbackMng/approve/salesFilesQueryVC.jsp" ;//服务车资料总部查询主页面
	private final String SALES_FILES_APPROVE_DO_URL = "/jsp/feedbackMng/approve/salesFilesApproveDoVC.jsp" ;//服务车销售部审核页面
	private final String OEM_MAILS_QUERY_URL = "/jsp/feedbackMng/approve/oemMailsQueryVC.jsp" ;//服务车资料总部签收查询页面
	private final String OEM_MAILS_GET_DO_URL = "/jsp/feedbackMng/approve/oemMailsGetVC.jsp" ;//服务车资料总部签收明细页面
	private final String SALES_MAILS_QUERY_URL = "/jsp/feedbackMng/approve/salesMailsQueryVC.jsp" ;//服务车资料销售部签收
	private final String SALES_MAILS_GET_DO_URL = "/jsp/feedbackMng/approve/salesMailsGetVC.jsp" ;//服务车资料销售部签收
	
	private final String PRINT_INIT_URL = "/jsp/feedbackMng/apply/printInitVC.jsp" ; //服务车打印查询页面
	private final String PRINT_URL = "/jsp/feedbackMng/apply/printVC.jsp" ; //服务车打印页面
	/*
	 * 服务商申请服务车首页
	 */
	public void applyUrlInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(APPLY_INIT_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表首页");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务商服务车申请查询方法
	 */
	public void queryByDealer(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String status = req.getParamValue("status");
			String b_date = req.getParamValue("b_date");
			String e_date = req.getParamValue("e_date");
			String applyNo = req.getParamValue("applyNo");
			StringBuffer con = new StringBuffer();
			con.append("  and s.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			if(StringUtil.notNull(applyNo))
				con.append("  and s.apply_no like '%").append(applyNo).append("%'\n");
			if(StringUtil.notNull(b_date))
				con.append("  and trunc(s.make_date) >= to_date('").append(b_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(e_date))
				con.append("  and trunc(s.make_date) <= to_date('").append(e_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(status))
				con.append("  and s.status = ").append(status).append("\n");
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage")==null?1
					:Integer.parseInt(req.getParamValue("curPage"));//分页首页代码
			
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务商新增服务车页面
	 */
	public void addServicecarInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerId(Long.parseLong(logonUser.getDealerId()));
			List list = dao.select(dealer);
			if(list.size()>0)
				act.setOutData("dealer", (TmDealerPO)list.get(0));
			act.setOutData("date", new Date());
			act.setForword(ADD_SERVICECAR_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表新增页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 保存此次服务车申请表方法
	 */
	public void servicecarSave(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealer_id = req.getParamValue("d_id");  //经销商ID
			String dealer_phone = req.getParamValue("d_phone");
			String faxNo = req.getParamValue("faxNo");
			String linkMan = req.getParamValue("linkMan");
			String linkPhone = req.getParamValue("linkPhone");
			String model = req.getParamValue("model"); //车型代码
			String model_id = req.getParamValue("model_id");//车型ID
			String carType = req.getParamValue("car_type");
			String remark = req.getParamValue("remark");
			String type = req.getParamValue("type");
			Integer status = Constant.SERVICE_CAR_APPLY_01 ;
			if("2".equals(type))
				status = Constant.SERVICE_CAR_APPLY_02 ;
			
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerId(Long.parseLong(logonUser.getDealerId()));
			List list = dao.select(dealer);
			if(list.size()>0)
				dealer = (TmDealerPO)list.get(0);
			
			TtAsServicecarPO po = new TtAsServicecarPO();
			String id = SequenceManager.getSequence("") ;
			po.setId(Utility.getLong(id));
			po.setApplyNo(SequenceManager.getSequence("FVC"));
			po.setDealerId(Long.parseLong(logonUser.getDealerId()));
			po.setDealerId2(Long.parseLong(dealer_id));
			po.setFaxNo(faxNo);
			po.setCarType(Integer.parseInt(carType));
			po.setDealerPhone(dealer_phone);
			po.setModelCode(model);
			po.setDealerLevel(dealer.getDealerLevel());
			po.setDealerType(dealer.getDealerClass());
			po.setModelId(Long.parseLong(model_id));
			po.setLinkMan(linkMan);
			po.setLinkPhone(linkPhone);
			if("2".equals(type))
				po.setMakeDate(new Date());
			po.setRemark(remark);
			po.setStatus(status);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			
			dao.insert(po);
			
			if("2".equals(type)){
				//同时添加一个审核记录进审核明细表中
				TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
				audit.setApplyId(Long.parseLong(id));
				audit.setAuditMan(logonUser.getUserId());
				audit.setAuditStatus(status);
				audit.setCreateBy(logonUser.getUserId());
				audit.setCreateDate(new Date());
				audit.setId(Utility.getLong(SequenceManager.getSequence("")));
				audit.setRemark(remark);
				
				dao.insert(audit);
			}
			
			this.applyUrlInit() ;
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"服务车申请表新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 进入服务车明细页面
	 */
	public void servicecarModifyInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			act.setForword(MODIFY_SERVICECAR_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表新增页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 
	 */
	public void servicecarModify(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String dealer_phone = req.getParamValue("d_phone");
			String linkMan = req.getParamValue("linkMan");
			String linkPhone = req.getParamValue("linkPhone");
			String faxNo = req.getParamValue("faxNo");
			String model = req.getParamValue("model"); //车型代码
			String model_id = req.getParamValue("model_id");//车型ID
			String carType = req.getParamValue("car_type");
			String remark = req.getParamValue("remark");
			String dealer_id = req.getParamValue("d_id");
			String type = req.getParamValue("type") ;
			Integer status = Constant.SERVICE_CAR_APPLY_01 ;
			if("2".equals(type))
				status = Constant.SERVICE_CAR_APPLY_02 ;
			
			TtAsServicecarPO po = new TtAsServicecarPO() ;
			TtAsServicecarPO po1 = new TtAsServicecarPO() ;
			po.setId(Long.parseLong(id));
			po.setDealerPhone(dealer_phone);
			po.setLinkMan(linkMan);
			po.setLinkPhone(linkPhone);
			po.setFaxNo(faxNo);
			if("2".equals(type))
				po.setMakeDate(new Date());
			po.setModelCode(model);
			po.setDealerId2(Long.parseLong(dealer_id));
			po.setModelId(Long.parseLong(model_id));
			po.setCarType(Integer.parseInt(carType));
			po.setRemark(remark);
			po.setStatus(status);
			po1.setId(Long.parseLong(id));
			
			dao.update(po1, po);
			
			if("2".equals(type)){
				//同时添加一个审核记录进审核明细表中
				TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
				audit.setApplyId(Long.parseLong(id));
				audit.setAuditMan(logonUser.getUserId());
				audit.setAuditStatus(status);
				audit.setCreateBy(logonUser.getUserId());
				audit.setCreateDate(new Date());
				audit.setId(Utility.getLong(SequenceManager.getSequence("")));
				audit.setRemark(remark);
				
				dao.insert(audit);
			}
			
			this.applyUrlInit();
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*************** OEM  ***************/
	/*
	 * 事业部初审页面初始化
	 */
	public void areaQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(AREA_APPROVE_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 事业部审核查询
	 */
	public void areaQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealer_id = req.getParamValue("d_id");
			String applyNo = req.getParamValue("applyNo");
			String status = req.getParamValue("status");
			String b_date = req.getParamValue("b_date");
			String e_date = req.getParamValue("e_date");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(applyNo))
				con.append("  and s.apply_no like '%").append(applyNo).append("%'\n");
			if(StringUtil.notNull(dealer_id))
				con.append("  and s.dealer_id = ").append(dealer_id).append("\n");
			if(StringUtil.notNull(b_date))
				con.append("  and trunc(s.make_date) >= to_date('").append(b_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(e_date))
				con.append("  and trunc(s.make_date) <= to_date('").append(e_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(status))
				con.append("  and s.status = ").append(status).append("\n");
			con.append("   and v.root_org_id =").append(logonUser.getOrgId()).append("\n");
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage")==null?1
					:Integer.parseInt(req.getParamValue("curPage"));//分页首页代码
			
			PageResult<Map<String,Object>> ps = dao.areaQuery(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 事业部真正审核页面初始化
	 */
	public void areaApproveInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("list", list);
			act.setForword(AREA_APPROVE_DO_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 事业部审核方法
	 */
	public void areaApprove(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			boolean flag = true ; //区分区域审核与总部审核，最终实现页面跳转判断。
			String id = req.getParamValue("id");
			String remark = req.getParamValue("remark");
			String type = req.getParamValue("type");
			Integer status = Constant.SERVICE_CAR_APPLY_03 ;
			if("2".equals(type))
				status = Constant.SERVICE_CAR_APPLY_04 ;
			else if("3".equals(type))
				status = Constant.SERVICE_CAR_APPLY_05 ;
			else if("8".equals(type)){
				status = Constant.SERVICE_CAR_APPLY_08 ;
				flag = false ;
			}
			else if("9".equals(type)){			
				status = Constant.SERVICE_CAR_APPLY_09 ;
				flag = false ;
			}
			
			TtAsServicecarPO po = new TtAsServicecarPO();
			TtAsServicecarPO po1 = new TtAsServicecarPO();
			po.setId(Long.parseLong(id));
			po.setStatus(status);
			//如果总部通过，则将文件状态置为“未上传”，然后经销商端就能上传文件了
			if("8".equals(type))
				po.setFilesStatus(Constant.SERVICE_CAR_FILES_01);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po1.setId(Long.parseLong(id));
			
			dao.update(po1, po);
			
			//同时添加一个审核记录进审核明细表中
			TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
			audit.setApplyId(Long.parseLong(id));
			audit.setAuditMan(logonUser.getUserId());
			audit.setAuditStatus(status);
			audit.setCreateBy(logonUser.getUserId());
			audit.setCreateDate(new Date());
			audit.setId(Utility.getLong(SequenceManager.getSequence("")));
			audit.setRemark(remark);
			
			dao.insert(audit);
			
			if(flag)
				this.areaQueryInit() ;
			else
				this.oemQueryInit() ;
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车申请明细
	 */
	public void showDetail(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.parseLong(id));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("lists",lists);
			act.setOutData("list", list);
			act.setForword(DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车财务初审初始化页面初始化
	 */
	public void fanceQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(FANCE_APPROVE_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 财务查询方法
	 */
	public void fanceQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealer_id = req.getParamValue("d_id");
			String applyNo = req.getParamValue("applyNo");
			String status = req.getParamValue("status");
			String b_date = req.getParamValue("b_date");
			String e_date = req.getParamValue("e_date");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(applyNo))
				con.append("  and s.apply_no like '%").append(applyNo).append("%'\n");
			if(StringUtil.notNull(dealer_id))
				con.append("  and s.dealer_id = ").append(dealer_id).append("\n");
			if(StringUtil.notNull(b_date))
				con.append("  and trunc(s.make_date) >= to_date('").append(b_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(e_date))
				con.append("  and trunc(s.make_date) <= to_date('").append(e_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(status))
				con.append("  and s.status = ").append(status).append("\n");
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage")==null?1
					:Integer.parseInt(req.getParamValue("curPage"));//分页首页代码
			
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 财务部真正审核页面初始化
	 */
	public void fanceApproveInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("list", list);
			act.setForword(FANCE_APPROVE_DO_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 财务初审真正实现
	 */
	public void fanceApprove(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String remark = req.getParamValue("remark");
			String p1 = req.getParamValue("standardPrice");
			String p2 = req.getParamValue("supportQuota");
			String p3 = req.getParamValue("supportQuota2");
			String p4 = req.getParamValue("discountAmount");
			String type = req.getParamValue("type");
			Integer status = Constant.SERVICE_CAR_APPLY_06 ;
			if("2".equals(type))
				status = Constant.SERVICE_CAR_APPLY_07 ;
			
			TtAsServicecarPO po = new TtAsServicecarPO();
			TtAsServicecarPO po1 = new TtAsServicecarPO();
			po.setId(Long.parseLong(id));
			po.setStatus(status);
		    if(StringUtil.notNull(p1))
		    	po.setStandardPrice(Double.parseDouble(p1));
		    if(StringUtil.notNull(p2))
		    	po.setSupportQuota(Double.parseDouble(p2));
		    if(StringUtil.notNull(p3))
		    	po.setSupportQuota2(Double.parseDouble(p3));
		    if(StringUtil.notNull(p4))
		    	po.setDiscountAmount(Double.parseDouble(p4));
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po1.setId(Long.parseLong(id));
			
			dao.update(po1, po);
			
			//同时添加一个审核记录进审核明细表中
			TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
			audit.setApplyId(Long.parseLong(id));
			audit.setAuditMan(logonUser.getUserId());
			audit.setAuditStatus(status);
			audit.setCreateBy(logonUser.getUserId());
			audit.setCreateDate(new Date());
			audit.setId(Utility.getLong(SequenceManager.getSequence("")));
			audit.setRemark(remark);
			
			dao.insert(audit);
			
			this.fanceQueryInit() ;
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车总部审核
	 */
	public void oemQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(OEM_APPROVE_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车总部审核明细页面初始化
	 */
	public void oemApproveInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("list", list);
			act.setForword(OEM_APPROVE_DO_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表审批页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/***************** DLR 服务资料上传 *********************/
	/*
	 * 经销商服务资料上传初始化页面
	 */
	public void applyFilesUrlInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(FILES_UPLOAD_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料上传页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 经销商资料上传查询方法
	 */
	public void filesQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String status = req.getParamValue("status");
			String b_date = req.getParamValue("b_date");
			String e_date = req.getParamValue("e_date");
			String applyNo = req.getParamValue("applyNo");
			StringBuffer con = new StringBuffer();
			con.append("  and s.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			if(StringUtil.notNull(applyNo))
				con.append("  and s.apply_no like '%").append(applyNo).append("%'\n");
			if(StringUtil.notNull(b_date))
				con.append("  and trunc(s.make_date) >= to_date('").append(b_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(e_date))
				con.append("  and trunc(s.make_date) <= to_date('").append(e_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(status))
				con.append("  and s.files_status = ").append(status).append("\n");
			else
				con.append("  and s.files_status is not null \n");
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage")==null?1
					:Integer.parseInt(req.getParamValue("curPage"));//分页首页代码
			
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 上传资料页面初始化
	 */
	public void filesUploadInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			act.setOutData("list", list);
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.parseLong(id));
			List<FsFileuploadPO> lists = dao.select(detail);
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("lists",lists);
			act.setForword(FILES_UPLOAD_DO_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料上传页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 经销商上传资料真正实现
	 */
	public void filesUpload(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id") ;
			String vin = req.getParamValue("vin");
			String engineNo = req.getParamValue("engine_no");
			String yieldly = req.getParamValue("yieldly");
			String remark = req.getParamValue("remark");
			String type = req.getParamValue("type");
			Integer status = Constant.SERVICE_CAR_FILES_01 ;
			if("2".equals(type))
				status = Constant.SERVICE_CAR_FILES_02 ;
			
			TtAsServicecarPO po = new TtAsServicecarPO();
			TtAsServicecarPO po1 = new TtAsServicecarPO();
			po.setId(Long.parseLong(id));
			po.setFilesStatus(status);
			po.setVin(vin);
			po.setEngineNo(engineNo);
			if(StringUtil.notNull(yieldly) && !"null".equals(yieldly))
				po.setYieldly(Integer.parseInt(yieldly));
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po1.setId(Long.parseLong(id));
			
			dao.update(po1, po);
			
			if("2".equals(type)){
				//同时添加一个审核记录进审核明细表中
				TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
				audit.setApplyId(Long.parseLong(id));
				audit.setAuditMan(logonUser.getUserId());
				audit.setAuditStatus(status);
				audit.setCreateBy(logonUser.getUserId());
				audit.setCreateDate(new Date());
				audit.setId(Utility.getLong(SequenceManager.getSequence("")));
				audit.setRemark(remark);
				
				dao.insert(audit);
			}
			//附件信息管理
			String[] fjids = req.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(id, fjids);
			FileUploadManager.fileUploadByBusiness(id, fjids, logonUser);
			
			this.applyFilesUrlInit();
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料上传页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料总部审核
	 */
	public void oemFilesQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(OEM_FILES_QUERY_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料总部审核页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料查询公用方法
	 */
	public void serviceCarFilesQuery(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealer_id = req.getParamValue("d_id");
			String applyNo = req.getParamValue("applyNo");
			String status = req.getParamValue("status");
			String b_date = req.getParamValue("b_date");
			String e_date = req.getParamValue("e_date");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(applyNo))
				con.append("  and s.apply_no like '%").append(applyNo).append("%'\n");
			if(StringUtil.notNull(dealer_id))
				con.append("  and s.dealer_id = ").append(dealer_id).append("\n");
			if(StringUtil.notNull(b_date))
				con.append("  and trunc(s.make_date) >= to_date('").append(b_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(e_date))
				con.append("  and trunc(s.make_date) <= to_date('").append(e_date).append("','yyyy-MM-dd')\n");
			if(StringUtil.notNull(status))
				con.append("  and s.files_status = ").append(status).append("\n");
			else
				con.append("  and s.files_status is not null \n");
			int pageSize = 10 ;
			Integer curPage = req.getParamValue("curPage")==null?1
					:Integer.parseInt(req.getParamValue("curPage"));//分页首页代码
			
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车资料查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料审核公用方法
	 */
	public void filesApproveInit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String type = req.getParamValue("type") ;//各级审核页面区分
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.queryAuditDetail(id) ;
			act.setOutData("list", list);
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.parseLong(id));
			List<FsFileuploadPO> lists = dao.select(detail);
			List list2 = dao.queryRelation(id);
			act.setOutData("relations", list2);
			act.setOutData("lists",lists);
			act.setOutData("type", type);
			
			if("1".equals(type))
				act.setForword(OEM_FILES_APPROVE_DO_URL);
			else if("2".equals(type))
				act.setForword(SALES_FILES_APPROVE_DO_URL);
			else if("3".equals(type))
				act.setForword(OEM_MAILS_GET_DO_URL) ;
			else if("4".equals(type))
				act.setForword(SALES_MAILS_GET_DO_URL) ;
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料上传页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车审核公用方法
	 */
	public void filesApprove(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id") ;
			String remark = req.getParamValue("remark");
			String type = req.getParamValue("type"); //资料审核页面区分
			Integer status = Integer.parseInt(req.getParamValue("status"));
			
			TtAsServicecarPO po = new TtAsServicecarPO();
			TtAsServicecarPO po1 = new TtAsServicecarPO();
			po.setId(Long.parseLong(id));
			po.setFilesStatus(status);
			if(status.toString().equals(Constant.SERVICE_CAR_FILES_12.toString()))
				po.setStatus(Constant.SERVICE_CAR_APPLY_10);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po1.setId(Long.parseLong(id));
			
			dao.update(po1, po);
			
			//同时添加一个审核记录进审核明细表中
			TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
			audit.setApplyId(Long.parseLong(id));
			audit.setAuditMan(logonUser.getUserId());
			audit.setAuditStatus(status);
			audit.setCreateBy(logonUser.getUserId());
			audit.setCreateDate(new Date());
			audit.setId(Utility.getLong(SequenceManager.getSequence("")));
			audit.setRemark(remark);
			
			dao.insert(audit);
			
			if("1".equals(type))
				act.setForword(OEM_FILES_QUERY_URL) ;
			else if("2".equals(type))
				act.setForword(SALES_FILES_QUERY_URL) ;
			else if("3".equals(type))
				act.setForword(OEM_MAILS_QUERY_URL) ;
			else if("4".equals(type))
				act.setForword(SALES_MAILS_QUERY_URL) ;
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料审核页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料销售部审核
	 */
	public void salesFilesQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(SALES_FILES_QUERY_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料销售部审核页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料总部签收页面初始化
	 */
	public void oemMailsQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(OEM_MAILS_QUERY_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料总部签收页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务车资料销售部签收页面初始化
	 */
	public void salesMailsQueryInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(SALES_MAILS_QUERY_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料销售部签收页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**************** 服务车打印功能 **********/
	public void serviceCarPrintInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PRINT_INIT_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车资料销售部签收页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void goPrint(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String con = "  and s.id = "+id;
			PageResult<Map<String,Object>> ps = dao.queryServicecar(con, 2, 1);
			if(ps.getTotalRecords()>0){
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("map", map);
			}
			List list = dao.printAuditDetail(id) ;
			act.setOutData("list", list);
			act.setForword(PRINT_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表打印页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//回退功能
	public void goLast(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String type = req.getParamValue("type");
			StringBuffer con = new StringBuffer() ;
			Integer status = Constant.GO_LAST_01 ;
			con.append("update tt_as_servicecar s set s.update_by = ");
			con.append(logonUser.getUserId());
			con.append(",s.update_date = sysdate ");
			if("1".equals(type))
				con.append(",s.status = "+Constant.SERVICE_CAR_APPLY_03+" where s.id = "+id+" and s.status = "+Constant.SERVICE_CAR_APPLY_06);
			else if("2".equals(type)){
				con.append(",s.files_status = '' ,s.status = "+Constant.SERVICE_CAR_APPLY_06+" where s.id = "+id+" and s.status = "+Constant.SERVICE_CAR_APPLY_08) ;
				con.append(" and s.files_status = ").append(Constant.SERVICE_CAR_FILES_01);
				status = Constant.GO_LAST_02 ;
			}
			int count = dao.update(con.toString(), null);
			
			//同时添加一个审核记录进审核明细表中
			if(count>0){
				TtAsServicecarAuditPO audit = new TtAsServicecarAuditPO();
				audit.setApplyId(Long.parseLong(id));
				audit.setAuditMan(logonUser.getUserId());
				audit.setAuditStatus(status);
				audit.setCreateBy(logonUser.getUserId());
				audit.setCreateDate(new Date());
				audit.setId(Utility.getLong(SequenceManager.getSequence("")));
				
				dao.insert(audit);
				
				act.setOutData("flag", 1);
			}
			else act.setOutData("flag",2);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车审核回退功能");
			logger.error(logonUser,e1);
			act.setOutData("flag",3);
			act.setException(e1);
		}
	}
	
	//车架号与发动机号匹配性验证
	public void queryEngine() throws Exception{
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String vin = req.getParamValue("vin");
			TmVehiclePO po = new TmVehiclePO() ;
			po.setVin(vin);
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(vin);
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			List list = dao.select(po);
			if(list.size()>0){
				act.setOutData("engineNo",((TmVehiclePO)list.get(0)).getEngineNo());
				act.setOutData("yieldly", ((TmVehiclePO)list.get(0)).getYieldly());
			}
			else
				act.setOutData("flag",true);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务车申请表打印页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
