<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 控制显示物料组信息(使用者：三包对象)  -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String ctx = request.getContextPath();
    String inputId = request.getAttribute("INPUTID").toString();
    String inputName = request.getAttribute("INPUTNAME").toString();
    String isMulti = request.getAttribute("ISMULTI").toString();
    String groupLevel = request.getAttribute("GROUPLEVEL").toString();
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var groupLevel="<%=groupLevel%>";
function setValue()
{
	$('#INPUTID')[0].value = inputId;
	$('#INPUTNAME')[0].value = inputName;
	$('#ISMULTI')[0].value = isMulti;
	$('#GROUPLEVEL')[0].value = groupLevel;
}
//--
</script>
</head>
<body   onload="goToSelect() ;">
<div class="wbox">
     <div class="navigation">     
		<img src="<%=ctx %>/img/nav.gif" />&nbsp;当前位置： 车型信息
	</div>
	 <form id="fm" name="fm" method="post">
	 <div class="form-panel">
		<h2><img src="<%=ctx%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" id="STRATEGYID" name="STRATEGYID" value="<%=request.getParameter("STRATEGYID")%>" />
		<table class="table_query" >
			<tr>
			   <td class="table_query_label" nowrap="nowrap" style="text-align:right">车系：
	           <select name="cxid" id="cxid" class="u-select" onchange="selectCategory(this.selectedIndex)">
	  	         <option value=""><c:out value="--请选择--"/></option>
				<c:forEach var="list" items="${list}">
					<option onclick="addoptions(<c:out value="'" escapeXml="false"/><c:forEach var="WRGROUP_ID" items="${list.CXZIDS}"><c:out value=",${WRGROUP_ID.WRGROUP_ID}"/></c:forEach><c:out value="'" escapeXml="false" />)" value="${list.TmVhclMaterialGroupPO.groupId}" >				         						      
						 <c:out value="${list.TmVhclMaterialGroupPO.groupName}"/>
					</option>
				</c:forEach>					
		</select>
				</td>
				<td nowrap="nowrap" class="table_query_label" style="text-align:right">车型代码：
				<select name="cxzid" id="cxzid" class="u-select" >
	  	               <option value=""><c:out value="--请选择--"/></option>
					<c:forEach var="cxzlist" items="${cxzlist}">
						<option value="${cxzlist.wrgroupId}" ><c:out value="${cxzlist.wrgroupName}"/></option>
					</c:forEach>
		       </select>
				</td>
				<td class="table_query_label" nowrap="nowrap" style="text-align:right">车型名称：
				  <input type="text" id="cxname" name="cxname"  class="middle_txt"/>
				</td>
		    </tr>
		    <tr>	
				<td colspan="6" style="text-align:center">
				  <input name="queryBtn" type="button" class="normal_btn" id="queryBtn" onclick="goToSelect();" value="查 询"/>&nbsp;&nbsp;
                  <input name="button" type="button" class="normal_btn" onclick="_hide();" value="关 闭" />
                </td>
		    </tr>
		    </table>
		    </div>
		    </div>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />	
	   </form>
		<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="normal_btn" onclick="checkAll();" value="全选" />
           <input class="normal_btn" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="normal_btn" onclick="setCheckModel();" value="确认" />               
		</div>
		<script language="JavaScript">
	    	if(isMulti == "true")
		    	document.getElementById("sel").style.display = "";
	    	else
	    	{
	    		document.getElementById("sel").style.display = "none";
	    	}	
	    </script>
		</div>
		</div>
		
<script type="text/javascript">
var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;
<!--

var url = "<%=ctx%>/claim/basicData/TreeGuaranteesStrategy/guaranteeGroupListQuery.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'GROUP_ID', width:"10px",renderer:myLink},
				{header:'车系',dataIndex:'PARENT_CODE',aling:'center'},
				{header:'车型组',dataIndex:'WRGROUP_NAME',aling:'center'},
				{header: "车型代码",dataIndex: 'GROUP_CODE',width:"40px"},
				{header: "车型名称",dataIndex: 'GROUP_NAME',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('#ISMULTI')[0].value=='true')
	{
		
		return "<input type='checkbox' name='cb' id=\""+data.GROUP_ID+"\"  value=\""+data.GROUP_ID+"\" />"
    }else
    {
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\")' />"  
    }
}
var setModel = function (groupId,groupName,groupCode){
	parentDocument.getElementById("carModelFrame").contentWindow.document.getElementById(inputId).value=reCode;
	_hide();	
}
function setCheckModel(){
	var reCode="";
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs)return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		if(groupCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0)
				reCode += "," + groupCheckBoxs[i].value;
			else
				reCode = reCode+groupCheckBoxs[i].value;
	    }
	}
	parentDocument.getElementById("carModelFrame").contentWindow.document.getElementById(inputId).value=reCode;
	parentDocument.getElementById("carModelFrame").contentWindow.saveModel();
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
	$('#groupId')[0].value="";
	  setValue();
	__extQuery__(1);
	
}
//以下为二级级联JS YH 2010.11.25
var categoryWithForums={};
(function(){
 var selectForums=document.getElementById('cxzid');
 for(var i=0;i<selectForums.options.length;i++){
  categoryWithForums[selectForums.options[i].value]=selectForums.options[i];
 }
 //selectForums.options.length=0;
 selectCategory(0);
})();

function selectCategory(index){
 if($('#cxid')[0].options.length==0){
    return;
  }
 $('#cxid')[0].options[index].selected = true;
 $('#cxid')[0].options[index].onclick();
}

function addoptions(ids,select){
 var selectForums=document.getElementById('cxzid');
 selectForums.options.length=1;
 ids=ids.replace(" ").split(",");
 for(var i=1;i<ids.length;i++){
  selectForums.options.add(categoryWithForums[ids[i]]);
  if(ids[i]==select){
   selectForums.options[selectForums.options.length-1].selected=true;
   }
 }
 selectForums.focus();
}
</script>	
</body>
</html>