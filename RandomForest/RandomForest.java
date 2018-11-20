import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomForest extends SupervisedLearner {
    DecisionTree[] forest;
    RandomForest(int n){
        forest = new DecisionTree[n];
    }
    @Override
    String name() {
        return "Random Forest";
    }
    Random rand = new Random(2);
    Matrix bootFeat;
    Matrix bootLab;
    @Override
    void train(Matrix features, Matrix labels) {
        for(int i=0;i<forest.length;i++){
            bootFeat = new Matrix(0,features.cols());
            bootFeat.copyMetaData(features);
            bootLab = new Matrix(0,labels.cols());
            bootLab.copyMetaData(labels);
            for(int j=0;j<features.rows();j++){
                int s = rand.nextInt(features.rows());
                Vec.copy(bootFeat.newRow(),features.row(s));
                Vec.copy(bootLab.newRow(),labels.row(s));
            }
            forest[i] = new DecisionTree();
            forest[i].train(bootFeat,bootLab);
        }
    }
    double mostFrequent(double arr[], int n)
    {
        Arrays.sort(arr);
        int max_count = 1;
        double res = arr[0];
        int curr_count = 1;

        for (int i = 1; i < n; i++)
        {
            if (arr[i] == arr[i - 1])
                curr_count++;
            else
            {
                if (curr_count > max_count)
                {
                    max_count = curr_count;
                    res = arr[i - 1];
                }
                curr_count = 1;
            }
        }
        if (curr_count > max_count)
        {
            max_count = curr_count;
            res = arr[n - 1];
        }
        return res;
    }
    @Override
    void predict(double[] in, double[] out) {
        double[] sol = new double[out.length];
        ArrayList<double[]> votes = new ArrayList<>();
        for(int i=0;i<forest.length;i++){
            double[] temp = new double[out.length];
            forest[i].predict(in,temp);
            votes.add(temp);
        }
        for(int i=0;i<votes.get(0).length;i++){
            double[] votesOne = new double[votes.size()];
            for(int j=0;j<forest.length;j++){
                votesOne[j] = votes.get(j)[i];
            }
            double res = mostFrequent(votesOne,votesOne.length);
            sol[i] = res;
        }
        Vec.copy(out,sol);
    }
}
