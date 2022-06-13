package cn.chiayhon.jdk.concurrent.twotermination;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminationToken {
  protected volatile boolean toShutdown = false;  // 终止状态的标志位
  public final AtomicInteger reservations = new AtomicInteger(0);  // 记录当前剩余任务数

  // 记录了所有注册了TerminationToken的实例，这里使用Queue是因为可能会有多个
  // Terminatable实例共享同一个TeraminationToken，如果是共享的，那么reservations
  // 实例就保存了所有共享当前TerminationToken实例的线程所需要执行的任务总数
  private final Queue<WeakReference<Terminatable>> coordinatedThreads;

  public TerminationToken() {
    coordinatedThreads = new ConcurrentLinkedQueue<>();
  }

  public boolean isToShutdown() {
    return toShutdown;
  }

  public void setToShutdown(boolean toShutdown) {
    this.toShutdown = toShutdown;
  }

  // 将当前Terminatable实例注册到当前TerminationToken中
  protected void register(Terminatable thread) {
    coordinatedThreads.add(new WeakReference<>(thread));
  }

  // 如果是多个Terminatable实例注册到当前TerminationToken中，
  // 则广播当前的终止状态，使得这些实例都会终止
  protected void notifyThreadTermination(Terminatable thread) {
    WeakReference<Terminatable> wrThread;
    Terminatable otherThread;
    while (null != (wrThread = coordinatedThreads.poll())) {
      otherThread = wrThread.get();
      if (null != otherThread && otherThread != thread) {
        otherThread.terminate();
      }
    }
  }
}
