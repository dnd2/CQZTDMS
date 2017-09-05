package com.infodms.dms.actions.parts.partAllotManager;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.util.CheckUtil;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.baseManager.partsBaseManager.PartBaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.partAllotManager.PartTransferDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartBuyPriceHistoryPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDlrOrderDtlPO;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartTransferManager 
 * @Description   : 配件调拨单管理 
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-7
 */
public class PartTransferManager extends PartBaseImport implements PTConstants{

	public Logger logger = Logger.getLogger(PartTransferManager.class);
	private PartTransferDao dao = PartTransferDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件调拨单查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-7
	 */
	public void queryPartTransferInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			boolean flag = true;//是否是车厂,如果是车厂只能查看调拨单
			if(logonUser.getDealerId()!=null){
				flag = false;
			}
			act.setOutData("flag", flag);
			act.setForword(PART_TRANSFER_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询配件调拨单
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-7
	 */
	public void queryPartTransferInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
    	    boolean flag = true;//是否是车厂,如果是车厂只能查看调拨单
			if(logonUser.getDealerId()!=null){
				flag = false;
			}
			act.setOutData("flag", flag);
			String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
			String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//调入单位
			String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//调出单位
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
			String state = CommonUtils.checkNull(request.getParamValue("STATE"));//订单状态
			String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//提交开始时间
			String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//提交结束时间
			
			TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
			po.setOrderCode(orderCode);
			po.setDealerName(dealerName);
			po.setSellerName(sellerName);
			if(!"".equals(state)){
				po.setState(CommonUtils.parseInteger(state));
			}
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartTransferList(po,startDate,endDate,startDate1,endDate1,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 调拨单新增初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-8
	 */
	public void addPartTransferInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_15);//调拨单号
			
			long sellerId=0;//调出单位id
			String sellerName="";//调出单位名称
			String sellerCode="";//调出单位编码
			TmDealerPO tmDealerPO = new TmDealerPO();
			sellerId = CommonUtils.parseLong(logonUser.getDealerId());
			tmDealerPO.setDealerId(sellerId);
			tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
			sellerName = tmDealerPO.getDealerName();
			sellerCode = tmDealerPO.getDealerCode();
			String createName = logonUser.getName();//制单人
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String now = format.format(date);//制单日期
			
			List list = dao.getPartWareHouseList(logonUser);//获取当前机构的库房信息
			act.setOutData("wareHouses", list);
			
			List list1 = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
			act.setOutData("transList",list1);
			
			act.setOutData("createName", createName);
			act.setOutData("orderCode", orderCode);
			act.setOutData("sellerId", sellerId);
			act.setOutData("sellerCode", sellerCode);
			act.setOutData("sellerName", sellerName);
			act.setOutData("now", now);
			act.setForword(PART_TRANSFER_ADD_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 通过仓库id查询配件信息  
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-8
	 */
	public void queryPartInfoByWhId(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
			String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
			String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartByWhIdList(whId,logonUser,partCode,partOldCode,partCname,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 保存或提报调拨信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-8
	 */
	public void savePartTransfer(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String flag = request.getParamValue("flag");//保存或提报标志
			String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_15);//调拨单号
			String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId"));//调出单位id
			String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode"));//调出单位编码
			String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//调出单位名称
			String now = CommonUtils.checkNull(request.getParamValue("now"));//制单日期
			String rcvOrgName = CommonUtils.checkNull(request.getParamValue("RCV_ORG"));//接收单位名称
			String rcvCode = CommonUtils.checkNull(request.getParamValue("RCV_CODE"));//接收单位编码
			String rcvOrgId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID"));//接收单位id
			String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER"));//接收人
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));//接收地址
			String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID"));//接收地址id
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));//接收人电话
			String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE"));//邮政编码
			String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
			String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE"));//付款方式
			String amount = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT"));//总金额
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
			String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));//仓库名称
			String station = CommonUtils.checkNull(request.getParamValue("STATION"));//到站名称
			String remark1 = CommonUtils.checkNull(request.getParamValue("remark1"));//备注
			
			TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
			mainPO.setOrderId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			mainPO.setOrderCode(orderCode);
			mainPO.setOrderType(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05);
			mainPO.setPayType(CommonUtils.parseInteger(payType));
			mainPO.setDealerId(CommonUtils.parseLong(rcvOrgId));
			mainPO.setDealerCode(rcvCode);
			mainPO.setDealerName(rcvOrgName);
			mainPO.setSellerId(CommonUtils.parseLong(sellerId));
			mainPO.setSellerCode(sellerCode);
			mainPO.setSellerName(sellerName);
			mainPO.setBuyerName(receiver);
			mainPO.setRcvOrgid(CommonUtils.parseLong(rcvOrgId));
			mainPO.setRcvOrg(rcvOrgName);
			mainPO.setAddrId(CommonUtils.parseLong(addrId));
			mainPO.setAddr(addr);
			mainPO.setReceiver(receiver);
			mainPO.setTel(tel);
			mainPO.setPostCode(postCode);
			mainPO.setStation(station);
			mainPO.setTransType(transType);
			mainPO.setOrderAmount(CommonUtils.parseDouble(amount.replace(",","")));
			mainPO.setRemark(remark1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			mainPO.setCreateDate(new Date());
			mainPO.setCreateBy(logonUser.getUserId());
			mainPO.setIsAutchk(Constant.IF_TYPE_NO);
			if("1".equals(flag)){//提报
				/*mainPO.setState(Constant.PART_TRANSFER_ORDER_STATE_01);*/ //modify by yuan 20130701    z
				mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
				mainPO.setSubmitDate(new Date());
			}
			if("0".equals(flag)){//保存
				mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01);
			}
			mainPO.setWhId(CommonUtils.parseLong(whId));
			mainPO.setWhCname(whName);
			
			String[] partIds = request.getParamValues("cb");//配件id
			//在保存明细之前还需要验证调拨数量与当前库房可用数量
			List<Map<String,Object>> list = dao.queryCurNormalQty(partIds,whId,logonUser.getDealerId());
			if(partIds!=null&&partIds.length>0){
				for(int i=0;i<partIds.length;i++){
					Map<String, Object> map = list.get(i);
					long curQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();//当前可用数量
					String allotQtyStr = CommonUtils.checkNull(request.getParamValue("allotQty_"+partIds[i]));//调拨数量
					long allotQty = CommonUtils.parseLong(allotQtyStr);
					String partCname = CommonUtils.checkNull(request.getParamValue("partCname_"+partIds[i]));//配件名称
					if(curQty<allotQty){
						act.setOutData("error", "当前"+partCname+"的调拨数量大于可调拨数量,请重新输入!");
						act.setOutData("curQty", curQty);
						act.setOutData("partId", partIds[i]);
						return;
					}
					String partCode = CommonUtils.checkNull(request.getParamValue("partCode_"+partIds[i]));//配件件号
					String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode_"+partIds[i]));//配件编码
					String unit = CommonUtils.checkNull(request.getParamValue("unit_"+partIds[i]));//单位
					String isDirect = CommonUtils.checkNull(request.getParamValue("IS_DIRECT"+partIds[i]));//是否直发
					String isPaln = CommonUtils.checkNull(request.getParamValue("IS_PLAN"+partIds[i]));//大件、占空间（如保险杠）
					String isLack = CommonUtils.checkNull(request.getParamValue("IS_LACK"+partIds[i]));//紧缺
					String isReplaced = CommonUtils.checkNull(request.getParamValue("IS_REPLACED"+partIds[i]));//是否有替代件
					String allotPrice = CommonUtils.checkNull(request.getParamValue("allotPrice_"+partIds[i]));//调拨单价
					String orderAmount = CommonUtils.checkNull(request.getParamValue("orderAmount_"+partIds[i]));//调拨金额
					String remark = CommonUtils.checkNull(request.getParamValue("remark_"+partIds[i]));//备注
					String idx = CommonUtils.checkNull(request.getParamValue("idx_"+partIds[i]));//行号
					String miniPack = CommonUtils.checkNull(request.getParamValue("miniPack_"+partIds[i]));//最小包装量
					
					TtPartDlrOrderDtlPO dtlPO = new TtPartDlrOrderDtlPO();
					dtlPO.setLineId(CommonUtils.parseLong(SequenceManager.getSequence("")));
					dtlPO.setOrderId(mainPO.getOrderId());
					dtlPO.setLineNo(CommonUtils.parseLong(idx));
					dtlPO.setPartId(CommonUtils.parseLong(partIds[i]));
					dtlPO.setPartCode(partCode);
					dtlPO.setPartOldcode(partOldcode);
					dtlPO.setPartCname(partCname);
					dtlPO.setUnit(unit);
					if(!"".equals(isDirect)&&!"null".equals(isDirect)&&!"".equals(isDirect)){
						dtlPO.setIsDirect(CommonUtils.parseInteger(isDirect));
					}
					if(isPaln!=null&&!"null".equals(isPaln)&&!"".equals(isPaln)){
						dtlPO.setIsPlan(CommonUtils.parseInteger(isPaln));
					}
					if(isLack!=null&&!"null".equals(isLack)&&!"".equals(isLack)){
						dtlPO.setIsLack(CommonUtils.parseInteger(isLack));
					}
					if(isReplaced!=null&&!"null".equals(isReplaced)&&!"".equals(isReplaced)){
						dtlPO.setIsReplaced(CommonUtils.parseInteger(isReplaced));
					}
					if(miniPack!=null&&!"null".equals(miniPack)&&!"".equals(miniPack)){
						dtlPO.setMinPackage(CommonUtils.parseLong(miniPack));
					}
					dtlPO.setStockQty(curQty);
					dtlPO.setBuyQty(allotQty);
					dtlPO.setBuyPrice(CommonUtils.parseDouble(allotPrice));
					dtlPO.setBuyAmount(Arith.mul(dtlPO.getBuyQty(), dtlPO.getBuyPrice()));
					dtlPO.setRemark(remark);
					dtlPO.setCreateDate(new Date());
					dtlPO.setCreateBy(logonUser.getUserId());
					
					dao.insert(dtlPO);
				}
			}
			dao.insert(mainPO);
			if("1".equals(flag)){//如果是提报的话就要调用占用逻辑
				//调用库存占用逻辑
	           /* List ins = new LinkedList<Object>();
	            ins.add(0, mainPO.getOrderId());
	            ins.add(1, Constant.PART_CODE_RELATION_15);
	            ins.add(2,1);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);*/
				act.setOutData("success", "提报成功!");
			}
			if("0".equals(flag)){//保存
				act.setOutData("success", "保存成功!");
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 上传模板下载 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-9
	 */
	public void expPartTransferTmp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
			listHead.add("配件编码");
			listHead.add("调拨数量");

			list.add(listHead);
			// 导出的文件名
			String fileName = "配件调拨模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 上传调拨信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-9
	 */
	public void uploadPartTransferExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID")); //仓库ID
			String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//调拨单号
			String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId"));//调出单位id
			String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode"));//调出单位编码
			String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//调出单位名称
			String now = CommonUtils.checkNull(request.getParamValue("now"));//制单日期
			String rcvOrgName = CommonUtils.checkNull(request.getParamValue("RCV_ORG"));//接收单位名称
			String rcvCode = CommonUtils.checkNull(request.getParamValue("RCV_CODE"));//接收单位编码
			String rcvOrgId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID"));//接收单位id
			String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER"));//接收人
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));//接收地址
			String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID"));//接收地址id
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));//接收人电话
			String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE"));//邮政编码
			String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
			String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE"));//付款方式
			String amount = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT"));//总金额
			String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));//仓库名称
			String station = CommonUtils.checkNull(request.getParamValue("STATION"));//到站名称
			String remark1 = CommonUtils.checkNull(request.getParamValue("remark1"));//备注
			String createName = logonUser.getName();//制单人
			long maxSize = 1024 * 1024 * 5;
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			List<Map<String,String>> maxLineErro= new ArrayList<Map<String,String>>();
			int errNum = insertIntoTmp(request, "uploadFile", 2, 3, maxSize);
			String err = "";

			if (errNum != 0) {
				switch (errNum) {
				case 1:
					err += "文件列数不是两列,请修改后再上传!";
					break;
				case 2:
					err += "空行不能大于三行,请修改后再上传!";
					break;
				case 3:
					err += "文件内容不能为空,请修改后再上传!";
					break;
				case 4:
					err += "文件类型错误,请重新上传!";
					break;
				case 5:
					err += "文件不能大于" + maxSize + ",请修改后再上传";
					break;
				default:
					break;
				}
			}

			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setForword(PART_TRANSFER_ERROR_URL);
			}else{
				List<Map> list=getMapList();
				List<Map<String,Object>> voList = new ArrayList<Map<String,Object>>();
				loadVoList(voList,list, errorInfo, maxLineErro,whId,logonUser);
				if(maxLineErro.size() > 0){
					err = maxLineErro.get(0).get("1").toString();
					act.setOutData("error", err);
					act.setForword(PART_TRANSFER_ERROR_URL);
				} else if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setForword(PART_TRANSFER_ERROR_URL);
				}else{
					act.setOutData("list", voList);
					act.setOutData("whId", whId);
					act.setOutData("orderCode", orderCode);
					act.setOutData("sellerId", sellerId);
					act.setOutData("sellerCode", sellerCode);
					act.setOutData("sellerName", sellerName);
					act.setOutData("now", now);
					act.setOutData("rcvOrgName", rcvOrgName);
					act.setOutData("rcvCode", rcvCode);
					act.setOutData("rcvOrgId", rcvOrgId);
					act.setOutData("receiver", receiver);
					act.setOutData("addr", addr);
					act.setOutData("addrId", addrId);
					act.setOutData("tel", tel);
					act.setOutData("postCode", postCode);
					act.setOutData("transType", transType);
					act.setOutData("payType", payType);
					act.setOutData("amount", amount);
					act.setOutData("whName", whName);
					act.setOutData("station", station);
					act.setOutData("remark1", remark1);
					act.setOutData("createName", createName);
					act.setOutData("uFlag", 1);
					act.setForword(PART_TRANSFER_ADD_URL);
				}
				
			}

		} catch (Exception e) {
			BizException e1 = null;
			if (e instanceof BizException) {
				e1 = (BizException) e;
			} else {
				e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
						"文件读取错误");
			}
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setForword(PART_TRANSFER_ADD_URL);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 读取CELL 
	 * @param      : @param voList
	 * @param      : @param list
	 * @param      : @param errorInfo
	 * @param      : @param maxLineErro
	 * @param      : @param parentOrgId
	 * @param      : @param whId      
	 * @return     :    
	 * @throws Exception 
	 * @throws     :
	 * LastDate    : 2013-5-9
	 */
	private void loadVoList(List<Map<String,Object>> voList,List<Map> list,List<Map<String,String>> errorInfo, List<Map<String,String>> maxLineErro,String whId,AclUserBean logonUser) throws Exception{
		List<Map<String, Object>> partList = dao.queryPartInfoByWhId(whId,logonUser);//查询满足当前条件的配件信息
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				Map<String, Object> tempmap = new HashMap<String, Object>();
				int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
				if (Integer.parseInt(key) > maxLineNum) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1","导入数据行数不能超过 " + maxLineNum + "行!");
					maxLineErro.add(errormap);
				} else if ("".equals(cells[0].getContents().trim())) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "配件编码");
					errormap.put("3", "空");
					errorInfo.add(errormap);
				} else {
					String partOldCode = cells[0].getContents().trim();//配件编码
					boolean flag = false;
					for(Map<String,Object> pmap:partList){
						if(pmap.containsValue(partOldCode)){//如果导入编码满足条件
							flag = true;
							String allotQtyStr ="";
							if(cells.length==2){//如果导入的列数是两列
								allotQtyStr =cells[1].getContents().trim();
							}
							if(allotQtyStr.matches("^[1-9]+\\d*$")){//如果上传的调拨数量是正整数
								Long allotQty = Long.parseLong(allotQtyStr);
								double allotPrice =((BigDecimal)pmap.get("SALE_PRICE1")).doubleValue();
								tempmap.put("orderAmount", Arith.mul(allotQty, allotPrice));
							}
							tempmap.put("partOldcode", partOldCode);
							tempmap.put("allotQty", allotQtyStr);
							tempmap.put("partCode", pmap.get("PART_CODE"));
							tempmap.put("partCname", pmap.get("PART_CNAME"));
							tempmap.put("unit", pmap.get("UNIT"));
							tempmap.put("partId", pmap.get("PART_ID"));
							tempmap.put("normalQty", pmap.get("NORMAL_QTY"));
							tempmap.put("isDirect", pmap.get("IS_DIRECT"));
							tempmap.put("isPlan", pmap.get("IS_PLAN"));
							tempmap.put("isLack", pmap.get("IS_LACK"));
							tempmap.put("isReplaced", pmap.get("IS_REPLACED"));
							tempmap.put("miniPack1", pmap.get("MIN_PACK1"));
							tempmap.put("salePrice1", pmap.get("SALE_PRICE1"));
						}
					}
					if(!flag){//如果导入编码都不满足条件
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件编码");
						errormap.put("3", "没有在当前选择仓库中,请修改后再上传!");
						errorInfo.add(errormap);
					}
				voList.add(tempmap);
			}
		}
	}
	
	}
/**
 * 
 * @Title      : 
 * @Description: 导出 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-9
 */
public void expPartTransferExcel(){
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	try {
		RequestWrapper request = act.getRequest();
		boolean flag = true;//是否是车厂,如果是车厂只能查看调拨单
		if(logonUser.getDealerId()!=null){
			flag = false;
		}
		act.setOutData("flag", flag);
		String[] head = new String[11];
		head[0] = "调拨号";
		head[1] = "调入单位编码";
		head[2] = "订货单位";
		head[3] = "调出单位编码";
		head[4] = "调出单位";
		head[5] = "制单人";
		head[6] = "制单日期";
		head[7] = "调拨总金额";
		head[8] = "提交时间";
		head[9] = "订单状态";
		List<Map<String, Object>> list = dao.queryPartTransfer(request,logonUser);
		List list1 = new ArrayList();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				if (map != null && map.size() != 0) {
					String[] detail = new String[11];
					detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
					detail[1] = CommonUtils.checkNull(map
							.get("DEALER_CODE"));
					detail[2] = CommonUtils
							.checkNull(map.get("DEALER_NAME"));
					detail[3] = CommonUtils.checkNull(map
							.get("SELLER_CODE"));
					detail[4] = CommonUtils.checkNull(map
							.get("SELLER_NAME"));
					detail[5] = CommonUtils.checkNull(map.get("NAME"));
					detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
					detail[7] = CommonUtils
							.checkNull(map.get("ORDER_AMOUNT"));
					detail[8] = CommonUtils
					.checkNull(map.get("SUBMIT_DATE"));
					detail[9] = CommonUtils
					.checkNull(map.get("STATE"));
					list1.add(detail);
				}
			}
			this.exportEx(ActionContext.getContext().getResponse(),
					request, head, list1);
		} else {
			BizException e1 = new BizException(act,
					ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
			throw e1;
		}

	} catch (Exception e) {
		BizException e1 = null;
		if (e instanceof BizException) {
			e1 = (BizException) e;
		} else {
			e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
					"文件下载错误");
		}
		logger.error(logonUser, e1);
		act.setException(e1);
		act.setForword(PART_TRANSFER_QUERY_URL);
	}
}

public static Object exportEx(ResponseWrapper response,
		RequestWrapper request, String[] head, List<String[]> list)
		throws Exception {

	String name = "配件调拨单信息.xls";
	jxl.write.WritableWorkbook wwb = null;
	OutputStream out = null;
	try {
		response.setContentType("application/octet-stream");
		response.addHeader("Content-disposition", "attachment;filename="
				+ URLEncoder.encode(name, "utf-8"));
		out = response.getOutputStream();
		wwb = Workbook.createWorkbook(out);
		jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

		if (head != null && head.length > 0) {
			for (int i = 0; i < head.length; i++) {
				ws.addCell(new Label(i, 0, head[i]));
			}
		}
		int pageSize = list.size() / 30000;
		for (int z = 1; z < list.size() + 1; z++) {
			String[] str = list.get(z - 1);
			for (int i = 0; i < str.length; i++) {
					/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                 if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                    ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                }else{
                    ws.addCell(new Label(i, z, str[i]));
                }
			}
		}
		wwb.write();
		out.flush();
	} catch (Exception e) {
		e.printStackTrace();
		throw e;
	} finally {
		if (null != wwb) {
			wwb.close();
		}
		if (null != out) {
			out.close();
		}
	}
	return null;
}
/**
 * 
 * @Title      : 
 * @Description: 提交调拨单 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-9
 */
public void submitTransfer(){

	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);

	try {
		String orderId = CommonUtils.checkNull(request
				.getParamValue("orderId")); // 调拨单id
		String curPage = CommonUtils.checkNull(request
				.getParamValue("curPage"));// 当前页

		String errors = "";//错误信息
		String success = "";//成功信息
		boolean flag = true;
		
		TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
		mainPO.setOrderId(CommonUtils.parseLong(orderId));
		mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
		//如果该调拨单已经被提交了,再次提交就要提示错误信息
		if(mainPO.getState().equals(Constant.PART_TRANSFER_ORDER_STATE_01)){
			errors+="该调拨单已经被提交,请选择其他调拨单提交!";
		}else{
			
			TtPartDlrOrderDtlPO dtlPO = new TtPartDlrOrderDtlPO();
			dtlPO.setOrderId(CommonUtils.parseLong(orderId));
			List<TtPartDlrOrderDtlPO> list = dao.select(dtlPO);
			String[] partIds = new String[list.size()];
			for(int i=0;i<list.size();i++){
				TtPartDlrOrderDtlPO dtlPO2 = list.get(i);
				partIds[i] = dtlPO2.getPartId().toString();
			}
			
			//在提交之前还需要验证调拨数量与当前库房可用数量
			List<Map<String,Object>> list1 = dao.queryCurNormalQty(partIds,mainPO.getWhId().toString(),logonUser.getDealerId());
				for(int i=0;i<list1.size();i++){
					Map<String, Object> map = list1.get(i);
					TtPartDlrOrderDtlPO dtlPO2 = list.get(i);
					long curQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();//当前可用数量
					long allotQty = dtlPO2.getBuyQty();//调拨数量
					if(curQty<allotQty){
						errors+="当前调拨单中的配件【"+dtlPO2.getPartCname()+"】的调拨数量大于可调拨数量,请修改后再提交!<br>";
						flag = false;
					}
				}
			
			
			if(flag){
				TtPartDlrOrderMainPO spo = new TtPartDlrOrderMainPO();// 源po
				spo.setOrderId(CommonUtils.parseLong(orderId));

				TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();// 更新po
                //add by yuan 20130701配件调拨时，经业务确认不需要审核，直接更新状态为车厂审核通过
				/*po.setState(Constant.PART_TRANSFER_ORDER_STATE_01);*/
                po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
				po.setSubmitDate(new Date());
				po.setSubmitBy(logonUser.getUserId());

				dao.update(spo, po);
				
				//提交成功之后需要调用占用逻辑
				//调用库存占用逻辑
	           /* List ins = new LinkedList<Object>();
	            ins.add(0, mainPO.getOrderId());
	            ins.add(1, Constant.PART_CODE_RELATION_15);
	            ins.add(2,1);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);*/
				
				success = "提交成功!";
			}
			
		}
		
		if ("".equals(curPage)) {
			curPage = "1";
		}
		act.setOutData("success", success);
		act.setOutData("error", errors);
		act.setOutData("curPage", curPage);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.SPECIAL_MEG, "配件调拨单提交失败,请联系管理员!");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

}
/**
 * 
 * @Title      : 
 * @Description: 作废调拨单 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-9
 */
public void cancelTransfer(){
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	try {
		String orderId = CommonUtils.checkNull(request
				.getParamValue("orderId")); // 调拨单id
		String curPage = CommonUtils.checkNull(request
				.getParamValue("curPage"));// 当前页
		
		TtPartDlrOrderMainPO spo = new TtPartDlrOrderMainPO();// 源po
		spo.setOrderId(CommonUtils.parseLong(orderId));
		
		TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();// 更新po
		po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04);
		po.setDisableDate(new Date());
		po.setDisableBy(logonUser.getUserId());
		
		if ("".equals(curPage)) {
			curPage = "1";
		}
		dao.update(spo, po);
		act.setOutData("success", "作废成功!");
		act.setOutData("curPage", curPage);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件调拨单作废");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
	
}

/**
 * 
 * @Title      : 
 * @Description: 查看调拨单明细初始化 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-9
 */
public void queryPartTransferDetailInit(){
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	try {
		String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
		Map map = dao.getPartDlrOrderMainInfo(orderId);
		List list1 = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
		act.setOutData("transList",list1);
		request.setAttribute("po", map);
		act.setForword(PART_TRANSFER_DETAIL_URL);
	} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "配件调拨明细");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

}
/**
 * 
 * @Title      : 
 * @Description: 查询调拨单明细 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-9
 */
public void queryPartTransferDetail(){
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
  try {
		String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//调拨单id
	    
		//分页方法 begin
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
		PageResult<Map<String, Object>> ps = dao.queryPartTransferDetailList(orderId,curPage,Constant.PAGE_SIZE);
		//分页方法 end
		act.setOutData("ps", ps);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"调拨单明细");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
  }
/**
 * 
 * @Title      : 
 * @Description: 调拨信息修改初始化 
 * @param      :       
 * @return     :    
 * @throws     :
 * LastDate    : 2013-5-10
 */
	public void updatePartTransferInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			Map map = dao.getPartDlrOrderMainInfo(orderId);
			List<Map<String,Object>> list = dao.queryPartDlrOrderDtl(orderId);
			List list1 = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
			act.setOutData("transList",list1);
			request.setAttribute("po", map);
			request.setAttribute("list", list);
			act.setForword(PART_TRANSFER_UPDATE_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件调拨明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除调拨单明细 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-11
	 */
	public void deletePartDlrOrderDtl(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String lineId = CommonUtils.checkNull(request.getParamValue("lineId"));//明细id
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id
			TtPartDlrOrderDtlPO spo = new TtPartDlrOrderDtlPO();
			TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
			spo.setLineId(CommonUtils.parseLong(lineId));
			po.setStatus(0);
			po.setDeleteDate(new Date());
			po.setDeleteBy(logonUser.getUserId());
			//删除明细之后要立即更新主表中的订单金额,用户有可能删除明细之后直接点返回按钮
			dao.update(spo, po);
			dao.updateOrderAmount(orderId);
			act.setOutData("success", "删除成功!");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "配件调拨明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改配件调拨信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-11
	 */
	public void updateTransfer(){

		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id
			String rcvOrgName = CommonUtils.checkNull(request.getParamValue("RCV_ORG"));//接收单位名称
			String rcvCode = CommonUtils.checkNull(request.getParamValue("RCV_CODE"));//接收单位编码
			String rcvOrgId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID"));//接收单位id
			String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER"));//接收人
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));//接收地址
			String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID"));//接收地址id
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));//接收人电话
			String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE"));//邮政编码
			String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
			String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE"));//付款方式
			String amount = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT"));//总金额
			String station = CommonUtils.checkNull(request.getParamValue("STATION"));//到站名称
			String remark1 = CommonUtils.checkNull(request.getParamValue("remark1"));//备注
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
			
			TtPartDlrOrderMainPO smainPO = new TtPartDlrOrderMainPO();
			smainPO.setOrderId(CommonUtils.parseLong(orderId));
			
			TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
			mainPO.setPayType(CommonUtils.parseInteger(payType));
			mainPO.setDealerId(CommonUtils.parseLong(rcvOrgId));
			mainPO.setDealerCode(rcvCode);
			mainPO.setDealerName(rcvOrgName);
			mainPO.setBuyerName(receiver);
			mainPO.setRcvOrgid(CommonUtils.parseLong(rcvOrgId));
			mainPO.setRcvOrg(rcvOrgName);
			mainPO.setAddrId(CommonUtils.parseLong(addrId));
			mainPO.setAddr(addr);
			mainPO.setReceiver(receiver);
			mainPO.setTel(tel);
			mainPO.setPostCode(postCode);
			mainPO.setStation(station);
			mainPO.setTransType(transType);
			mainPO.setOrderAmount(CommonUtils.parseDouble(amount));
			mainPO.setRemark(remark1);
            mainPO.setUpdateDate(new Date());
            mainPO.setUpdateBy(logonUser.getUserId());
            
            String[] partIds = request.getParamValues("cb");//配件id
			//在修改之前还需要验证调拨数量与当前库房可用数量
			List<Map<String,Object>> list = dao.queryCurNormalQty(partIds,whId,logonUser.getDealerId());
			if(partIds!=null&&partIds.length>0){
				for(int i=0;i<partIds.length;i++){
					Map<String, Object> map = list.get(i);
					long curQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();//当前可用数量
					String allotQtyStr = CommonUtils.checkNull(request.getParamValue("allotQty_"+partIds[i]));//调拨数量
					long allotQty = CommonUtils.parseLong(allotQtyStr);
					String partCname = CommonUtils.checkNull(request.getParamValue("partCname_"+partIds[i]));//配件名称
					if(curQty<allotQty){
						act.setOutData("error", "当前"+partCname+"的调拨数量大于可调拨数量,请重新输入!");
						act.setOutData("curQty", curQty);
						act.setOutData("partId", partIds[i]);
						return;
					}
					String lineId = CommonUtils.checkNull(request.getParamValue("lineId_"+partIds[i]));//明细id
					String idx = CommonUtils.checkNull(request.getParamValue("idx_"+partIds[i]));//行号
					String allotPrice = CommonUtils.checkNull(request.getParamValue("allotPrice_"+partIds[i]));//调拨单价
					String orderAmount = CommonUtils.checkNull(request.getParamValue("orderAmount_"+partIds[i]));//调拨金额
					String remark = CommonUtils.checkNull(request.getParamValue("remark_"+partIds[i]));//备注
					if("".equals(lineId)){//如果明细id为空,那么就是在修改的时候新添加的
						String partCode = CommonUtils.checkNull(request.getParamValue("partCode_"+partIds[i]));//配件件号
						String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode_"+partIds[i]));//配件编码
						String unit = CommonUtils.checkNull(request.getParamValue("unit_"+partIds[i]));//单位
						String isDirect = CommonUtils.checkNull(request.getParamValue("IS_DIRECT"+partIds[i]));//是否直发
						String isPaln = CommonUtils.checkNull(request.getParamValue("IS_PLAN"+partIds[i]));//大件、占空间（如保险杠）
						String isLack = CommonUtils.checkNull(request.getParamValue("IS_LACK"+partIds[i]));//紧缺
						String isReplaced = CommonUtils.checkNull(request.getParamValue("IS_REPLACED"+partIds[i]));//是否有替代件
						String miniPack = CommonUtils.checkNull(request.getParamValue("miniPack_"+partIds[i]));//最小包装量
						TtPartDlrOrderDtlPO dtlPO = new TtPartDlrOrderDtlPO();
						dtlPO.setLineId(CommonUtils.parseLong(SequenceManager.getSequence("")));
						dtlPO.setOrderId(CommonUtils.parseLong(orderId));
						dtlPO.setLineNo(CommonUtils.parseLong(idx));
						dtlPO.setPartId(CommonUtils.parseLong(partIds[i]));
						dtlPO.setPartCode(partCode);
						dtlPO.setPartOldcode(partOldcode);
						dtlPO.setPartCname(partCname);
						dtlPO.setUnit(unit);
						if(!"".equals(isDirect)&&!"null".equals(isDirect)&&!"".equals(isDirect)){
							dtlPO.setIsDirect(CommonUtils.parseInteger(isDirect));
						}
						if(isPaln!=null&&!"null".equals(isPaln)&&!"".equals(isPaln)){
							dtlPO.setIsPlan(CommonUtils.parseInteger(isPaln));
						}
						if(isLack!=null&&!"null".equals(isLack)&&!"".equals(isLack)){
							dtlPO.setIsLack(CommonUtils.parseInteger(isLack));
						}
						if(isReplaced!=null&&!"null".equals(isReplaced)&&!"".equals(isReplaced)){
							dtlPO.setIsReplaced(CommonUtils.parseInteger(isReplaced));
						}
						if(miniPack!=null&&!"null".equals(miniPack)&&!"".equals(miniPack)){
							dtlPO.setMinPackage(CommonUtils.parseLong(miniPack));
						}
						dtlPO.setStockQty(curQty);
						dtlPO.setBuyQty(allotQty);
						dtlPO.setBuyPrice(CommonUtils.parseDouble(allotPrice));
						dtlPO.setBuyAmount(Arith.mul(dtlPO.getBuyQty(), dtlPO.getBuyPrice()));
						dtlPO.setRemark(remark);
						dtlPO.setCreateDate(new Date());
						dtlPO.setCreateBy(logonUser.getUserId());
						
						dao.insert(dtlPO);
					}else{//如果不为空，那就只进行修改
						TtPartDlrOrderDtlPO spo = new TtPartDlrOrderDtlPO();
						spo.setLineId(CommonUtils.parseLong(lineId));
						TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
						po.setLineNo(CommonUtils.parseLong(idx));
						po.setStockQty(curQty);
						po.setBuyQty(allotQty);
						po.setBuyAmount(Arith.mul(allotQty, CommonUtils.parseDouble(allotPrice)));
						po.setRemark(remark);
						po.setUpdateDate(new Date());
						po.setUpdateBy(logonUser.getUserId());
						dao.update(spo, po);
					}
				}
			}
            dao.update(smainPO, mainPO);
			act.setOutData("success", "修改成功!");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件调拨信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取接收单位或者接收地址 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-7
	 */
	public void selSales() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
		    String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
		    String type = CommonUtils.checkNull(request.getParamValue("type"));
		    String rcvOrgId = CommonUtils.checkNull(request.getParamValue("rcvOrgId"));
		    String parentorgName = CommonUtils.checkNull(request.getParamValue("PARENTORG_NAME"));
		    String parentorgCode = CommonUtils.checkNull(request.getParamValue("PARENTORG_CODE"));
		    int page_size = Constant.PAGE_SIZE;
		    
		    TmDealerPO po = new TmDealerPO();
		    po.setDealerId(CommonUtils.parseLong(dealerId));
		    po = (TmDealerPO) dao.select(po).get(0);
		    
		    //分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.getSalesRelation(dealerId,po,type,rcvOrgId, parentorgName,parentorgCode,page_size, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	
}