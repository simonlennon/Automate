#include <nRF905.h>
#include <SPI.h>
#include <AM.h>

#define BUF_LEN          32

void setup()
{
  
  pinMode(4, OUTPUT);
  
  // Start up
  nRF905_init();

  // Set address of this device
  byte addr[] = PONDADDR;
  nRF905_setRXAddress(addr);
  digitalWrite(4, HIGH);

  // Put into receive mode
  nRF905_receive();

  Serial.begin(9600);
  
 
  Serial.println(F("Pondcont started;"));
  
  //Request the controller to reset our state.
  sendWirelessMsg("0:1:1:0:6;");
  
}

void loop()
{

  checkWireless();
  delay(100);

}

void checkWireless()
{

  // Make buffer for data
  byte buffer[BUF_LEN];
  // Wait for data
  while(!nRF905_getData(buffer, sizeof(buffer)));

  processCommand(buffer);

}


void processCommand(unsigned char *str)
{

  String commandStringIn;

  while(*str){
    commandStringIn += (char)*str++;
  }

  if(commandStringIn.startsWith("1:")){
    Serial.println("Cmd is for me...");
    processCommand(commandStringIn);
  } 

}

void processCommand(String commandStringIn){

  Serial.println("processCommand()...");
  Serial.println(commandStringIn);

  String msgParts[10];
  split(':',commandStringIn, msgParts);

  String sourceDevice = msgParts[1];
  String msgType = msgParts[2];
  String txId = msgParts[3];
  String cmd = msgParts[4];

  if(cmd == "0"){

    Serial.println("Got ping command, responding");
    String pingResponse = sourceDevice+":1:1:"+txId+":0";
    Serial.println(pingResponse);  
    delay(100);
    sendWirelessMsg(pingResponse);

  } 
  else if (cmd == "1"){
    Serial.println("pulling low");
    digitalWrite(4, LOW);
    sendWirelessMsg(sourceDevice+":1:1:"+txId+":1;");
  }
  else if (cmd == "2"){
    Serial.println("pulling high");
    digitalWrite(4, HIGH);
    sendWirelessMsg(sourceDevice+":1:1:"+txId+":2;");
  }


}

void sendWirelessMsg(String msg){

  char tx_buf[BUF_LEN]= {
    0    };
  msg.toCharArray(tx_buf,BUF_LEN);

  byte addr[] = MASTERADDR;
  nRF905_setTXAddress(addr);

  // Set payload data (reply with data received)
  nRF905_setData(tx_buf, sizeof(tx_buf));

  // Send payload (send fails if other transmissions are going on, keep trying until success)
  while(!nRF905_send());

  // Put back into receive mode
  nRF905_receive();

}


void split(char delim, String str, String *str_array) {
  // this method takes a string, chops it up at the point of delim and drops each piece
  // into str_array.
  split(delim, str, str_array, 0);
}


void split(char delim, String str, String *str_array, int limit) {
  // this method takes a string, chops it up at the point of delim and drops each piece
  // into str_array.
  int i = 0;
  while (str.indexOf(delim) >= 0) {
    str_array[i] = str.substring(0, str.indexOf(delim));
    str = str.substring(str.indexOf(delim)+1);
    i++;
    // this should set the limit now.
    if (limit != 0 && i >= limit) {
      break;
    }

  }
  // dump in the last part.
  str_array[i] = str;
  return;
}








