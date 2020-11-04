package codecraft.algorithm;

import codecraft.CrossMap;
import codecraft.DynamicData;
import codecraft.origin.Road;

import java.util.*;

public class Dijkstra{
	private final int n;
	public static int[][] d;
	
	public static int[] dis;
	public static int[] fatherNode;
	public static boolean[] isChecked;
	private static final int inf=Integer.MAX_VALUE;

	public Dijkstra(){
		n= DynamicData.crosses.size();
		dis=new int[n];
		fatherNode=new int[n];
		isChecked=new boolean[n];
	}

	public void create(ArrayList<Road> roads, ArrayList<Integer>crosses){
		d=new int[n+1][n+1];
		int value;
		for(int i=1;i<n+1;i++){
			d[0][i]=crosses.get(i-1);
			d[i][0]=crosses.get(i-1);
		}

		for(int i=1;i<=n;i++){
			for(int j=1;j<=n;j++){
				for(Road road:roads){
					if(crosses.get(i-1)==road.from && crosses.get(j-1)==road.to){
						value=road.length;
						d[i][j]=value;
						d[j][i]=value;
					}
				}
			}
		}
		for(int i=1;i<=n;i++){
			for(int j=1;j<=crosses.size();j++){
				if(d[i][j]==0){
					d[i][j]=inf;
				}
				if(i==j){
					d[i][j]=0;
				}
			}
		}

	}
	
	//生成 某个cross 到其他cross的路径及距离
	public void findOneCross(int a, ArrayList<Integer> crosses){
		fatherNode=new int[n];
		isChecked=new boolean[n];

		for(int i=0;i<n;i++){
			dis[i]=d[crosses.indexOf(a)+1][i+1];
			//fatherNode[i]=-1;
		}

		for(int i=0;i<n;i++){
			if(dis[i]!=inf){
				fatherNode[i]=crosses.indexOf(a);
			}
		}
		isChecked[crosses.indexOf(a)]=true;
		//循环n次，确定找到中转点后其他点都遍历过该点
		for(int i=0;i<n;i++){
			int min=inf;
			int k=crosses.indexOf(a);

			//找dis中的最小值，即查找过的最短路径length
			for(int j=0;j<n;j++){
				if(isChecked[j]!=true && dis[j]<min){
					min=dis[j];
					k=j;
				}
			}
			isChecked[k]=true;
			
			for(int j=0;j<n;j++){
				if(isChecked[j]!=true && d[k+1][j+1]!=inf){
					if(dis[j]>min+d[k+1][j+1]){
						dis[j]=min+d[k+1][j+1];
						fatherNode[j]=k;
					}
				}
			}

		}
	}

	//a 是cross的id
	public void findRoads(CrossMap map, int a, ArrayList<Integer> crosses){
		
		for(int i=0;i<n;i++){
			System.out.printf("%4d -->%4d | length:%4d | path:",a,crosses.get(i),dis[i]);
			if(dis[i]==inf || dis[i]==0){
				System.out.println("");
				continue;
			}
			
			ArrayList<Road> path=new ArrayList<>();
			Road road;
			int k=i;
			//System.out.println(fatherNode[k]);
			while(fatherNode[k]!=crosses.indexOf(a)){
				//System.out.println(crosses.get(fatherNode[k])+","+crosses.get(k));
				if(map.find(crosses.get(fatherNode[k]),crosses.get(k))==null){
					road=map.find(crosses.get(k),crosses.get(fatherNode[k]));//road正向返回null，则反向寻找
				}else {
					road=map.find(crosses.get(fatherNode[k]),crosses.get(k));
				}
				path.add(road);
				k=fatherNode[k];
			}
			//加上起点到第1个cross的路径
			if(map.find(a,crosses.get(k))==null){
				road=map.find(crosses.get(k),a);//road正向返回null，则反向寻找
			}else {
				road=map.find(a,crosses.get(k));
			}
			path.add(road);

			for(int j=path.size()-1;j>=0;j--){
				if(j==0){
					System.out.print(path.get(j).id);
					continue;
				}
				System.out.print(path.get(j).id+"--");
			}
			System.out.println("");
		}

	}

}