/**********************************************************************
 * <pre>
 * FILE : SysDealerAccountDao.java
 * CLASS : SysDealerAccountDao
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-30|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.dealermanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TmDealerAccountBean;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.po.TmDealerAccountPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysDealerAccountDao {
	public static Logger logger = Logger.getLogger(SysDealerDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<TmDealerAccountBean> findAllDealerAccountInfo(TmDealerAccountPO daPo, int curPage, int pageSize, String orderName, String da){
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();	
		
		sb.append("SELECT T.ACCOUNT_ID,T.INV_HEAD,T.INV_HEAD_CONTENT,T.ACCOUNT_NO,T.ACCOUNT_BANK,");
		sb.append("T.DEALER_ID,D.DEALER_SHORTNAME,T.CREATE_BY,T.CREATE_DATE,T.UPDATE_BY,T.UPDATE_DATE");
		sb.append(" FROM TM_DEALER_ACCOUNT T LEFT JOIN TM_DEALER D ON T.DEALER_ID = D.DEALER_ID ");
		if(null != daPo.getDealerId() && !"".equals(daPo.getDealerId())){
			params.add(daPo.getDealerId());
			sb.append(" WHERE T.DEALER_ID = ? ");
		}	
		sb.append(" ORDER BY T.ACCOUNT_BANK ");

		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmDealerAccountBean>() {
			public TmDealerAccountBean wrapper(ResultSet rs, int idx) {
				TmDealerAccountBean bean = new TmDealerAccountBean();
				try {
					bean.setAccountId(rs.getLong("ACCOUNT_ID"));
					bean.setInvHead(rs.getString("INV_HEAD"));
					bean.setInvHeadContent(rs.getString("INV_HEAD_CONTENT"));
					bean.setAccountNo(rs.getString("ACCOUNT_NO"));
					bean.setAccountBank(rs.getString("ACCOUNT_BANK"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setCreateBy(rs.getLong("CREATE_BY"));
					bean.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));
					bean.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);	
	}
	
	/**
	 * BY 代理商报支账户ID 查询出指定代理商报支账户信息
	 * @param accountId 代理商报支账户ID : accountId
	 * @return
	 */
	public static TmDealerAccountPO getDealerAccountInfoByAccountId(Long accountId){
		if(null != accountId && !"".equals(accountId)){
			TmDealerAccountPO po = new TmDealerAccountPO();
			po.setAccountId(accountId);
			List<TmDealerAccountPO> list = factory.select(po);
			if(null != list && list.size() > 0){
				po = list.get(0);
			}
			return po;
		}else{
			return null;
		}		
	}	
}
