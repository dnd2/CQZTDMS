package com.infodms.dms.dao.orgmng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TmCompanyPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;

public class TmAscDao {

	private static POFactory factory = POFactoryBuilder.getInstance();
	private static Logger logger = Logger.getLogger(TmAscDao.class);
	
	public static List<TmCompanyPO> searchCompanyList(Long ascId) throws Exception{
		StringBuffer sql=new StringBuffer();

		sql.append("SELECT TC.COMPANY_CODE\n");
		sql.append("  FROM TM_ASC TMA, TT_ORG_VS_ASC OVA, TM_COMPANY TC, TM_ORG ORG\n");  
		sql.append(" WHERE OVA.DEALER_ID = ORG.ORG_ID\n");  
		sql.append("   AND OVA.ASC_ID = TMA.ASC_ID\n");  
		sql.append("   AND ORG.COMPANY_ID = TC.COMPANY_ID\n");  
		sql.append("   AND TMA.ASC_ID = "+ascId);

		logger.debug(sql.toString());
		
		List<TmCompanyPO> list = new ArrayList<TmCompanyPO>();
		
		try{
			list = factory.select(sql.toString(), null, new DAOCallback<TmCompanyPO>(){
				public TmCompanyPO wrapper(ResultSet rs, int arg1) {
					TmCompanyPO po=new TmCompanyPO();
					try {
						po.setCompanyCode(rs.getString(1));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return po;
				}
			});
		} catch (Exception e) {
			throw e;
		}
		
		return list;
	}
	
}
