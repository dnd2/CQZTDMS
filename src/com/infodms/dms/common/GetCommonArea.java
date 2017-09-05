package com.infodms.dms.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.TmAscDao;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.callback.DAOCallback;

public class GetCommonArea extends BaseDao{
	private static ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static Logger logger = Logger.getLogger(GetCommonArea.class);
	public static String getMyArea(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String areaId="";
		if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
				for(int i=0;i<MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size();i++){
					if((i+1)!=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()){
						areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(i).get("AREA_ID").toString()+","+areaId;
					}else{
						areaId=areaId+MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(i).get("AREA_ID").toString();
					}
				}
		}
				return areaId;
	}
	public static  List<Map<String, Object>> getMyCarArea(String dealerIds){
		//AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//String areaId="";
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME\n");
		sql.append("  FROM TM_DEALER              TMD,\n");
		sql.append(" TM_DEALER_WAREHOUSE    TDW,\n");
		sql.append("    TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("    TM_VEHICLE             TMV,\n");
		sql.append("      TM_AREA_GROUP TAG,\n");
		sql.append("      TM_BUSINESS_AREA TBA\n");
		sql.append("WHERE TMD.DEALER_ID = TDW.DEALER_ID\n");
		sql.append("  AND TMV.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("  AND G.GROUP_ID = TMV.SERIES_ID\n");
		sql.append("  AND TBA.AREA_ID=TAG.AREA_ID");
		sql.append("  AND TBA.AREA_ID!=2010010100000003");
		sql.append("  AND TAG.MATERIAL_GROUP_ID=G.GROUP_ID\n");
		sql.append("  AND TMD.DEALER_ID IN("+dealerIds+")\n");
		sql.append("GROUP BY TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME\n");
		GetCommonArea dao=new GetCommonArea();
		List list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
