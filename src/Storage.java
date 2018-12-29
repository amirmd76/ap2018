import java.util.ArrayList;

class StorageItem {
    String type;
    long count;
    long size;
    StorageItem(String type, long count, long size) {                  //TODO make this class compatible with product class
        this.type = type;
        this.count = count;
        this.size = size;
    }
}

public class Storage implements UpgradeableObject {
    private long capacity = Constants.WAREHOUSE_INITIAL_CAPACITY;
    private int level = 1;
    private ArrayList<StorageItem> items;
    private long size() {
        long ans = 0;
        for (StorageItem storageItem : items)
            ans += storageItem.count * storageItem.size;
        return ans;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public long getLeftCapacity() {
        return capacity - size();
    }

    public ArrayList<StorageItem> getItems() {
        return items;
    }

    public Storage() {
        items = new ArrayList<>();
    }



    public String upgrade() {
        if(level >= 4)
            return "Warehouse is fully upgraded";
        level ++;
        capacity = Constants.WAREHOUSE_UPGRADE_CAPACITY[(int)level-1];
        return String.format("Warehouse upgraded to level %d", level);

    }

    public boolean canStore(String type, long count, long size) {
        if(count == 0)  return true;
        if(count > 0){
            if(count * size > capacity - size())
                return false;
            for(StorageItem storageItem: items)
                if(storageItem.type.equals(type))
                    return true;
            return true;
        }
        boolean flag = false;
        for(StorageItem storageItem: items)
            if(storageItem.type.equals(type) && storageItem.count + count >= 0) {
                flag = true;
                break;
            }
        return flag;
    }

    public String store(String type, long count, long size) { // returns "nok" or "ok", count could be positive or negative
        if(count == 0)  return "ok";
        if(count > 0){
            if(count * size > capacity - size())
                return "nok";
            for(StorageItem storageItem: items)
                if(storageItem.type.equals(type)) {
                    storageItem.count += count;
                    return "ok";
                }
            items.add(new StorageItem(type, count, size));
            return "ok";
        }
        else {
            boolean flag = false;
            int idx = 0;
            for(StorageItem storageItem: items) {
                if(storageItem.type.equals(type) && storageItem.count + count >= 0) {
                    flag = true;
                    break;
                }
                idx ++;
            }
            if(!flag)
                return "nok";
            StorageItem storageItem = items.get(idx);
            storageItem.count += count;
            if(storageItem.count > 0)
                items.set(idx, storageItem);
            else
                items.remove(idx);
            return "ok";
        }
    }
}
