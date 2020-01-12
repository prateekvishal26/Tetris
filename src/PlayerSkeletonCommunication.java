import java.util.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import py4j.GatewayServer;

public class PlayerSkeletonCommunication{
	private List<int []> features_teris = new ArrayList<>();
	private PlayerSkeleton player;
	
	public PlayerSkeletonCommunication(){
		player = new PlayerSkeleton();
	}

	public PlayerSkeleton getFeatures(){
		return player;
	}

	public List<int []> getInternalListSkeleton(){
		return this.features_teris;
	}
	
	public static void main(String[] args){
		double[] w = {-47.43428264281537 , 26.126322713902507 , -57.65203670790028 , -109.31064691871963 , -229.7157867858535 , -68.43036599510476 , -10.017167508019858 , -159.8210878410291 };
		//PlayerSkeletonCommunication app = new PlayerSkeletonCommunication();
		PlayerSkeleton player = new PlayerSkeleton();
		player.testRunNew(w);
		//app.features_teris = player.features;
		//for(int i = 0; i< app.features_teris.size(); i++)
		//System.out.println(Arrays.toString(app.features_teris.get(i)));
		//GatewayServer server = new GatewayServer(app);
        //server.start();
        //System.out.println("Gateway Server Started");
        //*/
	}
}
