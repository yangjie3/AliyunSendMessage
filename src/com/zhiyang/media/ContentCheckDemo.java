package com.zhiyang.media;

public class ContentCheckDemo {
	public static void main(String[] args) {
		ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
		pool.submit(new Test());
	}
	static class Test implements Runnable{

		@Override
		public void run() {
			ContentCheckConsumer.comsume();
		}
		
}
}
