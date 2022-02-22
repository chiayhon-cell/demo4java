package cn.chiayhon.designpattern.observer.demo1;

public interface Observer {
	public void update(double temp, double humidity, double pressure);//观察者当接收到来自数据源的信息时，调用此方法更新自身数据
}
