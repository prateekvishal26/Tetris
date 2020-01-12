from py4j.java_gateway import JavaGateway, GatewayParameters, launch_gateway
import numpy as np
import train


def start_api_server():
    port = launch_gateway(classpath="/home/nithin/Desktop/Georgia Tech/Spring 2019/Adaptive Control and Reinforcement Learning/ACRL/Tetris/src",
                          die_on_exit=True)

    global gateway
    gateway = JavaGateway(gateway_parameters=GatewayParameters(port=port))
    player_tetris = gateway.jvm.PlayerSkeletonCommunication().getFeatures()
    weight = gateway.jvm.java.util.ArrayList()
    weight.append(1.0)
    weight.append(2.0)
    weight.append(3.0)
    weight.append(4.0)
    weight.append(5.0)
    weight.append(6.0)
    weight.append(7.0)
    weight.append(8.0)
    return player_tetris, weight


def stop_api_server():
    global gateway
    gateway.shutdown()


if __name__ == '__main__':
    
    ## Number of iterations in Training
    n = 3 	
    for i in range(n):
	player_tetris, weight = start_api_server()
	feature_episode = player_tetris.run()
	feature_episode = np.array(list(feature_episode))
	score = player_tetris.testRun(weight)
        print(feature_episode)
        print(score)
	## The train.py needs to be called here and the weights need to be stored here
	stop_api_server()

