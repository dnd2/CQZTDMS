package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachMsDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : SendBoardSeachMs
 * @Description   : 发送组板明细查询
 * @author        : ranjian
 * CreateDate     : 2013-11-12
 */
public class SendBoardSeachMs {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendBoardSeachMsDao reDao = SendBoardSeachMsDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBoardSeachInitUtl = "/jsp/sales/storage/sendmanage/sendBoardSeahMs/sendBoardSeachMsList.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 发运组板明细查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-11-12
	 */
	public void sendBoardSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(sendBoardSeachInitUtl);
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运组板明细查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 发运组板查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-11-12
	 */
	public void sendBoardSeachQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("RAISE_STARTDATE")); // 组板日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("RAISE_ENDDATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商
			String transportType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); // 发运方式
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商
			
			
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("yieldly", yieldly);
			map.put("logiName", logiName);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("transportType", transportType);
			map.put("dealerCode", dealerCode);
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				//List<Map<String, Object>> list = reDao.getSendBoardSeachQueryExport(map);
				//String [] head={"序号","组板号","组板人","组板时间","组板数量","配车数量","出库数量","发运数量","验收数量"};
				//ToExcel.toBoardExcel(act.getResponse(),request,head,list, "组板详细信息.xls");
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendBoardSeachQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
