<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrRulePO"%>
<%@page import="com.infodms.dms.po.TtAsWrGamePO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包策略新增</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔基础数据&gt;三包策略维护
<%
	List ruleList = (List)request.getAttribute("ruleList");
	List sysRuleList = (List)request.getAttribute("sysRuleList");
%>

<select id="RULE_ID" style="display:none"><%-- 业务规则 --%>
	<% 
	if(ruleList!=null && ruleList.size()>0){
	%>
	<%
	for(int i=0;i<ruleList.size();i++){
		TtAsWrRulePO rulePO = (TtAsWrRulePO)ruleList.get(i);
	%>
	<option value="<%=rulePO.getId()%>"><%=rulePO.getRuleName()%></option>
	<%		}%>
	<% } else { %>
	<option value="">请先维护业务类型三包规则</option>
	<% } %>
</select>
<select id="SYS_RULE_ID" style="display:none"><%-- 系统规则 --%>
	<% 
	if(sysRuleList!=null && sysRuleList.size()>0){
	%>
	<%
	for(int i=0;i<sysRuleList.size();i++){
		TtAsWrRulePO rulePO = (TtAsWrRulePO)sysRuleList.get(i);
	%>
	<option value="<%=rulePO.getId()%>"><%=rulePO.getRuleName()%></option>
	<%		}%>
	<% } else { %>
	<option value="">请先维护业务类型三包规则</option>
	<% } %>
</select>
</div>
	<form  method="post" name="fm" id="fm">
	<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
	    <table border="0" width="100%">
	    <tr>
		    <td>
			    <!-- 三包策略基本信息(新增时)  -->
				<table class="table_query">
					<tr>
						<td style="text-align:right">三包策略类型：</td>
						<td>
							<script type="text/javascript">
								 genSelBoxExp("GAME_TYPE",<%=Constant.GAME_TYPE%>,"<%=Constant.GAME_TYPE_02%>",false,""," onchange=\"changeGameType(this)\"","false",'');
						    </script>
						</td>
						<td style="text-align:right">三包策略状态：</td>
						<td>
							<script type="text/javascript">
								 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",false,"","","false",'');
						    </script>
						</td>
						<td style="text-align:right">三包策略代码：</td>
						<td>
							<input type='text' name='STRATEGY_CODE' id='STRATEGY_CODE' 
							class="middle_txt" datatype="0,is_null,20"/>
						</td>
					</tr>
					<tr>
						<td style="text-align:right">三包策略名称：</td>
						<td>
							<input type='text' name='STRATEGY_NAME' id='STRATEGY_NAME'
							class="middle_txt" datatype="0,is_null,60"/>
						</td>
						<td style="text-align:right">三包规则：</td>
						<td>
						    <select name="GUARANTEE_RULE_ID" id="GUARANTEE_RULE_ID" 
						    	class="u-select">
							<% 
								if(ruleList!=null && ruleList.size()>0){
							%>
							<%
									for(int i=0;i<ruleList.size();i++){
										TtAsWrRulePO rulePO = (TtAsWrRulePO)ruleList.get(i);
							%>
									<option value="<%=rulePO.getId()%>"><%=rulePO.getRuleName()%></option>
							<%		}%>
							<% } else { %>
								<option value="">请先维护业务类型三包规则</option>
							<% } %>
							</select>
						</td>
						<td style="text-align:right">三包策略开始时间：</td>
						<td>
							<input name="START_TIME" id="START_TIME" readonly class="Wdate" type="text"  onclick="WdatePicker({maxDate:'#F{$dp.$D(\'END_TIME\')}'})"/>
							<!-- <input type="text" name="START_TIME" id="START_TIME" value="" type="text" class="short_txt" datatype="0,is_date,10" group="START_TIME,END_TIME" 
				             hasbtn="true" readonly />
				             <input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'START_TIME', false);" />  	 -->
						</td>
					</tr>
					<tr>
						<td style="text-align:right">三包策略结束时间：</td>
						<td>
							<input name="END_TIME" id="END_TIME" readonly class="Wdate" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'START_TIME\')}'})"/>
							<!-- <input type="text" name="END_TIME" id="END_TIME"
					             value="" type="text" class="short_txt" 
					             datatype="0,is_date,10" group="START_TIME,END_TIME" 
					             hasbtn="true" readonly />
					             <input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'END_TIME', false);" />  	 -->
						</td>
					</tr>
					 <tr>
				    	<td style="text-align:right">车辆性质:</td>
				    	<td colspan="6" >
							<textarea rows="3" cols="25" name="codeDesc" readonly="readonly" id="codeDesc" onclick='open_conent()' >${gamePO.vehicleProBusiDesc}</textarea><span style="color: red">*</span>
							<input type="hidden" datatype="0,is_null,98"  readonly="readonly" name='code_busi' id="code_busi" value="${gamePO.vehicleProBusi}"  />
							<input type="hidden" datatype="0,is_null,98"  readonly="readonly" name='code_id' id="code_id" value="${gamePO.vehicelProperty}"  />
							<!-- <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent()' value='...' /> -->
						</td>
				    </tr>
					<tr>
						<td style="text-align:right">备注：</td>
						<td colspan="6">
							<textarea id="REMARK" name="REMARK" rows="3" cols="80" datatype="1,is_textarea,1000"></textarea>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					<table class="table_query" border="0" cellpadding="0" cellspacing="0">
					    <tr>
							<td style="text-align:center">
								<!-- 三包策略ID -->
								<input type='hidden' name='GUARANTEE_COUNT' id='GUARANTEE_COUNT'  value="1" />
				       		    <input type="button" name="bt_add" class="normal_btn" id="bt_add"
								     onclick="frmSubmitConfirm();" value="新增"/>&nbsp;&nbsp;
								<input type="button" name="bt_back" class="normal_btn" id="bt_back"
								    onclick="backTo();" value="返回"/>
							</td>
					    </tr>
			 	     </table>
				</td>
			</tr>
		</table>
		</div>
		</div>
	</form>
	</div>
	<script type="text/javascript">
	function backTo(){
			var frm = document.getElementById('fm');
				frm.action="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do";
				frm.submit();
}
		function frmSubmitConfirm(){
			var remarkValue = $('REMARK').value;
			if(remarkValue && remarkValue.length>500){
				MyAlert("备注信息长度不能超过 500!");
				return;
			}
			if($('#codeDesc')[0].value==""){
				MyAlert("请选择车辆性质!");
				return;
			}
			if(submitForm('fm')){//查询表单数据格式
				//var nowDate = (new Date()).Format("yyyy-MM-dd");
			   //var startTime = document.getElementById('START_TIME').value;
			   // if(!checkDate(startTime,nowDate)){//选择三包策略开始时间必须大于当前时间    
			    	MyConfirm("是否确认新增？",frmSubmit,[null]);
			    //}else{
				    //MyAlert("三包策略开始日期必须大于当前日期！");
			    //}
			}
		}

		//保存保修费用(提示错误信息，但显示慢)
		function saveGuarantee(){
			makeNomalFormCall('<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/addStrategy.do',showResult,'fm','');
		}

		function showResult(){
			//不做处理
		}

		//使用Form提交的方式保存,不能提示错误信息
		function frmSubmit(){
		$('#bt_add')[0].disabled=true;
		$('#bt_back')[0].disabled=true;
			url="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/addStrategy.json"
			makeNomalFormCall(url,backDeal,'fm','');
		}
//保存后处理
		function backDeal(json){
			var msg=json.msg;
			if(msg==""){
				MyAlert("修改成功!");
				var frm = document.getElementById('fm');
				frm.action="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do";
				frm.submit();
			}else{
				MyAlert(msg);
				$('#bt_add')[0].disabled=false;
				$('#bt_back')[0].disabled=false;
				return false;
			}
		}
		//动态生成保养费用列表
		function dyncCreateGuarante(tbodyId,count){
			var varTbodyId = document.getElementById(tbodyId);
			var guaranteCount = count.value;
			if(varTbodyId!=null && varTbodyId!='undefined'){
				var content = '';
				for(var i=0;i<guaranteCount;i=i+2){
					content = content + getDyncContent(i,'');
			        if((i+1)<guaranteCount){
			        	content = content + getDyncContent((i+1),'');
			        }
					content = "<tr>" + content + "</tr>";
				}
				varTbodyId.innerHTML = '<table class="table_edit">' + content + '</table>';
				for(var i=0;i<guaranteCount;i++){
					setMustStyle([document.getElementById('GUARANTEE_AMOUNT'+i)]);
				}
			}
		}
		 function open_conent(val)
        {
            var code_id= document.getElementById('code_id').value;
       	   OpenHtmlWindow('<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/add_code.do?code_id='+code_id,600,300);
        }

		function getDyncContent(index,value){
			var content = '<td align="right">第'+(index+1)+'次保养费用(元)：</td>'+
			'<td align="left">'+
				'<input type="text" datatype="0,is_double,20" decimal="2" name="GUARANTEE_AMOUNT'+index+'" id="GUARANTEE_AMOUNT'+index+'"'+' value="'+value+'"/>'+
			'</td>';
			return content;
		}

		function changeGameType(gametype){

			var selectedValue = gametype.options[gametype.selectedIndex].value;
			var target = '<%=Constant.GAME_TYPE_01%>';
			var content = '';
			if(target==selectedValue){
				content = $('SYS_RULE_ID').innerHTML;
			}else{
				content = $('RULE_ID').innerHTML;
			}
			$('GUARANTEE_RULE_ID').outerHTML = '<select name="GUARANTEE_RULE_ID" id="GUARANTEE_RULE_ID" class="long_sel">' + content + '</select>';
		}
	</script>
</body>
</html>