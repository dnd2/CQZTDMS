package com.infodms.dms.actions.parts.purchaseOrderManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseArrConfirDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartDlrOrderDtlPO;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartOemPoPO;
import com.infodms.dms.po.TtPartOperationHistoryPO;
import com.infodms.dms.po.TtPartPoDtlPO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartPoMainPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartSoDtlPO;
import com.infodms.dms.po.TtPartSoMainPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.po.VwPartStockPO;
import com.infodms.dms.util.CalendarUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
/**
 * 采购到货确认、入库、入库查询
 * @author  
 * @version 2017-7-20
 * @see 
 * @since 
 * @deprecated
 */
public class PurchaseArrConfir extends BaseImport {
    public Logger logger = Logger.getLogger(PurchaseArrConfir.class);
    private static final PurchaseArrConfirDao dao = PurchaseArrConfirDao.getInstance();
	/**
	 * 跳转到采购到货确认页面
	 */
	private static final String TO_PUR_RCV_INIT = "/jsp/parts/purchaseOrderManager/toPurRcvInit.jsp";
	/**
	 * 跳转到采购到货入库初始页面
	 */
	private static final String TO_PUR_RCV_INWH_INIT = "/jsp/parts/purchaseOrderManager/toPurRcvInwhInit.jsp";
	/**
	 * 跳转到采购订单查询页面
	 */
	private static final String TO_PURCHASE_ORDER_INIT = "/jsp/parts/purchaseOrderManager/toPurchaseOrderInit.jsp";
	/**
	 * 跳转到采购订单查询页面
	 */
	private static final String TO_MONTHLY_PLAN_INIT = "/jsp/parts/purchaseOrderManager/toMonthlyPlanInit.jsp";
	/**
	 * 跳转到采购订单明细查看页面
	 */
	private static final String TO_PURCHAR_ORDER_MX = "/jsp/parts/purchaseOrderManager/toPurcharOrderMx.jsp";
	/**
	 * 跳转到采购订单入库查看页面
	 */
	private static final String TO_PURCHAR_ORDER_STORAGE = "/jsp/parts/purchaseOrderManager/toPurOrderStorageInit.jsp";
	private static final String TO_ROLLING_PLAN_YSELECT = "/jsp/parts/purchaseOrderManager/toRollingPlanySelect.jsp";
	
	/**
	 * 入库单打印页面
	 */
	private static final String ORDER_POIN_PRINT = "/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderPoinPrint.jsp";

	
	/**
	 * 跳转到采购到货确认页面
	 */
	public void toPurRcvInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_PUR_RCV_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货确认页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货待确认信息
	 */
	public void getPurRcvInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货待确认信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 保存采购确认到货数量 ,将数据插入到tt_part_oem_po表
	 */
	@SuppressWarnings("unchecked")
	public void saveConfirmPurRcvQty(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			String[] cks = request.getParamValues("ck");
			if(cks==null || cks.length==0){
				err = "没有数据，无法保存！";
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			for (int i = 0; i < cks.length; i++) {
				String poId = cks[i];
				String dInQty = request.getParamValue("dInQty_"+poId);//待确认数量
				//根据订单行id(poline_id)生成验收单tt_part_oem_po
				List<Object> ins = new LinkedList<Object>();
	            ins.add(0, poId);//poline_id订单行id
	            ins.add(1, dInQty);//本次验收数量
	            dao.callProcedure("PROC_TT_PART_CREATE_OEM", ins, null);
	            //更新订单明细
	            //采购数量=待确认数量+已确认数量   表示完全验收，否则部分验收
	            TtPartPoDtlPO dp = new TtPartPoDtlPO();//查询订单明细
	            dp.setPolineId(Long.parseLong(poId));
	            dp = (TtPartPoDtlPO) dao.select(dp).get(0);

	            TtPartPoDtlPO po=new TtPartPoDtlPO();
	            po.setSpareQty(dp.getBuyQty()- dp.getCheckQty()-Long.parseLong(dInQty));//待验收数量  采购量-已验收-本次验收=待验收
	            po.setCheckQty(dp.getCheckQty()+Long.parseLong(dInQty));//已验收数量  已验收=之前验收+本次验收
	            
	            if(dp.getBuyQty()==po.getCheckQty()){
	            	 po.setState(15041002);//完全验收
	            }else{
	            	 po.setState(15041001);//部分验收
	            }
	            
	            TtPartPoDtlPO oldPo=new TtPartPoDtlPO();
	            oldPo.setPolineId(Long.parseLong(poId));
				dao.update(oldPo, po);
				
//				TtPartOemPoPO po = new TtPartOemPoPO();
//				po.setPoId(Long.valueOf(poId));
//				po.setOrderId(orderId);--订单id
//		        po.setOrderCode(orderCode) ;--订单编号
//		         po.setPlanId(planId)
//		         po.setPlineId(plineId);
//		         po.setPlanCode(planCode);
//		         po.setPlineNo(plineNo) --计划
//		         po.setBuyerId(buyerId)
//		         po.setBuyer(buyer);
//		         po.setPartType(partType); --部品属性(自制、配套)
//		         po.setPartId(partId);
//		         po.setPartCode(partCode);
//		         po.setPartOldcode(partOldcode);
//		         po.setPartCname(partCname); --部品
//		         po.setUnit(unit);
//		         po.setVenderId(venderId);
//		         po.setVenderCode(venderCode);
//		         po.setVenderName(venderName);--供应商
//		         po.setBuyPrice(buyPrice);--采购单价
//		         po.setPlanQty(planQty);--计划数量
//		         po.setBuyQty(buyQty);--采购数量
//		         po.setBuyAmount(buyAmount);--采购金额
//		         po.setSpareQty(spareQty);--待验收数量
//		         po.setSpareinQty(spareinQty);--待入库数量
//		         po.setCheckQty(checkQty);--已验货数量
//		         po.setGenerateQty(generateQty);--生成数量
//		         po.setForecastDate(forecastDate);--预计到货日期
//		         po.setWhId(whId);
//		         po.setWhName(whName);--库房
//		         po.setRemark(remark);
//		         po.setCreateDate(createDate); 
//		         po.setCreateBy(createBy);
//		         po.setOrgId(orgId);
//		         po.setVer(1);--版本 
//		         po.setState(92271001);--单据状态
//		         po.setStatus(status);--状态
//		         po.setOdlineId(odlineId);--订单行ID
//		         po.setOriginType((92751001)--部品订单来源类型(92751001计划、领用、直发)
//		         po.setPartCategory(95711001);--配件类别：配件95711001
			}
			
			if(StringUtil.notNull(err)){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			act.setOutData("success", "保存成功！");
		}catch (Exception e) {//异常方法
			POContext.endTxn(false);//回滚事务
			POContext.cleanTxn();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存采购确认到货数量异常!");
			if(StringUtil.notNull(err)){
				e1.setMessage(err);
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 采购到货确认导出
	 */
	public void exportPurRcv(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurRcvInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("订单单号 ");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("配件件号");
            listHead.add("单位");
            listHead.add("供应商");
            listHead.add("计划数量");
            listHead.add("采购数量");
            listHead.add("待确认数量");
            listHead.add("制单日期");
            listHead.add("状态");
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CNAME");
            listKey.add("PART_CODE");
            listKey.add("UNIT");
            listKey.add("VENDER_NAME");
            listKey.add("PLAN_QTY");
            listKey.add("BUY_QTY");
            listKey.add("DCON_QTY");
            listKey.add("CREATE_DATE");
            listKey.add("STATE");
			// 导出的文件名
            String fileName = "到货确认"+CalendarUtil.convertDateToString(new Date())+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"下载周度计划批量修改模板异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurRcvInit();
		}
	}
	

	
	
	/**
	 * 跳转到采购到货入库初始页面
	 */
	public void toPurRcvInwhInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(TO_PUR_RCV_INWH_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购到货入库初始页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询采购到货入库信息
	 */
	public void getPurRcvInwhInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurRcvInwhInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询采购到货入库信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 采购到货入库
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void instockPurRcvParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{
			String[] cks = request.getParamValues("ck");
			if(cks==null || cks.length==0){
				err += "没有选择入库信息，无法入库!<br>";
            	throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有选择入库信息，无法入库!");
			}
			//入库单编码
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);
            
            Long soId = CommonUtils.parseLong(SequenceManager.getSequence(""));//销售单id
            int soIdi=0;//销售单行明细id 
            List<String> soMainStrList=new ArrayList<String>();//销售key,经销商采购订单号+经销商id
            List<Map<String,Object>> soMainList=new ArrayList<Map<String,Object>>();//存需要新增的销售单主数据

            List<String> inCodeStrList=new ArrayList<String>();//入库单key,采购订单号
			//入库  添加到tt_part_po_in
			for (int i = 0; i < cks.length; i++) {
				String poId = cks[i];//验收单id
				String dInQty = request.getParamValue("dInQty_"+poId);//待入库数量
				String inRemark = request.getParamValue("inRemark_"+poId);
				String batchNo=request.getParamValue("batchNo_"+poId);
				String locCode=request.getParamValue("LOC_CODE_"+poId);//货位loc_code
				if(StringUtil.notNull(inRemark)){
					if(inRemark.length()>100){
						inRemark = inRemark.substring(0, 100);
					}
				}
				//件入库
				this.instockMain(inCode,poId,dInQty,inRemark,batchNo,locCode,err,act,loginUser,inCodeStrList);
				//20170804 add 
				//通过验收单id查找订单id
				TtPartOemPoPO po1 = new TtPartOemPoPO();
				po1.setPoId(Long.valueOf(poId));
				TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po1).get(0);
				Long dealerOrderId=pos.getDealerOrderId();//经销商采购订单id
				Long orderId=pos.getOrderId();//总部采购订单id
				Long partId=pos.getPartId();//配件id
				
				//如果是销售采购订单类型需要生成销售单
				TtPartPoMainPO po = new TtPartPoMainPO();
				po.setOrderId(orderId);
				TtPartPoMainPO pom = (TtPartPoMainPO) dao.select(po).get(0);
				
//				System.out.println("******dealerOrderId:"+dealerOrderId);
//				System.out.println("******orderId:"+orderId);
//				System.out.println("******pom.getBuyerType():"+pom.getBuyerType());
//				System.out.println("******Constant:"+Constant.PARTS_PURCH_ORDER_TYPE_XZC);
				
				if((pom.getBuyerType()+"").equals(Constant.PARTS_PURCH_ORDER_TYPE_XZC+"")){
					soIdi=soIdi+1;
					//查询订单主数据
					TtPartDlrOrderMainPO mainPoSel=new TtPartDlrOrderMainPO(); 
					mainPoSel.setOrderId(dealerOrderId);
					TtPartDlrOrderMainPO mainPo = (TtPartDlrOrderMainPO) dao.select(mainPoSel).get(0);
					String key=mainPo.getOrderId()+"_"+mainPo.getDealerId();//订单id+订货单位id
					//查询订单单明细
					TtPartDlrOrderDtlPO dtlPoSel=new TtPartDlrOrderDtlPO(); 
					dtlPoSel.setOrderId(dealerOrderId);
					dtlPoSel.setPartId(partId);
					TtPartDlrOrderDtlPO dtlPo = (TtPartDlrOrderDtlPO) dao.select(dtlPoSel).get(0);
					//不包括的情况下重取销售单id
					if(!soMainStrList.contains(key)){
//						logger.info("******soId:"+soId);
						soId = CommonUtils.parseLong(SequenceManager.getSequence(""));
						Map<String,Object> map=new HashMap<String, Object>();
						map.put("soId", soId);
						map.put("dealerOrderId", dealerOrderId); 
						soMainList.add(map); 
					}
					//添加销售单明细数据
					insertSoDtlInfo(soIdi,pos,dtlPo,dInQty,soId,dealerOrderId,act,loginUser);

					soMainStrList.add(key);
				}
			}
			//添加销售单主数据
			if(soMainList.size()>0){
				 for (Map<String, Object> m : soMainList){
					 insertSoMainInfo(Long.parseLong(m.get("soId")+""),m.get("dealerOrderId")+"",loginUser);
					//资源锁定 start 资源占用(TT_PART_BOOK_DTL,占用明细表)
	                List ins = new ArrayList();
	                ins.add(0, Long.parseLong(m.get("soId")+""));
	                ins.add(1, Constant.PART_CODE_RELATION_07);
	                ins.add(2, 1);
	                dao.callProcedure("PROC_TT_PART_UPDATE_PART_STATE", ins, null);
	                //资源锁定 end
				 }
			}
			
        	act.setOutData("success", "入库成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购到货入库异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage("入库失败！"+e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 入库完成后，经销商销售采购订单类型需要生成销售单，明细
	 * @param pos 验收单对象
	 * @param dtlPo
	 * @param dInQty
	 * @param soId
	 * @param dealerOrderId 经销商采购订单id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void insertSoDtlInfo(int i,TtPartOemPoPO pos,TtPartDlrOrderDtlPO dtlPo,String dInQty,Long soId,
			Long dealerOrderId,ActionContext act,AclUserBean loginUser) throws Exception {
        try {
        	
            //获取当前用户机构ID,查询库存量
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            Long stockQty=0l;
            Map<String,Object> map=dao.getStockQty(dtlPo.getPartId(),orgId);
            if(map!=null && map.size()>0){
            	stockQty=Long.parseLong(map.get("STOCK_QTY")==null?"0":(map.get("STOCK_QTY")+""));
            }
            //-------------------------------------插入销售订单明细数据-------------------------------------
            //如果明细存在同样的part_id和so_id则更新
            TtPartSoDtlPO selPo = new TtPartSoDtlPO();
	       	selPo.setPartId(dtlPo.getPartId());
	       	selPo.setSoId(soId);
	
	       	List selList=dao.select(selPo);
	       	if(selList!=null && selList.size()>0){
	       		//更新销售数量，销售金额
	           	TtPartSoDtlPO selResultPo=(TtPartSoDtlPO)selList.get(0);
	           	TtPartSoDtlPO updSoPo = new TtPartSoDtlPO();
	           	updSoPo.setSalesQty(selResultPo.getSalesQty()+Long.parseLong(dInQty));//销售数量=本次入库的数量
	           	System.out.println("===stockQty:"+stockQty);
	           	System.out.println("===selResultPo.getStockQty():"+selResultPo.getStockQty());
	           	updSoPo.setStockQty(stockQty);//当前库存
	           	updSoPo.setBuyAmount(Double.parseDouble((selResultPo.getBuyPrice()*updSoPo.getSalesQty())+""));//销售金额
	           	dao.update(selPo, updSoPo); 
	       	}else{
	       		//添加销售单明细
	            TtPartSoDtlPO po = new TtPartSoDtlPO();
	            po.setSoId(soId);
	            po.setSlineId(Long.parseLong((i + 1 + soId) + ""));//明细id
	            po.setOrderId(dealerOrderId);
	            po.setPartId(Long.valueOf(dtlPo.getPartId()));
	            po.setPartOldcode(dtlPo.getPartOldcode());
	            po.setPartCode(dtlPo.getPartCode());
	            po.setPartCname(dtlPo.getPartCname());
	            po.setUnit(dtlPo.getUnit());
	            //根据订单中partid获取供应商
	            if (pos!=null) {
	                po.setVenderId(pos.getVenderId());
	                po.setVenderName(pos.getVenderName());
	                po.setVenderCode(pos.getVenderCode());
	            }
	            po.setIsDirect(dtlPo.getIsDirect());//直发件，如机油
	            po.setIsPlan(dtlPo.getIsPlan());//大件、占空间（如保险杠）
	            po.setIsLack(dtlPo.getIsLack());//紧缺件
	            po.setIsReplaced(Constant.IF_TYPE_NO);//是否替换件
	            po.setIsGift(Constant.IF_TYPE_NO);//是否赠品
	            po.setStockQty(stockQty);//当前库存
	            po.setMinPackage(dtlPo.getMinPackage());//最小包装量
	            po.setBuyQty(Long.valueOf(dtlPo.getBuyQty()));//订货数量
	            po.setSalesQty(Long.parseLong(dInQty));//销售数量=本次入库的数量
	            po.setBuyPrice(dtlPo.getBuyPrice());//销售单价
	            po.setBuyAmount(Double.parseDouble((dtlPo.getBuyPrice()*Integer.parseInt(dInQty))+""));//销售金额
	            po.setRemark("销售采购订单");
	            po.setCreateBy(loginUser.getUserId());
	            po.setCreateDate(new Date());
	            
	            po.setSalesQty(po.getSalesQty());

	            //单价和金额重新校验
	            if (po.getBuyPrice() == 0d || po.getBuyAmount() == 0d) {
	                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + po.getPartOldcode() + "】销售数据出现异常,请联系管理员!");
	                throw e1;
	            }
	            //插入销售订单明细数据
	            dao.insert(po);
	       	}
        } catch (Exception ex) {
            throw ex;
        }
    }
    
	
	/**
	 * 入库完成后，经销商销售采购订单类型需要生成销售单,主表
	 * @param mainPo
	 * @param soId
	 * @param orderId
	 * @param amount
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void insertSoMainInfo(Long soId,String dealerOrderId,AclUserBean loginUser) throws Exception {
        try {
            //-------------------------------------插入销售订单主数据-------------------------------------
            //订单编码
            String soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07);
            //查询订单信息
            TtPartDlrOrderMainPO po1=new TtPartDlrOrderMainPO();
            po1.setOrderId(Long.parseLong(dealerOrderId));
            TtPartDlrOrderMainPO mainPo=(TtPartDlrOrderMainPO)dao.select(po1).get(0);
            //销售主订单信息
            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(soId);
            po.setSoCode(soCode);
            po.setOrderId(Long.valueOf(dealerOrderId));
            po.setOrderCode(mainPo.getOrderCode());
            po.setOrderType(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12);//销售采购订单
            po.setIsBatchso(Constant.PART_BASE_FLAG_NO);//默认否铺货
            po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_02);//订单生成
            po.setPayType(mainPo.getPayType());//支付方式，目前只有现金
            po.setDealerId(mainPo.getDealerId());//订货单位ID
            po.setDealerCode(mainPo.getDealerCode());
            po.setDealerName(mainPo.getDealerName());
            po.setSellerId(mainPo.getSellerId());//销售单位ID
            po.setSellerCode(mainPo.getSellerCode());
            po.setSellerName(mainPo.getSellerName());
            po.setSaleDate(new Date());//审核日期
            po.setBuyerId(mainPo.getBuyerId());//订货人ID
            po.setBuyerName(mainPo.getBuyerName());
            po.setConsigneesId(mainPo.getRcvOrgid());//接收单位ID
            po.setConsignees(mainPo.getRcvOrg());//接收单位
            po.setAddrId(mainPo.getAddrId());//接收地址ID
            po.setAddr(mainPo.getAddr());//接收地址
            po.setReceiver(mainPo.getReceiver());//接收人
            po.setTel(mainPo.getTel());//接收人电话
            po.setPostCode(mainPo.getPostCode());//邮政编码
            po.setStation(mainPo.getStation());//到站名称
            po.setTransType(mainPo.getTransType());//发运方式
//            po.setTranspayType(Integer.valueOf(transpayType));//运费支付方式
            po.setAmount(dao.getSoMainAmount(soId));//销售金额
            po.setDiscount(mainPo.getDiscount());//折扣率
            po.setRemark(mainPo.getRemark());//订单备注
            po.setRemark2("销售采购订单");//备注
            
            po.setWhId(Long.parseLong("2013061319370891"));//仓库ID  中心仓默认库房id
            po.setVer(1);//版本
            po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);//生成销售单状态直接为财务审核通过
            po.setFcauditDate(new Date());//财务审核日期
            po.setSubmitDate(new Date());
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setAccountId(mainPo.getAccountId());//账户ID
            po.setCheckId(soId);//校验ID(唯一不允许重复)
            
            
//            po.setLockFreight(1);//运费锁定
//            po.setFreight(freight);//运费
//            po.setIsTransfree(Constant.IF_TYPE_NO);//是否免运费
            
            //插入销售订单主表数据
            dao.insert(po);
            
            //插入日志操作记录
            saveHistory(loginUser,soCode,Long.valueOf(dealerOrderId));
            //-----------------------------------------------------------------------------------------------------------
        } catch (Exception ex) {
            throw ex;
        }
    }
    
	/**
     * 保存日志 Tt_Part_Operation_History
     * @param req
     * @param act
     * @param status
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void saveHistory(AclUserBean loginUser,String soCode,Long orderId) throws Exception {
        try {
            TtPartOperationHistoryPO po= new TtPartOperationHistoryPO();
            po.setBussinessId(soCode);
            po.setOptId(Long.parseLong(SequenceManager.getSequence("")));
            po.setOptBy(loginUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_02);
            po.setWhat("配件销售单");
            po.setOptName(loginUser.getName());
            po.setStatus(Constant.CAR_FACTORY_SALE_ORDER_STATE_01);
            po.setOrderId(orderId);
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }

    
	
	/**
	 * 入库
	 * @param inCode 入库编码
	 * @param poId 采购单id
	 * @param dInQty 入库数
	 * @param inRemark 入库备注
	 * @param batchNo 批次号
	 * @param locCode 货位code
	 * @param err
	 * @param act
	 * @param loginUser
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void instockMain(String inCode,String poId,String dInQty, String inRemark,String batchNo, 
			String locCode,String err,ActionContext act, AclUserBean loginUser,List<String> inCodeStrList) throws Exception{
		//查询验收单信息
		TtPartOemPoPO po = new TtPartOemPoPO();
		po.setPoId(Long.valueOf(poId));
		TtPartOemPoPO pos = (TtPartOemPoPO) dao.select(po).get(0);
		//不存在时插入新的入库单编码
		if(!inCodeStrList.contains(pos.getOrderCode())){
			inCodeStrList.add(pos.getOrderCode());
			inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);
		}
		logger.info("===采购入库====in_po key:"+pos.getOrderCode()+"=======in_po inCode:"+inCode);
		
		//判断可入库数量
		Long dInQtyL = Long.valueOf(dInQty);
		Long krQty = pos.getBuyQty() - pos.getInQty();//可入库 = 采购 - 已入库
		if(dInQtyL>krQty){
			err += "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]入库数量["+dInQty+"]不能大于可入库数量["+krQty+"]!<br>");
		}
		//判断配件是否完全验收，只有部分验收才能入库
		if (pos.getState().equals(Constant.PARTS_ORDER_OEM_STATUS_YES)) {
			err += "配件["+pos.getPartOldcode()+"]已全部验收!";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]已全部验收!<br>");
        }

		/*if (pos.getState().equals(Constant.PART_PURCHASE_ORDERCHK_STATUS_02)) {
			err += "配件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>";
			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "备件["+pos.getPartOldcode()+"]已经被关闭,请打开后再入库!<br>");
        }*/
		
    	//判断当前要入库的仓库是否有效
        TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
        warehouseDefinePO.setWhId(pos.getWhId());
        warehouseDefinePO.setState(Constant.STATUS_ENABLE);
        warehouseDefinePO.setStatus(1);

        List<TtPartWarehouseDefinePO> wList = dao.select(warehouseDefinePO);
        if(wList==null || wList.isEmpty() || wList.size() == 0){
        	err += "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!";
            throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件["+pos.getPartOldcode()+"]对应的入库库房已经失效,请选择重新选择!<br>");
        }else{
        	//判断是否有对应货位信息
        	TtPartOemPoPO pox = new TtPartOemPoPO();
        	pox.setInQty(pos.getInQty() + dInQtyL);
//        	pox.setSpareQty(dInQtyL);//待验收数量
//        	pox.setCheckQty(pos.getCheckQty()-dInQtyL);//待入库数量
        	pox.setSpareinQty(pos.getSpareinQty()-dInQtyL);//待入库数量=待入库数量-本次入库数量
//        	if(pos.getBuyQty().intValue() == pox.getInQty()){//采购数量==入库数量 表示完全验收
//        		pox.setState(Integer.parseInt(Constant.PARTS_ORDER_STATUS_YES));//状态为PARTS_ORDER_STATUS_YES
//        	}
        	pox.setInBy(loginUser.getUserId());
        	pox.setInDate(new Date());
        	
        	Long partId = pos.getPartId();//获取配件id
            Long orgId = pos.getOrgId();//获取单位id
            Long whId = pos.getWhId();
            List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            Map<String, Object> locMap = dao.getLoc(locCode,partId,orgId, whId );
            Map<String, Object> map = dao.queryPartAndLocationInfo(partId, orgId, whId);//查询当前配件信息及其货位信息
            
            if (locMap==null) {
    			err += "未查找到配件["+pos.getPartOldcode()+"]的货位信息!";
    			throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG,  "未查找到配件["+pos.getPartOldcode()+"]的货位信息!<br>");
            }
            TtPartWarehouseDefinePO warehousePO = wList.get(0);
            
            
            TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中
            Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            inPo.setInId(InId);//入库id
            inPo.setInCode((inCode));//入库编码
            inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_01);//入库类型： 一般入库
            inPo.setCheckId(pos.getPoId());//验收ID
            inPo.setPoId(pos.getOrderId());//采购订单id
            inPo.setOrderCode(pos.getOrderCode());//采购订单号
            inPo.setIsCheck(1);//是否确认
            inPo.setPlanCode(pos.getPlanCode());//计划单号
            if (map != null && map.get("BUYER_ID") != null) {
                inPo.setBuyerId(pos.getBuyerId());//采购员ID
            }
            TtPartDefinePO dp = new TtPartDefinePO();//配件数
            dp.setPartOldcode(pos.getPartOldcode());
            dp = (TtPartDefinePO) dao.select(dp).get(0);
            String up = CommonUtils.checkNull(dp.getSuperiorPurchasing());
            if (!up.equals("") && up != null) {
            	//财务供应商，结算用
            	inPo.setFcvderId(pos.getVenderId());
                inPo.setFcvderrCode(pos.getVenderCode());
            } else {
                inPo.setFcvderId(pos.getVenderId());
                inPo.setFcvderrCode(pos.getVenderCode());
            }
            inPo.setPartType(pos.getPartType());//配件类型
            inPo.setPartId(pos.getPartId());//配件ID
            inPo.setPartCode(pos.getPartCode());//配件件号
            inPo.setPartOldcode(pos.getPartOldcode());//配件编码
            inPo.setPartCname(pos.getPartCname());//配件名称
            inPo.setBatchNo(batchNo);//批次号
            inPo.setUnit(pos.getUnit());//配件包装单位
            inPo.setSpareQty(dInQtyL);//待结算数量
            inPo.setVenderId(pos.getVenderId());//配件供应商ID
            inPo.setVenderCode(pos.getVenderCode());//配件供应商编码
            inPo.setVenderName(pos.getVenderName());//配件供应商名称
            inPo.setBuyPrice(pos.getBuyPrice());//配件采购单价，通过和供应商关系获取
            inPo.setBuyQty(pos.getBuyQty());//采购数量
            inPo.setCheckQty(pos.getCheckQty());//验货数量
            inPo.setInQty(dInQtyL);//已入库数量
            inPo.setBuyPriceNotax(Arith.round(Arith.div(pos.getBuyPrice(), Arith.add(1, Constant.PART_TAX_RATE)), 6));//无税单价
            inPo.setInAmount(Arith.mul(pos.getBuyPrice(), inPo.getInQty()));//入库金额
            inPo.setTaxRate(Constant.PART_TAX_RATE);//税率
            inPo.setInAmountNotax(Arith.mul(inPo.getBuyPriceNotax(), inPo.getInQty()));//无税总金额
            inPo.setWhId(whId);//库房iD
            inPo.setWhName(warehousePO.getWhName());//库房2015032711875757名称
            inPo.setLocCode(locCode);//货位编码
            inPo.setLocId(((BigDecimal) locMap.get("LOC_ID")).longValue());//货位ID

            inPo.setCheckDate(new Date());
            inPo.setCheckBy(loginUser.getUserId());

            inPo.setProduceFac(pos.getProduceFac());//采购方式 
            inPo.setSuperiorPurchasing(pos.getSuperiorPurchasing());//采购方式的结算单位
            inPo.setProduceState(pos.getProduceState());//采购状态，92631002表示外购，92631001表示自制
            //查询库存数量
            VwPartStockPO itemStockPO = new VwPartStockPO();
            itemStockPO.setPartId(inPo.getPartId());
            itemStockPO.setLocId(inPo.getLocId());
            itemStockPO.setOrgId(inPo.getOrgId());
            itemStockPO.setWhId(inPo.getWhId());
            itemStockPO.setState(Constant.STATUS_ENABLE);
            List<VwPartStockPO> list = dao.select(itemStockPO);
            if (list.size() > 0) {
                itemStockPO = (VwPartStockPO) list.get(0);
                inPo.setItemQty(itemStockPO.getItemQty());//库存数量
            }
            inPo.setRemark(inRemark);
            inPo.setInDate(new Date());
            inPo.setInBy(loginUser.getUserId());
            inPo.setCreateDate((new Date()));
            inPo.setCreateBy(loginUser.getUserId());
            inPo.setOrgId(loginUser.getOrgId());
            inPo.setOriginType(pos.getOriginType());
            //配件只要入库，入库单的状态就变为结算中
            inPo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
            
            
            
            //插入出入库记录表 add by yuan 20130513
            TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
            ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttPartRecordPO.setAddFlag(1);//入库标记
            ttPartRecordPO.setState(1);//正常入库
            ttPartRecordPO.setPartNum(dInQtyL);//入库数量
            ttPartRecordPO.setTranstypeId(0l);//默认0
            ttPartRecordPO.setPartId(pos.getPartId());//配件ID
            ttPartRecordPO.setPartCode(pos.getPartCode());//配件件号
            ttPartRecordPO.setPartOldcode(pos.getPartOldcode());//配件编码
            ttPartRecordPO.setPartName(pos.getPartCname());//配件名称
            ttPartRecordPO.setPartBatch(batchNo);//配件批次
//            ttPartRecordPO.setVenderId(pos.getVenderId());//配件供应商,数据库表默认21799
            ttPartRecordPO.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));//配件供应商,数据库表默认21799
            ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_04));//入库单
            ttPartRecordPO.setOrderId(InId);//入库单ID
            ttPartRecordPO.setOrderCode(inCode);//入库单编码
            //ttPartRecordPO.setLineId();
            ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
            ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
            ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
            ttPartRecordPO.setWhId(whId);
            ttPartRecordPO.setWhName(warehousePO.getWhName());
            ttPartRecordPO.setLocId(Long.valueOf(CommonUtils.checkNull(locMap.get("LOC_ID"))));
            ttPartRecordPO.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
            ttPartRecordPO.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
            ttPartRecordPO.setOptDate(new Date());
            ttPartRecordPO.setCreateDate(new Date());
            ttPartRecordPO.setPersonId(loginUser.getUserId());
            ttPartRecordPO.setPersonName(loginUser.getName());
            ttPartRecordPO.setPartState(1);
            
            dao.insert(inPo);//新增订单入库信息 tt_part_po_in
            dao.insert(ttPartRecordPO);//新增出入库记录tt_part_record
            dao.update(po, pox);//修改信息 tt_part_oem_po
            
            //调用入库逻辑
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, InId);//入库单id
            ins.add(1, Constant.PART_CODE_RELATION_04);
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
        }
        
		
	}
	
	
	
	/**
	 * 采购到货入库-导出功能
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void exportPurRcvParts() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> listData = dao.getPurRcvInwhInfo(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			if(listData.size() >= 60000) {
				throw new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "数据超过6万请分批导出！");
			}
			
			List headList = new ArrayList();
			headList.add("订单单号");
			headList.add("配件编码");
			headList.add("配件名称");
			headList.add("配件件号");
			headList.add("单位");
			headList.add("供应商");
			headList.add("批次号");
			headList.add("采购数量");
			headList.add("入库数量");
			headList.add("库存数量");
			headList.add("库房");
			headList.add("货位");
			headList.add("入库备注");
			headList.add("配件类型");
			headList.add("入库日期");
			headList.add("状态");
			/*headList.add("税率");
			headList.add("无税单价");
			headList.add("无税金额");*/
			
			
			List keyList = new ArrayList();
			keyList.add("ORDER_CODE");
			keyList.add("PART_OLDCODE");
			keyList.add("PART_CNAME");
			keyList.add("PART_CODE");
			keyList.add("UNIT");
			keyList.add("VENDER_NAME");
			keyList.add("BATCH_NO");
			keyList.add("BUY_QTY");
			keyList.add("IN_QTY");
			keyList.add("ITEM_QTY");
			keyList.add("WH_NAME");
			keyList.add("LOC_CODE");
			keyList.add("REMARK");
			keyList.add("PART_TYPE_CN");
			keyList.add("IN_DATE");
			keyList.add("STATE_CN");
			/*keyList.add("TAX_RATE");
			keyList.add("BUY_PRICE_NOTAX");
			keyList.add("IN_AMOUNT_NOTAX");*/
			
			String fileName = "采购到货入库导出" + CalendarUtil.convertDateToString(new Date())+".xls";
			pagingExportExcel(response, fileName, headList, keyList, listData, 60000);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出EXCEL表异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 跳转到采购订单查询页面
	 */
	public void toPurchaseOrderInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			String nowUserId = loginUser.getUserId().toString();
			act.setOutData("nowUserId", nowUserId);
			List<Map<String,Object>> purUserList = dao.getPurUserInfos();
			act.setOutData("purUserList", purUserList);
			act.setForword(TO_PURCHASE_ORDER_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到月度计划查询页面
	 */
	public void toMonthlyPlanInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			String nowUserId = loginUser.getUserId().toString();
			act.setOutData("nowUserId", nowUserId);
//			List<Map<String,Object>> purUserList = dao.getPurUserInfos();
//			act.setOutData("purUserList", purUserList);
			act.setForword(TO_MONTHLY_PLAN_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到月度计划查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取采购订单汇总
	 */
	public void getPurcharOrderHz(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurcharOrderHz(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取采购订单汇总异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 获取月度计划汇总
	 */
	public void getMonthlyPlanHz(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getMonthlyPlanHz(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取月度计划汇总异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询月度计划信息明细
	 */
	public void getMonthlyPlanHzs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getMonthlyPlanHzs(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询计划信息明细异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 导出订单汇总信息
	 */
	public void exportPurOrderHz(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurcharOrderHz(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			if(listRs!=null && !listRs.isEmpty() && listRs.size()>60000){
				throw new BizException(act,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息超过6万条，请输入查询条件，多次导出!");
			}
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("采购单号 ");
            listHead.add("计划单号");
            listHead.add("供应商编码");
            listHead.add("供应商名称");
            listHead.add("采购项数");
            listHead.add("采购数量");
            listHead.add("订单日期");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PLAN_CODE");
            listKey.add("VENDER_CODE");
            listKey.add("VENDER_NAME");
            listKey.add("PART_COUNT");
            listKey.add("CHECK_QTY_SUM");
            listKey.add("CREATE_DATE");
            
			// 导出的文件名
            String fileName = "采购订单汇总表"+CalendarUtil.convertDateToString(new Date())+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurchaseOrderInit();
		}
	}
	/**
	 * 导出月度计划汇总信息
	 */
	public void exMonthlyPlanHz(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			String plan_id=request.getParamValue("planId");
			List<Map<String, Object>> listRs = dao.getMonthlyPlanHzsList(plan_id);
			if(listRs!=null && listRs.size()>60000){
				throw new BizException(act,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息超过6万条，请输入查询条件，多次导出!");
			}
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("计划单号 ");
            listHead.add("计划数量");
            listHead.add("备件编码");
            listHead.add("备件件号");
            listHead.add("备件名称");
            listHead.add("备件类型");
            listHead.add("供应商编码");
            listHead.add("供应商名称");
            listHead.add("计划日期");
            listHead.add("状态");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("PLAN_NO");
            listKey.add("PLAN_NUM");
            listKey.add("PART_OLDCODE");
            listKey.add("PART_CODE");
            listKey.add("PART_CNAME");
            listKey.add("TARGET");
            listKey.add("VENDER_CODE");
            listKey.add("VENDER_NAME");
            listKey.add("MONTH_DATE");
            listKey.add("STATUS");
			// 导出的文件名
            String fileName = "月度需求计划表"+CalendarUtil.convertDateToString(new Date())+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出月度需求计划信息异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurchaseOrderInit();
		}
	}
	
	
	/**
	 * 跳转到采购订单明细查看页面
	 */
	public void toPurcharOrderMx(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String orderCode = request.getParamValue("orderCode");
			act.setOutData("orderCode", orderCode);
			act.setForword(TO_PURCHAR_ORDER_MX);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单明细查看页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据订单号获取采购订单明细
	 */
	public void getPurcharOrderMx(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurcharOrderMx(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "根据订单号获取采购订单明细异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 跳转到采购订单入库查询页面-初始化
	 */
	public void toPurOrderStorageInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(TO_PURCHAR_ORDER_STORAGE);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单入库查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 跳转到月度计划查看页面
	 */
	public void toRollingPlanySelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String planId = request.getParamValue("planId");
			String planTypes = request.getParamValue("planTypes");
            act.setOutData("planId", planId);
            act.setOutData("planTypes", planTypes);
			act.setForword(TO_ROLLING_PLAN_YSELECT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到滚动计划编制查看页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到采购订单入库查询页面--查询
	 */
	public void getPurOrderStorageInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPurOrderStorageInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到采购订单入库查询页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 导出采购订单入库信息--导出
	 */
	public void exportPurOrderStorage(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
		try{
			List<Map<String, Object>> listRs = dao.getPurcharOrderHz(request, loginUser, 1, Constant.PAGE_SIZE_MAX).getRecords();
			if(listRs!=null && !listRs.isEmpty() && listRs.size()>60000){
				throw new BizException(act,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出订单汇总信息超过6万条，请输入查询条件，多次导出!");
			}
			List<String> listHead = new ArrayList<String>();//导出模板第一列
            //标题
			listHead.add("采购单号 ");
            listHead.add("计划单号");
            listHead.add("供应商编码");
            listHead.add("供应商名称");
//            listHead.add("采购种类");
            listHead.add("采购数量");
            listHead.add("订单日期");
            
            List<String> listKey = new ArrayList<String>();
            listKey.add("ORDER_CODE");
            listKey.add("PLAN_CODE");
            listKey.add("VENDER_CODE");
            listKey.add("VENDER_NAME");
//            listKey.add("PART_COUNT");
            listKey.add("SUM_QTY");
            listKey.add("CREATE_DATE");
            
			// 导出的文件名
            String fileName = "采购订单入库"+CalendarUtil.convertDateToString(new Date())+".xls";
            // 分页导出
            pagingExportExcel(response,fileName,listHead,listKey,listRs,50000);
            
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出采购订单入库信息异常!");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser,e1);
			act.setException(e1);
			this.toPurchaseOrderInit();
		}
	}
	
	
	/**
	 * 批量入库单
	 */
	@SuppressWarnings("unchecked")
	public void toPurConfirmAsnPrints(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String[] cks = request.getParamValues("ck");
			List<Map<String,Object>> listPo = new ArrayList<Map<String,Object>>();
			if(cks!=null && cks.length>0){
				for (int i = 0; i < cks.length; i++) {
					String conId = cks[i];
					Map<String,Object> po = dao.getPurConfirmPrintInfoByConId(conId);
					listPo.add(po);
				}
			}
			act.setOutData("userName", loginUser.getName());
			act.setOutData("userDate", new  Date());
			act.setOutData("listPo", listPo);
			act.setForword(ORDER_POIN_PRINT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "批量打印入库单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
}
