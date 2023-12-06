import java.io.IOException;

class DataFrame implements java.io.Serializable {
    private String srcMAC;
    private String desMAC;
    private String srcStationName;
    private String desStationName;
    private String srcIPAddress;
    private String desIPAddress;
    private String msgPayload;
    private String type;

    @Override
    public String toString() {
        return "{" +
            " srcMAC='" + getSrcMAC() + "'" +
            ", desMAC='" + getDesMAC() + "'" +
            ", srcStationName='" + getSrcStationName() + "'" +
            ", desStationName='" + getDesStationName() + "'" +
            ", srcIPAddress='" + getSrcIPAddress() + "'" +
            ", desIPAddress='" + getDesIPAddress() + "'" +
            ", msgPayload='" + getMsgPayload() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }

    public DataFrame(String srcMAC, String desMAC, String srcStationName, String desStationName, String srcIPAddress, String desIPAddress, String msgPayload, String type) {
        this.srcMAC = srcMAC;
        this.desMAC = desMAC;
        this.srcStationName = srcStationName;
        this.desStationName = desStationName;
        this.srcIPAddress = srcIPAddress;
        this.desIPAddress = desIPAddress;
        this.msgPayload = msgPayload;
        this.type = type;
    }

    public DataFrame() {
    }

    public String getSrcMAC() {
        return this.srcMAC;
    }

    public void setSrcMAC(String srcMAC) {
        this.srcMAC = srcMAC;
    }

    public String getDesMAC() {
        return this.desMAC;
    }

    public void setDesMAC(String desMAC) {
        this.desMAC = desMAC;
    }

    public String getSrcStationName() {
        return this.srcStationName;
    }

    public void setSrcStationName(String srcStationName) {
        this.srcStationName = srcStationName;
    }

    public String getDesStationName() {
        return this.desStationName;
    }

    public void setDesStationName(String desStationName) {
        this.desStationName = desStationName;
    }

    public String getMsgPayload() {
        return this.msgPayload;
    }

    public void setMsgPayload(String msgPayload) {
        this.msgPayload = msgPayload;
    }

    public String getSrcIPAddress() {
        return this.srcIPAddress;
    }

    public void setSrcIPAddress(String srcIPAddress) {
        this.srcIPAddress = srcIPAddress;
    }

    public String getDesIPAddress() {
        return this.desIPAddress;
    }

    public void setDesIPAddress(String desIPAddress) {
        this.desIPAddress = desIPAddress;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(this.srcMAC);
        stream.writeObject(this.desMAC);
        stream.writeObject(this.srcStationName);
        stream.writeObject(this.desStationName);
        stream.writeObject(this.srcIPAddress);
        stream.writeObject(this.desIPAddress);
        stream.writeObject(this.msgPayload);
        stream.writeObject(this.type);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        this.srcMAC = (String) stream.readObject();
        this.desMAC = (String) stream.readObject();
        this.srcStationName = (String) stream.readObject();
        this.desStationName = (String) stream.readObject();
        this.srcIPAddress = (String) stream.readObject();
        this.desIPAddress = (String) stream.readObject();
        this.msgPayload = (String) stream.readObject();
        this.type = (String) stream.readObject();
    }
}
