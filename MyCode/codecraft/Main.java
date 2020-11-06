package codecraft;

import codecraft.algorithm.Floyd;
import codecraft.dynamic.DynamicCar;
import codecraft.origin.Car;

public class Main {
    public static void main(String[] args) {

        ImportData im=new ImportData(DynamicData.carPath,
                DynamicData.roadPath,
                DynamicData.crossPath,
                DynamicData.presetAnswerPath,
                DynamicData.ansPath
        );

        /*double currentTime=System.currentTimeMillis();
        //im.BubbleSort();
        im.sortWithId(DynamicData.cars);
        System.out.printf("cars sort runTime:%s ms\n",System.currentTimeMillis()-currentTime);*/

        im.sortCarWithId();
        im.sortCrossWithId();

        Floyd floyd=new Floyd();

        GetSolution solution=new GetSolution();
        solution.getSolution();

    }
}
