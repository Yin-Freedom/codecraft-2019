package codecraft;

import codecraft.algorithm.Floyd;
import codecraft.origin.Road;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ImportData im=new ImportData(DynamicData.carPath,
				DynamicData.roadPath,
				DynamicData.crossPath,
				DynamicData.presetAnswerPath,
				DynamicData.ansPath);
		Floyd f=new Floyd();
		f.createMap(DynamicData.roads,DynamicData.crosses);

		ArrayList<Road> path;
		CrossMap map=new CrossMap(DynamicData.roads);

		/*//Floyd D图
		for(int i=0;i<f.d.length;i++){
			for(int j=0;j<f.d.length;j++){
				if(f.d[i][j]==Integer.MAX_VALUE) {
					System.out.printf("--  ",f.d[i][j]);
					continue;
				}
				System.out.printf("%3d ",f.d[i][j]);
			}
			System.out.println("");
		}
		System.out.println();
		//Floyd P图
		for(int i=0;i<f.p.length;i++){
			for(int j=0;j<f.p.length;j++){

				System.out.printf("%3d ",f.p[i][j]);
			}
			System.out.println("");
		}*/

		//对每个路口的路径寻找最优解最打印
		for(int i=0;i<DynamicData.crosses.size();i++){
			for(int j=0;j<DynamicData.crosses.size();j++){
				if(i==j)
					continue;
				
				System.out.printf("%4d -->%4d | length:%4d | path:",DynamicData.crosses.get(i),DynamicData.crosses.get(j),f.d[i+1][j+1]);
				//System.out.print(DynamicData.crosses.get(i)+"-->"+DynamicData.crosses.get(j)+"  length:"+f.d[i+1][j+1]+"  path:");
				path=f.findRoads(map,DynamicData.crosses.get(i),DynamicData.crosses.get(j),DynamicData.crosses);
				for(Road road:path){
					if(path.indexOf(road)==(path.size()-1)){
						System.out.print(road.id);
						continue;
					}
					System.out.print(road.id+"--");
				}
				System.out.println("");
			}
			System.out.println("");
		}

		//Test Answer
		/*Answer answer=new Answer();
		answer.write();*/


	}
}