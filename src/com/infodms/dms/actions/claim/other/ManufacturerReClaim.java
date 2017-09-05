/**********************************************************************
* <pre>
* FILE : ServiceActivityManageReclaim.java
* CLASS : ServiceActivityManageReclaim
*
* AUTHOR : PGM
*
* FUNCTION :其他---生产商再索赔
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-13| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ManufacturerReClaim.java,v 1.1 2010/08/16 01:44:50 yuch Exp $
 */
package com.infodms.dms.actions.claim.other;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.other.ManufacturerReClaimDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  生产商再索赔
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ManufacturerReClaim {
	private Logger logger = Logger.getLogger(ManufacturerReClaim.class);
	private ManufacturerReClaimDao dao = ManufacturerReClaimDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ManufacturerReClaimInitUrl = "/jsp/claim/other/manufacturerReClaim.jsp";//查询页面
	/**
	 * Function       :  生产商再索赔页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageReclaimInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void manufacturerReClaimInit(){
		try {
			act.setForword(ManufacturerReClaimInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生产商再索赔页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询生产商再索赔---结算费用清单中符合条件的信息
	 * @param         :  request-活动编号、活动开始日期、活动结束日期
	 * @return        :  生产商再索赔
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void  serviceActivityManageReclaimQuery(){
		try {
			RequestWrapper request = act.getRequest();
					
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);	//从session中取得车厂公司id	
			String producerCode = request.getParamValue("producerCode");    //生产商代码
			String producerName = request.getParamValue("producerName");    //生产商名称
			String startdate = request.getParamValue("startDate");          //活动开始日期
			String enddate = request.getParamValue("endDate");              //活动结束日期
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getManufacturerReClaimQuery(producerCode,producerName,startdate,enddate,String.valueOf(oemCompanyId),curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ManufacturerReClaimInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--- 生产商再索赔");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void exportExcel(){
		try {
		RequestWrapper request = act.getRequest();
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);	//从session中取得车厂公司id
		String startdate = request.getParamValue("startDate");         	//活动开始日期
		String enddate = request.getParamValue("endDate");             	//活动结束日期
		String producerCode = request.getParamValue("producerCode");    //生产商代码
		String producerName = request.getParamValue("producerName");    //生产商名称
		String[] head=new String[14];
		head[0]="生产商代码";
		head[1]="生产商名称";
		head[2]="申请单号";
		head[3]="配件代码";
		head[4]="配件名称";
		head[5]="VIN";
		head[6]="车型";
		head[7]="购车日期";
		head[8]="行驶里程";
		head[9]="索赔类型";
		head[10]="工时费";
		head[11]="配件金额";
		head[12]="其它金额";
		head[13]="总金额";
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps = dao.getManufacturerReClaimQuery(producerCode,producerName,startdate,enddate,String.valueOf(oemCompanyId),curPage,Constant.PAGE_SIZE );
		List<Map<String, Object>> list= ps.getRecords(); 
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
				String[]detail=new String[14];
				detail[0]=(String) map.get("PRODUCER_CODE");
				detail[1]=(String) map.get("PRODUCER_NAME");
				detail[2]=(String) map.get("RO_NO");
				detail[3]=(String) map.get("PART_CODE");
				detail[4]=(String) map.get("PART_NAME");
				detail[5]=(String) map.get("VIN");
				detail[6]=(String) map.get("GROUP_NAME");
				detail[7]=(String) map.get("RO_STARTDATE");
				detail[8]=(String) map.get("IN_MILEAGE").toString();
				detail[9]=(String) map.get("CLAIM_TYPE").toString();
				detail[10]=(String) map.get("LABOUR_AMOUNT").toString();
				detail[11]=(String) map.get("PARTS_AMOUNT").toString();
				detail[12]=(String) map.get("OTHERITEM_AMOUNT").toString();
				detail[13]=(String) map.get("GROSS_CREDIT").toString();
				list1.add(detail);
		      }
		com.infodms.dms.actions.claim.serviceActivity.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	    act.setForword(ManufacturerReClaimInitUrl);	
		//act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"其他功能--- 生产商再索赔导出数据");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
}