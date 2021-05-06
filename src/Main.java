import utils.DBTool;

public class Main
{
    public static void main(String[] args) {
        //пускам тука DBTool и го injectvam в tabovete с dependency constructor injection
        DBTool dbTool = DBTool.getInstance();
        TabFrame tabFrame = new TabFrame(dbTool);
    }
}
