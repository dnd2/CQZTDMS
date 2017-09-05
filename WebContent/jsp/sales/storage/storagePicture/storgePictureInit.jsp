<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
 <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; text-align:center; 
			       background-color: #ffffff; }   
			#flashContent { display:none; }
        </style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
var strFeatures = "left=0,screenX=0,top=0,screenY=0";  
if (window.screen){
    //获取屏幕的分辨率
     var maxh = screen.availHeight-30;
     var maxw = screen.availWidth-10;
     strFeatures += ",height="+maxh;
     strFeatures += "innerHeight"+maxh;
     strFeatures += ",width="+maxw;
     strFeatures += "innerwidth"+maxw;
}else{
    strFeatures +=",resizable";    
}
window.open('<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/vehiclePictureFlexInit.do','图形管理',strFeatures);
</script>
</head>
	<body>
   </body>
</html>