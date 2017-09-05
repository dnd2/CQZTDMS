<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
function doInit()
{  
}
</script>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;新增业务范围</div>
 <form method="post" name = "fm"  id="fm">
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
  <input id="DEALERLEVEL" name="DEALERLEVEL" type="hidden" value="${DEALERLEVEL}"/>
   <input id="DEALERTYPE" name="DEALERTYPE" type="hidden" value="${DEALERTYPE}"/>
    <input id="DEALERSTATUS" name="DEALERSTATUS" type="hidden" value="${DEALERSTATUS}"/>
 <input id="areaId" name="areaId" type="hidden" value=""/>
     <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
     <tr><th colspan="3"><img src="<%=contextPath%>/img/nav.gif" />业务范围列表 </th></tr>
     <tr>
     <th><input type="checkbox" name="cb" id="cb" onClick="selectAll();"/>全选</th>
     <th>业务范围代码</th>
     <th>业务范围名称</th>
     </tr>
     <c:forEach items="${businessList}" var="bl" >
     <tr class="table_list_row1">
     <td><input type="checkbox" name="jb"  value="<c:out value="${bl.AREA_ID}"/>"/></td>
     <td><c:out value="${bl.AREA_CODE}"/></td>
     <td><c:out value="${bl.AREA_NAME}"/></td>
     </tr>
     </c:forEach>
    </table>
     <table class=table_query>
	 <tr>
	 <td>
	<input type="button" value="添加" name="saveBtn" class="normal_btn" onclick="saveBusinessInfo()"/>	
	<input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" /></td>
	</tr>
   </table>
</form>

<script type="text/javascript" >
function saveBusinessInfo()
{
  var businessArea=document.getElementsByName("jb");
  var dealerId=document.getElementById("DEALER_ID").value;
  var DEALERLEVEL=document.getElementById("DEALERLEVEL").value;
  var DEALERTYPE=document.getElementById("DEALERTYPE").value;
  var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
  var areaId=""; 
  if(!businessArea||businessArea.length==0)return;
  for(var i=0;i<businessArea.length;i++)
  {
	  if(businessArea[i].checked)
	  {
		  if(areaId.length>0)
		  areaId+=","+businessArea[i].value;
		  else
		  areaId+=businessArea[i].value;
	  }
  }
  
  if(areaId.length==0)
  {
	  MyAlert("请选择记录!");
	  return;
  }
 document.getElementById("areaId").value=areaId;
	 if(confirm("确认添加吗？"))
	{
			
			if(DEALERLEVEL==<%=Constant.DEALER_LEVEL_01%>){
					if(DEALERTYPE==<%=Constant.DEALER_TYPE_DVS%>||DEALERTYPE==<%=Constant.DEALER_TYPE_JSZX%>||DEALERTYPE==<%=Constant.DEALER_TYPE_QYZDL%>){
							if(DEALERSTATUS==<%=Constant.STATUS_ENABLE%>){
								 sendAjax('<%=contextPath%>/sysmng/dealer/DealerInfo/querySameBusiness.json',showResultCodeCheck,'fm');
								}
							else{
								 toSubmit();
								}
						}
					else{
						 toSubmit();
						}
				}

			else{
				 toSubmit();
				}
		
		
	} 
}

function showResultCodeCheck(obj){
	
	var msg=obj.msg;

	if(msg=='true'){
	
			 toSubmit();
				
	}else if(msg=='flase1')
		{
		
		MyAlert("同一经销商不能维护到多个基地");
	}
	else if(msg=='flase2'){
		MyAlert("同一基地同一经销商公司不为售后已存在一级经销商");
		}
	
}
function toSubmit(){
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/saveBusiness.json',showResult,'fm');
}
//回调方法
function showResult(json){
	if(json.returnValue == '1'){
		parentContainer.parentMonth();
	 	_hide();
	}else{
		windows.parent.MyAlert("操作失败！请联系系统管理员！");
	}
}
function selectAll()
{
	var businessArea=document.getElementsByName("jb");
	var cb=document.getElementById("cb");
	if(!businessArea||businessArea.length==0)return;
	for(var i=0;i<businessArea.length;i++)
	{
        if(cb.checked)
		businessArea[i].checked=true;
        else
        businessArea[i].checked=false; 
	}
	  
}
</script>

</body>
</html>
