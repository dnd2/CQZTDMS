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
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartVenderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPriceHistoryPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.tools.POGen;

/**
 * @ClassName : TtPartVender
 * @Description : 供应商信息维护
 * @author : chenjunjiang 
 * CreateDate : 2013-4-2
 */
public class PartVenderManager extends PartBaseImport implements PTConstants {

	public Logger logger = Logger.getLogger(PartVenderManager.class);
	private PartVenderDao dao = PartVenderDao.getInstance();
	
	/**
	 * 
	 * @Title :
	 * @Description: 查询初始化
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void queryPartVenderInit() {
		ActionContext act = ActionContext.getContext();
		//RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			 act.setForword(partVenderInitUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商信息维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 分页查询供应商信息
	 */
	public void queryPartVenderInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE"));// 供应商代码
			venderCode = venderCode.toUpperCase();
			String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));// 供应商名称
			String str_state = CommonUtils.checkNull(request.getParamValue("STATE"));
			String dlrInvTpeStr = CommonUtils.checkNull(request.getParamValue("dlrInvTpe"));
			
			TtPartVenderDefinePO bean = new TtPartVenderDefinePO();
			bean.setVenderCode(venderCode);
			bean.setVenderName(venderName);

			int state = 0;
			int dlrInvTpe = 0;

			if (!"".equals(str_state)) {
				state = CommonUtils.parseInteger(str_state);
			}
			
			if (!"".equals(dlrInvTpeStr)) {
				dlrInvTpe = CommonUtils.parseInteger(dlrInvTpeStr);
			}
			
			bean.setState(state);
			bean.setInvType(dlrInvTpe);
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartVenderList(bean, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 供应商信息添加初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPartVenderInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addPartVenderInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "供应商信息添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 添加供应商信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void addPartVenderInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String error = "";
			boolean flag = true;
			// 得到页面参数
			String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE"));
			String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));
			if (dao.existVenderCode(venderCode)) {// 判断供应商编码是否存在
				error = error + "供应商编码已经存在,请重新输入!<br>";
				flag = false;
			}

			if (dao.existVenderName(venderName)) {// 判断供应商名称是否存在
				error = error + "供应商名称已经存在,请重新输入!";
				flag = false;
			}

			if (!flag) {
				act.setOutData("error", error);
				return;
			}

			if (flag) {
				String str_isAbroad = CommonUtils.checkNull(request.getParamValue("IS_ABROAD"));
				String linkMan = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
				String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
				String fax = CommonUtils.checkNull(request.getParamValue("FAX"));
				String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
				String venderType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE"));
//				String dlrInvTpe = CommonUtils.checkNull(request.getParamValue("dlrInvTpe"));

				TtPartVenderDefinePO ttPartVenderDefinePO = new TtPartVenderDefinePO();
				ttPartVenderDefinePO.setVenderId(CommonUtils.parseLong(SequenceManager.getSequence("")));
				ttPartVenderDefinePO.setVenderCode(venderCode);
				ttPartVenderDefinePO.setVenderName(venderName);
				ttPartVenderDefinePO.setIsAbroad(CommonUtils.parseInteger((str_isAbroad)));
				ttPartVenderDefinePO.setLinkman(linkMan);
				ttPartVenderDefinePO.setTel(tel);
				ttPartVenderDefinePO.setFax(fax);
				ttPartVenderDefinePO.setAddr(addr);
				ttPartVenderDefinePO.setVenderType(CommonUtils.parseInteger(venderType));
				ttPartVenderDefinePO.setInvType(Constant.DLR_INVOICE_TYPE_02);
				ttPartVenderDefinePO.setCreateBy(logonUser.getUserId());
				ttPartVenderDefinePO.setCreateDate(new Date());// 新建日期
				ttPartVenderDefinePO.setState(Constant.STATUS_ENABLE);

				dao.insert(ttPartVenderDefinePO);
				act.setOutData("success", "保存成功!");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "供应商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 某个供应商详细信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void queryPartVenderDetail() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId")); // 供应商Id
			Map<String, Object> venderInfo = dao.getPartVenderDetail(venderId);
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("curPage", curPage);
			act.setOutData("venderInfo", venderInfo);
			act.setForword(PARTVENDER_INFO_MOD);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改供应商信息
	 * 
	 * @Title :
	 * @Description: TODO
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-2
	 */
	public void updatePartVenderInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String venderId = request.getParamValue("VENDER_ID");
			String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE"));
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页码

			String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));
			String str_isAbroad = CommonUtils.checkNull(request.getParamValue("IS_ABROAD"));
			String linkMan = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String fax = CommonUtils.checkNull(request.getParamValue("FAX"));
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
			String venderType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE"));
			String state = CommonUtils.checkNull(request.getParamValue("STATE"));
//			String dlrInvTpe = CommonUtils.checkNull(request.getParamValue("dlrInvTpe"));

			TtPartVenderDefinePO spo = new TtPartVenderDefinePO();// 源po
			spo.setVenderId((Long.parseLong(venderId)));
			TtPartVenderDefinePO po = new TtPartVenderDefinePO();// 更新po
			po.setVenderCode(venderCode);
			po.setVenderName(venderName);
			//modify by yuan 不选择判断
			if (str_isAbroad != null && !"".equals(str_isAbroad)) {
				po.setIsAbroad(CommonUtils.parseInteger(str_isAbroad));
			} else {
				po.setIsAbroad(null);
			}
			po.setLinkman(linkMan);
			po.setTel(tel);
			po.setFax(fax);
			po.setAddr(addr);
			//modify by yuan 不选择判断
			if (venderType != null && !"".equals(venderType)) {
				po.setVenderType(CommonUtils.parseInteger(venderType));
			} else {
				po.setVenderType(null);
			}
			po.setUpdateDate(new Date());
			po.setUpdateBy(logonUser.getUserId());// 修改人

			if (!"".equals(state)) {
				po.setState(CommonUtils.parseInteger(state));
			}
//			po.setInvType(CommonUtils.parseInteger(dlrInvTpe));
			po.setInvType(Constant.DLR_INVOICE_TYPE_02);
			dao.update(spo, po);
			if ("".equals(curPage)) {
				curPage = "1";
			}
			// dao.validPartVender(venderId);
            act.setOutData("success", "修改成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "供应商信息修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 让供应商失效
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-3
	 */
	public void celPartVender() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try {
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId")); // 供应商Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}

			TtPartVenderDefinePO spo = new TtPartVenderDefinePO();// 源po
			spo.setVenderId((Long.parseLong(venderId)));
			spo.setState(Constant.STATUS_ENABLE);
			TtPartVenderDefinePO po = new TtPartVenderDefinePO();// 更新po
			po.setState(Constant.STATUS_DISABLE);
			po.setDisableBy(logonUser.getUserId());
			po.setDisableDate(new Date());
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());

			dao.update(spo, po);
			act.setOutData("success", "设置无效成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "供应商信息失效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 让供应商有效
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-6
	 */
	public void validPartVender() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try {
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId")); // 供应商Id
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			// dao.validPartVender(venderId);
			act.setOutData("curPage", curPage);

			TtPartVenderDefinePO spo = new TtPartVenderDefinePO();// 源po
			spo.setVenderId((Long.parseLong(venderId)));
			spo.setState(Constant.STATUS_DISABLE);
			TtPartVenderDefinePO po = new TtPartVenderDefinePO();// 更新po
			po.setState(Constant.STATUS_ENABLE);
			po.setDisableBy(logonUser.getUserId());
			po.setDisableDate(new Date());
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			dao.update(spo, po);
			act.setOutData("success", "设置有效成功!");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "让供应商信息有效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 查看供应商的信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-6
	 */
	public void viewPartVenderInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId")); // 供应商Id
			Map<String, Object> venderInfo = dao.getPartVenderDetail(venderId);
			act.setOutData("venderInfo", venderInfo);

			act.setForword(PARTVENDER_INFO_VIEW);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title      : 
	 * @Description: 查询制造商信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-19
	 */
	public void queryPartMakerInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
			
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartMakerListByVId(venderId, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :
	 * @Description: 下载供应商模板
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-7
	 */
	public void exportVenderTemplate() {
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
			listHead.add("供应商编码");
			listHead.add("供应商名称");
			listHead.add("联系人");
			listHead.add("联系电话");
			listHead.add("地址");

			list.add(listHead);
			// 导出的文件名
			String fileName = "供应商维护模板.xls";
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
	 * @Description: 导入供应商
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-7
	 */
	public void uploadVenderExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		StringBuffer errorInfo = new StringBuffer("");
		RequestWrapper request = act.getRequest();
		try {
			long maxSize = 1024 * 1024 * 5;
			int errNum = insertIntoTmp(request, "uploadFile", 5, 3, maxSize);

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
				List<TtPartVenderDefinePO> voList = new ArrayList<TtPartVenderDefinePO>();
				loadVoList(voList, list, errorInfo);
				if (errorInfo.length() > 0) {

					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, errorInfo);
					throw e1;
				}
				for (int i = 0; i < voList.size(); i++) {
					
					TtPartVenderDefinePO venderDefinePO = voList.get(i);
					boolean flag = dao.existVenderCode(venderDefinePO.getVenderCode());
					boolean flag1 = dao.existVenderName(venderDefinePO.getVenderName());
					if(flag){//如果供应商编码已经存在
						errorInfo.append("第"+(i+2)+"行的供应商编码重复,请修改后再上传!<br>");
					}
					if(flag1){//如果供应商名称已经存在
						errorInfo.append("第"+(i+2)+"行的供应商名称重复,请修改后再上传!<br>");
					}
					
					if (errorInfo.length() > 0) {
						BizException e1 = new BizException(act,
								ErrorCodeConstant.SPECIAL_MEG, errorInfo);
						throw e1;
					}
					
					venderDefinePO.setVenderId(Long.parseLong(SequenceManager.getSequence("")));
					venderDefinePO.setIsAbroad(Constant.PARTVENDER_NO);
					venderDefinePO.setVenderType(Constant.PARTVENDER_INNER);
					venderDefinePO.setCreateDate(new Date());
					venderDefinePO.setCreateBy(logonUser.getUserId());
					venderDefinePO.setState(Constant.STATUS_ENABLE);
					venderDefinePO.setInvType(Constant.DLR_INVOICE_TYPE_02);
					dao.insert(venderDefinePO);
					}

				}
				act.setForword(partVenderInitUrl);

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
			act.setForword(partVenderInitUrl);
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
	private void loadVoList(List<TtPartVenderDefinePO> voList, List<Map> list, StringBuffer errorInfo)
			throws Exception {
		if (null == list) {
			list = new ArrayList<Map>();
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
	private void parseCells(List<TtPartVenderDefinePO> list, String rowNum, Cell[] cells,
			StringBuffer errorInfo) throws Exception {

		TtPartVenderDefinePO venderDefinePO = new TtPartVenderDefinePO();

		if ("" == subCell(cells[0].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的供应商编码不能为空,请修改后再上传!");
			return;
		}
		
		if(dao.existVenderCode(subCell(cells[0].getContents().trim()))){
			errorInfo.append("第" + rowNum + "行的供应商编码已经存在,请修改后再上传!");
			return;
		}
		
		venderDefinePO.setVenderCode(subCell(cells[0].getContents().trim()));
		
		if ("" == subCell(cells[1].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的供应商名称不能为空,请修改后再上传!");
			return;
		}

		if(dao.existVenderName(subCell(cells[1].getContents().trim()))){
			errorInfo.append("第" + rowNum + "行的供应商名称已经存在,请修改后再上传!");
			return;
		}
		venderDefinePO.setVenderName(subCell(cells[1].getContents().trim()));
		// 联系人
		venderDefinePO.setLinkman(subCell(cells[2].getContents().trim()));
		// 联系电话
		venderDefinePO.setTel(subCell(cells[3].getContents().trim()));
		// 地址
		venderDefinePO.setAddr(subCell(cells[4].getContents().trim()));
		list.add(venderDefinePO);
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
	
	/**
	 * <p>Description: 供应商配件比例初始化</p>
	 */
	public void partVenderRelationInit(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_VENDER_RELATION);
       } catch (Exception e) {// 异常方法
           BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商配件比例维护初始化");
           logger.error(logonUser, e1);
           act.setException(e1);
       }
	}
	
	/**
	 * <p>Description: 配件供应商供货比例查询</p>
	 */
	public void partVenderRelationQuery(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("PART_OLDCODE", CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")));
            paramMap.put("PART_CNAME", CommonUtils.checkNull(request.getParamValue("PART_CNAME")));
            paramMap.put("STATE", CommonUtils.checkNull(request.getParamValue("STATE")));
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.qeryPartInfoPageList(paramMap, curPage, Constant.PAGE_SIZE);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商配件比例查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
	}
	
	/**
	 * <p>Description: 配件供应商供货比例维护初始化</p>
	 */
	public void partVenderRelationMagInit(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            // 配件ID
            paramMap.put("PART_ID", CommonUtils.checkNull(request.getParamValue("partId")));
            // 根据配件ID查询配件信息
            Map<String, Object> partMap = dao.getPartInfoList(paramMap).get(0);
            // 根据配件ID查询供应商列表
            List<Map<String, Object>> relationList = dao.getPartVenderRelationList(paramMap);
            act.setOutData("partMap", partMap);
            act.setOutData("relationList", relationList);
            act.setOutData("curPage", request.getParamValue("curPage"));
            act.setForword(PART_VENDER_RELATION_MAG); 
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商配件比例维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
	}

	/**
	 * <p>Description: 保存配件供应商供货比例</p>
	 */
	@SuppressWarnings("unchecked")
    public void savePartVenderRelation(){
	    ActionContext act = ActionContext.getContext();
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    RequestWrapper req = act.getRequest();
	    try {
	        // 价格ID
            String priceIds = CommonUtils.checkNull(req.getParamValue("PRICE_ID"));
            // 比例值
            String coeefNums = CommonUtils.checkNull(req.getParamValue("COEEF_NUMS"));
            String[] priceIdArr = priceIds.split(",");
            String[] coeefNumArr = coeefNums.split(",");
            for(int i = 0; i < priceIdArr.length; i++){
                // 条件po
                TtPartBuyPricePO queryPo = new TtPartBuyPricePO();
                queryPo.setPriceId(Long.parseLong(priceIdArr[i]));
                // 更新值po
                TtPartBuyPricePO updatePo = new TtPartBuyPricePO();
                Float coeffNUm =  Float.parseFloat(coeefNumArr[i])/100;
                updatePo.setCoeffNum(coeffNUm);
                updatePo.setUpdateBy(logonUser.getUserId());
                updatePo.setUpdateDate(new Date());
                dao.update(queryPo, updatePo);
             } 
	        act.setOutData("returnCode", 1);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商配件比例维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
	}
	
}
