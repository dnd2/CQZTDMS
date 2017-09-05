/**********************************************************************
* <pre>
* FILE : ServiceActivityManageDealerDao.java
* CLASS : ServiceActivityManageDealerDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---经销商管理
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
 * $Id: ServiceActivityManageDealerDao.java,v 1.11 2011/06/30 06:00:30 xiongc Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ActivityDealerBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---经销商管理
 * @author        :  PGM
 * CreateDate     :  2010-06-01
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageDealerDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageDealerDao.class);
	private static final ServiceActivityManageDealerDao dao = new ServiceActivityManageDealerDao ();
	public  static final ServiceActivityManageDealerDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---经销商管理
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageDealerQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.ACTIVITY_ID,  A.ACTIVITY_NAME, A.ACTIVITY_CODE, A.ACTIVITY_TYPE,  to_char(A.STARTDATE,'yyyy-MM-dd')as STARTDATE , to_char(A.ENDDATE ,'yyyy-MM-dd')as ENDDATE,  TO_CHAR(COUNT(B.DEALER_ID)) AS NUM  FROM TT_AS_ACTIVITY A  \n");
		sql.append("    LEFT OUTER JOIN TT_AS_ACTIVITY_DEALER B ON A.ACTIVITY_ID = B.ACTIVITY_ID   \n");
		sql.append("    WHERE A.STATUS in( "+Constant.SERVICEACTIVITY_STATUS_01+") AND A.IS_DEL="+Constant.IS_DEL_00+" \n");//服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  A.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(!"".equals(ActivityBean.getActivityCode())&&!(null==ActivityBean.getActivityCode())){//活动编号不为空
			sql.append("		AND UPPER(A.ACTIVITY_CODE) like UPPER('%"+ActivityBean.getActivityCode()+"%')\n");
		}
		if(!"".equals(ActivityBean.getStartdate())&&!(null==ActivityBean.getStartdate())){      //活动开始日期不为空
			sql.append("		AND A.STARTDATE >=to_date('"+ActivityBean.getStartdate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getEnddate())&&!(null==ActivityBean.getEnddate())){         //活动结束日期不为空
			sql.append("	    AND A.ENDDATE  <= to_date('"+ActivityBean.getEnddate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            GROUP BY A.ACTIVITY_ID, A.ACTIVITY_NAME, A.ACTIVITY_CODE,  A.ACTIVITY_TYPE, A.STARTDATE, A.ENDDATE   \n");
		sql.append("            ORDER BY trim(A.ACTIVITY_CODE) desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function         : 服务活动管理---查询参与本次活动的经销商
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getserviceActivityManageDealerMaintionQuery(com.infodms.dms.bean.TtAsActivityDealerBean DealerPO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT DEALER.DEALER_ID, DEALER.DEALER_CODE,DEALER.DEALER_NAME,DEALER.ACTIVITY_ID,TM.PHONE AS PHONE  from TT_AS_ACTIVITY_DEALER DEALER ,TM_DEALER TM WHERE TM.DEALER_ID = DEALER.DEALER_ID  \n");
		if(!"".equals(DealerPO.getActivityId())&&!(null==DealerPO.getActivityId())){//活动ID不为空
			sql.append("	 AND DEALER.ACTIVITY_ID ='"+DealerPO.getActivityId()+"' \n");
		}
		sql.append("        ORDER BY DEALER.DEALER_ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  删除服务活动管理---经销商管理
	 * @param         :  request-工单号
	 * @return        :  服务活动管理信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-03
	 */
	public static void  serviceActivityManageDealerDelete(String[] dealerIdArray,String activityId){
		for (int i = 0;i<dealerIdArray.length;i++) {
			TtAsActivityDealerPO  DealerPO =new TtAsActivityDealerPO();//dealerId条件
			DealerPO.setDealerId(Long.parseLong(dealerIdArray[i]));
			DealerPO.setActivityId(Long.parseLong(activityId));
			dao.delete(DealerPO);
		}
	}
	/**
	 * Function       :  修改服务活动管理---经销商管理（活动状态，将已经发布修改为重新发布）
	 * @param         :  request-工单号
	 * @return        :  服务活动管理信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-03
	 * @deprecated    ：
	 */
	public static void  serviceActivityManageDealerUpdate(String[] dealerIdArray,String activityId,Long updateBy){
		for (int i = 0;i<dealerIdArray.length;i++) {
			TtAsActivityPO ActivityPO=new TtAsActivityPO();//条件
			ActivityPO.setActivityId(Long.parseLong(activityId));
			ActivityPO.setStatus(Constant.SERVICEACTIVITY_STATUS_02);//10681002:已经发布
			TtAsActivityPO ActivityPOCon=new TtAsActivityPO();//内容
			//ActivityPOCon.setStatus(Constant.SERVICEACTIVITY_STATUS_03);//10681003:重新发布
			ActivityPOCon.setUpdateBy(updateBy);//修改人
			ActivityPOCon.setUpdateDate(new Date());//修改时间
			dao.update(ActivityPO,ActivityPOCon);
		}
	}
	/**
	 * Function       :  服务活动管理---经销商管理(查询经销商代码)
	 * @param         :  request-工单号
	 * @return        :  服务活动管理信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public List  serviceActivityManageDealerNoExitsQuery(String activityId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	select DEALER_CODE  from TT_AS_ACTIVITY_DEALER \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sql.append("	where ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("order by DEALER_CODE desc  \n");
        List list = dao.select(TtAsActivityDealerPO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务活动管理---经销商管理(查询未参加的活动)
	 * @param         :  request-经销商代码、经销商名称
	 * @return        :  服务活动管理信息[sql:1.查询大区下面的小区，小区下面的经销商（如果有小区）；2.查询大区下面的经销商；]
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-03
	 */
	public  PageResult<Map<String, Object>>  serviceActivityManageDealerNoQuery(List querylist,String dealerCode,String activityId,String dealerName,String orgId,String regionName, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer("\n");
		
		StringBuffer sb = new StringBuffer();
		sb.append("	( select DEALER_CODE  from TT_AS_ACTIVITY_DEALER \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sb.append("	where ACTIVITY_ID = "+activityId+" \n");
		}
		sb.append(") \n");
		
/*		sql.append("	 select  *  from (select DISTINCT TMD.DEALER_ID,TMD.DEALER_CODE,TMD.DEALER_NAME,TMD.PHONE,TMD.ADDRESS,TMO.ORG_ID,TMO.ORG_NAME,TMR.REGION_NAME\n");
		sql.append("from (select dealer_id, dealer_level, org dealer_01\n");
		sql.append("from (select d1.company_id,\n");
		sql.append("          d1.dealer_id,\n");
		sql.append("        '一级' dealer_level,\n");
		sql.append("        d1.dealer_id org\n");
		sql.append("    from tm_dealer d1, tm_dealer d2\n");
		sql.append("   where d1.dealer_type = 10771002\n");
		sql.append("      and d1.parent_dealer_d = -1\n");
		sql.append("     and d1.parent_dealer_d = d2.dealer_id(+) --一级\n");
		sql.append("   union all\n");
		sql.append("   select d1.company_id,\n");
		sql.append("         d1.dealer_id,\n");
		sql.append("     '二级' dealer_level,\n");
		sql.append("     d1.parent_dealer_d org\n");
		sql.append("   from tm_dealer d1, tm_dealer d2\n");
		sql.append(" where d1.dealer_type = 10771002\n");
		sql.append("    and d1.parent_dealer_d <> -1\n");
		sql.append("    and d1.parent_dealer_d = d2.dealer_id(+)\n");
		sql.append("    and d2.parent_dealer_d = -1 --二级\n");
		sql.append(" union all\n");
		sql.append(" select d1.company_id,\n");
		sql.append("         d1.dealer_id,\n");
		sql.append("         '三级' dealer_level,\n");
		sql.append("         d2.parent_dealer_d org\n");
		sql.append("    from tm_dealer d1, tm_dealer d2\n");
		sql.append("   where d1.dealer_type = 10771002\n");
		sql.append("     and d1.parent_dealer_d <> -1\n");
		sql.append("     and d1.parent_dealer_d = d2.dealer_id(+)\n");
		sql.append("      and d2.parent_dealer_d <> -1\n");
		sql.append("    --三级\n");
		sql.append("     )) t1,\n");
		sql.append("    TM_DEALER_ORG_RELATION TDOR,\n");
		sql.append("   TM_ORG TMO,\n");
       sql.append("   TM_DEALER TMD,\n");
       sql.append("   TM_REGION TMR\n");
       sql.append(" WHERE T1.DEALER_01 = TDOR.DEALER_ID\n");
       sql.append("  AND TMO.ORG_ID = TDOR.ORG_ID\n");
   sql.append("  AND TMD.DEALER_ID = T1.DEALER_01\n");
   sql.append(" AND TMD.PROVINCE_ID = TMR.REGION_ID(+)\n");//10771002 表示：售后服务
*/	
		sql.append("SELECT TMD.DEALER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_NAME,\n");  
		sql.append("       TMD.PHONE,\n");  
		sql.append("       TMD.ADDRESS,\n");  
		sql.append("       TMO.ORG_ID,\n");  
		sql.append("       TMO.ORG_NAME,\n");  
		sql.append("       TMR.REGION_NAME\n");  
		sql.append("  FROM TM_DEALER              TMD,\n");  
		sql.append("       TM_DEALER              TMD1,\n");  
		sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");  
		sql.append("       TM_REGION              TMR,\n");  
		sql.append("       TM_ORG                 TMO\n");  
		sql.append(" WHERE F_GET_PID(TMD.DEALER_ID) = TMD1.DEALER_ID\n");  
		sql.append("   AND TDOR.DEALER_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TMD.PROVINCE_ID = TMR.REGION_CODE\n");  
		sql.append("   AND TDOR.ORG_ID = TMO.ORG_ID\n");  
		sql.append("   AND TMD.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append(" \n");  
		sql.append("   AND TMD.STATUS = ").append(Constant.STATUS_ENABLE).append(" \n");  

		if(null!=regionName&&!"".equals(regionName)){//省份不为空
			sql.append("	and TMR.REGION_NAME like '%"+regionName+"%'  \n");
		}
		if(null!=dealerCode&&null==dealerName){//经销商代码不为空，经销商名称为空
			sql.append("	and   TMD.DEALER_CODE like '%"+dealerCode+"%' \n");
		}
		else if(null==dealerCode&&null!=dealerName){//经销商代码为空，经销商名称不为空
			sql.append("	and TMD.DEALER_NAME like  '%"+dealerName+"%' \n");
		}
		else if(null!=dealerCode&&null!=dealerName){//经销商代码不为空，经销商名称不为空
			sql.append("	and   TMD.DEALER_CODE like '%"+dealerCode+"%' and TMD.dealer_name like '%"+dealerName+"%' \n");
		}
		/*else{//经销商代码为空，经销商名称为空
			sql.append("	 ) aa \n");
		}*/
		/*sql.append("	 where aa.ORG_ID in (select ORG_ID from TM_ORG where 1=1  \n");*/
		sql.append("   AND  TMO.ORG_ID in (select ORG_ID from TM_ORG where 1=1  \n");
		if(!"".equals(orgId)&&null!=orgId&&!"1".equals(orgId)){//组织ID;不为1：表示全查
			sql.append("	and (PARENT_ORG_ID = "+orgId+"   \n");
		}
		if(!"".equals(orgId)&&null!=orgId&&!"1".equals(orgId)){//组织ID;不为1：表示全查
			sql.append("	or ORG_ID = "+orgId+" )   \n");
		}
		sql.append("	) \n");
		
			sql.append("and TMD.DEALER_CODE not in " + sb.toString());
			
		sql.append("order by TMD.DEALER_CODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function       :  增加服务活动管理---经销商信息
	 * @param         :  request-经销商ID
	 * @return        :  服务活动管理信息---经销商列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public static void serviceActivityManageDealerOption(String []groupIdsArray,String []dealerCodeArray,String []dealerNameArray,TtAsActivityDealerPO DealerPO) {
		if(groupIdsArray.length>0){
			for (int i = 0;i<groupIdsArray.length;i++) {
				DealerPO.setAdId(Long.parseLong(SequenceManager.getSequence("")));
				DealerPO.setDealerId(Long.parseLong(groupIdsArray[i]));
				DealerPO.setDealerCode(dealerCodeArray[i]);
				DealerPO.setDealerName(dealerNameArray[i]);
				dao.insert(DealerPO);
			}
		}
    }
	
	/**
	 * Function       :  服务活动管理---经销商管理区域代码
	 * @param         :  request
	 * @return        :  服务活动管理信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public List  serviceActivityManageAreaSelected(){
		StringBuffer sql = new StringBuffer();
		sql.append("	select  o.org_id,o.org_name from tm_org o where o.duty_type=10431003 \n");
		sql.append("order by o.org_id desc  \n");
        List listSelected = dao.select(TmOrgPO.class, sql.toString(), null);
        return listSelected;
	}
	
	/**
	* @Title: queryDealerByActivityId 
	* @Description: TODO(根据服务活动ID查询执行经销商) 
	* @param activityId 服务活动ID
	* @return List<Map<String, Object>>    执行经销商信息
	* @throws
	 */
	public List<Map<String, Object>> queryDealerByActivityId(Long activityId) {
		ServiceActivityManageDealerDao dao2 = ServiceActivityManageDealerDao.getInstance() ;
		List<TtAsActivityDealerPO> tdList = dao2.getArea(activityId.toString());
		StringBuffer sql= new StringBuffer();
		if(tdList.size()>0){
			sql.append("select ds.dealer_code,ds.dealer_name from vw_org_dealer_service ds where ds.root_org_id in (\n" );
			sql.append("select distinct d.area_id from tt_as_activity_dealer d where d.activity_id="+activityId+"  and d.area_id is not null )");
		}
		else{
		sql.append("SELECT DEALER_CODE, DEALER_NAME FROM TT_AS_ACTIVITY_DEALER \n");
		sql.append(" WHERE ACTIVITY_ID = ").append(activityId);
		}
        List<Map<String, Object>> map = pageQuery(sql.toString(), null, getFunName());
        return map;
	}
	
	/*
	 * 主页面第一次查询
	 */
	public PageResult<Map<String,Object>> mainQUery(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select x.activity_id,\n" );
		sql.append("       x.activity_code,\n" );
		sql.append("       x.activity_name,\n" );
		sql.append("       min(z.SUBJECT_NO) SUBJECT_NO,\n" );
		sql.append("       min(z.SUBJECT_NAME) SUBJECT_NAME,\n" );
		sql.append("       count(y.dealer_id)as dealer_sum,\n" );
		sql.append("       count(distinct y.area_id) as area_sum,\n" );
		sql.append("       to_char(x.startdate,'yyyy-MM-dd') as startdate,\n" );
		sql.append("       to_char(x.enddate,'yyyy-MM-dd') as enddate\n" );
		sql.append("  from tt_as_activity x,TT_AS_ACTIVITY_SUBJECT z ,tt_as_activity_dealer y\n" );
		sql.append("   where x.activity_id = y.activity_id(+)\n" );
		sql.append("  AND z.SUBJECT_ID = x.SUBJECT_ID");
		sql.append(" and x.is_del=0  ");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" group by (x.activity_id, x.activity_code, x.activity_name,x.startdate,x.enddate)\n");
		sql.append(" order by x.activity_code desc\n");
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 服务活动管理--区域维护前置查询
	 */
	public List<ActivityDealerBean> getAreaList(String activityId){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select distinct a.org_id, a.org_code, a.org_name\n" );
		sql.append("  from tm_org a\n" );
		sql.append(" where org_type=").append(Constant.ORG_TYPE_OEM).append("\n");
		if(StringUtil.notNull(activityId)){
			sql.append("and org_id in\n" );
			sql.append("       (select area_id from tt_as_activity_dealer where activity_id =").append(activityId).append(")\n");
		}
		sql.append(" order by org_code asc\n");
		return this.select(ActivityDealerBean.class, sql.toString(), null);
	}
	
	/*
	 * 区域选择框查询方法
	 */
	public PageResult<TmOrgPO> getOrg(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select distinct a.org_id, a.org_code, a.org_name\n" );
		sql.append("  from tm_org a\n" );
		sql.append(" where org_type=").append(Constant.ORG_TYPE_OEM).append("\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" order by org_code asc\n");
		return this.pageQuery(TmOrgPO.class, sql.toString(), null, pageSize, curPage);
	}
	
	/*
	 * 根据区域ＩＤ查询下属所有经销商
	 */
	public List<TmDealerPO> getDealer(String orgId){ //YH 2010.11.29
		StringBuilder sql= new StringBuilder("\n");
		sql.append("  select * from tm_dealer d where d.dealer_id in( select T1.DEALER_ID from ( \n" );
		sql.append(" select dealer_id, dealer_level, org dealer_01,DEALER_NAME from ( \n" );
		sql.append(" select d1.company_id, d1.dealer_id,'一级' dealer_level,d1.dealer_id org,d1.dealer_name from \n" );
		sql.append(" tm_dealer d1, tm_dealer d2 where d1.dealer_type = 10771002 and d1.parent_dealer_d = -1 \n");
        sql.append(" and d1.parent_dealer_d = d2.dealer_id(+) union all \n");
        sql.append("  select d1.company_id,d1.dealer_id,'二级' dealer_level,d1.parent_dealer_d org,d1.dealer_name \n");
        sql.append(" from tm_dealer d1, tm_dealer d2 where d1.dealer_type = 10771002 and d1.parent_dealer_d <> -1 \n");
        sql.append(" and d1.parent_dealer_d = d2.dealer_id(+) and d2.parent_dealer_d = -1 union all \n");
        sql.append(" select d1.company_id,d1.dealer_id,'三级' dealer_level,d2.parent_dealer_d org,d1.dealer_name \n");
        sql.append(" from tm_dealer d1, tm_dealer d2 where d1.dealer_type = 10771002 and d1.parent_dealer_d <> -1 \n");
        sql.append("  and d1.parent_dealer_d = d2.dealer_id(+) and d2.parent_dealer_d <> -1 )) t1,TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO \n");
        sql.append(" WHERE T1.DEALER_01=TDOR.DEALER_ID AND TMO.ORG_ID=TDOR.ORG_ID AND TDOR.ORG_ID = '");       
		sql.append(orgId).append("') \n");
		return this.select(TmDealerPO.class, sql.toString(), null);
	}
	
	/*
	 * 根据区域ＩＤ查询下属所有经销商2
	 */
	public List<TmDealerPO> getDealerFromORG(String orgId){
		StringBuilder sql= new StringBuilder("\n");
		sql.append(" select d.* from vw_org_dealer_service v,tm_dealer d where d.dealer_type ="+Constant.DEALER_TYPE_DWR+" and d.dealer_id = v.dealer_id and v.root_org_id = ").append(orgId);
		return this.select(TmDealerPO.class, sql.toString(), null);
	}
	
	/*
	 * 根据服务活动ID查询它所维护的所有区域
	 */
	public List<TtAsActivityDealerPO> getArea(String activityId){
		StringBuilder sql= new StringBuilder("\n");
		sql.append(" select distinct d.area_id from tt_as_activity_dealer d where d.activity_id = ").append(activityId);
		return this.select(TtAsActivityDealerPO.class, sql.toString(), null);
	}
}