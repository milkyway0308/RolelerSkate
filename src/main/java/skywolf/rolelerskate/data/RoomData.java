package skywolf.rolelerskate.data;

import java.util.HashMap;

public class RoomData {
    private HashMap<Long, Long> maps = new HashMap<>();

    public boolean isInCooldown(long id) {
        return maps.containsKey(id) && maps.get(id) > System.currentTimeMillis();
    }

    public void applyCooldown(long id) {
        maps.put(id, System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }


}
