<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.util.Map"%>
<html> 
<head>
<%
	String contextPath = request.getContextPath();	
	//保存用户信息
	Map<String, Object>  userMap =(Map<String, Object>)request.getAttribute("userMap");
	String poseId="";
	String userId="";
	String materialId="";
	String boDeId="";
	//String orgId="";
	String yelId="";
	String order_type="";
	String specialOrderNo="";
	int boNum=0;
	int allocaNum=0;
	if(userMap!=null){
		poseId=userMap.get("poseId").toString();
		userId=userMap.get("userId").toString();
		materialId=userMap.get("materialId").toString();
		boDeId=userMap.get("boDeId").toString();
		boNum=Integer.parseInt(userMap.get("boNum").toString());
		allocaNum=Integer.parseInt(userMap.get("allocaNum").toString());
		//orgId=userMap.get("orgId").toString();
		yelId=userMap.get("yelId").toString();
		order_type=userMap.get("order_type").toString();
		specialOrderNo=userMap.get("specialOrderNo")==null?"-9999":userMap.get("specialOrderNo").toString();
	}
	
%>
 <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; text-align:center; 
			       background-color: #ffffff; }   
			#flashContent { display:none; }
        </style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/jsp/sales/storage/storagePicture/history/history.css"/>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/sales/storage/storagePicture/history/history.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/sales/storage/storagePicture/swfobject.js"></script>
 <script type="text/javascript">
 			var thisAllocaNum=window.opener.thisAllocaNum;
 	 		var checkVehicleObj=window.opener.checkVehicleObj;
 	 		var maxNum=window.opener.maxNum;
 			var userId=<%=userId%>;
 			var poseId=<%=poseId%>;
 			var materialId=<%=materialId%>;
 			var boNum=<%=boNum%>;
 			var allocaNum=<%=allocaNum%>;
 			var yelId=<%=yelId%>;
 			var order_type=<%=order_type%>;
 			var specialOrderNo=<%=specialOrderNo%>;
            var swfVersionStr = "10.0.0";
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {userId:userId,poseId:poseId,pageType:"allocaPicture",materialId:materialId,boNum:boNum,allocaNum:allocaNum,thisAllocaNum:thisAllocaNum,checkVehicleObj:checkVehicleObj,maxNum:maxNum,yelId:yelId,order_type:order_type,specialOrderNo:specialOrderNo};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#ffffff";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "storgePicture";
            attributes.name = "storgePicture";
            attributes.align = "middle";
            swfobject.embedSWF(
                "<%=request.getContextPath() %>/jsp/sales/storage/storagePicture/storgePicture.swf", "flashContent", 
                "100%", "100%", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        </script>
<script type="text/javascript">
function save(arr){	
	var arrayAllObj=new Array;
	arrayAllObj=arr;
	var changeType=window.opener.changeType; //获取选择类型（配车管理为多选:more,配车调整位单选:only）
	var boDeId=<%=boDeId%>;	//组板明细ID
	if(changeType=="more"){	
		if(arrayAllObj.length>0){
			MyAlert("选车成功");
			window.opener.addRowVehicle(arrayAllObj,boDeId,"null");
			window.close();
		}else{
			MyAlert("请选择车辆信息");
		}
	}else if(changeType=="only"){
		if(arrAllObj.length>0){
			var delId=window.opener.delID;//具体哪辆配车ID
			if(delId!="undefined"){
				window.opener.changeRowVehicle(arrAllObj,boDeId,delId);
				window.close();
			}else{
				MyAlert("连接错误!请重新打开该页面！");
				window.close();
			}
		}else{
			MyAlert("请选择调整车辆信息");
		}
	}else{
		 MyAlert("连接错误!请重新打开该页面！");
		 window.close();
	}
}        
</script>
<title>Flex图形(配车管理)</title>
</head>
  <body>
        <!-- SWFObject's dynamic embed method replaces this alternative HTML content with Flash content when enough 
			 JavaScript and Flash plug-in support is available. The div is initially hidden so that it doesn't show
			 when JavaScript is disabled.
		-->
        <div id="flashContent">
        	<p>
	        	To view this page ensure that Adobe Flash Player version 
				10.0.0 or greater is installed. 
			</p>
			<script type="text/javascript"> 
				var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
				document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
								+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
			</script> 
        </div>
	   	
       	<noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" id="storgePicture">
                <param name="movie" value="storgePicture.swf" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#ffffff" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="storgePicture.swf" width="100%" height="100%">
                    <param name="quality" value="high" />
                    <param name="bgcolor" value="#ffffff" />
                    <param name="allowScriptAccess" value="sameDomain" />
                    <param name="allowFullScreen" value="true" />
                <!--<![endif]-->
                <!--[if gte IE 6]>-->
                	<p> 
                		Either scripts and active content are not permitted to run or Adobe Flash Player version
                		10.0.0 or greater is not installed.
                	</p>
                <!--<![endif]-->
                    <a href="http://www.adobe.com/go/getflashplayer">
                        <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />
                    </a>
                <!--[if !IE]>-->
                </object>
                <!--<![endif]-->
            </object>
	    </noscript>		
   </body>
</html>