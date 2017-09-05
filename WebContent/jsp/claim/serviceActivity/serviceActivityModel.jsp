<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-车型列表</title>
<%
	String contextPath = request.getContextPath();
%>
<%
//List<TtAsActivityBean> ManageModelList=(List<TtAsActivityBean>)request.getAttribute("ManageModelList");
//List<TmVhclMaterialGroupPO> list=(List<TmVhclMaterialGroupPO>)request.getAttribute("list"); 
%>
<script language="JavaScript" type="text/javascript">
//修改活动
function modifyActivity(){
	   
	 	var id="";
	 	if(document.FRM.groupId==null){
	 		MyAlert("无车型可选择!");
   	return;
	 	}
	 	if(document.FRM.groupId.length==null){
	 		if(document.FRM.groupId.checked==true){
			id+=document.FRM.groupId.value+",";
		}
	 	}
	for(var i =0; i<document.FRM.groupId.length;i++) {
		if(document.FRM.groupId[i].checked==true){
			id+=document.FRM.groupId[i].value+",";
		}
   }
   if(id.length<1){
   	//if(confirm("确认不选择任何车型?")){
	//document.FRM.action = "/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelOption.do?groupIds=" + id+"&activityId="+activityId;
	//document.FRM.submit();
   //	}else{
   	//  return false;
   //	}
	   MyDivAlert("请选择车型！");
   }else{
	   MyDivConfirm("是否确认增加？",sures,[id]);
	//document.FRM.action = "/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelOption.do?groupIds=" + id+"&activityId="+activityId;
	//document.FRM.submit();
   }
}

function sures(id){
	 var activityId=document.getElementById("activityId").value; 
	document.FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelOption.do?groupIds=" + id+"&activityId="+activityId;
	document.FRM.submit();
	
}
	function selectClick() {
    	if(document.FRM.groupId==null){
    	}
    	else if(document.FRM.groupId.length==null){
    	    document.FRM.groupId.checked=document.FRM.selectAll.checked;
    	}
        else if(document.FRM.groupId.length!=null){
        for(var i =0; i<document.FRM.groupId.length;i++) {
            document.FRM.groupId[i].checked=document.FRM.selectAll.checked;
        }
       }
    }
	//查询车型信息
	//function selectVhcl(){
	//	var activityId=document.getElementById("activityId").value;
	//	var vehicleSeriesList=document.getElementById("groupIdXi").value;
	//	FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelQuery.do?vehicleSeriesList="+vehicleSeriesList+"&activityId="+activityId;
//		FRM.submit();
	//}
	//全选回显
	function chexing(){
		var modelist=document.getElementById("ModelList").value;
		
		var modelNew=modelist.toString();
		if(!modelNew){return}
		var model=modelNew.split(",");
		for(var i=0;i<model.length;i++){
			document.getElementById(model[i]).checked="checked";
		  }
		}
           
	function selectedIndex(){
		var groupIdXi=document.getElementById("listSeries").value;
		var groupNew=groupIdXi.toString();
		if(!groupNew){return}
		var group=groupNew.split(",");
		for(var i=0;i<group.length;i++){
				 document.getElementById(group[i]).selected="selected";
		}
           
		}
 
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
  <form method="post" name="fm">
  	   <input type="hidden" name="ids" id="ids" value="<%=request.getAttribute("ids")%>"/>
	   <input type="hidden" name="ModelList" id="ModelList" value="<%=request.getAttribute("ModelList")%>"/>
	   <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>"/>
	   <input type="hidden" name="listSeries" id="listSeries" value="<%=request.getAttribute("listSeries")%>"/>
	   <input type="hidden" name="free" id="free" value="<%=request.getAttribute("free")%>"/>
  <table width=95% border=0 class="table_list">
  	<tr>
  	  <td  align="right">车型:</td>
  	  <td>
	  	<!-- <select name="groupIdXi" id="groupIdXi" class="short_sel" onchange="selectCategory(this.selectedIndex)"> -->
	  	<select name="groupIdXi" id="groupIdXi" class="short_sel">
	  	         <option value=""><c:out value="--请选择--"/></option>
				<%-- <c:forEach var="list" items="${cxzlist}">
					<option onclick="addoptions(<c:out value="'" escapeXml="false"/><c:forEach var="WRGROUP_ID" items="${list.CXZIDS}"><c:out value=",${WRGROUP_ID.WRGROUP_ID}"/></c:forEach><c:out value="'" escapeXml="false" />)" value="${list.TmVhclMaterialGroupPO.groupId}" >				         						      
						 <c:out value="${list.TmVhclMaterialGroupPO.groupName}"/>
					</option>
				</c:forEach> --%>	
				<c:forEach var="list" items="${cxlist}">
						<option value="${list.groupId}" ><c:out value="${list.groupCode}"/></option>
				</c:forEach>				
		</select>
      </td>
      <td  align="right">车型组:</td>
  	  <td>
	  	<select name="cxzid" id="cxzid" class="short_sel" >
	  	               <option value=""><c:out value="--请选择--"/></option>
					<c:forEach var="cxzlist" items="${cxzlist}">
						<option value="${cxzlist.wrgroupId}" ><c:out value="${cxzlist.wrgroupName}"/></option>
					</c:forEach>
		</select>
		
      </td>
      <td  align="right">车型名:</td>
  	  <td>
  	    <input type="text" id="cxname" name="cxname"  class="middle_txt"/>
      </td>
  	  <td width="10%" align="center">
  	   		 <input type="button" name="bt_edit_id" id="bt_edit_id2" class="normal_btn" value="查询" onclick="__extQuery__(1);"/>
  	  </td>
  	  <td width="10%" align="center">
  	    <input type="button" name="bt_close" id="bt_delete_id" class="normal_btn" value="关闭" onclick="parent.window._hide();" />
  	    </td>
    </tr>
  </table>
  <br/>
  <!--分页 begin --> 
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
<!-- 分页 end -->

  <br/>
  </form>
  <input type="button" name="bt_add" id="bt_edit_id" class="normal_btn" value="确定" onclick="subChecked();"/>

<br/>
<!--页面列表 begin --> 
<script type="text/javascript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelQuery.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header:'车型',dataIndex:'PARENT_CODE',aling:'center'},
				{header:'车型组',dataIndex:'WRGROUP_NAME',aling:'center'},
				{header: "车型ID", dataIndex: 'GROUP_ID', align:'center'},
				{header: "车型代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "车型费用", dataIndex: 'FREE', align:'center'}
				
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record){
	    $('free').value = record.data.FREE;
		return String.format("<input type='checkbox' onclick='doMySel("+value+")' name='orderIds' id='" + value + "' value='" + value + "' />");
		
	}
	function doMySel(value){
		var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/deleteThisModelId.json?id="+value; 
		makeNomalFormCall(url,myDelBak,'fm');
	}
	function myDelBak(json){
		if(json.flag){
			$('ids').value = json.ids ;
		}
	}
	/*
  	增加方法
  	参数：action : "add":增加
  	取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
function subChecked() {
	var str="";
	var chk = document.getElementsByName("orderIds");
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{          
			if(str)
			{  
				str += ","+chk[i].value;
			}else{
				str = chk[i].value;
			} 
			cnt++;
		}
	}
	if(cnt==0){
        MyDivAlert("请选择！");
        return;
    }else{        
    	 MyDivConfirm("是否确认增加？",checkedSuccess,[str]);
    }
}

function checkedSuccess(str){
   makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelOption2.json?groupIds='+str+'&free='+$('free').value,delBack,'fm','queryBtn');
   parent._hide();
   if (parent.$('inIframe')) {	         
	   //parentContainer.showFree($('free').value);	
	   parentContainer.showFree(0);
	}else {
	    parent.showFree($('free').value);
	 }
}

//新增回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("新增成功！");
		parent.window._hide();
	} else {
		MyDivAlert("新增失败！请联系管理员！");
	}
}

//回显复选框
function customerFunc(json){
	var chk = document.getElementById("ids").value;
	var chkNew=chk.toString();
	if(!chkNew){return}
	var chk2=chkNew.split(",");
	var modelist=document.getElementById("ModelList").value;
	var modelNew=modelist.toString();
	if(!modelNew){return}
	var model2=modelNew.split(",");
	for(var i=0;i<model2.length;i++){
		for(var j=0;j<chk2.length;j++){ 
			if(model2[i]==chk2[j]){
				try{
					document.getElementById(model2[i]).checked="checked";
					if('${type}' == '1')
					{
						document.getElementById(model2[i]).disabled='disabled';
					}
				    
				}catch(e){}
			}
		}
	}
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
 if($('groupIdXi').options.length==0){
    return;
  }
 $('groupIdXi').options[index].selected = true;
 $('groupIdXi').options[index].onclick();
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
<!--页面列表 end -->
</body>
</html>