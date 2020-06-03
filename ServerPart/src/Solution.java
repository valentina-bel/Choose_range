import java.util.ArrayList;
import java.util.ListIterator;

public class Solution {
    private ArrayList<String> variants;
    private float[][] matrixMarks;
    private ArrayList<String> bestVariants;
    private Human user;
    private Human expert;

    public Solution(ArrayList<String> variants, float matrix[][], Human user, Human expert) {
        this.variants = variants;
        this.matrixMarks = matrix;
        bestVariants = new ArrayList<String>();
        this.user = user;
        this.expert = expert;
    }

    public void findBestVariants () {
        float[] prices = new float[variants.size()];
        float fullPrice = 0f;
        for (int i = 0; i < prices.length; i++) {
            prices[i] = 0;
            for(int j = 0; j < prices.length; j++) {
                prices[i] += matrixMarks[i][j];
            }
            fullPrice += prices[i];
        }
        for (int i = 0; i < prices.length; i++)
            prices[i] = prices[i]/fullPrice;
        float maxMark = -1f;
        ArrayList<Integer> indexMaxList = new ArrayList<>();
        ListIterator<Integer> iter = indexMaxList.listIterator();
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] > maxMark) {
                indexMaxList.clear();
                iter.add(i);
                maxMark = prices[i];
            }
            else {
                if (prices[i] == maxMark) {
                    iter.add(i);
                }
            }
        }
        iter = indexMaxList.listIterator();
        while (iter.hasNext()) {
            int ind = iter.next();
            bestVariants.add(variants.get(ind));
        }
    }

    public ArrayList<String> getBestVariants() {
        return bestVariants;
    }

    public void setUser(String userFullName) {
        String [] attributes = userFullName.split(" ");
        user.setLastName(attributes[0]);
        user.setFirstName(attributes[1]);
    }

    public void setExpert(String userFullName) {
        String [] attributes = userFullName.split(" ");
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = attributes[i].trim();
        }
        expert.setLastName(attributes[0]);
        if (attributes.length > 1)
            expert.setFirstName(attributes[1]);
    }

    public ArrayList<String> createReport(String dateTime) {
        ArrayList<String> report = new ArrayList<>();
        report.add("Отчет по выбору предпочтительного варианта ассортимента");
        report.add("");
        report.add("Дата и время выполнения выбора: " + dateTime);
        report.add("");
        report.add("Варианты предлагал(а): " + this.user.getLastName() + ' ' + this.user.getFirstName());
        report.add("");
        int i = 1;
        ListIterator<String> variantsIter = variants.listIterator();
        while(variantsIter.hasNext()) {
            report.add("Вариант " + i);
            report.add("------------------");
            report.add(String.format("|%9s|%6s|", "id группы", "%"));
            report.add("------------------");
            String [] var = variantsIter.next().split("\\*");
            for (int k = 0; k < var.length; k++ ) {
                report.add(var[k]);
            }
            //String var = variantsIter.next().replace('*','\n');
            report.add("------------------");
            i++;
        }
        report.add("");
        report.add("Сравнения проводил(а) эксперт: " + this.expert.getLastName() + ' ' + this.expert.getFirstName());
        report.add("");
        report.add("Матрица предпочтений");
        report.add("");
        for (int j = 0; j < matrixMarks.length; j++) {
            String row = "";
            for (int k = 0; k < matrixMarks.length - 1; k++) {
                row += String.format("%2.2f|",matrixMarks[j][k]);
            }
            row += String.format("%2.2f|",matrixMarks[j][matrixMarks.length - 1]);
            report.add('\t' + row);
        }
        report.add("");
        report.add("Предпочтительный(е) вариант(ы): ");
        ListIterator<String> bestVariantsIter = bestVariants.listIterator();
        while(bestVariantsIter.hasNext()) {
            report.add("Вариант ");
            report.add("------------------");
            report.add(String.format("|%9s|%6s|", "id группы", "%"));
            report.add("------------------");
            //report.add(bestVariantsIter.next().replace('*','\n'));

            String [] var = bestVariantsIter.next().split("\\*");
            for (int k = 0; k < var.length; k++ ) {
                report.add(var[k]);
            }
            report.add("------------------");
        }
        return report;
    }
}
