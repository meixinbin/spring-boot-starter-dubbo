package io.dubbo.springboot;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;

import com.alibaba.dubbo.config.spring.AnnotationBean;

/**
 * @author meixinbin
 * @since 0.0.0
 */
public class DubboHolderListener implements ApplicationListener<ApplicationEvent> , Ordered{
	private static Thread holdThread;
	private static Boolean running = Boolean.FALSE;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationPreparedEvent) {
			if (running == Boolean.FALSE)
				running = Boolean.TRUE;
			if (holdThread == null) {
				holdThread = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println(Thread.currentThread().getName());
						while (running && !Thread.currentThread().isInterrupted()) {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							}
						}
					}
				}, "Dubbo-Holder");
				holdThread.setDaemon(false);
				holdThread.start();
			}
		}
		if (event instanceof ContextClosedEvent) {
			running = Boolean.FALSE;
			if (null != holdThread) {
				holdThread.interrupt();
				holdThread = null;
			}
		}
		if(event instanceof ApplicationPreparedEvent){
			((ApplicationPreparedEvent) event).getApplicationContext().addBeanFactoryPostProcessor(new AnnotationBean());
		}
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
}
