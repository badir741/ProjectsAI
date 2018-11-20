import javafx.util.Pair;

import java.util.Arrays;
import java.util.Random;

abstract class Node
{
    abstract boolean isLeaf();
}

class InteriorNode extends Node
{
    int attribute; // which attribute to divide on
    double pivot; // which value to divide on
    Node a;
    Node b;
    boolean isContinue;
    InteriorNode(Node aT,Node bT,int col,double piv,boolean is){
        a = aT;
        b = bT;
        attribute = col;
        pivot = piv;
        isContinue = is;
    }
    boolean isLeaf() { return false; }
}

class LeafNode extends Node
{
    double[] label;
    LeafNode(Matrix labels){
        label = new double[labels.cols()];
        for(int i=0;i<labels.cols();i++){
            if(labels.valueCount(i) == 0)
                label[i] = labels.columnMean(i);
            else
                label[i] = labels.mostCommonValue(i);
        }
    }
    boolean isLeaf() { return true; }
}
public class DecisionTree extends SupervisedLearner {
    Node root;
    Random rand = new Random(2);
    Pair<Integer,Double> pickColAndPiv(Matrix feat,Matrix lab){
        int defCol = 0;
        double defPivot = 0.0;
        int dif = Integer.MAX_VALUE;
        int up =0,bot = 0;
        for(int patience=8;patience>0;patience--){
            int col = rand.nextInt(feat.cols());
            int rows = rand.nextInt(feat.rows());
            double pivot = feat.row(rows)[col];
            int vals = feat.valueCount(col);
            up =0;bot = 0;
            for(int i=0;i<feat.rows();i++){
                if(vals == 0){
                    if(feat.row(i)[col] < pivot)
                        up++;
                    else
                        bot++;
                }else{
                    if(feat.row(i)[col] == pivot)
                        up++;
                    else
                        bot++;
                }
            }
            if(Math.abs(up-bot)<dif){
                defCol = col;
                defPivot = pivot;
                dif = Math.abs(up - bot);
            }
        }
        return new Pair<Integer,Double>(defCol,defPivot);
    }
    Node buildTree(Matrix features,Matrix labels){
        Pair<Integer,Double> colAndPiv = pickColAndPiv(features,labels);
        Integer col=colAndPiv.getKey();
        Double pivot=colAndPiv.getValue();
        int vals = features.valueCount(col);
        Matrix featA = new Matrix(0,features.cols());
        featA.copyMetaData(features);
        Matrix featB = new Matrix(0,features.cols());
        featB.copyMetaData(features);
        Matrix labA = new Matrix(0,labels.cols());
        labA.copyMetaData(labels);
        Matrix labB = new Matrix(0,labels.cols());
        labB.copyMetaData(labels);
        boolean isContinues=false;
        int size = features.rows();
            for(int i=0;i<size;i++){
                if(vals == 0){
                    if(features.row(i)[col] < pivot){
                        Vec.copy(featA.newRow(),features.row(i));
                        Vec.copy(labA.newRow(),labels.row(i));
                    }else{
                        Vec.copy(featB.newRow(),features.row(i));
                        Vec.copy(labB.newRow(),labels.row(i));
                    }
                    isContinues = true;
                }else{
                    if(features.row(i)[col] == pivot){
                        Vec.copy(featA.newRow(),features.row(i));
                        Vec.copy(labA.newRow(),labels.row(i));
                    }else{
                        Vec.copy(featB.newRow(),features.row(i));
                        Vec.copy(labB.newRow(),labels.row(i));
                    }
                    isContinues = false;
                }
            }
        if(featA.rows() == 0 || featB.rows() == 0){
            return new LeafNode(labels);
        }
        Node a = buildTree(featA,labA);
        Node b = buildTree(featB,labB);
        return new InteriorNode(a,b,col,pivot,isContinues);
    }
    String name() { return "Decision Tree"; }
    void train(Matrix features, Matrix labels) {
        root = buildTree(features,labels);
    }
    void predict(double[] in, double[] out) {
        Node n = root;
        while(true){
            if(!n.isLeaf()){
                if(((InteriorNode) n).isContinue){
                    if(in[((InteriorNode) n).attribute] < ((InteriorNode) n).pivot){
                        n = ((InteriorNode) n).a;
                    }else{
                        n = ((InteriorNode) n).b;
                    }
                }else{
                    if(in[((InteriorNode) n).attribute] == ((InteriorNode) n).pivot){
                        n = ((InteriorNode) n).a;
                    }else{
                        n = ((InteriorNode) n).b;
                    }
                }

            }else{
                Vec.copy(out,((LeafNode)n).label);
                break;
            }
        }
    }
}
