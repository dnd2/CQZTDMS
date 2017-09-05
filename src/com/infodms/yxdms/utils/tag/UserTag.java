package com.infodms.yxdms.utils.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;

@SuppressWarnings("serial")
public class UserTag extends TagSupport {

	private String value;//对应的值
	private String showType;//对应的值 1为input 0为span 
	private final ApplicationDao dao = ApplicationDao.getInstance();
	
	@SuppressWarnings("unchecked")
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuffer sbTag=new StringBuffer();
		try {
			String userName="";
			if(!"".equals(value)){
				TcUserPO t=new TcUserPO();
				t.setUserId(Long.valueOf(value));
				List<TcUserPO> list = dao.select(t);
				if(DaoFactory.checkListNull(list)){
					userName=list.get(0).getName();
				}
			}
			if(showType==null){
				showType="1";
			}
			if("1".equals(showType)){
				sbTag.append("<input class='middle_txt' value="+userName+" readonly='readonly'  type='text' maxlength='30' />");
			}
			if("0".equals(showType)){
				sbTag.append("<span>"+userName+"</span>");
			}
			
			out.println(sbTag.toString());
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

}
