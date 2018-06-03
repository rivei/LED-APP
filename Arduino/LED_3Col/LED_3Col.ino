#include <SoftwareSerial.h> //for bluetooth serial communication

int ledr = 9;
int ledg = 10;
int ledb = 11;

//for bluetooth, we need to use the object of class software serial
SoftwareSerial mySerial(12, 13);//Rx Tx ; the Rx Tx pin cannot be used for the bluetooth, don't know why
char command;
String string;
int rr = 50;
int gg = 50;
int bb = 50;
boolean ledon = false;
String tmp;


void setup()
{
  Serial.begin(9600); //to monitor the the bluetooth received signal
  mySerial.begin(9600);
  pinMode(ledr,OUTPUT);
  pinMode(ledg,OUTPUT);
  pinMode(ledb,OUTPUT);
  setColor(0,0,0);
 rr = 50;
 gg = 50;
 bb = 50;
}

void loop()
{
  
  if (mySerial.available() > 0) 
  {
    string = "";
  }
  
  //strOld = string;
  while(mySerial.available() > 0)
  {
    command = ((byte)mySerial.read());
    if(command == ':')
    {
      break;
    }
    
    else
    {
      string += command;
    }
    
    delay(1);
  }

  //if(strOld != string)
  {  
    if(string == "TO")
    {
        ledOn();
        ledon = true;
    }
    
    else if(string =="TF")
    {
        ledOff();
        ledon = false;
    }
  
    //if((string.substring(0,3).toInt()>= 0) && (string.substring(0,3).toInt() <= 255))
    else if (string.length() > 2 && string.toFloat() >= 0)
    {
        rr = string.substring(0,3).toInt();
        gg = string.substring(3,6).toInt();
        bb = string.substring(6,9).toInt();        
      if (ledon==true)
      {
        setColor(rr, gg, bb);
        delay(10);
      }
  
      //Serial.println(String(red));
    }
    tmp = String(rr, HEX)+String(gg, HEX)+String(bb, HEX);
    Serial.println(string);
  }
}
 
void ledOn()
{
      //analogWrite(led, 255);
      setColor(rr,gg,bb);
      delay(10);
}
 
 void ledOff()
{
      setColor(0,0,0);
      //analogWrite(led, 0);
      delay(10);
}
 
void setColor(int red,int green,int blue)
{
  analogWrite(ledr,255 - red);
  analogWrite(ledg,255 - green);
  analogWrite(ledb,255 - blue);
}

    
