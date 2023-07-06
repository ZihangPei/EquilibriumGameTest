import GraphEntity.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.exp;

public class Main {
    public static void main(String[] args) throws Exception {
        double u=0.0016;
        double r=2;
        double canshu;
        int n=3925,e=9895;
        //9行三列数组，每一列分别为开始点、终止点、该边权值
        int[][] data =getFile2("aaa.txt");
        //  int data[][]={{0,1,3},{0,2,2},{1,3,4},{2,1,1},{2,3,2},{2,4,3},{3,4,2},{3,5,1},{4,5,2}};
        MyGraph g=new MyGraph(n,e);
        g.createMyGraph(g,n,e,data);

        int[] numwork=getFile3("50.txt");
        int[] reward=getFile3("reward4.txt");
       List<Task> alternativepath = getFile1("log3.txt");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < alternativepath.size(); i++) {
            alternativepath.get(i).strategy.get(0).probability = 1;             //初始化路径的概率
        }


        int[] t = new int[alternativepath.size()];
        double Max1;


        for(int z=1;z<3;z++) {
            for (int y = 0; y < z; y++) {

                MyGraph temp = new MyGraph(n, e);

                //new MyGraph(n, e);
                for (int o = 0; o < alternativepath.size(); o++) {
                    //  MyGraph temp = g;
                    temp.createMyGraph(temp, n, e, data);
                    for (int i = 0; i < alternativepath.size(); i++) {
                        if (o != i) {
                            for (int j = 0; j < alternativepath.get(i).strategy.size(); j++) {

                                for (int k = 0; k < alternativepath.get(i).strategy.get(j).path.size() - 1; k++) {
                                    MyPath q = alternativepath.get(i).strategy.get(j);
                                    List<Integer> h = alternativepath.get(i).strategy.get(j).path;
                                    changeWight(temp, h.get(k), h.get(k + 1), q.probability * numwork[i]);


                               //     double f = NavigationUtil.getEdgeWight(g, h.get(k), h.get(k + 1));
                               //     double p = NavigationUtil.getEdgeWight(temp, h.get(k), h.get(k + 1));
                               //     newWight(temp, h.get(k), h.get(k + 1), f * (1 + 0.15 * (p - f) * (p - f)));

                                }
                            }
                            }
                            //临时交通图
                        }


//for(int i = 1; i <=3924 ; i++) {

  //   EdegeNode a = null;
   //  a = g.point[i].firstArc;
   //  while (a != null) {
    //     double f = NavigationUtil.getEdgeWight(g, i, a.adjvex);
    //     double p = NavigationUtil.getEdgeWight(temp,i, a.adjvex);
    //     newWight(temp, i, a.adjvex, f * (1 + 0.15 * (p - f) * (p - f)/50));
    //     a = a.nextEdge;
   //  }
 //}







                        // System.out.println(temp.point[188].firstArc.value);


                        for (int j = 0; j < alternativepath.get(o).strategy.size(); j++) {
                            MyPath h = alternativepath.get(o).strategy.get(j);
                            alternativepath.get(o).strategy.get(j).weight = 0;
                            for (int k = 0; k < alternativepath.get(o).strategy.get(j).path.size() - 1; k++) {
                                alternativepath.get(o).strategy.get(j).weight = NavigationUtil.getEdgeWight(temp, h.path.get(k), h.path.get(k + 1)) + alternativepath.get(o).strategy.get(j).weight;
                            }
                            //更新后消耗
                            // System.out.println(alternativepath.get(o).strategy.get(j).weight);
                        }


                        double sum = 0;
                        double sum1;
                        for (int j = 0; j < alternativepath.get(o).strategy.size(); j++) {


                            sum = exp(-u*(alternativepath.get(o).strategy.get(j).weight +r*alternativepath.get(o).strategy.get(j).weight )) + sum;
                          // System.out.println(alternativepath.get(o).strategy.get(j).weight+0.2*alternativepath.get(o).strategy.get(j).weight);
                        }


                        for (int j = 0; j < alternativepath.get(o).strategy.size(); j++) {
                            alternativepath.get(o).strategy.get(j).probability = exp(-u*(alternativepath.get(o).strategy.get(j).weight + r*alternativepath.get(o).strategy.get(j).weight)) / sum;
                            System.out.println(alternativepath.get(o).strategy.get(j).probability);
                        }

                }
            }

            for (int i = 0; i < alternativepath.size(); i++) {
                Max1=0;
                for (int j= 0; j < alternativepath.get(i).strategy.size(); j++) {
                    MyPath q = alternativepath.get(i).strategy.get(j);
                    if(q.probability>Max1)
                    {
                        Max1=q.probability;
                        t[i]=j;
                    }
                }
            }


            MyGraph temp1 = new MyGraph(n, e);
            temp1.createMyGraph(temp1, n, e, data);




            for (int i = 0; i < alternativepath.size(); i++) {
                for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
                    for (int k = 0; k < alternativepath.get(i).strategy.get(l).path.size() - 1; k++) {
                        MyPath q = alternativepath.get(i).strategy.get(l);
                        List<Integer> h = alternativepath.get(i).strategy.get(l).path;
                        changeWight(temp1, h.get(k), h.get(k + 1), q.probability * numwork[i]);
                    }
                }
            }

            for (int i = 0; i < alternativepath.size(); i++) {

                for (int j = 0; j < alternativepath.get(i).strategy.size(); j++) {
                    MyPath h = alternativepath.get(i).strategy.get(j);
                     alternativepath.get(i).strategy.get(j).weight = 0;
                    for (int k = 0; k < alternativepath.get(i).strategy.get(j).path.size() - 1; k++) {
                        alternativepath.get(i).strategy.get(j).weight = NavigationUtil.getEdgeWight(temp1, h.path.get(k), h.path.get(k + 1)) + alternativepath.get(i).strategy.get(j).weight;
                    }
                    //更新后消耗
                    // System.out.println(alternativepath.get(o).strategy.get(j).weight);
                }
            }


            double xxx = 0;
            for (int i = 0; i < alternativepath.size(); i++) {
                for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
                    MyPath q = alternativepath.get(i).strategy.get(l);

                    xxx = q.probability * numwork[i] * alternativepath.get(i).strategy.get(l).weight + xxx;
                }
            }
            System.out.println(xxx);



            for (int i = 0; i < alternativepath.size(); i++) {

                for (int j = 0; j < alternativepath.get(i).strategy.size(); j++) {
                    MyPath h = alternativepath.get(i).strategy.get(j);
                    alternativepath.get(i).strategy.get(j).weight = 0;
                    for (int k = 0; k < alternativepath.get(i).strategy.get(j).path.size() - 1; k++) {
                        alternativepath.get(i).strategy.get(j).weight = NavigationUtil.getEdgeWight(g, h.path.get(k), h.path.get(k + 1)) + alternativepath.get(i).strategy.get(j).weight;
                    }
                    //更新后消耗
                    // System.out.println(alternativepath.get(o).strategy.get(j).weight);
                }
            }


            double x = 0;
            for (int i = 0; i < alternativepath.size(); i++) {
                x = numwork[i] * alternativepath.get(i).strategy.get(0).weight + x;

            }
           // System.out.println(x/36000);















            MyGraph temp3 = new MyGraph(n, e);
            temp3.createMyGraph(temp3, n, e, data);
            for (int p = 0; p < alternativepath.size(); p++) {

                for (int k = 0; k < alternativepath.get(p).strategy.get(0).path.size() - 1; k++) {
                    MyPath q = alternativepath.get(p).strategy.get(0);
                    List<Integer> h = alternativepath.get(p).strategy.get(0).path;
                    changeWight(temp3, h.get(k), h.get(k + 1), numwork[p]);
                }
                //临时交通图
            }

            for (int p = 0; p < alternativepath.size(); p++) {
                MyPath h = alternativepath.get(p).strategy.get(0);
                alternativepath.get(p).strategy.get(0).weight = 0;
                for (int k = 0; k < alternativepath.get(p).strategy.get(0).path.size() - 1; k++) {
                    alternativepath.get(p).strategy.get(0).weight = NavigationUtil.getEdgeWight(temp3, h.path.get(k), h.path.get(k + 1)) + alternativepath.get(p).strategy.get(0).weight;
                }
                //更新后消耗
                // System.out.println(alternativepath.get(o).strategy.get(j).weight);

            }


            double xx = 0;
            for (int i = 0; i < alternativepath.size(); i++) {
                xx = numwork[i] * alternativepath.get(i).strategy.get(0).weight + xx;

            }
            System.out.println(xx);


            long endTime = System.currentTimeMillis();    //获取结束时间

            System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        }

        //临时交通图
  //      for (int i = 0; i < alternativepath.size(); i++) {
       //     for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
       //         MyPath q = alternativepath.get(i).strategy.get(l);
       //         System.out.print(q.probability);
      //          System.out.print(" ");
      //      }
     //       System.out.println();
     //   }


        for (int i = 0; i < alternativepath.size(); i++) {
            for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
                MyPath q = alternativepath.get(i).strategy.get(l);
                q.probability=0;
            }

        }


        for (int i = 0; i < alternativepath.size(); i++) {
            alternativepath.get(i).strategy.get(t[i]).probability = 1;             //初始化路径的概率
        }




        MyGraph temp2 = new MyGraph(n, e);
        temp2.createMyGraph(temp2, n, e, data);

        for (int i = 0; i < alternativepath.size(); i++) {
            for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
                for (int k = 0; k < alternativepath.get(i).strategy.get(l).path.size() - 1; k++) {
                    MyPath q = alternativepath.get(i).strategy.get(l);
                    List<Integer> h = alternativepath.get(i).strategy.get(l).path;
                    changeWight(temp2, h.get(k), h.get(k + 1), q.probability * numwork[i]);
                }
            }
        }

        for (int i = 0; i < alternativepath.size(); i++) {
            for (int j = 0; j < alternativepath.get(i).strategy.size(); j++) {
                MyPath h = alternativepath.get(i).strategy.get(j);
                alternativepath.get(i).strategy.get(j).weight = 0;
                for (int k = 0; k < alternativepath.get(i).strategy.get(j).path.size() - 1; k++) {
                    alternativepath.get(i).strategy.get(j).weight = NavigationUtil.getEdgeWight(temp2, h.path.get(k), h.path.get(k + 1)) + alternativepath.get(i).strategy.get(j).weight;
                }
                //更新后消耗
                // System.out.println(alternativepath.get(o).strategy.get(j).weight);
            }
        }

        double xxxx = 0;
        for (int i = 0; i < alternativepath.size(); i++) {
            for (int l = 0; l < alternativepath.get(i).strategy.size(); l++) {
                MyPath q = alternativepath.get(i).strategy.get(l);

                xxxx = q.probability * numwork[i] * alternativepath.get(i).strategy.get(l).weight + xxxx;
            }
        }
        System.out.println(xxxx);

                //   Task d=alternativepath.get(1);
    //    List<MyPath> f=d.strategy;
     //   MyPath l=f.get(1);
      //  List<Integer> h=l.path;
     //   int j=h.get(1);
      //  System.out.println(j);



    }

    private static List<Task> getFile1(String pathName) throws Exception {
        File file = new File(pathName);
        if (!file.exists())
            throw new RuntimeException("Not File!");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        List<Task> list = new ArrayList<>();
        while ((str = br.readLine()) != null) {

            String[] arr = str.split("],");


            List<MyPath> dArr = new ArrayList < >();


            for (String ss : arr) {
                if (ss != null) {

                    String[] b = ss.split(",");
                    List<Integer> temp = new ArrayList < >();
                    for (int i = 0; i < b.length; i++) {
                        temp.add(Integer.parseInt(b[i]));
                    }
                    MyPath te = new MyPath();
                    te.path=temp;
                    dArr.add(te);
                }
            }
            Task ta = new Task();
            ta.strategy=dArr;
            list.add(ta);

        }
            return list;
    }


    public static class MyPath {
        // 路径上的各个节点对应的数组下标（从起点到终点）
        public List<Integer> path;
        public double weight;
        public double probability;
        public MyPath() {
        }

        public MyPath(List<Integer> path) {
            this.path = path;
            this.weight = weight;
            this.probability = probability;
        }
    }
    public static class Task {
        // 路径上的各个节点对应的数组下标（从起点到终点）
        public List<MyPath> strategy;

        public Task() {
        }

        public Task(List<MyPath> strategy) {
            this.strategy = strategy;

        }
    }


    private static int[][] getFile2(String pathName) throws Exception {
        File file = new File(pathName);
        if (!file.exists())
            throw new RuntimeException("Not File!");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        List<int[]> list = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            int s = 0;
            String[] arr = str.split(",");
            int[] dArr = new int[arr.length];
            for (String ss : arr) {
                if (ss != null) {
                    dArr[s++] = Integer.parseInt(ss);
                }

            }
            list.add(dArr);
        }
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i).length)
                max = list.get(i).length;
        }
        int[][] array = new int[list.size()][max];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                array[i][j] = list.get(i)[j];
            }
        }
        return array;
    }

    private static int[] getFile3(String pathName) throws Exception {
        File file = new File(pathName);
        if (!file.exists())
            throw new RuntimeException("Not File!");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        int[] dArr = new int[3924];
        while ((str = br.readLine()) != null) {
            int s = 0;
            String[] arr = str.split(",");

            for (String ss : arr) {
                if (ss != null) {
                    dArr[s++] = Integer.parseInt(ss);
                }

            }
        }
        return dArr;
    }
    public static void changeWight(MyGraph graph, int i, int j,double change)
    {

        EdegeNode a = null;
        a=graph.point[i].firstArc;
        while(a!=null)
        {
            if(a.adjvex==j)
            {
                a.value=change+a.value;
            }
            a=a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }

    }
    public static void newWight(MyGraph graph, int i, int j,double change)
    {

        EdegeNode a = null;
        a=graph.point[i].firstArc;
        while(a!=null)
        {
            if(a.adjvex==j)
            {
                a.value=change;
            }
            a=a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }

    }

}