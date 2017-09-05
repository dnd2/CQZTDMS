package com.infodms.dms.common.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.po.TcCodePO;
/**
 * 根据type 和 codeid 取字典的值
 * @author yuewei
 */
@SuppressWarnings("serial")
public class SelectTag extends TagSupport {

	private final ApplicationDao dao = ApplicationDao.getInstance();
	private String func;
	private String name;
	private String id;
	private String type;
	private String style;
	private String noExist;
	private String exist;
	private String value;
	private String noTop;
	private String fieldCode;
	private String fieldName;
	private String sql;
	private String clazz;

	@SuppressWarnings("unchecked")
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
		if(sql!=null && !"".equals(sql)){
			StringBuffer sb=new StringBuffer();//拼下拉菜单
			sb.append("<select  name='"+name+"' class='"+style+"'");
			if(func!=null&&!"".equals(func)){
				sb.append(" onchange='"+func+"(this);'");
			}
			if(id!=null && !"".equals(id) ){
				sb.append("id='"+id+"'");
			}else{
				sb.append("id='"+name+"'");
			}
			sb.append(">");
			if(noTop!=null&&noTop.equals("true")){
				//sb.append("<option value=''>--请选择--</option>");
			}else{
				sb.append("<option value=''>--请选择--</option>");
			}
			List<Map<String, String>> sqllsit = dao.pageQuery(sql, null, dao.getFunName());
			for (Map<String, String> map : sqllsit) {
				String fieldcode = map.get(fieldCode.toUpperCase()).toString();
				String fieldname = map.get(fieldName.toUpperCase()).toString();
				if(fieldcode.equals(value)){
					sb.append("<option selected='selected' value='"+fieldcode+"'>"+fieldname+"</option>");
				}else{
					sb.append("<option value='"+fieldcode+"'>"+fieldname+"</option>");
				}
			}
			sb.append("</select>");
			out.println(sb.toString());
		}else{
			StringBuffer sb=new StringBuffer();//拼下拉菜单
			StringBuffer sql=new StringBuffer();//拼sql查询语句
			sql.append("select tc.* from tc_code tc where 1=1 ");
			DaoFactory.getsql(sql, "tc.type", type, 1);
			if(exist!=null &&!"".equals(exist)){
				DaoFactory.getsql(sql, "tc.code_id", exist, 6);
			}
			if(noExist!=null &&!"".equals(noExist)){
				DaoFactory.getsql(sql, "tc.code_id", noExist, 8);
			}
			List<TcCodePO> list = dao.select(TcCodePO.class, sql.toString(),null);
			if(list!=null && list.size()>0){
				sb.append("<select id='"+name+"' name='"+name+"' class='"+style+"'");
				if(func!=null&&!"".equals(func)){
					sb.append(" onchange='"+func+"(this);'");
				}
				sb.append(">");
				if(noTop!=null&&noTop.equals("true")){
					//sb.append("<option value=''>--请选择--</option>");
				}else{
					sb.append("<option value=''>--请选择--</option>");
				}
				for (TcCodePO tc : list) {
					if(value!=null && tc.getCodeId().equals(value)){
						sb.append("<option selected='selected' value='"+tc.getCodeId()+"'>"+tc.getCodeDesc()+"</option>");
					}else{
						sb.append("<option value='"+tc.getCodeId()+"'>"+tc.getCodeDesc()+"</option>");
					}
				}
				sb.append("</select>");
				out.println(sb.toString());
			}
		}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getNoExist() {
		return noExist;
	}

	public void setNoExist(String noExist) {
		this.noExist = noExist;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNoTop() {
		return noTop;
	}

	public void setNoTop(String noTop) {
		this.noTop = noTop;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
