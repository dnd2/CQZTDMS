package com.infodms.dms.actions.sales.planmanage.YearTarget;

import java.util.List;
import java.util.Map;

import com.infodms.dms.po.TmVhclMaterialGroupPO;

public class YearlyPlanConfirmDataOrgnize {
	

	/**
	 * 要设置个参数，对没有任何计划的区域是否显示可控
	 */
	public String[][] orgData(List<Map<String, Object>> list,List<TmVhclMaterialGroupPO> groupList){
		String[][] tableArr=null;
		if(null==list||list.size()==0){
			tableArr=new String[0][0];
			return tableArr;
		}
		//所有车系
		if(null==groupList||groupList.size()==0){
			tableArr=new String[0][0];
			return tableArr;
		}
		int row=list.size()+2;
		int column=groupList.size()+3;
		String[] keyArr=new String[groupList.size()];
		tableArr=new String[row][column];
		tableArr[0][0]="区域";
		for(int i=1;i<groupList.size();i++){
			TmVhclMaterialGroupPO po=groupList.get(i);
			tableArr[0][i]=po.getGroupName();
			keyArr[i-1]=po.getGroupId().toString();
		}
		tableArr[0][column-2]="合计";
		tableArr[0][column-1]="贡献率";
		
		tableArr[row-1][0]="合计";
		Long total=new Long(0);
		for(int l=0;l>keyArr.length;l++){
			String key=keyArr[l];
			tableArr[row-1][l+1]=getAmt(list, key);
			total+=new Long(tableArr[row-1][l+1]);
		}
		tableArr[row-1][column-2]=total.toString();
		tableArr[row-1][column-1]="100%";
		
		
		for(int j=1;j<row-1;j++){
			Map<String, Object> map=list.get(j);
			tableArr[j][0]=(String)map.get("org_name");
			Long sum=new Long(0);
			for(int k=0;k<keyArr.length;k++){
				String key=keyArr[k];
				if(map.containsKey(key)){
					tableArr[j][k+1]=(String)map.get(key.toString());
					sum+=(Long)map.get(key.toString());
				}else{
					tableArr[j][k+1]="0";
					sum+=new Long(0);
				}
			}
			tableArr[j][column-2]=sum.toString();//合计
			tableArr[j][column-1]=sum.longValue()/total.longValue()+"%";//贡献率
		}
		return tableArr;
	}

	private String getAmt(List<Map<String, Object>> list,String key){
		String amt="0";
		Long sum=new Long(0);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			if(map.containsKey(key)){
				amt=(String)map.get(key);
				sum+=new Long(amt);
			}
		}
		return sum.toString();
	}
}
