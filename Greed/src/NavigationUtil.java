import GraphEntity.*;


public class NavigationUtil {

    public static double getEdgeWight(MyGraph graph, int i, int j)
    {
        double w=0;
        EdegeNode a = null;
        a=graph.point[i].firstArc;
        while(a!=null)
        {
            if(a.adjvex==j)
            {
                return w+a.value;
            }
            a=a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }
        return -1;
    }



    public static boolean isConnected(MyGraph graph,int i,int j)
    {
        double w=0;
        EdegeNode a = null;
        a=graph.point[i].firstArc;
        while(a!=null)
        {
            if(a.adjvex==j)
            {
                return true;
            }
            a=a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }
        return false;
    }


}
