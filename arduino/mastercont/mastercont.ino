#include <nRF905.h>
#include <SPI.h>
#include <AM.h>

#define BUF_LEN          32

AM am;

String serialDataStream;

void setup()
{

  
  // Start up
  nRF905_init();
  
  
  // Set address of this device
  byte addr[] = MASTERADDR;
  nRF905_setRXAddress(addr);

  // Put into receive mode
  nRF905_receive();

  Serial.begin(9600);

  debug("Mastercont started");

}

void loop()
{
  if(Serial.available())
  {
    // We've got some serial data, need to handle that
    readSearialCommand();

  }

  checkWireless();
  delay(100);
}


void checkWireless()
{

  // Make buffer for data
  byte buffer[BUF_LEN];
  // Wait for data
  if(nRF905_getData(buffer, sizeof(buffer))){
    processCommand(buffer);
  }

}

void readSearialCommand()
{

  while (Serial.available() > 0)
  {

    char recieved = Serial.read();

    // Process message when term character is recieved
    if (recieved == ';')
    {
      serialDataStream.trim();

      debug("Arduino Received->"+serialDataStream);

      String msgParts[10];
      am.split(':',serialDataStream, msgParts);

      String targetDevice = msgParts[0];
      String sourceDevice = msgParts[1];
      String msgType = msgParts[2];
      String txId = msgParts[3];
      String cmd = msgParts[4];

      if(targetDevice == "0"){
        debug("Message is for me");
      } 
      else {
        sendWirelessMsg(serialDataStream);
      }

      serialDataStream = ""; // Clear recieved buffer
    }
    else {
      serialDataStream += recieved; 
    }

  } 

}

void processCommand(unsigned char *str)
{

  String commandStringIn;

  while(*str){
    commandStringIn += (char)*str++;
  }
  
  commandStringIn.trim();
  
  if(commandStringIn.startsWith("0:")){
    Serial.print(commandStringIn+";");
  } 
  else {
    debug("Cmd is for NOT me:"+commandStringIn);
  }

}

void sendWirelessMsg(String msg){
  
  char tx_buf[BUF_LEN]= {
    0    };
  msg.toCharArray(tx_buf,BUF_LEN);

  String firstByte[1];
  am.split(':',msg, firstByte,1);
  
  byte b = firstByte[0].toInt();
  
  byte addr[] = {b,b,b,b};
  
  nRF905_setTXAddress(addr);

  // Set payload data (reply with data received)
  nRF905_setData(tx_buf, sizeof(tx_buf));

  // Send payload (send fails if other transmissions are going on, keep trying until success)
  while(!nRF905_send());

  // Put back into receive mode
  nRF905_receive();

}

void debug(String s){
  Serial.print("DEBUG:"+s+";");
}







