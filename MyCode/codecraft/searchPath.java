package codecraft;

import java.util.Arrays;
import java.util.Map;

import codecraft.origin.Cross;
import codecraft.origin.Road;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//-encoding utf-8 searchPath.java
public class searchPath{
	public static Log log=LogFactory.getLog(searchPath.class);

	public static Map<Integer, Cross> crossMap;
	public static Map<Integer, Road> roadMap;
	
	public static void main(String[] args) {
		ImportData im=new ImportData(DynamicData.carPath,
				DynamicData.roadPath,
				DynamicData.crossPath,
				DynamicData.presetAnswerPath,
				DynamicData.ansPath);
		crossMap=DynamicData.staticCrossMap;
		roadMap=DynamicData.staticRoadMap;

		AdjustCrossRoadDirection m=new AdjustCrossRoadDirection();
		DynamicData.staticCrossMap.get(DynamicData.crosses.get(0)).position=new int[]{0,0};
		m.recursion(DynamicData.staticCrossMap.get(DynamicData.crosses.get(0)));
		for(Integer key:crossMap.keySet()){
			//System.out.println(crossMap.get(key).id+":"+Arrays.deepToString(crossMap.get(key).orientation));//System.out.printf("%d,%d,%d,%d,\n",crossMap.get(key).r1,crossMap.get(key).r2,crossMap.get(key).r3,crossMap.get(key).r4);
		}

		CrossPosition cp=new CrossPosition();
		cp.recursion(DynamicData.staticCrossMap.get(DynamicData.crosses.get(0)));
		for(Integer key:crossMap.keySet()){
			System.out.println(crossMap.get(key).id+":"+Arrays.toString(crossMap.get(key).position));//System.out.printf("%d,%d,%d,%d,\n",crossMap.get(key).r1,crossMap.get(key).r2,crossMap.get(key).r3,crossMap.get(key).r4);
		}

		int xMin=Integer.MAX_VALUE,yMin=Integer.MAX_VALUE;
		//int xMax=Integer.MIN_VALUE,yMax=Integer.MIN_VALUE;
		for(Integer key:crossMap.keySet()){
			xMin=(DynamicData.staticCrossMap.get(key).position[0]<xMin?DynamicData.staticCrossMap.get(key).position[0]:xMin);
			yMin=(DynamicData.staticCrossMap.get(key).position[1]<yMin?DynamicData.staticCrossMap.get(key).position[1]:yMin);
		}

		for(Integer key:crossMap.keySet()){
			DynamicData.staticCrossMap.get(key).position[0]+=Math.abs(xMin);
			DynamicData.staticCrossMap.get(key).position[1]+=Math.abs(yMin);
		}
		System.out.println();

		int n=5;
		Integer[][] coordinate=new Integer[n][n];
		for(Integer key:crossMap.keySet()){
			coordinate[DynamicData.staticCrossMap.get(key).position[0]][DynamicData.staticCrossMap.get(key).position[1]]=DynamicData.staticCrossMap.get(key).id;
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				System.out.printf("%4d ",coordinate[i][j]);
			}
			System.out.println();
		}


		/*String path="D:/life/huawei_codecraft/codecraft/data/crossMap.txt";
		File file=new File(path);
		try{
			FileWriter fw=new FileWriter(file);
			BufferedWriter br=new BufferedWriter(fw);

			for(Integer key:crossMap.keySet()) {
				//log.info(String.valueOf(crossMap.get(key).id));
				fw.write(String.valueOf(crossMap.get(key).id));
				fw.write("(");
				if (crossMap.get(key).top != null) {
					fw.write("t:"+crossMap.get(key).top + ",");
				}else{fw.write(-1+ ",");}
				if (crossMap.get(key).right != null) {
					fw.write("r:"+crossMap.get(key).right + ",");
				}else{fw.write(-1+ ",");}
				if(crossMap.get(key).bottom != null){
					fw.write("b:"+crossMap.get(key).bottom + ",");
				}else{fw.write(-1+ ",");}
				if (crossMap.get(key).left != null){
					fw.write("l:"+crossMap.get(key).left);
				}else{fw.write(-1+ ",");}
				fw.write(")"+"\n");

			}
			br.close();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}*/
	}

	/*public static void mapping(){

		//给每个cross添加上下左右cross
		for(Integer i:crossMap.keySet()){
			if(((crossMap.get(i).r1)!=-1)){
				Road r=roadMap.get(crossMap.get(i).r1);
				if(i.equals(r.from)){
					crossMap.get(i).top=crossMap.get(r.to).id;
				}
				if(i.equals(r.endId)){
					crossMap.get(i).top=crossMap.get(r.from).id;
				}
			}
			if(((crossMap.get(i).r2)!=-1)){
				Road r=roadMap.get(crossMap.get(i).r2);
				if(i.equals(r.startId)){
					crossMap.get(i).right=crossMap.get(r.to).id;
				}
				if(i.equals(r.endId)){
					crossMap.get(i).right=crossMap.get(r.from).id;
				}
			}
			if((crossMap.get(i).r3!=-1)){
				Road r=roadMap.get(crossMap.get(i).r3);
				if(i.equals(r.startId)){
					crossMap.get(i).bottom=crossMap.get(r.to).id;
				}
				if(i.equals(r.endId)){
					crossMap.get(i).bottom=crossMap.get(r.from).id;
				}
			}
			if((crossMap.get(i).r4!=-1)){
				Road r=roadMap.get(crossMap.get(i).r4);
				if(i.equals(r.startId)){
					crossMap.get(i).left=crossMap.get(r.to).id;
				}
				if(i.equals(r.endId)){
					crossMap.get(i).left=crossMap.get(r.from).id;
				}
			}

		}

	}*/

}