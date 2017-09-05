package com.infodms.dms.dao.sales.customerRegisterSlot;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCusRegisterSlotPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class customerRegisterSlotDAO  extends BaseDao {
	public Logger logger = Logger.getLogger(customerRegisterSlotDAO.class);
	
	private static final customerRegisterSlotDAO dao = new customerRegisterSlotDAO();

	public static final customerRegisterSlotDAO getInstance() {
		return dao;
	}
	
	public void operatInsert(Map<String, String> map) {
		String[] subDate = map.get("subDate").split("-") ;
		String year = subDate[0] ;
		String month = subDate[1] ;
		String day = subDate[2] ;
		String cusName = map.get("cusName") ;
		String linkTell = map.get("linkTell") ;
		String hopeModel = map.get("hopeModel") ;
		String nowModel = map.get("nowModel") ;
		String mainUse = map.get("mainUse") ;
		String buyPoint = map.get("buyPoint") ;
		String info = map.get("info") ;
		String cusAgr = map.get("cusAgr") ;
		String cusAtt = map.get("cusAtt") ;
		String gaveUp = map.get("gaveUp") ;
		String saleCus = map.get("saleCus") ;
		String remark = map.get("remark") ;
		String companyId = map.get("companyId") ;
		String userId = map.get("userId") ;
		String isSJ = map.get("isSJ") ;
		
		TtCusRegisterSlotPO tcrs = new TtCusRegisterSlotPO() ;
		
		Long tcrsId = Long.parseLong(SequenceManager.getSequence("")) ;
		tcrs.setRegisterSlotId(tcrsId) ;
		String tcrsNo = "RS" + tcrsId ;
		tcrs.setRegisterSlotNo(tcrsNo) ;
		
		if(!CommonUtils.isNullString(cusName)) {
			tcrs.setCusName(cusName) ;
		}
		
		if(!CommonUtils.isNullString(linkTell)) {
			tcrs.setLinkTell(linkTell) ;
		}
		
		if(!CommonUtils.isNullString(hopeModel)) {
			tcrs.setHopeModel(hopeModel) ;
		}
		
		if(!CommonUtils.isNullString(nowModel)) {
			tcrs.setNowModel(nowModel) ;
		}
		
		if(!CommonUtils.isNullString(mainUse)) {
			tcrs.setMainUse(mainUse) ;
		}

		if(!CommonUtils.isNullString(buyPoint)) {
			tcrs.setBuyPoint(Integer.parseInt(buyPoint)) ;
		}
		
		if(!CommonUtils.isNullString(info)) {
			tcrs.setInfoDitch(Integer.parseInt(info)) ;
		}
		
		if(!CommonUtils.isNullString(cusAgr)) {
			tcrs.setBargainCus(Integer.parseInt(cusAgr)) ;
		}
		
		if(!CommonUtils.isNullString(cusAtt)) {
			tcrs.setCusNature(Integer.parseInt(cusAtt)) ;
		}
		
		if(!CommonUtils.isNullString(gaveUp)) {
			tcrs.setDesertReason(Integer.parseInt(gaveUp)) ;
		}
		
		if(!CommonUtils.isNullString(saleCus)) {
			tcrs.setSaleAdviser(saleCus) ;
		}
		
		if(!CommonUtils.isNullString(remark)) {
			tcrs.setRemark(remark) ;
		}
		
		if(!CommonUtils.isNullString(year)) {
			tcrs.setYear(year) ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			tcrs.setMonth(month) ;
		}
		
		if(!CommonUtils.isNullString(day)) {
			tcrs.setDay(day) ;
		}
		
		if(!CommonUtils.isNullString(isSJ)) {
			tcrs.setIsSj(Long.parseLong(isSJ)) ;
		}
		
		tcrs.setDealerId(Long.parseLong(companyId)) ;
		tcrs.setCreateDate(new Date(System.currentTimeMillis())) ;
		tcrs.setCreateBy(Long.parseLong(userId)) ;
		
		dao.insert(tcrs) ;
	}
	
	public void operatUpadate(Map<String, String> map) {
		String[] subDate = map.get("subDate").split("-") ;
		String year = subDate[0] ;
		String month = subDate[1] ;
		String day = subDate[2] ;
		String cusName = map.get("cusName") ;
		String linkTell = map.get("linkTell") ;
		String hopeModel = map.get("hopeModel") ;
		String nowModel = map.get("nowModel") ;
		String mainUse = map.get("mainUse") ;
		String buyPoint = map.get("buyPoint") ;
		String info = map.get("info") ;
		String cusAgr = map.get("cusAgr") ;
		String cusAtt = map.get("cusAtt") ;
		String gaveUp = map.get("gaveUp") ;
		String saleCus = map.get("saleCus") ;
		String remark = map.get("remark") ;
		String companyId = map.get("companyId") ;
		String userId = map.get("userId") ;
		String headId = map.get("headId") ;
		String isSJ = map.get("isSJ") ;
		
		TtCusRegisterSlotPO oldTcrs = new TtCusRegisterSlotPO() ;
		oldTcrs.setRegisterSlotId(Long.parseLong(headId)) ;
		TtCusRegisterSlotPO tcrs = new TtCusRegisterSlotPO() ;
		
		if(CommonUtils.isNullString(cusName)) {
			tcrs.setCusName("") ;
		} else {
			tcrs.setCusName(cusName) ;
		}
		
		if(CommonUtils.isNullString(linkTell)) {
			tcrs.setLinkTell("") ;
		} else {
			tcrs.setLinkTell(linkTell) ;
		}
		
		if(CommonUtils.isNullString(hopeModel)) {
			tcrs.setHopeModel("") ;
		} else {
			tcrs.setHopeModel(hopeModel) ;
		}
		
		if(CommonUtils.isNullString(nowModel)) {
			tcrs.setNowModel("") ;
		} else {
			tcrs.setNowModel(nowModel) ;
		}
		
		if(CommonUtils.isNullString(mainUse)) {
			tcrs.setMainUse("") ;
		} else {
			tcrs.setMainUse(mainUse) ;
		}

		if(CommonUtils.isNullString(buyPoint)) {
			tcrs.setBuyPoint(0) ;
		} else {
			tcrs.setBuyPoint(Integer.parseInt(buyPoint)) ;
		}
		
		if(CommonUtils.isNullString(info)) {
			tcrs.setInfoDitch(0) ;
		} else {
			tcrs.setInfoDitch(Integer.parseInt(info)) ;
		}
		
		if(CommonUtils.isNullString(cusAgr)) {
			tcrs.setBargainCus(0) ;
		} else {
			tcrs.setBargainCus(Integer.parseInt(cusAgr)) ;
		}
		
		if(CommonUtils.isNullString(cusAtt)) {
			tcrs.setCusNature(0) ;
		} else {
			tcrs.setCusNature(Integer.parseInt(cusAtt)) ;
		}
		
		if(CommonUtils.isNullString(gaveUp)) {
			tcrs.setDesertReason(0) ;
		} else {
			tcrs.setDesertReason(Integer.parseInt(gaveUp)) ;
		}
		
		if(CommonUtils.isNullString(saleCus)) {
			tcrs.setSaleAdviser("") ;
		}else {
			tcrs.setSaleAdviser(saleCus) ;
		}
		
		if(CommonUtils.isNullString(remark)) {
			tcrs.setRemark("") ;
		} else {
			tcrs.setRemark(remark) ;
		}
		
		if(!CommonUtils.isNullString(year)) {
			tcrs.setYear(year) ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			tcrs.setMonth(month) ;
		}
		
		if(!CommonUtils.isNullString(day)) {
			tcrs.setDay(day) ;
		}
		
		if(!CommonUtils.isNullString(isSJ)) {
			tcrs.setIsSj(Long.parseLong(isSJ)) ;
		}
		
		tcrs.setDealerId(Long.parseLong(companyId)) ;
		tcrs.setUpdateDate(new Date(System.currentTimeMillis())) ;
		tcrs.setUpdateBy(Long.parseLong(userId)) ;
		
		dao.update(oldTcrs,tcrs) ;
	}
	
	public PageResult<Map<String, Object>> operatQuery(Map<String, String> map,int curPage,int pageSize) {
		String companyId = map.get("companyId") ;
		String startDate = map.get("startDate").replaceAll("-", "") ;
		String endDate = map.get("endDate").replaceAll("-", "") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TCRS.YEAR || '-' || TCRS.MONTH || '-' || TCRS.DAY APPLYDATE,\n");
		sql.append("       TCRS.CUS_NAME,\n");  
		sql.append("       TCRS.LINK_TELL,\n");  
		sql.append("       TCRS.HOPE_MODEL,\n");  
		sql.append("       TCRS.NOW_MODEL,\n");  
		sql.append("       TCRS.MAIN_USE,\n");  
		sql.append("       TCRS.BUY_POINT,\n");  
		sql.append("       TCRS.INFO_DITCH,\n");  
		sql.append("       TCRS.BARGAIN_CUS,\n");  
		sql.append("       TCRS.CUS_NATURE,\n");  
		sql.append("       TCRS.DESERT_REASON,\n");  
		sql.append("       TCRS.SALE_ADVISER,\n");  
		sql.append("       TCRS.REMARK,\n");  
		sql.append("       TMC.COMPANY_NAME,\n");  
		sql.append("       TCRS.REGISTER_SLOT_NO,\n"); 
		sql.append("       TCRS.IS_SJ,\n");  
		sql.append("       TCRS.REGISTER_SLOT_ID\n");  
		sql.append("  FROM TT_CUS_REGISTER_SLOT TCRS, TM_COMPANY TMC\n");  
		sql.append(" WHERE TCRS.DEALER_ID = TMC.COMPANY_ID\n");
		
		if(!CommonUtils.isNullString(companyId)) {
			sql.append("   and tcrs.dealer_id in (").append(companyId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) >= ").append(startDate).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(endDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) <= ").append(endDate).append("\n") ;
		}
		
		sql.append(" order by TCRS.create_date desc\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	}
	
	public List<Map<String, Object>> oemTotolQuery(Map<String, String> map) {
		String startDate = map.get("startDate").replaceAll("-", "") ;
		String endDate = map.get("endDate").replaceAll("-", "") ;
		String dealerId = map.get("dealerId") ;
		String orgId = map.get("orgId") ;
		String region = map.get("region") ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select temp.year,\n");
		sql.append("       temp.month,\n");  
		sql.append("       temp.day,\n");  
		sql.append("       sum(temp.BUY_POINT_WG) BUY_POINT_WG_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_DLXN) BUY_POINT_DLXN_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_SYCB) BUY_POINT_SYCB_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_SHFB) BUY_POINT_SHFB_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_SSX) BUY_POINT_SSX_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_AQX) BUY_POINT_AQX_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_CKX) BUY_POINT_CKX_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_JG) BUY_POINT_JG_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_JSNY) BUY_POINT_JSNY_COUNT,\n");  
		sql.append("       sum(temp.BUY_POINT_KJ) BUY_POINT_KJ_COUNT,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.INFO_DITCH_PYTJ) INFO_DITCH_PYTJ_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_ZXH) INFO_DITCH_ZXH_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_DZXY) INFO_DITCH_DZXY_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_BZ) INFO_DITCH_BZ_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_DS) INFO_DITCH_DS_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_DX) INFO_DITCH_DX_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_GB) INFO_DITCH_GB_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_WL) INFO_DITCH_WL_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_HWGG) INFO_DITCH_HWGG_COUNT,\n");  
		sql.append("       sum(temp.INFO_DITCH_ZZ) INFO_DITCH_ZZ_COUNT,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SQ) + sum(temp.BARGAIN_CUS_GZ) BARGAIN_CUS_COUNT,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SQ) BARGAIN_CUS_SQ_COUNT,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_GZ) BARGAIN_CUS_GZ_COUNT,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SJ) BARGAIN_CUS_SJ_COUNT,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.CUS_NATURE_XG) CUS_NATURE_XG_COUNT,\n");  
		sql.append("       sum(temp.CUS_NATURE_HG) CUS_NATURE_HG_COUNT,\n");  
		sql.append("       sum(temp.CUS_NATURE_ZG) CUS_NATURE_ZG_COUNT,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.DESERT_REASON_CWG) DESERT_REASON_CWG_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_YH) DESERT_REASON_YH_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_CPZL) DESERT_REASON_CPZL_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_JG) DESERT_REASON_JG_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_PP) DESERT_REASON_PP_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_SHZC) DESERT_REASON_SHZC_COUNT,\n");  
		sql.append("       sum(temp.DESERT_REASON_QTPP) DESERT_REASON_QTPP_COUNT,\n");  
		sql.append("\n");  
		sql.append("	   sum(temp.save_info) sava_info_total_count,\n");
		sql.append("       count(temp.register_slot_id) total_count\n");  
		sql.append("  from (select tcrs.year,\n");  
		sql.append("               tcrs.month,\n");  
		sql.append("               tcrs.day,\n");  
		// sql.append("               tcrs.year || tcrs.month || tcrs.day myDay,\n");  
		sql.append("               tcrs.dealer_id,\n");  
		sql.append("               tcrs.register_slot_id,\n");  
		sql.append("               decode(tcrs.buy_point, 80531001, 1, 0) BUY_POINT_WG,\n");  
		sql.append("               decode(tcrs.buy_point, 80531002, 1, 0) BUY_POINT_DLXN,\n");  
		sql.append("               decode(tcrs.buy_point, 80531003, 1, 0) BUY_POINT_SYCB,\n");  
		sql.append("               decode(tcrs.buy_point, 80531004, 1, 0) BUY_POINT_SHFB,\n");  
		sql.append("               decode(tcrs.buy_point, 80531005, 1, 0) BUY_POINT_SSX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531006, 1, 0) BUY_POINT_AQX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531007, 1, 0) BUY_POINT_CKX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531008, 1, 0) BUY_POINT_JG,\n");  
		sql.append("               decode(tcrs.buy_point, 80531009, 1, 0) BUY_POINT_JSNY,\n");
		sql.append("               decode(tcrs.buy_point, 80531010, 1, 0) BUY_POINT_KJ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.info_ditch, 80521001, 1, 0) INFO_DITCH_PYTJ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521002, 1, 0) INFO_DITCH_ZXH,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521003, 1, 0) INFO_DITCH_DZXY,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521004, 1, 0) INFO_DITCH_BZ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521005, 1, 0) INFO_DITCH_DS,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521006, 1, 0) INFO_DITCH_DX,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521007, 1, 0) INFO_DITCH_GB,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521008, 1, 0) INFO_DITCH_WL,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521009, 1, 0) INFO_DITCH_HWGG,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521010, 1, 0) INFO_DITCH_ZZ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561001, 1, 0) BARGAIN_CUS_SQ,\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561002, 1, 0) BARGAIN_CUS_GZ,\n");  
		sql.append("               decode(tcrs.is_sj, 10041001, 1, 0) BARGAIN_CUS_SJ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.cus_nature, 80551001, 1, 0) CUS_NATURE_XG,\n");  
		sql.append("               decode(tcrs.cus_nature, 80551002, 1, 0) CUS_NATURE_HG,\n");  
		sql.append("               decode(tcrs.cus_nature, 80551003, 1, 0) CUS_NATURE_ZG,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.desert_reason, 80541001, 1, 0) DESERT_REASON_CWG,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541002, 1, 0) DESERT_REASON_YH,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541003, 1, 0) DESERT_REASON_CPZL,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541004, 1, 0) DESERT_REASON_JG,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541005, 1, 0) DESERT_REASON_PP,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541006, 1, 0) DESERT_REASON_SHZC,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541007, 1, 0) DESERT_REASON_QTPP,\n"); 
		sql.append("			   floor(decode(tcrs.cus_name, null, 0, 0.5) + decode(tcrs.link_tell, null, 0, 0.5)) save_info\n");
		sql.append("          from tt_cus_register_slot tcrs, tm_company tmc\n") ;
		sql.append("          where 1 = 1\n") ;
		sql.append("            and tcrs.dealer_id = tmc.company_id\n") ;
		
		/*if(!CommonUtils.isNullString(year)) {
			sql.append("            and tcrs.year = ").append(year).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			sql.append("            and tcrs.month = ").append(month).append("\n") ;
		}*/
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("            and tcrs.dealer_id in (").append(dealerId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(orgId) && !"null".equals(orgId)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tt_vs_org_region_r tvorr\n");  
			sql.append("         where tvorr.region_code = tmc.province_id\n");  
			sql.append("           and tvorr.org_id = ").append(orgId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("            and tmc.province_id in (").append(region).append(")\n") ;
		}
		
		/*if(!CommonUtils.isNullString(region)) {
			sql.append("            and tmd.province_id = ").append(region).append("\n") ;
		}*/
		
		if(!CommonUtils.isNullString(startDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) >= ").append(startDate).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(endDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) <= ").append(endDate).append("\n") ;
		}
		
		sql.append("		  ) temp\n");  
		sql.append(" group by temp.year, temp.month, temp.day\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String, Object>> oemTotolDlrQuery(Map<String, String> map) {
		String startDate = map.get("startDate").replaceAll("-", "") ;
		String endDate = map.get("endDate").replaceAll("-", "") ;
		String dealerId = map.get("dealerId") ;
		String orgId = map.get("orgId") ;
		String region = map.get("region") ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select temp.dealer_name,\n");
		sql.append("       temp.dealer_id,\n");  
		sql.append("       sum(temp.BUY_POINT_WG) buyPoiWGCount,\n");  
		sql.append("       sum(temp.BUY_POINT_DLXN) buyPoiDLXNCount,\n");  
		sql.append("       sum(temp.BUY_POINT_SYCB) buyPoiSYCBCount,\n");  
		sql.append("       sum(temp.BUY_POINT_SHFB) buyPoiSHFBCount,\n");  
		sql.append("       sum(temp.BUY_POINT_SSX) buyPoiSSXCount,\n");  
		sql.append("       sum(temp.BUY_POINT_AQX) buyPoiAQXCount,\n");  
		sql.append("       sum(temp.BUY_POINT_CKX) buyPoiCKXCount,\n");  
		sql.append("       sum(temp.BUY_POINT_JG) buyPoiJGCount,\n");  
		sql.append("       sum(temp.BUY_POINT_JSNY) buyPoiJSNYCount,\n");  
		sql.append("       sum(temp.BUY_POINT_KJ) buyPoiKJCount,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.INFO_DITCH_PYTJ) infoDitPYTJCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_ZXH) infoDitZXHCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_DZXY) infoDitDZXYCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_BZ) infoDitBZCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_DS) infoDitDSCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_DX) infoDitDXCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_GB) infoDitGBCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_WL) infoDitWLCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_HWGG) infoDitHWGGCount,\n");  
		sql.append("       sum(temp.INFO_DITCH_ZZ) infoDitZZCount,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SQ) + sum(temp.BARGAIN_CUS_GZ) bargainCount,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SQ) bargainSQCount,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_GZ) bargainGZCount,\n");  
		sql.append("       sum(temp.BARGAIN_CUS_SJ) bargainSJCount,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.CUS_NATURE_XG) cusNatXGCount,\n");  
		sql.append("       sum(temp.CUS_NATURE_HG) cusNatHGCount,\n");  
		sql.append("       sum(temp.CUS_NATURE_ZG) cusNatZGCount,\n");  
		sql.append("\n");  
		sql.append("       sum(temp.DESERT_REASON_CWG) desertReCDWGCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_YH) desertReHYCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_CPZL) desertReCPZLCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_JG) desertReJGCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_PP) desertRePPCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_SHZC) desertReSHZCCount,\n");  
		sql.append("       sum(temp.DESERT_REASON_QTPP) desertReQTPPCount,\n");  
		sql.append("\n");  
		sql.append("	   sum(temp.save_info) saveInfoCount,\n");
		sql.append("       count(temp.register_slot_id) totalCount\n");  
		sql.append("  from (select tmc.company_name dealer_name,\n");
		sql.append("               tmc.company_id dealer_id,\n");  
		sql.append("               tcrs.month,\n");  
		sql.append("               tcrs.day,\n");  
		// sql.append("               tcrs.year || tcrs.month || tcrs.day myDay,\n");  
		sql.append("               tcrs.register_slot_id,\n");  
		sql.append("               decode(tcrs.buy_point, 80531001, 1, 0) BUY_POINT_WG,\n");  
		sql.append("               decode(tcrs.buy_point, 80531002, 1, 0) BUY_POINT_DLXN,\n");  
		sql.append("               decode(tcrs.buy_point, 80531003, 1, 0) BUY_POINT_SYCB,\n");  
		sql.append("               decode(tcrs.buy_point, 80531004, 1, 0) BUY_POINT_SHFB,\n");  
		sql.append("               decode(tcrs.buy_point, 80531005, 1, 0) BUY_POINT_SSX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531006, 1, 0) BUY_POINT_AQX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531007, 1, 0) BUY_POINT_CKX,\n");  
		sql.append("               decode(tcrs.buy_point, 80531008, 1, 0) BUY_POINT_JG,\n");  
		sql.append("               decode(tcrs.buy_point, 80531009, 1, 0) BUY_POINT_JSNY,\n");
		sql.append("               decode(tcrs.buy_point, 80531010, 1, 0) BUY_POINT_KJ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.info_ditch, 80521001, 1, 0) INFO_DITCH_PYTJ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521002, 1, 0) INFO_DITCH_ZXH,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521003, 1, 0) INFO_DITCH_DZXY,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521004, 1, 0) INFO_DITCH_BZ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521005, 1, 0) INFO_DITCH_DS,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521006, 1, 0) INFO_DITCH_DX,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521007, 1, 0) INFO_DITCH_GB,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521008, 1, 0) INFO_DITCH_WL,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521009, 1, 0) INFO_DITCH_HWGG,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521010, 1, 0) INFO_DITCH_ZZ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561001, 1, 0) BARGAIN_CUS_SQ,\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561002, 1, 0) BARGAIN_CUS_GZ,\n");  
		sql.append("               decode(tcrs.is_sj, 10041001, 1, 0) BARGAIN_CUS_SJ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.cus_nature, 80551001, 1, 0) CUS_NATURE_XG,\n");  
		sql.append("               decode(tcrs.cus_nature, 80551002, 1, 0) CUS_NATURE_HG,\n");  
		sql.append("               decode(tcrs.cus_nature, 80551003, 1, 0) CUS_NATURE_ZG,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.desert_reason, 80541001, 1, 0) DESERT_REASON_CWG,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541002, 1, 0) DESERT_REASON_YH,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541003, 1, 0) DESERT_REASON_CPZL,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541004, 1, 0) DESERT_REASON_JG,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541005, 1, 0) DESERT_REASON_PP,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541006, 1, 0) DESERT_REASON_SHZC,\n");  
		sql.append("               decode(tcrs.desert_reason, 80541007, 1, 0) DESERT_REASON_QTPP,\n"); 
		sql.append("			   floor(decode(tcrs.cus_name, null, 0, 0.5) + decode(tcrs.link_tell, null, 0, 0.5)) save_info\n");
		sql.append("          from tt_cus_register_slot tcrs, tm_company tmc\n") ;
		sql.append("          where 1 = 1\n") ;
		sql.append("            and tcrs.dealer_id = tmc.company_id\n") ;
		
		/*if(!CommonUtils.isNullString(year)) {
			sql.append("            and tcrs.year = ").append(year).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			sql.append("            and tcrs.month = ").append(month).append("\n") ;
		}*/
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("            and tcrs.dealer_id in (").append(dealerId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(orgId) && !"null".equals(orgId)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tt_vs_org_region_r tvorr\n");  
			sql.append("         where tvorr.region_code = tmc.province_id\n");  
			sql.append("           and tvorr.org_id = ").append(orgId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("            and tmc.province_id in (").append(region).append(")\n") ;
		}
		
		/*if(!CommonUtils.isNullString(region)) {
			sql.append("            and tmd.province_id = ").append(region).append("\n") ;
		}*/
		
		if(!CommonUtils.isNullString(startDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) >= ").append(startDate).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(endDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) <= ").append(endDate).append("\n") ;
		}
		
		sql.append("		  ) temp\n");  
		sql.append(" group by temp.dealer_name,temp.dealer_id\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String,Object>> yearList() {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT distinct tds.set_year FROM Tm_Date_Set tds\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String, Object>> dealerQuery(Map<String, String> map) {
		String companyId = map.get("companyId") ;
		String orgId = map.get("orgId") ;
		String regionCode = map.get("regionCode") ;
		String startDate = map.get("startDate").replaceAll("-", "") ;
		String endDate = map.get("endDate").replaceAll("-", "") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select rownum num, tc.company_name, tcrs.year,\n");
		sql.append("               tcrs.month,\n");  
		sql.append("               tcrs.day,\n");  
		sql.append("               tcrs.year || '-' || tcrs.month || '-' || tcrs.day my_date,\n");  
		sql.append("               tcrs.dealer_id,\n");  
		sql.append("               tcrs.cus_name,\n");  
		sql.append("               tcrs.link_tell,\n");  
		sql.append("               tcrs.hope_model,\n");  
		sql.append("               tcrs.now_model,\n");  
		sql.append("               tcrs.main_use,\n");  
		sql.append("               tcrs.sale_adviser,\n");  
		sql.append("               tcrs.remark,\n");  
		sql.append("               tcrs.register_slot_id,\n");  
		sql.append("               tcrs.buy_point,\n");  
		sql.append("               tcc1.code_desc buy_point_desc,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.info_ditch, 80521001, 1) INFO_DITCH_PYTJ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521002, 1) INFO_DITCH_ZXH,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521003, 1) INFO_DITCH_DZXY,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521004, 1) INFO_DITCH_BZ,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521005, 1) INFO_DITCH_DS,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521006, 1) INFO_DITCH_DX,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521007, 1) INFO_DITCH_GB,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521008, 1) INFO_DITCH_WL,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521009, 1) INFO_DITCH_HWGG,\n");  
		sql.append("               decode(tcrs.info_ditch, 80521010, 1) INFO_DITCH_ZZ,\n");  
		sql.append("\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561001, 1) BARGAIN_CUS_SQ,\n");  
		sql.append("               decode(tcrs.bargain_cus, 80561002, 1) BARGAIN_CUS_GZ,\n");  
		sql.append("               decode(tcrs.is_sj, 10041001, 1) BARGAIN_CUS_SJ,\n");  
		sql.append("\n");  
		sql.append("               tcrs.cus_nature,\n");  
		sql.append("               tcc2.code_desc cus_nature_desc,\n");  
		sql.append("\n");  
		sql.append("               tcrs.desert_reason,\n");  
		sql.append("               tcc3.code_desc desert_reason_desc\n");  
		sql.append("          from tt_cus_register_slot tcrs, tc_code tcc1, tc_code tcc2, tc_code tcc3, tm_company tc\n");  
		sql.append("          where 1 = 1\n");  
		sql.append("            and tcrs.buy_point = tcc1.code_id(+)\n");  
		sql.append("            and tcrs.cus_nature = tcc2.code_id(+)\n");  
		sql.append("            and tcrs.desert_reason = tcc3.code_id(+)\n");  
		
		if(!CommonUtils.isNullString(companyId)) {
			sql.append("            and tcrs.dealer_id in (").append(companyId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(regionCode)) {
			sql.append("and exists (select 1\n");
			sql.append("          from tm_company tmc\n");  
			sql.append("         where tmc.company_id = tcrs.dealer_id\n");  
			sql.append("           and tmc.province_id in (").append(regionCode).append("))\n");
		}
		
		if(!CommonUtils.isNullString(orgId) && !"null".equals(orgId)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tt_vs_org_region_r tvorr, tm_company tmc1\n");  
			sql.append("         where tvorr.region_code = tmc1.province_id\n");  
			sql.append("           and tmc1.company_id = tcrs.dealer_id\n");
			sql.append("           and tvorr.org_id = ").append(orgId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(startDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) >= ").append(startDate).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(endDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) <= ").append(endDate).append("\n") ;
		}
		
		sql.append("and tc.company_id(+) = tcrs.dealer_id\n");


		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String, Object>> getDlrById(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_id, tmd.dealer_name\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where tmd.dealer_type <> ").append(Constant.MSG_TYPE_2).append("\n");  
		sql.append("   and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   and tmd.dealer_id in (").append(dealerId).append(")\n");
		}

		List<Map<String, Object>> dlrList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return dlrList ;
	}
	
	public List<Map<String, Object>> getComById(Map<String, String> map) {
		String companyId = map.get("dealerId") ;
		String region = map.get("region") ;
		String orgId = map.get("orgId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmc.company_id, tmc.company_name\n");
		sql.append("  from tm_company tmc\n");  
		sql.append(" where 1 = 1 \n"); 
		sql.append("   and tmc.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		
		if(!CommonUtils.isNullString(companyId)) {
			sql.append("   and tmc.company_id in (").append(companyId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and tmc.province_id in (").append(region).append(")\n");
		}
		
		sql.append("   and exists (select 1\n");  
		sql.append("          from tm_dealer tmd\n");  
		sql.append("         where tmd.company_id = tmc.company_id\n");  
		sql.append("           and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		sql.append("           and tmd.dealer_type <> ").append(Constant.MSG_TYPE_2).append(")\n");  
		
		if(!CommonUtils.isNullString(orgId) && !"null".equals(orgId)) {
			sql.append("   and exists (select 1\n");  
			sql.append("          from tt_vs_org_region_r tvorr\n");  
			sql.append("         where tvorr.region_code = tmc.province_id\n");  
			sql.append("           and tvorr.org_id = ").append(orgId).append(")\n");
		}

		List<Map<String, Object>> dlrList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return dlrList ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据公司ID查询跟踪登记表所有记录
	 * @param companyId 公司ID
	 * @param subDate 录入时间
	 * @return  跟踪登记表记录列表
	 * 2012-06-12 
	 * @author 韩晓宇
	 * */
	public List<Map<String, Object>> getRegisterSlotByCompanyId(String companyId/*, String subDate*/) {
		
		String endDate = null;
		endDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		/*subDate = subDate.replace("-", "");*/
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select tcrs.register_slot_id rsId, tcrs.link_tell, tcrs.cus_name \n");  
		sql.append("          from tt_cus_register_slot tcrs\n");  
		sql.append("where 1 = 1\n");
		if(!CommonUtils.isNullString(companyId)) {
			sql.append("   and tcrs.dealer_id in (\n");
			sql.append(companyId);
			sql.append(" )\n");
		}
		if(!CommonUtils.isNullString(endDate)) {
			sql.append("            and (tcrs.year || tcrs.month || tcrs.day) <= ").append(endDate).append("\n") ;
		}
		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}

}
