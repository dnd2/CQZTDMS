<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>DCRC客流录入</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doCusChange();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>顾问客流录入
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" />

			<table class="table_query" width="95%" align="center">
			<tr>
					<td>
						<span style="float: left;margin-left: 55px; margin-top: 10px" >客户姓名:
							<input type="text" id="customer_name" name="customer_name" />&nbsp;&nbsp;&nbsp;&nbsp;
							联系电话：
							<input type="text" id="telephone" name="telephone"/>							
						</span>
					</td>
				</tr>
				<tr >
					<td>
						<span style="float: left;margin-left: 55px; margin-top: 10px" >客户性别:
							<select id="sex" name="sex">
								<option id="sex1" value="10031001">男</option>
								<option id="sex2" value="10031002">女</option>
							</select>
						</span>
					</td>
				</tr>
				<tr >
					<td>
						<span style="float: left;margin-left: 55px; margin-top: 10px" >线索来源:
							<select id="leads_origin" name="leads_origin">
								<option id="laid1" value="60151007">车展/巡展/路演</option>
								<option id="laid2" value="60151019">商圈定展</option>
								<option id="laid3" value="60151008">品牌体验活动（上市活动、试乘试驾、大篷车等）</option>
								<option id="laid4" value="60151006">网络媒体</option>
								<option id="laid5" value="60151020">新媒体（移动端APP、移动网站等）</option>
								<option id="laid6" value="60151021">社会化媒体（微博、微信、论坛等）</option>
								<option id="laid7" value="60151016">亲朋/老客户介绍及其他</option>
								<option id="laid8" value="60151004">官网</option>
								<option id="laid9" value="60151003">客户中心</option>
								<option id="laid10" value="60151017">汽车之家</option>
								<option id="laid11" value="60151018">易车网</option>
							</select>&nbsp;&nbsp;&nbsp;&nbsp;
<!--							趋前迎接:-->
<!--							<select id="come_meet" name="come_meet">-->
<!--								<option id="come_meet1" value="10041002">是</option>-->
<!--								<option id="come_meet2" value="10041001">否</option>-->
<!--							</select>-->
						</span>
					</td>
				</tr>
				<tr >
					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">意向车型:
					<select id="intentVehicleA" onchange="toChangeMenu(this,'intentVehicleB')">
						<option id="all" value="">-请选择-</option>
						<c:forEach items="${menusAList }" var="alist">
							<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
						</c:forEach>
					</select>
					<select id="intentVehicleB" name="intentVehicleB">
						<option id="all" value="">-请选择-</option>
					</select></span></td>
				</tr>
<!--				<tr id="phoneDate">-->
<!--					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">来电时间: <input name="startDate2" id="startDate2" value="${nowDate }" type="text"-->
<!--								class="short_txt" datatype="1,is_date,10"-->
<!--								group="startDate2,endDate2" hasbtn="true"-->
<!--								callFunction="showcalendar(event, 'startDate2', false);" />-->
<!--								时:<select id="startHHDate2" name="startHHDate2"><option value="00">00</option><option id="hour201" value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option></select>-->
<!--								分:<select id="startMMDate2" name="startMMDate2"><option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option><option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option><option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option><option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option><option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option><option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option><option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option><option value="57">57</option><option value="58">58</option><option value="59">59</option></select>-->
<!--								</span></td>-->
<!--				</tr>-->
<!--				<tr style="display: none" id="comeDate">-->
<!--					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">来店时间: <input name="startDate" id="startDate" value="${nowDate }" type="text"-->
<!--								class="short_txt" datatype="1,is_date,10"-->
<!--								group="startDate,endDate" hasbtn="true"-->
<!--								callFunction="showcalendar(event, 'startDate', false);" />-->
<!--								时:<select id="startHHDate" name="startHHDate"><option value="00">00</option><option id="hour01" value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option></select>-->
<!--								分:<select id="startMMDate" name="startMMDate"><option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option><option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option><option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option><option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option><option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option><option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option><option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option><option value="57">57</option><option value="58">58</option><option value="59">59</option></select>-->
<!--								</span></td>-->
<!--				</tr>-->
<!-- 				<tr style="display: none" id="leaveDate"> -->
<%-- 					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">离店时间: <input name="endDate" id="endDate" value="${nowDate }" --%>
<!-- 								type="text" class="short_txt" datatype="1,is_date,10" -->
<!-- 								group="startDate,endDate" hasbtn="true" -->
<!-- 								callFunction="showcalendar(event, 'endDate', false);" /> -->
<!-- 								时:<select id="endHHDate" name="endHHDate"><option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option></select> -->
<!-- 								分:<select id="endMMDate" name="endMMDate"><option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option><option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option><option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option><option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option><option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option><option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option><option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option><option value="57">57</option><option value="58">58</option><option value="59">59</option></select> -->
<!-- 								</span></td> -->
<!-- 				</tr> -->
<!--				<tr >-->
<!--					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">销售顾问:-->
<!--					<select id="adviserId"> -->
<!--			      		<c:forEach var="item" items="${adviserList }" varStatus="status">-->
<!--			      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>-->
<!--			      		</c:forEach>-->
<!--			      	</select></span></td>-->
<!--				</tr>-->
				<tr >
					<td><span style="display: inline-block; margin-left: 50px; margin-top: 10px">客户描述:
					<textarea rows="3" cols="50" id="describe" name="describe"></textarea>
					</span></td>
				</tr>
				<tr>
					<td colspan="3" align="left">
					<span style="display: inline-block; margin-left: 50px; margin-top: 10px">
					<input name="queryBtn"
						type="button" class="normal_btn" onclick="doCheck()" id="saveButton"
						value="保存" />
						<input name="insertBtn"
						type="button" class="normal_btn" onClick="javascript:history.go(-1);"
						value="返回" /></span>
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
<script type="text/javascript" > 
function doCheck(){
	var leadsOrigin = document.getElementById("leads_origin").value;
	var intentVehicleB = document.getElementById("intentVehicleB").value;
	var customerName = document.getElementById("customer_name").value;
	var telephone = document.getElementById("telephone").value;
	//检查线索来源
	if((leadsOrigin==null||leadsOrigin=="")) {
		MyAlert("线索来源不能为空!");
		return false;
	} else if(intentVehicleB==null||intentVehicleB=="") {
		MyAlert("意向车型不能为空!");
		return false;
	} else if(customerName==null||customerName=="") {
		MyAlert("客户姓名不能为空!");
		return false;
	} else if(telephone==null||telephone=="") {
		MyAlert("联系电话不能为空!");
		return false;
	} else {
		//验证表单数据
		if(!validatemobile(telephone)){
			return false;
		};//验证手机号码
		document.getElementById("saveButton").disabled = true;
		$('fm').action = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/adviserEnterInsertSave.do";
		$('fm').submit();
	}
}
function doCusChange() {
	var objSelectHour = document.getElementById("startHHDate");
	var objSelectMin = document.getElementById("startMMDate");
 	var objSelectHour2 = document.getElementById("startHHDate2");
 	var objSelectMin2 = document.getElementById("startMMDate2");
	for(var i=0;i<objSelectHour.options.length;i++) {  
          if(objSelectHour.options[i].value == '${nowHour}') {  
        	  objSelectHour.options[i].selected = true;  
              break;  
          }  
      }  
	for(var j=0;j<objSelectMin.options.length;j++) {  
        if(objSelectMin.options[j].value == '${nowMinute}') {  
      	  objSelectMin.options[j].selected = true;  
            break;  
        }  
    } 
	for(var i2=0;i2<objSelectHour2.options.length;i2++) {  
        if(objSelectHour2.options[i2].value == '${nowHour}') {  
      	  objSelectHour2.options[i2].selected = true;  
            break;  
        }  
    }  
	for(var j2=0;j2<objSelectMin2.options.length;j2++) {  
      if(objSelectMin2.options[j2].value == '${nowMinute}') {  
    	  objSelectMin2.options[j2].selected = true;  
          break;  
      }  
  } 
	var obj = document.getElementById("leads_origin"); //定位id

	var index = obj.selectedIndex; // 选中索引

	var leadsOrigin = obj.options[index].value; // 选中值

	var comeDate=document.getElementById('comeDate');
 	var phoneDate=document.getElementById('phoneDate');
	if(leadsOrigin == '601510021001') {
		comeDate.style.display="block";
		phoneDate.style.display="none";
	} else {
		comeDate.style.display="none";
		phoneDate.style.display="block";
	}
}
</script>
	</div>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
</body>
</html>