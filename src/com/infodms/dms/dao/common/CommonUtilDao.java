package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.vehicleInfoManage.apply.QualityInfoReport;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.QualityReportInfoMaintasinPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;

import flex.messaging.io.ArrayList;

@SuppressWarnings("unchecked")
public class CommonUtilDao extends BaseDao {

    private static final CommonUtilDao dao = new CommonUtilDao();

    public static final CommonUtilDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }
    
    /**
     * 查询所有大区(及顶级昌河)
     *
     * @param : @return List<TmOrgPO>
     * @return : List<TmOrgPO>
     *         LastDate    : 2013-4-17
     * @Title : queryTmOrg
     * @Description: 查询所有大区(及顶级昌河)
     * @author wangming
     */
    public List<TmOrgPO> queryTmOrgAndTop() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_ORG org where org.org_type = 10191001 and org.org_level in(1,2) and org.status = " + Constant.STATUS_ENABLE);
        List<TmOrgPO> list = this.select(TmOrgPO.class, sql.toString(), null);
        return list;
    }

    /**
     * 查询所有大区内容
     *
     * @param : @return List<TmOrgPO>
     * @return : List<TmOrgPO>
     *         LastDate    : 2013-4-17
     * @Title : queryTmOrgPO
     * @Description: 查询所有大区内容
     * @author wangming
     */
    public List<TmOrgPO> queryTmOrgPO() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_ORG org where org.org_type = 10191001 and org.org_level =2 and org.status = " + Constant.STATUS_ENABLE);
        List<TmOrgPO> list = this.select(TmOrgPO.class, sql.toString(), null);
        return list;
    }
    
    /**
     * 查询所有小区内容
     *
     * @param : @return List<TmOrgPO>
     * @return : List<TmOrgPO>
     *         LastDate    : 2013-6-3
     * @Title : queryTmOrgPO
     * @Description: 查询所有小区内容
     * @author wangming
     */
    public List<TmOrgPO> querySmallTmOrgPO() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_ORG org where org.org_type = 10191001 and org.org_level =3 and org.status = " + Constant.STATUS_ENABLE);
        List<TmOrgPO> list = this.select(TmOrgPO.class, sql.toString(), null);
        return list;
    }
    
    /**
     * 查询所有小区内容
     *
     * @param : @return List<Map<String, Object>>
     * @return : List<Map<String, Object>>
     *         LastDate    : 2013-6-3
     * @Title : querygetSmallTmOrg
     * @Description: 查询所有小区内容
     * @author wangming
     */
    public List<Map<String, Object>> querygetSmallTmOrg() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct to_char(org.ORG_ID) ORG_ID,org.ORG_NAME ORG_NAME FROM tm_ORG org where org.org_type = 10191001 and org.org_level =3 and org.status = " + Constant.STATUS_ENABLE);
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * 查询所有车系
     *
     * @return :  List<TmVhclMaterialGroupPO>
     * @throws : LastDate    : 2013-4-17
     * @Title : getAllSeries
     * @Description: 查询所有车系
     * @author wangming
     */
    public List<TmVhclMaterialGroupPO> getAllSeries() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * from tm_vhcl_material_group t where t.group_level = 2 and t.status=" + Constant.STATUS_ENABLE);
        List<TmVhclMaterialGroupPO> list = this.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
        return list;
    }

    /**
     * 级联操作车种车型配置等
     *
     * @return :  List<TmVhclMaterialGroupPO>
     * @throws : LastDate    : 2013-4-17
     * @Title : getAllModel
     * @Description: 查询所有车型
     * @author wangming
     */
    public List<TmVhclMaterialGroupPO> getAllModel() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * from tm_vhcl_material_group t where t.group_level = 3 and t.status=" + Constant.STATUS_ENABLE + " ORDER BY 2 ");
        List<TmVhclMaterialGroupPO> list = this.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
        return list;
    }

    /**
     * 级联操作车种车型配置等
     *
     * @param : parentGroupId 父类Id
     * @return :  List<TmVhclMaterialGroupPO>
     * @throws : LastDate    : 2013-4-17
     * @Title : cascadeVhclMaterialGroup
     * @Description: 级联操作车种车型配置等
     * @author wangming
     */
    public List<Map<String, Object>> cascadeVhclMaterialGroup(long parentGroupId) {
        StringBuffer sql = new StringBuffer();
//        sql.append("select distinct * from tm_vhcl_material_group t where t.parent_group_id = " + parentGroupId + " and t.status=" + Constant.STATUS_ENABLE);
//        List<TmVhclMaterialGroupPO> list = this.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
//        // 艾春 9.24 修改 车系带出车型
        sql.append("SELECT to_char(t.group_id) GROUPID, t.group_name GROUPNAME FROM tm_vhcl_material_group t where t.parent_group_id = " + parentGroupId + " and t.status=" + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 查询所有产地
     *
     * @return : List<TmBusinessAreaPO>
     *         LastDate    : 2013-4-17
     * @Title : getYieldly
     * @Description: 查询所有产地
     * @author wangming
     */
    public List<TmBusinessAreaPO> getYieldly() {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tm_business_area t where  t.status=" + Constant.STATUS_ENABLE);
        List<TmBusinessAreaPO> list = this.select(TmBusinessAreaPO.class, sql.toString(), null);
        return list;
    }

    /**
     * 查询所有省份集合
     *
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : getProvice
     * @Description: 查询所有省份集合
     * @author wangming
     */
    public List<Map<String, Object>> getProvice() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_region t WHERE t.region_type = 10541002 and t.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 查询所有的城市
     *
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : getAllCity
     * @Description: 查询所有的城市
     * @author wangming
     */
    public List<Map<String, Object>> getAllCity() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_region t WHERE t.region_type = 10541003 and t.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 查询所有的区县
     *
     * @param : @return List<Map<String,Object>>
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : getAllTown
     * @Description: 查询所有的区县
     * @author wangming
     */
    public List<Map<String, Object>> getAllTown() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tm_region t WHERE t.region_type = 10541004 and t.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 级联操作地区内容
     *
     * @param : codeId 地区CODE
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : cascadeRegion
     * @Description: 级联操作地区内容
     * @author wangming
     */
    public List<Map<String, Object>> cascadeRegion(Integer codeId) {
        StringBuffer sql = new StringBuffer();
        // 艾春 2013.11.26 修改省份查询城市的级联方法
        sql.append("select distinct * FROM tm_region t WHERE t.parent_id in (select c.region_id from tm_region c where c.region_code = '" + codeId + "') and t.status = " + Constant.STATUS_ENABLE);
//        sql.append("select distinct * FROM tm_region t WHERE t.parent_id = (select c.region_id from tm_region c where c.region_code = " + codeId + ") and t.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 查询车厂
     *
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-19
     * @Title : cascadeRegion
     * @Description: 查询车厂
     * @author wangming
     */
    public List<Map<String, Object>> getVehicleCompany() {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * from tm_org org where org.org_type = 10191001 and org.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 根据省份CODE查询经销商
     *
     * @param : codeId 地区CODE
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : cascadeRegion
     * @Description: 级联操作地区内容
     * @author wangming
     */
    public List<Map<String, Object>> cascadeDealer(Integer codeId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct to_char(t.DEALER_ID) DEALERID,t.* FROM tm_dealer t WHERE t.province_id = " + codeId + " and t.status = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据大区ID查询大区下的用户
     *
     * @param : orgid 大区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : cascadeOrgUser
     * @Description: 根据大区ID查询大区下的用户
     * @author wangming
     */
    public List<Map<String, Object>> cascadeOrgUser(Long orgid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct tcuser.* from tc_pose pose \r\n");
        sql.append(" left join tr_user_pose userP on userP.Pose_Id = pose.pose_id \r\n");
        sql.append(" left join tc_user tcuser on tcuser.user_id = userP.User_Id \r\n");
        sql.append(" where pose.org_id = "+orgid+"and tcuser.user_status = "+Constant.STATUS_ENABLE); 

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据用户ID查询属于个大区ID(车厂大区)
     *
     * @param : userid 用户ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : getUserOrg
     * @Description: 根据用户ID查询属于个大区
     * @author wangming
     */
    public List<Map<String, Object>> getUserOrg(Long userid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct pose.org_id ORGID from tc_pose pose \r\n");
        sql.append(" left join tr_user_pose userP on userP.Pose_Id = pose.pose_id \r\n");
        sql.append(" where userP.User_Id = "+userid); 

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据用户ID查询属于个大区ID(经销商大区)
     *
     * @param : userid 用户ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-27
     * @Title : getUserOrgForDealer
     * @Description: 根据用户ID查询属于个大区
     * @author wangming
     */
    public List<Map<String, Object>> getUserOrgForDealer(Long userid) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select distinct d.parent_org_id ORGID \r\n");
    	sql.append(" 		from vw_org_dealer_service t, TM_COMPANY A, TM_DEALER B, TC_USER C,tm_org d\r\n");
    	sql.append("			where A.COMPANY_ID = B.COMPANY_ID\r\n");
    	sql.append("  			AND A.COMPANY_ID = C.COMPANY_ID\r\n");
    	sql.append("  			and t.dealer_id = b.dealer_id\r\n");
    	sql.append("			and t.org_id = d.org_id"); 
    	sql.append("  and c.user_id="+userid); 

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据用户ID查询属于个大区ID(处大区)
     *
     * @param : userid 用户ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-27
     * @Title : getUserOrgForAdminDepart
     * @Description: 根据用户ID查询属于个大区
     * @author wangming
     */
    public List<Map<String, Object>> getUserOrgForDepart(Long userid) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select distinct a.org_id ORGID \r\n");
    	sql.append(" 		from tm_org_custom a, tm_org_cus_user_relation b \r\n");
    	sql.append("			where a.org_id = b.org_id\r\n");
    	sql.append("  			and b.user_id = "+userid );

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    
    /**
     * 根据用户ID查询属于个大区及级别(处大区)
     *
     * @param : userid 用户ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-27
     * @Title : getUserOrgForAdminDepart
     * @Description: 根据用户ID查询属于个大区ID及级别(处大区)
     * @author wangming
     */
    public List<Map<String, Object>> getUserOrgLevel(Long userid) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select distinct a.org_id ORGID,b.OCULR_LEVEL OCULRLEVEL \r\n");
    	sql.append(" 		from tm_org_custom a, tm_org_cus_user_level_relation b \n");
    	sql.append("			where a.org_id = b.org_id\r\n");
    	sql.append("  			and b.user_id ="+userid ); 

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    /**
     * 根据TC_CODE type值查询type下的基础数据
     *
     * @param : type ->TC_CODE type值
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-19
     * @Title : getTcCode
     * @Description: 根据TC_CODE type值查询type下的基础数据
     * @author wangming
     */
    public List<Map<String, Object>> getTcCode(String type) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tc_code t WHERE t.type = " + type + " and t.status = " + Constant.STATUS_ENABLE+" order by t.num ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    public List<Map<String, Object>> getCode(String type) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct * FROM tc_code t WHERE t.type = " + type + " and t.status = " + Constant.STATUS_ENABLE +"");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 获取父类描述
     * @param id
     * @return
     */
    public Map<String, Object> getBizContentDetail(String id){
    	String sql = "select b.type_name,b.code_desc from tc_code a left join tc_code b on a.code_parent_id=b.code_id where a.code_id=?";
    	List<Object> params = new java.util.ArrayList<Object>();
    	params.add(id);
    	return pageQueryMap(sql, params, this.getFunName());
    }

    /**
     * 配件可维护变量(包装单位、发运方式、装箱方式、服务商类型、人员类型等)公用取值语句
     *
     * @param : Integer (Constant.java    9225 )
     * @return : List
     *         LastDate    : 2013-4-25
     * @Description: 根据type从TT_PART_FIXCODE_DEFINE查询数据
     * @author
     */
    public List getPartParaList(Integer type) throws Exception {
        try {
            TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
            po.setFixGouptype(type);
            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * 根据大区ID查询大区下的省份
     *
     * @param : orgid 大区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-4-17
     * @Title : cascadeOrgUser
     * @Description: 根据大区ID查询大区下的省份
     * @author wangming
     */
	public List<Map<String, Object>> cascadeOrgPro(long orgId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct tr.region_id,tr.region_name from tm_ORG_region_relation relation ");
		sql.append(" inner join tm_region tr on relation.region_id=tr.region_id ");
		sql.append(" where relation.org_id = "+orgId);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
    /**
     * 根据大区ID查询大区下的小区
     *
     * @param : orgid 大区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-7-30
     * @Title : cascadeOrgSmallOrg
     * @Description: 根据大区ID查询大区下的小区
     * @author wangming
     */
	public List<Map<String, Object>> cascadeOrgSmallOrg(long orgId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct to_char(t.org_id) ORG_ID,t.org_name ORG_NAME \r\n");
		sql.append("  from tm_org t\r\n");
		sql.append(" where t.parent_org_id = "+orgId+"\r\n");
		sql.append("   and t.status = "+Constant.STATUS_ENABLE); 
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	/**
	 * 根据经销商ID查询经销商下的人员
	 * @param dealerid 经销商ID
	 * @return List<Map<String, Object>>
	 * @author wangming
	 */
	public List<Map<String, Object>> cascadeDealerUser(long dealerid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct C.* from TM_COMPANY A,TM_DEALER B,TC_USER C\r\n");
		sql.append("WHERE A.COMPANY_ID=B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID\r\n");
		sql.append("AND B.DEALER_ID="+dealerid); 
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
    /**
     * 根据小区ID查询经销商
     *
     * @param : orgid 小区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-3
     * @Title : cascadeRegion
     * @Description: 根据小区ID查询经销商
     * @author wangming
     */
    public List<Map<String, Object>> cascadeOrgDealer1(Long cityid) {
        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT DISTINCT TO_CHAR(T.ROOT_DEALER_ID) DEALER_ID,T.ROOT_DEALER_NAME ||decode(t.dealer_type, 10771002, '（售后）', '（销售）') DEALER_NAME  FROM VW_ORG_DEALER_SERVICE T where t.org_id ="+orgid+" and dealer_type = "+Constant.MSG_TYPE_2 );
        sql.append("SELECT TO_CHAR(T.DEALER_ID) DEALER_ID, DECODE(T.DEALER_TYPE, 10771002, '（售后', '（销售') || '-' || DECODE(T.DEALER_LEVEL, 10851001, '一级）', '二级）')||T.DEALER_NAME DEALER_NAME FROM VW_ORG_DEALER_SERVICE T where t.city_id ="+cityid+" order by 2");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据小区ID查询经销商
     *
     * @param : orgid 小区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-3
     * @Title : cascadeRegion
     * @Description: 根据小区ID查询经销商
     * @author wangming
     */
    public List<Map<String, Object>> cascadeOrgDealer(Long orgid) {
        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT DISTINCT TO_CHAR(T.ROOT_DEALER_ID) DEALER_ID,T.ROOT_DEALER_NAME ||decode(t.dealer_type, 10771002, '（售后）', '（销售）') DEALER_NAME  FROM VW_ORG_DEALER_SERVICE T where t.org_id ="+orgid+" and dealer_type = "+Constant.MSG_TYPE_2 );
        sql.append("SELECT TO_CHAR(T.DEALER_ID) DEALER_ID, T.DEALER_CODE DEALER_CODE, DECODE(T.DEALER_TYPE, 10771002, '（售后', '（销售') || '-' || DECODE(T.DEALER_LEVEL, 10851001, '一级）', '二级）')||T.DEALER_NAME DEALER_NAME FROM VW_ORG_DEALER_SERVICE T where t.org_id ="+orgid+" order by 2");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    /**
     * 根据小区ID查询经销商
     *
     * @param : orgid 小区ID
     * @return : List<Map<String,Object>>
     *         LastDate    : 2013-6-3
     * @Title : cascadeRegion
     * @Description: 根据小区ID查询经销商
     * @author wangming
     */
    public List<Map<String, Object>> cascadeCity(Long orgid) {
        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT DISTINCT TO_CHAR(T.ROOT_DEALER_ID) DEALER_ID,T.ROOT_DEALER_NAME ||decode(t.dealer_type, 10771002, '（售后）', '（销售）') DEALER_NAME  FROM VW_ORG_DEALER_SERVICE T where t.org_id ="+orgid+" and dealer_type = "+Constant.MSG_TYPE_2 );
        sql.append("SELECT DISTINCT TO_CHAR(T.City_Id) CITY_ID, T.CITY_NAME FROM VW_ORG_DEALER_SERVICE T WHERE T.ORG_ID = "+orgid+" order by 2");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }
    
    public List<Map<String, Object>> getSeatAdminDepart() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct a.org_id,a.org_name \r\n");
		sql.append(" from tm_org_custom a, tm_org_cus_user_relation b, Tt_Crm_Seats c\r\n");
		sql.append(" where a.org_id = b.org_id \r\n"); 
		sql.append(" and b.user_id = c.se_user_id\r\n");
		sql.append(" and c.se_is_manamger = "+Constant.se_is_manamger_1);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
    /**
     * 艾春9.24 修改 查询坐席回访操作
     * @return 管理员操作类型
     */
	public List<Map<String, Object>> getSeatAdminDepartAc() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.ORG_ID, A.ORG_NAME\n" +
					"  FROM TM_ORG_CUSTOM A, TM_ORG_CUS_USER_RELATION B, TT_CRM_SEATS C\n" + 
					" WHERE A.ORG_ID = B.ORG_ID\n" + 
					"   AND B.USER_ID = C.SE_USER_ID\n" + 
					"   AND C.SE_IS_MANAMGER = "+Constant.se_is_manamger_1+"\n" + 
					"UNION\n" + 
					"SELECT TO_NUMBER(T.CODE_ID), T.CODE_DESC\n" + 
					"  FROM TC_CODE T\n" + 
					" WHERE T.CODE_ID = 95301001\n" + 
					" ORDER BY 1");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	//服务营销处大区
	public List<Map<String, Object>> getSeatDepart() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct a.org_id,a.org_name \r\n");
		sql.append(" from tm_org_custom a, tm_org_cus_user_relation b, Tt_Crm_Seats c\r\n");
		sql.append(" where a.org_id = b.org_id \r\n"); 
		sql.append(" and b.user_id = c.se_user_id\r\n");
		sql.append(" and a.org_id = 2013070519381899"); 
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	//服务营销处大区下的坐席人员
	public List<Map<String, Object>> getSeatUser(String orgid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.user_id USERID,b.se_name USERNANE FROM tm_org_cus_user_relation a,tt_crm_seats b where a.user_id=b.se_user_id(+)");
		sql.append(" and a.org_id = '"+orgid+"' and b.se_status = " +Constant.STATUS_ENABLE);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	

	public List<Map<String, Object>> getAllDealer() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct to_char(t.DEALER_ID) DEALER_ID,t.DEALER_CODE DEALER_CODE,t.DEALER_NAME DEALER_NAME FROM tm_dealer t WHERE t.status = " + Constant.STATUS_ENABLE);
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}

	public List<Map<String, Object>> getAllOrg() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct org.org_id,org.org_name FROM tm_ORG org where org.org_type = 10191001 and org.org_level =2 and org.status = " + Constant.STATUS_ENABLE );
		sql.append(" union all select t.org_id,t.org_name from tm_org_custom t where t.status = " + Constant.STATUS_ENABLE );
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getOrgByLv(String orgId,String level){
		StringBuffer sql = new StringBuffer("select distinct to_char(org_id) org_id,org_name from tm_org where org_type = 10191001 and status = "+Constant.STATUS_ENABLE);
		List<Object> params = new ArrayList();
		if (!XHBUtil.IsNull(orgId)) {
			sql.append(" and parent_org_id = ?");
			params.add(orgId);
		}
		if (!XHBUtil.IsNull(level)) {
			sql.append(" and org_level = ?");
			params.add(level);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}
	
	public List<Map<String, Object>> getOrgDealer(String orgId){
		StringBuffer sql = new StringBuffer("select * from VW_ORG_DEALER_SERVICE where 1=1");
		List<Object> params = new java.util.ArrayList<Object>();
		if (orgId != null) {
			sql.append(" and org_id = ?");
			params.add(orgId);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}
	public List<Map<String, Object>> getRootOrgDealer(String rootOrgId){
		StringBuffer sql = new StringBuffer("select * from VW_ORG_DEALER_SERVICE where 1=1");
		List<Object> params = new java.util.ArrayList<Object>();
		if (rootOrgId != null) {
			sql.append(" and ROOT_org_id = ?");
			params.add(rootOrgId);
		}
		return this.pageQuery(sql.toString(), params, this.getFunName());
	}
	public List<Map<String, Object>> getOfficeOrg() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id,t.org_name from tm_org_custom t where t.status = " + Constant.STATUS_ENABLE );
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	public List<Map<String, Object>> getOfficeOrgSale() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.org_id,t.org_name from tm_org_custom t where org_id = 2014050994697313 and t.status = " + Constant.STATUS_ENABLE );
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getDealerParOrg(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select o.parent_org_id ORGID\n");
        sql.append("from tm_org o ,tm_dealer d ,tm_dealer_org_relation od\n");
        sql.append("where o.org_id=od.org_id and d.dealer_id  =od.dealer_id\n");
        sql.append("and d.dealer_id="+dealerId); 


        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}
	
	public Long getID(){
		StringBuffer sql = new StringBuffer();
        sql.append("select f_getid id from dual");

        Map<String, Object> list = this.pageQueryMap(sql.toString(), null, this.getFunName());
        return Long.parseLong(list.get("ID").toString());
	}

	public List<Map<String, Object>> cascadePartMarker(long markerid) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT distinct to_char(D.MAKER_ID) MAKER_ID,D.MAKER_NAME MAKER_NAME\r\n");
		sbSql.append("  FROM TT_PART_MAKER_RELATION R, TT_PART_MAKER_DEFINE D\r\n");
		sbSql.append(" WHERE R.MAKER_ID = D.MAKER_ID\r\n");
		sbSql.append("   AND R.PART_ID = "+markerid+"\r\n");
		sbSql.append("   AND r.state="+Constant.STATUS_ENABLE+"\r\n");
		//sbSql.append("   AND r.status=1\r\n");
		sbSql.append("   AND d.state="+Constant.STATUS_ENABLE+"\r\n");
		//sbSql.append("   AND d.status=1"); 

		List<Map<String, Object>> list = this.pageQuery(sbSql.toString(), null, this.getFunName());
		return list;
	}

	public String getDealUserName(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl((select a.org_name from tm_org a where a.org_id = "+id+"),\r\n" );
		sql.append("       nvl((select b.dealer_name from tm_dealer b where b.dealer_id = "+id+"),\r\n" );
		sql.append("       nvl((select c.org_name from tm_org_custom c where c.org_id = "+id+"),\r\n" );
		sql.append("           (select d.name from tc_user d where d.user_id="+id+")\r\n" );
		sql.append("))) name from dual");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(list!=null && list.size()>0) return (String)list.get(0).get("NAME");
		return null;
	}
	
	/**
	 * 根据条件查询信息是否存在
	 * @param part 查询字段条件
	 * @param type 判断是哪个类型的查询
	 * @return
	 */
	 public Boolean queryPart(String part,String type) {
	        StringBuffer sql = new StringBuffer();
	        if(type.equals("1"))
	        {
	        	//查询配件厂代码
		        sql.append("SELECT * from TT_PART_MAKER_DEFINE t where t.MAKER_CODE = '" + part+"'");
	        }
	        else if(type.equals("2"))
	        {
	        	//查询故障件号
	        	sql.append("SELECT * from TM_PT_PART_BASE t where t.PART_NO = '"+part+"'");
	        }
	        else if(type.equals("3"))
	        {
	        	//查询故障类别代码
	        	sql.append("SELECT * from   TT_AS_WR_MALFUNCTION t where t.mal_code = '"+part+"'");
	        }
	        else if(type.equals("4"))
	        {
	        	//查询零件号
	        	sql.append("SELECT * from TM_PT_PART_BASE t where t.PART_CODE = '"+part+"'");
	        }
	        List<TtPartMakerDefinePO> list = this.select(TtPartMakerDefinePO.class, sql.toString(), null);
	        if(list.size() == 0)
	        {
	        	return false;
	        }
	        return true;
	    }
	 /**
	  * 查询是否存在vin
	  * @param vin
	  * @return
	  */
	 public Boolean queryVin(String vin) {
	        StringBuffer sql = new StringBuffer();
	        sql.append("select * from tm_vehicle t where t.vin = '" + vin +"'");
	        List<TmVehiclePO> list = this.select(TmVehiclePO.class, sql.toString(), null);
	        if(list.size() == 0)
	        {
	        	return false;
	        }
	        return true;
	    }
	 /**
	  * 查询生产时间
	  * @param vin
	  * @return
	  */
	 public TmVehiclePO queryDate(String vin) {
	        StringBuffer sql = new StringBuffer();
	        TmVehiclePO tm = new TmVehiclePO();
	        sql.append("select * from tm_vehicle t where t.vin = '" + vin +"'");
	        List<TmVehiclePO> list = this.select(TmVehiclePO.class, sql.toString(), null);
	        if(list.size() > 0)
	        {
	        	tm = list.get(0);
	        }
	        return tm;
	    }
	 /**
	  * 查询零件数量
	  * @param vin
	  * @param time1
	  * @param time2
	  * @return
	  */
	 public int queryQuantity(String vin ,String time1,String time2)
	 {
		 	StringBuffer sb=new StringBuffer();
			sb.append("select  count(t.id) PART_NUM from tt_as_ro_repair_part t left join tt_as_repair_order o on t.RO_ID=o.id where 1=1");
			sb.append("and o.vin = '"+vin+"'");
			sb.append("and  t.create_date >= to_date('"+time1+"','yyyy-mm-dd')");
			sb.append("and  t.create_date <  (to_date('"+time2+"','yyyy-mm-dd')+1)");
			TtAsWrPartsitemPO ta = new TtAsWrPartsitemPO();
			List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
			if(list.size() > 0)
	        {
	        	return Integer.parseInt(list.get(0).get("PART_NUM").toString());
	        }
	        return 0;
	 }
	 /**
	  * 根据ID查询当前的经销商信息
	  * @param id
	  * @return
	  */
	 public TmDealerPO queryById(Long id)
	 {
		 StringBuffer sql = new StringBuffer();
		 TmDealerPO tm = new TmDealerPO();
	       sql.append("select * from Tm_Dealer t where t.dealer_id = "+id);
	        List<TmDealerPO> list = this.select(TmDealerPO.class, sql.toString(), null);
	        if(list.size() > 0)
	        {
	        	tm = list.get(0);
	        }
	        return tm;
	 }
	 /**
	  * 查询是否存在service_code=1的数据
	  * @return
	  */
	 public QualityReportInfoMaintasinPO queryByCode()
	 {
		 StringBuffer sql = new StringBuffer();
	        sql.append("select * from quality_report_info_maintasin t where t.service_code='1'");
	        
	        List<QualityReportInfoMaintasinPO> list = this.select(QualityReportInfoMaintasinPO.class, sql.toString(), null);
	        QualityReportInfoMaintasinPO maintasinPO = null;
	        if(list.size() > 0)
	        {
	        	maintasinPO = list.get(0);
	        }
	        return maintasinPO;
	 }
	 
}
