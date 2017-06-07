package com.example.borna2.trlababalan;


import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.interfaces.linsol.LinearSolver;

import static org.ejml.alg.dense.mult.MatrixMatrixMult.multTransB;
import static org.ejml.ops.CommonOps.addEquals;
import static org.ejml.ops.CommonOps.mult;
import static org.ejml.ops.CommonOps.multTransA;
import static org.ejml.ops.CommonOps.subtract;
import static org.ejml.ops.CommonOps.subtractEquals;

public class KalmanFilterOperation {

   public KalmanFilterOperation(){

   }


    private DenseMatrix64F F,Q,H;


    private DenseMatrix64F x,P;


    private DenseMatrix64F a,b;
    private DenseMatrix64F y,S,S_inv,c,d;
    private DenseMatrix64F K;

    private LinearSolver<DenseMatrix64F> solver;


    public void configure(DenseMatrix64F F, DenseMatrix64F Q, DenseMatrix64F H) {
        this.F = F;
        this.Q = Q;
        this.H = H;

        int dimenX = F.numCols;
        int dimenZ = H.numRows;

        a = new DenseMatrix64F(dimenX,1);
        b = new DenseMatrix64F(dimenX,dimenX);
        y = new DenseMatrix64F(dimenZ,1);
        S = new DenseMatrix64F(dimenZ,dimenZ);
        S_inv = new DenseMatrix64F(dimenZ,dimenZ);
        c = new DenseMatrix64F(dimenZ,dimenX);
        d = new DenseMatrix64F(dimenX,dimenZ);
        K = new DenseMatrix64F(dimenX,dimenZ);

        x = new DenseMatrix64F(dimenX,1);
        P = new DenseMatrix64F(dimenX,dimenX);


        solver = LinearSolverFactory.symmPosDef(dimenX);
    }


    public void setState(DenseMatrix64F x, DenseMatrix64F P) {
        this.x.set(x);
        this.P.set(P);
    }


    public void predict() {


        mult(F,x,a);
        x.set(a);


        mult(F,P,b);
        multTransB(b,F, P);
        addEquals(P,Q);
    }


    public void update(DenseMatrix64F z, DenseMatrix64F R) {

        mult(H,x,y);
        subtract(z, y, y);


        mult(H,P,c);
        multTransB(c,H,S);
        addEquals(S,R);


        if( !solver.setA(S) ) throw new RuntimeException("Invert failed");
        solver.invert(S_inv);
        multTransA(H,S_inv,d);
        mult(P,d,K);


        mult(K,y,a);
        addEquals(x,a);


        mult(H,P,c);
        mult(K,c,b);
        subtractEquals(P, b);
    }


    public DenseMatrix64F getState() {
        return x;
    }


    public DenseMatrix64F getCovariance() {
        return P;
    }
}