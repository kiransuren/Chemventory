
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s300006085
 */
public class TestinShet

    static public String isExpired(String expiryDate) throws ParseException{//Checks exp date of item, returns info
    String answer =" ";
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
    Date expiry = formatter.parse(expiryDate);
    Date currentDate = new Date();
        
    double diff = expiry.getTime()- currentDate.getTime();
    diff =diff/86400000;

    if (expiry.compareTo(currentDate)<0){
        answer= "Product Expired";
    }else if(diff<=7){
        answer= "Expiring in "+diff+" days";
    }
    return answer;
}
    static public String lowQuant(String quantity) {//if quantity < 3, notify
    String r=" ";
    try{
    int q= Integer.parseInt(quantity);
    if (q<=3) {
        r="Low stock, only "+q+"left;
    }}catch Exception e {

        }
    return r;
}

    public static void main(String[] args) throws ParseException {//test
     String date= "3/04/2020";
     String Yee=isExpired(date);
     System.out.println(Yee);
     String q=3;
     String e=lowQuant(q);
     System.out.println(e);
}}

       
