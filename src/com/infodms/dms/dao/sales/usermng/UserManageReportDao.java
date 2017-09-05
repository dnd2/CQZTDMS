package com.infodms.dms.dao.sales.usermng;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsPersonChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.po.TtVsPersonRegistPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class UserManageReportDao extends BaseDao<PO> {
	public Logger logger = Logger.getLogger(UserManageReportDao.class);
	
	private static final UserManageReportDao dao = new UserManageReportDao();

	public static final UserManageReportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * 人员流失率询
	 * @author 殷顺辉
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personRateSelect(AclUserBean logonUser,Map<String,String> m,String dealerCodes,int pageSize,int curPage) throws Exception{
		String leaveStartDate=m.get("leaveStartDate");
		String leaveEndDate=m.get("leaveEndDate");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VODA.PQ_ORG_NAME,\n" );
		sql.append("       VODA.DEALER_NAME,\n" );
		sql.append("       VODA.DEALER_CODE,\n" );
		sql.append("       TV.ZZ,\n" );
		sql.append("       TV.LZ,\n" );
		sql.append("       (ROUND(TV.LZ / DECODE(TV.ZZ, 0, 1, TV.ZZ), 2)) * 100 || '%' RATE,\n" );
		sql.append("       TV.Z_JL,\n" );
		sql.append("       TV.XS_JL,\n" );
		sql.append("       TV.SC_JL,\n" );
		sql.append("       TV.XS_GW\n" );
		sql.append("  FROM (SELECT T.DEALER_ID,\n" );
		sql.append("               SUM(QCZC) - SUM(QCLZ) + SUM(DQZC) AS ZZ,\n" );
		sql.append("               SUM(DQLZ) AS LZ,\n" );
		sql.append("               SUM(Z_JL) AS Z_JL,\n" );
		sql.append("               SUM(XS_JL) AS XS_JL,\n" );
		sql.append("               SUM(SC_JL) AS SC_JL,\n" );
		sql.append("               SUM(XS_GW) AS XS_GW\n" );
		sql.append("          FROM (SELECT M.DEALER_ID,\n" );
		sql.append("                       COUNT(1) AS QCZC,\n" );
		sql.append("                       0 AS QCLZ,\n" );
		sql.append("                       0 AS DQZC,\n" );
		sql.append("                       0 AS DQLZ,\n" );
		sql.append("                       0 AS Z_JL,\n" );
		sql.append("                       0 AS XS_JL,\n" );
		sql.append("                       0 AS SC_JL,\n" );
		sql.append("                       0 AS XS_GW\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID, 0, COUNT(1), 0, 0, 0, 0, 0, 0\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931002\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID, 0, 0, COUNT(1), 0, 0, 0, 0, 0\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') >=\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <=\n" );
			sql.append("                       TO_DATE('"+leaveEndDate+"', 'YYYY-MM-DD')\n" );
		}
		
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID,\n" );
		sql.append("                       0,\n" );
		sql.append("                       0,\n" );
		sql.append("                       0,\n" );
		sql.append("                       COUNT(1),\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961004, 1, 0)) AS Z_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961003, 1, 0)) AS XS_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961002, 1, 0)) AS SC_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961001, 1, 0)) AS XS_GW\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') >=\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <=\n" );
			sql.append("                       TO_DATE('"+leaveEndDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID) T\n" );
		sql.append("         GROUP BY T.DEALER_ID) TV,\n" );
		sql.append("       VW_ORG_DEALER_ALL VODA\n" );
		sql.append(" WHERE VODA.DEALER_ID = TV.DEALER_ID\n" );
		sql.append("   AND VODA.STATUS = 10011001");

		
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
				sql.append(" AND  VODA.DEALER_CODE IN("+str+") \n");
		}
		sql.append(" ORDER BY VODA.PQ_ORG_CODE, VODA.DEALER_SHORTNAME");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 人员关键岗位明细信息查询下载
	 * @param tvprp
	 */
	public List<Map<String,Object>> personRateDownLoad(AclUserBean logonUser,Map<String,String> m,String dealerCodes) throws Exception{
		String leaveStartDate=m.get("leaveStartDate");
		String leaveEndDate=m.get("leaveEndDate");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VODA.PQ_ORG_NAME,\n" );
		sql.append("       VODA.DEALER_NAME,\n" );
		sql.append("       VODA.DEALER_CODE,\n" );
		sql.append("       TV.ZZ,\n" );
		sql.append("       TV.LZ,\n" );
		sql.append("       (ROUND(TV.LZ / DECODE(TV.ZZ, 0, 1, TV.ZZ), 2)) * 100 || '%' RATE,\n" );
		sql.append("       TV.Z_JL,\n" );
		sql.append("       TV.XS_JL,\n" );
		sql.append("       TV.SC_JL,\n" );
		sql.append("       TV.XS_GW\n" );
		sql.append("  FROM (SELECT T.DEALER_ID,\n" );
		sql.append("               SUM(QCZC) - SUM(QCLZ) + SUM(DQZC) AS ZZ,\n" );
		sql.append("               SUM(DQLZ) AS LZ,\n" );
		sql.append("               SUM(Z_JL) AS Z_JL,\n" );
		sql.append("               SUM(XS_JL) AS XS_JL,\n" );
		sql.append("               SUM(SC_JL) AS SC_JL,\n" );
		sql.append("               SUM(XS_GW) AS XS_GW\n" );
		sql.append("          FROM (SELECT M.DEALER_ID,\n" );
		sql.append("                       COUNT(1) AS QCZC,\n" );
		sql.append("                       0 AS QCLZ,\n" );
		sql.append("                       0 AS DQZC,\n" );
		sql.append("                       0 AS DQLZ,\n" );
		sql.append("                       0 AS Z_JL,\n" );
		sql.append("                       0 AS XS_JL,\n" );
		sql.append("                       0 AS SC_JL,\n" );
		sql.append("                       0 AS XS_GW\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID, 0, COUNT(1), 0, 0, 0, 0, 0, 0\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931002\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID, 0, 0, COUNT(1), 0, 0, 0, 0, 0\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') >=\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <=\n" );
			sql.append("                       TO_DATE('"+leaveEndDate+"', 'YYYY-MM-DD')\n" );
		}
		
		sql.append("                 GROUP BY M.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT M.DEALER_ID,\n" );
		sql.append("                       0,\n" );
		sql.append("                       0,\n" );
		sql.append("                       0,\n" );
		sql.append("                       COUNT(1),\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961004, 1, 0)) AS Z_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961003, 1, 0)) AS XS_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961002, 1, 0)) AS SC_JL,\n" );
		sql.append("                       SUM(DECODE(M.POSITION, 99961001, 1, 0)) AS XS_GW\n" );
		sql.append("                  FROM TT_VS_PERSON_CHANGE M\n" );
		sql.append("                 WHERE M.CHANGE_TYPE = 29931006\n" );
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') >=\n" );
			sql.append("                       TO_DATE('"+leaveStartDate+"', 'YYYY-MM-DD')\n" );
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append("                   AND TRUNC(M.CREATE_DATE, 'DD') <=\n" );
			sql.append("                       TO_DATE('"+leaveEndDate+"', 'YYYY-MM-DD')\n" );
		}
		sql.append("                 GROUP BY M.DEALER_ID) T\n" );
		sql.append("         GROUP BY T.DEALER_ID) TV,\n" );
		sql.append("       VW_ORG_DEALER_ALL VODA\n" );
		sql.append(" WHERE VODA.DEALER_ID = TV.DEALER_ID\n" );
		sql.append("   AND VODA.STATUS = 10011001");

		
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
				sql.append(" AND  VODA.DEALER_CODE IN("+str+") \n");
		}
		sql.append(" ORDER BY VODA.PQ_ORG_CODE, VODA.DEALER_SHORTNAME");
		List<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
