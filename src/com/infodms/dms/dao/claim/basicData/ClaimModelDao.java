/**   
* @Title: ClaimModelDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(车型组维护DAO) 
* @author wangjinbao   
* @date 2010-6-4 上午08:53:11 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimModelDao 
 * @Description: TODO(车型组维护DAO) 
 * @author wangjinbao 
 * @date 2010-6-4 上午08:53:11 
 *  
 */
public class ClaimModelDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimModelDao.class);
	private static final ClaimModelDao dao = new ClaimModelDao ();
	public static final ClaimModelDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: claimModelQuery  
	* @Description: 车型组查询
	* @param  pageSize           ：每页显示条数
	* @param  curPage            ：当前页数
	* @param  whereSql           ：查询条件
	* @param  params             ：查询条件对应的参数
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	*   
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimModelQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;

		StringBuffer sb = new StringBuffer("\n") ;
		sb.append("select tg.group_count,\n") ;
		sb.append("       tg.wrgroup_id,\n") ;
		sb.append("       tg.wrgroup_type,\n") ;
		sb.append("       tg.wrgroup_code,\n") ;
		sb.append("       tg.wrgroup_name,\n") ;
		sb.append("       tg.new_car_fee,\n") ;
		sb.append("       tg.free,\n") ;
		sb.append("       tg.end_mileage,\n") ;
		sb.append("       tg.max_days\n") ;
		sb.append("  from (select tawmg.wrgroup_id,\n") ;
		sb.append("               tawmg.wrgroup_type,\n") ;
		sb.append("               tawmg.wrgroup_code,\n") ;
		sb.append("               tawmg.wrgroup_name,\n") ;
		sb.append("               tawmg.oem_company_id,\n") ;
		sb.append("               tawmg.new_car_fee,\n") ;
		sb.append("               tawmg.free,\n") ;
		sb.append("               taq.end_mileage,\n") ;
		sb.append("               taq.max_days,\n") ;
		sb.append("               nvl(tawmi.group_count, 0) group_count\n") ;
		sb.append("          from TT_AS_WR_MODEL_GROUP tawmg\n") ;
		sb.append("          left outer join (SELECT a.WRGROUP_ID, count(1) group_count\n") ;
		sb.append("                            from TT_AS_WR_MODEL_ITEM a\n") ;
		sb.append("                           group by a.WRGROUP_ID) tawmi\n") ;
		sb.append("            on tawmg.wrgroup_id = tawmi.wrgroup_id\n") ;
		sb.append("          left outer join tt_as_wr_qamaintain taq\n") ;
		sb.append("            on taq.id = tawmg.qamaintain_id) tg\n") ;
		sb.append(" where 1 = 1\n") ;

		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tg.wrgroup_id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: claimModelUpdate 
	* @Description: TODO(车型组修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimModelUpdate(TtAsWrModelGroupPO selpo,TtAsWrModelGroupPO updatepo){
		dao.update(selpo, updatepo);
	}
	/**
	 * 
	* @Title: getClaimModel 
	* @Description: TODO(取对应车型组的车型详细信息) 
	* @param @param id
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimModel(Long id,Integer type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tvmg.group_code,tvmg.group_name from TM_VHCL_MATERIAL_GROUP tvmg ");
		sb.append(" where  tvmg.group_id in ( select tawmi.model_id ");
		sb.append(" from TT_AS_WR_MODEL_GROUP tawmg left outer join TT_AS_WR_MODEL_ITEM tawmi  ");
		sb.append(" on tawmg.wrgroup_id = tawmi.wrgroup_id ");
		sb.append("	where tawmg.wrgroup_id = ?  ");
		params.add(id);
		sb.append(" and tawmg.wrgroup_type = ? ");
		params.add(type);
		sb.append(" )");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params, getFunName());
		return list;
	}

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}
	/**
	 * 物料组维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaterialGroupManageQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String groupCode = (String) map.get("groupCode");
		String groupName = (String) map.get("groupName");
		String parentGroupCode = (String) map.get("parentGroupCode");
		String status = (String) map.get("status");
		String companyId = (String) map.get("companyId");
		String forcast_flag = (String)map.get("forcast_flag");
		String ifType= (String)map.get("ifType");
		StringBuffer sql = new StringBuffer();

		
		sql.append("SELECT TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME, TVMG1.STATUS, TVMG1.GROUP_LEVEL,TVMG1.FORCAST_FLAG,\n");
		sql.append("	TU.NAME,\n");
		sql.append(" decode(TVMG1.GROUP_LEVEL,4 ");
		sql.append(" ,TVMG2.GROUP_NAME||'/'||TVMG1.GROUP_NAME ");
		sql.append(" ,TVMG1.GROUP_NAME) ");
		sql.append(" as \"ALL_NAME\", ");
		sql.append("      TVMG1.UPDATE_DATE\n");

		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1, TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("	TC_USER                TU\n");
		if(Constant.IF_TYPE_NO.toString().equalsIgnoreCase(ifType)){
			sql.append("  ,  tt_as_wr_model_item mi  ");
		}
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID(+)\n");
		if(Constant.IF_TYPE_NO.toString().equalsIgnoreCase(ifType)){
			sql.append("   and TVMG1.group_id = mi.model_id(+)  and mi.model_id is null  and TVMG1.Group_Level=4 ");
		} 
		if (!groupCode.equals("")) {
			sql.append("   AND TVMG1.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!groupName.equals("")) {
			sql.append("   AND TVMG1.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		sql.append("	AND TU.USER_ID(+) = TVMG1.UPDATE_BY\n");
		if (!status.equals("")) {
			sql.append("   AND TVMG1.STATUS =" + status + "\n");
		}
		if (!forcast_flag.equals("")) {
			sql.append("   AND TVMG1.FORCAST_FLAG =" + forcast_flag + "\n");
		}
		if (!companyId.equals("")) {
			sql.append("   AND TVMG1.COMPANY_ID =" + companyId + "\n");
		}
		if (!parentGroupCode.equals("")) {
			sql.append("   AND TVMG2.GROUP_CODE LIKE '%" + parentGroupCode
					+ "%'");
		}
		sql.append("ORDER BY TVMG1.GROUP_LEVEL,TVMG1.GROUP_CODE") ;
		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMaterialGroupManageQueryList",
				pageSize, curPage);
		return ps;
	}
	public TmVhclMaterialGroupPO getTmVhclMaterialGroupPO(
			TmVhclMaterialGroupPO po) {
		List<PO> list = dao.select(po);
		TmVhclMaterialGroupPO result = list.size() != 0 ? (TmVhclMaterialGroupPO) list
				.get(0)
				: null;
		return result;
	}
	/**
	 * 获得索赔车型组
	 * 
	 * @param modelId
	 * @param wrgroupType
	 * @return
	 */
	public Map<String, Object> getModelGroup(Long modelId, Integer wrgroupType) {

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TAWMI.MODEL_ID, TAWMI.WRGROUP_ID, TAWMG.WRGROUP_TYPE\n");
		sql
				.append("  FROM TT_AS_WR_MODEL_ITEM TAWMI, TT_AS_WR_MODEL_GROUP TAWMG\n");
		sql.append(" WHERE TAWMI.WRGROUP_ID = TAWMG.WRGROUP_ID\n");
		sql.append("   AND TAWMI.MODEL_ID = " + modelId + "\n");
		sql.append("   AND TAWMG.WRGROUP_TYPE = " + wrgroupType);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getModelGroup");
		return list.size() != 0 ? (Map<String, Object>) list.get(0) : null;
	}
	/**
	 * 根据物料组ID获取该物料组的所有颜色
	 */
	public List<Map<String,Object>> getMaterialGroupColor(String groupId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TGC.COLOR_ID,TGC.GROUP_ID,             \n");
		sql.append("       TGC.COLOR_CODE,TGC.COLOR_NAME,         \n");
		sql.append("       TGC.CREATE_DATE,(SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TGC.CREATE_BY) CREATE_BY,      \n");
		sql.append("       TGC.UPDATE_DATE,(SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TGC.UPDATE_BY) UPDATE_BY       \n");
		sql.append("FROM TM_GROUP_COLOR TGC                       \n");
		sql.append("WHERE TGC.GROUP_ID="+groupId+"                \n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获得索赔车型组列表
	 * 
	 * @param wrgroupType
	 * @return
	 */
	public List<Map<String, Object>> getModelGroupList(Integer wrgroupType,
			Long companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TAWMG.WRGROUP_ID, TAWMG.WRGROUP_NAME\n");
		sql.append("  FROM TT_AS_WR_MODEL_GROUP TAWMG\n");
		sql.append(" WHERE TAWMG.WRGROUP_TYPE = " + wrgroupType + "\n");
		sql.append(" AND TAWMG.OEM_COMPANY_ID = " + companyId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getModelGroupList");
		return list;
	}
	public String getMaterialGroupTreeCode(String parentId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT F_GET_MATERIALGROUPCODE('" + parentId
				+ "') CODE FROM DUAL\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMaterialGroupTreeCode");

		Map<String, Object> map = list.size() != 0 ? (Map<String, Object>) list
				.get(0) : null;
		String code = (map == null ? "" : (String) map.get("CODE"));
		return code;
	}
	/**
	 * 根据物料组id进行递归查询
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getAllMaterialGroup(String groupId){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT G.GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" START WITH G.GROUP_ID = "+groupId+"\n");  
		sql.append("CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/**
	 * 根据物料组id删除颜色
	 * @param groupId
	 */
	public void deleteColorByGroupId(String groupId){
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE TM_GROUP_COLOR TGC WHERE  TGC.GROUP_ID="+groupId+"  \n");
		dao.delete(sql.toString(), null);
	}

	/*
	 * 判断数据是否已经存在数据库中
	 */
	@SuppressWarnings("unchecked")
	public TtAsWrLabourPricePO existLaborPrice(TtAsWrLabourPricePO po){
		List<TtAsWrLabourPricePO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
    /**
     * 查询经销商dealer_id
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<TtAsWrLabourPricePO> getTTASWRLABOURPRICEdeacler(String groupCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct tt.dealer_id from  TT_AS_WR_LABOUR_PRICE tt where  tt.series_code='"+groupCode+"'");
		return this.select(TtAsWrLabourPricePO.class, sql.toString(), null);
	}
	

	//=======================================================================2015-4-15 lj
//	public void insertTtAsWrLabourPrice(AclUserBean logonUser,
//			RequestWrapper request) {
//		try{
//			String laborCode = request.getParamValue("laborCode");
//			String modelGroup2 = request.getParamValue("modelGroup2");//之前存在的车型组
//			String modelGroup1 = request.getParamValue("modelGroup1");//修改后的车型组
//			String parentGroupCode  = request.getParamValue("parentGroupCode");//上级物料组代码
//			String groupCode= request.getParamValue("groupCode");//修改后物料组代码
//			String groupCode1= request.getParamValue("groupCode1");//原物料组代码
//			System.out.println(groupCode+"=========================");
//			System.out.println(parentGroupCode+"=========================");
//		    List<TtAsWrLabourPricePO> list =	dao.getTTASWRLABOURPRICEdeacler(parentGroupCode);//查经销商编码
//			// 向TT_AS_WR_LABOUR_PRICE表中添加数据
//			for(int i = 0 ; i<list.size(); i++){
//				       TtAsWrLabourPricePO po = null ;
//						po = new TtAsWrLabourPricePO() ;
//						po.setDealerId(list.get(i).getDealerId());
//						po.setSeriesCode(parentGroupCode);//车系
//						po.setModeType(groupCode1);//物料组代码
//						po = dao.existLaborPrice(po);//查询是否存在该记录
//						// 此经销商对应的工时大类不存在则执行添加操作
//						if(po==null){   
//									StringBuffer sb = new StringBuffer();
//									sb.append(
//											"insert into  TT_AS_WR_LABOUR_PRICE select f_getid(), t.dealer_id,'"+groupCode+"',  max(t.labour_price), 1000002433,  null,  null,  null,    2010010100070674, null, null,'"+
//											parentGroupCode+"'  from TT_AS_WR_LABOUR_PRICE t where t.series_code = '"+
//											parentGroupCode+"' group by t.dealer_id");
//									dao.insert(sb.toString());
//								
//						}else{
//							// 此经销商对应的工时大类存在则执行修改操作
//							po = new TtAsWrLabourPricePO();
//							po.setUpdateBy(logonUser.getUserId());
//							po.setUpdateDate(new Date());
////							po.setLabourPrice(list.get(i).getLabourPrice());
//							po.setModeType(groupCode);
//							po.setSeriesCode(parentGroupCode);
//							TtAsWrLabourPricePO p1 = new TtAsWrLabourPricePO() ;
//							p1.setDealerId(list.get(i).getDealerId());
//							p1.setSeriesCode(laborCode);
//							p1.setSeriesCode(parentGroupCode);
//							p1.setModeType(groupCode1);
//							dao.update(p1, po);
//						}
//				}
//		} catch(Exception e){
//			BizException be = new BizException(ActionContext.getContext(),e,ErrorCodeConstant.ADD_FAILURE_CODE, "索赔基本参数设定->新增");
//			logger.error(logonUser, be);
//			ActionContext.getContext().setException(be);
//		}
//	}

	public List<Map<String, Object>> firstMainConditionQuery() {

		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select taq.id, taq.end_mileage, taq.max_days\n") ;
		sql.append("  from tt_as_wr_qamaintain taq\n") ;
		sql.append(" where taq.free_times = 1\n") ;

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
}
