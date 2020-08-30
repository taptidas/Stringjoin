package JdbcConn;

import java.io.BufferedReader;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;

import java.util.List;

import java.util.Map;

import java.util.TreeMap;

import java.util.TreeSet;
 

/* Train Detail Value Object - DO NOT CHANGE*/

class TrainDetailsVO {

              private String trainNumber;

              private String route;

              private int source;

              private int destination;

             

              
              
              String s=null;

             

              public String getTrainNumber() {

                             return trainNumber;

              }

 

              public void setTrainNumber(final String trainNumber) {

                             this.trainNumber = trainNumber;

              }

 

              public String getRoute() {

                             return route;

              }

 

              public void setRoute(final String route) {

                             this.route = route;

              }

 

              public int getSource() {

                             return source;

              }

 

              public void setSource(final int source) {

                             this.source = source;

              }

 

              public int getDestination() {

                             return destination;

              }

 

              public void setDestination(final int destination) {

                             this.destination = destination;

              }

 

         /**     public char getSpecial() {

                             return special;

              }

 

              public void setSpecial(final char status) {

                             this.special = special;
                             
                           
              }**/

 

           
             
              @Override
            public String toString() {
            	 
            return "Train no.:"+this.trainNumber+" Route:"+this.route+" Source:"+this.source+" Destination:" +this.destination;
            }

                 

             

 

       /**       @Override

              public boolean equals(Object obj) {

                             if (this == obj) {

                                           return true;

                             }

                             if (obj == null) {

                                           return false;

                             }

                             if (getClass() != obj.getClass()) {

                                           return false;

                             }

                             TrainDetailsVO other = (TrainDetailsVO) obj;

                            

                             if (trainNumber == null) {

                                           if (other.trainNumber != null) {

                                                          return false;

                                           }

                             } else if (!trainNumber.equals(other.trainNumber)) {

                                           return false;

                             }

                             if (route == null) {

                                           if (other.route != null) {

                                                          return false;

                                           }

                             } else if (!route.equals(other.route)) {

                                           return false;

                             }

                             if (special == ' ') {

                                           if (other.special != ' ') {

                                                          return false;

                                           }

                             } else if (special != other.special) {

                                           return false;

                             }

                             if (destination != other.destination) {

                                           return false;

                             }

                             if (source != other.source) {

                                           return false;

                             }

             

                             return true;

              }**/

 

}

 

/* User defined Exception - DO NOT CHANGE */

class TrainServiceException extends Exception {

              /**

              *

               */

              private static final long serialVersionUID = 1L;

 

              public TrainServiceException(String message) {

                             super(message);

              }

 

              public TrainServiceException(Throwable throwable) {

                             super(throwable);

              }

}
