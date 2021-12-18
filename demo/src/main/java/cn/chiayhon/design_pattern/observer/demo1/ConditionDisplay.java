package cn.chiayhon.design_pattern.observer.demo1;

public class ConditionDisplay implements Observer,DisplayElement{
	private double temperature;
	private double humidity;
	private double pressure;
	private String name;
	private Subject weatherData;	//	观察者也要维护一个中介(WeatherData)作为数据源
	
	/*当观察者初始化时，需要指定数据源和观察者的名字*/
	public ConditionDisplay(Subject weatherData,String name) {
		this.name = name;
		this.weatherData = weatherData;
		weatherData.registerObserver(this);		//向数据源注册，代表需要从数据源获取数据
	}
	
	/*把数据打印给用户*/
	public void display() {
		System.out.println("观察者"+name+"的数据:");
		System.out.println("temperature:"+temperature);
		System.out.println("humidity:"+humidity);
		System.out.println("pressure:"+pressure);
		System.out.println();
	}
 
	/*当从数据源(WeatherData)获得数据后，用新数据更新自身数据*/
	public void update(double temp, double humidity, double pressure) {
		this.temperature = temp;
		this.humidity = humidity;
		this.pressure = pressure;
		display();					//信息更新完后，自动打印
	}
	
}