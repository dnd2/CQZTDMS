package com.infodms.dms.actions.common;
/**
 * 20150407
 * @author ranke
 *
 */

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class Demo {
	public Logger logger = Logger.getLogger(Demo.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String demo = "/jsp/demo/demo.jsp";
	private final String demo2 = "/jsp/demo/demo2.jsp";
	
	public void demo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(demo);
		}catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "demo页加载失败！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void demo2(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(demo2);
		}catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "demo页加载失败！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    
    /**
    * 汉字转拼音手写字母
    * @param name 汉字
    * @return 拼音
    */
	public String hanZiToPinyin(String name){
		String pinyinName = "";
        char[] nameChar = name.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += (PinyinHelper.toHanyuPinyinStringArray
                                           (nameChar[i], defaultFormat)[0]).substring(0, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }
        return pinyinName;
    }
 
    public static void main(String[] args) {
        System.out.println(new Demo().hanZiToPinyin("文进s沙发上对方收到"));
    }    
}
