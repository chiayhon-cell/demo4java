package cn.chiayhon.design_pattern.observer.demo1;

public interface Subject {
	public void registerObserver(Observer o);	//数据源提供用于 （ 观察者想要获取数据源时调用此方法注册）
	public void removeObserver(Observer o);		//数据源提供用于  （观察者不再需要数据源数据时，退出注册）
	public void notifyObservers();				//数据源向注册在案的所有观察者发送数据
}