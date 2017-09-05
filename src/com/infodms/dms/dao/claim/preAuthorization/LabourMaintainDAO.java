package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.po.TtAsWrForeapprovallabExtPO;
import com.infodms.dms.po.TtAsWrForeapprovallabPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

/**
 * 
* @ClassName: LabourMaintainDAO 
* @Description: TODO(预授权工时维护) 
* @author wangchao 
* @date Jun 18, 2010 4:37:15 PM 
*
 */
public class LabourMaintainDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(LabourMaintainDAO.class);
    private static final LabourMaintainDAO dao = new LabourMaintainDAO ();
	public static final LabourMaintainDAO getInstance() {
	   if(dao==null) return new LabourMaintainDAO();
	   return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<TtAsWrForeapprovallabExtPO> queryLabour(Map<String,String> map,Long oemCompanyId, int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select t.*,g.WRGROUP_CODE as model_code from TT_AS_WR_FOREAPPROVALLAB t \n");
		sqlStr.append(" left outer join TT_AS_WR_MODEL_GROUP g on g.WRGROUP_ID=t.WRGROUP_ID \n");
		sqlStr.append(" where 1=1 ");
		if (Utility.testString(map.get("OPERATION_CODE"))) {
			sqlStr.append(" and t.OPERATION_CODE like ? ");
			params.add("%"+map.get("OPERATION_CODE")+"%");
		}
		if (Utility.testString(map.get("OPERATION_DESC"))) {
			sqlStr.append(" and t.OPERATION_DESC like ? ");
			params.add("%"+map.get("OPERATION_DESC")+"%");
		}
		if (Utility.testString(map.get("WRGROUP_ID"))) {
			sqlStr.append(" and t.WRGROUP_ID = ? ");
			params.add(map.get("WRGROUP_ID"));
		}	
		sqlStr.append(" ORDER BY t.id DESC ");
		PageResult<TtAsWrForeapprovallabExtPO> ps = pageQuery(TtAsWrForeapprovallabExtPO.class,sqlStr.toString(),params,pageSize,curPage);
		return ps;
	}
	
	public static PageResult <Map<String,Object>> getCanLabourDispatchList(Long oemCompanyId ,String labourOperationNo,String labourOperationName,String wrgroupId,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.ID, G.WRGROUP_CODE, B.OPERATION_CODE, B.OPERATION_DESC \n");
		sql.append("  FROM TT_AS_WR_FOREAPPROVALLAB B, TT_AS_WR_MODEL_GROUP G \n");  
		sql.append(" WHERE B.WRGROUP_ID = G.WRGROUP_ID\n");  
		sql.append("   AND NVL(B.IS_SEND, 0) <> "+Constant.DOWNLOAD_CODE_STATUS_03+"\n");  
		if (null != labourOperationNo && !"".equals(labourOperationNo)) {
			sql.append("   AND B.OPERATION_CODE LIKE '%"+labourOperationNo.trim()+"%'\n");
		}
		if (null != labourOperationName && !"".equals(labourOperationName)) {
			sql.append("   AND B.OPERATION_DESC LIKE '%"+labourOperationName.trim()+"%'\n");
		}
		if (null != wrgroupId && !"".equals(wrgroupId)) {
			sql.append("   AND B.WRGROUP_ID ="+wrgroupId+"\n");
		}
		sql.append(" ORDER BY G.CREATE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null,"com.infodms.dms.dao.claim.preAuthorization.getCanLabourDispatchList", pageSize, curPage);
	}
	/**
	 * 
	* @Title: getExistPO 
	* @Description: TODO(根据车型组id和工时代码查询监控工时记录) 
	* @param @param id
	* @param @param code
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getExistPO(String id,String code){
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" SELECT * FROM TT_AS_WR_FOREAPPROVALLAB WHERE 1=1 ");
		sb1.append(" AND WRGROUP_ID='"+id+"' ");
		sb1.append(" and OPERATION_CODE='"+code+"' ");
		//sb1.append(" and is_del = 0 ");
		return select(TtAsWrForeapprovallabPO.class, sb1.toString(), null);
	}
}
