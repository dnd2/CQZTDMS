/**********************************************************************
* <pre>
* FILE : ServiceActivityManageModelDao.java
* CLASS : ServiceActivityManageModelDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理---车型列表.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageModelDao.java,v 1.9 2011/02/15 09:40:54 zuoxj Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsActivityMgroupPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.exceptions.DAOException;
/**
 * Function       :  服务活动管理---车型列表
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageModelDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ServiceActivityManageModelDao.class);
	
	private static final ServiceActivityManageModelDao dao = new ServiceActivityManageModelDao ();
	
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public  static final ServiceActivityManageModelDao getInstance() {
		return dao;
	}
	/**
	 * Function         : 服务活动管理---车型列表[车系]
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-06-09
	 */
	public List serviceActivityGroupName(String STRATEGYID){
		StringBuffer sql = new StringBuffer();
		sql.append("select g.group_id,g.group_code,g.group_name,g.parent_group_id  from tm_vhcl_material_group g where g.group_level=2 \n");
		sql.append("order by  g.group_id desc  \n");
        List list = dao.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
        return list;
	}
	public List serviceActivityGroupName(){
		StringBuffer sql = new StringBuffer();
		sql.append("select g.group_id,g.group_code,g.group_name,g.parent_group_id  from tm_vhcl_material_group g where g.group_level=2 \n");
		sql.append("order by  g.group_id desc  \n");
        List list = dao.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
        return list;
	}
	/**
	 * Function         : 服务活动管理---车型列表[车型组]
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-11-24 YH
	 */
	public List getCXZName(){
		StringBuffer sql = new StringBuffer();
		sql.append("select  w.wrgroup_id,w.wrgroup_code,w.wrgroup_name from tt_as_wr_model_group w \n");
		sql.append("order by  w.wrgroup_id desc  \n");
        List zlist = dao.select(TtAsWrModelGroupPO.class, sql.toString(), null);
        return zlist;
	}
	
	/**
	 * Function         : 通过车系ID得到子车型组ID(售后)
	 * @return          : Vector
	 * @throws          : 
	 * LastUpdate       : 2010-11-24 YH
	 */
	public List getShCxzidsByCxid(Long cxid){		
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct wm.wrgroup_id from TT_AS_WR_MODEL_ITEM wm,TT_AS_WR_MODEL_GROUP wg where wm.model_id in \n");
		sql.append("(SELECT mg.group_id FROM TM_VHCL_MATERIAL_GROUP mg\n");
		sql.append(" where mg.parent_group_id = '"+cxid+"')  and wm.wrgroup_id = wg.wrgroup_id and wg.wrgroup_type = '10451001' ");
		List list = dao.pageQuery(sql.toString(),null,getFunName());	
		return list;
	}
	

	/**
	 * Function         : 通过车系ID得到子车型组ID(销售)
	 * @return          : Vector
	 * @throws          : 
	 * LastUpdate       : 2010-11-24 YH
	 */
	public List getXsCxzidsByCxid(Long cxid){		
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct wm.wrgroup_id from TT_AS_WR_MODEL_ITEM wm,TT_AS_WR_MODEL_GROUP wg where wm.model_id in \n");
		sql.append("(SELECT mg.group_id FROM TM_VHCL_MATERIAL_GROUP mg\n");
		sql.append(" where mg.parent_group_id = '"+cxid+"')  and wm.wrgroup_id = wg.wrgroup_id and wg.wrgroup_type = '10451002' ");
		List list = dao.pageQuery(sql.toString(),null,getFunName());	
		return list;
	}	
	/**
	 * Function         : 服务活动管理---车型列表[车型]
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-11-24 YH
	 */
	public List getCXName(){
		StringBuffer sql = new StringBuffer();
		sql.append("select g.group_id,g.group_code,g.group_name,g.parent_group_id  from tm_vhcl_material_group g where g.group_level=3 \n");
		sql.append("order by  g.group_id desc  \n");
        List cxlist = dao.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
        return cxlist;
	}
		
	/**
	 * Function         : 服务活动管理---车型列表(售后)
	 * @param           ：车系
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车型列表
	 */
	public  PageResult<Map<String, Object>>  getAllserviceActivityManageModelInfo(String vehicleSeriesList,String cxzid,String cxname, int curPage,int pageSize) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  mg.GROUP_ID, wg.WRGROUP_NAME,mg2.group_code as PARENT_CODE,  mg.REMARK, mg.CREATE_DATE, mg.UPDATE_BY, mg.CREATE_BY,wg.FREE,   \n");
		sql.append("  mg.UPDATE_DATE, mg.GROUP_LEVEL, mg.GROUP_CODE, mg.GROUP_NAME, mg.PARENT_GROUP_ID, mg.STATUS   \n");
		sql.append("  FROM  TM_VHCL_MATERIAL_GROUP mg,TT_AS_WR_MODEL_ITEM wm,TT_AS_WR_MODEL_GROUP wg,TM_VHCL_MATERIAL_GROUP mg2 \n");
		sql.append("  WHERE mg.group_id = wm.model_id AND wm.wrgroup_id = wg.wrgroup_id and mg.parent_group_id = mg2.group_id and wg.wrgroup_type = '10451001' \n");//2 表示：车系; 3表示车型
		
		if(!"".equals(vehicleSeriesList)&&!(null==vehicleSeriesList)){//车系 1:表示请选择
			sql.append(" AND mg.PARENT_GROUP_ID = "+vehicleSeriesList+"  \n");		
		}
		if(!"".equals(cxzid)&&!(null==cxzid)){//车型组 
			sql.append(" AND wg.wrgroup_id = "+cxzid+"  \n");	
		}
		if(!"".equals(cxname)&&!(null==cxname)){ //车型
			sql.append(" AND mg.group_name like '%"+cxname+"%'  \n");
		}
		sql.append("  ORDER BY wg.WRGROUP_ID desc, mg.GROUP_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), 10, curPage);
		
		return ps;
	}
	/**
	 * Function         : 服务活动管理---车型列表(销售)
	 * @param           ：车系
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车型列表
	 */
	public  PageResult<Map<String, Object>>  getAllserviceActivityManageModelInfo2(String vehicleSeriesList,String cxzid,String cxname, int curPage,int pageSize) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  mg.GROUP_ID, wg.WRGROUP_NAME,mg2.group_code as PARENT_CODE,  mg.REMARK, mg.CREATE_DATE, mg.UPDATE_BY, mg.CREATE_BY,   \n");
		sql.append("  mg.UPDATE_DATE, mg.GROUP_LEVEL, mg.GROUP_CODE, mg.GROUP_NAME, mg.PARENT_GROUP_ID, mg.STATUS   \n");
		sql.append("  FROM  TM_VHCL_MATERIAL_GROUP mg,TT_AS_WR_MODEL_ITEM wm,TT_AS_WR_MODEL_GROUP wg,TM_VHCL_MATERIAL_GROUP mg2 \n");
		sql.append("  WHERE mg.group_id = wm.model_id AND wm.wrgroup_id = wg.wrgroup_id and mg.parent_group_id = mg2.group_id and wg.wrgroup_type = '10451001' \n");//2 表示：车系; 3表示车型
		
		if(!"".equals(vehicleSeriesList)&&!(null==vehicleSeriesList)){//车系 1:表示请选择
			sql.append(" AND mg.PARENT_GROUP_ID = "+vehicleSeriesList+"  \n");		
		}
		if(!"".equals(cxzid)&&!(null==cxzid)){//车型组 
			sql.append(" AND wg.wrgroup_id = "+cxzid+"  \n");	
		}
		if(!"".equals(cxname)&&!(null==cxname)){ //车型
			sql.append(" AND mg.group_name like '%"+cxname+"%'  \n");
		}
		sql.append("  ORDER BY mg.GROUP_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), 300, curPage);
		
		return ps;
	}
	
	public  PageResult<Map<String, Object>>  getAllserviceActivityManageModelInfo2(String vehicleSeriesList,String cxzid,String cxname,String strategyId, int curPage,int pageSize) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  mg.GROUP_ID, wg.WRGROUP_NAME,mg2.group_code as PARENT_CODE,  mg.REMARK, mg.CREATE_DATE, mg.UPDATE_BY, mg.CREATE_BY,   \n");
		sql.append("  mg.UPDATE_DATE, mg.GROUP_LEVEL, mg.GROUP_CODE, mg.GROUP_NAME, mg.PARENT_GROUP_ID, mg.STATUS   \n");
		sql.append("  FROM  TM_VHCL_MATERIAL_GROUP mg,TT_AS_WR_MODEL_ITEM wm,TT_AS_WR_MODEL_GROUP wg,TM_VHCL_MATERIAL_GROUP mg2 \n");
		sql.append("  WHERE mg.group_id = wm.model_id AND wm.wrgroup_id = wg.wrgroup_id and mg.parent_group_id = mg2.group_id and wg.wrgroup_type = '10451001' \n");//2 表示：车系; 3表示车型
		if(strategyId != null && strategyId.length()>0)
		{
			sql.append(" and  mg.GROUP_ID not in (SELECT B.MODEL_ID from TT_AS_WR_GAMEMODEL B where B.GAME_ID  = "+strategyId+")");
		}
		
		if(!"".equals(vehicleSeriesList)&&!(null==vehicleSeriesList)){//车系 1:表示请选择
			sql.append(" AND mg.PARENT_GROUP_ID = "+vehicleSeriesList+"  \n");		
		}
		if(!"".equals(cxzid)&&!(null==cxzid)){//车型组 
			sql.append(" AND wg.wrgroup_id = "+cxzid+"  \n");	
		}
		if(!"".equals(cxname)&&!(null==cxname)){ //车型
			sql.append(" AND mg.group_name like '%"+cxname+"%'  \n");
		}
		sql.append("  ORDER BY mg.GROUP_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), 300, curPage);
		
		return ps;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * Function       :  由车厂端[服务营销部]增加服务活动管理信息---车型列表
	 * @param         :  request-车型代码、车型名称
	 * @return        :  服务活动管理信息---车型列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public static void serviceActivityManageModelOption(String []groupIdsArray,TtAsActivityMgroupPO MgroupPO) {
		if(!"".equals(groupIdsArray)&&null!=groupIdsArray){
			TtAsActivityMgroupPO MgPo=new TtAsActivityMgroupPO();
			MgPo.setActivityId(MgroupPO.getActivityId());
			dao.delete(MgPo);
			for (int i = 0;i<groupIdsArray.length;i++) {
				//如果此车型与此服务活动已关联则不执行任何操作
				String sql = " select * from tt_as_activity_mgroup g where g.activity_id = "+MgroupPO.getActivityId()+" and g.material_group_id = "+groupIdsArray[i] ;
				if(dao.select(sql,null,TtAsActivityMgroupPO.class).size()<=0){
					MgroupPO.setId(Long.parseLong(SequenceManager.getSequence("")));
					MgroupPO.setMaterialGroupId(Long.parseLong(groupIdsArray[i]));
					dao.insert(MgroupPO);
				}
			}
		}else{
			TtAsActivityMgroupPO MgPo=new TtAsActivityMgroupPO();
			MgPo.setActivityId(MgroupPO.getActivityId());
			dao.delete(MgPo);
		}
    }
	public static void deleteAllModels(String group_id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("delete from tt_as_activity_mgroup mg\n");
		sql.append(" where mg.material_group_id in\n");  
		sql.append("       (select group_id\n");  
		sql.append("          from tm_vhcl_material_group g\n");  
		sql.append("         where g.parent_group_id =\n");  
		sql.append("               (select parent_group_id\n");  
		sql.append("                  from tm_vhcl_material_group\n");  
		sql.append("                 where group_id = "+group_id+"))\n");
		dao.delete(sql.toString(),null);
	}
	/**
	 * Function         : 服务活动管理---查询车型列表信息回显
	 * @param           ：车系
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车型列表
	 */
	public  List<TtAsActivityBean>  getAllserviceActivityManageModelList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT  ID, CREATE_DATE,  UPDATE_BY,  CREATE_BY,  UPDATE_DATE,  MATERIAL_GROUP_ID, ACTIVITY_ID  \n");
		sql.append("    FROM  TT_AS_ACTIVITY_MGROUP \n");
		if(!"".equals(activityId)&&!(null==activityId)){//车系
			sql.append("		WHERE ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("            ORDER BY ACTIVITY_ID DESC  \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function         : 查询物料组弹出树
	 * @param           : 职位ID
	 */
	public static List<TmVhclMaterialGroupPO> getMaterialGroupTree(Long poseId,String groupLevel)
	{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT GROUP_ID,GROUP_NAME,GROUP_CODE,PARENT_GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");  
		sql.append(" WHERE T.STATUS="+Constant.STATUS_ENABLE+" \n"); 
		if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		{
			sql.append(" AND T.GROUP_LEVEL <= "+groupLevel+"\n");
		}
		sql.append(" ORDER BY GROUP_ID \n");
		List<TmVhclMaterialGroupPO> list=factory.select(sql.toString(), null,
				new com.infoservice.po3.core.callback.DAOCallback<TmVhclMaterialGroupPO>() {
			public TmVhclMaterialGroupPO wrapper(ResultSet rs, int idx) {
				TmVhclMaterialGroupPO bean = new TmVhclMaterialGroupPO();
				try {
					bean.setGroupId(Long.valueOf(rs.getString("GROUP_ID")));
					if(rs.getString("PARENT_GROUP_ID")!=null)
					{
						bean.setParentGroupId(Long.valueOf(rs.getString("PARENT_GROUP_ID")));	
					}else
					{
						bean.setParentGroupId(new Long(-1));
					}
					
					bean.setGroupName(rs.getString("GROUP_NAME"));
					bean.setGroupCode(rs.getString("GROUP_CODE"));
				} catch (SQLException e) {
					throw new DAOException(e);
				}
				return bean;
			}
		});
		return list;
	}
	/**
	 * Function         : 查询配置
	 * @param           : 车系、车型、配置ID
	 * @param           : 车系、车型、配置CODE
	 * @param           : 车系、车型、配置名称
	 */
	public static PageResult<Map<String, Object>> getGroupList(String groupId,String groupCode,String groupName,String groupLevel,int curPage,int pageSize)
	{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");  
		sql.append("       T.GROUP_NAME,\n");  
		sql.append("       T.PARENT_GROUP_ID,\n");  
		sql.append("       T.TREE_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP T\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(!"".equals(groupCode)&&groupCode!=null)
		{
		sql.append("   AND T.GROUP_CODE LIKE '%"+groupCode+"%'\n");	
		}
		if(!"".equals(groupName)&&groupName!=null)
		{
		sql.append("   AND T.GROUP_NAME LIKE '%"+groupName+"%'\n"); 
		}
		if(!"".equals(groupLevel)&&groupLevel!=null&&!"null".equals(groupLevel))
		{
		sql.append("   AND T.GROUP_LEVEL = "+groupLevel+"\n");
		}
		sql.append("   AND T.STATUS = "+Constant.STATUS_ENABLE+"\n");
		if(!"".equals(groupId)&&groupId!=null)
		{
			if("".equals(groupLevel)||groupLevel==null||"null".equals(groupLevel))
		  {
				sql.append(" AND  T.PARENT_GROUP_ID = "+groupId+"\n");  
		  }else
		  {
			   sql.append(" START WITH T.GROUP_ID = "+groupId+"\n");  
			   sql.append("CONNECT BY PRIOR T.GROUP_ID = T.PARENT_GROUP_ID\n"); 	  
		  }
		
		}
		sql.append(" ORDER BY T.GROUP_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize, curPage);
		return rs;
	}
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> invoiceQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from tt_invoice_taxrate ip ");
		sb.append(" where 1=1   ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}		
		sb.append(" order by ip.id desc ");//是否排序
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	
}