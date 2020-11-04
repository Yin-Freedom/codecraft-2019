package codecraft;

import codecraft.dynamic.DynamicCar;
import codecraft.dynamic.DynamicCross;
import codecraft.dynamic.DynamicRoad;
import codecraft.origin.Car;
import codecraft.origin.Cross;
import codecraft.origin.Road;

import java.io.*;
import java.util.*;

//-encoding utf-8 ImportData.java
public class ImportData{
	
	public List<Car> carList=new ArrayList<>();
	public List<Cross> crossList=new ArrayList<>();
	public List<Road> roadList=new ArrayList<>();

	public ArrayList<Car> cars=new ArrayList<>();
	public ArrayList<Integer> crosses=new ArrayList<>();
	public ArrayList<Road> roads=new ArrayList<>();


	public ImportData(String carPath, String roadPath, String crossPath, String presetAnswerPath, String ansPath){
		long time=System.currentTimeMillis();

		importStaticData(carPath,crossPath,roadPath,presetAnswerPath,ansPath);
		importDynamicData();
	}
	public void importStaticData(String carPath,String crossPath,String roadPath,String presetAnswer,String ansPath){
		DynamicData.staticCarMap =readCar(carPath);
		DynamicData.staticCrossMap =readCross(crossPath);
		DynamicData.staticRoadMap =readRoad(roadPath);
	}
	public void importDynamicData(){
		for(Car car:DynamicData.staticCarMap.values()){
			DynamicCar newCar=new DynamicCar(car);
			DynamicData.dynamicCarMap.put(car.id,newCar);
			//DynamicData.carInGarage.put(car.id,newCar);
		}
		for(Road road:DynamicData.staticRoadMap.values()){
			DynamicData.dynamicRoadMap.put(road.id,new DynamicRoad(road));
		}
		for(Cross cross:DynamicData.staticCrossMap.values()){
			DynamicData.dynamicCrossMap.put(cross.id,new DynamicCross(cross));
		}

		setCarStartTime();
		GetSolution.carPathSolution();
	}

	public Map<Integer,Car> readCar(String path){
		Map<Integer, Car> map = new HashMap<>();
		File file=new File(path);
		//250ms
		try{
			FileReader reader=new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String line=null;
			while((line=br.readLine())!=null){
				if(line.isEmpty() || line.charAt(0)=='#'){
					continue;
				}
				String s1=line.substring(line.lastIndexOf("(")+1);
				//")" is a special char in regex
				String s2=s1.replaceAll("[)]","");
				s2=s2.replaceAll(" ","");

//				String[] str=s2.split(",",5);
//				Car car=new Car(str[0],str[1],str[2],str[3],str[4]);

				String[] str=s2.split(",",7);
				if(str[5]==null){
					Car car=new Car(str[0],str[1],str[2],str[3],str[4]);
					carList.add(car);
					cars.add(car);
					map.put(Integer.valueOf(str[0]),car);
				}else{
					Car car=new Car(str[0],str[1],str[2],str[3],str[4],str[5],str[6]);
					carList.add(car);
					cars.add(car);
					map.put(Integer.valueOf(str[0]),car);
				}
			}
			DynamicData.cars=cars;
			br.close();
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return map;
	}

	public Map<Integer,Cross> readCross(String path){
		Map<Integer, Cross> map = new HashMap<>();
		File file=new File(path);
		try{
			FileReader reader=new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String line=null;
			while((line=br.readLine())!=null){
				if(line.isEmpty() || line.charAt(0)=='#'){
					continue;
				}
				String s1=line.substring(line.lastIndexOf("(")+1);
				//")" is a special char in regex
				String s2=s1.replaceAll("[)]","");
				s2=s2.replaceAll(" ","");
				String[] str=s2.split(",",5);
				
				Cross cross=new Cross(str[0],str[1],str[2],str[3],str[4]);
				crossList.add(cross);
				crosses.add(cross.id);
				map.put(Integer.valueOf(str[0]),cross);
			}
			DynamicData.crosses=crosses;
			br.close();
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return map;
	}
	public Map<Integer,Road> readRoad(String path){
		Map<Integer, Road> map = new HashMap<>();
		File file=new File(path);
		
		try{
			FileReader reader=new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String line=null;
			while((line=br.readLine())!=null){
				if(line.isEmpty() || line.charAt(0)=='#'){
					continue;
				}
				String s1=line.substring(line.lastIndexOf("(")+1);
				//")" is a special char in regex
				String s2=s1.replaceAll("[)]","");
				s2=s2.replaceAll(" ","");
				String[] str=s2.split(",",7);
				Road road=new Road(str[0],str[1],str[2],str[3],str[4],str[5],str[6]);
				roadList.add(road);
				roads.add(road);
				map.put(Integer.valueOf(str[0]),road);
			}
			DynamicData.roads=roads;
			br.close();
			reader.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return map;
	}
	public void setCarStartTime(){
		int i=0;
		for(DynamicCar car:DynamicData.dynamicCarMap.values()){
			car.startTime=DynamicData.departCarNumber*(int)Math.floor(i/DynamicData.departCarNumber);
			i++;
		}
	}
	
	/*public static void main(String[] args) {
		ImportData imData=new ImportData();
	}*/
}