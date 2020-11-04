package codecraft.dynamic;

import codecraft.DynamicData;
import codecraft.origin.Cross;
import codecraft.origin.Road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicCross {
    public Cross info;
    public List<Integer> sortRoads=new ArrayList<>();
    public DynamicCross(Cross info){
        this.info=info;
        setStaticCrossRoundRoad();
    }


    public void setStaticCrossRoundRoad(){
        for(int i=0;i<4;i++){
            if(info.orientation[i].equals(-1)){
                continue;
            }
            DynamicData.dynamicRoadMap.get(info.orientation[i]).oppositeRoad=info.orientation[roundPlus(i+2)];
            DynamicData.dynamicRoadMap.get(info.orientation[i]).leftRoad=info.orientation[roundPlus(i+1)];
            DynamicData.dynamicRoadMap.get(info.orientation[i]).rightRoad=info.orientation[roundPlus(i+3)];
        }
    }

    public int roundPlus(int element){
        if(element>3){
            return element-4;
        }
        return element;
    }
}
