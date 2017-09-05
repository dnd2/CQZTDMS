package com.infodms.dms.exception;

import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.component.message.MessageComponent;
import com.infoservice.mvc.context.ActionContext;

/**
* 功能说明：
* 典型用法：
* 示例程序如下：
* 特殊用法：
* 创建者：xianchao zhang
* 创建时间：2009-8-22
* 修改人：
* 修改时间：
* 修改原因：
* 修改内容：
* 版本：0.1
*/
public class BizException extends Exception{
	/**
	* 功能说明：
	* 取值范围：
	* 依赖属性：
	* 最后修改时间：2009-8-22
	*/
	private static final long serialVersionUID = 1L;
	/**
	* 功能说明：异常的错误代码
	* 取值范围：errorMsg_zh.properties中定义的错误代码
	* 依赖属性：系统加载时加载到内存中
	* 最后修改时间：2009-8-22
	*/
	private String errCode;
	/**
	* 功能说明：错误代码对应的错误信息
	* 取值范围：errorMsg_zh.properties中定义的错误信息
	* 依赖属性：errCode
	* 最后修改时间：2009-8-22
	*/
	private String message;
	
	/**
	* 功能说明：异常的类型，现定义了两中类型，
	* 一种是公共模块处理的异常（类型代码：1），一种是需要自己处理的异常（类型代码：2），
	* 取值范围：{1,2},对应ErrorCodeConstant:AUTO_DEAL_WITH_CODE, SELF_DEAL_WITH_CODE ; 
	* 依赖属性：
	* 最后修改时间：2009-8-22
	*/
	private int type;
	
	/**
	 * 
	* 功能说明：构造函数,继承Exception
	* @param e 继承Exception
	* 最后修改时间：2009-8-22
	 */
	public BizException(Exception e){
		super(e);
	}
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @param type 
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,String errCode, int type){
		this.type = type;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode);
			String url = act.getRequest().getRequestURI();
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
			this.errCode = errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 
	* 功能说明：数据校验的BizException的构造函数
	* @param errCode 错误代码
	* @param message 错误信息
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,String errCode,StringBuffer message){
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = message.toString();
			String url = act.getRequest().getRequestURI();
			this.errCode = errCode;
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,String errCode){
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode);
			String url = act.getRequest().getRequestURI();
			this.errCode = errCode;
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 异常处理
     * 新增构造
     * Date:2017-06-29
     */
	public BizException(ActionContext act,String errCode, int type, String msg){
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode);
			String url = act.getRequest().getRequestURI();
			this.errCode = errCode;
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @param parms 错误信息的参数，使用时请依次传入
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(String errCode,Object... parms){
		this.errCode = errCode;
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode,parms);
		}catch(Exception e){
		//	throw new RuntimeException(e);   update by john huang
			this.message = errCode;
			this.errCode = "";
		}
	}
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @param parms 错误信息的参数，使用时请依次传入
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,String errCode,Object... parms){
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode,parms);
			String url = act.getRequest().getRequestURI();
			this.errCode = errCode;
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @param th:原使异常
	* @param parms 错误信息的参数，使用时请依次传入
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,Exception th,String errCode,Object... parms){
		super(th);
		this.type = ErrorCodeConstant.AUTO_DEAL_WITH_CODE;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode,parms);
			String url = act.getRequest().getRequestURI();
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
			this.errCode = errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	* 功能说明：构造函数
	* @param errCode 错误代码
	* @param type 错误类型：1：公共模块处理的异常，2：开发人员自己处理的异常，默认为公共模块处理
	* @param parms ：错误信息的参数，使用时请依次传入
	* @throws BizException 对于没有找到错误代码对应的错误信息或其它错误信息查找的错误都会抛出RuntimeException
	* 最后修改时间：2009-8-22
	 */
	public BizException(ActionContext act,String errCode,int type,Object... parms) {
		this.type = type;
		try{
			this.message = MessageComponent.getInstance().getMessage(errCode,parms);
			String url = act.getRequest().getRequestURI();
			//this.errCode = MessageComponent.getInstance().getMessage(url.substring(0,url.lastIndexOf(".")))+errCode;
			this.errCode = errCode;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 
	* 功能说明：
	* @return
	* 最后修改时间：2009-8-22
	 */
	public String getErrCode() {
		return errCode;
	}
	/**
	 * 
	* 功能说明：
	* @param errCode
	* 最后修改时间：2009-8-22
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 实现toString方法
	 */
	public String toString(){
		return this.getClass().getName()+":-----------errorCode:"+this.errCode+"------------message:"+this.message;
	}
}
