package com.infodms.dms.common.interceptor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.infodms.dms.common.FileConstant;

public class AddHeaderFilter implements Filter {
	private static Logger logger = Logger.getLogger(AddHeaderFilter.class);
	Map headers = new HashMap();
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if(req instanceof HttpServletRequest) {
			doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);
		}else {
			chain.doFilter(req, res);
		}
	}

	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			for(Iterator it = headers.entrySet().iterator();it.hasNext();) {
				Map.Entry entry = (Map.Entry)it.next();
				response.addHeader((String)entry.getKey(),(String)entry.getValue());
			}
			chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		//设置应用中的js路径
		logger.info("ServletContext Path: " + config.getServletContext().getRealPath(""));
		FileConstant.jsPath = config.getServletContext().getRealPath("")+"js" + File.separator;
		String headersStr = config.getInitParameter("headers");
		String[] headers = headersStr.split(";");
		for(int i = 0; i < headers.length; i++) {
			String[] temp = headers[i].split(":");
			this.headers.put(temp[0].trim(), temp[1].trim());
		}
	}

}
