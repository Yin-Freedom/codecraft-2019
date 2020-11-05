package codecraft;

import codecraft.dynamic.DynamicCar;
import codecraft.dynamic.DynamicCross;
import codecraft.dynamic.DynamicRoad;
import codecraft.origin.Car;
import codecraft.origin.Cross;
import codecraft.origin.Road;

import java.io.*;
import java.lang.reflect.Array;
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

	/**
	 * 冒泡排序
	 */
	public void BubbleSort(){
		ArrayList<Car> list=DynamicData.cars;
		for(int i=0;i<list.size();i++){
			for(int j=0;j<list.size()-i-1;j++){
				if(list.get(j).id>list.get(j+1).id){
					Car temp=list.get(j);
					list.set(j,list.get(j+1));
					list.set(j+1,temp);
				}
			}
		}
	}
	/**
	 * 堆排序
	 */
	public void sortCarWithId(){
		carBulidHeap(DynamicData.cars);
		int heapSize=DynamicData.cars.size();
		while(heapSize>1){
			carswap(DynamicData.cars,0,--heapSize);
			carHeapify(DynamicData.cars,0,heapSize);
		}
	}
	public void carBulidHeap(ArrayList<Car> list){
		for(int i=list.size()/2-1;i>=0;i--){
			carHeapify(list,i,list.size());
		}
	}
	public void carHeapify(ArrayList<Car> list, int father, int length){
		int leftChild=father*2+1;
		int rightChild=father*2+2;
		int max=father;
		if(leftChild<length && list.get(leftChild).id>list.get(max).id){
			max=leftChild;
		}
		if(rightChild<length && list.get(rightChild).id>list.get(max).id){
			max=rightChild;
		}
		if(max!=father){
			carswap(list,father,max);
			carHeapify(list,max,length);
		}
	}
	public void carswap(ArrayList<Car> list, int a, int b){
		Car tempCar=list.get(a);
		list.set(a,list.get(b));
		list.set(b,tempCar);
	}



	public void sortCrossWithId(){
		BulidHeap(DynamicData.crosses);
		int heapSize=DynamicData.crosses.size();
		while(heapSize>1){
			swap(DynamicData.crosses,0,--heapSize);
			Heapify(DynamicData.crosses,0,heapSize);
		}
	}
	public void BulidHeap(ArrayList<Integer> list){
		for(int i=list.size()/2-1;i>=0;i--){
			Heapify(list,i,list.size());
		}
	}
	public void Heapify(ArrayList<Integer> list, int father, int length){
		int leftChild=father*2+1;
		int rightChild=father*2+2;
		int max=father;
		if(leftChild<length && DynamicData.staticCrossMap.get(list.get(leftChild)).id>DynamicData.staticCrossMap.get(list.get(max)).id){
			max=leftChild;
		}
		if(rightChild<length && DynamicData.staticCrossMap.get(list.get(rightChild)).id>DynamicData.staticCrossMap.get(list.get(max)).id){
			max=rightChild;
		}
		if(max!=father){
			swap(list,father,max);
			Heapify(list,max,length);
		}
	}
	public void swap(ArrayList<Integer> list, int a, int b){
		Integer tempCross=list.get(a);
		list.set(a,list.get(b));
		list.set(b,tempCross);
	}
	/*public static void main(String[] args) {
		new ImportData(DynamicData.carPath,
				DynamicData.roadPath,
				DynamicData.crossPath,
				DynamicData.presetAnswerPath,
				DynamicData.ansPath
		);
	}*/
}