package codecraft;

import codecraft.algorithm.Floyd;
import codecraft.dynamic.DynamicCar;
import codecraft.dynamic.DynamicCross;
import codecraft.dynamic.DynamicRoad;
import codecraft.origin.Car;
import codecraft.origin.Cross;
import codecraft.origin.Road;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author 尹辉东
 */
public class DynamicData{
	public static long startTimeMillis;
	public static long currentMillisTime;
	public static Map<Integer, Car> staticCarMap =new HashMap<>();
	public static Map<Integer, Cross> staticCrossMap =new HashMap<>();
	public static Map<Integer, Road> staticRoadMap =new HashMap<>();

	public static ArrayList<Car> cars=new ArrayList<>();
	public static ArrayList<Integer> crosses=new ArrayList<>();
	public static ArrayList<Road> roads=new ArrayList<>();
	public static List<Road> r=new ArrayList<>();

	public static Map<Integer,DynamicCar> dynamicCarMap=new HashMap<>();
	public static Map<Integer,DynamicCross> dynamicCrossMap=new HashMap<>();
	public static Map<Integer,DynamicRoad> dynamicRoadMap=new HashMap<>();
	//public static Map<Integer,DynamicCar> carInGarage=new HashMap<>();

	public static Map<Integer,DynamicCar> departCarMap=new HashMap<>();
	public static List<Integer> carOutGarage=new ArrayList<>();
	public static List<Integer> arrivedCars=new ArrayList<>();
	public static int timeSlice=0;
	public static int runTimeSlice=1000;
	public static int carInSystem=0;
	
	public static String pre="D:/life/huawei_codecraft/MyCode/codecraft/data/input_2/";
//	public static String carPath = DynamicData.pre+"car_3.txt";
	public static String carPath = DynamicData.pre+"car.txt";
	public static String roadPath = DynamicData.pre+"road.txt";
	public static String crossPath = DynamicData.pre+"cross.txt";
	public static String presetAnswerPath = DynamicData.pre+"presetAnswer.txt";
	public static String ansPath=DynamicData.pre+"answer.txt";
	public static Log log= LogFactory.getLog(DynamicData.class);

	public static Floyd floyd=new Floyd();
	public static int currentDepartCarNumber=0;
	/* 100片
	150
	140 1914
	130
	120 2052
	110 2086
	80  1868
	70  1745
	60  1616
	40  1197

	*/
	public static int departCarNumber=100;
	public static int realDepartCarNumber=0;

}