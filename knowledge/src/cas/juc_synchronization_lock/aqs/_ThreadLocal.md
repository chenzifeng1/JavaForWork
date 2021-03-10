# ThreadLocal

设置值：
map = getMap -> getMap : Thread.currentThread.map  # 这个map是线程Thread的属性,所以我们获取的是当前线程
的Map对象  
map.set(ThreadLocal->this,T->value)
也就说我们使用ThreadLocal的时候，是每个线程都有自己的一个Map对象来存有ThreadLocal对象的属性


用途： 声明式事务，保证多个方法拿同一个Connection对象