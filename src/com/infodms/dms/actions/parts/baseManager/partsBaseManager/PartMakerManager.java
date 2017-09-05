package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartMakerDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartVenderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : TtPartVender
 * @Description : 制造商信息维护
 * @author : chenjunjiang CreateDate : 2013-4-2
 */
public class PartMakerManager extends PartBaseImport implements PTConstants {

	public Logger logger = Logger.getLogger(PartMakerManager.class);
	private PartMakerDao dao = PartMakerDao.getInstance();
	private PartVenderDao venderDao = PartVenderDao.getInstance();

	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void queryPartMakerInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("curPage", curPage);
			act.setForword(partMakerInitUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 分页查询制造商信息
	 */
	public void queryPartMakerInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerCode = CommonUtils.checkNull(request.getParamValue("MAKER_CODE"));// 制造商代码
			makerCode = makerCode.toUpperCase();
			String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME"));// 制造商名称
			String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE"));// 供应商代码
			venderCode = venderCode.toUpperCase();
			String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));// 供应商名称
			String str_state = CommonUtils.checkNull(request.getParamValue("STATE"));
			TtPartMakerDefinePO bean = new TtPartMakerDefinePO();
			bean.setMakerCode(makerCode);
			bean.setMakerName(makerName);

			int state = 0;
			if (!"".equals(str_state)) {
				state = CommonUtils.parseInteger(str_state);
			}
			bean.setState(state);

			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartMakerList(bean,venderCode,venderName, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 制造商信息添加初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPartMakderInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addPartMakerInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "制造商信息添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 添加制造商信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void addPartMakerInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String error = "";
			boolean flag = true;
			// 得到页面参数
			String makerCode = CommonUtils.checkNull(request.getParamValue("MAKER_CODE"));
			String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME"));
			if (dao.existMakerCode(makerCode)) {// 判断制造商编码是否存在
				error = error + "制造商编码已经存在,请重新输入!";
				flag = false;
			}

			if (dao.existMakerName(makerName)) {// 判断制造商名称是否存在
				error = error + "制造商名称已经存在,请重新输入!";
				flag = false;
			}

			if (!flag) {
				act.setOutData("error", error);
				return;
			}

			if (flag) {
//				String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
				String str_isAbroad = CommonUtils.checkNull(request.getParamValue("IS_ABROAD"));
				String linkMan = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
				String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
				String fax = CommonUtils.checkNull(request.getParamValue("FAX"));
				String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
				String makerType = CommonUtils.checkNull(request.getParamValue("MAKER_TYPE"));

				TtPartMakerDefinePO ttPartMakerDefinePO = new TtPartMakerDefinePO();
				ttPartMakerDefinePO.setMakerId(CommonUtils.parseLong(SequenceManager.getSequence("")));
				ttPartMakerDefinePO.setMakerCode(makerCode);
				ttPartMakerDefinePO.setMakerName(makerName);
				ttPartMakerDefinePO.setLinkman(linkMan);
				ttPartMakerDefinePO.setTel(tel);
				ttPartMakerDefinePO.setFax(fax);
				ttPartMakerDefinePO.setAddr(addr);
				ttPartMakerDefinePO.setIsAbroad(CommonUtils.parseInteger(str_isAbroad));
				ttPartMakerDefinePO.setMakerType(CommonUtils.parseInteger(makerType));
				ttPartMakerDefinePO.setCreateBy(logonUser.getUserId());
				ttPartMakerDefinePO.setCreateDate(new Date());// 维护日期
				ttPartMakerDefinePO.setState(Constant.STATUS_ENABLE);
//				ttPartMakerDefinePO.setVenderId(CommonUtils.parseLong(venderId));

				dao.insert(ttPartMakerDefinePO);
				act.setOutData("success", "保存成功!");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "制造商信息添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 查询某个制造商的详细信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void queryPartMakerDetail() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId")); // 制造商Id
			Map<String, Object> makerInfo = dao.getPartMakerDetail(makerId);
			act.setOutData("makerInfo", makerInfo);
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("curPage", curPage);

			act.setForword(PARMAKER_INFO_MOD);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改制造商信息
	 * 
	 * @Title :
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void updatePartMakerInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页码
			String makerId = request.getParamValue("MAKER_ID");
			String makerCode = CommonUtils.checkNull(request.getParamValue("MAKER_CODE"));
			String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME"));
//			String venderId = request.getParamValue("VENDER_ID");

			String str_isAbroad = CommonUtils.checkNull(request.getParamValue("IS_ABROAD"));
			String linkMan = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String fax = CommonUtils.checkNull(request.getParamValue("FAX"));
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
			String makerType = CommonUtils.checkNull(request.getParamValue("MAKER_TYPE"));
			String state = CommonUtils.checkNull(request.getParamValue("STATE"));

			TtPartMakerDefinePO spo = new TtPartMakerDefinePO();// 源po
			spo.setMakerId((Long.parseLong(makerId)));
			TtPartMakerDefinePO po = new TtPartMakerDefinePO();// 更新po

			po.setMakerCode(makerCode);
			po.setMakerName(makerName);
			po.setIsAbroad(CommonUtils.parseInteger(str_isAbroad));
			po.setLinkman(linkMan);
			po.setTel(tel);
			po.setFax(fax);
			po.setAddr(addr);
			po.setMakerType(CommonUtils.parseInteger(makerType));
			po.setUpdateDate(new Date());
			po.setUpdateBy(logonUser.getUserId());// 修改人ID
//			po.setVenderId(CommonUtils.parseLong(venderId));

			if (state != "") {
				po.setState(CommonUtils.parseInteger(state));
			}
			if ("".equals(curPage)) {
				curPage = "1";
			}
			dao.update(spo, po);
			act.setOutData("success", "修改成功!");
			act.setOutData("curPage", curPage);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "制造商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 让制造商失效
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-3
	 */
	public void celPartMaker() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId")); // 制造商Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}

			TtPartMakerDefinePO spo = new TtPartMakerDefinePO();// 源po
			spo.setMakerId((Long.parseLong(makerId)));
			spo.setState(Constant.STATUS_ENABLE);// 有效
			TtPartMakerDefinePO po = new TtPartMakerDefinePO();// 更新po
			po.setState(Constant.STATUS_DISABLE);// 无效
			po.setDisableBy((logonUser.getUserId()));
			po.setDisableDate(new Date());
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());

			dao.update(spo, po);
			act.setOutData("success", "失效成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "制造商信息失效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 让制造商有效
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-6
	 */
	public void validPartMaker() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId")); // 制造商Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}

			TtPartMakerDefinePO spo = new TtPartMakerDefinePO();// 源po
			spo.setMakerId((Long.parseLong(makerId)));
			spo.setState(Constant.STATUS_DISABLE);// 失效
			TtPartMakerDefinePO po = new TtPartMakerDefinePO();// 更新po
			po.setState(Constant.STATUS_ENABLE);// 有效
			po.setDisableBy((logonUser.getUserId()));
			po.setDisableDate(new Date());
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());

			dao.update(spo, po);
			act.setOutData("curPage", curPage);
			act.setOutData("success", "设置有效成功!");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "让制造商信息有效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 查看制造商的信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-6
	 */
	public void viewPartMakerInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId")); // 制造商Id
			Map<String, Object> makerInfo = dao.getPartMakerDetail(makerId);
			act.setOutData("makerInfo", makerInfo);

			act.setForword(PARMAKER_INFO_VIEW);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 制造商信息添加页面初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPartMakderRelationInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(req.getParamValue("makerId")); // 制造商Id
			String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			Map<String, Object> makerInfo = dao.getPartMakerDetail(makerId);
			act.setOutData("makerInfo", makerInfo);
			act.setOutData("makerId", makerId);
			act.setOutData("curPage", curPage);
			act.setForword(PARMAKER_SETTING);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "配件制造商信息设置初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 分页查询供应商和配件信息
	 */
	public void queryPartMakerRelation() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId"));
			String part_oldCode = CommonUtils.checkNull(request.getParamValue("part_oldCode"));
			// logger.info("makerId=====" + makerId);
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartMakerRelation(new Long(makerId), part_oldCode, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);// 向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 设置配件和供应商关系
	 */
	public void insertPartMakerRelation() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId"));
			String[] PartIds = request.getParamValues("PartIds");
			for (int i = 1; i < PartIds.length; i++) {
				// 循环判断并把没有的关系插入到数据库中
				TtPartMakerRelationPO spo = new TtPartMakerRelationPO();
				spo.setMakerId(Long.parseLong(makerId));
				spo.setPartId(Long.parseLong(PartIds[i]));
				List<?> result = dao.select(spo);
				if (result.size() == 0) {
					TtPartMakerRelationPO po = new TtPartMakerRelationPO();
					po.setRelaionId(CommonUtils.parseLong(SequenceManager.getSequence("")));
					po.setMakerId(Long.parseLong(makerId)); // 制造商ID
					po.setPartId(Long.parseLong(PartIds[i]));// 配件ID
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());
					po.setState(Constant.STATUS_ENABLE);
					po.setStatus(1);
					dao.insert(po);
				}
			}
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			Map<String, Object> makerInfo = dao.getPartMakerDetail(makerId);
			act.setOutData("makerInfo", makerInfo);
			act.setOutData("makerId", makerId);
			act.setOutData("curPage", curPage);
			act.setForword(PARMAKER_SETTING);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件和制造商关系插入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件制造商关系维护
	 */
	public void upDatePartMakerRelation() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId"));
			String realtionId = CommonUtils.checkNull(request.getParamValue("REALATION_ID"));// 关系ID

			TtPartMakerRelationPO po = new TtPartMakerRelationPO();
			po.setRelaionId(CommonUtils.parseLong(realtionId));
			dao.delete(po);
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			Map<String, Object> makerInfo = dao.getPartMakerDetail(makerId);
			act.setOutData("makerInfo", makerInfo);
			act.setOutData("makerId", makerId);
			act.setOutData("curPage", curPage);
			act.setOutData("success", "删除成功!");
			act.setForword(PARMAKER_SETTING);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件制造商关系维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title :
	 * @Description: 下载制造商模板
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-7
	 */
	public void exportMakerTemplate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
			listHead.add("制造商商编码");
			listHead.add("制造商名称");
			/* listHead.add("供应商编码");
			listHead.add("供应商名称");
            listHead.add("是否默认供应商(1:是；0：否)");
            listHead.add("是否默认制造商(1:是；0：否)");*/
			list.add(listHead);
			// 导出的文件名
			String fileName = "制造商维护模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 导入制造商
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-7
	 */
	public void uploadMakerExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		StringBuffer errorInfo = new StringBuffer("");
		RequestWrapper request = act.getRequest();
		try {
			long maxSize = 1024 * 1024 * 5;
			int errNum = insertIntoTmp(request, "uploadFile", 2, 3, maxSize);

			String err = "";

			if (errNum != 0) {
				switch (errNum) {
				case 1:
					err += "文件列数过多,请修改后再上传!";
					break;
				case 2:
					err += "空行不能大于三行,请修改后再上传!";
					break;
				case 3:
					err += "文件内容不能为空,请修改后再上传!";
					break;
				case 4:
					err += "文件类型错误,请重新上传!";
					break;
				case 5:
					err += "文件不能大于" + maxSize + ",请修改后再上传";
					break;
				default:
					break;
				}
			}

			if (!"".equals(err)) {
				BizException e1 = new BizException(act,
						ErrorCodeConstant.SPECIAL_MEG, err);
				throw e1;
			} else {
				List<Map> list = getMapList();
				List voList = new ArrayList();
				loadVoList(voList, list, errorInfo);
				if (errorInfo.length() > 0) {

					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, errorInfo);
					throw e1;
				}
				for (int i = 0; i < voList.size(); i++) {
					
					TtPartMakerDefinePO makerDefinePO = (TtPartMakerDefinePO) voList.get(i);
					boolean flag = dao.existMakerCode(makerDefinePO.getMakerCode());
					boolean flag1 = dao.existMakerName(makerDefinePO.getMakerName());
					if(flag){//如果供制造商编码已经存在
						errorInfo.append("第"+(i+2)+"行的制造商编码重复,请修改后再上传!<br>");
					}
					if(flag1){//如果制造商名称已经存在
						errorInfo.append("第"+(i+2)+"行的制造商名称重复,请修改后再上传!<br>");
					}
					
					if (errorInfo.length() > 0) {
						BizException e1 = new BizException(act,
								ErrorCodeConstant.SPECIAL_MEG, errorInfo);
						throw e1;
					}
					
					makerDefinePO.setMakerId(Long.parseLong(SequenceManager.getSequence("")));
					makerDefinePO.setIsAbroad(Constant.PARTMAKER_NO);
					makerDefinePO.setMakerType(Constant.PARTMAKER_INNER);
					makerDefinePO.setCreateDate(new Date());
					makerDefinePO.setCreateBy(logonUser.getUserId());
					makerDefinePO.setState(Constant.STATUS_ENABLE);
					
					dao.insert(makerDefinePO);
					}

				}
			    act.setOutData("curPage", "1");
				act.setForword(partMakerInitUrl);

		} catch (Exception e) {
			BizException e1 = null;
			if (e instanceof BizException) {
				e1 = (BizException) e;
			} else {
				e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
						"文件读取错误");
			}
			logger.error(logonUser, e1);
			act.setException(e1);
			act.setOutData("curPage", "1");
			act.setForword(partMakerInitUrl);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 循环获取cell
	 * @param : @param voList
	 * @param : @param list
	 * @param : @param errorInfo
	 * @return :
	 * @throws Exception
	 * @throws : LastDate : 2013-4-7
	 */
	private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo)
			throws Exception {
		if (null == list) {
			list = new ArrayList();
		}

		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(voList, key, cells, errorInfo);
				if (errorInfo.length() > 0) {
					break;
				}
			}
		}

	}
	
	/**
	 * 装载VO
	 * 
	 * @Title :
	 * @Description: TODO
	 * @param : @param list
	 * @param : @param rowNum
	 * @param : @param cells
	 * @param : @param errorInfo
	 * @return :
	 * @throws Exception
	 * @throws : LastDate : 2013-4-7
	 */
	private void parseCells(List list, String rowNum, Cell[] cells,
			StringBuffer errorInfo) throws Exception {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		TtPartMakerDefinePO makerDefinePO = new TtPartMakerDefinePO();

		if ("" == subCell(cells[0].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的制造商编码不能为空,请修改后再上传!");
			return;
		}
		
		if(dao.existMakerCode(subCell(cells[0].getContents().trim()))){
			errorInfo.append("第" + rowNum + "行的制造商编码已经存在,请修改后再上传!");
			return;
		}
		
		makerDefinePO.setMakerCode(subCell(cells[0].getContents().trim()));
		
		if ("" == subCell(cells[1].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的制造商名称不能为空,请修改后再上传!");
			return;
		}

		if(dao.existMakerName(subCell(cells[1].getContents().trim()))){
			errorInfo.append("第" + rowNum + "行的制造商名称已经存在,请修改后再上传!");
			return;
		}
		makerDefinePO.setMakerName(subCell(cells[1].getContents().trim()));
		
		/*if ("" == subCell(cells[3].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的供应商名称不能为空,请修改后再上传!");
			return;
		}
		
		if ("" == subCell(cells[2].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的供应商编码不能为空,请修改后再上传!");
			return;
		}else{
			Map map = venderDao.getPartVenderByCode(subCell(cells[2].getContents().trim()));
			if(map!=null){//如果该供应商编码在供应商表中已经存在
				makerDefinePO.setVenderId(((BigDecimal)map.get("VENDER_ID")).longValue());
			}else{//如果该供应商编码在供应商表中不存在,就像供应商表中插入一条记录
				TtPartVenderDefinePO venderDefinePO = new TtPartVenderDefinePO();
				venderDefinePO.setVenderId(Long.parseLong(SequenceManager.getSequence("")));
				venderDefinePO.setVenderCode(subCell(cells[2].getContents().trim()));
				venderDefinePO.setVenderName(subCell(cells[3].getContents().trim()));
				venderDefinePO.setIsAbroad(Constant.PARTVENDER_NO);
				venderDefinePO.setVenderType(Constant.PARTVENDER_INNER);
				venderDefinePO.setCreateDate(new Date());
				venderDefinePO.setCreateBy(logonUser.getUserId());
				venderDefinePO.setState(Constant.STATUS_ENABLE);
				makerDefinePO.setVenderId(venderDefinePO.getVenderId());
				venderDao.insert(venderDefinePO);
			}
		}*/
		
		list.add(makerDefinePO);
	}
	
	/**
	 * 截取字符串
	 * 
	 * @Title :
	 * @Description: TODO
	 * @param : @param orgAmt
	 * @param : @return
	 * @return :
	 * @throws : LastDate : 2013-4-7
	 */
	private String subCell(String orgAmt) {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}
}
