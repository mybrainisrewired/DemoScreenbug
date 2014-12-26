package android.support.v4.util;

public final class Pools {

    public static interface Pool<T> {
        T acquire();

        boolean release(T t);
    }

    public static class SimplePool<T> implements android.support.v4.util.Pools.Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[maxPoolSize];
        }

        private boolean isInPool(T instance) {
            int i = 0;
            while (i < this.mPoolSize) {
                if (this.mPool[i] == instance) {
                    return true;
                }
                i++;
            }
            return false;
        }

        public T acquire() {
            if (this.mPoolSize <= 0) {
                return null;
            }
            int lastPooledIndex = this.mPoolSize - 1;
            T instance = this.mPool[lastPooledIndex];
            this.mPool[lastPooledIndex] = null;
            this.mPoolSize--;
            return instance;
        }

        public boolean release(T instance) {
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            } else if (this.mPoolSize >= this.mPool.length) {
                return false;
            } else {
                this.mPool[this.mPoolSize] = instance;
                this.mPoolSize++;
                return true;
            }
        }
    }

    public static class SynchronizedPool<T> extends android.support.v4.util.Pools.SimplePool<T> {
        private final Object mLock;

        public SynchronizedPool(int maxPoolSize) {
            super(maxPoolSize);
            this.mLock = new Object();
        }

        public T acquire() {
            T acquire;
            synchronized (this.mLock) {
                acquire = super.acquire();
            }
            return acquire;
        }

        public boolean release(T element) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(element);
            }
            return release;
        }
    }

    private Pools() {
    }
}