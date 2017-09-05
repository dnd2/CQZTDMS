package com.infodms.dms.actions.sales.customerRegisterSlot;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.customerRegisterSlot.customerRegisterSlotDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCusRegisterSlotPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class customerRegisterSlot {
	public Logger logger = Logger.getLogger(customerRegisterSlot.class);
	private final int line = 253 ;
	private ActionContext act = ActionContext.getContext();
	private customerRegisterSlotDAO dao = customerRegisterSlotDAO.getInstance() ; 
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	
	private final String REGISTER_SLOT_INIT = "/jsp/sales/customerRegisterSlot/customerRegisterSlot.jsp" ;
	private final String OEM_QUERY_INIT = "/jsp/sales/customerRegisterSlot/oemRegisterSlotQueryInit.jsp" ;
	private final String OEM_QUERY_T_INIT = "/jsp/sales/customerRegisterSlot/oemRegisterSlotInit.jsp" ;
	
	public void salesInfoChangeInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			/*Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			
			act.setOutData("areaList", areaList);*/
			act.setForword(REGISTER_SLOT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪操作页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			Calendar calendar = new GregorianCalendar() ;
			int year = calendar.get(Calendar.YEAR) ;
			int month = calendar.get(Calendar.MONTH) + 1 ;
			List<Map<String, Object>> yearList = dao.yearList() ;
			
			act.setOutData("areaList", areaList);
			act.setOutData("yearList", yearList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("orgId", orgId);
			act.setForword(OEM_QUERY_T_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪操作页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void operateInsert() {
		AclUserBean logonUser = null;
		
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			/*String areaIds = request.getParamValue("areaId") ;
			String[] infos = areaIds.split("\\|") ;
			String dealerId = infos[1] ;*/
			String companyId = logonUser.getCompanyId().toString() ;
			String[] subDate = request.getParamValues("subDate") ;
			String[] cusName = request.getParamValues("cusName") ;
			String[] linkTell = request.getParamValues("linkTell") ;
			String[] hopeModel = request.getParamValues("hopeModel") ;
			String[] nowModel = request.getParamValues("nowModel") ;
			String[] mainUse = request.getParamValues("mainUse") ;
			String[] buyPoint = request.getParamValues("buyPoint") ;
			String[] info = request.getParamValues("info") ;
			String[] cusAgr = request.getParamValues("cusAgr") ;
			String[] cusAtt = request.getParamValues("cusAtt") ;
			String[] gaveUp = request.getParamValues("gaveUp") ;
			String[] saleCus = request.getParamValues("saleCus") ;
			String[] remark = request.getParamValues("remark") ;
			String[] headId = request.getParamValues("headId") ;
			String[] isSJ = request.getParamValues("isSJ") ;
			
			String retFlag = "1" ;
			
			int len = info.length ;
			TtCusRegisterSlotPO tcrs = null;
			
			if(len > 0) {
				// String companyId = logonUser.getCompanyId().toString() ;
				String userId = logonUser.getUserId().toString() ;
				Map<String, String> map = new HashMap<String, String>() ;
				
				for(int i=0; i<len; i++) {
					map.put("subDate", subDate[i]) ;
					map.put("cusName", cusName[i]) ;
					map.put("linkTell", linkTell[i]) ;
					map.put("hopeModel", hopeModel[i]) ;
					map.put("nowModel", nowModel[i]) ;
					map.put("mainUse", mainUse[i]) ;
					map.put("buyPoint", buyPoint[i]) ;
					map.put("info", info[i]) ;
					map.put("cusAgr", cusAgr[i]) ;
					map.put("cusAtt", cusAtt[i]) ;
					map.put("gaveUp", gaveUp[i]) ;
					map.put("saleCus", saleCus[i]) ;
					map.put("remark", remark[i]) ;
					map.put("companyId", companyId) ;
					map.put("userId", userId) ;
					map.put("headId", headId[i]) ;
					map.put("isSJ", isSJ[i]) ;
					
					//TODO 新增判断联系电话必须唯一 2012-07-10 韩晓宇
					List<Map<String, Object>> result = dao.getRegisterSlotByCompanyId(companyId/*, subDate[i]*/);
					for(int j=0; j<result.size(); j++) {
						String link_tell =  (String) result.get(j).get("LINK_TELL");
						BigDecimal rsId = (BigDecimal) result.get(j).get("RSID");
						if(linkTell[i] != null && linkTell[i].equals(link_tell)) {
							//TODO 判断是修改还是新增,修改时只判断是否是相同记录
							if(CommonUtils.isNullString(headId[i])) {
								throw new RuntimeException("联系电话"+link_tell+"重复!");
							} else if(!CommonUtils.isNullString(headId[i]) && !rsId.toString().equals(headId[i])){
								throw new RuntimeException("联系电话"+link_tell+"重复!");
							}
						}
					}
					
					if(!CommonUtils.isNullString(headId[i])) {
						dao.operatUpadate(map) ;
					} else {
						dao.operatInsert(map) ;
					}
				}
			}
			
			act.setOutData("retFlag", retFlag) ;
		} catch(RuntimeException re) {
			logger.error(logonUser,re);
			act.setException(re);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪新增操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
	}
	
	public void operatQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			/*String areaIds = request.getParamValue("areaId") ;
			String[] infos = areaIds.split("\\|") ;
			String dealerId = infos[1] ;*/
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
			
			String companyId = logonUser.getCompanyId().toString() ;
			Map<String, String> map = new HashMap<String, String>() ;
				
			map.put("companyId", companyId) ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.operatQuery(map , curPage, Constant.PAGE_SIZE) ;
			
			act.setOutData("ps", ps) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪查询操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemTotalQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;
			String region = CommonUtils.checkNull(request.getParamValue("regionCode")) ;
			
			String dutyType = logonUser.getDutyType() ;
			
			if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
				
			map.put("dealerId", dealerId) ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			map.put("orgId", orgId) ;
			map.put("region", region) ;
			
			List<Map<String, Object>> realList = retList(map) ;
			Map<String, Object> totalMap = getTotalMap(realList) ;
			
			act.setOutData("startDate", startDate) ;
			act.setOutData("endDate", endDate) ;
			act.setOutData("realList", realList) ;
			act.setOutData("totalMap", totalMap) ;
			act.setForword(OEM_QUERY_INIT) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪查询操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerDownLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			/*String areaIds = request.getParamValue("areaId") ;
			String[] infos = areaIds.split("\\|") ;
			String dealerId = infos[1] ;*/
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
			
			String dutyType = logonUser.getDutyType() ;
			
			
			Map<String, String> map = new HashMap<String, String>() ;
				
			if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType)) {
				String companyId = logonUser.getCompanyId().toString() ;
				
				map.put("companyId", companyId) ;
			} else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				String orgId = logonUser.getOrgId().toString() ;
				String companyId = request.getParamValue("dealerId") ;
				String regionCode = request.getParamValue("regionCode") ;
				
				map.put("orgId", orgId) ;
				map.put("companyId", companyId) ;
				map.put("regionCode", regionCode) ;
			} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				String companyId = request.getParamValue("dealerId") ;
				String regionCode = request.getParamValue("regionCode") ;
				
				map.put("companyId", companyId) ;
				map.put("regionCode", regionCode) ;
			}
			
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			
			List<Map<String, Object>> realList = dao.dealerQuery(map) ;
			int len = realList.size() ;
			// 导出的文件名
			String fileName = "集客量查询.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			
			int y = 0 ;
			int x = 0 ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"序号")) ;
			++x ;
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				sheet.mergeCells(x, y, x, y+1) ;
				sheet.addCell(new Label(x,y,"公司名称")) ;
				++x ;
			}
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"日期")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"客户姓名")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"联系电话")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"意向车型")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"现用车型")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"主要用途")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"购买侧重点")) ;
			++x ;
			sheet.mergeCells(x, y, x+9, y) ;
			sheet.addCell(new Label(x,y,"信息渠道")) ;
			sheet.addCell(new Label(x,y+1,"朋友推荐")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"展销会")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"店招吸引")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"报纸")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"电视")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"短信")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"广播")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"网络")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"户外广告")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"杂志")) ;
			++x ;
			sheet.mergeCells(x, y, x+3, y) ;
			sheet.addCell(new Label(x,y,"成交客户")) ;
			sheet.addCell(new Label(x,y+1,"首洽成交")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"跟踪成交")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"试驾成交")) ;
			++x ;
			sheet.addCell(new Label(x,y+1,"客户性质")) ;
			++x ;
			sheet.addCell(new Label(x,y,"放弃客户")) ;
			sheet.addCell(new Label(x,y+1,"放弃原因")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"销售顾问")) ;
			++x ;
			sheet.mergeCells(x, y, x, y+1) ;
			sheet.addCell(new Label(x,y,"备注")) ;
			++y;
			++y;
			for(int i=0; i<len; i++) {
				int j=0 ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("NUM") != null ? realList.get(i).get("NUM").toString() : "")) ;
				if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
					sheet.addCell(new Label(j++,y,realList.get(i).get("COMPANY_NAME") != null ? realList.get(i).get("COMPANY_NAME").toString() : "")) ;
				}
				sheet.addCell(new Label(j++,y,realList.get(i).get("MY_DATE") != null ? realList.get(i).get("MY_DATE").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("CUS_NAME") != null ? realList.get(i).get("CUS_NAME").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("LINK_TELL") != null ? realList.get(i).get("LINK_TELL").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("HOPE_MODEL") != null ? realList.get(i).get("HOPE_MODEL").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("NOW_MODEL") != null ? realList.get(i).get("NOW_MODEL").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("MAIN_USE") != null ? realList.get(i).get("MAIN_USE").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("BUY_POINT_DESC") != null ? realList.get(i).get("BUY_POINT_DESC").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_PYTJ") != null ? realList.get(i).get("INFO_DITCH_PYTJ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_ZXH") != null ? realList.get(i).get("INFO_DITCH_ZXH").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_DZXY") != null ? realList.get(i).get("INFO_DITCH_DZXY").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_BZ") != null ? realList.get(i).get("INFO_DITCH_BZ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_DS") != null ? realList.get(i).get("INFO_DITCH_DS").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_DX") != null ? realList.get(i).get("INFO_DITCH_DX").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_GB") != null ? realList.get(i).get("INFO_DITCH_GB").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_WL") != null ? realList.get(i).get("INFO_DITCH_WL").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_HWGG") != null ? realList.get(i).get("INFO_DITCH_HWGG").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("INFO_DITCH_ZZ") != null ? realList.get(i).get("INFO_DITCH_ZZ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("BARGAIN_CUS_SQ") != null ? realList.get(i).get("BARGAIN_CUS_SQ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("BARGAIN_CUS_GZ") != null ? realList.get(i).get("BARGAIN_CUS_GZ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("BARGAIN_CUS_SJ") != null ? realList.get(i).get("BARGAIN_CUS_SJ").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("CUS_NATURE_DESC") != null ? realList.get(i).get("CUS_NATURE_DESC").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("DESERT_REASON_DESC") != null ? realList.get(i).get("DESERT_REASON_DESC").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("SALE_ADVISER") != null ? realList.get(i).get("SALE_ADVISER").toString() : "")) ;
				sheet.addCell(new Label(j++,y,realList.get(i).get("REMARK") != null ? realList.get(i).get("REMARK").toString() : "")) ;
				y++ ;
			}
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪下载操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemTotalDownLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;
			String region = CommonUtils.checkNull(request.getParamValue("regionCode")) ;
			
			String dutyType = logonUser.getDutyType() ;
			
			if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				orgId = logonUser.getOrgId().toString() ;
			} else if(dutyType.equals(Constant.DUTY_TYPE_DEALER.toString())) {
				dealerId = logonUser.getCompanyId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
				
			map.put("dealerId", dealerId) ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			map.put("orgId", orgId) ;
			map.put("region", region) ;
			
			List<Map<String, Object>> realList = retList(map) ;
			Map<String, Object> totalMap = getTotalMap(realList) ;
			int len = realList.size() ;
			// 导出的文件名
			String fileName = startDate + "至" + endDate + "集客量汇总.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			
			int y = 0 ;
			sheet.mergeCells(0, y, 0, y+1) ;
			sheet.addCell(new Label(0,y,"时间")) ;
			sheet.addCell(new Label(1,y,"日期")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("day").toString())) ;
			}
			sheet.mergeCells(len+2, y, len+2, y+1) ;
			sheet.addCell(new Label(len+2,y,"合计")) ;
			++y ;
			sheet.addCell(new Label(1,y,"星期")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("week").toString())) ;
			}
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"来电/店客户数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("totalCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("totalCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"留有信息客户数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("saveInfoCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("saveInfoCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"成交数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("bargainCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("bargainCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"首洽成交数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("bargainSQCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("bargainSQCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"跟踪成交数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("bargainGZCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("bargainGZCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 1, y) ;
			sheet.addCell(new Label(0,y,"试驾成交数")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("bargainSJCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("bargainSJCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 0,y+9) ;
			sheet.addCell(new Label(0,y,"信息渠道")) ;
			sheet.addCell(new Label(1,y,"朋友推荐")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitPYTJCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitPYTJCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"展销会")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitZXHCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitZXHCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"店招吸引")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitDZXYCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitDZXYCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"报纸")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitBZCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitBZCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"电视")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitDSCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitDSCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"短信")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitDXCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitDXCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"广播")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitGBCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitGBCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"网络")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitWLCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitWLCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"户外广告")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitHWGGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitHWGGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"杂志")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("infoDitZZCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("infoDitZZCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 0,y+9) ;
			sheet.addCell(new Label(0,y,"购买侧重点")) ;
			sheet.addCell(new Label(1,y,"外观")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiWGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiWGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"动力性能")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiDLXNCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiDLXNCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"使用成本")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiSYCBCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiSYCBCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"售后方便")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiSHFBCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiSHFBCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"舒适性")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiSSXCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiSSXCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"安全性")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiAQXCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiAQXCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"操控性")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiCKXCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiCKXCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"价格")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiJGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiJGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"结实耐用")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiJSNYCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiJSNYCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"空间")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("buyPoiKJCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("buyPoiKJCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 0,y+6) ;
			sheet.addCell(new Label(0,y,"放弃购买原因")) ;
			sheet.addCell(new Label(1,y,"车的外观")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReCDWGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReCDWGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"油耗")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReHYCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReHYCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"产品质量")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReCPZLCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReCPZLCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"价格")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReJGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReJGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"空间")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertRePPCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertRePPCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"售后服务政策")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReSHZCCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReSHZCCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"考虑其他品牌")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("desertReQTPPCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("desertReQTPPCountT").toString())) ;
			++y ;
			sheet.mergeCells(0, y, 0,y+2) ;
			sheet.addCell(new Label(0,y,"客户性质")) ;
			sheet.addCell(new Label(1,y,"新购")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("cusNatXGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("cusNatXGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"换购")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("cusNatHGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("cusNatHGCountT").toString())) ;
			++y ;
			sheet.addCell(new Label(1,y,"增购")) ;
			for (int i = 0; i < len; i++) {
				sheet.addCell(new Label(i+2,y,realList.get(i).get("cusNatZGCount").toString())) ;
			}
			sheet.addCell(new Label(len+2,y,totalMap.get("cusNatZGCountT").toString())) ;
			
			if(!dutyType.equals(Constant.DUTY_TYPE_DEALER.toString())) {
			
			++y ;
			sheet.mergeCells(0, y, 0,y+4) ;
			sheet.addCell(new Label(0,y,"统计分析项目")) ;
			sheet.mergeCells(1, y, 25,y) ;
			sheet.addCell(new Label(1,y,"1.当月展厅集客指数R=当月来访客户批数合计/当月展厅销售任务数，R＜3，集客量少，加强集客量活动。")) ;
			++y ;
			sheet.mergeCells(1, y, 25,y) ;
			sheet.addCell(new Label(1,y,"2.当月客户信息留存率=当月留有资料客户数合计/当月来访客户批数合计×100%")) ;
			++y ;
			sheet.mergeCells(1, y, 25,y) ;
			sheet.addCell(new Label(1,y,"3.当月成交率=当月展厅销售台数/当月来访客户批数合计×100%")) ;
			++y ;
			sheet.mergeCells(1, y, 25,y) ;
			sheet.addCell(new Label(1,y,"4.首洽成交：指首次和用户接洽即达成购车的客户。线索成交：指前期留有客户信息，由潜在客户发展为最终成交的客户。试驾成交：指通过试驾后达成购车的客户。")) ;
			++y ;
			sheet.mergeCells(1, y, 25,y) ;
			sheet.addCell(new Label(1,y,"5.推荐率=当月朋友推荐成交数量/当月成交量×100%")) ;
			++y ;
			sheet.mergeCells(0, y, 0,y+4) ;
			sheet.addCell(new Label(0,y,"当月指标分析")) ;
			sheet.mergeCells(1, y, 3,y) ;
			sheet.addCell(new Label(1, y,"当月展厅销售任务数：")) ;
			++y ;
			sheet.mergeCells(1, y, 3,y) ;
			sheet.addCell(new Label(1,y,"当月展厅集客指数：")) ;
			++y ;
			sheet.mergeCells(1, y, 3,y) ;
			sheet.addCell(new Label(1,y,"当月客户信息留存率：")) ;
			++y ;
			sheet.mergeCells(1, y, 3,y) ;
			sheet.addCell(new Label(1,y,"当月成交率：")) ;
			++y ;
			sheet.mergeCells(1, y, 3,y) ;
			sheet.addCell(new Label(1,y,"推荐率：")) ;
			}
			// sheet.addCell(new Label(0,i,(content.get(i).get(j)).toString())) ;
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪下载操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemTotalDrlDownLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;
			String region = CommonUtils.checkNull(request.getParamValue("regionCode")) ;
			
			String dutyType = logonUser.getDutyType() ;
			
			if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
				
			map.put("dealerId", dealerId) ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			map.put("orgId", orgId) ;
			map.put("region", region) ;
			
			List<Map<String, Object>> realList = retListNew(map) ;
			Map<String, Object> totalMap = getTotalMap(realList) ;
			int len = realList.size() ;
			// 导出的文件名
			String fileName = startDate + "至" + endDate + "集客量汇总.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			
			for(int n=1; n<=Math.ceil(new Double(len)/line); n++) {
				WritableSheet sheet = workbook.createSheet("下载模板" + n, 0);
				
				int iAdd = (n - 1) * line ;
				int lenAdd = n * line ;
				
				if(Math.ceil(new Double(len)/line) == n) {
					lenAdd = len ;
				}
				
				int y = 0 ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"经销商公司全称")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("COMPANY_NAME").toString())) ;
				}
				sheet.mergeCells(line+2, y, line+2, y) ;
				sheet.addCell(new Label(line+2,y,"合计")) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"来电/店客户数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("totalCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("totalCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"留有信息客户数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("saveInfoCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("saveInfoCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"成交数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("bargainCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("bargainCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"首洽成交数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("bargainSQCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("bargainSQCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"跟踪成交数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("bargainGZCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("bargainGZCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 1, y) ;
				sheet.addCell(new Label(0,y,"试驾成交数")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("bargainSJCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("bargainSJCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+9) ;
				sheet.addCell(new Label(0,y,"信息渠道")) ;
				sheet.addCell(new Label(1,y,"朋友推荐")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitPYTJCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitPYTJCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"展销会")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitZXHCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitZXHCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"店招吸引")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitDZXYCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitDZXYCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"报纸")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitBZCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitBZCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"电视")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitDSCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitDSCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"短信")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitDXCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitDXCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"广播")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitGBCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitGBCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"网络")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitWLCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitWLCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"户外广告")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitHWGGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitHWGGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"杂志")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("infoDitZZCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("infoDitZZCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+9) ;
				sheet.addCell(new Label(0,y,"购买侧重点")) ;
				sheet.addCell(new Label(1,y,"外观")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiWGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiWGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"动力性能")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiDLXNCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiDLXNCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"使用成本")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiSYCBCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiSYCBCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"售后方便")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiSHFBCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiSHFBCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"舒适性")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiSSXCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiSSXCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"安全性")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiAQXCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiAQXCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"操控性")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiCKXCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiCKXCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"价格")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiJGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiJGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"结实耐用")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiJSNYCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiJSNYCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"空间")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("buyPoiKJCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("buyPoiKJCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+6) ;
				sheet.addCell(new Label(0,y,"放弃购买原因")) ;
				sheet.addCell(new Label(1,y,"车的外观")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReCDWGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReCDWGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"油耗")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReHYCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReHYCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"产品质量")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReCPZLCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReCPZLCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"价格")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReJGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReJGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"空间")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertRePPCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertRePPCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"售后服务政策")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReSHZCCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReSHZCCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"考虑其他品牌")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("desertReQTPPCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("desertReQTPPCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+2) ;
				sheet.addCell(new Label(0,y,"客户性质")) ;
				sheet.addCell(new Label(1,y,"新购")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("cusNatXGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("cusNatXGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"换购")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("cusNatHGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("cusNatHGCountT").toString())) ;
				++y ;
				sheet.addCell(new Label(1,y,"增购")) ;
				for (int i = iAdd; i < lenAdd; i++) {
					sheet.addCell(new Label(i%line+2,y,realList.get(i).get("cusNatZGCount").toString())) ;
				}
				sheet.addCell(new Label(line+2,y,totalMap.get("cusNatZGCountT").toString())) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+4) ;
				sheet.addCell(new Label(0,y,"统计分析项目")) ;
				sheet.mergeCells(1, y, 25,y) ;
				sheet.addCell(new Label(1,y,"1.当月展厅集客指数R=当月来访客户批数合计/当月展厅销售任务数，R＜3，集客量少，加强集客量活动。")) ;
				++y ;
				sheet.mergeCells(1, y, 25,y) ;
				sheet.addCell(new Label(1,y,"2.当月客户信息留存率=当月留有资料客户数合计/当月来访客户批数合计×100%")) ;
				++y ;
				sheet.mergeCells(1, y, 25,y) ;
				sheet.addCell(new Label(1,y,"3.当月成交率=当月展厅销售台数/当月来访客户批数合计×100%")) ;
				++y ;
				sheet.mergeCells(1, y, 25,y) ;
				sheet.addCell(new Label(1,y,"4.首洽成交：指首次和用户接洽即达成购车的客户。线索成交：指前期留有客户信息，由潜在客户发展为最终成交的客户。试驾成交：指通过试驾后达成购车的客户。")) ;
				++y ;
				sheet.mergeCells(1, y, 25,y) ;
				sheet.addCell(new Label(1,y,"5.推荐率=当月朋友推荐成交数量/当月成交量×100%")) ;
				++y ;
				sheet.mergeCells(0, y, 0,y+4) ;
				sheet.addCell(new Label(0,y,"当月指标分析")) ;
				sheet.mergeCells(1, y, 3,y) ;
				sheet.addCell(new Label(1, y,"当月展厅销售任务数：")) ;
				++y ;
				sheet.mergeCells(1, y, 3,y) ;
				sheet.addCell(new Label(1,y,"当月展厅集客指数：")) ;
				++y ;
				sheet.mergeCells(1, y, 3,y) ;
				sheet.addCell(new Label(1,y,"当月客户信息留存率：")) ;
				++y ;
				sheet.mergeCells(1, y, 3,y) ;
				sheet.addCell(new Label(1,y,"当月成交率：")) ;
				++y ;
				sheet.mergeCells(1, y, 3,y) ;
				sheet.addCell(new Label(1,y,"推荐率：")) ;
				// sheet.addCell(new Label(0,i,(content.get(i).get(j)).toString())) ;
			}
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "来电（店）客户信息登记及跟踪下载操作失败 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public List<Map<String, Object>> retList(Map<String, String> map) {
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		
		List<Map<String, Object>> crsList = dao.oemTotolQuery(map) ;
		int len = crsList.size() ;
		
		List<Map<String, Object>> realList = setDateList(startDate, endDate) ;
		
		Map<String, Object> realMap = null ;
		
		int dayCount = realList.size() ;
		
		for(int i=0; i<dayCount; i++) {
			String totalCount = "0" ;
			String saveInfoCount = "0" ;
			String bargainCount = "0" ;
			String bargainSQCount = "0" ;
			String bargainGZCount = "0" ;
			String bargainSJCount = "0" ;
			String infoDitPYTJCount = "0" ;
			String infoDitZXHCount = "0" ;
			String infoDitDZXYCount = "0" ;
			String infoDitBZCount = "0" ;
			String infoDitDSCount = "0" ;
			String infoDitDXCount = "0" ;
			String infoDitGBCount = "0" ;
			String infoDitWLCount = "0" ;
			String infoDitHWGGCount = "0" ;
			String infoDitZZCount = "0" ;
			String buyPoiWGCount = "0" ;
			String buyPoiDLXNCount = "0" ;
			String buyPoiSYCBCount = "0" ;
			String buyPoiSHFBCount = "0" ;
			String buyPoiSSXCount = "0" ;
			String buyPoiAQXCount = "0" ;
			String buyPoiCKXCount = "0" ;
			String buyPoiJGCount = "0" ;
			String buyPoiJSNYCount = "0" ;
			String buyPoiKJCount = "0" ;
			String desertReCDWGCount = "0" ;
			String desertReHYCount = "0" ;
			String desertReCPZLCount = "0" ;
			String desertReJGCount = "0" ;
			String desertRePPCount = "0" ;
			String desertReSHZCCount = "0" ;
			String desertReQTPPCount = "0" ;
			String cusNatXGCount = "0" ;
			String cusNatHGCount = "0" ;
			String cusNatZGCount = "0" ;
			
			realMap = new HashMap<String, Object>() ;
			
			for(int j=0; j<len; j++) {
				int oYear = Integer.parseInt(crsList.get(j).get("YEAR").toString()) ;
				int oMonth = Integer.parseInt(crsList.get(j).get("MONTH").toString()) ;
				int oDay = Integer.parseInt(crsList.get(j).get("DAY").toString()) ;
				
				int nYear = Integer.parseInt(realList.get(i).get("year").toString()) ;
				int nMonth = Integer.parseInt(realList.get(i).get("month").toString()) ;
				int nDay = Integer.parseInt(realList.get(i).get("day").toString()) ;
				
				if(oYear == nYear && oMonth == nMonth && oDay == nDay) {
					totalCount = crsList.get(j).get("TOTAL_COUNT").toString() ;
					saveInfoCount = crsList.get(j).get("SAVA_INFO_TOTAL_COUNT").toString() ;
					bargainCount = crsList.get(j).get("BARGAIN_CUS_COUNT").toString() ;
					
					bargainSQCount = crsList.get(j).get("BARGAIN_CUS_SQ_COUNT").toString() ;
					bargainGZCount = crsList.get(j).get("BARGAIN_CUS_GZ_COUNT").toString() ;
					bargainSJCount = crsList.get(j).get("BARGAIN_CUS_SJ_COUNT").toString() ;
					
					infoDitPYTJCount = crsList.get(j).get("INFO_DITCH_PYTJ_COUNT").toString() ;
					infoDitZXHCount = crsList.get(j).get("INFO_DITCH_ZXH_COUNT").toString() ;
					infoDitDZXYCount = crsList.get(j).get("INFO_DITCH_DZXY_COUNT").toString() ;
					infoDitBZCount = crsList.get(j).get("INFO_DITCH_BZ_COUNT").toString() ;
					infoDitDSCount = crsList.get(j).get("INFO_DITCH_DS_COUNT").toString() ;
					infoDitDXCount = crsList.get(j).get("INFO_DITCH_DX_COUNT").toString() ;
					infoDitGBCount = crsList.get(j).get("INFO_DITCH_GB_COUNT").toString() ;
					infoDitWLCount = crsList.get(j).get("INFO_DITCH_WL_COUNT").toString() ;
					infoDitHWGGCount = crsList.get(j).get("INFO_DITCH_HWGG_COUNT").toString() ;
					infoDitZZCount = crsList.get(j).get("INFO_DITCH_ZZ_COUNT").toString() ;
					
					buyPoiWGCount = crsList.get(j).get("BUY_POINT_WG_COUNT").toString() ;
					buyPoiDLXNCount = crsList.get(j).get("BUY_POINT_DLXN_COUNT").toString() ;
					buyPoiSYCBCount = crsList.get(j).get("BUY_POINT_SYCB_COUNT").toString() ;
					buyPoiSHFBCount = crsList.get(j).get("BUY_POINT_SHFB_COUNT").toString() ;
					buyPoiSSXCount = crsList.get(j).get("BUY_POINT_SSX_COUNT").toString() ;
					buyPoiAQXCount = crsList.get(j).get("BUY_POINT_AQX_COUNT").toString() ;
					buyPoiCKXCount = crsList.get(j).get("BUY_POINT_CKX_COUNT").toString() ;
					buyPoiJGCount = crsList.get(j).get("BUY_POINT_JG_COUNT").toString() ;
					buyPoiJSNYCount = crsList.get(j).get("BUY_POINT_JSNY_COUNT").toString() ;
					buyPoiKJCount = crsList.get(j).get("BUY_POINT_KJ_COUNT").toString() ;
					
					desertReCDWGCount = crsList.get(j).get("DESERT_REASON_CWG_COUNT").toString() ;
					desertReHYCount = crsList.get(j).get("DESERT_REASON_YH_COUNT").toString() ;
					desertReCPZLCount = crsList.get(j).get("DESERT_REASON_CPZL_COUNT").toString() ;
					desertReJGCount = crsList.get(j).get("DESERT_REASON_JG_COUNT").toString() ;
					desertRePPCount = crsList.get(j).get("DESERT_REASON_PP_COUNT").toString() ;
					desertReSHZCCount = crsList.get(j).get("DESERT_REASON_SHZC_COUNT").toString() ;
					desertReQTPPCount = crsList.get(j).get("DESERT_REASON_QTPP_COUNT").toString() ;
					
					cusNatXGCount = crsList.get(j).get("CUS_NATURE_XG_COUNT").toString() ;
					cusNatHGCount = crsList.get(j).get("CUS_NATURE_HG_COUNT").toString() ;
					cusNatZGCount = crsList.get(j).get("CUS_NATURE_ZG_COUNT").toString() ;
					
					break ;
				}
			}
			realList.get(i).put("totalCount", getSwitch(totalCount, "0", "")) ;
			realList.get(i).put("saveInfoCount", getSwitch(saveInfoCount, "0", "")) ;
			realList.get(i).put("bargainCount", getSwitch(bargainCount, "0", "")) ;
			realList.get(i).put("bargainSQCount", getSwitch(bargainSQCount, "0", "")) ;
			realList.get(i).put("bargainGZCount", getSwitch(bargainGZCount, "0", "")) ;
			realList.get(i).put("bargainSJCount", getSwitch(bargainSJCount, "0", "")) ;
			realList.get(i).put("infoDitPYTJCount", getSwitch(infoDitPYTJCount, "0", "")) ;
			realList.get(i).put("infoDitZXHCount", getSwitch(infoDitZXHCount, "0", "")) ;
			realList.get(i).put("infoDitDZXYCount", getSwitch(infoDitDZXYCount, "0", "")) ;
			realList.get(i).put("infoDitBZCount", getSwitch(infoDitBZCount, "0", "")) ;
			realList.get(i).put("infoDitDSCount", getSwitch(infoDitDSCount, "0", "")) ;
			realList.get(i).put("infoDitDXCount", getSwitch(infoDitDXCount, "0", "")) ;
			realList.get(i).put("infoDitGBCount", getSwitch(infoDitGBCount, "0", "")) ;
			realList.get(i).put("infoDitWLCount", getSwitch(infoDitWLCount, "0", "")) ;
			realList.get(i).put("infoDitHWGGCount", getSwitch(infoDitHWGGCount, "0", "")) ;
			realList.get(i).put("infoDitZZCount", getSwitch(infoDitZZCount, "0", "")) ;
			realList.get(i).put("buyPoiWGCount", getSwitch(buyPoiWGCount, "0", "")) ;
			realList.get(i).put("buyPoiDLXNCount", getSwitch(buyPoiDLXNCount, "0", "")) ;
			realList.get(i).put("buyPoiSYCBCount", getSwitch(buyPoiSYCBCount, "0", "")) ;
			realList.get(i).put("buyPoiSHFBCount", getSwitch(buyPoiSHFBCount, "0", "")) ;
			realList.get(i).put("buyPoiSSXCount", getSwitch(buyPoiSSXCount, "0", "")) ;
			realList.get(i).put("buyPoiAQXCount", getSwitch(buyPoiAQXCount, "0", "")) ;
			realList.get(i).put("buyPoiCKXCount", getSwitch(buyPoiCKXCount, "0", "")) ;
			realList.get(i).put("buyPoiJGCount", getSwitch(buyPoiJGCount, "0", "")) ;
			realList.get(i).put("buyPoiJSNYCount", getSwitch(buyPoiJSNYCount, "0", "")) ;
			realList.get(i).put("buyPoiKJCount", getSwitch(buyPoiKJCount, "0", "")) ;
			realList.get(i).put("desertReCDWGCount", getSwitch(desertReCDWGCount, "0", "")) ;
			realList.get(i).put("desertReHYCount", getSwitch(desertReHYCount, "0", "")) ;
			realList.get(i).put("desertReCPZLCount", getSwitch(desertReCPZLCount, "0", "")) ;
			realList.get(i).put("desertReJGCount", getSwitch(desertReJGCount, "0", "")) ;
			realList.get(i).put("desertRePPCount", getSwitch(desertRePPCount, "0", "")) ;
			realList.get(i).put("desertReSHZCCount", getSwitch(desertReSHZCCount, "0", "")) ;
			realList.get(i).put("desertReQTPPCount", getSwitch(desertReQTPPCount, "0", "")) ;
			realList.get(i).put("cusNatXGCount", getSwitch(cusNatXGCount, "0", "")) ;
			realList.get(i).put("cusNatHGCount", getSwitch(cusNatHGCount, "0", "")) ;
			realList.get(i).put("cusNatZGCount", getSwitch(cusNatZGCount, "0", "")) ;
			
			// realList.add(realMap) ;
		}
		
		return realList ;
	}
	
	public List<Map<String, Object>> retListNew(Map<String, String> map) {
		List<Map<String, Object>> crsList = dao.oemTotolDlrQuery(map) ;
		int len = crsList.size() ;
		
		List<Map<String, Object>> realList = dao.getComById(map) ;
		
		Map<String, Object> realMap = null ;
		
		int dlrCount = realList.size() ;
		
		for(int i=0; i<dlrCount; i++) {
			String totalCount = "0" ;
			String saveInfoCount = "0" ;
			String bargainCount = "0" ;
			String bargainSQCount = "0" ;
			String bargainGZCount = "0" ;
			String bargainSJCount = "0" ;
			String infoDitPYTJCount = "0" ;
			String infoDitZXHCount = "0" ;
			String infoDitDZXYCount = "0" ;
			String infoDitBZCount = "0" ;
			String infoDitDSCount = "0" ;
			String infoDitDXCount = "0" ;
			String infoDitGBCount = "0" ;
			String infoDitWLCount = "0" ;
			String infoDitHWGGCount = "0" ;
			String infoDitZZCount = "0" ;
			String buyPoiWGCount = "0" ;
			String buyPoiDLXNCount = "0" ;
			String buyPoiSYCBCount = "0" ;
			String buyPoiSHFBCount = "0" ;
			String buyPoiSSXCount = "0" ;
			String buyPoiAQXCount = "0" ;
			String buyPoiCKXCount = "0" ;
			String buyPoiJGCount = "0" ;
			String buyPoiJSNYCount = "0" ;
			String buyPoiKJCount = "0" ;
			String desertReCDWGCount = "0" ;
			String desertReHYCount = "0" ;
			String desertReCPZLCount = "0" ;
			String desertReJGCount = "0" ;
			String desertRePPCount = "0" ;
			String desertReSHZCCount = "0" ;
			String desertReQTPPCount = "0" ;
			String cusNatXGCount = "0" ;
			String cusNatHGCount = "0" ;
			String cusNatZGCount = "0" ;
			
			realMap = new HashMap<String, Object>() ;
			
			for(int j=0; j<len; j++) {
				long dealerId = Long.parseLong(crsList.get(j).get("DEALER_ID").toString()) ;
				
				long nDealerId = Long.parseLong(realList.get(i).get("COMPANY_ID").toString()) ;
				
				if(dealerId == nDealerId) {
					totalCount = crsList.get(j).get("TOTALCOUNT").toString() ;
					saveInfoCount = crsList.get(j).get("SAVEINFOCOUNT").toString() ;
					bargainCount = crsList.get(j).get("BARGAINCOUNT").toString() ;
					
					bargainSQCount = crsList.get(j).get("BARGAINSQCOUNT").toString() ;
					bargainGZCount = crsList.get(j).get("BARGAINGZCOUNT").toString() ;
					bargainSJCount = crsList.get(j).get("BARGAINSJCOUNT").toString() ;
					
					infoDitPYTJCount = crsList.get(j).get("INFODITPYTJCOUNT").toString() ;
					infoDitZXHCount = crsList.get(j).get("INFODITZXHCOUNT").toString() ;
					infoDitDZXYCount = crsList.get(j).get("INFODITDZXYCOUNT").toString() ;
					infoDitBZCount = crsList.get(j).get("INFODITBZCOUNT").toString() ;
					infoDitDSCount = crsList.get(j).get("INFODITDSCOUNT").toString() ;
					infoDitDXCount = crsList.get(j).get("INFODITDXCOUNT").toString() ;
					infoDitGBCount = crsList.get(j).get("INFODITGBCOUNT").toString() ;
					infoDitWLCount = crsList.get(j).get("INFODITWLCOUNT").toString() ;
					infoDitHWGGCount = crsList.get(j).get("INFODITHWGGCOUNT").toString() ;
					infoDitZZCount = crsList.get(j).get("INFODITZZCOUNT").toString() ;
					
					buyPoiWGCount = crsList.get(j).get("BUYPOIWGCOUNT").toString() ;
					buyPoiDLXNCount = crsList.get(j).get("BUYPOIDLXNCOUNT").toString() ;
					buyPoiSYCBCount = crsList.get(j).get("BUYPOISYCBCOUNT").toString() ;
					buyPoiSHFBCount = crsList.get(j).get("BUYPOISHFBCOUNT").toString() ;
					buyPoiSSXCount = crsList.get(j).get("BUYPOISSXCOUNT").toString() ;
					buyPoiAQXCount = crsList.get(j).get("BUYPOIAQXCOUNT").toString() ;
					buyPoiCKXCount = crsList.get(j).get("BUYPOICKXCOUNT").toString() ;
					buyPoiJGCount = crsList.get(j).get("BUYPOIJGCOUNT").toString() ;
					buyPoiJSNYCount = crsList.get(j).get("BUYPOIJSNYCOUNT").toString() ;
					buyPoiKJCount = crsList.get(j).get("BUYPOIKJCOUNT").toString() ;
					
					desertReCDWGCount = crsList.get(j).get("DESERTRECDWGCOUNT").toString() ;
					desertReHYCount = crsList.get(j).get("DESERTREHYCOUNT").toString() ;
					desertReCPZLCount = crsList.get(j).get("DESERTRECPZLCOUNT").toString() ;
					desertReJGCount = crsList.get(j).get("DESERTREJGCOUNT").toString() ;
					desertRePPCount = crsList.get(j).get("DESERTREPPCOUNT").toString() ;
					desertReSHZCCount = crsList.get(j).get("DESERTRESHZCCOUNT").toString() ;
					desertReQTPPCount = crsList.get(j).get("DESERTREQTPPCOUNT").toString() ;
					
					cusNatXGCount = crsList.get(j).get("CUSNATXGCOUNT").toString() ;
					cusNatHGCount = crsList.get(j).get("CUSNATHGCOUNT").toString() ;
					cusNatZGCount = crsList.get(j).get("CUSNATZGCOUNT").toString() ;
					
					break ;
				}
			}
			realList.get(i).put("totalCount", getSwitch(totalCount, "0", "")) ;
			realList.get(i).put("saveInfoCount", getSwitch(saveInfoCount, "0", "")) ;
			realList.get(i).put("bargainCount", getSwitch(bargainCount, "0", "")) ;
			realList.get(i).put("bargainSQCount", getSwitch(bargainSQCount, "0", "")) ;
			realList.get(i).put("bargainGZCount", getSwitch(bargainGZCount, "0", "")) ;
			realList.get(i).put("bargainSJCount", getSwitch(bargainSJCount, "0", "")) ;
			realList.get(i).put("infoDitPYTJCount", getSwitch(infoDitPYTJCount, "0", "")) ;
			realList.get(i).put("infoDitZXHCount", getSwitch(infoDitZXHCount, "0", "")) ;
			realList.get(i).put("infoDitDZXYCount", getSwitch(infoDitDZXYCount, "0", "")) ;
			realList.get(i).put("infoDitBZCount", getSwitch(infoDitBZCount, "0", "")) ;
			realList.get(i).put("infoDitDSCount", getSwitch(infoDitDSCount, "0", "")) ;
			realList.get(i).put("infoDitDXCount", getSwitch(infoDitDXCount, "0", "")) ;
			realList.get(i).put("infoDitGBCount", getSwitch(infoDitGBCount, "0", "")) ;
			realList.get(i).put("infoDitWLCount", getSwitch(infoDitWLCount, "0", "")) ;
			realList.get(i).put("infoDitHWGGCount", getSwitch(infoDitHWGGCount, "0", "")) ;
			realList.get(i).put("infoDitZZCount", getSwitch(infoDitZZCount, "0", "")) ;
			realList.get(i).put("buyPoiWGCount", getSwitch(buyPoiWGCount, "0", "")) ;
			realList.get(i).put("buyPoiDLXNCount", getSwitch(buyPoiDLXNCount, "0", "")) ;
			realList.get(i).put("buyPoiSYCBCount", getSwitch(buyPoiSYCBCount, "0", "")) ;
			realList.get(i).put("buyPoiSHFBCount", getSwitch(buyPoiSHFBCount, "0", "")) ;
			realList.get(i).put("buyPoiSSXCount", getSwitch(buyPoiSSXCount, "0", "")) ;
			realList.get(i).put("buyPoiAQXCount", getSwitch(buyPoiAQXCount, "0", "")) ;
			realList.get(i).put("buyPoiCKXCount", getSwitch(buyPoiCKXCount, "0", "")) ;
			realList.get(i).put("buyPoiJGCount", getSwitch(buyPoiJGCount, "0", "")) ;
			realList.get(i).put("buyPoiJSNYCount", getSwitch(buyPoiJSNYCount, "0", "")) ;
			realList.get(i).put("buyPoiKJCount", getSwitch(buyPoiKJCount, "0", "")) ;
			realList.get(i).put("desertReCDWGCount", getSwitch(desertReCDWGCount, "0", "")) ;
			realList.get(i).put("desertReHYCount", getSwitch(desertReHYCount, "0", "")) ;
			realList.get(i).put("desertReCPZLCount", getSwitch(desertReCPZLCount, "0", "")) ;
			realList.get(i).put("desertReJGCount", getSwitch(desertReJGCount, "0", "")) ;
			realList.get(i).put("desertRePPCount", getSwitch(desertRePPCount, "0", "")) ;
			realList.get(i).put("desertReSHZCCount", getSwitch(desertReSHZCCount, "0", "")) ;
			realList.get(i).put("desertReQTPPCount", getSwitch(desertReQTPPCount, "0", "")) ;
			realList.get(i).put("cusNatXGCount", getSwitch(cusNatXGCount, "0", "")) ;
			realList.get(i).put("cusNatHGCount", getSwitch(cusNatHGCount, "0", "")) ;
			realList.get(i).put("cusNatZGCount", getSwitch(cusNatZGCount, "0", "")) ;
			
			// realList.add(realMap) ;
		}
		
		return realList ;
	}
	
	public Map<String, Object> getTotalMap(List<Map<String, Object>> realList) {
		Map<String, Object> totalMap = new HashMap<String, Object>() ;
		totalMap.put("totalCountT", getMapTotal(realList, "totalCount")) ;
		totalMap.put("saveInfoCountT", getMapTotal(realList, "saveInfoCount")) ;
		totalMap.put("bargainCountT", getMapTotal(realList, "bargainCount")) ;
		totalMap.put("bargainSQCountT", getMapTotal(realList, "bargainSQCount")) ;
		totalMap.put("bargainGZCountT", getMapTotal(realList, "bargainGZCount")) ;
		totalMap.put("bargainSJCountT", getMapTotal(realList, "bargainSJCount")) ;
		totalMap.put("infoDitPYTJCountT", getMapTotal(realList, "infoDitPYTJCount")) ;
		totalMap.put("infoDitZXHCountT", getMapTotal(realList, "infoDitZXHCount")) ;
		totalMap.put("infoDitDZXYCountT", getMapTotal(realList, "infoDitDZXYCount")) ;
		totalMap.put("infoDitBZCountT", getMapTotal(realList, "infoDitBZCount")) ;
		totalMap.put("infoDitDSCountT", getMapTotal(realList, "infoDitDSCount")) ;
		totalMap.put("infoDitDXCountT", getMapTotal(realList, "infoDitDXCount")) ;
		totalMap.put("infoDitGBCountT", getMapTotal(realList, "infoDitGBCount")) ;
		totalMap.put("infoDitWLCountT", getMapTotal(realList, "infoDitWLCount")) ;
		totalMap.put("infoDitHWGGCountT", getMapTotal(realList, "infoDitHWGGCount")) ;
		totalMap.put("infoDitZZCountT", getMapTotal(realList, "infoDitZZCount")) ;
		totalMap.put("buyPoiWGCountT", getMapTotal(realList, "buyPoiWGCount")) ;
		totalMap.put("buyPoiDLXNCountT", getMapTotal(realList, "buyPoiDLXNCount")) ;
		totalMap.put("buyPoiSYCBCountT", getMapTotal(realList, "buyPoiSYCBCount")) ;
		totalMap.put("buyPoiSHFBCountT", getMapTotal(realList, "buyPoiSHFBCount")) ;
		totalMap.put("buyPoiSSXCountT", getMapTotal(realList, "buyPoiSSXCount")) ;
		totalMap.put("buyPoiAQXCountT", getMapTotal(realList, "buyPoiAQXCount")) ;
		totalMap.put("buyPoiCKXCountT", getMapTotal(realList, "buyPoiCKXCount")) ;
		totalMap.put("buyPoiJGCountT", getMapTotal(realList, "buyPoiJGCount")) ;
		totalMap.put("buyPoiJSNYCountT", getMapTotal(realList, "buyPoiJSNYCount")) ;
		totalMap.put("buyPoiKJCountT", getMapTotal(realList, "buyPoiKJCount")) ;
		totalMap.put("desertReCDWGCountT", getMapTotal(realList, "desertReCDWGCount")) ;
		totalMap.put("desertReHYCountT", getMapTotal(realList, "desertReHYCount")) ;
		totalMap.put("desertReCPZLCountT", getMapTotal(realList, "desertReCPZLCount")) ;
		totalMap.put("desertReJGCountT", getMapTotal(realList, "desertReJGCount")) ;
		totalMap.put("desertRePPCountT", getMapTotal(realList, "desertRePPCount")) ;
		totalMap.put("desertReSHZCCountT", getMapTotal(realList, "desertReSHZCCount")) ;
		totalMap.put("desertReQTPPCountT", getMapTotal(realList, "desertReQTPPCount")) ;
		totalMap.put("cusNatXGCountT", getMapTotal(realList, "cusNatXGCount")) ;
		totalMap.put("cusNatHGCountT", getMapTotal(realList, "cusNatHGCount")) ;
		totalMap.put("cusNatZGCountT", getMapTotal(realList, "cusNatZGCount")) ;
		
		return totalMap ;
	}
	
	public Map<String, Object> getTotalMapNew(List<Map<String, Object>> realList) {
		Map<String, Object> totalMap = new HashMap<String, Object>() ;
		totalMap.put("totalCountT", getMapTotal(realList, "TOTALCOUNT")) ;
		totalMap.put("saveInfoCountT", getMapTotal(realList, "SAVEINFOCOUNT")) ;
		totalMap.put("bargainCountT", getMapTotal(realList, "BARGAINCOUNT")) ;
		totalMap.put("bargainSQCountT", getMapTotal(realList, "BARGAINSQCOUNT")) ;
		totalMap.put("bargainGZCountT", getMapTotal(realList, "BARGAINGZCOUNT")) ;
		totalMap.put("bargainSJCountT", getMapTotal(realList, "BARGAINSJCOUNT")) ;
		totalMap.put("infoDitPYTJCountT", getMapTotal(realList, "INFODITPYTJCOUNT")) ;
		totalMap.put("infoDitZXHCountT", getMapTotal(realList, "INFODITZXHCOUNT")) ;
		totalMap.put("infoDitDZXYCountT", getMapTotal(realList, "INFODITDZXYCOUNT")) ;
		totalMap.put("infoDitBZCountT", getMapTotal(realList, "INFODITBZCOUNT")) ;
		totalMap.put("infoDitDSCountT", getMapTotal(realList, "INFODITDSCOUNT")) ;
		totalMap.put("infoDitDXCountT", getMapTotal(realList, "INFODITDXCOUNT")) ;
		totalMap.put("infoDitGBCountT", getMapTotal(realList, "INFODITGBCOUNT")) ;
		totalMap.put("infoDitWLCountT", getMapTotal(realList, "INFODITWLCOUNT")) ;
		totalMap.put("infoDitHWGGCountT", getMapTotal(realList, "INFODITHWGGCOUNT")) ;
		totalMap.put("infoDitZZCountT", getMapTotal(realList, "INFODITZZCOUNT")) ;
		totalMap.put("buyPoiWGCountT", getMapTotal(realList, "BUYPOIWGCOUNT")) ;
		totalMap.put("buyPoiDLXNCountT", getMapTotal(realList, "BUYPOIDLXNCOUNT")) ;
		totalMap.put("buyPoiSYCBCountT", getMapTotal(realList, "BUYPOISYCBCOUNT")) ;
		totalMap.put("buyPoiSHFBCountT", getMapTotal(realList, "BUYPOISHFBCOUNT")) ;
		totalMap.put("buyPoiSSXCountT", getMapTotal(realList, "BUYPOISSXCOUNT")) ;
		totalMap.put("buyPoiAQXCountT", getMapTotal(realList, "BUYPOIAQXCOUNT")) ;
		totalMap.put("buyPoiCKXCountT", getMapTotal(realList, "BUYPOICKXCOUNT")) ;
		totalMap.put("buyPoiJGCountT", getMapTotal(realList, "BUYPOIJGCOUNT")) ;
		totalMap.put("buyPoiJSNYCountT", getMapTotal(realList, "BUYPOIJSNYCOUNT")) ;
		totalMap.put("buyPoiKJCountT", getMapTotal(realList, "BUYPOIKJCOUNT")) ;
		totalMap.put("desertReCDWGCountT", getMapTotal(realList, "DESERTRECDWGCOUNT")) ;
		totalMap.put("desertReHYCountT", getMapTotal(realList, "DESERTREHYCOUNT")) ;
		totalMap.put("desertReCPZLCountT", getMapTotal(realList, "DESERTRECPZLCOUNT")) ;
		totalMap.put("desertReJGCountT", getMapTotal(realList, "DESERTREJGCOUNT")) ;
		totalMap.put("desertRePPCountT", getMapTotal(realList, "DESERTREPPCOUNT")) ;
		totalMap.put("desertReSHZCCountT", getMapTotal(realList, "DESERTRESHZCCOUNT")) ;
		totalMap.put("desertReQTPPCountT", getMapTotal(realList, "DESERTREQTPPCOUNT")) ;
		totalMap.put("cusNatXGCountT", getMapTotal(realList, "CUSNATXGCOUNT")) ;
		totalMap.put("cusNatHGCountT", getMapTotal(realList, "CUSNATHGCOUNT")) ;
		totalMap.put("cusNatZGCountT", getMapTotal(realList, "CUSNATZGCOUNT")) ;
		
		return totalMap ;
	}
	
	public String getWeekName(int week) {
		String weekName = "" ;
		if(week == 2) {
			weekName = "一" ;
		} else if(week == 3) {
			weekName = "二" ;
		} else if(week == 4) {
			weekName = "三" ;
		} else if(week == 5) {
			weekName = "四" ;
		} else if(week == 6) {
			weekName = "五" ;
		} else if(week == 7) {
			weekName = "六" ;
		} else if(week == 1) {
			weekName = "日" ;
		}
		
		return weekName ;
	}
	
	public String getMyNumber(String str, int admin) {
		StringBuffer strBuf = new StringBuffer(str) ;
		int len = strBuf.length() ;

		for (int i=len; i<admin; i++) {
			strBuf.insert(0, "0") ;
		}
		
		return strBuf.toString() ;
	}
	
	public List<Map<String, Object>> setDateList(String startDate, String endDate) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
		Map<String, Object> map = null ;

		long quot = 0 ;

		String[] date = startDate.split("-") ;
		int year = Integer.parseInt(date[0]) ;
		int month = Integer.parseInt(date[1]) ;
		int day = Integer.parseInt(date[2]) ;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		try {
			Date stDate = sdf.parse(startDate) ;
			Date enDate = sdf.parse(endDate) ;

			quot = enDate.getTime() - stDate.getTime() ;

			quot = quot/1000/60/60/24 ;
		} catch(Exception e) {

		} 
		
		Calendar calendar = new GregorianCalendar() ;

		for(int j=day; j<=day+quot; j++) {
			calendar.set(year, month-1, j) ;

			map = new HashMap<String, Object>() ;

			map.put("year", calendar.get(Calendar.YEAR)) ;
			map.put("month", calendar.get(Calendar.MONTH)+1) ;
			map.put("day", calendar.get(Calendar.DAY_OF_MONTH)) ;
			map.put("week", getWeekName(calendar.get(Calendar.DAY_OF_WEEK))) ;

			list.add(map) ;
		}

		return list ;
	}
	
	public long getMapTotal(List<Map<String, Object>> list, String str) {
		long total = 0 ;
		
		if(CommonUtils.isNullList(list)) {
			return total ;
		}
		
		int myLen = list.size() ;
		
		for(int i=0; i<myLen; i++) {
			total += Long.parseLong(getSwitch(list.get(i).get(str).toString(),"", "0")) ;
		}
		
		return total ;
	}
	
	public String getSwitch(String valueStr, String modValue, String swiValue) {
		String switchStr = swiValue ;
		if(!CommonUtils.isNullString(valueStr)) {
			if(modValue.equals(valueStr)) {
				switchStr = swiValue ;
			} else {
				switchStr = valueStr ;
			}
		}
		
		return switchStr ;
	}
}
