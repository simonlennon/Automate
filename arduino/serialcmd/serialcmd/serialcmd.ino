

String serialDataStream;


void setup()
{
  Serial.begin(9600);
  Serial.println("Ready");
}

void loop()
{
  recieveLoop();
}



void recieveLoop()
{
  // Wait for data
  while(true)
  {  
    if(Serial.available())
    {
      // We've got some serial data, need to handle that
      readSearialCommand();
    }
  }
}

/**
 * @TODO this should really timeout if the term character is not received quickly
 */

String readSearialCommand()
{
  
  while (Serial.available())
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

}

void debug(String s){
  Serial.println(s);
}


