package com.infodms.yxdms.utils.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;

@SuppressWarnings("serial")
public class ConditionTag extends TagSupport {

	private final ApplicationDao dao = ApplicationDao.getInstance();
	private String querySeq;
	

	@SuppressWarnings("unchecked")
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuilder sbTag=new StringBuilder();
		String msg="";
		try {
			sbTag.append("<table width='100%' border='0' align='center' cellpadding='1' cellspacing='1' ");
			StringBuffer sbTitle= new StringBuffer();
			sbTitle.append("select q.* from  query_title q where 1=1\n");
			DaoFactory.getsql(sbTitle, "q.query_id", querySeq, 1);
			List<Map<String, String>> titleLsit = dao.pageQuery(sbTitle.toString(), null, dao.getFunName());
			Map<String, String> mapTitle = titleLsit.get(0);
			String query_style = mapTitle.get("QUERY_STYLE");
			String memory_page = BaseUtils.checkNull(mapTitle.get("MEMORY_PAGE"));
			String memory_filed = BaseUtils.checkNull(mapTitle.get("MEMORY_FILED"));
			String button_function = mapTitle.get("BUTTON_FUNCTION");
			String button_name = mapTitle.get("BUTTON_NAME");
			if(BaseUtils.notNull(query_style)){
				sbTag.append(" class='"+query_style+"' ");
			}else{
				sbTag.append(" class='table_query' ");
			}
			sbTag.append(" >");
			sbTag.append(" <input id='querySeq' type='hidden' value='"+this.querySeq+"'/>");
			StringBuffer sb= new StringBuffer();
			sb.append("select t.* from \n" );
			sb.append(" query_Condition t,query_title tt \n" );
			sb.append("where t.query_id=tt.query_id \n");
			DaoFactory.getsql(sb, "tt.query_id", this.querySeq, 1);
			sb.append(" order by t.Condition_id \n");
			List<Map<String, String>> conditionLsit = dao.pageQuery(sb.toString(), null, dao.getFunName());
			int size = conditionLsit.size();
			int temp=0;
			sbTag.append("<tr><td width='12.5%' nowrap='true'></td><td width='15%' nowrap='true'></td><td width='10%' nowrap='true'></td><td width='15%' nowrap='true'></td><td width='10%' nowrap='true'></td><td width='15%' nowrap='true'></td><td width='10%' nowrap='true'></td><td width='15%' nowrap='true'></td></tr>");
			if(size%3==0){
				sbTag.append(" <tr> <td width='12.5%' nowrap='true'></td>");
			}
			for (Map<String, String> map : conditionLsit) {
				String condition_feild = map.get("CONDITION_FEILD");
				String condition_name = map.get("CONDITION_NAME");
				String condition_value = map.get("CONDITION_VALUE");
				String condition_type = map.get("CONDITION_TYPE");
				String condition_length = map.get("CONDITION_LENGTH");
				String condition_readonly = map.get("CONDITION_READONLY");
				String condition_style = map.get("CONDITION_STYLE");
				String condition_function = map.get("CONDITION_FUNCTION");
				String condition_sql = map.get("CONDITION_SQL");
				String condition_sql_val = map.get("CONDITION_SQL_VAL");
				String condition_sql_name = map.get("CONDITION_SQL_NAME");
				if(BaseUtils.notNull(condition_feild)&&BaseUtils.notNull(condition_name)){
					sbTag.append("<td width='10%'class='table_query_2Col_label_5Letter' nowrap='true'>&nbsp;&nbsp;&nbsp;"+condition_name+":&nbsp;&nbsp; </td>");
					if(BaseUtils.notNull(condition_type)){
						if("1".equals(condition_type)){
							sbTag.append("<td width='20%'><input name='"+condition_feild+"' type='text' id='"+condition_feild+"' ");
							this.joinStr(sbTag,"maxlength", condition_length);
							this.joinStr(sbTag,"readonly", condition_readonly);
							this.joinStr(sbTag,"class", condition_style);
							this.joinStr(sbTag,"onclick", condition_function);
							this.joinStr(sbTag,"value", condition_value);
							sbTag.append("/>");
						}
						
						if("2".equals(condition_type)){
							sbTag.append("<td width='20%'><select name='"+condition_feild+"' type='text' id='"+condition_feild+"' ");
							this.joinStr(sbTag,"maxlength", condition_length);
							this.joinStr(sbTag,"readonly", condition_readonly);
							this.joinStr(sbTag,"class", condition_style);
							this.joinStr(sbTag,"onclick", condition_function);
							this.joinStr(sbTag,"value", condition_value);
							sbTag.append("><option value=''>--请选择--</option>");
							List<Map<String, String>> condition_sql_list =dao.pageQuery(condition_sql, null,dao.getFunName());
							if(condition_sql_list!=null && condition_sql_list.size()>0){
							for (Map<String, String> condition_sql_map : condition_sql_list) {
								String val = condition_sql_map.get(condition_sql_val.toUpperCase());
								String name = condition_sql_map.get(condition_sql_name.toUpperCase());
								if(val.equals(condition_value)){
									sbTag.append("<option selected='selected' value='"+val+"'>"+name+"</option>");
								}else{
									sbTag.append("<option value='"+val+"'>"+name+"</option>");
								}
								}
							}
						}
						if("3".equals(condition_type)){
			 				sbTag.append("<td width='20%'><input name='"+condition_feild+"' type='text' id='"+condition_feild+"'");
							this.joinStr(sbTag,"maxlength", condition_length);
							this.joinStr(sbTag,"readonly", condition_readonly);
							this.joinStr(sbTag,"class", condition_style);
							this.joinStr(sbTag,"onclick", condition_function);	
							this.joinStr(sbTag,"value", condition_value);
							sbTag.append("/>");
							sbTag.append("&nbsp;&nbsp;&nbsp;<input type='hidden' name='dealerId' id='dealerId' value=''/><input type='button' value='清除' class='min_btn' onclick='wrapOut();'/>");
						}
						if("4".equals(condition_type)){
							sbTag.append("<td width='20%'>");
							List<Map<String, String>> condition_sql_list =dao.pageQuery(condition_sql, null,dao.getFunName());
							if(condition_sql_list!=null && condition_sql_list.size()>0){
							for (Map<String, String>  condition_sql_check: condition_sql_list) {
								String val = condition_sql_check.get(condition_sql_val.toUpperCase());
								String name = condition_sql_check.get(condition_sql_name.toUpperCase());
								sbTag.append("<input type='checkbox' name='"+condition_feild+"' type='checkbox' value='"+val+"' ");
								this.joinStr(sbTag,"maxlength", condition_length);
								this.joinStr(sbTag,"readonly", condition_readonly);
								this.joinStr(sbTag,"class", condition_style);
								this.joinStr(sbTag,"onclick", condition_function);
								this.joinStr(sbTag,"value", condition_value);
								String[] condition_values = condition_value.split(",");
								if(condition_values!=null &&condition_values.length>0 ){
									for (String strVal : condition_values) {
										if(val.equals(strVal)){
											sbTag.append(" checked='checked' ");
										}
									}
								}
								sbTag.append("/>"+name+"");
							}
						}
						}
						/*
						if("5".equals(condition_type)){
							this.joinStr(sbTag,"maxlength", condition_length);
							this.joinStr(sbTag,"readonly", condition_readonly);
							this.joinStr(sbTag,"class", condition_style);
							this.joinStr(sbTag,"onclick", condition_function);
							sbTag.append(" <td class='table_query_2Col_label_5Letter' nowrap='true'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~至~</td>");
							sbTag.append("<td width='20%'><input name='"+condition_feilds[1]+"' type='text' id='"+condition_feild+"' class='middle_txt'");
							this.joinStr(sbTag,"maxlength", condition_length);
							this.joinStr(sbTag,"readonly", condition_readonly);
							this.joinStr(sbTag,"class", condition_style);
							this.joinStr(sbTag,"onclick", condition_function);
						}
						if("6".equals(condition_type)){
							
						}
						if("7".equals(condition_type)){
							
						}
						if("8".equals(condition_type)){
							
						}*/
					}
				}
				if("2".equals(condition_type)){
					sbTag.append("</select>");
				}else{
					sbTag.append("</td>");
				}
				temp++;
				if(temp%3==0){
					sbTag.append(" <td width='12.5%' nowrap='true'></td></tr>");
				}
			}
			sbTag.append(" <tr><td align='center' colspan='8'>");
			String[] button_functions = button_function.split(";");
			String[] button_names = button_name.split(";");
			for (int i = 0; i < button_names.length; i++) {
				sbTag.append("<input type='button' name='btnQuery'  value='"+button_names[i]+"' class='normal_btn' onClick='"+button_functions[i]+"' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			}
			if("1".equals(memory_filed)){
				sbTag.append("<input type='checkbox' checked='checked' onclick='memory_filed(this)'/>记忆字段功能 ");
			}else{
				sbTag.append("<input type='checkbox' onclick='memory_filed(this)'/>记忆字段功能 ");
			}
			if(!"".equals(memory_page)&&Integer.parseInt(memory_page)>0){
				sbTag.append("<input type='checkbox' checked='checked' onclick='memory_page(this)'/>记忆页数功能&nbsp;&nbsp;&nbsp;");
			}else{
				sbTag.append("<input type='checkbox'  onclick='memory_page(this)'/>记忆页数功能&nbsp;&nbsp;&nbsp;");
			}
			sbTag.append("<input type='text' id='memory_page_temp' class='mini_txt'  value='"+memory_page+"'/>");
			sbTag.append(" </td></tr>");
			sbTag.append(" </table>");
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

	public String getQuerySeq() {
		return querySeq;
	}

	public void setQuerySeq(String querySeq) {
		this.querySeq = querySeq;
	}
	public void joinStr(StringBuilder sbTag,String name,String str){
		if(BaseUtils.notNull(str)){
			sbTag.append(" "+name+"='"+str+"' ");
		}
	}
}
