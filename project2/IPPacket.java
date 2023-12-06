import java.io.IOException;

public class IPPacket implements java.io.Serializable {

    private DataFrame dataFrame;
    private String desIPAddress;
    private String srcIPAddress;

    @Override
    public String toString() {
        return "{" +
            " dataFrame='" + getDataFrame() + "'" +
            ", desIPAddress='" + getDesIPAddress() + "'" +
            ", srcIPAddress='" + getSrcIPAddress() + "'" +
            "}";
    }

    public IPPacket(DataFrame dataFrame, String desIPAddress, String srcIPAddress) {
        this.dataFrame = dataFrame;
        this.desIPAddress = desIPAddress;
        this.srcIPAddress = srcIPAddress;
    }

    public DataFrame getDataFrame() {
        return this.dataFrame;
    }

    public void setDataFrame(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
    }

    public String getDesIPAddress() {
        return this.desIPAddress;
    }

    public void setDesIPAddress(String desIPAddress) {
        this.desIPAddress = desIPAddress;
    }

    public String getSrcIPAddress() {
        return this.srcIPAddress;
    }

    public void setSrcIPAddress(String srcIPAddress) {
        this.srcIPAddress = srcIPAddress;
    }

    

}

