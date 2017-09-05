package com.infodms.dms.common;

import java.util.Comparator;

import com.infodms.dms.bean.CustomerViewBean;

public class CustViewComparator implements Comparator<Object> {
	public int compare(Object arg0, Object arg1) {
		CustomerViewBean r1 = (CustomerViewBean) arg0;
		CustomerViewBean r2 = (CustomerViewBean) arg1;
		return r2.getDoDate().compareTo(r1.getDoDate());
	}
}
