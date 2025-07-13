package PaooGame.Entities;

import PaooGame.Graphics.Constants;
import java.util.HashMap;
import java.util.Map;

public class EnemyFactory {
    private static final Map<Integer, EnemyPrototype> prototypes = new HashMap<>();

    static {
        // Inițializăm prototipurile cu poziția (0, 0)
        prototypes.put(Constants.EnemyConstants.CAT, new Cat(0, 0));
        prototypes.put(Constants.EnemyConstants.BOY, new Boy(0, 0));
    }

    public static Enemy createEnemy(int enemyType, float x, float y) {
        if (prototypes.containsKey(enemyType)) {
            // Folosim cloneEnemy cu parametrii pentru poziție
            return (Enemy) prototypes.get(enemyType).cloneEnemy(x, y);
        }

        // Fallback pentru ceilalți inamici care nu folosesc prototype pattern
        switch (enemyType) {
            case Constants.EnemyConstants.BEE:
                return new Bee(x, y);
            case Constants.EnemyConstants.GRANDMA:
                return new Grandma(x, y);
            case Constants.EnemyConstants.SPIKE:
                return new Spike(x, y);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }
    }

    // Metodă pentru a adăuga noi prototipuri la runtime (opțional)
    public static void registerPrototype(int enemyType, EnemyPrototype prototype) {
        prototypes.put(enemyType, prototype);
    }

    // Metodă pentru a obține toate tipurile de inamici disponibile (opțional)
    public static int[] getAvailableEnemyTypes() {
        return prototypes.keySet().stream().mapToInt(Integer::intValue).toArray();
    }
}