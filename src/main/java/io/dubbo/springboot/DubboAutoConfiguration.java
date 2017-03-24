package io.dubbo.springboot;

import io.dubbo.springboot.endpoint.DubboEndpoint;
import io.dubbo.springboot.health.DubboHealthIndicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;

@Configuration
@EnableConfigurationProperties(DubboProperties.class)
public class DubboAutoConfiguration  implements ApplicationContextAware,InitializingBean{

	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private DubboProperties dubboProperties;
    private ConfigurableApplicationContext applicationContext;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		//
		if(dubboProperties.getProtocol()!=null && dubboProperties.getProtocol().size()>0){
			for(ProtocolConfig pc :this.dubboProperties.getProtocol()){
				if(!this.applicationContext.getBeanFactory().containsBean(pc.getName())){
					this.applicationContext.getBeanFactory().registerSingleton(pc.getName(), pc);
				}
	    	}
		}else{
			log.error("no protocol config");
			//TODO add a default protocol
		}
		
		if(dubboProperties.getRegistry()!=null && dubboProperties.getRegistry().size()>0){
			for(RegistryConfig rc :this.dubboProperties.getRegistry()){
				if(!this.applicationContext.getBeanFactory().containsBean(rc.getId())){
					this.applicationContext.getBeanFactory().registerSingleton(rc.getId(), rc);
				}
			}
		}else{
			log.error("no Registry config");
		}
		//监控配置
		if(dubboProperties.getMonitor()!=null){
			if(!this.applicationContext.getBeanFactory().containsBean(dubboProperties.getMonitor().getId())){
				this.applicationContext.getBeanFactory().registerSingleton(dubboProperties.getMonitor().getId(), dubboProperties.getMonitor());
			}
		}
		
	}
	
    @Bean
    public ApplicationConfig requestApplicationConfig() {
        return dubboProperties.getApplication();
    }

   /* @Bean
    public RegistryConfig requestRegistryConfig() {
        return dubboProperties.getRegistry();
    }*/

  /*  @Bean
    public ProtocolConfig requestProtocolConfig() {
        return dubboProperties.getProtocol();
    }*/
    
    @Bean
    @ConfigurationProperties(prefix = "endpoints.dubbo", ignoreUnknownFields = false)
    public DubboEndpoint dubboEndpoint() {
        return new DubboEndpoint();
    }
    
    @Bean
    public DubboHealthIndicator dubboHealthIndicator() {
        return new DubboHealthIndicator();
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

}
