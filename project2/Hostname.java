public class Hostname {
    
    String name;
    String ipAddress;

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            "}";
    }

    public Hostname(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
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

}
