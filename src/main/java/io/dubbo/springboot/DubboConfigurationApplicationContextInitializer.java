package io.dubbo.springboot;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;

public class DubboConfigurationApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>,Ordered{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        String scan = env.getProperty("spring.dubbo.scan");
        if (scan != null) {
            AnnotationBean scanner = BeanUtils.instantiate(AnnotationBean.class);
            scanner.setPackage(scan);
            scanner.setApplicationContext(applicationContext);
            applicationContext.addBeanFactoryPostProcessor(scanner);
            applicationContext.getBeanFactory().addBeanPostProcessor(scanner);
            applicationContext.getBeanFactory().registerSingleton("annotationBean", scanner);
            for(int i=0;i<10;i++){
            	String protocol_name = env.getProperty("spring.dubbo.protocol["+i+"].name");
            	String protocol_serialization = env.getProperty("spring.dubbo.protocol["+i+"].serialization");
            	String protocol_port = env.getProperty("spring.dubbo.protocol["+i+"].port");
            	String protocol_host = env.getProperty("spring.dubbo.protocol["+i+"].host");
            	String protocol_contextpath = env.getProperty("spring.dubbo.protocol["+i+"].contextpath");
            	if(StringUtils.isNotBlank(protocol_name) && StringUtils.isNotBlank(protocol_port)){
            		ProtocolConfig pc = new ProtocolConfig(protocol_name, Integer.parseInt(protocol_port));
                	pc.setHost(protocol_host);
                	pc.setSerialization(protocol_serialization);
                	pc.setContextpath(protocol_contextpath);
                	if("rest".equals(protocol_name)){
                		String protocol_server = env.getProperty("spring.dubbo.protocol["+i+"].server");
                		pc.setServer(protocol_server);
                	}
                	applicationContext.getBeanFactory().registerSingleton(protocol_name, pc);
            	}else{
            		break;
            	}
            	
            }
        }

    }

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
}
