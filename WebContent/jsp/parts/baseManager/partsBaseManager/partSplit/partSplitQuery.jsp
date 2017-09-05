<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件拆合比例查询</title>

</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 配件拆合比例设置
</div>
<form method="post" name ="fm" id="fm">
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	    <tr>
	      <td width="10%" class="table_query_right" align="right" >总成件编码：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/>
	      <input type="hidden" name="PART_CODE" id="PART_CODE"/>
	      <input type="hidden" name="PART_CNAME" id="PART_CNAME"/>
	      <input type="hidden" name="PART_ID" id="PART_ID"/>
	      <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','false')"/>
	      </td>
	      <td width="10%" class="table_query_right" align="right" >分总成件编码：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text"  name="SUBPART_OLDCODE" id="SUBPART_OLDCODE"/>
	      <input type="hidden" name="SUBPART_CODE" id="SUBPART_CODE"/>
	      <input type="hidden" name="SUBPART_CNAME" id="SUBPART_CNAME"/>
	      <input type="hidden" name="SUBPART_ID" id="SUBPART_ID"/>
	       <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo('SUBPART_CODE','SUBPART_OLDCODE','SUBPART_CNAME','SUBPART_ID','false')"/>
	      </td>
	      <td  width="10%" class="table_query_right" align="right" >是否有效：</td>
	      <td width="20%"  >
	      <script type="text/javascript">
		       genSelBoxExp("STATE",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
		  </script>
	      </td>
      </tr>
      <tr>
		<td colspan="6" align="center" >
		  <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onClick="__extQuery__(1)"/>
		  <input class="normal_btn" type="button" value="新 增" 
		  onclick="window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/addPartSplitInit.do'"/>	    
		  </td>
	  </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/queryPartSplitInfo.json";
				
	var title = null;

	var columns = [
	            {header: "序号",width:'10%',  renderer:getIndex},
                {id:'action',header: "操作",sortable: false,dataIndex: 'SPLIT_ID',renderer:myLink , style: 'text-align:left'},
				{header: "总成件配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
				{header: "总成件配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
				{header: "分总成件编码", dataIndex: 'SUBPART_OLDCODE',  style: 'text-align:left'},
				{header: "分总成件名称", dataIndex: 'SUBPART_CNAME',  style: 'text-align:left'},
				{header: "拆合件数量", dataIndex: 'SPLIT_NUM',  style: 'text-align:center'},
				//{header: "拆合件成本比例", dataIndex: 'COST_RATE',  style: 'text-align:center'},
				{header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center',renderer:getItemValue}

		      ];

//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{    
        var state = record.data.STATE;
        if(state=='<%=Constant.STATUS_DISABLE %>'){
            return String.format("<a href=\"#\" onclick='viewSplit(\""+record.data.PART_ID+"\")'>[查看]</a><a href=\"#\" onclick='valid(\""+record.data.PART_ID+"\")'>[有效]</a>");
        }	    
  		return String.format("<a href=\"#\" onclick='viewSplit(\""+record.data.PART_ID+"\")'>[查看]</a><a href=\"#\" onclick='updateSplit(\""+record.data.PART_ID+"\")'>[修改]</a><a href=\"#\" onclick='cel(\""+record.data.PART_ID+"\")'>[失效]</a>");
	}
	
	//查看配件拆合比例信息
	function viewSplit(value){
	     OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/viewPartSplitInfo.do?partId='+value,800,500);
	}
	
	//打开配件拆合比例修改页面
	function updateSplit(value)
	{
		window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/queryPartSplitDetail.do?partId='+value;
	}
	
	//失效
	function cel(value){
	    MyConfirm("确定要失效?",celAction,[value]);
	}
	
	//有效
	function valid(value){
	     MyConfirm("确定要有效?",validAction,[value]);
	}
	
	function celAction(value){
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/celPartSplit.json?partId='+value+'&curPage='+myPage.page;
	   makeNomalFormCall(url,handleCel,'fm');
	}
	
	function validAction(value){
	    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/validPartSplit.json?partId='+value+'&curPage='+myPage.page;
	    makeNomalFormCall(url,handleCel,'fm');
	}
	
	function handleCel(jsonObj) {
	  if(jsonObj!=null){
	     var success = jsonObj.success;
	     MyAlert(success);
	      __extQuery__(jsonObj.curPage);
	  }
   }
   function reloadAction(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}
	
</script>
</div>
</body>
</html>