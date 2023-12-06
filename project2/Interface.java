public class Interface {
    
    String name;
    String ipAddress;
    String networkMask;
    String ethernetAddress;
    String bridgeName;

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", networkMask='" + getNetworkMask() + "'" +
            ", ethernetAddress='" + getEthernetAddress() + "'" +
            ", bridgeName='" + getBridgeName() + "'" +
            "}";
    }

    public Interface(String name, String ipAddress, String networkMask, String ethernetAddress, String bridgeName) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.networkMask = networkMask;
        this.ethernetAddress = ethernetAddress;
        this.bridgeName = bridgeName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNetworkMask() {
        return this.networkMask;
    }

    public void setNetworkMask(String networkMask) {
        this.networkMask = networkMask;
    }

    public String getEthernetAddress() {
        return this.ethernetAddress;
    }

    public void setEthernetAddress(String ethernetAddress) {
        this.ethernetAddress = ethernetAddress;
    }

    public String getBridgeName() {
        return this.bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }
}
