package org.springframework.samples.petclinic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

@Configuration
@Profile("postgresql")
@ConfigurationProperties("extdb")

public class ExtdbProperties {

   @NotNull
   @NotEmpty
   private String dbHost;

   @NotNull
   @NotEmpty
   private String dbPort;

   @NotNull
   @NotEmpty
   private String dbName;

   @NotNull
   @NotEmpty
   private String dbUser;

   @NotNull
   @NotEmpty
   private String dbPassword;


   private String getDbHost() {
     return this.dbHost;
   }

   private String getDbName() {
     return this.dbName;
   }

   private String getDbPort() {
     return this.dbPort;
   }

   private String getDbUser() {
     return this.dbUser;
   }

   private String getDbPassword() {
     return this.dbPassword;
   }

   public void setDbHost(String dbHost) {
     this.dbHost = dbHost;
   }

   public void setDbPort(String dbPort) {
     this.dbPort = dbPort;
   }

   public void setDbName(String dbName) {
     this.dbName = dbName;
   }

   public void setDbUser(String dbUser) {
     this.dbUser = dbUser;
   }

   public void setDbPassword(String dbPassword) {
     this.dbUser = dbPassword;
   }

   @Override
   public String toString() {

       return "dbHost: "+ this.dbHost+"\n"
               + "dbPort: "+this.dbPort+"\n"
               + "dbName: "+this.dbName+"\n"
               + "dbUser: "+this.dbUser+"\n"
               + "dbPassword: ***********"+"\n";
   }


}
