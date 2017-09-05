package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * 索赔单自动审核结果信息，包括需要审核人员代码和审核原因
 * @author XZM
 */
public class AuditingVO {
	
	/** 需要审核的授权级别,以","分隔 */
	private String roles = "";
	/** 需要审核的原因 */
	private String reasions = "";
	
	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getReasions() {
		return reasions;
	}
	
	public void setReasions(String reasions) {
		if(reasions!=null)
			this.reasions = " * " + reasions;
	}
	
	public SortedSet<String> rolesToArrays(){
		SortedSet<String> resSet = new TreeSet<String>();
		if(this.roles!=null && !"".equals(this.roles)){
			StringTokenizer st = new StringTokenizer(this.roles,",");
			while (st.hasMoreTokens())
			{
				String oneRole = st.nextToken();
				if (oneRole!=null && !"".equals(oneRole) && !resSet.contains(oneRole)){
					resSet.add(oneRole);
				}
			}
		}
		return resSet;
	}
}
