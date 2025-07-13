package PaooGame.Entities;

public interface EnemyPrototype {
    EnemyPrototype cloneEnemy();
    EnemyPrototype cloneEnemy(float x, float y); // Adăugat pentru a permite specificarea poziției
}