/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s300006085
 */
public class Item {
      String ID, type, title, dateOrdered, expiryDate, dateAcq,prec,usage,dept, quantity;//values from main document/data collection
      boolean isExpiring=false,lowStock=false;
      
      public Item(String _ID,String _type,String _title, String _dateOrdered,String _expiryDate,String _dateAcq,String _prec,String _usage,String _dept,String _quantity){
          ID=_ID;//converting values to values in object class 
          type=_type;
          title=_title;
          dateOrdered=_dateOrdered;
          expiryDate=_expiryDate;
          dateAcq=_dateAcq;
          prec=_prec;
          usage=_usage;
          dept=_dept;
          quantity=_quantity;
}}
