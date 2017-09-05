<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
    String dealerLevel = request.getParameter("DEALERLEVEL");
    String campaignId = (String)request.getAttribute("CAMPAIGNID");
  
%>
<script>

var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/sales/marketmanage/plancommon/MarketDealerTree/initOrgTree.json";
var DEALERLEVEL="<%=dealerLevel%>";
var campaignId="<%=campaignId%>";

var tree_root_id = {"tree_root_id" : ""};
var subStr = "funlist";
function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		orgId = prolistobj[i].orgId;
		orgName = prolistobj[i].orgName;
		orgCode = prolistobj[i].orgCode;
		parentOrgId = prolistobj[i].parentOrgId;
		a.add(orgId,addNodeId,orgName,orgCode);
		//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
	}
	a.draw();
} 		
var createModelTree =function(){
	goToSelect();
 }
 	



function productPos(id) {
	var orgId = a.aNodes[id].id;
	$('ORGID').value = orgId;
	setValue();
	__extQuery__(1);
 }
 
function createTree(reobj) {
	var prolistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	for(var i=0; i<prolistobj.length; i++) {
		orgId = prolistobj[i].orgId;
		orgName = prolistobj[i].orgName;
		orgCode = prolistobj[i].orgCode;
		parentOrgId = prolistobj[i].parentOrgId;
		if(parentOrgId == "-1") { //系统根节点
			a.add(orgId,"-1",orgName,orgCode);
				
		} else {
			a.add(orgId,parentOrgId,orgName,orgCode);
			//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
		}
	}
	a.draw();

 }
function setValue()
{
	$('DEALERLEVEL').value = DEALERLEVEL;
	$('campaignId').value = campaignId;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);" >
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
	 <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ORGID" name="ORGID" value="" />
		<input type="hidden" id="DEALERLEVEL" name="DEALERLEVEL" value="" />
		<input type="hidden" id="campaignId" name="campaignId" value="" />
		
		<table class="table_query" >
		    <tr>
		    	<td class="table_query_2Col_label_5Letter" nowrap="nowrap" >区域：</td>
				<td><select class="short_sel" id="txt1" name="DRLPROVINCE"></select></td>
				<td><input name="button2" type="button" class="cssbutton" id="queryBtn1" onclick="goToSelect();" value="查 询"/></td>
                <td><input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" /></td>
		    </tr>
		    </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />	
	   </form>
		<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="setCheckModel();" value="确认" />               
		</div>
		</div>
		
		
<script type="text/javascript">
<!--

var url = "<%=ctx%>/sales/marketmanage/plancommon/MarketDealerTree/allOrgQuery.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
                {header: "选择",   dataIndex: 'ORG_ID', width:"10px",renderer:myLink},
				{header: "所属区域",dataIndex: 'ORG_NAME',width:"40px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;

    return "<input type='checkbox' name='cb'  id='"+value+"' value = '"+value+"'/>" 
    	 + "<input type='hidden' name='cbh' id=\""+data.DEALER_CODE+"\"  value=\""+data.DEALER_CODE+","+data.DEALER_SHORTNAME+","+data.ORG_NAME+","+getRegionName(data.PROVINCE_ID)+"\" />";
  
}


var setModel = function (dealerId,dealerShortname,dealerCode,orgName){
	if(parent.$('inIframe'))
	{
	 parent.$('inIframe').contentWindow.getDealerInfo(dealerId,dealerShortname,dealerCode,orgName);
	  _hide();
	}else
	{
		parent.$('inIframe').contentWindow.getDealerInfo(dealerId,dealerShortname,dealerCode,orgName);
		parent._hide();	
	}
}

// modify by zouchao@2010-08-27
function setCheckModel(){
	var reCode="";
	
	var groupCheckBoxs=document.getElementsByName("cb");
	
	var arr = document.getElementsByName("cbh");
	
	var str="";
	
	if(!groupCheckBoxs)return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		if(groupCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0){
				reCode += "," + groupCheckBoxs[i].value;
				str += "@" + arr[i].value;
			}else{
				reCode = reCode+groupCheckBoxs[i].value;
				str = str+arr[i].value;
			}	
	    }
	}
	
	if(reCode.length>0){
		if(parent.$('inIframe'))
	{
		parent.$('inIframe').contentWindow.getDealerInfo(reCode,str);
		
		_hide();	
	}else
	{
		parent.$('inIframe').contentWindow.getDealerInfo(reCode,str);
		
		parent._hide();
	}
	
	}
	
	_hide();
}
function checkAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=true;
	}
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
	}
}
function goToSelect(){
	$('ORGID').value="";
	  setValue();
	__extQuery__(1);
	
}
function getIndex(){}
//--
</script>	
</body>
</html>