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
<title>客户投诉查询(总部)</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		selModel();      //取得车系生成下拉框
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉查询(总部)</div>
 <form method="post" name = "fm" >
 <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
          <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户查询</th>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">客户姓名：</td>
		    <td><input type='text'  class="middle_txt" name="linkman"  id='linkmanId' value=""/></td>
		    <td class="table_query_2Col_label_6Letter">客户电话：</td>
		    <td><input type='text'  class="middle_txt" name="tel"  id='telId' value=""/></td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">投诉时间：</td>
		    <td><input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            	&nbsp;至&nbsp;
            	<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            </td>
		    <td class="table_query_2Col_label_6Letter">所购车型：</td>
		   	<td id="aaa">
		    </td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">
		        <input type="radio" name = "complainobject" id="ownOrgRd" onclick="checkOwnOrg()" value="0"/>所属区域：</td>
		    <td>
		    	<input type="text"  class="middle_txt" name="ownOrgCode"  id="ownOrgCode" value=""/>
		    	<input name="ownOrg" type="button" id="ownOrgBtn" class="mini_btn" onclick="showOrg('ownOrgCode' ,'ownOrgId' ,false,'')" value="&hellip;" disabled/>	
		    	<input name="ownOrgId" type="hidden" id="ownOrgId"/>
		    	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('ownOrgCode','ownOrgId');"/>    
		    </td>
		    <td class="table_query_2Col_label_6Letter">
		    	<input type="radio" name = "complainobject" id="compDlr" value="1" onclick="checkCompDLR()" checked/>投诉经销商：</td>
		    <td>
		    	<input class="middle_txt" id="compDealerCode" name="compDealerCode" value="" type="text"/>
            	<input class="mini_btn" type="button"  id="comDealerBtn" value="&hellip;" onclick="showOrgDealer('compDealerCode','compDealerId','false','',true)"/> 
            	<input type="hidden" name="compDealerId" id="compDealerId" value=""/>    
            	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('compDealerCode','compDealerId');"/>    
		    </td>
	      </tr>
		  <tr>
            <td class="table_query_2Col_label_5Letter">投诉来源：</td>
            <td><script type="text/javascript">
            	genSelBoxExp("compSource",<%=Constant.COMP_SOURCE_TYPE%>,"",true,"short_sel","","false",'');
                </script>
            </td>
            <td class="table_query_2Col_label_6Letter">投诉等级：</td>
            <td><script type="text/javascript">
            	genSelBoxExp("compLevel",<%=Constant.COMP_LEVEL_TYPE%>,"",true,"short_sel","","false",'');
                </script>
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter">投诉类型：</td>
            <td>
            	<script type="text/javascript">
            	genSelBoxExp("compType",<%=Constant.COMP_TYPE_TYPE%>,"",true,"min_sel","onchange='selType()'","true",'');
                </script>
            	<span id="compTypechng">
            	<select name="compType2" class="min_sel">
				<option value="">-请选择-</option>				
			    </select>
            	</span>
            </td>
            <td class="table_query_2Col_label_6Letter">投诉状态：</td>
            <td>
            	<script type="text/javascript">
            	genSelBoxExp("compStatus",<%=Constant.COMP_STATUS_TYPE%>,"",true,"min_sel","onchange='selectAction()'","true",'');
                </script>
                &nbsp;&nbsp;&nbsp;&nbsp;
                  处理中状态：
                <span id="statuschng">
                <select name="auditResult" class="min_sel">
				<option value="">-请选择-</option>				
			    </select>
			    </span>
			   
            </td>
          </tr>
           <tr>
            <td class="table_query_2Col_label_5Letter">投诉编号：</td>
            <td><input type='text'  class="middle_txt" name="compCode"  id='compCode' value=""/></td>
            <td class="table_query_2Col_label_6Letter">DMS处理：</td>
            <td><select name="dmsStatus" class="short_sel">
               <option value="1">处理</option>
               <option value="0">不处理</option>
            </select> </td>
          </tr>
          <tr><td class="table_query_2Col_label_5Letter">车牌号：</td>
              <td><input type='text'  class="middle_txt" name="licenseNo"  id='licenseNo' value=""/></td>
          </tr>
          <tr>
          <td align="center" colspan="5">
          <input name="searchBtn" type="button" class="normal_btn" onclick="__extQuery__(1);showExport();" value="查询" /></td>
         </tr>
     </table> 
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</br>
<form name="form1" style="display:none">
   <table align="center" class="table_query" >
      <tr align="center">
        <th align="center">
        	<input name="expData" type="button"  id="assignBtn" class="long_btn" onclick="doExport();" value="导出到Excel" />
        	<input name="expAllData" type="button"  id="assignBtn" class="long_btn" onclick="doExportAll();" value="全部导出" />
        </th>
      </tr>
  </table>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/search/ComplaintSearch/queryComplaintOem.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"compIds\")'/>", width:'8%',sortable: false,dataIndex: 'COMP_ID',renderer:myCheckBox},
				{header: "投诉编号", dataIndex: 'COMP_CODE', align:'center'},
				{header: "客户名称",sortable: false,dataIndex: 'LINK_MAN',align:'center'},
				{header: "联系电话", dataIndex: 'TEL', align:'center'},
				{header: "所购车型", dataIndex: 'GROUP_NAME', align:'center'},
				//{header: "车牌号", dataIndex: 'LICENSE_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'PROVINCE', align:'center',renderer:getRegionName},
				{header: "投诉经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "投诉来源", dataIndex: 'COMP_SOURCE', align:'center',renderer:getItemValue},
				{header: "投诉等级", dataIndex: 'COMP_LEVEL', align:'center',renderer:getItemValue},
				{header: "投诉类型", dataIndex: 'COMP_TYPE', align:'center',renderer:getItemValue},
				{header: "投诉时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "投诉状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "处理结果", dataIndex: 'AUDIT_RESULT', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'COMP_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//处理的超链接
	function myLink(value,meta,record){
  		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='明细'/>");
	}
	
	//处理的ACTION设置
	function viewDetail(value){
		fm.action = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/viewComplaintDetail.do?compId=' + value;
	 	fm.submit();
	}
	
	
	//动态显示投诉状态和处理状态的联动
	function selectAction(){
		if(document.fm.compStatus.value == '<%=Constant.COMP_STATUS_TYPE_02%>') {
			var sc = genSelBoxStrExp('auditResult','<%=Constant.AUDIT_RESULT_TYPE%>','',true,'min_sel','','true','');
			var pu = document.getElementById("statuschng").innerHTML = sc;
		}else{
			var sc = genSelBoxStrExp('auditResult','','',true,'min_sel','','true','');
			var pu = document.getElementById("statuschng").innerHTML = sc;
		}
	}
	
	// 投诉类型的联动显示
function selType(){
	if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_01%>') {
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_QUALITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
			
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_02%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_MEDIUM_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_03%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_04%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACCESSORIES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_05%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_CONSULTS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_06%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_07%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_HELPS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_08%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES_DEALER%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_09%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACTIVITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_10%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_OTHER_PROBLEMS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else{
			var sc = genSelBoxStrExp('compType2','','',true,'min_sel','','true','');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		}
}
	// 取得所有车系
	function selModel()
	{
		makeNomalFormCall('<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/getModel.json',showModelBack,'fm');
	}
	
	// 通过传回的查询结果生成下拉框
	function showModelBack(json)
	{
		var seriesList=json.seriesList
    	var str="";
    	str += "<select id='modelCode' name='modelCode' class='short_sel'>";
    	str +=seriesList;
    	str += "</select>";
    	document.getElementById("aaa").innerHTML = str;
	}  
	
	// 单选按钮灰显
	function checkOwnOrg(){
		document.getElementById("comDealerBtn").disabled = true;
		document.getElementById("ownOrgBtn").disabled = false;
	
	}
	
	// 单选按钮灰显
	function checkCompDLR(){
		document.getElementById("comDealerBtn").disabled = false;
		document.getElementById("ownOrgBtn").disabled = true;
	
	}
	
	// 清空按钮
	function clrTxt(txtCode,txtId){
	    	document.getElementById(txtCode).value="";
	    	document.getElementById(txtId).value="";
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='compIds' value='" + value + "' />");
	}
	//判断是否显示导出按钮
	function showExport(){
		document.form1.style.display = "inline";
	}
	//导出按钮
	function doExport() {
		var cnt = 0;
		var chk = document.getElementsByName("compIds");//选定的投诉编号
		var l = chk.length;
		for(var i=0; i < l; i++){
			if (chk[i].checked) {
		       cnt++;
			}
		 }
		if (cnt == 0) {
		    MyAlert("请选择要导出的投诉编号");
		    return;
		} else {
	    	MyConfirm("确认导出", exportConfirm);
		}
	}
	
	function doExportAll() {
		MyConfirm("确认导出", exportConfirmAll);
	}
	
	//导出确认
	function exportConfirm() {
		var url = '<%=contextPath%>/customerRelationships/export/ComplaintExpImp/expComplaint.do';
		fm.action = url;
		fm.submit();
	}
	
	//全部导出确认
	function exportConfirmAll() {
		var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/expAllComplaint.do';
		fm.action = url;
		fm.submit();
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
