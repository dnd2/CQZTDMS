package com.infodms.dms.actions.sales.storage.balancemanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.balancemanage.SalesBalanceApplyDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtSalesBalApplyPO;
import com.infodms.dms.po.TtSalesBalFilePO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 运费结算申请
 * @author shuyh
 *
 */
public class SalesBalanceApply{
	public Logger logger = Logger.getLogger(SalesBalanceApply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SalesBalanceApplyDao reDao = SalesBalanceApplyDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final String applyInitUrl = "/jsp/sales/storage/balance/billBalApply.jsp";
	private final String modifyBalUrl = "/jsp/sales/storage/balance/modifyBalInfo.jsp";
	private final String applySubInitUrl = "/jsp/sales/storage/balance/billBalApplySub.jsp";
	private final String modifyApplyUrl = "/jsp/sales/storage/balance/modifyApplyInfo.jsp";
	private final String BILL_DETAIL_PAGE = "/jsp/sales/storage/balance/balanceApplyDel.jsp";
	private final String INVOICE_DETAIL_PAGE = "/jsp/sales/storage/balance/balanceInvoiceDel.jsp";
	/**
	 * 初始化
	 */
	public void applyInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			
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
			act.setForword(applyInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费结算保存初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算保存查询
	 */
	public void billApplyList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String balNo = CommonUtils.checkNull(request.getParamValue("bal_no")); //对账单号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			String isChange = CommonUtils.checkNull(request.getParamValue("isChange")); //是否调整
			
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 查询类型
			/******************************页面查询字段end***************************/
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("balNo", balNo);
			map.put("logiId", logiId);
			map.put("isChange", isChange);
			
			map.put("poseId", logonUser.getPoseId().toString());
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiIdU", (BigDecimal)pmap.get("LOGI_ID"));
			
//			if("1".equals(common)){//统计 调用
//				Map<String, Object> valueMap = reDao.tgSum(map);
//				act.setOutData("valueMap", valueMap);	
//			}else if("2".equals(common)){//导出 调用(暂时无导出)
//				List<Map<String, Object>> mapList = reDao.getSendAssignmentExport(map);
//				String [] head={};//导出的字段表头
//				String [] cols={};//导出的字段名称
//				ToExcel.toReportExcel(act.getResponse(),request, "发运分派管理信息.xls",head,cols,mapList);
//			}else{
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getBalanceApplyQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
//			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算保存查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改页面跳转
	 */
	public void toEditBalAddr(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //交接单ID
			String otherMoney = CommonUtils.checkNull(request.getParamValue("otherMoney")); //其他金额
			TtSalesWaybillPO po=new TtSalesWaybillPO();
			po.setBillId(Long.parseLong(billId));
			po=(TtSalesWaybillPO) reDao.select(po).get(0);
			act.setOutData("billId", billId);
			act.setOutData("otherMoney", otherMoney);
			act.setOutData("balProv", po.getBalProvId());
			act.setOutData("balCity", po.getBalCityId());
			act.setOutData("balCounty", po.getBalCountyId());
			TmRegionPO tr1=new TmRegionPO();
			tr1.setRegionCode(po.getDlvBalProvId().toString());
			tr1.setRegionType(Constant.REGION_TYPE_02);
			tr1=(TmRegionPO) reDao.select(tr1).get(0);
			act.setOutData("dlvBalProv",tr1.getRegionName());
			
			TmRegionPO tr2=new TmRegionPO();
			tr2.setRegionCode( po.getDlvBalCityId().toString());
			tr2.setRegionType(Constant.REGION_TYPE_03);
			tr2=(TmRegionPO) reDao.select(tr2).get(0);
			act.setOutData("dlvBalCity",tr2.getRegionName());
			
			TmRegionPO tr3=new TmRegionPO();
			tr3.setRegionCode( po.getDlvBalCountyId().toString());
			tr3.setRegionType(Constant.REGION_TYPE_04);
			tr3=(TmRegionPO) reDao.select(tr3).get(0);
			act.setOutData("dlvBalCounty", tr3.getRegionName());
			act.setForword(modifyBalUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"财务结算地修改跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 修改财务结算地
	 */
	public void editBalAddr(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //交接单ID
			String jsProv = CommonUtils.checkNull(request.getParamValue("JS_PROVINCE")); //结算省份
			String jsCity = CommonUtils.checkNull(request.getParamValue("JS_CITY")); //结算城市
			String jsCounty = CommonUtils.checkNull(request.getParamValue("JS_COUNTY")); //结算区县
			String otherMoney = CommonUtils.checkNull(request.getParamValue("otherMoney")); //其他金额
			TtSalesWaybillPO po=new TtSalesWaybillPO();
			po.setBillId(Long.parseLong(billId));
			TtSalesWaybillPO po2=new TtSalesWaybillPO();
			po2.setBalProvId(Long.parseLong(jsProv));
			po2.setBalCityId(Long.parseLong(jsCity));
			po2.setBalCountyId(Long.parseLong(jsCounty));
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(new Date());
			//根据若结算地发生变更，计算补充金额和扣款金额。。。。。暂时设为0
			po2.setSupplyMoney(0d);
			po2.setDeductMoney(0d);
			
			po2.setOtherMoney(Double.valueOf(otherMoney));//其他金额
			TtSalesWaybillPO tsw=new TtSalesWaybillPO();
			tsw.setBillId(Long.parseLong(billId));
			tsw=(TtSalesWaybillPO) reDao.select(tsw).get(0);
			if((!tsw.getDlvBalProvId().toString().equals(jsProv))||(!tsw.getDlvBalCityId().toString().equals(jsCity))
					||(!tsw.getDlvBalCountyId().toString().equals(jsCounty))){
				po2.setIsChange(Constant.IF_TYPE_YES);//是否调整：是
			}
			reDao.update(po, po2);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"财务结算地修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算提交
	 */
	public void  subBill(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String[] groupIds=request.getParamValues("groupIds");//交接单ID
			String[] remarks=request.getParamValues("remarks");//结算申请备注
			
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			
			if(groupIds!=null && groupIds.length>0){
				StringBuffer billStr=new StringBuffer();
				for(int j=0;j<groupIds.length;j++){
					if(j==groupIds.length-1){
						billStr.append("'"+groupIds[j]+"'");
					}else{
						billStr.append("'"+groupIds[j]+"',");
					}
				}
				Map<String,Object> map=reDao.getWayBillSum(billStr.toString());
				//生成一条结算保存信息
				TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
				String applyId=SequenceManager.getSequence(null);//结算保存ID
				String applyNo=CommonUtils.getBusNo(Constant.NOCRT_JS_APPLY_NO,0);
				
				tsb.setApplyId(Long.parseLong(applyId));
				tsb.setApplyNo(applyNo);
				tsb.setLogiId(Long.parseLong(map.get("LOGI_ID").toString()));
				tsb.setBalCount(Integer.parseInt(map.get("BAL_COUNT").toString()));
				tsb.setBalAmount(Double.parseDouble(map.get("BAL_AMOUNT").toString()));
				tsb.setDeductMoney(Double.valueOf(map.get("DEDUCT_MONEY").toString()));
				tsb.setOtherMoney(Double.valueOf(map.get("OTHER_MONEY").toString()));
				tsb.setSupplyMoney(Double.valueOf(map.get("SUPPLY_MONEY").toString()));
				tsb.setCreateBy(logonUser.getUserId());
				tsb.setCreateDate(new Date());
				tsb.setStatus(Constant.BAL_ORDER_STATUS_07);//已保存
				reDao.insert(tsb);
				//更新结算保存ID到交接单主表
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
						if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String applyRemark=remarks[index];
					String billId=groupIds[i];
					TtSalesWaybillPO tsw=new TtSalesWaybillPO();
					tsw.setBillId(Long.parseLong(billId));
					TtSalesWaybillPO tsw2=new TtSalesWaybillPO();
					tsw2.setApplyId(Long.parseLong(applyId));
					tsw2.setApplyRemark(applyRemark);
					tsw2.setUpdateBy(logonUser.getUserId());
					tsw2.setUpdateDate(new Date());
					reDao.update(tsw, tsw2);
					//更新结算保存ID到交接单主表
					//reDao.updateWayBill(billStr.toString(), applyId, logonUser.getUserId().toString());
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
	 * 申请初始化
	 */
	public void applySubInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			
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
			act.setForword(applySubInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费结算申请初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算申请查询
	 */
	public void billApplySubList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String applyNo = CommonUtils.checkNull(request.getParamValue("apply_no")); //对账单号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			/******************************页面查询字段end***************************/
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("logiId", logiId);
			
			map.put("poseId", logonUser.getPoseId().toString());
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiIdU", (BigDecimal)pmap.get("LOGI_ID"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getBalanceApplySubQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算申请查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 删除结算申请单
	 */
	public void deleteSubBill(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算单ID
			//清空对应交接单的申请ID，还原财务结算地，清零扣款金额、补款金额和其他合计,是否调整为否
			reDao.updateWayBillApply(applyId, logonUser.getUserId().toString());
			
			//删除结算申请表中的数据
			TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
			tsb.setApplyId(Long.parseLong(applyId));
			reDao.delete(tsb);
			
			act.setOutData("returnValue", 1);//成功
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费结算申请删除");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算申请提交
	 */
	public void  subBillConfirm(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//结算单ID
			
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String applyId=groupIds[i];
					//更新结算申请单状态为已申请
					TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
					tsb.setApplyId(Long.parseLong(applyId));
					TtSalesBalApplyPO tsb2=new TtSalesBalApplyPO();
					tsb2.setStatus(Constant.BAL_ORDER_STATUS_02);//已申请
					tsb2.setUpdateBy(logonUser.getUserId());
					tsb2.setUpdateDate(new Date());
					
					reDao.update(tsb, tsb2);
				}
				
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算申请提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 申请修改页面跳转
	 */
	public void toEditBalApply(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //申请ID
			List<Map<String,Object>> list=reDao.getWayBillModify(applyId);
			act.setOutData("list", list);
			act.setOutData("Lsize", list.size());
			act.setOutData("applyId", applyId);
			act.setForword(modifyApplyUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"申请修改页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 保存修改信息
	 */
	public void saveBillInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String jsProv = CommonUtils.checkNull(request.getParamValue("jsProvince")); //财务结算省份
			String jsCity = CommonUtils.checkNull(request.getParamValue("jsCity")); //财务结算城市
			String jsCounty = CommonUtils.checkNull(request.getParamValue("jsCounty")); //财务结算区县
			String otherMoney = CommonUtils.checkNull(request.getParamValue("otherMoney")); //其他金额
			String billId = CommonUtils.checkNull(request.getParamValue("billId")); //交接单ID
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算申请ID
			//修改交接单信息
			TtSalesWaybillPO po=new TtSalesWaybillPO();
			po.setBillId(Long.parseLong(billId));
			TtSalesWaybillPO po2=new TtSalesWaybillPO();
			po2.setBalProvId(Long.parseLong(jsProv));
			po2.setBalCityId(Long.parseLong(jsCity));
			po2.setBalCountyId(Long.parseLong(jsCounty));
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(new Date());
			//根据若结算地发生变更，计算补充金额和扣款金额。。。。。暂时设为0
			po2.setSupplyMoney(0d);
			po2.setDeductMoney(0d);
			
			po2.setOtherMoney(Double.valueOf(otherMoney));//其他金额
			TtSalesWaybillPO tsw=new TtSalesWaybillPO();
			tsw.setBillId(Long.parseLong(billId));
			tsw=(TtSalesWaybillPO) reDao.select(tsw).get(0);
			if((!tsw.getDlvBalProvId().toString().equals(jsProv))||(!tsw.getDlvBalCityId().toString().equals(jsCity))
					||(!tsw.getDlvBalCountyId().toString().equals(jsCounty))){
				po2.setIsChange(Constant.IF_TYPE_YES);//是否调整：是
			}
			reDao.update(po, po2);
			
			//根据申请ID获取交接单信息，，重新统计金额信息，并更新
			TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
			tsb.setApplyId(Long.parseLong(applyId));
			TtSalesBalApplyPO tsb2=new TtSalesBalApplyPO();
			tsb2.setUpdateBy(logonUser.getUserId());
			tsb2.setUpdateDate(new Date());
			TtSalesWaybillPO tswb=new TtSalesWaybillPO();
			tswb.setApplyId(Long.parseLong(applyId));
			List tlist=reDao.select(tswb);
			StringBuffer billStr=new StringBuffer();
			for(int m=0;m<tlist.size();m++){
				TtSalesWaybillPO tsp=(TtSalesWaybillPO) tlist.get(m);
				if(m==tlist.size()-1){
					billStr.append("'"+tsp.getBillId()+"'");
				}else{
					billStr.append("'"+tsp.getBillId()+"',");
				}
			}
			Map<String,Object> map=reDao.getWayBillSum(billStr.toString());
			//tsb2.setLogiId(Long.parseLong(map.get("LOGI_ID").toString()));
			tsb2.setBalCount(Integer.parseInt(map.get("BAL_COUNT").toString()));
			tsb2.setBalAmount(Double.parseDouble(map.get("BAL_AMOUNT").toString()));
			tsb2.setDeductMoney(Double.valueOf(map.get("DEDUCT_MONEY").toString()));
			tsb2.setOtherMoney(Double.valueOf(map.get("OTHER_MONEY").toString()));
			tsb2.setSupplyMoney(Double.valueOf(map.get("SUPPLY_MONEY").toString()));
			reDao.update(tsb, tsb2);
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算信息修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 结算详情显示
	 */
	public void showBalanceDel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //申请ID
			//根据申请ID获取结算主信息
			Map<String,Object> map=reDao.getBalBillById(applyId);
			//根据申请ID获取交接单信息
			List<Map<String,Object>> flist=reDao.getWayBillMainByAid(applyId);//运费明细
			//根据申请ID获取交接单明细信息
			List<Map<String,Object>> vlist=reDao.getWayBillDtlByAid(applyId);//车辆明细
			
			act.setOutData("map", map);
		    act.setOutData("flist", flist);
		    act.setOutData("vlist", vlist);
			act.setForword(BILL_DETAIL_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"详情页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发票明细信息显示
	 */
	public void showInvoiceDel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //申请ID
			//根据申请ID获取结算主信息
			Map<String,Object> map=reDao.getBalBillById(applyId);
			//根据申请ID获取附件列表
			List<Map<String,Object>> flist=reDao.getFileListByAId(applyId);
			
			act.setOutData("map", map);
		    act.setOutData("flist", flist);
		    act.setOutData("fsize", flist.size());
			act.setForword(INVOICE_DETAIL_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"详情页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
