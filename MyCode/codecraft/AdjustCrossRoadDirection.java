package codecraft;

import codecraft.origin.Cross;

import java.util.*;

//-encoding utf-8 Mapping.java
public class AdjustCrossRoadDirection {
	public static ArrayList<Integer> visitCrosses;
	public static ArrayList<Integer> nearCrosses;
	public static HashMap<Integer,ArrayList<Integer>> currentCross;

	public AdjustCrossRoadDirection(){
		visitCrosses=new ArrayList<>();
		nearCrosses=new ArrayList<>();
		recursion(DynamicData.staticCrossMap.get(DynamicData.crosses.get(0)));
	}

	public void recursion(Cross cross){

		if(!visitCrosses.contains(cross.id)){
			visitCrosses.add(cross.id);
			for(int i=0;i<4;i++){
				if(cross.orientation[i].equals(-1)){continue;}
				setDirection(cross,cross.orientation[i],i);
			}
		}

		while(!nearCrosses.isEmpty()){
			recursion(DynamicData.staticCrossMap.get(nearCrosses.remove(0)));
		}
	}
	public void setDirection(Cross cross,Integer road, int index){

		Integer nextCross;
		if(DynamicData.staticRoadMap.get(road).from.equals(cross.id)){
			nextCross=DynamicData.staticRoadMap.get(road).to;
		}else{
			nextCross=DynamicData.staticRoadMap.get(road).from;
		}
		if(!visitCrosses.contains(nextCross)){
			nearCrosses.add(nextCross);
		}

		if(!visitCrosses.contains(nextCross)){
			int k=0;
			for(k=0;k<4;k++){
				if(DynamicData.staticCrossMap.get(nextCross).orientation[k].equals(road)){
					break;
				}
			}

			//枚举的话需要16种，减法的话只需要5种。
			int n=2;
			if((n=index-k)!=2 || (n=index-k)!=-2){
				if(n==0){DynamicData.staticCrossMap.get(nextCross).rotation(2);}
				if(n==1){DynamicData.staticCrossMap.get(nextCross).rotation(3);}
				if(n==-1){DynamicData.staticCrossMap.get(nextCross).rotation(1);}
				if(n==-3){DynamicData.staticCrossMap.get(nextCross).rotation(3);}
				if(n==3){DynamicData.staticCrossMap.get(nextCross).rotation(1);}
			}
		}

	}
	
}