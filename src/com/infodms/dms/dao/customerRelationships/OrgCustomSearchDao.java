package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class OrgCustomSearchDao extends BaseDao{

	private static final OrgCustomSearchDao dao = new OrgCustomSearchDao();
	
	public static final OrgCustomSearchDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryOrgCustomSearch(String parentID,String orderName,String da,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.ORG_CODE ORG_CODE,t.ORG_NAME ORG_NAME,t.STATUS STATUS,torg.ORG_NAME PARENT_ORG_NAME \r\n");
		sql.append(" from TM_ORG_CUSTOM t\r\n");
		sql.append(" left join TM_ORG_CUSTOM torg on torg.ORG_ID = t.PARENT_ORG_ID ");
		sql.append(" START WITH t.PARENT_ORG_ID ="+parentID+"\r\n");
		sql.append(" CONNECT BY PRIOR  t.ORG_ID = t.PARENT_ORG_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.ORG_ID "); 
		
		String sqlStr = OrderUtil.addOrderBy(sql.toString(), orderName, da);

		return (PageResult<Map<String, Object>>)this.pageQuery(sqlStr,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public PageResult<Map<String,Object>> queryOrgCustom(String parentID,String orgCode,String orderName,String da,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.ORG_CODE ORG_CODE,t.ORG_NAME ORG_NAME,t.STATUS STATUS,torg.ORG_NAME PARENT_ORG_NAME \r\n");
		sql.append(" from TM_ORG_CUSTOM t\r\n");
		sql.append(" left join TM_ORG_CUSTOM torg on torg.ORG_ID = t.PARENT_ORG_ID ");
		sql.append(" START WITH t.PARENT_ORG_ID ="+parentID+"\r\n");
		sql.append(" CONNECT BY PRIOR  t.ORG_ID = t.PARENT_ORG_ID \r\n");
		sql.append(" ORDER BY t.ORG_NAME "); 
		
		StringBuffer sqlStr = new StringBuffer(OrderUtil.addOrderBy(sql.toString(), orderName, da));

		if(!XHBUtil.IsNull(orgCode)){
			sqlStr.insert(0, "select * from (");
			sqlStr.append(") x where x.ORG_CODE LIKE '%"+orgCode+"%'");
		}
		return (PageResult<Map<String, Object>>)this.pageQuery(sqlStr.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	/**根据orgid查询客户关系
	 * @return List<Map<String,Object>>
	 */
	public Map<String,Object> getOrgCustomByOrgId(Long orgid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.ORG_CODE ORG_CODE,t.ORG_NAME ORG_NAME,t.STATUS STATUS,\r\n");
		sql.append(" case when t.ORG_LEVEL = 1 then '部门' when t.ORG_LEVEL = 2 then '处级' end ORG_LEVEL,t.PARENT_ORG_ID PARENT_ORG_ID,torg.ORG_NAME PARENT_ORG_NAME \r\n ");
		sql.append(" from TM_ORG_CUSTOM t\r\n");
		sql.append(" left join TM_ORG_CUSTOM torg on torg.ORG_ID = t.PARENT_ORG_ID ");
		sql.append(" where t.ORG_ID = " + orgid);
		return dao.pageQueryMap(sql.toString(), null, this.getFunName());
	}
	
	
	/**
	 * 级联查询客户关系组织树结构
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getOrgCustomSelete(String level){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.ORG_CODE ORG_CODE,t.ORG_NAME ORG_NAME,t.STATUS STATUS,torg.ORG_NAME PARENT_ORG_NAME,t.PARENT_ORG_ID PARENT_ORG_ID,F_GET_POSE_LEVEL(t.ORG_ID,t.ORG_LEVEL) POSE_LEVEL \r\n");
		sql.append(" from TM_ORG_CUSTOM t\r\n");
		sql.append(" left join TM_ORG_CUSTOM torg on torg.ORG_ID = t.PARENT_ORG_ID ");
		sql.append(" where t.status="+Constant.STATUS_ENABLE +" and level in ("+level+") \r\n");
		sql.append(" START WITH t.PARENT_ORG_ID =-1\r\n");
		sql.append(" CONNECT BY PRIOR  t.ORG_ID = t.PARENT_ORG_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.ORG_NAME "); 
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> getOrgCustomSeleteOrgInfo(String level,String orgCode,String orgName){
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id ORG_ID,t.ORG_CODE ORG_CODE,t.ORG_NAME ORG_NAME,t.STATUS STATUS,torg.ORG_NAME PARENT_ORG_NAME,t.PARENT_ORG_ID PARENT_ORG_ID \r\n");
		sql.append(" from TM_ORG_CUSTOM t\r\n");
		sql.append(" left join TM_ORG_CUSTOM torg on torg.ORG_ID = t.PARENT_ORG_ID ");
		sql.append(" where t.status="+Constant.STATUS_ENABLE +" and level in ("+level+") \r\n");
		sql.append(" START WITH t.PARENT_ORG_ID =-1\r\n");
		sql.append(" CONNECT BY PRIOR  t.ORG_ID = t.PARENT_ORG_ID \r\n");
		sql.append(" ORDER SIBLINGS BY t.ORG_NAME "); 
		if(!XHBUtil.IsNull(orgCode) || !XHBUtil.IsNull(orgName)){
			sql.insert(0, "select * from (");
			sql.append(") t where 1=1");
			if(!XHBUtil.IsNull(orgCode)){
				sql.append(" and t.ORG_CODE like '%"+orgCode+"%'");
			}
			if (!XHBUtil.IsNull(orgName)) {
				sql.append(" and t.ORG_NAME like '%"+orgName+"%'");
			}
		}
		return dao.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public Map<String,Object> getUser(Long userid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * \r\n");
		sql.append(" from tc_user t\r\n");
		sql.append(" where t.user_id="+userid);
		return dao.pageQueryMap(sql.toString(), null, this.getFunName());
	}
	
	/**
	 * 
	* @Title: queryOrgCustomUser 
	* @author: xyfue
	* @Description: 查询部门组织人员
	* @param @param parentID
	* @param @param orgCode
	* @param @param orderName
	* @param @param da
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @date 2014年11月18日 下午7:32:40 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String,Object>> queryOrgCustomUser(RequestWrapper request , AclUserBean logonUser,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM (SELECT  TU.USER_ID,TU.NAME,AA.POSE_TYPE,LISTAGG(AA.POSE_NAME,  ',')  within  group  (order  by  AA.POSE_NAME) POSE_NAME, LISTAGG(TOC.ORG_NAME ,',')  within  group  (order  by TOC.ORG_NAME )    ORG_NAME,LISTAGG(AA.POSE_CODE,  ',')  within  group  (order  by  AA.POSE_CODE)    POSE_CODE   FROM  TM_ORG_CUS_USER_RELATION  T2 \n");
		sql.append("   LEFT  JOIN  TC_USER  TU  ON  TU.USER_ID  =  T2.USER_ID \n");
		sql.append("   LEFT  JOIN  TM_ORG_CUSTOM  TOC  ON  TOC.ORG_ID  =  T2.ORG_ID \n");
		sql.append("   LEFT  JOIN  (SELECT  TUP.USER_ID,MAX(TP.POSE_TYPE)  POSE_TYPE,LISTAGG(TP.POSE_NAME,  ',')  within  group  (order  by  TP.POSE_NAME)    POSE_NAME,LISTAGG(TP.POSE_CODE,  ',')  within  group  (order  by  TP.POSE_CODE)    POSE_CODE      FROM  TR_USER_POSE  TUP \n");
		sql.append("   LEFT  JOIN  TC_POSE  TP  ON  TP.POSE_ID  =  TUP.POSE_ID \n");
		sql.append("   WHERE  TP.POSE_TYPE  =  10021001 \n");
		sql.append("   GROUP  BY  TUP.USER_ID,TP.POSE_ID)  AA  ON  AA.USER_ID  =  TU.USER_ID \n");
		sql.append("   WHERE  1=1 \n");
		sql.append("   AND TU.USER_STATUS = 10011001 and TU.user_type=10021001 \n");
		DaoFactory.getsql(sql, "T2.ORG_ID", CommonUtils.checkNull(request.getParamValue("FUNS")), 6);
		DaoFactory.getsql(sql, "TU.NAME ", CommonUtils.checkNull(request.getParamValue("USERNAME")), 2);
		DaoFactory.getsql(sql, "TU.ACNT ", CommonUtils.checkNull(request.getParamValue("USERCODE")), 2);
		sql.append("   GROUP BY TU.USER_ID,TU.NAME,AA.POSE_TYPE  ) BB  \n");
		sql.append("   ORDER  BY  BB.POSE_NAME \n");
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

}
