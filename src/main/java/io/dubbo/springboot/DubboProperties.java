package io.dubbo.springboot;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;

@ConfigurationProperties(prefix = "spring.dubbo")
public class DubboProperties {

    private String            scan;

    private ApplicationConfig application;

    private List<RegistryConfig>    registry;

    private List<ProtocolConfig>    protocol;

    private ConsumerConfig 	  consumer;
    
    private MonitorConfig monitor;
    
    public String getScan() {
        return scan;
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

	public ConsumerConfig getConsumer() {
		return consumer;
	}

	public void setConsumer(ConsumerConfig consumer) {
		this.consumer = consumer;
	}

	public List<ProtocolConfig> getProtocol() {
		return protocol;
	}

	public void setProtocol(List<ProtocolConfig> protocol) {
		this.protocol = protocol;
	}

	public List<RegistryConfig> getRegistry() {
		return registry;
	}

	public void setRegistry(List<RegistryConfig> registry) {
		this.registry = registry;
	}

	public MonitorConfig getMonitor() {
		return monitor;
	}

	public void setMonitor(MonitorConfig monitor) {
		this.monitor = monitor;
	}

}
