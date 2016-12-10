package config;
/**
 * Created by thang on 20.11.2016.
 */
import com.netflix.config.ConfigurationManager;

import dw.api.implementation.GameRest;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.apache.commons.configuration.AbstractConfiguration;

/*
    Dropwizard uses:
    - Jetty: for servlet implementation
    - Jersey: for JAX-RS (in contrast to RestEasy in Wildfly)
    - Jackson: for Json un/marshaling (same as Wildfly)
 */
public class GameApplication extends Application<GameConfiguration> {


    public static void main(String[] args) throws Exception {
        new GameApplication().run(args);
    }

    @Override
    public String getName() {
        return "GameAPI written in DropWizard";
    }

    @Override
    public void initialize(Bootstrap<GameConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", null, "a"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "b"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "c"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images", null, "d"));
        bootstrap.addBundle(new AssetsBundle("/assets/lang", "/lang", null, "e"));
        bootstrap.addBundle(new AssetsBundle("/assets/lib", "/lib", null, "f"));

    }


    @Override
    public void run(GameConfiguration configuration, Environment environment) {

        environment.jersey().setUrlPattern("/mygames/api");
        environment.jersey().register(new GameRest());

        //swagger
        environment.jersey().register(new ApiListingResource());

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:9090");
        beanConfig.setBasePath("/mygames/api");
        beanConfig.setResourcePackage("dw.api");
        beanConfig.setScan(true);

        //add further configuration to activate SWAGGER
        environment.jersey().register(new io.swagger.jaxrs.listing.ApiListingResource());
        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());

        //Hystrix configuration
        AbstractConfiguration conf = ConfigurationManager.getConfigInstance();
        // how long to wait before giving up a request?
        conf.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 20000); //default is 1000
        // how many failures before activating the CB?
        conf.setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 2); //default 20
        conf.setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50);
        //for how long should the CB stop requests? after this, 1 single request will try to check if remote server is ok
        conf.setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000);

    }
}