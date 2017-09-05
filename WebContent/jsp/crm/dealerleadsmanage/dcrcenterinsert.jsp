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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
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
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>DCRC客流录入
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" /> <input
				type="hidden" id="leadsCode" name="leadsCode" value="${leadsCode }" />
			<input type="hidden" id="leadsAllotId" name="leadsAllotId"
				value="${leadsAllotId }" />

			<table class="table_query" width="95%" align="center">
				<tr>
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px">线索来源:
							<select id="leads_origin" name="leads_origin" onchange="doCusChange();">
								<c:if test="${leadsOrigin =='60151012' }">
									<option id="laid1" value="60151012">来电</option>
									<option id="laid2" value="60151011">来店</option>
								</c:if>
								<c:if test="${leadsOrigin =='60151011' }">
									<option id="laid2" value="60151011">来店</option>
									<option id="laid1" value="60151012">来电</option>
								</c:if>
								<c:if test="${leadsOrigin ==''||leadsOrigin==null }">
									<option id="laid2" value="60151011">来店</option>
									<option id="laid1" value="60151012">来电</option>
								</c:if>
						</select>
					</span></td>
				</tr>
				<tr id="come_meetX">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px">趋前迎接:
							<select id="come_meet" name="come_meet">
								<c:if test="${comeMeet !='10041002' }">
									<option id="come_meet1" value="10041001">是</option>
									<option id="come_meet2" value="10041002">否</option>
								</c:if>
								<c:if test="${comeMeet =='10041002' }">
									<option id="come_meet2" value="10041002">否</option>
									<option id="come_meet1" value="10041001">是</option>
								</c:if>
						</select>
					</span></td>
				</tr>
				<tr id="customerNameX" align="left">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px">客户姓名:
							<input type="text" id="customer_name" name="customer_name"
							value="${customerName}" />
					</span></td>
				</tr>
				<tr id="telephoneX" align="left">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px">联系电话:
							<input type="text" id="telephone" name="telephone"
							value="${telephone}" onchange="nameBlurChange()" />
					</span></td>
				</tr>
				<tr id="collect_fashionX">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px;"><font
							style="float: left;margin-top: 10px;">集客方式:&nbsp</font> <input
							type="hidden" id="collect_fashion" name="collect_fashion"
							value="${jcWay}" />

							<div id="ddtopmenubar29" class="mattblackmenu"
								style="margin-top: 5px;">
								<ul>
									<li><a id="jc_wayh" style="width:170px;" rel="ddsubmenu29"
										href="###" isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadCollectFashion);"
										deftitle="--请选择--"> <c:if test="${jcWay==null||jcWay==''}">--请选择--</c:if>

											<c:if test="${jcWay!=null}">${jcWay2}</c:if>

									</a>

										<ul id="ddsubmenu29" class="ddsubmenustyle"></ul></li>
								</ul>
							</div> </span></td>
				</tr>

			
				
				
			

				<tr>
					<td><span
						style="display: inline-block; margin-left: 50px; margin-top: 10px">意向车型:
							<select id="intentVehicleA"
							onchange="toChangeMenuSelected(this,'intentVehicleB')">
								<c:if test="${intentVehicle==null}">
									<option id="all" value="">-请选择-</option>
									<c:forEach items="${menusAList }" var="alist">
										<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
									</c:forEach>
								</c:if>
								<c:if test="${intentVehicle!=null}">
									<c:forEach items="${menusAList }" var="blist">
										<c:if test="${upSeriesCode == blist.MAINID }">
											<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
										</c:if>
										<c:if test="${upSeriesCode != blist.MAINID }">
											<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
										</c:if>
									</c:forEach>
								</c:if>
						</select> <select id="intentVehicleB" name="intentVehicleB">
								<c:if test="${intentVehicle==null}">
									<option id="all" value="">-请选择-</option>
								</c:if>
								<c:if test="${intentVehicle!=null}">
									<c:forEach items="${menusABList2 }" var="blist">
										<c:if test="${intentVehicle == blist.MAINID }">
											<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
										</c:if>
										<c:if test="${intentVehicle != blist.MAINID }">
											<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
										</c:if>
									</c:forEach>
								</c:if>
						</select>
					</span></td>
				</tr>
				<tr>
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px;"><font
							style="float: left;margin-top: 10px;">客户类型:&nbsp</font> <input
							type="hidden" id="customer_type" name="customer_type"
							value="${customerType}" />
							<div id="ddtopmenubar28" class="mattblackmenu"
								style="margin-top: 5px;">
								<ul>
									<li><a id="customer_typeh" style="width:103px;"
										rel="ddsubmenu28" href="###" isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6035', loadCustomerType);"
										deftitle="--请选择--"> <c:if
												test="${customerType==null||customerType==''}">--请选择--</c:if>
											<c:if test="${customerType!=null}">${customerType2}</c:if></a>
										<ul id="ddsubmenu28" class="ddsubmenustyle"></ul></li>
								</ul>
							</div> </span> <span style="float: left;margin-left: 35px; margin-top: 10px;"><font
							style="float: left;margin-top: 10px;">购车预算:&nbsp</font> <input
							type="hidden" id="buy_budget" name="buy_budget"
							value="${buyBudget}" />
							<div id="ddtopmenubar19" class="mattblackmenu"
								style="margin-top: 5px;">
								<ul>
									<li><a id="buy_budgeth" style="width:103px;"
										rel="ddsubmenu19" href="###" isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6050', loadBuyBudget2);"
										deftitle="--请选择--"> <c:if
												test="${buyBudget==null||buyBudget==''}">--请选择--</c:if> <c:if
												test="${buyBudget!=null}">${buyBudget2}</c:if></a>
										<ul id="ddsubmenu19" class="ddsubmenustyle"></ul></li>
								</ul>
							</div> </span></td>
				</tr>


				<tr id="test_drivingX">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px;"><font
							style="float: left;margin-top: 10px;">试乘试驾:&nbsp</font> <input
							type="hidden" id="test_driving" name="test_driving"
							value="${testDriving}" />
							<div id="ddtopmenubar27" class="mattblackmenu"
								style="margin-top: 5px;">
								<ul>
									<li><a id="test_drivingh" style="width:103px;"
										rel="ddsubmenu27" href="###" isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDriving2);"
										deftitle="--请选择--"> <c:if
												test="${testDriving==null||testDriving==''}">--请选择--</c:if>
											<c:if test="${testDriving!=null}">${testDriving2}</c:if></a>
										<ul id="ddsubmenu27" class="ddsubmenustyle"></ul></li>
								</ul>
							</div> </span> <span style="float: left;margin-left: 35px; margin-top: 10px;"><font
							style="float: left;margin-top: 10px;">购买类型:&nbsp</font> <input
							type="hidden" id="buy_type" name="buy_type" value="${buyType}" />
							<div id="ddtopmenubar25" class="mattblackmenu"
								style="margin-top: 5px;">
								<ul>
									<li><a id="buy_typeh" style="width:103px;"
										rel="ddsubmenu25" href="###" isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6009', loadBuyType2);"
										deftitle="--请选择--"> <c:if
												test="${buyType==null||buyType==''}">--请选择--</c:if> <c:if
												test="${buyType!=null}">${buyType2}</c:if></a>
										<ul id="ddsubmenu25" class="ddsubmenustyle"></ul></li>
								</ul>
							</div> </span></td>
				</tr>
				
				<tr id="oldCustomerNameX" align="left" style="display:none;">
					<td><span
						style="float: left;margin-left: 55px; margin-top: 10px">朋友/老客户姓名:
							<input type="text" id="old_customer_name"
							name="old_customer_name" value="${oldCustomerName}" />
					</span>
					</td>
					
				</tr>
				
				<tr id="oldTelephoneX" align="left"  style="display:none;">
					<td> 
					<span
						style="float: left;margin-left: 55px; margin-top: 10px">朋友/老客户电话:
							<input type="text" id="old_telephone" name="old_telephone"
							value="${oldTelephone}" />
					</span>
					
					<span id="oldVehicleIdX" style="display:none;"
						style="float: left;margin-left: 35px; margin-top: 10px">老客户车架号:
							<input type="text" id="old_vehicle_id" name="old_vehicle_id"
							value="${oldVehicleId}" />
					</span>
					</td>
				</tr>
				
			
					
				<c:if test="${jcWay=='60021008'}">
					<script type="text/javascript">
										 
				var  oldCustomerNameX = document.getElementById("oldCustomerNameX");
				var  oldTelephoneX= document.getElementById("oldTelephoneX");
				var  oldVehicleIdX= document.getElementById("oldVehicleIdX");
				oldCustomerNameX.style.display="block";
				oldTelephoneX.style.display="block";
				oldVehicleIdX.style.display="block";
					 </script>
				</c:if>
				
				<c:if test="${jcWay=='60021004'}">
					<script type="text/javascript">
										 
				var  oldCustomerNameX = document.getElementById("oldCustomerNameX");
				var  oldTelephoneX= document.getElementById("oldTelephoneX");
				
				
				oldCustomerNameX.style.display="block";
				oldTelephoneX.style.display="block";
				
												 
					 </script>
				</c:if>
				<tr id="phoneDate">
					<td><span
						style="display: inline-block; margin-left: 50px; margin-top: 10px">来电时间:
							<input name="startDate2" id="startDate2" value="${nowDate }"
							type="text" class="short_txt" datatype="1,is_date,10"
							group="startDate2,endDate2" hasbtn="true"
							callFunction="showcalendar(event, 'startDate2', false);" /> 时:<select
							id="startHHDate2" name="startHHDate2"><option value="00">00</option>
								<option id="hour201" value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option></select> 分:<select id="startMMDate2"
							name="startMMDate2"><option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="39">39</option>
								<option value="40">40</option>
								<option value="41">41</option>
								<option value="42">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option></select>
					</span></td>
				</tr>
				<tr style="display: none" id="comeDate">
					<td><span
						style="display: inline-block; margin-left: 50px; margin-top: 10px">来店时间:
							<input name="startDate" id="startDate" value="${nowDate }"
							type="text" class="short_txt" datatype="1,is_date,10"
							group="startDate,endDate" hasbtn="true"
							callFunction="showcalendar(event, 'startDate', false);" /> 时:<select
							id="startHHDate" name="startHHDate"><option value="00">00</option>
								<option id="hour01" value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option></select> 分:<select id="startMMDate"
							name="startMMDate"><option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="39">39</option>
								<option value="40">40</option>
								<option value="41">41</option>
								<option value="42">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option></select>
					</span></td>
				</tr>

				<c:if test="${updateFlag=='yes' }">
					<tr style="display: none" id="leaveDate">
						<td><span
							style="display: inline-block; margin-left: 50px; margin-top: 10px">离店时间:
								<input name="endDate" id="endDate" value="${nowDate }"
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" hasbtn="true"
								callFunction="showcalendar(event, 'endDate', false);" /> 时:<select
								id="endHHDate" name="endHHDate"><option value="00">00</option>
									<option value="01">01</option>
									<option value="02">02</option>
									<option value="03">03</option>
									<option value="04">04</option>
									<option value="05">05</option>
									<option value="06">06</option>
									<option value="07">07</option>
									<option value="08">08</option>
									<option value="09">09</option>
									<option value="10">10</option>
									<option value="11">11</option>
									<option value="12">12</option>
									<option value="13">13</option>
									<option value="14">14</option>
									<option value="15">15</option>
									<option value="16">16</option>
									<option value="17">17</option>
									<option value="18">18</option>
									<option value="19">19</option>
									<option value="20">20</option>
									<option value="21">21</option>
									<option value="22">22</option>
									<option value="23">23</option>
									<option value="24">24</option></select> 分:<select id="endMMDate"
								name="endMMDate"><option value="00">00</option>
									<option value="01">01</option>
									<option value="02">02</option>
									<option value="03">03</option>
									<option value="04">04</option>
									<option value="05">05</option>
									<option value="06">06</option>
									<option value="07">07</option>
									<option value="08">08</option>
									<option value="09">09</option>
									<option value="10">10</option>
									<option value="11">11</option>
									<option value="12">12</option>
									<option value="13">13</option>
									<option value="14">14</option>
									<option value="15">15</option>
									<option value="16">16</option>
									<option value="17">17</option>
									<option value="18">18</option>
									<option value="19">19</option>
									<option value="20">20</option>
									<option value="21">21</option>
									<option value="22">22</option>
									<option value="23">23</option>
									<option value="24">24</option>
									<option value="25">25</option>
									<option value="26">26</option>
									<option value="27">27</option>
									<option value="28">28</option>
									<option value="29">29</option>
									<option value="30">30</option>
									<option value="31">31</option>
									<option value="32">32</option>
									<option value="33">33</option>
									<option value="34">34</option>
									<option value="35">35</option>
									<option value="36">36</option>
									<option value="37">37</option>
									<option value="38">38</option>
									<option value="39">39</option>
									<option value="40">40</option>
									<option value="41">41</option>
									<option value="42">42</option>
									<option value="43">43</option>
									<option value="44">44</option>
									<option value="45">45</option>
									<option value="46">46</option>
									<option value="47">47</option>
									<option value="48">48</option>
									<option value="49">49</option>
									<option value="50">50</option>
									<option value="51">51</option>
									<option value="52">52</option>
									<option value="53">53</option>
									<option value="54">54</option>
									<option value="55">55</option>
									<option value="56">56</option>
									<option value="57">57</option>
									<option value="58">58</option>
									<option value="59">59</option></select>
						</span></td>
					</tr>
				</c:if>

				<tr>
					<td><span
						style="display: inline-block; margin-left: 50px; margin-top: 10px">销售顾问:
							<select id="adviserId">
								<c:if test="${adviser==null }">
									<c:forEach var="item" items="${adviserList }"
										varStatus="status">
										<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
									</c:forEach>
								</c:if>
								<c:if test="${adviser!=null }">
									<c:forEach var="item" items="${adviserList }"
										varStatus="status">
										<c:if test="${adviser == item.USER_ID }">
											<option id="all" value="${item.USER_ID }" selected="selected">${item.NAME }</option>
										</c:if>
										<c:if test="${adviser != item.USER_ID }">
											<option id="all" value="${item.USER_ID }">${item.NAME }</option>
										</c:if>
									</c:forEach>
								</c:if>
						</select>
					</span></td>
				</tr>
				<tr>
					<td><span
						style="display: inline-block; margin-left: 50px; margin-top: 10px">客户描述:
							<textarea rows="3" cols="50" id="describe" name="describe">${customerDescribe }</textarea>
					</span></td>
				</tr>
				<tr>
					<td colspan="3" align="left">
						<span style="display: inline-block; margin-left: 50px; margin-top: 10px">
							<c:choose>
								<c:when test="${updateFlag=='yes' }">
									<input name="queryBtn" type="button" class="normal_btn" onclick="doUpdate()" id="saveButton"  value="保存" />
								</c:when>
								<c:otherwise>
									<input name="queryBtn" type="button" class="normal_btn" onclick="doCheck()" id="saveButton" value="保存" />
								</c:otherwise>
							</c:choose>
							<input name="insertBtn" id="insertBtn" type="button" class="normal_btn" onClick="javascript:history.go(-1);" value="返回" />
						</span>
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
<script type="text/javascript"> 
//保存
function doCheck(){
	var leadsOrigin = document.getElementById("leads_origin").value;
	var startDate = document.getElementById("startDate").value;
	var startDate2 = document.getElementById("startDate2").value;
	var telephone = document.getElementById("telephone").value;
// 	var endDate = document.getElementById("endDate").value;
	var adviserId = document.getElementById("adviserId").value;
	var collectFashion = document.getElementById("collect_fashion").value;
	var leadsCode = document.getElementById("leadsCode").value;
	var fashion=$("collect_fashion").value;
	 if(fashion!='60021008' && fashion!='60021004'){
			var old_customer_name = document.getElementById("old_customer_name"); //获取老客户对应的输入框
			var old_telephone = document.getElementById("old_telephone");
			var old_vehicle_id = document.getElementById("old_vehicle_id");
			old_customer_name.value="";
			old_telephone.value="";
			old_vehicle_id.value="";
		}
	
	if(leadsCode==null||leadsCode==""){
		leadsCode="";
		}
	var skCount=0;
	//检查线索来源
	if((leadsOrigin==null||leadsOrigin=="")) {
		MyAlert("线索来源不能为空!");
		return false;
	} else if(leadsOrigin=='60151011'&&(startDate=="")) {
		MyAlert("来店时间不能为空!");
		return false;
	} else if(leadsOrigin=='60151012'&&(startDate2=="")) {
		MyAlert("来电时间不能为空!");
		return false;
	} else if(leadsOrigin=='60151012'&&(telephone=="")) {
		MyAlert("联系电话不能为空!");
		return false;
	} else if(leadsOrigin=='60151011'&&(collectFashion=="")) {
		MyAlert("集客方式不能为空!");
		return false;
	}else {
		// MyAlert("telephone=="+telephone);
		if(telephone!="" && telephone!=null){
		//验证表单数据leadsOrigin=='60151012'&&
			if(!validatemobile(telephone)){
				return false;
			}
		}
	if(leadsOrigin=='60151011')
	{
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/getFirstGuest.json?telePhone="+telephone+"&leadsCode="+leadsCode;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			// MyAlert(JSON.stringify(json));
			skCount=json.skCount;
		}
		// MyAlert("collectFashion==="+collectFashion);
		if(skCount>0 && (collectFashion=="60021001"||collectFashion=="60021003"||collectFashion=="60021004" ||collectFashion=="60021008")){
			MyAlert("该客户已经有过首次来店,不能再选择有首次来店的集客方式！");
			return;
			}
		if(skCount==0 && (collectFashion!=60021001 && collectFashion!=60021003 && collectFashion!=60021004 && collectFashion!=60021008)){
			MyAlert("该客户没有首客线索,必须选择首次来店的集客方式!");
			return;
			}
	}
		document.getElementById("saveButton").disabled = true;
		document.getElementById("insertBtn").disabled = true;
		//验证手机号码
		$('fm').action = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcEnterInsertSave.do?adviserId="+adviserId;
		$('fm').submit();
	}
}

//保存
function doUpdate(){
	if(checkEndDate()==false){
		return;
	}
	var leadsOrigin = document.getElementById("leads_origin").value;
	var startDate = document.getElementById("startDate").value;
	var startDate2 = document.getElementById("startDate2").value;
	var telephone = document.getElementById("telephone").value;
// 	var endDate = document.getElementById("endDate").value;
	var adviserId = document.getElementById("adviserId").value;
	var collectFashion = document.getElementById("collect_fashion").value;
	var leadsCode = document.getElementById("leadsCode").value;
	var fashion=$("collect_fashion").value;
	 if(fashion!='60021008'  && fashion!='60021004'){
			var old_customer_name = document.getElementById("old_customer_name"); //获取老客户对应的输入框
			var old_telephone = document.getElementById("old_telephone");
			var old_vehicle_id = document.getElementById("old_vehicle_id"); 
			old_customer_name.value="";
			old_telephone.value="";
			old_vehicle_id.value="";
		}
	if(leadsCode==null||leadsCode==""){
		leadsCode="";
		}
	var skCount=0;
	//检查线索来源
	if((leadsOrigin==null||leadsOrigin=="")) {
		MyAlert("线索来源不能为空!");
		return false;
	} else if(leadsOrigin=='60151011'&&(startDate=="")) {
		MyAlert("来店时间不能为空!");
		return false;
	} else if(leadsOrigin=='60151012'&&(startDate2=="")) {
		MyAlert("来电时间不能为空!");
		return false;
	} else if(leadsOrigin=='60151012'&&(telephone=="")) {
		MyAlert("联系电话不能为空!");
		return false;
	} else if(leadsOrigin=='60151011'&&(collectFashion=="")) {
		MyAlert("集客方式不能为空!");
		return false;
	} else {
		if(telephone!="" && telephone!=null){
		//验证表单数据leadsOrigin=='60151012'&&
			if(!validatemobile(telephone)){
				return false;
			}
		}
		if(leadsOrigin=='60151011')
		{
			var url = "<%=contextPath%>/crm/taskmanage/TaskManage/getFirstGuest.json?telePhone="+telephone+"&leadsCode="+leadsCode;
			makeSameCall(url, showInfo, "fm") ;
			function showInfo(json) {
				skCount=json.skCount;
				}
		
			if(skCount>0 && (collectFashion=="60021001"||collectFashion=="60021003"||collectFashion=="60021004" ||collectFashion=="60021008")){
				MyAlert("该客户已经有过首次来店,不能再选择有首次来店的集客方式！");
				return;
				}
			if(skCount==0 && (collectFashion!="60021001" && collectFashion!="60021003" && collectFashion!="60021004" && collectFashion!="60021008")){
				MyAlert("该客户没有首客线索,必须选择首次来店的集客方式!");
				return;
				}
		}
		document.getElementById("saveButton").disabled = true;
		document.getElementById("insertBtn").disabled = true;
		//验证手机号码
		$('fm').action = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcEnterInsertUpdate.do?adviserId="+adviserId;
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
	var leaveDate=document.getElementById('leaveDate');
 	var phoneDate=document.getElementById('phoneDate');
 	var telephoneX=document.getElementById('telephoneX');
 	var come_meetX=document.getElementById('come_meetX');
 	var collect_fashionX=document.getElementById('collect_fashionX');
	if(leadsOrigin == '60151011') {
		comeDate.style.display="block";
		phoneDate.style.display="none";
		telephoneX.style.display="block";
		come_meetX.style.display="block";
		collect_fashionX.style.display="block";
		leaveDate.style.display="block";
	} else {
		comeDate.style.display="none";
		phoneDate.style.display="block";
		telephoneX.style.display="block";
		come_meetX.style.display="none";
		collect_fashionX.style.display="none";
		leaveDate.style.display="none";
	}
}

function checkEndDate(){
		var startDate = document.getElementById("startDate").value;
		var objSelectHour = document.getElementById("startHHDate").value;
		var objSelectMin = document.getElementById("startMMDate").value;

		var endDate = document.getElementById("endDate").value;
		var endHour = document.getElementById("endHHDate").value;
		var endMin = document.getElementById("endMMDate").value;
		
		if(endDate<startDate){
			MyAlert("离店时间不能早于来店时间！");
			return false;
			
		}else if(endDate==startDate && endHour<objSelectHour){
			MyAlert("离店时间不能早于来店时间！");
			return false;
		}else if(endDate==startDate && endHour==objSelectHour && endMin<objSelectMin){
			MyAlert("离店时间不能早于来店时间！");
			return false;
		}
	
}

//电话异步处理信息
function nameBlurChange(){
	var telephone = document.getElementById("telephone").value;
	var intentVehicleA = document.getElementById("intentVehicleA");
	var intentVehicleB = document.getElementById("intentVehicleB");
	var adviserId = document.getElementById("adviserId");
	if(telephone==null || telephone=="") {
		return false;
	}
	var url = "<%=contextPath%>/crm/taskmanage/TaskManage/getAdviserIfHas.json";
	makeCall(url, showInfo,{telePhone:telephone});
	function showInfo(json) {
		//判断是否已有其他顾问建档信息
		if(json.ps[0]!=null) {
		//回填数据
		if(json.ps[0].ADVISER!=null) {
			MyAlert("系统已存在该客户档案,可继续操作并录入线索!");
		}
		var customerName = json.ps[0].CUSTOMER_NAME;
		var customer_typeh = json.ps[0].CUSTOMER_TYPE2;
		var if_driveh = json.ps[0].TEST_DRIVING2;
		var buy_typeh = json.ps[0].BUY_TYPE2;
		var buy_budgeth = json.ps[0].BUY_BUDGET2;
		var jc_way=json.ps[0].JC_WAY2;
		var collect_fashion=json.ps[0].JC_WAY;
		document.getElementById("customer_name").value=customerName;
		document.getElementById("customer_typeh").innerHTML=customer_typeh;
		document.getElementById("test_drivingh").innerHTML=if_driveh;
		document.getElementById("buy_budgeth").innerHTML=buy_budgeth;
		document.getElementById("buy_typeh").innerHTML=buy_typeh;
		document.getElementById("jc_wayh").innerHTML=jc_way;
		document.getElementById("collect_fashion").value=collect_fashion;
		
		for(var i=0;i<intentVehicleA.options.length;i++) {
			 if(intentVehicleA.options[i].value==json.ps[0].UP_SERIES_ID){  
				 intentVehicleA.options[i].selected=true;  
                   break;  
               }  
		}
		toChangeMenu(intentVehicleA,'intentVehicleB');
		for(var i=0;i<intentVehicleB.options.length;i++) {
			 if(intentVehicleB.options[i].value==json.ps[0].INTENT_VEHICLE){  
				 intentVehicleB.options[i].selected=true;  
                   break;  
               }  
		}
		
		for(var i=0;i<adviserId.options.length;i++) {
			 if(adviserId.options[i].value==json.ps[0].ADVISER){  
				 adviserId.options[i].selected=true;  
                    break;  
                }  
		}
		
		for(var i=0;i<adviserId.options.length;i++) {
			 if(adviserId.options[i].value==json.ps[0].ADVISER){  
				 adviserId.options[i].selected=true;  
                    break;  
                }  
		}
	} else {
		return false;
	}
	}
}

//如果为老客服介绍的 则增加3个老客户信息输入框
function loadCollectFashion(obj){
	
	$("collect_fashion").value=obj.getAttribute("TREE_ID");
	var fashion=$("collect_fashion").value;
	var  old_vehicle_id = document.getElementById("old_vehicle_id");
	var  oldCustomerNameX = document.getElementById("oldCustomerNameX"); //获得老客户相关信息对应的<tr>
	var  oldTelephoneX= document.getElementById("oldTelephoneX");
	var  oldVehicleIdX = document.getElementById("oldVehicleIdX");
	if(fashion=='60021008'){  //如果为老客户介绍  则出现填写老客户资料的 输入框，反之则隐藏 并且把值设为空
		oldCustomerNameX.style.display="block";
		oldTelephoneX.style.display="block";
		oldVehicleIdX.style.display="block";
	}else if(fashion=='60021004'){
		oldCustomerNameX.style.display="block";
		oldTelephoneX.style.display="block";
		oldVehicleIdX.style.display="none";
		old_vehicle_id.value="";
	}else{
		oldCustomerNameX.style.display="none";   
		oldTelephoneX.style.display="none";
		oldVehicleIdX.style.display="none";
		}
	}


//禁止用F5键 
function onkeydown() 
	{ 
		if ( event.keyCode==116) 
		{ 
		event.keyCode = 0; 
		event.cancelBubble = true; 
		return false; 
	} 
} 

//禁止右键弹出菜单 
function oncontextmenu(){
		event.returnValue=false;
} //屏蔽鼠标右键 
</script>
	</div>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar19", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>

</body>
</html>