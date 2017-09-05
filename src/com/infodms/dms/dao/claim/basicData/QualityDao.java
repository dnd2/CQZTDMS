/**   
* @Title: QualityDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(索赔配件质保期维护DAO) 
* @author wangjinbao   
* @date 2010-6-5 下午02:55:07 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrModelPartGuaranteesPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: QualityDao 
 * @Description: TODO(索赔配件质保期维护DAO) 
 * @author wangjinbao 
 * @date 2010-6-5 下午02:55:07 
 *  
 */
public class QualityDao extends BaseDao {
	public static Logger logger = Logger.getLogger(QualityDao.class);
	private static final QualityDao dao = new QualityDao ();
	public static final QualityDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: qualityQuery 
	* @Description: TODO(索赔配件质保期维护查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param whereSql
	* @param @param params
	* @param @param flagStr
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> qualityQuery(int pageSize, int curPage, String whereSql,List<Object> params,String flagStr) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select tvmg.group_id,tvmg.group_code,tvmg.group_name ");
		sb.append(" from TM_VHCL_MATERIAL_GROUP tvmg where tvmg.group_level=3 ");
		sb.append(" and tvmg.group_id ");
		sb.append(flagStr);
		sb.append(" ( select tawmpg.model_id from TT_AS_WR_MODEL_PART_GUARANTEES tawmpg  ");
		sb.append(" group by tawmpg.model_id) ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tvmg.group_id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getModelType 
	* @Description: TODO(取得配件类型) 
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getModelType(String type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tc.code_id,tc.code_desc,tc.type from tc_code tc ");
		sb.append(" where 1=1 ");
		if(Utility.testString(type)){
			sb.append(" and tc.type = ? ");
			params.add(type);
		}
		sb.append(" and tc.status = ? ");
		sb.append(Constant.STATUS_ENABLE);
		sb.append(" order by tc.num");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getModelByIdType 
	* @Description: TODO() 
	* @param @param modelId
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	public List getModelByIdType(Long modelId,Integer type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tawmpg.gurn_id,tawmpg.model_id,tc.code_id,tc.code_desc,tawmpg.gurn_month,tawmpg.gurn_mile  ");
		sb.append(" from (select * from TT_AS_WR_MODEL_PART_GUARANTEES where model_id = ?  ");
		params.add(modelId);
		sb.append(" ) tawmpg,tc_code tc where tawmpg.part_type(+)=tc.code_id ");
		sb.append(" and tc.type = ? ");
		params.add(type);
		sb.append(" order by tc.code_id ");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getModelByIdTypeDetail 
	* @Description: TODO(索赔配件质保期维护明细) 
	* @param @param modelId
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getModelByIdTypeDetail(Long modelId,Integer type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select t1.*,tvmg.group_code  ");
		sb.append(" from ( select tt2.code_id,tt2.code_desc, ");
		sb.append(" tt1.gurn_id,");
		sb.append("'"+modelId+"' as ");
		sb.append(" model_id,tt1.part_type,tt1.gurn_month,tt1.gurn_mile ");
		sb.append(" from (select * from TT_AS_WR_MODEL_PART_GUARANTEES where model_id = ? ) tt1, ");
		params.add(modelId);
		sb.append(" tc_code  tt2 ");
		sb.append(" where tt1.part_type(+)=tt2.code_id");
		sb.append(" and tt2.type = ? ");
		params.add(type);
		sb.append(" group by tt2.code_id,tt2.code_desc,tt1.gurn_id,tt1.model_id,tt1.part_type,tt1.gurn_month,tt1.gurn_mile) t1, ");
		sb.append(" TM_VHCL_MATERIAL_GROUP tvmg ");
		sb.append(" where t1.model_id=tvmg.group_id ");
		sb.append(" order by t1.code_id");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: updateModelPartGuarantees 
	* @Description: TODO(索赔配件质保期维护修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateModelPartGuarantees(TtAsWrModelPartGuaranteesPO selpo,TtAsWrModelPartGuaranteesPO updatepo){
		dao.update(selpo, updatepo);
	}

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}

}
