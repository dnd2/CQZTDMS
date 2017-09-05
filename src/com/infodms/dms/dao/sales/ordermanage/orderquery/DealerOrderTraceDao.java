package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;
/**
 * 该类用于经销商跟踪已经提报的订单执行情况
 * 的数据操作类
 * @author Devin Qin
 *
 */
public class DealerOrderTraceDao extends BaseDao {
	public static Logger logger = Logger.getLogger(DealerOrderTraceDao.class);
	private static final DealerOrderTraceDao dao = new DealerOrderTraceDao();

	public static final DealerOrderTraceDao getInstance() {
		return dao;
	}
	
	public List<TmDealerPO> getDealerInfo(AclUserBean logonUser){
		List<Object> params = new ArrayList<Object>();
		//设置查询SQL
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME,D.DEALER_SHORTNAME,D.DEALER_TYPE,D.DEALER_ORG_ID,D.OEM_COMPANY_ID,D.COMPANY_ID FROM TM_DEALER D ");
		sql.append(" WHERE D.DEALER_ORG_ID = ?  AND UPPER(D.DEALER_CODE)  NOT  LIKE 'JS%'  AND UPPER(D.DEALER_CODE)  NOT  LIKE 'S%' /*去除服务商列表(JS或S开头)*/ "); 
		//AND D.DEALER_TYPE IN ("+dealer_type+")" +
		sql.append(" AND D.STATUS= ? ");
		//设置SQL所需参数
		params.add(logonUser.getOrgId());
		params.add(Constant.STATUS_ENABLE);
		
		//返回经销商信息列表
  		List<TmDealerPO> list = factory.select(sql.toString(), params,
				new DAOCallback<TmDealerPO>() {
					public TmDealerPO wrapper(ResultSet rs, int idx) {
						TmDealerPO bean = new TmDealerPO();
						try 
						{
							bean.setDealerId(rs.getLong("DEALER_ID"));
							bean.setDealerCode(rs.getString("DEALER_CODE"));
							bean.setDealerName(rs.getString("DEALER_NAME"));
							bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
							bean.setDealerType(rs.getInt("DEALER_TYPE"));
							bean.setDealerOrgId(rs.getLong("DEALER_ORG_ID"));
							bean.setOemCompanyId(rs.getLong("OEM_COMPANY_ID"));
							bean.setCompanyId(rs.getLong("COMPANY_ID"));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
		return list;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
