public class RoutingTable {
    
    String desNetworkPrefix;
    String nextHopIpAddress;
    String networkMask;
    String networkInterface;

    @Override
    public String toString() {
        return "{" +
            " desNetworkPrefix='" + getDesNetworkPrefix() + "'" +
            ", nextHopIpAddress='" + getNextHopIpAddress() + "'" +
            ", networkMask='" + getNetworkMask() + "'" +
            ", networkInterface='" + getNetworkInterface() + "'" +
            "}";
    }

    public RoutingTable(String desNetworkPrefix, String nextHopIpAddress, String networkMask, String networkInterface) {
        this.desNetworkPrefix = desNetworkPrefix;
        this.nextHopIpAddress = nextHopIpAddress;
        this.networkMask = networkMask;
        this.networkInterface = networkInterface;
    }

    public String getDesNetworkPrefix() {
        return this.desNetworkPrefix;
    }

    public void setDesNetworkPrefix(String desNetworkPrefix) {
        this.desNetworkPrefix = desNetworkPrefix;
    }

    public String getNextHopIpAddress() {
        return this.nextHopIpAddress;
    }

    public void setNextHopIpAddress(String nextHopIpAddress) {
        this.nextHopIpAddress = nextHopIpAddress;
    }

    public String getNetworkMask() {
        return this.networkMask;
    }

    public void setNetworkMask(String networkMask) {
        this.networkMask = networkMask;
    }

    public String getNetworkInterface() {
        return this.networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

}
