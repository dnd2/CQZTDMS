<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件折扣率查询</title>
</head>
<body onload="__extQuery__(1);loadcalendar();"> <!-- onunload='javascript:destoryPrototype();' -->
<div class="wbox">
	 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 基础信息管理  &gt;
    配件基础信息维护 &gt; 配件折扣率维护</div>
	<form method="post" name="fm" id="fm" enctype="multipart/form-data">
	 <input type="hidden" name="curPage" id="curPage"/>
	  <div class="form-panel">
		  <h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" alt="" class="panel-query-title nav" />查询条件</h2>
		  <div class="form-body">
			 <table class="table_query">
				<tr>
				<td width="10%" class="table_query_right right" align="right">折扣类型：</td>
				<td width="20%"  >
				<script type="text/javascript">
						genSelBoxExp("DISCOUNT_TYPE",<%=Constant.PART_DISCOUNT_TYPE%>,"",true,"short_sel u-select","","false",'');
				</script>
				</td>
				<td width="10%" class="table_query_right right" align="right">有效日期：</td>
				<td width="20%" >
							<input name="startDate" id="t1" value="" type="text" class="short_txt calendar-input-long" datatype="1,is_date,10" group="t1,t2">
							<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间"/>
							&nbsp;至&nbsp;
							<input name="endDate" id="t2" value="" type="text" class="short_txt calendar-input-long" datatype="1,is_date,10" group="t1,t2">
							<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间"/>
				</td>
				</tr>
				<tr style="display:none" id="nameAndCode">
				<td class="table_query_right right" align="right">名称：</td>
				<td><input class="middle_txt" type="text"  name="DEALER_NAME" id="DEALER_NAME"/></td>
				<td class="table_query_right right" align="right">编码：</td>
				<td><input class="middle_txt" type="text"  name="PART_LODCODE" id="PART_LODCODE"/></td>
				</tr>
				<tr>
					<td class="table_query_right right" align="right">是否有效：</td>
					<td>
					<script type="text/javascript">
							genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"short_sel u-select","","false",'');
					</script>
					</td>
				</tr>
			<tr>
				<td class="txt-center" colspan="4" align="center">
				<input name="BtnQuery" id="queryBtn" class="u-button u-query" type="button" value="查 询" onclick="query();"/>
				<input name="DtlQuery" id="DtlQuery" class="u-button" type="button" value="明细查询" onclick="queryDtl();"/>
				<input class="u-button u-submit" type="button" value="新增" onclick="addInit();;"/>
				</td>
			</tr>
				</table>		  
		  </div>	
	  </div>
	  
	  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
    //autoAlertException();//输出错误信息
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountInfo.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
                {id:'action',header: "操作",sortable: false,dataIndex: 'DISCOUNT_ID',renderer:myLink ,align:'center'},
				{header: "折扣类型", dataIndex: 'DISCOUNT_TYPE', style: 'text-align: left;',renderer:getItemValue},
				{header: "折扣率", dataIndex: 'DISCOUNT_RATE', align:'center'},
				{header: "有效时间开始", dataIndex: 'VALID_FROM', align:'center',renderer:formatDate},
				{header: "有效时间结束", dataIndex: 'VALID_TO', align:'center',renderer:formatDate},
				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
				{header: "创建人", dataIndex: 'NAME', align:'center'},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}

		      ];
	    
//设置超链接  begin    
	  
	
	//设置超链接
	function myLink(value,meta,record){
		var discountType = record.data.DISCOUNT_TYPE;
		return String.format("<a href=\"#\" onclick='updateDiscount("+value+","+discountType+")'>[维护]</a>");
	}

    function query(){
    	$("DtlQuery").disabled=true;
    	document.getElementById("nameAndCode").style.display="none";
    	url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountInfo.json";
    	columns = [
   				{header: "序号", align:'center',renderer:getIndex},
   				{header: "折扣类型", dataIndex: 'DISCOUNT_TYPE', style: 'text-align: left;',renderer:getItemValue},
   				{header: "折扣率", dataIndex: 'DISCOUNT_RATE', align:'center'},
   				{header: "有效时间开始", dataIndex: 'VALID_FROM', align:'center',renderer:formatDate},
   				{header: "有效时间结束", dataIndex: 'VALID_TO', align:'center',renderer:formatDate},
   				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
   				{header: "创建人", dataIndex: 'NAME', align:'center'},
   				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
   				{id:'action',header: "操作",sortable: false,dataIndex: 'DISCOUNT_ID',renderer:myLink ,align:'center'}
   		      ];
    	__extQuery__(1);
    }
    
    function queryDtl(){
    	$("DtlQuery").disabled=true;
        url="<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountDtlInfo.json";
        columns = [
   				{header: "序号", align:'center',renderer:getIndex},
   				{header: "编码", dataIndex: 'DP_CODE', style: 'text-align:left'},
   				{header: "名称", dataIndex: 'DP_NAME', style: 'text-align:left'},
   				{header: "折扣类型", dataIndex: 'DISCOUNT_TYPE', style: 'text-align: left;',renderer:getItemValue},
   				{header: "折扣率", dataIndex: 'DISCOUNT_RATE', align:'center'},
   				{header: "有效时间开始", dataIndex: 'VALID_FROM', align:'center',renderer:formatDate},
   				{header: "有效时间结束", dataIndex: 'VALID_TO', align:'center',renderer:formatDate},
   				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
   				{header: "创建人", dataIndex: 'NAME', align:'center'},
   				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}
   		      ];
        document.getElementById("nameAndCode").style.display="";
        __extQuery__(1);
    }
	//维护
	function updateDiscount(value,discountType)
	{
		window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/updateDiscountInit.do?discountId='+value+'&discountType='+discountType;
	}
	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}
	function addInit(){
		window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/addPartDiscountInit.do';
	}

	function callBack(json){
		var ps;
		//设置对应数据
		if(Object.keys(json).length>0){
			keys = Object.keys(json);
			for(var i=0;i<keys.length;i++){
			   if(keys[i] =="ps"){
				   ps = json[keys[i]];
				   break;
			   }
			}
		}
		
		//生成数据集
		if(ps.records != null){
			$("_page").hide();
			$('myGrid').show();
			new createGrid(title,columns, $("myGrid"),ps).load();			
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			hiddenDocObject(2);
			$("DtlQuery").disabled="";
		}else{
			$("_page").show();
			$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("myPage").innerHTML = "";
			removeGird('myGrid');
			$('myGrid').hide();
			hiddenDocObject(1);
			$("DtlQuery").disabled="";
		}
	}
	//设置超链接 end
</script>
</div>
</body>
</html>