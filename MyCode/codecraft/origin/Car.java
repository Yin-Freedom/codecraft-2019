package codecraft.origin;

import java.lang.String;

//-encoding utf-8 Car.java
public class Car{
	//(车辆id，始发地、目的地、最高速度、计划出发时间)
	//(1001,1,16,6,1)(101,,,5,)

	public Integer id;
	public Integer startId;
	public Integer endId;
	public Integer speed;
	public Integer time;
	public Integer priority;
	public Integer preset;
	public Integer from;
	public Integer to;

	public Car(String id, String startId, String endId, String speed, String time, String priority, String preset) {
		this.id = Integer.valueOf(id);
		this.startId = Integer.valueOf(startId);
		this.endId = Integer.valueOf(endId);
		this.speed = Integer.valueOf(speed);
		this.time = Integer.valueOf(time);
		this.priority = Integer.valueOf(priority);
		this.preset = Integer.valueOf(preset);
		this.from=this.startId;
		this.to=this.endId;
	}
	public Car(String id, String startId, String endId, String speed, String time) {
		this.id = Integer.valueOf(id);
		this.startId = Integer.valueOf(startId);
		this.endId = Integer.valueOf(endId);
		this.speed = Integer.valueOf(speed);
		this.time = Integer.valueOf(time);
		this.from=this.startId;
		this.to=this.endId;
	}

	public Car(int id, int startId, int endId, int speed, int time, int priority, int preset) {
		this.id = id;
		this.startId = startId;
		this.endId = endId;
		this.speed = speed;
		this.time = time;
		this.priority = priority;
		this.preset = preset;
	}

	@Override
	public String toString() {
		return "Car{" +
				"id=" + id +
				", startId=" + startId +
				", endId=" + endId +
				", speed=" + speed +
				", time=" + time +
				", priority=" + priority +
				", preset=" + preset +
				'}';
	}
}