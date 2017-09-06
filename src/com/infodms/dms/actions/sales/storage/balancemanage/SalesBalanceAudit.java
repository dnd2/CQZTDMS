package com.infodms.dms.actions.sales.storage.balancemanage;

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
import com.infodms.dms.dao.sales.storage.balancemanage.SalesBalanceApplyDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBalApplyPO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.service.balancemanage.SalesBalanceService;
import com.infodms.dms.service.balancemanage.SalesBalanceServiceImpl;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 运费结算审核
 * @author shuyh
 *
 */
public class SalesBalanceAudit{
	public Logger logger = Logger.getLogger(SalesBalanceAudit.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SalesBalanceApplyDao reDao = SalesBalanceApplyDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final String auditInitUrl = "/jsp/sales/storage/balance/billBalAudit.jsp";
	private final String modifyBalUrl = "/jsp/sales/storage/balance/modifyBalInfo.jsp";
	private final String BILL_DETAIL_PAGE = "/jsp/sales/storage/balance/balanceViewDel2.jsp";
	/**
	 * 初始化
	 */
	public void auditInit(){
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
			act.setForword(auditInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费结算审核初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算审核查询
	 */
	public void billAuditList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String applyNo = CommonUtils.checkNull(request.getParamValue("apply_no")); //申请单号
			String logiId = CommonUtils.checkNull(request.getParamValue("logi_id")); //承运商
			/******************************页面查询字段end***************************/
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("logiId", logiId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getBalanceAuditQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算申请查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 信息审核
	 */
	public void auditBillInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] jsProvs = request.getParamValues("JS_PROVINCES"); //财务结算省份
			String[] jsCitys = request.getParamValues("JS_CITYS"); //财务结算城市
			String[] jsCountys = request.getParamValues("JS_COUNTYS"); //财务结算区县
			String[] otherMoneys = request.getParamValues("otherMoneys"); //其他金额
			String[] billIds = request.getParamValues("billIds"); //交接单ID
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算申请ID
			String remark = CommonUtils.checkNull(request.getParamValue("REMARK")); //审核备注
			String pflag = CommonUtils.checkNull(request.getParamValue("pflag")); //1表示通过。2表示驳回
			
			if(billIds!=null&&billIds.length>0&&billIds.length==jsCountys.length){
				if(pflag.equals("1")){//审核通过
					for(int i=0;i<billIds.length;i++){
						String billId=billIds[i];
						String jsProv=jsProvs[i];
						String jsCity=jsCitys[i];
						String jsCounty=jsCountys[i];
						String otherMoney=otherMoneys[i];
						
						//修改交接单信息
						TtSalesWaybillPO po=new TtSalesWaybillPO();
						po.setBillId(Long.parseLong(billId));
						TtSalesWaybillPO po2=new TtSalesWaybillPO();
						po2.setBalProvId(Long.parseLong(jsProv));
						po2.setBalCityId(Long.parseLong(jsCity));
						po2.setBalCountyId(Long.parseLong(jsCounty));
						po2.setUpdateBy(logonUser.getUserId());
						po2.setUpdateDate(new Date());
						
						po2.setOtherMoney(Double.valueOf(otherMoney));//其他金额
						TtSalesWaybillPO tsw=new TtSalesWaybillPO();
						tsw.setBillId(Long.parseLong(billId));
						tsw=(TtSalesWaybillPO) reDao.select(tsw).get(0);
						boolean flag=false;//是否调整，默认否
						if((!tsw.getDlvBalProvId().toString().equals(jsProv))||(!tsw.getDlvBalCityId().toString().equals(jsCity))
								||(!tsw.getDlvBalCountyId().toString().equals(jsCounty))){
							po2.setIsChange(Constant.IF_TYPE_YES);//是否调整：是
							flag=true;
						}
						//根据交接单ID获取交接单明细
						TtSalesWayBillDtlPO tswb=new TtSalesWayBillDtlPO();
						tswb.setBillId(Long.parseLong(billId));
						List blist=reDao.select(tswb);
						Double sumPrice=0d;//用于存放汇总的新结算金额
						for(int j=0;j<blist.size();j++){
							TtSalesWayBillDtlPO tsww=(TtSalesWayBillDtlPO) blist.get(j);
							//根据交接单明细中的物料ID获取运费系数
							Map<String,Object> rmap=reDao.getRatioNumByMat(tsww.getMatId().toString());
							String ratioNum=rmap.get("RATIO_NUM").toString();//系数
							
							String dlvWhId=tsww.getDlvWhId().toString();//发运仓库
							String balCountyId=jsCounty;//财务结算区县
							String shipType=tsw.getDlvShipType().toString();//发运方式
							//根据明细中是否中转计算金额-----start
							//若为中转，并且发运仓库=中转仓库，则挂账金额=中转仓库到结算省市县金额；否则，挂账金额=发运仓库到中转仓库金额+中转仓库到结算省市县金额
							if(tsww.getDlvIsZz().toString().equals(Constant.IF_TYPE_YES.toString())){//中转
								String zzCountyId=tsww.getDlvZzCountyId().toString();//中转区县
								String zzWhId=tsww.getZzWhId().toString();//中转仓库ID
								Double dSum=0d;//用于存放两段总金额
								
								Double newPrice=0d;
								Double dprice=0d;
								if(!dlvWhId.equals(zzWhId)){//发运仓库!=中转仓库
									//根据发运仓库、中转区县、发运方式获取里程信息
									Map<String,Object> dmap=reDao.getDisInfo(dlvWhId, zzCountyId, shipType);
									Double distance=0d;
									Double singlePrice=0d;
									Double handPrice=0d;
									if(null!=dmap&&!dmap.isEmpty()){
										handPrice=Double.valueOf(dmap.get("HAND_PRICE").toString());//手工运价
										distance=Double.valueOf(dmap.get("DISTANCE").toString());//里程
										singlePrice=Double.valueOf(dmap.get("SINGLE_PLACE").toString());//单价
										//运费系数*手工运价即等于新的结算金额
										//if(flag){//已调整
											newPrice=Double.valueOf(ratioNum)*handPrice;//新金额
										//}
										dprice=Double.valueOf(ratioNum)*handPrice;
									}
								}
								//根据中转仓库、结算区县、发运方式获取城市里程信息
								Map<String,Object> dmap2=reDao.getDisInfo(zzWhId, balCountyId, shipType);
								Double handPrice2=0d;
								Double distance2=0d;
								Double singlePrice2=0d;
								Double newPrice2=0d;
								Double dprice2=0d;
								if(null!=dmap2&&!dmap2.isEmpty()){
									handPrice2=Double.valueOf(dmap2.get("HAND_PRICE").toString());//手工运价
									distance2=Double.valueOf(dmap2.get("DISTANCE").toString());//里程
									singlePrice2=Double.valueOf(dmap2.get("SINGLE_PLACE").toString());//单价
									//运费系数*手工运价即等于新的结算金额
									
									//if(flag){//已调整
										newPrice2=Double.valueOf(ratioNum)*handPrice2;//新金额
									//}
									dprice2=Double.valueOf(ratioNum)*handPrice2;
								}
								dSum=newPrice+newPrice2;
								//更新新单价、新里程到交接单明细表
								TtSalesWayBillDtlPO tswp=new TtSalesWayBillDtlPO();
								tswp.setDtlId(tsww.getDtlId());
								TtSalesWayBillDtlPO tswp2=new TtSalesWayBillDtlPO();
								tswp2.setNewPrice(singlePrice2);
								tswp2.setNewMileage(distance2);
								tswp2.setUpdateBy(logonUser.getUserId());
								tswp2.setUpdateDate(new Date());
								//tswp2.setNewAmount(dprice2);
								tswp2.setNewAmount(dprice+dprice2);
								reDao.update(tswp, tswp2);
								sumPrice+=dSum;
							}//若不为中转，挂账金额=发运仓库到结算省市县金额
							else{
								//根据交接单明细中的发运仓库，主表中的财务结算区县、发运方式获取手工运价
								Map<String,Object> dmap=reDao.getDisInfo(dlvWhId, balCountyId, shipType);
								Double handPrice=0d;
								Double distance=0d;
								Double singlePrice=0d;
								Double newPrice=0d;
								Double dprice=0d;
								if(null!=dmap&&!dmap.isEmpty()){
									handPrice=Double.valueOf(dmap.get("HAND_PRICE").toString());//手工运价
									distance=Double.valueOf(dmap.get("DISTANCE").toString());//里程
									singlePrice=Double.valueOf(dmap.get("SINGLE_PLACE").toString());//单价
									//运费系数*手工运价即等于新的结算金额
									//if(flag){//已调整
										newPrice=Double.valueOf(ratioNum)*handPrice;
									//}
									dprice=Double.valueOf(ratioNum)*handPrice;
									
									
								}
								sumPrice+=newPrice;
								//更新新单价、新里程到交接单明细表
								TtSalesWayBillDtlPO tswp=new TtSalesWayBillDtlPO();
								tswp.setDtlId(tsww.getDtlId());
								TtSalesWayBillDtlPO tswp2=new TtSalesWayBillDtlPO();
								tswp2.setNewPrice(singlePrice);
								tswp2.setNewMileage(distance);
								tswp2.setUpdateBy(logonUser.getUserId());
								tswp2.setUpdateDate(new Date());
								tswp2.setNewAmount(dprice);
								reDao.update(tswp, tswp2);
							}
							
						}
						
						//汇总新结算金额，与之前的挂账金额作比较，若新>挂，更新差异金额到补充金额中，否则更新到扣款金额中
						if(sumPrice>tsw.getBillAmount()){
							po2.setSupplyMoney(sumPrice-tsw.getBillAmount());
							po2.setDeductMoney(0d);
						}else{
							po2.setSupplyMoney(0d);
							po2.setDeductMoney(tsw.getBillAmount()-sumPrice);
						}
						
						reDao.update(po, po2);
						
					}
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
					tsb2.setStatus(Constant.BAL_ORDER_STATUS_03);//审核通过
					tsb2.setAuditBy(logonUser.getUserId());
					tsb2.setAuditTime(new Date());
					tsb2.setAuditRemark(remark);
					reDao.update(tsb, tsb2);
				}else{//审核驳回
					TtSalesBalApplyPO ts=new TtSalesBalApplyPO();
					ts.setApplyId(Long.parseLong(applyId));
					TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
					tsb.setStatus(Constant.BAL_ORDER_STATUS_06);//审核驳回
					tsb.setAuditBy(logonUser.getUserId());
					tsb.setAuditTime(new Date());
					tsb.setAuditRemark(remark);
					tsb.setUpdateBy(logonUser.getUserId());
					tsb.setUpdateDate(new Date());
					reDao.update(ts, tsb);
				}
				
			}else{
				throw new Exception("结算审核信息异常！");
			}
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算信息审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 外层列表审核操作
	 */
	public void auditSingle(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String applyId = CommonUtils.checkNull(request.getParamValue("applyId")); //结算申请ID
			String pflag = CommonUtils.checkNull(request.getParamValue("pflag")); //1表示通过。2表示驳回
			TtSalesBalApplyPO ts=new TtSalesBalApplyPO();
			ts.setApplyId(Long.parseLong(applyId));
			TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
			if(pflag.equals("1")){
				tsb.setStatus(Constant.BAL_ORDER_STATUS_03);//审核通过
			}else{
				tsb.setStatus(Constant.BAL_ORDER_STATUS_06);//审核驳回
			}
			tsb.setAuditBy(logonUser.getUserId());
			tsb.setAuditTime(new Date());
			//tsb.setAuditRemark(remark);
			tsb.setUpdateBy(logonUser.getUserId());
			tsb.setUpdateDate(new Date());
			reDao.update(ts, tsb);		
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"结算信息审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 结算提交审核
	 */
	public void  auditDo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//申请单ID
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String remark = CommonUtils.checkNull(request.getParamValue("REMARK")); //审核备注
			String pflag = CommonUtils.checkNull(request.getParamValue("pflag")); //1表示通过。2表示驳回
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
//					int index=0;
//					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
//					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
//							index=j;//获取对应行的下标
//							break;
//						}
//					}
					String applyId=groupIds[i];
					TtSalesBalApplyPO ts=new TtSalesBalApplyPO();
					ts.setApplyId(Long.parseLong(applyId));
					TtSalesBalApplyPO tsb=new TtSalesBalApplyPO();
					if(pflag.equals("1")){
						tsb.setStatus(Constant.BAL_ORDER_STATUS_03);//审核通过
					}else{
						tsb.setStatus(Constant.BAL_ORDER_STATUS_06);//审核驳回
					}
					tsb.setAuditBy(logonUser.getUserId());
					tsb.setAuditTime(new Date());
					tsb.setAuditRemark(remark);
					tsb.setUpdateBy(logonUser.getUserId());
					tsb.setUpdateDate(new Date());
					reDao.update(ts, tsb);
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
