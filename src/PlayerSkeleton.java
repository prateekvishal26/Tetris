import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class PlayerSkeleton {
	public List<int[]> features = new ArrayList<>(); 

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		Random generator = new Random();
		int randomIndex = generator.nextInt(legalMoves.length);
		return randomIndex;
	}

	public List<int []> run() {
		PlayerSkeleton p = new PlayerSkeleton();
		State s = new State();
		TFrame t = new TFrame(s);
		Features feat = new Features();
		//List<int []> features = new ArrayList<>();

		while (!s.hasLost()) {
			int move = p.pickMove(s, s.legalMoves());
			this.features.add(feat.getFeatures(s, move));
			s.makeMove(move);
			s.draw();
			s.drawNext(0, 0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//for(int i = 0; i< this.features.size(); i++)
		//System.out.println(Arrays.toString(this.features.get(i)));
		System.out.println(s.getRowsCleared());
		return this.features;
	}


	/*public static void main(String[] args) {
		PlayerSkeleton player = new PlayerSkeleton();
		player.run();
		for (int i = 0; i < player.features.size(); i++)
			System.out.println(Arrays.toString(player.features.get(i)));
	}*/

	public int testRun(List<Double> weights) {
		double w[] = weights.stream().mapToDouble(Double::doubleValue).toArray();
		State s = new State();
		while (!s.hasLost()) {
			s.makeMove(getActionFromLinearPolicy(s, s.legalMoves(), w));
		}
		return s.getRowsCleared();
	}

	public int testRunNew(double [] w) {
		State s = new State();
		while (!s.hasLost()) {
			s.makeMove(getActionFromLinearPolicy(s, s.legalMoves(), w));
		}
		return s.getRowsCleared();
	}

	public int getActionFromLinearPolicy(State s, int[][] moves, double[] weights) {
		double max = -1;
		int maxIndex = -1;
		Features feat = new Features();

		for (int i = 0; i < moves.length; i++) {
			State temp = new State(s);
			temp.makeMove(i);

			int[] features = feat.getFeatures(s, i);
			double value = linearValueFunction(features, weights);
			if (value > max || i == 0) {
				maxIndex = i;
				max = value;
			}
		}
		return maxIndex;
	}

	public double linearValueFunction(int[] features, double[] weights) {
		double value = 0;
		for (int i = 0; i < weights.length; i++)
			value += weights[i] * (double) features[i];
		System.out.format("values %d %d %d %d %d %d %d %d %f \n", features[0], features[1],features[2],features[3],
				features[4],features[5],features[6], features[7], value);
		return value;
	}


}
