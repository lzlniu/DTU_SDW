package dtuPay.Token;

import objects.DtuPayUser;

import java.util.*;

public class TokenManager {

    private Map<DtuPayUser,List<String>>activeTokens;
    private Map<DtuPayUser,List<String>>suedTokens;

    public TokenManager(){

    }

    public List<String> generateTokens(DtuPayUser user){
        return null;
    }

    public boolean depleteToken(String tokenID){
        return false;
    }

    public List<String> getUserTokens(DtuPayUser customer){
        return null;
    }

    public DtuPayUser getCustomerFromToken(){
        return null;
    }
}
