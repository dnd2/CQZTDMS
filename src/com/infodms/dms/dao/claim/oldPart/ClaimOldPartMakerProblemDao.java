package com.infodms.dms.dao.claim.oldPart;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOldPartOutPreListBean;
import com.infodms.dms.bean.SpefeeBaseBean;
import com.infodms.dms.bean.TtAsWrMainPartClaimBean;
import com.infodms.dms.bean.TtAsWrOldOutDetailBean;
import com.infodms.dms.bean.TtAsWrOldOutDoorBean;
import com.infodms.dms.bean.TtAsWrOldOutNoticeBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtAsSecondInStoreDetailPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrDiscountPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrOldOutDoorDetailPO;
import com.infodms.dms.po.TtAsWrOldOutNoticeDetailPO;
import com.infodms.dms.po.TtAsWrOldOutNoticePO;
import com.infodms.dms.po.TtAsWrOldOutPartPO;
import com.infodms.dms.po.TtAsWrOldPartLabourPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailExtendPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrPartStockLogPO;
import com.infodms.dms.po.TtAsWrPartStockPO;
import com.infodms.dms.po.TtAsWrPartsitemBarcodePO;
import com.infodms.dms.po.TtDeliveryOrderPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：索赔旧件出库Dao
 * 作者：  赵伦达
 */
@SuppressWarnings("rawtypes")
public class ClaimOldPartMakerProblemDao extends BaseDao{
	
    public static Logger logger = Logger.getLogger(ClaimOldPartMakerProblemDao.class);
	
	private static  ClaimOldPartMakerProblemDao dao = null;
	
	public static final ClaimOldPartMakerProblemDao getInstance() {
	   if(dao==null) return new ClaimOldPartMakerProblemDao();
	   return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> querymakerProblem(RequestWrapper request, Integer page,
			Integer currPage, Map params) {
		String part_name=request.getParamValue("part_name");
		String part_code=request.getParamValue("part_code");
		String supply_name=request.getParamValue("supply_name");
		String supply_code=request.getParamValue("supply_code");
		String type=request.getParamValue("type");
		String status=request.getParamValue("status");
		
		StringBuffer sb= new StringBuffer();
		sb.append("select p.PROBLEM_ID,\n" );
		sb.append("       p.SUPPLY_CODE,\n" );
		sb.append("       p.SUPPLY_NAME,\n" );
		sb.append("       p.PART_NAME,\n" );
		sb.append("       p.PART_CODE,\n" );
		sb.append("       p.NUM,\n" );
		sb.append("       p.create_date,\n" );
		sb.append("       p.update_date,\n" );
		sb.append("       p.type,\n" );
		sb.append("       (select code_desc from tc_code c where c.code_id = p.STATUS) as status_desc,\n" );
		sb.append("       p.status,\n" );
		sb.append("       REMARK\n" );
		sb.append("  from TT_PART_MAKER_PROBLEM p\n" );
		sb.append(" where 1 = 1");
		DaoFactory.getsql(sb, "p.part_name",part_name , 2);
		DaoFactory.getsql(sb, "p.part_code",part_code , 2);
		DaoFactory.getsql(sb, "p.supply_name",supply_name , 2);
		DaoFactory.getsql(sb, "p.supply_code",supply_code , 2);
		DaoFactory.getsql(sb, "p.type",type , 1);
		DaoFactory.getsql(sb, "p.status",status , 1);
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), Constant.PAGE_SIZE, currPage);
		return ps;
		
	}
	public PageResult<Map<String, Object>> querymakerProblemDetail(RequestWrapper request, Integer page,
			Integer currPage, Map params) {
		String problem_id=request.getParamValue("problem_id");
		String part_name=request.getParamValue("part_name");
		String part_code=request.getParamValue("part_code");
		String supply_name=request.getParamValue("supply_name");
		String supply_code=request.getParamValue("supply_code");
		
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("select * from TT_PART_MAKER_PROBLEM_DTL \n");
		sb.append("where 1=1 \n");
		sb.append("and problem_id= "+problem_id+" \n");
		
		
		if(part_name!=null){
			sb.append(" and part_name like '%"+part_name+"%'");
		}
		if(part_code!=null){
			sb.append(" and part_code like '%"+part_code+"%'");
		}
		if(supply_name!=null){
			sb.append(" and supply_name like '%"+supply_name+"%'");
		}
		if(supply_code!=null){
			sb.append(" and supply_code like '%"+supply_code+"%'");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), Constant.PAGE_SIZE, currPage);
		return ps;
		
		
	}
	public void updateProblemRemark(RequestWrapper request) {
		String problem_id=request.getParamValue("problem_id");
		String remark=request.getParamValue("remark");
	}
}
