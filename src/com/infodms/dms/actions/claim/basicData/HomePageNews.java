package com.infodms.dms.actions.claim.basicData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import com.infodms.dms.actions.crmphone.common.Common;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.transaction.annotation.Transactional;

import com.infodms.dms.actions.claim.serviceActivity.Download;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.actions.sysbusinesparams.businesparamsmanage.BusinessAreaManage;
import com.infodms.dms.actions.util.NewsFileStoreImple;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.claim.basicData.HomePageNewsDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrKnowledgebasePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrNewsOrgPO;
import com.infodms.dms.po.TtAsWrNewsPO;
import com.infodms.dms.po.TtAsWrNewsRolePO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.BeanUtils;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.excel.ExcelUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.filestore.FileStoreException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
@Transactional(rollbackFor=Exception.class)
public class HomePageNews extends BaseDao {
	public Logger logger = Logger.getLogger(BusinessAreaManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final HomePageNewsDao dao = HomePageNewsDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private final ClaimBillMaintainDAO cbmDao = ClaimBillMaintainDAO.getInstance();
	private final String addNews = "/jsp/claim/basicData/homePageNews.jsp";
	private final String addApplicationKnowledgeBase = "/jsp/claim/basicData/addApplicationKnowledgeBase.jsp";
	private final String modifyNews = "/jsp/claim/basicData/homePageNewsModify.jsp";
	private final String knowledgeBaseModify = "/jsp/claim/basicData/knowledgeBaseModify.jsp";
	private final String mainNews = "/jsp/claim/basicData/homePageNewsMain.jsp";
	private final String mainNewsQuery = "/jsp/claim/basicData/homePageNewsMainQuery.jsp";
	private final String importNewsUrl = "/jsp/claim/basicData/homepagenews/homePageNewsImport.jsp";
	private final String dealerNewsQuery = "/jsp/claim/basicData/dealerNewsMainQuery.jsp";
	private final String mainNewsDealer = "/jsp/claim/basicData/homePageNewsMainDealer.jsp";
	private final String knowledgeBaseDealer = "/jsp/claim/basicData/knowledgeBaseDealer.jsp";
	private final String ApplicationKnowledgeBase = "/jsp/claim/basicData/ApplicationKnowledgeBase.jsp";
	private final String viewNews = "/jsp/claim/basicData/homePageNewsView.jsp";
	private final String knowledgeBaseView = "/jsp/claim/basicData/knowledgeBaseView.jsp";
	private final String viewPs = "/jsp/claim/basicData/receiveNewsDetailQuery.jsp";
	private final String DEALERNEWSDETAILQUERY = "/jsp/claim/basicData/dealerNewsDetailQuery.jsp";
	
	/********
	 * 首页新闻跳转页面
	 */
	public void mainNews()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		if (Utility.testString(logonUser.getOemCompanyId()))
		{ // 判断是车厂用户还是经销商 YH 2010.12.3
			act.setForword(mainNewsDealer);
		}
		else
		{
			act.setForword(mainNews);
		}
	}
	
	/**
	 * 首页新闻查询跳转页面
	 */
	public void mainNewsQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		act.setForword(mainNewsQuery);
	}
	
	
	/**
	 * 经销商已阅明细跳转页面
	 */
	public void dealerNews()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		act.setForword(dealerNewsQuery);
	}
	
	public void dealerNewsReadDetailInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		List<Map<String, Object>> orgList = null;
		try {
			orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM,logonUser.getOrgId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msgTypeString = request.getParamValue("messageType");
		String viewNewsType = request.getParamValue("viewNewsType");
		act.setOutData("orglist", orgList);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		act.setOutData("newsId", CommonUtils.checkNull(request.getParamValue("newsId")));
		act.setOutData("msgType", CommonUtils.checkNull(msgTypeString));
		act.setOutData("viewNewsType", viewNewsType);
		act.setForword(DEALERNEWSDETAILQUERY);
	}
	
	
	
	/**
	 * 首页新闻查询跳转页面
	 */
	public void dealerNewsReadQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			HashMap<String, Object> paramsMap = new HashMap<String,Object>();
			String newsCode = CommonUtils.checkNull(request.getParamValue("newsCode"));
			String person = CommonUtils.checkNull(request.getParamValue("person"));
			String applyDateStart = CommonUtils.checkNull(request.getParamValue("APPLY_DATE_START"));
			String applyDateEnd = CommonUtils.checkNull(request.getParamValue("APPLY_DATE_END"));
			String title = CommonUtils.checkNull(request.getParamValue("title"));
			String newsStatus = CommonUtils.checkNull(request.getParamValue("NEWS_STATUS"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			
			paramsMap.put("newsCode", newsCode);
			paramsMap.put("person", person);
			paramsMap.put("applyDateStart", applyDateStart);
			paramsMap.put("applyDateEnd", applyDateEnd);
			paramsMap.put("title", title);
			paramsMap.put("newsStatus", newsStatus);
			paramsMap.put("dealerCode", dealerCode);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerNewsReadPage(curPage, Constant.PAGE_SIZE, paramsMap);
			
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新闻明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 首页新闻查询跳转页面
	 */
	@SuppressWarnings("unchecked")
	public void dealerNewsReadDetailQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			HashMap<String, Object> paramsMap = new HashMap<String,Object>();
			String newsId = CommonUtils.checkNull(request.getParamValue("newsId"));
			String rootOrgId = CommonUtils.checkNull(request.getParamValue("__large_org"));
			String orgId = CommonUtils.checkNull(request.getParamValue("__province_org"));
			String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds"));
			String common = CommonUtils.checkNull(request.getParamValue("ttt"));
			String isRead = CommonUtils.checkNull(request.getParamValue("isRead"));
			String msgType = CommonUtils.checkNull(request.getParamValue("msgType"));
			String viewNewsType = CommonUtils.checkNull(request.getParamValue("viewNewsType"));
			
			String code = CommonUtils.checkNull(request.getParamValue("code"));
			String pName = CommonUtils.checkNull(request.getParamValue("pName"));
			paramsMap.put("newsId", newsId);
			paramsMap.put("rootOrgId", rootOrgId);
			paramsMap.put("orgId", orgId);
			paramsMap.put("dealerIds", dealerIds);
			paramsMap.put("isRead", isRead);
			paramsMap.put("msgType", msgType);
			paramsMap.put("viewNewsType", viewNewsType);
			paramsMap.put("code", code);
			paramsMap.put("pName", pName);
//			logger.info("查看权限:"+viewNewsType);
			if("1".equals(common)){
				List<Map<String,Object>> mapList = dao.getDealerNewsDetailExport(1, Constant.PAGE_SIZE_MAX, paramsMap).getRecords();
				ApplicationDao application = new ApplicationDao();
				 String[] head={"大区","省份","经销商代码","经销商名称","阅读回复","阅读时间"};
					List<Map<String, Object>> records = mapList;
					List params=new ArrayList();
					if(records!=null &&records.size()>0){
						for (Map<String, Object> map1 : records) {
							String ROOT_ORG_NAME = BaseUtils.checkNull(map1.get("ROOT_ORG_NAME"));
							String ORG_NAME = BaseUtils.checkNull(map1.get("ORG_NAME"));
							String DEALER_CODE = BaseUtils.checkNull(map1.get("DEALER_CODE"));
							String DEALER_NAME = BaseUtils.checkNull(map1.get("DEALER_NAME"));
							String NEWSBACK = BaseUtils.checkNull(map1.get("NEWSBACK"));
							String READ_DATE = BaseUtils.checkNull(map1.get("READ_DATE"));
							String[] detail={ROOT_ORG_NAME,ORG_NAME,DEALER_CODE,DEALER_NAME,NEWSBACK,READ_DATE};
							params.add(detail);
						}
					}
						application.toExcel(act, head, params,null,"新闻阅读明细导出");
			}else {
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getDealerNewsDetailReadPage(curPage, Constant.PAGE_SIZE, paramsMap);
				act.setOutData("ps", ps);
			}
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商新闻明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/****
	 * 经销商端首页新闻
	 */
	public void mainNewsDealer()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		act.setForword(mainNewsDealer);
	}
	
	/******
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void HomePageNewsQuary()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String comman = request.getParamValue("comman");
			String dealerId = logonUser.getDealerId();
			String newsCode = request.getParamValue("newsCode");
			String person = request.getParamValue("person");
			String startDate = request.getParamValue("APPLY_DATE_START");
			String endDate = request.getParamValue("APPLY_DATE_END");
			String newsStatus = request.getParamValue("NEWS_STATUS");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			
			ps = dao.homepageNewsList(logonUser, ActionUtil.getPageSize(request), curPage, dealerId, newsCode, person, startDate, endDate, newsStatus, comman);
			
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("dealer", logonUser.getDealerId());
			act.setOutData("ps", ps);
			ActionUtil.setCustomPageSizeFlag(act, true);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/******
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void HomePageNewsQuary3()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String comman = request.getParamValue("comman");
			String dealerId = logonUser.getDealerId();
			String newsCode = request.getParamValue("newsCode");
			String person = request.getParamValue("person");
			String startDate = request.getParamValue("APPLY_DATE_START");
			String endDate = request.getParamValue("APPLY_DATE_END");
			String newsStatus = request.getParamValue("NEWS_STATUS");
			String newsTitle = request.getParamValue("newsTitle");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.homepageNewsList3(logonUser, Constant.PAGE_SIZE, curPage, dealerId, newsCode, person, startDate, endDate, newsStatus, comman,newsTitle);
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("dealer", logonUser.getDealerId());
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void HomePageNewsQuary2()
	{ // YH.2010.12.21
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String comman = request.getParamValue("comman");
			String dealerId = logonUser.getDealerId();
			String newsCode = request.getParamValue("newsCode");
			String person = request.getParamValue("person");
			String startDate = request.getParamValue("APPLY_DATE_START");
			String endDate = request.getParamValue("APPLY_DATE_END");
			String newsStatus = request.getParamValue("NEWS_STATUS");
			String title = request.getParamValue("title");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.homepageNewsList2(logonUser, Constant.PAGE_SIZE, curPage, dealerId, newsCode, person, startDate, endDate, newsStatus, comman, title,request);
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**********
	 * 经销商端 首页新闻查询
	 */
	public void HomePageNewsQuaryDealer()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String comman = request.getParamValue("comman");
			String dealerId = logonUser.getDealerId();
			String newsCode = request.getParamValue("newsCode");
			String person = request.getParamValue("person");
			String startDate = request.getParamValue("APPLY_DATE_START");
			String endDate = request.getParamValue("APPLY_DATE_END");
			String newsStatus = request.getParamValue("NEWS_STATUS");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.homepageNewsList(logonUser, Constant.PAGE_SIZE, curPage, dealerId, newsCode, person, startDate, endDate, newsStatus, comman);
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/********
	 * 新增首页新闻界面
	 */
	@SuppressWarnings("unchecked")
	public void addNews()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(new Date());// 获取当前时间
			long userId = logonUser.getUserId();// 获取用户ID
			HomePageNewsDao HoDao = new HomePageNewsDao();// 获取用户名和公司名称
			
			TtAsWrNewsPO po = new TtAsWrNewsPO();
			String newsCode = SequenceManager.getSequence2("SYXW");
			
			// 加载大区省份数据到页面
			List<Map<String,Object>> orgList = dao.dutyTypeDQ();
			act.setOutData("orgList", orgList);
			
			// 新闻发布人单位信息 - 新闻发布单位、
			List<Map<String, Object>> list = HoDao.getUserName(String.valueOf(userId));
			act.setOutData("list", list.get(0));
			
			act.setOutData("date", date);
			act.setOutData("newsCode", newsCode);
			act.setOutData("flag", 1);// 新增
			act.setForword(addNews);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻新增失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*******
	 * 点击修改执行操作
	 */
	@SuppressWarnings("unchecked")
	public void modifyNews()
	{
		try
		{
			String newsId = request.getParamValue("newsId");// 获取新闻ID
			List<Map<String, Object>> list = dao.newslist(newsId);// 查询首页新闻信息
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(newsId));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			// 加载大区省份数据到页面
			StringBuffer sql = new StringBuffer();
			sql.append(" select o.*, ");
			sql.append(" (select max(1) from tt_as_wr_news_send s where s.news_id = "+newsId+" and s.org_id = o.org_id) as is_checked");
			sql.append(" from tm_org o where o.duty_type=" + Constant.DUTY_TYPE_LARGEREGION + " and o.status=" + Constant.STATUS_ENABLE + " order by name_sort");
			List<Map<String,Object>> orgList = dao.dutyTypeDQ();
			act.setOutData("orgList", orgList);
			
			// 加载新闻渠道信息
			List<Map<String,Object>> channelList = dao.getNewsChannelListById(newsId);
			String channel = "";
			for(Map<String, Object> map : channelList){
				channel += map.get("CHANNEL_ID").toString() + ",";
			}
			act.setOutData("channel", channel);
			
			// 加载新闻发送范围信息
			List<Map<String,Object>> sendOrgList = dao.getNewsSendOrgListById(newsId);
			act.setOutData("sendOrgList", sendOrgList);
			
			act.setOutData("date", list.get(0).get("NEWS_DATE"));
			act.setOutData("newsCode", list.get(0).get("NEWS_CODE"));
			act.setOutData("list", list.get(0));
			act.setOutData("lists", lists);
			act.setForword(addNews);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻修改失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/*******
	 * 点击修改执行操作
	 */
	@SuppressWarnings("unchecked")
	public void viewReceive()
	{
		try
		{
			String newsId = request.getParamValue("newsId");// 获取新闻ID
			List<Map<String, Object>> list = dao.newslist(newsId);// 查询首页新闻信息
			//List<Map<String, Object>> listtype = dao.dutyTypeDQ();// 获取所有有效的大区
			//FsFileuploadPO detail = new FsFileuploadPO();
			//detail.setYwzj(Long.valueOf(newsId));
			//List<FsFileuploadPO> lists = dao.select(detail);
			
			StringBuffer sbSql = new StringBuffer();
			// 发送经销商列表
			sbSql.append("SELECT D.DEALER_ID,\n");
			sbSql.append("       D.DEALER_CODE,\n");
			sbSql.append("       D.DEALER_NAME,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = D.DEALER_TYPE) DEALER_TYPE\n");
			sbSql.append("  FROM TT_AS_WR_NEWS_ORG A, TM_DEALER D\n");
			sbSql.append(" WHERE A.MSG_TYPE = '1'\n");
			sbSql.append("   AND A.DEALER_ID = D.DEALER_ID"); 
			sbSql.append("   AND A.NEWS_ID = " + newsId); 
			List<Map<String, Object>> recDealerList = dao.pageQuery(sbSql.toString(), null, getFunName());
			act.setOutData("recDealerList", recDealerList);
			
			// 主机厂抄送列表
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT U.USER_ID,\n");
			sbSql.append("       za_concat(TP.POSE_NAME) POSE_NAME,\n");
			sbSql.append("       U.NAME,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TP.POSE_BUS_TYPE) POSE_TYPE\n");
			sbSql.append("  FROM TT_AS_WR_NEWS_ORG A, TC_USER U, TR_USER_POSE TUP, TC_POSE TP\n");
			sbSql.append(" WHERE A.MSG_TYPE = '-1'\n");
			sbSql.append("   AND A.DEALER_ID = U.USER_ID\n");
			sbSql.append("   AND TUP.USER_ID = U.USER_ID\n");
			sbSql.append("   AND TUP.POSE_ID = TP.POSE_ID\n");
			sbSql.append("   AND A.NEWS_ID = " + newsId + "\n"); 
			sbSql.append(" GROUP BY U.USER_ID,U.NAME,TP.POSE_BUS_TYPE\n");
			List<Map<String, Object>> recOemDealerList = dao.pageQuery(sbSql.toString(), null, getFunName());
			act.setOutData("recOemDealerList", recOemDealerList);
			
			// 主机厂发送列表
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT U.USER_ID,\n");
			sbSql.append("       za_concat(TP.POSE_NAME) POSE_NAME,\n");
			sbSql.append("       U.NAME,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TP.POSE_TYPE) POSE_TYPE\n");
			sbSql.append("  FROM TT_AS_WR_NEWS_ORG A, TC_USER U, TR_USER_POSE TUP, TC_POSE TP\n");
			sbSql.append(" WHERE A.MSG_TYPE = '1'\n");
			sbSql.append("   AND A.DEALER_ID = U.USER_ID\n");
			sbSql.append("   AND TUP.USER_ID = U.USER_ID\n");
			sbSql.append("   AND TUP.POSE_ID = TP.POSE_ID\n");
			sbSql.append("   AND A.NEWS_ID = " + newsId + "\n"); 
			sbSql.append(" GROUP BY U.USER_ID,U.NAME,TP.POSE_TYPE\n");
			List<Map<String, Object>> recOemUserList = dao.pageQuery(sbSql.toString(), null, getFunName());
			act.setOutData("recOemUserList", recOemUserList);
			
			// 经销商不需要发送的角色
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT A.ROLE_ID,\n");
			sbSql.append("       A.ROLE_NAME,\n");
			sbSql.append("       A.ROLE_DESC,\n");
			sbSql.append("       (SELECT 1\n");
			sbSql.append("          FROM TT_AS_WR_NEWS_ROLE B\n");
			sbSql.append("         WHERE A.ROLE_ID = B.ROLE_ID\n");
			sbSql.append("           AND B.NEWS_ID = "+newsId+") SELECT_STATUS\n");
			sbSql.append("  FROM TC_ROLE A\n");
			sbSql.append(" WHERE A.ROLE_TYPE = 10021002\n");
			sbSql.append("   AND A.ROLE_STATUS = 10011001\n");
			sbSql.append(" ORDER BY A.ROLE_DESC"); 

			List<Map<String, Object>> roleList = dao.pageQuery(sbSql.toString(), null, getFunName());
			act.setOutData("roleList", roleList);
			
		//	act.setOutData("date", list.get(0).get("NEWS_DATE"));
		//	act.setOutData("newsCode", list.get(0).get("NEWS_CODE"));
			act.setOutData("list", list.get(0));
		//	act.setOutData("listtype", listtype);
		//	act.setOutData("lists", lists);
			act.setForword(viewPs);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻修改失败");
			act.setException(e1);
		}
	}
	
	/*********
	 * 查询页面
	 */
	@SuppressWarnings("unchecked")
	public void viewNews()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String newsId = request.getParamValue("newsId");// 获取新闻ID
			String sql = "select * from tt_as_wr_news where CONTENTS is not null and NEWS_ID= " + newsId;
			List<TtAsWrNewsPO> newList = dao.select(TtAsWrNewsPO.class, sql, null);
			int type = 0;
			if (newList != null && newList.size() > 0)
			{
				type = 1;
			}
			
			List<Map<String, Object>> list = dao.newslist(newsId, type);// 查询首页新闻信息
			String comman = request.getParamValue("comman");
			// 首页新闻查看日志开始
			// TtVsNewsReadLogPO readLogPO = new TtVsNewsReadLogPO();
			// readLogPO.setReadLogId(Utility.getLong(SequenceManager.getSequence("")));//阅读日志ID（公共序列ID）
			// readLogPO.setNewsId(Utility.getLong(newsId));//新闻ID
			// AclUserBean logonUser =
			// (AclUserBean)act.getSession().get(Constant.LOGON_USER);//阅读者ID（用户ID）
			// readLogPO.setReaderId(logonUser.getUserId());//
			// readLogPO.setOperateType(0);//操作类型（0：阅读；1：下载）
			// readLogPO.setReaderDate(new Date());//阅读日期
			// dao.insert(readLogPO);//插入日志记录
			// 首页新闻查看日志结束
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(newsId));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			act.setOutData("dealer", logonUser.getDealerId());
			act.setOutData("comman", comman);
			act.setOutData("list", list.get(0));
			act.setOutData("lists", lists);
			act.setForword(viewNews);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			
			act.setException(e1);
		}
	}
	
	/**
	 * @author wizard_lee
	 * @param
	 * @for:首页新闻下载附件,隐藏文件服务器地址,下载文件名与上传一致
	 */
	@SuppressWarnings("unchecked")
	public void HomePageNewsAttachQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		FsFileuploadPO detail = new FsFileuploadPO();
		ResponseWrapper response = act.getResponse();
		String fjid = request.getParamValue("fjid");// 获取附件ID
		detail.setFjid(Long.valueOf(fjid));
		List<FsFileuploadPO> lists = dao.select(detail);
		try
		{
			for (int i = 0; i < lists.size(); i++)
			{
				Download.downloadTemplate(response, "", lists.get(i).getFileurl().toString(), lists.get(i).getFilename().toString());
			}
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "下载公告附件出错");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存或更新新闻信息
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveOrUpdateNews() throws Exception
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String newsId = request.getParamValue("newsId");// 新闻ID 用于修改页面
			String newsCode = request.getParamValue("newsCode");// 获取新闻编码
			String orgId = request.getParamValue("companyId");// 获取机构ID;
			String orgName = request.getParamValue("companyName");// 获取机构名称
			String name = request.getParamValue("name");// 发表人
			String checkType = request.getParamValue("viewNewsType");// 新闻类型
			String channel[] = request.getParamValues("channel"); // 新闻渠道
			String newsType = request.getParamValue("news_type");// 新闻类别
			String expiryDate = request.getParamValue("expiryDate"); // 失效日期
			String sendOrgList[] = request.getParamValues("sendOrg"); // 新闻发送区域
			
			String title = request.getParamValue("title");// 新闻标题
			String contents = request.getParamValue("contents");// 新闻内容
			String archiveFlag = request.getParamValue("archive_flag"); // 是否存档
			
			if(newsId != null) {
				/* **********************************
				 * 如果是对新闻做修改 
				 * 1、删除发送区域数据 
				 * 2、删除新闻渠道数据
				 ************************************/
				dao.deleteNewsSendOrgs(newsId);
				dao.deleteNewsChannel(newsId);
				//TODO 考虑是否删除附件以节省空间
			}
			
			// 新增的时候
			TtAsWrNewsPO po = new TtAsWrNewsPO();
			
			po.setNewsCode(newsCode);
			po.setOrgId(Long.valueOf(orgId));
			po.setOrgName(orgName);
			po.setVoicePerson(name);
			po.setDutyType(Integer.valueOf(checkType));// 新闻类型 经销商端、OEM端、公用
			po.setNewsType(Integer.valueOf(newsType));
			if (null != expiryDate && !"".equals(expiryDate)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				po.setExpiryDate(sdf.parse(expiryDate));
			}
			po.setNewsTitle(title);
			po.setContents(contents);
			if (archiveFlag != null && !archiveFlag.equals("")) {
				po.setArchiveFlag(Integer.valueOf(archiveFlag));// 是否存档
			} else {
				po.setArchiveFlag(0);// 是否存档
			}
			po.setIsPrivate("1");
			po.setStatus(Constant.NEWS_STATUS_1);// 将状态置为正常
			if (newsId != null && !newsId.equals("")) {
				po.setUpdateDate(new Date());
				po.setUpdateBy(logonUser.getUserId());
				
				// 更新新闻内容
				TtAsWrNewsPO condtionPO = new TtAsWrNewsPO();
				condtionPO.setNewsId(Long.parseLong(newsId));
				
				dao.update(condtionPO, po);
			} else {
				newsId = SequenceManager.getSequence("");
				po.setNewsId(Long.parseLong(newsId));
				po.setCreateDate(new Date());
				po.setNewsDate(new Date());// 发布时间
				po.setCreateBy(logonUser.getUserId());
				
				dao.insert(po);
			}
			
			// 附件保存
			String[] fjids = request.getParamValues("fjid");// 获取文件ID
			FileUploadManager.fileUploadByBusiness(newsId, fjids, logonUser);
			
			// 新闻渠道保存
			dao.saveNewsChannels(newsId, channel);
			
			// 新闻发送范围保存
			dao.saveNewsSendOrgs(newsId, sendOrgList);

//			// 如果修改的话 就先删除权限在查并更新首页新闻
//			if (newsId != null && !newsId.equals(""))
//			{
//				dao.delNewsOrg(newsId);// 删出权限表
//				dao.updateNews(newsId);// 更新首页新闻的查看权限
//				TtAsWrNewsPO oldpo = new TtAsWrNewsPO();
//				oldpo.setNewsId(Long.valueOf(newsId));
//				HomePageNewsDao.updateNewsPO(oldpo, po);// update操作 YH 2011.5.17
//			}
//			
//			// 删除经销上角色权限数据
//			dao.delete("delete from tt_as_wr_news_role where news_id = " + newsId, null);
//			
//			if (checkType.equals(String.valueOf(Constant.VIEW_NEWS_type_1)))
//			{
//				// 如果是选择的是经销商端
//				// 如果发送的经销商上列表不为空
//				if (recDealerId != null)
//				{
//					for (int i = 0; i < recDealerId.length; i++)
//					{
//						// 将经销商对应的新闻ID
//						// 插入到新闻权限表中··········································
//						TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
//						orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
//						orgPo.setCreateDate(new Date());
//						orgPo.setCreateBy(logonUser.getUserId());
//						orgPo.setDealerId(Long.parseLong(recDealerId[i]));
//						orgPo.setNewsId(newsIdS);
//						orgPo.setNewsCode(newsCode);
//						orgPo.setMsg_type("1");
//						
//						HomePageNewsDao.insertNewsOrgPO(orgPo);
//					}
//				}
//				
//				// 如果发送的经销商有角色的筛选
//				if(recDealerRole != null) {
//					for (int i = 0; i < recDealerRole.length; i++)
//					{
//						TtAsWrNewsRolePO expRole = new TtAsWrNewsRolePO();
//						expRole.setNewsId(newsIdS);
//						expRole.setRoleId(Long.parseLong(recDealerRole[i]));
//						
//						dao.insert(expRole);
//					}
//				}
//				
//				// 如果发送的新闻有主机厂的抄送人
//				if(recOemDealer != null) {
//					for (int i = 0; i < recOemDealer.length; i++)
//					{
//						// 将经销商对应的新闻ID
//						// 插入到新闻权限表中··········································
//						TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
//						orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
//						orgPo.setCreateDate(new Date());
//						orgPo.setCreateBy(logonUser.getUserId());
//						orgPo.setDealerId(Long.parseLong(recOemDealer[i]));
//						orgPo.setNewsId(newsIdS);
//						orgPo.setNewsCode(newsCode);
//						orgPo.setMsg_type("-1");
//						
//						HomePageNewsDao.insertNewsOrgPO(orgPo);
//					}
//				}
//				
//			}
//			else if(checkType.equals(String.valueOf(Constant.VIEW_NEWS_type_2)))
//			{
//				// 如果是OEM端
//				for (int i = 0; i < recOemUser.length; i++)
//				{
//					// 将经销商对应的新闻ID
//					// 插入到新闻权限表中··········································
//					TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
//					orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
//					orgPo.setCreateDate(new Date());
//					orgPo.setCreateBy(logonUser.getUserId());
//					orgPo.setDealerId(Long.parseLong(recOemUser[i]));
//					orgPo.setNewsId(newsIdS);
//					orgPo.setNewsCode(newsCode);
//					orgPo.setMsg_type("1");
//					
//					HomePageNewsDao.insertNewsOrgPO(orgPo);
//				}
//			}
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻保存失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/********
	 * 关闭操作
	 */
	public void closeNews()
	{
		String newsId = request.getParamValue("newsId");// 获取要关闭的新闻ID
		dao.closeNews(newsId);
		act.setOutData("flag", true);
	}
	
	/********
	 * 作废操作
	 */
	public void cancelNews()
	{
		String newsId = request.getParamValue("newsId");// 获取要关闭的新闻ID
		dao.cancelNews(newsId);
		act.setOutData("flag", true);
	}
	
	/********
	 * 删除操作 YH 2011.3.23
	 */
	public void DropNews()
	{
		String newsId = request.getParamValue("newsId");// 获取要关闭的新闻ID
		dao.delNewsOrg(newsId);
		dao.delNews(newsId);
		dao.deleteNewsChannel(newsId);
		dao.deleteNewsSendOrgs(newsId);
		act.setOutData("flag", true);
	}
	
	/********
	 * 置顶操作 YH 2011.3.23
	 */
	public void setTopNews()
	{
		String newsId = request.getParamValue("newsId");// 获取要置顶的新闻ID
		dao.setTop(newsId);
		act.setOutData("flag", true);
	}
	
	/********
	 * 取消置顶操作 YH 2011.3.23
	 */
	public void dropTopNews()
	{
		String newsId = request.getParamValue("newsId");// 获取要取消置顶的新闻ID
		dao.dropTop(newsId);
		act.setOutData("flag", true);
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	/**
	 * Iverson 2010-12-08 应用知识库
	 */
	public void ApplicationKnowledgeBase()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setOutData("LONGON_USER_ID", logonUser.getUserId());
		
		// 判断登陆系统
		List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
		Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
		if (code == Constant.chana_jc)
		{
			TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
			mg.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
			List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
			act.setOutData("listModelGroup", listModelGroup);
		}
		
		if (Utility.testString(logonUser.getOemCompanyId()))
		{ // 判断是车厂用户还是经销商 YH 2010.12.3
			act.setForword(knowledgeBaseDealer);
		}
		else
		{
			
			act.setForword(ApplicationKnowledgeBase);
		}
	}
	
	/**
	 * Iverson 2010-12-08 应用知识库
	 * 
	 * @throws Exception
	 */
	public void HomePageNewsQuary1()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String comman = request.getParamValue("comman");
			String newsCode = request.getParamValue("newsCode");
			String person = request.getParamValue("person");
			String startDate = request.getParamValue("APPLY_DATE_START");
			String endDate = request.getParamValue("APPLY_DATE_END");
			String newsStatus = request.getParamValue("NEWS_STATUS");
			String title = request.getParamValue("title");
			String modelGroupId = CommonUtils.checkNull(request.getParamValue("modelGroupSel"));
			String modelPart = CommonUtils.checkNull(request.getParamValue("modelPartSel"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.homepageNewsList1(Constant.PAGE_SIZE, curPage, newsCode, person, startDate, endDate, newsStatus, comman, title, modelGroupId, modelPart);
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson 2010-12-08 新增应用知识库界面
	 */
	public void addApplicationKnowledgeBase()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(new Date());// 获取当前时间
			long userId = logonUser.getUserId();// 获取用户ID
			HomePageNewsDao HoDao = new HomePageNewsDao();// 获取用户名和公司名称
			List<Map<String, Object>> list = HoDao.getUserName(String.valueOf(userId));
			//String newsCode = this.getSequenceZSK("ZSK");
			// List<Map<String, Object>> listtype=dao.dutyTypeDQ();//获取所有有效的大区
			act.setOutData("list", list.get(0));
			// act.setOutData("listtype", listtype);
			act.setOutData("date", date);
			//act.setOutData("newsCode", newsCode);
			act.setOutData("flag", 1);// 新增
			
			// 判断登陆系统
			List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
			if (code == Constant.chana_jc)
			{
				// 查询车型组
				TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
				mg.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
				List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
				act.setOutData("listModelGroup", listModelGroup);
				// 查询车型组
			}
			
			act.setForword(addApplicationKnowledgeBase);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻新增失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function：获得知识库code方法
	 * @param  ：
	 *           _shortName不为空则返回11位的code
	 * LastUpdate：	2014-8-11
	 */
	@SuppressWarnings("unchecked")
	public String getSequenceZSK(String _shortName){
		List shortName2=new ArrayList();
		shortName2.add(_shortName);
		String ret = new String();
		if(_shortName==null||"".equals(_shortName)){
			ret=POFactoryBuilder.getInstance().callFunction("F_GETID",java.sql.Types.VARCHAR,null).toString();
		}else{
			ret=POFactoryBuilder.getInstance().callFunction("F_ZSKNO",java.sql.Types.VARCHAR,shortName2).toString();
		}
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}
	
	/**
	 * Iverson 2010-12-09 保存时执行
	 * 
	 * @throws Exception
	 */
	public void saveApplicationKnowledgeBase() throws Exception
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			//String newsCode = request.getParamValue("newsCode");// 获取新闻编码
			String orgId = request.getParamValue("orgId");// 获取机构ID;
			String orgName = request.getParamValue("orgName");// 获取机构名称
			String name = request.getParamValue("name");// 发表人
			String title = request.getParamValue("title");// 标题
			String contents = request.getParamValue("contents");// 内容
			String newsId = request.getParamValue("newsId");// 新闻ID用于(用于判断是新增还是修改，空为新增)
			
			String modelGroupId = CommonUtils.checkNull(request.getParamValue("modelGroupSel"));
			String modelPart = CommonUtils.checkNull(request.getParamValue("modelPartSel"));
			
			// 新增的时候
			// Long newsIdS=0L;
			TtAsWrKnowledgebasePO po = new TtAsWrKnowledgebasePO();
			po.setContents(contents);
			po.setCode(this.getSequenceZSK("ZSK"));
			po.setVoicePerson(name);
			po.setOrgId(Long.valueOf(orgId));
			po.setOrgName(orgName);
			po.setTitle(title);
			po.setStatus(Constant.NEWS_STATUS_1);// 将状态置为正常
			// if(newsId!=null&&!newsId.equals("")){
			// newsIdS = Long.valueOf(newsId);
			// po.setUpdateDate(new Date());
			// po.setUpdateBy(logonUser.getUserId());
			// }else{
			// newsIdS = Utility.getLong(SequenceManager.getSequence(""));
			// po.setId(newsIdS);
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setCreateDate(new Date());
			po.setPublishedDate(new Date());// 发布时间
			po.setCreateBy(logonUser.getUserId());
			if (!"".equals(modelPart))
			{
				po.setModelPart(Long.parseLong(modelPart));
			}
			if ("".equals(modelGroupId))
			{
				po.setWrgroupId(0l);
			}
			else
			{
				po.setWrgroupId(Long.parseLong(modelGroupId));
			}
			HomePageNewsDao.insert(po);// insert操作
			// }
			
			// 将附件保存
			String ywzj = "";
			if (newsId != null && !newsId.equals(""))
			{
				ywzj = newsId;
			}
			else
			{
				ywzj = String.valueOf(po.getId());
			}
			String[] fjids = request.getParamValues("fjid");// 获取文件ID
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			if (newsId != null && !newsId.equals(""))
			{// 修改的时候
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}
			else
			{
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}
			
			// 判断登陆系统
			List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
			if (code == Constant.chana_jc)
			{
				// 查询车型组
				TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
				mg.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
				List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
				act.setOutData("listModelGroup", listModelGroup);
				// 查询车型组
			}
			
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setForword(ApplicationKnowledgeBase);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻保存失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson 2010-12-09 修改时执行
	 * 
	 * @throws Exception
	 */
	public void updateApplicationKnowledgeBase() throws Exception
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String title = request.getParamValue("title");// 标题
			String contents = request.getParamValue("contents");// 内容
			String newsId = request.getParamValue("newsId");// 新闻ID用于(用于判断是新增还是修改，空为新增)
			String modelPart = CommonUtils.checkNull(request.getParamValue("modelPartSel"));
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("modelGroupSel"));
			// 新增的时候
			TtAsWrKnowledgebasePO po = new TtAsWrKnowledgebasePO();
			po.setId(Long.parseLong(newsId));
			TtAsWrKnowledgebasePO poValue = new TtAsWrKnowledgebasePO();
			poValue.setContents(contents);
			poValue.setTitle(title);
			poValue.setUpdateDate(new Date());
			poValue.setUpdateBy(logonUser.getUserId());
			
			if (!"".equals(modelPart))
			{
				poValue.setModelPart(Long.parseLong(modelPart));
			}
			if ("".equals(wrgroupId))
			{
				poValue.setWrgroupId(0l);
			}
			else
			{
				poValue.setWrgroupId(Long.valueOf(wrgroupId));
			}
			
			HomePageNewsDao.update(po, poValue);// update操作
			
			// 将附件保存
			String ywzj = newsId;
			String[] fjids = request.getParamValues("fjid");// 获取文件ID
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			if (newsId != null && !newsId.equals(""))
			{// 修改的时候
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}
			else
			{
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}
			
			// 判断登陆系统
			List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
			if (code == Constant.chana_jc)
			{
				TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
				mg.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
				List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
				act.setOutData("listModelGroup", listModelGroup);
			}
			
			act.setOutData("LONGON_USER_ID", logonUser.getUserId());
			act.setOutData("success", "success");
			//act.setForword(ApplicationKnowledgeBase);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻保存失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson 2010-12-09 关闭操作
	 */
	public void closeApplicationKnowledgeBase()
	{
		String newsId = request.getParamValue("newsId");// 获取要关闭的新闻ID
		dao.closeNews1(newsId);
		act.setOutData("flag", true);
	}
	
	/**
	 * Iverson 2010-12-09 查询页面
	 */
	@SuppressWarnings("unchecked")
	public void viewNews1()
	{
		try
		{
			String newsId = request.getParamValue("newsId");// 获取新闻ID
			List<Map<String, Object>> list = dao.newslist1(newsId);
			String comman = request.getParamValue("comman");
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(newsId));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			// 判断登陆系统
			List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
			if (code == Constant.chana_jc)
			{
				// 查询车型组
				if (list.get(0).get("WRGROUP_ID") != null)
				{
					String modelName = "";
					TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
					if ("0".equals(list.get(0).get("WRGROUP_ID").toString()))
					{
						modelName = "0";
					}
					else
					{
						mg.setWrgroupId(Long.parseLong(list.get(0).get("WRGROUP_ID").toString()));
						List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
						modelName = listModelGroup.get(0).getWrgroupName();
					}
					
					act.setOutData("listModelName", modelName);
				}
				// 查询车型组
			}
			
			act.setOutData("comman", comman);
			act.setOutData("list", list.get(0));
			act.setOutData("lists", lists);
			
			act.setForword(knowledgeBaseView);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			act.setException(e1);
		}
	}
	
	/**
	 * Iverson 2010-12-09 点击修改执行操作
	 */
	@SuppressWarnings("unchecked")
	public void modifyNews1()
	{
		try
		{
			String newsId = request.getParamValue("newsId");// 获取新闻ID
			List<Map<String, Object>> list = dao.newslist1(newsId);
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(newsId));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			// 判断登陆系统
			List<Map<String, Object>> ListCode = cbmDao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
			if (code == Constant.chana_jc)
			{
				// 查询车型组
				
				TtAsWrModelGroupPO mg = new TtAsWrModelGroupPO();
				mg.setWrgroupType(Constant.WR_MODEL_GROUP_TYPE_01);
				List<TtAsWrModelGroupPO> listModelGroup = factory.select(mg);
				act.setOutData("listModelGroup", listModelGroup);
				
				if (list.get(0).get("WRGROUP_ID") != null)
				{
					String modelId = "";
					TtAsWrModelGroupPO mg2 = new TtAsWrModelGroupPO();
					if ("0".equals(list.get(0).get("WRGROUP_ID").toString()))
					{
						modelId = "0";
					}
					else
					{
						mg2.setWrgroupId(Long.parseLong(list.get(0).get("WRGROUP_ID").toString()));
						List<TtAsWrModelGroupPO> listModelGroupPo = factory.select(mg2);
						modelId = listModelGroupPo.get(0).getWrgroupId().toString();
					}
					
					act.setOutData("listModelId", modelId);
				}
				// 查询车型组
			}
			act.setOutData("list", list.get(0));
			act.setOutData("lists", lists);
			act.setForword(knowledgeBaseModify);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻修改失败");
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getOrgIdByDealerCode(String dealerCodes)
	{
		
		String orgids = null;
		String[] dealerCS = dealerCodes.split(",");
		Set setorgids = new HashSet();
		if (dealerCS.length == 1)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select org_id\n");
			sql.append("  from Tm_Dealer_Org_Relation do, tm_dealer d\n");
			sql.append(" where d.dealer_code = '" + dealerCS[0] + "'\n");
			sql.append("   and do.dealer_id = d.dealer_id");
			Map map = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
			if (null != map && map.size() > 0)
			{
				orgids = map.get("ORG_ID").toString();
			}
			return map.get("ORG_ID").toString();
		}
		else if (dealerCS.length > 1)
		{
			StringBuffer orgIds = new StringBuffer();
			
			for (int i = 0; i < dealerCS.length; i++)
			{
				StringBuffer sql = new StringBuffer();
				sql.append("select org_id\n");
				sql.append("  from Tm_Dealer_Org_Relation do, tm_dealer d\n");
				sql.append(" where d.dealer_code = '" + dealerCS[i] + "'\n");
				sql.append("   and do.dealer_id = d.dealer_id");
				Map map = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
				if (null != map && map.size() > 0)
				{
					setorgids.add(map.get("ORG_ID").toString());
				}
			}
			Iterator it = setorgids.iterator();
			while (it.hasNext())
			{
				orgIds.append((String) it.next() + ",");
			}
			
			orgids = orgIds.substring(0, orgIds.length() - 1);
		}
		return orgids;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getDealerTypeByDealerCode(String dealerCodes)
	{
		TmDealerPO dp = new TmDealerPO();
		String[] dealerCS = dealerCodes.split(",");
		dp.setDealerCode(dealerCS[0]);
		List list = dao.select(dp);
		dp = (TmDealerPO) list.get(0);
		return dp.getDealerType();
	}
	
	/**
	 * 经销商已读状态表示
	 */
	public void saveNewsRead()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String newsId = CommonUtils.checkNull(request.getParamValue("newsId"));
			String newsback = CommonUtils.checkNull(request.getParamValue("newsback"));
			
			dao.recordDealerRead(newsId, newsback, logonUser);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述：新闻发送方，车厂端用户选择
	 *
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public void oemUserChooseInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String poseIds[] = request.getParamValues("poseIds");
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM TC_POSE WHERE POSE_TYPE = 10021001 AND POSE_STATUS = 10011001 AND POSE_BUS_TYPE <> 10781001 ORDER BY POSE_NAME"); 
			List<Map<String,Object>> poseList = dao.pageQuery(sbSql.toString(), null, dao.getFunName());
			act.setOutData("poseList", poseList);
			
			act.setForword("/jsp/claim/basicData/homePageNewsOemChoose.jsp");
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述：新闻发送方，车厂端用户选择页面初始化
	 *
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public void oemUserChooseQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String poseIds[] = request.getParamValues("poseIds");
			String userName = CommonUtils.checkNull(request.getParamValue("userName"));
			StringBuffer sbSql = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			sbSql.append("SELECT T.USER_ID, T.NAME, TP.POSE_TYPE, za_concat(TP.POSE_NAME) POSE_NAME\n");
			sbSql.append("  FROM TC_USER T, TR_USER_POSE TUP, TC_POSE TP\n");
			sbSql.append(" WHERE T.USER_TYPE = 10021001\n");
			sbSql.append("   AND T.USER_ID = TUP.USER_ID \n");
			sbSql.append("   AND TUP.POSE_ID = TP.POSE_ID \n");
			sbSql.append("   AND EXISTS (SELECT 1\n");
			sbSql.append("          FROM TC_POSE A, TR_USER_POSE B\n");
			sbSql.append("         WHERE A.POSE_ID = B.POSE_ID\n");
			sbSql.append("           AND B.USER_ID = T.USER_ID\n");
			sbSql.append("  AND (T.NAME like '%"+userName+"%' or T.ACNT like '%"+userName+"%')");
			if(poseIds != null) {
				sbSql.append(Utility.getConSqlByParamForEqual(poseIds, params, "TP", "POSE_ID"));
			} else {
				sbSql.append("		AND 1 = 2");
			}
			sbSql.append("           AND A.POSE_BUS_TYPE <> 10781001) GROUP BY T.USER_ID, T.NAME, TP.POSE_TYPE,TP.POSE_NAME ORDER BY TP.POSE_NAME\n"); 
			
			PageResult<Map<String, Object>> ps = dao.pageQuery(sbSql.toString(), params, getFunName(), Constant.PAGE_SIZE_MAX, 1);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述：新闻发送方，经销商端用户选择
	 *
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public void dlrUserChooseInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String dealerType = request.getParamValue("dealerType"); // 消息发送类型，销售、售后或公用消息
			StringBuffer sbSql = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			// 加载大区省份数据到页面
			sbSql.append("SELECT O.ORG_NAME, O.ORG_ID\n");
			sbSql.append("  FROM TM_ORG O\n");
			sbSql.append(" WHERE O.ORG_TYPE = 10191001\n");
			sbSql.append("   AND O.STATUS = 10011001\n");
			sbSql.append("   AND O.ORG_LEVEL = 2\n");
			sbSql.append("   AND o.STATUS = 10011001\n");
			sbSql.append(" ORDER BY o.NAME_SORT"); 
			List<Map<String,Object>> orgList = dao.pageQuery(sbSql.toString(), null, dao.getFunName());
			act.setOutData("orgList", orgList);
			
			// 加载省份数据到页面
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT A1.ORG_ID PARENT_ORG_ID, O.ORG_NAME, O.ORG_ID\n");
			sbSql.append("  FROM TM_ORG O, TM_ORG A1\n");
			sbSql.append(" WHERE O.ORG_TYPE = 10191001\n");
			sbSql.append("   AND O.STATUS = 10011001\n");
			sbSql.append("   AND O.ORG_LEVEL = 3\n");
			sbSql.append("   AND O.PARENT_ORG_ID = A1.ORG_ID\n");
			sbSql.append("   AND A1.STATUS = 10011001\n");
			sbSql.append(" ORDER BY A1.NAME_SORT"); 
			List<Map<String,Object>> proviceList = dao.pageQuery(sbSql.toString(), null, dao.getFunName());
			act.setOutData("proviceList", proviceList);
			
			// 加载经销商数据到前台页面
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT *\n");
			sbSql.append("  FROM TM_DEALER D\n");
			sbSql.append(" WHERE D.STATUS = 10011001\n");
			sbSql.append("   AND D.SERVICE_STATUS = 13691004\n");
			if(dealerType.equals(Constant.MSG_TYPE_1)) {
				sbSql.append("   AND D.DEALER_TYPE = ?"); 
				params.add(Constant.DEALER_TYPE_DVS);
			} else if(dealerType.equals(Constant.MSG_TYPE_2)) {
				sbSql.append("   AND D.DEALER_TYPE = ?"); 
				params.add(Constant.DEALER_TYPE_DWR);
			}
			List<Map<String,Object>> dealerList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
			act.setOutData("dealerList", dealerList);

			act.setOutData("dealerType", dealerType);
			act.setForword("/jsp/claim/basicData/homePageNewsDlrChoose.jsp");
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述：新闻发送方，车厂端用户选择页面初始化
	 *
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public void dlrUserChooseQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String dealerType = request.getParamValue("dealerType"); // 消息发送类型，销售、售后或公用消息
			String orgIds[] = request.getParamValues("org");
			StringBuffer sbSql = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			sbSql.append("SELECT *\n");
			sbSql.append("  FROM TM_DEALER D\n");
			sbSql.append(" WHERE D.STATUS = 10011001\n");
			sbSql.append("   AND D.SERVICE_STATUS = 13691002\n");
			if(dealerType.equals(Constant.MSG_TYPE_1)) {
				sbSql.append("   AND D.DEALER_TYPE = ?"); 
				params.add(Constant.DEALER_TYPE_DVS);
			} else if(dealerType.equals(Constant.MSG_TYPE_2)) {
				sbSql.append("   AND D.DEALER_TYPE = ?"); 
				params.add(Constant.DEALER_TYPE_DWR);
			}
			if(orgIds != null) {
				sbSql.append("	AND EXISTS(");
				sbSql.append("		SELECT 1\n");
				sbSql.append("        FROM TM_DEALER_ORG_RELATION A, TM_ORG B\n");
				sbSql.append(" 		 WHERE A.ORG_ID = B.ORG_ID\n");
				sbSql.append("   	   AND A.DEALER_ID = D.DEALER_ID");
				sbSql.append(Utility.getConSqlByParamForEqual(orgIds, params, "B", "ORG_ID"));
				sbSql.append("	)");
			} else {
				sbSql.append("	AND 1 = 2\n");
			}
			
			PageResult<List<Map<String, Object>>> ps = dao.pageQuery(sbSql.toString(), params, dao.getFunName(), Constant.PAGE_SIZE_MAX, 1);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "首页新闻查询失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 进入导入新闻功能
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void toImportNews(){
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(importNewsUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转导入新闻失败");
			logger.error(e);
			act.setException(e1);
		}
			
	}
	
	/**
	 * 接收上传的新闻内容文件
	 * 
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("unchecked")
	public void importNews(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String hint = "导入新闻";
		try {
			String regex = ",";
			FileObject file = request.getParamObject("inputFile");
			ByteArrayInputStream is = new ByteArrayInputStream(file.getContent());
			Workbook wb = ExcelUtil.getWorkbookByIS(file.getFileName(), is);
			Sheet st0 = wb.getSheetAt(0);

			List<Map<String, Object>> roleList = getUnsendRoles();
			Map<String, Object> roleCaches = new HashMap<String, Object>();
			for (Map<String, Object> role : roleList) {
				roleCaches.put(role.get("ROLE_NAME").toString(), role.get("ROLE_ID"));	
			}

			List<Map<String, Object>> orgList = getReceiveOrgs();
			Map<String, Object> orgCaches = new HashMap<String, Object>();
			for (Map<String, Object> org : orgList) {
				orgCaches.put(org.get("DEALER_CODE").toString(), org.get("DEALER_ID"));	
			}
			List<Map<String, Object>> userList = getReceiveUsers();
			Map<String, Object> userCaches = new HashMap<String, Object>();
			for (Map<String, Object> user : userList) {
				userCaches.put(user.get("ACNT").toString(), user.get("USER_ID"));	
			}
			int k=1;
			while(true){
				int ix = 0;
				String newsCode = null;// 获取新闻编码
				String orgId = ExcelUtil.getStringValue(st0.getRow(ix).getCell(1));// 获取机构ID;
				String orgName = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(2));// 获取机构名称
				String name = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(2));// 发表人
				ix++;
				String title = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k));// 标题
				if(null==title){
					break;
				}
				Date expirydate = ExcelUtil.getDateValue(st0.getRow(ix++).getCell(k));
				String archiveFlag = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"是否存档"); // 是否存档
				String contents = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"内容");// 内容
				String checkType = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"查看类型");// 获取选中的查看类型
				String msg_type = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"消息类型"); // 获取所选消息类型
				String newsType = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"新闻类别");// 新闻类别
				String unsends = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"不发送角色");// 不发送角色
				String receiveOrgs = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"接收方");// 接收方
				String receiveusers = ExcelUtil.getStringValue(st0.getRow(ix++).getCell(k),"新闻类别");// 新闻类别
				
				k++;
				String expiryDate = null==expirydate?null:new SimpleDateFormat("yyyy-MM-dd").format(expirydate); // 失效日期
				String newsId = null;// 新闻ID 用于修改页面
				if("是".equals(archiveFlag)){
					archiveFlag = "1";
				} else{
					archiveFlag = null;
				}
				
				String[] recDealerId = null;// 发送经销商
				if (null != receiveOrgs) {
					recDealerId = receiveOrgs.split(regex);
					for (int i = 0; i < recDealerId.length; i++) {
						if (orgCaches.containsKey(recDealerId[i])) {
							recDealerId[i] = orgCaches.get(recDealerId[i]).toString();
						} else {
							recDealerId[i] = null;
						}
					}
				}
	
				String[] recOemDealer = null;// 抄送方
				if (null != receiveusers) {
					recOemDealer = receiveusers.split(regex);
					for (int i = 0; i < recOemDealer.length; i++) {
						if (userCaches.containsKey(recOemDealer[i])) {
							recOemDealer[i] = userCaches.get(recOemDealer[i]).toString();
						} else {
							recOemDealer[i] = null;
						}
					}
				}

				String[] recDealerRole = null;// 不发送角色
				if(null!=unsends){
					recDealerRole = unsends.split(regex);
					for(int i = 0;i<recDealerRole.length;i++){
						if(roleCaches.containsKey(recDealerRole[i])){
							recDealerRole[i] = roleCaches.get(recDealerRole[i]).toString();
						} else {
							recDealerRole[i] = null;
						}
					}
				}
				
				String[] recOemUser = null;// 主机厂用户
				if(null!=receiveOrgs){
					recOemUser = receiveOrgs.split(regex);
					for(int i = 0;i<recOemUser.length;i++){
						if(userCaches.containsKey(recOemUser[i])){
							recOemUser[i] = userCaches.get(recOemUser[i]).toString();
						} else {
							recOemUser[i] = null;
						}
					}
				}
				
				//if(CommonUtils.checkNull(newsCode))//Constant.MSG_TYPE_2
				if ("".equals(CommonUtils.checkNull(newsId))){
					if ("".equals(CommonUtils.checkNull(newsCode))) {
						if(Constant.MSG_TYPE_2.equals(msg_type)){
							newsCode = SequenceManager.getSequence3("FW");
						}else {
							newsCode = SequenceManager.getSequence2("SYXW");
						}
					}
				}
				// 新增的时候
				Long newsIdS = 0L;
				TtAsWrNewsPO po = new TtAsWrNewsPO();
				
				po.setContents(contents);
				po.setNewsCode(newsCode);
				po.setVoicePerson(name);
				po.setOrgId(Long.valueOf(orgId));
				po.setOrgName(orgName);
				po.setNewsTitle(title);
				po.setDutyType(Integer.valueOf(checkType));// 查看类型
				po.setMsgType(msg_type);
				po.setIsPrivate("1");
				po.setNewsType(Integer.valueOf(newsType));
				po.setStatus(Constant.NEWS_STATUS_1);// 将状态置为正常
				
				if (null != expiryDate && !"".equals(expiryDate))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					po.setExpiryDate(sdf.parse(expiryDate));
				}
				if (archiveFlag != null && !archiveFlag.equals(""))
				{
					po.setArchiveFlag(Integer.valueOf(archiveFlag));// 是否存档
				}
				else
				{
					po.setArchiveFlag(0);// 是否存档
				}
				
	
				newsIdS = Utility.getLong(SequenceManager.getSequence(""));
				po.setNewsId(newsIdS);
				po.setCreateDate(new Date());
				po.setNewsDate(new Date());// 发布时间
				po.setCreateBy(logonUser.getUserId());
	
				HomePageNewsDao.insertNewsPO(po);// insert操作
	
				// 删除经销上角色权限数据
				dao.delete("delete from tt_as_wr_news_role where news_id = " + newsId, null);
				
				if (checkType.equals(String.valueOf(Constant.VIEW_NEWS_type_1)))
				{
					// 如果是选择的是经销商端
					// 如果发送的经销商上列表不为空
					if (recDealerId != null)
					{
						for (int i = 0; i < recDealerId.length; i++)
						{
							if(null==recDealerId[i]){
								continue;
							}
							// 将经销商对应的新闻ID
							// 插入到新闻权限表中··········································
							TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
							orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
							orgPo.setCreateDate(new Date());
							orgPo.setCreateBy(logonUser.getUserId());
							orgPo.setDealerId(Long.parseLong(recDealerId[i]));
							orgPo.setNewsId(newsIdS);
							orgPo.setNewsCode(newsCode);
							orgPo.setMsg_type("1");
							
							HomePageNewsDao.insertNewsOrgPO(orgPo);
						}
					}
					
					// 如果发送的经销商有角色的筛选
					if(recDealerRole != null) {
						for (int i = 0; i < recDealerRole.length; i++)
						{
							if(null==recDealerRole[i]){
								continue;
							}
							TtAsWrNewsRolePO expRole = new TtAsWrNewsRolePO();
							expRole.setNewsId(newsIdS);
							expRole.setRoleId(Long.parseLong(recDealerRole[i]));
							
							dao.insert(expRole);
						}
					}
					
					// 如果发送的新闻有主机厂的抄送人
					if(recOemDealer != null) {
						for (int i = 0; i < recOemDealer.length; i++)
						{
							if(null==recOemDealer[i]){
								continue;
							}
							// 将经销商对应的新闻ID
							// 插入到新闻权限表中··········································
							TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
							orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
							orgPo.setCreateDate(new Date());
							orgPo.setCreateBy(logonUser.getUserId());
							orgPo.setDealerId(Long.parseLong(recOemDealer[i]));
							orgPo.setNewsId(newsIdS);
							orgPo.setNewsCode(newsCode);
							orgPo.setMsg_type("-1");
							
							HomePageNewsDao.insertNewsOrgPO(orgPo);
						}
					}
					
				}
				else if(checkType.equals(String.valueOf(Constant.VIEW_NEWS_type_2)))
				{
					// 如果是OEM端
					for (int i = 0; i < recOemUser.length; i++)
					{
						if(null==recOemUser[i]){
							continue;
						}
						// 将经销商对应的新闻ID
						// 插入到新闻权限表中··········································
						TtAsWrNewsOrgPO orgPo = new TtAsWrNewsOrgPO();
						orgPo.setId(Utility.getLong(SequenceManager.getSequence("")));
						orgPo.setCreateDate(new Date());
						orgPo.setCreateBy(logonUser.getUserId());
						orgPo.setDealerId(Long.parseLong(recOemUser[i]));
						orgPo.setNewsId(newsIdS);
						orgPo.setNewsCode(newsCode);
						orgPo.setMsg_type("1");
						
						HomePageNewsDao.insertNewsOrgPO(orgPo);
					}
				}
			}
			act.setOutData("hint", hint+"完成<br/");
			// 跳转到列表页面
			toImportNews();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			act.setOutData("hint", hint+"失败!"+e.getMessage());
			toImportNews();
		}
	}
	
	/**
	 * 导出新闻模版
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void exportNewsModel(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			long userId = logonUser.getUserId();// 获取用户ID
			HomePageNewsDao HoDao = new HomePageNewsDao();// 获取用户名和公司名称
			List<Map<String, Object>> list = HoDao.getUserName(String.valueOf(userId));
			Map<String, Object> userinfo = list.get(0);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(new Date());
			
			
			String filename = "新闻导入模版.xlsx";
			ResponseWrapper response = ActionContext.getContext().getResponse();
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
			OutputStream out = response.getOutputStream();
			
			
			Workbook wb = new XSSFWorkbook();
			// 主要内容
			Sheet st1 = wb.createSheet("新闻内容");
			ExcelUtil.appendRow(st1, new String[]{"发表单位:",userinfo.get("ORG_ID").toString(),userinfo.get("ORG_NAME").toString()});
			ExcelUtil.appendRow(st1, new String[]{"发表人:",String.valueOf(userId),userinfo.get("VOICE_PERSON").toString()});
			ExcelUtil.appendRow(st1, new String[]{"发表日期:",date});
			ExcelUtil.appendRow(st1, new String[]{"新闻标题:"});
			ExcelUtil.appendRow(st1, new String[]{"失效日期(yyyy/MM/dd):"});
			ExcelUtil.appendRow(st1, new String[]{"是否存档(是/否):"});
			ExcelUtil.appendRow(st1, new String[]{"新闻内容:"});
			ExcelUtil.appendRow(st1, new String[]{"查看类型:"});
			ExcelUtil.appendRow(st1, new String[]{"消息类型:"});
			ExcelUtil.appendRow(st1, new String[]{"新闻类别:"});
			ExcelUtil.appendRow(st1, new String[]{"不发送角色:"});
			ExcelUtil.appendRow(st1, new String[]{"接收方:"});
			ExcelUtil.appendRow(st1, new String[]{"抄送方:"});
			
			
			// 不发送角色
			Sheet st2 = wb.createSheet("不发送角色");
			List<Map<String, Object>> roleList = getUnsendRoles();
			ExcelUtil.appendRow(st2,new String[]{"角色名","角色描述"});
			for (Map<String, Object> role : roleList) {
				ExcelUtil.appendRow(st2, new String[] {
//						role.get("ROLE_ID").toString(),
						role.get("ROLE_NAME").toString(),
						role.get("ROLE_DESC").toString() });
			}

			// 接收方
			Sheet st3 = wb.createSheet("接收方");
			List<Map<String, Object>> orgList = getReceiveOrgs();
			ExcelUtil.appendRow(st3,new String[]{"上级组织名称","组织名称","经销商编码","经销商名称"});
			for (Map<String, Object> org : orgList) {
				ExcelUtil.appendRow(st3, new String[] {
						org.get("PARENT_ORG_NAME").toString(),
						org.get("ORG_NAME").toString(),
						org.get("DEALER_CODE").toString(),
						org.get("DEALER_NAME").toString() });
			}
			
			
			// 抄送方
			Sheet st4 = wb.createSheet("抄送方");
			List<Map<String, Object>> userList = getReceiveUsers();
			ExcelUtil.appendRow(st4,new String[]{"部门","登录名","用户名"});
			for (Map<String, Object> user : userList) {
				ExcelUtil.appendRow(st4, new String[] {
						user.get("ORG_NAME").toString(),
						user.get("ACNT").toString(),
						user.get("USER_NAME").toString()});
			}
			
			Sheet st5 = wb.createSheet("数据字典");
			ExcelUtil.appendRow(st5, new String[] {"查看类型id","查看类型名称","消息类型id","消息类型名称"
					,"新闻类别id","新闻类别名称"});
			ExcelUtil.appendRow(st5,new String[] { 
					String.valueOf(Constant.VIEW_NEWS_type_1),"经销商端"
					, String.valueOf(Constant.MSG_TYPE_3),"经销商公用消息"
					, String.valueOf(Constant.NEWS_type_1),"备件管理"});
			ExcelUtil.appendRow(st5,new String[] { 
					String.valueOf(Constant.VIEW_NEWS_type_2),"OEM端"
					, String.valueOf(Constant.MSG_TYPE_2),"经销商售后消息"
					, String.valueOf(Constant.NEWS_type_2),"服务政策"});
			ExcelUtil.appendRow(st5,new String[] { 
					String.valueOf(Constant.VIEW_NEWS_type_3),"公用"
					, String.valueOf(Constant.MSG_TYPE_1),"经销商销售消息"
					, String.valueOf(Constant.NEWS_type_3),"系统应用"});
			ExcelUtil.appendRow(st5,
					new String[] { "",""
					, "",""
					, String.valueOf(Constant.NEWS_type_4),"技术通告"});
			ExcelUtil.appendRow(st5,
					new String[] { "",""
					, "",""
					, String.valueOf(Constant.NEWS_type_5),"汽车维修"});
			ExcelUtil.appendRow(st5,
					new String[] { "",""
					, "",""
					, String.valueOf(Constant.NEWS_type_6),"维修方法"});
			ExcelUtil.appendRow(st5,
					new String[] { "",""
					, "",""
					, String.valueOf(Constant.NEWS_type_7),"销售公告"});
			
			wb.write(out);
			out.flush();
			IOUtils.closeQuietly(out);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导入新闻失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	
	/**
	 * 导入新闻附件
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void importNewsAttachment(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String hint = "导入新闻附件";
		try {
			FileObject file = request.getParamObject("inputFile");
			if(file.getLength() > 1024*1024*50)
			{
				throw new Exception("上传文件限制大小为50M，服务器资源有限，请节约使用！");
			}
			ByteArrayInputStream is = new ByteArrayInputStream(file.getContent());
			String fileName = file.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
			if (fileName == null
					|| fileName.length() < 4
					|| (!fileName.toLowerCase().endsWith(".zip")
//					&& !fileName.toLowerCase().endsWith(".xlsx")
				)) {
				throw new Exception("文件类型错误，请导入zip文件");
			}
			
			String path = File.separator
					+  request.getContextPath();
			String dirPath = "attachment" + File.separator + "newsAttachment"
					+ File.separator;
			String dir = path + File.separator + dirPath;
			Map<String, String> files = getFiles(is, dir,fileName);
			/*FileUpLoadDAO fuDao = new FileUpLoadDAO();
			for (Entry<String, String> f : files.entrySet()) {
				String fName = f.getKey();
				String fjid = f.getValue();
				String newsName = fName;
				int idx = Math.min(fName.indexOf("."),fName.indexOf("_"));
				if(idx<0){
					idx = Math.max(fName.indexOf("."),fName.indexOf("_"));
				}
				if(idx>0){
					newsName = fName.substring(0, idx);
				}
				if(null==newsName||newsName.trim().length()==0){
					continue;
				}
				TtAsWrNewsPO condition = new TtAsWrNewsPO();
				condition.setNewsTitle(newsName);
				List<TtAsWrNewsPO> newsList = dao.select(condition);
				for (TtAsWrNewsPO news : newsList) {
					fuDao.addenableFile(news.getNewsId().toString(), fjid, logonUser);
				}
			}*/
			act.setOutData("hint", hint+"完成<br/");
			// 跳转到列表页面
			toImportNews();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			act.setOutData("hint", hint+"失败!"+e.getMessage());
			toImportNews();
		}
	}
	
	/**
	 * 导入新闻附件和新闻的关系文件
	 * 
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("unchecked")
	public void importRelations(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String hint = "导入新闻附件关系";
		try {
			FileObject file = request.getParamObject("inputFile");
			ByteArrayInputStream is = new ByteArrayInputStream(file.getContent());
			Workbook wb = ExcelUtil.getWorkbookByIS(file.getFileName(), is);
			if (null == wb) {
				throw new RuntimeException("导入文件为空!");
			}
			Sheet st0 = wb.getSheetAt(0);
			int ix = 0;
			int rowNum = st0.getPhysicalNumberOfRows();
			Map<String, List<TtAsWrNewsPO>> relations = new HashMap<String, List<TtAsWrNewsPO>>();
			for (int i = 1; i < rowNum; i++) {
				ix = 0;
				Row row = st0.getRow(i);
				if (null == row.getCell(0)) {
					break;
				}
				String newsCode = ExcelUtil.getStringValue(row.getCell(ix++));
				String fileName = ExcelUtil.getStringValue(row.getCell(ix));
				List<TtAsWrNewsPO> clist = null;
				if (relations.containsKey(fileName)) {
					clist = relations.get(fileName);
				} else {
					clist = new ArrayList<TtAsWrNewsPO>();
					relations.put(fileName, clist);
				}
				TtAsWrNewsPO news = new TtAsWrNewsPO();
				news.setNewsCode(newsCode);
				List<TtAsWrNewsPO> pos = dao.select(news);
				if(!CommonUtils.isNullList(pos)){
					clist.addAll(pos);
				}
				
			}
			
			if(!CommonUtils.isNullMap(relations)){
				for(Entry<String, List<TtAsWrNewsPO>> relation:relations.entrySet()){
					List<TtAsWrNewsPO> newsCodeList = relation.getValue();
					String fileName = relation.getKey();
					updateAttachmentRelations(fileName,newsCodeList,logonUser);
				}
			}
			
			
			act.setOutData("hint", hint + "完成!<br/>");
			// 跳转到列表页面
			toImportNews();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			act.setOutData("hint", hint + "失败!<br/>" + e.getMessage());
			toImportNews();
		}
	}

	/**
	 * 更新新闻与附件的关系
	 * @param fileName
	 * @param newsCodeList
	 * @author chenyub@yonyou.com
	 * @throws SQLException 
	 * @throws FileStoreException 
	 */
	@SuppressWarnings("unchecked")
	private void updateAttachmentRelations(String fileName,
			List<TtAsWrNewsPO> newsList,AclUserBean logonUser) throws SQLException, FileStoreException {
		StringBuffer buff = new StringBuffer();
		buff.append(" select * from FS_FILEUPLOAD t ");
		buff.append(" where t.filename = '").append(fileName).append("' ");
		buff.append(" and t.YWZJ is null ");
		PageResult<Map<String, Object>> records = dao.pageQuery(
				buff.toString(), new LinkedList(), getFunName(),
				Constant.PAGE_SIZE_MAX, 1);
		List<Map<String, Object>> rds = records.getRecords();
		List<FsFileuploadPO> fileList = new ArrayList<FsFileuploadPO>();
		if(!CommonUtils.isNullList(rds)){
			for (Map<String, Object> rd : rds) {
				fileList.add(BeanUtils.map2Bean(rd, new FsFileuploadPO()));
			}
		}
		FileUpLoadDAO fuDao = new FileUpLoadDAO();
		NewsFileStoreImple store = NewsFileStoreImple.getInstance();
		List<String> invalidFile = new LinkedList<String>();
		String validFile = null;
		if(!CommonUtils.isNullList(fileList)){
			for (FsFileuploadPO file : fileList) {
				for (TtAsWrNewsPO news : newsList) {
					String fileid = file.getFileid();
					String fileUrl = null;
					try{
						fileUrl = store.getDomainURL(fileid, "news");
						if(null==validFile){
							validFile = fileid;
						}
					} catch (FileStoreException e){
						if(!invalidFile.contains(fileid)){
							invalidFile.add(fileid);
						}
						continue;
					}
					FsFileuploadPO po = new FsFileuploadPO();
					po.setFileid(fileid);
					po.setFileurl(fileUrl);
					po.setFilename(fileName);
					fuDao.addDisableFile(po, logonUser);
					fuDao.addenableFile(news.getNewsId().toString(), po.getFjid().toString(), logonUser);
				}
			}
			if(null!=validFile&&!CommonUtils.isNullList(invalidFile)){
				StringBuffer usql = new StringBuffer(); 
				usql.append(" update fs_fileupload t ");
				usql.append(" set t.fileid='").append(validFile).append("' ");
				usql.append(" where 1=1 ");
				DaoFactory.getsql(usql, "t.fileid", DaoFactory.getSqlByarrIn(invalidFile), 6);
				dao.update(usql.toString(),new ArrayList());
			}
		}
	}

	/**
	 * 导出新闻模版
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void exportNewsRelations(){
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String filename = "新闻关系模版.xlsx";
			ResponseWrapper response = ActionContext.getContext().getResponse();
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
			OutputStream out = response.getOutputStream();
			
			
			Workbook wb = new XSSFWorkbook();
			// 主要内容
			Sheet st1 = wb.createSheet("新闻附件关系");
			ExcelUtil.appendRow(st1, new String[]{"新闻编码","附件名称"});
			wb.write(out);
			out.flush();
			IOUtils.closeQuietly(out);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导入新闻失败");
			logger.error(e);
			act.setException(e1);
		}
	}
	

	/**
	 * 获得zip包中的文件
	 * 
	 * @param importFile
	 * @param path
	 *            存放路径
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getFiles(InputStream is, String path,String fileName)
			throws Exception {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		String dir = new String(path);
		File dirf = new File(dir);
		if (!dirf.exists()){
			dirf.mkdirs();
		}
		String classPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		//HomePageNews.class.getClassLoader().getResource("/").getPath();
		
		String rootPath = "";
		String encoding = "UTF-8";
		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
			encoding = "GBK";
		} else if("\\".equals(File.separator)){
			rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
			encoding = "GBK";
		}
		System.out.println("===========================================");
		System.out.println(rootPath);
		System.out.println(File.separator);
		System.out.println("===========================================");
		path = rootPath;
		String tName = System.currentTimeMillis() + fileName;
		String tPath = path + File.separator + tName;
		File tFile = new File(path + File.separator + tName);
		OutputStream tos = new FileOutputStream(tFile);
		int b;
		while ((b = is.read()) != -1){
			tos.write(b);
		}
		tos.flush();
		IOUtils.closeQuietly(tos);
		IOUtils.closeQuietly(is);
		ZipFile zipFile = new ZipFile(tPath,encoding);
		Enumeration e = zipFile.getEntries();
		ZipEntry z = null;
		Map<String, String> files = new HashMap<String, String>();
		NewsFileStoreImple store = NewsFileStoreImple.getInstance();
		FileUpLoadDAO fuDao = new FileUpLoadDAO();
		InputStream in = null;
		while (e.hasMoreElements()) {
			z = (ZipEntry) e.nextElement();
			in = zipFile.getInputStream(z);
			if (z.isDirectory()) {
				continue;
			} else {
				String fName = z.getName();
				String fileid = store.write(null, null, fName, in, "news");
				String fileUrl = store.getDomainURL(fileid, "news");
				FsFileuploadPO po = new FsFileuploadPO();
				po.setFileid(fileid);
				po.setFileurl(fileUrl);
				po.setFilename(fName);
				fuDao.addDisableFile(po, logonUser);
				files.put(fName, po.getFjid().toString());
				System.out.println("===========================================");
				System.out.println(fName+":"+po.getFjid().toString());
				System.out.println(File.separator);
				System.out.println("===========================================");
			}
		}
		FileUtils.deleteQuietly(tFile);
		return files;
	}
	
	/**
	 * 查询不发送角色
	 * @return
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getUnsendRoles(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROLE_ID||'' as \"ROLE_ID\",ROLE_NAME,ROLE_DESC,");
		sql.append(" -1 AS SELECT_STATUS FROM TC_ROLE ");
		sql.append(" WHERE ROLE_TYPE = 10021002 AND ROLE_STATUS = 10011001 ");
		sql.append(" AND ROLE_ID IN (2014031894226707,2014031894226704,4000010725,");
		sql.append(" 4000010726,4000010727,4000010728,4000010729,4000010730,");
		sql.append(" 2014031894226703,2014031894226706,2014031894226705) order by role_desc");
		List<Map<String, Object>> roleList = dao.pageQuery(sql.toString(), null, getFunName());
		return roleList;
	}

	/**
	 * 查询接收方
	 * @return
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getReceiveOrgs() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A1.ORG_ID||'' PARENT_ORG_ID,a1.org_name as PARENT_ORG_NAME ");
		sql.append(" , O.ORG_ID||'' as ORG_ID, O.ORG_NAME ");
		sql.append(" ,d.dealer_id||'' as DEALER_ID,d.DEALER_NAME ");
		sql.append(" ,d.DEALER_CODE ");
		sql.append(" FROM TM_ORG O ");
		sql.append(" left join tm_org a1 on a1.org_id=o.parent_org_id ");
		sql.append(" left join tm_dealer_org_relation dr on dr.org_id=o.org_id ");
		sql.append(" left join tm_dealer d on d.dealer_id=dr.dealer_id ");
		sql.append(" WHERE O.ORG_TYPE = 10191001 ");
		sql.append(" AND O.STATUS = 10011001 ");
		sql.append(" AND O.ORG_LEVEL = 3 ");
		sql.append(" AND A1.STATUS = 10011001 ");
		sql.append(" and d.status= 10011001 ");
		sql.append(" and d.service_status=13691002 ");
		sql.append(" ORDER BY a1.org_name,d.dealer_id ");
		List<Map<String, Object>> orgList = dao.pageQuery(sql.toString(), null, getFunName());
		return orgList;
	}
	
	/**
	 * 查询抄送方
	 * @return
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getReceiveUsers(){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM (SELECT TU.USER_ID||'' as USER_ID,TU.NAME as USER_NAME, ");
		sql.append(" TU.ACNT,AA.POSE_TYPE,LISTAGG(AA.POSE_NAME, ',')  ");
		sql.append(" within group (order by AA.POSE_NAME) POSE_NAME, LISTAGG(TOC.ORG_NAME ,',')  ");
		sql.append(" within group (order by TOC.ORG_NAME ) ORG_NAME,LISTAGG(AA.POSE_CODE, ',')  ");
		sql.append(" within group (order by AA.POSE_CODE) POSE_CODE FROM TM_ORG_CUS_USER_RELATION T2  ");
		sql.append(" LEFT JOIN TC_USER TU ON TU.USER_ID = T2.USER_ID  ");
		sql.append(" LEFT JOIN TM_ORG_CUSTOM TOC ON TOC.ORG_ID = T2.ORG_ID  ");
		sql.append(" LEFT JOIN (SELECT TUP.USER_ID,MAX(TP.POSE_TYPE) POSE_TYPE,LISTAGG(TP.POSE_NAME, ',')  ");
		sql.append(" within group (order by TP.POSE_NAME) POSE_NAME,LISTAGG(TP.POSE_CODE, ',')  ");
		sql.append(" within group (order by TP.POSE_CODE) POSE_CODE FROM TR_USER_POSE TUP  ");
		sql.append(" LEFT JOIN TC_POSE TP ON TP.POSE_ID = TUP.POSE_ID  ");
		sql.append(" WHERE TP.POSE_TYPE = 10021001  ");
		sql.append(" GROUP BY TUP.USER_ID,TP.POSE_ID) AA ON AA.USER_ID = TU.USER_ID  ");
		sql.append(" WHERE TU.USER_STATUS = 10011001 and TU.user_type=10021001  ");
		sql.append(" GROUP BY TU.USER_ID,TU.NAME,AA.POSE_TYPE,TU.ACNT ) BB  ");
		sql.append(" ORDER BY BB.POSE_NAME ");
		List<Map<String, Object>> userList = dao.pageQuery(sql.toString(), null, getFunName());
		return userList;
	}
}
