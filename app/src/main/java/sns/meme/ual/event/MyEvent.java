package sns.meme.ual.event;

/**
 * Created by amyung on 10/19/14.
 */
public class MyEvent {
    private int busEventCode;
    private int busEventPosition;


    public MyEvent() {
    }

    public MyEvent(int busEventCode) {
        this.busEventCode = busEventCode;
    }

    public MyEvent(int busEventCode, int busEventPosition) {
        this.busEventCode = busEventCode;
        this.busEventPosition = busEventPosition;
    }

    public int getBusEventCode() {
        return busEventCode;
    }

    public void setBusEventCode(int busEventCode) {
        this.busEventCode = busEventCode;
    }

    public int getBusEventPosition() {
        return busEventPosition;
    }

    public void setBusEventPosition(int busEventPosition) {
        this.busEventPosition = busEventPosition;
    }

    //    private String name;
//    private int position;
//
//    public MyEvent(String name) {
//        setName(name);
//    }
//
//    public MyEvent(String name, int position) {
//        this.name = name;
//        this.position = position;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getPosition() {
//        return this.position;
//    }
//
//    public void setPosition(int position) {
//        this.position = position;
//    }

}
