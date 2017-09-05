package com.infodms.yxdms.utils.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class RadioTag extends TagSupport {

	private String name;//name属性
	private String className;//class属性
	private String value;//对应的值
	private String valueName;//对应的值
	
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuffer sbTag=new StringBuffer();
		try {
			String[] values = StringUtils.split(value,",");
			String[] valueNames = StringUtils.split(valueName,",");
			int temp=0;
			for (String valueName : valueNames) {
				sbTag.append("<input type='radio' name='"+this.name+"'  class='"+this.className+"' value='"+values[temp]+"'/>"+valueName+"");
				temp++;
			}
			out.println(sbTag);
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
}
