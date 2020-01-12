import numpy as np
from py4j.java_gateway import JavaGateway, GatewayParameters, launch_gateway

def start_api_server():
    port = launch_gateway(classpath="/home/nithin/Desktop/Georgia Tech/Spring 2019/Adaptive Control and Reinforcement Learning/ACRL/Tetris/src",
                          die_on_exit=True)

    global gateway
    gateway = JavaGateway(gateway_parameters=GatewayParameters(port=port))
    player_tetris = gateway.jvm.PlayerSkeletonCommunication().getFeatures()
    return player_tetris


def stop_api_server():
    global gateway
    gateway.shutdown()


def generateSamples(u, sigma, numSamples):
    return np.random.multivariate_normal(u, np.diag(sigma), numSamples)

def train():
    iterations = 50
    numSamples = 100
    numFeatures = 8
    numTrials = 2

    numResample = 2
    u = [0]*numFeatures
    sigma = [100]*numFeatures
    bestSample = [0]*numFeatures

    for i in range(0, iterations):
        samples = generateSamples(u, sigma, numSamples)
        scores = [-1]*numSamples	
	
        for j in range(0, numSamples):
	    player_tetris = start_api_server()
	    weights = gateway.jvm.java.util.ArrayList() 
	    for k in range(samples[j].size):
		weights.append(samples[j][k])   	
	    scores[j] = player_tetris.testRun(weights)
	    print(scores)
	    feature_episode = player_tetris.run()
	    stop_api_server()

        sampleScores = sorted(zip(samples, scores), key=lambda x:x[1], reverse=True)
        newSamples, scores = zip(*sampleScores)
        resamples = np.asarray(newSamples[0:numResample])
        u = np.true_divide(np.sum(resamples, axis=0), numResample)
        sigma = np.zeros(resamples[0].shape)
        for j in range(0, numResample):
            sigma = np.add(sigma, np.square(np.subtract(resamples[j], u)))
        np.true_divide(sigma, numResample)
        sigma = np.sqrt(np.add(sigma, max(5-i/10, 0)*np.ones(sigma.shape))) # needs to be verified
        if i==iterations-1:
            bestSample = resamples[0]

    return bestSample, scores



if __name__=="__main__":
	best_sample, scores = train()
	print(best_sample)
	print(scores)
	
