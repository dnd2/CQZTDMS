<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%
	String contextPath = request.getContextPath();
	String reFlag = (String)request.getAttribute("REFLAG");
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	String dealerId = logonUser.getDealerId();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时查询</title>
<script>
	

</script>
</head>
<body>
<div id="wbox">
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时查询</div>
  <div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter" style="text-align:right">索赔车型组：</td>
            <td>
            <input type="text" value="" name="WRGROUP_CODE"  id ="WRGROUP_CODE" class="middle_txt" readonly="readonly" onclick="showLabor();"/>
            <input type="hidden" value="" name="WRGROUP_ID"  id ="WRGROUP_ID" class="middle_txt"/>
         	<!-- <input type="button" value="..." class="normal_btn" onclick="showLabor();"/> -->
         	<input type="button" value="清空" class="normal_btn" onclick="cleanInput();"/>
            </td>
            <td class="table_query_2Col_label_6Letter" style="text-align:right">工时大类代码：</td>
          	<td>
          		<input name="LABOUR_CODE_BIG" type="text" id="LABOUR_CODE_BIG"  class="middle_txt" />
          		<input name="coefficient " type="hidden" id="coefficient"  />
          		<input name="quantity" type="hidden" id="quantity"  />
          	</td>
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时大类名称：</td>
          	<td><input name="CN_DES_BIG" type="text" id="CN_DES_BIG"   class="middle_txt"/></td>
           </tr>
          <tr>
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时代码：</td>
          	<td><input name="LABOUR_CODE" type="text" id="LABOUR_CODE"   class="middle_txt" /></td>
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时名称：</td>
          	<td><input name="CN_DES" type="text" id="CN_DES"    class="middle_txt"/></td>
          	<td></td>
          </tr> 
		   <tr>    
		   <td colspan="6" style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>
            <%if(dealerId == null){ %>
            <input class="normal_btn" type="button" value="导出" name="add" onclick="exports();"/>
			<%} %>
           </td>
           <td>
           <div id="savebt" style="display:none">
					<input type="button"  value="" onclick="batchUpdate();" class="normal_btn"/>
				</div>
           </td>
           </tr>
       
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
</div>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/claimLaborQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: '车型组',align:'center',dataIndex:'WRGROUP_CODE'},
				{header: "工时代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},//,renderer:mySelect},				
				{header: "工时名称",sortable: false,dataIndex: 'CN_DES',align:'center'},
				{header: "工时大类代码",sortable: false,dataIndex: 'LABOUR_CODE_BIG',align:'center'},			
				{header: "工时大类名称",sortable: false,dataIndex: 'CN_DES_BIG',align:'center'},
				{header: "工时系数",sortable: false,dataIndex: 'LABOUR_QUOTIETY',align:'center'},
				{header: "索赔工时",sortable: false,dataIndex:'LABOUR_HOUR',align:'center',renderer:repale}
			//	{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	function batchUpdate(){
		if($('WRGROUP_CODE').value!=""){
			if($('LABOUR_CODE').value!=""||$('CN_DES').value!=""){
				OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/batchUpdateMain.do',200,200);
			

			}
			else{
				MyAlert("批量更新必须选择工时名称或者工时代码");
			}
		}
		else {
			MyAlert("批量更新必须选择车型组");
		}
	
	}
	function repale(value,meta,record)
	{
	   if(value.substring(0,1) == '.')
	   {
	     value = '0'+value;
	   }
	   return String.format(value);
	}
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do?ID="
			+ value + "\">[修改]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}

	function showLabor(){
		var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit1.do' ;
		OpenHtmlWindow(url,600,400);
	}
	function setLaborList(codes,Wcodes){
		 var scode="";
		 var wcode="";
			 for(var i=0;i<codes.length;i++){
				 scode+=codes[i]+",";
				 wcode += Wcodes[i]+",";
			 }
			 
		 	document.getElementById("WRGROUP_ID").value = scode.substring(0,scode.length-1);
		 	document.getElementById("WRGROUP_CODE").value = wcode.substring(0,wcode.length-1);
		}

	function cleanInput(){
		document.getElementById("WRGROUP_ID").value="";
		document.getElementById("WRGROUP_CODE").value='';
	}
	
//设置超链接 end

//设置超链接
function mySelect(value,meta,record){
 		return String.format(
        "<a href=\"#\" onclick='selbyid(\""+record.data.ID+"\")'>["+ value +"]</a>");
}

//详细页面
function selbyid(value){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborDetail.do?ID='+value,900,500);
}
  //新增
  function subFun(){
    location="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborAddInit.do";   
  }
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborDel.json?ID='+str,delBack,'fm','');
}

function getLabourPrice(var1,var2){
	$('coefficient').value=var1;
	$('quantity').value=var2;
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/batchUpdate.json?quantity2='+var2,updateBack,'fm','');
}
//删除回调方法：

function updateBack(){
	if(json.success != null && json.success == "true") {
		MyAlert("批量修改成功！");
		__extQuery__(1);
	} else {
		MyAlert("批量修改失败！请联系管理员！");
	}
	
}
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	fm.action="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do";
	fm.submit();
}

function exports(){
	fm.action="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborQueryExport.do";
	fm.submit();
}
</script>
</body>
</html>