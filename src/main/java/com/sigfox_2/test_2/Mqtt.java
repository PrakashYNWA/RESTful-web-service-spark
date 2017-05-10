package com.sigfox_2.test_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.sigfox_2.test_2.App.Model;

/**
 * Hello world!
 *
 */
public class Mqtt {
	public static void main( String[] args) {
		
	 Model model = new Model();	
	 String topic        = "/sigfox";
     String content      = model.getAllPosts().toString();
     int qos             = 2;
     String broker       = "tcp://203.0.113.124:1883";
     String clientId     = "DataTest";
     MemoryPersistence persistence = new MemoryPersistence();
     int a = 1;

     while (a > 0){
     try {
         MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
         MqttConnectOptions connOpts = new MqttConnectOptions();
         connOpts.setCleanSession(true);
         System.out.println("Connecting to broker: "+broker);
         sampleClient.connect(connOpts);
         System.out.println("Connected");
         System.out.println("Publishing message: "+content);
         MqttMessage message = new MqttMessage(content.getBytes());
         message.setQos(qos);
         sampleClient.publish(topic, message);
         System.out.println("Message published");
         sampleClient.disconnect();
         System.out.println("Disconnected");
         //tmp.clear();
         //System.exit(0);
     } catch(MqttException me) {
         System.out.println("reason "+me.getReasonCode());
         System.out.println("msg "+me.getMessage());
         System.out.println("loc "+me.getLocalizedMessage());
         System.out.println("cause "+me.getCause());
         System.out.println("excep "+me);
         me.printStackTrace();
     }
     }
}
}
