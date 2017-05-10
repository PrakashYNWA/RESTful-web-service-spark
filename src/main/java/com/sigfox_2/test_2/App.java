package com.sigfox_2.test_2;

import static spark.Spark.get;
import static spark.Spark.post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.sigfox_2.test_2.App.Model.Post;

import lombok.Data;


import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {

    private static final int HTTP_BAD_REQUEST = 400;
    static final int firstLetter = 1;
    static final int firstDigit = 27;
    static int ass = 0;
    
    interface Validable {
        boolean isValid();
    }

    @Data
    static class NewPostPayload {
    	private String device;
    	private String data;
    	private String ctr;
    	private String lig;
    	private String tmp;
    	private String snd;
    	private String air;

        public boolean isValid() {
            return !device.isEmpty() && !data.isEmpty(); 
        }
        
        public String getDevice(){
    		return device;
    	}
    	
    	public void setDevice(String device){
    		this.device = device;
    	}
    	
    	public String getData(){
    		return data;
    	}
    	
    	public void setData(String data){
    		this.data = data;
    	}
    	
    	public String getCtr(){
    		return ctr;
    	}
    	
    	public void setCtr(String ctr){
    		this.ctr = ctr;
    	}
    	
    	public String getLig(){
    		return lig;
    	}
    	
    	public void setLig(String lig){
    		this.lig = lig;
    	}
    	
    	public String getTmp(){
    		return tmp;
    	}
    	
    	public void setTmp(String tmp){
    		this.tmp = tmp;
    	}
    	
    	public String getSnd(){
    		return snd;
    	}
    	
    	public void setSnd(String snd){
    		this.snd = snd;
    	}
    	
    	public String getAir(){
    		return air;
    	}
    	
    	public void setAir(String air){
    		this.air = air;
    	}
    }
    
    // In a real application you may want to use a DB, for this example we just store the posts in memory
    public static class Model {
        
    	private Map<String, Post> posts = new HashMap<>();

        
        @Data
        public static class Post {
        	private String id;
        	private String device;
        	private static String ctr;
        	private String lig;
        	private String tmp;
        	private String snd;
        	private String air;
        	
        	public String getId() {
        		return id;
        	}

        	public void setId(String id) {
        		this.id = id;
        	}
        	
        	public String getDevice(){
        		return device;
        	}
        	
        	public void setDevice(String device){
        		this.device = device;
        	}
        	

        	
        	public static String getCtr(){
        		return ctr;
        	}
        	
        	public void setCtr(String ctr){
        		Post.ctr = ctr;
        	}
        	
        	public String getLig(){
        		return lig;
        	}
        	
        	public void setLig(String lig){
        		this.lig = lig;
        	}
        	
        	public String getTmp(){
        		return tmp;
        	}
        	
        	public void setTmp(String tmp){
        		this.tmp = tmp;
        	}
        	
        	public String getSnd(){
        		return snd;
        	}
        	
        	public void setSnd(String snd){
        		this.snd = snd;
        	}
        	
        	public String getAir(){
        		return air;
        	}
        	
        	public void setAir(String air){
        		this.air = air;
        	}

        }
        
        
        
        Post test = new Post();
		@SuppressWarnings("static-access")
		public Post createPost(String device,String data){ 
            String id = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()).toString();
            String ctr = decodeMessage_ctr(data);
            String lig = decodeMessage_lig(data);
            String tmp = decodeMessage_tmp(data);
            String snd = decodeMessage_snd(data);
            String air = decodeMessage_air(data);
            Post post = new Post();
            post.setId(id);
            post.setDevice(device);
            post.setCtr(ctr);
            post.setLig(lig);
            post.setTmp(tmp);
            post.setSnd(snd);
            post.setAir(air);
            //posts.put(post.getId(), post);
            posts.put(post.getCtr(), post);
            
            return post;
        }
        
        
		@SuppressWarnings("unchecked")
		public List getAllPosts(){
            return (List) posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());
        }
		
		public Post getPost(String ctr){
			return (Post) posts.get(ctr);
		}
		
		public String getThatCtr(){
			return Post.getCtr();
		}
		
		
    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
    
    // decode data to display counter
    public static String decodeMessage_ctr(String msg){
    	
    		ArrayList<String> result = new ArrayList<String>();
    		for (int i = 0; i < msg.length(); i = i + 4){
    		String val = msg.substring(i, i + 4);
    		
    		String a1 = val.substring(0,1);
    		String b2 = val.substring(1,2);
    		String c2 = val.substring(2,3);
    		String d2 = val.substring(3,4);
    		
    		int val2 = ((Integer.parseInt(c2,16)) << 12) + ((Integer.parseInt(d2,16)) << 8) + ((Integer.parseInt(a1,16)) << 4) + (Integer.parseInt(b2,16));
    		
    		String q =  Integer.toString(val2/10);
    		
    		
    		result.add(q);
    	    
    	    
    	}
    	return result.get(0);
    			
    }
    
    //decode data to display light intensity
    public static String decodeMessage_lig(String msg){
    	
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < msg.length(); i = i + 4){
		String val = msg.substring(i, i + 4);
		
		String a1 = val.substring(0,1);
		String b2 = val.substring(1,2);
		String c2 = val.substring(2,3);
		String d2 = val.substring(3,4);
		
		int val2 = ((Integer.parseInt(c2,16)) << 12) + ((Integer.parseInt(d2,16)) << 8) + ((Integer.parseInt(a1,16)) << 4) + (Integer.parseInt(b2,16));
		
		String q =  Integer.toString(val2/10);
		
		
		result.add(q);
	    
	    
	}
	return result.get(1);
			
}
    
    //decode data to display temperature
    public static String decodeMessage_tmp(String msg){
    	
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < msg.length(); i = i + 4){
		String val = msg.substring(i, i + 4);
		
		String a1 = val.substring(0,1);
		String b2 = val.substring(1,2);
		String c2 = val.substring(2,3);
		String d2 = val.substring(3,4);
		
		int val2 = ((Integer.parseInt(c2,16)) << 12) + ((Integer.parseInt(d2,16)) << 8) + ((Integer.parseInt(a1,16)) << 4) + (Integer.parseInt(b2,16));
		
		String q =  Integer.toString(val2/10);
		
		
		result.add(q);
	    
	    
	}
	return result.get(2);
			
}
    public static String decodeMessage_snd(String msg){
    	
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < msg.length(); i = i + 4){
		String val = msg.substring(i, i + 4);
		
		String a1 = val.substring(0,1);
		String b2 = val.substring(1,2);
		String c2 = val.substring(2,3);
		String d2 = val.substring(3,4);
		
		int val2 = ((Integer.parseInt(c2,16)) << 12) + ((Integer.parseInt(d2,16)) << 8) + ((Integer.parseInt(a1,16)) << 4) + (Integer.parseInt(b2,16));
		float newFloat = (float) val2;
		
		String q =  String.valueOf(newFloat/100);
		
		
		result.add(q);
	    
	    
	}
	return result.get(3);
			
}
    	
 public static String decodeMessage_air(String msg){
    	
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < msg.length(); i = i + 4){
		String val = msg.substring(i, i + 4);
		
		String a1 = val.substring(0,1);
		String b2 = val.substring(1,2);
		String c2 = val.substring(2,3);
		String d2 = val.substring(3,4);
		
		int val2 = ((Integer.parseInt(c2,16)) << 12) + ((Integer.parseInt(d2,16)) << 8) + ((Integer.parseInt(a1,16)) << 4) + (Integer.parseInt(b2,16));
		float newFloat = (float) val2;
		
		String q =  String.valueOf(newFloat/1000);
		
		
		result.add(q);
	    
	    
	}
	return result.get(4);
			
}
    
    
    
    public static void main( String[] args) throws InterruptedException {
        Model model = new Model();
        ArrayList<Post>tmp = new ArrayList<Post>();
        ArrayList<String>storage = new ArrayList<String>();
        
    	
        
        
        
        
        // insert a post (using HTTP post method)
        post("/posts", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "Bad";
                }
                Post id = model.createPost(creation.getDevice(), (creation.getData())); 
                tmp.add(id);
                response.status(200);
                response.type("application/json");
                return id;
                
            } catch (IOException jpe) {
                response.status(HTTP_BAD_REQUEST);
                return "Very bad";
            }
            
        });
        
        // get all post (using HTTP get method)
        get("/posts", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return dataToJson(model.getAllPosts());
        });
        
        get("/posts/:ctr", (request, response) -> {
        	String ctr = request.params(":ctr");
            response.status(200);
        	response.type("application/json");

            return dataToJson(model.getPost(ctr));
        });
        
        
        //Mqtt part
        TimeUnit.SECONDS.sleep(30); // delay for 30 secs for connection with sigfox
        
        int b = 0;
        
        while (b >= 0){
        	int a = Integer.parseInt(model.getThatCtr()) ;

        	
	        String topic        = "/sigfox";
	        String content      = dataToJson(model.getPost(Integer.toString(a)));
	        int qos             = 2;
	        String broker       = "tcp://203.0.113.124:1883";
	        String clientId     = "DataTest";
	        MemoryPersistence persistence = new MemoryPersistence();
	       
	        TimeUnit.SECONDS.sleep(20); // delay for 20 secs before sending data to make sure content is available before publishing message
	        
	        
	        try {
	            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
	            System.out.println("Connecting to broker: "+broker);
	            sampleClient.connect(connOpts);
	            System.out.println("Connected");
	            System.out.println("Publishing message: "+content );
	            MqttMessage message = new MqttMessage(content.getBytes());
	            message.setQos(qos);
	            sampleClient.publish(topic, message);
	            System.out.println("Message published");
	            sampleClient.disconnect();
	            System.out.println("Disconnected");
	            
	            
	            storage.clear();
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
    

