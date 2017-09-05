package com.infodms.dms.dao.zotye.base;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcBusinessParametersPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtSalesConsultantPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class BusinessParametersDAO extends BaseDao {
		public Logger logger = Logger.getLogger(BusinessParametersDAO.class);
		
		private static final BusinessParametersDAO dao = new BusinessParametersDAO();

		public static final BusinessParametersDAO getInstance() {
			return dao;
		}
		private BusinessParametersDAO() {}
		
		public void insert(Map<String, String> map) throws ParseException {
			String type = map.get("type") ;
			String code = map.get("code") ;
			String name = map.get("name") ;
			String value = map.get("value") ;
			String remark = map.get("remark") ;
			String sort = map.get("sort") ;
			String status = map.get("status") ;
			String isShow = map.get("isShow") ;
			String isValue = map.get("isValue") ;
			String userId = map.get("userId") ;
			
			TcBusinessParametersPO tq = new TcBusinessParametersPO() ;
			
			String id = SequenceManager.getSequence("") ;
			
			tq.setId(Long.parseLong(id)) ;
			tq.setCode(Long.parseLong(code));
			tq.setName(name);
			tq.setType(Integer.parseInt(type));
			tq.setValue(value);
			tq.setRemark(remark);
			tq.setSort(Long.parseLong(sort));
			tq.setStatus(Long.parseLong(status));
			tq.setIsShow(Long.parseLong(isShow));
			tq.setIsValue(Long.parseLong(isValue));
			tq.setCreateDate(new Date()) ;
			tq.setCreateBy(Long.parseLong(userId)) ;
			
			dao.insert(tq) ;
		}
		
		public void update(Map<String, String> map) throws ParseException {
			String id = map.get("id") ;
			String value = map.get("value") ;
			String remark = map.get("remark") ;
			String sort = map.get("sort") ;
			String status = map.get("status") ;
			String isShow = map.get("isShow") ;
			String isValue = map.get("isValue") ;
			String userId = map.get("userId") ;
			

			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("UPDATE TC_BUSINESS_PARAMETERS TBP\n") ;
			sql.append("   SET TBP.UPDATE_DATE = SYSDATE\n") ;
			
			if(!CommonUtils.isNullString(userId))
				sql.append("       ,TBP.UPDATE_BY   = ").append(id).append("\n") ;
			
			if(!CommonUtils.isNullString(value))
				sql.append("       ,TBP.VALUE       = '").append(value).append("'\n") ;
			
			if(!CommonUtils.isNullString(remark))
				sql.append("       ,TBP.REMARK      = '").append(remark).append("'\n") ;
			
			if(!CommonUtils.isNullString(sort))
				sql.append("       ,TBP.SORT        = ").append(sort).append("\n") ;
			
			if(!CommonUtils.isNullString(status))
				sql.append("       ,TBP.STATUS      = ").append(status).append("\n") ;
			
			if(!CommonUtils.isNullString(isShow))
				sql.append("       ,TBP.IS_SHOW     = ").append(isShow).append("\n") ;
			
			if(!CommonUtils.isNullString(isValue))
				sql.append("       ,TBP.IS_VALUE    = ").append(isValue).append("\n") ;
			
			sql.append(" WHERE TBP.ID = ").append(id).append("\n") ;

			dao.update(sql.toString(), null) ;
		}
		
		public PageResult<Map<String, Object>> query(Map<String, String> map,int pageSize,int curPage) {
			String moduleCode = map.get("moduleCode") ;
			String businessCode = map.get("businessCode") ;
			String type = map.get("type") ;
			String isShow = map.get("isShow") ;
			
			StringBuffer sql = new StringBuffer("\n") ;
			
			sql.append("select tbp.id,\n") ;
			sql.append("       tbp.code,\n") ;
			sql.append("       tbp.name,\n") ;
			sql.append("       tbp.value,\n") ;
			sql.append("       tbp.remark,\n") ;
			sql.append("       tbp.sort,\n") ;
			sql.append("       tbp.status,\n") ;
			sql.append("       tbp.type,\n") ;
			sql.append("       tbp.is_show,\n") ;
			sql.append("       tbt.name   type_name,\n") ;
			sql.append("       tbt.is_para_modify,\n") ;
			sql.append("       tbt.is_para_add,\n") ;
			sql.append("       tbpa.name  module_name,\n") ;
			sql.append("       tbpb.name  business_name\n") ;
			sql.append("  from TC_BUSINESS_PARAMETERS      tbp,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS_type tbt,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS      tbpa,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS      tbpb\n") ;
			sql.append(" where tbp.type = tbt.code\n") ;
			sql.append("   and tbt.module = tbpa.code\n") ;
			sql.append("   and tbt.business = tbpb.code\n") ;
			
			if(!CommonUtils.isNullString(moduleCode)) 
				sql.append("   and tbt.module = ").append(moduleCode).append("\n") ;
				
			if(!CommonUtils.isNullString(businessCode)) 
				sql.append("   and tbt.business = ").append(businessCode).append("\n") ;
			
			if(!CommonUtils.isNullString(type)) 
				sql.append("   and tbp.type = ").append(type).append("\n") ;
			
			if(!CommonUtils.isNullString(isShow)) 
				sql.append("   and tbp.is_show = ").append(isShow).append("\n") ;
			
			sql.append(" order by tbt.sort, tbp.sort\n") ;
			
			return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
		}
		
		public List<Map<String, Object>> query(Map<String, String> map) {
			String moduleCode = map.get("moduleCode") ;
			String businessCode = map.get("businessCode") ;
			String type = map.get("type") ;
			String isShow = map.get("isShow") ;
			String id = map.get("id") ;
			
			StringBuffer sql = new StringBuffer("\n") ;
			
			sql.append("select tbp.id,\n") ;
			sql.append("       tbp.code,\n") ;
			sql.append("       tbp.name,\n") ;
			sql.append("       tbp.value,\n") ;
			sql.append("       tbp.remark,\n") ;
			sql.append("       tbp.sort,\n") ;
			sql.append("       tbp.status,\n") ;
			sql.append("       tbp.type,\n") ;
			sql.append("       tbp.is_show,\n") ;
			sql.append("       tbt.name   type_name,\n") ;
			sql.append("       tbpa.name  module_name,\n") ;
			sql.append("       tbpb.name  business_name\n") ;
			sql.append("  from TC_BUSINESS_PARAMETERS      tbp,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS_type tbt,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS      tbpa,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS      tbpb\n") ;
			sql.append(" where tbp.type = tbt.code\n") ;
			sql.append("   and tbt.module = tbpa.code\n") ;
			sql.append("   and tbt.business = tbpb.code\n") ;
			
			if(!CommonUtils.isNullString(moduleCode)) 
				sql.append("   and tbt.module = ").append(moduleCode).append("\n") ;
				
			if(!CommonUtils.isNullString(businessCode)) 
				sql.append("   and tbt.business = ").append(businessCode).append("\n") ;
			
			if(!CommonUtils.isNullString(type)) 
				sql.append("   and tbp.type = ").append(type).append("\n") ;
			
			if(!CommonUtils.isNullString(isShow)) 
				sql.append("   and tbp.is_show = ").append(isShow).append("\n") ;
			
			if(!CommonUtils.isNullString(id)) 
				sql.append("   and tbp.id = ").append(id).append("\n") ;
			
			sql.append(" order by tbt.sort, tbp.sort\n") ;
			
			return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		}
		
		public List<Map<String, Object>> businessParamtersQuery(Map<String, String> map) {
			String type = map.get("type") ;

			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("SELECT TBP.CODE, TBP.NAME\n") ;
			sql.append("  FROM TC_BUSINESS_PARAMETERS TBP\n") ;
			sql.append(" WHERE TBP.STATUS = ").append(Constant.STATUS_ENABLE).append("\n") ;
			
			if(!CommonUtils.isNullString(type)) 
				sql.append("   AND TBP.TYPE = ").append(type).append("\n") ;
			
			sql.append(" ORDER BY TBP.SORT\n") ;

			return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		}
		
		public List<Map<String, Object>> systemBusinessQeury(Map<String, String> map) {
			String moduleCode = map.get("moduleCode") ;

			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("SELECT TBP.CODE, TBP.NAME\n") ;
			sql.append("  FROM (SELECT TBT.BUSINESS\n") ;
			sql.append("          FROM TC_BUSINESS_PARAMETERS_TYPE TBT\n") ;
			
			if(!CommonUtils.isNullString(moduleCode)) 
				sql.append("         WHERE TBT.MODULE = ").append(moduleCode).append("\n") ;
			
			sql.append("         GROUP BY TBT.MODULE, TBT.BUSINESS) TB,\n") ;
			sql.append("       TC_BUSINESS_PARAMETERS TBP\n") ;
			sql.append(" WHERE TB.BUSINESS = TBP.CODE\n") ;
			sql.append(" ORDER BY TBP.SORT\n") ;

			return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		}
		
		public List<Map<String, Object>> systemBusinessTypeQeury(Map<String, String> map) {
			String businessCode = map.get("businessCode") ;

			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("SELECT TBT.CODE, TBT.NAME, TBT.IS_PARA_ADD\n") ;
			sql.append("  FROM TC_BUSINESS_PARAMETERS_TYPE TBT\n") ;
			sql.append(" WHERE TBT.STATUS = ").append(Constant.STATUS_ENABLE).append("\n") ;
			
			if(!CommonUtils.isNullString(businessCode)) 
				sql.append(" AND TBT.BUSINESS = ").append(businessCode).append("\n") ;

			return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		}
		
		public String businessParamtersCodeByType(Map<String, String> map) {
			String type = map.get("type") ;
			
			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("SELECT DECODE(MAX(TBP.CODE), NULL, '").append(type).append("' || '1001', MAX(TBP.CODE) + 1) CODE\n") ;
			sql.append("  FROM TC_BUSINESS_PARAMETERS TBP\n") ;
			sql.append(" WHERE TBP.TYPE = ").append(type).append("\n") ;
			
			Map<String, Object> maxMap = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
			
			return maxMap.get("CODE").toString() ;
		}
		
		@Override
		protected PO wrapperPO(ResultSet rs, int idx) {
			// TODO Auto-generated method stub
			return null;
		}
		
	
}
