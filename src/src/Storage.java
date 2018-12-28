import java.util.ArrayList;

class StorageItem {
    String name;
    long count;
    StorageItem(String name, long count) {
        this.name = name;
        this.count = count;
    }
}

public class Storage {
    private long capacity = Consts.WAREHOUSE_INITIAL_CAPACITY, level = 1;
    private ArrayList<StorageItem> items;
    private long size() {
        long ans = 0;
        for(StorageItem storageItem: items)
            ans += storageItem.count;
        return ans;
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

    public void upgrare() {
        level ++;
        capacity += Consts.WAREHOUSE_UPGRADE_CAPACITY;
    }

    public String store(String name, long count) { // returns "nok" or "ok", count could be positive or negative
        if(count == 0)  return "ok";
        if(count > 0){
            if(count > capacity - size())
                return "nok";
            for(StorageItem storageItem: items)
                if(storageItem.name.equals(name)) {
                    storageItem.count += count;
                    return "ok";
                }
            items.add(new StorageItem(name, count));
            return "ok";
        }
        else {
            boolean flag = false;
            int idx = 0;
            for(StorageItem storageItem: items) {
                if(storageItem.name.equals(name) && storageItem.count + count >= 0) {
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
