package codecraft.dynamic;

import codecraft.origin.Car;
import codecraft.enums.CarDir;
import codecraft.enums.CarStatus;

import java.util.ArrayList;
import java.util.List;

public class DynamicCar {
    public Car info;
    public CarDir dir;
    public int runSpeed;
    public int startTime;
    public Integer currentRoad;
    public int currentRoadLane;
    public Integer nextRoad;
    public int position;

    public CarStatus status;
    public boolean blocked=false;
    public DynamicCar blockedCar;

    public boolean isWait;
    public Integer waitCar;
    public Integer waitRoad;

    public boolean isArrived;

    public List<Integer> path;
    public List<Integer> passPath;

    public DynamicCar(Car info){
        this.info=info;
        path=new ArrayList<>();
        passPath=new ArrayList<>();
    }

    public void format(){
        blocked=false;
        blockedCar=null;
        isWait=false;
        waitCar=null;
        waitRoad=null;
        status=null;
        dir=null;
    }
    public void arrivedFormat(){
        currentRoad=null;
        nextRoad=null;
        status=null;
        position=0;
    }
}
