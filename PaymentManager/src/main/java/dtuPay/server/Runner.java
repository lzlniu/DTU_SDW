package dtuPay.server;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Runner {
    public static void main(String[] args){
        new PaymentManagerFactory().getManager();
        Quarkus.run(args);
    }
}
