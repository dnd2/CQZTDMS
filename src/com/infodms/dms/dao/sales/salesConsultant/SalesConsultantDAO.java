package com.infodms.dms.dao.sales.salesConsultant;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesConsultantChkPO;
import com.infodms.dms.po.TtSalesConsultantPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SalesConsultantDAO extends BaseDao {
	public Logger logger = Logger.getLogger(SalesConsultantDAO.class);
	
	private static final SalesConsultantDAO dao = new SalesConsultantDAO();

	public static final SalesConsultantDAO getInstance() {
		return dao;
	}
	
	/**
	 * 销售顾问新增
	 * @throws ParseException 
	 */
	public void salesConsultantInsert(Map<String, String> map) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		
		String dealerId = map.get("dealerId") ;
		String salesConName = map.get("salesConName") ;
		String sex = map.get("sex") ;
		String bornYear = map.get("bornYear") ;
		String academicRecords = map.get("academicRecords") ;
		String tradeYear = map.get("tradeYear") ;
		String chanaTradeYear = map.get("chanaTradeYear") ;
		String tel = map.get("tel") ;
		String reason = map.get("reason") ;
		String userId = map.get("userId") ;
		String status = map.get("status") ;
		String identityNumber = map.get("identityNumber");
		
		TtSalesConsultantPO tsc = new TtSalesConsultantPO() ;
		
		String headId = SequenceManager.getSequence("") ;
		
		tsc.setId(Long.parseLong(headId)) ;
		tsc.setName(salesConName) ;
		tsc.setSex(Integer.parseInt(sex)) ;
		tsc.setBornYear(sdf.parse(bornYear)) ;
		tsc.setAcademicRecords(Integer.parseInt(academicRecords)) ;
		tsc.setTradeYear(sdf.parse(tradeYear)) ;
		//tsc.setChanaTradeYear(sdf.parse(chanaTradeYear)) ;
		tsc.setTel(tel) ;
		tsc.setReason(reason) ;
		tsc.setDealerId(Long.parseLong(dealerId)) ;
		tsc.setCreateBy(Long.parseLong(userId)) ;
		tsc.setCreateDate(new Date(System.currentTimeMillis())) ;
		tsc.setStatus(Integer.parseInt(status)) ;
		tsc.setIdentityNumber(identityNumber);
		
		dao.insert(tsc) ;
	}
	
	/**
	 * 销售顾问修改
	 * @param map
	 * @throws ParseException 
	 */
	public void salesConsultantUpdate(Map<String, String> map) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		
		String headId = map.get("headId") ;
		String salesConName = map.get("salesConName") ;
		String sex = map.get("sex") ;
		String bornYear = map.get("bornYear") ;
		String academicRecords = map.get("academicRecords") ;
		String tradeYear = map.get("tradeYear") ;
		String chanaTradeYear = map.get("chanaTradeYear") ;
		String tel = map.get("tel") ;
		String reason = map.get("reason") ;
		String userId = map.get("userId") ;
		String status = map.get("status") ;
		String dealerId = map.get("dealerId") ;
		String identityNumber = map.get("identityNumber");
		
		TtSalesConsultantPO oldTsc = new TtSalesConsultantPO() ;
		oldTsc.setId(Long.parseLong(headId)) ;
		TtSalesConsultantPO tsc = new TtSalesConsultantPO() ;
		
		tsc.setName(salesConName) ;
		
		if(!CommonUtils.isNullString(dealerId)) {
			tsc.setDealerId(Long.parseLong(dealerId)) ;
		}
		
		if(!CommonUtils.isNullString(sex)) {
			tsc.setSex(Integer.parseInt(sex)) ;
		}
		
		if(!CommonUtils.isNullString(bornYear)) {
			tsc.setBornYear(sdf.parse(bornYear)) ;
		}
		
		if(!CommonUtils.isNullString(academicRecords)) {
			tsc.setAcademicRecords(Integer.parseInt(academicRecords)) ;
		}
		
		if(!CommonUtils.isNullString(tradeYear)) {
			tsc.setTradeYear(sdf.parse(tradeYear)) ;
		}
		
//		if(!CommonUtils.isNullString(chanaTradeYear)) {
//			tsc.setChanaTradeYear(sdf.parse(chanaTradeYear)) ;
//		}
		
		if(!CommonUtils.isNullString(userId)) {
			tsc.setUpdateBy(Long.parseLong(userId)) ;
		}
		
		if(!CommonUtils.isNullString(status)) {
			tsc.setStatus(Integer.parseInt(status)) ;
		}
		
		if(!CommonUtils.isNullString(identityNumber)) {
			tsc.setIdentityNumber(identityNumber) ;
		}
		
		tsc.setTel(tel) ;
		tsc.setReason(reason) ;
		tsc.setUpdateDate(new Date(System.currentTimeMillis())) ;
		
		dao.update(oldTsc, tsc) ;
	}
	
	/**
	 * 销售顾问查询
	 * @param map
	 */
	public PageResult<Map<String, Object>> salesConsultantQuery(Map<String, String> map,int pageSize,int curPage,AclUserBean logonUser) {
//		String areaIds = map.get("areaIds") ;
		String dealerId = map.get("dealerId") ;
		String orgCode = map.get("orgCode") ;
		String org_id = map.get("orgId") ;
		String salesConName = map.get("salesConName") ;
		String status = map.get("status") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tsc.id,\n");
		sql.append("       tsc.create_date,\n");  
		sql.append("       tsc.name,\n");  
		sql.append("       tsc.sex,\n");  
		sql.append("       tsc.born_year,\n");  
		sql.append("       tsc.academic_records,\n");  
		sql.append("       tsc.trade_year,\n");  
		sql.append("       tsc.chana_trade_year,\n");  
		sql.append("       tsc.tel,\n");  
		sql.append("       tsc.reason,\n");  
		sql.append("       tsc.status,\n");  
		sql.append("       VOD.dealer_name,\n");
//		sql.append("       vod.dealer_name,\n");  
		sql.append("       VOD.org_name root_org_name,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy')) - 1\n");  
		sql.append("       end) age,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) tradeYear,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) chanaTradeYear\n");  
		sql.append("  from tt_sales_consultant tsc,VW_ORG_DEALER VOD \n");  
		sql.append(" where tsc.dealer_id = VOD.dealer_id\n"); 
		List par=new ArrayList();
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerId, par,"tsc", "dealer_id"));
		}
		
		if(!CommonUtils.isNullString(salesConName)) {
			sql.append(" and tsc.name like ?\n"); 
			par.add("%"+salesConName.trim()+"%");
		}
		
		if(!CommonUtils.isNullString(status)) {
			//sql.append(" and tsc.status in (").append(status).append(")\n"); 
			sql.append(Utility.getConSqlByParamForEqual(status, par,"tsc", "status"));
		}
		if(org_id !=null && !"".equals(org_id)){
			sql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("TSC.DEALER_ID", logonUser,par));
		}
		if(!CommonUtils.isNullString(orgCode)) {
			//sql.append(" and vod.org_code in("")");
			sql.append(Utility.getConSqlByParamForEqual(orgCode, par,"vod", "org_code"));
		}
		
//		if(!CommonUtils.isNullString(areaIds)) {
//			sql.append(" and tdba.area_id in (").append(areaIds).append(")\n");  
//		}
		sql.append(" order by tsc.create_date desc\n");

		return dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage) ;
	}
	
	/**
	 * 销售顾问下载
	 * @param map
	 */
	public List<Map<String, Object>> salesConsultantDownload(Map<String, String> map,AclUserBean logonUser) {
		String areaIds = map.get("areaIds") ;
		String dealerId = map.get("dealerId") ;
		String orgId = map.get("orgId") ;
		String salesConName = map.get("salesConName") ;
		String status = map.get("status") ;
		String orgCode = map.get("orgCode");
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tsc.id,\n");
		sql.append("                tsc.create_date,\n");  
		sql.append("                tsc.name,\n");  
		sql.append("                tsc.sex,\n");  
		sql.append("                tsc.IDENTITY_NUMBER,\n"); 
		sql.append("                tcc1.code_desc sex_desc,\n");  
		sql.append("                tsc.born_year,\n");  
		sql.append("                tsc.academic_records,\n");  
		sql.append("                tcc2.code_desc ar_desc,\n");  
		sql.append("                tsc.trade_year,\n");  
		sql.append("                tsc.chana_trade_year,\n");  
		sql.append("                tsc.tel,\n");  
		sql.append("                tsc.reason,\n");  
		sql.append("                tsc.status,\n");  
		sql.append("                tcc3.code_desc status_desc,\n");  
		sql.append("                td.dealer_name,\n");  
		sql.append("                td.org_name root_org_name,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.born_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.born_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.born_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.born_year, 'yyyy')) - 1\n");  
		sql.append("                end) age,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.trade_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.trade_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.trade_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.trade_year, 'yyyy')) - 1\n");  
		sql.append("                end) tradeYear,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.chana_trade_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.chana_trade_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.chana_trade_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.chana_trade_year, 'yyyy')) - 1\n");  
		sql.append("                end) chanaTradeYear\n");  
		sql.append("  from tt_sales_consultant     tsc,VW_ORG_DEALER td, \n");  
//		sql.append("       vw_org_dealer           vod,\n");  
//		sql.append("       tm_dealer_business_area tdba,\n");  
		sql.append("       tc_code                 tcc1,\n");  
		sql.append("       tc_code                 tcc2,\n");  
		sql.append("       tc_code                 tcc3\n");  
		sql.append(" where TSC.DEALER_ID=td.DEALER_ID \n");  
		sql.append("   and tsc.sex = tcc1.code_id\n");  
		sql.append("   and tsc.academic_records = tcc2.code_id\n");  
		sql.append("   and tsc.status = tcc3.code_id\n");
		
		List par=new ArrayList();
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerId, par,"tsc", "dealer_id"));
			
		}
		
		if(!CommonUtils.isNullString(salesConName)) {
			sql.append(" and tsc.name like ?\n"); 
			par.add("%"+salesConName.trim()+"%");
		}
		
		if(!CommonUtils.isNullString(status)) {
			sql.append(Utility.getConSqlByParamForEqual(status, par,"tsc", "status"));
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			//sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TSC.DEALER_ID", logonUser));
			sql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByPar("TSC.DEALER_ID", logonUser,par));
		}
		
//		if(!CommonUtils.isNullString(areaIds)) {
//			sql.append(" and tdba.area_id in (").append(areaIds).append(")\n");  
//		}
		if(!CommonUtils.isNullString(orgCode)) {
			sql.append(Utility.getConSqlByParamForEqual(orgCode, par,"td", "org_code"));
		}
		sql.append(" order by tsc.create_date desc\n");

		return dao.pageQuery(sql.toString(), par, dao.getFunName()) ;
	}
	
	/**
	 * 销售顾问明细查询
	 * @param map
	 */
	public Map<String, Object> salesConsultantDtlQuery(Map<String, String> map) {
		List<Map<String, Object>> scList = dao.salesConsultantListQuery(map) ;
		
		Map<String, Object> scMap = new HashMap<String, Object>() ;
		
		if(!CommonUtils.isNullList(scList)) {
			scMap = scList.get(0) ;
		}
		
		return scMap ;
	}
	
	public List<Map<String, Object>> getSalesConsultantList(String dealerId){
		String sql = "SELECT ID,NAME FROM TT_SALES_CONSULTANT WHERE DEALER_ID = "+dealerId+" AND STATUS = "+Constant.SALES_CONSULTANT_STATUS_PASS;
		return dao.pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getSalesConsultantById(Long id){
		String sql = "SELECT ID,NAME FROM TT_SALES_CONSULTANT WHERE ID = "+id+" AND STATUS = "+Constant.SALES_CONSULTANT_STATUS_PASS;
		return dao.pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 销售顾问查询
	 */
	public List<Map<String, Object>> salesConsultantListQuery(Map<String, String> map) {
		String headId = map.get("headId") ;
		String dealerId = map.get("dealerId") ;
		String status = map.get("status") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tsc.id,\n");
		sql.append("       tsc.name,\n");  
		sql.append("       tsc.sex,\n");  
		sql.append("       tsc.Identity_Number,\n");  
		sql.append("       to_char(tsc.born_year, 'yyyy-mm-dd') born_year,\n");  
		sql.append("       tsc.academic_records,\n");  
		sql.append("       to_char(tsc.trade_year, 'yyyy-mm-dd') trade_year,\n");  
		sql.append("       to_char(tsc.chana_trade_year, 'yyyy-mm-dd') chana_trade_year,\n");  
		sql.append("       tsc.tel,\n");  
		sql.append("       tsc.reason,\n");  
		sql.append("       tsc.status,\n");  
		sql.append("       vod.dealer_name,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy')) - 1\n");  
		sql.append("       end) age,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) tradeYear,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) chanaTradeYear\n");  
//		sql.append("  from tt_sales_consultant tsc, vw_org_dealer vod, tm_dealer_business_area tdba\n");  
		sql.append("       from tt_sales_consultant tsc, vw_org_dealer vod \n");
		sql.append(" where tsc.dealer_id = vod.dealer_id\n");  
//		sql.append(" and vod.dealer_id = tdba.dealer_id\n"); 	
		if(!CommonUtils.isNullString(headId)) {
			sql.append(" and tsc.id = ").append(headId).append("\n"); 
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and tsc.dealer_id = ").append(dealerId).append("\n"); 
		}
		
		if(!CommonUtils.isNullString(status)) {
			sql.append(" and tsc.status = ").append(status).append("\n"); 
		}
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	/**
	 * 销售顾问审核日志新增
	 * @param map
	 */
	public void salesConsultantChkInsert(Map<String, String> map) {
		String headId = map.get("headId") ;
		String status = map.get("status") ;
		String depict = map.get("depict") ;
		String userId = map.get("userId") ;
		TtSalesConsultantChkPO tscc = new TtSalesConsultantChkPO() ;
		
		String chkId = SequenceManager.getSequence("") ;
		tscc.setId(Long.parseLong(chkId)) ;
		tscc.setHeadId(Long.parseLong(headId)) ;
		tscc.setStatus(Integer.parseInt(status)) ;
		tscc.setDepict(depict) ;
		tscc.setCreateBy(Long.parseLong(userId)) ;
		tscc.setCreateDate(new Date(System.currentTimeMillis())) ;
		
		dao.insert(tscc) ;
	}
	
	/**
	 * 销售顾问审核日志查询
	 * @param map
	 */
	public List<Map<String, Object>> salesConsultantChkQuery(Map<String, String> map) {
		String headId = map.get("headId") ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tscc.id,\n");
		sql.append("       tscc.status,\n");  
		sql.append("       tscc.depict,\n");  
		sql.append("       tcu.name,\n");  
		sql.append("       to_char(tscc.create_date, 'yyyy-mm-dd') chkDate\n");  
		sql.append("  from tt_sales_consultant_chk tscc, tc_user tcu\n");  
		sql.append(" where tscc.create_by = tcu.user_id\n");  
		sql.append("   and tscc.head_id = ").append(headId).append("\n");  
		sql.append("   order by tscc.create_date desc\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 销售顾问查询
	 * @param map
	 */
	public PageResult<Map<String, Object>> salesConsultantQuery(Map<String, String> map,int pageSize,int curPage) {
		String areaIds = map.get("areaIds") ;
		String dealerId = map.get("dealerId") ;
		String orgId = map.get("orgId") ;
		String salesConName = map.get("salesConName") ;
		String status = map.get("status") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tsc.id,\n");
		sql.append("       tsc.create_date,\n");  
		sql.append("       tsc.name,\n");  
		sql.append("       tsc.sex,\n");  
		sql.append("       tsc.born_year,\n");  
		sql.append("       tsc.academic_records,\n");  
		sql.append("       tsc.trade_year,\n");  
		sql.append("       tsc.chana_trade_year,\n");  
		sql.append("       tsc.tel,\n");  
		sql.append("       tsc.reason,\n");  
		sql.append("       tsc.status,\n");  
		sql.append("       vod.dealer_name,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.born_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.born_year, 'yyyy')) - 1\n");  
		sql.append("       end) age,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) tradeYear,\n");  
		sql.append("       (case\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') <= to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy'))\n");  
		sql.append("         when to_char(tsc.chana_trade_year, 'mm-dd') > to_char(sysdate, 'mm-dd') then\n");  
		sql.append("          to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("          to_number(to_char(tsc.chana_trade_year, 'yyyy')) - 1\n");  
		sql.append("       end) chanaTradeYear\n");  
		sql.append("  from tt_sales_consultant tsc, vw_org_dealer vod, tm_dealer_business_area tdba\n");  
		sql.append(" where tsc.dealer_id = vod.dealer_id\n"); 
		sql.append(" and vod.dealer_id = tdba.dealer_id\n"); 
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and tsc.dealer_id in (").append(dealerId).append(")\n"); 
		}
		
		if(!CommonUtils.isNullString(salesConName)) {
			sql.append(" and tsc.name like '%").append(salesConName).append("%'\n"); 
		}
		
		if(!CommonUtils.isNullString(status)) {
			sql.append(" and tsc.status in (").append(status).append(")\n"); 
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and vod.root_org_id = ").append(orgId).append("\n"); 
		}
		
		if(!CommonUtils.isNullString(areaIds)) {
			sql.append(" and tdba.area_id in (").append(areaIds).append(")\n");  
		}
		sql.append(" order by tsc.create_date desc\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
	}
	
	/**
	 * 销售顾问下载
	 * @param map
	 */
	public List<Map<String, Object>> salesConsultantDownload(Map<String, String> map) {
		String areaIds = map.get("areaIds") ;
		String dealerId = map.get("dealerId") ;
		String orgId = map.get("orgId") ;
		String salesConName = map.get("salesConName") ;
		String status = map.get("status") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tsc.id,\n");
		sql.append("                tsc.create_date,\n");  
		sql.append("                tsc.name,\n");  
		sql.append("                tsc.sex,\n");  
		sql.append("                tsc.IDENTITY_NUMBER,\n"); 
		sql.append("                tcc1.code_desc sex_desc,\n");  
		sql.append("                tsc.born_year,\n");  
		sql.append("                tsc.academic_records,\n");  
		sql.append("                tcc2.code_desc ar_desc,\n");  
		sql.append("                tsc.trade_year,\n");  
		sql.append("                tsc.chana_trade_year,\n");  
		sql.append("                tsc.tel,\n");  
		sql.append("                tsc.reason,\n");  
		sql.append("                tsc.status,\n");  
		sql.append("                tcc3.code_desc status_desc,\n");  
		sql.append("                vod.dealer_name,\n");  
		sql.append("                vod.root_org_name,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.born_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.born_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.born_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.born_year, 'yyyy')) - 1\n");  
		sql.append("                end) age,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.trade_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.trade_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.trade_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.trade_year, 'yyyy')) - 1\n");  
		sql.append("                end) tradeYear,\n");  
		sql.append("                (case\n");  
		sql.append("                  when to_char(tsc.chana_trade_year, 'mm-dd') <=\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.chana_trade_year, 'yyyy'))\n");  
		sql.append("                  when to_char(tsc.chana_trade_year, 'mm-dd') >\n");  
		sql.append("                       to_char(sysdate, 'mm-dd') then\n");  
		sql.append("                   to_number(to_char(sysdate, 'yyyy')) -\n");  
		sql.append("                   to_number(to_char(tsc.chana_trade_year, 'yyyy')) - 1\n");  
		sql.append("                end) chanaTradeYear\n");  
		sql.append("  from tt_sales_consultant     tsc,\n");  
		sql.append("       vw_org_dealer           vod,\n");  
		sql.append("       tm_dealer_business_area tdba,\n");  
		sql.append("       tc_code                 tcc1,\n");  
		sql.append("       tc_code                 tcc2,\n");  
		sql.append("       tc_code                 tcc3\n");  
		sql.append(" where tsc.dealer_id = vod.dealer_id\n");  
		sql.append("   and vod.dealer_id = tdba.dealer_id\n");  
		sql.append("   and tsc.sex = tcc1.code_id\n");  
		sql.append("   and tsc.academic_records = tcc2.code_id\n");  
		sql.append("   and tsc.status = tcc3.code_id\n");

		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and tsc.dealer_id in (").append(dealerId).append(")\n"); 
		}
		
		if(!CommonUtils.isNullString(salesConName)) {
			sql.append(" and tsc.name like '%").append(salesConName).append("%'\n"); 
		}
		
		if(!CommonUtils.isNullString(status)) {
			sql.append(" and tsc.status in (").append(status).append(")\n"); 
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and vod.root_org_id = ").append(orgId).append("\n"); 
		}
		
		if(!CommonUtils.isNullString(areaIds)) {
			sql.append(" and tdba.area_id in (").append(areaIds).append(")\n");  
		}
		sql.append(" order by tsc.create_date desc\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
}
