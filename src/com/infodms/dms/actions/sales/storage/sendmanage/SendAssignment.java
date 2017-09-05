package com.infodms.dms.actions.sales.storage.sendmanage;

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
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : SendAssignment
 * @Description   : 发送分派管理
 * @author        : ranjian
 * CreateDate     : 2013-4-17
 */
public class SendAssignment {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendAssignmentDao reDao = SendAssignmentDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendAssignmentInitUtl = "/jsp/sales/storage/sendmanage/sendAssignment/sendAssignmentList.jsp";
	private final String sendQUeryUrl = "/jsp/sales/storage/sendmanage/sendAssignment/sendQuery.jsp";
	private final String sendDeatilUrl = "/jsp/sales/storage/sendmanage/sendAssignment/sendDeatil.jsp";
	private final String sendModifyUrl = "/jsp/sales/storage/sendmanage/sendAssignment/modifyDlvInfo.jsp";
	private final String sendModifyDBUrl = "/jsp/sales/storage/sendmanage/sendAssignment/modifyDlvInfoDB.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 发送分派管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void sendAssignmentInit(){
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
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(sendAssignmentInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发送分派管理初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 发送分派管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void sendAssignmentQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String dlvOrdType = CommonUtils.checkNull(request.getParamValue("dlvOrdType")); //单据来源
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //创建日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 创建日期结束
			String reqNO = CommonUtils.checkNull(request.getParamValue("reqNO")); //申请单号
			
			String dlvStartDate = CommonUtils.checkNull(request.getParamValue("DLV_START_DATE")); //最晚发运开始日期
			String dlvEndDate = CommonUtils.checkNull(request.getParamValue("DLV_END_DATE")); //最晚发运结束日期
			String dlvStatus = CommonUtils.checkNull(request.getParamValue("dlvStatus")); //发运状态
			String arrStartDate = CommonUtils.checkNull(request.getParamValue("ARR_START_DATE")); //最晚到货开始日期
			String arrEndDate = CommonUtils.checkNull(request.getParamValue("ARR_END_DATE")); //最晚到货结束日期
			String logiId = CommonUtils.checkNull(request.getParamValue("logiId")); //承运商ID
			String isMiddleTurn = CommonUtils.checkNull(request.getParamValue("isMiddleTurn")); //是否中转
			String isSdan = CommonUtils.checkNull(request.getParamValue("isSdan")); //是否散单
			String jsProvince = CommonUtils.checkNull(request.getParamValue("jsProvince")); //发运结算省份
			String jsCity = CommonUtils.checkNull(request.getParamValue("jsCity")); //发运结算城市
			String jsCounty = CommonUtils.checkNull(request.getParamValue("jsCounty")); //发运结算区县
			
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 查询类型
			/******************************页面查询字段end***************************/
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("dlvOrdType", dlvOrdType);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("reqNO", reqNO);
			map.put("dlvStartDate", dlvStartDate);
			map.put("dlvEndDate", dlvEndDate);
			map.put("dlvStatus", dlvStatus);
			map.put("arrStartDate", arrStartDate);
			map.put("arrEndDate", arrEndDate);
			map.put("logiId", logiId);
			map.put("isMiddleTurn", isMiddleTurn);
			map.put("isSdan", isSdan);
			map.put("jsProvince", jsProvince);
			map.put("jsCity", jsCity);
			map.put("jsCounty", jsCounty);
			
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用(暂时无导出)
				List<Map<String, Object>> mapList = reDao.getSendAssignmentExport(map);
				String [] head={};//导出的字段表头
				String [] cols={};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "发运分派管理信息.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendAssignmentQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 跳转到修改页面
	 */
	public void toEditDlvInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")); //调拨单ID
			String isSand = CommonUtils.checkNull(request.getParamValue("isSand")); //是否散单
			String logiName = CommonUtils.checkNull(request.getParamValue("logiName")); //承运商名称
			String dlvType = CommonUtils.checkNull(request.getParamValue("dlvType")); //发运类型
			String flag = CommonUtils.checkNull(request.getParamValue("flag")); //1表示分派管理，2表示分派更改
			
			act.setOutData("reqId", reqId);
			act.setOutData("isSand", isSand);
			act.setOutData("logiName", logiName);
			act.setOutData("flag", flag);
			//act.setOutData("dlvType", dlvType);
			//根据申请ID查询信息
			Map<String, Object> map=reDao.getDlvInfoById(reqId);
			act.setOutData("map", map);
			
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			if(Integer.parseInt(dlvType)==Constant.DELIVERY_ORD_TYPE_ALLOCAT){//发运类型为调拨单
				act.setForword(sendModifyDBUrl);
			}else{
				act.setForword(sendModifyUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨修改初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改发运信息
	 */
	public void modifyInfoDo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")); //发运申请ID
			String sendWareId = CommonUtils.checkNull(request.getParamValue("sendWareId")); //发运仓库
			String jsProvince = CommonUtils.checkNull(request.getParamValue("JS_PROVINCE")); //结算省份
			String jsCity = CommonUtils.checkNull(request.getParamValue("JS_CITY")); //结算城市
			String jsCounty = CommonUtils.checkNull(request.getParamValue("JS_COUNTY")); //结算区县
			String isMiddleTurn = CommonUtils.checkNull(request.getParamValue("isMiddleTurn")); //是否中转
			String zzWareId = CommonUtils.checkNull(request.getParamValue("zzWareId")); //中转仓库
			String zZProvince = CommonUtils.checkNull(request.getParamValue("zZProvinceM")); //中转省份
			String zZCity = CommonUtils.checkNull(request.getParamValue("zZCityM")); //中转城市
			String zZCounty = CommonUtils.checkNull(request.getParamValue("zZCountyM")); //中转区县
			String isSand = CommonUtils.checkNull(request.getParamValue("isSand")); //是否散单
			String logiName = CommonUtils.checkNull(request.getParamValue("logiName")); //承运商名称
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); //备注
			String transType=CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			//String recWareId = CommonUtils.checkNull(request.getParamValue("recWareId")); //收货仓库
			String flag = CommonUtils.checkNull(request.getParamValue("flag")); //1表示分派管理，2表示分派更改
			Integer orderNum=0;
			
			TtVsDlvryPO tvdp=new TtVsDlvryPO();
			tvdp.setReqId(Long.parseLong(reqId));
			List list=reDao.select(tvdp);
			if(list!=null&&list.size()>0){
				TtVsDlvryPO tv=(TtVsDlvryPO) list.get(0);
				orderNum=Integer.parseInt(tv.getReqTotal());
				int dlvType=tv.getDlvType();
				String ordId=tv.getOrdId().toString();
				if(dlvType==Constant.DELIVERY_ORD_TYPE_ALLOCAT){//调拨单
					//根据发运仓库和调拨单ID获取可用库存数不足的订单明细
					List<Map<String, Object>> dlist=reDao.getStockMinusNum(sendWareId, ordId);
					if(dlist!=null&&dlist.size()>0){
						act.setOutData("returnValue", "2");
						return;
					}
				}else if(dlvType==Constant.DELIVERY_ORD_TYPE_ORDER){//订单
					
				}
				
			}
			
			TtVsDlvryPO tt=new TtVsDlvryPO();
			tt.setDlvWhId(Long.parseLong(sendWareId));
			tt.setDlvBalProvId(Long.parseLong(jsProvince));
			tt.setDlvBalCityId(Long.parseLong(jsCity));
			tt.setDlvBalCountyId(Long.parseLong(jsCounty));
			tt.setDlvIsZz(Integer.parseInt(isMiddleTurn));
			if(Integer.parseInt(isMiddleTurn)==Constant.IF_TYPE_YES){
				tt.setZzWhId(Long.parseLong(zzWareId));
				tt.setDlvZzProvId(Long.parseLong(zZProvince));
				tt.setDlvZzCityId(Long.parseLong(zZCity));
				tt.setDlvZzCountyId(Long.parseLong(zZCounty));
			}
			tt.setUpdateBy(logonUser.getUserId());
			tt.setUpdateDate(new Date());
			tt.setDlvIsSd(Integer.parseInt(isSand));//是否散单
			if(Integer.parseInt(isSand)==Constant.IF_TYPE_NO){
				tt.setDlvLogiId(Long.parseLong(logiName));
			}
			
			tt.setDlvFpTotal(orderNum);//分派数量
			if(flag.equals("1")){
				tt.setDlvStatus(Constant.ORDER_STATUS_02);//已分派提交
			}else{
				tt.setDlvStatus(Constant.ORDER_STATUS_03);//已分派审核
			}
			
			tt.setDlvDate(new Date());//分派时间
			tt.setDlvBy(logonUser.getUserId());//分派人
			tt.setDlvRemark(remark);//分派备注
			tt.setDlvShipType(Integer.parseInt(transType));
			reDao.update(tvdp, tt);
			if(!flag.equals("1")){//审核通过时并且发运类型为调拨单，需要回写分派数量
				if(list!=null&&list.size()>0){
					TtVsDlvryPO tv=(TtVsDlvryPO) list.get(0);
					int dlvType=tv.getDlvType();
					if(dlvType==Constant.DELIVERY_ORD_TYPE_ALLOCAT){
						Long dispId=tv.getOrdId();
						TtVsDispatchOrderPO tdp=new TtVsDispatchOrderPO();
						tdp.setDispId(dispId);
						TtVsDispatchOrderPO tdp2=new TtVsDispatchOrderPO();
						tdp2.setFpNum(orderNum);
						tdp2.setUpdateBy(logonUser.getUserId());
						tdp2.setUpdateDate(new Date());
						reDao.update(tdp, tdp2);
					}
					
				}
			}
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运分派信息修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改发运信息
	 */
	public void modifyInfoDbDo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")); //发运申请ID
			String sendWareId = CommonUtils.checkNull(request.getParamValue("sendWareId")); //发运仓库
			String isSand = CommonUtils.checkNull(request.getParamValue("isSand")); //是否散单
			String logiName = CommonUtils.checkNull(request.getParamValue("logiName")); //承运商名称
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); //备注
			String transType=CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			String recWareId = CommonUtils.checkNull(request.getParamValue("recWareId")); //收货仓库
			String flag = CommonUtils.checkNull(request.getParamValue("flag")); //1表示分派管理，2表示分派更改
			Integer orderNum=0;
			
			TtVsDlvryPO tvdp=new TtVsDlvryPO();
			tvdp.setReqId(Long.parseLong(reqId));
			List list=reDao.select(tvdp);
			if(list!=null&&list.size()>0){
				TtVsDlvryPO tv=(TtVsDlvryPO) list.get(0);
				orderNum=Integer.parseInt(tv.getReqTotal());
				int dlvType=tv.getDlvType();
				String ordId=tv.getOrdId().toString();
				if(dlvType==Constant.DELIVERY_ORD_TYPE_ALLOCAT){//调拨单
					//根据发运仓库和调拨单ID获取可用库存数不足的订单明细
					List<Map<String, Object>> dlist=reDao.getStockMinusNum(sendWareId, ordId);
					if(dlist!=null&&dlist.size()>0){
						act.setOutData("returnValue", "2");
						return;
					}
				}
			}
			//根据收货仓库获取省市县
			TmWarehousePO twp=new TmWarehousePO();
			twp.setWarehouseId(Long.parseLong(recWareId));
			List tlist=reDao.select(twp);
			twp=(TmWarehousePO)tlist.get(0);
			TtVsDlvryPO tt=new TtVsDlvryPO();
			tt.setDlvWhId(Long.parseLong(sendWareId));
			tt.setDlvBalProvId(Long.parseLong(twp.getProvCode()));
			tt.setDlvBalCityId(Long.parseLong(twp.getCityCode()));
			tt.setDlvBalCountyId(Long.parseLong(twp.getCountyCode()));
			tt.setDlvRecWhId(Long.parseLong(recWareId));
			tt.setUpdateBy(logonUser.getUserId());
			tt.setUpdateDate(new Date());
			tt.setDlvIsSd(Integer.parseInt(isSand));//是否散单
			if(Integer.parseInt(isSand)==Constant.IF_TYPE_NO){
				tt.setDlvLogiId(Long.parseLong(logiName));
			}
			tt.setDlvFpTotal(orderNum);//分派数量
			if(flag.equals("1")){
				tt.setDlvStatus(Constant.ORDER_STATUS_02);//已分派提交
			}else{
				tt.setDlvStatus(Constant.ORDER_STATUS_03);//已分派审核
			}
			
			tt.setDlvDate(new Date());//分派时间
			tt.setDlvBy(logonUser.getUserId());//分派人
			tt.setDlvRemark(remark);//分派备注
			tt.setDlvShipType(Integer.parseInt(transType));
			reDao.update(tvdp, tt);
			if(!flag.equals("1")){//审核通过时并且发运类型为调拨单，需要回写分派数量
				if(list!=null&&list.size()>0){
					TtVsDlvryPO tv=(TtVsDlvryPO) list.get(0);
					int dlvType=tv.getDlvType();
					if(dlvType==Constant.DELIVERY_ORD_TYPE_ALLOCAT){
						Long dispId=tv.getOrdId();
						TtVsDispatchOrderPO tdp=new TtVsDispatchOrderPO();
						tdp.setDispId(dispId);
						TtVsDispatchOrderPO tdp2=new TtVsDispatchOrderPO();
						tdp2.setFpNum(orderNum);
						tdp2.setUpdateBy(logonUser.getUserId());
						tdp2.setUpdateDate(new Date());
						reDao.update(tdp, tdp2);
					}
					
				}
			}
			act.setOutData("returnValue", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运分派信息修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 确定发运
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void  sendAssignmentMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//申请ID
			String[] logiNames=request.getParamValues("LOGI_NAME");//获取物流商IDS
			String[] isSands=request.getParamValues("IS_SAND");//是否散单
			String[] dlvType=request.getParamValues("dlvType");//发运类型（订单或调拨）
			String[] ordIds=request.getParamValues("ordId");//订单ID
			
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String[] orderNums=request.getParamValues("orderNum");//订单数量
			String remark=request.getParamValue("REMARK");//备注
			String pFlag=request.getParamValue("pFlag");//1表示发运分派更改
//			String[] areaId=request.getParamValues("areaIds");//产地
//			String[] planDates = request.getParamValues("PLAN_DATE");//计划装车时间
//			String[] asRemark = request.getParamValues("AS_REMARK");//说明
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String reqId=groupIds[i];
					String isSand=isSands[index];
					String logiName=logiNames[index];
					String orderNum=orderNums[index];
					TtVsDlvryPO tvp=new TtVsDlvryPO();
					tvp.setReqId(Long.parseLong(reqId));
					TtVsDlvryPO tvp2=new TtVsDlvryPO();
					tvp2.setDlvIsSd(Integer.parseInt(isSand));
					if(Integer.parseInt(isSand)==Constant.IF_TYPE_NO){
						tvp2.setDlvLogiId(Long.parseLong(logiName));
					}
					tvp2.setDlvFpTotal(Integer.parseInt(orderNum));
					if(pFlag.equals("1")){
						tvp2.setDlvStatus(Constant.ORDER_STATUS_03);//已分派审核
						//审核日期
						tvp2.setAuditDate(new Date());
						//审核人
						tvp2.setAuditBy(logonUser.getUserId());
						//审核备注
						tvp2.setAuditRemark(remark);
					}else{
						tvp2.setDlvStatus(Constant.ORDER_STATUS_02);//已分派提交
						//分派日期
						tvp2.setDlvDate(new Date());
						//分派人
						tvp2.setDlvBy(logonUser.getUserId());
						//分派备注
						tvp2.setDlvRemark(remark);
					}
					
					tvp2.setUpdateBy(logonUser.getUserId());
					tvp2.setUpdateDate(new Date());
					
					reDao.update(tvp, tvp2);
					if(pFlag.equals("1")){//审核通过时并且为调拨单时，回写调拨单分派数量
						String dType=dlvType[index];
						if(Integer.parseInt(dType)==Constant.DELIVERY_ORD_TYPE_ALLOCAT){
							String dispId=ordIds[index];
							TtVsDispatchOrderPO tdp=new TtVsDispatchOrderPO();
							tdp.setDispId(Long.parseLong(dispId));
							TtVsDispatchOrderPO tdp2=new TtVsDispatchOrderPO();
							tdp2.setFpNum(Integer.parseInt(orderNum));
							tdp2.setUpdateBy(logonUser.getUserId());
							tdp2.setUpdateDate(new Date());
							reDao.update(tdp, tdp2);
						}
					}
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派信息确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 进入发送分派查询主页面
	 */
	public void sendQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(sendQUeryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发送分派查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运分派查询
	 */
	public void sendQueryList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 发运仓库
			String logiId = CommonUtils.checkNull(request.getParamValue("logiId")); // 承运商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")); // 申请单号
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); // 申请发运方式
			String dlvIsSd = CommonUtils.checkNull(request.getParamValue("dlvIsSd")); // 是否散单
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 申请日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 申请日期结束
			String fpStartDate = CommonUtils.checkNull(request.getParamValue("FP_START_DATE")); // 分派日期开始
			String fpEndDate = CommonUtils.checkNull(request.getParamValue("FP_END_DATE")); // 分派日期结束
			String dlvType = CommonUtils.checkNull(request.getParamValue("DLV_TYPE")); // 分派日期结束
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 查询类型
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("whId", whId);
			map.put("dealerCode", dealerCode);
			map.put("reqNo", reqNo);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("logiId", logiId);
			map.put("transType", transType);
			map.put("fpStartDate", fpStartDate);
			map.put("fpEndDate", fpEndDate);
			map.put("dlvType", dlvType);
			map.put("dlvIsSd", dlvIsSd);
			
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				//Map<String, Object> valueMap = reDao.tgSum(map);
				//act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用(暂时无导出)
				List<Map<String, Object>> mapList = reDao.getSendAssExport(map);
				String [] head={"是否散单","来源类型","承运商","发运仓库","发运方式","经销商","基地仓库","发运地","分派量","未分派量","分派日期","批售单号","申请单号","申请量","申请发运仓库","申请发运方式","申请收货地","申请收货详细地址","申请时间"};//导出的字段表头
				String [] cols={"SD_NAME","DLV_TYPE_NAME","LOGI_NAME","DLV_WH_NAME","DS_SHIP","DEALER_NAME","REC_WH_NAME","DLV_ADDR","DLV_FP_TOTAL","NO_FP_TOTAL","DLV_DATE","ORD_NO","REQ_NO","REQ_TOTAL","REQ_WH_NAME","REQ_SHIP","REQ_ADDR","REQ_REC_ADDR","REQ_DATE"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "发运分派管理信息.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendAssQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询发运分派详细信息
	 */
	public void sendQueryDeatil()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("Id");//得到该订单
			Map<String, Object> list = reDao.getSendQueryById(orderId);
			act.setOutData("list", list);
			act.setForword(sendDeatilUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
