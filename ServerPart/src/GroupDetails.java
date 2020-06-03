import java.util.Comparator;

public class GroupDetails implements Comparable<GroupDetails> {
    private int groupId;
    private String machine;
    private String engine;
    private String machineManufacturer;

    public GroupDetails() {
        this.groupId = 0;
        this.machine = " ";
        this.engine = " ";
        this.machineManufacturer = " ";
    }

    public GroupDetails(String str) {
        String [] attributes = str.split("\\|");
        for(int i = 0; i < attributes.length; i++) {
            attributes[i] = attributes[i].trim();
        }
        this.groupId = Integer.parseInt(attributes[0]);
        this.machine = attributes[1];
        this.engine = attributes[2];
        this.machineManufacturer = attributes[3];
    }

    public int getGroupId() {
        return this.groupId;
    }

    public String getMachine() {
        return this.machine;
    }

    public String getEngine() {
        return this.engine;
    }

    public String getMachineManufacturer() {
        return machineManufacturer;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setMachineManufacturer(String machineManufacturer) {
        this.machineManufacturer = machineManufacturer;
    }

    public int compareTo(GroupDetails obj){
        return this.machine.compareTo(obj.getMachine());
    }

    public String stringFile() {
        String str = Integer.toString(this.groupId) + '|' + this.machine + '|' + this.engine + '|' + this.machineManufacturer;
        return str;
    }

    @Override
    public String toString() {
        String str = String.format("|%3d|%21s|%13s|%34s|", this.groupId, this.machine, this.engine, this.machineManufacturer);
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        GroupDetails guest = (GroupDetails) obj;
        return (this.machine.equals(guest.getMachine()) && this.engine.equals(guest.getEngine()) && this.machineManufacturer.equals(guest.getMachineManufacturer()) );
    }
}
