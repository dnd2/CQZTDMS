package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import com.infoservice.de.DEMessage;
import com.infoservice.dms.chana.vo.MaterialGroupVO;

public class TestAction extends AbstractReceiveAction {

	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		Map<String, Serializable> bodys = msg.getBody();// 获得来至FRM2的消息数据
		for (Entry<String, Serializable> entry : bodys.entrySet()) {
			MaterialGroupVO vo = new MaterialGroupVO();
			System.out.println("key======" + entry.getKey());
			vo = (MaterialGroupVO) entry.getValue();
			System.out.println(vo.getBrandCode() + "   " + vo.getBrandName());
			
		}
		return null;
	}

}