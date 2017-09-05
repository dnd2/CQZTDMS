package com.infodms.dms.util.gzip;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GZipFilter implements Filter{

  public void destroy() {
  }

  public void init(FilterConfig fConfig)throws ServletException{
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
  throws IOException, ServletException{
    HttpServletRequest req=(HttpServletRequest)request;
    HttpServletResponse res=(HttpServletResponse)response;
    if(isGZipEncoding(req)){
      GZipResponse zipResponse=new GZipResponse(res);
      res.setHeader("Content-Encoding","gzip");
      chain.doFilter(request, zipResponse);
      zipResponse.flush();
    }else {
      chain.doFilter(request, response);
    }
  }
  
  /**
   * 判断浏览器是否支持GZIP
   *@paramrequest
   *@return
   */
  private static boolean isGZipEncoding(HttpServletRequest request){
    boolean flag=false;
    String encoding=request.getHeader("Accept-Encoding");
    if(encoding.indexOf("gzip")!=-1){
      flag=true;
    }
    return flag;
  }

}