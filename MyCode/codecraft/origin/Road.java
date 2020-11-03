package codecraft.origin;

//-encoding utf-8 Road.java
public class Road{
	//(道路id，道路长度，最高限速，车道数目，起始点id，终点id，是否双向)
	//(501, 10, 6, 5, 1, 2, 1)
	public Integer id;
	public Integer length;
	public Integer speed;
	public Integer laneNumber;//这个Integer运算会不会出错？
	public Integer startId;
	public Integer endId;
	public Integer twoWay;//只用在Floyd改邻接矩阵就行，searchPath就自动走定义方向

	public Integer from;
	public Integer to;

	public Road(String id, String length, String speed, String laneNumber, String startId, String endId, String twoWay) {
		this.id = Integer.valueOf(id);
		this.length = Integer.valueOf(length);
		this.speed = Integer.valueOf(speed);
		this.laneNumber = Integer.valueOf(laneNumber);
		this.startId = Integer.valueOf(startId);
		this.endId = Integer.valueOf(endId);
		this.twoWay = Integer.valueOf(twoWay);

		this.from=this.startId;
		this.to=this.endId;
	}

}