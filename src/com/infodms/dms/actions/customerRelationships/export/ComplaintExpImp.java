package com.infodms.dms.actions.customerRelationships.export;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.Download;
import com.infodms.dms.actions.claim.serviceActivity.ServiceActivityVinImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtCrComplaintsCallPO;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.po.TtCrComplaintsTmpPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: ComplaintExpImp 
* @Description: TODO(投诉信息导入导出类) 
* @author liuqiang 
* @date Sep 11, 2010 3:36:24 PM 
*
 */
public class ComplaintExpImp {
	private final Logger logger = Logger.getLogger(ComplaintExpImp.class);
	//投诉信息导入初始化页面
	private final String COM_IMP_INIT = "/jsp/customerRelationships/complaintDisposal/complaintDiposalImport.jsp";
	//投诉信息导入预览页面
	private final String COM_IMP_PRE = "/jsp/customerRelationships/complaintDisposal/complaintDiposalImportPreview.jsp";
	//文件服务器上的文件名
	private final String FILE_NAME = "/20141009.xls";
	//下载显示的文件名
	private final String DOWN_NAME = "投诉信息导入模板.xls";
	
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private ComplaintDisposalDao dao = ComplaintDisposalDao.getInstance();
	/**导入失败记录的文件信息**/
	private  Map<Long, List<String>> errors = new HashMap<Long, List<String>>();
	private final String ERR_FILE = "error.txt";
	/**投诉单编号**/
	private String code;
	public void complaintImpInit() {
		try {
			act.setForword(COM_IMP_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: complaintImpDown 
	* @Description: TODO(导入模板下载) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void complaintImpDown() {
		try {
			Properties props = new Properties();
			//读取FileStore.properties配置文件
    		props.load(ServiceActivityVinImport.class.getClassLoader().getResourceAsStream("FileStore.properties"));
    		String filePath = props.getProperty("domain");
    		Download.downloadTemplateXls(ActionContext.getContext().getResponse(), DOWN_NAME, filePath, DOWN_NAME);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"导入模板下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void complaintImp() throws Exception {
		List<String> err = errors.get(logonUser.getUserId());
		if (null != err && err.size() > 0) {
			err.clear();//清空错误记录
		}
		FileObject uploadFile = request.getParamObject("uploadFile");//获取上传的Excel
		ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
		List<Map<String, String>> maps = ExcelUtil.impExcel(is, 1, 36);//解析Excel
		for (Map<String, String> map : maps) {
			try {
				if (!Utility.testString(map.get("请求编号"))) {
					continue;
				}
				TtCrComplaintsTmpPO po = new TtCrComplaintsTmpPO();
				po.setCompCode(map.get("请求编号"));
				this.code = po.getCompCode();
				List pos = dao.select(po);
				if (null != pos && pos.size() > 0) {
					logger.info("投诉信息" + map.get("请求编号") + "已存在");
					TtCrComplaintsTmpPO po1 = (TtCrComplaintsTmpPO) pos.get(0);
					TtCrComplaintsTmpPO srcpo = new TtCrComplaintsTmpPO();
					srcpo.setCompCode(po1.getCompCode());
					StringBuffer callCenCon = new StringBuffer();
					TtCrComplaintsTmpPO despo = new TtCrComplaintsTmpPO();
					if (Utility.testString(po1.getCallCenCon())) {
						callCenCon.append(po1.getCallCenCon()).append("  ").append(CommonUtils.checkNull(map.get("呼叫中心处理情况")));
					} else {
						callCenCon.append(CommonUtils.checkNull(map.get("呼叫中心处理情况")));
					}
					despo.setCallCenCon(callCenCon.toString());
					dao.update(srcpo, despo);//将呼叫中心处理情况累加到原来的记录
					continue;
				}
				po = new TtCrComplaintsTmpPO();
				po.setCompId(Long.parseLong(SequenceManager.getSequence("")));
				po.setCompCode(map.get("请求编号"));
				po.setCreateDate(DateTimeUtil.stringToDate(map.get("创建时间")));
				po.setCreateBy(logonUser.getUserId());
				TmOrgPO tmop= new TmOrgPO();
				tmop.setOrgName(map.get("大区"));
				List orgs = dao.select(tmop);
				testList(orgs, map.get("大区"));
				po.setOwnOrgId(((TmOrgPO) orgs.get(0)).getOrgId());				//大区
				po.setCompSource(getTcCodeByName(map.get("信息来源")));			//信息来源
				po.setLinkMan(map.get("用户名"));									//用户名
				po.setProvince(getRegionByName(map.get("省份"), 1));				//省份
				po.setCity(getRegionByName(map.get("城市"), 2));					//城市
				po.setTel(map.get("电话"));										//电话
				po.setComType(getTcCodeByName(dropPreStr(map.get("请求类型"), ".")));	//投诉大类
				po.setCompType(getTcCodeByName(dropPreStr(map.get("问题代码"), "_")));	//投诉小类
				po.setCompContent(CommonUtils.checkNull(map.get("问题汇总")));							//客户投诉内容
				po.setCallCenCon(CommonUtils.checkNull(map.get("呼叫中心处理情况")));						//呼叫中心处理情况
				if (Utility.testString(map.get("被投经销商"))) {
					po.setCompDealer(getDealerByCode(dropSufStr(map.get("被投经销商"), ".")));	//被投诉经销商
				}
				po.setCompLevel(getTcCodeByName(map.get("重要程度").endsWith("投诉") ? map.get("重要程度") : 
									map.get("重要程度").concat("投诉")));				//判断重要程度是否以投诉结束,投诉等级
				po.setModelCode(map.get("车型"));									//车型
				po.setStatus(Constant.COMP_STATUS_TYPE_01);                         //未分配状态
				po.setAuditResult(getTcCodeByName(map.get("是否处理完")));			//是否处理完
				po.setUserId(getUserByName(map.get("处理人")));						//处理人
				po.setShut(map.get("是否关闭"));
				po.setSatisfied(map.get("是否满意"));
				po.setCause(map.get("不满意的原因"));
				po.setCallPerson(map.get("回访人"));
				po.setCallCycle(map.get("须回复周期"));
				po.setRemark(map.get("备注"));
				po.setCallDate(DateTimeUtil.stringToDate(map.get("回访时间")));
				po.setCallFail(map.get("回访未能关闭"));
				po.setSupport(map.get("技术室处理情况"));
				po.setSupportPerson(map.get("技术室处理人"));
				dao.insert(po);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		act.setForword(COM_IMP_PRE);
	}
	/**
	 * 
	* @Title: queryImpTmp 
	* @Description: TODO(查询导入临时表的投诉信息) 
	* @throws
	 */
	public void queryImpTmp() {
		Integer curPage = request.getParamValue("curPage") != null ?
				Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页	
		PageResult<Map<String, Object>> ps = dao.queryImpComplaintDisposal(logonUser.getUserId(), curPage, Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	
	
	/**
	 * 
	* @Title: testList 
	* @Description: TODO(判断list是否有值) 
	* @param @param pos
	* @param @param msg  Excel表头
	 */
	@SuppressWarnings("unchecked")
	private void testList(List pos, String msg) {
		if (null == pos || pos.size() == 0) {
			saveError(msg);
			throw new IllegalArgumentException("Cann't find key " + msg);
		}
	}
	/**
	 * 
	* @Title: testMap 
	* @Description: TODO(判断map是否有值) 
	* @param @param pos
	* @param @param msg    设定文件 
	 */
	private void testMap(Map<String, Object> pos, String msg) {
		if (null == pos || pos.size() == 0) {
			saveError(msg);
			throw new IllegalArgumentException("Cann't find key " + msg);
		}
	}
	/**
	 * 
	* @Title: saveError 
	* @Description: TODO(将错误消息放到内存中) 
	* @param    错误消息
	* @throws
	 */
	private void saveError(String msg) {
		List<String> err = errors.get(logonUser.getUserId());
		if (null == err) {
			err = new ArrayList<String>();
		}
		err.add("投诉单[" + code + "] 没有找到 [" + msg + "]");
		errors.put(logonUser.getUserId(), err);
	}
	/**
	 * 
	* @Title: getTcCodeByName 
	* @Description: TODO(根据CODE_NAME查询CODE_ID) 
	* @param @param msg CODE_NAME
	* @param @return    设定文件 
	* @return Integer    CODE_ID
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private Integer getTcCodeByName(String msg) {
		if (!Utility.testString(msg)) {
			return null;
		}
		TcCodePO tcCode = new TcCodePO();
		tcCode.setCodeDesc(msg);
		List pos = dao.select(tcCode);
		testList(pos, msg);
		TcCodePO po = (TcCodePO) pos.get(0);
		return Integer.parseInt(po.getCodeId());
	}
	/**
	 * 
	* @Title: getRegionByName 
	* @Description: TODO(根据名字查询省/市code) 
	* @param @param 省/市名字
	* @param @param flag 1 代表省 2 代表市
	* @param @return    设定文件 
	* @return Integer  省/市code	 
	* */
	@SuppressWarnings("unchecked")
	private Integer getRegionByName(String name, int flag) {
		switch (flag) {
		case 1 : 
			//查询省
			Map<String, Object> pos = dao.queryProvinceByName(name);
			testMap(pos, name);
			String provinceCode = pos.get("REGION_CODE").toString();
			return Integer.parseInt(provinceCode);
		case 2 :
			//查询市
			Map<String, Object> map = dao.queryCityByName(dropPreStr(name, "."));
			testMap(map, name);
			String cityCode = map.get("REGION_CODE").toString();
			return Integer.parseInt(cityCode);
		default :
			throw new IllegalArgumentException("Unknown region " + flag);
		}
	}
	/**
	 * 
	* @Title: getDealerByCode 
	* @Description: TODO(根据经销商名取ID) 
	* @param @param name 经销商代码
	* @return Long   经销商ID
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private Long getDealerByCode(String code) {
		TmDealerPO dealer = new TmDealerPO();
		dealer.setDealerCode(code);
		List pos = dao.select(dealer);
		testList(pos, code);
		dealer = (TmDealerPO) pos.get(0);
		return dealer.getDealerId();
	}
	/**
	 * 
	* @Title: getUserByName 
	* @Description: TODO(根据用户名取ID) 
	* @param @param name 用户名
	* @return Long   用户ID
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private Long getUserByName(String name) {
		if (!Utility.testString(name)) {
			return null;
		} 
		TcUserPO user = new TcUserPO();
		user.setName(name);
		List pos = dao.select(user);
		testList(pos, name);
		user = (TcUserPO) pos.get(0);
		return user.getUserId();
	}
	/**
	 * 
	* @Title: dropPreStr 
	* @Description: TODO(删除字符串的前缀,比如城市,投诉大类,小类) 
	* @param @param name 要处理的字符串
	* @param @return    设定文件 
	* @return String    处理完的字符串
	* @throws
	 */
	private String dropPreStr(String name, String flag) {
		int pos = name.indexOf(flag);
		name = name.substring(pos + 1);
		return name;
	}
	/**
	 * 
	* @Title: dropSufStr 
	* @Description: TODO(删除字符串的后缀,比如经销商,只截取前面的CODE) 
	* @param @param name 要处理的字符串
	* @param @param flag 截取标志
	* @throws
	 */
	private String dropSufStr(String name, String flag) {
		int pos = name.indexOf(flag);
		return pos > 0 ? name.substring(0, pos) : name;
	}
	
	/**
	 * 
	* @Title: delImpTmp 
	* @Description: TODO(取消按钮,删除临时表中的数据) 
	* @throws
	 */
	public void delImpTmp() {
		try {
			dao.deleteTmp(logonUser.getUserId());
			act.setForword(COM_IMP_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * 
	* @Title: saveImp 
	* @Description: TODO(将临时表中的投诉信息删除,导入到业务表中) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void saveImp() {
		try {
			TtCrComplaintsTmpPO po = new TtCrComplaintsTmpPO();
			po.setCreateBy(logonUser.getUserId());
			List<TtCrComplaintsTmpPO> pos = dao.select(po);//临时表的数据
			for (TtCrComplaintsTmpPO tpo : pos) {
				TtCrComplaintsPO npo = new TtCrComplaintsPO();//要插入业务表的记录
				BeanUtils.copyProperties(npo, tpo);
				TtCrComplaintsPO tmp = new TtCrComplaintsPO();//要进行查询的条件
				tmp.setCompCode(npo.getCompCode());
				List npos = dao.select(tmp);  //根据要导入的投诉单号进行查询
				if (null != npos && npos.size() > 0) {
					//如果有此记录,根据投诉单号更新呼叫中心处理情况
					TtCrComplaintsPO tmp1 = new TtCrComplaintsPO();
					tmp1.setCompCode(npo.getCompCode());
					TtCrComplaintsPO tmp2 = new TtCrComplaintsPO();
					tmp2.setCallCenCon(CommonUtils.checkNull(npo.getCallCenCon()));
					dao.update(tmp1, tmp2);
					//更新投诉信息子表 根据投诉ID和操作人更新操作结果
					TtCrComplaintsAuditPO srcAudit = new TtCrComplaintsAuditPO();
					srcAudit.setCreateBy(tpo.getUserId());
					TtCrComplaintsPO poo = (TtCrComplaintsPO) npos.get(0);
					srcAudit.setCompId(poo.getCompId());
					TtCrComplaintsAuditPO desAudit = new TtCrComplaintsAuditPO();
					desAudit.setAuditResult(tpo.getAuditResult());
					dao.update(srcAudit, desAudit);
					//将投诉回访信息插入到投诉信息回访表
					if (!Utility.testString(tpo.getShut())) {
						continue;
					}
					TtCrComplaintsCallPO call = new TtCrComplaintsCallPO();
					call.setCompId(poo.getCompId());
					List calls = dao.select(call);
					if (null != calls && calls.size() > 0) {
						continue;
					}
					call.setId(Long.parseLong(SequenceManager.getSequence("")));
					call.setShut(tpo.getShut());
					call.setSatisfied(tpo.getSatisfied());
					call.setCause(tpo.getCause());
					call.setCallPerson(tpo.getCallPerson());
					call.setCallCycle(tpo.getCallCycle());
					call.setRemark(tpo.getRemark());
					call.setCallDate(tpo.getCallDate());
					call.setCallFail(tpo.getCallFail());
					call.setSupport(tpo.getSupport());
					call.setSupportPerson(tpo.getSupportPerson());
					dao.insert(call);
				} else {
					dao.insert(npo);
				}
			}
			dao.deleteTmp(logonUser.getUserId());
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	public void expComplaint() {
		try {
			String[] compIds = request.getParamValues("compIds");
			StringBuffer str = new StringBuffer();
			for (String compId : compIds) {
				str.append(compId).append(",");
			}
			str.deleteCharAt(str.length() - 1);
			List<Map<String, Object>> maps = dao.queryExportComplaint(str.toString());
			ResponseWrapper response = act.getResponse();
			ExcelUtil.expExcel(response, maps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void expError() {
		PrintWriter pw = null;
		try {
			ResponseWrapper response = act.getResponse();
			response.setContentType("application/text");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(ERR_FILE, "utf-8"));
			List<String> errs = errors.get(logonUser.getUserId());
			OutputStream os = response.getOutputStream();
			pw = new PrintWriter(os);
			for (int i = 0; i < errs.size(); i++) {
				pw.print(String.valueOf(i + 1));
				pw.print(". ");
				pw.println(errs.get(i));
			}
			pw.flush();
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉查询(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
	
}
