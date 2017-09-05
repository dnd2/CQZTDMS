package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.basicData.QualityDao;
import com.infodms.dms.dao.claim.basicData.WarningTimeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWarningTimePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName     : WarningTime 
 * @Description   : 预警时间规则维护
 * @author        : luole
 * CreateDate     : 2013-4-17
 */
public class WarningTime {
	private Logger logger = Logger.getLogger(WarningTime.class);
	private final WarningTimeDao  dao = WarningTimeDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	
	private final String WARNING_TIME_QUERY_URL = "/jsp/claim/basicData/warningTimeQueryInit.jsp";//主页面（查询）
	private final String WARNING_TIME_ADD_URL = "/jsp/claim/basicData/warningTimeAdd.jsp";//新增页面
	private final String WARNING_TIME_UPDATE_URL = "/jsp/claim/basicData/warningTimeUpdate.jsp";//新增页面
	
	/**
	 * @Title      : warningTimeQueryInit
	 * @Description: 查询页面初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-17
	 */
	public void warningTimeQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(WARNING_TIME_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"预警时间规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * @Title      : 新增初始化
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public void warningTimeAddInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("authinfo", dao.getAuthinfo("",logonUser.getCompanyId().toString()));
			act.setForword(WARNING_TIME_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"预警时间规则维护新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * @Title      : 新增
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public void warningTimeAdd(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String wainingCode = CommonUtils.checkNull(request.getParamValue("WAINING_CODE"));  //预警规则
			String wainingRemark = CommonUtils.checkNull(request.getParamValue("WAINING_REMARK"));  //预警说明
			String warningLevel = CommonUtils.checkNull(request.getParamValue("WARNING_LEVEL"));// 预警等级
			String warningType = CommonUtils.checkNull(request.getParamValue("WARNING_TYPE"));//预警类型
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));  //状态
			String validDate = CommonUtils.checkNull(request.getParamValue("VALID_DATE")); //有效期(月)
			String validMileage = CommonUtils.checkNull(request.getParamValue("VALID_MILEAGE")); // 有效里程(公里)
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 预警起时间(日)
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); //预警止时间(日)
			String clauseStatute = CommonUtils.checkNull(request.getParamValue("CLAUSE_STATUTE")); //法规条款
			String approvalLever = request.getParamValue("APPROVAL_LEVER"); //授权级别
			
			//保存数据
			TtAsWarningTimePO po = new TtAsWarningTimePO();
			po.setId(Long.parseLong(SequenceManager.getSequence(""))); //ID 
			po.setWarningCode(wainingCode.toUpperCase());
			po.setWainingRemark(wainingRemark);
			po.setWainingLevel(Integer.parseInt(warningLevel));
			po.setWainingType(Integer.parseInt(warningType));
			po.setStatus(Integer.parseInt(status));
			po.setValidDate(Integer.parseInt(validDate));
			po.setValidMileage(Integer.parseInt(validMileage));
			po.setWarningTimeStart(Integer.parseInt(startDate));
			po.setWarningTimeEnd(Integer.parseInt(endDate));
			po.setClauseStatute(clauseStatute);
			po.setApprovalLeverCode(approvalLever);
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			dao.insert(po);
			
			act.setOutData("flag", "success");
			this.warningTimeQueryInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"预警时间规则维护新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * @Title      : 修改初始化
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public void warningTimeUpdateInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));  //ID
			this.UpdateInit(id);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"预警时间规则维护新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	//进入修改页面
	protected void  UpdateInit(String id){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TtAsWarningTimePO basePo = new TtAsWarningTimePO();
			basePo.setId(Long.parseLong(id));
			List<PO> warnlist = dao.select(basePo);
			if(warnlist.size()>0)
				basePo = (TtAsWarningTimePO)warnlist.get(0);
			act.setOutData("warninfo", basePo);
			act.setOutData("selAuthinfo", dao.getAuthinfo(basePo.getApprovalLeverCode(),logonUser.getCompanyId().toString())); //当前选中的
			act.setOutData("allAuthinfo", dao.getAuthinfo("",logonUser.getCompanyId().toString()));  //所有的
			act.setForword(WARNING_TIME_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"预警时间规则维护新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * @Title      : 修改
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public void warningTimeUpdate(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));  //预警规则
			String wainingCode = CommonUtils.checkNull(request.getParamValue("WAINING_CODE"));  //预警规则
			String wainingRemark = CommonUtils.checkNull(request.getParamValue("WAINING_REMARK"));  //预警说明
			String warningLevel = CommonUtils.checkNull(request.getParamValue("WARNING_LEVEL"));// 预警等级
			String warningType = CommonUtils.checkNull(request.getParamValue("WARNING_TYPE"));//预警类型
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));  //状态
			String validDate = CommonUtils.checkNull(request.getParamValue("VALID_DATE")); //有效期(月)
			String validMileage = CommonUtils.checkNull(request.getParamValue("VALID_MILEAGE")); // 有效里程(公里)
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 预警起时间(日)
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); //预警止时间(日)
			String clauseStatute = CommonUtils.checkNull(request.getParamValue("CLAUSE_STATUTE")); //法规条款
			String approvalLever = request.getParamValue("APPROVAL_LEVER"); //授权级别
			
			TtAsWarningTimePO oldpo = new TtAsWarningTimePO();
			oldpo.setId(Long.parseLong(id));
			//保存数据
			TtAsWarningTimePO po = new TtAsWarningTimePO();
			po.setWarningCode(wainingCode.toUpperCase());
			po.setWainingRemark(wainingRemark);
			po.setWainingLevel(Integer.parseInt(warningLevel));
			po.setWainingType(Integer.parseInt(warningType));
			po.setStatus(Integer.parseInt(status));
			po.setValidDate(Integer.parseInt(validDate));
			po.setValidMileage(Integer.parseInt(validMileage));
			po.setWarningTimeStart(Integer.parseInt(startDate));
			po.setWarningTimeEnd(Integer.parseInt(endDate));
			po.setClauseStatute(clauseStatute);
			po.setApprovalLeverCode(approvalLever);
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			dao.update(oldpo, po);
			//this.UpdateInit(id);
			act.setOutData("flag", "success");
			this.warningTimeQueryInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"预警时间规则维护新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * @Title      : 主页面分布查询
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-18
	 */
	public void warningTimeQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sql = new StringBuffer();
		try {
			act.getResponse().setContentType("application/json");//设置返回类型
			//获得上查询条件列表
			String wainingCode = CommonUtils.checkNull(request.getParamValue("WAINING_CODE"));  //预警规则
			String wainingRemark = CommonUtils.checkNull(request.getParamValue("WAINING_REMARK"));  //预警说明
			String warningLevel = CommonUtils.checkNull(request.getParamValue("WARNING_LEVEL"));// 预警等级
			String warningType = CommonUtils.checkNull(request.getParamValue("WARNING_TYPE"));//预警类型
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));  //状态
			if(wainingCode!=null && !wainingCode.equals("")){//工时代码 不区分大小写
				sql.append("and wt.WARNING_CODE like '%"+wainingCode.toUpperCase()+"%'\n");
			}
			if(wainingRemark!=null && !wainingRemark.equals("")){
				sql.append("and wt.WAINING_REMARK like '%"+wainingRemark+"%'\n");
			}
			if(warningLevel!=null && !warningLevel.equals("")){
				sql.append("and wt.WAINING_LEVEL ="+warningLevel+"\n");
			}
			if(warningType!=null && !warningType.equals("")){
				sql.append("and wt.WAINING_TYPE ="+warningType+"\n");
			}
			if(status!=null && !status.equals("")){
				sql.append("and wt.STATUS = "+status+" \n");
			}
			sql.append("ORDER BY  WT.WAINING_TYPE,WT.WAINING_level desc \n");
			Integer curPage = request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;
			PageResult<Map<String, Object>> ps = dao.warningTimeQuery( sql.toString() ,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
