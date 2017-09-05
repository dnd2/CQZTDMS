package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 备件供应商维护dao
 * @author fanzhineng
 */
@SuppressWarnings("rawtypes")
public class PartAndVenderDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PartAndVenderDao.class);
	private static final PartAndVenderDao dao = new PartAndVenderDao();
	public static final PartAndVenderDao getInstance() {
        return dao;
	}
	public PartAndVenderDao(){
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/*========================================================*/
	/**
	 * 验证备件和供应商行关系是否存在
	 * @param subCell
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validatePartAndVenderIsCz(String dealerId, String partId) throws Exception{
		List<Object> param = new ArrayList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPV.SV_ID\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_VENDER TPV\n");
		sbSql.append("WHERE\n");
		sbSql.append("  TPV.VENDER_ID = ?\n");
		sbSql.append("  AND TPV.PART_ID = ?\n"); 
		param.add(dealerId);
		param.add(partId);
		Map<String, Object> map = pageQueryMap(sbSql.toString(), param, getFunName());
		return map;
	}
	
	/**
	 * 查询备件与公司关系
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartAndVenderMain(RequestWrapper request, Integer pageSize, Integer curPage) throws Exception{
		String partOldcode = request.getParamValue("partOldcode");
		String partCode = request.getParamValue("partCode");
		String partCname = request.getParamValue("partCname");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName = request.getParamValue("dealerName");
		String state = request.getParamValue("state");
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPV.SV_ID,\n");
		sbSql.append("  TPD.PART_ID,\n");
		sbSql.append("  TPD.PART_OLDCODE,\n");
		sbSql.append("  TPD.PART_CODE,\n");
		sbSql.append("  TPD.PART_CNAME,\n");
		sbSql.append("  TPD.UNIT,\n");
		sbSql.append("  TD.VENDER_ID AS DEALER_ID,\n");
		sbSql.append("  TD.VENDER_CODE AS DEALER_CODE,\n");
		sbSql.append("  TD.VENDER_NAME AS DEALER_NAME,\n");
		sbSql.append("  TPV.STATE,\n");
		sbSql.append("  TPV.CREATE_DATE,\n");
		sbSql.append("  TPV.MODIFY_DATE,\n");
		sbSql.append("  TPV.IS_DEFULT,\n");
		sbSql.append("  TPV.REMARK,\n");
		sbSql.append("  (SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TPV.MODIFY_USER) AS MODIFY_USER\n");
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_VENDER TPV,\n");
		sbSql.append("  TT_PART_DEFINE TPD,\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TD\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  TPD.PART_ID = TPV.PART_ID\n");
		sbSql.append("  AND TD.VENDER_ID = TPV.VENDER_ID"); 

		if(StringUtil.notNull(partOldcode)){
			sbSql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			param.add("%"+partOldcode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCode)){
			sbSql.append("  AND TPD.PART_CODE LIKE ?\n");
			param.add("%"+partCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCname)){
			sbSql.append("  AND TPD.PART_CNAME LIKE ?\n");
			param.add("%"+partCname+"%");
		}
		if(StringUtil.notNull(dealerCode)){
			sbSql.append("  AND TD.VENDER_CODE LIKE ?\n");
			param.add("%"+dealerCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("  AND TD.VENDER_NAME LIKE ?\n");
			param.add("%"+dealerName+"%");
		}
		if(StringUtil.notNull(state)){
			sbSql.append("  AND TPV.STATE = ?\n");
			param.add(state);
		}
		
		sbSql.append("  ORDER BY TD.VENDER_CODE DESC\n"); 

		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 获取备件信息
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartInfos(RequestWrapper request, Integer pageSize, Integer curPage)throws Exception {
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPD.PART_ID,\n");
		sbSql.append("  TPD.PART_OLDCODE,\n");
		sbSql.append("  TPD.PART_CNAME,\n");
		sbSql.append("  TPD.PART_CODE,\n");
		sbSql.append("  TPD.PART_ENAME\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_DEFINE TPD\n");
		sbSql.append("WHERE\n");
		sbSql.append("  TPD.STATUS = 1\n");
		sbSql.append("  AND TPD.STATE = '"+Constant.STATUS_ENABLE+"'"); 
		if(StringUtil.notNull(PART_OLDCODE)){
			sbSql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			param.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sbSql.append("  AND TPD.PART_CNAME LIKE ?\n");
			param.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sbSql.append("  AND TPD.PART_CODE LIKE ?"); 
			param.add("%"+PART_CODE.toUpperCase()+"%");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 获取关系根据id
	 * @param svId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, String> getInfoBySvId(String svId) throws Exception{
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
//		sbSql.append("  TPV.SV_ID,td.is_advance,\n");
		sbSql.append("  TPV.SV_ID,\n");
		sbSql.append("  TPD.PART_ID,\n");
		sbSql.append("  TPD.PART_OLDCODE,\n");
		sbSql.append("  TPD.PART_CODE,\n");
		sbSql.append("  TPD.PART_CNAME,\n");
		sbSql.append("  TPD.UNIT,\n");
		sbSql.append("  TD.VENDER_ID AS DEALER_ID,\n");
		sbSql.append("  TD.VENDER_CODE AS DEALER_CODE,\n");
		sbSql.append("  TD.VENDER_NAME AS DEALER_NAME,\n");
		sbSql.append("  TPV.STATE,\n");
		sbSql.append("  TPV.IS_DEFULT,\n");
		sbSql.append("  TPV.CREATE_DATE,\n");
		sbSql.append("  TPV.MODIFY_DATE,\n");
		sbSql.append("  (SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TPV.MODIFY_USER) AS MODIFY_USER\n");
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_VENDER TPV,\n");
		sbSql.append("  TT_PART_DEFINE TPD,\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TD\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  TPD.PART_ID = TPV.PART_ID\n");
		sbSql.append("  AND TD.VENDER_ID = TPV.VENDER_ID"); 
		
		sbSql.append("  AND TPV.SV_ID = ?\n");
		param.add(svId);
		
		return pageQueryMap(sbSql.toString(), param, getFunName());
	}
	
	/**
	 * 获取供应商信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVenderInfos() throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPVD.VENDER_ID,\n");
		sbSql.append("  TPVD.VENDER_CODE,\n");
		sbSql.append("  TPVD.VENDER_NAME\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TPVD\n");
		sbSql.append("WHERE\n");
		sbSql.append("  TPVD.STATUS = 1\n");
		sbSql.append("  AND TPVD.STATE = '"+Constant.STATUS_ENABLE+"'");
		List<Map<String, Object>>  list = pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 获取供应商信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getVenderInfos(String venderCode) throws Exception{
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPVD.VENDER_ID,\n");
		sbSql.append("  TPVD.VENDER_CODE,\n");
		sbSql.append("  TPVD.VENDER_NAME\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TPVD\n");
		sbSql.append("WHERE\n");
		sbSql.append("  TPVD.STATUS = 1\n");
		sbSql.append("  AND TPVD.STATE = '"+Constant.STATUS_ENABLE+"'"); 
		if(StringUtil.notNull(venderCode)){
			sbSql.append("  AND TPVD.VENDER_CODE = ?");
			param.add(venderCode.toUpperCase());
		}
		Map<String, Object> map = pageQueryMap(sbSql.toString(), param, getFunName());
		return map;
	}
	/**
	 * 条件查询供应商信息
	 * @param request
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getVenderInfos(RequestWrapper request, Integer pageSize, Integer curPage) {
		String venderCode = request.getParamValue("VENDER_CODE");
		String venderName = request.getParamValue("VENDER_NAME");
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPVD.VENDER_ID,\n");
		sbSql.append("  TPVD.VENDER_CODE,\n");
		sbSql.append("  TPVD.VENDER_NAME\n");
		sbSql.append("FROM\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TPVD\n");
		sbSql.append("WHERE\n");
		sbSql.append("  TPVD.STATUS = 1\n");
		sbSql.append("  AND TPVD.STATE = '"+Constant.STATUS_ENABLE+"'\n");
		if(StringUtil.notNull(venderCode)){
			sbSql.append("  AND TPVD.VENDER_CODE LIKE ?\n");
			param.add("%"+venderCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(venderName)){
			sbSql.append("  AND TPVD.VENDER_NAME LIKE ?\n");
			param.add("%"+venderName+"%");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 要导出的数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExportByCondition(RequestWrapper request) throws Exception{
		String partOldcode = request.getParamValue("partOldcode");
		String partCode = request.getParamValue("partCode");
		String partCname = request.getParamValue("partCname");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName = request.getParamValue("dealerName");
		String state = request.getParamValue("state");
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n");
		sbSql.append("  TPV.SV_ID,\n");
		sbSql.append("  TPD.PART_ID,\n");
		sbSql.append("  TPD.PART_OLDCODE,\n");
		sbSql.append("  TPD.PART_CODE,\n");
		sbSql.append("  TPD.PART_CNAME,\n");
		sbSql.append("  TPD.UNIT,\n");
		sbSql.append("  TD.VENDER_ID AS DEALER_ID,\n");
		sbSql.append("  TD.VENDER_CODE AS DEALER_CODE,\n");
		sbSql.append("  TD.VENDER_NAME AS DEALER_NAME,\n");
		sbSql.append("  TPV.STATE,\n");
		sbSql.append("  TPV.REMARK,\n");
		sbSql.append("  TPV.CREATE_DATE,\n");
		sbSql.append("  TPV.MODIFY_DATE,\n");
		sbSql.append("  (SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TPV.MODIFY_USER) AS MODIFY_USER\n");
		sbSql.append(" FROM\n");
		sbSql.append("  TT_PART_VENDER TPV,\n");
		sbSql.append("  TT_PART_DEFINE TPD,\n");
		sbSql.append("  TT_PART_VENDER_DEFINE TD\n");
		sbSql.append(" WHERE\n");
		sbSql.append("  TPD.PART_ID = TPV.PART_ID\n");
		sbSql.append("  AND TD.VENDER_ID = TPV.VENDER_ID");

		if(StringUtil.notNull(partOldcode)){
			sbSql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			param.add("%"+partOldcode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCode)){
			sbSql.append("  AND TPD.PART_CODE LIKE ?\n");
			param.add("%"+partCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(partCname)){
			sbSql.append("  AND TPD.PART_CNAME LIKE ?\n");
			param.add("%"+partCname+"%");
		}
		if(StringUtil.notNull(dealerCode)){
			sbSql.append("  AND TD.VENDER_CODE LIKE ?\n");
			param.add("%"+dealerCode.toUpperCase()+"%");
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("  AND TD.VENDER_NAME LIKE ?\n");
			param.add("%"+dealerName+"%");
		}
		if(StringUtil.notNull(state)){
			sbSql.append("  AND TPV.STATE = ?\n");
			param.add(state);
		}
		List<Map<String, Object>> ps = pageQuery(sbSql.toString(), param, getFunName());
		return ps;
	}
}
