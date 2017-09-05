package com.infodms.dms.common.repairorder.edwclient;

import java.util.Comparator;

public class RepairOrderComparator implements Comparator<Object> {
	public int compare(Object arg0, Object arg1) {
		RepairOrder r1 = (RepairOrder) arg0;
		RepairOrder r2 = (RepairOrder) arg1;
		return r1.getStartTime().compareTo(r2.getStartTime());
	}
}
