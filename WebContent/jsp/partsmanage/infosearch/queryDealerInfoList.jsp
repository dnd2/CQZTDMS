<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 供应商查询 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商查询</title>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body onbeforeunload="showReaction()">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：经销商查询
<form method="post" name ="fm" id="fm">
<input type="hidden" name="dcId" value="<c:out value="${dcId}"/>"/>
	<table class="table_edit">
     <tr>
      <td width="19%" align="right" nowrap>经销商代码：</td>
      <td colspan="2" align="left" >
           <input class="middle_txt" id="dealerCode" name="dealerCode" value="" type="text"/>
           <input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)"/>
      </td>
      <td align="right" nowrap="nowrap">经销商名称：</td>
       <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerName" style="cursor: pointer;" name="dealerName" type="text"/>
	  </td>
	  </tr>
	  <tr>
	    <td align="right" nowrap="nowrap">经销商级别：</td>
        <td colspan="2" align="left" >
            <script type="text/javascript">
 				 genSelBoxExp("dealerLevel",<%=Constant.DEALER_LEVEL%>,"",true,"short_sel","","false",'');
			</script>
	    </td>
      	<td class="table_query_2Col_label_2Letter">
      		<input type="button" name="BtnQuery" id="queryBtn" value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      	</td>
  	  </tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" type="button" value="保存" onclick="reactionConfirm();">&nbsp;
    		<input class="normal_btn" type="button" value="关闭" onclick="buttonClose()">
       </th> 
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/queryDealerInfoList.json";
				
	var title = null;

	var columns = [
	{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dealerIds\")' />", width:'8%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "经销商级别", dataIndex: 'DEALER_LEVEL', align:'center',renderer:getItemValue},
				{header: "所属大区", dataIndex: 'ORG_NAME', align:'center'}
		      ];
		    
//设置超链接  begin      
	
	//设置超链接
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='dealerIds' value='" + value + "'/>");
	}
	
	//添加供货方和经销商的关系
	function reactionConfirm()
	{
		var chk = document.getElementsByName("dealerIds");
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
             MyAlert("请选择要增加的经销商！");
             return;
        }
		conserveReaction();
	}
	
	
	//添加供货方和经销商的关系
	function conserveReaction(){
		makeNomalFormCall("<%=contextPath%>/partsmanage/infoSearch/DealerSupplierInfo/conserveReaction.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//回显方法
	function showForwordValue(json){
		if(json.returnValue == '1')
		{
			MyAlert("维护经销商成功！");
			_hide();//关闭子页面并刷新父页面
		}else
		{
			MyAlert("维护经销商失败！请联系系统管理员！");
		}
	}
	
	
	//刷新父页面
	function showReaction(){
		parentContainer.doInit();
	}
	
	//关闭按钮
	function buttonClose()
	{
		parent._hide();
	}
//设置超链接 end
	
</script>
</body>
</html>