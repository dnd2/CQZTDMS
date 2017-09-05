<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>机构人员积分兑现</title>
<script type="text/javascript">
	var id_array=new Array();
	//查询路径           
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/usermng/IntegationManage/integAgainstSelect.json";
	var title = null;
	var columns = [
				{header: "操作",dataIndex: 'INTEGRATION_AGAINST_ID',align:'center',renderer:myLink},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "身份证号",dataIndex: 'ID_NO',align:'center'},
				{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "所属银行", dataIndex: 'BANK', align:'center',renderer:getItemValue},
				{header: "职位", dataIndex: 'POSITION', align:'center',renderer:getItemValue},
				{header: "N-3年20%业绩", dataIndex: 'THIRD_PERFORMANCE_AGAINST', align:'center'},
				{header: "N-2年30%业绩", dataIndex: 'SENCOND_PERFORMANCE_AGAINST', align:'center'},
				{header: "N-1年50%业绩", dataIndex: 'FIRST_PERFORMANCE_AGAINST', align:'center'},
				{header: "N-3年20%认证", dataIndex: 'THIRD_AUTHENTICATION_AGAINST', align:'center'},
				{header: "N-2年30%认证", dataIndex: 'SECOND_AUTHENTICATION_AGAINST', align:'center'},
				{header: "N-1年50%认证", dataIndex: 'FIRST_AUTHENTICATION_AGAINST', align:'center'},
				{header: "N-3年20%年限", dataIndex: 'THIRD_YEAR', align:'center'},
				{header: "N-2年30%年限", dataIndex: 'SENCOND_YEAR', align:'center'},
				{header: "N-1年50%年限", dataIndex: 'FIRST_YEAR', align:'center'},
				{header: "本次兑现积分", dataIndex: 'THIS_AGAINST_INTEG', align:'center'},
				{header: "本次兑现金额", dataIndex: 'THIS_AGAINST_MONEY', align:'center'},
				{header: "是否兑现", dataIndex: 'IS_AGAINST', align:'center',renderer:getItemValue},
				{header: "兑现时间", dataIndex: 'AGAINST_DATE', align:'center'}
				//{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];
	
	function executeQuery(){
		url= "<%=contextPath%>/sales/usermng/IntegationManage/integAgainstSelect.json";
		__extQuery__(1);
	}
	function myLink(value,data,record){
		return String.format("<input type='checkbox' onclick='checkBoxSelect("+record.data.INTEGRATION_AGAINST_ID+",this)'/>");
	}
	//给数组array添加remove的方法
	Array.prototype.remove=function(dx){
    	//if(isNaN(dx)||dx>this.length){return false;}
	    for(var i=0,n=0;i<this.length;i++){
	        if(this[i]!=dx){
	            this[n++]=this[i]
	        }
	    }
	    this.length-=1
	}
	
	//选择了操作对象执行的方法
	function checkBoxSelect(against_id,obj){
		if(obj.checked){
			id_array.push(against_id);
		}else{
			id_array.remove(against_id);
		}
	}
	//积分兑现的回调函数
	function againstReturn(json){
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("积分兑现成功!") ;
		}else if(subFlag == 'nullvalue'){
			MyAlert("请至少选择一个积分兑现的人!") ;
		}else{
			MyAlert("积分兑现失败!") ;
		}
	}
	
	//覆写方法
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
		//	ps = json[Object.keys(json)[0]]; 
		}
		//生成数据集
		if(ps.records != null){
			$("againstSub").style.display="block";
			$("clearSub").style.display="block";
			$("_page").hide();
			$('myGrid').show();
			new createGrid(title,columns, $("myGrid"),ps).load();			
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			hiddenDocObject(2);
		}else{
			$("againstSub").style.display="none";
			$("clearSub").style.display="none";
			$("_page").show();
			$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("myPage").innerHTML = "";
			removeGird('myGrid');
			$('myGrid').hide();
			hiddenDocObject(1);
		}
	}
	//兑现操作
	function againstOperate(){
		var str="";
		for(var i=0;i<id_array.length;i++){
			if((id_array.length-1)!=i){
				str+=id_array[i]+',';
			}else{
				str+=id_array[i]+'';
			}
		}
		var urls="<%=contextPath%>/sales/usermng/IntegationManage/integAgainstOper.json?againstId="+str;
		makeFormCall(urls, againstReturn, "fm") ;
	}
	//积分清零操作
	function clearOperate(){
		var str="";
		for(var i=0;i<id_array.length;i++){
			if((id_array.length-1)!=i){
				str+=id_array[i]+',';
			}else{
				str+=id_array[i]+'';
			}
		}
		var urls="<%=contextPath%>/sales/usermng/IntegationManage/integClearOper.json?againstId="+str;
		makeFormCall(urls, clearReturn, "fm") ;
	}
	//积分清零回调函数
	function clearReturn(json){
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("积分清零成功!") ;
		}else if(subFlag == 'nullvalue'){
			MyAlert("请至少选择一个积分清零的人!") ;
		}else{
			MyAlert("积分清零失败!") ;
		}
	}
	function txtClr(value){
		$(value).value="";
	}
</script>
</head>

<body onload="executeQuery();">
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 积分管理 &gt; 人员积分兑现</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name" value=""  />
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="idNo" id="idNo" value=""  />	
			</td>
		</tr>
		
		 <tr>
			<td width="20%" class="tblopt"><div align="right">选择经销商：</div></td>
				<td width="39%" >
      			  <input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" />
                  <c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>                    
                    <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
    			</td>
    			<td align="right">职位：</td>
				  <td align="left">
				 		 <script type="text/javascript">
			                genSelBoxExp("position",9996,"",true,"mini_sel","","false",'');
			            </script> 
			       </td>
		</tr>
		<tr>
		<td align="right">职位状态：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("positionStatus",9994,"",true,"mini_sel","","false",'');
	            </script> 
	      </td>
	       <td align="right">年份：</td>
	        <td align="left">
		  	<select name="year">
		  		<option value="">-请选择-</option>
		  		<c:forEach var="s" items="${dateList}">
		  			<option value="${s}">${s}</option>
		  		</c:forEach>
		  	</select>
	       </td>
		</tr>
		
		<tr>
			<td align="right"></td>
		  <td align="left">
		  	<input type="hidden" name="isAgainst" value="29901002" id="isAgainst"/>
		 		
	      </td>
	       <td align="right"></td>
	        <td align="left">
	       </td>
		</tr>
	  <tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="executeQuery();" value="查询" id="addSub" />
		</td>
		<td align="right" colspan="2">
			<input name="pagesizes" id="pagesizes" value="10" datatype="0,is_digit,20" style="width:30px"/>
		</td>
	</tr>
	</table>
	<br />
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	
	
	<br/>
	<table border="0" align="left">
	<tr>
		<td align="left" >
			<input type="button" class="normal_btn" style="display: none;" onclick="againstOperate();" value="兑现" id="againstSub" />
		</td>
		<td align="center" width="10px;">
		</td>
		<td align="right" style="display:none;">
			<input type="button" class="normal_btn" style="display: none;" onclick="clearOperate();" value="清零" id="clearSub" />
		</td>
	</tr>
</table>
</form>
</div>
</body>

</html>
