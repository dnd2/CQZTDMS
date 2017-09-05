<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
    <head>
        <jsp:include page="${contextPath}/common/jsp_head_new.jsp" /><title>批发申请</title>
        <script type="text/javascript" src="getJs.do?fileName=vehicleAllocateInit"></script>
    </head>
    <body onunload='javascript:destoryPrototype();' onload='checkFirst();'>
        <div class="wbox">
            <div class="navigation">
                <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 库存管理 &gt; 调拨通知
            </div>
            <form id="fm" name="fm" method="post">
                <input type="hidden" name="curPage" id="curPage" value="1" /><input type="hidden" id="dlrId" name="dlrId" value="" />
                <table class="table_query" border="0">
                	<tr>
							<td  colspan="2"><font color="red">注意事项:</font></td>
						</tr>
							<tr>
							<td  colspan="2"><font color="red">1、选择了车辆如果想选择另外的调出经销商，请删除所有已选择的车辆。</font></td>
							</tr>
							
                    <tr>
                        <td class="table_query_3Col_input">
                            <input type="button" class="normal_btn" onclick="chooseVehicleAllocateInit(1);" value="  添  加  " id="queryBtn_add" />
                        </td>
                    </tr>
                </table><jsp:include page="${contextPath}/queryPage/orderHidden.html" />
                <div id="myGrid">
                    <table style="border-bottom: 1px solid #DAE0EE" class="table_list" id="myTable">
                        <tbody>
                            <tr class="table_list_th">
                                <th class="noSort" style="text-align: center;">
                                </th>
                                <th class="noSort">
                                    VIN
                                </th>
                                <th class="noSort">
                                    车辆业务范围
                                </th>
                              
                                <th class="noSort">
                                    物料代码
                                </th>
                                <th class="noSort">
                                    物料名称
                                </th>
                            
                                <th class="noSort">
                                    操作
                                </th>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
        <form name="form1" id="form1">
            <table class="table_query" width="85%" align="center" border="0" id="roll">
                <tr>
                    <td width="11%" class="right">
                        调出经销商：
                    </td>
                    <td width="25%" align=""left"">
                        <input id="dealerCodeOut" name="dealerCodeOut" onchange="showFundDealerIdOut()" type="text" class="middle_txt" size="15" />						   <input type="button" id="button1" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCodeOut','dealerIdOut','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')"/><input type="hidden"  id="dealerIdOut" name="dealerIdOut" />
                    </td>
                    <td width="11%" align="left">
                        调入经销商：
                    </td>
                    <td width="25%" align="left">
                        <input id="dealerCodeIn" name="dealerCodeIn" onchange="showFundDealerIdIn()" type="text" class="middle_txt" size="15" readonly="readonly" /><input type="button" id="button2" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCodeIn','dealerIdIn'	 ,'false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')"/><input type="hidden"  id="dealerIdIn"  name="dealerIdIn" />
                    </td>
                </tr>
                <tr>
                    <td width="11%" class="right">
                        调出经销商资金类型：
                    </td>
                    <td width="25%" align="left">
                        <select id="fundTypeOut" name="fundTypeOut" onchange="availableAmountOut();"  type="text" class="middle_txt"  >
                        </select>
                    </td>
                    <td width="11%" align="left">
                        调入经销商资金类型：
                    </td>
                    <td width="25%" align="left">
                        <select id="fundTypeIn" name="fundTypeIn" onchange="availableAmountIn();"  type="text" class="middle_txt"  >
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="11%" class="right">
                        <span>可用余额：</span>
                    </td>
                    <td width="25%" align="left">
                        <span id="spanFundTypeOut" class="STYLE2"></span>
                    </td>
                    <td width="11%" align="left">
                        <span>可用余额：</span>
                    </td>
                    <td width="25%" align="left">
                        <span id="spanFundTypeIn"  class="STYLE2"></span>
                    </td>
                </tr>
                <tr>
                    <td width="11%" class="right">
                        调拨原因：
                    </td>
                    <td width="20%" align="left">
                        <textarea id="allocate_reason" name="allocate_reason" datatype="0,is_textarea,300"></textarea>
                    </td>
                    <td width="37%" align="left" colspan="2">
                        <input name="button2" id="button2" type="button" class="normal_btn" onclick="Allocate();" value="下达调拨通知" />
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>