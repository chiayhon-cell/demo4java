package cn.chiayhon.concurrent.twotermination;

public abstract class TerminatableSupport extends Thread implements Terminatable {
  public final TerminationToken terminationToken;  // 记录当前的标志位

  public TerminatableSupport() {
    this(new TerminationToken());  // 初始化当前标志位
  }

  public TerminatableSupport(TerminationToken terminationToken) {
    super();
    this.terminationToken = terminationToken;  // 初始化标志位
    terminationToken.register(this);  // 注册当前对象的标志位
  }

  protected abstract void doRun() throws Exception;  // 供子类实现具体任务的方法

  // 钩子方法，用于子类进行一些清理工作
  protected void doCleanup(Exception cause) {}

  // 钩子方法，用于子类进行终止时的一些定制化操作
  protected void doTerminate() {}

  @Override
  public void run() {
    Exception ex = null;
    try {
      // 在当前线程中执行任务时，会判断是否标识为终止，并且剩余任务数小于等于0，是才会真正终止当前线程
      while (!terminationToken.isToShutdown() || terminationToken.reservations.get() > 0) {
        doRun();
      }
    } catch (Exception e) {
      ex = e;
    } finally {
      try {
        doCleanup(ex);  // 当前线程终止后需要执行的操作
      } finally {
        terminationToken.notifyThreadTermination(this);
      }
    }
  }

  @Override
  public void interrupt() {
    terminate();
  }

  @Override
  public void terminate() {
    terminationToken.setToShutdown(true);  // 设置终止状态
    try {
      doTerminate();  // 执行客户端定制的终止操作
    } finally {
      if (terminationToken.reservations.get() <= 0) {
        super.interrupt();  // 如果当前线程处于终止状态，则强制终止当前线程
      }
    }
  }

  // 提供给客户端调用的，即客户端线程必须等待终止完成之后才会继续往下执行
  public void terminate(boolean waitUntilThreadTerminated) {
    terminate();
    if (waitUntilThreadTerminated) {
      try {
        this.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
