package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrForeapprovalptPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: PartMaintainDAO 
* @Description: TODO(配件维护DAO) 
* @author wangchao 
* @date Jun 18, 2010 4:37:15 PM 
*
 */
public class PartMaintainDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(PartMaintainDAO.class);
    private static final PartMaintainDAO dao = null;
	
	public static final PartMaintainDAO getInstance() {
	   if(dao==null) return new PartMaintainDAO();
	   return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: claimPartWatchQuery 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param whereSql
	* @param @param params
	* @param @return
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String,Object>> claimPartWatchQuery(Long oemCompanyId , Map<String,String> map,int pageSize, int curPage, String whereSql,
			List<Object> params) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT B.WRGROUP_NAME,B.WRGROUP_CODE,A.PART_CODE,A.PART_NAME,A.ID  \n");
		sb.append(" FROM TT_AS_WR_FOREAPPROVALPT A,TT_AS_WR_MODEL_GROUP B \n ");
		sb.append(" WHERE A.MODEL_GROUP = B.WRGROUP_ID(+) \n ");

		if (Utility.testString(map.get("partCode"))) {
			sb.append(" and a.part_code like ? ");
			params.add("%"+map.get("partCode")+"%");
		}
		if (Utility.testString(map.get("partName"))) {
			sb.append(" and a.part_name like ? ");
			params.add("%"+map.get("partName")+"%");
		}
		if (Utility.testString(map.get("wrGroupId"))) {
			sb.append(" and a.MODEL_GROUP = ? ");
			params.add(map.get("wrGroupId"));
		}
		sb.append(" order by a.id desc ");
		
		PageResult<Map<String,Object>> result = this.pageQuery(sb.toString(),
				params,
				this.getClass().getName()+".claimPartWatchQuery()",
				pageSize,
				curPage);
		return result;
	}
	
	/*
	 * 查询可下发的配件
	 * */
	public   PageResult<Map<String,Object>> claimPartDispatchQueryInit(Long oemCompanyId , Map<String,String> map,int pageSize, int curPage, String whereSql,
			List<Object> params) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT B.WRGROUP_NAME,B.WRGROUP_CODE,A.PART_CODE,A.PART_NAME,A.ID  \n");
		sb.append(" FROM TT_AS_WR_FOREAPPROVALPT A,TT_AS_WR_MODEL_GROUP B \n ");
		sb.append(" WHERE A.MODEL_GROUP = B.WRGROUP_ID(+) AND NVL(A.IS_SEND, 0) <> "+Constant.DOWNLOAD_CODE_STATUS_03+"  \n ");

		if (Utility.testString(map.get("partCode"))) {
			sb.append(" and a.part_code like ? ");
			params.add("%"+map.get("partCode")+"%");
		}
		if (Utility.testString(map.get("partName"))) {
			sb.append(" and a.part_name like ? ");
			params.add("%"+map.get("partName")+"%");
		}
		if (Utility.testString(map.get("wrGroupId"))) {
			sb.append(" and a.MODEL_GROUP = ? ");
			params.add(map.get("wrGroupId"));
		}
		sb.append(" order by a.id desc ");
		
		PageResult<Map<String,Object>> result = this.pageQuery(sb.toString(),
				params,
				this.getClass().getName()+".claimPartWatchQuery()",
				pageSize,
				curPage);
		return result;
	}
	
	/** 
	* @Title: queryMonitorPart 
	* @Description: TODO(查询该车型组对应的配件是否监控) 
	* @param @param id             ：车型组id
	* @param @param code           ：配件代码
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List queryMonitorPart(String id,String code){
		StringBuffer sql= new StringBuffer();
		sql.append("select part_code,model_group from tt_as_wr_foreapprovalpt\n");
		sql.append(" where 1=1\n");
		sql.append(" and part_code = '").append(CommonUtils.checkNull(code)).append("'\n");
		sql.append(" and model_group = ").append(CommonUtils.checkNull(id)).append("\n");
		return pageQuery(sql.toString(), null,getFunName());
	}
}
