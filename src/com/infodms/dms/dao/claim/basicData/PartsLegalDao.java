/**********************************************************************
* <pre>
* FILE : ClaimRuleDao.java
* CLASS : ClaimRuleDao
*
* AUTHOR : PGM
*
* FUNCTION : 三包规则维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-14| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PartsLegalDao.java,v 1.1 2013/07/07 09:43:13 wanghx Exp $
 */
package com.infodms.dms.dao.claim.basicData;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsRepairOrderExtPO;
import com.infodms.dms.po.TtAsWrFaultPartsPO;
import com.infodms.dms.po.TtAsWrFaultPartsTempPO;
import com.infodms.dms.po.TtAsWrPartLegalExtPO;
import com.infodms.dms.po.TtAsWrPartLegalPO;
import com.infodms.dms.po.TtAsWrPartLegallDetailPO;
import com.infodms.dms.po.TtAsWrPartsAssemblyPO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.PageQuery;
/**
 * Function       :  三包规则维护 
 * @author        :  PGM
 * CreateDate     :  2010-07-14
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PartsLegalDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ClaimRuleDao.class);
	private static final PartsLegalDao dao = new PartsLegalDao ();
	public  static final PartsLegalDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*********
	 *   查询功能
	 * @param code
	 * @param name
	 * @param status
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<TtAsWrPartLegalExtPO> getPartsLrgalView(String legalCode,String legalName,String status,String parts_assembly_id,Integer pageSize,Integer  curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select l.*,a.parts_assembly_name from Tt_As_Wr_Part_Legal l,Tt_As_Wr_Parts_Assembly a  where 1=1 and   l.parts_assembly_id=a.parts_assembly_id ");
		if(legalCode!=null&&!legalCode.equals("")){
			sql.append("and  l.part_legal_code like '%"+legalCode+"%'\n" );
		}
		if(legalName!=null&&!legalName.equals("")){
			sql.append("and l.part_legal_name like '%"+legalName+"%'\n" );
		}
		if(status!=null&&!status.equals("")){
			sql.append("and l.status="+status+"\n" );
		}
		if(parts_assembly_id!=null&&!parts_assembly_id.equals("")){
			sql.append("and l.parts_assembly_id ="+parts_assembly_id+"\n" );
		}
		
		return pageQuery(TtAsWrPartLegalExtPO.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	public void delPartsAssembly(String ids){
		
		
		
		
	}
	
	/*********
	 * 查询零件法定名称对应的总成明细
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> modifyPartsLegal(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select l.*,a.parts_assembly_name,a.PARTS_ASSEMBLY_ID from Tt_As_Wr_Part_Legal l,Tt_As_Wr_Parts_Assembly a  where 1=1 and   l.parts_assembly_id=a.parts_assembly_id ");
		sql.append("and l.part_legal_id="+id+"");
		return this.pageQuery(sql.toString(), null, getFunName());
		
	}
	
	public List<Map<String,Object>> viewPartLegallDetail(String id,Integer pageSize,Integer curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from  TT_AS_WR_PART_LEGALl_DETAIL d   where d.part_legal_id="+id+" and d.status=10041001");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	/*******
	 * 查询总成
	 */
	public List<Map<String,Object>> viewAssemblyDetail(){
		StringBuffer sql =  new StringBuffer();
		sql.append("select s.parts_assembly_id,s.parts_assembly_code,s.parts_assembly_name  from Tt_As_Wr_Parts_Assembly s where 1=1 and s.status=10011001 ");
		List<Map<String, Object>> list =  this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	public void claimRulePartsImportAdd(TtAsWrFaultPartsTempPO ruleListPO){
		dao.insert(ruleListPO);              
	}
	public void claimRulePartsImportAddMerge(TtAsWrFaultPartsTempPO RuleListPO){
		StringBuffer sql= new StringBuffer();
		sql.append("MERGE INTO TT_AS_WR_PART_LEGALl_DETAIL r\n" );
		sql.append("USING\n" );
		sql.append("  (select fault_id,part_code, part_name, status, is_de\n" );
		sql.append("    from tt_as_wr_fault_parts_temp where fault_id="+RuleListPO.getFaultId()+") l ON ( r.part_legal_id=l.fault_id and r.part_code = l.part_code and\n" );
		sql.append("                                 r.part_name = l.part_name) WHEN\n" );
		sql.append("   MATCHED THEN\n" );
		sql.append("    UPDATE\n" );
		sql.append("       SET \n" );
		sql.append("           \n" );
		sql.append("           r.update_by    = "+RuleListPO.getUpdateBy()+",\n" );
		sql.append("           r.update_date  = sysdate\n" );
		sql.append("  WHEN NOT MATCHED THEN\n" );
		sql.append("    INSERT\n" );
		sql.append("      (r.id,\n" );
		sql.append("       r.part_legal_id,\n" );
		sql.append("       r.part_code,\n" );
		sql.append("       r.part_name,\n" );
		sql.append("       r.status,\n" );
		sql.append("       r.is_de,\n" );
		sql.append("       r.create_by,\n" );
		sql.append("       r.create_date)\n" );
		sql.append("    VALUES\n" );
		sql.append("      (f_getid(),\n" );
		sql.append("       "+RuleListPO.getFaultId()+",\n" );
		sql.append("       '"+RuleListPO.getPartCode()+"',\n" );
		sql.append("       '"+RuleListPO.getPartName()+"',\n" );
		sql.append("       "+RuleListPO.getStatus()+",\n" );
		sql.append("       "+RuleListPO.getIsDe()+",\n" );
		sql.append("       "+RuleListPO.getCreateBy()+",\n" );
		sql.append("       sysdate)");

		dao.update(sql.toString(), null);
	}
	
	public void claimRulePartsImportDelete(TtAsWrFaultPartsTempPO RuleListPO){
		dao.delete(RuleListPO);
	}
	
	


}