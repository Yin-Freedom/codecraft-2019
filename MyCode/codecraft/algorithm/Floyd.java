package codecraft.algorithm;

import codecraft.CrossMap;
import codecraft.DynamicData;
import codecraft.origin.Road;

import java.util.*;

public class Floyd{
	private final int n;
	public static int[][] d;
	public static int[][] p;
	public int value;
	private static final int inf=Integer.MAX_VALUE;
	public static ArrayList<Integer> crosses;
	public static ArrayList<Road> roads;

	public Floyd(){
		//ImportData im=new ImportData();
		n= DynamicData.crosses.size();
		crosses=DynamicData.crosses;
		roads=DynamicData.roads;

		d=new int[n+1][n+1];
		p=new int[n+1][n+1];
	}

	public void createMap(ArrayList<Road> roads,ArrayList<Integer>crosses){


		for(int i=0;i<crosses.size();i++){
			p[0][i+1]=crosses.get(i);
			p[i+1][0]=crosses.get(i);
		}

		for(int x=1;x<=n;x++){
			for(int y=1;y<=n;y++){
				for(Road road:roads){
					if(p[x][0]==road.from && p[0][y]==road.to){
						value=road.length/road.speed;
						d[x][y]=value;
						if(road.twoWay==1){
							d[y][x]=value;
						}
						p[x][y]=p[0][y];
					}
				}
			}
		}
		
		for(int x=1;x<=n;x++){
			for(int y=1;y<=n;y++){
				if(d[x][y]==0)
					d[x][y]=inf;
				if(p[x][y]==0)
					p[x][y]=p[0][y];
			}
		}
		for(int k=1;k<=n;k++){
			for(int i=1;i<=n;i++){
				for(int j=1;j<=n;j++){
					int temp=(d[i][k]==inf || d[k][j]==inf)?inf:
					(d[i][k]+d[k][j]);
					if(d[i][j]>temp){
						d[i][j]=temp;
						p[i][j]=p[i][k];
					}
				}
			}
		}
	}
	
	public ArrayList<Road> findRoads(CrossMap map, int a, int b, ArrayList<Integer> crosses){
		//起点a，终点b
		ArrayList<Road> path=new ArrayList<>();
		if(d[crosses.indexOf(a)+1][crosses.indexOf(b)+1]!=inf){
			int k=p[crosses.indexOf(a)+1][crosses.indexOf(b)+1];
				//k=经过某些转点后路径最短的转点
			if(map.find(a,k)==null){
				path.add(map.find(k,a));//road正向返回null，则反向寻找
			}else {
				path.add(map.find(a, k));
			}
				//加入a到该转点的road
			while(k!=b){
				int temp=k;
				k=p[crosses.indexOf(k)+1][crosses.indexOf(b)+1];
				if(map.find(temp,k)==null){
					path.add(map.find(k,temp));
				}else{
					path.add(map.find(temp,k));
				}
			}
		}
		return path;
	}
	public ArrayList<Integer> findPath(CrossMap map, int a, int b, ArrayList<Integer> crosses){
		//起点a，终点b
		ArrayList<Integer> path=new ArrayList<>();
		if(d[crosses.indexOf(a)+1][crosses.indexOf(b)+1]!=inf){
			int k=p[crosses.indexOf(a)+1][crosses.indexOf(b)+1];
			//k=经过某些转点后路径最短的转点
			if(map.find(a,k)==null){
				path.add(map.find(k,a).id);//road正向返回null，则反向寻找
			}else {
				path.add(map.find(a, k).id);
			}
			//加入a到该转点的road
			while(k!=b){
				int temp=k;
				k=p[crosses.indexOf(k)+1][crosses.indexOf(b)+1];
				if(map.find(temp,k)==null){
					path.add(map.find(k,temp).id);
				}else{
					path.add(map.find(temp,k).id);
				}
			}
		}
		return path;
	}

	public void upDateValue(){
		d=new int[n+1][n+1];
		p=new int[n+1][n+1];
		for(int x=1;x<=n;x++){
			for(int y=1;y<=n;y++){
				for(Road road:roads){
					if(p[x][0]==road.from && p[0][y]==road.to){
						value=road.length/road.speed;
						d[x][y]=value;
						if(road.twoWay==1){
							d[y][x]=value;
						}
						p[x][y]=p[0][y];
					}
				}
			}
		}
	}
	
	/*public static void main(String[] args){
		ArrayList<Road> path;
		CrossMap map=new CrossMap(DynamicData.roads);
		for(int i=0;i<crosses.size();i++){
			for(int j=0;j<crosses.size();j++){
				if(i==j)
					continue;
				//findRoads(CrossMap map, int a, int b, ArrayList<Integer> crosses)
				path=findRoads(map,DynamicData.crosses.indexOf(i),DynamicData.crosses.indexOf(j),crosses);
				System.out.print(crosses.get(i)+"-->"+crosses.get(j)+"  length:"+d[i][j]+"  path:");
				for(Road road:path){
					System.out.print(road.id+"--");
				}
				System.out.println("");
			}
		}
	}*/
}