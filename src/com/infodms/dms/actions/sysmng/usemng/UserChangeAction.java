package com.infodms.dms.actions.sysmng.usemng;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.usermng.UserChangeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: UserChangeAction
 * @Description: TODO(用户切换)
 * @author yangheng
 * @date Nov 17, 2010 6:11:34 PM
 * 
 */
public class UserChangeAction implements Constant {
	public Logger logger = Logger.getLogger(UserChangeAction.class);
	private static final UserChangeDao dao = new UserChangeDao();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();

	/**
	 * 
	 * @Title: modForward
	 * @Description: TODO(跳转到修改页面)
	 * @return void 返回类型
	 * @throws
	 */

	@SuppressWarnings("static-access")
	public void queryOrder() {

		try {
			String selectType = request.getParamValue("selectType");// 判断是部门用户还是公司用户
			PageResult<TcUserPO> ps = null;
			// 部门ID
			String orgId = request.getParamValue("DEPT_ID");
			String acnt = request.getParamValue("ACNT");
			String name = request.getParamValue("NAME");
			String orderName = request.getParamValue("orderCol");
			String da = request.getParamValue("order");
			String companyId = request.getParamValue("COMPANY_ID");
			String companyName = request.getParamValue("companyName"); //得到公司名称
			Long oemCompanyId = logonUser.getCompanyId();
			String poseName = request.getParamValue("poseName") ;

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if (Utility.testString(selectType)) {
				if ("1".equals(selectType)) { // 部门车厂用户
					ps = dao.sgmSysUserQuery(orgId, logonUser.getCompanyId(),poseName,
							acnt, name, Constant.PAGE_SIZE, curPage, orderName,
							da);
				} else if (selectType.equals("2")) { // 公司经销商用户
					ps = dao.sgmDrlSysUserQuery(companyId, oemCompanyId, orgId,companyName,
							acnt, name, Constant.PAGE_SIZE, curPage, orderName,da);
				} else {
					System.out.println("没有选择查询方式");
					return;
				}
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.FAILURE_CODE, "用户切换失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
