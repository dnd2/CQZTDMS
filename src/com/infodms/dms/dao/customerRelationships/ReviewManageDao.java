package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtCrmExamDetailPO;
import com.infodms.dms.po.TtCrmExamPO;
import com.infodms.dms.po.TtCrmQueAnswerPO;
import com.infodms.dms.po.TtCrmQueDetailPO;
import com.infodms.dms.po.TtCrmQuestionnairePO;
import com.infodms.dms.po.TtCrmReturnVisitPO;
import com.infodms.dms.po.TtCrmReturnVisitRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class ReviewManageDao extends BaseDao{

	private static final ReviewManageDao dao = new ReviewManageDao();
	private static final Object[][] String = null;
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ReviewManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询生效的所有问卷名称 
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getQuestionairList()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select  QR_ID, QR_NAME from TT_CRM_QUESTIONNAIRE  where 1=1 and qr_status_del = "+Constant.STATUS_ENABLE+" and QR_STATUS="+Constant.QR_STATUS_1 );
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 根据名称查询
	 * @param questName
	 * @return
	 */
	public List<Map<String, Object>> queryQuestionInfo(String questName)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select  QR_ID, QR_NAME from TT_CRM_QUESTIONNAIRE  where 1=1 and qr_status_del = "+Constant.STATUS_ENABLE+" and QR_STATUS="+Constant.QR_STATUS_1+" and QR_NAME = ?" );
		List<Object> params = new ArrayList<Object>();
		params.add(questName);
		List<Map<String, Object>> list= pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 获得当前状态下拉选项
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStatusList()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select  CODE_ID, CODE_DESC from tc_code where status= "+Constant.STATUS_ENABLE);
		sql.append("	and num <5 and type="+Constant.RV_STATUS);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取回访人
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-16
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReviewerList()
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT S.SE_USER_ID,S.SE_NAME FROM TT_CRM_SEATS S    ");
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> getReviewerList(int pageSize, int curPage)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT S.* FROM TT_CRM_SEATS S    ");
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 我的回访查询
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param keyName
	 * @param      : @param knowType
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> reviewManageQuery(AclUserBean logonUser,int pageSize, int curPage,String RV_CUS_NAME,String RV_TYPE,String RV_STATUS, String QR_ID) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select v.rv_id,v.rv_cus_id,v.rv_cus_name,(select code_desc from tc_code where code_id=rv_type) rv_type,to_char(v.rv_date,'yyyy-mm-dd') rv_date,(select code_desc from tc_code where code_id=rv_status) rv_status from TT_CRM_RETURN_VISIT v,tc_user tu  where 1=1 ");
		sql.append("  and  v.rv_status in ("+Constant.RV_STATUS_1+","+Constant.RV_STATUS_2+","+Constant.RV_STATUS_4+")");
		sql.append("  and v.rv_ass_user_id=tu.user_id ");
		sql.append("  and tu.user_id="+logonUser.getUserId());

		if(!("".equals(RV_CUS_NAME))){
			sql.append(" and RV_CUS_NAME like '%"+RV_CUS_NAME+"%'");
		}
		if(!("".equals(RV_TYPE))){
			sql.append(" and RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(RV_STATUS))){
			sql.append(" and RV_STATUS = "+RV_STATUS+"");
		}
		if(!("".equals(QR_ID))){
			sql.append(" and QR_ID  ="+QR_ID+"");
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  待处理回访查询
	 * @param      : @param logonUser
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param RV_CUS_NAME
	 * @param      : @param RV_TYPE
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> pedingReviewQuery(AclUserBean logonUser,int pageSize, int curPage,String RV_CUS_NAME,String RV_TYPE) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(
				"select v.rv_id rv_id,/*回访ID*/\n" +
				"       v.rv_cus_id,/*客户ID*/\n" + 
				"       v.rv_cus_name,/*客户名称*/\n" + 
				"       tc.code_desc rv_type,/*回访类型*/\n" + 
				"       to_char(v.rv_date, 'yyyy-mm-dd') rv_date,/*生成时间*/\n" + 
				"       con.rd_content/*备注*/\n" + 
				"  from TT_CRM_RETURN_VISIT v/*客户回访表*/\n" + 
				"  JOIN tc_user tu ON v.rv_ass_user_id = tu.user_id/*用户表*/\n" + 
				"  LEFT JOIN tc_code tc on tc.code_id = v.rv_type/*TC_CODE表*/\n" + 
				"  LEFT JOIN (SELECT rv_id, RD_CONTENT\n" + 
				"               FROM (SELECT t.rv_id,\n" + 
				"                            DENSE_RANK() OVER(partition by t.rv_id ORDER BY t.rd_date DESC) RES,\n" + 
				"                            T.RD_CONTENT\n" + 
				"                       FROM TT_CRM_RETURN_VISIT_RECORD T)\n" + 
				"              WHERE res = 1) con on v.rv_id = con.rv_id /* 最后一次回访备注*/\n" + 
				" where 1 = 1"+
				"	 and v.rv_ass_user_id=tu.user_id " +
				"	and tu.user_id="+logonUser.getUserId()+
				"  ");
		if(!("".equals(RV_CUS_NAME))){
			sql.append(" and RV_CUS_NAME like '%"+RV_CUS_NAME+"%'");
		}
		if(!("".equals(RV_TYPE))){
			sql.append(" and RV_TYPE = "+RV_TYPE+"");
		}
		sql.append(" and rv_status="+Constant.RV_STATUS_2);
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 基本信息查询 
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> basicInofQuery(String QR_ID, Long userId) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select vs.RV_id,    /*回访ID*/\n" +
				"       CR.CTM_NAME, /*客户名称*/\n" + 
				"       vs.RV_PHONE, /*手机*/\n" + 
				"       vs.RV_TYPE, /*回访类型*/\n" + 
				"       to_char(vs.RV_DATE, 'yyyy-mm-dd') RV_DATE, /*回访时间*/\n" + 
				"       vs.RV_RESULT, /*回访结果*/\n" + 
				"       vs.rv_times, /*回访次数*/\n" + 
				"       CR.CARD_NUM, /*证件号码*/\n" + 
				"       Q.QR_ID, /*问卷ID*/\n" + 
				"       Q.QR_NAME, /*问卷名称*/\n" + 
				"       Q.QR_DESCRIPTION, /*问卷描述*/\n" + 
				"       Q.QR_GUIDE, /*问卷引导语*/\n" + 
				"       Q.QR_THANKS, /*结束感谢语*/\n" + 
				"       cr.CTM_TYPE, /*客户类型*/\n" + 
				"       tc.code_desc SALES_ADDRESS, /*购买用途*/\n" + 
				"       prov.region_name PROVINCE, /*省份*/\n" + 
				"       city.region_name CITY, /*城市*/\n" + 
				"       CR.POST_CODE, /*邮编*/\n" + 
				"       CR.ADDRESS, /*详细地址*/\n" + 
				"       (select code_desc from tc_code where code_id = cr.guest_stars) GUEST_STARS, /*客户等级*/\n" + 
				"       CR.COMPANY_PHONE, /*公司电话*/\n" + 
				"       CR.OTHER_PHONE, /*住宅电话*/\n" + 
				"       CR.MAIN_PHONE, /*手机*/\n" + 
				"       p.SERIES_NAME, /*车系*/\n" + 
				"       p.MODEL_NAME, /*车型*/\n" + 
				"       ce.COLOR, /*颜色*/\n" + 
				"       ta.area_name YIELDLY, /*产地*/\n" + 
				"       dis.region_name  DISTRICT, /*地区*/\n" + 
				"       p.PACKAGE_NAME, /*配置*/\n" + 
				"       ce.ENGINE_NO, /*发动机号*/\n" + 
				"       ce.VIN vin, /*VIN*/\n" + 
				"       to_char(ce.PRODUCT_DATE, 'yyyy-mm-dd') PRODUCT_DATE, /*详细地址*/\n" + 
				"       to_char(ce.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE, /*购买日期*/\n" + 
				"       das.PRICE, /*价格*/\n" + 
				"       l.material_code, /*装配代码*/\n" + 
				"       dl.dealer_name, /*经销商名称*/\n" + 
				" (SELECT t.acnt FROM tc_user t WHERE t.user_id = "+userId+") SEATNO /*登录账号*/ " +
				"  from TT_CRM_RETURN_VISIT      vs /*回访表*/\n" + 
				"       JOIN  TT_CUSTOMER        cr ON VS.RV_CUS_ID=CR.CTM_ID /*客户表*/\n" + 
//				"       LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.ctm_id = cr.ctm_id /*实销表*/\n" + 
//				"       LEFT JOIN tm_vehicle               ce  on das.vehicle_id= ce.vehicle_id /*车辆表*/\n" + 
				"		LEFT JOIN tm_vehicle                 ce  on ce.vin = vs.vin /*车辆表*/\n"+
				"		LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.vehicle_id= ce.vehicle_id /*实销表*/"+
				"       LEFT JOIN tm_vhcl_material         l on  ce.material_id=l.material_id /*物料表*/\n" + 
				"       LEFT JOIN tm_vhcl_material_group_r r on  l.material_id=r.material_id /*物料组物料关系表*/\n" + 
				"       LEFT JOIN vw_material_group        p on  r.group_id=p.PACKAGE_ID /*物料组表*/\n" + 
				"       LEFT JOIN TM_DEALER                dl on dl.DEALER_ID = ce.dealer_id /*经销商表*/\n" + 
				"       LEFT JOIN tm_region                dis on dl.province_id=dis.region_code /*地区表*/\n" + 
				"       LEFT JOIN tc_code                  tc on  das.car_charactor=tc.code_id/*tc_code表*/\n" + 
				"       LEFT JOIN tm_business_area         ta on ce.yieldly=ta.area_id/*产地表*/\n" + 
				"       LEFT JOIN TT_CRM_QUESTIONNAIRE     Q  on vs.qr_id=q.qr_id /*问卷表*/\n" + 
				"       LEFT JOIN tm_region                prov on cr.province=prov.region_code /*省份表*/\n" + 
				"       LEFT JOIN tm_region                city on cr.city=city.region_code /*城市表*/\n" + 
				"       where 1=1\n" + 
				"             and  vs.rv_id="+QR_ID);
		result = (LinkedList<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName());
		
		for (Map<String, Object> qf : result) {
			String str = getTitleLanguage((String)qf.get("CTM_NAME"), (String)qf.get("SEATNO")) + (String)qf.get("QR_GUIDE");
			qf.put("QR_GUIDE", str);
		}
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询 客户及车辆详细信息 
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> detailInofQuery(String QR_ID) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select" +
				" CR.CTM_NAME, CR.CARD_NUM," +
				" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = CTM_TYPE) CTM_TYPE," +
				" das.SALES_ADDRESS," +
				" (select re.region_name from tm_region  re where re.region_code=CR.PROVINCE) PROVINCE," +
				" (select re.region_name from tm_region  re where re.region_code=CR.CITY) CITY," +
				" CR.POST_CODE," +
				" CR.ADDRESS," +
				" CR.CTM_NAME," +
				" CR.GUEST_STARS," +
				" CR.COMPANY_PHONE," +
				" CR.OTHER_PHONE," +
				" CR.MAIN_PHONE," +
				" p.SERIES_NAME," +
				" p.MODEL_NAME," +
				" ce.COLOR," +
				" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = ce.YIELDLY) YIELDLY," +
				" p.PACKAGE_NAME," +
				" ce.ENGINE_NO," +
				" ce.VIN," +
				" to_char(ce.PRODUCT_DATE,'yyyy-mm-dd') PRODUCT_DATE," +
				" to_char(ce.PURCHASED_DATE,'yyyy-mm-dd') PURCHASED_DATE," +
				" das.PRICE," +
				" l.material_code," +
				" dl.dealer_name" +
				"" +
				"  from vw_material_group        p," +
				"       tm_vhcl_material         l," +
				"       tm_vhcl_material_group_r r," +
				"       tm_vehicle               ce," +
				"       TT_CUSTOMER              cr," +
				"       TT_DEALER_ACTUAL_SALES   das," +
				"       TM_DEALER dl" +
				" where 1 = 1" +
				"   and r.material_id = l.material_id" +
				"   and r.group_id = p.PACKAGE_ID" +
				"   and ce.material_id = l.material_id" +
				"   and cr.ctm_id = das.ctm_id" +
				"   and das.vehicle_id = ce.vehicle_id" +
				"   and dl.DEALER_ID = ce.dealer_id" +
				"   and cr.ctm_id = (select vsit.rv_cus_id from  TT_CRM_RETURN_VISIT vsit where vsit.rv_id="+QR_ID+")");
		result = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询问卷及答案
	 * @param      : @param RV_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> questionInofQuery(String RV_ID) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		Map<String,Object> num =isExist(RV_ID);
		StringBuffer sql= new StringBuffer();
		if(null!=num&&(Integer.parseInt(num.get("NUM").toString())==0)){
		sql.append("select Q.QR_ID , q.qd_no," +
				" q.qd_que_type," +
				" q.qd_question,q.qd_txt_type, q.qd_choice, q.ed_txt_width, q.ed_txt_hight, ('null') qd_que_answer,('null') qd_que_reason" +
//				" (SELECT t.acnt FROM tc_user t WHERE t.user_id = "+userId+") SEATNO " +
				" from TT_CRM_QUE_DETAIL q" +
				" where q.qr_id = (select QR_ID from TT_CRM_RETURN_VISIT where RV_ID="+RV_ID+") and q.qd_status = "+Constant.STATUS_ENABLE+" order by q.qd_no asc");
		}else{
		sql.append(
				"select q.qd_no,\n" +
				"       q.qd_que_type,\n" + 
				"       q.qd_question,\n" + 
				"       q.qd_txt_type,\n" + 
				"       q.qd_choice,\n" +
				"       q.ed_txt_width,\n" + 
				"       q.ed_txt_hight,\n" + 
				"       (case\n" + 
				"         when a.qd_que_answer is null then\n" + 
				"          'null'\n" + 
				"         else\n" + 
				"          a.qd_que_answer\n" + 
				"       end) qd_que_answer,\n" +
				"       (case\n" + 
				"         when a.qd_que_reason is null then\n" + 
				"          'null'\n" + 
				"         else\n" + 
				"          a.qd_que_reason\n" + 
				"       end) qd_que_reason\n" + 
//				" (SELECT t.acnt FROM tc_user t WHERE t.user_id = "+userId+") SEATNO " +
				//"  from TT_CRM_QUE_DETAIL q /*问题表*/\n" + 
				"	from (select * from  TT_CRM_QUE_DETAIL where qr_id = (select distinct qr_id from tt_crm_que_answer where rv_id="+RV_ID+") and qd_status = "+Constant.STATUS_ENABLE+") q/*问题表*/\n"+
				"	left join (select * from  TT_CRM_QUE_ANSWER where rv_id = "+RV_ID+") a /*答案表*/\n"+
				//"  left join TT_CRM_QUE_ANSWER a /*答案表*/\n" + 
				"    on a.qr_id = q.qr_id\n" + 
				"   and q.qd_no = a.qd_no\n" + 
				//"   and a.rv_id = " +RV_ID+ 
				"  order by qd_no asc");
		}
		result = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
		
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询回访记录
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> reviewInofQuery(String QR_ID) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select rownum, t.* from (" +
				"	SELECT " +
				" (select code_desc from tc_code where code_id=RD_IS_ACCEPT) RD_IS_ACCEPT," +
				" (select code_desc from tc_code where code_id=RD_MODE) RD_MODE,RD_USER," +
				"  RD_DATE,RD_CONTENT, rd_id " +
				" FROM TT_CRM_RETURN_VISIT_RECORD WHERE RV_ID = "+QR_ID+" order by  RD_DATE asc ) t");
		result = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
		return result;		
	}	
	/**
	 * 
	 * @Title      : 
	 * @Description:  查询问卷答案 
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> answerInofQuery(String QR_ID) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		/*StringBuffer sql= new StringBuffer();
		sql.append(""+QR_ID);
		result = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());*/
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询 问卷引导及结束语
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	@SuppressWarnings("unchecked")
	public   LinkedList<Map<String, Object>> questionairInofQuery(String QR_ID) throws Exception {
		LinkedList<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT Q.QR_ID ,Q.QR_NAME,Q.QR_DESCRIPTION,Q.QR_GUIDE,Q.QR_THANKS FROM TT_CRM_QUESTIONNAIRE Q  WHERE Q.QR_ID="+QR_ID);
		result = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
		return result;		
	}	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  保存问卷答案
	 * @param      : @param answers
	 * @param      : @param rvId
	 * @param      : @param logonUser
	 * @param      : @param qrId      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void saveAnswer(String answers,String rvId,AclUserBean logonUser,String qrId )
	{
		
		//Map<String,Object> qn=questionNum(qrId); //获得问卷中问题的个数。
		//首先删除原来问卷答案
		StringBuilder delsql = new StringBuilder();
		delsql.append(" delete from TT_CRM_QUE_ANSWER where RV_ID ="+rvId);
		delsql.append("	and QR_ID ="+qrId);
		factory.delete(delsql.toString(), null);
		//保存新答案
		String regex=":q1[0-9]{1,2}+:";
		Pattern p = Pattern.compile(regex);
		Matcher mr=p.matcher(answers);
		StringBuilder tempstr= new StringBuilder();
		String var="";
		int num =0;
		tempstr.append("");
		while(mr.find())
		{
			var=mr.group();
			if(tempstr.indexOf(var)==-1)
			{
				tempstr.append(var);
				num++;
			}
		}
		
		
		if((answers.indexOf(":|")==-1))
		{
		String [] answer =answers.split("\\|");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		int length=0;
		for(String a:answer)
		{
			TtCrmQueAnswerPO ao= new TtCrmQueAnswerPO();
			String[] ta = a.split(":");
			StringBuilder sql=new StringBuilder();
			if(!ta[0].equals("checkbox"))
			{
				ao.setRdAnsId(Long.parseLong(SequenceManager.getSequence("")));
				ao.setRvId(Long.parseLong(rvId));
				ao.setQdQueAnswer(ta[2]);
				if(ta.length > 3){
					ao.setQdQueReason(ta[3]);
				}
				ao.setQrId(Long.parseLong(qrId));
				ao.setQdNo(Integer.parseInt(ta[1].substring(2,ta[1].length())));
				ao.setRdDate(new Date ());
				ao.setRdUserId(logonUser.getUserId());
				ao.setCreateBy(logonUser.getUserId().toString());
				ao.setCreateDate(new Date());
				factory.insert(ao);
			}else{
				length++;
			}
			
		}
		String [][] ans= new String[length][2] ;
		for(int j =0;j<length;j++)
		{
			ans[j][0]="";
			ans[j][1]="";
		}
		int kk=0;
		for(String a:answer)
		{
			String[] ta = a.split(":");
			if(ta[0].equals("checkbox")){
				ans[kk][0]=ta[1];
				ans[kk][1]=ta[2];
				kk++;
			}
			
		}
		String [][] joinanswer = new String[kk][2];
		int aa=0;
		int b=0;
		for(int m=0;m<ans.length;m++)
		{
			
			if(!ans[m][0].equals("")){
				String temp=ans[m][0];//id
				String a=ans[m][1];//answer
				for(int n=m+1;n<ans.length;n++)
				{
					if(ans[n][0].equals(temp))
					{
						a+="|"+ans[n][1];
						ans[n][0]="";
					}
				}
			  joinanswer[b][1]=a;
			  joinanswer[b++][0]=temp;
			}
			
		}
		for (int d=0;d<joinanswer.length;d++)
		{
			if(null!=joinanswer[d][1]){
				TtCrmQueAnswerPO po= new TtCrmQueAnswerPO();
				po.setRdAnsId(Long.parseLong(SequenceManager.getSequence("")));
				po.setRvId(Long.parseLong(rvId));
				po.setQdQueAnswer(joinanswer[d][1]);
				po.setQrId(Long.parseLong(qrId));
				po.setQdNo(Integer.parseInt(joinanswer[d][0].substring(2,joinanswer[d][0].length())));
				po.setRdDate(new Date ());
				po.setRdUserId(logonUser.getUserId());
				factory.insert(po);
			}
		}
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  查询问卷是否已经被回答
	 * @param      : @param RV_ID
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public Map<String, Object> isExist(String RV_ID){
		Map<String, Object>  map = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select count(*) num from TT_CRM_QUE_ANSWER t where 1 =1");
		sql.append(" and t.rv_id ="+RV_ID+"");
			map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	public Map<String, Object>  questionNum(String QR_ID){
		Map<String, Object>  map = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select count(*) num from TT_CRM_QUE_DETAIL t where 1 =1");
		sql.append(" and t.QR_id ="+QR_ID+"");
			map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  获得系统中汽车种类
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public static List<Map<String,Object>> getAllTypeList(){
		StringBuffer sql = new StringBuffer("");
		sql.append("select distinct series_name  from vw_material_group \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  客户回访查询
	 * @param      : @param logonUser
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param RV_CUS_NAME
	 * @param      : @param RV_TYPE
	 * @param      : @param RV_DATES
	 * @param      : @param RV_DATEE
	 * @param      : @param vehicleType
	 * @param      : @param RD_IS_ACCEPT
	 * @param      : @param TELEPHONE
	 * @param      : @param RV_STATUS
	 * @param      : @param RV_ASS_USER
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> reviewSearchQuery(AclUserBean logonUser,int pageSize, int curPage,String RV_CUS_NAME,String RV_TYPE,String RV_DATES,String RV_DATEE,String vehicleType,String RD_IS_ACCEPT,String TELEPHONE,String RV_STATUS,String RV_ASS_USER) throws Exception {
		PageResult<Map<String, Object>> result = null;
		//.ACC_STATUS;
		StringBuffer sql= new StringBuffer();
		List<Map<String,Object>> right= getRight(logonUser);//SE_IS_MANAMGER
//		if(right == null || right.size() == 0) return null;
//		
		String isManager=Constant.se_is_manamger_1.toString();
		if(isManager.equals(right.get(0).get("SE_IS_MANAMGER").toString())) {
				sql.append(	"SELECT VS.RV_ID,/*客户回访ID*/\n" +
						"       VS.RV_CUS_NAME,/*客户名称*/\n" + 
						"       TC.CODE_DESC RV_TYPE,/*回访类型*/\n" + 
						"       VS.RV_RESULT,/*回访结果*/\n" + 
						"       T.CODE_DESC RV_STATUS,/*当前状态*/\n" + 
						"       VS.RV_PHONE,/*回访电话*/\n" + 
						"       TO_CHAR(VS.RV_DATE, 'yyyy-MM-dd') RV_DATE,/*生成时间*/\n" + 
						"       VS.RV_ASS_USER,/*回访人*/\n" +
						"		'YES' AS MANAGER /* 是否座席管理员*/\n" + 
						"  FROM TT_CRM_RETURN_VISIT VS/**/\n" + 
						"  JOIN TT_CUSTOMER C ON VS.RV_CUS_ID = C.CTM_ID /* 客户表*/\n" + 
//						"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON C.CTM_ID = DAS.CTM_ID /*实销表*/\n" + 
//						"  LEFT JOIN TM_VEHICLE V ON DAS.VEHICLE_ID = V.VEHICLE_ID/*车辆表*/\n" + 
						"  LEFT JOIN tm_vehicle v  on v.vin = vs.vin /*车辆表*/\n"+
						"  LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.vehicle_id= v.vehicle_id /*实销表*/\n"+
						"  LEFT JOIN TM_VHCL_MATERIAL L ON V.MATERIAL_ID = L.MATERIAL_ID/*物料表*/\n" + 
						"  LEFT JOIN TM_VHCL_MATERIAL_GROUP_R R ON L.MATERIAL_ID = R.MATERIAL_ID/*物料-物料组关系表*/\n" + 
						"  LEFT JOIN VW_MATERIAL_GROUP P ON P.PACKAGE_ID = R.GROUP_ID/*物料组表*/\n" + 
						"  JOIN TC_CODE TC ON TC.CODE_ID = VS.RV_TYPE/* 回访类型*/\n" + 
						"  JOIN TC_CODE T ON T.CODE_ID = VS.RV_STATUS/*当前状态*/\n" + 
						" WHERE 1 = 1 ");
		}else{
			sql.append(	
					"SELECT VS.RV_ID,/*客户回访ID*/\n" +
					"       VS.RV_CUS_NAME,/*客户名称*/\n" + 
					"       TC.CODE_DESC RV_TYPE,/*回访类型*/\n" + 
					"       VS.RV_RESULT,/*回访结果*/\n" + 
					"       T.CODE_DESC RV_STATUS,/*当前状态*/\n" + 
					"       VS.RV_PHONE,/*回访电话*/\n" + 
					"       TO_CHAR(VS.RV_DATE, 'yyyy-MM-dd') RV_DATE,/*生成时间*/\n" + 
					"       VS.RV_ASS_USER,/*回访人*/\n" + 
					"		'NO' AS MANAGER /* 是否座席管理员*/\n" + 
					"  FROM TT_CRM_RETURN_VISIT VS/**/\n" + 
					"  JOIN TT_CUSTOMER C ON VS.RV_CUS_ID = C.CTM_ID /* 客户表*/\n" + 
//					"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON C.CTM_ID = DAS.CTM_ID /*实销表*/\n" + 
//					"  LEFT JOIN TM_VEHICLE V ON DAS.VEHICLE_ID = V.VEHICLE_ID/*车辆表*/\n" + 
					"  LEFT JOIN TM_VEHICLE  v  on v.vin = vs.vin /*车辆表*/\n"+
					"  LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.vehicle_id= v.vehicle_id /*实销表*/\n"+
					"  LEFT JOIN TM_VHCL_MATERIAL L ON V.MATERIAL_ID = L.MATERIAL_ID/*物料表*/\n" + 
					"  LEFT JOIN TM_VHCL_MATERIAL_GROUP_R R ON L.MATERIAL_ID = R.MATERIAL_ID/*物料-物料组关系表*/\n" + 
					"  LEFT JOIN VW_MATERIAL_GROUP P ON P.PACKAGE_ID = R.GROUP_ID/*物料组表*/\n" + 
					"  JOIN TC_CODE TC ON TC.CODE_ID = VS.RV_TYPE/* 回访类型*/\n" + 
					"  JOIN TC_CODE T ON T.CODE_ID = VS.RV_STATUS/*当前状态*/\n" +
					"  JOIN tc_user tu  ON VS.rv_ass_user_id=tu.user_id and tu.user_id=" +logonUser.getUserId()+
					"  WHERE 1 = 1 ");
		}
		

		if(!("".equals(RV_CUS_NAME))){
			sql.append(" and RV_CUS_NAME like '%"+RV_CUS_NAME+"%'");
		}
		if(!("".equals(RV_TYPE))){
			sql.append(" and RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(vehicleType))){
			sql.append(" and p.SERIES_NAME ='"+vehicleType+"'");
		}
		if(!("".equals(RD_IS_ACCEPT))){
			sql.append(" and RV_RESULT = "+RD_IS_ACCEPT+"");
		}
		if(!("".equals(TELEPHONE))){
			sql.append(" and RV_PHONE = "+TELEPHONE+"");
		}
		if(!("".equals(RV_STATUS))){
			sql.append(" and RV_STATUS = "+RV_STATUS+"");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(RV_DATES))&&(!("".equals(RV_DATEE))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+RV_DATES+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+RV_DATEE+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(RV_DATES)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+RV_DATES+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(RV_DATEE)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+RV_DATEE+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(" order by RV_DATE desc");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> allocateReviewQuery(int pageSize, int curPage, String RV_TYPE, String checkSDate, String checkEDate, String RV_ASS_USER, String questionair) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(	
					"SELECT V.RV_ID, /*客户回访ID*/\n" +
					"       V.RV_CUS_NAME, /*客户名称*/\n" + 
					"       T.CODE_DESC RV_TYPE, /*回访类型*/\n" + 
					"       to_char(v.RV_DATE,'yyyy-mm-dd') RV_DATE,/*生成时间 */\n" + 
					"       TT.CODE_DESC RV_STATUS, /*当前状态*/\n" + 
					"       V.RV_ASS_USER /*回访人*/\n" + 
					"  FROM TT_CRM_RETURN_VISIT V, /*客户回访表*/\n" + 
					"       TT_CRM_QUESTIONNAIRE Q, /*问卷表*/\n" +
					"		tc_code              T,  /*tc_code 表*/\n" +
					"	 	tc_code              TT  /*tc_code 表*/\n" + 
					"  WHERE 1 = 1\n" +
					"	AND V.RV_TYPE=T.CODE_ID(+)" +
					"	AND V.RV_STATUS=TT.CODE_ID(+)" + 
					"   AND V.QR_ID = Q.QR_ID(+)" +
					"	AND V.RV_STATUS="+Constant.RV_STATUS_6);

		if(!("".equals(RV_TYPE))){
			sql.append(" and V.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and V.RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(questionair))){
			sql.append(" and Q.QR_NAME  ="+questionair+"");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(" order by rv_date desc");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  问卷设置查询
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param RV_TYPE
	 * @param      : @param checkSDate
	 * @param      : @param checkEDate
	 * @param      : @param RV_ASS_USER
	 * @param      : @param QR_ID
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-16
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> setQuestionairQuery(int pageSize, int curPage, String RV_TYPE, String checkSDate, String checkEDate, String RV_ASS_USER, String QR_ID) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(	
					"SELECT V.RV_ID, /*客户回访ID*/\n" +
					"       V.RV_CUS_NAME, /*客户名称*/\n" + 
					"       T.CODE_DESC RV_TYPE, /*回访类型*/\n" + 
					"       to_char(v.RV_DATE,'yyyy-mm-dd') RV_DATE,/*生成时间 */\n" + 
					"       TT.CODE_DESC RV_STATUS, /*当前状态*/\n" + 
					"       V.RV_ASS_USER /*回访人*/\n" + 
					"  FROM TT_CRM_RETURN_VISIT V, /*客户回访表*/\n" + 
					"       TT_CRM_QUESTIONNAIRE Q, /*问卷表*/\n" +
					"		tc_code              T,  /*tc_code 表*/\n" +
					"	 	tc_code              TT  /*tc_code 表*/\n" + 
					"  WHERE 1 = 1\n" +
					"	AND V.RV_TYPE=T.CODE_ID(+)" +
					"	AND V.RV_STATUS=TT.CODE_ID(+)" + 
					"   AND V.QR_ID = Q.QR_ID(+)" +
					"	AND V.RV_STATUS="+Constant.RV_STATUS_5);

		if(!("".equals(RV_TYPE))){
			sql.append(" and V.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and V.RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(" order by RV_DATE desc");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  为客户设置问卷
	 * @param      : @param code
	 * @param      : @param QR_ID
	 * @param      : @param NumberFrom
	 * @param      : @param NumberTo      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public void  setQuestionair(AclUserBean logonUse,String[] code,String QR_ID,String SELECT_RV_TYPE ,String NumberFrom, String NumberTo,String RV_TYPE,String checkSDate,String checkEDate,String RV_ASS_USER)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" UPDATE TT_CRM_RETURN_VISIT V SET  V.QR_ID = "+QR_ID);
		if(!("".equals(RV_TYPE))){
			sql.append(" ,V.RV_TYPE="+SELECT_RV_TYPE);
		}
		sql.append(" ,V.RV_STATUS ="+Constant.RV_STATUS_6);//尚未回访状态
		sql.append(" ,V.UPDATE_BY= "+logonUse.getUserId());
		sql.append(" ,V.UPDATE_DATE= sysdate ");
		sql.append(" WHERE V.RV_ID IN(");
		if((!("".equals(NumberFrom)))&&(!("".equals(NumberTo)))){
		sql.append(
				"SELECT RV_ID\n" +
				"  FROM (SELECT A.*, ROWNUM R\n" + 
				"          FROM (SELECT V.RV_ID  /*客户回访ID*/\n" + 
				"                  FROM TT_CRM_RETURN_VISIT  V /*客户回访表*/\n" + 
				"                 WHERE 1 = 1 \n" + 
				"					AND V.RV_STATUS="+Constant.RV_STATUS_5 );//等待设置问卷状态
		sql.append("");
		if(!("".equals(RV_TYPE))){
			sql.append(" and V.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and V.RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(" ) A) \n WHERE R >= \n" + NumberFrom+"  AND R <= "+NumberTo);
		//sql.append(" )" );
		}else if(code.length>0){
			for(String c:code)
			{
				sql.append(c);
				sql.append(",");
			}
			sql.setCharAt(sql.lastIndexOf(","), ' ');
		}
		sql.append(")");
	
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  设置 回访人
	 * @param      : @param code
	 * @param      : @param QR_ID
	 * @param      : @param NumberFrom
	 * @param      : @param NumberTo      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-14
	 */
	public void setReviewPerson(AclUserBean logonUse,String[] code,String resvalue, String NumberFrom, String NumberTo,String RV_TYPE,String checkSDate,String checkEDate)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("");
		if((!("".equals(NumberFrom)))&&(!("".equals(NumberTo))))
		{
			//如果回访人数大于客户数
			List<Map<String, Object>> idLIst = getReviewId(NumberFrom,NumberTo,RV_TYPE,checkSDate,checkEDate);
			String[] reviewers=resvalue.split(",");
			if(reviewers.length>=idLIst.size())
			{
				for(int i=0;i<reviewers.length;i++)
				{
					if(i<idLIst.size())
					{
						sql.append(" UPDATE TT_CRM_RETURN_VISIT V SET  V.RV_ASS_USER_ID= "+reviewers[i]);
						sql.append(" ,V.RV_ASS_USER=(SELECT SE_NAME FROM TT_CRM_SEATS WHERE SE_USER_ID= "+reviewers[i]+")");
						sql.append(" ,V.RV_STATUS= "+Constant.RV_STATUS_1);
						sql.append(" ,V.UPDATE_BY= "+logonUse.getUserId());
						sql.append(" ,V.UPDATE_DATE= sysdate ");
						sql.append(" WHERE V.RV_ID ="+idLIst.get(i).get("RV_ID")+";");
					}else{
						break;
					}
					
				}
			}else{	//如果指定回访人数小于客户数，大多数是这种情况。
				int temp=0;
				for(Map<String, Object> m:idLIst)
				{
					if(temp>=reviewers.length)
					{
						temp=0;
					}
					sql.append("");
					sql.append(" UPDATE TT_CRM_RETURN_VISIT V SET V.RV_ASS_USER_ID= "+reviewers[temp]);
					sql.append(" , V.RV_ASS_USER=(SELECT SE_NAME FROM TT_CRM_SEATS WHERE SE_USER_ID= "+reviewers[temp]+")");
					sql.append(" , V.RV_STATUS= "+Constant.RV_STATUS_1);
					sql.append(" , V.UPDATE_BY= "+logonUse.getUserId());
					sql.append(" , V.UPDATE_DATE= sysdate ");
					sql.append(" WHERE V.RV_ID ="+m.get("RV_ID"));
					sql.append(" ;");
					temp++;
				}
			}
			
		}else if(code.length>0){
			
			//如果回访人数大于等于客户数
			String[] reviewers=resvalue.split(",");
			if(reviewers.length>=code.length)
			{
				for(int i=0;i<reviewers.length;i++)
				{
					if(i<code.length)
					{
						sql.append("UPDATE TT_CRM_RETURN_VISIT V SET  V.RV_ASS_USER_ID= "+reviewers[i]);
						sql.append(" , V.RV_ASS_USER=(SELECT SE_NAME FROM TT_CRM_SEATS WHERE SE_USER_ID= "+reviewers[i]+")");
						sql.append(" , V.RV_STATUS= "+Constant.RV_STATUS_1);
						sql.append(" , V.UPDATE_BY= "+logonUse.getUserId());
						sql.append(" , V.UPDATE_DATE= sysdate ");
						sql.append("	WHERE V.RV_ID ="+code[i]);
						sql.append(";");
					}else
					{
						break;
					}
					
				}
			}else{
				int temp=0;
				for(int j=0;j<code.length;j++)
				{
					if(temp>=reviewers.length)
					{
						temp=0;
					}
					sql.append("UPDATE TT_CRM_RETURN_VISIT V SET  V.RV_ASS_USER_ID= "+reviewers[temp]);
					sql.append(" ,V.RV_ASS_USER=(SELECT SE_NAME FROM TT_CRM_SEATS WHERE SE_USER_ID= "+reviewers[temp]+")");
					sql.append(" ,V.RV_STATUS= "+Constant.RV_STATUS_1);
					sql.append(" ,V.UPDATE_BY= "+logonUse.getUserId());
					sql.append(" ,V.UPDATE_DATE= sysdate ");
					sql.append("	WHERE V.RV_ID ="+code[j]);
					sql.append(";");
					temp++;
				}
			}
		}
		
		String [] sqls=sql.toString().split(";");
		for (String s :sqls)
		{
			if(s.length()>25)
			{
				factory.update(s,null);
			}
			
		}
		
	}
	 /**
	  * 
	  * @Title      : 
	  * @Description: TODO  根据序号查询待指定回访人的回访ID
	  * @param      : @param NumberFrom
	  * @param      : @param NumberTo
	  * @param      : @param RV_TYPE
	  * @param      : @param checkSDate
	  * @param      : @param checkEDate
	  * @param      : @return      
	  * @return     :    
	  * @throws     :
	  * LastDate    : 2013-4-16
	  */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReviewId(String NumberFrom,String NumberTo,String RV_TYPE,String checkSDate,String checkEDate)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT RV_ID \n" +
				"  FROM (SELECT A.*, ROWNUM R\n" + 
				"          FROM (SELECT V.RV_ID"+
				"                  FROM TT_CRM_RETURN_VISIT  V /*客户回访表*/\n" + 
				"                 WHERE 1 = 1 \n" + 
				"					AND V.RV_STATUS="+Constant.RV_STATUS_6 );//等待指定回访人
		sql.append(" ");
		if(!("".equals(RV_TYPE))){
			sql.append(" and V.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(
				"         ) A) \n WHERE R >= \n" + NumberFrom+
				"           AND R <= "+NumberTo);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  题库查询
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param EX_NAME
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> examManageQuery(int pageSize, int curPage, String EX_NAME) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT E.EX_ID,\n" + 
				"       E.EX_NAME,\n" + 
				"       E.EX_DESCRIPTION,\n" + 
				"       (SELECT COUNT(EX.ED_ID)\n" + 
				"          FROM TT_CRM_EXAM_DETAIL EX\n" + 
				"         WHERE EX.EX_ID = E.EX_ID AND EX.ED_STATUS<>"+Constant.STATUS_DISABLE+") EXNUM\n" + 
				"  FROM TT_CRM_EXAM E\n" + 
				" WHERE 1=1 ");
		sql.append("	AND E.EX_STATUS<>"+Constant.STATUS_DISABLE);
		if(!("".equals(EX_NAME))){
			sql.append(" and E.EX_NAME like '%"+EX_NAME+"%'");
		}
		sql.append("	ORDER BY CREATE_DATE DESC ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题库删除 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void examDelete(String [] code)
	{
		allQuestionDelete(code);
		StringBuffer sql= new StringBuffer();
		sql.append(" update  TT_CRM_EXAM set ex_status="+Constant.STATUS_DISABLE+"  where EX_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void allQuestionDelete(String [] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update    TT_CRM_EXAM_DETAIL SET ED_STATUS="+Constant.STATUS_DISABLE+" where EX_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  查询题 库中问题
	 * @param      : @param exId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getQuestionList(String exId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select rownum ed_no,q.* from (select t.ex_id,/*题库ID*/\n" +
				"       t.ed_que_type,/*问题类型*/\n" + 
				"       t.ed_question,/*问题*/\n" + 
				"       t.ed_choice,/*问题选项*/\n" + 
				"       t.ed_txt_type,/*文本框类型*/\n" + 
				"       t.ed_width,/*宽度*/\n" + 
				"       t.ed_hight/*高度*/\n" + 
				"  from TT_CRM_EXAM_DETAIL t/*题库详情表*/\n" + 
				"  where 1=1\n" +
				"  and t.ED_STATUS=" +Constant.STATUS_ENABLE+ 
				"        and t.ex_id=" +exId+
				" order by t.create_date asc) q order by ed_no asc ");
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> questionEditQuary(int pageSize, int curPage,String exId)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select  ed_id, t.ex_id,/*题库ID*/\n" +
				"       qt.code_desc ed_que_type,  /*问题类型*/\n" + 
				"       t.ed_question,/*问题*/\n" + 
				"       t.ed_choice,/*问题选项*/\n" + 
				"       tt.code_desc ed_txt_type,/*文本框类型*/\n" + 
				"       t.ed_width,/*宽度*/\n" + 
				"       t.ed_hight /*高度*/\n" + 
				"  from TT_CRM_EXAM_DETAIL t,    /*题库详情表*/\n" + 
				"       tc_code qt,   /*问题类型表*/\n" + 
				"       tc_code tt  /*文本类型*/\n" + 
				"     where 1=1\n" +
				"  			and t.ED_STATUS=" +Constant.STATUS_ENABLE+ 
				"           and t.ed_que_type=qt.code_id(+)\n" + 
				"           and t.ed_txt_type=tt.code_id(+)\n" + 
				"           and t.ex_id=  " +exId+
				"			order by t.ed_id asc");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询题库信息
	 * @param      : @param exId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public List<Map<String,Object>> getExamInfo(String exId)
	{

		StringBuilder sql = new StringBuilder();
		sql.append("select t.ex_id,/*题库ID*/\n" +
				"       t.ex_name,/*题库名称*/\n" + 
				"       t.ex_description/*题库说明*/\n" + 
				"  from TT_CRM_EXAM t/*题库表*/\n" + 
				"  where 1=1\n" + 
				"        and t.ex_id=" + exId);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public List<Map<String,Object>> getRight(AclUserBean logonUser)
	{

		StringBuilder sql = new StringBuilder();
		sql.append("select NVL(T.SE_IS_MANAMGER,0) SE_IS_MANAMGER /*是否是座席管理员*/ " +
				"  from tc_user u, TT_CRM_SEATS t/*座席表*/ " + 
				"  where u.user_id = t.se_user_id(+) " + 
				"  AND u.user_id =" + logonUser.getUserId());
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询所有题库
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public List<Map<String,Object>> getExams()
	{

		StringBuilder sql = new StringBuilder();
		sql.append("select t.ex_id,/*题库ID*/\n" +
				"       t.ex_name /*题库名称*/\n" + 
				"  from TT_CRM_EXAM t/*题库表*/\n" + 
				"  where 1=1\n" ); 
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题 库修改保存
	 * @param      : @param exId
	 * @param      : @param exName
	 * @param      : @param exDes      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void editSaveFact(AclUserBean logonUser,String exId,String exName,String exDes)
	{
		TtCrmExamPO oldPo= new TtCrmExamPO();
		oldPo.setExId(Long.parseLong(exId));
		List<TtCrmExamPO> old =dao.select(oldPo);
		
		TtCrmExamPO newPo=new TtCrmExamPO();
		newPo.setExId(Long.parseLong(exId));
		newPo.setExName(exName);
		newPo.setExDescription(exDes);
		newPo.setUpdateBy(logonUser.getUserId().toString());
		newPo.setUpdateDate(new Date());
		dao.update(oldPo, newPo);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 保存题库
	 * @param      : @param logonUser
	 * @param      : @param exId
	 * @param      : @param exName
	 * @param      : @param exDes      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-20
	 */
	public void addSaveFact(AclUserBean logonUser,String exId,String exName,String exDes)
	{
		TtCrmExamPO po=new TtCrmExamPO();
		po.setExId(Long.parseLong(exId));
		po.setExName(exName);
		po.setExDescription(exDes);
		po.setCreateBy(logonUser.getUserId().toString());
		po.setCreateDate(new Date());
		factory.insert(po);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 题库唯一性检查
	 * @param      : @param exName      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	
	public List<Map<String,Object>> addSaveFactCheck(String exName)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_EXAM WHERE EX_NAME ='"+exName+"' AND EX_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  题库修改唯一性检测
	 * @param      : @param exName
	 * @param      : @param exId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> editSaveFactCheck(String exName,String exId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_EXAM WHERE EX_NAME ='"+exName+"' AND EX_ID<>'"+exId+"' AND EX_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除问题 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void questionDelete(String [] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update   TT_CRM_EXAM_DETAIL SET ED_STATUS="+Constant.STATUS_DISABLE+" where ED_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 删除问题
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void questionDelete2(String [] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update TT_CRM_QUE_DETAIL set QD_STATUS="+Constant.STATUS_DISABLE+" where QD_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获得问题类型 
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChoiceList()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select  CODE_ID, CODE_DESC from TC_CODE WHERE TYPE = "+Constant.QD_QUE_TYPE +" ORDER BY NUM ASC");
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  保存问题
	 * @param      : @param logonUser
	 * @param      : @param exId
	 * @param      : @param questuionType
	 * @param      : @param rblTextMode
	 * @param      : @param txtWidth
	 * @param      : @param txtHeight
	 * @param      : @param txtQuestion
	 * @param      : @param txtSelection      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void questionAddFact(AclUserBean logonUser,String exId,String questuionType,String rblTextMode,String txtWidth,String txtHeight,String txtQuestion,String txtSelection)
	{
		TtCrmExamDetailPO po= new TtCrmExamDetailPO();
		Long ED_ID=Long.parseLong(SequenceManager.getSequence(""));
		po.setEdId(ED_ID);
		po.setExId(Long.parseLong(exId));
		po.setEdQueType(Integer.parseInt(questuionType));
		po.setEdQuestion(txtQuestion);
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		if(!"".equals(questuionType))
		{
			if(questuionType.equals(Constant.QD_QUE_TYPE_1.toString()))//单选
			{
				po.setEdChoice(txtSelection);
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_2.toString()))//多选
			{
				po.setEdChoice(txtSelection);
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_3.toString()))//问答
			{
				if(rblTextMode.equals("1"))
				{
					po.setEdTxtType(Constant.qd_txt_type_1);
					po.setEdWidth(Integer.parseInt(txtWidth));
				}else
				{	po.setEdTxtType(Constant.qd_txt_type_2);
					po.setEdHight(Integer.parseInt(txtHeight));
					po.setEdWidth(Integer.parseInt(txtWidth));
				}
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_4.toString()))//问答统计
			{
				if(rblTextMode.equals("1"))
				{
					po.setEdTxtType(Constant.qd_txt_type_1);
					po.setEdWidth(Integer.parseInt(txtWidth));
				}else
				{	po.setEdTxtType(Constant.qd_txt_type_2);
					po.setEdHight(Integer.parseInt(txtHeight));
					po.setEdWidth(Integer.parseInt(txtWidth));
				}
			}
		}
		dao.insert(po);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 题库 新增问题唯一性检查
	 * @param      : @param exId
	 * @param      : @param txtQuestion
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> questionAddFactCheck(String exId,String txtQuestion)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_EXAM_DETAIL WHERE EX_ID ='"+exId+"' AND ED_QUESTION='"+txtQuestion.replaceAll("'", "''")+"' AND ED_STATUS='"+Constant.STATUS_ENABLE+"'");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 问卷新增问题唯一性验证
	 * @param      : @param qrId
	 * @param      : @param txtQuestion
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public List<Map<String,Object>> questionAddFactCheck2(String qrId,String txtQuestion)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUE_DETAIL WHERE QR_ID ='"+qrId+"' AND QD_QUESTION='"+txtQuestion.replaceAll("'", "''")+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"'");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  题库修改问题唯一性检测
	 * @param      : @param exId
	 * @param      : @param txtQuestion
	 * @param      : @param edId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> questionEditFactCheck(String exId,String txtQuestion,String edId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_EXAM_DETAIL WHERE EX_ID ='"+exId+"' AND ED_QUESTION='"+txtQuestion.replaceAll("'", "''")+"' AND ED_ID <> '"+edId+"' AND ED_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  问卷修改问题唯一性检测
	 * @param      : @param qrId
	 * @param      : @param txtQuestion
	 * @param      : @param qdId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	
	public List<Map<String,Object>> questionEditFactCheck2(String qrId,String txtQuestion,String qdId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUE_DETAIL WHERE QR_ID ='"+qrId+"' AND QD_QUESTION='"+txtQuestion.replaceAll("'", "''")+"' AND QD_ID <> '"+qdId+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  问卷修改问题题号唯一性检测
	 * @param      : @param qrId
	 * @param      : @param txtQuestion
	 * @param      : @param qdId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	
	public List<Map<String,Object>> questionNoEditFactCheck2(String qrId,String qdNo,String qdId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUE_DETAIL WHERE QR_ID ='"+qrId+"' AND QD_NO='"+qdNo+"' AND QD_ID <> '"+qdId+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list1= pageQuery(sql.toString(), null, getFunName());
		return list1;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 编辑题库--编辑问题--查询被编辑问题信息
	 * @param      : @param exId
	 * @param      : @param edId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public List<Map<String,Object>> getQuestion(String exId, String edId )
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select t.ex_id,/*题库ID*/\n" +
				"       t.ed_id,/*问题ID*/\n" + 
				"       t.ed_que_type,/*问题类型*/\n" + 
				"       t.ed_question,/*问题*/\n" + 
				"       t.ed_choice,/*问题选项*/\n" + 
				"       t.ed_txt_type,/*文本框类型*/\n" + 
				"       t.ed_width,/*宽度*/\n" + 
				"       t.ed_hight/*高度*/\n" + 
				"  from TT_CRM_EXAM_DETAIL t/*题库详情表*/\n" + 
				"  where 1=1\n" + 
				"        and t.ex_id=" + exId+
				"        and t.ed_id= "+edId);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 编辑问卷--编辑问题--查询被编辑问题信息
	 * @param      : @param qrId
	 * @param      : @param qdId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> getQuestion2(String qrId, String qdId )
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select t.qr_id,/*问卷ID*/\n" +
				"       t.qd_id,/*问题ID*/\n" + 
				"       t.qd_no,/*题号*/\n" + 
				"       t.qd_que_type,/*问题类型*/\n" + 
				"       t.qd_question,/*问题*/\n" + 
				"       t.qd_choice,/*问题选项*/\n" + 
				"       t.qd_txt_type,/*文本框类型*/\n" + 
				"       t.ED_TXT_WIDTH,/*宽度*/\n" + 
				"       t.ED_TXT_HIGHT/*高度*/\n" + 
				"  from TT_CRM_QUE_DETAIL t/*题库详情表*/\n" + 
				"  where 1=1\n" + 
				"        and t.qr_id=" + qrId+
				"        and t.qd_id= "+qdId);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  编辑题库--编辑问题--保存题库中被编辑的问题
	 * @param      : @param logonUser
	 * @param      : @param exId
	 * @param      : @param edId
	 * @param      : @param questuionType
	 * @param      : @param rblTextMode
	 * @param      : @param txtWidth
	 * @param      : @param txtHeight
	 * @param      : @param txtQuestion
	 * @param      : @param txtSelection      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void questionEditFact(AclUserBean logonUser,String exId,String edId,String questuionType,String rblTextMode,String txtWidth,String txtHeight,String txtQuestion,String txtSelection)
	{
		TtCrmExamDetailPO po= new TtCrmExamDetailPO();
		po.setEdId(Long.parseLong(edId));
		po.setExId(Long.parseLong(exId));
		List<TtCrmExamDetailPO> list = dao.select(po);
		TtCrmExamDetailPO newpo= new TtCrmExamDetailPO();
		
		newpo.setEdId(Long.parseLong(edId));
		newpo.setExId(Long.parseLong(exId));
		newpo.setEdQueType(Integer.parseInt(questuionType));
		newpo.setEdQuestion(txtQuestion);
		newpo.setUpdateBy(logonUser.getUserId());
		newpo.setUpdateDate(new Date());
		
		if(questuionType.equals(Constant.QD_QUE_TYPE_1.toString()))//单选
		{
			newpo.setEdChoice(txtSelection);
			newpo.setEdWidth(600);
			newpo.setEdHight(110);
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_2.toString()))//多选
		{
			newpo.setEdChoice(txtSelection);
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_3.toString()))//问答
		{
			if(rblTextMode.equals("1"))
			{
				newpo.setEdTxtType(Constant.qd_txt_type_1);
				newpo.setEdWidth(Integer.parseInt(txtWidth));
				newpo.setEdHight(110);
				newpo.setEdChoice("");
				
			}else if(rblTextMode.equals("2"))
			{
				newpo.setEdChoice("");
				newpo.setEdTxtType(Constant.qd_txt_type_2);
				newpo.setEdWidth(Integer.parseInt(txtWidth));
				newpo.setEdHight(Integer.parseInt(txtHeight));
			}
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_4.toString()))//问答统计
		{
			if(rblTextMode.equals("1"))
			{
				newpo.setEdChoice("");
				newpo.setEdTxtType(Constant.qd_txt_type_1);
				newpo.setEdWidth(Integer.parseInt(txtWidth));
			}else if(rblTextMode.equals("2"))
			{
				newpo.setEdChoice("");
				newpo.setEdTxtType(Constant.qd_txt_type_2);
				newpo.setEdWidth(Integer.parseInt(txtWidth));
				newpo.setEdHight(Integer.parseInt(txtHeight));
			}
		}
		
		dao.update(po, newpo);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 编辑问卷--编辑问题--保存问卷中被编辑的问题
	 * @param      : @param logonUser
	 * @param      : @param exId
	 * @param      : @param edId
	 * @param      : @param questuionType
	 * @param      : @param rblTextMode
	 * @param      : @param txtWidth
	 * @param      : @param txtHeight
	 * @param      : @param txtQuestion
	 * @param      : @param txtSelection      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void questionEditFact2(AclUserBean logonUser,String qrId,String qdId,String qdNo, String questuionType,String rblTextMode,String txtWidth,String txtHeight,String txtQuestion,String txtSelection)
	{
		TtCrmQueDetailPO po = new TtCrmQueDetailPO();
		po.setQrId(Long.parseLong(qrId));
		po.setQdId(Long.parseLong(qdId));
		List<TtCrmExamDetailPO> list = dao.select(po);
		
		TtCrmQueDetailPO newpo= new TtCrmQueDetailPO();
		newpo.setQrId(Long.parseLong(qrId));
		newpo.setQdId(Long.parseLong(qdId));
		newpo.setQdNo(Integer.parseInt(qdNo));
		newpo.setQdQueType(Integer.parseInt(questuionType));
		newpo.setQdQuestion(txtQuestion);
		newpo.setUpdateBy(logonUser.getUserId());
		newpo.setUpdateDate(new Date());
		
		StringBuilder sql = new StringBuilder();
		
		if(questuionType.equals(Constant.QD_QUE_TYPE_1.toString()))
		{
			newpo.setQdChoice(txtSelection);
			
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_2.toString()))
		{
			newpo.setQdChoice(txtSelection);
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_3.toString()))
		{
			if(rblTextMode.equals("1"))
			{
				newpo.setQdTxtType(Constant.qd_txt_type_1);
				newpo.setEdTxtWidth(Integer.parseInt(txtWidth));
				newpo.setQdChoice("");
			}else if(rblTextMode.equals("2"))
			{
				newpo.setQdTxtType(Constant.qd_txt_type_2);
				newpo.setEdTxtWidth(Integer.parseInt(txtWidth));
				newpo.setEdTxtHight(Integer.parseInt(txtHeight));
				newpo.setQdChoice("");
			}
		}else if(questuionType.equals(Constant.QD_QUE_TYPE_4.toString()))
		{
			if(rblTextMode.equals("1"))
			{
				newpo.setQdTxtType(Constant.qd_txt_type_1);
				newpo.setEdTxtWidth(Integer.parseInt(txtWidth));
				newpo.setQdChoice("");
			}else if(rblTextMode.equals("2"))
			{
				newpo.setQdTxtType(Constant.qd_txt_type_2);
				newpo.setEdTxtWidth(Integer.parseInt(txtWidth));
				newpo.setEdTxtHight(Integer.parseInt(txtHeight));
				newpo.setQdChoice("");
			}
		}
		dao.update(po, newpo);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 获得车系
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-20
	 */
	public List<Map<String, Object>> getSeriesList(){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT GROUP_ID, GROUP_CODE, GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_LEVEL = 2");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获得车型
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public List<Map<String, Object>> getVehicleTypeList(){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT GROUP_ID, GROUP_CODE, GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_LEVEL = 3");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 获取服务站类型
	 * @return
	 */
	public List<Map<String,Object>> getDealerClass(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tc_code where type = "+Constant.DEALER_CLASS_TYPE+" order by code_id");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reviewScheduleQuery(int pageSize, int curPage,String RV_ASS_USER,String checkSDate,String checkEDate)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		StringBuffer conditionSql = new StringBuffer();
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			conditionSql.append(" and V.UPDATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			conditionSql.append(" and V.UPDATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			conditionSql.append(" and V.UPDATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			conditionSql.append(" and V.UPDATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sql.append("SELECT DISTINCT(A.RV_ASS_USER_ID),A.RV_ASS_USER,B.TYPE0,C.TYPE1,C.TYPE2,C.TYPE3,a.se_seats_no,(C.TYPE2+c.TYPE3) as TYPE4 FROM\n" +
				"  (SELECT DISTINCT(V.RV_ASS_USER_ID),V.RV_ASS_USER,V.UPDATE_DATE as RV_DATE,s.se_seats_no FROM  TT_CRM_RETURN_VISIT V  left join TT_CRM_SEATS s on v.rv_ass_user_id = s.se_user_id WHERE V.RV_ASS_USER_ID IS NOT NULL AND v.rv_status in(" +Constant.RV_STATUS_1+","+Constant.RV_STATUS_2+","+ Constant.RV_STATUS_4+")) A,\n" + 
				"  (SELECT V.RV_ASS_USER_ID ,COUNT(0) AS TYPE0 FROM   TT_CRM_RETURN_VISIT V WHERE V.RV_ASS_USER_ID IS NOT NULL and v.rv_status in(" +Constant.RV_STATUS_1+","+Constant.RV_STATUS_2+","+ Constant.RV_STATUS_4+") "+conditionSql.toString()+" GROUP BY  V.RV_ASS_USER_ID) B,\n" + 
				"  (select  r.rv_ass_user_id,\n" + 
				"          SUM(decode (r.rv_status,'95081001',mumb,0)) AS TYPE1 ,/*未回访*/\n" + 
				"          SUM(decode (r.rv_status,'95081002',mumb,0)) AS TYPE2,/*继续回访*/\n" + 
				"          SUM(decode (r.rv_status,'95081004',mumb,0)) AS TYPE3/*已回访*/\n" + 
				"  from(     select v.rv_ass_user_id, COUNT(rv_ass_user_id), v.rv_status, count( v.rv_status) as mumb\n" + 
				"             from TT_CRM_RETURN_VISIT v WHERE  V.RV_STATUS IN(" +Constant.RV_STATUS_1+","+Constant.RV_STATUS_2+","+ Constant.RV_STATUS_4+") "+conditionSql.toString()+
				"            group by v.rv_ass_user_id, v.rv_status) r group by rv_ass_user_id) C\n" + 
				"  WHERE A.rv_ass_user_id=B.rv_ass_user_id\n" + 
				"    AND A.rv_ass_user_id=C.rv_ass_user_id\n" + 
				"    AND B.rv_ass_user_id=C.rv_ass_user_id");
		if(!"".equals(RV_ASS_USER))
		{
			sql.append("	AND RV_ASS_USER LIKE '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  	满意度回访查询
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param checkSDate
	 * @param      : @param checkEDate
	 * @param      : @param ddlClasses
	 * @param      : @param ddlAutoTypeCode
	 * @param      : @param VIN
	 * @param      : @param chkServiceStationType
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> reviewSatisfactionQuery(int pageSize, int curPage,String checkSDate,String checkEDate,String ddlClasses,String ddlAutoTypeCode,String VIN,String chkServiceStationType )
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT R.ID, /*工单ID*/\n" +
				"       R.RO_NO, /*工单号*/\n" + 
				"       TC.CODE_DESC AS REPAIR_TYPE_CODE, /*维修类型*/\n" + 
				"       TO_CHAR(R.RO_CREATE_DATE, 'yyyy-MM-dd') RO_CREATE_DATE, /*工单生成日期*/\n" + 
				"       R.OWNER_NAME, /*车主姓名*/\n" + 
				"       R.OWNER_NAME CTM_NAME, /*客户姓名*/\n" + 
				"       DAS.CTM_ID, /*客户ID*/\n" + 
				"       R.VIN, /*VIN*/\n" + 
				"       R.IN_MILEAGE TOTAL_MILEAGE, /*总量程数*/\n" + 
				"       R.DEALER_ID, /*经销商代码*/\n" + 
				"       TD.DEALER_NAME, /*经销商全称*/\n" + 
				"       R.SERIES SERIES_NAME, /*车系*/\n" + 
				"       R.MODEL MODEL_CODE /*车型*/\n" + 
				"  FROM TT_AS_REPAIR_ORDER R\n" + 
				"  JOIN TC_CODE TC ON R.REPAIR_TYPE_CODE = TC.CODE_ID AND TC.TYPE = 1144 /*tc_code表*/\n" + 
				"  JOIN TM_VEHICLE V ON R.VIN = V.VIN\n" + 
				"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON V.VEHICLE_ID = DAS.VEHICLE_ID\n" + 
				"  LEFT JOIN TM_DEALER TD ON R.DEALER_ID = TD.DEALER_ID\n" + 
				" WHERE 1 = 1");
		if(!"".equals(VIN))
		{
			sql.append("	AND R.VIN LIKE '%"+VIN+"%'" );
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" AND R.RO_CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND R.RO_CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" AND R.RO_CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" AND R.RO_CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!"".equals(ddlClasses))//汽车种类
		{
			sql.append("	AND r.SERIES='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode))//汽车型号
		{
			sql.append("	AND r.MODEL like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		if(!"".equals(chkServiceStationType)){//服务站类型
			sql.append(" and td.dealer_class in ("+chkServiceStationType+")");
		}
		sql.append(" order by RO_CREATE_DATE desc");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 问卷查询
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param QR_TYPE
	 * @param      : @param checkSDate
	 * @param      : @param checkEDate
	 * @param      : @param QR_STATUS
	 * @param      : @param QR_NAME
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> questionairManageQuery(int pageSize, int curPage,String QR_TYPE,String checkSDate,String checkEDate,String QR_STATUS,String QR_NAME)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT Q.QR_ID, Q.QR_NAME, TP.CODE_DESC AS QR_TYPE, ST.CODE_ID, ST.CODE_DESC AS QR_STATUS, Q.CREATE_DATE\n" +
				"  FROM TT_CRM_QUESTIONNAIRE Q\n" + 
				"  LEFT JOIN TC_CODE  TP ON TP.CODE_ID=Q.QR_TYPE\n" + 
				"  LEFT JOIN TC_CODE  ST ON ST.CODE_ID=Q.QR_STATUS");
		sql.append(" WHERE 1=1 ");
		sql.append("	AND Q.QR_STATUS_DEL="+Constant.STATUS_ENABLE);
		if(!"".equals(QR_NAME))
		{
			sql.append("	AND Q.QR_NAME LIKE '%"+QR_NAME+"%'");
		}
		if(!"".equals(QR_TYPE))
		{
			sql.append("	AND Q.QR_TYPE = "+QR_TYPE+"");
		}
		if(!"".equals(QR_STATUS))
		{
			sql.append("	AND Q.QR_STATUS = "+QR_STATUS+"");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" AND Q.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND Q.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" AND Q.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" AND Q.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sql.append(" ORDER BY Q.CREATE_DATE DESC");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 设置问卷生效 
	 * @param      : @param logonUser
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void setStatus(AclUserBean logonUser,String [] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update  TT_CRM_QUESTIONNAIRE" +
				"		SET QR_STATUS=" +Constant.QR_STATUS_1+","+
				"			UPDATE_BY="+logonUser.getUserId()+","+
				"			UPDATE_DATE=sysdate"+
				""+
				" where QR_ID in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  删除问卷
	 * @param      : @param logonUser
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void deleteQuestionair(AclUserBean logonUser,String [] code)
	{
		deleteQuestions(code);
		StringBuffer sql= new StringBuffer();
		sql.append(" update TT_CRM_QUESTIONNAIRE set QR_STATUS_DEL="+Constant.STATUS_DISABLE+" where QR_ID  in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除问卷中的所有问题。 
	 * @param      : @param code      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void deleteQuestions(String [] code)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update  TT_CRM_QUE_DETAIL set QD_STATUS="+Constant.STATUS_DISABLE+" where QR_ID  in(");
		for(String c:code)
		{
			sql.append(c);
			sql.append(",");
		}
		sql.setCharAt(sql.lastIndexOf(","), ')');
		factory.update(sql.toString(),null);
	}
	@SuppressWarnings("unchecked")
	public  Map<String,LinkedList<Map<String,Object>>> questionairQuery(String QR_ID) throws Exception {
		Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
		LinkedList<Map<String, Object>> questionair = null;
		LinkedList<Map<String, Object>> questions = null;
		StringBuffer sql= new StringBuffer();
		StringBuffer sql2= new StringBuffer();
		sql.append("SELECT Q.QR_NAME,Q.QR_GUIDE,Q.QR_THANKS FROM  TT_CRM_QUESTIONNAIRE Q WHERE Q.QR_ID="+QR_ID);
		sql2.append("SELECT QD.QD_NO,/*题 号*/\n" +
				"       QD.QD_QUESTION,/*问题*/\n" + 
				"       QD.QD_QUE_TYPE,/*问题类型*/\n" + 
				"       QD.QD_TXT_TYPE,/*文本框类型*/\n" + 
				"       QD.QD_CHOICE, /*问题选项*/\n" + 
				"       QD.ED_TXT_HIGHT,\n" + 
				"       QD.ED_TXT_WIDTH\n" + 
				"       FROM TT_CRM_QUE_DETAIL QD\n" + 
				"       WHERE  qd.qd_status = "+Constant.STATUS_ENABLE+"\n" + 
				"         AND QD.QR_ID="+QR_ID);
		sql2.append("	ORDER BY  QD.QD_NO ASC");
		questionair = (LinkedList<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
		questions=(LinkedList<Map<String, Object>>) pageQuery(sql2.toString(), null, getFunName());
		result.put("questionair", questionair);
		result.put("questions", questions);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 编辑问卷中查询问卷信息
	 * @param      : @param qrId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public List<Map<String,Object>> getQuestionairDetail(String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT Q.QR_ID,Q.QR_TYPE,Q.QR_NAME, Q.QR_DESCRIPTION, Q.QR_GUIDE, Q.QR_THANKS\n" +
				"  FROM TT_CRM_QUESTIONNAIRE Q\n" + 
				" WHERE Q.QR_ID =" + qrId);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  编辑问卷中查询该问卷包含的问题
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @param qrId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> questionAddQuery(int pageSize, int curPage,String qrId)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT QD.QD_ID, QD.QD_NO, /*题号*/\n" +
				"       TC.CODE_DESC QD_QUE_TYPE,/*问题类型*/\n" + 
				"       QD.QD_QUESTION,/*问题*/\n" + 
				"       QD.QD_CHOICE,/*答案选项*/\n" + 
				"       QD.QD_POINTS/*分值*/\n" + 
				"        FROM TT_CRM_QUE_DETAIL QD /*问卷明细表*/\n" + 
				"        JOIN TC_CODE           TC ON TC.CODE_ID=QD.QD_QUE_TYPE\n" + 
				"        WHERE QD.QR_ID ="+qrId);
		sql.append("	AND QD.QD_STATUS="+Constant.STATUS_ENABLE);
		sql.append(" ORDER BY QD.QD_ID ASC");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  保存新增问卷
	 * @param      : @param logonUser
	 * @param      : @param qrId
	 * @param      : @param QR_NAME
	 * @param      : @param QR_DESCRIPTION
	 * @param      : @param QR_GUIDE
	 * @param      : @param QR_THANKS      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void questionairAddFactf(AclUserBean logonUser,String qrId,String QR_NAME,String QR_TYPE,String QR_DESCRIPTION,String QR_GUIDE,String QR_THANKS)
	{
		TtCrmQuestionnairePO po= new TtCrmQuestionnairePO();
		po.setQrId(Long.parseLong(qrId));
		if(!"".equals(QR_NAME))
		{
			po.setQrName(QR_NAME);
		}
		po.setQrType(Integer.parseInt(QR_TYPE));
		po.setQrDescription(QR_DESCRIPTION);
		po.setQrGuide(QR_GUIDE);
		po.setQrThanks(QR_THANKS);
		po.setQrStatus(Integer.parseInt(Constant.QR_STATUS_2.toString()));
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		dao.insert(po);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 保存修改后的问卷
	 * @param      : @param logonUser
	 * @param      : @param qrId
	 * @param      : @param QR_NAME
	 * @param      : @param QR_DESCRIPTION
	 * @param      : @param QR_GUIDE
	 * @param      : @param QR_THANKS      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void questionairEditFact(AclUserBean logonUser,String qrId,String QR_NAME,String QR_TYPE,String QR_DESCRIPTION,String QR_GUIDE,String QR_THANKS)
	{
		TtCrmQuestionnairePO oldPo= new TtCrmQuestionnairePO();
		oldPo.setQrId(Long.parseLong(qrId));
		//新PO
		TtCrmQuestionnairePO newPo= new TtCrmQuestionnairePO();
		//Long QR_ID=Long.parseLong(SequenceManager.getSequence(""));
		newPo.setQrId(Long.parseLong(qrId));
		if(!"".equals(QR_NAME))
		{
			newPo.setQrName(QR_NAME);
		}
		newPo.setQrType(Integer.parseInt(QR_TYPE));
		newPo.setQrDescription(QR_DESCRIPTION);
		newPo.setQrGuide(QR_GUIDE);
		newPo.setQrThanks(QR_THANKS);
		newPo.setQrStatus(Integer.parseInt(Constant.QR_STATUS_2.toString()));
		newPo.setCreateBy(logonUser.getUserId());
		newPo.setCreateDate(new Date());
		dao.update(oldPo,newPo);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  问卷新增唯一必检查
	 * @param      : @param QR_NAME
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> questionairAddFact(String QR_NAME)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUESTIONNAIRE WHERE QR_NAME ='"+QR_NAME.replaceAll("'", "''")+"' AND QR_STATUS_DEL='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 编辑问卷唯一性检测 
	 * @param      : @param QR_NAME
	 * @param      : @param qrId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> questionairEditFact(String QR_NAME,String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUESTIONNAIRE WHERE QR_NAME ='"+QR_NAME.replaceAll("'", "''")+"' AND QR_ID<>'"+qrId+"' AND QR_STATUS_DEL='"+Constant.STATUS_ENABLE+"'");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 保存问卷新增问题
	 * @param      : @param logonUser
	 * @param      : @param exId
	 * @param      : @param questuionType
	 * @param      : @param rblTextMode
	 * @param      : @param txtWidth
	 * @param      : @param txtHeight
	 * @param      : @param txtQuestion
	 * @param      : @param txtSelection      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void questionAddFact2(AclUserBean logonUser,String qrId,String qdNo,String questuionType,String rblTextMode,String txtWidth,String txtHeight,String txtQuestion,String txtSelection)
	{
		TtCrmQueDetailPO po= new TtCrmQueDetailPO();
		Long QD_ID=Long.parseLong(SequenceManager.getSequence(""));
		po.setQdId(QD_ID);
		po.setQrId(Long.parseLong(qrId));
		po.setQdNo(Integer.parseInt(qdNo));
		po.setQdQueType(Integer.parseInt(questuionType));
		po.setQdQuestion(txtQuestion);
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		if(!"".equals(questuionType))
		{
			if(questuionType.equals(Constant.QD_QUE_TYPE_1.toString()))//单选
			{
				po.setQdChoice(txtSelection);
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_2.toString()))//多选
			{
				po.setQdChoice(txtSelection);
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_3.toString()))//问答
			{
				if(rblTextMode.equals("1"))
				{
					po.setQdTxtType(Constant.qd_txt_type_1);
					po.setEdTxtWidth(Integer.parseInt(txtWidth));
				}else
				{	po.setQdTxtType(Constant.qd_txt_type_2);
					po.setEdTxtHight(Integer.parseInt(txtHeight));
					po.setEdTxtWidth(Integer.parseInt(txtWidth));
				}
			}else if(questuionType.equals(Constant.QD_QUE_TYPE_4.toString()))//问答统计
			{
				if(rblTextMode.equals("1"))
				{
					po.setQdTxtType(Constant.qd_txt_type_1);
					po.setEdTxtWidth(Integer.parseInt(txtWidth));
				}else
				{	po.setQdTxtType(Constant.qd_txt_type_2);
					po.setEdTxtHight(Integer.parseInt(txtHeight));
					po.setEdTxtWidth(Integer.parseInt(txtWidth));
				}
			}
		}
//		List<Map<String,Object>> list= getNextQD_NO(qrId);
//		Integer qdNo= Integer.parseInt(list.get(0).get("NEXTNO").toString());
//		po.setQdNo(qdNo);
		dao.insert(po);
	}
	//获取最新的题号（在最大的题号上+1）
	public List<Map<String,Object>> getNextQD_NO(String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" select case when MAX(QD_NO) is null then 1 else MAX(QD_NO)  + 1 end nextNO from TT_CRM_QUE_DETAIL t WHERE t.qr_id='"+qrId+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  问卷新增问题题号唯一性检测
	 * @param      : @param qrId
	 * @param      : @param txtQuestion
	 * @param      : @param qdId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	
	public List<Map<String,Object>> questionNoAddFactCheck2(String qrId,String qdNo)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT COUNT (* ) AS NUM FROM TT_CRM_QUE_DETAIL WHERE QR_ID ='"+qrId+"' AND QD_NO='"+qdNo+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"' ");
		List<Map<String,Object>> list1= pageQuery(sql.toString(), null, getFunName());
		return list1;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 复制问卷数据
	 * @param      : @param logonUser
	 * @param      : @param qrId      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void copyQuestionair(AclUserBean logonUser,String qrId)
	{
		//复制问卷数据
		Long QR_ID=Long.parseLong(SequenceManager.getSequence(""));
		StringBuilder sql = new StringBuilder();
		sql.append(
				"INSERT INTO TT_CRM_QUESTIONNAIRE\n" +
				"  (SELECT "+QR_ID+",\n" + 
				"         Q.QR_NAME||'【复制】',\n" + 
				"         Q.QR_TYPE,\n" + 
				"         Q.QR_DESCRIPTION,\n" + 
				"         Q.QR_GUIDE,\n" + 
				"         Q.QR_THANKS,\n" + 
				"         "+logonUser.getUserId()+",\n" + 
				"         SYSDATE,\n" + 
				"         Q.UPDATE_BY,\n" + 
				"         Q.UPDATE_DATE,\n" + 
				"         Q.VAR,\n" + 
				"         "+Constant.QR_STATUS_2+",\n" + 
				"         "+Constant.STATUS_ENABLE+"\n" + 
				"     FROM TT_CRM_QUESTIONNAIRE Q\n" + 
				"    WHERE Q.QR_ID = "+qrId+")");
		
		factory.insert(sql.toString(), null);
		
		//复制 问卷中问题数据
		List<Map<String,Object>> list = getQuestionIds(qrId);
		StringBuilder sql2 = new StringBuilder();
		sql.append("");
		if(list.size()>0)
		{
			for(Map m:list)
			{
				Long QD_ID=Long.parseLong(SequenceManager.getSequence(""));
				sql2.append("INSERT INTO TT_CRM_QUE_DETAIL\n" +
						"  (SELECT "+QD_ID+",\n" + 
						"          "+QR_ID+",\n" + 
						"          QD_NO,\n" + 
						"          QD_QUE_TYPE,\n" + 
						"          QD_QUESTION,\n" + 
						"          QD_TXT_TYPE,\n" + 
						"          QD_CHOICE,\n" + 
						"          QD_POINTS,\n" + 
						"          ED_TXT_WIDTH,\n" + 
						"          ED_TXT_HIGHT,\n" + 
						"         "+logonUser.getUserId()+",\n" + 
						"          SYSDATE,\n" + 
						"          UPDATE_BY,\n" + 
						"          UPDATE_DATE,\n" + 
						"          VAR,\n" + 
						"          QD_STATUS\n" + 
						"     FROM TT_CRM_QUE_DETAIL\n" + 
						"    WHERE QD_ID = "+m.get("QD_ID")+")");
				factory.insert(sql2.toString(), null);
				sql2.delete(0, sql2.length());
			}
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 获得问卷中问题ID 
	 * @param      : @param qrId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public List<Map<String,Object>> getQuestionIds(String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT QD_ID FROM TT_CRM_QUE_DETAIL WHERE QR_ID ="+qrId+"");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询问卷信息
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getQuestionairInfo(String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  count(*) as num from TT_CRM_QUE_DETAIL WHERE QR_ID= "+qrId);
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询题库中的问题
	 * @param      : @param qrId
	 * @param      : @param dcode
	 * @param      : @param downtow
	 * @param      : @param dealerClass
	 * @param      : @param curPage
	 * @param      : @param pageSize
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	@SuppressWarnings("unchecked")
	public  PageResult<Map<String, Object>> getAllDRLByDeptId(String qrId,String dcode,String downtown,String dealerClass,int curPage,int pageSize) throws Exception {
		//List<Map<String,Object>> nameList = getNames( qrId);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ED.ED_ID, /*问题ID*/\n" +
				"       ED.EX_ID,/*题库ID*/\n" + 
				"       ED.ED_QUESTION, /*问题*/\n" + 
				"       TC.CODE_DESC ED_QUE_TYPE, /*问题类型*/\n" + 
				"       ED.ED_CHOICE, /*问题选项*/\n" + 
				"       ED.ED_WIDTH, /*问题宽度*/\n" + 
				"       ED.ED_HIGHT, /*问题高度*/\n" + 
				"       ES.EX_NAME /*问题题库名称*/\n" + 
				"  FROM TT_CRM_EXAM_DETAIL ED\n" + 
				"  JOIN TT_CRM_EXAM ES\n" + 
				"    ON ES.EX_ID = ED.EX_ID\n" + 
				"  LEFT JOIN TC_CODE TC\n" + 
				"    ON TC.CODE_ID = ED.ED_QUE_TYPE\n" + 
				" WHERE 1=1 " );
		sql.append("	AND ED.ED_STATUS="+Constant.STATUS_ENABLE);
		sql.append("	AND ED.ED_QUESTION NOT IN(");
		sql.append("SELECT QD_QUESTION FROM TT_CRM_QUE_DETAIL WHERE QR_ID ='"+qrId+"' AND QD_STATUS='"+Constant.STATUS_ENABLE+"' ");
		sql.append("	)");
		if(!"".equals(dcode))
		{
			sql.append("	AND	ES.ED_QUESTION like '%"+dcode+"%'");
		}
		if(!"".equals(downtown))
		{
			sql.append("	AND	ED.EX_ID ="+downtown+" ");
		}
		if(!"".equals(dealerClass))
		{
			sql.append("	AND	ED.ED_QUE_TYPE ="+dealerClass+"");
		}
		sql.append(" ORDER BY ED.ED_ID");
		
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);

		/*return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("ED_ID"));
					bean.setDealerId(rs.getLong("EX_ID"));
					bean.setDealerName(rs.getString("ED_QUESTION"));
					bean.setDealerShortname(rs.getString("ED_CHOICE"));
					bean.setDealerType(rs.getInt("ED_QUE_TYPE"));
					bean.setDealerStar(rs.getString("EX_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);*/
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询问卷中问题名称
	 * @param      : @param qrId
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public List<Map<String,Object>> getNames(String qrId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append(" SELECT QD_QUESTION FROM TT_CRM_QUE_DETAIL WHERE QR_ID ="+qrId+"");
		List<Map<String,Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 问卷从题库中添加问题
	 * @param      : @param logonUser
	 * @param      : @param qrId
	 * @param      : @param ids      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void setQuestions(AclUserBean logonUser,String qrId,String ids) 
	{
		 StringBuilder sql = new StringBuilder();
		 sql.append(
				 "SELECT E.ED_ID, /*问题ID*/\n" +
				 "       E.ED_QUESTION, /*问题名称*/\n" + 
				 "       E.ED_QUE_TYPE, /*问题类型 */\n" + 
				 "       E.ED_CHOICE, /*问题选项*/\n" + 
				 "       E.ED_HIGHT, /*高度*/\n" + 
				 "       E.ED_TXT_TYPE, /*文本类型*/\n" + 
				 "       E.ED_WIDTH /*宽度*/\n" + 
				 "\n" + 
				 "  FROM TT_CRM_EXAM_DETAIL E\n" + 
				 " WHERE E.ED_ID IN ("+ids+") ORDER BY E.ED_ID ");
		List<TtCrmExamDetailPO> questions=dao.select(TtCrmExamDetailPO.class, sql.toString(), null);
		
		for(TtCrmExamDetailPO p:questions)
		{
			List<Map<String,Object>> list= getNextQD_NO(qrId);
			Integer qdNo= Integer.parseInt(list.get(0).get("NEXTNO").toString());
			Long qdId=Long.parseLong(SequenceManager.getSequence(""));
			TtCrmQueDetailPO tempPo= new TtCrmQueDetailPO();
			tempPo.setQrId(Long.parseLong(qrId));
			tempPo.setQdQuestion(p.getEdQuestion());
			tempPo.setQdId(qdId);
			tempPo.setQdNo(qdNo);
			tempPo.setCreateBy(logonUser.getUserId());
			tempPo.setCreateDate(new Date());
			tempPo.setEdTxtHight(p.getEdHight());
			tempPo.setEdTxtWidth(p.getEdWidth());
			tempPo.setQdChoice(p.getEdChoice());
			tempPo.setQdQueType(p.getEdQueType());
			tempPo.setQdTxtType(p.getEdTxtType());
			
			dao.insert(tempPo);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  查询要进行回访的客户
	 * @param      : @param checkSDate2
	 * @param      : @param checkEDate2
	 * @param      : @param checkSDate
	 * @param      : @param checkEDate
	 * @param      : @param ddlAutoTypeCode
	 * @param      : @param ddlClasses
	 * @param      : @param txtUserLevel
	 * @param      : @param downtown
	 * @param      : @param txtPurpose
	 * @param      : @param customer_type
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public PageResult<Map<String, Object>> customerQuery(String checkSDate2,String checkEDate2,String checkSDate,String checkEDate,String ddlAutoTypeCode,String ddlClasses,String txtUserLevel,String downtown,String txtPurpose,String customer_type,String txtBatholithNum, String dealerName,int pageSize, int curPage)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT CR.CTM_ID, /*客户ID*/\n");
		sql.append("                       CR.CTM_NAME, /*客户名称*/\n");
		sql.append("                       CT.CODE_DESC CTM_TYPE, /*客户类型*/\n");
		sql.append("                       TC.CODE_DESC SALES_ADDRESS, /*购买用途*/\n");
		sql.append("                       PROV.REGION_NAME PROVINCE, /*省份*/\n");
		sql.append("                       CITY.REGION_NAME CITY, /*城市*/\n");
		sql.append("                       CR.GUEST_STARS, /*客户等级*/\n");
		sql.append("                       S.GROUP_NAME SERIES_NAME, /*车系*/\n");
		sql.append("                       M.GROUP_NAME MODEL_CODE, /*车型*/\n");
		sql.append("                       CE.VIN VIN, /*VIN*/\n");
		sql.append("                       TO_CHAR(CE.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE, /*购买日期*/\n");
		sql.append("                       D.DEALER_CODE, /*经销商代码*/\n");
		sql.append("                       D.DEALER_NAME /*经销商名称*/\n");
		sql.append("                  FROM TT_CUSTOMER CR /*客户表*/\n");
		sql.append("                  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON DAS.CTM_ID = CR.CTM_ID /*实销表*/\n");
		sql.append("                  LEFT JOIN TM_VEHICLE CE ON DAS.VEHICLE_ID = CE.VEHICLE_ID /*车辆表*/\n");
		sql.append("                  LEFT JOIN TM_VHCL_MATERIAL_GROUP S ON S.GROUP_ID = CE.SERIES_ID AND S.GROUP_LEVEL = 2\n");
		sql.append("                  LEFT JOIN TM_VHCL_MATERIAL_GROUP M ON M.GROUP_ID = CE.MODEL_ID  AND M.GROUP_LEVEL = 3\n");
		sql.append("                  LEFT JOIN TC_CODE TC ON DAS.CAR_CHARACTOR = TC.CODE_ID AND tc.TYPE = 1083/*tc_code表*/\n");
		sql.append("                  LEFT JOIN TM_REGION PROV ON CR.PROVINCE = PROV.REGION_CODE AND PROV.REGION_TYPE = 10541002 /*省份表*/\n");
		sql.append("                  LEFT JOIN TM_REGION CITY ON CR.CITY = CITY.REGION_CODE AND CITY.REGION_TYPE = 10541003 /*城市表*/\n");
		sql.append("                  LEFT JOIN TC_CODE CT ON CT.CODE_ID = CR.CTM_TYPE\n");
		sql.append("                  LEFT JOIN TM_DEALER D ON D.DEALER_ID = DAS.DEALER_ID"); 
		sql.append("  where 1=1 ");
		
		if(!"".equals(txtBatholithNum))//vin
		{
			sql.append("	AND ce.vin like '%"+txtBatholithNum+"%'");
		}
		if(!"".equals(dealerName))//经销商简称
		{
			sql.append("	AND D.DEALER_NAME like '%"+dealerName+"%'");
		}
		if(!"".equals(customer_type))//客户类型
		{
			sql.append("	AND cr.CTM_TYPE="+customer_type);
		}
		if(!"".equals(txtPurpose))//用途
		{
			sql.append("	AND  das.car_charactor="+txtPurpose);
		}
		if(!"".equals(downtown))//地区
		{
			sql.append("	AND prov.region_code="+downtown);
		}
		if(!"".equals(txtUserLevel))//用户级别
		{
			sql.append("	AND CR.GUEST_STARS="+Integer.parseInt(txtUserLevel));
		}
		if(!"".equals(ddlClasses))//汽车种类
		{
			sql.append("	AND p.SERIES_NAME='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode))//汽车型号
		{
			sql.append("	AND p.MODEL_CODE like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		
		if(!("".equals(checkSDate2))&&(!("".equals(checkEDate2))))
		{
			sql.append(" AND ce.PURCHASED_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND ce.PURCHASED_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate2)))
		{
			sql.append(" AND ce.PURCHASED_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate2)))
		{
			sql.append(" AND ce.PURCHASED_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" AND cr.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND cr.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" AND cr.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" AND cr.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append(" order by d.dealer_code desc, ce.PURCHASED_DATE desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  查询要进行回访的客户
	 * @param      : @param checkSDate2
	 * @param      : @param checkEDate2
	 * @param      : @param checkSDate
	 * @param      : @param checkEDate
	 * @param      : @param ddlAutoTypeCode
	 * @param      : @param ddlClasses
	 * @param      : @param txtUserLevel
	 * @param      : @param downtown
	 * @param      : @param txtPurpose
	 * @param      : @param customer_type
	 * @param      : @param pageSize
	 * @param      : @param curPage
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public PageResult<Map<String, Object>> customerQueryNew(String checkSDate,String checkEDate,String 
			ddlAutoTypeCode,String ddlClasses,String txtUserLevel,String downtown,String txtPurpose,String customer_type,String txtBatholithNum, 
			String dealerName,String roSDate, String roEDate, int pageSize, int curPage)
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.CTM_ID, /*客户ID*/\n");
		sql.append("       T.MAIN_PHONE, /*手机号码*/\n"); 
		sql.append("       T.CTM_NAME, /*客户名称*/\n"); 
		sql.append("       T.GUEST_STARS, /*客户等级*/\n"); 
		sql.append("       PROV.REGION_NAME PROVINCE, /*省份*/\n"); 
		sql.append("       CITY.REGION_NAME CITY, /*城市*/\n"); 
		sql.append("       CT.CODE_DESC CTM_TYPE, /*客户类型*/\n"); 
		sql.append("       TO_CHAR(T.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE, /*购买日期*/\n"); 
		sql.append("       T.SALES_ADDRESS, /*购买用途*/\n"); 
		sql.append("       T.SERIES_NAME, /*车系*/\n"); 
		sql.append("       T.MODEL_CODE, /*车型*/\n"); 
		sql.append("       T.VIN VIN, /*VIN*/\n"); 
		sql.append("       T.DEALER_CODE, /*经销商代码*/\n"); 
		sql.append("       T.DEALER_NAME /*经销商名称*/\n"); 
		sql.append("     FROM TT_CRM_CUSTOMER_CREATE T");
		sql.append("     LEFT JOIN TM_REGION PROV ON t.PROVINCE = PROV.REGION_CODE AND PROV.REGION_TYPE = 10541002 /*省份表*/\n");
		sql.append("     LEFT JOIN TM_REGION CITY ON t.CITY = CITY.REGION_CODE AND CITY.REGION_TYPE = 10541003 /*城市表*/\n");
		sql.append("     LEFT JOIN TC_CODE CT ON CT.CODE_ID = t.CTM_TYPE AND ct.TYPE = 1083 /*客户类型*/\n");
		sql.append("  where 1=1 ");
		
		if(!"".equals(txtBatholithNum))//vin
		{
			sql.append("	AND t.vin like '%"+txtBatholithNum+"%'");
		}
		if(!"".equals(dealerName))//经销商简称
		{
			sql.append("	AND t.DEALER_NAME like '%"+dealerName+"%'");
		}
		if(!"".equals(customer_type))//客户类型
		{
			sql.append("	AND t.CTM_TYPE="+customer_type);
		}
		if(!"".equals(txtPurpose))//用途
		{
			sql.append("	AND  t.car_charactor="+txtPurpose);
		}
		if(!"".equals(downtown))//地区
		{
			sql.append("	AND t.province="+downtown);
		}
		if(!"".equals(txtUserLevel))//用户级别
		{
			sql.append("	AND t.GUEST_STARS="+Integer.parseInt(txtUserLevel));
		}
		if(!"".equals(ddlClasses))//汽车种类
		{
			sql.append("	AND t.SERIES_NAME='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode))//汽车型号
		{
			sql.append("	AND t.MODEL_CODE like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		
		if(!("".equals(checkSDate)))
		{
			sql.append(" AND t.PURCHASED_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkEDate)))
		{
			sql.append(" AND t.PURCHASED_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
//		if(!("".equals(checkSDate)))
//		{
//			sql.append(" AND t.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
//		}
//		if(!("".equals(checkEDate)))
//		{
//			sql.append(" AND t.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
//		}
		if(!("".equals(checkSDate)))
		{
			sql.append(" AND t.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkEDate)))
		{
			sql.append(" AND t.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!"".equals(roSDate) && !"".equals(roEDate)){
			sql.append(" AND EXISTS (SELECT 1 FROM TT_AS_REPAIR_ORDER RO WHERE RO.VIN = T.VIN \n");
			sql.append(" AND ro.ro_CREATE_DATE >="+" to_date('"+roSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS') \n");
			sql.append(" AND ro.ro_CREATE_DATE <="+" to_date('"+roEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')) \n");
		}
		if(!("".equals(roSDate)))
		{
			sql.append(" AND EXISTS (SELECT 1 FROM TT_AS_REPAIR_ORDER RO WHERE RO.VIN = T.VIN \n");
			sql.append(" AND ro.ro_CREATE_DATE >="+" to_date('"+roSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS'))");
		}
		if(!("".equals(roEDate)))
		{
			sql.append(" AND EXISTS (SELECT 1 FROM TT_AS_REPAIR_ORDER RO WHERE RO.VIN = T.VIN \n");
			sql.append(" AND ro.ro_CREATE_DATE <="+" to_date('"+roEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS'))");
		}
		sql.append(" ORDER BY T.SR, T.CTM_ID DESC ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;		
	}
	
	public List<Map<String, Object>> radomSel(String txtRand,String checkSDate2,String checkEDate2,String checkSDate,String checkEDate,String ddlAutoTypeCode,String ddlClasses,String txtUserLevel,String downtown,String txtPurpose,String customer_type,String txtBatholithNum, int pageSize, int curPage)
	{
		List<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(
				"select * from (select cr.ctm_id,    /*客户ID*/\n" +
				"       CR.CTM_NAME, /*客户名称*/\n" + 
				"       CT.CODE_DESC CTM_TYPE, /*客户类型*/\n" + 
				"       tc.code_desc SALES_ADDRESS, /*购买用途*/\n" + 
				"       prov.region_name PROVINCE, /*省份*/\n" + 
				"       city.region_name CITY, /*城市*/\n" + 
				"       CR.GUEST_STARS, /*客户等级*/\n" + 
				"       p.SERIES_NAME, /*车系*/\n" + 
				"       p.MODEL_CODE, /*车型*/\n" + 
				"       ce.VIN vin, /*VIN*/\n" + 
				"       to_char(ce.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE /*购买日期*/\n" + 
				"  from TT_CUSTOMER                         cr /*客户表*/\n" + 
				"       LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.ctm_id = cr.ctm_id /*实销表*/\n" + 
				"       LEFT JOIN tm_vehicle               ce  on das.vehicle_id= ce.vehicle_id /*车辆表*/\n" + 
				"       LEFT JOIN tm_vhcl_material         l on  ce.material_id=l.material_id /*物料表*/\n" + 
				"       LEFT JOIN tm_vhcl_material_group_r r on  l.material_id=r.material_id /*物料组物料关系表*/\n" + 
				"       LEFT JOIN vw_material_group        p on  r.group_id=p.PACKAGE_ID /*物料组表*/\n" + 
				"       LEFT JOIN tc_code                  tc on  das.car_charactor=tc.code_id/*tc_code表*/\n" + 
				"       LEFT JOIN tm_region                prov on cr.province=prov.region_code /*省份表*/\n" + 
				"       LEFT JOIN tm_region                city on cr.city=city.region_code /*城市表*/\n" + 
				"    LEFT JOIN TC_CODE                  CT ON CT.CODE_ID=CR.CTM_TYPE        where 1=1\n" );

	
		
		if(!"".equals(txtBatholithNum))//vin
		{
			sql.append("	AND ce.vin like '%"+txtBatholithNum+"%'");
		}
		if(!"".equals(customer_type))//客户类型
		{
			sql.append("	AND cr.CTM_TYPE="+customer_type);
		}
		if(!"".equals(txtPurpose))//用途
		{
			sql.append("	AND  das.car_charactor="+txtPurpose);
		}
		if(!"".equals(downtown))//地区
		{
			sql.append("	AND prov.region_code="+downtown);
		}
		if(!"".equals(txtUserLevel))//用户级别
		{
			sql.append("	AND CR.GUEST_STARS="+txtUserLevel);
		}
		if(!"".equals(ddlClasses))//汽车种类
		{
			sql.append("	AND p.SERIES_NAME='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode))//汽车型号
		{
			sql.append("	AND p.MODEL_CODE like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		
		if(!("".equals(checkSDate2))&&(!("".equals(checkEDate2))))
		{
			sql.append(" AND ce.PURCHASED_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND ce.PURCHASED_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate2)))
		{
			sql.append(" AND ce.PURCHASED_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate2)))
		{
			sql.append(" AND ce.PURCHASED_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" AND cr.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND cr.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" AND cr.CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" AND cr.CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		sql.append("");
		sql.append("     ORDER BY dbms_random.VALUE\n" + 
		"    )WHERE rownum<="+txtRand);
		result =  pageQuery(sql.toString(), null, getFunName());
		return result;		
	}
	/**
	 * 生成客户回访
	 * @Title      : 
	 * @Description: TODO 
	 * @param      : @param RV_TYPE
	 * @param      : @param Results      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void generateReview(AclUserBean logonUser,String RV_TYPE,String Results)
	{
		//TT_CRM_RETURN_VISIT
		List<Map<String, Object>> ctmlist = getCtms(Results);
		
		if(null!=ctmlist&&ctmlist.size()>0)
		{
			for(Map<String, Object>  m:ctmlist)
			{
				TtCrmReturnVisitPO p = new TtCrmReturnVisitPO();
				Long rvId=Long.parseLong(SequenceManager.getSequence(""));
				p.setRvId(rvId);
				p.setCreateBy(logonUser.getUserId());
				p.setCreateDate(new Date());
				p.setRvCusId(Long.parseLong(m.get("CTM_ID").toString()));
				p.setRvCusName(m.get("CTM_NAME").toString());
				p.setRvPhone(m.get("MAIN_PHONE")==null?"":m.get("MAIN_PHONE").toString());
				p.setRvType(Integer.parseInt(RV_TYPE));
				p.setRvStatus(Constant.RV_STATUS_5);//等设置问卷
				//生成时间 add by chenl 2013-5-3
				p.setRvDate(new Date());
				
				dao.insert(p);
				
			}	
		}				
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 根据客户ID查询客户信息
	 * @param      : @param Results
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public List<Map<String, Object>> getCtms(String Results)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT C.CTM_ID, C.CTM_NAME, C.MAIN_PHONE\n" +
				"  FROM TT_CUSTOMER C\n" + 
				" WHERE C.CTM_ID IN ("+Results+")");
		return  pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 判断同一客户是否有相同的回访
	 * @param result
	 * @param rvType
	 * @return
	 */
	public List<TtCrmReturnVisitPO> checkReview(String result,String rvType){
		StringBuffer sql = new StringBuffer();
		List<TtCrmReturnVisitPO> list=null;
		String date = CommonUtils.printDate(new Date());
		sql.append(" select * from tt_crm_return_visit where 1=1 and rv_type = "+Integer.parseInt(rvType));
		sql.append(" and rv_cus_id in ("+result+") ");
		sql.append(" and rv_date >="+" to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		sql.append(" and rv_date <="+" to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		list = dao.select(TtCrmReturnVisitPO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 随机查询满意度回访
	 * @param pageSize
	 * @param curPage
	 * @param checkSDate
	 * @param checkEDate
	 * @param ddlClasses
	 * @param ddlAutoTypeCode
	 * @param vin
	 * @param chkServiceStationType
	 * @param txtRand随机数
	 * @return
	 */
	public List<Map<String,Object>> satisRandSel(int pageSize,int curPage,String checkSDate,String checkEDate,
			String ddlClasses,String ddlAutoTypeCode,String vin,String chkServiceStationType,String txtRand){
		List<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (SELECT R.ID, /*工单ID*/\n" +
				"R.RO_NO, /*工单号*/\n" +
				"       TC.CODE_DESC AS REPAIR_TYPE_CODE, /*维修类型*/\n" + 
				"       TO_CHAR(R.RO_CREATE_DATE, 'yyyy-MM-dd') RO_CREATE_DATE, /*工单生成日期*/\n" + 
				"       R.OWNER_NAME, /*车主姓名*/\n" + 
				"       R.OWNER_NAME CTM_NAME, /*客户姓名*/\n" + 
				"       DAS.CTM_ID, /*客户ID*/\n" + 
				"       R.VIN, /*VIN*/\n" + 
				"       R.IN_MILEAGE TOTAL_MILEAGE, /*总量程数*/\n" + 
				"       R.DEALER_ID, /*经销商代码*/\n" + 
				"       TD.DEALER_NAME, /*经销商全称*/\n" + 
				"       R.SERIES SERIES_NAME, /*车系*/\n" + 
				"       R.MODEL MODEL_CODE /*车型*/\n" + 
				"  FROM TT_AS_REPAIR_ORDER R\n" + 
				"  JOIN TC_CODE TC ON R.REPAIR_TYPE_CODE = TC.CODE_ID AND TC.TYPE = 1144 /*tc_code表*/\n" + 
				"  JOIN TM_VEHICLE V ON R.VIN = V.VIN\n" + 
				"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON V.VEHICLE_ID = DAS.VEHICLE_ID\n" + 
				"  LEFT JOIN TM_DEALER TD ON R.DEALER_ID = TD.DEALER_ID\n" + 
				" WHERE 1 = 1");
		if(!"".equals(vin))
		{
			sql.append("	AND R.VIN LIKE '%"+vin+"%'" );
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))
		{
			sql.append(" AND R.RO_CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" AND R.RO_CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" AND R.RO_CREATE_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" AND R.RO_CREATE_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!"".equals(ddlClasses))//汽车种类
		{
			sql.append("	AND r.SERIES='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode))//汽车型号
		{
			sql.append("	AND r.MODEL like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		if(!"".equals(chkServiceStationType)){//服务站类型
			sql.append(" and td.dealer_class in ("+chkServiceStationType+")");
		}
		sql.append("     ORDER BY dbms_random.VALUE\n" + 
				"    )WHERE rownum<="+txtRand);
		result =  pageQuery(sql.toString(), null, getFunName());
		return result;
		
	}
	/**
	 * 判断客户满意度回访是否存在：同一客户的id，vin
	 * @param result
	 * @param rvType
	 * @param vin
	 * @return
	 */
	public List<TtCrmReturnVisitPO> checkSatisReview(String result,String rvType,String vin){
		StringBuffer sql = new StringBuffer();
		List<TtCrmReturnVisitPO> list=null;
		String date = CommonUtils.printDate(new Date());
		String vins = vin.replaceAll(",", "','");
		sql.append(" select rv.* from tt_crm_return_visit rv  \n");
		sql.append(" where 1=1 and rv.rv_type = "+Integer.parseInt(rvType)+" \n");
		if(result!=null&&result!=""){
			sql.append(" and rv.rv_cus_id in ("+result+") \n");
		}
		if(vins!=null&&vins!=""){
			sql.append(" and rv.vin in ('"+vins+"') \n");
		}
		sql.append(" and rv.rv_date >="+" to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:MI:SS') \n");
		sql.append(" and rv.rv_date <="+" to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		list = dao.select(TtCrmReturnVisitPO.class, sql.toString(), null);
		return list;
	}
	/**
	 * 获取满意度回访客户的信息
	 * @param Results
	 * @param vin
	 * @return
	 */
	public List<Map<String, Object>> getCtmsByVin(String Results,String vin)
	{
		StringBuilder sql = new StringBuilder();
		String vins = vin.replaceAll(",", "','");
		sql.append("SELECT C.CTM_ID, C.CTM_NAME,V.VIN, \n"); 
		sql.append(" case when c.main_phone is null then (case when c.company_phone is null then c.OTHER_PHONE else c.company_phone end) else c.main_phone end main_phone");
		sql.append("  FROM TT_CUSTOMER C\n"); 
		sql.append("  left join TT_DEALER_ACTUAL_SALES DAS ON C.CTM_ID = DAS.CTM_ID \n");
		sql.append(" left join TM_VEHICLE v on v.vehicle_id = das.vehicle_id \n");
		sql.append(" WHERE 1=1 ");
		if(Results!=null&&Results!=""){
			sql.append(" and c.ctm_id in ("+Results+") \n");
		}else{
			sql.append(" and c.ctm_id ='' \n");
		}
		if(vins!=null&&vins!=""){
			sql.append(" and v.vin in ('"+vins+"')");
		}else{
			sql.append(" and v.vin =''");
		}
		return  pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 设置服务站满意度回访
	 * @param logonUser
	 * @param result
	 */
	public void generateSatisReview(AclUserBean logonUser,List<Map<String, Object>> ctmlist,String rvType){
		if(null!=ctmlist&&ctmlist.size()>0){
			for(Map<String, Object>  m:ctmlist){
				TtCrmReturnVisitPO p = new TtCrmReturnVisitPO();
				Long rvId=Long.parseLong(SequenceManager.getSequence(""));
				p.setRvId(rvId);
				p.setCreateBy(logonUser.getUserId());
				p.setCreateDate(new Date());
				p.setRvCusId(Long.parseLong(m.get("CTM_ID").toString()));
				p.setRvCusName(m.get("CTM_NAME").toString());
				p.setRvPhone(m.get("MAIN_PHONE")==null?"":m.get("MAIN_PHONE").toString());
				p.setRvType(Integer.parseInt(rvType));//服务站满意度回访
				p.setRvStatus(Constant.RV_STATUS_5);//等设置问卷
				p.setRvDate(new Date());
				p.setVin(m.get("VIN").toString());
				
				dao.insert(p);
			}	
		}		
	}
	
	/**
	 * 回访问卷导出--查询
	 * @param logonUser
	 * @param pageSize
	 * @param curPage
	 * @param RV_CUS_NAME
	 * @param RV_TYPE
	 * @param checkSDate生成日期
	 * @param checkEDate
	 * @param checkSDate2回访日期
	 * @param checkEDate2
	 * @param RD_IS_ACCEPT回访结果
	 * @param TELEPHONE
	 * @param QR_ID
	 * @param RV_ASS_USER
	 * @param chkIsEmptyQuest
	 * @param ddlAutoTypeCode
	 * @param ddlClasses
	 * @return
	 */
	public PageResult<Map<String, Object>> reviewExportSearch(AclUserBean logonUser,int pageSize,int curPage,String RV_CUS_NAME,String RV_TYPE,String checkSDate,String checkEDate,
			String checkSDate2,String checkEDate2,String RD_IS_ACCEPT,String TELEPHONE,String QR_ID,String RV_ASS_USER,String chkIsEmptyQuest,String ddlAutoTypeCode,String ddlClasses,List<Map<String,Object>>questions){
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
			sql.append(	
					"SELECT VS.RV_ID,/*客户回访ID*/\n" +
					"       VS.RV_CUS_NAME,/*客户名称*/\n" + 
					"       TC.CODE_DESC RV_TYPE,/*回访类型*/\n" + 
					"       VS.RV_RESULT,/*回访结果*/\n" + 
					//"       vr.RD_IS_ACCEPT rv_result,/*回访结果*/\n" +
					//"       VS.RV_PHONE,/*回访电话*/\n" + 
					"		case when c.main_phone is null then (case when c.company_phone is null then c.OTHER_PHONE else c.company_phone end) else c.main_phone end phone,/*回访电话*/\n"+
					//"       TO_CHAR(VS.RV_DATE, 'yyyy-MM-dd') RV_DATE,/*生成时间*/\n" + 
					"		TO_CHAR(v.PURCHASED_DATE,'yyyy-MM-dd') PURCHASED_DATE,/*购买日期*/\n"+
					"       VS.RV_ASS_USER,/*回访人*/\n" +
					"       p.SERIES_NAME, /*车系*/\n" + 
					"       p.MODEL_CODE, /*车型*/\n" + 
					"       prov.region_name PROVINCE, /*省份*/\n" + 
					"       vr.rd_content, /*备注*/\n" +
					"       v.VIN vin /*VIN*/\n" );
			if(questions!=null && questions.size()>0){
				for(int i=0;i<questions.size();i++){
					sql.append("		,ta.q"+i+"\n");
				}
			}
				sql.append("  FROM TT_CRM_RETURN_VISIT VS/**/\n" + 
					"  LEFT JOIN TT_CUSTOMER C ON VS.RV_CUS_ID = C.CTM_ID /* 客户表*/\n" + 
					"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON C.CTM_ID = DAS.CTM_ID /*实销表*/\n" + 
					"  LEFT JOIN TM_VEHICLE V ON DAS.VEHICLE_ID = V.VEHICLE_ID/*车辆表*/\n" + 
					"  LEFT JOIN TM_VHCL_MATERIAL L ON V.MATERIAL_ID = L.MATERIAL_ID/*物料表*/\n" + 
					"  LEFT JOIN TM_VHCL_MATERIAL_GROUP_R R ON L.MATERIAL_ID = R.MATERIAL_ID/*物料-物料组关系表*/\n" + 
					"  LEFT JOIN VW_MATERIAL_GROUP P ON P.PACKAGE_ID = R.GROUP_ID/*物料组表*/\n" + 
					"  LEFT JOIN tm_region  prov on c.province=prov.region_code /*省份表*/\n" + 
					"  left join TT_CRM_RETURN_VISIT_RECORD vr on vs.rv_id = vr.rv_id /*回访明细表*/"+
					"  JOIN TC_CODE TC ON TC.CODE_ID = VS.RV_TYPE/* 回访类型*/\n");
		if(questions!=null && questions.size()>0){
			sql.append(" left join ( select rv_id");
			for(int i=0;i<questions.size();i++){
				int a = Integer.parseInt(questions.get(i).get("QD_NO").toString());
				sql.append(" ,max(decode(qd_no,"+a+",qd_que_answer,null)) q"+i+" \n");
			}
			sql.append(" from TT_CRM_QUE_ANSWER group by rv_id) ta on ta.rv_id = vs.rv_id /*问卷答案*/\n");
		}
			sql.append(" where 1=1 ");
		if(!("".equals(RV_CUS_NAME))){
			sql.append(" and vs.RV_CUS_NAME like '%"+RV_CUS_NAME+"%'");
		}
		if(!("".equals(RV_TYPE))){
			sql.append(" and vs.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(ddlClasses))){
			sql.append(" and p.SERIES_NAME ='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode)){//汽车型号
			sql.append(" AND p.MODEL_CODE like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		if(!("".equals(RD_IS_ACCEPT))){
			sql.append(" and vr.RD_IS_ACCEPT = "+RD_IS_ACCEPT+"");
		}
		if(!("".equals(TELEPHONE))){
			sql.append(" and vs.RV_PHONE like '%"+TELEPHONE+"%' ");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and vs.RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))//生成日期
		{
			sql.append(" and vs.RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and vs.RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and vs.RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and vs.RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkSDate2))&&(!("".equals(checkEDate2))))//回访日期
		{
			sql.append(" and VR.RD_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and VR.RD_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate2)))
		{
			sql.append(" and VR.RD_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate2)))
		{
			sql.append(" and VR.RD_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(chkIsEmptyQuest))){//只显示空问卷：问卷没有作答--rv_id 不在 答案表中
			sql.append(" and vs.rv_id not in (select distinct rv_id from tt_crm_que_answer where qr_id = "+QR_ID+" )");
		}
		if(!("".equals(QR_ID))){
			sql.append(" and vs.QR_ID ="+QR_ID+"");
		}
		
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		sql.append("");
		return result;
	}
	/**
	 * 根据qr_id查询问题
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getQuestionById(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT qd_no,QD_QUESTION FROM TT_CRM_QUE_DETAIL WHERE QR_ID = "+id+" and qd_status = "+Constant.STATUS_ENABLE+" order by qd_no");
		List<Map<String,Object>> map = pageQuery(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * 问卷回访导出--导出查询
	 * @param RV_CUS_NAME
	 * @param RV_TYPE
	 * @param checkSDate
	 * @param checkEDate
	 * @param checkSDate2
	 * @param checkEDate2
	 * @param RD_IS_ACCEPT
	 * @param TELEPHONE
	 * @param QR_ID
	 * @param RV_ASS_USER
	 * @param chkIsEmptyQuest
	 * @param ddlAutoTypeCode
	 * @param ddlClasses
	 * @return
	 */
	public List<Map<String,Object>> queryReviewInfo(String RV_CUS_NAME,String RV_TYPE,String checkSDate,String checkEDate,
			String checkSDate2,String checkEDate2,String RD_IS_ACCEPT,String TELEPHONE,String QR_ID,String RV_ASS_USER,String chkIsEmptyQuest,String ddlAutoTypeCode,String ddlClasses,List<Map<String,Object>>questions){
		StringBuffer sql = new StringBuffer();
		sql.append(	
				"SELECT VS.RV_ID,/*客户回访ID*/\n" +
				"       VS.RV_CUS_NAME,/*客户名称*/\n" + 
				"       TC.CODE_DESC RV_TYPE,/*回访类型*/\n" + 
				//"       VS.RV_RESULT,/*回访结果*/\n" + 
				"       (select code_desc from tc_code where code_id = vs.rv_result) as rv_result,/*回访结果*/\n" +
				//"       VS.RV_PHONE,/*回访电话*/\n" + 
				"		case when c.main_phone is null then (case when c.company_phone is null then c.OTHER_PHONE else c.company_phone end) else c.main_phone end phone,/*回访电话*/\n"+
				"		TO_CHAR(v.PURCHASED_DATE,'yyyy-MM-dd') PURCHASED_DATE,/*购买日期*/\n"+
				"       VS.RV_ASS_USER,/*回访人*/\n" +
				"       p.SERIES_NAME, /*车系*/\n" + 
				"       p.MODEL_CODE, /*车型*/\n" + 
				"       prov.region_name PROVINCE, /*省份*/\n" + 
				"       vr.rd_content, /*备注*/\n" +
				"       v.VIN vin /*VIN*/\n");
		if(questions!=null && questions.size()>0){
			for(int i=0;i<questions.size();i++){
				sql.append("		,ta.q"+i+"\n");
			}
		}
			sql.append("  FROM TT_CRM_RETURN_VISIT VS/**/\n" + 
				"  LEFT JOIN TT_CUSTOMER C ON VS.RV_CUS_ID = C.CTM_ID /* 客户表*/\n" + 
				"  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON C.CTM_ID = DAS.CTM_ID /*实销表*/\n" + 
				"  LEFT JOIN TM_VEHICLE V ON DAS.VEHICLE_ID = V.VEHICLE_ID/*车辆表*/\n" + 
				"  LEFT JOIN TM_VHCL_MATERIAL L ON V.MATERIAL_ID = L.MATERIAL_ID/*物料表*/\n" + 
				"  LEFT JOIN TM_VHCL_MATERIAL_GROUP_R R ON L.MATERIAL_ID = R.MATERIAL_ID/*物料-物料组关系表*/\n" + 
				"  LEFT JOIN VW_MATERIAL_GROUP P ON P.PACKAGE_ID = R.GROUP_ID/*物料组表*/\n" + 
				"  LEFT JOIN tm_region  prov on c.province=prov.region_code /*省份表*/\n" + 
				"  left join TT_CRM_RETURN_VISIT_RECORD vr on vs.rv_id = vr.rv_id /*回访明细表*/"+
				"  JOIN TC_CODE TC ON TC.CODE_ID = VS.RV_TYPE/* 回访类型*/\n"); 
		if(questions!=null && questions.size()>0){
			sql.append(" left join ( select rv_id");
			for(int i=0;i<questions.size();i++){
				int a = Integer.parseInt(questions.get(i).get("QD_NO").toString());
				sql.append(" ,max(decode(qd_no,"+a+",qd_que_answer,null)) q"+i+" \n");
			}
			sql.append(" from TT_CRM_QUE_ANSWER group by rv_id) ta on ta.rv_id = vs.rv_id /*问卷答案*/\n");
		}
			sql.append(" where 1=1 ");
		if(!("".equals(RV_CUS_NAME))){
			sql.append(" and vs.RV_CUS_NAME like '%"+RV_CUS_NAME+"%'");
		}
		if(!("".equals(RV_TYPE))){
			sql.append(" and vs.RV_TYPE = "+RV_TYPE+"");
		}
		if(!("".equals(ddlClasses))){
			sql.append(" and p.SERIES_NAME ='"+ddlClasses+"'");
		}
		if(!"".equals(ddlAutoTypeCode)){//汽车型号
			sql.append(" AND p.MODEL_CODE like '%"+ddlAutoTypeCode.toUpperCase()+"%'");
		}
		if(!("".equals(RD_IS_ACCEPT))){
			sql.append(" and vr.RD_IS_ACCEPT = "+RD_IS_ACCEPT+"");
		}
		if(!("".equals(TELEPHONE))){
			sql.append(" and vs.RV_PHONE like '%"+TELEPHONE+"%' ");
		}
		if(!("".equals(RV_ASS_USER))){
			sql.append(" and vs.RV_ASS_USER like '%"+RV_ASS_USER+"%'");
		}
		if(!("".equals(checkSDate))&&(!("".equals(checkEDate))))//生成日期
		{
			sql.append(" and vs.RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and vs.RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate)))
		{
			sql.append(" and vs.RV_DATE >="+" to_date('"+checkSDate+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate)))
		{
			sql.append(" and vs.RV_DATE <="+" to_date('"+checkEDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(checkSDate2))&&(!("".equals(checkEDate2))))//回访日期
		{
			sql.append(" and VR.RD_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
			sql.append(" and VR.RD_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkSDate2)))
		{
			sql.append(" and VR.RD_DATE >="+" to_date('"+checkSDate2+" 00:00:00','yyyy-mm-dd HH24:MI:SS')");
		}else if(!("".equals(checkEDate2)))
		{
			sql.append(" and VR.RD_DATE <="+" to_date('"+checkEDate2+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		if(!("".equals(chkIsEmptyQuest))){//只显示空问卷：问卷没有作答--rv_id 不在 答案表中
			sql.append(" and vs.rv_id not in (select distinct rv_id from tt_crm_que_answer where qr_id = "+QR_ID+" )");
		}
		if(!("".equals(QR_ID))){
			sql.append(" and vs.QR_ID ="+QR_ID+"");
		}
		List<Map<String,Object>> result = pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 查询工单相关信息
	 * @param rv_id
	 * @return
	 */
	public LinkedList<Map<String,Object>>getRoInfo(String rv_id){
		LinkedList<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT R.ID, /*工单ID*/\n" +
				"       R.RO_NO, /*工单号*/\n" + 
				"       TC.CODE_DESC AS REPAIR_TYPE_CODE, /*维修类型*/\n" + 
				"       TO_CHAR(R.RO_CREATE_DATE,'yyyy-MM-dd') RO_CREATE_DATE, /*工单生成日期*/\n" + 
				"       R.OWNER_NAME, /*车主姓名*/\n" + 
				"       R.VIN, /*VIN*/\n" + 
				"       R.TOTAL_MILEAGE, /*总量程数*/\n" + 
				"       R.DEALER_ID, /*经销商代码*/\n" + 
				"       td.DEALER_NAME, /*经销商全称*/\n" + 
				"       p.SERIES_NAME, /*车系*/\n" + 
				"       p.MODEL_NAME /*车型*/\n" + 
				"  FROM TT_CRM_RETURN_VISIT VR\n" + 
				"		left join TT_AS_REPAIR_ORDER	r on vr.RV_CUS_NAME = r.owner_name \n"+
				"       JOIN TC_CODE  TC ON R.REPAIR_TYPE_CODE=TC.CODE_ID \n" +
				"       LEFT JOIN TT_DEALER_ACTUAL_SALES   das on das.ctm_id = vr.RV_CUS_ID /*实销表*/\n" + 
				"       LEFT JOIN tm_vehicle               ce  on das.vehicle_id= ce.vehicle_id /*车辆表*/\n" + 
				"       LEFT JOIN tm_vhcl_material         l on  ce.material_id=l.material_id /*物料表*/\n" + 
				"       LEFT JOIN tm_vhcl_material_group_r g on  l.material_id=g.material_id /*物料组物料关系表*/\n" + 
				"       LEFT JOIN vw_material_group        p on  g.group_id=p.PACKAGE_ID /*物料组表*/\n" + 
				"		left join tm_dealer td on r.dealer_id = td.dealer_id"+
				"  WHERE 1=1 " +
				"		and vr.rv_id = "+rv_id);
		result = (LinkedList<Map<java.lang.String, Object>>) pageQuery(sql.toString(), null, getFunName());
			return result;
	}
	
	public String getTitleLanguage(String cusName, String seatNo) {
		String titleLanguage = "";
		titleLanguage += cusName+" 先生/女士，您好，";
		titleLanguage += "我是 "+Constant.COMPLAY_INFO+seatNo+" 号回访员。";
		return titleLanguage;
	}
		
	
	/**
	 * @FUNCTION :获取下一张回访单
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */	
	public String getNextReviewId(long userId){
		StringBuffer sql = new StringBuffer();
		sql.append("select RV_ID from     \n");
        sql.append("(select RV_ID from TT_CRM_RETURN_VISIT    \n");
        sql.append("where RV_STATUS in("+Constant.RV_STATUS_1+","+Constant.RV_STATUS_2+") and RV_ASS_USER_ID = "+userId+" order by RV_ID)   \n");
        sql.append(" where rownum=1   \n");
        List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null==list||list.size()<=0||list.get(0)==null){
			return null;
		}
		return list.get(0).get("RV_ID").toString();
	}
	
	/**
	 * 热线抽查查询
	 * @param dealerCode
	 * @param dealerName
	 * @param phone
	 * @param result
	 * @param satisfAction
	 * @param beginDate
	 * @param endDate
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult queryPageHotSpot(String dealerCode,String dealerName,String phone,
			String result,String satisfAction,String beginDate,String endDate,Integer pageSize,Integer curPage){
		StringBuffer sql = new StringBuffer("" +
				"select A.*,B.DEALER_NAME FROM TT_CRM_HOT_SPOT A,TM_DEALER B WHERE "+
				"A.DEALER_ID = B.DEALER_ID(+)");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(dealerCode)) {
			sql.append(" AND B.DEALER_CODE LIKE ?");
			params.add(dealerCode);
		}
		if (!XHBUtil.IsNull(dealerName)) {
			sql.append(" AND B.DEALER_NAME LIKE ?");
			params.add(dealerName);
		}
		if (!XHBUtil.IsNull(phone)) {
			sql.append(" AND A.PHONE LIKE ?");
			params.add(phone);
		}
		if (!XHBUtil.IsNull(result)) {
			sql.append(" AND A.SPOT_RESULT = ?");
			params.add(result);
		}
		if (!XHBUtil.IsNull(satisfAction)) {
			sql.append(" AND A.SPOT_SATISFACTION = ?");
			params.add(satisfAction);
		}
		if (!XHBUtil.IsNull(beginDate)) {
			sql.append(" AND A.SPOT_DATE >= TO_DATE(?,'yyyy-mm-dd')");
			params.add(beginDate);
		}
		if (!XHBUtil.IsNull(endDate)) {
			sql.append(" AND A.SPOT_DATE <= TO_DATE(?,'yyyy-mm-dd')");
			params.add(endDate);
		}
		return pageQuery(sql.toString(), params, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 根据客户ID和问卷ID查询回访信息
	 * @param cusId
	 * @param qrId
	 * @return
	 */
	public List<Map<String, Object>> queryByCusIdAndQrId(String cusId,String qrId){
		StringBuffer sql = new StringBuffer("select * from tt_crm_return_visit where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(cusId)) {
			sql.append(" and rv_cus_id = ?");
			params.add(cusId);
		}
		if (!XHBUtil.IsNull(qrId)) {
			sql.append(" and qr_id = ?");
			params.add(qrId);
		}
		return pageQuery(sql.toString(), params, this.getFunName());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
