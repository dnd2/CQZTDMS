package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ClaimWrRuleDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimWrRuleDao.class);
	private static final ClaimWrRuleDao dao = new ClaimWrRuleDao ();
	public  static final ClaimWrRuleDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 三包预警规则查询
	 * @param po
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String,Object>> getClaimWrRuleQuery(TtAsWrVinRulePO po,int curPage,int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append(" select tt.*,(select tc.code_desc from tc_code tc where tt.part_wr_type = tc.code_id) AS wrtype from (");
		sql.append(" select t.*,'' pname from tt_as_wr_vin_rule t where 1=1 and t.vr_type = "+Constant.VR_TYPE_1);
		sql.append(" union all ");
		sql.append(" select t.*,'' pname from tt_as_wr_vin_rule t where 1=1 and t.part_wr_type = "+Constant.PART_WR_TYPE_1);
		sql.append(" union all ");
		sql.append(" select t.*,p.part_name pname from tt_as_wr_vin_rule t , tm_pt_part_base p where t.vr_part_code = p.part_code ");
		sql.append(" union all ");
		sql.append(" select t.*,tp.pos_name pname from tt_as_wr_vin_rule t , tt_as_wr_malfunction_position tp where t.vr_part_code = tp.pos_code ");
		sql.append(" ) tt where 1=1 ");
		if(!"".equals(po.getVrCode())&&po.getVrCode()!=null){
			sql.append(" and tt.vr_code like '%"+po.getVrCode()+"%' ");
		}
		if(!"".equals(po.getVrLevel())&&po.getVrLevel()!=null){
			sql.append(" and tt.vr_level = "+po.getVrLevel());
		}
		if(!"".equals(po.getVrType())&&po.getVrType()!=null){
			sql.append(" and tt.vr_type = "+po.getVrType());
		}
		if(!"".equals(po.getPartWrType())&&po.getPartWrType()!=null){
			sql.append(" and tt.part_wr_type = "+po.getPartWrType());
		}
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 获取易损配件代码和名称
	 * @return
	 */
	public List<TmPtPartBasePO> getPartCode(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select part_code,part_name from tm_pt_part_base where part_war_type = "+Constant.PART_WR_TYPE_2);
		List<TmPtPartBasePO> list = dao.select(TmPtPartBasePO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 获取关注部位代码和名称
	 * @return
	 */
	public List<TtAsWrMalfunctionPositionPO> getPosCode(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select pos_code,pos_name from tt_as_wr_malfunction_position ");
		List<TtAsWrMalfunctionPositionPO> list = dao.select(TtAsWrMalfunctionPositionPO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 根据给定的条件查询Tt_As_Wr_Vin_Rule的数据用于判断是否已经存在此数据
	 * @param partWrType
	 * @param vrLaw
	 * @param vrWarranty
	 * @param part
	 * @param position
	 * @param level
	 * @return
	 */
	public List<TtAsWrVinRulePO> checkRuleExist(String sta,String id,String type,String partWrType,String vrLaw,String vrWarranty,String part,String position,String level){
		StringBuffer sql = new StringBuffer();
		if(type.equals(Constant.VR_TYPE_1)){//整车
			sql.append(" select * from tt_as_wr_vin_rule where 1=1 and vr_type = "+Integer.parseInt(type));
			sql.append(" and vr_level = "+Integer.parseInt(level));
		}else{
			if(partWrType.equals(Constant.PART_WR_TYPE_1)){//常规件--等级，法定，预警
				sql.append("select * from tt_as_wr_vin_rule where 1=1 and part_wr_type = "+Integer.parseInt(partWrType));
				sql.append(" and vr_law = "+Integer.parseInt(vrLaw));
				sql.append(" and vr_warranty = "+Integer.parseInt(vrWarranty));
			}else if(partWrType.equals(Constant.PART_WR_TYPE_2)){//易损件
				sql.append("select * from tt_as_wr_vin_rule where 1=1 and vr_part_code = '"+part+"' ");
			}else if(partWrType.equals(Constant.PART_WR_TYPE_3)){//关注部位
				sql.append("select * from tt_as_wr_vin_rule where 1=1 and vr_law = "+vrLaw+" and  vr_part_code = '"+position+"' ");
			}
			sql.append(" and vr_level = "+Integer.parseInt(level));
		}
		if("2".equals(sta)){//修改判断
			sql.append(" minus ");
			sql.append(" select * from tt_as_wr_vin_rule where 1=1 and vr_id = "+Long.parseLong(id));
		}
		
		
		List<TtAsWrVinRulePO> list = dao.select(TtAsWrVinRulePO.class, sql.toString(), null);
		return list;
	}
	/**
	 * 获取上下等级的预警时间（次数）
	 * @param type
	 * @param level
	 * @param partWrType
	 * @param part
	 * @param position
	 * @param sta
	 * @return
	 */
	public List<TtAsWrVinRulePO> getClaimWrRuleByPart(String type,String level,String partWrType,String part,String position,String sta){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tt_as_wr_vin_rule where 1=1 \n");
		if(type.equals(Constant.VR_TYPE_1)){//整车
			sql.append(" and vr_type = "+Integer.parseInt(type));
		}else{
			if(partWrType.equals(Constant.PART_WR_TYPE_1)){//常规件
				sql.append(" part_wr_type = "+Integer.parseInt(partWrType));
			}else if(partWrType.equals(Constant.PART_WR_TYPE_2)){//易损件
				sql.append(" and vr_part_code = '"+part+"' ");
			}else if(partWrType.equals(Constant.PART_WR_TYPE_3)){//关注部位
				sql.append(" and vr_part_code = '"+position+"' ");
			}
		}
		if(sta.equals("big")){
			sql.append(" and vr_level > "+Integer.parseInt(level));
			sql.append(" order by vr_level asc");
		}else{
			sql.append(" and vr_level < "+Integer.parseInt(level));
			sql.append(" order by vr_level desc");
		}
		return dao.select(TtAsWrVinRulePO.class, sql.toString(), null);
	}
	
	/**
	 * 查询配件三包类型
	 * @return
	 */
	public List<TcCodePO> getPartWrType(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tc_code where type = 9403");
		List<TcCodePO> list = dao.select(TcCodePO.class,sql.toString(),null);
		return list;
	}

}
