/**********************************************************************
* <pre>
* FILE : PreclaimSearchDao.java
* CLASS : PreclaimSearchDao
*
* AUTHOR : PGM
*
* FUNCTION : 索赔预授权.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-22| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PreclaimSearchDao.java,v 1.2 2010/08/30 14:22:53 yuch Exp $
 */
package com.infodms.dms.dao.claim.preAuthorization;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.PreclaimAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrForeapprovalitemPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  索赔预授权---索赔预授权审核作业
 * @author        :  PGM
 * CreateDate     :  2010-06-22
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PreclaimSearchDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PreclaimSearchDao.class);
	private static final PreclaimSearchDao dao = new PreclaimSearchDao ();
	public  static final PreclaimSearchDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 索赔预授权---索赔预授权审核作业
	 * @param           : 经销商代码
	 * @param           : 经销商名称
	 * @param           : 申请开始、结束日期
	 * @param           : 预授权单号
	 * @param           : 维修工单号
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的索赔预授权信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 索赔预授权
	 */
	public  PageResult<Map<String, Object>>  getAllPreclaimAuditQuery(Long companyId,String dealerCode,String dealerName,String startdate,String enddate,String foNo ,String roNo, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	 select   a.ID, a.RO_NO,  a.FO_NO, c.DEALER_CODE,  c.DEALER_SHORTNAME,  a.vin, to_char(a.APPROVAL_DATE, 'yyyy-MM-dd') as APPROVAL_DATE  \n");
		sql.append("     FROM TT_AS_WR_FOREAPPROVAL a, TM_DEALER c   \n");
		//modify at 2010-07-19 start 
		sql.append("     where 1=1 \n");
		//modify end
		sql.append("     AND a.DEALER_ID = c.DEALER_ID  AND a.REPORT_STATUS IN ("+Constant.PRE_AUTH_STATUS_02+")  \n");//已接收
		if(!"".equals(dealerCode)&&!(null==dealerCode)){//经销商代码不为空
			String returnStr = "";
			String[] paramStrArr = dealerCode.split(",");
			if(paramStrArr.length > 0){
				sql.append(" and (");
				for(int i=0;i<paramStrArr.length;i++){
					returnStr+=" c.dealer_code='"+paramStrArr[i]+"' or\n";
				}
				sql.append(returnStr.substring(0, returnStr.length()-3));
				sql.append(")\n");				
			}			
		}
		if(!"".equals(dealerName)&&!(null==dealerName)){//经销商名称不为空
			sql.append("		AND UPPER(c.DEALER_NAME) like UPPER('%"+ dealerName+"%')\n");
		}
		if(!"".equals(startdate)&&null!=startdate){      //申请开始日期不为空
			sql.append("		AND a.APPROVAL_DATE  >=to_date('"+startdate+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(enddate)&&null!=enddate){         //申请结束日期不为空
			sql.append("	    AND a.APPROVAL_DATE  <= to_date('"+enddate+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(foNo)&&!(null==foNo)){			//预授权单号不为空
			sql.append("		AND UPPER(a.FO_NO) like UPPER('%"+foNo+"%')\n");
		}
		if(!"".equals(roNo)&&!(null==roNo)){			//维修工单号不为空
			sql.append("		AND UPPER(a.RO_NO) like UPPER('%"+roNo+"%')\n");
		}
		sql.append("            order by a.ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业--[审核]基本信息查询
	 * @param         :  request-ID
	 * @return        :  索赔预授权
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public  PreclaimAuditBean  preclaimAuditInfo(Long companyId, String id){
		StringBuilder sql = new StringBuilder();
		
		//sql.append("   SELECT a.ID,  d.dealer_code,  d.dealer_shortname, a.ro_no, to_char(a.KEEP_BEG_DATE, 'yyyy-MM-dd') as KEEP_BEG_DATE,  to_char(a.IN_FACTORY_DATE, 'yyyy-MM-dd') as IN_FACTORY_DATE,   \n");
		//sql.append("   a.IN_MILEAGE, a.DEST_CLERK, a.vin,  c.LICENSE_NO,  c.ENGINE_NO,bgn.brand_group_name, sgn.serises_group_name, mgn.model_group_name,   \n");
		//sql.append("   a.DELIVERER,  to_char(a.APPROVAL_DATE, 'yyyy-MM-dd') as APPROVAL_DATE,  a.DELIVERER_PHONE   \n");
		//sql.append("   FROM tt_as_wr_foreapprovalitem b, TM_VEHICLE  c, TM_DEALER  d,  tt_as_wr_foreapproval   a   \n");
		//sql.append("   left outer join  (select e.group_name as brand_group_name,f.vin from tm_vhcl_material_group e, TM_VEHICLE f  where  e.group_level=1  and f.model_id = e.group_id) bgn on a.vin=bgn.vin   \n");
		//sql.append("   left outer join   (select h.group_name as serises_group_name,g.vin from tm_vhcl_material_group h, TM_VEHICLE g where h.group_level= 2  and g.model_id = h.group_id) sgn on a.vin=sgn.vin   \n");
		//sql.append("   left outer join  (select distinct j.group_name as model_group_name,i.vin from tm_vhcl_material_group j, TM_VEHICLE i where j.group_level = 3 and i.model_id = j.group_id) mgn  on a.vin =mgn.vin   \n");
		//sql.append("   WHERE b.FID = a.ID and a.vin = c.vin and a.dealer_id = d.dealer_id \n");
		sql.append("   select t1.RO_NO,  to_char(t1.KEEP_BEG_DATE,'yyyy-MM-dd') as KEEP_BEG_DATE, to_char(t1.IN_FACTORY_DATE,'yyyy-MM-dd') as IN_FACTORY_DATE , t1.IN_MILEAGE,  \n");
		sql.append("   t1.DEST_CLERK, to_char(t1.APPROVAL_DATE,'yyyy-MM-dd') as APPROVAL_DATE, t1.approval_person, t1.approval_phone,  \n");
		sql.append("  t1.VIN, t1.LICENSE_NO, t2.ENGINE_NO, d.dealer_code,  d.dealer_shortname,  \n");
		sql.append("  t3.BRAND_NAME ,  t3.SERIES_NAME  , t3.MODEL_NAME,to_char(t1.OUT_DATE,'yyyy-MM-dd') as OUT_DATE,t1.OUT_PERSON,t1.OUT_FEE,  \n");
		sql.append("  t1.approval_type,t1.yieldly \n");
		sql.append("   from tt_as_wr_foreapproval t1,  tm_vehicle t2, vw_material_group  t3, TM_DEALER  d   \n");
		//modify at 2010-07-19 start 
		sql.append("     where 1=1 \n");
		//modify end
		sql.append("   and t1.vin = t2.vin   and d.dealer_id = t1.dealer_id and t2.model_id = t3.MODEL_ID  and t2.series_id = t3.SERIES_ID    and t2.package_id = t3.PACKAGE_ID \n");
		if (id!=null&&!("").equals(id)){
		sql.append("  and t1.id =  ");
		sql.append(id);
		}
		PreclaimAuditBean auditBean=new PreclaimAuditBean();
		PageResult<PreclaimAuditBean> rs = pageQuery(PreclaimAuditBean.class,sql.toString(), null, 10, 1);
		List<PreclaimAuditBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				auditBean = ls.get(0);
			}
		}
		return auditBean;
	}
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业--[审核]查询
	 * @param         :  request-ID
	 * @return        :  索赔预授权
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public  List<PreclaimAuditBean>  preclaimAuditInfoList(String id){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select  rownum as rum ,a.ITEM_ID, a.ITEM_TYPE,a.ITEM_CODE,a.ITEM_DESC , a.dealer_remark   \n");
		sql.append("  from TT_AS_WR_FOREAPPROVALITEM a where 1=1  \n");
		if (id!=null&&!("").equals(id)){
		sql.append("  and  a.FID = ? ");
		params.add(id);
		}
		sql.append("order by rum asc,a.ITEM_TYPE desc ");
		List list = dao.select(PreclaimAuditBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业[审核]--维护授权代码、备注
	 * @param         :  request-授权代码、审批意见
	 * @return        :  索赔预授权
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public static void preclaimAuditDetialAdd(String id,String []itemId,String []authCode,String []status) throws   ParseException {
		//修改主表状态为已审核
		TtAsWrForeapprovalPO approvalPO =new TtAsWrForeapprovalPO();//条件
		approvalPO.setId(Long.parseLong(id));
		TtAsWrForeapprovalPO approvalPOCon =new TtAsWrForeapprovalPO();//内容
		approvalPOCon.setReportStatus(Constant.PRE_AUTH_STATUS_03);//已审核
		dao.update(approvalPO, approvalPOCon);
		//修改子表状态为审批意见
		for (int i = 0;i<authCode.length;i++) {
			TtAsWrForeapprovalitemPO itemPO =new TtAsWrForeapprovalitemPO();//条件
			if(!"".equals(itemId)&&null!=itemId){
			itemPO.setItemId(Long.parseLong(itemId[i]));
			}
			TtAsWrForeapprovalitemPO itemPOContent =new TtAsWrForeapprovalitemPO();//内容
			itemPOContent.setAuthCode(authCode[i]);
			itemPOContent.setStatus(Integer.parseInt(status[i]));
			dao.update(itemPO, itemPOContent);
		}
    }
	
	/**
	 *                                     
	* @Title: queryAttById 
	* @Description: TODO(通过ID查询附件) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<FsFileuploadPO>    返回类型 
	* @throws
	 */
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		//sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 " );
		sql.append(" AND A.YWZJ='"+id+"'");
		ls = select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
}