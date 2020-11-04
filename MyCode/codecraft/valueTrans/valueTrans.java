package codecraft.valueTrans;

import java.util.Arrays;

public class valueTrans {
    public static void main(String[] args) {
        /*Father father=new Father();
//        father.l.put(1,new People(11));
//        father.l.put(2,new People(22));

        father.human[0]=new People(1);
        father.human[1]=new People(2);
        People p3=new People(3);
        People[] newHuman=father.human;

        //newHuman[0]=new People(3);
        //newHuman[0]=p3;
        //father.human[0]=p3;

        for(int i=0;i<newHuman.length;i++){
            System.out.println(newHuman[i].name);
        }
        System.out.println("father.human:");
        for(int i=0;i<newHuman.length;i++){
            System.out.println(father.human[i].name);
        }
*/
        Father father=new Father();
        father.humanT[0][0]=new People(1);
        father.humanT[0][1]=new People(2);
        father.humanT[1][0]=new People(4);

        //People p3=new People(4);
        People p3=father.humanT[1][0];
        People[][] newHumanT=father.humanT;

        //newHumanT[0][0]=new People(3);
        newHumanT[0][0]=p3;
        //father.humanT[0][0]=p3;


        for(int i=0;i<newHumanT.length;i++){
            System.out.println(newHumanT[0][i].name);
        }
        System.out.println("father.human:");
        for(int i=0;i<newHumanT.length;i++){
            System.out.println(father.humanT[0][i].name);
        }
    }
}
