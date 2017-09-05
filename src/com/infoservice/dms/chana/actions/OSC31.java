package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.CustomerComplaintDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.ActivityVO;
import com.infoservice.dms.chana.vo.CustomerComplaintVO;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
 * @ClassName: OSC31
 * @Description: TODO(投诉信息下发类)
 * @author liuqiang
 * @date Jul 30, 2010 2:25:06 PM
 * 
 */
public class OSC31 {
	private static final Logger LOG = Logger.getLogger(OSC31.class);
	private CustomerComplaintDao dao = CustomerComplaintDao.getInstance();

	public String execute(List<TtCrComplaintsPO> pos) {
		LOG.info("====投诉信息下发开始====");
		try {
			String codes = getQueryCode(pos);
			Long dealerId = pos.get(0).getDealerId();
			List<CustomerComplaintVO> vos = dao.queryCustomerComplaint(codes,
					dealerId);
			if (null == vos || vos.size() == 0) {
				return null;
			}
			DeUtility de = new DeUtility();
			HashMap<String, Serializable> body = DEUtil.assembleBody(vos);
			de.sendAMsg("DRC31", vos.get(0).getEntityCode(), body);
			LOG.info("====投诉信息下发结束====,下发了(" + body.size() + ")条数据");
		} catch (Exception e) {
			LOG.error("投诉信息下发失败", e);
			//throw new RpcException(e);
		}
		return null;
	}

	private String getQueryCode(List<TtCrComplaintsPO> pos) {
		StringBuilder str = new StringBuilder();
		for (TtCrComplaintsPO po : pos) {
			str.append(po.getCompCode());
			str.append(",");
		}
		str.deleteCharAt(str.length() - 1);
		return StringUtil.compileStr(str.toString());
	}

	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		OSC31 o = new OSC31();
		List<TtCrComplaintsPO> pos = new ArrayList<TtCrComplaintsPO>();
		TtCrComplaintsPO po1 = new TtCrComplaintsPO();
		po1.setCompCode("TSBH20100715002528");
		pos.add(po1);
		TtCrComplaintsPO po2 = new TtCrComplaintsPO();
		po2.setCompCode("TSBH20100809003461");
		pos.add(po2);
		o.execute(pos);
	}

}
