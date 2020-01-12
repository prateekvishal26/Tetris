import numpy as np
from py4j.java_gateway import JavaGateway, GatewayParameters, launch_gateway

def generateSamples(u, sigma, numSamples):
    return np.random.multivariate_normal(u, np.diag(sigma), numSamples)

def computeScore(sample, numTrials):
    return -1


def train():
    iterations = 50
    numSamples = 100
    numFeatures = 8
    numTrials = 2

    numResample = 20
    u = [0]*numFeatures
    sigma = [100]*numFeatures
    #print(sigma)
    bestSample = [0]*numFeatures

    for i in range(0, iterations):
        samples = generateSamples(u, sigma, numSamples)
        scores = [-1]*numSamples

        for j in range(0, numSamples):
            scores[j] = computeScore(samples[j], numTrials)
            #scores[j] = numSamples - j

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

    print(bestSample)
    return bestSample

train()
