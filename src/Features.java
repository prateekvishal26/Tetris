import java.util.HashSet;
import java.util.Set;


public class Features {

    private int getHoleDepth(State s)
    {
        int[][] field = s.getField();
        int[] top = s.getTop();
        int holes = 0;

        for(int i = 0; i < State.COLS; i++)
        {
            if(top[i] != 0)
            {
                boolean hole_found = false;

                for (int j = 0; j < 0; j++) {
                    if (field[j][i] != 0 && hole_found)
                        holes++;
                    else if (field[j][i] == 0)
                        hole_found = true;
                }
            }
        }
        return holes;
    }

    private int getCumulativeWells(State s)
    {
        int[] top = s.getTop();
        int total = 0;
        for(int i = 0; i < State.COLS; i++)
        {
            int target;
            if(i == 0)
            {
                target = top[i+1];
            }
            else if(i == State.COLS - 1)
            {
                target = top[i-1];
            }
            else
            {
                target = Math.min(top[i+1], top[i-1]);
            }
            for(int j =1; j <= target - top[i]; j++)
            {
                total += j;
            }
        }
        return total;
    }

    private int getColumnTransitions(State s)
    {
        int[] top = s.getTop();
        int[][] field = s.getField();

        int transitions = 0;

        for(int i = 0; i < State.COLS; i++)
        {
            if(top[i] != 0)
                for(int j = 0; j < top[i]; j++)
                {
                    if(field[j][i] == 0)
                    {
                        if(j == 0)
                            transitions++;
                    }
                    else
                    {
                        if(j > 0)
                        {
                            if(field[j-1][i] == 0)
                            {
                                transitions++;
                                continue;
                            }
                        }
                        if(j < top[i] - 1)
                        {
                            if(field[j+1][i] == 0)
                            {
                                transitions++;
                                continue;
                            }
                        }
                    }
                }
        }
        return transitions;
    }

    private int getRowTransitions(State s)
    {
        int max_top = findMaximum(s.getTop());
        int[][] field = s.getField();
        int transitions = 0;

        for(int i = 0; i < max_top; i++)
        {
            for(int j = 0; j < State.COLS; j++)
            {
                if(field[i][j] == 0)
                {
                    if(j == 0 || j == State.COLS -1)
                        transitions++;
                }
                else
                {
                    if(j > 0)
                    {
                        if(field[i][j-1] == 0)
                        {
                            transitions++;
                            continue;
                        }
                    }
                    if(j < State.COLS - 1)
                    {
                        if(field[i][j+1] == 0)
                        {
                            transitions++;
                        }
                    }
                }
            }
        }
        return transitions;
    }

    private int getErodedPieces(State before, State after)
    {
        return after.getRowsCleared() - before.getRowsCleared();
    }

    private int getLandingHeight(State s1, int orient, int slot)
    {
        int [] top = s1.getTop();
        int [][][] pBottom= State.getpBottom();
        return top[slot] - pBottom[s1.nextPiece][orient][0];
    }

    private int findMaximum(int[] array)
    {
        int max = -1;
        for(int i = 0; i < array.length; i++)
            if(array[i] > max)
                max = array[i];
        return max;
    }

    private int[] getHoleFeatures(State s)
    {
        Set<Integer> rowsWithHoles = new HashSet<>();
        int[][] field = s.getField();
        int[] top = s.getTop();

        int holes = 0;

        for(int i = 0; i < State.COLS; i++)
        {
            if(top[i] != 0)
                for(int j = 0; j < top[i]; j++)
                    if(field[j][i] == 0)
                    {
                        rowsWithHoles.add(j);
                        holes++;
                    }
        }

        int[] result = {holes, rowsWithHoles.size()};
        return result;
    }

    public int[] getFeatures(State s, int move)
    {
        int [] features = new int[8];
        int slot = s.getSlot(move);
        int orient = s.getOrient(move);
        System.out.format("State: %d slot: %d orient: %d \n", s.getNextPiece(), slot, orient);
        State temp = new State(s);
        temp.makeMove(move);

        features[0] = getLandingHeight(s, orient, slot);
        features[1] = getErodedPieces(s, temp);
        features[2] = getRowTransitions(temp);
        features[3] = getColumnTransitions(temp);

        int [] holes = getHoleFeatures(temp);

        features[4] = holes[0];
        features[5] = getCumulativeWells(temp);
        features[6] = getHoleDepth(temp);
        features[7] = holes[1];
        return features;
    }
}
