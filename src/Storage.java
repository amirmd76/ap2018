import java.util.ArrayList;

class StorageItem {
    String type;
    long count;
    StorageItem(String type, long count) {
        this.type = type;
        this.count = count;
    }
}

public class Storage {
    private long capacity = Constants.WAREHOUSE_INITIAL_CAPACITY, level = 1;
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

    public void upgrade() {
        level ++;
        capacity += Constants.WAREHOUSE_UPGRADE_CAPACITY;
    }

    public boolean canStore(String type, long count) {
        if(count == 0)  return true;
        if(count > 0){
            if(count > capacity - size())
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

    public String store(String type, long count) { // returns "nok" or "ok", count could be positive or negative
        if(count == 0)  return "ok";
        if(count > 0){
            if(count > capacity - size())
                return "nok";
            for(StorageItem storageItem: items)
                if(storageItem.type.equals(type)) {
                    storageItem.count += count;
                    return "ok";
                }
            items.add(new StorageItem(type, count));
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

    public String remove(String type, long count){           //TODO handle removing stored products
        return null;                                         //TODO handle exception of removing items more than they are stored
    }

    public String sell(String type, long count){                        //TODO handle selling stored products
        return null;                                                    //TODO handle exception of selling items more than they are stored
    }
}
