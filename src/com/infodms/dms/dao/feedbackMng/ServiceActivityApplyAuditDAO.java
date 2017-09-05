package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtIfActivityAuditPO;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infodms.dms.po.TtIfWrActivityExtPO;
import com.infodms.dms.po.TtIfWrActivityPO;
import com.infodms.dms.util.UserProvinceRelation;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ServiceActivityApplyAuditDAO 
* @Description: TODO(服务活动申请表审批) 
* @author wangchao 
* @date May 24, 2010 5:27:01 PM 
*
 */
public class ServiceActivityApplyAuditDAO extends BaseDao{

	private static final ServiceActivityApplyAuditDAO dao = new ServiceActivityApplyAuditDAO();
	
	public static final ServiceActivityApplyAuditDAO getInstance() {
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
	* @param @param param
	* @param @param curpage
	* @param @param pagesize
	* @param @return PageResult<TtIfWrActivityExtPO>    
	* @return PageResult<TtIfWrActivityExtPO>   
	* @throws
	 */
	public  PageResult<TtIfWrActivityExtPO> applyQuery(Long userId,String con,List param, int curpage,int pagesize) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.*,d.dealer_code as dealer_code,d.dealer_name as dealer_name ");
		sql.append(" from TT_IF_WR_ACTIVITY t  ");
		sql.append(" left outer join TM_DEALER D ON d.dealer_id=t.dealer_id ");
		sql.append(" where  1=1 ");
		sql.append(" and is_del=0 ");
		if (con!=null&&!("").equals(con)){
		sql.append(con);
		}
		sql.append(UserProvinceRelation.getDealerIds(userId, "D"));
		sql.append(" order by t.create_date desc ");
		PageResult<TtIfWrActivityExtPO> rs = pageQuery(TtIfWrActivityExtPO.class,sql.toString(), param,
				 pagesize, curpage);
		return rs;
	}
	/**
	 * 
	* @Title: passOrRefuse 
	* @Description: TODO(审批通过或驳回操作) 
	* @param @param right
	* @param @param type
	* @param @param param
	* @param @param param1     
	* @return void    返回类型 
	* @throws
	 */
	public void passOrRefuse(String right,String type,TtIfActivityAuditPO param,TtIfWrActivityPO param1) {
		TtIfActivityAuditPO tiscapForInsert = new TtIfActivityAuditPO();
		TtIfWrActivityPO tisp = new TtIfWrActivityPO();
		tisp.setOrderId(param1.getOrderId());
		TtIfWrActivityPO tispForUpdate = new TtIfWrActivityPO();
		
		tispForUpdate.setOrderId(param1.getOrderId());
		tispForUpdate.setUpdateBy(param1.getUpdateBy());
		tispForUpdate.setUpdateDate(new Date());
		
		tiscapForInsert.setId(Long.parseLong(SequenceManager.getSequence("")));
		tiscapForInsert.setOrderId(param.getOrderId());
		tiscapForInsert.setAuditBy(param.getAuditBy());
		tiscapForInsert.setOrgId(param.getOrgId());
		tiscapForInsert.setAuditDate(new Date());
		tiscapForInsert.setAuditContent(param.getAuditContent());
		if ("pass".equals(type)){
			if ("TEAM".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS); //区域审核通过
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS);
			}else if ("SERVICE".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS); //售后服务审核通过
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS);
			}else if ("CAR".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS); //轿车事业部审核通过
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS);
			}
			
		}else if("refuse".equals(type)){
			if ("TEAM".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT); //区域审核驳回
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT);
			}else if ("SERVICE".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT); //售后服务审核驳回
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT);
			}else if ("CAR".equals(right)){
				tiscapForInsert.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT); //轿车事业部审核驳回
				tispForUpdate.setActStatus(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT);
			}
		}
		insert(tiscapForInsert);
		update(tisp,tispForUpdate);
	}
	
}
