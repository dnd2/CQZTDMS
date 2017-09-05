package com.infodms.dms.actions.customerRelationships.complaint;

public class ComplaintDisposalType {
	/**
	 * 根据小类获取大类
	 */
	public static final Integer getBTypeByLType(Integer complaintType) {
		switch(complaintType) {

		case    11701001 :
		case	11701002 :
		case	11701003 :
		case	11701004 :
		case	11701005 :
		case	11701006 :
		case	11701007 :
		case	11701008 :
		case	11701009 :
		case	11701010 :

			return 10651001;
		case    11711001 :
		case	11711002 :
		case	11711003 :
			return 10651002;
		case 11621001:
		case 11621002:
		case 11621003:
		case 11621004:
		case 11621005:
		case 11621006:
			return 10651003;
		case 11631001:
		case 11631002:
		case 11631003:
		case 11631004:
		case 11631005:
		case 11631006:
		case 11631007:
		case 11631008:
		case 11631009:			
			return 10651004;
		case 11641001:
		case 11641002:
		case 11641003:
		case 11641004:
		case 11641005:
		case 11641006:
			return 10651005;
		case 11651001:
		case 11651002:
		case 11651003:
		case 11651004:
		case 11651005:
		case 11651006:
		case 11651007:
			return 10651006;
		case 11661001:
		case 11661002:
			return 10651007;
		case 11671001:
		case 11671002:
			return 10651008;
		case 11681001:
		case 11681002:
			return 10651009;	
		case 11691001:
		case 11691002:
		case 11691003:
		case 11691004:
		case 11691005:
			return 10651010;			
		default :
			throw new IllegalArgumentException("Invalid complaintType, complaintType == " + complaintType);
		}
	}
	
}
