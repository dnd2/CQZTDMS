/**********************************************************************
* <pre>
* FILE : ErrorCodeConstant.java
* CLASS : ErrorCodeConstant
*
* AUTHOR : xianchao zhang
*
* FUNCTION : 错误代码的常量类
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-8-20| xianchao zhang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: ErrorCodeConstant.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/
package com.infodms.dms.common;

/**
 * 功能说明：对各功能定义的错误代码
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：xianchao zhang
 * 创建时间：2009-8-20
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class ErrorCodeConstant {
	/**
	 * --------------------------------异常代码类型--------开始--------------------------
	 */
	//由公共模块处理 的异常，代码为1
	public static int AUTO_DEAL_WITH_CODE = 1; 
	//由自己处理的的异常，代码为2
	public static int SELF_DEAL_WITH_CODE = 2; 
	/**
	 * --------------------------------结束---------------------------------------------
	 */
	/**
	 * -------------------------公共错误错码----------开始-------------------------------
	 */
	//普通失败错误代码，显示如：特殊车辆新增失败
	public static String FAILURE_CODE = "01"; //{0}失败，请于系统管理员联系
	
	//已经存在的错误代码
	public static String ALREADY_EXIST_CODE = "03"; //{0}:{1}已经存在
	
	//写CSV文件失败的错误代码
	public static String WRITE_CSV_ERROR_CODE = "02"; //写入CSV文件失败
	
	//查找错误代码的错误信息失败
	public static String FIND_ERROR_CODE_MESSAGE = "04"; //错误代码：{0}没有错误信息
	
	//不存在的错误代码
	public static String NO_EXIST_CODE = "05"; //{0}：{1}不存在
	
	//导入的数据格式不正确
	public static String WRITE_CSV_FORMAT_FALSENESS = "06"; //导入的数据格式不正确
	
	//导入的数据格式不正确
	public static String PLAN_ACCMPLISH_VALIDATE = "07"; //计划完成情况如果填写，须填写完整
	
	//普通失败错误代码，显示如：特殊车辆新增失败
	public static String ADD_FAILURE_CODE = "08"; //{0}新增失败，请于系统管理员联系
	
	//普通失败错误代码，显示如：特殊车辆修改失败
	public static String UPDATE_FAILURE_CODE = "09"; //{0}修改失败，请于系统管理员联系
	
	//普通失败错误代码，显示如：特殊车辆查询失败
	public static String QUERY_FAILURE_CODE = "10"; //{0}查询失败，请于系统管理员联系
	
	//普通失败错误代码，显示如：特殊车辆明细查询失败
	public static String DETAIL_FAILURE_CODE = "11"; //{0}明细查询失败，请于系统管理员联系
	
	//普通失败错误代码，显示如：特殊车辆查询失败
	public static String DELETE_FAILURE_CODE = "12"; //{0}删除失败，请于系统管理员联系
	
	//批量导入CSV文件出错，用来标识事务回滚
	public static String BATCH_IMPORT_FAILURE_CODE = "13"; //批量导入CSV文件出错，用来标识事务回滚
	
	//{0}:{1}失效失败，请于系统管理员联系
	public static String DISABLED_FAILURE_CODE = "14"; //{0}:{1}失效失败，请于系统管理员联系
	
	//参数无效，请正确使用！
	public static String PARAM_FAILURE_CODE = "15"; //参数无效，请正确使用！
	
	//{0}:{1}已经存在,不能进行修改
	public static String ALREADY_EXIST_NO_UPDATE_CODE = "16"; //{0}:{1}已经存在,不能进行修改
	
	//{0}:{1}没有过期，不能进行延期
	public static String NO_INVALIDATION_CODE = "17"; //{0}:{1}没有过期，不能进行延期
	
	//无效数据
	public static String INVALIDATION_DATA = "18"; //{0}不存在或者为无效数据
	
	//普通失败错误代码，显示如：特殊车辆查询失败
	public static String SAVE_FAILURE_CODE = "19"; //{0}保存失败，请于系统管理员联系
	
	//普通失败错误代码，显示如：特殊车辆查询失败
	public static String PUTIN_FAILURE_CODE = "20"; //{0}提交失败，请于系统管理员联系
	
	//完全自定义显示
	public static String SPECIAL_MEG = "21"; //操作失败！失败原因：{0}
	
	//车辆：{0}已经销售
	public static String ALREADY_SALE_CODE = "22"; //车辆：{0}已经销售
	
	//车辆：{0}不能销售，状态：{1}
	public static String NOT_ALLOW_SALE_CODE = "23"; //车辆：{0}不能销售，状态：{1}
	
	//actionName错误,当请求的URL在数据库中不存在时
	public static String ACTION_NAME_ERROR_CODE = "24"; //actionName错误
	
	//未找到该用户的数据权限
	public static String DATA_ACL_NOT_FOUND_CODE = "25"; //未找到该用户的数据权限
	/**
	 * -------------------------------------结束------------------------------------------------
	 */
	
	/**
	 * ---------------------------输入检验错误代码--------------------------
	 */
	public static String NOT_NULL = "51"; // {0}不能为空，请输入后再提交

	public static String NOT_COMPLETE = "52"; // {0}不完整，请补充完整后再提交
	
	public static String NOT_INTEGER = "53"; // {0}必须为正整数
	
	public static String NOT_NUMBER = "54"; // {0}必须为数字
	
	public static String NOT_MATCH = "55"; // {0}格式不正确
	/**
	 * ---------------------------------结束------------------------------
	 */
	//普通失败错误代码，显示:试驾路线管理失败！请联系管理元
	public static String QUERY_ROUTE_CODE = "26"; //{0}查询失败，请于系统管理员联系
	/**
	 * ---------------------------------结束------------------------------
	 */
	public static String ERP_ERROR = "60";//{0}ERP接收失败
}
