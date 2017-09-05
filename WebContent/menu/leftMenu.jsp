<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="StyleSheet"type="text/css" href="<%=contextPath %>/style/dtree.css"/>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dtree.js"></script>
</head>
<body style="background-color:#FFF; margin:0px; padding:0px;">
	<div style=" margin:2px;">
	<script type="text/javascript">
		var thisid = parent.thisfunid;
		var sysfuns = parent.sysFuns.sysfun;
		var userfuns = parent.userFuns.userfuns;
		//控制页面树限制，树只从funcId长度为LEVEL_LIMIT开始显示
		var LEVEL_LIMIT = 6;
	
		function isVal(fid) {
			for(var j=0; j<userfuns.length; j++) {
				var mid = userfuns[j].funcId;
				if(mid.indexOf(fid) == 0) {
					return true;
				}
			}
			return false;
		}
		
        //取得所在行应缩进的空格数
		function getSpaces(fid,start){
			var resSpaces = '';
			if(fid.length>start){
			    MyAlert(fid.length+'=========='+start)
				var count = (fid.length-start)/2;
				for(var i=0;i<count;i++){
					resSpaces = resSpaces + '&nbsp;&nbsp;&nbsp;&nbsp;';
				}
			}
			return resSpaces;
		}
		
		d = new dTree('d');
		d.add(0,-1,'','','','');
		
			for(var j=0; j<userfuns.length; j++) {
				var mid = userfuns[j].funcId;
				if(mid.indexOf(thisid)==0){
					var pmid = mid.substring(0,mid.length-2);
					d.add(mid,pmid,'&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=contextPath %>/img/h.gif"></img>&nbsp;'+userfuns[j].funcName,'<%=request.getContextPath()%>'+userfuns[j].funcCode+'.do',userfuns[j].funcName,'inIframe');
				}
			}
		
		for(var i=0; i<sysfuns.length; i++) {
			var fid = sysfuns[i].funcId;
			if(fid.indexOf(thisid)==0){
				if(fid.length==LEVEL_LIMIT){
					d.add(fid,0,sysfuns[i].funcName,'');
				}else if(fid.length>LEVEL_LIMIT){
					var pfid = fid.substring(0,fid.length-2);
					d.add(fid,pfid,'&nbsp;&nbsp;'+sysfuns[i].funcName,'');
				}
		 	}			
		}

			for(var j=0; j<userfuns.length; j++) {
				var mid = userfuns[j].funcId;
				if(mid.indexOf(thisid)==0){
					var pmid = mid.substring(0,mid.length-2);
					d.add(mid,pmid,'&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=contextPath %>/img/h.gif"></img>&nbsp;'+userfuns[j].funcName,'<%=request.getContextPath()%>'+userfuns[j].funcCode+'.do',userfuns[j].funcName,'inIframe');
				}
			}
		document.write(d);
		d.closeAll();
		
		if(thisid != 1009)
		{
			d.o(1);
		}
	</script>
	</div>
</body>
</html>