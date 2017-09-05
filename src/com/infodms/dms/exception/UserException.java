package com.infodms.dms.exception;

/**
 * <ul>
 * <li>文件名称: UserException.java</li>
 * <li>文件描述: 常规异常信息抛出</li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>公        司: 用友汽车信息科技（上海）有限公司-重庆分公司</li>
 * <li>内容摘要: </li>
 * <li>完成日期: 2014-1-16 下午1:36:53</li>
 * <li>修改记录: </li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
public class UserException extends RuntimeException {

	private static final long serialVersionUID = 230694366546100923L;
	
	public UserException(String message)
	{
		super(message);
	}
}
