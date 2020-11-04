package codecraft;

public class Main {
    public static void main(String[] args) {

        new ImportData(DynamicData.carPath,
                DynamicData.roadPath,
                DynamicData.crossPath,
                DynamicData.presetAnswerPath,
                DynamicData.ansPath
        );
        GetSolution solution=new GetSolution();
        solution.getSolution();
    }
}
