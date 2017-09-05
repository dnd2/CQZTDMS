package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.SendPlanSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesCheckParPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
/**
 * 
 * @ClassName     : SendPlanSet 
 * @Description   : 发运计划自动审核参数控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-9
 */
public class SendPlanSet {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendPlanSetDao reDao = SendPlanSetDao.getInstance();
	private final String sendPlanSetInitUrl = "/jsp/sales/storage/storagebase/sendPlanSet/sendPlanSet.jsp";
												

	/**
	 * 
	 * @Title      : 
	 * @Description: 发运计划自动审核参数初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void sendPlanSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String seachY=yieldly;	
			if(yieldly==null || "".equals(yieldly)){//首次进入
				if(list_yieldly.size()>0){
					Map<String, Object> map=(Map<String, Object>)list_yieldly.get(0);//获取第一个 如有多个情况下
					seachY=map.get("AREA_ID").toString();
				}
			}

			Map<String,Object> complaintMap =reDao.getSendPlanSetQuery(seachY);
			act.setOutData("seachY", seachY);
			act.setOutData("complaintMap", complaintMap);
			act.setForword(sendPlanSetInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划自动审核参数初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 发运计划自动审核参数保存
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void saveSendPlanSet(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String parId = request.getParamValue("PAR_ID"); // 参数ID
			
			String userId = logonUser.getUserId().toString();
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String parValue = request.getParamValue("PAR_VALUE");// 参数值


			if(parId==null || parId==""){//添加
				String newParId = SequenceManager.getSequence(""); // 参数ID
				TtSalesCheckParPO tscpp=new TtSalesCheckParPO();
				tscpp.setParId(Long.parseLong(newParId));
				tscpp.setParValue(Long.parseLong(parValue));
				tscpp.setYieldly(Long.parseLong(yieldly));
				tscpp.setCreateBy(Long.parseLong(userId));
				tscpp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				reDao.sendPlanSetAdd(tscpp);
			}else{//修改
				TtSalesCheckParPO tscpp=new TtSalesCheckParPO();
				tscpp.setParValue(Long.parseLong(parValue));
				tscpp.setYieldly(Long.parseLong(yieldly));
				tscpp.setUpdateBy(Long.parseLong(userId));
				tscpp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
				TtSalesCheckParPO tscppSeach=new TtSalesCheckParPO();
				tscppSeach.setParId(Long.parseLong(parId));
				reDao.sendPlanSetUpdate(tscppSeach, tscpp);
			}
			act.setOutData("returnValue", 1);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数保存信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
