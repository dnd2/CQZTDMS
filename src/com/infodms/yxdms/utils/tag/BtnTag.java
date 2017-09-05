package com.infodms.yxdms.utils.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.infodms.yxdms.service.CommonService;
import com.infodms.yxdms.service.impl.CommonServiceImpl;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;

@SuppressWarnings("serial")
public class BtnTag extends TagSupport {

	private String mainTainSql;//维护的对应唯一标示
	private CommonService  CommonService=new CommonServiceImpl();
	
	
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		StringBuffer sbTag=new StringBuffer();
		try {
			List<Map<String,Object>> list=CommonService.findBtnQury(mainTainSql);
			if(DaoFactory.checkListNull(list)){
				sbTag.append("<table width='100%'  border='0' style='text-align: center;' cellspacing='0' cellpadding='0' >");
				sbTag.append("<tr>");
				sbTag.append("<td>");
				for (Map<String, Object> map : list) {
					String btn_action = BaseUtils.checkNull(map.get("BTN_ACTION"));
					String class_name = BaseUtils.checkNull(map.get("CLASS_NAME"));
					String fun_onlick = BaseUtils.checkNull(map.get("FUN_ONLICK"));
					//String is_tips = BaseUtils.checkNull(map.get("IS_TIPS"));
					//String is_text = BaseUtils.checkNull(map.get("IS_TEXT"));
					//String is_record = BaseUtils.checkNull(map.get("IS_RECORD"));
					String btn_name = BaseUtils.checkNull(map.get("BTN_NAME"));
					
					sbTag.append("&nbsp;&nbsp;&nbsp;<input  id='"+btn_action+"' name='"+btn_action+"'  value='"+btn_name+"' ");
					if(!"".equals(btn_action) && btn_action.equals("res_btn")){
						sbTag.append(" type='reset' "); 
					}else{
						sbTag.append(" type='button' "); 
					}
					if(!"".equals(class_name)){
						sbTag.append(" class='"+class_name+"' ");
					}else{
						sbTag.append(" class='normal_btn' ");
					}
					if(!"".equals(fun_onlick)){
						sbTag.append(" onclick='"+fun_onlick+"(this);' ");
					}
					sbTag.append("/>");
				}
				sbTag.append("</td>");
				sbTag.append("</tr>");
				sbTag.append("</table>");
				sbTag.append("<br/>");
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

	public String getMainTainSql() {
		return mainTainSql;
	}

	public void setMainTainSql(String mainTainSql) {
		this.mainTainSql = mainTainSql;
	}
}
