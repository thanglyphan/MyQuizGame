package config;
/**
 * Created by thang on 20.11.2016.
 */
import dw.api.implementation.GameRest;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

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
        return "Counter written in DropWizard";
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
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/mygames/api");
        beanConfig.setResourcePackage("dw.api");
        beanConfig.setScan(true);

        //add further configuration to activate SWAGGER
        environment.jersey().register(new io.swagger.jaxrs.listing.ApiListingResource());
        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());
    }
}