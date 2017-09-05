package com.infoservice.dms.chana.actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.infodms.dms.dao.OldLuoTestClassDAO;
import com.infoservice.de.DEException;
import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DefaultParaDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.DefaultParaTempVO;
import com.infoservice.dms.chana.vo.DefaultParaVO;
import com.infoservice.dms.chana.vo.PtAllocationOutVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
/**
 * 
 * @ClassName     : OBJ13 
 * @Description   : 店面系统库存验证下发接口
 * @author        : luole
 * CreateDate     : 2013-7-10
 */
public class OBJ14{
	private final Logger LOG = Logger.getLogger(OBJ14.class);
	private DefaultParaDao dpDao = DefaultParaDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	//直接取数据库的方式
	/*public static void main(String[] args) throws Exception{
		ContextUtil.loadConf();
		DBService ds = DBService.getInstance();
		POContext.beginTxn(ds.getDefTxnManager(), -1);
		DEService.getInstance().init();
		OBJ14 service = new OBJ14();
		OldLuoTestClassDAO dao = OldLuoTestClassDAO.getInstance();
		List<Map<String,Object>> list = dao.getEntityCodes();
		for(Map map :list){
			service.execute((String)(map.get("DEALER_CODE")));
		}
		POContext.endTxn(true);
	}*/
	//取文件方式 
	public static void main(String[] args) {
			
			/*try {
				ContextUtil.loadConf();
				DEService.getInstance().init();
				DBService ds = DBService.getInstance();
				File file = new File("D:\\1.txt");
				InputStream is=null;
				BufferedReader br = null;
				String tmp = "";
				FileInputStream fis = new FileInputStream(file);
				is=new BufferedInputStream(new FileInputStream(file));
				br = new BufferedReader(new InputStreamReader(is,"GBK"));
				POContext.beginTxn(ds.getDefTxnManager(), -1);
				while((tmp=br.readLine())!=null){
					new OBJ14().execute(tmp);
					//System.out.println(tmp);
				}
				POContext.endTxn(true);
				is.close();
				br.close();
				fis.close();
				
			} catch (Exception e) {
				POContext.endTxn(false);
				
				//throw new RpcException();
			} finally {
				//POContext.cleanTxn();
			}*/
		}
	public String execute(String dealerCode) throws Exception{
		LOG.info("====店面系统库存验证下发开始====");
		List<DefaultParaTempVO> tmpvos = dpDao.queryPart(dealerCode);
		if (null == tmpvos || tmpvos.size() == 0) {
			LOG.info("====店面系统库存验证下发结束====");
			return null;
		}
		DefaultParaTempVO tmpvo = tmpvos.get(0);
		DeUtility de = new DeUtility();
		try {
			Map<String, Object> entitys = deCommonDao.getDmsDealerCode(tmpvo.getEntityCode());
			String entityCode = entitys.get("DMS_CODE").toString();
			List<DefaultParaVO> vos = new ArrayList<DefaultParaVO>();
			//OEM配件只能入OEM库 
			DefaultParaVO vo1 = new DefaultParaVO();
			vo1.setItemCode("1274");
			vo1.setDefaultValue(tmpvo.getIsOem());
			vo1.setDownTimestamp(tmpvo.getDownTimestamp());
			vo1.setIsValid(tmpvo.getIsValid());
			vos.add(vo1);
			//8573	非OEM配件不允许入OEM仓库
			DefaultParaVO vo4 = new DefaultParaVO();
			vo4.setItemCode("8573");
			vo4.setDefaultValue(tmpvo.getIsOem());
			vo4.setDownTimestamp(tmpvo.getDownTimestamp());
			vo4.setIsValid(tmpvo.getIsValid());
			vos.add(vo4);
			//OEM库限制配件调拨 
			DefaultParaVO vo2 = new DefaultParaVO();
			vo2.setItemCode("1275");
			vo2.setDefaultValue(tmpvo.getIsAllocation());
			vo2.setDownTimestamp(tmpvo.getDownTimestamp());
			vo2.setIsValid(tmpvo.getIsValid());
			vos.add(vo2);
			//OEM库存流水上报
			DefaultParaVO vo3 = new DefaultParaVO();
			vo3.setItemCode("1276");
			vo3.setDefaultValue(tmpvo.getIsRechard());
			vo3.setDownTimestamp(tmpvo.getDownTimestamp());
			vo3.setIsValid(tmpvo.getIsValid());
			vos.add(vo3);
			HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
			de.sendAMsg("DRD01",entityCode,body);
			LOG.info("====店面系统库存验证下发结束====,下发了(" + vos.size() + ")条数据");
		} catch (Exception e) {
			LOG.error("====店面系统库存验证下发失败====", e);
		}
		return null;
	}
}
