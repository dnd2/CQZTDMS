package com.infodms.yxdms.utils.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.po.TcCodePO;

@SuppressWarnings("serial")
public class TcCodeTag extends TagSupport {

	private String value;//对应的值
	private String showType;//对应的值 1为input 0为span 
	private final ApplicationDao dao = ApplicationDao.getInstance();
	
	@SuppressWarnings("unchecked")
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuffer sbTag=new StringBuffer();
		try {
			TcCodePO t=new TcCodePO();
			t.setCodeId(value);
			List<TcCodePO> list = dao.select(t);
			String desc="";
			if(list!=null && list.size()>0){
				desc=list.get(0).getCodeDesc();
			}
			if(showType==null){
				showType="1";
			}
			if("1".equals(showType)){
				sbTag.append("<input class='middle_txt' value="+desc+" readonly='readonly'  type='text' maxlength='30' />");
			}
			if("0".equals(showType)){
				sbTag.append("<span>"+desc+"</span>");
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
