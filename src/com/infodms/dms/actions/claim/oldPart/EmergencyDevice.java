package com.infodms.dms.actions.claim.oldPart;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.oldPart.EmergencyDeviceDao;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsPartBorrowSubclassPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.po3.bean.PageResult;

public class EmergencyDevice extends BaseAction {
	
	private final EmergencyDeviceDao dao = EmergencyDeviceDao.getInstance();
	private PageResult<Map<String, Object>> list=null;
	
	/**
	 * 紧急调件显示页面跳转
	 */
	public void listShow(){
		this.sendUrl("listShow");
	}
	/**
	 * 展示首页
	 */
	public void listShowDataView(){
		list=dao.listShowDataView(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 * 选择紧急件查询
	 */
	public void listShowData(){
		list=dao.listShowData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 * 查询出所有的回运件
	 */
	@SuppressWarnings("unchecked")
	public void setDataShow(){
		TtAsPartBorrowSubclassPO po = new TtAsPartBorrowSubclassPO();
		String id = DaoFactory.getParam(request, "id");
		po.setParentId(BaseUtils.ConvertLong(id));
		List<TtAsPartBorrowSubclassPO> list=dao.select(po);
		act.setOutData("borrowSubclassList", list);
	}
	/**
	 * 选择紧急回运单
	 */
	public void showListData(){
		String dealer_id = DaoFactory.getParam(request, "dealer_id");
		list=dao.listShowData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
		act.setOutData("dealer_id", dealer_id);
		this.sendUrl("showListData");
	}
	/**
	 * 紧急调件显示页面跳转
	 */
	public void newAdd(){
		String name = super.loginUser.getName();
		String chooseType = DaoFactory.getParam(request, "chooseType");
		request.setAttribute("chooseType",  chooseType);
//		request.setAttribute("name", name);
		request.setAttribute("login_user", name);
		this.sendUrl("newAdd");
	}
	/**
	 * 紧急调件显示页面跳转
	 */
	public void showAppList(){
		String dealer_id = DaoFactory.getParam(request, "dealer_id");
		request.setAttribute("dealer_id", dealer_id);
		this.sendUrl("showAppList");
	}

	public void showCarType(){
		this.sendUrl("showCarType");
	}
	public void showCarRebate(){
		this.sendUrl("showCarRebate");
	}
	
	public void queryCarType(){
		list=dao.listMeterialGroup(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps",list);
	}
	
	
	public void showAppListData(){
		list=dao.showAppListData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	public void saveOrUpdare(){
		int res = dao.saveOrUpdare(request,loginUser);
		if(res==1){
			this.sendUrl("listShow");
		}else{
			this.sendUrl("listShowErrer");
		}
	}
	@SuppressWarnings("unchecked")
	public void forward(){
		String type = DaoFactory.getParam(request, "type");
		String id = DaoFactory.getParam(request, "id");
		request.setAttribute("type", type);
		int res=1;
		try {
			Long pkid = BaseUtils.ConvertLong(id);
			if(type.equals("2")){
				TtAsPartBorrowSubclassPO p=new TtAsPartBorrowSubclassPO();
				p.setParentId(pkid);
				List<TtAsPartBorrowSubclassPO> select = dao.select(p);
				for (TtAsPartBorrowSubclassPO tt : select) {
					TtAsWrApplicationPO app1=new TtAsWrApplicationPO();
					app1.setClaimNo(tt.getClaimNo());
					TtAsWrApplicationPO app2=new TtAsWrApplicationPO();
					app2.setUrgent(0);//加紧急调件标识
					dao.update(app1, app2);
				}
				TtAsPartBorrowPO po1=new TtAsPartBorrowPO();
				po1.setId(pkid);
				dao.delete(po1);
			}else{
				TtAsPartBorrowPO po=new TtAsPartBorrowPO();
				po.setId(pkid);
				po=(TtAsPartBorrowPO) dao.select(po).get(0);
				request.setAttribute("chooseType", 1);
				TtAsPartBorrowSubclassPO pp=new TtAsPartBorrowSubclassPO();
				pp.setParentId(pkid);
				List<TtAsPartBorrowSubclassPO> borrowSubclassList=dao.select(pp);
				act.setOutData("po", po);
				act.setOutData("borrowSubclassList", borrowSubclassList);
				this.sendUrl("newAdd");
			}
			
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			setJsonSuccByres(res);
		}
	}
	public void listShowAll(){
		this.sendUrl("listShowAll");
	}
	public void listShowAllData(){
		list=dao.listShowAllData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	/**
	 *  公共的跳转页面
	 * @param jsp
	 */
	private void sendUrl(String jsp) {
		String sendUrl = super.sendUrl(EmergencyDevice.class,jsp);
		super.sendMsgByUrl(sendUrl, "紧急调件");
	}
	
}
