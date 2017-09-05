package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sound.midi.Sequence;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtIfActivityAuditPO;
import com.infodms.dms.po.TtIfWrActivityExtPO;
import com.infodms.dms.po.TtIfWrActivityPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: ServiceActivityApplyDAO
 * @Description: TODO(服务活动申请表)
 * @author wangchao
 * @date May 24, 2010 5:27:38 PM
 * 
 */
public class ServiceActivityApplyDAO extends BaseDao {
	private static final ServiceActivityApplyDAO dao = new ServiceActivityApplyDAO();

	public static final ServiceActivityApplyDAO getInstance() {
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
	 * @return PageResult<TtIfWrActivityExtPO> 返回类型
	 * @throws
	 */
	public PageResult<TtIfWrActivityExtPO> applyQuery(String con,
			List<Object> param, int curpage, int pagesize) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder();
		sql
				.append("select t.*,d.dealer_code as dealer_code,d.dealer_name as dealer_name ");
		sql.append(" from TT_IF_WR_ACTIVITY t,TM_DEALER D  ");
		sql.append(" where d.dealer_id=t.dealer_id ");
		sql.append(" and is_del=0 ");

		if (con != null && !("").equals(con)) {
			sql.append(con);
		}
		sql.append(" order by t.create_date desc ");
		PageResult<TtIfWrActivityExtPO> rs = pageQuery(
				TtIfWrActivityExtPO.class, sql.toString(), param, pagesize,
				curpage);
		return rs;
	}

	/**
	 * 
	 * @Title: queryByOrderId
	 * @Description: TODO(根据工单号查询服务活动申请表)
	 * @param @param orderId
	 * @param @return TtIfServicecarPO
	 * @return TtIfWrActivityPO 返回类型
	 * @throws
	 */
	public TtIfWrActivityPO queryByOrderId(String orderId) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		TtIfWrActivityPO tisep = new TtIfWrActivityPO();
		tisep.setOrderId(orderId);
		List<TtIfWrActivityPO> ls = select(tisep);
		if (ls != null) {
			if (ls.size() > 0) {
				tisep = ls.get(0);
			}
		}

		return tisep;
	}

	/**
	 * 
	 * @Title: queryDetailByOrderId
	 * @Description: TODO(明细查询)
	 * @param @param orderId
	 * @param @return TtIfServicecarExtPO
	 * @return TtIfWrActivityExtPO 返回类型
	 * @throws
	 */
	public TtIfWrActivityExtPO queryDetailByOrderId(String orderId) {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuilder sql = new StringBuilder();
		sql
				.append("select t.*,d.dealer_code as dealer_code,d.dealer_name as dealer_name ");
		sql.append(" from TT_IF_WR_ACTIVITY t ");
		sql.append(" left outer join TM_DEALER d on t.dealer_id=d.dealer_id ");
		sql.append(" where  1=1 ");
		if (orderId != null && !("").equals(orderId)) {
			sql.append(" AND t.ORDER_ID = '");
			sql.append(orderId);
			sql.append("'");
		}
		TtIfWrActivityExtPO tisep = new TtIfWrActivityExtPO();
		PageResult<TtIfWrActivityExtPO> rs = pageQuery(
				TtIfWrActivityExtPO.class, sql.toString(), null, 10, 1);
		List<TtIfWrActivityExtPO> ls = rs.getRecords();
		if (ls != null) {
			if (ls.size() > 0) {
				tisep = ls.get(0);
			}
		}
		return tisep;
	}

	/**
	 * 
	 * @Title: queryAuditDetailByOrderId
	 * @Description: TODO(审批明细)
	 * @param @param orderId
	 * @param @return List<TtIfServicecarExtPO>
	 * @return List<TtIfWrActivityExtPO> 返回类型
	 * @throws
	 */
	public List<TtIfWrActivityExtPO> queryAuditDetailByOrderId(String orderId) {
		StringBuilder sql = new StringBuilder();
		sql
				.append("select t1.*,U.name as audit_by_name,o.org_name as dept_name  from TT_IF_ACTIVITY_AUDIT t1  ");
		sql.append(" left outer join TC_USER U ON T1.audit_by=u.user_id ");
		sql.append(" left outer join tm_org o on t1.org_id = o.org_id ");
		sql.append(" where 1=1 ");
		if (orderId != null && !("").equals(orderId)) {
			sql.append(" AND t1.ORDER_ID ='");
			sql.append(orderId);
			sql.append("'");
		}
		sql.append(" ORDER BY AUDIT_DATE DESC ");
		List<TtIfWrActivityExtPO> rs = select(TtIfWrActivityExtPO.class, sql
				.toString(), null);
		return rs;
	}

	/**
	 * 
	 * @Title: submit
	 * @Description: TODO(申请上报)
	 * @param @param orderIds 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void submit(String[] orderIds, AclUserBean user) {
		Date date = new Date();
		for (int i = 0; i < orderIds.length; i++) {
			TtIfWrActivityPO tisep = new TtIfWrActivityPO();
			tisep.setOrderId(orderIds[i]);
			TtIfWrActivityPO tisepU = new TtIfWrActivityPO();
			tisepU.setOrderId(orderIds[i]);
			tisepU.setActStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED); // 已上报
			tisepU.setActDate(date);
			tisepU.setUpdateDate(date);
			update(tisep, tisepU);
			TtIfActivityAuditPO tiap = new TtIfActivityAuditPO();
			// modify by xiayanpeng begin 主键ID未插入
			tiap.setId(new Long(SequenceManager.getSequence("")));
			// modify by xiayanpeng end
			tiap.setAuditBy(user.getUserId());
			tiap.setAuditDate(date);
			tiap.setAuditStatus(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED); // 已上报
			tiap.setOrderId(orderIds[i]);
			tiap.setOrgId(user.getOrgId());
			insert(tiap);
		}
	}

	/**
	 * 
	 * @Title: deleteRecord
	 * @Description: TODO(删除记录)
	 * @param @param orderIds
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteRecord(String[] orderIds) {
		for (int i = 0; i < orderIds.length; i++) {
			TtIfWrActivityPO tisep = new TtIfWrActivityPO();
			tisep.setOrderId(orderIds[i]);
			TtIfWrActivityPO tisepU = new TtIfWrActivityPO();
			tisepU.setOrderId(orderIds[i]);
			tisepU.setIsDel(1); // 将其设置为1，已删除
			update(tisep, tisepU);
		}
	}

	/**
	 * 
	 * @Title: updateRecord
	 * @Description: TODO(修改记录)
	 * @param @param orderId
	 * @param @param tisp
	 * @return void 返回类型
	 * @throws
	 */
	public void updateRecord(String orderId, TtIfWrActivityPO tisp) {

		TtIfWrActivityPO tisep = new TtIfWrActivityPO();
		tisep.setOrderId(orderId);
		update(tisep, tisp);

	}

	/**
	 * 
	 * @Title: addRecord
	 * @Description: TODO(新增记录)
	 * @param @param tisp
	 * @return void 返回类型
	 * @throws
	 */
	public String addRecord(TtIfWrActivityPO tisp) {
		String activityId = SequenceManager.getSequence("");
		tisp.setId(Long.parseLong(activityId));
		insert(tisp);
		return activityId;
	}

	/**
	 * Function：获得附件信息列表
	 * 
	 * @param ：
	 * @return: @param id
	 * @return: @return
	 * @throw： LastUpdate： 2010-7-15
	 */
	public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='" + id + "'");
		List<FsFileuploadPO> ls = select(FsFileuploadPO.class, sql.toString(),
				null);
		return ls;
	}
}
