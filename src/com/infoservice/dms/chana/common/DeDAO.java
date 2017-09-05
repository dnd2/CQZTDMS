package com.infoservice.dms.chana.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.dms.chana.po.MqUserInfoPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;

public class DeDAO {
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 获取GROUP USER
	 * <p>
	 * @param conn <code>数据库连接</code>
	 * @return group user list
	 * @throws Exception
	 */
	public static List<MqUserInfoPO> getGroupUser() throws Exception{
		List<MqUserInfoPO> returnList = new LinkedList<MqUserInfoPO>();
		MqUserInfoPO conPo = new MqUserInfoPO();
		conPo.setUserType("GROUP");
		conPo.setEnable(1);
		try
		{
			returnList = factory.select(conPo);
		}
		catch (Exception e)
		{
			logger.error("DeDAO.getGroupUser is err!", e);
			throw e;
		}
		return returnList;
	}
	/**
	 * 查询DMS端组织代码在上端对应的公司ID
	 * <p>
	 * @param dmsOrgCode
	 * @return companyId
	 * @throws Exception
	 */
	public static Long getDcsCompany(String dmsOrgCode) throws Exception{
		TmCompanyPO conComPo = new TmCompanyPO();
		conComPo.setCompanyCode(dmsOrgCode);
		List<TmCompanyPO> comList = new ArrayList<TmCompanyPO>();
		Long companyId = 0l;
		try
		{
			comList = factory.select(conComPo);
			if(comList.size()<1)
				throw new Exception("DCS端未查询到代码为"+dmsOrgCode+"的代理商实体公司记录");
			companyId = comList.get(0).getCompanyId();
		}
		catch (Exception e)
		{
			logger.error("DeDAO.getDcsCompany is err!", e);
			throw e;
		}
		return companyId;
	}
	/**
	 * 查询DMS端组织代码在上端对应的组织ID
	 * <p>
	 * @param dmsOrgCode
	 * @return orgId
	 * @throws Exception
	 */
	public static Long getDcsOrg(String dmsOrgCode,Integer orgType) throws Exception{
		Long agentOrgId = 0l;
		Long agentCompanyId = 0l;
		try
		{
			agentCompanyId = getDcsCompany(dmsOrgCode);
			logger.debug("DMS COMPANY_CODE对应的公司id为"+agentCompanyId);
			TmOrgPO conOrgPo = new TmOrgPO();
			conOrgPo.setCompanyId(agentCompanyId);
			conOrgPo.setOrgType(orgType);
			List<TmOrgPO> orgList = new ArrayList<TmOrgPO>();
			orgList = factory.select(conOrgPo);
			if(orgList.size()<1)
				throw new Exception("DCS端未查询到代码为"+dmsOrgCode+"对应的"+orgType+"组织ID!");
			agentOrgId = orgList.get(0).getOrgId();
		}
		catch (Exception e)
		{
			logger.error("DeDAO.getDcsOrg is err!", e);
			throw e;
		}
		return agentOrgId;
	}
	/**
	 * 查询车辆和实体公司编号在DCS端对应的组织ID
	 * <p>
	 * @param companyCode
	 * @param vin
	 * @return orgId
	 * @throws Exception
	 */
	public static Long getOrgId(String companyCode, String vin) throws Exception{
		Long orgId = 0l;
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT TMO.ORG_ID \n");
		sqlBuffer.append("  FROM TM_ORG TMO, \n");
		sqlBuffer.append("       TM_COMPANY TMC, \n");
		sqlBuffer.append("       TM_ORG_BUSINESS_AREA TMB, \n");
		sqlBuffer.append("       TM_VEHICLE TMV, \n");
		sqlBuffer.append("       VW_PRODUCT VWP \n");
		sqlBuffer.append(" WHERE TMC.COMPANY_ID = TMO.COMPANY_ID \n");
		sqlBuffer.append("   AND TMO.ORG_ID = TMB.ORG_ID \n");
		sqlBuffer.append("   AND TMB.BRAND_ID = VWP.BRAND_ID \n");
		sqlBuffer.append("   AND VWP.MODEL_ID = TMV.MODEL_ID \n");
		sqlBuffer.append("   AND TMC.COMPANY_CODE = '"+companyCode+"' \n");
		sqlBuffer.append("   AND TMV.VIN = '"+vin+"' \n");
		String sql = sqlBuffer.toString();
		logger.debug("the getOrgId SQL===== " + sql.toString());
		List<TmOrgPO> orgList = factory.select(sql, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		if(orgList.size()<1)
			throw new Exception("未能查询到实体公司"+companyCode+"对应车辆"+vin+"的组织记录!");
		orgId = orgList.get(0).getOrgId();
		return orgId;
	}	
	/**
	 * 查询下端实体公司+品牌代码在上端对应的的组织记录
	 * <p>
	 * @param companyCode
	 * @param brandCode
	 * @return TmOrgPO
	 * @throws Exception
	 */
	public static TmOrgPO getOrg(String companyCode, String brandCode) throws Exception{
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT TMO.COMPANY_ID,TMO.ORG_ID,TMO.ORG_CODE \n");
		sqlBuffer.append("  FROM TM_ORG TMO, \n");
		sqlBuffer.append("       TM_COMPANY TMC, \n");
		sqlBuffer.append("       TM_ORG_BUSINESS_AREA TMB, \n");
		sqlBuffer.append("       TM_BRAND TB \n");
		sqlBuffer.append(" WHERE TMC.COMPANY_ID = TMO.COMPANY_ID \n");
		sqlBuffer.append("   AND TMO.ORG_ID = TMB.ORG_ID \n");
		sqlBuffer.append("   AND TMB.BRAND_ID = TB.BRAND_ID \n");
		sqlBuffer.append("   AND TMC.COMPANY_CODE = '"+companyCode+"' \n");
		sqlBuffer.append("   AND TB.BRAND_CODE = '"+brandCode+"' \n");
		String sql = sqlBuffer.toString();
		logger.debug("the getOrg SQL===== " + sql.toString());
		List<TmOrgPO> orgList = factory.select(sql, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setCompanyId(rs.getLong("COMPANY_ID"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		if(orgList.size()<1)
			throw new Exception("未能查询到实体公司"+companyCode+"对应品牌"+brandCode+"的组织记录!");
		return orgList.get(0);
	}
	/**
	 * 查询代理商代码对应的实体公司代码
	 * <p>
	 * @param dealerCode
	 * @return companyCode
	 * @throws Exception
	 */
	public static String getComCode(String dealerCode) throws Exception{
		String comCode = "";
		TmDealerPO conDealer = new TmDealerPO();
		conDealer.setDealerCode(dealerCode);
		List<TmDealerPO> dealerList = factory.select(conDealer);
		if(dealerList.size()<1)
			throw new Exception("DCS端不存在编码为"+dealerCode+"的代理商渠道记录");
		TmCompanyPO conCom = new TmCompanyPO();
		conCom.setCompanyId(dealerList.get(0).getCompanyId());
		List<TmCompanyPO> comList = factory.select(conCom);
		if(comList.size()<1)
			throw new Exception("DCS端不存在编码为"+dealerCode+"的代理商渠道的实体公司");
		comCode = comList.get(0).getCompanyCode();
		return comCode;
	}
}
