package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.AccarPerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAccarPerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class AccarPerManage {
	/**
	 * 
	 * @ClassName     : AccarPerManage 
	 * @Description   : 接车员管理控制类 
	 * @author        : ranjian
	 * CreateDate     : 2013-4-9
	 */
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AccarPerDao reDao = AccarPerDao.getInstance();
	private final String accarPerInitUrl = "/jsp/sales/storage/storagebase/accarPer/accarPerList.jsp";
	private final String addAccarPerUrl = "/jsp/sales/storage/storagebase/accarPer/addAccarPer.jsp";
	private final String editAccarPerUrl = "/jsp/sales/storage/storagebase/accarPer/updateAccarPer.jsp";
												

	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void accarPerInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(accarPerInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"物流商管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 接车员查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void accarPerQuery() {
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String perCode = CommonUtils.checkNull(request.getParamValue("PER_CODE")); // 人员代码
			String perName = CommonUtils.checkNull(request.getParamValue("PER_NAME")); //人员名称
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String status = CommonUtils.checkNull(request.getParamValue("STATUS")); // 状态

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PER_CODE", perCode);
			map.put("PER_NAME", perName);
			map.put("YIELDLY", yieldly);
			map.put("STATUS", status);
			map.put("poseId",logonUser.getPoseId().toString());//权限控制
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getAccarPerQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车员信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 接车员新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void addAccarPerInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(addAccarPerUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"接车员新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		}
	/**
	 * 
	 * @Title      : 
	 * @Description: 接车员修改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void editAccarPerInit(){ 
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String id =  CommonUtils.checkNull(request.getParamValue("Id"));
			Map<String, Object> complaintMap = reDao.getAccarPerMsg(id);
			act.setOutData("complaintMap", complaintMap);	
			act.setForword(editAccarPerUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "接车员修改信息初始化");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加接车员
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void addAccarPer() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String perId=SequenceManager.getSequence("");
			String yieldaly =  CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String perCode =  CommonUtils.checkNull(request.getParamValue("PER_CODE")); //接车员编码
			String perName =  CommonUtils.checkNull(request.getParamValue("PER_NAME")); //接车员名称
			String conTel =  CommonUtils.checkNull(request.getParamValue("CON_TEL")); //联系人电话
			String status =  CommonUtils.checkNull(request.getParamValue("STATUS")); //状态
			String remark =  CommonUtils.checkNull(request.getParamValue("REMARK")); //备注
			TtSalesAccarPerPO tsapp=new TtSalesAccarPerPO();
			tsapp.setPerId(Long.parseLong(perId));
			tsapp.setPerCode(perCode);
			tsapp.setPerName(perName);
			tsapp.setConTel(conTel);
			tsapp.setStatus(Long.parseLong(status));
			tsapp.setRemark(remark);
			tsapp.setYieldly(Long.parseLong(yieldaly));
			tsapp.setPerId(Long.parseLong(perId));
			tsapp.setCreateBy(logonUser.getUserId());//创建人
			tsapp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
			reDao.accarPerAdd(tsapp);
			act.setOutData("returnValue", 1);
		
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增接车员信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改接车员
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void editAccarPer() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String perId= CommonUtils.checkNull(request.getParamValue("PER_ID"));//接车员ID
			String yieldaly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //产地
			String perCode =  CommonUtils.checkNull(request.getParamValue("PER_CODE")); //接车员编码
			String perName =  CommonUtils.checkNull(request.getParamValue("PER_NAME")); //接车员名称
			String conTel =  CommonUtils.checkNull(request.getParamValue("CON_TEL")); //联系人电话
			String status =  CommonUtils.checkNull(request.getParamValue("STATUS")); //状态
			String remark =  CommonUtils.checkNull(request.getParamValue("REMARK")); //备注
			TtSalesAccarPerPO tsapp=new TtSalesAccarPerPO();
			tsapp.setPerId(Long.parseLong(perId));
			tsapp.setPerCode(perCode);
			tsapp.setPerName(perName);
			tsapp.setConTel(conTel);
			tsapp.setStatus(Long.parseLong(status));
			tsapp.setRemark(remark);
			tsapp.setYieldly(Long.parseLong(yieldaly));
			tsapp.setPerId(Long.parseLong(perId));
			tsapp.setUpdateBy(logonUser.getUserId());//修改人
			tsapp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			TtSalesAccarPerPO tsappseach=new TtSalesAccarPerPO();//接车员查询条件
			tsappseach.setPerId(Long.parseLong(perId));
			reDao.accarPerUpdate(tsappseach, tsapp);
			act.setOutData("returnValue", 1);
		
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 修改接车员信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
