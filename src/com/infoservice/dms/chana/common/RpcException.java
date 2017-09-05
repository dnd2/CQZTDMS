package com.infoservice.dms.chana.common;
/**
 * 
* @ClassName: RpcException 
* @Description: TODO(接口异常类) 
* @author liuqiang 
* @date Jul 23, 2010 1:48:26 PM 
*
 */
public class RpcException extends RuntimeException {
	
	private static final long serialVersionUID = 6503460240794843859L;
	public RpcException() {
		super();
	}

	public RpcException(String message) {
		super(message);
	}
	
	public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
	    
	public RpcException(Exception message) {
		super(message);
	}
}
