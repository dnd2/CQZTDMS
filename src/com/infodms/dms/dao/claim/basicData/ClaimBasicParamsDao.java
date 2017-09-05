/**   
* @Title: ClaimBasicParamsDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: 索赔基本参数设定DAO 
* @author wangjinbao   
* @date 2010-5-26 下午01:54:10 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDownParameterPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimBasicParamsDao 
 * @Description: 索赔基本参数设定DAO
 * @author wangjinbao 
 * @date 2010-5-26 下午01:54:10 
 *  
 */
public class ClaimBasicParamsDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimBasicParamsDao.class);
	private static final ClaimBasicParamsDao dao = new ClaimBasicParamsDao ();
	public static final ClaimBasicParamsDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getClaimBasicParams 
	* @Description: TODO(取索赔基本参数) 
	* @param  type         ：tc_code对应的类型
	* @return List<TcCodePO>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimBasicParams(String type){
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
	* @Title: claimBasicParamsQuery 
	* @Description: 索赔基本参数设定查询
	* @param  pageSize           ：每页显示条数
	* @param  curPage            ：当前页数
	* @param  whereSql           ：查询条件
	* @param  params             ：查询条件对应的参数
	* @param  type               ：索赔基本参数的定义的tc_code 
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimBasicParamsQuery(int pageSize, int curPage, String whereSql,List<Object> params,String type) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getClaimBasicParams(type);
		StringBuffer sb = new StringBuffer();
		sb.append(" select c.dealer_code as DEALER_CODE,c.dealer_shortname ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,max(decode(c.code_id,"+tcpo.get("CODE_ID")+",c.parameter_value)) as \""+tcpo.get("CODE_ID")+"\" ");
		}
		sb.append(" from (select td.dealer_code,td.dealer_shortname,tc.code_id,tdp.parameter_value,tc.num from tm_down_parameter tdp ");
		sb.append(" left outer join tc_code tc on tdp.parameter_code = tc.code_id ");
		sb.append(" left outer join tm_dealer td on td.dealer_id = tdp.dealer_id and td.dealer_type="+Constant.MSG_TYPE_2+"\n");
		sb.append(" where 1=1 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tc.num) c group by c.dealer_code,c.dealer_shortname ");
		sb.append(" order by c.dealer_code ");
		//"com.infodms.dms.dao.claim.basicData.ClaimBasicParamsDao.claimBasicParamsQuery"
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	
    /**
     * 
    * @Title: getCodeToPO 
    * @Description: 根据经销商代码查询对应的经销商PO
    * @param  dealerCode
    * @return TmDealerPO    经销商PO 
    * @throws
     */
	@SuppressWarnings("unchecked")
	public TmDealerPO getCodeToPO(String dealerCode){
		List<Object> params = new LinkedList<Object>();
		TmDealerPO tmdelerpo = new TmDealerPO();
		StringBuilder sb = new StringBuilder();
		sb.append(" select td.dealer_id,td.dealer_shortname,td.dealer_code from tm_dealer td where td.dealer_code = ? and td.status = ?  ");
		params.add(dealerCode);
		params.add(Constant.STATUS_ENABLE);
		PageResult<TmDealerPO> rs = pageQuery(TmDealerPO.class,sb.toString(), params,10, 1);
		List<TmDealerPO> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				tmdelerpo = ls.get(0);
			}
		}
		return tmdelerpo;
	}
	/**
	 * 
	* @Title: updateBasicParams 
	* @Description: TODO 索赔基本参数设定修改
	* @param  selpo      ：  查询条件PO
	* @param  updatepo   ：  修改后的PO 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateBasicParams(TmDownParameterPO selpo,TmDownParameterPO updatepo){
		dao.update(selpo, updatepo);
	}
	
	/**
	 * 
	* @Title: getObjFromList 
	* @Description: TODO   根据条件PO查询索赔基本参数表并以PO的形式返回
	* @param  po
	* @return TmDownParameterPO    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public TmDownParameterPO getObjFromList(TmDownParameterPO po) {
		List<TmDownParameterPO> list = dao.select(po);
		if (list != null && list.size() > 0) {
			return (TmDownParameterPO)list.get(0);
		} else {
			return null;
		}
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
	public PageResult<Map<String, Object>> claimBasicParamsQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		PageResult<Map<String, Object>> result = null;
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//经销商id集合
				String dealerIds = request.getParamValue("dealerCode");//待查询的经销商code，以,隔开
				//经销商name
				String delearName = request.getParamValue("DEALER_NAME");
				String parameter_value = request.getParamValue("parameter_value");
				//拼sql的查询条件
					
				List list = dao.getClaimBasicParams("1042"); 
				    if ("0".equals(parameter_value))
				    {
				    	sb.append(" select * from ( ");
				     }
				sb.append(" select c.dealer_code as DEALER_CODE,c.dealer_shortname ");
				HashMap tcpo=null;
				for(int i = 0 ; i < list.size(); i++){
					 tcpo = (HashMap)list.get(i);
					sb.append(" ,max(decode(c.code_id,"+tcpo.get("CODE_ID")+",c.parameter_value)) as \""+tcpo.get("CODE_ID")+"\" ");
				}
				sb.append(" from (select td.dealer_code,td.dealer_shortname,tc.code_id,tdp.parameter_value,tc.num from tm_down_parameter tdp ");
				sb.append(" left outer join tc_code tc on tdp.parameter_code = tc.code_id ");
				sb.append(" left outer join tm_dealer td on td.dealer_id = tdp.dealer_id and td.dealer_type="+Constant.MSG_TYPE_2+"\n");
				sb.append(" where 1=1 ");
				if (Utility.testString(dealerIds)) {
					sb.append(Utility.getConSqlByParamForEqual(dealerIds, params, "td", "dealer_code"));
				}
				if (Utility.testString(delearName)) {
					sb.append(" and td.dealer_shortname like ? ");
					params.add("%"+delearName+"%");
				}	
				if (Utility.testString(parameter_value)) {
					sb.append(" and tdp.PARAMETER_VALUE= "+parameter_value);
				}	
				sb.append(" order by tc.num) c group by c.dealer_code,c.dealer_shortname ");
				sb.append(" order by c.dealer_code ");
				if ("0".equals(parameter_value))
			    {
					sb.append(") t where 1=1 and " +'"'+tcpo.get("CODE_ID")+'"' +" is not null");
			     }
				
				result = this.pageQuery(sb.toString(), params, getFunName(), pageSize, currPage);
				
	       }
			return result;
	}
}
