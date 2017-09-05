package com.infodms.dms.common.component.acl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TcFuncPO;
import com.infoservice.mvc.component.acl.AclDataProvider;
import com.infoservice.mvc.component.acl.AclResource;
import com.infoservice.mvc.component.acl.AclRole;
import com.infoservice.mvc.component.acl.AclUser;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;

public class CommonAclProvider implements AclDataProvider {
	
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	private AclRole role = null;
	private AclResource res = null;
	
	/**
	 * 截取funcCode的类路径
	 */
	public AclResource getResource(String funcCode) {
		String str = (funcCode!=null&&funcCode.trim().length()>0)?funcCode.substring(0, funcCode.lastIndexOf("/")):"";
		res = new CommonAclResource(str);
		return res;
	}

	/**
	 * 通过func_id拿到func_code
	 */
	public String[] getResourceUIDsByRole(String funcId) {
		if(funcId!=null&&funcId!=Constant.Login_Function_Id) {	
			TcFuncPO func = new TcFuncPO();
			func.setFuncId(new Long(funcId));
			List<TcFuncPO> funcs = factory.select(func);
			if(funcs!=null&&funcs.size()>0) {
				return new String[]{funcs.get(0).getFuncCode()};
			}else {
				return new String[]{""};
			}
		}else {
			return new String[]{Constant.COMMON_URI};
		}
	}

	public AclRole getRole(String uid) {
		return role;
	}


	/**
	 * 通过职位ID拿到功能ID
	 * @param poseId:职位ID
	 * @return 用户拥有的功能列表
	 */
	public String[] getRoleUIDsByUser(String poseId) { 
		if(poseId != null){
			String sql = "SELECT FUNC_ID FROM TR_POSE_FUNC WHERE POSE_ID=?";
			List<Object> params = new ArrayList<Object>();
			params.add(poseId);
			List<String> list = factory.select(sql, params, new DAOCallback<String>(){
				public String wrapper(ResultSet rs, int idx) {
					String val = "";
					try {
						val =  rs.getString("FUNC_ID");
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					return val;
				}
			}) ;
			return (String[])list.toArray(new String[list.size()]);
		}
		return new String[]{Constant.Login_Function_Id};
	}


	public AclUser getUser(String uid) {
		AclUser usr = new AclUser();
		usr.setUID(uid);
		usr.setName("anonymUser");
		return usr;
	}

}
