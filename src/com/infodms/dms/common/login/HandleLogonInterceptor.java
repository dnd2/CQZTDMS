package com.infodms.dms.common.login;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.interceptor.LogonInterceptor;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class HandleLogonInterceptor {
	
	private static final Logger LOG = LogManager.getLogger(LogonInterceptor.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private DeCommonDao DECommonDao = DeCommonDao.getInstance();
	public AclUserBean getAclUserBeanByUserName(String rpcName, String rpcDealerCode, String rpcFlag, String rpcUserId,String rpcPoseId) throws Exception {
		LOG.info("接口登录 [rpcName == " + rpcName + "] \n"
				+ " [rpcDealerCode == " + rpcDealerCode + "] \n"
				+ " [rpcFlag == " + rpcFlag + "] \n"
				+ " [rpcUserId == " + rpcUserId + "] \n");
		if (Utility.testString(rpcName) && Utility.testString(rpcDealerCode)) {
			Map<String, Object> map = DECommonDao.getDcsCompanyCode(rpcDealerCode);
			String companyCode = map.get("DCS_CODE").toString();
			rpcName = rpcName.toUpperCase();
			//根据userName查询companyId,userId,userType
			AclUserBean logonUser = CommonDAO.getLogonUser(rpcName);
			if (null == logonUser) {
				LOG.error("getLogonUser(" + rpcName + ") no found userName");
				throw new IllegalArgumentException("没有在DCS系统中找到此用户");
			} else {
				logonUser.setCompanyCode(companyCode);
				logonUser.setRpcName(rpcName);
				logonUser.setRpcFlag(rpcFlag);
				logonUser.setRpcUserId(rpcUserId);
				//根据userId查询职位,只能有一个职位,否则报错
				TcPosePO tcpp = new TcPosePO() ;
				tcpp.setPoseId(Long.parseLong(rpcPoseId)) ;
				List<?> poseList = DECommonDao.select(tcpp) ; // CommonDAO.queryUserPosition(logonUser.getUserId());
				if (null == poseList || poseList.size() == 0) {
					throw new IllegalArgumentException("queryUserPosition(" + logonUser.getUserId() + ") no found, userName == " + logonUser.getName());
				} 
				if (poseList.size() > 1) {
					throw new IllegalArgumentException("queryUserPosition(" + logonUser.getUserId() + ") " +logonUser.getName() + " 对应多个职位");
				}
				TcPosePO tcPosePO = (TcPosePO)poseList.get(0);
				if (null == tcPosePO) {
					throw new IllegalArgumentException("queryUserPosition(" + logonUser.getUserId() + ") no found position, userName == " + logonUser.getName());
				}
				logonUser.setPoseType(tcPosePO.getPoseType());
				logonUser.setPoseBusType(tcPosePO.getPoseBusType());
				logonUser.setOrgId(tcPosePO.getOrgId());
				logonUser.setPoseId(tcPosePO.getPoseId());
				logonUser.setUserType(tcPosePO.getPoseType());
				//如果当前用户在数据库里的状态为在线
				if (CommonDAO.isLogUserOnline(logonUser.getUserId())) {
					//将状态置为下线
					CommonDAO.disableUserOnlineInfo(logonUser.getUserId());
				}
				TmOrgPO conOrg = new TmOrgPO();
				conOrg.setOrgId(logonUser.getOrgId());
				//获取组织信息
				List<TmOrgPO> orgList = factory.select(conOrg);
				if (null != orgList && orgList.size() > 0) {
					TmOrgPO orgPO = orgList.get(0);
					if (null != orgPO) {
						logonUser.setOrgType(orgPO.getOrgType());
						logonUser.setDutyType(orgPO.getDutyType().toString());
						logonUser.setParentOrgId(orgPO.getParentOrgId().toString());
					}
				}
				logonUser.setUID(String.valueOf(tcPosePO.getPoseId()));
				//根据poseType，如果poseType是经销商，则设置logonUser中的dealer_id
				if (logonUser.getUserType().equals(Constant.SYS_USER_DEALER)) {
					LOG.info(logonUser.getName() + " 经销商用户登录");
					HashMap<String, String> hm = new HashMap<String, String>();
					//获取经销商ID
					getUserDearId(logonUser.getOrgId() ,logonUser.getPoseBusType().toString() ,hm);
					logonUser.setDealerId(hm.get("DEALER_IDS"));
					logonUser.setOemCompanyId(hm.get("OEM_COMPANY_ID"));
					if (hm.get("COMPANY") != null && !"".equals(hm.get("COMPANY"))) {
						logonUser.setCompanyId(Long.valueOf(hm.get("COMPANY")));
					}
					logonUser.setDealerOrgId(hm.get("DEALER_ORG_ID"));
					logonUser.setDealerCode(hm.get("DEALER_CODE"));
				}
				if (logonUser.getUserType().equals(Constant.SYS_USER_SGM)) {
					LOG.info(logonUser.getName() + " 主机厂用户登录");
					logonUser.setOemPositionArea(getOemUserArea(logonUser.getPoseId()));
				}
				logonUser.setBxjDept(getXjbm(logonUser.getOrgId(), logonUser.getCompanyId()));
				return logonUser;
			}
		} else {
			throw new IllegalArgumentException("接口登录失败,缺少 userName == " + rpcName);
		}
	}
	
	public void getUserDearId(Long orgId ,String pose_bus_type ,HashMap<String, String> hm) 
	{
		String dealer_id = "";
		String dealer_org_id = "";
		String oem_company_id = "";
		String company_id = "";
		String dealer_code="";
		int dealer_type = 0;
		switch (Integer.valueOf(pose_bus_type))
		{
			case Constant.POSE_BUS_TYPE_WL:
				dealer_type = Constant.DEALER_TYPE_DVS;
				break;
			case Constant.POSE_BUS_TYPE_DVS:
				dealer_type = Constant.DEALER_TYPE_DVS;
				break;
			case Constant.POSE_BUS_TYPE_DWR:
				dealer_type = Constant.DEALER_TYPE_DWR;
				break;
			default:
				break;
		}
		StringBuffer sql = new StringBuffer("");
		//modify by andy.ten@tom.com begin 
		//2010-8-2 查询条件加上STATUS=10011001
		sql.append(" SELECT D.DEALER_ID,D.DEALER_CODE,D.DEALER_ORG_ID,D.OEM_COMPANY_ID,D.COMPANY_ID FROM TM_DEALER D ");
		sql.append(" WHERE D.DEALER_ORG_ID = ? AND D.DEALER_TYPE = ? AND D.STATUS="+Constant.STATUS_ENABLE);
		//end
		List<Object> params = new ArrayList<Object>();
		params.add(orgId);
		params.add(Integer.valueOf(dealer_type));
  		List<TmDealerPO> list = factory.select(sql.toString(), params,
				new DAOCallback<TmDealerPO>()
				{
					public TmDealerPO wrapper(ResultSet rs, int idx) 
					{
						TmDealerPO tmDealerPO = new TmDealerPO();
						try 
						{
							tmDealerPO.setDealerId(rs.getLong("DEALER_ID"));
							tmDealerPO.setDealerOrgId(rs.getLong("DEALER_ORG_ID"));
							tmDealerPO.setOemCompanyId(rs.getLong("OEM_COMPANY_ID"));
							tmDealerPO.setCompanyId(rs.getLong("COMPANY_ID"));
							tmDealerPO.setDealerCode(rs.getString("DEALER_CODE"));
						} catch (SQLException e) 
						{
							throw new DAOException(e);
						}
						return tmDealerPO;
					}
				});
  		if (list.size() > 0 && list.get(0) != null) {
  			/**
  			 * added by andy.ten@tom.com 一个orgId对应多个dealerId ，中间用","分开
  			 * 只对整车销售职位有该情况，对于售后，一个orgId还是对应一个dealerId，没有业务范围之分
  			 */
  			TmDealerPO dealerPO = (TmDealerPO) list.get(0);
			oem_company_id = dealerPO.getOemCompanyId().toString();
			dealer_org_id = dealerPO.getDealerOrgId().toString();
			company_id = dealerPO.getCompanyId().toString();
			dealer_code = dealerPO.getDealerCode();
			
  			if(list.size() == 1)
  			{
  				 dealer_id = dealerPO.getDealerId().toString(); 
  			}	 
  			else 
  			{
			   for (TmDealerPO dPO : list)
			   {
				   if(dealer_id.length() > 0 )
					   dealer_id += "," + dPO.getDealerId();
				   else
					   dealer_id = dPO.getDealerId().toString(); 
			   }
			}
  			// end
  		}
		
  		hm.put("DEALER_IDS", dealer_id);
  		hm.put("OEM_COMPANY_ID", oem_company_id);
  		hm.put("DEALER_ORG_ID", dealer_org_id);
  		hm.put("COMPANY", company_id);
  		hm.put("DEALER_CODE", dealer_code);
	}
	
	public String getOemUserArea(Long poseId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select ba.area_id\n");
		sql.append("  from tm_pose_business_area pa, tm_business_area ba\n");  
		sql.append(" where pa.area_id = ba.area_id\n"); 
		sql.append("   and pa.pose_id = "+poseId);
		sql.append("   and ba.status ="+Constant.STATUS_ENABLE);
		List list =  factory.select(sql.toString(), null, new DAOCallback<HashMap>() {
		
			public HashMap wrapper(ResultSet rs, int idx) {
				// TODO Auto-generated method stub
				Map map = new HashMap();
				try {
					ResultSetMetaData rsd = rs.getMetaData();
					int columnCount = rsd.getColumnCount();
					map = new HashMap();
					for (int i=1; i<=columnCount; i++){
						map.put(rsd.getColumnName(i).toString(),rs.getObject(i));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return (HashMap) map;
			}
		
		});
		String s = "(";
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map)list.get(i);
			s=s+map.get("AREA_ID");
			if(i<list.size()-1){
				s=s+",";
			}
		}
		return s+")";
	}
	
	private String getXjbm(Long orgId,Long companyId) {
		String bm = orgId+",";
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = CommonDAO.getXjbm(orgId,companyId, list);
		if(zDept != null && zDept.size() > 0) {
			for (int i = 0; i < zDept.size(); i++) {
				bm += zDept.get(i).getOrgId()+",";
			}
		}
		return bm.substring(0, bm.length()-1);
	}
	
}
