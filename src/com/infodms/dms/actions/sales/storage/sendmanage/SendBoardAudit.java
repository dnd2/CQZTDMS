package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
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
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TsiTtSalesBoardPO;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtVsDispatchOrderDtlPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 发运组板审核
 * @author shuyh
 * 2017/7/28
 */
public class SendBoardAudit {
	public Logger logger = Logger.getLogger(SendBoardAudit.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendBoardDao reDao = SendBoardDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBoardInitUtl = "/jsp/sales/storage/sendmanage/sendBoard/sendBoardAuditList.jsp";
	
	private final String cancelInitUtl = "/jsp/sales/storage/sendmanage/sendBoard/sendBoardCancelList.jsp";
	/**
	 * 发运组板审核初始化
	 */
	public void sendBoardAuditInit(){
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
			act.setForword(sendBoardInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"组板审核查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 组板审核查询
	 */
	public void SendBoardAuditQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String boardNo = CommonUtils.checkNull(request.getParamValue("boardNo")); //组板号
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //组板提交日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 组板提交日期结束
			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //省份
			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 城市
			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 区县
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("boardNo", boardNo);
			map.put("transType", transType);
			map.put("logiName", logiName);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("provinceId", provinceId);
			map.put("cityId", cityId);
			map.put("countyId", countyId);
			map.put("poseId", logonUser.getPoseId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.sendBoardAuditQuery(map, curPage,
					Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("ps", ps);	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 组板审核
	 */
	public void  checkBoardAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] boIds=request.getParamValues("boIds");//组板IDs
			//String[] dlvTypes=request.getParamValues("dlvTypes");//发运类型（订单或调拨）
			//String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String type=request.getParamValue("type");//1表示审核通过，2表示审核驳回
			String remark=request.getParamValue("REMARK");//审核备注
			if(type.equals("1")){//为审核通过时
				if(boIds.length>0){
					for(int i=0;i<boIds.length;i++){
//						int index=0;
//						for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
//						if(boIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
//								index=j;//获取对应行的下标
//								break;
//							}
//						}
						/**
						 * 1、更新订单发运主表的发运状态
						 */
						//根据调拨单ID判断发运表中订单是否已全部组板，若是，则更新发运主表状态
						Map<String,Object> mapT=reDao.getDlvInfoByBid(boIds[i]);
						if(Integer.parseInt(mapT.get("ORD_TOTAL").toString())<=Integer.parseInt(mapT.get("DLV_BD_TOTAL").toString())){
							reDao.updateDvlStatus(boIds[i], logonUser.getUserId().toString());
						}
						/**
						 * 2、如果发运类型为调拨单，则更新调拨单主表和明细表的组板数量
						 */
						List<Map<String, Object>> malist=reDao.getDlvTypeByBid(boIds[i]);
						StringBuffer sb=new StringBuffer();
						if(malist!=null&&malist.size()>0){
							for(int j=0;j<malist.size();j++){
								Map<String, Object> amap=malist.get(j);
								if(amap.get("DLV_TYPE").toString().equals(String.valueOf(Constant.DELIVERY_ORD_TYPE_ALLOCAT))){
									TtVsDispatchOrderDtlPO tvd=new TtVsDispatchOrderDtlPO();
									tvd.setDetailId(Long.parseLong(amap.get("OR_DE_ID").toString()));
									tvd.setMaterialId(Long.parseLong(amap.get("MAT_ID").toString()));
									List rlist=reDao.select(tvd);
									tvd=(TtVsDispatchOrderDtlPO)rlist.get(0);
									TtVsDispatchOrderDtlPO tvdd=new TtVsDispatchOrderDtlPO();
									tvdd.setDetailId(Long.parseLong(amap.get("OR_DE_ID").toString()));
									tvdd.setMaterialId(Long.parseLong(amap.get("MAT_ID").toString()));
									TtVsDispatchOrderDtlPO tvdd2=new TtVsDispatchOrderDtlPO();
									if(tvd.getBoardNumber()!=null){
										tvdd2.setBoardNumber(tvd.getBoardNumber()+Integer.parseInt(amap.get("BOARD_NUM").toString()));//针对部分组板
									}else{
										tvdd2.setBoardNumber(Integer.parseInt(amap.get("BOARD_NUM").toString()));//针对部分组板
									}
									tvdd2.setUpdateBy(logonUser.getUserId());
									tvdd2.setUpdateDate(new Date());
									reDao.update(tvdd, tvdd2);//更新调拨单明细
									//mslist.add(amap.get("BO_ID").toString());
									sb.append(amap.get("BO_ID").toString()+",");
								}
							}
							//更新调拨单主表的调拨数量
							String[] str=sb.toString().split(",");
							StringBuffer ss=new StringBuffer();
							for(int s=0;s<str.length;s++){
								if(s==str.length-1){
									ss.append("'"+str[s]+"'");
								}else
									ss.append("'"+str[s]+"',");
								
							}
							List<Map<String, Object>> blist=reDao.getBDNumByOrdId(ss.toString());
							if(blist!=null&&blist.size()>0){
								for(int k=0;k<blist.size();k++){
									Map<String, Object> map=(Map<String, Object>) blist.get(k);
									TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
									tvp.setDispId(Long.parseLong(map.get("ORDER_ID").toString()));
									List vlist=reDao.select(tvp);
									if(vlist!=null&&vlist.size()>0){
										TtVsDispatchOrderPO tva=(TtVsDispatchOrderPO) vlist.get(0);
										TtVsDispatchOrderPO tvo=new TtVsDispatchOrderPO();
										if(tva.getBoNum()!=null){
											tvo.setBoNum(tva.getBoNum()+Integer.parseInt(map.get("BO_NUM").toString()));
										}else{
											tvo.setBoNum(Integer.parseInt(map.get("BO_NUM").toString()));
										}
										reDao.update(tvp, tvo);
									}
								}
							}
							
						}
						
						//更新组板主表的组板确认状态
						TtSalesBoardPO tsb=new TtSalesBoardPO();//组板表实体
						tsb.setBoId(Long.parseLong(boIds[i]));
						TtSalesBoardPO tsb2=new TtSalesBoardPO();//组板表实体
						tsb2.setBoStatus("1");//已确认
						tsb2.setUpdateBy(logonUser.getUserId());
						tsb2.setUpdateDate(new Date());
						tsb2.setAuditBy(logonUser.getUserId());
						tsb2.setAuditRemark(remark);
						tsb2.setAuditTime(new Date());
						reDao.update(tsb, tsb2);
					}
				}
				
				
			}else if(type.equals("2")){//为审核驳回时
				if(boIds.length>0){
					for(int i=0;i<boIds.length;i++){
						/**
						 * 1、更新订单发运主表和明细表的组板数量和发运状态
						 */
						//更新发运明细表的组板数量
						List<Map<String, Object>> mlist=reDao.getBODetailByBid(boIds[i]);
						List rlist=new ArrayList();
						for(int j=0;j<mlist.size();j++){
							Map<String, Object> map=mlist.get(j);
							TtVsDlvryDtlPO tvdd=new TtVsDlvryDtlPO();
							tvdd.setOrdDetailId(Long.parseLong(map.get("OR_DE_ID").toString()));
							tvdd.setMaterialId(Long.parseLong(map.get("MAT_ID").toString()));
							List dlist=reDao.select(tvdd);
							for(int m=0;m<dlist.size();m++){
								TtVsDlvryDtlPO tvdp=(TtVsDlvryDtlPO) dlist.get(m);
								rlist.add(tvdp.getReqId());//获取变更的发运ID
								TtVsDlvryDtlPO tvdd2=new TtVsDlvryDtlPO();
								//明细组板总数-本次取消组板数量
								tvdd2.setBdTotal(tvdp.getBdTotal()-Integer.parseInt(map.get("BOARD_NUM").toString()));
								tvdd2.setUpdateBy(logonUser.getUserId());
								tvdd2.setUpdateDate(new Date());
								reDao.update(tvdd, tvdd2);
							}
						}
						//更新发运主表的组板数量和发运状态
						if(rlist!=null&&rlist.size()>0){
							for(int n=0;n<rlist.size();n++){
								Map<String, Object> map=reDao.getDlvBoByRid(rlist.get(n).toString());
								TtVsDlvryPO tvd=new TtVsDlvryPO();
								String reqId=map.get("REQ_ID").toString();
								tvd.setReqId(Long.parseLong(reqId));
								TtVsDlvryPO tvd2=new TtVsDlvryPO();
								tvd2.setDlvBdTotal(Integer.parseInt(map.get("BD_TOTAL").toString()));
								//根据发运ID查询是否存在已组板确认的数据，若存在，变更为部分组板，否则为已分派审核
								Map<String,Object> mapb=reDao.getBoCountAlready(reqId);
								if(Integer.parseInt(mapb.get("BO_COUNT").toString())>0){
									tvd2.setDlvStatus(Constant.ORDER_STATUS_04);//部分组板
								}else{
									tvd2.setDlvStatus(Constant.ORDER_STATUS_03);//已分派审核
								}
								
								tvd2.setUpdateBy(logonUser.getUserId());
								tvd2.setUpdateDate(new Date());
								reDao.update(tvd, tvd2);
								
							}
							
						}
						
						/**
						 * 2、删除组板主表和明细表的相关数据
						 */
						//删除组板明细表
						TtSalesBoDetailPO tsbd =new TtSalesBoDetailPO();//组板明细表实体
						tsbd.setBoId(Long.parseLong(boIds[i]));
						reDao.delete(tsbd);
						//删除组板主表
						TtSalesBoardPO tsb=new TtSalesBoardPO();//组板表实体
						tsb.setBoId(Long.parseLong(boIds[i]));
						reDao.delete(tsb);
					}
				}
				
				
			}
		act.setOutData("returnValue", 1);
	} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "组板信息确认");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
  }
	/**
	 * 发运组板取消初始化
	 */
	public void cancelQueryInit(){
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
			act.setForword(cancelInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"组板取消查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 组板取消查询
	 */
	public void SendBoardCancelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String boardNo = CommonUtils.checkNull(request.getParamValue("boardNo")); //组板号
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //组板提交日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 组板提交日期结束
			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //省份
			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 城市
			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 区县
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("boardNo", boardNo);
			map.put("transType", transType);
			map.put("logiName", logiName);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("provinceId", provinceId);
			map.put("cityId", cityId);
			map.put("countyId", countyId);
			map.put("poseId", logonUser.getPoseId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.sendBoardCancelQuery(map, curPage,
					Constant.PAGE_SIZE_MIDDLE);
			act.setOutData("ps", ps);	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板取消查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 组板取消
	 */
	public void  cancelBoardAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] boIds=request.getParamValues("boIds");//组板IDs
			if(boIds.length>0){
				for(int i=0;i<boIds.length;i++){
					/**
					 * 1、向日志主表和明细表中新增组板记录
					 * 2、更新发运主表和明细表的组板数量和发运状态
					 * 3、判断发运类型是否为调拨单，若是，更新调拨单主表和明细表的组板数量
					 * 4、删除组板主表和明细表的相关记录
					 */
					reDao.insert(insertTsiBoardPo(boIds[i], logonUser.getUserId()));
					reDao.insert(insertTsiBoardDtlPo(boIds[i], logonUser.getUserId()));
					
					/**
					 * 根据组板ID查询发运信息;
					 * 若全部组板并且为同一组板号，则更新 发运状态为分派已审核，更新组板数量为0
					 * 若全部组板但不是同一组板号，更新发运状态为部分组板，并且减去本次取消组板的数量
					 * 若部分组板并且为同一组板号，更新 发运状态为分派已审核，更新组板数量为0
					 * 若部分组板但不是同一组板号，更新为减去本次组板数量的值
					 */
					List<Map<String, Object>> list=reDao.getDlvOrderByBoid(boIds[i]);
					if(list!=null&&list.size()>0){
						for(int j=0;j<list.size();j++){
							Map<String, Object> smap=list.get(j);
							String dlvStatus=smap.get("DLV_STATUS").toString();
							String ordId=smap.get("ORD_ID").toString();
							//获取组板主信息
//							TtSalesBoardPO tsb=new TtSalesBoardPO();
//							tsb.setBoId(Long.parseLong(boIds[i]));
//							tsb=(TtSalesBoardPO) reDao.select(tsb).get(0);
							//获取组板明细信息
//							TtSalesBoDetailPO tsbd=new TtSalesBoDetailPO();
//							tsbd.setBoId(Long.parseLong(boIds[i]));
//							List dlist=reDao.select(tsbd);
							
							//循环获取订单ID，根据订单ID获取组板号
							Map<String,Object> bmap=reDao.getBoCountByoId(ordId);
							if(bmap==null||bmap.isEmpty()){
								throw new Exception("获取组板信息异常！");
							}
							int countBo=Integer.parseInt(bmap.get("COUNT_BO").toString());
							if(countBo==1){//同一组板号，更新 发运状态为分派已审核，更新组板数量为0
								//更新发运主表和明细表的组板数量
								TtVsDlvryPO tvd=new TtVsDlvryPO();
								tvd.setOrdId(Long.parseLong(ordId));
								TtVsDlvryPO tvd2=new TtVsDlvryPO();
								tvd2.setDlvStatus(Constant.ORDER_STATUS_03);//分派已审核
								tvd2.setDlvBdTotal(0);
								tvd2.setUpdateBy(logonUser.getUserId());
								tvd2.setUpdateDate(new Date());
								reDao.update(tvd, tvd2);
								
								TtVsDlvryDtlPO tvdp=new TtVsDlvryDtlPO();
								tvdp.setOrdId(Long.parseLong(ordId));
								TtVsDlvryDtlPO tvdp2=new TtVsDlvryDtlPO();
								tvdp2.setBdTotal(0);
								tvdp2.setUpdateBy(logonUser.getUserId());
								tvdp2.setUpdateDate(new Date());
								reDao.update(tvdp, tvdp2);
								//判断发运类型是否为调拨单，若是，更新调拨单主表和明细表的组板数量
								tvd=(TtVsDlvryPO) reDao.select(tvd).get(0);
								if(tvd.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ALLOCAT.toString())){
									//更新调拨单主表
									TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
									tvp.setDispId(Long.parseLong(ordId));
									TtVsDispatchOrderPO tvp2=new TtVsDispatchOrderPO();
									tvp2.setBoNum(0);
									tvp2.setUpdateBy(logonUser.getUserId());
									tvp2.setUpdateDate(new Date());
									reDao.update(tvp, tvp2);
									//更新调拨单明细表
									TtVsDispatchOrderDtlPO tvpd=new TtVsDispatchOrderDtlPO();
									tvpd.setDispId(Long.parseLong(ordId));
									TtVsDispatchOrderDtlPO tvpd2=new TtVsDispatchOrderDtlPO();
									tvpd2.setBoardNumber(0);
									tvpd2.setUpdateBy(logonUser.getUserId());
									tvpd2.setUpdateDate(new Date());
									reDao.update(tvpd, tvpd2);
								}
							}else if(countBo>1){//非同一组板号
								//根据组板ID和订单ID汇总本次组板数量
								Map<String,Object> omap=reDao.getBoSumCountByOrdId(ordId, boIds[i]);
								//若为全部组板，更新发运状态为部分组板
								TtVsDlvryPO tvs=new TtVsDlvryPO();
								tvs.setOrdId(Long.parseLong(ordId));
								tvs=(TtVsDlvryPO) reDao.select(tvs).get(0);
								TtVsDlvryPO tvd=new TtVsDlvryPO();
								tvd.setOrdId(Long.parseLong(ordId));
								TtVsDlvryPO tvd2=new TtVsDlvryPO();
								if(dlvStatus.equals(Constant.ORDER_STATUS_06.toString())
										||dlvStatus.equals(Constant.ORDER_STATUS_05.toString())){//全部组板
									tvd2.setDlvStatus(Constant.ORDER_STATUS_03);//分派已审核
								}
								if(tvs.getDlvBdTotal()==0||tvs.getDlvBdTotal()==null){
									tvd2.setDlvBdTotal(0);
								}else{
									tvd2.setDlvBdTotal(tvs.getDlvBdTotal()-Integer.parseInt(omap.get("BO_SUM").toString()));//已组板数量-本次组板数量
								}
								tvd2.setUpdateBy(logonUser.getUserId());
								tvd2.setUpdateDate(new Date());
								reDao.update(tvd, tvd2);
								//判断发运类型是否为调拨单，若是，更新调拨单主表的组板数量
								if(tvs.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ALLOCAT.toString())){
									TtVsDispatchOrderPO tvps=new TtVsDispatchOrderPO();
									tvps.setDispId(Long.parseLong(ordId));
									tvps=(TtVsDispatchOrderPO) reDao.select(tvps).get(0);
									//更新调拨单主表
									TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
									tvp.setDispId(Long.parseLong(ordId));
									TtVsDispatchOrderPO tvp2=new TtVsDispatchOrderPO();
									
									if(tvps.getBoNum()==0||tvps.getBoNum()==null){
										tvp2.setBoNum(0);
									}else{
										tvp2.setBoNum(tvps.getBoNum()-Integer.parseInt(omap.get("BO_SUM").toString()));//已组板数量-本次组板数量
									}
									
									tvp2.setUpdateBy(logonUser.getUserId());
									tvp2.setUpdateDate(new Date());
									reDao.update(tvp, tvp2);
								}
								TtVsDlvryDtlPO tdd=new TtVsDlvryDtlPO();
								tdd.setOrdId(Long.parseLong(ordId));
								List dlist=reDao.select(tdd);
								//更新发运明细表的组板数量
								for(int m=0;m<dlist.size();m++){
									TtVsDlvryDtlPO tsbp=(TtVsDlvryDtlPO) dlist.get(m);
//									//根据组板ID和订单明细ID汇总组板数量
									Map<String,Object> ddmap=reDao.getBoSumCountDtlByOrdId(tsbp.getOrdDetailId().toString(), boIds[i]);
									
									TtVsDlvryDtlPO tvdp=new TtVsDlvryDtlPO();
									tvdp.setReqDetailId(tsbp.getReqDetailId());
									TtVsDlvryDtlPO tvdp2=new TtVsDlvryDtlPO();
									if(tsbp.getBdTotal()==0||tsbp.getBdTotal()==null){
										tvdp2.setBdTotal(0);
									}else{
										tvdp2.setBdTotal(tsbp.getBdTotal()-Integer.parseInt(ddmap.get("BO_SUM_D").toString()));//已组板数量-本次组板数量
									}
									
									tvd2.setUpdateBy(logonUser.getUserId());
									tvd2.setUpdateDate(new Date());
									reDao.update(tvdp, tvdp2);
									//判断发运类型是否为调拨单，若是，更新调拨单明细表的组板数量
									if(tvs.getDlvType().toString().equals(Constant.DELIVERY_ORD_TYPE_ALLOCAT.toString())){
										TtVsDispatchOrderDtlPO tvps=new TtVsDispatchOrderDtlPO();
										tvps.setDetailId(Long.parseLong(tsbp.getOrdDetailId().toString()));
										tvps=(TtVsDispatchOrderDtlPO) reDao.select(tvps).get(0);
										
										TtVsDispatchOrderDtlPO tvp=new TtVsDispatchOrderDtlPO();
										tvp.setDetailId(Long.parseLong(tsbp.getOrdDetailId().toString()));
										TtVsDispatchOrderDtlPO tvp2=new TtVsDispatchOrderDtlPO();
										if(tvps.getBoardNumber()==0||tvps.getBoardNumber()==null){
											tvp2.setBoardNumber(0);
										}else{
											tvp2.setBoardNumber(tvps.getBoardNumber()-Integer.parseInt(ddmap.get("BO_SUM_D").toString()));//已组板数量-本次组板数量
										}
										
										tvp2.setUpdateBy(logonUser.getUserId());
										tvp2.setUpdateDate(new Date());
										reDao.update(tvp, tvp2);
									}
								}
								
							}else{
								throw new Exception("获取组板信息异常！");
							}
						}
					}
					//根据组板ID删除组板主表和明细表的数据
					//删除组板明细信息
					TtSalesBoDetailPO tsbd=new TtSalesBoDetailPO();
					tsbd.setBoId(Long.parseLong(boIds[i]));
					reDao.delete(tsbd);
					//删除组板主信息
					TtSalesBoardPO tsb=new TtSalesBoardPO();
					tsb.setBoId(Long.parseLong(boIds[i]));
					reDao.delete(tsb);
					
				}
			}
		act.setOutData("returnValue", 1);
	} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "组板信息确认");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
  }
	/**
	 * 向组板取消日志主表插入数据
	 * @param boId
	 * @param userId
	 * @return
	 */
	public String insertTsiBoardPo(String boId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into tsi_tt_sales_board\n" );
		sql.append("  (BO_ID,\n" );
		sql.append("   BO_NO,\n" );
		sql.append("   BO_PER,\n" );
		sql.append("   BO_DATE,\n" );
		sql.append("   BO_NUM,\n" );
		sql.append("   ALLOCA_NUM,\n" );
		sql.append("   ALLOCA_PER,\n" );
		sql.append("   ALLOCA_DATE,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   HANDLE_STATUS,\n" );
		sql.append("   OUT_NUM,\n" );
		sql.append("   SEND_NUM,\n" );
		sql.append("   ACC_NUM,\n" );
		sql.append("   IS_ENABLE,\n" );
		sql.append("   AREA_ID,\n" );
		sql.append("   CAR_TEAM,\n" );
		sql.append("   LOADS,\n" );
		sql.append("   CAR_NO,\n" );
		sql.append("   POLICY_NO,\n" );
		sql.append("   POLICY_TYPE,\n" );
		sql.append("   DRIVER_NAME,\n" );
		sql.append("   DRIVER_TEL,\n" );
		sql.append("   HAVE_RETAIL,\n" );
		sql.append("   BO_STATUS,\n" );
		sql.append("   DLV_SHIP_TYPE,\n" );
		sql.append("   DLV_LOGI_ID,\n" );
		sql.append("   DLV_BAL_PROV_ID,\n" );
		sql.append("   DLV_BAL_CITY_ID,\n" );
		sql.append("   DLV_BAL_COUNTY_ID,\n" );
		sql.append("   DLV_FY_DATE,\n" );
		sql.append("   DLV_JJ_DATE,\n" );
		sql.append("   PLAN_LOAD_DATE)\n" );
		sql.append("  select BO_ID,\n" );
		sql.append("         BO_NO,\n" );
		sql.append("         BO_PER,\n" );
		sql.append("         BO_DATE,\n" );
		sql.append("         BO_NUM,\n" );
		sql.append("         ALLOCA_NUM,\n" );
		sql.append("         ALLOCA_PER,\n" );
		sql.append("         ALLOCA_DATE,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         sysdate,\n" );
		sql.append("         HANDLE_STATUS,\n" );
		sql.append("         OUT_NUM,\n" );
		sql.append("         SEND_NUM,\n" );
		sql.append("         ACC_NUM,\n" );
		sql.append("         IS_ENABLE,\n" );
		sql.append("         AREA_ID,\n" );
		sql.append("         CAR_TEAM,\n" );
		sql.append("         LOADS,\n" );
		sql.append("         CAR_NO,\n" );
		sql.append("         POLICY_NO,\n" );
		sql.append("         POLICY_TYPE,\n" );
		sql.append("         DRIVER_NAME,\n" );
		sql.append("         DRIVER_TEL,\n" );
		sql.append("         HAVE_RETAIL,\n" );
		sql.append("         BO_STATUS,\n" );
		sql.append("         DLV_SHIP_TYPE,\n" );
		sql.append("         DLV_LOGI_ID,\n" );
		sql.append("         DLV_BAL_PROV_ID,\n" );
		sql.append("         DLV_BAL_CITY_ID,\n" );
		sql.append("         DLV_BAL_COUNTY_ID,\n" );
		sql.append("         DLV_FY_DATE,\n" );
		sql.append("         DLV_JJ_DATE,\n" );
		sql.append("         PLAN_LOAD_DATE\n" );
		sql.append("    from tt_sales_board tt\n" );
		sql.append("   where tt.bo_id = "+boId+"\n");
		return sql.toString();
	}
	/**
	 * 向组板取消明细日志表写入数据
	 * @param boId
	 * @param userId
	 * @return
	 */
	public String insertTsiBoardDtlPo(String boId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into tsi_tt_sales_bo_detail\n" );
		sql.append("  (BO_DE_ID,\n" );
		sql.append("   BO_ID,\n" );
		sql.append("   OR_DE_ID,\n" );
		sql.append("   MAT_ID,\n" );
		sql.append("   COLOR_CODE,\n" );
		sql.append("   INVOICE_NUM,\n" );
		sql.append("   BOARD_NUM,\n" );
		sql.append("   ALLOCA_NUM,\n" );
		sql.append("   OUT_NUM,\n" );
		sql.append("   SEND_NUM,\n" );
		sql.append("   ACC_NUM,\n" );
		sql.append("   CAR_NO,\n" );
		sql.append("   LOADS,\n" );
		sql.append("   IS_ENABLE,\n" );
		sql.append("   CAR_TEAM,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   LOGI_ID,\n" );
		sql.append("   DEALER_ID,\n" );
		sql.append("   REC_DEALER_ID,\n" );
		sql.append("   ADDRESS_ID,\n" );
		sql.append("   ORDER_TYPE,\n" );
		sql.append("   ORDER_NO,\n" );
		sql.append("   ACC_TYPE,\n" );
		sql.append("   AREA_ID,\n" );
		sql.append("   INVOICE_NO,\n" );
		sql.append("   FIN_CHK_DATE,\n" );
		sql.append("   PLAN_CHK_DATE,\n" );
		sql.append("   RAISE_DATE,\n" );
		sql.append("   SEND_TYPE,\n" );
		sql.append("   LINK_MAN,\n" );
		sql.append("   TEL,\n" );
		sql.append("   ORDER_ID,\n" );
		sql.append("   ASS_DATE,\n" );
		sql.append("   ASS_PER,\n" );
		sql.append("   ASS_REMARK,\n" );
		sql.append("   BILL_ID,\n" );
		sql.append("   ORDER_NUM,\n" );
		sql.append("   INVOICE_NO_VER,\n" );
		sql.append("   IS_RETAIL,\n" );
		sql.append("   REC_DEALER_NAME,\n" );
		sql.append("   REC_SHORTDEALER_NAME)\n" );
		sql.append("  select BO_DE_ID,\n" );
		sql.append("         BO_ID,\n" );
		sql.append("         OR_DE_ID,\n" );
		sql.append("         MAT_ID,\n" );
		sql.append("         COLOR_CODE,\n" );
		sql.append("         INVOICE_NUM,\n" );
		sql.append("         BOARD_NUM,\n" );
		sql.append("         ALLOCA_NUM,\n" );
		sql.append("         OUT_NUM,\n" );
		sql.append("         SEND_NUM,\n" );
		sql.append("         ACC_NUM,\n" );
		sql.append("         CAR_NO,\n" );
		sql.append("         LOADS,\n" );
		sql.append("         IS_ENABLE,\n" );
		sql.append("         CAR_TEAM,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         sysdate,\n" );
		sql.append("         LOGI_ID,\n" );
		sql.append("         DEALER_ID,\n" );
		sql.append("         REC_DEALER_ID,\n" );
		sql.append("         ADDRESS_ID,\n" );
		sql.append("         ORDER_TYPE,\n" );
		sql.append("         ORDER_NO,\n" );
		sql.append("         ACC_TYPE,\n" );
		sql.append("         AREA_ID,\n" );
		sql.append("         INVOICE_NO,\n" );
		sql.append("         FIN_CHK_DATE,\n" );
		sql.append("         PLAN_CHK_DATE,\n" );
		sql.append("         RAISE_DATE,\n" );
		sql.append("         SEND_TYPE,\n" );
		sql.append("         LINK_MAN,\n" );
		sql.append("         TEL,\n" );
		sql.append("         ORDER_ID,\n" );
		sql.append("         ASS_DATE,\n" );
		sql.append("         ASS_PER,\n" );
		sql.append("         ASS_REMARK,\n" );
		sql.append("         BILL_ID,\n" );
		sql.append("         ORDER_NUM,\n" );
		sql.append("         INVOICE_NO_VER,\n" );
		sql.append("         IS_RETAIL,\n" );
		sql.append("         REC_DEALER_NAME,\n" );
		sql.append("         REC_SHORTDEALER_NAME\n" );
		sql.append("    from tt_sales_bo_detail t\n" );
		sql.append("   where t.bo_id = "+boId+"\n");
		return sql.toString();
	}
}
