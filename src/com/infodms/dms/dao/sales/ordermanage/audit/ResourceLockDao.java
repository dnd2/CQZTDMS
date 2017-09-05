package com.infodms.dms.dao.sales.ordermanage.audit;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.resourceQuery.ResourceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ResourceLockDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(OrderDeliveryDao.class);
	private static final OrderAuditDao dao = new OrderAuditDao();

	public static final OrderAuditDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	public PageResult<Map<String, Object>> getResourceQueryList(
			Map<String, String> map, int pageSize, Integer curPage) {
		String resStatus=map.get("resStatus");
		String vin=map.get("vin");
		String materialCode=map.get("materialCode");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TV.VEHICLE_ID,\n" );
		sql.append("       TV.VIN,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       TV.LOCK_STATUS\n" );
		sql.append("  FROM TM_VEHICLE TV, TM_VHCL_MATERIAL TVM\n" );
		sql.append(" WHERE TV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TV.LIFE_CYCLE = 10321002\n" );
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		if (null != resStatus && !"".equals(resStatus)) {
			sql.append(" AND TV.LOCK_STATUS="+resStatus+"\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			sql.append(" AND TVM.MATERIAL_CODE LIKE '%"+materialCode+"%'\n");
		}
		sql.append(" ORDER BY TVM.MATERIAL_ID");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}


}
