package com.infodms.dms.common.tag;

import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.infodms.dms.po.TcCodePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
/**
 * 根据type 和 codeid 取字典的值
 * @author yuewei
 */
@SuppressWarnings("serial")
public class ChangeTag extends TagSupport {

	protected POFactory factory = POFactoryBuilder.getInstance();
	
	private String val;

	private String type;

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		TcCodePO po=new TcCodePO();
		try {
			po.setType(type);
			po.setCodeId(val);
			List<TcCodePO> list = factory.select(po);
			if(list!=null && list.size()==1){
				po= list.get(0);
				out.println(po.getCodeDesc());
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

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
