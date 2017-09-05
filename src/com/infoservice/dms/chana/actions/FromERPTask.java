package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infoservice.dms.chana.dao.DeFromERPDao;
import com.infoservice.dms.chana.po.XxdmsVinCodePO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
 * @ClassName: FromERPTask 
 * @Description: TODO(ERP供应商车辆信息接收计划任务) 
 * @author yangheng 
 * @date Nov 17, 2010 4:18:47 PM 
 *
 */
public class FromERPTask extends AbstractSendTask {

	private final Logger LOG = Logger.getLogger(FromERPTask.class);

	private DeFromERPDao dao = DeFromERPDao.getInstance();

	public static String chongqing_code = "11311001";

	public static String hebei_code = "11311002";

	public static String nanjing_code = "11311003";
	
	public static String changhe_code = "11311004";

	public static void main(String[] args) {
		ContextUtil.loadConf();

		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);

		FromERPTask pt = new FromERPTask();
		try {

			pt.handleExecute();

		} catch (Exception e) {

			e.printStackTrace();
		}
		POContext.endTxn(true);
	}

	@Override
	protected String handleExecute() throws Exception {

		LOG.info("FromERPTask receive starting...");

		Set<String> fromerpVins = new HashSet<String>();//存储更新或插入成功的ERP

		List<XxdmsVinCodePO> pos = queryfromerpData(); //查询ERP接口表

		if (null != pos && pos.size() > 0) {

			updateTmVehicle(pos, fromerpVins); //ERP商信息插入或更新到表中

			if (fromerpVins.size() > 0) {

				//delFromERP(fromerpVins); //ERP商插入完毕,将接口表的数据删除
				updateFromERP(fromerpVins); //ERP商插入完毕,将接口表的数据发送状态更新
			}
		}
		LOG.info("FromERPTask receive ending...");
		return null;
	}

	private void delFromERP(Set<String> fromerpVins) {
		if (fromerpVins.size() > 0) {
			for (String vin : fromerpVins) {
				dao.delFromERPByvin(vin);
			}		
		}
	}
	
	private void updateFromERP(Set<String> fromerpVins) {
		if (fromerpVins.size() > 0) {
			for (String vin : fromerpVins) {				 
				 //表示状态为已更新
				String sql = " UPDATE Xxdms_Vin_Code SET Organization_Id = Organization_Id+10000 WHERE 1=1  AND Vin_Code=? AND Organization_Id < 10000";
				List params = new ArrayList();
				params.add(vin);
				int i = dao.update(sql, params);
				if(1==i){
					LOG.info("更新接口表XxdmsVinCode成功!"+vin);
				}else {
				    LOG.info("更新接口表XxdmsVinCode失败!"+vin);

				}
			}		
		}
	}

	private List<XxdmsVinCodePO> queryfromerpData() {
		
		List<XxdmsVinCodePO> pos = dao.queryfromerpData();
		
		return pos;
	}

	@SuppressWarnings("unchecked")
	private void updateTmVehicle(List<XxdmsVinCodePO> pos,Set<String> fromerpVins) {

		for (XxdmsVinCodePO po : pos) {
			try {
				/*Map<String, Object> tvp = dao.selectTmVehicle(po.getVinCode());				
				if( null == tvp) {				
					StringBuffer sql= new StringBuffer();
					sql.append("update XXDMS_VIN_CODE xvc set xvc.ORGANIZATION_ID = xvc.ORGANIZATION_ID + 5000");
					sql.append(" WHERE ORGANIZATION_ID < 5000 ");
					sql.append(" AND xvc.vin_code = '").append(po.getVinCode()).append("'");
					dao.update(sql.toString(), null) ;
					continue;					
				}*/
                    
				/*	String sql1 = " UPDATE Tm_Vehicle SET Engine_No=? WHERE 1=1  AND Vin=? "; //更新发动机号
					List params1 = new ArrayList();
					params1.add(po.getEngineCode());
					params1.add(po.getVinCode());					
                    	
					int i = dao.update(sql1, params1);
					
					if(1 == i){
						LOG.info("fromERP更新发动机号成功:"+po.getEngineCode());
						fromerpVins.add(po.getVinCode());
					 }else{
						LOG.info("fromERP更新发动机号失败:"+po.getEngineCode());
					 }
					*/
					String sql2 = " UPDATE Tm_Vehicle SET YIELDLY=?,Engine_No=?,PRODUCT_DATE=?,HEGEZHENG_CODE=? WHERE 1=1  AND Vin=? "; //更新产地,发动机号,生产日期,合格证代码
					List params2 = new ArrayList();							

					if (84 == (po.getOrganizationId())) {
						params2.add(chongqing_code);
						
					} else if (176 ==(po.getOrganizationId())) {
						params2.add(chongqing_code);
						

					} else if (161 ==(po.getOrganizationId())) {
						params2.add(hebei_code);
						
					} else if (198 ==(po.getOrganizationId())) {
						params2.add(nanjing_code);
						
					}else if (1098 ==(po.getOrganizationId())) {
						params2.add(changhe_code);
						
					}else if(1126 ==(po.getOrganizationId()) || 1221 ==(po.getOrganizationId())){
						
					  params2.add(chongqing_code);
					}
					 if(null != po.getEngineCode()){
					    params2.add(po.getEngineCode());
					  }else {
						  params2.add("");  
					  }
					 if(null !=po.getHgDate()){
						 params2.add(po.getHgDate()); 
					 }else {
						 params2.add("");
					 }
					 if(null != po.getHegezhengCode() ){
						  params2.add(po.getHegezhengCode());//YH 2011.10.25
					 }else {
						  params2.add("");
					 }									
					  params2.add(po.getVinCode());	
									 			
					int i2 = dao.update(sql2, params2);
					
					if(1 == i2){
						LOG.info("fromERP更新生产地:"+po.getOrganizationId()+"发动机号:"+po.getEngineCode()+"生产日期:"+po.getHgDate()+"合格证代码成功:"+po.getHegezhengCode());
						fromerpVins.add(po.getVinCode());
					  }else{
						LOG.info("fromERP更新生产地:"+po.getOrganizationId()+"发动机号:"+po.getEngineCode()+"生产日期:"+po.getHgDate()+"合格证代码成功:"+po.getHegezhengCode());  
					  }				
					
					/*String sql3 = " UPDATE Tm_Vehicle SET PRODUCT_DATE=?,HEGEZHENG_CODE=? WHERE 1=1  AND Vin=? "; //更新生产日期
					List params3 = new ArrayList();	
					params3.add(po.getHgDate());
					params3.add(po.getHegezhengCode()); //YH 2011.10.25
					params3.add(po.getVinCode());				
					int i3 = dao.update(sql3, params3);					
					if(1 == i3){
						LOG.info("fromERP更新生产日期,合格证代码成功:"+po.getHgDate());
						fromerpVins.add(po.getVinCode());
				    }else {
				    	LOG.info("fromERP更新生产日期,合格证代码失败:"+po.getHgDate()); 
				    }*/
				
			} catch (Exception e) {
				
				LOG.error("FromERPTask receive fail... " + po.getVinCode(), e);
				e.printStackTrace();
			}
						
		}
	}
	
}
