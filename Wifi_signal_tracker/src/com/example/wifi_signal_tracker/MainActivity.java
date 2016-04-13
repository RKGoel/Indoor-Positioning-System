package com.example.wifi_signal_tracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity {
	ArrayList<String> scan_result=new ArrayList<String>();
	EditText reference_signal;
	Button scan,show_map;
	double act_dist[]=new double[20];
	double x[]=new double[3];
	double y[]=new double[3];
	double x_temp,y_temp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		reference_signal=(EditText)findViewById(R.id.signal);
		scan=(Button)findViewById(R.id.scan);
		show_map=(Button)findViewById(R.id.map);
		Calendar c = Calendar.getInstance(); 
				
		scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				find_signal();
			}
		});
		
		show_map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.e("co_ord", " "+x_temp+" "+y_temp);
				Intent intent=new Intent( getApplicationContext(), Show_Map.class);
				intent.putExtra("x", x_temp);
				intent.putExtra("y", y_temp);
				startActivity(intent);
			}
		});
		ListView list=(ListView)findViewById(R.id.list);

		WifiManager wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		if(!wifi.isWifiEnabled())
		wifi.setWifiEnabled(true);
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<ScanResult> results = null;
		int acc_pts=3;//indicating no. of access points in consideration
		double dist[][]= new double[acc_pts][10];
		double ap[]=new double[acc_pts];
		double mean[]=new double[20];
		double std_dev[]=new double[acc_pts];
		int rssi[][]=new int[10][10];
		HashMap<String,Integer> one_meter_dist =new HashMap<String, Integer>();
		HashMap<Integer,String> map =new HashMap<Integer,String>();
		ArrayList<String> pool=new ArrayList<String>();
		ArrayList<String> pre_pool=new ArrayList<String>();
		
		pre_pool.add("78:54:2e:33:76:d0");
		pre_pool.add("78:54:2e:33:77:10");
		pre_pool.add("d8:55:a3:fe:6f:f3");
		pre_pool.add("34:aa:8b:df:d8:cf");
		pre_pool.add("5a:76:3f:f6:27:b5");
		pre_pool.add("d8:55:a3:e3:25:c4");
		pre_pool.add("92:21:81:41:a4:09");
		pre_pool.add("00:1c:f0:8a:19:99");
		pre_pool.add("c4:a8:1d:0b:0a:80");
		pre_pool.add("18:59:36:0b:86:e6");
		
		one_meter_dist.put("78:54:2e:33:76:d0",-42);//n/w lab
		one_meter_dist.put("78:54:2e:33:77:10",-30);
		one_meter_dist.put("d8:55:a3:fe:6f:f3",-42);//my home mts
		one_meter_dist.put("34:aa:8b:df:d8:cf",-42);//papa mob samsung
		one_meter_dist.put("5a:76:3f:f6:27:b5",-45);
		one_meter_dist.put("d8:55:a3:e3:25:c4",-38);
		one_meter_dist.put("92:21:81:41:a4:09",-39);//rajat mobile
		one_meter_dist.put("00:1c:f0:8a:19:99",-77);//Ap1 D-link n/w lab sw1-181
		one_meter_dist.put("c4:a8:1d:0b:0a:80",-30);
		one_meter_dist.put("18:59:36:0b:86:e6",-54);//Manika didi
		
		
		long sec=c.getTimeInMillis();
		Log.e("Time in millis",""+sec);
		for(int l=0;l<acc_pts;l++){
			ap[l]=0;
			for(int p=0;p<10;p++){
				dist[l][p]=0;
			}
		}
		
		wifi.startScan();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int ptr[]=new int[acc_pts];
		int f=0;
		
		results = wifi.getScanResults();
		
		int l=0;
		int all_rssi_values[]=new int[10];
		
		for (ScanResult result : results){
			map.put(result.level, result.BSSID);
			if(pre_pool.contains(result.BSSID))
			{all_rssi_values[l]=result.level;
			l++;}
		}
		
		all_rssi_values=sort(all_rssi_values);
		for(int q=0;q<10;q++)
		Log.e("rssi", ""+all_rssi_values[q]);
		
		for(int i=0;i<acc_pts;i++)
		{
			if(all_rssi_values[i]!=0&& map.get(all_rssi_values[i])!=null)
		pool.add(map.get(all_rssi_values[i]));
		}
		for(int i=0;i<acc_pts;i++)
		Log.e("rssi", ""+pool.get(i));
		
		for(int i=0;i<10;i++)// Here scanning 10 times one at a time
		{
		
			wifi.startScan();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		results = wifi.getScanResults();// getting results of a single scan
		
		for (ScanResult result : results) {
			//scan_result.removeAll(scan_result);
			  //scan_result.add(result.SSID+" "+"("+result.level+")"+" "+result.BSSID);
			  for(int u=0;u<pool.size();u++)
			if(result.BSSID.equalsIgnoreCase(pool.get(0)))//n/w lab
			  {
				  rssi[u][ptr[u]]=result.level;
				  ptr[u]++;
			  }	

			/*else if(result.BSSID.equalsIgnoreCase(pool.get(1)))//n/w lab
			  {
				  rssi[1][ptr[1]]=result.level;
				  ptr[1]++;
			  }	*/

			/*else if(result.BSSID.equalsIgnoreCase(pool.get(2)))//n/w lab
			  {
				  rssi[2][ptr[2]]=result.level;
				  ptr[2]++;
			  }*/	
			
			  		}
		
		}
		
		int ptr_[]=new int[acc_pts];
		for(int j=0;j<acc_pts;j++){
			for(int i=0;i<10;i++){
				dist[j][ptr_[j]]=(double)calculate_distance(rssi[j][i],one_meter_dist.get(pool.get(j)));
				ap[j]+=dist[j][ptr_[j]];
				ptr_[j]++;
			}
		}
						
			double var[]=new double[acc_pts];
			for(int j=0;j<acc_pts;j++){
				mean[j]=ap[j]/10;
				for(int k=0;k<10;k++)
				var[j]+=Math.pow(dist[j][k]-mean[j],2);
			var[j]/=10;
			std_dev[j]=(double)Math.sqrt(var[j]);
			act_dist[j]=(double)((Math.pow(mean[j], 4))/((Math.pow(mean[j], 2))+(Math.pow(std_dev[j],2))));			
			act_dist[j]=(double)Math.sqrt((double)act_dist[j]);
			}
			
		double error = 0;
		double X0 = 0,Y0 = 0;
		//double x[]=new double[3];
		//double y[]=new double[3];
		x[0]=11.9; x[1]=14.3; x[2]=1.5;
		y[0]=17.5; y[1]=2.4; y[2]=11.4;
		
		X0=(x[0]+x[1]+x[2])/3.0;
		Y0=(y[0]+y[1]+y[2])/3.0;
		double x_dash,y_dash;
		//temporarily assigning calculated values
		act_dist[0]=7.9; act_dist[1]=8.2; act_dist[2]=6.3;
				
		x_dash=X0-((fxn(X0,Y0)*alpha(X0,Y0))/(Math.pow(alpha(X0,Y0),2)+Math.pow(beta(X0,Y0),2)));
		y_dash=Y0-((fxn(X0,Y0)*beta(X0,Y0))/(Math.pow(alpha(X0,Y0),2)+Math.pow(beta(X0,Y0),2)));
		x_temp=X0; y_temp=Y0;
		
		while((alpha(X0,Y0)*alpha(x_dash,y_dash)>0)&&(beta(X0,Y0)*beta(x_dash,y_dash)>0)){
			X0=x_dash;
			Y0=y_dash;
			x_temp=X0; y_temp=Y0;
			x_dash=X0-((fxn(X0,Y0)*alpha(X0,Y0))/(Math.pow(alpha(X0,Y0),2)+Math.pow(beta(X0,Y0),2)));
			y_dash=Y0-((fxn(X0,Y0)*beta(X0,Y0))/(Math.pow(alpha(X0,Y0),2)+Math.pow(beta(X0,Y0),2)));
		}
		
		double lambda;
		lambda=((fxn(X0,Y0)*alpha(X0,Y0))/(Math.pow(alpha(X0,Y0),2)+Math.pow(beta(X0,Y0),2)));
		lambda=lambda/10.0;
		
		while((alpha(X0,Y0)*alpha(x_dash,y_dash)>0)&&(beta(X0,Y0)*beta(x_dash,y_dash)>0)){
			X0=x_dash;
			Y0=y_dash;
			x_temp=X0; y_temp=Y0;
			x_dash=X0-((lambda*alpha(x_temp,y_temp)));
			y_dash=Y0-((lambda*beta(x_temp,y_temp)));
		}
		// loop that goes through list		
		scan_result.add("Name "+" RSSI(dBm) "+" MAC "+" Dist.(m)");
		int p=0;
		for (int z=0;z<acc_pts;z++) {
				scan_result.add(pool.get(z)+" "+act_dist[z]+" "+x_temp+" "+y_temp);
		//temporarily putting this line in case those wifi in "if-block" are not there
			//scan_result.add(result.SSID+" "+"("+mean[p]+")"+" "+result.BSSID+" "+act_dist[p]+" "+x_temp+" "+y_temp);
		}
		
		 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
              this, 
               android.R.layout.simple_list_item_1,
               scan_result );

       list.setAdapter(arrayAdapter); 

			//find_signal();
			
		/*	final Timer th=new Timer();
       th.schedule(new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//find_signal();
			th.cancel();
		}
	},2000 );
       */
	}
	
	public int [] sort(int arr[]){
		int i=0,j=0;// for bubble sort
		for(i=arr.length-1;i>0;i--){
			for(j=0;j<i;j++){
				if(arr[j]>arr[j+1]){
					int temp=arr[j+1];
					arr[j+1]=arr[j];
					arr[j]=temp;
				}
			}
		}
		return arr;
	}
	
	public double alpha(double X,double Y){
		double alpha=0.0;
		for(int i=0;i<3;i++)
			alpha+=(X-x[i])*((Math.pow(X-x[i],2))+(Math.pow(Y-y[i],2))-Math.pow(act_dist[i],2));
			alpha=4*alpha;
		return alpha;
	}

public double beta(double X,double Y){
	double beta=0.0;
	for(int i=0;i<3;i++)
		beta+=(Y-y[i])*((Math.pow(X-x[i],2))+(Math.pow(Y-y[i],2))-Math.pow(act_dist[i],2));
	beta=4*beta;
		
	return beta;
}
	
public double fxn(double X,double Y){
	double fxn=0.0;
	for(int s=0;s<3;s++)
		fxn+=Math.pow((Math.pow(X-x[s],2 )+(Math.pow(Y-y[s],2 ))-(Math.pow(act_dist[s],2))),2);
		
	return fxn;
}
public void find_signal(){
		ListView list=(ListView)findViewById(R.id.list);
		list.setAdapter(null);
		WifiManager wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		if(!wifi.isWifiEnabled())
		wifi.setWifiEnabled(true);
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		wifi.startScan();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//currently we are not using the Normalized value of signal strength but may use in future
		final int MIN_RSSI = -100;
		  // Anything better than or equal to this will show the max bars.
		  final int MAX_RSSI = -55;

		  int range = MAX_RSSI - MIN_RSSI;
		  
		List<ScanResult> results = wifi.getScanResults();
		
		// loop that goes through list		
		scan_result.add("Name "+" RSSI(dBm) "+" Norm_RSSI "+" MAC "+" Dist.(m)");
		for (ScanResult result : results) {
			int rssi=100 - ((MAX_RSSI - result.level) * 100 / range);
		  scan_result.add(result.SSID+" "+"("+result.level+")"+" "+rssi+" "+result.BSSID+" "+calculate_distance(result.level,-30));
		}
		 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
              this, 
               android.R.layout.simple_list_item_1,
               scan_result );

       list.setAdapter(arrayAdapter); 
		
       //wifi.setWifiEnabled(false);

		
	}
	
	protected double calculate_distance(int rssi_level,int x){
		double temp;double dist;
		//double temp_signal=Double.parseDouble(reference_signal.getText().toString());
		double temp_signal=x;
		temp=(double)(rssi_level-temp_signal); //temp_signal is RSSI calculated at roughly 1 meter distance
		temp=temp/20.0;// taking n=3 for free space
		temp=-temp;
		dist=Math.pow(10,temp);
		return dist;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
