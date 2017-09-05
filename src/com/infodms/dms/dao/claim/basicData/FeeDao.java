/**   
* @Title: FeeDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(保养费用维护DAO) 
* @author wangjinbao   
* @date 2010-6-7 下午02:59:23 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrModelFeePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: FeeDao 
 * @Description: TODO(保养费用维护DAO) 
 * @author wangjinbao 
 * @date 2010-6-7 下午02:59:23 
 *  
 */
public class FeeDao extends BaseDao {
	public static Logger logger = Logger.getLogger(FeeDao.class);
	private static final FeeDao dao = new FeeDao ();
	public static final FeeDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getFeeType 
	* @Description: TODO(获得保养费用的tc_code列表) 
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getFeeType(String type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tc.code_id,tc.code_desc,tc.type from tc_code tc ");
		sb.append(" where 1=1 ");
		if(Utility.testString(type)){
			sb.append(" and tc.type = ? ");
			params.add(type);
		}
		sb.append(" and tc.status = ? ");
		params.add(Constant.STATUS_ENABLE);
		sb.append(" order by tc.num");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: feeQuery 
	* @Description: 保养费用查询
	* @param  pageSize           ：每页显示条数
	* @param  curPage            ：当前页数
	* @param  whereSql           ：查询条件
	* @param  params             ：查询条件对应的参数
	* @param  type               ：保养费用类型的tc_code 
	* @param  flagStr            ：是否设置保养费用
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> feeQuery(int pageSize, int curPage, String whereSql,List<Object> params,String type,String flagStr) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getFeeType(type);
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.group_id,t.group_code,t.group_name ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,max(decode(t.fee_type,"+tcpo.get("CODE_ID")+",t.fee)) as \""+tcpo.get("CODE_ID")+"\" ");
		}
		sb.append(" from ");
		sb.append(" (select tvmg.group_id,tvmg.group_code,tvmg.group_name,tawmf.fee_id,tawmf.fee,tawmf.fee_type ");
		sb.append(" from TM_VHCL_MATERIAL_GROUP tvmg ");
		sb.append(" left outer join TT_AS_WR_MODEL_FEE tawmf on tvmg.group_id = tawmf.model_id ");
		sb.append(" where tvmg.group_level = 3 ");
		sb.append(" and tvmg.group_id ");
		sb.append(flagStr);
		sb.append(" ( select a.model_id from TT_AS_WR_MODEL_FEE a group by a.model_id ) ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" ) t ");
		sb.append(" group by t.group_id,t.group_code,t.group_name ");
		sb.append(" order by t.group_id ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getObjFromList 
	* @Description: TODO(根据po获得保养费用的po) 
	* @param @param po
	* @param @return   
	* @return TtAsWrModelFeePO  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public TtAsWrModelFeePO getObjFromList(TtAsWrModelFeePO po) {
		List<TtAsWrModelFeePO> list = dao.select(po);
		if (list != null && list.size() > 0) {
			return (TtAsWrModelFeePO)list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 
	* @Title: updateFee 
	* @Description: TODO(保养费用的修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateFee(TtAsWrModelFeePO selpo,TtAsWrModelFeePO updatepo){
		dao.update(selpo,updatepo);
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
