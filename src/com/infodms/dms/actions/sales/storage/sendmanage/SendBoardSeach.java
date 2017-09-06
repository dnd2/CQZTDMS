package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
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
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : SendBoardSeach
 * @Description   : 发送组板查询
 * @author        : ranjian
 * CreateDate     : 2013-4-22
 */
public class SendBoardSeach {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendBoardSeachDao reDao = SendBoardSeachDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBoardSeachInitUtl = "/jsp/sales/storage/sendmanage/sendBoardSeach/sendBoardSeachList.jsp";
	private final String updateSendBoardSeachInitUtl = "/jsp/sales/storage/sendmanage/sendBoardSeach/updateSendBoardSeach.jsp";
	private final String seachInitUtl = "/jsp/sales/storage/sendmanage/sendBoardSeach/sendBoardSeach.jsp";

	/**
	 * 
	 * @Title      : 
	 * @Description: 发运组板查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void sendBoardSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
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
					"发运组板查询初始化");
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
	 * LastDate    : 2013-4-22
	 */
	public void sendBoardSeachQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 组板日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商
			String transportType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); // 发运方式
//			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //结算省份
//			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 结算城市
//			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 结算区县
			
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("logiName", logiName);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("transportType", transportType);
//			map.put("provinceId", provinceId);
//			map.put("cityId", cityId);
//			map.put("countyId", countyId);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getSendBoardSeachQueryExport(map);
				String [] head={"组板号","发运方式","物流公司","组板申请日期","组板数量","配车数量","出库数量","发运数量","验收数量","审核人","审核备注","审核时间"};
				String [] cols={"BO_NO","SHIP_NAME","LOGI_NAME","BO_DATE","BO_NUM","ALLOCA_NUM","OUT_NUM","SEND_NUM","ACC_NUM","AUDIT_BY","AUDIT_REMARK","AUDIT_TIME"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "组板查询列表.xls",head,cols,list);
				//ToExcel.toBoardExcel(act.getResponse(),request,head,list, "组板详细信息.xls");
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendBoardSeachQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化组板修改页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void updateSendBordSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(params);
			Map<String,Object> sendBoardMap=reDao.getBoardByBoId(params);
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("list", list);
			act.setForword(updateSendBoardSeachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"修改组板信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 确定组板
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void  sendBordSeachMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId=CommonUtils.checkNullNum(request.getParamValue("boId"));//组板ID
			String[] boDeId=request.getParamValues("BO_DE_ID");//组板明细ID
			String[] boardNum=request.getParamValues("BOARD_NUM");//组板数量维护数
			String carNo=CommonUtils.checkNull(request.getParamValue("CAR_NO"));//承运车牌号
			String loads=CommonUtils.checkNull(request.getParamValue("LOADS"));//装车道次
			String carTeam=CommonUtils.checkNull(request.getParamValue("CAR_TEAM"));//领票车队
			String driverName=CommonUtils.checkNull(request.getParamValue("DRIVER_NAME"));//驾驶员姓名
			String driverTel=CommonUtils.checkNull(request.getParamValue("DRIVER_TEL"));//驾驶员电话
			/************************************************组板明细表信息修改START*********************/	
			//修改组板表信息
			TtSalesBoardPO po=new TtSalesBoardPO();
			po.setCarNo(carNo);
			po.setLoads(loads);
			po.setCarTeam(carTeam);
			po.setDriverName(driverName);
			po.setDriverTel(driverTel);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			TtSalesBoardPO po1=new TtSalesBoardPO();
			po1.setBoId(Long.parseLong(boId));
			reDao.update(po1, po);
			//组板明细表修改【本次组板数，承运车牌号，装车道次，领票车队，修改人，修改时间】
			if(boardNum.length>0){
				for(int i=0;i<boardNum.length;i++){
					//修改组板明细表
					List<Object> paramsBoDe=new ArrayList<Object>();
					paramsBoDe.add(CommonUtils.checkNullNum(boardNum[i]));//维护数
					paramsBoDe.add(logonUser.getUserId());
					paramsBoDe.add(CommonUtils.checkNull(boDeId[i]));
					reDao.updateSendBoardDel(paramsBoDe);/**组板明细表修改*/
				}
				//修改已组板数量
				stoUtil.updateBoardOrAllocaNum(Long.parseLong(boId), logonUser.getUserId());
				act.setOutData("returnValue", 1);//成功添加
			}
			else{
				act.setOutData("returnValue", 2);//错误提示，
			}
			/************************************************组板明细表信息修改END**********************/
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "组板信息确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化组板查看页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void seachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=reDao.getBoardByBoId(params);
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(params);
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("list", list);
			act.setForword(seachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"组板信息查看初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
