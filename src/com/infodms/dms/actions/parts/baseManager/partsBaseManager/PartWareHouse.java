package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : PartWareHouse
 * @Description : 配件仓库维护
 * @author : luole CreateDate : 2013-4-2
 */
public class PartWareHouse {
	public Logger         logger                     = Logger.getLogger(PartWareHouse.class);
	private ActionContext act                        = ActionContext.getContext();
	RequestWrapper        request                    = act.getRequest();
	PartWareHouseDao      dao                        = PartWareHouseDao.getInstance();
	private final String  PART_WARE_HOUSE_INIT_URL   = "/jsp/parts/baseManager/partsBaseManager/partWareHouse/partWareHouseInit.jsp";      // 配件仓库维护初始化页面
	private final String  PART_WARE_HOUSE_ADD_URL    = "/jsp/parts/baseManager/partsBaseManager/partWareHouse/partWareHouseAdd.jsp";   // 配件仓库维护初始化页面
	private final String  PART_WARE_HOUSE_UPDATE_URL = "/jsp/parts/baseManager/partsBaseManager/partWareHouse/partWareHouseMod.jsp"; // 配件仓库维护初始化页面

	/**
	 * @Title : 配件仓库维护查询初始化
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-2
	 */
	public void partWareHouseInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_WARE_HOUSE_INIT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 配件仓库新增初始化
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-2
	 */
	public void partWareHouseAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			OrgBean orgBean = dao.getOrgInfo(logonUser).get(0);
			act.setOutData("orgBean", orgBean);
			act.setForword(PART_WARE_HOUSE_ADD_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护新增页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 新增
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-2
	 */
	public void savePartWareHouse() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			boolean flag = true;
			String whCode = CommonUtils.checkNull(request.getParamValue("WH_CODE")); // 仓库编码
			String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME")); // 仓库名称
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 仓库所属机构代码
			String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN")); // 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("TEL")); // 联系人电话
			String addr = CommonUtils.checkNull(request.getParamValue("addr")); // 仓库地址
			String wareHouseType = CommonUtils.checkNull(request.getParamValue("partsWareHouseType")); // 仓库类型		
			//2170807 add
			String provinceId=CommonUtils.checkNull(act.getRequest().getParamValue("PROVINCE_ID"));//省  
			String cityId=CommonUtils.checkNull(act.getRequest().getParamValue("CITY_ID"));//市
			String countiesId=CommonUtils.checkNull(act.getRequest().getParamValue("COUNTIES"));//区/县
			
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			String errorinfo = "";
			TtPartWarehouseDefinePO po1 = new TtPartWarehouseDefinePO();
			po1.setWhCode(whCode);
			
			
			List<PO> pslist1 = dao.select(po1);
			if (pslist1.size() != 0) {
				flag = false;
				errorinfo = errorinfo + "仓库编码重复,请重新输入!\n";
			}
			TtPartWarehouseDefinePO po2 = new TtPartWarehouseDefinePO();
			po2.setWhName(whName);
			List<PO> pslist2 = dao.select(po2);
			if (pslist2.size() != 0) {
				flag = false;
				errorinfo = errorinfo + "仓库名称重复,请重新输入\n";
			}
			if (flag) {
				TmDealerPO dealerPO = new TmDealerPO();
				TmOrgPO orgPO = new TmOrgPO();
				
				dealerPO.setDealerCode(dealerCode);
				List<PO> areaList = dao.select(dealerPO);
				Long dealerID = null;
				if (areaList.size() != 0) {
					dealerPO = (TmDealerPO) areaList.get(0);
					dealerID = dealerPO.getDealerId();
				}else{
					orgPO.setOrgCode(dealerCode);
					orgPO = (TmOrgPO) dao.select(orgPO).get(0);
					dealerID = orgPO.getOrgId();
				}
				po.setWhId(Long.parseLong(SequenceManager.getSequence("")));
				po.setWhCode(whCode.trim());
				po.setWhName(whName);
				po.setOrgId(dealerID);
				po.setLinkman(linkman);
				po.setTel(tel);
				po.setAddr(addr);
				po.setState(Constant.STATUS_ENABLE);
				po.setStatus(1);
				po.setWhType(Integer.parseInt(wareHouseType));//配件仓库类型
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				po.setProvinceId(Integer.parseInt(provinceId));
				po.setCityId(Integer.parseInt(cityId));
				po.setCounties(Integer.parseInt(countiesId));
				dao.insert(po);
				act.setOutData("success", "success");
			} else {
				act.setOutData("error", errorinfo);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件仓库");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 分页查询
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-2
	 */
	public void partWareHouseQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			String whCode = request.getParamValue("WH_CODE");
			String whName = request.getParamValue("WH_NAME");
			String status = request.getParamValue("STATE");
			String wareHouseType =request.getParamValue("partsWareHouseType"); // 仓库类型
			StringBuffer sql = new StringBuffer();
			if (!CommonUtils.isNullString(whCode)) {
				whCode = whCode.toUpperCase();
				sql.append(" and UPPER(tpwd.WH_CODE) like '%" + whCode + "%' ");
			}
			if (!CommonUtils.isNullString(whName)) {
				sql.append(" and tpwd.WH_NAME like '%" + whName + "%' ");
			}
			if (!CommonUtils.isNullString(status)) {
				sql.append(" and tpwd.state = " + status);
			}
			if (!CommonUtils.isNullString(wareHouseType)) {
				sql.append(" and tpwd.WH_TYPE = " + wareHouseType);
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getPartPageQuery(sql.toString(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件仓库");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 修改页面初始化
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-2
	 */
	public void partTypeUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			long orgId = 0l;
			String orgName = "";

			String id = CommonUtils.checkNull(request.getParamValue("Id"));
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			po.setWhId(Long.parseLong(id));
			List<PO> areaList = dao.select(po);
			if (areaList.size() != 0) {
				po = (TtPartWarehouseDefinePO) areaList.get(0);
			}
			
			List<OrgBean> obl = dao.getOrgInfo(logonUser);
			
			TmOrgPO orgPO = new TmOrgPO();
			TmDealerPO dealerPO = new TmDealerPO();
			orgPO.setOrgId(po.getOrgId());
			dealerPO.setDealerId(po.getOrgId());
			List orgList = dao.select(orgPO);
			if(orgList.size()>0){
				orgPO = (TmOrgPO) orgList.get(0);
				orgId = orgPO.getOrgId();
				orgName = orgPO.getOrgName();
			}else{
				dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);
				orgId = dealerPO.getDealerId();
				orgName = dealerPO.getDealerName();
			}
			
			act.setOutData("po", po);
			act.setOutData("orgName", orgName);
			act.setOutData("orgId", orgId);
			act.setForword(PART_WARE_HOUSE_UPDATE_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护新增页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 修改
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void updatePartWareHouse() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			boolean flag = true;
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID")); // 仓库ID
			String whCode = CommonUtils.checkNull(request.getParamValue("WH_CODE")); // 仓库编码
			String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME")); // 仓库名称
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 仓库所属机构ID
			String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN")); // 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("TEL")); // 联系人电话
			String addr = CommonUtils.checkNull(request.getParamValue("addr")); // 仓库地址
			String wareHouseType = CommonUtils.checkNull(request.getParamValue("partsWareHouseType")); // 仓库类型
			//2170807 add
			String provinceId=CommonUtils.checkNull(act.getRequest().getParamValue("PROVINCE_ID"));//省  
			String cityId=CommonUtils.checkNull(act.getRequest().getParamValue("CITY_ID"));//市
			String countiesId=CommonUtils.checkNull(act.getRequest().getParamValue("COUNTIES"));//区/县
			
			
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			String errorinfo = "";
			TtPartWarehouseDefinePO po1 = new TtPartWarehouseDefinePO();
			po1.setWhCode(whCode);
			po1.setOrgId(Long.parseLong(orgId));
			List<PO> pslist1 = dao.select(po1);
			if (pslist1.size() > 1) {
				po1 = (TtPartWarehouseDefinePO) pslist1.get(0);
				flag = false;
				errorinfo = errorinfo + "仓库编码重复,请重新输入!\n";
			}
			TtPartWarehouseDefinePO po2 = new TtPartWarehouseDefinePO();
			po2.setWhName(whName);
			List<PO> pslist2 = dao.select(po2);
			if (pslist2.size() > 1) {
				po2 = (TtPartWarehouseDefinePO) pslist2.get(0);
				flag = false;
				errorinfo = errorinfo + "仓库名称重复,请重新输入\n";
			}
			if (flag) {
				TtPartWarehouseDefinePO po3 = new TtPartWarehouseDefinePO();
				po3.setWhId(Long.parseLong(whId));
				po.setWhCode(whCode.trim());
				po.setWhName(whName.trim());
				po.setWhType(Integer.parseInt(wareHouseType));
				po.setOrgId(Long.parseLong(orgId));
				po.setLinkman(linkman.trim());
				po.setTel(tel.trim());
				po.setAddr(addr.trim());
				po.setUpdateDate(new Date());
				po.setUpdateBy(logonUser.getUserId());
				
				po.setProvinceId(Integer.parseInt(provinceId));
				po.setCityId(Integer.parseInt(cityId));
				po.setCounties(Integer.parseInt(countiesId));
				dao.update(po3, po);
				//act.setForword(PART_WARE_HOUSE_INIT_URL);
				act.setOutData("success", "success");
				
			} else {
				act.setOutData("error", errorinfo);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "修改配件仓库");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 失效
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partNotState() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String whId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库ID
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			po.setWhId(Long.parseLong(whId));
			TtPartWarehouseDefinePO po1 = new TtPartWarehouseDefinePO();
			po1.setState(Constant.STATUS_DISABLE);
			po1.setDisableBy(logonUser.getUserId());
			po1.setDeleteDate(new Date());
			dao.update(po, po1);
			act.setOutData("success", "失效成功");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护失效操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 有效
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partStateEnable() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String whId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库ID
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
			po.setWhId(Long.parseLong(whId));
			po.setState(Constant.STATUS_DISABLE);
			TtPartWarehouseDefinePO po1 = new TtPartWarehouseDefinePO();
			po1.setState(Constant.STATUS_ENABLE);
			po1.setDisableBy(logonUser.getUserId());
			po1.setDeleteDate(new Date());
			dao.update(po, po1);
			if ("".equals(curPage)) {
				curPage = "1";
			}
			// System.out.println(curPage);
			act.setOutData("success", "设置成功");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件仓库维护有效操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
