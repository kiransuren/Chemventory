
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
public class TestinShet {
    static public String isExpired(String expiryDate) throws ParseException{
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
     public static void main(String[] args) throws ParseException {
     String date= "3/04/2020";
     String Yee=isExpired(date);
     System.out.println(Yee);
}}

       
