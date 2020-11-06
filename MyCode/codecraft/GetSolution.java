package codecraft;

import codecraft.algorithm.Floyd;
import codecraft.dynamic.DynamicCar;
import codecraft.dynamic.DynamicCross;
import codecraft.dynamic.DynamicRoad;
import codecraft.enums.CarDir;
import codecraft.enums.CarStatus;
import codecraft.origin.Car;
import codecraft.origin.Cross;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 尹辉东
 *
 * 20201027
 * 1.需要改下DynamicRoad leftRoad\rightRoad\oppositeRoad 解决
 * 2.driveCarInGarage rightCarMap\leftCarMap 解决
 * 3.runSpeed没更新，需要每次mark车辆时更新runSpeed吗？
 * 4.WAIT被堵在当前道路需要run in road吗？处理方式是在当前道路行驶
 *
 * 5.runInCross中passPath的逻辑问题
 */
public class GetSolution{
	private boolean deadlock=false;

	public void solution(){
		driveCarInGarage();
	}
	public void getSolution(){
		AdjustCrossRoadDirection m=new AdjustCrossRoadDirection();

		while(DynamicData.arrivedCars.size()!=DynamicData.cars.size()) {

			DynamicData.carInSystem=0;
			//DynamicData.currentMillisTime=System.currentTimeMillis();
			for (DynamicRoad road : DynamicData.dynamicRoadMap.values()) {
				roadBlockState(road);
				mark(road);
			}


			//drive All Wait Car
			//只设置一次车辆的dir,只设置WAIT车辆的dir
			for (Cross cross:DynamicData.staticCrossMap.values()){
				for (Integer road : cross.sortRoads) {
					if (road.equals(Integer.MAX_VALUE)) {
						continue;
					}
					//设置方向同样只考虑进入路口方向的车辆
					getCarDirection(cross, road);
				}
			}

			/**
			 * 调整所有车道内Status为End的车辆
			 * 只调整一次
			 */
			for(DynamicRoad road:DynamicData.dynamicRoadMap.values()){
				runInRoad(road);
			}


			//一个事件片内，一次调度时7与8的调度会出现多次重复调度
			for (Integer crossId : DynamicData.crosses) {
				Cross cross=DynamicData.staticCrossMap.get(crossId);
				int cycleTime=0;
				boolean cycleEnd=false;
				crossOut:
				while(!cycleEnd) {
					//System.out.printf("  corss:%5d,循环次数：%s\n",crossId,cycleTime);
					cycleEnd=true;
					for (Integer r : cross.sortRoads) {
						if (r.equals(Integer.MAX_VALUE)) {
							continue;
						}
						DynamicRoad road = DynamicData.dynamicRoadMap.get(r);

						//只调度该路口出路口的道路
						int roadNumber;
						for(roadNumber=0;roadNumber<4;roadNumber++){
							if(DynamicData.staticCrossMap.get(cross.id).orientation[roadNumber].equals(r)){
								break;
							}
						}
						DynamicCar[][] cars=road.rightCarMap;
						if(roadNumber==0 || roadNumber==3){
							cars=road.rightCarMap;
						}
						if(roadNumber==1 || roadNumber==2){
							cars=road.leftCarMap;
						}

						DynamicCar car;
						int i = 0, j = 0;
						waitBreak:
						{
							for (j = road.info.length - 1; j >= 0; j--) {
								for (i = 0; i < cars.length; i++) {
									//runInRoad format() null了car的status
									if (cars[i][j] == null || cars[i][j].status==null || !cars[i][j].status.equals(CarStatus.WAIT)) {
										continue;
									}

									car = cars[i][j];
									if(car.dir!=null){
										cycleEnd=false;
									}

									if(car.status==null || !car.status.equals(CarStatus.WAIT)){continue;}

									if (car.dir==null && car.isArrivedLastRoad){
										int speed=(road.info.speed<car.info.speed?road.info.speed:car.info.speed);
										int S1=road.info.length-1-j;
										if(speed>S1){
											car.isArrived=true;
											DynamicData.arrivedCars.add(car.info.id);
											cars[i][j]=null;
											//System.out.printf("%s到达终点\n",car.info.id);
											continue;
										}else{
											runInRoadOfCar(road,cars,car,i,j);
										}
										continue;
									}

									if (car.dir == CarDir.D) {
										runInCross(road.info.id, car.nextRoad,cross.id,i,j);
										continue;
									}

									//等待rightRoad的D车辆 && right L
									if (car.dir == CarDir.L) {
										if (findFirstCarOnRoad(crossId,road.getRightRoad(crossId)) != null && (findFirstCarOnRoad(crossId,road.getRightRoad(crossId)).dir == CarDir.D)) {
											car.isWait = true;
											car.waitRoad = road.getRightRoad(crossId);
											car.waitCar = findFirstCarOnRoad(crossId,road.getRightRoad(crossId)).info.id;
											break waitBreak;
										}
										if(findFirstCarOnRoad(crossId,road.getRightRoad(crossId))!=null && findFirstCarOnRoad(crossId,road.getRightRoad(crossId)).dir==CarDir.L){
											DynamicCar waitCar=findFirstCarOnRoad(crossId,road.getRightRoad(crossId));
											//该左转车辆可能在等待，
											if(waitCar.isWait && findFirstCarOnRoad(crossId,waitCar.waitRoad)!=null
													&& findFirstCarOnRoad(crossId,waitCar.waitRoad).info.id.equals(waitCar.waitCar)){
												runInCross(road.info.id, car.nextRoad,crossId,i,j);
												continue;
											}
											car.isWait=true;
											car.waitCar=waitCar.info.id;
											car.waitRoad=road.getRightRoad(crossId);
											break waitBreak;
										}
										runInCross(road.info.id, car.nextRoad,crossId,i,j);
									}

									//等待leftD && oppositeL
									if (car.dir == CarDir.R) {
										if(findFirstCarOnRoad(crossId,road.getLeftRoad(crossId))!=null && findFirstCarOnRoad(crossId,road.getLeftRoad(crossId)).dir==CarDir.D){
											car.isWait=true;
											car.waitCar=findFirstCarOnRoad(crossId,road.getLeftRoad(crossId)).info.id;
											car.waitRoad=road.getLeftRoad(crossId);
											break waitBreak;
										}
										if(findFirstCarOnRoad(crossId,road.getOppositeRoad(crossId))!=null && findFirstCarOnRoad(crossId,road.getOppositeRoad(crossId)).dir==CarDir.L){
											DynamicCar waitCar=findFirstCarOnRoad(crossId,road.getOppositeRoad(crossId));
											//该左转车辆可能在等待，
											if(waitCar.isWait && findFirstCarOnRoad(crossId,waitCar.waitRoad)!=null
													&& findFirstCarOnRoad(crossId,waitCar.waitRoad).info.id.equals(waitCar.waitCar)){
												runInCross(road.info.id, car.nextRoad,crossId,i,j);
												continue;
											}
											car.isWait=true;
											car.waitCar=waitCar.info.id;
											car.waitRoad=road.getOppositeRoad(crossId);

											judgeDeadlock(car);
											break waitBreak;
										}
										//都没有阻碍，R通行
										runInCross(road.info.id, car.nextRoad,crossId,i,j);
									}
									//
								}
							}
						}

					}
					cycleTime++;
					if(cycleTime>100){
						System.out.printf("循环大于%5d次\n",cycleTime);
						break;
					}
				}
			}
			//每个cross，每条road循环结束

			//更新道路实时负载
			for(DynamicRoad road:DynamicData.dynamicRoadMap.values()){
				int k=0,t=0;
				DynamicCar[][] carMap=road.rightCarMap;
				for(int i=0;i<carMap.length;i++){
					for(int j=0;j<carMap[0].length;j++){
						if(carMap[i][j]!=null){
							k+=1;
							DynamicData.carInSystem++;
						}
					}
				}
				carMap=road.leftCarMap;
				for(int i=0;i<carMap.length;i++){
					for(int j=0;j<carMap[0].length;j++){
						if(carMap[i][j]!=null){
							t+=1;
							DynamicData.carInSystem++;
						}
					}
				}
				road.realLoad=k+t+1;
			}


			driveCarInGarage();
			deadlock=judgeDeadlock();
			DynamicData.timeSlice+=1;

			if(DynamicData.timeSlice%100==0){
			}
			System.out.printf("已处理时间片:%s,carInSystem:%s,已到达车辆:%s\n",
					DynamicData.timeSlice,
					DynamicData.carInSystem,
					DynamicData.arrivedCars.size());

			/*if(deadlock){
				System.out.println("发生死锁----------------------------------------");
				break;
			}*/

			if(DynamicData.timeSlice>DynamicData.runTimeSlice) {
				System.out.printf("时间片超过%s",DynamicData.runTimeSlice);
				System.out.printf("用时:%s s,",(System.currentTimeMillis()-DynamicData.startTimeMillis)/1000);
				break;
			}
		}

	}

	//标记road上每辆车的行驶状态
	public void mark(DynamicRoad road){
		DynamicCar[][] carMap=road.rightCarMap;
		markWithTwoDirection(road,carMap);
		carMap=road.leftCarMap;
		markWithTwoDirection(road,carMap);
	}


	/**
	 * 标记与行进顺序无关，但是三车道时同处于
	 * 但是如果改了block顺序由出车道到进车道，则会导致END的车辆block出错，所以不能改。
	 * @param road
	 * @param carMap
	 */
	public void markWithTwoDirection(DynamicRoad road, DynamicCar[][] carMap){
		for(int j=carMap[0].length-1;j>=0;j--){
				for(int i=0;i<carMap.length;i++){
				if(carMap[i][j]==null){continue;}

				int runSpeed=(road.info.speed>carMap[i][j].info.speed?carMap[i][j].info.speed:road.info.speed);

				if((!carMap[i][j].blocked) && (road.info.length-1-carMap[i][j].position)<runSpeed){
					carMap[i][j].status= CarStatus.WAIT;
				}
				if((!carMap[i][j].blocked) && (road.info.length-carMap[i][j].position)>=runSpeed){
					carMap[i][j].status= CarStatus.END;
				}

				//no out cross
				if(carMap[i][j].blocked && carMap[i][j].blockedCar.status.equals(CarStatus.WAIT)){
					carMap[i][j].status= CarStatus.WAIT;
				}
				if(carMap[i][j].blocked && carMap[i][j].blockedCar.status.equals(CarStatus.END)){
					carMap[i][j].status= CarStatus.END;
					//carMap[i][j].runSpeed=carMap[i][j].blockedCar.position-carMap[i][j].position;
				}

			}
		}
	}

	//确定道路上每辆车的blocked，与车辆行进顺序无关
	public void roadBlockState(DynamicRoad road){
		DynamicCar[][] carMap=road.rightCarMap;
		roadBlockStateWithTwoDirection(road,carMap);
		carMap=road.leftCarMap;
		roadBlockStateWithTwoDirection(road,carMap);
	}
	public void roadBlockStateWithTwoDirection(DynamicRoad road, DynamicCar[][] carMap){
		for(int i=0;i<carMap.length;i++){
			for(int j=0;j<carMap[i].length;j++){
				if(carMap[i][j]==null){continue;}
				carMap[i][j].blocked=false;
				carMap[i][j].blockedCar=null;
				carMap[i][j].runSpeed=0;

				int runSpeed=carMap[i][j].info.speed>road.info.speed?road.info.speed:carMap[i][j].info.speed;
				for(int k=1;k<=runSpeed;k++){
					if(j+k>=carMap[i].length){
						carMap[i][j].blocked= false;
						break;
					}
					if(carMap[i][j+k]!=null){
						carMap[i][j].blocked= true;
						carMap[i][j].blockedCar=carMap[i][j+k];
						break;
					}
				}
			}
		}
	}

	private void getCarDirection(Cross cross,Integer road){
		int x,y;
		for(x=0;x<4;x++){
			if(DynamicData.staticCrossMap.get(cross.id).orientation[x].equals(road)){
				break;
			}
		}
		DynamicCar[][] carMap=DynamicData.dynamicRoadMap.get(road).rightCarMap;
		if(x==0 || x==3){
			carMap=DynamicData.dynamicRoadMap.get(road).rightCarMap;
		}
		if(x==1 || x==2){
			carMap=DynamicData.dynamicRoadMap.get(road).leftCarMap;
		}
		getCarDirectionWithRoadDirection(cross,carMap);

	}
	public void getCarDirectionWithRoadDirection(Cross cross,DynamicCar[][] carMap){
		for(int i=0;i<carMap.length;i++) {
			for (int j = 0; j < carMap[i].length; j++) {
				if(carMap[i][j]==null){continue;}
				if(carMap[i][j].status!=CarStatus.WAIT){
					continue;
				}

				DynamicCar car=carMap[i][j];
				//当前道路为path的最后一条道路，将car.dir设置为null
				if(car.path.indexOf(car.currentRoad)==(car.path.size()-1)){
					car.dir=null;
					car.isArrivedLastRoad=true;
					continue;
				}
				car.nextRoad=car.path.get(car.path.indexOf(car.currentRoad)+1);
				Integer nextRoad=car.nextRoad;
				Integer nowRoad=car.currentRoad;
				int x,y;
				for(x=0;x<4;x++){
					if(cross.orientation[x].equals(nowRoad)) break;
				}
				for(y=0;y<4;y++){
					if(cross.orientation[y].equals(nextRoad)) break;
				}

				int ySubX=y-x;
				if(ySubX<0){
					ySubX=y-x+4;
				}
				if(ySubX==2){
					car.dir= CarDir.D;
				}
				if(ySubX==1){
					car.dir= CarDir.L;
				}
				if(ySubX==3){
					car.dir= CarDir.R;
				}
			}
		}
	}

	/**
	 * 与道路right left有关
	 * 如果被堵住了，需要从上次堵住的地方发车
	 *
	 * logical
	 * for(DynamicData.departCarNumber=150){
	 *     if(堵住了) continue; 先换回遍历所有车，如果在carOutGarage里面 || arrived则continue，每次发150辆
	 *     没堵住：
	 * }
	 * @return
	 */
	public boolean driveCarInGarage(){
		Floyd.update(DynamicData.roads,DynamicData.crosses);
		CrossMap map=new CrossMap(DynamicData.roads);
		DynamicData.realDepartCarNumber=0;

		//if(DynamicData.carOutGarage.size()>DynamicData.dynamicCarMap.size()){ return false;}
		if(DynamicData.carInSystem+DynamicData.arrivedCars.size()==DynamicData.cars.size()){
			return false;
		}

		int k=DynamicData.departCarNumber;

		for(Car carI:DynamicData.cars){
			if(DynamicData.carOutGarage.contains(carI.id)){
				continue;
			}
			if(k<0){
				break;
			}

			DynamicCar car=DynamicData.dynamicCarMap.get(carI.id);
			car.path=Floyd.findPath(map,car.info.from,car.info.to,DynamicData.crosses);
			car.startTime=DynamicData.timeSlice;

			Integer startCross=car.info.from;
			Integer startRoad=car.path.get(0);
			DynamicRoad road=DynamicData.dynamicRoadMap.get(startRoad);
			int index;
			for(index=0;index<4;index++){
				if(DynamicData.staticCrossMap.get(startCross).orientation[index].equals(startRoad)){
					break;
				}
			}
			DynamicCar[][] cars=road.leftCarMap;
			if(index==0 || index==3){
				cars=road.leftCarMap;
			}
			if(index==1 || index==2){
				cars=road.rightCarMap;
			}

			car.runSpeed=(car.info.speed<road.info.speed?car.info.speed:road.info.speed);

			int[] coordinate=getNextRoadSpaceCoordinate(null,road,cars);

			if(coordinate==null){
				continue;
			}
			car.currentRoadLane=coordinate[0];

			if(coordinate[1]+1<car.runSpeed){
				cars[coordinate[0]][coordinate[1]]=car;
				car.currentRoad=road.info.id;
				car.position=coordinate[1];
			}else{
				cars[coordinate[0]][car.runSpeed-1]=car;
				car.currentRoad=road.info.id;
				car.position=car.runSpeed-1;
			}
			DynamicData.carOutGarage.add(car.info.id);
			DynamicData.realDepartCarNumber++;
			k--;
		}
		DynamicData.currentDepartCarNumber+=DynamicData.realDepartCarNumber;

		return false;
	}

	public static int carSpeedInCross(DynamicRoad r1,DynamicRoad r2,DynamicCar car,int S1){
		int R1=r1.info.speed;
		int R2=r2.info.speed;
		int V=car.info.speed;
		int V1=R1<V?R1:V;
		int V2=R2<V?R2:V;
		int SV1=V1;//当前道路单位时间最大行驶距离
		int SV2=V2;//下一条道路单位时间最大行驶距离
		//int S1=0;//car在当前道路可行驶的距离
		int S2;//car在下一条道路可行驶的距离

		S2=SV2-S1;
		if(S1>=SV2){
			if(SV2-S1<0){
				S2=0;
			}
		}
		return S2;
	}


	//get下一道路空位
	//如果i>0,表明第一行没有车，
	public static int[] getNextRoadSpaceCoordinate(DynamicRoad r1, DynamicRoad r2, DynamicCar[][] cars2){
		int[] coordinate=new int[2];
		goOut_2:
		for(int i=0;i<r2.info.laneNumber;i++){
			if(cars2[i][0]!=null){
				coordinate[0]=i+1;
				if(coordinate[0]==r2.info.laneNumber){return null;}
				continue;
			}

			for(int j=0;j<r2.info.length;j++){
 				if(cars2[i][j]!=null){
					coordinate[1]=j-1;
					break goOut_2;
				}else{
					coordinate[1]=j;
				}
			}
		}
		return coordinate;
		//coordinate[0]是有空位的车道
		//coordinate[1]是length
	}

	/**
	 * runInCross需要更新currentRoad currentRoadLane nextRoad passPath position isArrived
	 * 在这里判断是否到达终点
	 *
	 * 与道路right left有关
	 * @param road1
	 * @param road2
	 */
	//通过路口进入下一条道路 verify
	public static void runInCross(Integer road1, Integer road2, Integer cross, int i, int j){
		int x,y;
		for(x=0;x<4;x++){
			if(DynamicData.staticCrossMap.get(cross).orientation[x].equals(road1)){
				break;
			}
		}
		for(y=0;y<4;y++){
			if(DynamicData.staticCrossMap.get(cross).orientation[y].equals(road2)){
				break;
			}
		}


		DynamicRoad r1=DynamicData.dynamicRoadMap.get(road1);
		DynamicRoad r2=DynamicData.dynamicRoadMap.get(road2);

		DynamicCar[][] cars1=r1.rightCarMap;
		DynamicCar[][] cars2=r2.rightCarMap;
		if(x==0 || x==3){
			cars1=r1.rightCarMap;
		}
		if(x==1 || x==2){
			cars1=r1.leftCarMap;
		}

		if(y==0 || y==3){
			cars2=r2.leftCarMap;
		}
		if(y==1 || y==2){
			cars2=r2.rightCarMap;
		}
		runInCrossWithDirection(r1,r2,cars1,cars2,i,j);
	}
	public static boolean runInCrossWithDirection(DynamicRoad r1, DynamicRoad r2
			, DynamicCar[][] cars1, DynamicCar[][] cars2, int i, int j){

		DynamicCar car=cars1[i][j];
		int S1=r1.info.length-1-j;

		int S2=carSpeedInCross(r1,r2,car,S1);
		int[] coordinate= getNextRoadSpaceCoordinate(r1,r2,cars2);
		if(coordinate==null || S2==0){
			//System.out.println(cars1[i][j].info.id+":下一条道路堵死了");
			return runInRoadOfCar(r1,cars1,cars1[i][j],i,j);
		}


		if(coordinate[1]>=(S2-1)){
			cars2[coordinate[0]][S2-1]=car;
			car.position=S2-1;
		}else{
			cars2[coordinate[0]][coordinate[1]]=car;
			car.position=coordinate[1];
		}
		car.currentRoad=r2.info.id;
		car.currentRoadLane=coordinate[0];
		if(car.path.indexOf(car.currentRoad)!=(car.path.size()-1)){
			car.nextRoad=car.path.get(car.path.indexOf(car.currentRoad)+1);
		}
		car.passPath.add(r1.info.id);
		//System.out.printf("%s-%s\n",cars1[i][j].dir,cars1[i][j].info.id);

		car.format();
		//如果在当前道路被堵在原地，同样被删除
		if(S2==0 && S1==0){
			return false;
		}
		cars1[i][j]=null;
		return true;
	}

	/**
	 * runInRoad需要更新DynamicCar position
	 *
	 * 与道路right left有关
	 * @param r
	 */
	//道路内行驶 i从上往下,j从后往前 verify
	public void runInRoad(DynamicRoad r){
		DynamicCar[][] carMap=r.rightCarMap;
		runInRoadWithTwoDirection(r,carMap);
		carMap=r.leftCarMap;
		runInRoadWithTwoDirection(r,carMap);
	}
	public static void runInRoadWithTwoDirection(DynamicRoad r,DynamicCar[][] carMap){
		for(int i=0;i<carMap.length;i++){
			for(int j=carMap[0].length-1;j>=0;j--){
				if(carMap[i][j]==null ){continue;}
				if(carMap[i][j].status==null || !carMap[i][j].status.equals(CarStatus.END)){continue;}

				DynamicCar car=carMap[i][j];
				//runSpeed没更新，需要每次mark车辆时更新runSpeed吗？
				int S=car.info.speed<r.info.speed?car.info.speed:car.info.speed;

				int k;
				//如果前方都是null，则最后一个k值应该+1
				for(k=j+1;k<r.info.length;k++){
					if(carMap[i][k]!=null){break;}
				}

				car.format();
				//如果前方堵住了就跳过,保留状态信息
				if(k-1-j==0){
					continue;
				}
				if(k-1-j>=S){
					carMap[i][j+S]=car;
					car.position=j+S;
				}else{
					carMap[i][k-1]=car;
					car.position=k-1;
				}
				carMap[i][j]=null;
			}
		}
	}
	// verify
	private static boolean runInRoadOfCar(DynamicRoad r, DynamicCar[][] carMap, DynamicCar car,int i,int j){
		int S=car.info.speed<r.info.speed?car.info.speed:car.info.speed;

		int k;
		for(k=j+1;k<r.info.length;k++){
			if(carMap[i][k]!=null){break;}
		}

		car.format();
		if(k-1-j==0){
			return false;
		}
		if(k-1-j>=S){
			carMap[i][j+S]=car;
			car.position=j+S;
		}else{
			carMap[i][k-1]=car;
			car.position=k-1;
		}
		carMap[i][j]=null;
		return true;
	}

	private static DynamicCar findFirstCarOnRoad(Integer cross, Integer r){
		if(r==null){
			return null;
		}

		int roadNumber;
		DynamicRoad road=DynamicData.dynamicRoadMap.get(r);
		for(roadNumber=0;roadNumber<4;roadNumber++){
			if(DynamicData.staticCrossMap.get(cross).orientation[roadNumber].equals(r)){
				break;
			}
		}
		DynamicCar[][] cars=road.rightCarMap;
		if(roadNumber==0 || roadNumber==3){
			cars=road.rightCarMap;
		}
		if(roadNumber==1 || roadNumber==2){
			cars=road.leftCarMap;
		}

		int i;
		int j;
		DynamicCar car=null;
		for(j=road.info.length-1;j>=0;j--){
			for(i=0;i<cars.length;i++){
				if(cars[i][j]!=null){
					car=cars[i][j];
					return car;
				}
			}
		}
		return car;
	}
	public static boolean findCarAtRoad(Integer cross,Integer currentRoad, Integer currentCar){
		DynamicRoad road=DynamicData.dynamicRoadMap.get(currentRoad);
		DynamicCar car=DynamicData.dynamicCarMap.get(currentCar);

		int roadNumber;
		for(roadNumber=0;roadNumber<4;roadNumber++){
			if(DynamicData.staticCrossMap.get(cross).orientation[roadNumber].equals(currentRoad)){
				break;
			}
		}
		DynamicCar[][] cars=road.rightCarMap;
		if(roadNumber==0 || roadNumber==3){
			cars=road.rightCarMap;
		}
		if(roadNumber==1 || roadNumber==2){
			cars=road.leftCarMap;
		}

		int i=0;
		int j=0;
		for(j=road.info.length-1;j>=0;j--){
			for(i=0;i<cars.length;i++){
				if(cars[i][j]==null){continue;}
				if(cars[i][j].info.id.equals(car.info.id)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 死锁只会发生在两次右转等左转的情况
	 * @return
	 */
	public static boolean judgeDeadlock(){
		for(DynamicCar car:DynamicData.dynamicCarMap.values()){
			if(DynamicData.arrivedCars.contains(car.info.id)){continue;}
			if(judgeDeadlock(car)){
				return true;
			}
		}
		return false;
	}
	public static boolean judgeDeadlock(DynamicCar car){
		List<Integer> visitedCar=new ArrayList<>();
		boolean judge=recursion(visitedCar,car);
		if(judge){
			System.out.printf("%s进入死锁,list:",car.info.id);
			System.out.println(Arrays.deepToString(visitedCar.toArray()));
			return true;
		}
		return false;
	}
	public static boolean recursion(List<Integer> list, DynamicCar car){
		DynamicCar blockedCar;
		DynamicCar waitCar;
		if(!list.contains(car.info.id)){
			list.add(car.info.id);
			if(car.blocked){
				blockedCar=car.blockedCar;
				//list.add(blockedCar.info.id);
				boolean judgeResult=recursion(list,blockedCar);
				if(judgeResult){
					return judgeResult;
				}
			}
			if(car.isWait){
				waitCar=DynamicData.dynamicCarMap.get(car.waitCar);
				//list.add(waitCar.info.id);
				boolean judgeResult=recursion(list,waitCar);
				if(judgeResult){
					return judgeResult;
				}
			}
		}else if(list.contains(car.info.id)){
			return true;
		}

		return false;
	}

/*	//验证死锁
	public static void main(String[] args) {
		new ImportData(DynamicData.carPath,
				DynamicData.roadPath,
				DynamicData.crossPath,
				DynamicData.presetAnswerPath,
				DynamicData.ansPath
		);

		DynamicData.dynamicCarMap.get(77990).isWait=true;
		DynamicData.dynamicCarMap.get(77990).waitCar=53426;*//*
		DynamicData.dynamicCarMap.get(53426).blocked=false;
		DynamicData.dynamicCarMap.get(53426).blockedCar=null;*//*

		DynamicData.dynamicCarMap.get(53426).blocked=true;
		DynamicData.dynamicCarMap.get(53426).blockedCar=DynamicData.dynamicCarMap.get(82522);

		DynamicData.dynamicCarMap.get(82522).isWait=true;
		DynamicData.dynamicCarMap.get(82522).waitCar=82523;

		DynamicData.dynamicCarMap.get(82523).isWait=true;
		DynamicData.dynamicCarMap.get(82523).waitCar=82524;

		DynamicData.dynamicCarMap.get(82524).isWait=true;
		DynamicData.dynamicCarMap.get(82524).waitCar=77990;
		System.out.println(judgeDeadlock());
	}*/


}