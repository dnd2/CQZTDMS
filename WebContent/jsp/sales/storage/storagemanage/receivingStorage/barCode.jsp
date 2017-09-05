<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.infodms.dms.util.BarcodeTools"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.OutputStream"%>
<%@ page contentType="image/jpeg"
	import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*"%>
<%
	String barCode = request.getParameter("barCode");
	response.setHeader("Pragma","No-cache");    
	response.setHeader("Cache-Control","no-cache");    
	response.setDateHeader("Expires", 0);
	
	Map<String, String > strMap = new HashMap<String, String>();
	strMap.put("msg", barCode);//条码号
	strMap.put("height", "20");//高度
	strMap.put("mw", "0.5");//宽度
	/**
	 * fmt 类型
	 * image/svg+xml XML文件
	 * image/e-eps eps文件
	 * image/tiff tiff文件
	 * image/jpeg jpeg图片类型文件
	 * image/x-png png图片类型文件
	 * image/gif gif 图片类型文件
	 * image/bmp bmp图片类型文件
	 */
	strMap.put("fmt", "image/jpeg");//类型为空为XML e.g:图片为 image/jpeg;xml文件为image/svg+xml;
	BarcodeTools bs = new BarcodeTools(strMap);
	ByteArrayOutputStream ba = bs.getFile();
	
	OutputStream os = null;  
	ByteArrayInputStream bi = new ByteArrayInputStream(ba.toByteArray());
	try {
		byte[] buffer = new byte[512];  
		response.reset();  
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("image/jpeg");  
		response.setContentLength(bi.available());  
		os = response.getOutputStream();  
		int n;  
		while ((n = bi.read(buffer)) != -1) {  
		  os.write(buffer, 0, n);  
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (os != null){
			os.flush();  
			os.close();
		}
	}
	out.clear();
	out = pageContext.pushBody();
%>