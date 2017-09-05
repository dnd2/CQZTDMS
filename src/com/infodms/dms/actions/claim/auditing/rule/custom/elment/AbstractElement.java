package com.infodms.dms.actions.claim.auditing.rule.custom.elment;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.repairOrder.RoOrderVO;
import com.infodms.dms.common.Constant;

/**
 * 授权项抽象类
 * 注:新增授权项步骤
 *    1、在TC_CODE增加新授权项信息
 *    2、创建授权项对应取值了（需要继承AbstractElement）
 *    3、在AbstractElement工厂方法（getElement）中加入取得该授权项实例代码
 * @author XZM
 */
public abstract class AbstractElement {
	
	private static Logger logger = Logger.getLogger(AbstractElement.class);
	/** 
	 * 取的对应授权项需要校验的数据
	 * @return Object
	 */
	protected abstract Object getElementValue(RoOrderVO orderVO);
	
	/**
	 * 处理取值
	 * @param orderVO
	 * @return
	 */
	public Object getPackElementValue(RoOrderVO orderVO){
		Object obj = this.getElementValue(orderVO);
		
		if(obj==null)
			obj = "";
		
		return obj;
	}
	
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 新增方法
     * Date:2017-06-29
     * java：CustomMonitorAuditing。java
     */
	protected abstract Object getElementValue(ClaimOrderVO orderVO);
	public Object getPackElementValue(ClaimOrderVO orderVO){
		Object obj = this.getElementValue(orderVO);
		
		if(obj==null)
			obj = "";
		
		return obj;
	}
	
	/**
	 * 取的授权项对应值
	 * @param elementCode TC_CODE中维护的授权项代码
	 * @return AbstractElement
	 */
	public final static AbstractElement getElement(String elementCode){
		
		AbstractElement resElement = null;
		
		Integer eCode = Integer.parseInt(elementCode);
		
		if(Constant.CLAIM_AUTH_TYPE_01.equals(eCode)){//索赔配件三包期内金额
			resElement = new Element01();
		}else if(Constant.CLAIM_AUTH_TYPE_02.equals(eCode)){//维修项目总金额
			resElement = new Element02();
		}else if(Constant.CLAIM_AUTH_TYPE_03.equals(eCode)){//主要件次数
			resElement = new Element03();
		}else if(Constant.CLAIM_AUTH_TYPE_04.equals(eCode)){//非主要件次数
			resElement = new Element04();
		}else if(Constant.CLAIM_AUTH_TYPE_05.equals(eCode)){//修理累计时间
			resElement = new Element05();
		}else if(Constant.CLAIM_AUTH_TYPE_06.equals(eCode)){//维修总费用
			resElement = new Element06();
		}{//没有对应类型
			logger.error(">>>>>>>>>>>>>[该类型授权项没有维护]>>>>>>>>>>>>");
		}
		
		return resElement;
	}
}
