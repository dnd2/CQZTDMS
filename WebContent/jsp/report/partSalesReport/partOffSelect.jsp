<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String inputOldCode = request.getParameter("INPUTOLDCODE");
    String isMulti = request.getParameter("ISMULTI");
    String flag = request.getParameter("FLAG");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件流失件选择框</title>

<script language="JavaScript">

</script>
</head>
<body onload="__extQuery__(1);" onbeforeunload="doSupp();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择配件</div>
<form method="post" name ="fm" id="fm">
	<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" value=""/>
	<input id="FLAG" name="FLAG" type="hidden" value="<%=flag%>"/>
	<table class="table_edit">
    <tr>
      <td width="10%" nowrap="nowrap">配件编码：</td>
      <td width="10%" nowrap="nowrap">
      	<input class="normal_txt" id="partOldCode" name="partOldCode" value="" type="text"/>
      </td>
      <td width="10%" nowrap="nowrap">配件名称：</td>
      <td>
      	<input name="partCname" type="text" id="partCname"  class="normal_txt"/>
      </td>
      <td align="right">件号：</td>
      <td>
          <input name="partCode" type="text" id="partCode"  class="normal_txt"/>
      </td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      	<input type="button" name="closeBtn" id="closeBtn"  value="关闭"  class="normal_btn" onClick="_hide();" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisAllClick()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="doConfirm();" value="确认" />
               	<input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
		</div>
		<script language="JavaScript">
		var isMulti="<%=isMulti%>";
	    	if(isMulti == "true"){
		    	document.getElementById("sel").style.display = "";
	    	}
	    	else
	    	{  
	    		document.getElementById("sel").style.display = "none";
	    	}	
	    </script>
<script type="text/javascript" >


    
	var myPage;

	var url = "<%=contextPath%>/report/partReport/partSalesReport/PartOffReport/queryPartInfo.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:seled},
				{header: "配件代码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
				{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		      
	function seled(value,meta,record){
		var data = record.data;
		var isMulti = "<%=isMulti%>";
 		if(isMulti == "false"){
 			return "<input type='radio' name='singleSel' id='singleSel"+value+"' onclick=singleSelect('"+ data.PART_OLDCODE +"'); />";
 		}else{
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ data.PART_OLDCODE +"' />";
        }
    }

    function singleSelect(partOldCode){
    	 $('PART_OLDCODE').value = partOldCode;
    	 _hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
		var inputOldCode = "<%=inputOldCode%>";
		var partOldCode = document.getElementById("PART_OLDCODE").value;
		if(partOldCode && partOldCode.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(inputOldCode)) {
					top.$(inputOldCode).value = partOldCode;
				}
			}else{
				parentDocument.getElementById(inputOldCode).value = document.getElementById("PART_OLDCODE").value;
			}
		}
	}
	
	
	function doDisAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = false;
		}
	}

	function checkAll(){
		var groupCheckBoxs=document.getElementsByName("checkCode");
		if(!groupCheckBoxs) return;
		for(var i=0;i<groupCheckBoxs.length;i++)
		{
			groupCheckBoxs[i].checked=true;
		}
	}
	
	function doConfirm()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择配件！");
        }else{
	        var OldCodes = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var partOldCode = chk[i].value;
				        if(OldCodes){
				        	OldCodes += "," + partOldCode;
				        }
				    	else{
				    		OldCodes = partOldCode;
				    	}
				    }    
				}				
			}
	
			if(OldCodes && OldCodes.length > 0){
			   $('PART_OLDCODE').value = OldCodes;
			}   
			_hide();
		}
	}
</script>
</body>
</html>