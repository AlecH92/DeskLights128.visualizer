package com.hilltoprobotics.DeskLightsVU;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	 private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }

		  public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONArray json = new JSONArray(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
		  }
		  public static void main(String[] args) throws IOException, JSONException {
		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        System.out.println("Enter DeskLights128 IP Address: ");
		        System.out.println("0 for 192.168.0.220");
		        System.out.println("1 for 192.168.1.220");
		        System.out.print("IP: ");
		        String s = br.readLine();
		        if(s.equalsIgnoreCase("1")) {
		        	s = "192.168.1.220";
		        }
		        if(s.equalsIgnoreCase("0")) {
		        	s = "192.168.0.220";
		        }
			  String sendstuff = "";
			  DecimalFormat df = new DecimalFormat("#");
			  while(true) {
				  JSONArray json = readJsonFromUrl("http://127.0.0.1/_fft_expt.json");
				  JSONObject test = json.getJSONObject(0);
				  JSONArray thebins = test.getJSONArray("bins");
				  for (int j=0; j < thebins.length(); j++){
					  //System.out.println(thebins.get(j));
					  double dBValue = (double) thebins.get(j);
					  dBValue = map(dBValue, -75, -15, 0, 8);
					  if(dBValue < 0) {
						  dBValue = 0;
					  }
					  sendstuff = sendstuff + df.format(dBValue);
				  }
				  
				  //System.out.println(sendstuff); //debug
				  
				  try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				  
				  
				  ////udp request
				  DatagramSocket clientSocket = new DatagramSocket();
				  InetAddress IPAddress = InetAddress.getByName(s);
				  byte[] sendData = new byte[1024];
				  sendData = sendstuff.getBytes();
				  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
				  clientSocket.send(sendPacket);
				  clientSocket.close();      
				  ////end udp request
				  
				  sendstuff = "";
					      
				  try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			  }
		  }

		  private static int map(double dBValue, long in_min, long in_max, long out_min, long out_max)
		  {
		    return (int) ((dBValue - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
		  }
		}