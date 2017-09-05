package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartFixcodeDAO;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : partFixcode
 * @Description : 配件变量维护
 * @author : luole CreateDate : 2013-4-3
 */
public class PartFixcodeQuery {
	public Logger         logger                 = Logger.getLogger(PartFixcodeQuery.class);
	private ActionContext act                    = ActionContext.getContext();
	RequestWrapper        request                = act.getRequest();
	PartFixcodeDAO        dao                    = PartFixcodeDAO.getInstance();
	private final String  PART_FIXCODE_QUERY_URL = "/jsp/parts/baseManager/partsBaseManager/partFixcode/partFixcodeQuery.jsp"; // 配件货位维护初始化页面
	private final String  PART_FIXCODE_ADD_URL   = "/jsp/parts/baseManager/partsBaseManager/partFixcode/partFixcodeAdd.jsp";  // 新增初始化页面
	private final String  PART_FIXCODE_MOD_URL   = "/jsp/parts/baseManager/partsBaseManager/partFixcode/partFixcodeMod.jsp";  // 修改初始化页面

	/**
	 * @Title : 初始化
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partFixcodeQueryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_FIXCODE_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 新增初始化
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partFixcodeAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> typelist = dao.getPartFixcodeType();
			act.setOutData("typelist", typelist);
			act.setForword(PART_FIXCODE_ADD_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位维护新增页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 新增
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-11
	 */
	public void add() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String strids = CommonUtils.checkNull(request.getParamValue("ids")); // 列表INDEX
			String typeId = CommonUtils.checkNull(request.getParamValue("TYPEID")); // 类型ID
			TcCodeDao tcd = TcCodeDao.getInstance();
			String typeName = tcd.getCodeDescByCodeId(typeId);
			String[] ids = strids.split(",");
			Date date = new Date();
			for (String index : ids) {
				String fixName = CommonUtils.checkNull(request.getParamValue("FIX_NAME" + index)); // 变量名称
				String sortNo = CommonUtils.checkNull(request.getParamValue("SORT_NO" + index)); // 变量序列
				TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
				po.setFixId(Long.parseLong(SequenceManager.getSequence("")));
				po.setFixGouptype(Integer.parseInt(typeId));
				po.setFixGroupname(typeName);
				po.setFixName(fixName);
				po.setFixValue(index);
				po.setSortNo(sortNo);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(date);
				dao.insert(po);
			}
			partFixcodeQueryInit();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 修改
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-11
	 */
	public void update() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String strids = CommonUtils.checkNull(request.getParamValue("ids")); // 列表INDEX
			String typeId = CommonUtils.checkNull(request.getParamValue("TYPEID")); // 类型ID
			String typeName = CommonUtils.checkNull(request.getParamValue("TYPENAME"));
			String[] ids = strids.split(",");
			Date date = new Date();
			for (String index : ids) {
				String fixId = CommonUtils.checkNull(request.getParamValue("FIX_ID" + index)); // 变量名称
				String fixName = CommonUtils.checkNull(request.getParamValue("FIX_NAME" + index)); // 变量名称
				String sortNo = CommonUtils.checkNull(request.getParamValue("SORT_NO" + index)); // 变量序列
				if (!"".equals(fixId)) {// 修改
					TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
					po.setFixName(fixName);
					po.setSortNo(sortNo);
					po.setUpdateBy(logonUser.getUserId());
					po.setUpdateDate(date);

					TtPartFixcodeDefinePO po1 = new TtPartFixcodeDefinePO();
					po1.setFixId(Long.parseLong(fixId));
					dao.update(po1, po);
				} else { // 新增
					TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
					po.setFixId(Long.parseLong(SequenceManager.getSequence("")));
					po.setFixGouptype(Integer.parseInt(typeId));
					po.setFixGroupname(typeName);
					po.setFixName(fixName);
					po.setFixValue(index);
					po.setSortNo(sortNo);
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(date);
					dao.insert(po);
				}
			}
			partFixcodeQueryInit();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 分布查询
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partFixcodeQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			String fixcodeType = request.getParamValue("FIXCODETYPE"); // 变量类型
			String fixName = request.getParamValue("FIXNAME"); // 配件编码
			String state = request.getParamValue("STATE"); // 附属货位
			StringBuffer sql = new StringBuffer();
			if (!CommonUtils.isNullString(fixcodeType)) {
				sql.append(" and t.Fix_Gouptype = " + fixcodeType);
			}
			if (!CommonUtils.isNullString(fixName)) {
				sql.append(" and t.Fix_Name  like '%" + fixName + "%' ");
			}
			if (!CommonUtils.isNullString(state)) {
				sql.append(" and  t.state = " + state);
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.fixcodePageQuery(sql.toString(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : 修改初始化页面
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-11
	 */
	public void partFixcodeModInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String fixId = CommonUtils.checkNull(request.getParamValue("fixId"));
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
			po.setFixId(Long.parseLong(fixId));
			List<PO> areaList = dao.select(po);
			if (areaList.size() != 0) {
				po = (TtPartFixcodeDefinePO) areaList.get(0);
			}
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("fixGouptype", po.getFixGouptype());
			act.setOutData("fixGoupname", po.getFixGroupname());
			act.setOutData("typelist", dao.getPartFixcodeListByTypeId(po.getFixGouptype()));
			act.setOutData("curPage", curPage);
			act.setOutData("fixId", fixId);
			act.setForword(PART_FIXCODE_MOD_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量修改页面初始化");
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
	public void partUpdateState() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String fixId = CommonUtils.checkNull(request.getParamValue("fixId")); // 仓库ID
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));// 有效无效更新标记
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
			po.setFixId(Long.parseLong(fixId));

			TtPartFixcodeDefinePO po1 = new TtPartFixcodeDefinePO();
			if ("enable".equalsIgnoreCase(flag)) {
				po1.setState(Constant.STATUS_ENABLE);
			} else {
				po1.setState(Constant.STATUS_DISABLE);
			}
			po1.setUpdateBy(logonUser.getUserId());
			po1.setUpdateDate(new Date());
			dao.update(po, po1);
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("success", "success");
			act.setOutData("curPage", curPage);
			act.setOutData("fixId", fixId);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量失效");
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
	public void partEnableState() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String fixId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库IDcurPage
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			if ("".equals(curPage)) {
				curPage = "1";
			}
			TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
			po.setFixId(Long.parseLong(fixId));
			po.setState(Constant.STATUS_DISABLE);
			TtPartFixcodeDefinePO po1 = new TtPartFixcodeDefinePO();
			po1.setState(Constant.STATUS_ENABLE);
			po1.setDisableBy(logonUser.getUserId());
			po1.setDeleteDate(new Date());
			dao.update(po, po1);
			act.setOutData("success", "设置有效成功");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量有效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    
	/**
	 * @Title : 无效
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : luole LastDate : 2013-4-3
	 */
	public void partNotState() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String fixId = CommonUtils.checkNull(request.getParamValue("Id")); // 仓库IDcurPage
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			if ("".equals(curPage)) {
				curPage = "1";
			}
			TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
			po.setFixId(Long.parseLong(fixId));
			po.setState(Constant.STATUS_ENABLE);
			TtPartFixcodeDefinePO po1 = new TtPartFixcodeDefinePO();
			po1.setState(Constant.STATUS_DISABLE);
			po1.setDisableBy(logonUser.getUserId());
			po1.setDeleteDate(new Date());
			dao.update(po, po1);
			act.setOutData("success", "设置无效成功");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件变量无效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
