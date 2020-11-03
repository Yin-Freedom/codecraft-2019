package codecraft;

import codecraft.algorithm.Floyd;
import codecraft.origin.Car;
import codecraft.origin.Road;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Answer {

    public void writeAnswer(){
        ImportData im=new ImportData(DynamicData.carPath,
                DynamicData.roadPath,
                DynamicData.crossPath,
                DynamicData.presetAnswerPath,
                DynamicData.ansPath);
        Floyd f=new Floyd();
        f.createMap(DynamicData.roads,DynamicData.crosses);
        CrossMap map=new CrossMap(DynamicData.roads);

        String filePath=DynamicData.pre+"answer.txt";
        try(FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);) {

            //#(carId,StartTime,RoadId...)
            //(1001, 1, 501, 502, 503, 516, 506, 505, 518, 508, 509, 524)
            bw.write("#(carId,StartTime,RoadId...)\n");
            for (Car car : DynamicData.cars) {
                //System.out.printf("from:%d,to:%d",car.from,car.to);
                ArrayList<Road> path=f.findRoads(map,car.from,car.to,DynamicData.crosses);
                bw.write("(");
                bw.write(car.id+",");
                for(Road road:path){
                    if(path.indexOf(road)==(path.size()-1)){
                        bw.write(road.id.toString());
                        break;
                    }
                    bw.write(road.id+",");
                }
                bw.write(")\n");
            }
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
