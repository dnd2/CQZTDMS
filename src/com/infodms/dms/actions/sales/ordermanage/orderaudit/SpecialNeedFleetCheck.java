/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsSpecialReqChkPO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 *
 */
public class SpecialNeedFleetCheck {
	public Logger logger = Logger.getLogger(SpecialNeedFleetCheck.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedFleetCheckQuery.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedFleetDetailCheck.jsp";
	/**
	 * 订做车需求大客户审核页面初始化
	 */
	public void specialNeedFleetCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求大客户审核页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求大客户审核查询
	 */
	public void specialNeedFleetCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			String dealerCode = request.getParamValue("dealerCode");	//经销商CODE
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedFleetCheckList(areaId, area, dealerCode, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求大客户审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求大客户审核明细查询
	 */
	public void specialNeedFleetDetailCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			String ver = request.getParamValue("ver");
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			List<Map<String, Object>> list = dao.getSpecialNeedDetailCheckList(reqId);
			List<Map<String, Object>> attachList = dao.getAttachInfos(reqId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetName = fleet.getFleetName();
				}
			}
			act.setOutData("list", list);
			act.setOutData("reqId", reqId);
			act.setOutData("ver", ver);
			act.setOutData("remark", remark);
			act.setOutData("fleetName", fleetName);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求大客户审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求大客户明细审核保存
	 */
	public void specialNeedFleetDetailCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String flag = request.getParamValue("flag");						//页面参数
			String reqId = request.getParamValue("reqId");						//需求ID
			String ver = request.getParamValue("ver");							//需求ID
			String remark = request.getParamValue("remark");					//审核描述
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_SPECIAL_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				TtVsSpecialReqPO tvsrpContion =  new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpValue =  new TtVsSpecialReqPO();
				tvsrpContion.setReqId(Long.parseLong(reqId));
				if("0".equals(flag)){
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);
				}
				if("1".equals(flag)){
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_05);
				}
				tvsrpValue.setVer(Integer.parseInt(ver)+1);
				tvsrpValue.setUpdateBy(logonUser.getUserId());
				tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvsrpContion, tvsrpValue);			//更新订做车需求表
				
				TtVsSpecialReqChkPO tvsrcp =  new TtVsSpecialReqChkPO();
				tvsrcp.setChkId(Long.parseLong(SequenceManager.getSequence("")));
				tvsrcp.setReqId(Long.parseLong(reqId));
				tvsrcp.setChkDesc(remark);
				tvsrcp.setChkDate(new Date(System.currentTimeMillis()));
				tvsrcp.setChkOrgId(logonUser.getOrgId());
				tvsrcp.setChkUserId(logonUser.getUserId());
				if("0".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_05);
				}
				if("1".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_06);
				}
				tvsrcp.setCreateBy(logonUser.getOrgId());
				tvsrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvsrcp);		
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"订做车需求大客户明细审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
