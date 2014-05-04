/*
 * Project: nRF905 AVR/Arduino Library/Driver
 * Author: Zak Kemble, contact@zakkemble.co.uk
 * Copyright: (C) 2014 by Zak Kemble
 * License: GNU GPL v3 (see License.txt)
 * Web: http://blog.zakkemble.co.uk/nrf905-avrarduino-librarydriver/
 */

/*
 * Wireless serial link
 *
 * 7 -> CE
 * 8 -> PWR
 * 9 -> TXE
 * 2 -> CD
 * 3 -> DR
 * 10 -> CSN
 * 12 -> SO
 * 11 -> SI
 * 13 -> SCK
 */

#include <nRF905.h>
#include <SPI.h>

#define PACKET_TYPE_DATA	0
#define PACKET_TYPE_ACK		1

#define MAX_PACKET_SIZE (NRF905_MAX_PAYLOAD - 2)
typedef struct {
  byte dstAddress[NRF905_ADDR_SIZE];
  byte type;
  byte len;
  byte data[MAX_PACKET_SIZE];
} 
packet_s;


String serialDataStream;

const int  MAX_STRING_LEN = 20; // set this to the largest string
// you will process

char stringBuffer[MAX_STRING_LEN+1]; // a static buffer for computation and output

void setup()
{
  // Start up the wireless 
  nRF905_init();

  // Put into receive mode
  nRF905_receive();

  Serial.begin(9600);

  Serial.println(F("Ready"));
}

void loop()
{

  recieveLoop();
}



void recieveLoop()
{
  packet_s packet;
  
  // Wait for data
  while(1)
  {  

    if(getPacket(&packet) && packet.type == PACKET_TYPE_DATA) // Got a packet and is it a data packet?
    {
      // Print data
      Serial.write(packet.data, packet.len);

      // Reply with ACK
      packet.type = PACKET_TYPE_ACK;
      packet.len = 0;
      sendPacket(&packet);

      // Put into receive mode
      nRF905_receive();
    }
    else 

    if(Serial.available())
    {
      
      // We've got some serial data, need to handle that
      readSearialCommand();
      
    }

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

      debug("Arduino Received: ");
      debug(serialDataStream);

      // You can put some if and else here to process the message juste like that:

      if(serialDataStream == "help"){
        Serial.println("OK. Send commands with a semicolin to terminate.\nPond commands start with 1:");
      }   

      if(serialDataStream.startsWith("1:")){
        handlePondCommand();
      }

      serialDataStream = ""; // Clear recieved buffer
    }
    else {
      debug("Appending");
      serialDataStream += recieved; 
      debug(serialDataStream);
    }

  } 

}

/**
 * Devices: 0=the adruino board itself, 1=the pond pump, 2=first relay, 3=second relay, 4=the DHT
 */
void handlePondCommand(){

  debug("Pond control command received.");

//  String msgParts[3];
//  split(':',serialDataStream, msgParts);

//  String deviceId = msgParts[1];
//  String statusSwitch = msgParts[2];

  debug("\n\rSending command to pond\n\r");

  packet_s packet;
  packet.type = PACKET_TYPE_DATA;  
  packet.len = serialDataStream.length();
  serialDataStream.getBytes(packet.data, MAX_PACKET_SIZE);
  sendPacket(&packet);

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

void debug(String s){
  Serial.println(s);
}

// Send a packet
static void sendPacket(void* _packet)
{
  // Void pointer to packet_s pointer hack
  // Arduino puts all the function defs at the top of the file before packet_s being declared :/
  packet_s* packet = (packet_s*)_packet;

  // Convert packet data to plain byte array
  byte totalLength = packet->len + 2;
  byte tmpBuff[totalLength];
  tmpBuff[0] = packet->type;
  tmpBuff[1] = packet->len;
  memcpy(&tmpBuff[2], packet->data, packet->len);

  // Set address of device to send to
  //nRF905_setTXAddress(packet->dstAddress);

  // Set payload data
  nRF905_setData(tmpBuff, totalLength);
  Serial.write(tmpBuff, totalLength);
  Serial.write("\n\rAbout to send...");

  // Send payload (send fails if other transmissions are going on, keep trying until success)
  while(!nRF905_send());

  Serial.write("\n\rSent...\n\r");
}



// Get a packet
static bool getPacket(void* _packet)
{
  // Void pointer to packet_s pointer hack
  // Arduino puts all the function defs at the top of the file before packet_s being declared :/
  packet_s* packet = (packet_s*)_packet;

  byte buffer[NRF905_MAX_PAYLOAD];

  // See if any data available
  if(!nRF905_getData(buffer, sizeof(buffer)))
    return false;

  // Convert byte array to packet
  packet->type = buffer[0];
  packet->len = buffer[1];

  // Sanity check
  if(packet->len > MAX_PACKET_SIZE)
    packet->len = MAX_PACKET_SIZE;

  memcpy(packet->data, &buffer[2], packet->len);

  return true;
}






