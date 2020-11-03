package codecraft.dynamic;

import codecraft.DynamicData;
import codecraft.origin.Road;


import java.util.ArrayList;
import java.util.List;

public class DynamicRoad {
    public Road info;
    public List<DynamicCar> queues;

    public Integer oppositeRoad;
    public Integer leftRoad;
    public Integer rightRoad;
    public DynamicCar[][] leftCarMap;
    public DynamicCar[][] rightCarMap;

    public List<Integer> carInRoad=new ArrayList<>();


    public DynamicRoad(Road info){
        this.info=info;
        queues=new ArrayList<>();
        leftCarMap=new DynamicCar[info.laneNumber][info.length];
        rightCarMap=new DynamicCar[info.laneNumber][info.length];
    }

    public Integer getOppositeRoad(Integer cross){
        int x;
        for(x=0;x<4;x++){
            if(DynamicData.staticCrossMap.get(cross).orientation[x].equals(this.info.id)){
                break;
            }
        }
        if(DynamicData.staticCrossMap.get(cross).orientation[(x+2)>3?(x+2-4):x+2].equals(-1)){
            return null;
        }
        return DynamicData.staticCrossMap.get(cross).orientation[(x+2)>3?(x+2-4):x+2];
    }
    public Integer getLeftRoad(Integer cross){
        int x;
        for(x=0;x<4;x++){
            if(DynamicData.staticCrossMap.get(cross).orientation[x].equals(this.info.id)){
                break;
            }
        }
        if(DynamicData.staticCrossMap.get(cross).orientation[(x+1)>3?(x+1-4):x+1].equals(-1)){
            return null;
        }
        return DynamicData.staticCrossMap.get(cross).orientation[(x+1)>3?(x+1-4):x+1];
    }
    public Integer getRightRoad(Integer cross){
        int x;
        for(x=0;x<4;x++){
            if(DynamicData.staticCrossMap.get(cross).orientation[x].equals(this.info.id)){
                break;
            }
        }
        if(DynamicData.staticCrossMap.get(cross).orientation[(x+3)>3?(x+3-4):x+3].equals(-1)){
            return null;
        }
        return DynamicData.staticCrossMap.get(cross).orientation[(x+3)>3?(x+3-4):x+3];
    }
}
