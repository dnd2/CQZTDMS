package com.infodms.dms.util;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 公用类,验证用户操作是否超时操作
 * 
 * @author 韩晓宇 2012-04-28
 */
public class OrderCode {

	private Logger logger = Logger.getLogger(OrderCode.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();

	/**
	 * 验证是否是当前用户操作,避免同台机器登陆不同用户时出现后权限错乱问题
	 */

	/**
	 * 补充订单NO
	 */
	public static String getDLVRY_CG(Long fundType) {
		String no = "1" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}

	public static String getDLVRY_BC(Long fundType) {
		String no = "2" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}

	public static String getDLVRY_TS(Long fundType) {
		String no = "3" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}

	public static String get_TC(Long fundType) {
		String no = "4" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}

	public static String get_DB(Long fundType) {
		String no = "5" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}
	//电商订单
	public static String getDLVRY_DS(Long fundType) {
		String no = "6" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}
	public static String getDLVRY_ZG(Long fundType) {
		String no = "D" + acountType(fundType) + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}
	/**
	 *获取宣传物料单号：
	 * @param fundType
	 * @return
	 */
	public static String get_Public() {
		
		String no = "7" + acountType("90") + SequenceManager.getSequence_F_GETID_L6();
		return no;
	}
	private static final OrderReportDao dao = OrderReportDao.getInstance();

	public static String acountType(Long fundType) {
		
		
		TtVsAccountTypePO tvat = new TtVsAccountTypePO();
		tvat.setTypeId(fundType);
		tvat = (TtVsAccountTypePO) dao.select(tvat).iterator().next();
		
		return tvat.getMarkCode();
	}
	public static String acountType(String type_code){
		TtVsAccountTypePO tvat = new TtVsAccountTypePO();
		tvat.setTypeCode(type_code);
		tvat = (TtVsAccountTypePO) dao.select(tvat).iterator().next();
		return tvat.getMarkCode();
	}

}
