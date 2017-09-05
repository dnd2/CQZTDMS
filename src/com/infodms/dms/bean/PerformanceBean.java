/**********************************************************************
* <pre>
* FILE : PerformanceBean.java
* CLASS : PerformanceBean
*
* AUTHOR : SuMMeR
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-9-21| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: PerformanceBean.java,v 1.1 2010/08/16 01:42:32 yuch Exp $
*/

package com.infodms.dms.bean;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-21
 * @version    :
 */
public class PerformanceBean
{
	// 姓名
	private String name;

	// 日期-年月
	private String date;

	// 来访新增数
	private int vistAlctNum;

	// 意想新增数
	private int purNewIntent;

	// 评估次数
	private int evaNum;

	// 收购数
	private int purNum;

	// 销售数
	private int saleNum;

	// 平均收车价格
	private double avgPurVhclPrice;

	// 平均单车利润
	private double sigVhclAvgProfit;

	// 总利润
	private double sumProfit;
	
	//销售意向新增数
	private int saleNewIntent;

	public double getSumProfit()
	{
		return sumProfit;
	}

	public void setSumProfit(double sumProfit)
	{
		this.sumProfit = sumProfit;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public int getVistAlctNum()
	{
		return vistAlctNum;
	}

	public void setVistAlctNum(int vistAlctNum)
	{
		this.vistAlctNum = vistAlctNum;
	}

	public int getPurNewIntent()
	{
		return purNewIntent;
	}

	public void setPurNewIntent(int purNewIntent)
	{
		this.purNewIntent = purNewIntent;
	}

	public int getEvaNum()
	{
		return evaNum;
	}

	public void setEvaNum(int evaNum)
	{
		this.evaNum = evaNum;
	}

	public int getPurNum()
	{
		return purNum;
	}

	public void setPurNum(int purNum)
	{
		this.purNum = purNum;
	}

	public double getAvgPurVhclPrice()
	{
		return avgPurVhclPrice;
	}

	public void setAvgPurVhclPrice(double avgPurVhclPrice)
	{
		this.avgPurVhclPrice = avgPurVhclPrice;
	}

	public double getSigVhclAvgProfit()
	{
		return sigVhclAvgProfit;
	}

	public void setSigVhclAvgProfit(double sigVhclAvgProfit)
	{
		this.sigVhclAvgProfit = sigVhclAvgProfit;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSaleNum()
	{
		return saleNum;
	}

	public void setSaleNum(int saleNum)
	{
		this.saleNum = saleNum;
	}

	/**
	 * @return the saleNewIntent
	 */
	public int getSaleNewIntent() {
		return saleNewIntent;
	}

	/**
	 * @param saleNewIntent the saleNewIntent to set
	 */
	public void setSaleNewIntent(int saleNewIntent) {
		this.saleNewIntent = saleNewIntent;
	}

}
