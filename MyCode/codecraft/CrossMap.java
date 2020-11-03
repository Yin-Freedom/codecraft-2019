package codecraft;

import codecraft.origin.Road;

import java.util.*;

public class CrossMap {
    private HashMap<Integer, Bag<Road>> adj;

	//读入
    public CrossMap(ArrayList<Road> roads) {
        adj = new HashMap<>();
        for(Road road : roads) {
            adj.put(road.from, new Bag<>());
        }

        for(Road road:DynamicData.roads){
            this.addRoad(road);
        }
    }

    public void addRoad(Road road) {
        Bag<Road> bag;
        bag = adj.get(road.from);
        bag.add(road);
        adj.put(road.from, bag);
		
    }

	//i,j为cross的id
    public Road find(int i, int j) {

        if(adj.get(i)==null){
            return null;
        }
        for(Road road : adj.get(i)) {
            if(road.to == j) {
                return road;
            }
        }
        //System.out.println("No way from " + i + " to " + j + "!");
        return null;
    }
}