package codecraft;

import codecraft.origin.Cross;

import java.util.*;

//-encoding utf-8 Mapping.java
public class CrossPosition{
	public static ArrayList<Integer> visitCrosses;
	public static ArrayList<Integer> nearCrosses;

	public CrossPosition(){
		visitCrosses=new ArrayList<>();
		nearCrosses=new ArrayList<>();
	}

	public void recursion(Cross cross){

		if(!visitCrosses.contains(cross.id)){
			visitCrosses.add(cross.id);
			for(int i=0;i<4;i++){
				if(cross.orientation[i].equals(-1)){continue;}
				setPosition(cross,cross.orientation[i],i);
			}
		}

		while(!nearCrosses.isEmpty()){
			recursion(DynamicData.staticCrossMap.get(nearCrosses.remove(0)));
		}
	}
	public void setPosition(Cross cross,Integer road, int index){

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
			if(index==0){
				DynamicData.staticCrossMap.get(nextCross).position[0]=cross.position[0];
				DynamicData.staticCrossMap.get(nextCross).position[1]=cross.position[1]+1;
			}
			if(index==1){
				DynamicData.staticCrossMap.get(nextCross).position[0]=cross.position[0]+1;
				DynamicData.staticCrossMap.get(nextCross).position[1]=cross.position[1];
			}
			if(index==2){
				DynamicData.staticCrossMap.get(nextCross).position[0]=cross.position[0];
				DynamicData.staticCrossMap.get(nextCross).position[1]=cross.position[1]-1;
			}
			if(index==3){
				DynamicData.staticCrossMap.get(nextCross).position[0]=cross.position[0]-1;
				DynamicData.staticCrossMap.get(nextCross).position[1]=cross.position[1];
			}

		}

	}
	
}