package codecraft.origin;

import codecraft.dynamic.DynamicRoad;

import java.util.ArrayList;
import java.util.List;

//-encoding utf-8 Cross.java
public class Cross{
	//(路口id,道路id,道路id,道路id,道路id)
	//(1, 501, 513, -1, -1)
	public Integer id;
	public Integer r1;
	public Integer r2;
	public Integer r3;
	public Integer r4;

	public Integer top;
	public Integer bottom;
	public Integer left;
	public Integer right;

	public Integer[] orientation;
	public int[] position;

	public Integer[] sortRoads;

	public Cross(String crossId, String r1, String r2, String r3, String r4) {
		orientation=new Integer[4];
		position=new int[2];

		this.id = Integer.valueOf(crossId);
		orientation[0] = Integer.valueOf(r1);
		orientation[1] = Integer.valueOf(r2);
		orientation[2] = Integer.valueOf(r3);
		orientation[3] = Integer.valueOf(r4);
		//coordinate=new Integer[2];

		this.r1=orientation[0];
		this.r2=orientation[1];
		this.r3=orientation[2];
		this.r4=orientation[3];
		sortRoads=roadSorting(orientation);

	}

	//r=-1,返回Integer.MAX_VALUE
	public static Integer[] roadSorting(Integer[] roads){
		int n=4;

		Integer[] list=new Integer[n];
		for(int i=0;i<4;i++){
			if(roads[i].equals(-1)){
				list[i]=Integer.MAX_VALUE;
			}else{
				list[i]=roads[i];
			}
		}

		int index=0;
		for(int i=0;i<n-1;i++){
			for(int j=0;j<n-i-1;j++){
				if(list[j].intValue()>list[j+1].intValue()){
					Integer temp=list[j+1];
					list[j+1]=list[j];
					list[j]=temp;
				}
			}
		}
		return list;
	}
	
	public void rotation(int N){
		for(int i=0;i<N;i++){
			Integer temp=orientation[3];
			orientation[3]=orientation[2];
			orientation[2]=orientation[1];
			orientation[1]=orientation[0];
			orientation[0]=temp;
		}
	}
}