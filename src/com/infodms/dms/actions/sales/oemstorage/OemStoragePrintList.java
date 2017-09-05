package com.infodms.dms.actions.sales.oemstorage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.oemstorage.OemStoragePrintDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtStoPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OemStoragePrintList {
	private final String initUrl = "/jsp/sales/oemstorage/oemStoragePrintList.jsp";
	private final  String printNewUrl="/jsp/sales/oemstorage/oemStoragePrintView.jsp";
	private final OrderAuditDao auditDao = OrderAuditDao.getInstance();
	OemStoragePrintDao dao=OemStoragePrintDao.getInstance();
	public Logger logger = Logger.getLogger(OemStoragePrintList.class);
	private ActionContext act=ActionContext.getContext();
	public void oemStoragePrintQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
		try {
		act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"移库单打印查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 移库单打印
	 */
	public void commandQueryPrint(){
		RequestWrapper request=act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//移库单号
			String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));//打印状态
			String startDate = request.getParamValue("startDate");			//移库起始时间
			String endDate = request.getParamValue("endDate");				//移库结束时间
			//String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//大区
			Long companyId = logonUser.getCompanyId();
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getCommandPrintList(startDate, endDate, orderNo,printFlag, companyId, areaIds, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"发运指令查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 打印预览内容
	 */
	public void commandPrintInfo(){
		RequestWrapper request=act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId"));//发运ID
			//String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Map<String, Object> map = dao.getstoInfoMap(reqId);
			List<Map<String, Object>> list = dao.getstorageResourceReserveDetailList(reqId);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(printNewUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"发运指令打印内容");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 打印完成
	 */
	public void commandPrintOver(){
		RequestWrapper request=act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String[] deliveryIds = request.getParamValues("deliveryIds");
			
			if(deliveryIds!=null&&deliveryIds.length>0){
				
				for(int i=0;i<deliveryIds.length;i++){
					TtStoPO po1=new TtStoPO();
					TtStoPO po2=new TtStoPO();
					po1.setStoId(Long.parseLong(deliveryIds[i]));
					//po.setDeliveryId();
					po2.setPrintFlag(Integer.parseInt(Constant.PRINT_FLAG_01));
					po2.setUpdateBy(logonUser.getUserId());
					po2.setUpdateDate(new Date());
					//tp.setPrintFlag(Integer.parseInt(Constant.PRINT_FLAG_01));
					//tp.setUpdateBy(logonUser.getUserId());
					//tp.setUpdateDate(new Date());
					dao.update(po1,po2);
				}
			}
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
