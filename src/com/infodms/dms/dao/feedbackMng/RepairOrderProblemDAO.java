package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsRepairOrderBackupPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRepairOrderProblemPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: RepairOrderProblemDAO 
* @Description: TODO(问题工单查询) 
* @author yh 
* @date May 24, 2010 5:27:38 PM 
*
 */
public class RepairOrderProblemDAO extends BaseDao{
	private static final RepairOrderProblemDAO dao = new RepairOrderProblemDAO();
	
	//工单子表集合
	private final String[] tables = {"TT_AS_RO_ADD_ITEM", "TT_AS_RO_LABOUR", "TT_AS_RO_REPAIR_PART", "TT_AS_RO_MANAGE"};
	
	public static final RepairOrderProblemDAO getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param @param con
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult
	* @return PageResult<TtAsRepairOrderProblemPO>    返回类型 
	* @throws
	 */
	public  PageResult<Map<String, Object>> applyQuery(String con ,int curpage,int pagesize) {		
		StringBuffer sql= new StringBuffer();
		sql.append("select tarop.id,tarop.dealer_code,\n" );
		sql.append("       d.dealer_name,\n" );
		sql.append("       tarop.ro_no,\n" );
		sql.append("decode(tarop.repair_type_code,11441001,'一般维修',\n" );
		sql.append("       11441002,'外出维修',11441003,'售前维修',\n" );
		sql.append("       11441004,'保养',11441005,'服务活动')\n" );
		sql.append("       repair_type_code,\n" );
		sql.append("       tarop.license,\n" );
		sql.append("       tarop.vin,\n" );
		sql.append("       tarop.delivery_date,\n" );
		sql.append("       tarop.owner_name,\n" );
		sql.append("       tvmg.group_name,\n" );
		//增加判断是否为特殊无零件工单 modify by tanv 2012-12-25
		sql.append("	   tarop.insuration_no,\n");
		sql.append("       tarop.remark1\n" );
		sql.append("  from tt_as_repair_order_problem tarop,\n" );
		sql.append("       tm_vhcl_material_group     tvmg,\n" );
		sql.append("       tm_dealer                  d\n" );
		sql.append(" where tarop.series = tvmg.group_code(+)\n" );
		sql.append("   and tarop.dealer_code = d.dealer_code\n" );
 	
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append("   and tarop.is_de = 1\n" );
		sql.append(" order by tarop.create_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	
	

	public int delProblemRo(String id)throws Exception{
		int rows = 0;
		TtAsRepairOrderPO po = new TtAsRepairOrderPO();
		po.setId(Long.valueOf(id));
		List list = dao.select(po);
		
		rows = dao.delete(po);//删除工单表记录
		TtAsRepairOrderProblemPO ppo = new TtAsRepairOrderProblemPO();
		ppo.setId(Long.valueOf(id));
		dao.delete(ppo);//删除问题工单表记录
		this.delCasadeTable(po);//级联删除子表
		
		if(list.size()>0){
			po = (TtAsRepairOrderPO)list.get(0);
		}
		TtAsRepairOrderBackupPO bpo = new TtAsRepairOrderBackupPO();
		BeanUtils.copyProperties(bpo, po);
		dao.insert(bpo);//备份

		return rows;
	}
	
	/**
	 * 
	* @Title: delCasadeTable 
	* @Description: TODO(级联删除相关联子表) 
	* @param @param po    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void delCasadeTable(TtAsRepairOrderPO po) {
		for (String table : tables) {
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM " + table + "\n");
			sql.append("WHERE RO_ID = ").append(po.getId());
			delete(sql.toString(), null);
		}
	}
	//add by tanv 维修工单转历史
	public String ropToHis(String id, Long userId) {
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		String rs = "";//执行结果
		if("".equals(id)||null==id){
			rs = "-1";
		}else{
			Long roid=Long.valueOf(id.trim());
			inParameter.add(roid);
			inParameter.add(userId);
			outParameter.add(Types.VARCHAR);
			outParameter = dao.callProcedure("P_C_RO_ROPTOHIS", inParameter, outParameter);
			rs = outParameter.get(0)==null?"-1":outParameter.get(0).toString();//返回值
		}
		return rs;
	}

}
