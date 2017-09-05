package com.infodms.dms.common;

import java.util.Date;

public interface ValidityDate
{
	public Date getInvalidationDate(Date startDate);
}
