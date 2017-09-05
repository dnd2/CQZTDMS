package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendDriverSeachDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 发运司机调整
 * @author shuyh
 * 2017/8/1
 */
public class SendDriverManage {
	public Logger logger = Logger.getLogger(SendDriverManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendDriverSeachDao reDao = SendDriverSeachDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendDriverSeachInitUtl = "/jsp/sales/storage/sendmanage/sendDriver/sendDriverSeachList.jsp";
	
	/**
	 * 发运司机调整查询初始化
	 */
	public void sendDriverSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(sendDriverSeachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运组板查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运司机调整信息查询
	 */
	public void sendDriverSeachQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			//String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 组板日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商
			String transportType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); // 发运方式
			String driverName = CommonUtils.checkNull(request.getParamValue("driverName")); //司机姓名
			String driverTel = CommonUtils.checkNull(request.getParamValue("driverTel")); // 司机电话
			
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("logiName", logiName);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("transportType", transportType);
			map.put("driverName", driverName);
			map.put("driverTel", driverTel);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getSendDriverSeachQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运司机调整
	 */
	public void  saveSendDriverModify(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//组板ID
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String[] dNames=request.getParamValues("dNames");//司机姓名
			String[] dTels=request.getParamValues("dTels");//司机电话
			String[] carTeams=request.getParamValues("carTeams");//所属车队
			String[] carNos=request.getParamValues("carNos");//车牌号
			
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String boId=groupIds[i];
					String driverName=dNames[index];
					String driverTel=dTels[index];
					String carTeam=CommonUtils.checkNull(carTeams[index]);
					String carNo=CommonUtils.checkNull(carNos[index]);
					TtSalesBoardPO tsb=new TtSalesBoardPO();//组板表实体
					tsb.setBoId(Long.parseLong(boId));
					TtSalesBoardPO tsbp=new TtSalesBoardPO();//组板表实体
					tsbp.setDriverName(driverName);
					tsbp.setDriverTel(driverTel);
					tsbp.setCarTeam(carTeam);
					tsbp.setCarNo(carNo);
					tsbp.setUpdateBy(logonUser.getUserId());
					tsbp.setUpdateDate(new Date());
					reDao.update(tsb, tsbp);
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运司机调整");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
