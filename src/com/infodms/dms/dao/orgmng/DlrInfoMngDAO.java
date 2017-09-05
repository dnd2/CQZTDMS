package com.infodms.dms.dao.orgmng;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerChgHisPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;

public class DlrInfoMngDAO extends BaseDao<PO> {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 生成经销商名称更改历史
	 * @param dealerCondition
	 * @param newName
	 * @author chenyub@yonyou.com
	 */
	public void createDealerNameChgHis(TmDealerPO dealerCondition, String newName) {
		if (null == newName || null == dealerCondition) {
			return;
		}
		List<TmDealerPO> oldList = factory.select(dealerCondition);
		Date date = new Date();
		AclUserBean logonUser = (AclUserBean)ActionContext.getContext().getSession().get(Constant.LOGON_USER);
		if (!CommonUtils.isNullList(oldList)) {
			List<TmDealerChgHisPO> hisList = new LinkedList<TmDealerChgHisPO>();
			for (TmDealerPO old : oldList) {
				if (!newName.equals(old.getDealerName())) {
					TmDealerChgHisPO dch = new TmDealerChgHisPO();
					dch.setId(CommonUtils.getUUID());
					dch.setDealerId(old.getDealerId());
					dch.setDealerCode(old.getDealerCode());
					dch.setDealerName(old.getDealerName());
					dch.setCreateDate(date);
					dch.setCreateUser(logonUser.getUserId());
					hisList.add(dch);
				}
			}
			factory.insert(hisList);
		}
	}

	/**
	 * 根据code和name模糊查询所有的经销商
	 * @param code
	 * @param name
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public String queryHisDealerIdsByNameOrCodeSql(String code,String name){
		if(null==code&&null==name){
			return null;
		}
		StringBuffer buff = new StringBuffer();
		buff.append("( ");
		boolean hasName = false;
		if (name != null && name.length() > 0) {
			buff.append(" select distinct dchghis.dealer_id ");
			buff.append(" from TM_DEALER_CHG_HIS dchghis ");
			buff.append(" where dchghis.DEALER_NAME like '");
			buff.append("%").append(name).append("%' ");
			hasName = true;
		}
		if(null != code && code.length() > 0){
			if (hasName) {
				buff.append(" union all ");
			}
			// 历史code
			buff.append(" select distinct sdbindo.bind_dealer_id ");
			buff.append(" from TT_SERVICE_DEALER_BIND sdbindo ");
			buff.append(" where sdbindo.bind_flag in ( ");
			buff.append(queryDealerIdByCodeSql(code));
			buff.append(" ) ");
			
			//最新的code
			buff.append(" or sdbindo.bind_dealer_id in (");
			buff.append(queryDealerIdByCodeSql(code));
			buff.append(" ) ");
		}
		buff.append(" )");
		return buff.toString();
	}

	/**
	 * 根据name模糊查询所有的经销商
	 * @param code
	 * @param name
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public String queryHisDealerIdsByNameSql(String name){
		if(null==name){
			return null;
		}
		StringBuffer buff = new StringBuffer();
		if (name != null && name.length() > 0) {
			buff.append(" select distinct dchghis.dealer_id ");
			buff.append(" from TM_DEALER_CHG_HIS dchghis ");
			buff.append(" where dchghis.DEALER_NAME like '");
			buff.append("%").append(name).append("%' ");
		}
		return buff.toString();
	}
	/**
	 * 根据name模糊查询所有的经销商
	 * @param code
	 * @param code
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public String queryHisDealerIdsByCodeSql(String code){
		if(null==code){
			return null;
		}
		StringBuffer buff = new StringBuffer();
		if (code != null && code.length() > 0) {
			// 历史code
			buff.append(" select distinct sdbindo.bind_dealer_id ");
			buff.append(" from TT_SERVICE_DEALER_BIND sdbindo ");
			buff.append(" where sdbindo.bind_flag in ( ");
			buff.append(queryDealerIdByCodeSql(code));
			buff.append(" ) ");
			
			//最新的code
			buff.append(" or sdbindo.bind_dealer_id in (");
			buff.append(queryDealerIdByCodeSql(code));
			buff.append(" ) ");
		}
		return buff.toString();
	}
	
	/**
	 * 根据code模糊查询经销商ID
	 * @param code
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public String queryDealerIdByCodeSql(String code){
		if(null==code){
			return null;
		}
		StringBuffer buff = new StringBuffer();
		buff.append(" select distinct sdbd.dealer_id ");
		buff.append(" from tm_dealer sdbd ");
		buff.append(" where sdbd.dealer_code like '%");
		buff.append(code);
		buff.append("%' ");
		return buff.toString();
		
	}
	
	/**
	 * 根据codes精确查询经销商ID
	 * @param codes
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public String queryHisDealerIdsByCodesSql(String codes){
		if(null==codes){
			return null;
		}
		StringBuffer buff = new StringBuffer();
		buff.append(" select distinct sdbd.dealer_id ");
		buff.append(" from tm_dealer sdbd ");
		buff.append(" where 1=1 ");
		DaoFactory.getsql(buff, "sdbd.dealer_Code", codes, 6);
		return buff.toString();
	}

}
