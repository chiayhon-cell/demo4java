package cn.chiayhon.designpattern.observer.demo1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionDisplay implements Observer, DisplayElement {
    private double temperature;
    private double humidity;
    private double pressure;
    private String name;

    /*当观察者初始化时，需要指定数据源和观察者的名字*/
    public ConditionDisplay(Subject weatherData, String name) {
        this.name = name;
        weatherData.registerObserver(this);        //向数据源注册，代表需要从数据源获取数据
    }

    /*把数据打印给用户*/
    public void display() {
        log.info("观察者" + name + "的数据:");
        log.info("temperature:" + temperature);
        log.info("humidity:" + humidity);
        log.info("pressure:" + pressure);
    }

    /*当从数据源(WeatherData)获得数据后，用新数据更新自身数据*/
    public void update(double temp, double humidity, double pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        display();                    //信息更新完后，自动打印
    }

}