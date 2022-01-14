package dtuPay.server;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Runner {
    public static void main(String[] args){
        new AccountManagerFactory().getManager();
        Quarkus.run(args);
    }
}
