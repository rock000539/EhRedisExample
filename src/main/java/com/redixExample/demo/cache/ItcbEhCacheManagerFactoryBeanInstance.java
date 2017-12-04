package com.redixExample.demo.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import generated.Cache;
import generated.Ehcache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;

public class ItcbEhCacheManagerFactoryBeanInstance implements FactoryBean<CacheManager>, InitializingBean, DisposableBean {
	protected final Log logger = LogFactory.getLog(getClass());

	private String configLocationPath;

	private String cacheManagerName;

	private boolean shared = false;

	private CacheManager cacheManager;

	private boolean locallyManaged = true;
	
	public void setConfigLocation(String configLocationPath) {
		this.configLocationPath = configLocationPath;
	}
	
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}
	
	public net.sf.ehcache.CacheManager manualCacheManager()  {
		net.sf.ehcache.CacheManager returnCache=null;
		try {
		Jaxb2Marshaller ehcacheMarshaller = new Jaxb2Marshaller();
		ehcacheMarshaller.setClassesToBeBound(generated.BootstrapCacheLoaderFactory.class, generated.Cache.class,
				generated.CacheDecoratorFactory.class, generated.CacheEventListenerFactory.class,
				generated.CacheExceptionHandlerFactory.class, generated.CacheExtensionFactory.class,
				generated.CacheLoaderFactory.class, generated.CacheManagerEventListenerFactory.class,
				generated.CacheManagerPeerListenerFactory.class, generated.CacheManagerPeerProviderFactory.class,
				generated.CacheWriter.class,generated.CacheWriterFactory.class,generated.ConsistencyType.class, generated.CopyStrategy.class,
				generated.DefaultCache.class, generated.DiskStore.class, generated.Ehcache.class,
				generated.ElementValueComparator.class, generated.MonitoringType.class, generated.Nonstop.class,
				generated.NotificationScope.class, generated.Searchable.class, generated.SearchAttribute.class,
				generated.StorageStrategyType.class, generated.Terracotta.class,
				generated.TerracottaCacheValueType.class, generated.TerracottaConfig.class,
				generated.TimeoutBehavior.class, generated.TimeoutBehaviorType.class, generated.TransactionalMode.class,
				generated.TransactionManagerLookup.class, generated.WriteModeType.class);
		PathMatchingResourcePatternResolver c=new PathMatchingResourcePatternResolver();
		String path =configLocationPath;
		Resource[] result = c.getResources(path);
		Ehcache ehcache;
		Ehcache ehcacheToReturn=null;
		for(Resource ehcacheXml : result) {
			if(ehcacheToReturn==null) {
				ehcacheToReturn = (Ehcache)ehcacheMarshaller.unmarshal(new StreamSource(ehcacheXml.getInputStream()));
			}
			else {
				ehcache= (Ehcache)ehcacheMarshaller.unmarshal(new StreamSource(ehcacheXml.getInputStream()));
				ehcacheToReturn.getCache().addAll(ehcache.getCache());
			}
		}
		for(Cache cache:ehcacheToReturn.getCache()) {
			System.out.println("cache name=="+cache.getName());
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult stream = new StreamResult(bos);
		ehcacheMarshaller.marshal(ehcacheToReturn, stream);
		InputStream io = new ByteArrayInputStream(bos.toByteArray());
		returnCache=net.sf.ehcache.CacheManager.newInstance(io);
		}catch(Exception e){
			e.printStackTrace();
			}
		return returnCache;
	}
	
	@Override
	public CacheManager getObject() {
		cacheManager =manualCacheManager();
		return cacheManager;
	}

	@Override
	public void destroy() throws Exception {
		if (this.locallyManaged) {
			if (logger.isInfoEnabled()) {
				logger.info("Shutting down EhCache CacheManager" +
						(this.cacheManagerName != null ? " '" + this.cacheManagerName + "'" : ""));
			}
			this.cacheManager.shutdown();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public Class<?> getObjectType() {
		return (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
