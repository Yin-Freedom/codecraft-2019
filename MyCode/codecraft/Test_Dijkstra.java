package codecraft;

import codecraft.algorithm.Dijkstra;

public class Test_Dijkstra {
	public static void main(String[] args) {
		ImportData im=new ImportData(DynamicData.carPath,
				DynamicData.roadPath,
				DynamicData.crossPath,
				DynamicData.presetAnswerPath,
				DynamicData.ansPath);
		Dijkstra d=new Dijkstra();
		d.create(DynamicData.roads,DynamicData.crosses);

		//Floyd D图
		for(int i=0;i<d.d.length;i++){
			for(int j=0;j<d.d.length;j++){
				if(d.d[i][j]==Integer.MAX_VALUE) {
					System.out.printf("--- ",d.d[i][j]);
					continue;
				}
				System.out.printf("%3d ",d.d[i][j]);
			}
			System.out.println("");
		}
		System.out.println();

		CrossMap map=new CrossMap(DynamicData.roads);

		/*d.findOneCross(103,DynamicData.crosses);
		d.findRoads(map,103,DynamicData.crosses);*/
		//对每个路口的路径寻找最优解最打印
		for(int i=0;i<DynamicData.crosses.size();i++){
			d.findOneCross(DynamicData.crosses.get(i),DynamicData.crosses);
			d.findRoads(map,DynamicData.crosses.get(i),DynamicData.crosses);
			System.out.println("");
		}
	}
}