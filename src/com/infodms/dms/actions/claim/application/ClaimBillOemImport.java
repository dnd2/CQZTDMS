package com.infodms.dms.actions.claim.application;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.infodms.dms.bean.MessErr;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.FileObject;

public class ClaimBillOemImport extends BaseAction{
	
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	
	/**
	 * 跳转导入页面
	 */
	public void claimBillImportPre() {
		sendMsgByUrl(sendUrl(this.getClass(), "claimBillImport"), "索赔单状态跟踪");
	}
	/**
	 * 验证索赔单
	 */
	@SuppressWarnings("unchecked")
	public void excelOperateRerRule(){
		try {
			List<MessErr> listM = new ArrayList<MessErr>();
			List<Map<String,String>> listResult = new ArrayList<Map<String,String>>();
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2  = new SimpleDateFormat("yyyy-MM-dd");
			FileObject uploadFile = request.getParamObject("uploadFile");
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet[] sheets = wb.getSheets();
			Sheet sheet = null;
			for (int i = 0; i < sheets.length; i++) {
				sheet = sheets[i];
				int totalRows = sheet.getRows();
				for (int j = 1; j < totalRows; j++) {
					boolean checkFlag = true;
					Map<String,String> map = new HashMap<String, String>();
					Long appId = null;    //没有作新增，存在作修改
					Cell[] cells = sheet.getRow(j);
					String claimNo = cells[0].getContents();
					String dealerCode = cells[1].getContents();
					String dealerName = cells[2].getContents();
					String wwDealerCode = cells[3].getContents();
					String wwDealerName = cells[4].getContents();
					String roNo = cells[5].getContents();
					String vin = cells[6].getContents();
					String roStartDate = cells[7].getContents();
					String roEndDate = cells[8].getContents();
					String claimReportDate = cells[9].getContents();
					String claimType = cells[10].getContents();
					String verseon = cells[11].getContents();
					String troubleDesc = cells[12].getContents();
					String labourHours = cells[13].getContents();
					String labourAmount = cells[14].getContents();
					String applyAmount = cells[15].getContents();//配件
					String amount = cells[16].getContents();//外出
					String repairTotal = cells[17].getContents();
					String inMileage = cells[18].getContents();
					String status = cells[19].getContents();
					String repairMethod = cells[20].getContents();
					String troubleReason = cells[21].getContents();
					String engineNo = cells[22].getContents();
					String modelCode = cells[23].getContents();
					String quelityGrate = cells[24].getContents();
					String partCode = cells[25].getContents();
					String partName = cells[26].getContents();
					String wrLabourCode = cells[27].getContents();
					String wrLabourName = cells[28].getContents();
					String producerName = cells[29].getContents();
					String producerCode = cells[30].getContents();
					String partNum = cells[31].getContents();
					String dealMethod = cells[32].getContents();
					String guaranteeDate = cells[33].getContents();
					String dealerId = null;
					//验证索赔单号
					if(!Utility.testString(claimNo))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第1列");
						err.setName("索赔单号");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					} else {
						TtAsWrApplicationPO app = new TtAsWrApplicationPO();
						app.setClaimNo(claimNo);
						List<TtAsWrApplicationPO> listApp = dao.select(app);
						if(listApp!=null&&listApp.size()>0){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第1列");
							err.setName(claimNo);
							err.setMess("已存在");
							listM.add(err);
							checkFlag = false;
							appId = listApp.get(0).getId();
						}
					}
					//验证经销商代码
					if(!Utility.testString(dealerCode))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第2列");
						err.setName("经销商代码");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						TmDealerPO dealer = new TmDealerPO();
						dealer.setDealerCode(dealerCode.trim());
						List<TmDealerPO> listBean = dao.select(dealer);
						if(listBean==null||listBean.size()<=0){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第2列");
							err.setName(dealerCode);
							err.setMess("没有找到该经销商");
							listM.add(err);
							checkFlag = false;
						}else{
							dealerId = listBean.get(0).getDealerId().toString();
						}
					}
					//难威望经销商代码
					if(!Utility.testString(wwDealerCode))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第4列");
						err.setName("威望经销商代码");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}
					if(!Utility.testString(wwDealerName))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第5列");
						err.setName("威望经销商名称");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}
					//验证工单,为空默认为索赔单号
					if(!Utility.testString(roNo))
					{
						roNo = claimNo;
					}
					//验证索VIN
					if(!Utility.testString(vin))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第7列");
						err.setName("vin");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else if(vin.length()!=17){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第7列");
						err.setName("vin");
						err.setMess("长度或者格式错误");
						listM.add(err);
						checkFlag = false;
					}
					//验证工单开始时间
					if(!Utility.testString(roStartDate))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第8列");
						err.setName("工单开单开始日期");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else if(!ValidateUtil.isDayOfMonth(roStartDate)){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第8列");
						err.setName("工单开始日期");
						err.setMess("格式或者时间值错误");
						listM.add(err);
						checkFlag = false;
					}else{
						roStartDate=sdf2.format(sdf.parseObject(roStartDate));
					}
					//验证工单结束时间
					if(!Utility.testString(roEndDate))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第9列");
						err.setName("工单结束日期");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else if(!ValidateUtil.isDayOfMonth(roEndDate)){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第9列");
						err.setName("工单结束日期");
						err.setMess("格式或者时间值错误");
						listM.add(err);
						checkFlag = false;
					}else{
						roEndDate=sdf2.format(sdf.parseObject(roEndDate));
					}
					//验证索赔单上报时间
					if(!Utility.testString(claimReportDate))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第10列");
						err.setName("索赔单上报日期");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else if(!ValidateUtil.isDayOfMonth(claimReportDate)){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第10列");
						err.setName("索赔单上报日期");
						err.setMess("格式或者时间值错误");
						listM.add(err);
						checkFlag = false;
					}
					else{
						claimReportDate=sdf2.format(sdf.parseObject(claimReportDate));
					}
					//====================================lj2015-5-11
					//验证索赔单购车日期
					if(!Utility.testString(guaranteeDate))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第34列");
						err.setName("索赔单购车日期");
						err.setMess("不能为空");
						listM.add(err);
						checkFlag = false;
					}else if(!ValidateUtil.isDayOfMonth(guaranteeDate)){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第34列");
						err.setName("索赔单购车日期");
						err.setMess("格式或者时间值错误");
						listM.add(err);
						checkFlag = false;
					}
					else{
						guaranteeDate=sdf2.format(sdf.parseObject(guaranteeDate));
					}
					//======================================
					//验证索赔单类型
					if(!Utility.testString(claimType))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第11列");
						err.setName("索赔单类型");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}
					//验证工时数量
					if(!Utility.testString(labourHours))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第14列");
						err.setName("工时数量");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						if(!ValidateUtil.isCount(labourHours)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第14列");
							err.setName("工时数量");
							err.setMess("格式错误(最多2位小数)");
							listM.add(err);
							checkFlag = false;
						}
						//判断是否为金额类型
					}
					//验证工时金额
					if(!Utility.testString(labourAmount))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第15列");
						err.setName("工时金额");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						//判断是否为金额类型
						if(!ValidateUtil.isCount(labourAmount)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第15列");
							err.setName("工时金额");
							err.setMess("格式错误(最多2位小数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					//验证配件费
					if(!Utility.testString(applyAmount))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第16列");
						err.setName("配件费");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						//判断是否为金额类型
						if(!ValidateUtil.isCount(applyAmount)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第16列");
							err.setName("配件费");
							err.setMess("格式错误(最多2位小数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					//外出费用
					if(!Utility.testString(amount))
					{
						amount="0";
					}else{
						//判断是否为金额类型
						if(!ValidateUtil.isCount(amount)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第17列");
							err.setName("外出费用");
							err.setMess("格式错误(最多2位小数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					//验证总费用
					if(!Utility.testString(repairTotal))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第18列");
						err.setName("总费用");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						//判断是否为金额类型
						if(!ValidateUtil.isCount(repairTotal)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第18列");
							err.setName("总费用");
							err.setMess("格式错误(最多2位小数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					//验证进厂里程
					if(!Utility.testString(inMileage))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第19列");
						err.setName("进厂里程");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						//判断是否正整数
						if(!ValidateUtil.isNum(inMileage)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第19列");
							err.setName("进厂里程");
							err.setMess("格式错误(正整数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					//验证索赔单状态
						status = Constant.CLAIM_APPLY_ORD_TYPE_13.toString();
					//验证质量等级
						quelityGrate = Constant.QUELITY_GRATE_01.toString();
					//验证配件代码
						TtPartDefinePO d = new TtPartDefinePO();
						d.setPartOldcode(partCode);
						List<TtPartDefinePO> list = dao.select(d);
					if(!Utility.testString(partCode))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第26列");
						err.setName("配件代码");
						err.setMess("不能为空");
						listM.add(err);
						checkFlag = false;
					}else {//配件代码不为空 
						Map map1 =  dao.queryRangeSingle(loginUser,request,partCode);
	                    if (null!=map1 && !"".equals(map1)) {//根据配件代码查询近90天出现次数最多的供应商，如果有则选择，否则选择导入的供应商
	                    	producerCode =  map1.get("SUPPLY_CODE").toString();//设置供应商代码
	                    	producerName =  map1.get("SUPPLY_NAME").toString();//设置供应商名称
						}
					}
					//验证配件名称
					if(!Utility.testString(partName))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第27列");
						err.setName("配件名称");
						err.setMess("不能为空");
						listM.add(err);
						checkFlag = false;
					}
					//验证工时代码
					if(!Utility.testString(wrLabourCode))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第28列");
						err.setName("工时代码");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}
					//验证操作名称
					if(!Utility.testString(wrLabourName))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第29列");
						err.setName("操作名称");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}
					//验证供应商名称
					if(!Utility.testString(producerName))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第30列");
						err.setName("供应商名称");
						err.setMess("不能为空");
						listM.add(err);
						checkFlag = false;
					}
					//验证供应商代码
					TtPartMakerDefinePO dd = new TtPartMakerDefinePO();
					dd.setMakerCode(producerCode);
					List<TtPartMakerDefinePO> mlist = dao.select(dd);
					if(!Utility.testString(producerCode))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第13列");
						err.setName("供应商代码");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else if(mlist==null|| mlist.size()<1){
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第13列");
						err.setName("供应商代码");
						err.setMess("不存在");
						listM.add(err);
						checkFlag = false;
					}
					//验证配件数量
					if(!Utility.testString(partNum))
					{
						MessErr err = new MessErr();
						err.setHead("第"+(j+1)+"行"+"第31列");
						err.setName("配件数量");
						err.setMess("不能不空");
						listM.add(err);
						checkFlag = false;
					}else{
						//判断是否正整数
						if(!ValidateUtil.isNum(partNum)){
							MessErr err = new MessErr();
							err.setHead("第"+(j+1)+"行"+"第31列");
							err.setName("配件数量");
							err.setMess("格式错误(非负整数)");
							listM.add(err);
							checkFlag = false;
						}
					}
					if(checkFlag){
						map.put("claimNo", claimNo);
						map.put("dealerId", dealerId);
						map.put("dealerCode", dealerCode);
						map.put("dealerName", dealerName);
						map.put("wwDealerCode", wwDealerCode);
						map.put("wwDealerName", wwDealerName);
						map.put("roNo", roNo);
						map.put("vin", vin);
						map.put("roStartDate", roStartDate);
						map.put("roEndDate", roEndDate);
						map.put("claimReportDate", claimReportDate);
						map.put("claimType", claimType);
						map.put("verseon", verseon);
						map.put("troubleDesc", troubleDesc);
						map.put("labourHours", labourHours);
						map.put("labourAmount", labourAmount);
						map.put("applyAmount", applyAmount);
						map.put("amount", amount);
						map.put("repairTotal", repairTotal);
						map.put("inMileage", inMileage);
						map.put("status", status);
						map.put("repairMethod", repairMethod);
						map.put("troubleReason", troubleReason);
						map.put("engineNo", engineNo);
						map.put("modelCode", modelCode);
						map.put("quelityGrate", quelityGrate);
						map.put("partCode", partCode);
						map.put("partName", partName);
						map.put("wrLabourCode", wrLabourCode);
						map.put("wrLabourName", wrLabourName);
						map.put("producerName", producerName);
						map.put("producerCode", producerCode);
						map.put("partNum", partNum);
						map.put("dealMethod", dealMethod);
						map.put("appId", appId==null?null:appId.toString().trim());
						map.put("guaranteeDate", guaranteeDate);//lj2015-5-11
						listResult.add(map);
					}
					}
				act.getSession().set("listOk", listResult);
				request.setAttribute("listErr", listM);
				request.setAttribute("successNum", listResult.size());
				act.setOutData("faileNum", totalRows-1-listResult.size());
				sendMsgByUrl(sendUrl(this.getClass(), "claimBillImportErr"), "索赔单状态跟踪");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 索赔单导入
	 */
    @SuppressWarnings("unchecked")
	public void claimBillImport(){
	  
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String,String>> listMap = (List<Map<String, String>>) act.getSession().get("listOk");
			Long userId = loginUser.getUserId();
			for(Map<String,String> map:listMap){
				String claimNo = map.get("claimNo");
				String dealerId = map.get("dealerId");
				String dealerCode = map.get("dealerCode");
				String dealerName = map.get("dealerName");
				String wwDealerCode = map.get("wwDealerCode");
				String wwDealerName = map.get("wwDealerName");
				String roNo = map.get("roNo");
				String vin = map.get("vin");
				String roStartDate = map.get("roStartDate");
				String roEndDate = map.get("roEndDate");
				String claimReportDate = map.get("claimReportDate");
				String claimType = map.get("claimType");
				TcCodePO code = new TcCodePO();
				code.setCodeDesc(claimType.trim());
				code.setType("1066");
				List<TcCodePO> listCode =  dao.select(code);
				if(listCode.size() > 0)
					claimType = listCode.get(0).getCodeId();
				String verseon = map.get("verseon");
				String troubleDesc = map.get("troubleDesc");
				String labourHours = map.get("labourHours");
				String labourAmount = map.get("labourAmount");
				String applyAmount = map.get("applyAmount");
				String amount = map.get("amount");
				String repairTotal = map.get("repairTotal");
				String inMileage = map.get("inMileage");
				String status = map.get("status");
				String existsAppId = map.get("appId");
				String repairMethod = map.get("repairMethod");
				String troubleReason = map.get("troubleReason");
				String engineNo = map.get("engineNo");
				String modelCode = map.get("modelCode");
				String quelityGrate = map.get("quelityGrate");
				
				String partCode = map.get("partCode");
				String partName = map.get("partName");
				String wrLabourcode = map.get("wrLabourCode");
				String wrLabourname = map.get("wrLabourName");
				String producerName = map.get("producerName");
				String producerCode = map.get("producerCode");
				String partNum = map.get("partNum");
				String dealMehtod = map.get("dealMehtod");
				String guaranteeDate = map.get("guaranteeDate");//lj2015-5-11
				TtAsWrApplicationPO app = new TtAsWrApplicationPO();
				TtAsWrLabouritemPO item = new TtAsWrLabouritemPO();
				TtAsWrPartsitemPO part = new TtAsWrPartsitemPO();
				TtAsWrNetitemPO netitem = new TtAsWrNetitemPO();
				
				TtAsWrApplicationPO appTar = null;
				TtAsWrLabouritemPO itemTar = null;
				TtAsWrPartsitemPO partTar = null;
				TtAsWrNetitemPO netitemTar = null;
				Long appId = null;
				if(existsAppId==null){  //作新增
					appId = Utility.getLong(SequenceManager.getSequence(""));
					app.setId(appId);
					app.setCreateBy(userId);
					app.setCreateDate(new Date());
					item.setId(appId);
					item.setCreateBy(userId);
					item.setCreateDate(new Date());
					part.setId(appId);
					part.setCreateBy(userId);
					part.setCreateDate(new Date());
					netitem.setId(appId);
					netitem.setCreateBy(userId);
					netitem.setCreateDate(new Date());
				}else{                       
					appTar = new TtAsWrApplicationPO();
					itemTar = new TtAsWrLabouritemPO();
					partTar = new TtAsWrPartsitemPO();
					netitemTar = new TtAsWrNetitemPO();
					
					appTar.setId(Long.valueOf(existsAppId.trim()));
					itemTar.setId(Long.valueOf(existsAppId.trim()));
					partTar.setId(Long.valueOf(existsAppId.trim()));
					netitemTar.setId(Long.valueOf(existsAppId.trim()));
					
					app.setUpdateBy(userId);
					app.setUpdateDate(new Date());
					
					item.setUpdateBy(userId);
					item.setUpdateDate(new Date());
					
					part.setUpdateBy(userId);
					part.setUpdateDate(new Date());
					
					netitem.setUpdateBy(userId);
					netitem.setUpdateDate(new Date());
				}
				app.setClaimNo(claimNo.trim());
				app.setDealerId(Long.valueOf(dealerId));
				app.setDealerName(dealerName);
				
				
				TmDealerPO tmDealerPO = new TmDealerPO();
				tmDealerPO.setDealerId(Long.valueOf(dealerId));
				List<TmDealerPO> selectLists = dao.select(tmDealerPO);
				if(selectLists != null && selectLists.size() == 1) {
					app.setDealerShortname(selectLists.get(0).getDealerShortname());
				}
				app.setWwDealerCode(wwDealerCode.trim());
				app.setWwDealerName(wwDealerName.trim());
				app.setRoNo(roNo.trim());
				app.setVin(vin.trim());
				String roStartDateStr = roStartDate;
				String roEndDateStr = roEndDate;
				String claimReportDateStr = claimReportDate;
				app.setRoStartdate(sdf.parse(roStartDateStr));
				app.setRoEnddate(sdf.parse(roEndDateStr));
				app.setReportDate(sdf.parse(claimReportDateStr));
				app.setSubDate(sdf.parse(claimReportDateStr));
				app.setClaimType(Integer.valueOf(claimType));
				app.setVerseon(Integer.valueOf(verseon));
				app.setTroubleDesc(troubleDesc);
				app.setReporter(dealerCode);
				app.setSubmitTimes(Integer.valueOf(verseon));
				app.setPartsCount(Integer.parseInt(partNum));
				app.setPartAmount(Double.valueOf(applyAmount));
				app.setApplyPartAmount(Double.valueOf(applyAmount));
				app.setBalancePartAmount(Double.valueOf(applyAmount));
				app.setLabourHours(Float.valueOf(labourHours));
				app.setLabourAmount(Double.valueOf(labourAmount));
				app.setApplyLabourAmount(Double.valueOf(labourAmount));
				app.setBalanceLabourAmount(Double.valueOf(labourAmount));
				app.setNetitemAmount(Double.valueOf(amount));
				app.setApplyNetitemAmount(Double.valueOf(amount));
				app.setBalanceNetitemAmount(Double.valueOf(amount));
				app.setRepairTotal(Double.valueOf(repairTotal));
				app.setGrossCredit(Double.valueOf(repairTotal));
				app.setApplyRepairTotal(Double.valueOf(repairTotal));
				app.setBalanceAmount(Double.valueOf(repairTotal));
				app.setLastStatus(Integer.parseInt(status));
				app.setIsInvoice(0);
				//app.setInvoiceDate(new Date());
				app.setIsLetter(0);
				app.setFkNo(claimNo.trim());
				app.setFiDate(new Date());
				app.setIsOldAudit(0);
				app.setYieldly(Constant.areaIdJZD);
				app.setInMileage(Double.valueOf(inMileage));
				app.setStatus(Integer.valueOf(status));
				app.setRepairMethod(repairMethod.trim());
				app.setTroubleReason(troubleReason.trim());
				app.setEngineNo(engineNo.trim());
				//String modelCodeStr = modelCode.substring(0,7);
				//String modelNameStr = modelCode.substring(7, modelCode.length());
				app.setModelCode(modelCode.trim());
				app.setModelName(modelCode.trim());
				app.setQuelityGrate(Integer.valueOf(quelityGrate));
				app.setIsAudit(Constant.IF_TYPE_YES);
				app.setBalanceYieldly(Constant.PART_IS_CHANGHE_01);
				app.setIsImport(Constant.IF_TYPE_YES);
				String guaranteeDateStr = guaranteeDate;
				app.setGuaranteeDate(sdf.parse(guaranteeDateStr));//设置购车日期lj2015-5-11

				item.setLabourId(Long.valueOf(SequenceManager.getSequence("")));
				item.setLabourHours(Float.valueOf(labourHours));
				item.setApplyQuantity(Double.valueOf(labourHours));
				item.setBalanceQuantity(Float.valueOf(labourHours));
				item.setLabourQuantity(Float.valueOf(labourHours));
				item.setLabourAmount(Double.valueOf(labourAmount));
				item.setApplyAmount(Double.valueOf(labourAmount));
				item.setBalanceAmount(Double.valueOf(labourAmount));
				
				item.setWrLabourcode(wrLabourcode.trim());
				item.setLabourCode(wrLabourcode.trim());
				item.setWrLabourname(wrLabourname.trim());
				item.setLabourName(wrLabourname.trim());
				item.setPayType(Constant.PAY_TYPE_02);
				item.setIsClaim(Constant.IF_TYPE_YES);
				if("0".equalsIgnoreCase(labourHours)){
					item.setLabourPrice(Float.valueOf(labourAmount));
					item.setApplyPrice(Double.valueOf(labourAmount));
					item.setBalancePrice(Double.valueOf(labourAmount));
				}else{
					item.setLabourPrice(Float.valueOf(labourAmount)/Float.valueOf(labourHours));
					item.setApplyPrice(Double.valueOf(labourAmount)/Double.valueOf(labourHours));
					item.setBalancePrice(Double.valueOf(labourAmount)/Double.valueOf(labourHours));
				}
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT d.maker_code,d.maker_name\n");
				sql.append("FROM tt_part_maker_define d ,tt_part_define pd,tt_part_maker_relation mr\n");
				sql.append("WHERE d.maker_id = mr.maker_id AND pd.part_id = mr.part_id\n");
				sql.append("AND d.maker_code NOT IN ('902307','902306','902309','902311')\n");
				sql.append("AND pd.part_oldcode ='"+partCode.trim().substring(0, partCode.trim().length()-3)+"000' AND Rownum=1"); 
				
				List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
				if(list!=null&& list.size()>0){
					producerCode = list.get(0).get("MAKER_CODE").toString();
					producerName = list.get(0).get("MAKER_NAME").toString();
				}
				part.setPartId(Long.valueOf(SequenceManager.getSequence("")));
				part.setAmount(Double.valueOf(applyAmount));
				part.setApplyAmount(Double.valueOf(applyAmount));
				part.setBalanceAmount(Double.valueOf(applyAmount));
				part.setRemark(troubleReason.trim());
				part.setPartCode(partCode.trim());
				part.setDownPartCode(partCode.trim());
				part.setPartName(partName.trim());
				part.setDownPartName(partName.trim());
				part.setProducerName(producerName.trim());
				part.setDownProductName(producerName.trim());
				part.setProducerCode(producerCode.trim());
				part.setDownProductCode(producerCode.trim());
				part.setPayType(Constant.PAY_TYPE_02);
				part.setIsGua(1);
				part.setAuditBy(loginUser.getUserId());
				part.setAuditDate(new Date());
				part.setAuditStatus(Constant.PART_AUDIT_STATUS_03);
				part.setIsClaim(Long.valueOf(Constant.IF_TYPE_YES));
				part.setResponsibilityType(Constant.RESPONS_NATURE_STATUS_01);
				part.setMainPartCode("-1");
				part.setQuantity(Float.valueOf(partNum));
				part.setApplyQuantity(Double.valueOf(partNum));
				part.setBalanceQuantity(Float.valueOf(partNum));
				if(!"0".equalsIgnoreCase(partNum)){
					part.setApplyPrice(Double.valueOf(applyAmount)/Double.valueOf(partNum));
					part.setBalancePrice(Double.valueOf(applyAmount)/Double.valueOf(partNum));
					part.setPrice(Double.valueOf(applyAmount)/Double.valueOf(partNum));
				}else{
					part.setApplyPrice(Double.valueOf(applyAmount));
					part.setBalancePrice(Double.valueOf(applyAmount));
					part.setPrice(Double.valueOf(applyAmount));
				}
			if(!"0".equalsIgnoreCase(partNum)&& !"".equalsIgnoreCase(dealMehtod)&&!"0".equalsIgnoreCase(dealMehtod)){
				//如果数量不为0 并且处理方式不为空且不为0 说明是更换
				part.setPartUseType(1);
			}else{
				part.setPartUseType(0);
			}
				part.setWrLabourcode(wrLabourcode);
				part.setIsReturn(Constant.IS_RETURN_01);
				part.setIsNotice(Constant.IF_TYPE_NO);
				netitem.setNetitemId(Long.valueOf(SequenceManager.getSequence("")));
				netitem.setAmount(Double.valueOf(amount));
				netitem.setPayType(Constant.PAY_TYPE_02);
				netitem.setItemCode("QT001");
				netitem.setItemDesc("拖车费");
				netitem.setMainPartCode(partCode);
				if(existsAppId==null){  //作新增
					dao.insert(app);
					dao.insert(item);
					dao.insert(part);
					if(!"0".equalsIgnoreCase(amount)){
						dao.insert(netitem);
					}
					if(!"0".equalsIgnoreCase(partNum)){
						dao.insertBarCode(part.getPartId().toString(),"1/1",Long.valueOf(dealerId), dealerCode);
					}
				} else {
					dao.update(appTar, app);
					dao.update(itemTar, item);
					dao.update(partTar, part);
					if(!"0".equalsIgnoreCase(amount)){
						dao.update(netitemTar, netitem);
					}
				}
			}
			act.getSession().remove("listOk");
			request.setAttribute("msg", "1");
			sendMsgByUrl(sendUrl(this.getClass(), "claimBillImport"), "索赔单状态跟踪");
    		} catch (Exception e) {
    			BizException e1 = new BizException(act, e,
    					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单导入");
    			logger.error(loginUser, e1);
    			act.setException(e1);
    		}	
	   }
	
	/**
	 * 索赔单导入模板下载
	 */
    public void downloadClaimBillModel(){
		OutputStream os = null;
		try{
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("索赔单号");
			listHead.add("dms经销商代码");
			listHead.add("dms经销商名称");
			listHead.add("威旺经销商代码");
			listHead.add("威旺经销商名称");
			listHead.add("工单号");
			listHead.add("VIN");
			listHead.add("工单开始时间");
			listHead.add("工单结束时间");
			listHead.add("索赔单上报时间");
			listHead.add("索赔类型");
			listHead.add("修改次数");
			listHead.add("故障描述");
			listHead.add("工时数量");
			listHead.add("主损件工时金额");
			listHead.add("主损件配件费");
			listHead.add("外出救援和其他费用总计");
			listHead.add("维修总费用");
			listHead.add("进厂里程");
			listHead.add("索赔单状态");
			listHead.add("维修措施");
			listHead.add("故障原因");
			listHead.add("发动机号");
			listHead.add("车型代码");
			listHead.add("质量等级");
			listHead.add("主损件代码");
			listHead.add("主损件名称");
			listHead.add("主因件对应的工时代码");
			listHead.add("主操作名称");
			listHead.add("供应商代码名称");
			listHead.add("供应商代码");
			listHead.add("配件数量");
			listHead.add("处理方式");
			listHead.add("购车日期");
			list.add(listHead);
			// 导出的文件名
			String fileName = "索赔单导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
    		} catch (Exception e) {
    			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单导入模板下载");
    			logger.error(loginUser, e1);
    			act.setException(e1);
    		}	
	   }
}



