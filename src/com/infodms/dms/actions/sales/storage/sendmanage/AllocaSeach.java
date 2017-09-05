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
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : AllocaSeach 
 * @Description   : 配车查询控制类
 * @author        : ranjian
 * CreateDate     : 2013-4-26
 */
public class AllocaSeach {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AllocaDao aDao = AllocaDao.getInstance();
	private final AllocaSeachDao reDao = AllocaSeachDao.getInstance();
	private final String allocaListInitUtl = "/jsp/sales/storage/sendmanage/allocaSeach/allocaSeachList.jsp";
	private final String allocaseachInitUtl = "/jsp/sales/storage/sendmanage/allocaSeach/allocaSeach.jsp";
	private final String allocaPrintUtl = "/jsp/sales/storage/sendmanage/allocaSeach/allocaPrint.jsp";//配车清单打印
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车查询列表页面初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void allocaSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(allocaListInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车查询列表页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void allocaSeachQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("RAISE_STARTDATE")); //提报日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("RAISE_ENDDATE")); // 提报日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String handleStatus = CommonUtils.checkNull(request.getParamValue("HANDLE_STATUS")); // 状态
			String transportType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); // 发运方式
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN查询
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("handleStatus", handleStatus);
			map.put("transportType", transportType);
			map.put("VIN", vin);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("yieldly", yieldly);
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getAllocaSeachQueryExport(map);
				String [] head={"组板号","组板时间","组板数量","配车数量","出库数量","发运数量","验收数量"};
				String [] cols={"BO_NO","BO_DATE","BO_NUM","ALLOCA_NUM","OUT_NUM","SEND_NUM","ACC_NUM"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "配车查询列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getAllocaSeachQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配车查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化配车页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void updateAllocaSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = aDao.getSendBoardMatListQuery(params);//查询组板明细信息
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= aDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
				}
			}
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setForword(allocaseachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车查看信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车清单打印
	 * @param      :       
	 * @return     :    
	 * @throws     : 
	 * LastDate    : 2013-6-26 ranjian
	 */
	public void allocaPrint(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId=CommonUtils.checkNull(request.getParamValue("boId"));//组板ID	
			Map<String,Object> valueMap=reDao.allocaPrint(Long.parseLong(boId));
			List<Map<String,Object>> valueList=reDao.allocaPrintMain(Long.parseLong(boId));
			List<Map<String,Object>> listRow=reDao.rowSpanList(Long.parseLong(boId));
			
			TcUserPO po=new TcUserPO();
			po.setUserId(logonUser.getUserId());
			po=(TcUserPO)reDao.select(po).get(0);
			act.setOutData("userMap", po);
			act.setOutData("valueMap", valueMap);
			act.setOutData("valueList", valueList);
			act.setOutData("listRow", listRow);
			act.setForword(allocaPrintUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车清单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
