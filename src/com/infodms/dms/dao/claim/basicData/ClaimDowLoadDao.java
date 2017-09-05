/**   
* @Title: ClaimDowLoadDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(下发索赔工时DAO) 
* @author wangjinbao   
* @date 2010-7-9 上午10:26:38 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimDowLoadDao 
 * @Description: TODO(下发索赔工时DAO) 
 * @author wangjinbao 
 * @date 2010-7-9 上午10:26:38 
 *  
 */
public class ClaimDowLoadDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimDowLoadDao.class);
	private static final ClaimDowLoadDao dao = new ClaimDowLoadDao ();
	public static final ClaimDowLoadDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getClaimModel 
	* @Description: TODO(查询所有的索赔车型组) 
	* @param @param type  ：车型组类型（索赔车型组）
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimModel(Long companyId,Integer type){
		StringBuffer sql= new StringBuffer();
		sql.append("select count(tg.model_id) as group_count,\n" );
		sql.append("       tg.wrgroup_id,\n" );
		sql.append("       tg.wrgroup_type,\n" );
		sql.append("       tg.wrgroup_code,\n" );
		sql.append("       tg.wrgroup_name\n" );
		sql.append("  from (select tawmg.wrgroup_id,\n" );
		sql.append("               tawmg.wrgroup_type,\n" );
		sql.append("               tawmg.wrgroup_code,\n" );
		sql.append("               tawmg.wrgroup_name,\n" );
		sql.append("               tawmi.model_id,\n" );
		sql.append("               tawmg.oem_company_id\n");
		sql.append("          from TT_AS_WR_MODEL_GROUP tawmg\n" );
		sql.append("          left outer join TT_AS_WR_MODEL_ITEM tawmi on tawmg.wrgroup_id =\n" );
		sql.append("                                                       tawmi.wrgroup_id) tg\n" );
		sql.append(" group by tg.wrgroup_id, tg.wrgroup_type, tg.wrgroup_code, tg.wrgroup_name,tg.oem_company_id\n" );
		sql.append(" having 1 = 1 and tg.wrgroup_type = "+type+"\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getClaimLabourByWrgroupId 
	* @Description: TODO(车型组对应的索赔工时列表) 
	* @param @param id  ：车型组ID
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimLabourByWrgroupId(Long id,Long companyId){
		StringBuffer sql= new StringBuffer();
		sql.append("select taww.id,\n" );
		sql.append("       taww.wrgroup_id,\n" );
		sql.append("       taww.labour_code,\n" );
		sql.append("       taww.cn_des,\n" );
		sql.append("       taww.en_des,\n" );
		sql.append("       trim(to_char(taww.labour_quotiety, '999999.00')) as labour_quotiety,\n" );
		sql.append("       trim(to_char(taww.labour_hour, '999999.00')) as labour_hour\n" );
		sql.append("  from TT_AS_WR_WRLABINFO taww\n" );
		sql.append(" where taww.is_del = 0\n" );
		sql.append("   and taww.tree_code = '3'\n" );
		//sql.append("   and taww.is_send = "+Constant.DOWNLOAD_CODE_STATUS_01+"\n");
		sql.append(" and (taww.if_status is null or taww.if_status<>2)");
		sql.append("   and taww.wrgroup_id = "+id+"\n" );
		sql.append(" order by taww.id desc");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getAddLabourById 
	* @Description: TODO(索赔工时对应的附加工时列表) 
	* @param @param id  ： 索赔工时ID
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getAddLabourById(Long id){
		StringBuilder sb = new StringBuilder();
		sb.append("select tawa.id,taww.wrgroup_id,taww.labour_code,taww.cn_des,taww.en_des,taww.id as add_id,");
		sb.append(" trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety, ");
		sb.append(" trim(to_char(taww.labour_hour,'999999.00')) as labour_hour ");
		sb.append(" from TT_AS_WR_ADDITIONALITEM tawa ");
		sb.append(" left outer join TT_AS_WR_WRLABINFO  taww on tawa.add_id = taww.id ");
		sb.append(" where tawa.is_del = 0 and taww.is_del = 0 and tawa.w_id = "+id+" ");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getDealerList 
	* @Description: TODO(获取所有的经销商列表) 
	* @param @param oemCompanyId
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getDealerList(Long oemCompanyId){
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n" );
		sql.append("  from tm_dealer t\n" );
		sql.append(" where t.oem_company_id = "+oemCompanyId+"\n" );
		sql.append(" and t.status ="+Constant.STATUS_ENABLE+"\n");
		sql.append(" order by t.dealer_id ");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
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
